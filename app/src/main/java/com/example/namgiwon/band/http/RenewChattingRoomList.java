package com.example.namgiwon.band.http;

import android.os.AsyncTask;
import android.util.Log;

import com.example.namgiwon.band.ChattingRoomItem;
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

public class RenewChattingRoomList extends AsyncTask<Void,Void,ArrayList<ChattingRoomItem>> {
    String answer = "";
    Gson gson;
    JsonArray jsonarr;
    String userID;
    JsonObject jsonobj;
    ChattingRoomItem room;
    ArrayList<ChattingRoomItem> data;
    SharedMemory sharedMemory;
    public RenewChattingRoomList(String userID){

        gson = new Gson();
        this.userID = userID;
        data = new ArrayList<ChattingRoomItem>();
        sharedMemory = SharedMemory.getinstance();

    }
    @Override
    protected ArrayList<ChattingRoomItem> doInBackground(Void... params) {


        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody =null;
        requestBody = new FormBody.Builder().add("Type","chatting").add("Option","renewChattingRoomList")
                .add("userID",userID).build();

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
                    Log.d("asdfasdf", jsonobj + "<<<<<<<<<<<<<<<<66");
                    ChattingRoomItem room = gson.fromJson(jsonobj.toString(), ChattingRoomItem.class);
                    Log.d("ttttt", room.getRoomid() + "<<<<");
                    data.add(room);
                }
            }
            Log.d("ChattingRoomList",data+"<<<<<<<");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<ChattingRoomItem> s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }
}
