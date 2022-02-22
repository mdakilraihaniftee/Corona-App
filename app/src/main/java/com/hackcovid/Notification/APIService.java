package com.hackcovid.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAfwu0JBc:APA91bEJ16K-Q9JJtnpZ7HyXuGFc3ESP1s1mVBgbD9Q1x1ON3DZfwVZmQPH4N_t7VHeAZVi8atcMPfPj9Dis2cRTOFm2LkWXNQBNfLpvvsw-bZWFoMmw9xD31bb1xZcJs64D67F_j-z7"


    })

    @POST("fcm/send")
    Call<Response>sendNotifcation(@Body Sender body);
}
