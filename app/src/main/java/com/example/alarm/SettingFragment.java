package com.example.alarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingFragment extends PreferenceFragmentCompat {
    SharedPreferences prefs;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    // key값에 해당하는 명령 넣기
                    if (key.equals("soundset")) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals("vibrate")) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals("sound_list")) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals("defaultset")) {
                        Log.d("TAG", key + "SELECTED");
                    } else if (key.equals("push_alarm")) {
                        Log.d("TAG", key + "SELECTED");
                    }

                }
            };

}

