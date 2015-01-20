/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.osgi;

import java.util.List;
import javax.inject.Inject;
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

/**
 *
 * @author seth
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class SubscriberServiceIT extends BasePaxIT {

    @Inject
    SubscriberService subscriberService;

    @Before
    @After
    public void cleanupDatabase() {
        getLogger().info("cleanupDatabase");
        subscriberService.deleteAll();
    }

    @Before
    public void waitForBeans() {
        try {
            Thread.sleep(MMNaijaTestSuite.BUNDLE_MS_WAIT_TIME);
        } catch (InterruptedException e) {
        }
    }

    @Test
    public void verifyServiceFunctional() {

        /*
        *
         */
        getLogger().info("verifyServiceFunctional");
        System.out.println("Verifying functionality");
        Subscriber subscriber = subscriberService.createAndSubscribe("233201063177", 1, 22, 1, "en", "pregnancy", 5);

        Subscriber testSubscriber = subscriberService.findRecordByMsisdn(subscriber.getMsisdn());
        assertEquals(subscriber, testSubscriber);

        List<Subscriber> subscibers = subscriberService.getRecords();
        assertTrue(subscibers.contains(subscriber));

        subscriberService.delete(subscriber);

        subscriber = subscriberService.findRecordByMsisdn(testSubscriber.getMsisdn());
        assertNull(subscriber);

    }

}
