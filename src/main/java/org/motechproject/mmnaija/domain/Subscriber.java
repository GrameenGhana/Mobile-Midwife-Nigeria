/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.domain;

import java.io.Serializable;
import javax.jdo.annotations.Unique;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 *
 * @author seth
 */
@Entity
public class Subscriber extends MdsEntity implements Serializable {

    @Field
//    @Unique
    private String msisdn;
    @Field
    private int gender;
    @Field
    private int age;
    @Field
    private int pregnant;
    @Field
    private String language_id;
    @Field
    private Long provider;

    public Subscriber(String msisdn, int gender, int age, int pregnant, Language language) {
        this.msisdn = msisdn;
        this.gender = gender;
        this.age = age;
        this.pregnant = pregnant;
        this.language_id = language.getIsoCode();
    }

    public Subscriber(String msisdn, int gender, int age, int pregnant, Language language, Long provider) {
        this.msisdn = msisdn;
        this.gender = gender;
        this.age = age;
        this.pregnant = pregnant;
        this.language_id = language.getIsoCode();
        this.provider = provider;
    }

    /**
     * @return the msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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
     * @return the language_id
     */
    public String getLanguage_id() {
        return language_id;
    }

    /**
     * @param language_id the language_id to set
     */
    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

}
