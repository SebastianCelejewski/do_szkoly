package pl.sebcel.do_szkoly.jse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import pl.sebcel.do_szkoly.engine.*;

public class MainFrame extends JFrame {

    private final static Font LABEL_FONT = new Font("Arial", Font.PLAIN, 24);
    private final static Font INFO_FONT = new Font("Arial", Font.PLAIN, 96);
    private final static Font SCHEDULE_FONT = new Font("Arial", Font.PLAIN, 48);

    private JLabel currentTimeLabel = new JLabel("Aktualny czas");
    private JLabel currentTimeInfo = new JLabel("");
    private JLabel currentEventLabel = new JLabel("Aktualny krok");
    private JLabel currentEventInfo = new JLabel("");
    private JLabel nextEventLabel = new JLabel("Następny krok");
    private JLabel nextEventInfo = new JLabel("");
    private JLabel timeToNextEventLabel = new JLabel("Czas do następnego kroku");
    private JLabel timeToNextEventInfo = new JLabel("");
    private JLabel schedule = new JLabel();

    private String timeFormatString = "H:mm";
    private DateFormat tf = new SimpleDateFormat(timeFormatString);

    public MainFrame() {
        this.setTitle("Do szkoły!");

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.add(currentTimeLabel);
        this.add(currentTimeInfo);
        this.add(currentEventLabel);
        this.add(currentEventInfo);
        this.add(nextEventLabel);
        this.add(nextEventInfo);
        this.add(timeToNextEventLabel);
        this.add(timeToNextEventInfo);
        this.add(schedule);

        currentTimeLabel.setFont(LABEL_FONT);
        currentTimeInfo.setFont(INFO_FONT);
        currentEventLabel.setFont(LABEL_FONT);
        currentEventInfo.setFont(INFO_FONT);
        nextEventLabel.setFont(LABEL_FONT);
        nextEventInfo.setFont(INFO_FONT);
        timeToNextEventLabel.setFont(LABEL_FONT);
        timeToNextEventInfo.setFont(INFO_FONT);
        schedule.setFont(SCHEDULE_FONT);
    }

    public void displayTimeInformation(TimeInformation timeInformation) {
        if (!isVisible()) {
            initializeFrame();
        }

        Date currentTime = timeInformation.getCurrentTime();
        Date nextEventTime = timeInformation.getNextEventTime();
        String nextEvent = timeInformation.getNextEvent();
        long timeToNextStep = timeInformation.getTimeToNextStepInMinutes();

        currentTimeInfo.setText(tf.format(currentTime));
        currentEventInfo.setText(timeInformation.getCurrentEvent());
        if (nextEventTime != null) {
            nextEventInfo.setText(tf.format(nextEventTime) + " " + nextEvent);
            timeToNextEventInfo.setText(timeToNextStep + " min");
            timeToNextEventInfo.setForeground(getTimeToNextEventColour(timeToNextStep));
        } else {
            nextEventInfo.setText("-");
            timeToNextEventInfo.setText("-");
        }

        schedule.setText("<html><br>"+timeInformation.getOutstandingEvents().entrySet().stream().map(x -> scheduleEntryToLine(timeInformation.getCurrentTime(), x)).collect(Collectors.joining("<br>"))+"</html>");
    }

    private void initializeFrame() {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameDimension = new Dimension((int) (screenDimension.getWidth() * 0.8), (int) (screenDimension.getHeight() * 0.8));
        this.setSize(frameDimension);
        this.setLocation(screenDimension.width / 10, screenDimension.height / 10);
        this.setVisible(true);
    }

    private String scheduleEntryToLine(Date currentTime, Map.Entry<Date, String> scheduleEntry) {
        String result = "";
        if (scheduleEntry.getKey().before(currentTime)) {
            result += "<span style='color:#d0d0d0'>";
        } else {
            result += "<span style='color:#000000'>";
        }
        result += tf.format(scheduleEntry.getKey()) + " " + scheduleEntry.getValue();
        result += "</span>";
        return result;
    }

    private Color getTimeToNextEventColour(long timeToNextEvent) {
        if (timeToNextEvent < 2) {
            return Color.red;
        }

        if (timeToNextEvent < 5) {
            return Color.yellow;
        }

        return Color.green;
    }
}