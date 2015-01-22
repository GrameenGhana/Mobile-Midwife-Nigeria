/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.motechproject.mmnaija.web.util;

import org.json.JSONObject;



/**
 *
 * @author seth
 */
public class MMNaijaUtil {
    
    
    public static String getDefaultResponseMessage(boolean error,String message)
    {
        JSONObject json  = new JSONObject();
        json.put("error", error);
        json.put("message", message);
        return json.toString();
    }
    
    
     public static boolean validateMMNaijaMsgKey(String campaignKey) {
        return (campaignKey.startsWith("mmnaija") && !campaignKey.contains("_SMS"));
    }
    
}
