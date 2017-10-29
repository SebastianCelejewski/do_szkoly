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

    private JLabel currentTimeLabel = new JLabel("Aktualny czas");
    private JLabel currentTimeInfo = new JLabel("");
    private JLabel nextEventLabel = new JLabel("Następny krok");
    private JLabel nextEventInfo = new JLabel("");
    private JLabel timeToNextEventLabel = new JLabel("Czas do następnego kroku");
    private JLabel timeToNextEventInfo = new JLabel("");
    private JLabel separatorLabel = new JLabel();
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
        this.add(nextEventLabel);
        this.add(nextEventInfo);
        this.add(timeToNextEventLabel);
        this.add(timeToNextEventInfo);
        this.add(separatorLabel);
        this.add(schedule);

        currentTimeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        currentTimeInfo.setFont(new Font("Arial", Font.PLAIN, 96));
        nextEventLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        nextEventInfo.setFont(new Font("Arial", Font.PLAIN, 96));
        timeToNextEventLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        timeToNextEventInfo.setFont(new Font("Arial", Font.PLAIN, 96));
        separatorLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        schedule.setFont(new Font("Arial", Font.PLAIN, 48));
    }

    public void displayTimeInformation(TimeInformation timeInformation) {
        if (!isVisible()) {
            Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameDimension = new Dimension((int) (screenDimension.getWidth() * 0.8), (int) (screenDimension.getHeight() * 0.8));
            this.setSize(frameDimension);
            this.setLocation(screenDimension.width / 10, screenDimension.height / 10);
            this.setVisible(true);
        }

        currentTimeInfo.setText(tf.format(timeInformation.getCurrentTime()));
        if (timeInformation.getNextEventTime() != null) {
            nextEventInfo.setText(tf.format(timeInformation.getNextEventTime()) + " " + timeInformation.getNextEvent());
            timeToNextEventInfo.setText(timeInformation.getTimeToNextStepInMinutes() + " min");
            timeToNextEventInfo.setForeground(getTimeToNextEventColour(timeInformation.getTimeToNextStepInMinutes()));
        } else {
            nextEventInfo.setText("-");
            timeToNextEventInfo.setText("-");
        }

        schedule.setText("<html><br>"+timeInformation.getOutstandingEvents().entrySet().stream().map(x -> scheduleEntryToLine(timeInformation.getCurrentTime(), x)).collect(Collectors.joining("<br>"))+"</html>");
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