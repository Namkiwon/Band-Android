package com.example.namgiwon.band;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.namgiwon.band.http.LogIn;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 10. 4..
 */

public class LogInActivity extends AppCompatActivity {
    EditText ID;
    EditText PW;
    Button SignUp;
    Button SignIn;
    Intent SignupIntent;
    Intent HomeIntent;
    InputMethodManager imm1;
    LinearLayout loginLayout;
    String answer;
    String token;
    UserInfo userinfo;
    Gson gson;
    SharedMemory sharedMemory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int titlecolor = Color.parseColor("#00BBBB");
        getSupportActionBar().setTitle("Log In");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(titlecolor));

        gson = new Gson();
        imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        SignupIntent = new Intent(this,SignupActivity.class);
        HomeIntent = new Intent(this,HomeActivity.class);

        ID = (EditText) findViewById(R.id.id);
        PW = (EditText) findViewById(R.id.passwd);

        SignIn = (Button) findViewById(R.id.signIn);
        SignIn.setOnClickListener(bListener);

        SignUp = (Button) findViewById(R.id.signUp);
        SignUp.setOnClickListener(bListener);

        loginLayout = (LinearLayout) findViewById(R.id.loginlayout);
        loginLayout.setOnClickListener(bListener);
        token = FirebaseInstanceId.getInstance().getToken();
    }

    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override

        public void onClick(View v) {
            hidekeyboard();
            switch (v.getId()){
                case R.id.signUp : finish(); startActivity(SignupIntent); break;
                case R.id.signIn :
                    LogIn li = new LogIn(ID.getText().toString(), PW.getText().toString(), token);
                    try {
                        answer = li.execute().get();
                        Toast.makeText(getApplicationContext(),answer,Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(!answer.equals("login fail")){
                        userinfo = gson.fromJson(answer,UserInfo.class);
                        Log.d("tTTTTT",answer);
                        HomeIntent.putExtra("userinfo",userinfo);
                        startActivity(HomeIntent);

                        // 로그인 성공시에 sharedmemory에 아이디 올리기
                        sharedMemory = SharedMemory.getinstance();
                        sharedMemory.setUserinfo(userinfo);
                        finish();
                    }

                    break;
            }
        }
    };

    private void hidekeyboard(){
        imm1.hideSoftInputFromWindow(ID.getWindowToken(),0);
        imm1.hideSoftInputFromWindow(PW.getWindowToken(),0);
    }


}
