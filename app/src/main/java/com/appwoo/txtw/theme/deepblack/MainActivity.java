package com.appwoo.txtw.theme.deepblack;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.os.SystemProperties;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity {

    private EditText m_editText_Input;
    private Button m_button_Run;
    private final static String TAG = "my_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_editText_Input = (EditText)findViewById(R.id.editText_Input);
        m_button_Run = (Button)findViewById(R.id.button_Run);

        m_button_Run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                OnClick_run();
            }
        });
    }

    private void OnClick_run()
    {
        if(null == m_editText_Input) return;

        String strCmd = m_editText_Input.getText().toString();
        m_editText_Input.setText("");

        if(null == strCmd) return;

        Log.e(TAG, " strCmd : " + strCmd);

        String[] Vars = strCmd.split(" ");

        List lVars = new ArrayList();

        for(int i =0; i < Vars.length ; i++)
        {
            String strTmp = Vars[i].trim();
            if(!strTmp.isEmpty() )
            {
                lVars.add(strTmp);
                Log.e(TAG, "Vars : [" +  Vars[i] + "]");
            }
        }

        if(lVars.size() < 1) return;
        strCmd = (String)lVars.get(0);

        Log.e(TAG, " strCmd : " + strCmd);
        if(strCmd.equals("1") )
        {
            SystemProperties.set("persist.sys.usbdebugdisablelw", "1");
            Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 1);
        }
        else if(strCmd.equals("2") )
        {
            Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 0);
            SystemProperties.set("persist.sys.usbdebugdisablelw", "0");
        }
        else if(strCmd.equals("3") )
        {
            Settings.Global.putInt(getContentResolver(), Settings.Global.PACKAGE_VERIFIER_ENABLE, 0);
        }
        else if(strCmd.equals("4") )
        {
            if(lVars.size() < 2) return;

            String strVar = (String)lVars.get(1);

            Log.e(TAG, " strVar : " + strVar);

            Uri uri = Uri.parse("content://com.txtw.provider.scan.question");
            ContentValues values = new ContentValues();
            values.put("mark", strVar);

            getContentResolver().update(uri, values, " item = 1 ", null);
        }
        else if(strCmd.equals("100") )
        {
            Intent intent = new Intent(Intent.ACTION_MASTER_CLEAR);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            intent.putExtra(Intent.EXTRA_REASON, "Lw_Tools_diff");
            sendBroadcast(intent);
        }
        else if(strCmd.equals("1001") )
        {
            PackageManager packageManager = getPackageManager();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);

            List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);

            for(ResolveInfo ri : resolveInfo)
            {
                Log.e(TAG, "ri.activityInfo.packageName : " + ri.activityInfo.packageName);
            }
        }
        else if(strCmd.equals("1002") )
        {
            String filename = "screenshot.png";
            String mSavedPath = "/sdcard/" + filename;

            Log.e(TAG, mSavedPath);

            try {
                Runtime.getRuntime().exec("screencap -p " + mSavedPath);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            Log.e(TAG, "screencap -p " + mSavedPath);
        }
        else if(strCmd.equals("1003") )
        {
            final ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            @SuppressWarnings("deprecation")
            final List<ActivityManager.RecentTaskInfo> recentTasks =
                    am.getRecentTasks(20, ActivityManager.RECENT_IGNORE_UNAVAILABLE);

            for(ActivityManager.RecentTaskInfo rt:recentTasks )
            {
                Log.e(TAG, " : " + rt.persistentId);
                if (am != null) am.removeTask(rt.persistentId);
            }
        }
        else if(strCmd.equals("1004") )
        {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.Settings");
            int res = packageManager.getComponentEnabledSetting(componentName);
            if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                    || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                // 隐藏应用图标
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            } else {
                // 显示应用图标
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                        PackageManager.DONT_KILL_APP);
            }
        }
        else if(strCmd.equals("1005") )
        {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            int res = packageManager.getComponentEnabledSetting(componentName);
            if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                    || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                // 隐藏应用图标
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            } else {
                // 显示应用图标
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                        PackageManager.DONT_KILL_APP);
            }
        }
        else if(strCmd.equals("1006") )
        {

            int Uid  = getUserId();
            Log.e(TAG, "Uid : " + Uid);


            File dir = new File("/data/data/com.browser.txtw/databases");
            if(dir.exists() )
            {
                Log.e(TAG, "exists : " + dir + "   ok");
            }
            else
            {
                Log.e(TAG, "exists : " + dir + "   fail");
            }

            if(dir.isDirectory() )
            {
                Log.e(TAG, "isDirectory : " + dir + "   true");
            }
            else
            {
                Log.e(TAG, "isDirectory : " + dir + "   false");
            }

            //FileUtils.setPermissions("/data/data/com.browser.txtw/databases", 0777, -1, -1);


            File file = new File("/data/data/com.browser.txtw/databases/cc.db");
            if(file.exists() )
            {
                Log.e(TAG, "exists : " + file + "   ok");
            }
            else
            {
                Log.e(TAG, "exists : " + file + "   fail");
            }

            if(file.isFile() )
            {
                Log.e(TAG, "isFile : " + file + "   true");
            }
            else
            {
                Log.e(TAG, "isFile : " + file + "   false");
            }

            if (file.delete() )
            {
                Log.e(TAG, "del file : " + file + "   ok");
            }
            else
            {
                Log.e(TAG, "del file : " + file + "   fail");
            }
        }
        else if(strCmd.equals("1007") )
        {
            String cm = "ps /init";

            Log.e(TAG, "cm : " + cm);

            String memoryUsage = null;

            int ch;
            try {
                Process p = Runtime.getRuntime().exec(cm);
                InputStream in = p.getInputStream();
                StringBuffer sb = new StringBuffer(512);
                while ((ch = in.read()) != -1) {
                    sb.append((char) ch);
                }
                memoryUsage = sb.toString();
            } catch (IOException e) {
                Log.v(TAG, e.toString());
            }
            String[] poList = memoryUsage.split("\r|\n|\r\n");
            String memusage = poList[1].concat("\n");

            Log.e(TAG, "memusage : " + memusage);
        }
        else if(strCmd.equals("1008") )
        {
            int Uid  = getUserId();
            Log.e(TAG, "Uid : " + Uid);

            {
                //String cm = "reboot";
                //String cm = "mount";
                String cm = "mkdir /data/data/com.browser.txtw/files/yan";
                //String cm = "/data/local/hello";
                //String cm = "mount -o remount /system";
                //String cm = "top";

                Log.e(TAG, "cm : " + cm);

                String msg = null;

                int ch;
                try {
                    Process p = Runtime.getRuntime().exec(cm);
                    InputStream in = p.getInputStream();
                    InputStream err = p.getErrorStream();
                    StringBuffer sb = new StringBuffer(1024);

                    while ((ch = in.read()) != -1) {
                        sb.append((char) ch);
                    }

                    while ((ch = err.read()) != -1) {
                        sb.append((char) ch);
                    }

                    msg = sb.toString();
                } catch (IOException e) {
                    Log.v(TAG, e.toString());
                }

                Log.e(TAG, "msg : [" + msg + "]");
            }
        }
        else if(strCmd.equals("1009") )
        {
            //String dir = "/cache/one/two/three";
            String dir = "/data/data/com.browser.txtw/files/yanqiang";
            Log.e(TAG, "dir : " + dir);

            File file = new File(dir);
            boolean bMkdir = false;
            bMkdir = file.mkdirs();
            Log.e(TAG, "bMkdir : " + bMkdir);
        }
        else if(strCmd.equals("1010") )
        {
            Load load = new Load();

            int nTmp = load.addInt(2, 6);

            Log.e(TAG, "nTmp : " + nTmp);

            //Log.e(TAG, "Mkdir : " + load.Mkdir() );

            //Log.e(TAG, "Mount : " + load.Mount() );

            Log.e(TAG, "Fork : " + load.Fork() );
        }
        else if(strCmd.equals("1011") )
        {
            if(lVars.size() < 2) return;
            String strMode = (String)lVars.get(1);

            Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE, Integer.parseInt(strMode) );

