package pl.sebcel.do_szkoly.engine;

import java.util.Date;
import java.util.TreeMap;

public class TimeInformation {

    private Date currentTime;
    private Date nextEventTime;
    private String nextEvent;
    private TreeMap<Date, String> outstandingEvents;

    public TimeInformation(Date currentTime, Date nextEventTime, String nextEvent, TreeMap<Date, String> outstandingEvents) {
        this.currentTime = currentTime;
        this.nextEventTime = nextEventTime;
        this.nextEvent = nextEvent;
        this.outstandingEvents = outstandingEvents;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public Date getNextEventTime() {
        return nextEventTime;
    }

    public String getNextEvent() {
        return nextEvent;
    }

    public TreeMap<Date, String> getOutstandingEvents() {
        return outstandingEvents;
    }

    public long getTimeToNextStepInMinutes() {
        return (nextEventTime.getTime() -  currentTime.getTime()) / 1000 / 60 + 1;
    }
}