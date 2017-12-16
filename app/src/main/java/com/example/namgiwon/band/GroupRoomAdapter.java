package com.example.namgiwon.band;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.namgiwon.band.http.BringPhoto;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 11. 18..
 */

public class GroupRoomAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<ContentsItem> data;
    private int layout;
    private UserInfo userinfo;
    private Bitmap bitimage;



    public GroupRoomAdapter(Context context, int layout, ArrayList<ContentsItem> data, UserInfo userinfo){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
        this.userinfo = userinfo;
    }

    @Override
    public int getCount(){return data.size();}

    @Override
    public String getItem(int position){
        /*******/
        return null;
    }
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }



        ContentsItem contentsItem = data.get(position);

        ImageView contents_photo = convertView.findViewById(R.id.contents_photo);
        Log.d("contents image",contentsItem.getPicture_path() + ">>>>>>>");
        if(contentsItem.getPicture_path() != null) {
            BringPhoto BP = new BringPhoto(contentsItem.getPicture_path());
            try {

                bitimage = BP.execute().get();
                contents_photo.setImageBitmap(bitimage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }



        LinearLayout chatitemlayout = (LinearLayout) convertView.findViewById(R.id.contentsItem_LinearLayout);

        TextView contentsWriter = convertView.findViewById(R.id.contents_writer);
        TextView contentsTime = convertView.findViewById(R.id.contents_time);
        TextView contentsLetter = convertView.findViewById(R.id.contents_letter);

        contentsWriter.setText(contentsItem.getSender_name());
        contentsTime.setText(contentsItem.getTime());
        contentsLetter.setText(contentsItem.getMessage());
        chatitemlayout.setBackgroundResource(R.drawable.contentscontainer);

        return convertView;
    }
}
