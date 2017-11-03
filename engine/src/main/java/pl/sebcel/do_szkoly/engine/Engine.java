package pl.sebcel.do_szkoly.engine;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.util.TreeMap;

public class Engine {

    private TimeUtils timeUtils = new TimeUtils();
    private TimeCalculator timeCalculator = new TimeCalculator();
    private Set<EventListener> listeners = new HashSet<>();

    private TreeMap<Date, String> schedule = new TreeMap<>();

    public void addStep(String time, String description) {
        if (time == null || time.trim().equals("")) {
            throw new RuntimeException("Time cannot be null or empty");
        }
        schedule.put(timeUtils.parseTimeString(time), description);
    }

    public void addEventListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void start(int delayInSeconds) {
        if (delayInSeconds < 1) {
            delayInSeconds = 1;
        }

        final int delayInMillis = delayInSeconds * 1000;

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    TimeInformation timeInformation = timeCalculator.calculateTimeInformation(schedule, timeUtils.getCurrentTime());
                    notifyListeners(timeInformation);

                    try {
                        Thread.sleep(delayInMillis);
                    } catch (Exception ex) {
                        // intentional
                    }
                }
            }
        });
        worker.start();
    }

    private void notifyListeners(TimeInformation timeInformation) {
        for (EventListener listener : listeners) {
            listener.handleTimeEvent(timeInformation);
        }
    }
}