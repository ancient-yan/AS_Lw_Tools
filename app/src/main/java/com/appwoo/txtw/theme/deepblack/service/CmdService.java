package com.appwoo.txtw.theme.deepblack.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.mia.MiaMdmPolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;

import com.appwoo.txtw.theme.deepblack.utils.ReflectionEX;
import com.appwoo.txtw.theme.deepblack.utils.Tools;

import java.io.IOException;
import java.util.List;

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

            case 5: {
                UserManager userManager = (UserManager) getSystemService(Context.USER_SERVICE);
                List<UserHandle> users = userManager.getUserProfiles();

                Log.d(TAG, "users : " + users);

                LauncherApps launcherApps = (LauncherApps) getSystemService(Context.LAUNCHER_APPS_SERVICE);
                final List<LauncherActivityInfo> apps = launcherApps.getActivityList(null, users.get(1));

                for (LauncherActivityInfo app : apps) {
                    Log.d(TAG, "app : " + app.getComponentName());
                }
            }
            break;

            case 6: {
                PackageManager packageManager = getPackageManager();

                Intent intent_2 = new Intent(Intent.ACTION_MAIN);
                intent_2.addCategory(Intent.CATEGORY_LAUNCHER);

                List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent_2,
                        PackageManager.MATCH_ALL);

                for (ResolveInfo ri : resolveInfo) {
                    Log.d(TAG, "ri.activityInfo.packageName : " + ri.activityInfo.packageName);
                }
            }
            break;

            case 7: {
                ReflectionEX.ShowAllMethods(MiaMdmPolicyManager.class);
            }
            break;

            case -1: {
            }
            break;
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
