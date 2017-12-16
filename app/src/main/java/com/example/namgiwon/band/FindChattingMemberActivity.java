package com.example.namgiwon.band;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.namgiwon.band.http.MakeChattingRoom;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 11. 7..
 */

public class FindChattingMemberActivity extends AppCompatActivity {
    private String userID="";
    FindChattingMemberFragment cfFragment;
    String roomid;
    Intent Returnintent;
    Intent chattingintent;
    UserInfo userinfo;
    String answer;
    ChattingRoomItem chattingRoomItem;
    Gson gson ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findchattingmember);
        //액션바 설정하기//
        int titlecolor = Color.parseColor("#00BBBB");
        //액션바 타이틀 변경하기
        getSupportActionBar().setTitle("대화상대 선택");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(titlecolor));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        gson = new Gson();
        Intent chattingFragment = getIntent();

        userinfo = (UserInfo) chattingFragment.getSerializableExtra("userinfo");
        Log.d("사용자정보 ", userinfo.getId());
        Log.d("사용자정보 ", userinfo.getName());
        Log.d("사용자정보 ", userinfo.getSno());

        // fragment 에 값을 전달하기 위한 번들사용
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 수
        bundle.putString("userID",userinfo.getId());

        //Fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        cfFragment = new FindChattingMemberFragment();

        cfFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.findchattingmemberlayout,cfFragment);
        fragmentTransaction.commitNow();

        chattingintent = new Intent(this,ChattingActivity.class);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_findmember, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.findmember_ok :
                ///////// 추후에  방을 하나 만들고 메세지를 하나 보내야  방 생성되게
                // 아니면 메세지를 안보내고 바로 나오면 방 바로 삭제   <<< 이게 나을듯 유념!!!!
                // 채탱방 만들고 꼭 수정하쟈
                StringBuilder sb;
                StringBuilder sb1;
                sb = cfFragment.sb3;
                if(!sb.toString().equals("")){  // 추가한 친구내역이 없다면
                    String roomname = cfFragment.jsonarr.toString();
                    MakeChattingRoom makeChattingRoom = new MakeChattingRoom(userinfo.getId(), sb.toString(), roomname);
                    try {
                        answer = makeChattingRoom.execute().get();
                        Log.d("item",answer.toString() +"zzzzzzzzttttttttttttttt");
                        chattingRoomItem = gson.fromJson(answer,ChattingRoomItem.class);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    chattingintent.putExtra("userID", userinfo.getId());
                    chattingintent.putExtra("roominfo", chattingRoomItem);
                    chattingintent.putExtra("userinfo", userinfo);
                    startActivityForResult(chattingintent, 1);
                    break;
                    //or switch문을 이용하면 될듯 하다.
                }
            case android.R.id.home :
                Returnintent = new Intent();
//                Returnintent.putExtra("roomid",String.valueOf(chattingRoomItem.getRoomid()));
                setResult(Activity.RESULT_CANCELED, Returnintent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                Returnintent = new Intent();
                Returnintent.putExtra("roomid",String.valueOf(chattingRoomItem.getRoomid()));
                setResult(Activity.RESULT_OK, Returnintent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Intent a= getIntent();
                Returnintent = new Intent();
                Returnintent.putExtra("roomid",data.getStringExtra("roomid"));
                setResult(Activity.RESULT_CANCELED, Returnintent);
                finish();
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }

    }

    @Override
    public void onBackPressed() {
        Returnintent = new Intent();
        setResult(Activity.RESULT_CANCELED, Returnintent);
        finish();
    }
}
