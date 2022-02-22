package com.hackcovid.covidhack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hackcovid.Notification.OreoAndAboveNotification;

import java.util.Random;

public class FirebaseMessaging extends FirebaseMessagingService {
    private static  final String ADMIN_CHANNEL_ID="admin_channel";




    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //get current user from shared preferences
        SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
        String savedCurrentUser=sp.getString("Current_USERID","None");

        String onlineUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        String notificationType=remoteMessage.getData().get("notificationType");
        if(notificationType.equals("PostNotification"))
        {
            String sender=remoteMessage.getData().get("sender");
            String pId=remoteMessage.getData().get("pId");
            String pTitle=remoteMessage.getData().get("pTitle");
            String pDescription=remoteMessage.getData().get("pDescription");


            //if user is same that has posted don't show notifation
            //if(!sender.equals(onlineUserId))
            //{
                showPostNotification(""+pId,
                        ""+pTitle,
                        ""+pDescription);
            //}
        }
        else if(notificationType.equals("ChatNotification"))
        {
              String sent=remoteMessage.getData().get("sent");
              String user=remoteMessage.getData().get("user");
              FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
              //if(fuser!=null & sent.equals(fuser.getUid()))
              //{
                 // if(!savedCurrentUser.equals(user))
                  //{
                      //if(!user.equals(onlineUserId))
                      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                      {
                          sendOAndAboveNotification(remoteMessage);
                      }
                      else
                      {
                          sendNormalNotification(remoteMessage);
                      }
                  //}
              //}

        }

        else if(notificationType.equals("commentNotification"))
        {
            String sent=remoteMessage.getData().get("sent");
            String user=remoteMessage.getData().get("user");
            FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
            //if(fuser!=null & sent.equals(fuser.getUid()))
            //{
            // if(!savedCurrentUser.equals(user))
            //{
            //if(!user.equals(onlineUserId))
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                sendOAndAboveNotification2(remoteMessage);
            }
            else
            {
                sendNormalNotification2(remoteMessage);
            }
            //}
            //}

        }

    }

    private void sendOAndAboveNotification(RemoteMessage remoteMessage) {

        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this,chatActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        OreoAndAboveNotification notification1=new  OreoAndAboveNotification(this);
        Notification.Builder builder= notification1.getONotifications(title,body,pIntent,defSoundUri,icon);
        int j=0;
        if(i>0){
            j=i;
        }
        notification1.getManager().notify(j,builder.build());

    }

    private void sendNormalNotification(RemoteMessage remoteMessage) {
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this,chatActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defSoundUri)
                .setContentIntent(pIntent);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        int j=0;
        if(i>0){
            j=i;
        }
        notificationManager.notify(j,builder.build());




    }

    private void sendOAndAboveNotification2(RemoteMessage remoteMessage) {

        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this,notificationActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        OreoAndAboveNotification notification1=new  OreoAndAboveNotification(this);
        Notification.Builder builder= notification1.getONotifications(title,body,pIntent,defSoundUri,icon);
        int j=0;
        if(i>0){
            j=i;
        }
        notification1.getManager().notify(j,builder.build());




    }

    private void sendNormalNotification2(RemoteMessage remoteMessage)   {
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this,notificationActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defSoundUri)
                .setContentIntent(pIntent);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        int j=0;
        if(i>0){
            j=i;
        }
        notificationManager.notify(j,builder.build());




    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void showPostNotification(String pId, String pTitle, String pDescription) {
        NotificationManager notificationManager=(NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        int notificationId=new Random().nextInt(3000);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            setupPostNotificationChannel(notificationManager);
        }

        Intent intent=new Intent(this,quesAnsActivity.class);
        //intent.putExtra("postId"+pId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        //LargeIcon
        Bitmap largeIcon= BitmapFactory.decodeResource(getResources(),R.drawable.statistics);

        Uri notificationSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this,""+ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentText(pDescription)
                .setContentTitle(pTitle)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notificationId,notificationBuilder.build() );

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupPostNotificationChannel(NotificationManager notificationManager) {

        CharSequence channelName="New Notification";
        String channelDescription="Device to device post notification";
        NotificationChannel adminChannel=new NotificationChannel(ADMIN_CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(channelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if(notificationManager!=null) {
            notificationManager.createNotificationChannel(adminChannel);

        }
    }
}
