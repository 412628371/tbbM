package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * Created by lvhantai on 2017/9/20.
 */
public class OrderStatusStatsDTO implements Serializable{
    private long progressCounts;
    private long waitingPickCounts;
    private long deliveryingCounts;
    private long undeliveredCounts; //驿站单  骑手未妥投
    private long waitingGrabCounts; //待接单

    public long getWaitingGrabCounts() {
        return waitingGrabCounts;
    }

    public void setWaitingGrabCounts(long waitingGrabCounts) {
        this.waitingGrabCounts = waitingGrabCounts;
    }

    public long getProgressCounts() {
        return progressCounts;
    }

    public void setProgressCounts(long progressCounts) {
        this.progressCounts = progressCounts;
    }

    public long getWaitingPickCounts() {
        return waitingPickCounts;
    }

    public void setWaitingPickCounts(long waitingPickCounts) {
        this.waitingPickCounts = waitingPickCounts;
    }

    public long getDeliveryingCounts() {
        return deliveryingCounts;
    }

    public void setDeliveryingCounts(long deliveryingCounts) {
        this.deliveryingCounts = deliveryingCounts;
    }

    public long getUndeliveredCounts() {
        return undeliveredCounts;
    }

    public void setUndeliveredCounts(long undeliveredCounts) {
        this.undeliveredCounts = undeliveredCounts;
    }
}
