package pl.sebcel.do_szkoly.jse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class Main {

    private String timeFormatString = "H:mm";
    private DateTimeFormatter tf = DateTimeFormatter.ofPattern(timeFormatString);

    public static void main(String[] args) {
        System.out.println("Starting Do Szkoły application");
        new Main().run();
    }

    public void run() {
        TreeMap<LocalTime, String> schedule = new TreeMap<>();
        schedule.put(dateFromString("8git:00"), "Ubieranie się");
        schedule.put(dateFromString("8:30"), "Wychodzenie z domu");
        schedule.put(dateFromString("8:43"), "Autobus 268");
        schedule.put(dateFromString("9:00"), "W szkole");

        while (true) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n");
            LocalTime currentTime = LocalTime.now();

            LocalTime nextEventTime = null;
            String nextEvent = "";

            for (LocalTime date : schedule.keySet()) {
                if (date.isBefore(currentTime)) {
                    System.out.println("  " + tf.format(date) + " " + schedule.get(date));
                }

                if (date.isAfter(currentTime)) {
                    System.out.println("> " + tf.format(date) + " " + schedule.get(date));
                    if (nextEventTime == null) {
                        nextEventTime = date;
                        nextEvent = schedule.get(date);
                    }
                }
            }


            Duration timeToNextEvent = Duration.between(currentTime, nextEventTime);

            System.out.println("");
            System.out.println("Teraz jest " + tf.format(currentTime));
            System.out.println("Następny krok musicie wykonać za " + timeToNextEvent.toMinutes() + " minut, czyli o godzinie " + tf.format(nextEventTime) + ". Będzie to " + nextEvent + ".");

            try {
                Thread.sleep(10000);
            } catch (Exception ex) {
                // intentional
            }
        }


    }

    private LocalTime dateFromString(String dateString) {
        try {
            return LocalTime.parse(dateString, tf);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse '"+dateString+"' into a time using time format " + timeFormatString +": " + ex.getMessage(), ex);
        }
    }
}