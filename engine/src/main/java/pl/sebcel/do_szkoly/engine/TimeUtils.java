package pl.sebcel.do_szkoly.engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    private String timeFormatString = "H:mm";
    private DateFormat tf = new SimpleDateFormat(timeFormatString);

    public Date getCurrentTime() {
        return new Date();
    }

    public Date parseTimeString(String timeStr) {
        try {
            Calendar currentTime = Calendar.getInstance();
            Calendar targetTime = Calendar.getInstance();
            targetTime.setTime(tf.parse(timeStr));

            targetTime.set(Calendar.YEAR, currentTime.get(Calendar.YEAR));
            targetTime.set(Calendar.MONTH, currentTime.get(Calendar.MONTH));
            targetTime.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH));

            return targetTime.getTime();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse '"+timeStr+"' using pattern '"+timeFormatString+"': "+ex.getMessage(), ex);
        }
    }
}