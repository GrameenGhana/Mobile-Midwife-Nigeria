/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.mmnaija.domain.Language;

/**
 *
 * @author seth
 */
public interface LanguageDataService extends MotechDataService<Language> {

    @Lookup
    Language findByIsoCode(@LookupField(name = "isoCode") String isoCode);

    @Lookup
    Language findByName(@LookupField(name = "name") String name);
}
