package com.example.namgiwon.band;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by namgiwon on 2017. 11. 7..
 */

public class ChattingRoomListAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<ChattingRoomItem> data;
    private int layout;
    public ImageView noti;

    public ChattingRoomListAdapter(Context context, int layout, ArrayList<ChattingRoomItem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;

    }


    @Override
    public int getCount(){return data.size();}


    @Override
    public ChattingRoomItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position){return data.get(position).getRoomid();}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }

        ChattingRoomItem chattingRoomItem = data.get(position);
        TextView name=(TextView)convertView.findViewById(R.id.chattingroom_name);
        name.setText(chattingRoomItem.getRoomname());

        noti = (ImageView) convertView.findViewById(R.id.newnoti);



        if(chattingRoomItem.getNew() == false)noti.setImageResource(R.drawable.nored);
        if(chattingRoomItem.getNew() == true)noti.setImageResource(R.drawable.red);

        if(chattingRoomItem.getLastchat() != null){
        TextView preview=(TextView)convertView.findViewById(R.id.chattingroom_previewmsg);
        preview.setText(chattingRoomItem.getLastchat());
        }

        return convertView;
    }
}


