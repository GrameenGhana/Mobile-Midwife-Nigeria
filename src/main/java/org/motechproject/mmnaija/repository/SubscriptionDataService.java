/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.repository;

import java.util.List;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;

/**
 *
 * @author seth
 */
public interface SubscriptionDataService extends MotechDataService<Subscription> {

    @Lookup
    Subscription findRecordByEnrollment(@LookupField(name = "enrollment") String externalId);

    @Lookup
    public Subscription findActiveSubscription(
            @LookupField(name = "subscriber") String subscriber,
            @LookupField(name = "service") Integer messageService,
            @LookupField(name = "status") String status);

    @Lookup
    List<Subscription> findRecordBySubscriber(
            @LookupField(name = "subscriber") String subscriber,
            @LookupField(name = "service") Integer msgSrv,
            @LookupField(name = "status") String status
    );
}
