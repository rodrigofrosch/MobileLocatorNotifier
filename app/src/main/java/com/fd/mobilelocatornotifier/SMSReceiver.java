/*

User with sms.sendSmsBinary()

Register receiver in appmanifest.xml

        <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
        <receiver android:name=".SMSReceiver" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.intent.action.DATA_SMS_RECEIVED" />
                <data android:scheme="sms" />
                <data android:port="8901" />
            </intent-filter>
        </receiver>
*/


package com.fd.mobilelocatornotifier;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.gsm.SmsMessage;

/**
 * Created by rfrosch on 09/01/2015.
 */
public class SMSReceiver extends BroadcastReceiver {

    private SmsMessage msg;
    private char[] sequenceCharMsgBody;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Object[] rawMsgs=(Object[])intent.getExtras().get("pdus");
            for (Object raw : rawMsgs) {
                msg = SmsMessage.createFromPdu((byte[])raw);
                if (checkTokenPass()) {
                    switch (msg.getMessageBody()) {
                        case "CHECK":
                            if (!msg.getOriginatingAddress().isEmpty())
                                new Notifier(context.getApplicationContext()).notifyByRemoteSms(msg.getOriginatingAddress());
                            msg = null;
                            break;
                    }
                }
            }
        }
    }

    private boolean checkTokenPass() {
        String token = "";
        sequenceCharMsgBody = msg.getMessageBody().toCharArray();
        for (int i = 0; i < sequenceCharMsgBody.length; i++){
            if (i>5){
                if (sequenceCharMsgBody[i] == ';') break;
                token+=sequenceCharMsgBody[i];
            }
        }
        return Token.tokenIsValid(token);
    }
}
