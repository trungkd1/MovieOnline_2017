package com.workhard.movieonline.rest;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

//import com.jaredrummler.android.device.DeviceName;

import java.util.Locale;

/**
 * Created by HoanNguyen on 30/04/2017
 */

public class ApiInitialization {
    private Context context;

    public ApiInitialization(Context context) {
        this.context = context;
    }

    public String getDeviceToken() {
        return "";
    }

    public String getDeviceId() {
        return Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getOsType() {
        return "android";
    }

    public String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public String getHardware() {
        return Build.MODEL;
    }

    public String getDeviceName() {
        return "";//DeviceName.getDeviceName();
    }

    public String getAppVersion(Context context) {
        PackageInfo pInfo      = null;
        String      appVersion = "";
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appVersion;
    }

    public String getAppName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int             stringId        = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public String getOsName() {
        return Build.VERSION.BASE_OS;
    }

    public String getOsVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }
}