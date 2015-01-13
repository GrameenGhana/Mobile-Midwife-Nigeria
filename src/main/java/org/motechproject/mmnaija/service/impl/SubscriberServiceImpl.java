/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.service.impl;

import java.util.List;
import org.motechproject.mmnaija.domain.Language;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.repository.LanguageDataService;
import org.motechproject.mmnaija.repository.SubscriberDataService;
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
      return  createAndSubscribe(msisdn, gender, age, pregnant, languageService.findByIsoCode(language), campaignName, start);
    }

    @Override
    public Subscriber createAndSubscribe(String msisdn, int gender, int age, int pregnant, Language language, String campaignName, int start) {
        Subscriber subscriber = create(msisdn, gender, age, pregnant, language);
        subscriberControllerService.addSubscription(subscriber, campaignName, start, "1");
        return subscriber;
    }

}
