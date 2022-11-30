package com.example.alarm;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout calenderView;
    private LinearLayout mainView;

    public MainActivity() {
        super(R.layout.activity_main);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calenderView = findViewById(R.id.calender);
        mainView = findViewById(R.id.main);
        calenderView.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);

        findViewById(R.id.tab1).setOnClickListener(this);
        findViewById(R.id.tab2).setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        calenderView.setVisibility(View.GONE);
        mainView.setVisibility(View.GONE);

        if (view.getId() == R.id.tab1) calenderView.setVisibility(View.VISIBLE);
        else if (view.getId() == R.id.tab2) mainView.setVisibility(View.VISIBLE);
    }

}