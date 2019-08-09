package com.recall.uaplogin.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.ITelephony;
import com.recall.uaplogin.utils.ContactsUtil;
import com.recall.uaplogin.utils.PhoneUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PhoneReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneReceiver";
    public static final String PHONE_ACTION = "android.intent.action.PHONE_STATE";
    public static final String PHONE_CALL = Intent.ACTION_NEW_OUTGOING_CALL;
    private TelephonyManager telephonyManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (TextUtils.isEmpty(action)) {
            return;
        }

        if (!PHONE_ACTION.equals(action)) {
            return;
        }


        if (!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {//去电
            //查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电
            telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            //设置一个监听器
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //state 当前状态 incomingNumber,貌似没有去电的API
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    System.out.println("挂断");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("响铃:来电号码" + incomingNumber);
                    //输出来电号码
                    try {
                        if (!ContactsUtil.constantContacts(incomingNumber)) {
                            //if (incomingNumber.equals("18256682469")) {
                            PhoneUtils.getITelephony(telephonyManager).endCall();
                            //rejectCall();
                            release();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void release() {
        TelephonyManager tm = telephonyManager;
        try {
            Method getITelephonyMethod = tm.getClass().getDeclaredMethod("getITelephony");// 强行调用getITelephony()方法。这里可以通过getDeclaredMethod获得TelephonyManager类中
            // 的所有方法进行使用
            getITelephonyMethod.setAccessible(true);
            Object endCallMethod = getITelephonyMethod.invoke(tm);
            Method endcall = endCallMethod.getClass().getMethod("endCall");
            endcall.invoke(endCallMethod);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void rejectCall() {
        try {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.endCall();
        } catch (NoSuchMethodException e) {
            Log.d(TAG, "", e);
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "", e);
        } catch (Exception e) {
        }
    }


}