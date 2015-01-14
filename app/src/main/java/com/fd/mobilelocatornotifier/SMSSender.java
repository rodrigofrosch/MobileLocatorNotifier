package com.fd.mobilelocatornotifier;

import android.app.PendingIntent;
import android.telephony.SmsManager;

/**
 * Created by frog on 05/01/15.
 */
public class SMSSender {
    public static void sendSms(String number, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);
    }
    public static void sendSms(String destNumber, String srcNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destNumber, srcNumber, message, null, null);
    }
    public static void sendSms(String destNumber, String message, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destNumber, null, message, sentIntent, deliveryIntent);
    }
    public static void sendSms(String destNumber, String srcNumber,String message, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destNumber, srcNumber, message, sentIntent, deliveryIntent);
    }
    public static void sendSmsData(String destinationAddress, short destinationPort, byte[] data){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendDataMessage(destinationAddress, null, destinationPort, data, null, null);
    }
}