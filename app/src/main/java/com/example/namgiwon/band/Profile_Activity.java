package com.example.namgiwon.band;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.namgiwon.band.http.BringPhoto;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 10. 7..
 */

public class Profile_Activity extends FragmentActivity {


    TextView profile_chat ;
    LinearLayout profile_layout;
    TextView profile_alternickname;
    TextView profile_nickname;
    TextView profile_name;
    TextView profile_phonenumber;
    Intent FLintent;
    Intent Returnintent;
    String nickname;
    String userID;
    String name;
    String phonenumber;
    int position;
    boolean isNickanameUpdate = false;
    InputMethodManager imm1;
    Intent UpdatenicknameIntent;
    ImageView iv;
    Bitmap bitimage;
    UserInfo userInfo;
    Frienditem frienditem;
    String a;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀바 없애기 -> 대신에  FragmentActivity 를 상송받아야한다
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.6f; // 투명도 0 ~ 1
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_profile);

        Gson gson = new Gson();
        iv= (ImageView) findViewById(R.id.profileactivity_photo);
        imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //닉네임변경 액티비티 인텐트 설정


        //뷰 및 클릭리스너 설정
        profile_layout = (LinearLayout) findViewById(R.id.profile_layout);
        profile_layout.setOnClickListener(bListener);


        profile_nickname = (TextView) findViewById(R.id.profile_nickname);

        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_phonenumber =(TextView) findViewById(R.id.profile_phonenumber);

        //FriendListFragment 에서 받은 값들 저장
        FLintent = getIntent();
        userID = FLintent.getStringExtra("userID");
        name = FLintent.getStringExtra("name");
        phonenumber = FLintent.getStringExtra("phonenumber");
        nickname = FLintent.getStringExtra("nickname");
        position = FLintent.getIntExtra("position",1);
        userInfo = (UserInfo) FLintent.getSerializableExtra("userinfo");
        a = FLintent.getStringExtra("friendinfo");
        frienditem = gson.fromJson(a,Frienditem.class);
        profile_nickname.setText(nickname);
        profile_name.setText(name);
        profile_phonenumber.setText(phonenumber);
        profile_phonenumber.setOnClickListener(bListener);


        if(!frienditem.getProfile_path().equals("http://117.17.142.133:8080/img/no.jpg")) {
            BringPhoto BP = new BringPhoto(frienditem.getProfile_path());
            try {
                bitimage = BP.execute().get();
                iv.setImageBitmap(bitimage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
//            hidekeyboard();
            switch (v.getId()){
                case R.id.profile_layout :
                    Returnintent = new Intent();
                    if(!isNickanameUpdate){ // 닉네임 변경이 있었을시에
                        setResult(Activity.RESULT_CANCELED,Returnintent);
                    }

                    if(isNickanameUpdate){ // 닉네임 변경이 없었을시에 friendlistFragment 에 반환값
                        Returnintent = new Intent();
                        Returnintent.putExtra("nickname",profile_nickname.getText().toString());
                        Returnintent.putExtra("position",position);
                        setResult(Activity.RESULT_OK,Returnintent);}
                         finish();
                    break;
                case R.id.profile_phonenumber :
                    String tel = "tel:"+profile_phonenumber.getText().toString();
                    Log.d("tel",tel);
                            //profile_phonenumber.getText().toString();
//                        Intent callIntent = new Intent("android.intent.action.CALL", Uri.parse(tel));
//                    startActivity(new Intent(callIntent));
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));



            }
        }
    };

    private void hidekeyboard(){
        imm1.hideSoftInputFromWindow(profile_layout.getWindowToken(),0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { //setnicknameActivity 에서 반환받은후 일처리
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                profile_nickname.setText(result);
                isNickanameUpdate = true;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }

    }



}
