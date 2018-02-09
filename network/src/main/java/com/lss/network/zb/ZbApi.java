package com.lss.network.zb;

import com.lss.network.zb.data.TickerBean;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by li.shensong on 08/02/2018.
 */

public class ZbApi {
    private static final String TAG = "ZbApi";

    public static final String BASE_URL = "http://api.zb.com/data/v1/";

    private static ZbApi INSTANCE;

    private ZbApi(){
    }

    public synchronized static ZbApi instance(){
        if(INSTANCE == null){
            INSTANCE = new ZbApi();
        }
        return INSTANCE;
    }


    public Observable<TickerBean> getMarketObservable(final String market){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ZbService zbService = retrofit.create(ZbService.class);
       return zbService.getTickerBean(market);
    }
}
