package pl.sebcel.do_szkoly.android;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import pl.sebcel.do_szkoly.engine.Engine;
import pl.sebcel.do_szkoly.engine.EventListener;
import pl.sebcel.do_szkoly.engine.TimeInformation;

public class ScheduleService extends IntentService implements EventListener{

    public static final String HANDLE_TIME_UPDATE_ACTION = "pl.sebcel.do_szkoly.HANDLE_TIME_UPDATE";
    public static final String HANDLE_SCHEDULE_CHANGED_ACTION = "pl.sebcel.do_szkoly.HANDLE_SCHEDULE_CHANGED";
    public static final String TIME_UPDATE_DATA = "pl.sebcel.do_szkoly.TIME_UPDATE_DATA";

    public ScheduleService() {
        super("ScheduleService");
    }

    public ScheduleService(String name) {
        super(name);
    }

    private Engine engine;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        subscribeToScheduleServiceNotifications();
        configureEngine();
    }

    private void configureEngine() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String targetTime = sharedPreferences.getString("pref_target_time", "");

        if (engine != null) {
            engine.removeEventListener(this);
        }

        engine = new Engine();
        addSteps(engine, targetTime);

        engine.addEventListener(this);
        engine.start(10);
    }

    @Override
    public void handleTimeEvent(final TimeInformation timeInformation) {
        Intent updateTime = new Intent(HANDLE_TIME_UPDATE_ACTION);
        updateTime.putExtra(TIME_UPDATE_DATA, timeInformation);
        LocalBroadcastManager.getInstance(ScheduleService.this).sendBroadcast(updateTime);
    }


    private void subscribeToScheduleServiceNotifications() {
        IntentFilter scheduleChangedFilter = new IntentFilter(ScheduleService.HANDLE_SCHEDULE_CHANGED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                configureEngine();
            }
        }, scheduleChangedFilter);
    }

    private void addSteps(Engine engine, String targetTime) {
        System.out.println("AddSteps: " + targetTime);
        if (targetTime.equals("8_10")) {
            engine.addStep("7:15", "Szykowanie się");
            engine.addStep("7:30", "Ubieranie się");
            engine.addStep("7:45", "Wychodzenie z domu");
            engine.addStep("8:00", "Autobus 168");
            engine.addStep("8:10", "W szkole");
        }

        if (targetTime.equals("9_05")) {
            engine.addStep("8:00", "Szykowanie się");
            engine.addStep("8:15", "Ubieranie się");
            engine.addStep("8:30", "Wychodzenie z domu");
            engine.addStep("8:43", "Autobus 268");
            engine.addStep("9:05", "W szkole");
        }

        if (targetTime.equals("10_00")) {
            engine.addStep("9:00", "Szykowanie się");
            engine.addStep("9:15", "Ubieranie się");
            engine.addStep("9:30", "Wychodzenie z domu");
            engine.addStep("9:48", "Autobus 268");
            engine.addStep("10:00", "W szkole");
        }
    }
}