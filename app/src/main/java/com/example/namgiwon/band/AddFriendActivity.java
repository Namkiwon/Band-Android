package com.example.namgiwon.band;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.namgiwon.band.http.AddFriend;
import com.example.namgiwon.band.http.RenewFriendList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 10. 5..
 */

public class AddFriendActivity extends AppCompatActivity {
    Button AddFriend;
    Button Cancel;
    EditText FriendNumber;
    EditText FriendName;
    String userID;
    String answer;
    InputMethodManager imm;
    RenewFriendList RFList;
    LinearLayout addFriendLayout;

    Intent FLintent;
    Intent Returnintent;
    ArrayList<Frienditem> data;
    Frienditem ResultItem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        addFriendLayout = (LinearLayout) findViewById(R.id.addfriendlayout);
        addFriendLayout.setOnClickListener(bListener);
        int titlecolor = Color.parseColor("#00BBBB");
        getSupportActionBar().setTitle("친구 추가");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(titlecolor));

        FLintent = getIntent();
        userID = FLintent.getStringExtra("userID");

        Cancel = (Button) findViewById(R.id.cancel);
        Cancel.setOnClickListener(bListener);

        AddFriend = (Button) findViewById(R.id.addFriend);
        AddFriend.setOnClickListener(bListener);

        FriendNumber = (EditText) findViewById(R.id.friendNumber);
        FriendName = (EditText) findViewById(R.id.friendName);
    }

    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            hidekeyboard();
            switch (v.getId()){
                case R.id.addFriend :
                    com.example.namgiwon.band.http.AddFriend AF = new AddFriend(userID,FriendNumber.getText().toString(),FriendName.getText().toString());
                    try {
                        answer = AF.execute().get();
                        Toast.makeText(getApplicationContext(),answer,Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(answer.equals("success add friend")) {
                        RFList = new RenewFriendList(userID);
                        try {
                            data = RFList.execute().get();

                            //새로 추가한 친구의 정보만을 뽑는다.
                            for(int i = 0 ; i < data.size(); i++){
                                if(data.get(i).getPhonenumber().equals(FriendNumber.getText().toString())){
                                    ResultItem = data.get(i);
                                    break;
                                }
                            }

                            Returnintent = new Intent();
                            Returnintent.putExtra("result", ResultItem);
                            setResult(Activity.RESULT_OK, Returnintent);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case R.id.cancel :
                    Returnintent = new Intent();
                    setResult(Activity.RESULT_CANCELED,Returnintent);
                    finish();
                    break;
            }
        }
    };

    private void hidekeyboard(){
        imm.hideSoftInputFromWindow(FriendNumber.getWindowToken(),0);
    }



}
