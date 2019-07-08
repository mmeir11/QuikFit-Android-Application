package com.evyatartzik.android2_project.Notifictions;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.evyatartzik.android2_project.Adapters.GlobalApplication;
import com.evyatartzik.android2_project.Fragments.HomeFragment;
import com.evyatartzik.android2_project.Fragments.MyChatFragment;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.ChatActivity;
import com.evyatartzik.android2_project.UI.MenuActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    Context context = GlobalApplication.getAppContext();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size() > 0){
            sendNotificationData(remoteMessage);

        }

        if(remoteMessage.getNotification() != null){
            sendNotificationNotification(remoteMessage);
        }


 /*       String sended = remoteMessage.getData().get("sended");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null && sended.equals(firebaseUser.getUid())){
            sendNotificationData(remoteMessage);
        }*/

    }

    private void sendNotificationData(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(context, MenuActivity.class);    //MessageActivity.class
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defualtSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defualtSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if(j>0){
            i=j;
        }

        noti.notify(i, builder.build());


    }


    private void sendNotificationNotification(RemoteMessage remoteMessage){
        String user = "User123";
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(context, ChatActivity.class);    //MessageActivity.class
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        bundle.putString("groupName", title);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defualtSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defualtSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if(j>0){
            i=j;
        }

        noti.notify(i, builder.build());


    }






















}
