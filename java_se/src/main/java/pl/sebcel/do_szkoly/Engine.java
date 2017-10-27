package pl.sebcel.do_szkoly;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class Engine {

    private String timeFormatString = "H:mm";
    private DateTimeFormatter tf = DateTimeFormatter.ofPattern(timeFormatString);
    private Set<EventListener> listeners = new HashSet<>();

    private TreeMap<LocalTime, String> schedule = new TreeMap<>();

    public void addEvent(String time, String description) {
        if (time == null || time.trim().equals("")) {
            throw new RuntimeException("Time cannot be null or empty");
        }
        schedule.put(LocalTime.parse(time, tf), description);
    }

    public void addEventListener(EventListener listener) {
        listeners.add(listener);
    }

    public void start() {
        Thread worker = new Thread(() -> workerMethod());
        worker.start();
    }

    private void workerMethod() {
        while (true) {
            LocalTime currentTime = LocalTime.now();
            LocalTime nextEventTime = null;
            String nextEvent = "";

            for (LocalTime date : schedule.keySet()) {
                if (date.isAfter(currentTime)) {
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
}