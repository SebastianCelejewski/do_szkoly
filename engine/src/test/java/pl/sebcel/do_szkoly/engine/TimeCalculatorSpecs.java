package pl.sebcel.do_szkoly.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class TimeCalculatorSpecs {

    private TimeCalculator cut = new TimeCalculator();
    private TreeMap<Date, String> schedule = new TreeMap<>();
    private TimeInformation timeInformation;

    @Before
    public void setUp() {
        schedule.put(buildDate("5:00"), "Step 1");
        schedule.put(buildDate("6:00"), "Step 2");
        schedule.put(buildDate("7:00"), "Step 3");
        schedule.put(buildDate("8:00"), "Step 4");
        schedule.put(buildDate("9:00"), "Step 5");
        schedule.put(buildDate("10:00"), "Step 6");

        Date currentTime = buildDate("7:30");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);
    }

    @Test
    public void should_return_steps_that_have_time_earlier_to_current_time_to_CompletedSteps() {
        List<Step> actualResult = timeInformation.getCompletedSteps();
        Assert.assertEquals(2, actualResult.size());
        Assert.assertEquals("Step 1", actualResult.get(0).getDescription());
        Assert.assertEquals("Step 2", actualResult.get(1).getDescription());
    }

    @Test
    public void should_return_first_step_that_has_time_after_current_time_as_CurrentStep() {
        Step actualResult = timeInformation.getCurrentStep();
        Assert.assertEquals("Step 3", actualResult.getDescription());
    }

    @Test
    public void should_return_second_step_that_has_time_after_current_time_as_NextStep() {
        Step actualResult = timeInformation.getNextStep();
        Assert.assertEquals("Step 4", actualResult.getDescription());
    }

    @Test
    public void should_return_steps_that_have_time_after_current_time_as_OutstandingSteps() {
        List<Step> actualResult = timeInformation.getOutstandingSteps();
        Assert.assertEquals(3, actualResult.size());
        Assert.assertEquals("Step 4", actualResult.get(0).getDescription());
        Assert.assertEquals("Step 5", actualResult.get(1).getDescription());
        Assert.assertEquals("Step 6", actualResult.get(2).getDescription());
    }

    @Test
    public void should_return_empty_CompletedSteps_when_current_time_is_before_first_step_time() {
        setUp();
        Date currentTime = buildDate("4:00");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Assert.assertEquals(0, timeInformation.getCompletedSteps().size());
    }

    @Test
    public void should_return_all_steps_as_OutstandingSteps_when_current_time_is_before_first_step_time() {
        setUp();
        Date currentTime = buildDate("4:00");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Assert.assertEquals(6, timeInformation.getOutstandingSteps().size());
    }

    private Date buildDate(String timeString) {
        return new TimeUtils().parseTimeString(timeString);
    }
}