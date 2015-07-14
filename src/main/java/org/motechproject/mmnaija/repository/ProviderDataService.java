/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Provider;
import org.motechproject.mmnaija.domain.Subscriber;

/**
 *
 * @author skwakwa
 */
public interface ProviderDataService
        extends MotechDataService<Provider> {

    @Lookup
    Provider findProviderByName(@LookupField(name = "name") String name);

}
