package com.example.alarm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class setting_push_time extends AppCompatActivity implements View.OnClickListener {

    Button back;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_push_time);
    }

    @Override
    public void onClick(View view){

        //뒤로가기 버튼 누르면 다시 설정화면으로 돌아가기
        int v = view.getId();
        if (v == R.id.repeat_alarm_backbutton) {



        }

    }



}
