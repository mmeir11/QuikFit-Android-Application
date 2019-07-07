package com.evyatartzik.android2_project.Interfaces;

import com.evyatartzik.android2_project.Notifictions.MyResponse;
import com.evyatartzik.android2_project.Notifictions.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA5WZ4Jj4:APA91bGA8UjHxmKqKQNNnArxNTgIA87WLiSHG_W3UKPcHHmyI_6KgJNxcEGOlSqPAzTSgPnU5BcMO8AlEYMyHDrNX2jgrmMhYjGH8smr9qS0VxZv1dGIOoFBT8Ne2tZGRsRoZv4_GfJf"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
