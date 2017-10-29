package pl.sebcel.do_szkoly.android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pl.sebcel.do_szkoly.engine.Engine;
import pl.sebcel.do_szkoly.engine.EventListener;
import pl.sebcel.do_szkoly.engine.TimeInformation;

public class DisplayTime extends AppCompatActivity {

    private Engine engine;
    private DateFormat df = new SimpleDateFormat("H:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        engine = new Engine();
        engine.addEvent("10:00", "Ubieranie się");
        engine.addEvent("10:30", "Wychodzenie z domu");
        engine.addEvent("10:43", "Autobus 268");
        engine.addEvent("11:00", "W szkole");

        engine.addEventListener(new EventListener() {
            @Override
            public void handleTimeEvent(final TimeInformation timeInformation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.currentTimeInfo)).setText(df.format(timeInformation.getCurrentTime()));
                        if (timeInformation.getNextEventTime() != null) {
                            ((TextView) findViewById(R.id.nextEventTime)).setText(df.format(timeInformation.getNextEventTime()));
                            ((TextView) findViewById(R.id.nextEventInfo)).setText(timeInformation.getNextEvent());
                            ((TextView) findViewById(R.id.timeToNextEventInfo)).setText("" + timeInformation.getTimeToNextStepInMinutes() + " min");
                            ((TextView) findViewById(R.id.timeToNextEventInfo)).setTextColor(getTimeToNextEventColour(timeInformation.getTimeToNextStepInMinutes()));
                        } else {
                            ((TextView) findViewById(R.id.nextEventInfo)).setText("-");
                            ((TextView) findViewById(R.id.timeToNextEventInfo)).setText("-");
                        }
                    }
                });
            }
        });

        engine.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
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