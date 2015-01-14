/*
Usage:


 network.turn3GOn();
 network.turn3GOff();

 AppManifest =
 <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 <uses-permission android:name="android.permission.MODIFY_PHONE_STATE" />
 */

package com.fd.mobilelocatornotifier;
import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by rfrosch on 10/01/2015.
 */
public class NetworkMobile {
    private Context context;
    private final Method setMobileDataEnabledMethod;
    private final Object iConnectivityManager;
    private final Class iConnectivityManagerClass;
    private final Field iConnectivityManagerField;
    private final Class conmanClass;
    private final ConnectivityManager conman;

    public NetworkMobile(Context context) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException{
        this.context = context;
        conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        conmanClass = Class.forName(conman.getClass().getName());
        iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        iConnectivityManager = iConnectivityManagerField.get(conman);
        iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);
    }

    public void turn3GOn() throws InvocationTargetException, IllegalAccessException {
        setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
    }

    public void turn3GOff() throws InvocationTargetException, IllegalAccessException {
        setMobileDataEnabledMethod.invoke(iConnectivityManager, false);
    }

}

