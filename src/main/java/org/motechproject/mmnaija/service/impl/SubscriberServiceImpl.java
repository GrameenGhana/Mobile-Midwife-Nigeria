/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.service.impl;

import java.util.List;
import org.motechproject.mmnaija.domain.Language;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.LanguageDataService;
import org.motechproject.mmnaija.repository.SubscriberDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.service.SubscriberControllerService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author seth
 */
@Service("subscriberService")
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberDataService subscriberDataService;

    @Autowired
    private SubscriptionDataService subscriptionDataService;

    @Autowired
    private SubscriberControllerService subscriberControllerService;
    @Autowired
    LanguageDataService languageService;

    @Override
    public Subscriber create(String msisdn, int gender, int age, int pregnant, String language) {

        return create(msisdn, gender, age, pregnant, languageService.findByIsoCode(language));
    }

    public Subscriber create(String msisdn, int gender, int age, int pregnant, Language language) {

        return subscriberDataService.create(new Subscriber(msisdn, gender, age, pregnant, (language)));
    }

    @Override
    public void add(Subscriber subscriber) {
        subscriberDataService.create(subscriber);
    }

    @Override
    public Subscriber findRecordByMsisdn(String msisdn) {
        return subscriberDataService.findRecordByMsisdn(msisdn);
    }

    @Override
    public List<Subscriber> getRecords() {
        return subscriberDataService.retrieveAll();
    }

    @Override
    public Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, String language, String campaignName, int start) {
        return createAndSubscribe(msisdn, gender, age, pregnant, languageService.findByIsoCode(language), campaignName, start);
    }

    @Override
    public Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, Language language, String campaignName, int start) {
        Subscriber subscriber = create(msisdn, gender, age, pregnant, language);
        subscriberControllerService.addSubscription(subscriber, campaignName, start);
        return subscriber;
    }

    @Override
    public boolean subscribeUser(Subscriber subscriber, String campaignName, int start) {
        return subscriberControllerService.addSubscription(subscriber, campaignName, start);
    }

    boolean deactivateSubscription(Subscription subscription) {

        return (subscriberControllerService.unSubscribe(subscription));
    }

    @Override
    public boolean unsubscribeUser(Subscription subscription) {

        return deactivateSubscription(subscription);
    }

    @Override
    public Subscription findActiveSubscription(Subscriber subscriber, MessageService msgService) {
        return subscriptionDataService.findActiveSubscription(subscriber.getMsisdn(), msgService.getContentId(), org.motechproject.mmnaija.domain.Status.Active.toString());
    }

    @Override
    public boolean deleteAll() {
        subscriberDataService.deleteAll();
        return true;
    }

    @Override
    public boolean delete(Subscriber subcriber) {
subscriberDataService.delete(subcriber);
return true;
    }

}
