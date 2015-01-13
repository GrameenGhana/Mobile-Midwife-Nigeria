/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.service.impl;

import java.util.Date;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.ServiceDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.service.SubscriberControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author seth
 */
@Service("subscriberControllerServer")
public class SubscriberControllerServerImpl implements SubscriberControllerService {

    @Autowired
    CampaignEnrollmentDataService enrollmentService;

    @Autowired
    ServiceDataService serviceDataService;

    @Autowired
    SubscriptionDataService subscriptionDataService;
    public static String Campaign_Nam = "";

    @Override
    public boolean addSubscription(Subscriber sub, String campaign, int start, String status) {
        org.motechproject.mmnaija.domain.MessageService service = serviceDataService.findServiceByContentId(Integer.parseInt(campaign));

        CampaignEnrollment enrolment = enrollmentService.create(new CampaignEnrollment(String.valueOf(sub.getMsisdn()), service.getSkey()));
        if (null != enrolment) {
            System.out.println("Start Point");
            start =getSMSStartPoint(service, start);
            System.out.println("End start point : "+start);
            Subscription subscription = new Subscription(sub, service, start, start, status, new Date(), enrolment);
//          new Subscription(sub, service, start, currentPoint, status, new Date(), enrolment), status, null, enrolment);
            return true;
        }
        return false;
    }

    @Override
    public int getSMSStartPoint(org.motechproject.mmnaija.domain.MessageService service, int startService) {

//        int startService=0;
        if (service.getContentId() == 4) {
            startService = (5 * startService) - 4;// taking into account SMS goes 5 times a week.
        } else {
            startService = (5 * (startService - 4)) - 4;// taking into account SMS goes 5 times a week and pregnancy content starts at week 5.
        }
        int smsStartPoint = (startService < service.getMinEntryPoint()) ? service.getMinEntryPoint() : startService;
        smsStartPoint = (startService > service.getMaxEntryPoint()) ? service.getMaxEntryPoint() : startService;

        return smsStartPoint;

    }

    @Override
    public boolean unSubscribe(String msisdn, String service) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Subscriber updateSubscription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
