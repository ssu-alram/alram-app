package com.example.alarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.google.android.material.shadow.ShadowRenderer;
public class SettingsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.settings_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.settings_fragment, new SettingsFragment())
                .commit();
    }

}

