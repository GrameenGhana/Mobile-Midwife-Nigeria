/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.mmnaija.domain.Message;

/**
 *
 * @author seth
 */
public interface MessageDataService extends MotechDataService<Message> {

    @Lookup
    Message findByMessageKeyLanguageType(
            @LookupField(name = "messageKey") String msgKey,
            @LookupField(name = "language") String langugge,
            @LookupField(name = "contentType") String msgType);

    @Lookup
    Message findByMessageKeyLanguage(
            @LookupField(name = "messageKey") String msgKey,
            @LookupField(name = "language") String langugge);

    @Lookup
    Message findByMessageKeyType(
            @LookupField(name = "messageKey") String msgKey,
            @LookupField(name = "contentType") String msgType);

}
