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
import org.motechproject.mmnaija.repository.MessageServiceDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.service.MessageService;
import org.motechproject.mmnaija.service.SubscriberService;
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
    MessageServiceDataService messageServiceDataService;

    @MotechListener(subjects = {EventKeys.SEND_MESSAGE})
    public void handleAfterMsgSent(MotechEvent event) {
       
        String campaignKey = (String) event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY);
       if(campaignKey.contains("pregnant") || campaignKey.contains("child")){
        
        String msgKey = (String) event.getParameters().get(EventKeys.MESSAGE_KEY);
        String jobId = (String) event.getParameters().get(EventKeys.SCHEDULE_JOB_ID_KEY);
        String externalId = (String) event.getParameters().get(EventKeys.EXTERNAL_ID_KEY);

        Subscription subscription = dataService.findRecordByEnrollment(externalId);
        org.motechproject.mmnaija.domain.MessageService service = subscription.getService();
        Message msg = msgService.findByMessageKeyLanguageType(msgKey, subscription.getSubscriber().getLanguage_id().getIsoCode(), service.getChannel());

       }else{
           System.out.println("Not HandledHer :"+campaignKey);
       }
        
    }

}
