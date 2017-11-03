package pl.sebcel.do_szkoly.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_time);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IntentFilter timeUpdateFilter = new IntentFilter(ScheduleService.HANDLE_TIME_UPDATE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TimeInformation timeInformation = (TimeInformation) intent.getSerializableExtra(ScheduleService.TIME_UPDATE_DATA);
                handleTimeInformation(timeInformation);
            }
        }, timeUpdateFilter);

        Intent scheduleServiceIntent = new Intent(this, ScheduleService.class);
        startService(scheduleServiceIntent);
    }

    private void handleTimeInformation(final TimeInformation timeInformation) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.currentTimeInfo)).setText(df.format(timeInformation.getCurrentTime()));
                if (timeInformation.getCurrentStep() != null) {
                    ((TextView) findViewById(R.id.currentEventInfo)).setText(timeInformation.getCurrentStep().getDescription());
                } else {
                    ((TextView) findViewById(R.id.currentEventInfo)).setText("-");
                }

                if (timeInformation.getNextStep() != null) {
                    ((TextView) findViewById(R.id.nextEventTime)).setText(df.format(timeInformation.getNextStep().getStartTime()));
                    ((TextView) findViewById(R.id.nextEventInfo)).setText(timeInformation.getNextStep().getDescription());
                    ((TextView) findViewById(R.id.timeToNextEventInfo)).setText(getString(R.string.time_in_minutes, timeInformation.getTimeToNextStepInMinutes()));
                    ((TextView) findViewById(R.id.timeToNextEventInfo)).setTextColor(getTimeToNextEventColour(timeInformation.getTimeToNextStepInMinutes()));
                } else {
                    ((TextView) findViewById(R.id.nextEventInfo)).setText("-");
                    ((TextView) findViewById(R.id.timeToNextEventInfo)).setText("-");
                }
            }
        });
    }

    public void onShowScheduleClick() {
        Intent intent = new Intent(this, DisplaySchedule.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_show_schedule) {
            onShowScheduleClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
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