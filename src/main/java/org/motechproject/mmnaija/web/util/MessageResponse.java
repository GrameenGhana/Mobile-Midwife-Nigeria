/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web.util;

import org.motechproject.mmnaija.domain.Subscription;

/**
 *
 * @author seth
 */
public class MessageResponse 

  {
    private String msg;
    private Subscription subscriptions;

    public MessageResponse() {
    }

    
    public MessageResponse(String msg, Subscription subscriptions) {
        this.msg = msg;
        this.subscriptions = subscriptions;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the subscriptions
     */
    public Subscription getSubscriptions() {
        return subscriptions;
    }

    /**
     * @param subscriptions the subscriptions to set
     */
    public void setSubscriptions(Subscription subscriptions) {
        this.subscriptions = subscriptions;
    }

}
