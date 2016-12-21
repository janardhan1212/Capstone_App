package com.janardhan.blood2life.Rest;


import com.janardhan.blood2life.pojos.Response_Push;

import java.util.Map;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.QueryMap;

public interface ApiInterface {


    @POST("/jans_exp/blood_app/send_push_bld.php")
    Call<Response_Push> send_post_notif(@QueryMap Map<String, String> options);

}
