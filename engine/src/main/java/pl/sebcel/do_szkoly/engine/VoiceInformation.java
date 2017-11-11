package pl.sebcel.do_szkoly.engine;

import java.io.Serializable;

public class VoiceInformation implements Serializable {

    private Step referencedStep;
    private int syntheticStepTimeToNextStepInMinutes;
    private int realTimeToNextStepInMinutes;
    private String text;

    public VoiceInformation(Step referencedStep, int syntheticStepTimeToNextStepInMinutes, int realTimeToNextStepInMinutes, String text) {
        this.referencedStep = referencedStep;
        this.syntheticStepTimeToNextStepInMinutes = syntheticStepTimeToNextStepInMinutes;
        this.realTimeToNextStepInMinutes = realTimeToNextStepInMinutes;
        this.text = text;
    }

    public Step getReferencedStep() {
        return referencedStep;
    }

    public int getSyntheticStepTimeToNextStepInMinutes() {
        return syntheticStepTimeToNextStepInMinutes;
    }

    public int getRealTimeToNextStepInMinutes() {
        return realTimeToNextStepInMinutes;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        VoiceInformation that = (VoiceInformation) other;
        if (syntheticStepTimeToNextStepInMinutes != that.syntheticStepTimeToNextStepInMinutes) return false;
        return referencedStep.equals(that.referencedStep);
    }

    @Override
    public int hashCode() {
        int result = referencedStep.hashCode();
        result = 31 * result + syntheticStepTimeToNextStepInMinutes;
        return result;
    }
}