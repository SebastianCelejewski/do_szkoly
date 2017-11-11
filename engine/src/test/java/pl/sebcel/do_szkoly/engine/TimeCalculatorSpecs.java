package pl.sebcel.do_szkoly.engine;

import org.junit.Assert;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class TimeCalculatorSpecs {

    private TimeCalculator cut = new TimeCalculator();
    private TreeMap<Date, String> schedule = new TreeMap<>();
    private TimeInformation timeInformation;

    public void setUp() {
        schedule.put(buildDate("5:00"), "Step 1");
        schedule.put(buildDate("6:00"), "Step 2");
        schedule.put(buildDate("7:00"), "Step 3");
        schedule.put(buildDate("8:00"), "Step 4");
        schedule.put(buildDate("9:00"), "Step 5");
        schedule.put(buildDate("10:00"), "Step 6");
    }

    @Test
    public void should_return_steps_that_have_time_earlier_to_current_time_as_CompletedSteps() {
        setUp();
        Date currentTime = buildDate("7:30");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        List<Step> actualResult = timeInformation.getCompletedSteps();
        Assert.assertEquals(2, actualResult.size());
        Assert.assertEquals("Step 1", actualResult.get(0).getDescription());
        Assert.assertEquals("Step 2", actualResult.get(1).getDescription());
    }

    @Test
    public void should_return_first_step_that_has_time_after_current_time_as_CurrentStep() {
        setUp();
        Date currentTime = buildDate("7:30");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Step actualResult = timeInformation.getCurrentStep();
        Assert.assertEquals("Step 3", actualResult.getDescription());
    }

    @Test
    public void should_return_second_step_that_has_time_after_current_time_as_NextStep() {
        setUp();
        Date currentTime = buildDate("7:30");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Step actualResult = timeInformation.getNextStep();
        Assert.assertEquals("Step 4", actualResult.getDescription());
    }

    @Test
    public void should_return_steps_that_have_time_after_current_time_as_OutstandingSteps() {
        setUp();
        Date currentTime = buildDate("7:30");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

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

    @Test
    public void VoiceInformation_should_be_null_if_there_is_no_next_step() {
        setUp();
        Date currentTime = buildDate("10:30");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Assert.assertEquals(null, timeInformation.getVoiceInformation());
    }

    @Test
    public void VoiceInformation_should_not_be_null_if_there_is_a_next_step() {
        setUp();
        Date currentTime = buildDate("9:30");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Assert.assertNotNull(timeInformation.getVoiceInformation());
    }

    @Test
    public void VoiceInformation_should_be_related_to_the_very_next_step() {
        setUp();
        Date currentTime = buildDate("9:30");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Assert.assertEquals("Step 6", timeInformation.getVoiceInformation().getReferencedStep().getDescription());
    }

    @Test
    public void VoiceInformation_should_be_triggered_after_crossing_the_most_distant_synthetic_step() {
        setUp();
        Date currentTime = buildDate("9:31");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Assert.assertEquals(30, timeInformation.getVoiceInformation().getSyntheticStepTimeToNextStepInMinutes());
    }

    @Test
    public void VoiceInformation_should_provide_real_time_to_the_next_step() {
        setUp();
        Date currentTime = buildDate("9:31");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Assert.assertEquals(29, timeInformation.getVoiceInformation().getRealTimeToNextStepInMinutes());
    }

    @Test
    public void VoiceInformation_should_not_be_triggered_twice_for_the_same_synthetic_step() {
        setUp();
        Date currentTime = buildDate("9:31");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        currentTime = buildDate("9:32");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);

        Assert.assertEquals(null, timeInformation.getVoiceInformation());
    }

    @Test
    public void VoiceInformation_should_contain_text_to_be_spoken() {
        setUp();
        Date currentTime = buildDate("9:31");
        timeInformation = cut.calculateTimeInformation(schedule, currentTime);
        Assert.assertNotNull(timeInformation.getVoiceInformation().getText());
    }

    private Date buildDate(String timeString) {
        return new TimeUtils().parseTimeString(timeString);
    }
}