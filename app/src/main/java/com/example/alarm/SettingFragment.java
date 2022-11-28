package com.example.alarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

public class SettingFragment extends PreferenceFragment {

    SharedPreferences pref;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.root_preferences);
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences.OnSharedPreferenceChangeListener listner;
        pref.registerOnSharedPreferenceChangeListener(listner);
    }
    @Override
    public void onPause(){
        super.onPause();
        pref.unregisterOnSharedPreferenceChangeListener(listner);
    }
}

