package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class setting_default_time extends AppCompatActivity implements View.OnClickListener{


    int setdefaulthour;
    int setdefaultmin;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_default_time);

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

        TimePicker timePicker = findViewById(R.id.repeat_alarm_time_picker);
        if(savedInstanceState!=null){
            timePicker.setHour(savedInstanceState.getInt("setdefaulthour"));
            timePicker.setMinute(savedInstanceState.getInt("setdefaultmin"));}

        else{timePicker.setHour(sf.getInt("setdefaultHour",setdefaulthour));
            timePicker.setMinute(sf.getInt("setdefaultMin",setdefaultmin));

        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                setdefaulthour = hour;
                setdefaultmin = min;
                editor.putInt("setdefaultHour",setdefaulthour);
                editor.putInt("setdefaultMin",setdefaultmin);
                editor.commit();

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt("setdefaultHour", setdefaulthour);
        outState.putInt("setdefaultMin",setdefaultmin);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        if(savedInstanceState != null){
            setdefaulthour = savedInstanceState.getInt("setdefaulthour");
            setdefaultmin = savedInstanceState.getInt("setdefaultmin");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }


}