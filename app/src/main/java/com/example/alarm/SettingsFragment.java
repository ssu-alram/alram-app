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

    //데이터 저장?
    SharedPreferences spref ;
    SharedPreferences.Editor editor;

    //프리퍼런스를 전역변수 선언
    PreferenceScreen sound;
    PreferenceScreen simple;
    PreferenceScreen language;
    PreferenceScreen display;

    //프리퍼런스 생성하기
    //https://lcw126.tistory.com/111
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreate(savedInstanceState);
        //XML가져와서 생성하겠다
        addPreferencesFromResource(R.xml.root_preferences);
        //이건 뭐지?spref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(rootKey==null){
            sound = findPreference("sound");


        }
    }

    //프리퍼런스 클릭리스너 역할
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference){
        String key = preferenceScreen.getKey();
        if(key.equals("sound")){

        }

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



}
