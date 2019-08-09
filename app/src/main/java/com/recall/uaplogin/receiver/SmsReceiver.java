package com.recall.uaplogin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.recall.uaplogin.utils.ContactsUtil;
import com.recall.uaplogin.utils.MobileUtil;

public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        // 1、接收短信协议
        Bundle bundle = intent.getExtras();
        // 2.通过Bundle取值 ，短信以pdu形式存储数据，s表示很多的意思，以键值对的形式保存数据
        Object[] objs = (Object[]) bundle.get("pdus");
        for (Object object : objs) {
            // 3.获得短信对象
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
            // 5.短信拦截，设置短信黑名单，当发送短信的号码是110时进行拦截，不将短信发送到手机
            //收到短信时，系统会发一个有序广播，默认优先级是500，我们可以设置短信窃听器的广播优先级为1000
            //if (!ContactsUtil.constantContacts(sms.getOriginatingAddress())) {
            if (MobileUtil.getNumber(sms.getOriginatingAddress()).equals("18256682469")) {
                // 终止广播
                abortBroadcast();
            }

        }

    }

}