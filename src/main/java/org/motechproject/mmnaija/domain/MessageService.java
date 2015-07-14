/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.domain;

import java.util.Date;
import javax.jdo.annotations.Column;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 *
 * @author seth
 */
@Entity
public class MessageService {

    @Column(length = 11)
    @Field(defaultValue = "1", name = "content_id")
    private Integer contentId;

    @Field
    String name;
    @Field
    String skey;

    @Column(length = 10)
    @Field
    String channel;

    @Field
    String description;
    
    @Field(name = "max_entry_point")
    Integer maxEntryPoint;
    @Field(name = "min_entry_point")
    Integer minEntryPoint;

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getMaxEntryPoint() {
        return maxEntryPoint;
    }

    public void setMaxEntryPoint(Integer maxEntryPoint) {
        this.maxEntryPoint = maxEntryPoint;
    }

    public Integer getMinEntryPoint() {
        return minEntryPoint;
    }

    public void setMinEntryPoint(Integer minEntryPoint) {
        this.minEntryPoint = minEntryPoint;
    }

    public String getTimeToStart() {
        return timeToStart;
    }

    public void setTimeToStart(String timeToStart) {
        this.timeToStart = timeToStart;
    }

    public Integer getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(Integer repeatFrequency) {
        this.repeatFrequency = repeatFrequency;
    }

    public String getRepeatTimeUnit() {
        return repeatTimeUnit;
    }

    public void setRepeatTimeUnit(String repeatTimeUnit) {
        this.repeatTimeUnit = repeatTimeUnit;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Field(name = "time_to_start")
   String timeToStart;

    @Field(name = "repeat_freq")
    Integer repeatFrequency;

    @Field(name = "repeat_time_unit")
    String repeatTimeUnit;

    @Field
    Status status;

    public MessageService(Integer contentId, String name, String skey, String channel, Integer maxEntryPoint, Integer minEntryPoint,String timeToStart, Integer repeatFrequency, String repeatTimeUnit, String status) {
        this.contentId = contentId;
        this.name = name;
        this.skey = skey;
        this.channel = channel;
        this.maxEntryPoint = maxEntryPoint;
        this.minEntryPoint = minEntryPoint;
        this.timeToStart = timeToStart;
        this.repeatFrequency = repeatFrequency;
        this.repeatTimeUnit = repeatTimeUnit;
        this.status = Status.valueOf(status);
    }

    public MessageService(Integer contentId, String name, String skey, String channel, Integer maxEntryPoint, Integer minEntryPoint,String timeToStart, Integer repeatFrequency, String repeatTimeUnit, Status status) {
        this.contentId = contentId;
        this.name = name;
        this.skey = skey;
        this.channel = channel;
        this.maxEntryPoint = maxEntryPoint;
        this.minEntryPoint = minEntryPoint;
        this.timeToStart = timeToStart;
        this.repeatFrequency = repeatFrequency;
        this.repeatTimeUnit = repeatTimeUnit;
        this.status = status;
    }   
}
