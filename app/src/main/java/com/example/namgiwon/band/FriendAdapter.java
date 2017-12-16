package com.example.namgiwon.band;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.namgiwon.band.http.BringPhoto;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 10. 5..
 */

public class FriendAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Frienditem> data;
    private int layout;
    private Bitmap bitimage;
    public FriendAdapter(Context context, int layout, ArrayList<Frienditem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }


    @Override
    public int getCount(){return data.size();}

    @Override
    public String getItem(int position){
        JsonObject jsonobj= new JsonObject();
        jsonobj.addProperty("sno",data.get(position).getSno());
        jsonobj.addProperty("nickname",data.get(position).getNickname());
        jsonobj.addProperty("name",data.get(position).getName());
        jsonobj.addProperty("phonenumber",data.get(position).getPhonenumber());
        jsonobj.addProperty("profile_path",data.get(position).getProfile_path());
        return jsonobj.toString();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        Frienditem listviewitem=data.get(position);
        ImageView profile_picture = (ImageView) convertView.findViewById(R.id.profile_picture);
        profile_picture.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profile_picture.setClipToOutline(true);
        }



        if(listviewitem.getProfile_path()!= null) {
            BringPhoto BP = new BringPhoto(listviewitem.getProfile_path());
            try {
                bitimage = BP.execute().get();
                profile_picture.setImageBitmap(bitimage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }




        TextView name=(TextView)convertView.findViewById(R.id.textview);
        name.setText(listviewitem.getNickname());
        return convertView;
    }
}
