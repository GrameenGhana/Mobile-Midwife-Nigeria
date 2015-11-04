/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.mmnaija.domain.Message;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Schedule;
import org.motechproject.mmnaija.domain.ScheduleStatus;
import org.motechproject.mmnaija.domain.Status;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.MessageDataService;
import org.motechproject.mmnaija.repository.ServiceDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.service.ScheduleService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.mmnaija.web.util.HTTPCommunicator;
import org.motechproject.mmnaija.web.util.MMConstants;
import org.motechproject.mmnaija.web.util.SimpleMail;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.sms.SmsEventSubjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author seth
 */
@Component
public class MessageSentEventHandler {

    @Autowired
    SubscriberService subscriberService;

    @Autowired
    private MessageDataService msgService;

    @Autowired
    private ServiceDataService serviceDate;
    @Autowired
    ServiceDataService messageServiceataService;
    @Autowired
    private SubscriptionDataService dataService;
    @Autowired
    ScheduleService scheduleService;
    private static final Logger log = LoggerFactory.getLogger(MessageSentEventHandler.class);

    @Autowired
    MessageDataService msgDataService;

    @Autowired
    MotechSchedulerService schedulerService;

    @MotechListener(subjects = {EventKeys.SEND_MESSAGE})
    public void handleAfterMsgSent(MotechEvent event) {
        String campaignKey = (String) event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY);

