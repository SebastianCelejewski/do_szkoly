package pl.sebcel.do_szkoly.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pl.sebcel.do_szkoly.engine.TimeInformation;

public class DisplayTime extends AppCompatActivity {

    private DateFormat df = new SimpleDateFormat("H:mm");

    private TextView currentTimeInfo;
    private TextView currentStepInfo;
    private TextView nextStepTime;
    private TextView nextStepInfo;
    private TextView timeToNextStepInfo;

    private TimeInformation mostRecentTimeInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeGUI();
        subscribeToScheduleServiceNotifications();
        createScheduleService();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String s = sharedPreferences.getString("pref_target_time", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_show_schedule) {
            onShowScheduleClick();
            return true;
        }

        if (item.getItemId() == R.id.action_show_settings) {
            onShowSettingsClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeGUI() {
        setContentView(R.layout.activity_display_time);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentTimeInfo = findViewById(R.id.currentTimeInfo);
        currentStepInfo = findViewById(R.id.currentStepInfo);
        nextStepTime = findViewById(R.id.nextStepTime);
        nextStepInfo = findViewById(R.id.nextStepInfo);
        timeToNextStepInfo = findViewById(R.id.timeToNextStepInfo);
    }

    private void subscribeToScheduleServiceNotifications() {
        IntentFilter timeUpdateFilter = new IntentFilter(ScheduleService.HANDLE_TIME_UPDATE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TimeInformation timeInformation = (TimeInformation) intent.getSerializableExtra(ScheduleService.TIME_UPDATE_DATA);
                handleTimeInformation(timeInformation);
            }
        }, timeUpdateFilter);
    }

    private void createScheduleService() {
        Intent scheduleServiceIntent = new Intent(this, ScheduleService.class);
        startService(scheduleServiceIntent);
    }

    private void handleTimeInformation(final TimeInformation timeInformation) {
        this.mostRecentTimeInformation = timeInformation;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentTimeInfo.setText(df.format(timeInformation.getCurrentTime()));

                if (timeInformation.getCurrentStep() != null) {
                    currentStepInfo.setText(timeInformation.getCurrentStep().getDescription());
                } else {
                    currentStepInfo.setText("-");
                }

                if (timeInformation.getNextStep() != null) {
                    nextStepTime.setText(df.format(timeInformation.getNextStep().getStartTime()));
                    nextStepInfo.setText(timeInformation.getNextStep().getDescription());
                    timeToNextStepInfo.setText(getString(R.string.time_in_minutes, timeInformation.getTimeToNextStepInMinutes()));
                    timeToNextStepInfo.setTextColor(getTimeToNextEventColour(timeInformation.getTimeToNextStepInMinutes()));
                } else {
                    nextStepInfo.setText("-");
                    timeToNextStepInfo.setText("-");
                }
            }
        });
    }

    private void onShowScheduleClick() {
        Intent intent = new Intent(this, DisplaySchedule.class);
        intent.putExtra(ScheduleService.TIME_UPDATE_DATA, mostRecentTimeInformation);
        startActivity(intent);
    }

    private void onShowSettingsClick() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private int getTimeToNextEventColour(long timeToNextEvent) {
        if (timeToNextEvent < 2) {
            return Color.RED;
        }

        if (timeToNextEvent < 5) {
            return Color.YELLOW;
        }

        return Color.GREEN;
    }
}