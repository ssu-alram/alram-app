package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitRepeatAlram extends AppCompatActivity implements View.OnClickListener {

    Button backbutton;
    Button nextbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_repeat_alram);

        backbutton = findViewById(R.id.backbutton);
        nextbutton = findViewById(R.id.nextbutton);

        backbutton.setOnClickListener(this);
        nextbutton.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        ComponentName init_one = new ComponentName(
                "com.example.alarm",
                "com.example.alarm.InitPushAlram"
        );
        intent.setComponent(init_one);
        startActivity(intent);
    }

}