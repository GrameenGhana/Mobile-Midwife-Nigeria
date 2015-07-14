/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author skwakwa
 */
public class RequestWrapper extends ArrayList<RegistrationRequest> {

    public RequestWrapper() {
    }

    public RequestWrapper(Collection<? extends RegistrationRequest> c) {
        super(c);
    }
    
    
    
}
