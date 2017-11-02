package pl.sebcel.do_szkoly.engine;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class TimeInformation {

    private Date currentTime;
    private Step currentStep;
    private Step nextStep;
    private List<Step> completedSteps;
    private List<Step> outstandingSteps;

    public TimeInformation(Date currentTime, Step currentStep, Step nextStep, List<Step> completedSteps, List<Step> outstandingSteps) {
        this.currentTime = currentTime;
        this.currentStep = currentStep;
        this.nextStep = nextStep;
        this.completedSteps = completedSteps;
        this.outstandingSteps = outstandingSteps;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public Step getCurrentStep() {
        return currentStep;
    }

    public Step getNextStep() {
        return nextStep;
    }

    public List<Step> getCompletedSteps() {
        return completedSteps;
    }

    public List<Step> getOutstandingSteps() {
        return outstandingSteps;
    }

    public long getTimeToNextStepInMinutes() {
        return (nextStep.getStartTime().getTime() -  currentTime.getTime()) / 1000 / 60 + 1;
    }
}