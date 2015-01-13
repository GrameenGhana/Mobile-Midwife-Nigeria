/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web;

import org.motechproject.mmnaija.domain.Subscriber;
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
    public String status(
            @RequestParam(value = "msisdn") String msisdn,
            @RequestParam(value = "age") int age,
            @RequestParam(value = "gender") String maleOrFemale,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "pregnant") int preg,
            @RequestParam(value = "service") String campaign,
            @RequestParam(value = "start_point") int start) {

        Subscriber subscriber = subscriberService.findRecordByMsisdn(msisdn);
        System.out.println("Subscriber :"+subscriber);
//        System.out.println("ID : "+subscriber.getMsisdn());
        //Check of subscriber is already registered
        if (null == subscriber) {
            subscriber = subscriberService.createAndSubscribe(msisdn, (maleOrFemale.equalsIgnoreCase("m")) ? 1 : 2, age, preg, language, campaign, start);

            return (null == subscriber)
                    ? MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD)
                    : MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED);
        } else {
            return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_ALREADY_REGISTERED);
        }

    }

    @RequestMapping("/web-api/subs")
    @ResponseBody
    public String createSubscription() {
        return "MM Naija dey run";
    }

    @RequestMapping("/web-api/mmnaija")
    @ResponseBody
    public String hiMMNaija() {
        return "MM Naija dey run";
    }

}
