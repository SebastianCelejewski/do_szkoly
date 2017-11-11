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

    private Set<VoiceInformation> pastVoiceInformations = new HashSet<>();

    public TimeInformation calculateTimeInformation(TreeMap<Date, String> schedule, Date currentTime) {

        Step currentStep = findCurrentStep(schedule, currentTime);
        Step nextStep = findNextStep(schedule, currentTime);
        List<Step> completedSteps = findCompletedSteps(schedule, currentStep);
        List<Step> outstandingSteps = findOutstandingSteps(schedule, currentStep);
        VoiceInformation voiceInformation = calculateVoiceInformation(nextStep, currentTime);

        return new TimeInformation(currentTime, currentStep, nextStep, completedSteps, outstandingSteps, voiceInformation);
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

    private VoiceInformation calculateVoiceInformation(Step nextStep, Date currentTime) {
        if (weAreAfterLastStep(nextStep)) {
            return null;
        }

        Map<Date, Integer> syntheticEvents = generateSyntheticEvents(nextStep);
        Date mostRecentlyCrossedSyntheticEventDate = findMostRecentlyCrossedSyntheticEventDate(syntheticEvents, currentTime);

        if (weAreWayBeforeTheFirstSyntheticEvent(mostRecentlyCrossedSyntheticEventDate)) {
            return null;
        }

        int syntheticTimeToNextStepInMinutes = (int) (nextStep.getStartTime().getTime() - mostRecentlyCrossedSyntheticEventDate.getTime())/60/1000;
        int realTimeToNextStepInMinutes = (int) (nextStep.getStartTime().getTime() -  currentTime.getTime())/60/1000;
        String text = "Next step in " + (realTimeToNextStepInMinutes + 1) + " minutes. " + nextStep.getDescription();

        VoiceInformation voiceInformation = new VoiceInformation(nextStep, syntheticTimeToNextStepInMinutes, realTimeToNextStepInMinutes, text);

        if (voiceInformationAlreadySent(voiceInformation)) {
            return null;
        }

        pastVoiceInformations.add(voiceInformation);
        return voiceInformation;
    }


    private boolean weAreAfterLastStep(Step nextStep) {
        return nextStep == null;
    }

    private Date findMostRecentlyCrossedSyntheticEventDate(Map<Date, Integer> syntheticEvents, Date currentTime) {
        Date selectedSyntheticEventDate = null;
        for (Date syntheticEventDate : syntheticEvents.keySet()) {
            if (syntheticEventDate.before(currentTime)) {
                selectedSyntheticEventDate = syntheticEventDate;
            }
        }
        return selectedSyntheticEventDate;
    }

    private boolean weAreWayBeforeTheFirstSyntheticEvent(Date selectedSyntheticEventDate) {
        return selectedSyntheticEventDate == null;
    }

    private boolean voiceInformationAlreadySent(VoiceInformation voiceInformation) {
        return pastVoiceInformations.contains(voiceInformation);
    }

    private Map<Date,Integer> generateSyntheticEvents(Step nextStep) {
        Map<Date, Integer> syntheticSteps = new TreeMap<>();
        syntheticSteps.put(new Date(nextStep.getStartTime().getTime() - 1*60*1000), 1);
        syntheticSteps.put(new Date(nextStep.getStartTime().getTime() - 2*60*1000), 2);
        syntheticSteps.put(new Date(nextStep.getStartTime().getTime() - 5*60*1000), 5);
        syntheticSteps.put(new Date(nextStep.getStartTime().getTime() - 10*60*1000), 10);
        syntheticSteps.put(new Date(nextStep.getStartTime().getTime() - 15*60*1000), 15);
        syntheticSteps.put(new Date(nextStep.getStartTime().getTime() - 30*60*1000), 30);
        syntheticSteps.put(new Date(nextStep.getStartTime().getTime() - 60*60*1000), 60);
        return syntheticSteps;
    }
}