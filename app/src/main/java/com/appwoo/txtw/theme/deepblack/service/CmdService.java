package com.appwoo.txtw.theme.deepblack.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.appwoo.txtw.theme.deepblack.utils.Tools;

import java.io.IOException;

public class CmdService extends Service {
    private static final  String TAG = "my_log";

    public CmdService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "CmdService.onStartCommand");

        int nCmd = intent.getIntExtra("cmd", -1);
        Log.e(TAG, "CmdService.onStartCommand.nCmd : " + nCmd);

        switch (nCmd) {
            case 1: {
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

                Log.d(TAG, "runningActivity : " + runningActivity);
            }
            break;

            case 2: {
                String cm = "input keyevent  KEYCODE_BACK";

                try {
                    Runtime.getRuntime().exec(cm);
                } catch (IOException e) {
                    Log.e(TAG, "IOException : " + e);
                }
            }
            break;

            case 3: {
                Tools.getInstalled3PApp(this);
            }
            break;

            case 4: {
                getPackageManager().setApplicationEnabledSetting("com.unionpay", PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
                getPackageManager().setApplicationEnabledSetting("com.tencent.mobileqq", PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
                getPackageManager().setApplicationEnabledSetting("com.tencent.mm", PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
                getPackageManager().setApplicationEnabledSetting("com.eg.android.AlipayGphone", PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
            }
            break;
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
