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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.namgiwon.band.http.GetChattingRoomItem;
import com.example.namgiwon.band.http.RenewChattingRoomList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on
 * 2017. 11. 6..
 */

public class ChattingRoomListFragment extends Fragment {
    ListView listView;
    ImageView noti;
    Intent AddChattingRoom;
    String userID = "";
    ArrayList<ChattingRoomItem> datalist;
    ChattingRoomListAdapter adapter;
    Intent findchattingmemeberIntent;
    Button Plus;
    RenewChattingRoomList renewCRL;;
    Intent chattingintent;
    String roomid;
    String receivemessage;
    Gson gson;
    UserInfo userinfo;
    int seq;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datalist = new ArrayList<ChattingRoomItem>();
        findchattingmemeberIntent = new Intent(getActivity(),FindChattingMemberActivity.class);
        Bundle bundle = getArguments();
        userinfo = (UserInfo) bundle.getSerializable("userinfo");
        renewCRL = new RenewChattingRoomList(userinfo.getId());
        chattingintent = new Intent(getActivity(),ChattingActivity.class);
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
        View view = inflater.inflate(R.layout.fragment_chatting,null);
        listView = (ListView) view.findViewById(R.id.chattinglistview);
        Plus = (Button) view.findViewById(R.id.Plus);
        Plus.bringToFront();
        Plus.setOnClickListener(bListener);


        //친구목록을 최신화 한다.
//****

        try {
            datalist = renewCRL.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        adapter = new ChattingRoomListAdapter(getActivity(),R.layout.chattingroomitem,datalist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(iListener);
        return view;
    }


    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.Plus :
                    findchattingmemeberIntent.putExtra("userinfo",userinfo);
                    getActivity().startActivityForResult(findchattingmemeberIntent,1);

                    break;
            }
        }
    };


    AdapterView.OnItemClickListener iListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ListView listView = (ListView) adapterView;
            ChattingRoomItem selectitem = (ChattingRoomItem) listView.getAdapter().getItem(i);
            chattingintent.putExtra("roominfo",selectitem);
            chattingintent.putExtra("userID",userinfo.getId());
            chattingintent.putExtra("userinfo",userinfo);

            getActivity().startActivityForResult(chattingintent,2);

        }
    };


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if(resultCode == Activity.RESULT_OK){
//                Log.d("bb","cc");
//                String roomid =  data.getStringExtra("roomid");
//                Log.d("키키키키",roomid);
//                for(int i = 0 ; i < datalist.size(); i++){
//                    if(roomid.equals(String.valueOf(datalist.get(i).getRoomid()))){
//                        datalist.remove(i);
//                        break;
//                    }
//                }
//
//                try {
//                    GetChattingRoomItem getCRI = new GetChattingRoomItem(roomid,userinfo);
//                    ChattingRoomItem cri = null;
//                    cri = getCRI.execute().get();
//                    Log.d("zzzzz",cri.getRoomname());
//                    datalist.add(0, cri);
//                    refreshChattinglist();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//                refreshChattinglist();
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
//
//
//            }
//        }
//
//        if (requestCode == 2) {
//            if(resultCode == Activity.RESULT_OK){
//                Log.d("bb","cc");
//                String roomid =  data.getStringExtra("roomid");
//                Log.d("키키키키",roomid);
//                for(int i = 0 ; i < datalist.size(); i++){
//                    if(roomid.equals(String.valueOf(datalist.get(i).getRoomid()))){
//                        datalist.remove(i);
//                        break;
//                    }
//                }
//
//                    try {
//                        GetChattingRoomItem getCRI = new GetChattingRoomItem(roomid,userinfo);
//                        ChattingRoomItem cri = null;
//                        cri = getCRI.execute().get();
//                        Log.d("zzzzz",cri.getRoomname());
//                        datalist.add(0, cri);
//                        refreshChattinglist();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                    refreshChattinglist();
//
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //만약 반환값이 없을 경우의 코드를 여기에 작성
//                String roomid =  data.getStringExtra("roomid");
//                Log.d("rrrr","rr");
//                for(int i = 0 ; i < datalist.size(); i++){
//                    if(roomid.equals(String.valueOf(datalist.get(i).getRoomid()))){
//                        adapter.getItem(i).setNew(false);
//                        refreshChattinglist();
//                        break;
//                    }
//                }
//            }
//        }
//
//    }

    public void refreshChattinglist(){
        adapter.notifyDataSetInvalidated();
        listView.invalidateViews();
    }


    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            receivemessage = intent.getStringExtra("message");
            JsonObject jsonobj = gson.fromJson(receivemessage,JsonObject.class);
            JsonObject CRI = gson.fromJson(jsonobj.get("chattingroomInfo").getAsString(),JsonObject.class);
            ChattingRoomItem cri = gson.fromJson(CRI,ChattingRoomItem.class);
            Log.d("키키키키ㅣ키키키키키 ",CRI.toString());
            if(receivemessage != null)
            {

                Boolean ishas = false;
                for(int i= 0; i < datalist.size(); i++){
                    if(jsonobj.get("roomid").getAsString().equals(String.valueOf(datalist.get(i).getRoomid()))){
                        ishas = true;
                        datalist.remove(datalist.get(i));
                        cri.setNew(true);
                        datalist.add(0,cri);
                        refreshChattinglist();
                    }
                }
                if(ishas == false){
                        cri.setNew(true);
                        datalist.add(0,cri);
                        refreshChattinglist();
                    }
                }



        }
    };

////////////////////////OnActivityResult method/////
    public void fragmentResult_OK_1(String roomid){
        for(int i = 0 ; i < datalist.size(); i++){
            if(roomid.equals(String.valueOf(datalist.get(i).getRoomid()))){
                datalist.remove(i);
                break;
            }
        }

        try {
            GetChattingRoomItem getCRI = new GetChattingRoomItem(roomid,userinfo);
            ChattingRoomItem cri = null;
            cri = getCRI.execute().get();
            Log.d("zzzzz",cri.getRoomname());
            datalist.add(0, cri);
            refreshChattinglist();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        refreshChattinglist();
    }


    public void fragmentResult_OK_2(String roomid){
        Log.d("bb","cc");
        Log.d("키키키키",roomid);
        for(int i = 0 ; i < datalist.size(); i++){
            if(roomid.equals(String.valueOf(datalist.get(i).getRoomid()))){
                datalist.remove(i);
                break;
            }
        }

        try {
            GetChattingRoomItem getCRI = new GetChattingRoomItem(roomid,userinfo);
            ChattingRoomItem cri = null;
            cri = getCRI.execute().get();
            Log.d("zzzzz",cri.getRoomname());
            datalist.add(0, cri);
            refreshChattinglist();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        refreshChattinglist();
    }


    public void fragmentResult_CANCEL_2(String roomid){
        for(int i = 0 ; i < datalist.size(); i++){
            if(roomid.equals(String.valueOf(datalist.get(i).getRoomid()))){
                adapter.getItem(i).setNew(false);
                refreshChattinglist();
                break;
            }
        }

    }


}
