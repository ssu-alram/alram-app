package com.example.alarm;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.firestore.core.ComponentProvider;

import java.sql.Time;

public class setting_push_time extends AppCompatActivity implements View.OnClickListener {

    int sethour;
    int setmin;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_push_time);

        Button back = (Button) findViewById(R.id.repeat_alarm_backbutton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        FragmentManager fragmentManager;


        TimePicker timePicker = findViewById(R.id.push_alarm_time_picker);
        if(savedInstanceState!=null){
        timePicker.setHour(savedInstanceState.getInt("sethour"));
        timePicker.setMinute(savedInstanceState.getInt("setmin"));}

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                sethour = hour;
                setmin = min;

            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt("sethour", sethour);
        outState.putInt("setmin",setmin);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        if(savedInstanceState != null){
            sethour = savedInstanceState.getInt("sethour");
            setmin = savedInstanceState.getInt("setmin");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    public void onClick(View view) {

    }
}


