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
import org.motechproject.mmnaija.domain.Status;

/**
 *
 * @author seth
 */
public interface ServiceDataService extends MotechDataService<MessageService> {

    @Lookup
    MessageService findServiceByContentId(@LookupField(name = "contentId") Integer contentId);

    @Lookup
    List<MessageService> findActiveServices(@LookupField(name = "status") Status status);

    
      @Lookup
    MessageService findServiceByName(@LookupField(name = "name") String name);

}
