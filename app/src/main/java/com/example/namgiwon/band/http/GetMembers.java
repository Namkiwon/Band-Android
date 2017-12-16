package com.example.namgiwon.band.http;

import android.os.AsyncTask;

import com.example.namgiwon.band.SharedMemory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by namgiwon on 2017. 12. 6..
 */

public class GetMembers extends AsyncTask<Void,Void,String> {
    Gson gson = null;
    String answer;
    String userID;
    String groupID;


    public GetMembers(String userID, String groupID){
        gson = new Gson();
        this.userID = userID;
        this.groupID = groupID;
    }
    @Override
    protected String doInBackground(Void... params) {


        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody =null;
        requestBody = new FormBody.Builder().add("Type","group").add("Option","getMembers")
                .add("userID",userID).add("groupID",groupID)
                .build();
        Request request = new Request.Builder()
                .url("http://117.17.142.133:8080/Band/BandServer")
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
