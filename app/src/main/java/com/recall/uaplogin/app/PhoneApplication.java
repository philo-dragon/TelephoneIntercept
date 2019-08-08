package com.recall.uaplogin.app;

import android.app.Application;
import android.content.IntentFilter;
import com.qw.soul.permission.SoulPermission;
import com.recall.uaplogin.receiver.PhoneReceiver;

public class PhoneApplication extends Application {

    public static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        SoulPermission.init(this);
        registerReceiver();
    }

    private void registerReceiver() {
        registerReceiver(new PhoneReceiver(), new IntentFilter(PhoneReceiver.PHONE_ACTION));
    }

    public static Application getApplication() {
        return sApplication;
    }
}
