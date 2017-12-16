package com.example.namgiwon.band;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by namgiwon on 2017. 11. 23..
 */

public class GroupListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<GroupItem> data;
    private int layout;
    private UserInfo userInfo;


    public GroupListAdapter(Context context, int layout, ArrayList<GroupItem> data,UserInfo userInfo){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
        this.userInfo = userInfo;
    }


    @Override
    public int getCount(){return data.size();}


    @Override
    public GroupItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position){return data.get(position).getGroup_id();}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        GroupItem groupItem = data.get(position);
        TextView name=(TextView)convertView.findViewById(R.id.group_name);
        TextView chief =(TextView)convertView.findViewById(R.id.chief);
        name.setText(groupItem.getGroup_name());
        LinearLayout itemparentlayout = (LinearLayout) convertView.findViewById(R.id.groupitemparentLayout);
        chief.setText("그룹장 : "+groupItem.getChief());
        if(groupItem.getGroup_member().contains(userInfo.getSno())){itemparentlayout.setBackgroundColor(Color.parseColor("#FFFFFF"));} // 내가 속한 그룹일때
        else {itemparentlayout.setBackgroundColor(Color.parseColor("#CCCCCC"));} //내가 속하지 않은 그룹


        return convertView;
    }
}
