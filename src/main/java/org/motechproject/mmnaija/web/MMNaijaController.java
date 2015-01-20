/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Status;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.ServiceDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.mmnaija.web.util.MMConstants;
import org.motechproject.mmnaija.web.util.MMNaijaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author seth
 */
@Controller
public class MMNaijaController {

    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private ServiceDataService messageService;

    @RequestMapping("/status")
    @ResponseBody
    public String status() {
        return MMConstants.MMNAIJA_OK;
    }

    /**
     *
     * @param msisdn
     * @param age
     * @param maleOrFemale
     * @param language
     * @param preg
     * @param campaign
     * @param start
     * @param current
     * @return
     */
    @RequestMapping(value = "/web-api/v1/subscriber/register", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String registerSubscriber(
            @RequestParam(value = "msisdn") String msisdn,
            @RequestParam(value = "age") int age,
            @RequestParam(value = "gender") String maleOrFemale,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "pregnant") int preg,
            @RequestParam(value = "service") String campaign,
            @RequestParam(value = "start_point") int start) {
///msisdn=233277143521&age=28&gender=m&language=en&pregnant=1&service=1&start_point=1
        Subscriber subscriber = subscriberService.findRecordByMsisdn(msisdn);
        System.out.println("Subscriber :" + subscriber);
//        System.out.println("ID : "+subscriber.getMsisdn());
        //Check of subscriber is already registered
        if (null == subscriber) {
            subscriber = subscriberService.createAndSubscribe(msisdn, (maleOrFemale.equalsIgnoreCase("m")) ? 1 : 2, age, preg, language, campaign, start);

            return (null == subscriber)
                    ? MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD)
                    : MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED);
        } else {
            MessageService content = messageService.findServiceByContentId(Integer.parseInt(campaign));
            Subscription subscription = subscriberService.findActiveSubscription(subscriber, content);
            //not subscribed
            if (null == subscription) {
                //add subscription
                return (subscriberService.subscribeUser(subscriber, campaign, start))
                        ? MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD)
                        : MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED);

            } else {
                //already subscribed
                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIPTION_ALREADY_EXIST);

            }
        }

    }

    /**
     * For nsubscription users
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/web-api/v1/subscriber/unsubscribe", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String unSubscribe(HttpServletRequest request) {
        String msisdn = request.getParameter("msisdn");
        String service = request.getParameter("service");
        Subscriber subscriber = subscriberService.findRecordByMsisdn(msisdn);

        //check if subscriber exist
        if (null == subscriber) {
            return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_NOT_FOUND_EXCEPTION);
        } else {
            //checking service
            MessageService msgService = messageService.findServiceByContentId(Integer.parseInt(service));
            if (null == msgService) {
                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SERVICE_NOT_FOUND_EXCEPTION);

            } else {
                //Checking subscription
                Subscription subscriptions = subscriberService.findActiveSubscription(subscriber, msgService);
                if (null == subscriptions) {
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIPTION_NOT_FOUND_EXCEPTION);
                } else {
                    //Unsibscribe users
                    subscriberService.unsubscribeUser(subscriptions);
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.UNSUBSCRIPTION_SUCCESSFUL);
                }
            }

        }
//        return "MM Naija dey run";
    }

    @RequestMapping("/web-api/mmnaija")
    @ResponseBody
    public String hiMMNaija() {
        return "MM Naija dey run";
    }

}
