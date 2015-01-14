package com.fd.mobilelocatornotifier;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rfrosch on 10/01/2015.
 */
public class Preferences {


    public static final String PREFS_LOCATION = "Location";
    public static final String KEY_LAST_LATITUDE = "LastLatitude";
    public static final String KEY_LAST_LONGITUDE = "LastLongitude";


    public final String PREFS_CONFIG = "Config";
    public final String KEY_HAS_INIT_CONFIG = "InitConfig";

    public final String PREFS_SMS = "Sms";
    public final String KEY_SMS_BODY = "SmsBody";
    public final String KEY_SMS_NO_ACCESS_GPS_NOW_BODY = "SmsNoAccessGpsNow";

    public final String PREFS_MAIL = "Mail";
    public final String KEY_AUTH_MAIL = "AuthMail";
    public final String KEY_MAIL_MAILTO = "MailTo";
    public final String KEY_MAIL_SUBJECT = "MailSubject";
    public final String KEY_MAIL_USER = "MailUser";
    public final String KEY_MAIL_PASS = "MailPass";

    public final String PREFS_PHOTO = "Photo";
    public final String KEY_AUTH_PHOTO_TO_MAIL = "AuthPhotoToMail";

    public final String PREFS_AUDIO = "Audio";
    public final String KEY_AUTH_AUDIO_TO_MAIL = "AuthAudioToMail";

    public final String PREFS_3G = "Prefs3g";
    public final String KEY_AUTH_AUTO3G = "AuthAuto3g";

    public final String PREFS_NUMBER_HELPER = "NumberHelper";
    public final String KEY_AUTH_NUMBER_HELPER = "AuthNumberHelper";
    public final String KEY_AUTH_SMS_CASH_TO_HELPER = "SmsCashToHelper";
    public final String KEY_NUMBER_HELPER = "GetNumberHelper";
    public final String KEY_SMS_DANGER_BODY = "SmsDangerBody";

    public final String PREFS_ALL_NUMBERS = "AllNumbers";
    public final String KEY_NUMBER_SIM1 = "GetNumberSim1";
    public final String KEY_NUMBER_SIM2 = "GetNumberSim2";
    public final String KEY_NUMBER_SIM3 = "GetNumberSim3";
    public final String KEY_NUMBER_SIM4 = "GetNumberSim4";

    public final String KEY_PERSON_ICON = "PersonIcon";//get directory to png;

    public Preferences(Context context) {
        this.context = context;
    }

    private Context context;


    public String get(String prefsName, String key){
        // Restore preferences
        SharedPreferences settings = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    public void set(String prefsName, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(prefsName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        // Commit the edits!
        editor.commit();
    }
}
