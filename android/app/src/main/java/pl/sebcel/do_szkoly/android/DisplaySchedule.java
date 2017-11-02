package pl.sebcel.do_szkoly.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import pl.sebcel.do_szkoly.engine.Engine;
import pl.sebcel.do_szkoly.engine.EventListener;
import pl.sebcel.do_szkoly.engine.TimeInformation;

public class DisplaySchedule extends AppCompatActivity {

    private Engine engine = new Engine();
    private DateFormat df = new SimpleDateFormat("H:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        engine = new Engine();
        engine.addEvent("9:00", "Ubieranie się");
        engine.addEvent("9:30", "Wychodzenie z domu");
        engine.addEvent("9:43", "Autobus 268");
        engine.addEvent("10:00", "W szkole");

        engine.addEventListener(new EventListener() {
            @Override
            public void handleTimeEvent(final TimeInformation timeInformation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TextView completedStepsView = ((TextView) findViewById(R.id.completedSteps));
                        TextView currentStepView = ((TextView) findViewById(R.id.currentStep));
                        TextView outstandingStepsView = ((TextView) findViewById(R.id.outstandingSteps));

                        String completedStepsText = "";
                        for (Map.Entry<Date, String> step : timeInformation.getOutstandingEvents().entrySet()) {
                            completedStepsText += stepToString(step) + "\n";
                        }

                        completedStepsView.setText(completedStepsText);
                    }
                });
            }
        });

        engine.start();
    }

    private String stepToString(Map.Entry<Date, String> step) {
        return df.format(step.getKey()) + "\t" + step.getValue();
    }
}
