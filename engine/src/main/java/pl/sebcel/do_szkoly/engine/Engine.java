package pl.sebcel.do_szkoly.engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Engine {

    private String timeFormatString = "H:mm";
    private DateFormat tf = new SimpleDateFormat(timeFormatString);
    private Set<EventListener> listeners = new HashSet<>();

    private TreeMap<Date, String> schedule = new TreeMap<>();

    public void addEvent(String time, String description) {
        if (time == null || time.trim().equals("")) {
            throw new RuntimeException("Time cannot be null or empty");
        }
        schedule.put(parseTimeString(time), description);
    }

    public void addEventListener(EventListener listener) {
        listeners.add(listener);
    }

    public void start() {
        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                workerMethod();
            }
        });
        worker.start();
    }

    private void workerMethod() {
        while (true) {
            Date currentTime = getCurrentTime();
            Date nextEventTime = null;

            String nextEvent = "";

            for (Date date : schedule.keySet()) {
                if (date.after(currentTime)) {
                    if (nextEventTime == null) {
                        nextEventTime = date;
                        nextEvent = schedule.get(date);
                    }
                }
            }

            TimeInformation timeInformation = new TimeInformation(currentTime, nextEventTime, nextEvent, schedule);
            for (EventListener listener : listeners) {
                listener.handleTimeEvent(timeInformation);
            }

            try {
                Thread.sleep(10000);
            } catch (Exception ex) {
                // intentional
            }
        }
    }

    private Date parseTimeString(String timeStr) {
        try {
            Calendar currentTime = Calendar.getInstance();
            Calendar targetTime = Calendar.getInstance();
            targetTime.setTime(tf.parse(timeStr));

            targetTime.set(Calendar.YEAR, currentTime.get(Calendar.YEAR));
            targetTime.set(Calendar.MONTH, currentTime.get(Calendar.MONTH));
            targetTime.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH));

            return targetTime.getTime();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse '"+timeStr+"' using pattern '"+timeFormatString+"': "+ex.getMessage(), ex);
        }
    }

    private Date getCurrentTime() {
        return new Date();
    }
}