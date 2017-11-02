package pl.sebcel.do_szkoly.engine;

import java.util.Date;
import java.util.TreeMap;

public class TimeCalculator {

    public TimeInformation calculateTimeInformation(TreeMap<Date, String> schedule, Date currentTime) {
        Date nextEventTime = null;
        Date currentEventTime = null;
        String nextEvent = "";
        String currentEvent = "";

        System.out.println(currentTime);

        TreeMap<Date, String> completedSteps = new TreeMap<>();
        TreeMap<Date, String> outstandingSteps = new TreeMap<>();

        /*
        for (Date date : schedule.keySet()) {
            String step = schedule.get(date);
            if (date.after(currentTime)) {
                if (nextEventTime == null) {
                    nextEventTime = date;
                    nextEvent = step;
                }

                outstandingSteps.put(date, step);
            } else {
                completedSteps.put(date, step);
                currentEvent = step;
                currentEventTime = date;
            }
        }

        */

        for (Date date : schedule.keySet()) {
            String step = schedule.get(date);

            if (date.before(currentTime)) {
                completedSteps.put(date, step);
            } else {
                if (currentEventTime == null) {
                    currentEventTime = date;
                    currentEvent = step;
                } else {
                    if (nextEventTime == null) {
                        nextEventTime = date;
                        nextEvent = step;
                    }
                    outstandingSteps.put(date, step);
                }
            }
        }

        return new TimeInformation(currentTime, currentEvent, nextEventTime, nextEvent, completedSteps, outstandingSteps);
    }
}