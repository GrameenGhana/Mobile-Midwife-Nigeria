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
 Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, String language, String campaignName, int start,Long provider);

    boolean unsubscribeUser(Subscription subscription);
    boolean updateSubscription(Subscription subscription);
    boolean updateSubscriber(Subscriber subscription);

    boolean pauseSubscribeUser(Subscription subscription);

    boolean resumeSubscribeUser(Subscription subscription);
 public boolean enrolUsersSubscribed(Subscription subscription);
    boolean reactivateSubscribe(Subscription subscription);
   public void deleteCampaign(String campaign);
    boolean completeSubscribeUser(Subscription subscription);

    public Subscription findActiveSubscription(Subscriber subscriber, org.motechproject.mmnaija.domain.MessageService msgService);

    public Subscription findSubscription(Subscriber subscriber, org.motechproject.mmnaija.domain.MessageService msgService);
public Subscription findSubscriptionById(Long subscriptionId);

    boolean subscribeUser(Subscriber subscriber, String campaignName, int start);

    Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, Language language, String campaignName, int start);

    void add(Subscriber subscriber);

    Subscriber findRecordByMsisdn(String msisdn);

    List<Subscriber> getRecords();
List<Subscription> subscriptionFindAll();
List<Subscription> findByStatus(String status);
List<Subscription> findByStatusService(String status,Integer service);
List<Subscription> findByStatusByUser(String msisdn);

    boolean deleteAll();

    boolean delete(Subscriber subcriber);
    

}
