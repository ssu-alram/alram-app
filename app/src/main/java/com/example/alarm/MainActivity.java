package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity{
    Fragment fragment_main, fragment_calender;

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fm.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프래그먼트 객체 선언
        fragment_main = new FragmentMain();
        fragment_calender = new FragmentCalender();

        fragmentTransaction.replace(R.id.fragment_main, new FragmentMain()).commit();

        LinearLayout tabView = findViewById(R.id.tabView);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tab1:
                        fm.beginTransaction().replace(R.id.fragment_calender, fragment_calender).commitAllowingStateLoss();
                        break;
                    case R.id.tab2:
                        fm.beginTransaction().replace(R.id.fragment_main, fragment_main).commitAllowingStateLoss();
                        break;
                }
            }
        });
    }
}