//            Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
//            Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_BATTERY_SAVING);
//            Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_SENSORS_ONLY);
        }
        else if(strCmd.equals("1012") )
        {
            PackageManager packageManager = getPackageManager();
            List<PermissionGroupInfo> groupInfos = packageManager.getAllPermissionGroups(0);
            for(PermissionGroupInfo groupInfo : groupInfos)
            {
                Log.e(TAG, "groupInfo : " + groupInfo);
            }
        }
        else if(strCmd.equals("1013") )
        {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = null;

            try {
                packageInfo = packageManager.getPackageInfo("com.freeme.freemelite", PackageManager.GET_PERMISSIONS);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "No package : " + e);
            }

            if(null == packageInfo) return;

            PermissionInfo permissionInfo;

            for (String requestedPerm : packageInfo.requestedPermissions)
            {
//                Log.e(TAG, "requestedPerm : " + requestedPerm);

                try {
                    permissionInfo = packageManager.getPermissionInfo(requestedPerm, 0);
                } catch (PackageManager.NameNotFoundException e) {
//                    Log.e(TAG, "No permissionInfo : " + e);

                    continue;
                }

                if(null == permissionInfo)
                {
                    Log.e(TAG, "null == permissionInfo");

                    continue;
                }

//                Log.e(TAG, "protectionLevel : " + permissionInfo.protectionLevel);

                if (permissionInfo.protectionLevel == PermissionInfo.PROTECTION_DANGEROUS)
                {
                    Log.e(TAG, "requestedPerm : " + requestedPerm);
                }

                PackageItemInfo groupInfo = permissionInfo;
                if (permissionInfo.group != null) {
                    try {
                        groupInfo = getPackageManager().getPermissionGroupInfo(
                                permissionInfo.group, 0);
                    } catch (PackageManager.NameNotFoundException e) {
                    }

                    Log.e(TAG, "groupInfo.name : " + groupInfo.name);
                }
            }
        }
    }
}
