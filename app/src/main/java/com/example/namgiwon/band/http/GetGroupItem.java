package com.example.namgiwon.band.http;

import android.os.AsyncTask;
import android.util.Log;

import com.example.namgiwon.band.GroupItem;
import com.example.namgiwon.band.SharedMemory;
import com.example.namgiwon.band.UserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by namgiwon on 2017. 11. 23..
 */

public class GetGroupItem extends AsyncTask<Void,Void,GroupItem>{
    String answer = "";
    Gson gson;
    JsonArray jsonarr;
    String roomid;
    JsonObject jsonobj;
    GroupItem groupItem;
    UserInfo userinfo;
    SharedMemory sharedMemory;

    public GetGroupItem(String roomid, UserInfo userinfo){
        gson = new Gson();
        this.roomid = roomid;
        this.userinfo = userinfo;
        sharedMemory = SharedMemory.getinstance();

    }
    @Override
    protected GroupItem doInBackground(Void... params) {


        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody =null;
        requestBody = new FormBody.Builder().add("Type","chatting").add("Option","getChattingRoomItem")
                .add("roomid",roomid)
                .add("userID",userinfo.getId())
                .build();

        Request request = new Request.Builder()
                .url(sharedMemory.getUrl())
                .post(requestBody)
                .build();
        try {
            response = client.newCall(request).execute();
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();
            Log.d("asdfasdfasdfasdf",answer);
            JsonArray jsonarr = gson.fromJson(answer,JsonArray.class);
            groupItem = gson.fromJson(jsonarr.get(0).getAsJsonObject(),GroupItem.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return groupItem;
    }

    @Override
    protected void onPostExecute(GroupItem s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }
}
