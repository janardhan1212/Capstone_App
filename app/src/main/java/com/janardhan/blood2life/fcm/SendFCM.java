package com.janardhan.blood2life.fcm;

import com.janardhan.blood2life.pojos.Response_Push;

import java.util.Map;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by janardhanyerranagu on 01/12/16.
 */

public interface SendFCM {
    @POST("/api/index.php/api/new_set_coun_report_ratings")
    Call<Response_Push> set_coun_wekly_ratings(@QueryMap Map<String, Object> options);
}
