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
import org.motechproject.mmnaija.domain.Schedule;
import org.motechproject.mmnaija.domain.ScheduleStatus;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.MessageDataService;
import org.motechproject.mmnaija.repository.ScheduleDataService;
import org.motechproject.mmnaija.service.ScheduleService;
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

//    @Autowired
    @Override
    public Schedule add(Schedule schedule) {
        schedule.setLastAttemptDate(new Date());
        schedule.setAttempts(0);
        Message msg = msgDataService.findByMessageKeyLanguageType(schedule.getMessage());
        schedule.setContentType(msg.getContentType());
        return scheduleDataService.create(schedule);
    }

    @Override
    public Schedule create(Subscription subscription, Message msg, ScheduleStatus status) {
        return create(subscription, msg, status, "", "");
    }

    @Override
    public Schedule create(Subscription subscription, Message msg, ScheduleStatus status, String callStatus, String callDuration) {
        Schedule schedule = new Schedule();
        schedule.setSubscription(subscription);

        schedule.setMessage(msg);
        schedule.setStatus(status);
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

        List<Schedule> smsMessages = scheduleDataService.findByStatusAndChannel(ScheduleStatus.PENDING, Channel.V.toString());
        processSMS(ivrShedules);
    }

    public void processIVRs(List<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            //Check if number of retries have not been sent
            if (schedule.getAttempts() <= NUMBER_OF_SCHEDULES) {
                schedule.setStatus(ScheduleStatus.PROCESSING);
                schedule.setAttempts(schedule.getAttempts() + 1);
                update(schedule);
                Message msg = msgDataService.findByMessageKeyLanguageType(schedule.getMessage());
                String response = HTTPCommunicator.sendVoice(schedule, msg);
                if (response.equalsIgnoreCase("00")) {
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

    public void processSMS(List<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            //Check if number of retries have not been sent
            if (schedule.getAttempts() <= NUMBER_OF_SCHEDULES) {
                schedule.setStatus(ScheduleStatus.PROCESSING);
                schedule.setAttempts(schedule.getAttempts() + 1);
                update(schedule);

                Message msg = msgDataService.findByMessageKeyLanguageType(schedule.getMessage());
                String response = HTTPCommunicator.sendSMS(schedule, msg);
                if (response.equalsIgnoreCase("00")) {
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

}
