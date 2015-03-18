/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.event;

import java.util.Map;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.mmnaija.domain.Message;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.MessageDataService;
import org.motechproject.mmnaija.repository.ServiceDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.service.ScheduleService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.mmnaija.web.util.MMConstants;
import org.motechproject.mmnaija.web.util.SimpleMail;
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

            Subscriber sub = subscriberService.findRecordByMsisdn(externalId);
            Subscription subscription = dataService.findRecordByEnrollmentService(externalId, msr.getContentId());
            if (subscription.getCurrentPoint() >= msr.getMinEntryPoint() && subscription.getCurrentPoint() <= msr.getMaxEntryPoint()) {
                if (campaignKey.toUpperCase().endsWith("IVR")) {
                    msgType = "voice";
                }
                scheduleService.playMessage(subscription, sub, msr, msgType);
            } else {
                if (subscription.getCurrentPoint() > msr.getMaxEntryPoint()) {
                    subscriberService.completeSubscribeUser(subscription);
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

    public boolean validateMMNaijaMsgKey(String campaignKey) {
//        return (campaignKey.startsWith("mmnaija") && !campaignKey.contains("_SMS"));
        return (campaignKey.startsWith("mmnaija"));
    }
}
