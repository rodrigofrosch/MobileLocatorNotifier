package com.fd.mobilelocatornotifier;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by rfrosch on 11/01/2015.
 */
public class Telephony {
    public static String getSimNumber(Context context){
        TelephonyManager telMgr =
                (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telMgr.getLine1Number();
    }
}
