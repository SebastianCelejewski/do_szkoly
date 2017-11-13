package pl.sebcel.do_szkoly.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

public class SettingsActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String PREFERENCES_TARGET_TIME = "pref_target_time";

    private SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, settingsFragment)
                .commit();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTargetTimeSettingDescription();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREFERENCES_TARGET_TIME)) {
            Intent scheduleChanged = new Intent(ScheduleService.HANDLE_SCHEDULE_CHANGED_ACTION);
            LocalBroadcastManager.getInstance(SettingsActivity.this).sendBroadcast(scheduleChanged);

            updateTargetTimeSettingDescription();
        }
    }

    private void updateTargetTimeSettingDescription() {
        Preference targetTimePreference = settingsFragment.findPreference(PREFERENCES_TARGET_TIME);
        targetTimePreference.setSummary(((ListPreference) targetTimePreference).getEntry());
    }
}