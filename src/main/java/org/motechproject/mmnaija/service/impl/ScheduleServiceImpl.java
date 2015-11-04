/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.service.impl;

import java.util.Date;
import java.util.List;
import org.motechproject.mmnaija.domain.Channel;
import org.motechproject.mmnaija.domain.Message;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Schedule;
import org.motechproject.mmnaija.domain.ScheduleStatus;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.MessageDataService;
import org.motechproject.mmnaija.repository.ScheduleDataService;
import org.motechproject.mmnaija.service.ScheduleService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.mmnaija.service.SubscriptionService;
import org.motechproject.mmnaija.web.util.HTTPCommunicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author seth
 */
@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
    
    static int NUMBER_OF_SCHEDULES = 5;
    @Autowired
    ScheduleDataService scheduleDataService;
    
    @Autowired
    MessageDataService msgDataService;
    
    @Autowired
    SubscriberService subscriptionService;
//    @Autowired

    @Override
    public Schedule add(Schedule schedule) {
        schedule.setLastAttemptDate(new Date());
        schedule.setAttempts(0);
//        Message msg = msgDataService.findByMessageKeyLanguageType(schedule.getMessage());
//        schedule.setContentType("V");
        return scheduleDataService.create(schedule);
    }
    
    @Override
    public Schedule create(Subscription subscription, Message msg, ScheduleStatus status) {
        return create(subscription, msg, status, "", "");
    } @Override
    public Schedule create(Subscription subscription, Message msg, ScheduleStatus status,String response) {
        return create(subscription, msg, status, "", "", response);
    }
    
    public Schedule create(Subscription subscription, String msg, ScheduleStatus status) {
        return create(subscription, msg, status, "", "");
    }
    
    @Override
    public Schedule create(Subscription subscription, Message msg, ScheduleStatus status, String callStatus, String callDuration) {
       return create(subscription, msg, status, callStatus, callDuration, "");
    }
    
    public Schedule create(Subscription subscription, Message msg, ScheduleStatus status, String callStatus, String callDuration,String response) {
        Schedule schedule = new Schedule();
        schedule.setSubscription(subscription);
        schedule.setSubscriber(subscription.getSubscriber());
        schedule.setMessage(msg.getMessageKey());
        schedule.setContentType(msg.getContentType().equalsIgnoreCase("voice")? Channel.V.toString():Channel.SMS.toString());
        schedule.setStatus(status);
        schedule.setCallStatus(callStatus);
        schedule.setCall_retry(callStatus);
        schedule.setScheduleResponse(response);
        
        return add(schedule);
    }
    
    public Schedule create(Subscription subscription, String msg, ScheduleStatus status, String callStatus, String callDuration) {
        Schedule schedule = new Schedule();
        schedule.setSubscription(subscription);
        schedule.setSubscriber(subscription.getSubscriber());
        schedule.setMessage(msg);
        schedule.setStatus(status);
        schedule.setContentType(Channel.V.toString());
        schedule.setCallStatus(callStatus);
        schedule.setCall_retry(callStatus);
        return add(schedule);
    }
    
    @Override
    public Schedule findActiveScheduleRequestBySubscription(String callerId) {
        return null;//scheduleDataService.findByStatusAndChannel(ScheduleStatus.NEW, callerId)
    }
    
    @Override
    public List<Schedule> findScheduledRequests() {
        return scheduleDataService.findByStatus(ScheduleStatus.PENDING);
    }
    
    @Override
    public List<Schedule> getAll() {
        return scheduleDataService.retrieveAll();
    }
    
    @Override
    public void delete(Schedule messageRequest) {
        scheduleDataService.delete(messageRequest);
    }
    
    @Override
    public Schedule update(Schedule messageRequest) {
        return scheduleDataService.update(messageRequest);
    }
    
    @Override
    public Schedule schedule(Schedule sch) {
        sch.setStatus(ScheduleStatus.PENDING);
        return update(sch);
    }
    
    @Override
    public void scheduledRequest() {
        List<Schedule> ivrShedules = scheduleDataService.findByStatusAndChannel(ScheduleStatus.PENDING, Channel.V.toString()); 
        processIVRs(ivrShedules);
        
        List<Schedule> smsMessages = scheduleDataService.findByStatusAndChannel(ScheduleStatus.PENDING, Channel.SMS.toString());
        processSMS(smsMessages);
    }
    
    public void processIVRs(List<Schedule> schedules) {
        
        for (Schedule schedule : schedules) {
            //Check if number of retries have not been sent
            if (schedule.getAttempts() <= NUMBER_OF_SCHEDULES) {
                
                schedule.setStatus(ScheduleStatus.PROCESSING);
                schedule.setAttempts(schedule.getAttempts() + 1);
                update(schedule);
                Subscriber subscriber =  subscriptionService.findRecordByMsisdn(schedule.getSubscriber());
//                Message msg = msgDataService.findByMessageKeyLanguageType(schedule.getMessage());
                String response = HTTPCommunicator.sendVoice(schedule.getSubscriber(), schedule.getMessage(),subscriber.getProvider());
                if (response.equalsIgnoreCase("00")) {
                    schedule.setStatus(ScheduleStatus.SENT);
                } else {
                    //Just Testing
                    schedule.setStatus(ScheduleStatus.PAUSED);
                }
            } else {
                schedule.setStatus(ScheduleStatus.STOPPED);
                
            }
            update(schedule);
            
        }
    }
    
    public void processSMS(List<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            //Check if number of retries have not been sent
            if (schedule.getAttempts() <= NUMBER_OF_SCHEDULES) {
                schedule.setStatus(ScheduleStatus.PROCESSING);
                schedule.setAttempts(schedule.getAttempts() + 1);
                update(schedule);
                Subscriber sub = subscriptionService.findRecordByMsisdn(schedule.getSubscriber());
                
                Message msg = msgDataService.findByMessageKeyLanguageType(schedule.getMessage());
                String response = HTTPCommunicator.sendSMS(schedule, msg,sub.getProvider());
                if (response.startsWith("OK")) {
                    schedule.setStatus(ScheduleStatus.SENT);
                } else {
                    //Just Testing
                    schedule.setStatus(ScheduleStatus.PAUSED);
                }
            } else {
                schedule.setStatus(ScheduleStatus.STOPPED);
                update(schedule);
            }
        }
    }
    
    @Override
    public Schedule playMessage(Subscription sub, Message msg) {
        Schedule sch = create(sub, msg, ScheduleStatus.PENDING);
        scheduledRequest();
        return sch;
    }
    
    @Override
    public Schedule playMessage(Subscription sub, String msg) {
        
        Schedule sch = create(sub, msg, ScheduleStatus.PENDING);
        scheduledRequest();
        return sch;
    }
    
    public Schedule playMessage(Subscription sub, Subscriber subscriber, MessageService msgService, String messageType) {
     
        String lang="1";
        if(msgService.getChannel().equalsIgnoreCase("voice")){
            lang= subscriber.getLanguage_id();
        }
        Message msg = msgDataService.findByContentCurrentPositionLanguage(msgService.getContentId(), lang, sub.getCurrentPoint(), messageType);
        Schedule sch = create(sub, msg, ScheduleStatus.PENDING);
        sub.setCurrentPoint(sub.getCurrentPoint() + 1);
        subscriptionService.updateSubscription(sub);
        scheduledRequest();
        return sch;
    }
    
}
