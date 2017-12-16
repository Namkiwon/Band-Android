package com.example.namgiwon.band;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namgiwon.band.http.BringPhoto;
import com.example.namgiwon.band.http.CheckToken;
import com.example.namgiwon.band.http.WriteContents;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by namgiwon on 2017. 10. 4..
 */

public class HomeActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_CAMERA=1111;
    private static final int REQUEST_TAKE_PHOTO=2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP=4444;
    String imageFileName;
    String imgPath = "";
    Uri albumUri;
    Uri photoUri;
    Bitmap bitimage;
    String token;
    String answer;

    ///
    ChattingRoomListFragment chattingFragment;

    ///

    Gson gson;
    String userID = "";
    TextView renewButton;
    GroupListFragment glFragment;
    FriendListFragment flFragment;
    UserInfo userinfo;
    ImageView bufferimage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //액션바 설정하기//
        int titlecolor = Color.parseColor("#00BBBB");
        //액션바 타이틀 변경하기
        getSupportActionBar().setTitle("Band");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(titlecolor));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);





        gson = new Gson();
        Intent loginIntent = getIntent();
        userinfo = (UserInfo) loginIntent.getSerializableExtra("userinfo");
        Log.d("65656556",userinfo.getId());
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost);
        tabHost1.setup();

        // fragment 에 값을 전달하기 위한 번들사용
        Bundle bundle = new Bundle(2); // 파라미터는 전달할 데이터 수
        bundle.putString("userID",userinfo.getId());
        bundle.putSerializable("userinfo",userinfo);

        //Fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //=========================================================
        // 첫 번째 Tab. (탭 표시 텍스트:"FRIENDS"), (페이지 뷰:"Friends")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.friends);
        ts1.setIndicator("friends");
        tabHost1.addTab(ts1);



        flFragment = new FriendListFragment();

        flFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.friends,flFragment);
        fragmentTransaction.commitNow();


        //=======================================================
        // 두 번째 Tab. (탭 표시 텍스트:"GROUPS"), (페이지 뷰:"Group")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.group);
        ts2.setIndicator("group");
        tabHost1.addTab(ts2);


        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
        glFragment = new GroupListFragment();
        glFragment.setArguments(bundle);
        fragmentTransaction2.replace(R.id.group,glFragment);
        fragmentTransaction2.commitNow();


        //==========================================================
        // 세 번째 Tab. (탭 표시 텍스트:"CHATTING"), (페이지 뷰:"Chatting")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.chatting);
        ts3.setIndicator("chatting");
        tabHost1.addTab(ts3);



        FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
        chattingFragment = new ChattingRoomListFragment();
        chattingFragment.setArguments(bundle);
        fragmentTransaction3.replace(R.id.chatting,chattingFragment);
        fragmentTransaction3.commitNow();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == R.id.home_r) {
            flFragment.sync();
            return true;}
        if (id == R.id.resetGroup) {
            glFragment.sync();
            return true;
        }
        if (id == R.id.changeMyphoto) {
            show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buffer_image :
                    checkPremission();
                    getAlbum();
                    break;
            }
        }
    };

    /////////////////////update Profile Photo////////////////////////////////////////

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_profilephoto, null);

        bufferimage = (ImageView) view.findViewById(R.id.buffer_image);


        bufferimage.setOnClickListener(bListener);
        TextView my_name = (TextView) view.findViewById(R.id.my_name);
        TextView my_sno = (TextView) view.findViewById(R.id.my_sno);
        TextView my_number = (TextView) view.findViewById(R.id.my_number);
        my_name.setText(userinfo.getName());
        my_sno.setText(userinfo.getSno());
        my_number.setText(userinfo.getPhonenumber());

        BringPhoto BP = new BringPhoto(userinfo.getProfile_path());
        try {
            bitimage = BP.execute().get();
            bufferimage.setImageBitmap(bitimage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        builder.setView(view);

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String photopath = "http://117.17.142.133:8080/img/"+imageFileName;

                        DoFileUpload dfu = new DoFileUpload("http://117.17.142.133:8080/Band/BandServer?id="+userinfo.getId()+"&Type=friendList&Option=updateProfilePath&photo="+photopath,imgPath);
                        dfu.execute();
                        token = FirebaseInstanceId.getInstance().getToken();
                        CheckToken CT = new CheckToken(token);
                        try {
                            answer = CT.execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        userinfo = gson.fromJson(answer,UserInfo.class);


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
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA))){
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
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},MY_PERMISSION_CAMERA);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_CAMERA:
                for(int i=0; i<grantResults.length;i++){
                    if(grantResults[i]<0){
                        Toast.makeText(HomeActivity.this,"해당 권한을 활성화 해야합니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }

    public File createImageFile()throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = timeStamp+".jpg";

        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/Pictures","WIT");

        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        imageFile = new File(storageDir, imageFileName);
        imgPath = imageFile.getAbsolutePath();

        Log.d("-------storageDir","create file path : "+imgPath);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
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
                    bufferimage.setImageURI(albumUri);
                    System.out.println(imgPath+"asdfasdfasdfasdfasdfasdfasdfasdfasfd");
                }
                break;
        }
            if(requestCode ==1) {
                if (resultCode == Activity.RESULT_OK) {
                    String roomid = data.getStringExtra("roomid");
                    chattingFragment.fragmentResult_OK_1(roomid);
                }
            }
            if(requestCode ==2) {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("nonooooo", "nonono");
                    String roomid = data.getStringExtra("roomid");
                    chattingFragment.fragmentResult_OK_2(roomid);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    String roomid = data.getStringExtra("roomid");
                    chattingFragment.fragmentResult_CANCEL_2(roomid);
                }
            }
        if(requestCode ==3) {
            if (resultCode == Activity.RESULT_OK) {
                GroupItem groupItem = (GroupItem) data.getSerializableExtra("groupitem");
                glFragment.ReusltActivity_3_OK(groupItem);
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }

    }








}
