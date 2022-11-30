package com.example.alarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences spref ;
    SharedPreferences.Editor editor;

    PreferenceScreen sound;
    PreferenceScreen simple;
    PreferenceScreen language;
    PreferenceScreen display;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }



}
