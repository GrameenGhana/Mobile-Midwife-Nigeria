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
public class FormatedRegRequest {
    private String msidn;
    private int gender;
    private String language;
    private int service;
    private int start_point;
    private int age;
    private int pregnant;
    private long provider_id;

    public FormatedRegRequest(String msidn, String gender, String language, String service, String start_point, String age, String pregnant, String provider_id) {
        this.msidn = msidn;
        this.gender =Integer.parseInt(gender);
        this.language = language;
        this.service = ("PRE".equals(service))? 1:2;
        this.start_point = Integer.parseInt(start_point);
        this.age = Integer.parseInt(age);
        this.pregnant = Integer.parseInt(pregnant);
        this.provider_id = Long.parseLong(provider_id);
    }

   public FormatedRegRequest(RegistrationRequest regRequest1) {
    this.msidn = regRequest1.getMsidn();
        this.gender =Integer.parseInt(regRequest1.getGender());
        this.language = regRequest1.getLanguage();
        this.service = ("PRE".equals(regRequest1.getService()))? 1:2;
        this.start_point = Integer.parseInt(regRequest1.getStart_poString());
        this.age = Integer.parseInt(regRequest1.getAge());
        this.pregnant = Integer.parseInt(regRequest1.getPregnant());
        this.provider_id = Long.parseLong(regRequest1.getProvider_id());
    }

    /**
     * @return the msidn
     */
    public String getMsidn() {
        return msidn;
    }

    /**
     * @param msidn the msidn to set
     */
    public void setMsidn(String msidn) {
        this.msidn = msidn;
    }

    /**
     * @return the gender
     */
    public int getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(int gender) {
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
    public int getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(int service) {
        this.service = service;
    }

    /**
     * @return the start_point
     */
    public int getStart_point() {
        return start_point;
    }

    /**
     * @param start_point the start_point to set
     */
    public void setStart_point(int start_point) {
        this.start_point = start_point;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the pregnant
     */
    public int getPregnant() {
        return pregnant;
    }

    /**
     * @param pregnant the pregnant to set
     */
    public void setPregnant(int pregnant) {
        this.pregnant = pregnant;
    }

    /**
     * @return the provider_id
     */
    public long getProvider_id() {
        return provider_id;
    }

    /**
     * @param provider_id the provider_id to set
     */
    public void setProvider_id(long provider_id) {
        this.provider_id = provider_id;
    }
}
