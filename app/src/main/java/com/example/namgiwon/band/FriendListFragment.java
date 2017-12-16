package com.example.namgiwon.band;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.namgiwon.band.http.RenewFriendList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 10. 5..
 */

public class FriendListFragment extends Fragment {

    ListView listView;
//    Button Plus;
    Intent AddFriendIntent;
    String userID = "";
    ArrayList<Frienditem> datalist;
    FriendAdapter adapter;
    Frienditem ResultItem;
    Intent profileIntent;
    UserInfo userInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddFriendIntent = new Intent(getActivity(),AddFriendActivity.class);
        profileIntent = new Intent(getActivity(),Profile_Activity.class);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //엑티비티에서 프래그먼트로 데이터를 전달할 때에는 bundle 을 사용한다.
        Bundle bundle = getArguments();
        userID = bundle.getString("userID");
        userInfo = (UserInfo) bundle.getSerializable("userinfo");
        //inflater 를 사용하여 view 를 생성
        View view = inflater.inflate(R.layout.friendlistfragment,null);
        listView = (ListView) view.findViewById(R.id.friendlistview);
//        Plus = (Button) view.findViewById(R.id.Plus);
//        Plus.bringToFront();
//        Plus.setOnClickListener(bListener);

        //친구목록을 최신화 한다.
        RenewFriendList renewFL = new RenewFriendList(userID);
        try {
            datalist = renewFL.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter=new FriendAdapter(getActivity(),R.layout.frienditem,datalist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(iListener);

        return view;
    }


//    Button.OnClickListener bListener = new Button.OnClickListener(){
//        @Override
//        public void onClick(View v) {
//
//            switch (v.getId()){
//                case R.id.Plus :
//                    RenewFriendList renewFL = new RenewFriendList(userID);
//                    try {
//                        datalist = renewFL.execute().get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                    adapter=new FriendAdapter(getActivity(),R.layout.frienditem,datalist);
//                    listView.setAdapter(adapter);
//                    listView.setOnItemClickListener(iListener);
//                    break;
//            }
//        }
//    };


    AdapterView.OnItemClickListener iListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ListView listView = (ListView) adapterView;

            String itemjsonstr = (String) listView.getAdapter().getItem(i);
            Gson gson = new Gson();
            JsonObject jsonobj = gson.fromJson(itemjsonstr,JsonObject.class);
            profileIntent.putExtra("friendinfo",itemjsonstr);
            profileIntent.putExtra("userID",userID);
            profileIntent.putExtra("name",jsonobj.get("name").getAsString());
            profileIntent.putExtra("phonenumber",jsonobj.get("phonenumber").getAsString());
            profileIntent.putExtra("nickname",jsonobj.get("nickname").getAsString());
            profileIntent.putExtra("position",i);
            startActivityForResult(profileIntent,2);

//            Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
        }
    };




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                    //StratActivitlyresult 로 받아온 값을 datalist 에 추가
                ResultItem = (Frienditem) data.getSerializableExtra("result");
                datalist.add(ResultItem);

                    //변경사항 체크하고 listview  에 반
                    refreshFriendlist();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }

        if (requestCode == 2) { //profile 의 반환값
            if(resultCode == Activity.RESULT_OK){
                int position = data.getIntExtra("position",1);
                String nickname = data.getStringExtra("nickname");
                datalist.get(position).setNickname(nickname);
                refreshFriendlist();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    public void refreshFriendlist(){
        adapter.notifyDataSetInvalidated();
        listView.invalidateViews();
    }

    public void sync(){
        RenewFriendList renewFL = new RenewFriendList(userID);
        try {
            datalist = renewFL.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter=new FriendAdapter(getActivity(),R.layout.frienditem,datalist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(iListener);
    }

}
