package pl.sebcel.do_szkoly.jse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

public class Main {

    private String timeFormatString = "H:mm";
    private DateTimeFormatter tf = DateTimeFormatter.ofPattern(timeFormatString);
    private MainFrame mainFrame = new MainFrame();

    public static void main(String[] args) {
        System.out.println("Starting Do Szkoły application");
        new Main().run();
    }

    public void run() {
        TreeMap<LocalTime, String> schedule = new TreeMap<>();
        schedule.put(dateFromString("7:32"), "Ubieranie się");
        schedule.put(dateFromString("8:30"), "Wychodzenie z domu");
        schedule.put(dateFromString("8:43"), "Autobus 268");
        schedule.put(dateFromString("9:00"), "W szkole");

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
            mainFrame.displayTimeInformation(timeInformation);

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