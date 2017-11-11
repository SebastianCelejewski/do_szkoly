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

    public Engine() {
        System.out.println("[" + this.toString() + "] Creating Engine instance");
    }

    public void addStep(String time, String description) {
        if (time == null || time.trim().equals("")) {
            throw new RuntimeException("Time cannot be null or empty");
        }
        schedule.put(timeUtils.parseTimeString(time), description);
    }

    public void addEventListener(EventListener listener) {
        System.out.println("[" + this.toString() + "] Adding listener " + listener.toString());
        listeners.add(listener);
    }

    public void removeEventListener(EventListener listener) {
        System.out.println("[" + this.toString() + "] Removing listener " + listener.toString());
        listeners.remove(listener);
    }

    public void start(int delayInSeconds) {
        System.out.println("[" + this.toString() + "] Starting Engine");
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
            System.out.println("[" + this.toString() + "] Tick to " + listener.toString());
            listener.handleTimeEvent(timeInformation);
        }
    }
}