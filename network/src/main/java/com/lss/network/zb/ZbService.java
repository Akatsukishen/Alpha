package com.lss.network.zb;

import com.lss.network.zb.data.TickerBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by li.shensong on 08/02/2018.
 */

public interface ZbService {

    @GET("ticker")
    Observable<TickerBean> getTickerBean(@Query("market") String market);

}
