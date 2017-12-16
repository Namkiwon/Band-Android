package com.example.namgiwon.band;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by namgiwon on 2017. 11. 10..
 */

public class FindChattingMemberAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<Frienditem> data;
    private int layout;
    public FindChattingMemberAdapter(Context context, int layout, ArrayList<Frienditem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }


    @Override
    public int getCount(){return data.size();}

    @Override
    public Frienditem getItem(int position){
        return data.get(position);}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        Frienditem listviewitem=data.get(position);

        TextView name=(TextView)convertView.findViewById(R.id.textview);
        name.setText(listviewitem.getNickname());
        return convertView;
    }
}
