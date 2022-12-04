package com.example.alarm;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;


public class SettingsFragment extends PreferenceFragmentCompat {

    //설정값 저장
    SharedPreferences SPref;

    private Preference Sound;
    private Preference Default;
    private Preference Language;
    private Preference Display;


    //프리퍼런스 생성하기
    //https://lcw126.tistory.com/111
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey) {
        setPreferencesFromResource(R.xml.main_preferences, rootkey);

        if(rootkey != null) return;

        Sound = (Preference) findPreference("sound_list");
        Default = (Preference) findPreference("default");
        Language = (Preference) findPreference("language_list");
        Display = (Preference) findPreference("display_list");

        SPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //선택되어있는 값 저장하기
        if (!SPref.getString("sound_list", "").equals(""))
            Sound.setSummary(SPref.getString("sound_list", "모닝콜"));
        if (!SPref.getString("language_list", "").equals(""))
            Language.setSummary(SPref.getString("language_list", "한국어"));
        if (!SPref.getString("display_list", "").equals(""))
            Display.setSummary(SPref.getString("display_list", "자동모드"));

        SPref.registerOnSharedPreferenceChangeListener(prefListener);

    }
    //설정 바뀌었을 때
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sPrefs, String key) {
            if (key.equals("sound_list"))
                Sound.setSummary(sPrefs.getString("sound_list", "카톡"));

        }
    };

}


