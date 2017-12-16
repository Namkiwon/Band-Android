package com.example.namgiwon.band;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.namgiwon.band.http.SignUp;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

/**
 * Created by Namkiwon on 2017-09-28.
 */

public class SignupActivity extends AppCompatActivity {

    String answer;

    EditText ID;
    EditText PW;
    EditText Name;
    EditText Studentnumber;
    Spinner School;
    Spinner Dept;
    EditText PhoneNumber;
    Button FinishSignUp;
    Button IDcheck;
    Button Cancel;
    String id;
    String pw;
    String name;
    String school;
    String phonenumber;
    String department;
    String studentnumber;
    JSONObject jsonobj = new JSONObject();
    LinearLayout signupLayout;
    InputMethodManager imm;
    Intent loginIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        int titlecolor = Color.parseColor("#00BBBB");
        getSupportActionBar().setTitle("Intro");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(titlecolor));


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ID = (EditText) findViewById(R.id.ID);
        PW = (EditText) findViewById(R.id.PW);
        Studentnumber = (EditText) findViewById(R.id.studentnumber);
        Name = (EditText) findViewById(R.id.Name);
        PhoneNumber = (EditText) findViewById(R.id.PhoneNumber);
        School = (Spinner) findViewById(R.id.school);
        Dept = (Spinner) findViewById(R.id.department);
        FinishSignUp = (Button) findViewById(R.id.finishSignup);
        FinishSignUp.setOnClickListener(bListener);

        IDcheck = (Button) findViewById(R.id.IDcheck);
        IDcheck.setOnClickListener(bListener);

        Cancel = (Button) findViewById(R.id.cancel);
        Cancel.setOnClickListener(bListener);


        signupLayout = (LinearLayout) findViewById(R.id.signuplayout);
        signupLayout.setOnClickListener(bListener);

        loginIntent = new Intent(this,LogInActivity.class);

        ArrayAdapter SchoolSpinneradapter = ArrayAdapter.createFromResource(this, R.array.school, android.R.layout.simple_spinner_item);
        School.setAdapter(SchoolSpinneradapter);
        ArrayAdapter DeptSpinneradapter = ArrayAdapter.createFromResource(this, R.array.department, android.R.layout.simple_spinner_item);
        Dept.setAdapter(DeptSpinneradapter);




    }

    Button.OnClickListener bListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
           hidekeyboard();
            switch (v.getId()){
                case R.id.finishSignup :
                    id = ID.getText().toString();
                    pw = PW.getText().toString();
                    name = Name.getText().toString();
                    phonenumber = PhoneNumber.getText().toString();
                    school = School.getSelectedItem().toString();
                    department = Dept.getSelectedItem().toString();
                    studentnumber = Studentnumber.getText().toString();
                    try {
                        jsonobj.put("ID",id);
                        jsonobj.put("PW",pw);
                        jsonobj.put("name",name);
                        jsonobj.put("phonenumber",phonenumber);
                        jsonobj.put("school",school);
                        jsonobj.put("department",department);
                        jsonobj.put("studentnumber",studentnumber);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SignUp s = new SignUp("Finish",jsonobj);

                    try {

                        answer = s.execute().get();
                        Toast.makeText(getApplicationContext(),answer,Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(answer.equals("signup success\n")){
                        startActivity(loginIntent);
                        finish();
                    }

                    break;


                case R.id.IDcheck :
                    id = ID.getText().toString();


                    try {
                        jsonobj.put("ID",id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SignUp checkID = new SignUp("IDcheck",jsonobj);
                    try {
                        answer = checkID.execute().get();
                        Toast.makeText(getApplicationContext(),answer, Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    break;
                case  R.id.cancel :
                    startActivity(loginIntent);
                    finish();
                    break;
            }

        }
    };

    private void hidekeyboard(){
        imm.hideSoftInputFromWindow(ID.getWindowToken(),0);
        imm.hideSoftInputFromWindow(PW.getWindowToken(),0);
        imm.hideSoftInputFromWindow(Name.getWindowToken(),0);
        imm.hideSoftInputFromWindow(PhoneNumber.getWindowToken(),0);
    }



}

