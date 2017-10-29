package pl.sebcel.do_szkoly.engine;

import java.util.Date;
import java.util.TreeMap;

public class TimeCalculator {

    public TimeInformation calculateTimeInformation(TreeMap<Date, String> schedule, Date currentTime) {
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

        return new TimeInformation(currentTime, nextEventTime, nextEvent, schedule);
    }
}