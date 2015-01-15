/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.event;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.mmnaija.domain.Message;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.MessageDataService;
import org.motechproject.mmnaija.repository.ServiceDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.web.util.MMConstants;
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
    private SubscriptionDataService dataService;
  
    @Autowired
    private MessageDataService msgService;
    @Autowired
    ServiceDataService messageServiceataService;
    private static final Logger log = LoggerFactory.getLogger(MessageSentEventHandler.class);
    
    @MotechListener(subjects = {EventKeys.SEND_MESSAGE})
    public void handleAfterMsgSent(MotechEvent event) {
        
        String campaignKey = (String) event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY);
        if (validateMMNaijaMsgKey(campaignKey)) {
            
            String msgKey = (String) event.getParameters().get(EventKeys.MESSAGE_KEY);
            String jobId = (String) event.getParameters().get(EventKeys.SCHEDULE_JOB_ID_KEY);
            String externalId = (String) event.getParameters().get(EventKeys.EXTERNAL_ID_KEY);
            
            Subscription subscription = dataService.findRecordByEnrollment(externalId);
            Message msg = msgService.findByMessageKeyLanguageType(msgKey);
            
        } else {
            log.warn("Not HandledHer :" + campaignKey);
        }
        
    }
    
    @MotechListener(subjects = {EventKeys.CAMPAIGN_COMPLETED})
    public void processCompletedCampaignEvent(MotechEvent event) {
    }
    
    public boolean validateMMNaijaMsgKey(String campaignKey) {
        return (campaignKey.equalsIgnoreCase(MMConstants.CAMPAIGN_CHILD_SMS)
                || campaignKey.equalsIgnoreCase(MMConstants.CAMPAIGN_CHILD)
                || campaignKey.equalsIgnoreCase(MMConstants.CAMPAIGN_PREGNANCY)
                || campaignKey.equalsIgnoreCase(MMConstants.CAMPAIGN_PREGNANCY_SMS));
    }
}
