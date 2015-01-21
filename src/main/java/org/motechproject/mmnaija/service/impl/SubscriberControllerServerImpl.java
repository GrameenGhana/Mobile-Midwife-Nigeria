/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.service.impl;

import java.util.Date;
import org.joda.time.LocalDate;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Status;
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
    MessageCampaignService messageCampaignService;
    @Autowired
    SubscriptionDataService subscriptionDataService;
    public static String Campaign_Nam = "";

    @Override
    public boolean addSubscription(Subscriber sub, String campaign, int start, String status) {
        org.motechproject.mmnaija.domain.MessageService service = serviceDataService.findServiceByContentId(Integer.parseInt(campaign));

        CampaignRequest request = new CampaignRequest(String.valueOf(sub.getMsisdn()), service.getSkey(), new LocalDate(), null);
//        CampaignEnrollment enr = new CampaignEnrollment(String.valueOf(sub.getMsisdn()), service.getSkey());
//
//        enr.setReferenceDate(new LocalDate());
        messageCampaignService.enroll(request);
        
        CampaignEnrollment enrolment = enrollmentService.findByExternalIdAndCampaignName(String.valueOf(sub.getMsisdn()), service.getSkey());
        if (null != enrolment) {
            System.out.println("Start Point");
            start = getSMSStartPoint(service, start);
            System.out.println("End start point : " + start);
            Subscription subscription = new Subscription(sub, service, start, start, status, new Date(), enrolment);
            subscriptionDataService.create(subscription);
//          new Subscription(sub, service, start, currentPoint, status, new Date(), enrolment), status, null, enrolment);
            return true;
        }
        return false;
    }

    public CampaignEnrollment enrollSubscriber(Subscription subscription, String campaign, Date refDate) {
//        CampaignEnrollment enr = new CampaignEnrollment(String.valueOf(subscription.getSubscriber()), campaign);
//
//        enr.setReferenceDate(new LocalDate(refDate));
       CampaignRequest request= new CampaignRequest(subscription.getSubscriber(), campaign , new LocalDate(refDate), null);
       messageCampaignService.enroll(request);
       CampaignEnrollment enr  = enrollmentService.findByExternalIdAndCampaignName(subscription.getSubscriber(), campaign);
        return enr;
    }

    @Override
    public int getSMSStartPoint(org.motechproject.mmnaija.domain.MessageService service, int startService) {
        System.out.println("From :" + service.getContentId());
//        int startService=0;
        if (service.getContentId() == 4) {
            startService = (5 * startService) - 4;// taking into account SMS goes 5 times a week.
        } else {
            startService = (5 * (startService - 4)) - 4;// taking into account SMS goes 5 times a week and pregnancy content starts at week 5.
        }
        System.out.println("Serive :" + service.getMaxEntryPoint());
        System.out.println("Serive 3:" + service.getMinEntryPoint());

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

    @Override
    public boolean addSubscription(Subscriber sub, String campaign, int start) {
        //Set default status to active
        return addSubscription(sub, campaign, start, Status.Active.toString());
    }

    @Override
    public boolean unSubscribe(Subscription subscription) {
        if (!subscription.getStatus().equalsIgnoreCase(Status.Active.toString())) {
            //Already unsubsribed
            return false;
        }

        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());
        subscription.setEndDate(new Date());
        subscription.setStatus(Status.InActive.toString());
        CampaignEnrollment enroll = enrollmentService.findById(Long.parseLong(subscription.getEnrollment()));
        enrollmentService.delete(enroll);
        subscriptionDataService.update(subscription);
        return true;

    }

    public boolean pauseSubscription(Subscription subscription) {
        if (!subscription.getStatus().equalsIgnoreCase(Status.Active.toString())) {
            //Already unsubsribed
            return false;
        }

        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());
//        subscription.setEndDate(new Date());
        subscription.setStatus(Status.Paused.toString());
        System.out.println("Subscriotion : " + subscription.getSubscriber());
        System.out.println("Msg : " + messageSer.getSkey());
        CampaignEnrollment enroll = enrollmentService.findById(Long.parseLong(subscription.getEnrollment()));
        enroll.setStatus(CampaignEnrollmentStatus.INACTIVE);
        enrollmentService.update(enroll);
        subscriptionDataService.update(subscription);
        return true;

    }

    public boolean resumeSubscription(Subscription subscription) {
        if (!subscription.getStatus().equalsIgnoreCase(Status.Paused.toString())) {
            //Already unsubsribed
            return false;
        }
        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());
        subscription.setStatus(Status.Active.toString());
        CampaignEnrollment enroll = enrollmentService.findById(Long.parseLong(subscription.getEnrollment()));
        enroll.setStatus(CampaignEnrollmentStatus.ACTIVE);
        enrollmentService.update(enroll);
        subscriptionDataService.update(subscription);
        return true;
    }

    public boolean completeSubscription(Subscription subscription) {
        if (!subscription.getStatus().equalsIgnoreCase(Status.Active.toString())) {
            //Already unsubsribed
            return false;
        }

        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());
//        subscription.setEndDate(new Date());
        subscription.setStatus(Status.Completed.toString());
        CampaignEnrollment enroll = enrollmentService.findById(Long.parseLong(subscription.getEnrollment()));
        enroll.setStatus(CampaignEnrollmentStatus.COMPLETED);
        enrollmentService.update(enroll);
        subscriptionDataService.update(subscription);
        return true;

    }

    @Override
    public boolean reactivateSubscription(Subscription subscription) {

        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());

        return null != enrollSubscriber(subscription, messageSer.getSkey(), new Date());

    }
}
