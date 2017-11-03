package pl.sebcel.do_szkoly.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

public class SettingsActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String PREFERENCES_TARGET_TIME = "pref_target_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREFERENCES_TARGET_TIME)) {
            System.out.println("Broadcasting that schedule had changed");
            Intent scheduleChanged = new Intent(ScheduleService.HANDLE_SCHEDULE_CHANGED_ACTION);
            LocalBroadcastManager.getInstance(SettingsActivity.this).sendBroadcast(scheduleChanged);
        }
    }
}