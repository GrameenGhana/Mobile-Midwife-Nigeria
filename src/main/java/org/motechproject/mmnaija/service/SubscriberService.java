/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.service;

import java.util.List;
import org.motechproject.mmnaija.domain.Language;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;

/**
 *
 * @author seth
 */
public interface SubscriberService {

    Subscriber create(String msisdn, int gender, int age, int pregnant, String language);

    public Subscriber create(String msisdn, int gender, int age, int pregnant, Language language);

    Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, String language, String campaignName, int start);

    boolean unsubscribeUser(Subscription subscription);
    public Subscription findActiveSubscription(Subscriber subscriber,org.motechproject.mmnaija.domain.MessageService msgService);

    boolean subscribeUser(Subscriber subscriber, String campaignName, int start);

    Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, Language language, String campaignName, int start);

    void add(Subscriber subscriber);

    Subscriber findRecordByMsisdn(String msisdn);

    List<Subscriber> getRecords();

}
