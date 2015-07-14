/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.event;

import java.util.HashMap;
import java.util.Map;
import org.motechproject.event.MotechEvent;

/**
 *
 * @author skwakwa
 */
public final class MsgSendEvent {

    public static final String SUBSCRIPTION_ID="subscriptionId";
    public static final String SUBSCRIBER_ID="subscriberId";
    
    public static final String EVENT_MSG="MMnaija_msg_sent";
    
     public static MotechEvent sendMsgEvent(String subscriptionId, String subscriberId) {
        Map<String, Object> params = new HashMap<>();
        params.put(SUBSCRIPTION_ID, subscriptionId);
        params.put(SUBSCRIBER_ID, subscriptionId);
        return new MotechEvent(EVENT_MSG, params);
    }
    public MsgSendEvent() {
    }
    
    
    
}
