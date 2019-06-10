package com.appwoo.txtw.theme.deepblack.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
