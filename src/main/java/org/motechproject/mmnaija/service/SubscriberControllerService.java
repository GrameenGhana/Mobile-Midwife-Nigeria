/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.service;

import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;

/**
 *
 * @author seth
 */
public interface SubscriberControllerService {

    boolean addSubscription(Subscriber sub, String campaign, int start);

    boolean addSubscription(Subscriber sub, String campaign, int start, String status);

    int getSMSStartPoint(MessageService service, int currentPosition);

    boolean unSubscribe(String msisdn, String service);

    boolean unSubscribe(Subscription subscription);

    boolean pauseSubscription(Subscription subscription);

    boolean resumeSubscription(Subscription subscription);

    boolean completeSubscription(Subscription subscription);
    boolean reactivateSubscription(Subscription subscription);

    Subscriber updateSubscription();
}
