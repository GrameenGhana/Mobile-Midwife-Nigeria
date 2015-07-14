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

    @Override
    public boolean enrolUsersSubscribed(Subscription subscription) {
        return subscriberControllerService.enrolSubscription(subscription);
    }

    public void deleteCampaign(String campaign) {
        subscriberControllerService.deleteCampaign(campaign);
    }

    public Subscriber create(String msisdn, int gender, int age, int pregnant, Language language) {

        return subscriberDataService.create(new Subscriber(msisdn, gender, age, pregnant, (language)));
    }

    public Subscriber create(String msisdn, int gender, int age, int pregnant, Language language, Long provider) {

        return subscriberDataService.create(new Subscriber(msisdn, gender, age, pregnant, (language), provider));
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
        Language lang = languageService.findById(Long.parseLong(language));
        System.out.println("Language Selected : " + lang.getIsoCode());
        return createAndSubscribe(msisdn, gender, age, pregnant, languageService.findById(Long.parseLong(language)), campaignName, start);
    }

    @Override
    public Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, String language, String campaignName, int start, Long provider) {
        Language lang = languageService.findById(Long.parseLong(language));
        System.out.println("Language Selected : " + lang.getIsoCode());
        return createAndSubscribe(msisdn, gender, age, pregnant, languageService.findById(Long.parseLong(language)), campaignName, start, provider);
    }

    @Override
    public Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, Language language, String campaignName, int start) {
        Subscriber subscriber = create(msisdn, gender, age, pregnant, language);
        subscriberControllerService.addSubscription(subscriber, campaignName, start);
        return subscriber;
    }

    public Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, Language language, String campaignName, int start, Long provider) {
        Subscriber subscriber = create(msisdn, gender, age, pregnant, language, provider);
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

    public Subscription findSubscription(Subscriber subscriber, MessageService msgService) {
        return subscriptionDataService.findSubscription(subscriber.getMsisdn(), msgService.getContentId());
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

    @Override
    public boolean pauseSubscribeUser(Subscription subscription) {
        return subscriberControllerService.pauseSubscription(subscription);
    }

    @Override
    public boolean resumeSubscribeUser(Subscription subscription) {
        return subscriberControllerService.resumeSubscription(subscription);
    }

    @Override
    public boolean completeSubscribeUser(Subscription subscription) {
        return subscriberControllerService.completeSubscription(subscription);
    }

    @Override
    public boolean reactivateSubscribe(Subscription subscription) {
        return subscriberControllerService.reactivateSubscription(subscription);
    }

    @Override
    public boolean updateSubscription(Subscription subscription) {
        subscriptionDataService.update(subscription);
        return true;
    }

    @Override
    public List<Subscription> subscriptionFindAll() {
        return subscriptionDataService.retrieveAll();
    }

    @Override
    public List<Subscription> findByStatus(String status) {
        return subscriptionDataService.findRecordByStatus(status);
    }

    @Override
    public List<Subscription> findByStatusService(String status, Integer service) {

        return subscriptionDataService.findRecordByServiceStatus(service, status);
    }

    @Override
    public Subscription findSubscriptionById(Long subscriptionId) {
        return subscriptionDataService.findById(subscriptionId);
    }

    @Override
    public List<Subscription> findByStatusByUser(String msisdn) {

        return subscriptionDataService.findRecordByUser(msisdn, "Active");
    }

    @Override
    public boolean updateSubscriber(Subscriber subscription) {
 subscriberDataService.update(subscription);
return true;
    }

}
