package com.evyatartzik.android2_project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.evyatartzik.android2_project.UI.MenuActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class OpenActivityReceiver extends BroadcastReceiver {

    private Context Mycontext;
    final int NOTIF_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Mycontext = context;
        String channelId = null;
        String activity_id = intent.getStringExtra("activity_id");
        String date = intent.getStringExtra("date");

        final NotificationManager manager = (NotificationManager)Mycontext.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            channelId = "some_id";
            CharSequence channel_name = "Some channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId,channel_name,importance);
            manager.createNotificationChannel(notificationChannel);

        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(Mycontext,channelId);
        builder.setSmallIcon(android.R.drawable.star_on).setContentTitle("test").setContentText("sometest");
        Intent intent1 = new Intent(Mycontext, MenuActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Mycontext,0,intent1,0);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

//        RemoteViews remoteViews = new RemoteViews(Mycontext.getPackageName(),R.layout.custom_notification_news);
//        Intent testIntent = new Intent(Mycontext, NewsDetailActivity.class);
//        testIntent.putExtra("url",firstArt.getUrl());
//        testIntent.putExtra("img",firstArt.getUrlToImage());
//        testIntent.putExtra("title",firstArt.getTitle());
//        testIntent.putExtra("date", firstArt.getPublishedAt());
//        testIntent.putExtra("source",firstArt.getSource().getName());
//        testIntent.putExtra("author",firstArt.getAuthor());
//
//        PendingIntent pendingIntent = (PendingIntent) PendingIntent.getActivity(Mycontext,0,testIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setCustomBigContentView(remoteViews);
//
//
//        url_to_removeview_image(firstArt.getUrlToImage(),remoteViews);
//        remoteViews.setTextViewText(R.id.author,firstArt.getAuthor());
//        remoteViews.setTextViewText(R.id.publishedAt, Utils.DateFormat(firstArt.getPublishedAt()));
//        remoteViews.setTextViewText(R.id.title,firstArt.getTitle());
//        remoteViews.setTextViewText(R.id.source,firstArt.getSource().getName());
//        remoteViews.setTextViewText(R.id.desc,firstArt.getDescription());
//        remoteViews.setTextViewText(R.id.time," \u2022 " + Utils.DateToTimeFormat(firstArt.getPublishedAt()));
//        builder.setContent(remoteViews);
//        remoteViews.setOnClickPendingIntent(R.id.layout,pendingIntent);


        manager.notify(NOTIF_ID, builder.build());




    }
}
