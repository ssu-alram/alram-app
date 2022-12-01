package com.example.alarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences spref ;
    SharedPreferences.Editor editor;

    //
    SharedPreferences.OnSharedPreferenceChangeListener listner = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("진동")){
                boolean b = spref.getBoolean("진동",false);
                Toast.makeText(getActivity(), "진동 알림 :" +b, Toast.LENGTH_SHORT).show();
            }
        }
    };

    PreferenceScreen sound;
    PreferenceScreen simple;
    PreferenceScreen language;
    PreferenceScreen display;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.root_preferences);
// https://lcw126.tistory.com/111
        spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume(){
        super.onResume();

        spref.registerOnSharedPreferenceChangeListener(listner);

    }
    @Override
    public void onPause(){
        super.onPause();
        spref.unregisterOnSharedPreferenceChangeListener(listner);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        ///setPreferencesFromResource(R.xml.root_preferences,rootKey);

    }



}
