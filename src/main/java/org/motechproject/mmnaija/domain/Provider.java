/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.domain;

import java.io.Serializable;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 *
 * @author skwakwa
 */
@Entity
public class Provider extends MdsEntity implements Serializable {

    @Field
    private String name;
    @Field
    private String smsUrl;
    @Field
    private String ivrUrl;
    @Field
    private int status;

    public Provider(String name, String smsUrl, String ivrUrl, int status) {
        this.name = name;
        this.smsUrl = smsUrl;
        this.ivrUrl = ivrUrl;
        this.status = status;
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
     * @return the smsUrl
     */
    public String getSmsUrl() {
        return smsUrl;
    }

    /**
     * @param smsUrl the smsUrl to set
     */
    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    /**
     * @return the ivrUrl
     */
    public String getIvrUrl() {
        return ivrUrl;
    }

    /**
     * @param ivrUrl the ivrUrl to set
     */
    public void setIvrUrl(String ivrUrl) {
        this.ivrUrl = ivrUrl;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    
    
    
}
