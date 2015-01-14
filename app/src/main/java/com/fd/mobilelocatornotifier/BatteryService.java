package com.fd.mobilelocatornotifier;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by rfrosch on 10/01/2015.
 */
public class BatteryService extends Service{

    private float levelBattery;
    private int scale;
    private Intent batteryStatus;
    private IntentFilter ifilter;
    private static  String TAG = "BatteryService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        BatteryCheckAsync checkAsync;
        checkAsync = new BatteryCheckAsync();
        checkAsync.execute();

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class BatteryCheckAsync extends AsyncTask<Void, Void, Float> {

        @Override
        protected Float doInBackground(Void... arg0) {
            //Battery State check - create log entries of current battery state
            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            batteryStatus = BatteryService.this.registerReceiver(null, ifilter);

            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            Log.i("BatteryInfo", "Battery is charging: " + isCharging);

            levelBattery = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            Log.i("BatteryInfo", "Battery charge level: " + (levelBattery / scale));
            Float aux = (levelBattery / scale);
            if (aux <= 0.05){
                new Notifier(getApplicationContext()).notifyBatteryDanger();
            }
            return levelBattery;
        }

        protected void onPostExecute(){
            //BatteryService.this.stopSelf();
        }
    }


}
