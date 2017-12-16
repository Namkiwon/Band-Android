package com.example.namgiwon.band;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.namgiwon.band.http.GetoutChattingRoom;
import com.example.namgiwon.band.http.RenewChattingList;
import com.example.namgiwon.band.http.SendMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 11. 11..
 */

public class ChattingActivity extends AppCompatActivity {
    String answer;
    String userID;
    InputMethodManager imm1;
    ListView Conversation;
    EditText Message;
    Button Sendmessage;
    LinearLayout chattinglinearlayout;
    JsonObject jsonobj;
    ChattingAdapter adapter;
    ArrayList<ChatItem> chatlist;
    RenewChattingList renewCL;
    Gson gson;
    String receivemessage;
    UserInfo userinfo;
    ChattingRoomItem CRI;
    Boolean flag = false;
    long now;
    Date date;
    SimpleDateFormat sdfNow;
    ChattingRoomItem roominfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        //액션바 설정하기//
        int titlecolor = Color.parseColor("#00BBBB");
        //액션바 타이틀 변경하기
//        getSupportActionBar().setTitle("chatting");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(titlecolor));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        roominfo = (ChattingRoomItem) getIntent().getSerializableExtra("roominfo");

        userinfo = (UserInfo) getIntent().getSerializableExtra("userinfo");

        getSupportActionBar().setTitle(roominfo.getRoomname());



        //메세지를 받기위한 브로드캐스트 설정
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("messageReceiver"));

        gson = new Gson();

        imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Conversation = (ListView) findViewById(R.id.conversation);

        Message = (EditText) findViewById(R.id.message);
        Sendmessage = (Button)findViewById(R.id.sendmessage);
        chattinglinearlayout = (LinearLayout)findViewById(R.id.chattingLinearLayout);

        Sendmessage.setOnClickListener(bListener);
        chattinglinearlayout.setOnClickListener(bListener);


        chatlist = new ArrayList<ChatItem>();

        renewCL = new RenewChattingList(String.valueOf(roominfo.getRoomid()));
        try {
            chatlist = renewCL.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter=new ChattingAdapter(getApplicationContext(),R.layout.chatitem,chatlist,userinfo);
        Conversation.setAdapter(adapter);
        Conversation.setSelection(adapter.getCount()-1);
        Conversation.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }


    Button.OnClickListener bListener = new Button.OnClickListener(){
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override

        public void onClick(View v) {
//            hidekeyboard();
            switch (v.getId()){
                case R.id.sendmessage :
                    if(!Message.getText().toString().trim().equals("")) {
                        jsonobj = new JsonObject();

                        jsonobj.addProperty("time",time());
                        jsonobj.addProperty("roomid", roominfo.getRoomid());
                        jsonobj.addProperty("sender_id", userinfo.getId());
                        jsonobj.addProperty("sender_name", userinfo.getName());
                        jsonobj.addProperty("message", Message.getText().toString().trim());

                        SendMessage sm = new SendMessage(jsonobj, userinfo.getId());
                        sm.execute();
                        Message.setText("");

                        ChatItem chatitem = gson.fromJson(jsonobj, ChatItem.class);
                        chatlist.add(chatitem);
                        refreshConversation();
                        flag = true;

                    }
                        break;
                case R.id.message :
                    Conversation.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    break;
                case R.id.chattingLinearLayout :
                    hidekeyboard();
                    break;
            }
        }
    };

    private void hidekeyboard(){
        imm1.hideSoftInputFromWindow(Message.getWindowToken(),0);
    }

    public void sync(){
        renewCL = new RenewChattingList(String.valueOf(roominfo.getRoomid()));
        try {
            chatlist = renewCL.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter=new ChattingAdapter(getApplicationContext(),R.layout.chatitem,chatlist,userinfo);
        Conversation.setAdapter(adapter);
    }

    public void refreshConversation(){
        adapter.notifyDataSetInvalidated();
        Conversation.invalidateViews();
    }


    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            receivemessage = intent.getStringExtra("message");
            ChatItem chatitem = gson.fromJson(receivemessage,ChatItem.class);
            if(receivemessage != null)
            {
                //what you want to do
                if(chatitem.getRoomid().equals(String.valueOf(roominfo.getRoomid()))){
                chatlist.add(chatitem);
                refreshConversation();}
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent Returnintent = new Intent();
        Returnintent.putExtra("roomid",String.valueOf(roominfo.getRoomid()));
        if(flag) setResult(Activity.RESULT_OK, Returnintent);
        else setResult(Activity.RESULT_CANCELED,Returnintent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        switch (item.getItemId()){
            case android.R.id.home :
                Intent Returnintent = new Intent();
                Log.d("yyyyyyy", String.valueOf(roominfo.getRoomid()));
                Returnintent.putExtra("roomid",String.valueOf(roominfo.getRoomid()));
                if(flag){ setResult(Activity.RESULT_OK, Returnintent);
                            Log.d("aa","bb");}
                else setResult(Activity.RESULT_CANCELED,Returnintent);
                finish();
                return true;

            case R.id.getout :
                GetoutChattingRoom GCR = new GetoutChattingRoom(String.valueOf(roominfo.getRoomid()));
                GCR.execute();
                Returnintent = new Intent();
                Returnintent.putExtra("roomid",String.valueOf(roominfo.getRoomid()));
                setResult(Activity.RESULT_CANCELED, Returnintent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String time(){  //시간발생
        now = System.currentTimeMillis();
        date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        sdfNow = new SimpleDateFormat("HH:mm");
        String formatDate = sdfNow.format(date);
        return  formatDate;
    }




}
