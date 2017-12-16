package com.example.namgiwon.band.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.example.namgiwon.band.SharedMemory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by namgiwon on 2017. 12. 5..
 */

public class BringPhoto extends AsyncTask<Void,Void,Bitmap>{
    Bitmap bmImg;
    String path;
    Bitmap resized;
    ImageView imageView = null;
 public BringPhoto(String path){
     this.path = path;
 }





    @Override
    protected Bitmap doInBackground(Void... voids) {
        try{
            URL imageUrl = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            InputStream is = conn.getInputStream();
            bmImg = BitmapFactory.decodeStream(is,null,options);
            resized = Bitmap.createScaledBitmap( bmImg, 1024,1024, true );



//            bmImg = BitmapFactory.decodeStream(is);
            Log.d("aaaaa",bmImg.toString());

        }catch(IOException e){
            e.printStackTrace();
        }
        return resized;
    }

    protected void onPostExecute(Bitmap img){

    }



}

