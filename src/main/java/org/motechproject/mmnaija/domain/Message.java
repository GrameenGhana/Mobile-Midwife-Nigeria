/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 *
 * @author seth
 */
@Entity
public class Message {

    @Field(name = "message_key")
    private String messageKey;
    @Field(name = "content_id")
    private int contentId;
    @Field(name = "content_type")
    private String contentType;

    @Field(name = "language")
    private String language;
    @Field
    private String content;
    @Field
    private int length;
    @Field
    private int order;
    @Field
    private String tag;

    /**
     * @return the messageKey
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * @param messageKey the messageKey to set
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * @return the contentId
     */
    public int getContentId() {
        return contentId;
    }

    /**
     * @param contentId the contentId to set
     */
    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the Language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param Language the Language to set
     */
    public void setLanguage(String Language) {
        this.language = Language;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
 public Message(String messageKey, int contentId, String contentType, String language, String content, int length, int order, String tag) {
        this.messageKey = messageKey;
        this.contentId = contentId;
        this.contentType = contentType;
        this.language = language;
        this.content = content;
        this.length = length;
        this.order = order;
        this.tag = tag;
    }
    
    
}

   
