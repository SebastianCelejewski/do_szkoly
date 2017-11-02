package pl.sebcel.do_szkoly.engine;

import java.util.Date;

public class Step {

    private Date startTime;

    private String description;

    public Step(Date startTime, String description) {
        this.startTime = startTime;
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getDescription() {
        return description;
    }
}
