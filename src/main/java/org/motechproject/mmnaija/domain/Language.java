/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.motechproject.mmnaija.domain;

import javax.jdo.annotations.Unique;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 *
 * @author seth
 */
@Entity
public class Language   {
    @Field
    private String name;

    @Field(name="iso_code")
    @Unique
    private String isoCode;

    public Language(String name, String isoCode) {
        this.name = name;
        this.isoCode = isoCode;
    }

    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the isoCode
     */
    public String getIsoCode() {
        return isoCode;
    }

    /**
     * @param isoCode the isoCode to set
     */
    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }
    
    
}
