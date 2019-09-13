package com.itvillageltd.emphoyee.tracker.CallLogs.Interface;

/**
 * Created by Rasel on 6/16/2019.
 */

public interface CallLogObject {

    String getNumber();

    void setNumber(String number);

    int getType();

    void setType(int type);

    long getDate();

    void setDate(long date);

    int getDuration();

    void setDuration(int duration);

    String getCoolDuration();

    String getContactName();
}
