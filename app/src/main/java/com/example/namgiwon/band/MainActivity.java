package com.example.namgiwon.band;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.namgiwon.band.http.CheckToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;


public class MainActivity extends FragmentActivity {

    Intent loginIntent;
    Intent HomeIntent;

    String token;
    String token1;
    String answer;
    UserInfo userinfo;
    SharedMemory sharedMemory;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedMemory = SharedMemory.getinstance();
        sharedMemory.setUrl("http://117.17.142.133:8080/Band/BandServer");

        gson = new Gson();




        loginIntent = new Intent(this,LogInActivity.class);
        HomeIntent = new Intent(this,HomeActivity.class);

        //토큰값을 받기위해 브로드캐스트 리시버 설
        LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver,
                new IntentFilter("tokenReceiver"));

        Handler mHandler = new Handler();
        	    mHandler.postDelayed(new Runnable() {
         @Override
   public void run() {
             token1 = FirebaseInstanceId.getInstance().getToken();
             if(token1 != null){ //이미 어플이 설치되어 있는 상태에서 토큰값을 가져왔을경우
                 CheckToken CT = new CheckToken(token1);
                 try {
                     answer = CT.execute().get();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 } catch (ExecutionException e) {
                     e.printStackTrace();
                 }
                 if(answer.equals("not found token")){ // 토큰과 매치되는 아이디가 없을 경우
                     startActivity(loginIntent);
                     finish();
                 }else{ //가지고 있는 토큰으로 아이디를 찾았을 경우
                     Log.d(">>>>>>>>>",answer + "aaaaaaaaa");
                     userinfo = gson.fromJson(answer,UserInfo.class);
                     HomeIntent.putExtra("userinfo",userinfo);
                     startActivity(HomeIntent);
                     finish();}
             }
             sharedMemory.setUserinfo(userinfo);

                	        }
}, 3000);





    }
    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            switch (v.getId()){


            }
        }
    };

    BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            token = intent.getStringExtra("token");
            if(token != null)
            {
                //send token to your server or what you want to do
                Log.d("토큰",token+"<<<<<<<<<<<<<<<<<<<<<");
                startActivity(loginIntent);
                finish();

            }
        }
    };

}