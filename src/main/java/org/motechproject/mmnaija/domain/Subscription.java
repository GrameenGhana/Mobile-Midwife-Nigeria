/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.domain;

import java.util.Date;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;

/**
 *
 * @author seth
 */
@Entity
public class Subscription {

    @Field(name = "start_point")
    private int startPoint;
    @Field(name = "current_point")
    private int currentPoint;
    @Field
    private String status;
    @Field(name = "start_date")
    private Date startDate;
    @Field(name = "end_date")
    private Date endDate;
    @Field(name = "service_id")
    MessageService service;
    @Field
    Subscriber subscriber;
    @Field
    private String enrollment;

    public MessageService getService() {
        return service;
    }

    public void setService(MessageService service) {
        this.service = service;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Subscription(Subscriber subscriber, MessageService service, int startPoint, int currentPoint, String status, Date startDate, Date endDate, CampaignEnrollment enrollment) {
        this.subscriber = subscriber;
        this.startPoint = startPoint;
        this.currentPoint = currentPoint;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.service = service;
        this.enrollment = enrollment.getExternalId();
    }

    public Subscription(Subscriber subscriber, MessageService service, int startPoint, int currentPoint, String status, Date startDate, CampaignEnrollment enrollment) {
        this.subscriber = subscriber;
        this.startPoint = startPoint;
        this.currentPoint = currentPoint;
        this.status = status;
        this.startDate = startDate;

        this.enrollment = enrollment.getExternalId();
        this.service = service;
    }

    /**
     * @return the startPoint
     */
    public int getStartPoint() {
        return startPoint;
    }

    /**
     * @param startPoint the startPoint to set
     */
    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * @return the currentPoint
     */
    public int getCurrentPoint() {
        return currentPoint;
    }

    /**
     * @param currentPoint the currentPoint to set
     */
    public void setCurrentPoint(int currentPoint) {
        this.currentPoint = currentPoint;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the enrollment
     */
    public String getEnrollment() {
        return enrollment;
    }

    /**
     * @param enrollment the enrollment to set
     */
    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

}
