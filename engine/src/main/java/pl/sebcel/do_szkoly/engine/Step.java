package pl.sebcel.do_szkoly.engine;

import java.io.Serializable;
import java.util.Date;

public class Step implements Serializable {

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

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return startTime.equals(((Step) other).startTime);
    }

    @Override
    public int hashCode() {
        return startTime.hashCode();
    }
}