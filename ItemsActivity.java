package com.example.karup.clockspot;

/**
 * Created by spawrks on 2/13/14.
 */
public class ItemsActivity {

    String activityName;
    String timer;
    String totalTimer;


    public ItemsActivity(String activityName, String timer) {
        this.totalTimer = timer;
        this.activityName = activityName;
        this.timer = timer;
    }


    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getTotalTimer() {
        return totalTimer;
    }

    public void setTotalTimer(String totalTimer) {
        this.totalTimer = totalTimer;
    }
}
