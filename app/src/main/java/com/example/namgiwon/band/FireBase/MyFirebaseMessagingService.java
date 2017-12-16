package com.example.namgiwon.band.FireBase;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Switch;

import com.example.namgiwon.band.ChattingActivity;
import com.example.namgiwon.band.ChattingRoomItem;
import com.example.namgiwon.band.GroupItem;
import com.example.namgiwon.band.GroupRoomActivity;
import com.example.namgiwon.band.R;
import com.example.namgiwon.band.SharedMemory;
import com.example.namgiwon.band.UserInfo;
import com.example.namgiwon.band.http.CheckToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 11. 15..
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    private String userID;
    private SharedMemory sharedMemory;
    private UserInfo userInfo;
    private Gson gson;
    private CheckToken CT;
    private ChattingRoomItem cri;

    // [START receive_message]
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //추가한것
        gson = new Gson();
        String a = FirebaseInstanceId.getInstance().getToken();
        CT = new CheckToken(a);

        sharedMemory = SharedMemory.getinstance();
        if(sharedMemory.getUserinfo() != null){
            userID = sharedMemory.getUserinfo().getId();
            Log.d("ttttttttt",userID);
        }
        else {
            try {
                String answer = CT.execute().get();
                userInfo = gson.fromJson(answer,UserInfo.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData().get("body"));
            Log.d("push1",remoteMessage.getData().get("body"));

        }
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());
            Log.d("push2",remoteMessage.getNotification().getBody());
        }


    }

    private void sendNotification(String messageBody) {
        Gson gson = new Gson();
        JsonObject jsonobj;
        jsonobj = gson.fromJson(messageBody,JsonObject.class);

        switch(jsonobj.get("type").getAsString()){
            case "chat" :
                Log.d("ffff",jsonobj.get("chattingroomInfo").getAsString());
                cri = gson.fromJson(jsonobj.get("chattingroomInfo").getAsString(),ChattingRoomItem.class);
                Log.d("fff1",String.valueOf(cri.getRoomid()));
                //받은 메세지를 ChattingActivity에 보냄
                final Intent broadintent = new Intent("messageReceiver");
                final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
                broadintent.putExtra("message",jsonobj.toString());
                broadcastManager.sendBroadcast(broadintent);


                // 알림 눌렀을때 화면
                Intent intent = new Intent(this, ChattingActivity.class);
                intent.putExtra("roominfo",cri);
                intent.putExtra("userID",jsonobj.get("targetID").getAsString());

                if(sharedMemory.getUserinfo() != null) intent.putExtra("userinfo", sharedMemory.getUserinfo());
                else intent.putExtra("userinfo", userInfo);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon( BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                        .setContentTitle(jsonobj.get("sender_name").getAsString()) // 이부분은 어플 켜놓은 상태에서 알림 메세지 받으면 저 텍스트로 띄워준다.
                        .setContentText(jsonobj.get("message").getAsString())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setNumber(100);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                break;

            case "contents" :
                GroupItem groupItem =  gson.fromJson(jsonobj.get("groupinfo").getAsString(),GroupItem.class);

                //받은 메세지를 ChattingActivity에 보냄
                final Intent broadintent2 = new Intent("contentsReceiver");
                final LocalBroadcastManager broadcastManager2 = LocalBroadcastManager.getInstance(this);
                broadintent2.putExtra("message",jsonobj.toString());
                broadcastManager2.sendBroadcast(broadintent2);


                // 알림 눌렀을때 화면
                Intent intent2 = new Intent(this, GroupRoomActivity.class);
                intent2.putExtra("groupinfo",groupItem);

                if(sharedMemory.getUserinfo() != null) intent2.putExtra("userinfo", sharedMemory.getUserinfo());
                else intent2.putExtra("userinfo", userInfo);

                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //
                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0 /* Request code */, intent2, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri2= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notificationBuilder2 = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon( BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                        .setContentTitle(groupItem.getGroup_name()) // 이부분은 어플 켜놓은 상태에서 알림 메세지 받으면 저 텍스트로 띄워준다.
                        .setContentText(jsonobj.get("sender_name").getAsString())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri2)
                        .setContentIntent(pendingIntent2);

                NotificationManager notificationManager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager2.notify(0 /* ID of notification */, notificationBuilder2.build());
                break;

        }

    }

//    @Override
//    public void handleIntent(Intent intent) {
//        super.handleIntent(intent);
//        Log.d("banner","Banner");
//    }
}
