package com.recall.uaplogin.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.recall.uaplogin.utils.ContactsUtil;
import com.recall.uaplogin.utils.MobileUtil;
import com.recall.uaplogin.utils.PhoneUtils;

public class PhoneReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneReceiver";
    public static final String PHONE_ACTION = "android.intent.action.PHONE_STATE";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (TextUtils.isEmpty(action)) {
            return;
        }

        if (!PHONE_ACTION.equals(action)) {
            return;
        }


        String phoneNo = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

        switch (tm.getCallState()) {
            case TelephonyManager.CALL_STATE_OFFHOOK:// 电话打进来接通状态；电话打出时首先监听到的状态。
                Log.i(TAG, "CALL_STATE_OFFHOOK");
                break;
            case TelephonyManager.CALL_STATE_RINGING:// 电话打进来状态
                Log.i(TAG, "CALL_STATE_RINGING");
                break;
            case TelephonyManager.CALL_STATE_IDLE:// 不管是电话打出去还是电话打进来都会监听到的状态。
                Log.i(TAG, "CALL_STATE_IDLE");
                try {
                    if (!ContactsUtil.constantContacts(phoneNo)) {
                        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        PhoneUtils.getITelephony(telephonyManager).endCall();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


} 