        String msgKey = (String) event.getParameters().get(EventKeys.MESSAGE_KEY);
        String jobId = (String) event.getParameters().get(EventKeys.SCHEDULE_JOB_ID_KEY);
        String externalId = (String) event.getParameters().get(EventKeys.EXTERNAL_ID_KEY);
        System.out.println(String.format("message Details  msgkey %s externalId %s", msgKey, externalId));
        if (validateMMNaijaMsgKey(campaignKey)) {
            MessageService msr = serviceDate.findServiceBySkey(campaignKey);
            String msgType = "sms";

            String lang = "1";
            String config = "mmnaijaSMS_";
            Subscriber sub = subscriberService.findRecordByMsisdn(externalId);
             ScheduleStatus status  =  ScheduleStatus.PROCESSING;
            Subscription subscription = dataService.findRecordByEnrollmentService(externalId, msr.getContentId());
            if (subscription.getCurrentPoint() >= msr.getMinEntryPoint() && subscription.getCurrentPoint() <= msr.getMaxEntryPoint()) {
                if (campaignKey.toUpperCase().endsWith("IVR")) {
                    msgType = "voice";
                    lang = sub.getLanguage_id();
                    config = "mmnaijaIVR";
                } else {
                    //SMS configuration in motech mmnaijaSMS_1 # for airtel
                    config += sub.getProvider();
                    
                     status  =  ScheduleStatus.DELIVERED;
                }

                Message msg = msgDataService.findByContentCurrentPositionLanguage(msr.getContentId(), lang, subscription.getCurrentPoint(), msgType);
//        Schedule sch = create(sub, msg, ScheduleStatus.SENT);

                Schedule s = null;
                try {
                    s = scheduleService.create(subscription, msg, status, "");
                } catch (Exception e) {
                }

                if ("voice".equalsIgnoreCase(msgType)) {

                    String response = HTTPCommunicator.sendVoice(sub.getMsisdn(), msg.getMessageKey(), sub.getProvider());

                    System.out.println("===============================================");
                    System.out.println("Config   : " + config);
                    System.out.println("Message  : " + msg.getContent());
                    System.out.println("Recipient: " + sub.getMsisdn());
                    System.out.println("================================================");
                    try {

                        ScheduleStatus st = ScheduleStatus.FAILED;
                        if (response.isEmpty()) {

                        } else {
                            if (msg.getContentType().equalsIgnoreCase("sms")) {
                                if (response.startsWith("OK")) {
                                    st = ScheduleStatus.DELIVERED;
                                }
                            } else {
                                if (response.startsWith("1")) {
                                    st = ScheduleStatus.DELIVERED;
                                }
                            }

                        }

                        if (null != s) {
                            s.setScheduleResponse(response);
                            s.setStatus(st);
                            scheduleService.update(s);
                        }
                        System.out.println("Scheduler saved");
                    } catch (Exception e) {
                    }

                }
                subscription.setCurrentPoint(subscription.getCurrentPoint() + 1);
                subscriberService.updateSubscription(subscription);

//                scheduleMessage(getMessage(msg.getContent(), sub.getMsisdn(), config));
//        scheduledRequest();
//        return sch;
//                scheduleService.playMessage(subscription, sub, msr, msgType);
            } else {
                if (subscription.getCurrentPoint() > msr.getMaxEntryPoint()) {
                    subscriberService.completeSubscribeUser(subscription);
                } else {
                    subscription.setCurrentPoint(subscription.getCurrentPoint() + 1);
                    subscriberService.updateSubscription(subscription);
                }
            }
        } else {
            log.warn("Not HandledHer :" + campaignKey);
        }
    }

    @MotechListener(subjects = {EventKeys.CAMPAIGN_COMPLETED})
    public void processCompletedCampaignEvent(MotechEvent event) {

        if (event.getParameters().get("CampaignName").toString().startsWith("mmnaija")) {
            log.info("Handling CAMPAIGN_COMPLETED event {}: message={} from campaign={} for externalId={}", event.getSubject(),
                    event.getParameters().get("MessageKey"), event.getParameters().get("CampaignName"), event.getParameters().get("ExternalID"));
            Map<String, Object> parametersMap = event.getParameters();
            String clientId = (String) parametersMap.get("ExternalID");
            String campaign = (String) parametersMap.get("CampaignName");
            MessageService msr = serviceDate.findServiceBySkey(campaign);

            Subscription s = dataService.findRecordByEnrollmentService(clientId, msr.getContentId());
            subscriberService.completeSubscribeUser(s);

        }
    }

    @MotechListener(subjects = {MsgSendEvent.EVENT_MSG})
    public void handleMsgSent(MotechEvent event) {
        Long subscriberId = (Long) event.getParameters().get(MsgSendEvent.SUBSCRIBER_ID);

        Long subId = (Long) event.getParameters().get(MsgSendEvent.SUBSCRIPTION_ID);

        Subscription sub = subscriberService.findSubscriptionById(subscriberId);
        MessageService msgService = messageServiceataService.findServiceByContentId(sub.getService());

        sub.setCurrentPoint(sub.getCurrentPoint() + 1);
        if (sub.getCurrentPoint() > msgService.getMaxEntryPoint()) {
            sub.setStatus(Status.Completed.toString());
            subscriberService.completeSubscribeUser(sub);
        }
        subscriberService.updateSubscription(sub);
    }

    public boolean validateMMNaijaMsgKey(String campaignKey) {
//        return (campaignKey.startsWith("mmnaija") && !campaignKey.contains("_SMS"));
        return (campaignKey.startsWith("mmnaija"));
    }

    public MotechEvent getMessage(String message, String recipient, String config) {
        Map<String, Object> params = new HashMap();
        params.put("message", message);
        List<String> recipients = new ArrayList<String>();
        recipients.add(recipient);
        params.put("recipients", recipients);
        params.put("config", config);

        System.out.println("===============================================");
        System.out.println("Config   : " + config);
        System.out.println("Message  : " + message);
        System.out.println("Recipient: " + recipient);
        System.out.println("================================================");
        return new MotechEvent(SmsEventSubjects.SEND_SMS, params);
//        return new MotechEvent("send_SMS_now", params);
    }

    public void scheduleMessage(MotechEvent motechEvent) {

        Calendar calendar = Calendar.getInstance();
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.MINUTE, 5);
        sendTime = calendar.getTime();
        //Now let' create our job
        RunOnceSchedulableJob job = new RunOnceSchedulableJob(motechEvent, sendTime);

        schedulerService.safeScheduleRunOnceJob(job);
    }
}
