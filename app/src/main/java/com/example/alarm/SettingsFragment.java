package com.example.alarm;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;


import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.app.DialogFragment;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import org.checkerframework.checker.units.qual.C;

import java.util.Calendar;


public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    //설정값 저장
    SharedPreferences SPref;
    SharedPreferences.Editor editor;

    private Preference Sound;
    private Preference Default;
    private Preference Language;
    private Preference Display;

    private SwitchPreference Vibrate;
    private SwitchPreference PushAlarm;
    private SwitchPreference DefaultAlarm;


    //프리퍼런스 생성하기
    //https://lcw126.tistory.com/111
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey) {
        setPreferencesFromResource(R.xml.main_preferences, rootkey);

        ///프리퍼런스 만들기
        Sound = (ListPreference) findPreference("sound_list");
        Default = (Preference) findPreference("default");
        Language = (ListPreference) findPreference("language_list");
        Display = (ListPreference) findPreference("display_list");

        Vibrate = (SwitchPreference) findPreference("vibrate");
        PushAlarm = (SwitchPreference) findPreference("pushAlarm");
        DefaultAlarm = (SwitchPreference) findPreference("defaultAlarm");

        ///Shared Preference 불러오기
        SPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = SPref.edit();

        ///프리퍼런스 값 불러오기
        boolean isVibrateOn = SPref.getBoolean("vibrate", true);
        boolean isPushAlarmOn = SPref.getBoolean("pushAlarm", false);
        boolean isDefaultAlarmOn = SPref.getBoolean("defaultAlarm", false);

        String sound_0 = SPref.getString("sound_list", "모닝콜");
        String language_0 = SPref.getString("language_list", "한국어");
        String display_0 = SPref.getString("display_list", "자동모드");


        SPref.registerOnSharedPreferenceChangeListener(prefListener);


        //선택되어있는 값이 있다면 summary로 나타내기기
        if (!SPref.getString("sound_list", "").equals("")) {
            Sound.setSummary(SPref.getString("sound_list", "모닝콜"));
        }
        if (!SPref.getString("language_list", "").equals("")) {
            Language.setSummary(SPref.getString("language_list", "한국어"));
        }

        if (!SPref.getString("display_list", "").equals("")) {
            Display.setSummary(SPref.getString("display_list", "자동모드"));
        }


    }

    //설정값 리스너 등록
    @Override
    public void onResume() {
        super.onResume();
        SPref.registerOnSharedPreferenceChangeListener(prefListener);
    }

    //설정값 리스너 해제
    @Override
    public void onPause() {
        super.onPause();
        SPref.unregisterOnSharedPreferenceChangeListener(prefListener);
    }



    //설정 바뀌었을 때
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sPrefs, String key) {
            if (key.equals("sound_list")) {
                Sound.setSummary(SPref.getString("sound_list", "모닝콜"));
                editor.putString("선택된벨소리", SPref.getString("sound_list", "모닝콜"));
                editor.commit();
            }
            else if (key.equals("language_list")) {
                Language.setSummary(SPref.getString("language_list", "한국어"));
                editor.putString("선택된언어", SPref.getString("language_list", "한국어"));
                editor.commit();
            }
            else if (key.equals("display_list")) {
                Display.setSummary(SPref.getString("display_list", "자동모드"));
                editor.putString("선택된모드", SPref.getString("display_list", "자동모드"));
                editor.commit();
            }

        }

    };

    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (preference.getKey().equals("setting_push_alarm")) {

        }
        return true;


}
}

