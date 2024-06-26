package com.example.alarm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

        SharedPreferences sf = getSharedPreferences("isfirst", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();

        TimePicker timePicker = findViewById(R.id.init_push_alarm_time_picker);
        if(savedInstanceState!=null){
            timePicker.setHour(savedInstanceState.getInt("sethour"));
            timePicker.setMinute(savedInstanceState.getInt("setmin"));}

        else{timePicker.setHour(sf.getInt("setHour",sethour));
            timePicker.setMinute(sf.getInt("setMin",setmin));

        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                sethour = hour;
                setmin = min;
                editor.putInt("setHour",sethour);
                editor.putInt("setMin",setmin);
                editor.commit();

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt("setHour", sethour);
        outState.putInt("setMin",setmin);

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

