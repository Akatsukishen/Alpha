package com.lss.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lss.network.zb.ZbBgService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by li.shensong on 08/02/2018.
 */

public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitActivity";

    @BindView(R.id.rg_base)
    RadioGroup mRadioGroup;
    @BindView(R.id.ed_coin)
    TextView mEditCoin;
    @BindView(R.id.tv_result)
    TextView mTvResult;

    String base = "qc";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_btc:
                        base = "btc";
                        break;
                    case R.id.rb_qc:
                        base = "qc";
                        break;
                    case R.id.rb_usdt:
                        base = "usdt";
                        break;
                }
            }
        });
    }

    public void getMarket(View view){
        String coin = mEditCoin.getText().toString();
        if(TextUtils.isEmpty(coin)){
            Toast.makeText(this, "虚拟货币为空.", Toast.LENGTH_SHORT).show();
        }
        String market = coin + "_" + base;
        Log.d(TAG, "==> getMarket: market = " + market);
        Intent intent = new Intent(this, ZbBgService.class);
        stopService(intent);
        intent.putExtra("market",market.toLowerCase());
        startService(intent);
    }

    public void stopCheck(View view){
        Intent intent = new Intent(this, ZbBgService.class);
        stopService(intent);
    }

}
