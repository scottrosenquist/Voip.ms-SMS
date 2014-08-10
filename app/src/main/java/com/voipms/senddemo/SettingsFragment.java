package com.voipms.senddemo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by scott-androiddevelopment on 2014-08-10.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
