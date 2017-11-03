package pl.sebcel.do_szkoly.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pl.sebcel.do_szkoly.engine.Step;
import pl.sebcel.do_szkoly.engine.TimeInformation;

public class DisplaySchedule extends AppCompatActivity {

    private DateFormat df = new SimpleDateFormat("H:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_schedule);
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
    }

    private void handleTimeInformation(final TimeInformation timeInformation) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView completedStepsView = (findViewById(R.id.completedSteps));
                TextView currentStepView = (findViewById(R.id.currentStep));
                TextView outstandingStepsView = (findViewById(R.id.outstandingSteps));

                String completedStepsText = "";
                String currentStepText = "";
                String outstandingStepsText = "";

                for (Step step : timeInformation.getCompletedSteps()) {
                    if (completedStepsText.length() > 0) {
                        completedStepsText += "\n";
                    }
                    completedStepsText += stepToString(step);
                }

                if (timeInformation.getCurrentStep() != null) {
                    currentStepText = "Teraz: " + timeInformation.getCurrentStep().getDescription() + "\n";
                }

                if (timeInformation.getNextStep() != null) {
                    currentStepText += "Jeszcze " + timeInformation.getTimeToNextStepInMinutes() + " min.";
                }

                for (Step step : timeInformation.getOutstandingSteps()) {
                    if (outstandingStepsText.length() > 0) {
                        outstandingStepsText += "\n";
                    }
                    outstandingStepsText += stepToString(step);
                }

                completedStepsView.setText(completedStepsText);
                currentStepView.setText(currentStepText);
                outstandingStepsView.setText(outstandingStepsText);
            }
        });
    }

    private String stepToString(Step step) {
        return df.format(step.getStartTime()) + " " + step.getDescription();
    }
}
