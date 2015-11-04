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
import org.motechproject.mmnaija.domain.Schedule;
import org.motechproject.mmnaija.domain.ScheduleStatus;

/**
 *
 * @author seth
 */
public interface ScheduleDataService extends MotechDataService<Schedule> {

    @Lookup
    List<Schedule> findByStatus(@LookupField(name = "status") ScheduleStatus status);

    @Lookup
    List<Schedule> findByStatusAndChannel(@LookupField(name = "status") ScheduleStatus status, @LookupField(name = "contentType") String channel);

    @Lookup
    Schedule findBySubscriber(@LookupField(name = "callStatus") String msisdn);  
    
    @Lookup
    Schedule findByMsisdn(@LookupField(name = "subscriber") String msisdn);
}
