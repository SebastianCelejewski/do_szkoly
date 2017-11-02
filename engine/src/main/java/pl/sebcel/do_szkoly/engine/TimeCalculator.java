package pl.sebcel.do_szkoly.engine;

import java.util.*;

/**
 * Inputs                   Outputs
 *
 * Time 1 - Step 1       <- past step
 *
 * Time 2 - Step 2       <- past step
 *
 * Time 3 - Step 3       <- current step
 *
 * current time
 *
 * Time 4 - Step 4       <- next step & outstanding step
 *
 * Time 5 - Step 5       <- outstanding step
 *
 * Time 6 - Step 6       <- outstanding step
 *
 */
public class TimeCalculator {

    public TimeInformation calculateTimeInformation(TreeMap<Date, String> schedule, Date currentTime) {

        Step currentStep = findCurrentStep(schedule, currentTime);
        Step nextStep = findNextStep(schedule, currentTime);
        List<Step> completedSteps = findCompletedSteps(schedule, currentStep);
        List<Step> outstandingSteps = findOutstandingSteps(schedule, currentStep);

        return new TimeInformation(currentTime, currentStep, nextStep, completedSteps, outstandingSteps);
    }

    private Step findCurrentStep(TreeMap<Date, String> schedule, Date currentTime) {
        Date currentStepStartTime = null;
        String currentStepDescription = null;

        for (Date date : schedule.keySet()) {
            if (date.before(currentTime)) {
                currentStepStartTime = date;
                currentStepDescription = schedule.get(date);
            }
        }

        if (currentStepStartTime != null) {
            return new Step(currentStepStartTime, currentStepDescription);
        } else {
            return null;
        }

    }

    private Step findNextStep(TreeMap<Date, String> schedule, Date currentTime) {
        for (Date date : schedule.keySet()) {
            if (date.after(currentTime)) {
                return new Step(date, schedule.get(date));
            }
        }
        return null;
    }

    private List<Step> findCompletedSteps(TreeMap<Date, String> schedule, Step currentStep) {
        List<Step> completedSteps = new ArrayList<>();

        if (currentStep == null) {
            return completedSteps;
        }

        for (Date date : schedule.keySet()) {
            if (date.before(currentStep.getStartTime())) {
                completedSteps.add(new Step(date, schedule.get(date)));
            }
        }

        return completedSteps;
    }

    private List<Step> findOutstandingSteps(TreeMap<Date, String> schedule, Step currentStep) {
        List<Step> outstandingSteps = new ArrayList<>();

        for (Date date : schedule.keySet()) {
            if (currentStep == null || date.after(currentStep.getStartTime())) {
                outstandingSteps.add(new Step(date, schedule.get(date)));
            }
        }

        return outstandingSteps;
    }
}