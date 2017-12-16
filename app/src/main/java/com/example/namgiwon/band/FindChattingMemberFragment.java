package com.example.namgiwon.band;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.namgiwon.band.http.RenewFriendList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 10. 5..
 */

public class FindChattingMemberFragment extends Fragment {
    TextView invitestatement;
    ListView listView;
    String userID = "";
    ArrayList<Frienditem> datalist;
    ArrayList<Frienditem> memberlist;
    FindChattingMemberAdapter adapter;
    Frienditem ResultItem;
    Intent profileIntent;
    Intent AddFriendIntent;
    StringBuilder sb;
    JsonArray jsonarr;
    public StringBuilder sb3;
    public StringBuilder roomname;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddFriendIntent = new Intent(getActivity(),AddFriendActivity.class);
        profileIntent = new Intent(getActivity(),Profile_Activity.class);
        memberlist = new ArrayList<Frienditem>();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //엑티비티에서 프래그먼트로 데이터를 전달할 때에는 bundle 을 사용한다.
        Bundle bundle = getArguments();
        userID = bundle.getString("userID");
        sb3 = new StringBuilder();
        Log.d("asdfasdfasfd",userID + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        //inflater 를 사용하여 view 를 생성
        View view = inflater.inflate(R.layout.fragment_chattingfriend,null);
        listView = (ListView) view.findViewById(R.id.chattingfriendlistview);
        invitestatement = (TextView) view.findViewById(R.id.invitestatement);
//        radio = (RadioButton) view.findViewById(R.id.friendcheck);

        //친구목록을 최신화 한다.
        RenewFriendList renewFL = new RenewFriendList(userID);
        try {
            datalist = renewFL.execute().get();
            for(int i =0 ; i < datalist.size(); i++){
                if(datalist.get(i).getID().equals(userID)) datalist.remove(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter=new FindChattingMemberAdapter(getActivity(),R.layout.findmember_frienditem,datalist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(iListener);

        return view;
    }





    AdapterView.OnItemClickListener iListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ListView listView = (ListView) adapterView;


            //아이템 클릭시 해당 멤버 jsonobj에 추가
            jsonarr = new JsonArray();
            JsonObject jsonobj = new JsonObject();
            sb3 = new StringBuilder();
            sb = new StringBuilder();
            roomname = new StringBuilder();
            jsonarr = new JsonArray();

            Frienditem item = (Frienditem) listView.getAdapter().getItem(i);
            if(!memberlist.contains(item)){ memberlist.add(item);}
            else {memberlist.remove(item);}

            for(int j = 0; j < memberlist.size();j++){
                roomname.append(memberlist.get(j).getName());
                if(j != memberlist.size()-1)roomname.append(", ");
                sb3.append(memberlist.get(j).getSno()).append(",");
                jsonobj.addProperty("name",memberlist.get(j).getName());
                jsonobj.addProperty("sno",memberlist.get(j).getSno());
                jsonarr.add(jsonobj);
                jsonobj = new JsonObject();

            }

            invitestatement.setText(roomname.toString());
        }
    };



    public void refreshFriendlist(){
        adapter.notifyDataSetInvalidated();
        listView.invalidateViews();
    }

}
