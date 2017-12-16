package com.example.namgiwon.band.http;

import android.os.AsyncTask;
import android.util.Log;

import com.example.namgiwon.band.ChatItem;
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
 * Created by namgiwon on 2017. 11. 18..
 */

public class RenewChattingList extends AsyncTask<Void,Void,ArrayList<ChatItem>>{
    String roomid;
    Gson gson;
    JsonArray jsonarr;
    JsonObject jsonobj;
    ChatItem chatitem;
    ArrayList<ChatItem> chatlist;
    String answer;
    SharedMemory sharedMemory;

    public RenewChattingList(String roomid){

        gson = new Gson();
        this.roomid = roomid;
        chatlist = new ArrayList<ChatItem>();
        sharedMemory = SharedMemory.getinstance();
    }
    @Override
    protected ArrayList<ChatItem> doInBackground(Void... params) {


        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody =null;
        requestBody = new FormBody.Builder().add("Type","chatting").add("Option","renewChattingList")
                .add("roomid",roomid).build();

        Request request = new Request.Builder()
                .url("http://117.17.142.133:8080/Band/BandServer")
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
                    ChatItem chat = gson.fromJson(jsonobj.toString(), ChatItem.class);
                    chatlist.add(chat);
                }
            }
            Log.d("ChattingRoomList",chatlist+"<<<<<<<");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return chatlist;
    }

    @Override
    protected void onPostExecute(ArrayList<ChatItem> s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }
}
