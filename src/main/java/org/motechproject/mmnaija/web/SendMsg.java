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
public class SendMsg {

    String message;

    public SendMsg(String message) {
        this.message = message;
    }

    private void printMessage() {
        System.out.println("Message: " + message);
    }

    public void invokeIndefinitePrintTask() {
        for (int i = 1; i <= 9; i++) {
            try {
                Thread.sleep(1 * 60 * 1000);
                System.out.println("Slept : " + i + " >> " + (i * 60 * 1000));
            } catch (InterruptedException e) {
                System.out.println("Caught an exception while sleeping.Description: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        new SendMsg("ok").invokeIndefinitePrintTask();
    }
}
