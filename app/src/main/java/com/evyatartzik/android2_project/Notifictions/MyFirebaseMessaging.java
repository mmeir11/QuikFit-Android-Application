package com.evyatartzik.android2_project.Notifictions;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

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
//            sendNotificationData(remoteMessage);

            Intent intent2 = new Intent(context, ChatActivity.class);    //MessageActivity.class
            Bundle bundle = new Bundle();
            String topics = remoteMessage.getFrom();
            String[] output = topics.split("/");
            String title = output[2] ;
            bundle.putString("groupName", title);
            intent2.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent2, PendingIntent.FLAG_ONE_SHOT);


            Intent intent = new Intent("message_recived");
            intent.putExtra("message", remoteMessage.getData().get("message"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this);
            if(Build.VERSION.SDK_INT >=26) {
                NotificationChannel channel = new NotificationChannel("id_1", "name_1", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId("id_1")/*.setContentIntent(pendingIntent)*/;
            }

            builder.setContentTitle(title).setContentText(remoteMessage.getData().get("message"))
                    .setSmallIcon(R.drawable.quickfix_logo).setContentIntent(pendingIntent);

            manager.notify(1, builder.build());


        }

        if(remoteMessage.getNotification() != null){

            Intent intent2 = new Intent(context, ChatActivity.class);    //MessageActivity.class
            Bundle bundle = new Bundle();
            String topics = remoteMessage.getFrom();
            String[] output = topics.split("/");
            String title = output[2] ;
            bundle.putString("groupName", title);
            intent2.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent2, PendingIntent.FLAG_ONE_SHOT);


            Intent intent = new Intent("message_recived");
            intent.putExtra("message", remoteMessage.getData().get("message"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this);
            if(Build.VERSION.SDK_INT >=26) {
                NotificationChannel channel = new NotificationChannel("id_1", "name_1", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId("id_1");
            }

            builder.setContentTitle(title).setContentText(remoteMessage.getData().get("message"))
                    .setSmallIcon(R.drawable.quickfix_logo);

            manager.notify(1, builder.build());

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
//        String body = remoteMessage.getData().get("body");
        String body = remoteMessage.getData().get("message");

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
