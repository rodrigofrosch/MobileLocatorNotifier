package com.fd.mobilelocatornotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rfrosch on 10/01/2015.
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(ACTION_BOOT)) {
            // This intent action can only be set by the Android system after a boot
            Intent monitorIntent = new Intent(context, BatteryService.class);
            context.startService(monitorIntent);
        }
    }
}