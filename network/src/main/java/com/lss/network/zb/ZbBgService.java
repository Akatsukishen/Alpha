package com.lss.network.zb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lss.network.R;
import com.lss.network.zb.data.TickerBean;
import com.lss.network.zb.data.TickerBean.Ticker;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by li.shensong on 08/02/2018.
 */

public class ZbBgService extends Service {
    private static final String TAG = "ZbBgService";

    private String mMarket = "";

    private float mLast = -1.0f;
    private boolean mIsIncreasing = true;
    private int mCoutinuous = 0;

    private final int NOTICATION_ID = 100;

    private CompositeDisposable mCompositeDisposable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMarket = intent.getStringExtra("market");
        Log.d(TAG, "==> onStartCommand: market = " + mMarket + " thread = " + Thread.currentThread().getName());
        mCompositeDisposable.clear();
        looperCheck(mMarket);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    public void looperCheck(final String market) {
        mCompositeDisposable.add(
                Observable.interval(5, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .concatMap(new Function<Long, ObservableSource<TickerBean>>() {
                        @Override
                        public ObservableSource<TickerBean> apply(Long aLong) throws Exception {
                            Log.d(TAG, "==> concatMap: long =" + aLong);
                            return ZbApi.instance().getMarketObservable(market);
                        }
                    })
                    .subscribe(new Consumer<TickerBean>() {
                        @Override
                        public void accept(TickerBean tickerBean) throws Exception {
                            Ticker ticker = tickerBean.getTicker();
                            float last = ticker.getLast();
                            Log.d(TAG, "==> accept:" + market + " last = " + last + " date = " + tickerBean.getDate()
                                    + " thread = " + Thread.currentThread().getName());
                            if (mLast < 0) {
                                mLast = last;
                            } else if (mLast != last) {
                                if (mLast > last) {
                                    if (mIsIncreasing) {
                                        mCoutinuous++;
                                        mLast = last;
                                        if (mCoutinuous >= 5) {
                                            Log.d(TAG, "==> accept:" + market + "  连续涨" + mCoutinuous + "次了。");
                                            notification(  "连续涨" + mCoutinuous + "次了。当前价格:" + mLast);
                                            mCoutinuous = 0;
                                            mIsIncreasing = false;
                                        }
                                    } else {
                                        mLast = last;
                                        mCoutinuous = 1;
                                        mIsIncreasing = true;
                                    }
                                } else if (mLast < last) {
                                    if (!mIsIncreasing) {
                                        mCoutinuous++;
                                        mLast = last;
                                        if (mCoutinuous >= 5) {
                                            Log.d(TAG, "==> accept:" + market + "  连续跌" + mCoutinuous + "次了。");
                                            notification(  "连续跌" + mCoutinuous + "次了。当前价格:" + mLast);
                                            mCoutinuous = 0;
                                            mIsIncreasing = false;
                                        }
                                    } else {
                                        mLast = last;
                                        mCoutinuous = 1;
                                        mIsIncreasing = false;
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d(TAG, "accept() called with: throwable = [" + throwable + "]");
                        }
                    })
        );
    }

    private void notification(String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,mMarket);
        Intent resultIntent = new Intent("lss.action.retrofit");
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(mMarket)
                .setContentText(msg)
                .setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(NOTICATION_ID, builder.build());
    }

}
