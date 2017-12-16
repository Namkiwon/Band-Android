package com.example.namgiwon.band;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.namgiwon.band.http.RenewFriendList;
import com.example.namgiwon.band.http.RenewGroupList;
import com.google.gson.Gson;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 import android.app.Activity;
 * Created by namgiwon on 2017. 11. 23..
 */

public class GroupListFragment extends Fragment{
    ListView listView;
    String userID = "";
    ArrayList<GroupItem> datalist;
    GroupListAdapter adapter;
    Intent findGroupMemberIntent;
    Button Plus;
    RenewGroupList renewGroupList;
    String roomid;
    String receivemessage;
    Gson gson;
    UserInfo userinfo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 1f; // 투명도 0 ~ 1
        getActivity().getWindow().setAttributes(layoutParams);
        getActivity().getWindow().setAttributes(layoutParams);
        datalist = new ArrayList<GroupItem>();
        findGroupMemberIntent = new Intent(getActivity(),MakeGroupActivity.class);
        Bundle bundle = getArguments();
        userinfo = (UserInfo) bundle.getSerializable("userinfo");
        renewGroupList = new RenewGroupList(userinfo);
        gson = new Gson();
        //토큰값을 받기위해 브로드캐스트 리시버 설
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver,
                new IntentFilter("messageReceiver"));


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //엑티비티에서 프래그먼트로 데이터를 전달할 때에는 bundle 을 사용한다.

        //inflater 를 사용하여 view 를 생성
        View view = inflater.inflate(R.layout.fragment_grouplist,null);

        listView = (ListView) view.findViewById(R.id.grouplistview);
        Plus = (Button) view.findViewById(R.id.addGroup);
        Plus.bringToFront();
        Plus.setOnClickListener(bListener);
        //친구목록을 최신화 한다.
        //****

        try {
            datalist = renewGroupList.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        adapter = new GroupListAdapter(getActivity(),R.layout.groupitem,datalist,userinfo);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(iListener);

        return view;
    }


    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.addGroup :
                    findGroupMemberIntent.putExtra("userinfo",userinfo);
                    getActivity().startActivityForResult(findGroupMemberIntent,3);
                    break;
            }
        }
    };




    AdapterView.OnItemClickListener iListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ListView listView = (ListView) adapterView;
                GroupItem selectitem = (GroupItem) listView.getAdapter().getItem(i);
            if(selectitem.getGroup_member().contains(userinfo.getSno())) {
                Intent GroupRoomIntent = new Intent(getActivity(), GroupRoomActivity.class);
                GroupRoomIntent.putExtra("groupinfo", selectitem);
                GroupRoomIntent.putExtra("userinfo", userinfo);
                startActivityForResult(GroupRoomIntent, 2);
            }
        }
    };

    public void ReusltActivity_3_OK(GroupItem groupItem){
//        GroupItem groupItem = (GroupItem) data.getSerializableExtra("groupitem");
        Log.d("rrrrr",groupItem.getGroup_name());
        datalist.add(0,groupItem);
        refreshGrouplist();
    }


    public void refreshGrouplist(){
        adapter.notifyDataSetInvalidated();
        listView.invalidateViews();
    }



    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            receivemessage = intent.getStringExtra("message");
        }
    };

    public void sync(){
        RenewGroupList renewGL = new RenewGroupList(userinfo);
        try {
            datalist = renewGL.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter=new GroupListAdapter(getActivity(),R.layout.groupitem,datalist,userinfo);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(iListener);
    }


}
