
package com.example.namgiwon.band;
        import android.os.Environment;
        import android.content.pm.PackageManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.Manifest;
        import android.widget.Toast;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentFilter;
        import java.io.File;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import java.io.IOException;
        import android.support.annotation.NonNull;
        import android.net.Uri;
        import android.support.annotation.Nullable;
        import android.support.annotation.RequiresApi;
        import android.support.v7.app.AppCompatActivity;
        import android.provider.Settings;

        import android.support.v4.content.LocalBroadcastManager;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;


        import com.example.namgiwon.band.http.GetMembers;
        import com.example.namgiwon.band.http.RenewContents;
        import com.example.namgiwon.band.http.RenewFriendList;
        import com.example.namgiwon.band.http.WriteContents;
        import com.google.gson.Gson;
        import com.google.gson.JsonArray;
        import com.google.gson.JsonObject;



        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 11. 26..
 */

public class GroupRoomActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_CAMERA=1111;
    private static final int REQUEST_TAKE_PHOTO=2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP=4444;
    ListView contentlistview;
    UserInfo userInfo;
    GroupItem groupItem;
    GroupRoomAdapter grouproomAdapter;
    Gson gson;
    String receivecontents;
    ArrayList<ContentsItem> contentslist;
    String mem;
    long now;
    Date date;
    SimpleDateFormat sdfNow;
    InputMethodManager imm1;
    String imgPath = "";
    Uri photoUri;
    Uri albumUri;
    ImageView imageView;
    String imageFileName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouproom);

        //액션바 설정하기//
        int titlecolor = Color.parseColor("#00BBBB");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(titlecolor));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        userInfo = (UserInfo) getIntent().getSerializableExtra("userinfo");
        groupItem = (GroupItem) getIntent().getSerializableExtra("groupinfo");
        //액션바 타이틀 변경하기
        getSupportActionBar().setTitle(groupItem.getGroup_name());


        gson = new Gson();

        contentlistview = (ListView) findViewById(R.id.contents_list);

        ///*
        contentslist = new ArrayList<ContentsItem>();
        RenewContents renewContents = new RenewContents(String.valueOf(groupItem.getGroup_id()));
        try {
            contentslist = renewContents.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        // */
        grouproomAdapter = new GroupRoomAdapter(getApplicationContext(),R.layout.contentsitem,contentslist,userInfo);
        //토큰값을 받기위해 브로드캐스트 리시버 설

        contentlistview.setAdapter(grouproomAdapter);
        contentlistview.setSelection(grouproomAdapter.getCount()-1);
        contentlistview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        LocalBroadcastManager.getInstance(this).registerReceiver(contentsReceiver,
                new IntentFilter("contentsReceiver"));




    }


    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            hidekeyboard();
            switch (v.getId()){
                case R.id.contents_image :
                    checkPremission();
                    getAlbum();
                    break;
            }
        }
    };

    private void hidekeyboard(){
    }



    public void refreshContents(){
        grouproomAdapter.notifyDataSetInvalidated();
        contentlistview.invalidateViews();

    }


    BroadcastReceiver contentsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            receivecontents = intent.getStringExtra("message");
            Log.d("yyyyyy",receivecontents + "<<<");
            ContentsItem ci = gson.fromJson(receivecontents,ContentsItem.class);
            Log.d("yyyyyy",ci.getGroup_id() + "<<<");
            Log.d("yyyyyy",groupItem.getGroup_id() + "<<<");
            if(receivecontents != null)
            {   //what you want to do
                if(ci.getGroup_id().equals(String.valueOf(groupItem.getGroup_id()))){
                    Log.d("yyyyyy",ci.getMessage() + "<<<");
                    contentslist.add(ci);
                    refreshContents();
                }
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_grouproom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        switch (item.getItemId()){
            case android.R.id.home :
                Intent ReturnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,ReturnIntent);
                finish();
                return true;
            case R.id.writecontents :
                show();
                return true;

            case R.id.lookmembers :
                GetMembers gm = new GetMembers(userInfo.getId(),String.valueOf(groupItem.getGroup_id()));

                try {
                    mem = gm.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                JsonArray jsonarr= gson.fromJson(mem,JsonArray.class);
                //Toast.makeText(getApplicationContext(),jsonarr.toString(),Toast.LENGTH_LONG).show();
                showMembers(jsonarr);
                return true;
            case R.id.invite :
                Intent inviteIntent= new Intent(this,InviteGroupActivity.class);
                inviteIntent.putExtra("groupinfo",groupItem);
                inviteIntent.putExtra("userinfo",userInfo);
                startActivityForResult(inviteIntent,1);
                return true;



        }
        return super.onOptionsItemSelected(item);
    }

    public String time(){  //시간발생
        now = System.currentTimeMillis();
        date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        sdfNow = new SimpleDateFormat("yy-MM-dd   HH:mm");
        String formatDate = sdfNow.format(date);
        return  formatDate;
    }

    @Override
    public void onBackPressed() {
        Intent ReturnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,ReturnIntent);
        finish();
    }




    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_writecontents, null);
        final EditText contentsLetter = (EditText) view.findViewById(R.id.letter);
        imageView = (ImageView) view.findViewById(R.id.contents_image);
        imageView.setOnClickListener(bListener);
        builder.setView(view);

        builder.setPositiveButton("게시",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        JsonObject jsonobj = new JsonObject();
                        String groupinfo = gson.toJson(groupItem);
                        jsonobj.addProperty("sender_id",userInfo.getId());
                        jsonobj.addProperty("sender_name",userInfo.getName());
                        jsonobj.addProperty("message",contentsLetter.getText().toString().trim());
                        jsonobj.addProperty("group_id",groupItem.getGroup_id());
                        jsonobj.addProperty("time",time());
                        jsonobj.addProperty("picture_path","http://117.17.142.133:8080/img/"+imageFileName);
                        imageFileName = null;
                        ContentsItem contentsitem = gson.fromJson(jsonobj,ContentsItem.class);
                        contentslist.add(contentsitem);
                        refreshContents();
                        WriteContents WC = new WriteContents(jsonobj,groupinfo,userInfo.getId());
                        WC.execute();
                        DoFileUpload dfu = new DoFileUpload("http://117.17.142.133:8080/Band/BandServer?Type=group&Option=savePhoto&photo="+imgPath,imgPath);
                        dfu.execute();

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }



    ///////////////////////////////////사진 메소드///////////////////////////////////////////


    private void checkPremission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if((ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE))||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA))){
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한 거부")
                        .setNegativeButton("설정",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i =new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.setData(Uri.parse("package"+getPackageName()));
                                startActivity(i);
                            }
                        })
                        .setPositiveButton("확인",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();

            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},MY_PERMISSION_CAMERA);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_CAMERA:
                for(int i=0; i<grantResults.length;i++){
                    if(grantResults[i]<0){
                        Toast.makeText(GroupRoomActivity.this,"해당 권한을 활성화 해야합니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }

    public File createImageFile()throws IOException{
        String nowtime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = nowtime+".jpg";

        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/Pictures","WIT");

        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        imageFile = new File(storageDir, imageFileName);
        imgPath = imageFile.getAbsolutePath();
        return imageFile;
    }
    private void getAlbum(){
        Log.i("getAlbum","Call");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,REQUEST_TAKE_ALBUM);

    }

    public void cropImage(){
        Log.i("cropImage","Call");
        Log.i("cropImage","photoURI:"+photoUri+" /albumURI : "+albumUri);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoUri,"image/*");
        cropIntent.putExtra("aspectX",1);
        cropIntent.putExtra("aspectY",1);
        cropIntent.putExtra("output",albumUri);
        startActivityForResult(cropIntent,REQUEST_IMAGE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode ==Activity.RESULT_OK){

                }
                break;
            case REQUEST_TAKE_ALBUM:
                if(resultCode== Activity.RESULT_OK){
                    if(data.getData()!=null){
                        try{
                            File albumFile = null;
                            albumFile = createImageFile();
                            photoUri=data.getData();
                            albumUri=Uri.fromFile(albumFile);
                            cropImage();
                        }catch (Exception e){
                            Log.e("Take_ALBUM_SINGLE ERROR",e.toString());
                        }
                    }
                }
                break;
            case REQUEST_IMAGE_CROP:
                if(resultCode==Activity.RESULT_OK){
                    // savePicture();
                    imageView.setImageURI(albumUri);
                    System.out.println(imgPath+"asdfasdfasdfasdfasdfasdfasdfasdfasfd");
                }
                break;
        }
    }




    public void showMembers(JsonArray jsonarr){

        final CharSequence[] items = new CharSequence[jsonarr.size()];
        for(int i = 0; i < jsonarr.size(); i++){
            String a = jsonarr.get(i).getAsJsonObject().get("sno").getAsString()+" "+jsonarr.get(i).getAsJsonObject().get("name").getAsString();
            items[i] = a;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // 제목셋팅
        alertDialogBuilder.setTitle("멤버 목록");
        alertDialogBuilder.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

                        // 프로그램을 종료한다
                        Toast.makeText(getApplicationContext(),
                                items[id] + " 선택했습니다.",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

        // 다이얼로그 생성
        AlertDialog alertDialog = alertDialogBuilder.create();

        // 다이얼로그 보여주기
        alertDialog.show();


    }



}
