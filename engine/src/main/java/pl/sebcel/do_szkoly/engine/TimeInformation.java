package pl.sebcel.do_szkoly.engine;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TimeInformation implements Serializable {

    private Date currentTime;
    private Step currentStep;
    private Step nextStep;
    private List<Step> completedSteps;
    private List<Step> outstandingSteps;
    private VoiceInformation voiceInformation;

    public TimeInformation(Date currentTime, Step currentStep, Step nextStep, List<Step> completedSteps, List<Step> outstandingSteps, VoiceInformation voiceInformation) {
        this.currentTime = currentTime;
        this.currentStep = currentStep;
        this.nextStep = nextStep;
        this.completedSteps = completedSteps;
        this.outstandingSteps = outstandingSteps;
        this.voiceInformation = voiceInformation;
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

    public VoiceInformation getVoiceInformation() {
        return voiceInformation;
    }

    public long getTimeToNextStepInMinutes() {
        return (nextStep.getStartTime().getTime() -  currentTime.getTime()) / 1000 / 60 + 1;
    }
}