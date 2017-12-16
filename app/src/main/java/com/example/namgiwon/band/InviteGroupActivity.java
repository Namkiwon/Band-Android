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

import com.example.namgiwon.band.http.InviteGroupMember;
import com.example.namgiwon.band.http.MakeGroup;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 12. 7..
 */

public class InviteGroupActivity extends AppCompatActivity {
    FindChattingMemberFragment cfFragment;
    InputMethodManager imm;
    UserInfo userInfo;
    String alreadymember;
    GroupItem groupItem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitegroup);
        //액션바 설정하기//
        int titlecolor = Color.parseColor("#00BBBB");
        //액션바 타이틀 변경하기
        getSupportActionBar().setTitle("초대상대 선택");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(titlecolor));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cfFragment = new FindChattingMemberFragment();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        groupItem = (GroupItem) getIntent().getSerializableExtra("groupinfo");

        userInfo = (UserInfo) getIntent().getSerializableExtra("userinfo");
        // fragment 에 값을 전달하기 위한 번들사용
        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 수
        bundle.putString("userID",userInfo.getId());

        //Fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        cfFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.InviteGrouplayout,cfFragment);
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

//        imm.hideSoftInputFromWindow(groupName.getWindowToken(),0);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_findmember, menu);
        return true;
    }



//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
            case R.id.findmember_ok:
                StringBuilder sb = cfFragment.sb3;
                String finalInviteMember  = sb.toString();
                String a[] = groupItem.getGroup_member().split(",");
                for(int i = 0 ; i < a.length;i++){
                    finalInviteMember = finalInviteMember.toString().replace(a[i],"");
                }

                InviteGroupMember igm = new InviteGroupMember(userInfo.getId(),finalInviteMember,groupItem);
                igm.execute();


                finish();
                    break;
                }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void onBackPressed() {
//        ReturnIntent = new Intent();
//        setResult(Activity.RESULT_CANCELED, ReturnIntent);
//        finish();
//    }
}
