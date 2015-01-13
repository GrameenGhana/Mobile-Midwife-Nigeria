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
import org.motechproject.mmnaija.domain.Language;
import org.motechproject.mmnaija.domain.Subscriber;

/**
 *
 * @author seth
 */
public interface SubscriberDataService 
    extends MotechDataService<Subscriber> {
    @Lookup
    Subscriber findRecordByMsisdn(@LookupField(name = "msisdn") String phoneNumber);

}
