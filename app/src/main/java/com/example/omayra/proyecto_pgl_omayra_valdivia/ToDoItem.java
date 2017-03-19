package com.example.omayra.proyecto_pgl_omayra_valdivia;

/**
 * Created by omayra on 16/3/17.
 */

public class ToDoItem {
    private String mTitle = new String();
    private Priority mPriority = Priority.MED;
    private Status mStatus = Status.NOTDONE;
    private String mDate = new String();
    private String mTime = new String();

    public enum Priority {
        LOW, MED, HIGH
    };
    public enum Status {
        NOTDONE, DONE
    };

    public ToDoItem(String mTitle, Priority mPriority, Status mStatus, String mDate, String mTime) {
        this.mTitle = mTitle;
        this.mPriority = mPriority;
        this.mStatus = mStatus;
        this.mDate = mDate;
        this.mTime = mTime;
    }
    public String getmTitle() {
        return mTitle;
    }
    public Priority getmPriority() {
        return mPriority;
    }
    public Status getmStatus() {
        return mStatus;
    }
    public String getmDate() {
        return mDate;
    }
    public String getmTime() {
        return mTime;
    }
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    public void setmPriority(Priority mPriority) {
        this.mPriority = mPriority;
    }
    public void setmStatus(Status mStatus) {
        this.mStatus = mStatus;
    }
    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}
