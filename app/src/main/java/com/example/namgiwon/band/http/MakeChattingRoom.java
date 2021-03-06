package com.example.namgiwon.band.http;

import android.os.AsyncTask;
import android.util.Log;

import com.example.namgiwon.band.SharedMemory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by namgiwon on 2017. 11. 10..
 */

public class MakeChattingRoom extends AsyncTask<Void, Void, String>{
    Gson gson = null;
    String userID;
    String friendlist;
    String answer;
    String roomid;
    String tokens;
    String roomname;
    JsonArray jsonarr;
    SharedMemory sharedMemory;

    public MakeChattingRoom(String userID,String friendlist,String roomname){
    gson = new Gson();
    this.userID = userID;
    this.friendlist = friendlist;
    this.roomname = roomname;
    sharedMemory = SharedMemory.getinstance();
}
    @Override
    protected String doInBackground(Void... params) {


        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody =null;
        requestBody = new FormBody.Builder().add("Type","chatting").add("Option","makeroom")
                .add("roomname",roomname)
                .add("friendlist",friendlist).add("userID",userID).build();
        Request request = new Request.Builder()
                .url(sharedMemory.getUrl())
                .post(requestBody)
                .build();

        try {
            response = client.newCall(request).execute();
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }
}
