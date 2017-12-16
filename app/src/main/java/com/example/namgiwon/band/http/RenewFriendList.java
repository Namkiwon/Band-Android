package com.example.namgiwon.band.http;

import android.os.AsyncTask;

import com.example.namgiwon.band.Frienditem;
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
 * Created by namgiwon on 2017. 10. 6..
 */

public class RenewFriendList extends AsyncTask<Void,Void,ArrayList<Frienditem>>{
    String answer;
    Gson gson;
    JsonArray jsonarr;
    String userID;
    JsonObject jsonobj;
    ArrayList<Frienditem> data;
    SharedMemory sharedMemory;
    public RenewFriendList(String userID){

        gson = new Gson();
        this.userID = userID;
        data = new ArrayList<Frienditem>();
        sharedMemory = SharedMemory.getinstance();

    }
    @Override
    protected ArrayList<Frienditem> doInBackground(Void... params) {


        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody =null;
        requestBody = new FormBody.Builder().add("Type","friendList").add("Option","renewFriendList").add("userID",userID).build();
        Request request = new Request.Builder()
                .url(sharedMemory.getUrl())
                .post(requestBody)
                .build();
        try {
            response = client.newCall(request).execute();
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();

            jsonarr = gson.fromJson(answer,JsonArray.class);

            for(int i=0; i < jsonarr.size(); i++){
                jsonobj = new JsonObject();
                jsonobj = jsonarr.get(i).getAsJsonObject();

                Frienditem friend = gson.fromJson(jsonobj.toString(),Frienditem.class);
                friend.setNickname(friend.getSno()+" "+ friend.getName());
//                Log.d("friend",friend.getID().toString()+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                if(!friend.getID().toString().equals(userID))
                    data.add(friend);
            }

//            Log.d("friendList",data+"<<<<<<<");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<Frienditem> s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }
}
