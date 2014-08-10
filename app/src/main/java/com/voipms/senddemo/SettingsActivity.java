package com.voipms.senddemo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by scott-androiddevelopment on 2014-08-10.
 */
public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
