package com.example.namgiwon.band.http;
import android.os.AsyncTask;

import com.example.namgiwon.band.SharedMemory;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Namkiwon on 2017-09-28.
 */

public class LogIn extends AsyncTask<Void,Void,String> {

    private String ID;
    private String PW;
    private String answer;
    private String token;
    private SharedMemory sharedMemory;


    public LogIn(String ID, String PW,String token) {
        super();
        this.ID = ID;
        this.PW = PW;
        this.token = token;
        sharedMemory = SharedMemory.getinstance();
    }

    @Override
    protected String doInBackground(Void... params) {

        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody =null;



        requestBody = new FormBody.Builder().add("Type","logIn").add("ID",ID).add("PW",PW).add("token",token).build();
        Request request = new Request.Builder()
                .url(sharedMemory.getUrl())
                .post(requestBody)
                .build();


        try {
            response = client.newCall(request).execute();
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();
            System.out.println(answer);
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


