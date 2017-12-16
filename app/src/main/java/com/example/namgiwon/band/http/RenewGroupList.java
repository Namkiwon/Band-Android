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
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by namgiwon on 2017. 11. 23..
 */

public class RenewGroupList extends AsyncTask<Void,Void,ArrayList<GroupItem>>{
    String answer = "";
    Gson gson;
    JsonArray jsonarr;
    String userID;
    JsonObject jsonobj;
    GroupItem groupitem;
    UserInfo userInfo;
    ArrayList<GroupItem> data;
    SharedMemory sharedMemory;
    public RenewGroupList(UserInfo userInfo){

        gson = new Gson();
        this.userInfo = userInfo;
        data = new ArrayList<GroupItem>();
        sharedMemory = SharedMemory.getinstance();

    }
    @Override
    protected ArrayList<GroupItem> doInBackground(Void... params) {


        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody =null;
        requestBody = new FormBody.Builder().add("Type","group").add("Option","renewGroupList")
                .add("userID",userInfo.getId()).build();

        Request request = new Request.Builder()
                .url(sharedMemory.getUrl())
                .post(requestBody)
                .build();
        try {
            response = client.newCall(request).execute();
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();
            Log.d("asdfasdfasdfasdf",answer);
            if(answer != null) jsonarr = gson.fromJson(answer,JsonArray.class);

            if(jsonarr.size() != 0) {
                for (int i = 0; i < jsonarr.size(); i++) {
                    jsonobj = new JsonObject();
                    jsonobj = jsonarr.get(i).getAsJsonObject();
                    GroupItem groupitem = gson.fromJson(jsonobj.toString(), GroupItem.class);
                    if(groupitem.getGroup_member().contains(userInfo.getSno()))data.add(0,groupitem);
                    else {data.add(groupitem);}
                }
            }
            Log.d("GroupList",data+"<<<<<<<");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<GroupItem> s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }
}
