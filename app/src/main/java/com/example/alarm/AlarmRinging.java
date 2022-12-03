package com.example.alarm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

// 정한 시간이 되었을 때, 즉 알람이 울릴 때 뜨는 미션창입니다.
public class AlarmRinging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);
    }
}