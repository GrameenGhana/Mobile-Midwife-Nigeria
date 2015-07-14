/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web;

/**
 *
 * @author skwakwa
 */
/**
 * [{"msisdn":"2348171602855","gender":"2","language":"1","service":"PRE","start_point":"12","age":"21","pregnant":"1","provider_id":"2"},{"msisdn":"2348178929989","gender":"2","language":"1","service":"PRE","start_point":"19","age":"23","pregnant":"1","provider_id":"2"}
 *
 * @author skwakwa
 */
public class RegistrationRequest {

//   [{"msisdn":"2338171602855","gender":"2","language":"1","service":"PRE","start_point":"12","age":"21","pregnant":"1","provider_id":"2"}]
    public String msisdn;
    public String gender;
    public String language;
    public String service;
    public String start_point;
    public String age;
    public String pregnant;
    public String provider_id;

    public RegistrationRequest(String msisdn, String gender, String language, String service, String start_point, String age, String pregnant, String provider_id) {
        this.msisdn = msisdn;
        this.gender =(gender);
        this.language = language;
        this.service =service;
        this.start_point = start_point;
        this.age = (age);
        this.pregnant = (pregnant);
        this.provider_id = (provider_id);
    }

    /**
     * @return the msisdn
     */
    public String getMsidn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsidn(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return the start_point
     */
    public String getStart_poString() {
        return start_point;
    }

    /**
     * @param start_point the start_point to set
     */
    public void setStart_poString(String start_point) {
        this.start_point = start_point;
    }

    /**
     * @return the age
     */
    public String getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * @return the pregnant
     */
    public String getPregnant() {
        return pregnant;
    }

    /**
     * @param pregnant the pregnant to set
     */
    public void setPregnant(String pregnant) {
        this.pregnant = pregnant;
    }

    /**
     * @return the provider_id
     */
    public String getProvider_id() {
        return provider_id;
    }

    /**
     * @param provider_id the provider_id to set
     */
    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

}
