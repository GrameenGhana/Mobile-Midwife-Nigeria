/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.domain;

import java.util.Date;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 *
 * @author seth
 */
@Entity
public class Schedule {

    @Field
    private Subscription subscription;
    @Field 
    private Message message;
    @Field
    private String contentType;
    @Field(name = "delivery_date")
    private Date deliveryDate;

    @Field(name = "last_attempt_date")
    private Date lastAttemptDate;

    @Field
    private int attempts;
    @Field
    private ScheduleStatus status;

    @Field(name = "call_status")
    private String callStatus;
    @Field(name = "call_retry")
    private String call_retry;
    @Field(name = "call_duration")
    private String callDuration;

    /**
     * @return the subscription
     */
    public Subscription getSubscription() {
        return subscription;
    }

    /**
     * @param subscription the subscription to set
     */
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    /**
     * @return the message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * @return the deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return the lastAttemptDate
     */
    public Date getLastAttemptDate() {
        return lastAttemptDate;
    }

    /**
     * @param lastAttemptDate the lastAttemptDate to set
     */
    public void setLastAttemptDate(Date lastAttemptDate) {
        this.lastAttemptDate = lastAttemptDate;
    }

    /**
     * @return the attempts
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * @param attempts the attempts to set
     */
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    /**
     * @return the status
     */
    public ScheduleStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    /**
     * @return the callStatus
     */
    public String getCallStatus() {
        return callStatus;
    }

    /**
     * @param callStatus the callStatus to set
     */
    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    /**
     * @return the call_retry
     */
    public String getCall_retry() {
        return call_retry;
    }

    /**
     * @param call_retry the call_retry to set
     */
    public void setCall_retry(String call_retry) {
        this.call_retry = call_retry;
    }

    /**
     * @return the callDuration
     */
    public String getCallDuration() {
        return callDuration;
    }

    /**
     * @param callDuration the callDuration to set
     */
    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public Schedule(Subscription subscription, Message message, Date deliveryDate, Date lastAttemptDate, int attempts, ScheduleStatus status, String callStatus, String call_retry, String callDuration) {
        this.subscription = subscription;
        this.message = message;
        this.deliveryDate = deliveryDate;
        this.lastAttemptDate = lastAttemptDate;
        this.attempts = attempts;
        this.status = status;
        this.callStatus = callStatus;
        this.call_retry = call_retry;
        this.callDuration = callDuration;
    }

    public Schedule() {
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    
}
