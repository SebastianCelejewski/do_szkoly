package pl.sebcel.do_szkoly.jse;

import java.time.Duration;
import java.time.LocalTime;
import java.util.TreeMap;
import java.util.TreeSet;

public class TimeInformation {

    private LocalTime currentTime;
    private LocalTime nextEventTime;
    private String nextEvent;
    private TreeMap<LocalTime, String> outstandingEvents;

    public TimeInformation(LocalTime currentTime, LocalTime nextEventTime, String nextEvent, TreeMap<LocalTime, String> outstandingEvents) {
        this.currentTime = currentTime;
        this.nextEventTime = nextEventTime;
        this.nextEvent = nextEvent;
        this.outstandingEvents = outstandingEvents;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public LocalTime getNextEventTime() {
        return nextEventTime;
    }

    public String getNextEvent() {
        return nextEvent;
    }

    public TreeMap<LocalTime, String> getOutstandingEvents() {
        return outstandingEvents;
    }

    public long getTimeToNextStep() {
        return Duration.between(currentTime, nextEventTime).toMinutes() + 1;
    }
}