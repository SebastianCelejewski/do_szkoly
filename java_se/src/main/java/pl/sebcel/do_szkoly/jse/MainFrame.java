package pl.sebcel.do_szkoly.jse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import pl.sebcel.do_szkoly.engine.*;

public class MainFrame extends JFrame {

    private final static Font LABEL_FONT = new Font("Arial", Font.PLAIN, 24);
    private final static Font INFO_FONT = new Font("Arial", Font.PLAIN, 96);
    private final static Font SCHEDULE_FONT = new Font("Arial", Font.PLAIN, 48);

    private JLabel currentTimeLabel = new JLabel("Aktualny czas");
    private JLabel currentTimeInfo = new JLabel("");
    private JLabel currentStepLabel = new JLabel("Aktualny krok");
    private JLabel currentStepInfo = new JLabel("");
    private JLabel nextStepLabel = new JLabel("Następny krok");
    private JLabel nextStepInfo = new JLabel("");
    private JLabel timeToNextStepLabel = new JLabel("Czas do następnego kroku");
    private JLabel timeToNextStepInfo = new JLabel("");
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
        this.add(currentStepLabel);
        this.add(currentStepInfo);
        this.add(nextStepLabel);
        this.add(nextStepInfo);
        this.add(timeToNextStepLabel);
        this.add(timeToNextStepInfo);
        this.add(schedule);

        currentTimeLabel.setFont(LABEL_FONT);
        currentTimeInfo.setFont(INFO_FONT);
        currentStepLabel.setFont(LABEL_FONT);
        currentStepInfo.setFont(INFO_FONT);
        nextStepLabel.setFont(LABEL_FONT);
        nextStepInfo.setFont(INFO_FONT);
        timeToNextStepLabel.setFont(LABEL_FONT);
        timeToNextStepInfo.setFont(INFO_FONT);
        schedule.setFont(SCHEDULE_FONT);
    }

    public void displayTimeInformation(TimeInformation timeInformation) {
        if (!isVisible()) {
            initializeFrame();
        }

        Date currentTime = timeInformation.getCurrentTime();
        Step currentStep = timeInformation.getCurrentStep();
        Step nextStep = timeInformation.getNextStep();

        currentTimeInfo.setText(tf.format(currentTime));
        if (currentStep != null) {
            currentStepInfo.setText(currentStep.getDescription());
        } else {
            currentStepInfo.setText("-");
        }

        if (nextStep != null) {
            long timeToNextStep = timeInformation.getTimeToNextStepInMinutes();
            nextStepInfo.setText(tf.format(nextStep.getStartTime()) + " " + nextStep.getDescription());
            timeToNextStepInfo.setText(timeToNextStep + " min");
            timeToNextStepInfo.setForeground(getTimeToNextStepColour(timeToNextStep));
        } else {
            nextStepInfo.setText("-");
            timeToNextStepInfo.setText("-");
        }

        String scheduleText = "";
        scheduleText += "<html><br>";

        scheduleText += timeInformation.getCompletedSteps().stream().map(x -> "<span style='color:grey'>" + scheduleEntryToLine(x) + "</span>").collect(Collectors.joining("<br>")) + "<br>";
        if (currentStep != null) {
            scheduleText += "<span style='color:red'>" + scheduleEntryToLine(currentStep) + "</span><br>";
        }
        scheduleText += timeInformation.getOutstandingSteps().stream().map(x -> "<span style='color:green'>" + scheduleEntryToLine(x) + "</span>").collect(Collectors.joining("<br>")) + "<br>";

        scheduleText += "</html>";

        schedule.setText(scheduleText);
    }

    private void initializeFrame() {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameDimension = new Dimension((int) (screenDimension.getWidth() * 0.8), (int) (screenDimension.getHeight() * 0.8));
        this.setSize(frameDimension);
        this.setLocation(screenDimension.width / 10, screenDimension.height / 10);
        this.setVisible(true);
    }

    private String scheduleEntryToLine(Step step) {
        return tf.format(step.getStartTime()) + " " + step.getDescription();
    }

    private Color getTimeToNextStepColour(long timeToNextStep) {
        if (timeToNextStep < 2) {
            return Color.red;
        }

        if (timeToNextStep < 5) {
            return Color.yellow;
        }

        return Color.green;
    }
}