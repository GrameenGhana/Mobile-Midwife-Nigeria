/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.motechproject.event.MotechEvent;
import org.motechproject.mmnaija.domain.Message;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.MessageDataService;
import org.motechproject.mmnaija.service.SubscriberControllerService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.sms.SmsEventSubjects;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author skwakwa
 */
public class SubscriptionScheduler {
    
    @Autowired
    MotechSchedulerService schedulerService;
    
    @Autowired
    MessageDataService messageDataService;
    
    @Autowired
    SubscriberService subscriberControllerService;
    
    public MotechEvent getMessage(String message, String recipient, String config) {
        Map<String, Object> params = new HashMap();
        params.put("message", message);
        params.put("recipient", recipient);
        params.put("config", config);
        
        return new MotechEvent(SmsEventSubjects.SEND_SMS, params);
//      return new MotechEvent("send_SMS_now", params);
    }
    
    public void subcriptionMessages(Subscriber subscriber, Subscription subscription) {
        MotechEvent motechEvent = null;
        Message msg = null;
        String config = "mmnaijaSMS";
        boolean isSMS = false;
        //IVR Schedule for evening
        if (subscription.getService() < 3) {
            config = "mmnaijaIVR" ;//+ subscriber.getProvider();
            msg = messageDataService.findByContentCurrentPositionLanguage(subscription.getService(), subscriber.getLanguage_id(), subscription.getCurrentPoint(), "voice");
        } else {
            isSMS = true;
           config+="_"+subscriber.getProvider();
            msg = messageDataService.findByContentCurrentPositionLanguage(subscription.getService(), "1", subscription.getCurrentPoint(), "sms");
        }
        motechEvent = getMessage(msg.getContent(), subscriber.getMsisdn(), config);
        scheduleMessage(motechEvent, isSMS);
        subscription.setCurrentPoint(subscription.getCurrentPoint() + 1);
        subscriberControllerService.updateSubscription(subscription);
    }
    
    public void scheduleMessage(MotechEvent motechEvent, boolean isSMS) {
        
        Calendar calendar = Calendar.getInstance();
        if (isSMS) {
            if (calendar.get(Calendar.HOUR_OF_DAY) < 17) {
                calendar.set(Calendar.HOUR_OF_DAY, 17);
            } else {
                calendar.add(Calendar.MINUTE, 4);
            }
        } else {
            calendar.add(Calendar.MINUTE, 2);
        }
        Date sendTime = calendar.getTime();

        //Now let' create our job
        RunOnceSchedulableJob job = new RunOnceSchedulableJob(motechEvent, sendTime);
        
        schedulerService.safeScheduleRunOnceJob(job);
    }
}
