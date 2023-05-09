package com.skomorokhin.timerpcapp;

import java.util.stream.IntStream;

public class TimeValue {

    int totalTimeInSeconds = 0;


    public void setTimeValue(int hh, int mm, int ss) {
        int[] totalSeconds = {hh * 3600, mm * 60, ss};
        this.totalTimeInSeconds = IntStream.of(totalSeconds).sum();
    }

    public void setTimeValue(int seconds) {
        this.totalTimeInSeconds = seconds;
    }

    public int getTimeInSecond() {
        return totalTimeInSeconds;
    }

    public String getInTimeFormat () {
        int hours = totalTimeInSeconds / 3600;
        int minutes = totalTimeInSeconds % 3600 / 60;
        int seconds = totalTimeInSeconds % 3600 % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void decreaseTimeValue(int seconds) {
        totalTimeInSeconds--;
    }

}
