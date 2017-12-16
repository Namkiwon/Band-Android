package com.example.namgiwon.band;

import android.app.Activity;
import android.content.Context;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.namgiwon.band.FindChattingMemberFragment;
import com.example.namgiwon.band.R;
import com.example.namgiwon.band.UserInfo;
import com.example.namgiwon.band.http.MakeChattingRoom;
import com.example.namgiwon.band.http.MakeGroup;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;


/**
 * Created by namgiwon on 2017. 11. 7..
 */

public class MakeGroupActivity extends AppCompatActivity {
    private String userID="";
    FindChattingMemberFragment cfFragment;
    String answer;
    GroupItem groupItem;
    Intent ReturnIntent;
    UserInfo userinfo;
    InputMethodManager imm;
    LinearLayout parentLayout;
    EditText groupName;
    String GroupName;
    Gson gson;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makegroup);
        //액션바 설정하기//
        int titlecolor = Color.parseColor("#00BBBB");
        //액션바 타이틀 변경하기
        getSupportActionBar().setTitle("초대상대 선택");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(titlecolor));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parentLayout = (LinearLayout) findViewById(R.id.makegrouplayout);
        parentLayout.setOnClickListener(bListener);
        groupName = (EditText) findViewById(R.id.makegroupname);
        cfFragment = new FindChattingMemberFragment();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent GLFragment = getIntent();

        userinfo = (UserInfo) GLFragment.getSerializableExtra("userinfo");
        // fragment 에 값을 전달하기 위한 번들사용
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 수
        bundle.putString("userID",userinfo.getId());

        //Fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        cfFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.group_findfriend,cfFragment);
        fragmentTransaction.commitNow();
Log.d("1111","222222");

    }

    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override

        public void onClick(View v) {
            hidekeyboard();
            switch (v.getId()){
            }
        }
    };

    private void hidekeyboard(){

        imm.hideSoftInputFromWindow(groupName.getWindowToken(),0);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_findmember, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.findmember_ok:
                gson = new Gson();
                GroupName = groupName.getText().toString();
                StringBuilder sb;
                sb = cfFragment.sb3;
                if (!sb.toString().equals("") && !GroupName.equals("")) {  // 추가한 친구내역이 없다면
                    MakeGroup makeGroup = new MakeGroup(userinfo.getId(), sb.toString(), GroupName);
                    try {
                        answer = makeGroup.execute().get();
                        groupItem = new GroupItem();
                        groupItem = gson.fromJson(answer,GroupItem.class);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    ReturnIntent = new Intent();
                    ReturnIntent.putExtra("groupitem",groupItem);
                    setResult(Activity.RESULT_OK, ReturnIntent);
                    finish();
                    break;
                }
        }
                return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        ReturnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, ReturnIntent);
        finish();
    }
}
