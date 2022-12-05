package com.example.alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Calendar;


public class SettingsActivity extends AppCompatActivity
        implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    //액티비티 생성하기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.settings_fragment, new SettingsFragment())
                .commit();

    }





        @Override
        public boolean onPreferenceStartFragment (PreferenceFragmentCompat caller, Preference pref){
            // 새 fragment를 인스턴스화
            final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                    getClassLoader(), pref.getFragment());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.settings_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }


    }





