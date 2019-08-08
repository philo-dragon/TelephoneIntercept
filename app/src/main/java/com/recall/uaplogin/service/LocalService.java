package com.recall.uaplogin.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.telephony.IMyAidlInterface;
import com.recall.uaplogin.MainActivity;

/**
 * @author chaochaowu
 */
public class LocalService extends Service {

    private MyBinder mBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IMyAidlInterface iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                Log.i("LocalService", "connected with " + iMyAidlInterface.getServiceName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Intent intent = new Intent(LocalService.this, RemoteService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                 //适配8.0机制
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            bindService(intent,connection, Context.BIND_IMPORTANT);
            createMainActivity();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startService(new Intent(LocalService.this,RemoteService.class));
        bindService(new Intent(this,RemoteService.class),connection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new MyBinder();
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(LocalService.this, RemoteService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            //适配8.0机制
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        bindService(intent,connection, Context.BIND_IMPORTANT);
    }

    private class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public String getServiceName() throws RemoteException {
            return LocalService.class.getName();
        }

    }

    private void createMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}