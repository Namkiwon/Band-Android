package com.example.namgiwon.band;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by namgiwon on 2017. 11. 18..
 */

public class ChattingAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<ChatItem> data;
    private int layout;
    private UserInfo userinfo;
    private String sender_profilepath;
    private Bitmap bitimage;


    public ChattingAdapter(Context context, int layout, ArrayList<ChatItem> data, UserInfo userinfo){
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




        ChatItem chatitem = data.get(position);

        TextView message=(TextView)convertView.findViewById(R.id.msg);
        message.setText(chatitem.getMessage());

        LinearLayout chatitemlayout = (LinearLayout) convertView.findViewById(R.id.chatitemLinearLayout);

        TextView Sender=(TextView)convertView.findViewById(R.id.msg_sender);
        TextView msgtime1 = (TextView)convertView.findViewById(R.id.msg_time1);
        TextView msgtime2 = (TextView)convertView.findViewById(R.id.msg_time2);


        if(chatitem.getSender_id().equals(userinfo.getId())){
            message.setBackgroundResource(R.drawable.bbb);
            chatitemlayout.setGravity(Gravity.RIGHT);
            Sender.setText("");
            msgtime1.setText(chatitem.getTime()+" ");
            msgtime2.setText("");

        }else{


            message.setBackgroundResource(R.drawable.aaa);
            chatitemlayout.setGravity(Gravity.LEFT);
            Sender.setText(chatitem.getSender_name());
            msgtime2.setText(" "+chatitem.getTime());
            msgtime1.setText("");
        }



        return convertView;
    }
}
