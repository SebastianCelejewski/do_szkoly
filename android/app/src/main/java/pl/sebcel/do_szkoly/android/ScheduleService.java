package pl.sebcel.do_szkoly.android;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import pl.sebcel.do_szkoly.engine.Engine;
import pl.sebcel.do_szkoly.engine.EventListener;
import pl.sebcel.do_szkoly.engine.TimeInformation;

public class ScheduleService extends IntentService {

    public static final String HANDLE_TIME_UPDATE_ACTION = "pl.sebcel.do_szkoly.HANDLE_TIME_UPDATE";
    public static final String TIME_UPDATE_DATA = "pl.sebcel.do_szkoly.TIME_UPDATE_DATA";

    public ScheduleService() {
        super("ScheduleService");
    }

    public ScheduleService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Engine engine = new Engine();
        engine.addStep("14:00", "Ubieranie się");
        engine.addStep("14:30", "Wychodzenie z domu");
        engine.addStep("14:43", "Autobus 268");
        engine.addStep("14:00", "W szkole");

        engine.addEventListener(new EventListener() {
            @Override
            public void handleTimeEvent(final TimeInformation timeInformation) {
                Intent updateTime = new Intent(HANDLE_TIME_UPDATE_ACTION);
                updateTime.putExtra(TIME_UPDATE_DATA, timeInformation);
                LocalBroadcastManager.getInstance(ScheduleService.this).sendBroadcast(updateTime);
            }
        });

        engine.start(10);
    }
}