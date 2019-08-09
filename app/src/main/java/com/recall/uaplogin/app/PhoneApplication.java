package com.recall.uaplogin.app;

import android.app.Application;
import android.content.IntentFilter;
import com.recall.uaplogin.receiver.PhoneReceiver;
import com.recall.uaplogin.receiver.SmsReceiver;

public class PhoneApplication extends Application {

    public static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(PhoneReceiver.PHONE_ACTION);
        intentFilter.addAction(PhoneReceiver.PHONE_CALL);
        registerReceiver(new PhoneReceiver(), intentFilter);

        intentFilter = new IntentFilter(SmsReceiver.SMS_ACTION);
        registerReceiver(new SmsReceiver(), intentFilter);
    }

    public static Application getApplication() {
        return sApplication;
    }
}
