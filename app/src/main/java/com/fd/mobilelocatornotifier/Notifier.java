package com.fd.mobilelocatornotifier;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;
import com.kristijandraca.backgroundmaillibrary.Utils;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by frog on 10/01/15.
 */
public class Notifier {
    private Context context;
    private Intent thisIntent;
    private static  String TAG = "Notifier";
    private static final String VIDEOTAKED = "";
    private static String LATITUDE = "";
    private static String LONGITUDE = "";
    private Boolean networkState = false;
    private NetworkMobile net3g;
    private AudioRecorder audio;
    private String MAILTO = "";
    private String SUBJECT = "";
    private String BODY = "";
    private String USER = "";
    private String PASSWORD = "";
    private String PHOTOTAKED = "";
    private String AUDIOREC = "";
    private double BATTERYLEVELDANGER = 0.3;
    private Preferences p = new Preferences(context);
    private byte[] smsBody;

    public Notifier(Context context) {
        this.context = context;
    }

    public void notifyBatteryDanger() {
        if (p.get(p.PREFS_PHOTO, p.KEY_AUTH_PHOTO_TO_MAIL).equals("yes"))
            takePhoto();
        if (p.get(p.PREFS_AUDIO, p.KEY_AUTH_AUDIO_TO_MAIL).equals("yes"))
            audioRecorder();
        if (p.get(p.PREFS_3G, p.KEY_AUTH_AUTO3G).equals("yes")) {
            enableNetwork(true);
            getCoordinates();
            SystemClock.sleep(200);
            if (p.get(p.PREFS_MAIL, p.KEY_AUTH_MAIL).equals("yes"))
                sendingMail();
            if (p.get(p.PREFS_NUMBER_HELPER, p.KEY_AUTH_NUMBER_HELPER).equals("yes"))
                sendSMS();
            SystemClock.sleep(200);
            enableNetwork(false);
        } else {
            if (p.get(p.PREFS_NUMBER_HELPER, p.KEY_AUTH_NUMBER_HELPER).equals("yes"))
                sendSMSNoAccesGPSNow();
        }
        SystemClock.sleep(5000);
        shutdown();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void sendSMSNoAccesGPSNow() {
        SMSSender sms = new SMSSender();
        smsBody = getBodySMSNoAccessGPSNow().getBytes(Charset.forName("UTF-8"));
        String num = "";
        if (p.get(p.PREFS_NUMBER_HELPER, p.KEY_AUTH_SMS_CASH_TO_HELPER).equals("yes"))
            num = "9090" + p.get(p.PREFS_NUMBER_HELPER, p.KEY_NUMBER_HELPER);
        else
            num = p.get(p.PREFS_NUMBER_HELPER, p.KEY_NUMBER_HELPER);
        sms.sendSmsData(num, (short) 8901, smsBody);
    }

    private String getBodySMSNoAccessGPSNow() {
        //return String.format("The last location of number device %s is: %s, %s", p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM1), LATITUDE, LONGITUDE);
        return String.format(p.get(p.PREFS_SMS, p.KEY_SMS_NO_ACCESS_GPS_NOW_BODY),
                p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM1),
                p.get(p.PREFS_LOCATION, p.KEY_LAST_LATITUDE),
                p.get(p.PREFS_LOCATION, p.KEY_LAST_LONGITUDE));
    }

