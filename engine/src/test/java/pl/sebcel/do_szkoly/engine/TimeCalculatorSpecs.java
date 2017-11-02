package pl.sebcel.do_szkoly.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.TreeMap;

public class TimeCalculatorSpecs {

    private TimeCalculator cut = new TimeCalculator();
    private TreeMap<Date, String> schedule = new TreeMap<>();

    @Before
    public void setUp() {
        schedule.put(buildDate("7:00"), "Step 1");
        schedule.put(buildDate("8:00"), "Step 2");
        schedule.put(buildDate("9:00"), "Step 3");
        schedule.put(buildDate("10:00"), "Step 4");
    }

    @Test
    public void should_return_steps_that_have_time_earlier_to_current_time_to_PastSteps() {
        Date currentTime = buildDate("8:01");
        TimeInformation timeInformation = cut.calculateTimeInformation(schedule, currentTime);
        TreeMap<Date, String> actualResult = timeInformation.getCompletedEvents();
        Assert.assertEquals(2, actualResult.size());
        Assert.assertEquals("Step 1", actualResult.values().iterator().next());
    }

    @Test
    public void should_return_first_step_that_has_time_after_current_time_as_CurrentStep() {
        Date currentTime = buildDate("8:01");
        TimeInformation timeInformation = cut.calculateTimeInformation(schedule, currentTime);
        String actualResult = timeInformation.getCurrentEvent();
        Assert.assertEquals("Step 3", actualResult);
    }

    @Test
    public void should_return_second_step_that_has_time_after_current_time_as_NextStep() {
        Date currentTime = buildDate("8:01");
        TimeInformation timeInformation = cut.calculateTimeInformation(schedule, currentTime);
        String actualResult = timeInformation.getNextEvent();
        Assert.assertEquals("Step 4", actualResult);
    }

    @Test
    public void should_return_steps_that_have_time_after_current_time_as_OutstandingSteps() {
        Date currentTime = buildDate("8:01");
        TimeInformation timeInformation = cut.calculateTimeInformation(schedule, currentTime);
        TreeMap<Date, String> actualResult = timeInformation.getOutstandingEvents();
        Assert.assertEquals(1, actualResult.size());
        Assert.assertEquals("Step 4", actualResult.values().iterator().next());
    }

    private Date buildDate(String timeString) {
        return new TimeUtils().parseTimeString(timeString);
    }
}