    public void notifyByRemoteSms(String srcNumber) {
        if (p.get(p.PREFS_PHOTO, p.KEY_AUTH_PHOTO_TO_MAIL).equals("yes"))
            takePhoto();
        if (p.get(p.PREFS_AUDIO, p.KEY_AUTH_AUDIO_TO_MAIL).equals("yes"))
            audioRecorder();
        if (p.get(p.PREFS_3G, p.KEY_AUTH_AUTO3G).equals("yes")) {
            enableNetwork(true);
            getCoordinates();
            SystemClock.sleep(200);
            checkBatteryLevel();
            if (p.get(p.PREFS_MAIL, p.KEY_AUTH_MAIL).equals("yes"))
                sendingMail();
            if (p.get(p.PREFS_NUMBER_HELPER, p.KEY_AUTH_NUMBER_HELPER).equals("yes"))
                sendSMSByOtherNumber(srcNumber);
            SystemClock.sleep(200);
            enableNetwork(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void sendSMSByOtherNumber(String srcNumber) {
        SMSSender sms = new SMSSender();
        smsBody = getBodySMS().getBytes(Charset.forName("UTF-8"));
        sms.sendSmsData(srcNumber, (short) 8901, smsBody);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void sendDangerSMS() {
        SMSSender sms = new SMSSender();
        smsBody = getBodyDangerSMS().getBytes(Charset.forName("UTF-8"));
        String num = "";
        if (p.get(p.PREFS_NUMBER_HELPER, p.KEY_AUTH_SMS_CASH_TO_HELPER).equals("yes"))
            num = "9090" + p.get(p.PREFS_NUMBER_HELPER, p.KEY_NUMBER_HELPER);
        else
            num = p.get(p.PREFS_NUMBER_HELPER, p.KEY_NUMBER_HELPER);
        sms.sendSmsData(num, (short) 8901, smsBody);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void sendSMS() {
        SMSSender sms = new SMSSender();
        smsBody = getBodySMS().getBytes(Charset.forName("UTF-8"));
        String num = "";
        if (p.get(p.PREFS_NUMBER_HELPER, p.KEY_AUTH_SMS_CASH_TO_HELPER).equals("yes"))
            num = "9090" + p.get(p.PREFS_NUMBER_HELPER, p.KEY_NUMBER_HELPER);
        else
            num = p.get(p.PREFS_NUMBER_HELPER, p.KEY_NUMBER_HELPER);
        sms.sendSmsData(num, (short) 8901, smsBody);
    }

    private String getBodySMS() {
        //return String.format("The location of number device %s is: %s, %s", p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM1), LATITUDE, LONGITUDE);
        return String.format(p.get(p.PREFS_SMS, p.KEY_SMS_BODY), p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM1), LATITUDE, LONGITUDE);
    }

    private String getBodyDangerSMS(){
        String numbers = "";
        if (!p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM1).equals(""))
            numbers += p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM1);
        if (!p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM2).equals(""))
            numbers += ", " + p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM2);
        if (!p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM3).equals(""))
            numbers += ", " + p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM3);
        if (!p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM4).equals(""))
            numbers += ", " + p.get(p.PREFS_ALL_NUMBERS, p.KEY_NUMBER_SIM4);
        //return String.format("Check if one of the numbers: %s have changed in your device. Make sure your friend lost cell.", numbers);
        return String.format(p.get(p.PREFS_NUMBER_HELPER, p.KEY_SMS_DANGER_BODY), numbers);
    }

    private void shutdown() {
        try {
            Process proc = Runtime.getRuntime()
                    .exec(new String[]{ "su", "-c", "reboot -p" });
            proc.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getCoordinates() {
        GPSTracker gps = new GPSTracker(context.getApplicationContext(), "", "");
        LATITUDE = Double.toString(gps.getLatitude());
        LONGITUDE = Double.toString(gps.getLongitude());
        gps.stopUsingGPS();
        gps = null;
        Log.d(TAG, "Get Coordinates from GPS - Auto Enable/Disable.");
    }

    private void audioRecorder(){
        audio = new AudioRecorder();
        audio.onRecord(true);
        SystemClock.sleep(5000);
        AUDIOREC = audio.onRecord(false);
    }

    private void checkBatteryLevel(){
        Intent batteryIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int rawlevel = batteryIntent.getIntExtra("level", -1);
        double scale = batteryIntent.getIntExtra("scale", -1);
        if (rawlevel >= 0 && scale > 0) {
            BATTERYLEVELDANGER = rawlevel / scale;
        }
    }

    private void takePhoto() {
        try {
            CameraBackground camera = new CameraBackground(context.getApplicationContext(), Application.class.getName());
            camera.Type = CameraBackground.MEDIA_TYPE_IMAGE;
            PHOTOTAKED = camera.takePicture();
            Log.d(TAG, "TakePhoto");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendingMail(){
        Log.d(TAG, "SendMail");
        configureMail();
        BackgroundMail bm = new BackgroundMail(context.getApplicationContext());
        bm.setGmailUserName(USER);
        bm.setGmailPassword(Utils.decryptIt(PASSWORD));
        bm.setMailTo(MAILTO);
        bm.setFormSubject(SUBJECT);
        bm.setFormBody(BODY);
        bm.setAttachment(PHOTOTAKED);
        bm.setAttachment(AUDIOREC);
        bm.send();
    }

    private void configureMail() {
        Preferences pref = new Preferences(context.getApplicationContext());
        Date date = new Date();
        USER = pref.get(pref.PREFS_MAIL, pref.KEY_MAIL_USER);
        PASSWORD = pref.get(pref.PREFS_MAIL, pref.KEY_MAIL_PASS);
        MAILTO = pref.get(pref.PREFS_MAIL, pref.KEY_MAIL_MAILTO);
        SUBJECT = pref.get(pref.PREFS_MAIL, pref.KEY_MAIL_SUBJECT);

        //format body mail
        BODY = "Last coordinate: lat/lng " + LATITUDE + ", " + LONGITUDE + "\n" +
                "Date/Time: " + date.getDate() + " " + date.getTime() + "\n" +
                "Battery level: " + Double.toString(BATTERYLEVELDANGER) + "\n" +
                "Photo is attached." + "\n" +
                "Video(use app to convert this text as mp4): " + VIDEOTAKED;
    }

    private void enableNetwork(Boolean state){
        try {
            net3g = new NetworkMobile(new Application().getApplicationContext());
            if (state) {
                try {
                    net3g.turn3GOff();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                state = false;
            } else {
                try {
                    net3g.turn3GOn();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                state = true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        finally {
            if (net3g != null){
                net3g = null;
            }
        }
    }
}
