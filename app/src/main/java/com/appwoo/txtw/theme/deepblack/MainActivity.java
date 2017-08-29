package com.appwoo.txtw.theme.deepblack;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.persistentdata.PersistentDataBlockManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import android.os.SystemProperties;
import android.provider.Settings;
import com.appwoo.txtw.theme.deepblack.Tools;
import android.os.LwGlobal;

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
                //String cm = "mkdir /data/data/com.browser.txtw/files/yan";
                //String cm = "/data/local/hello";
                //String cm = "mount -o rw -o remount /system";
                //String cm = "top";
                //String cm = "ls -l -Z /cache/recovery/";
                //String cm = "ls -l -Z /data/data/com.browser.txtw/";
                String cm = "su 1000,1000 top";

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

            int i = 0;

            for (String requestedPerm : packageInfo.requestedPermissions)
            {
//                Log.e(TAG, "requestedPerm : " + requestedPerm);

                final boolean granted = (packageInfo.requestedPermissionsFlags[i]
                        & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0;

                i++;

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
                    Log.e(TAG, "=====================================================================================");

                    Log.e(TAG, "requestedPerm : " + requestedPerm);
                    Log.e(TAG, "granted : " + granted);
                    Log.e(TAG, "packageName : " + permissionInfo.packageName);
                    Log.e(TAG, "name : " + permissionInfo.name);
                    final int appOp = AppOpsManager.permissionToOpCode(permissionInfo.name);
                    Log.e(TAG, "appOp : " + appOp);
                    final boolean appOpAllowed = getSystemService(AppOpsManager.class).checkOp(appOp,
                            packageInfo.applicationInfo.uid, packageInfo.packageName) == AppOpsManager.MODE_ALLOWED ;
                    Log.e(TAG, "appOpAllowed : " + appOpAllowed);
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
        else if(strCmd.equals("1014") )
        {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = null;

            try {
                packageInfo = packageManager.getPackageInfo("com.freeme.freemelite", PackageManager.GET_PERMISSIONS);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "No package : " + e);
            }

            final int uid = packageInfo.applicationInfo.uid;

            Log.e(TAG, "uid : " + uid);

            final int appOp = AppOpsManager.permissionToOpCode("android.permission.ACCESS_FINE_LOCATION");
            Log.e(TAG, "appOp : " + appOp);
            //getSystemService(AppOpsManager.class).setUidMode(appOp, uid, AppOpsManager.MODE_ALLOWED);
            getSystemService(AppOpsManager.class).setUidMode(appOp, uid, AppOpsManager.MODE_IGNORED);
        }
        else if(strCmd.equals("1015") )
        {
            PackageManager packageManager = getPackageManager();
            for (PackageInfo pInfo : packageManager.getInstalledPackages(0) )
            {
                Log.e(TAG, "packageName : " + pInfo.packageName);

                if("com.android.ServiceMenu".equals(pInfo.packageName) )
                {
                    Log.e(TAG, "equals : " + pInfo.packageName);
                    Intent query = new Intent();
//                    query.setAction("android.intent.action.MAIN");
                    query.setPackage(pInfo.packageName);
                    List<ResolveInfo> activities = packageManager.queryIntentActivities(query, 0);
                    for (ResolveInfo info : activities)
                    {
                        Log.e(TAG, "name : " + info.activityInfo.name);
                    }
                }
            }
        }
        else if(strCmd.equals("1016") )
        {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = null;

            try {
                packageInfo = packageManager.getPackageInfo("com.android.settings", PackageManager.GET_SIGNATURES);
            //      packageInfo = packageManager.getPackageInfo("com.android.mms", PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Signature signatures[] = packageInfo.signatures;

            for (Signature signature : signatures)
            {
                Log.e(TAG, "signature : [" + signature.toCharsString() + "]" );
                Tools.parseSignature(signature.toByteArray() );
            }
        }
        else if(strCmd.equals("1017") )
        {
            PackageManager packageManager = getPackageManager();
            final Intent verification = new Intent(Intent.ACTION_BOOT_COMPLETED);

            final List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(verification, 0);

            Log.e(TAG, "receivers.size : " + receivers.size() );

            for (ResolveInfo info : receivers)
            {
                Log.e(TAG, "info : " + info);
            }
        }
        else if(strCmd.equals("1018") )
        {
            final String PACKAGE_MIME_TYPE = "application/vnd.android.package-archive";

            PackageManager packageManager = getPackageManager();

            final Intent verification = new Intent(Intent.ACTION_PACKAGE_NEEDS_VERIFICATION);
            verification.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            verification.setDataAndType(Uri.fromFile(new File("/test.apk")),
                    PACKAGE_MIME_TYPE);

            final List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(verification, 0);

            Log.e(TAG, "receivers.size : " + receivers.size() );

            for (ResolveInfo info : receivers)
            {
                Log.e(TAG, "info : " + info);
            }
        }
        else if(strCmd.equals("1019") )
        {
            final String PACKAGE_MIME_TYPE = "application/vnd.android.package-archive";

            PackageManager packageManager = getPackageManager();
            final Intent verification = new Intent(Intent.ACTION_PACKAGE_NEEDS_VERIFICATION);
            verification.setType(PACKAGE_MIME_TYPE);

            final List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(verification, 0);

            Log.e(TAG, "receivers.size : " + receivers.size() );

            for (ResolveInfo info : receivers)
            {
                Log.e(TAG, "info : " + info);
            }
        }
        else if(strCmd.equals("1020") )
        {
            File file = new File("/sdcard/Download/baidu.apk");
            if(file.exists() )
            {
                Log.e(TAG, "exists : " + file + "   ok");
            }
            else
            {
                Log.e(TAG, "exists : " + file + "   fail");
            }

            PackageParser.ApkLite apk = null;
            try {
                apk = PackageParser.parseApkLite(file, PackageParser.PARSE_COLLECT_CERTIFICATES);
            } catch (PackageParser.PackageParserException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "apk.packageName : " + apk.packageName);
            Log.e(TAG, "apk.codePath : " + apk.codePath);
            Log.e(TAG, "apk.splitName : " + apk.splitName);
            Log.e(TAG, "apk.versionCode : " + apk.versionCode);

            Signature signatures[] = apk.signatures;
            for (Signature signature : signatures)
            {
                Log.e(TAG, "signature : [" + signature.toCharsString() + "]" );
                Tools.parseSignature(signature.toByteArray() );
            }
        }
        else if(strCmd.equals("1021") )
        {
            IPackageManager packageManager =
                    IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
            PackageDeleteObserver observer = new PackageDeleteObserver();

            try {
                packageManager.deletePackageAsUser("com.softboy.swf20", observer, 0, 0);
//                packageManager.deletePackageAsUser("com.appwoo.txtw.theme.deepblack", observer, 0, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else if(strCmd.equals("1022") )
        {
            PackageManager packageManager = getPackageManager();
            PackageInstallObserver observer = new PackageInstallObserver();

            Uri uri = Uri.fromFile(new File("/sdcard/Download/zs_flash.apk") );

            Log.e(TAG, "uri : " + uri);

            packageManager.installPackage(uri, observer,PackageManager.INSTALL_REPLACE_EXISTING, "");
        }
        else if(strCmd.equals("1023") )
        {
            PackageManager packageManager = getPackageManager();
            boolean bRet = false;

            try {
                Method method = PackageManager.class.getDeclaredMethod("LwSetPermission", new Class[]{String.class, String.class, Boolean.TYPE});
                method.setAccessible(true);
                try {
                    bRet = (boolean)method.invoke(packageManager, new Object[]{"com.browser.txtw", "android.permission.INTERNET", Boolean.valueOf(false)});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            Log.e(TAG,  "bRet : " + bRet);
        }
        else if(strCmd.equals("1024") )
        {
            PackageManager packageManager = getPackageManager();
            boolean bRet = false;

            try {
                Method method = PackageManager.class.getDeclaredMethod("LwSetPermission", new Class[]{String.class, String.class, Boolean.TYPE});
                method.setAccessible(true);
                try {
                    bRet = (boolean)method.invoke(packageManager, new Object[]{"com.browser.txtw", "android.permission.INTERNET", Boolean.valueOf(true)});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            Log.e(TAG,  "bRet : " + bRet);
        }
        else if(strCmd.equals("1025") )
        {
            PackageManager packageManager = getPackageManager();
            boolean bRet = false;

          //  bRet = packageManager.LwSetPermission("com.browser.txtw", "android.permission.INTERNET", false);
            bRet = packageManager.LwSetPermission("com.lw.permissionsmanager", "android.permission.READ_CONTACTS", false);

            Log.e(TAG,  "bRet : " + bRet);
        }
        else if(strCmd.equals("1026") )
        {
            PackageManager packageManager = getPackageManager();
            int nRet = 100;

          //  nRet = packageManager.LwGetPermission("com.browser.txtw", "android.permission.INTERNET");
            nRet = packageManager.LwGetPermission("com.lw.permissionsmanager", "android.permission.READ_CONTACTS");

            Log.e(TAG,  "nRet : " + nRet);
        }
        else if(strCmd.equals("1027") )
        {
            String dbName;
            dbName = "PermissionsManager.db";
            try {
                Tools.copyFile(getAssets().open(dbName), "/data/data/android/" + dbName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(strCmd.equals("1028") )
        {
            SQLiteDatabase db;
            db = new DBOpenHelper(this, "/data/data/android/PermissionsManager.db").open();
            db.execSQL("delete from permissions_manager");
            db.close();
        }
        else if(strCmd.equals("1029") )
        {
            SQLiteDatabase db;
            db = SQLiteDatabase.openDatabase("/data/data/android/PermissionsManager.db",
                    null, SQLiteDatabase.OPEN_READWRITE);
            db.execSQL("delete from permissions_manager");
            db.close();
        }
        else if(strCmd.equals("1030") )
        {
            String strTable_Name = "permissions_manager";
            File file = new File("/data/data/android/PermissionsManager.db");
            SQLiteDatabase db = null;
            if(file.exists() )
            {
                Log.e(TAG, "exists : " + file + "   ok");
                db = SQLiteDatabase.openDatabase("/data/data/android/PermissionsManager.db",
                        null, SQLiteDatabase.OPEN_READWRITE);
            }
            else
            {
                Log.e(TAG, "exists : " + file + "   fail");
                db = SQLiteDatabase.openOrCreateDatabase(file, null);
                String CREATE_TABLE_PERMISSIONS_MANAGER =
                        "CREATE TABLE " + strTable_Name +
                                "(packageName STRING NOT NULL, " +
                                "permissionName STRING NOT NULL, " +
                                "granted INTEGER NOT NULL, " +
                                "create_time INTEGER NOT NULL, " +
                                "update_time INTEGER NOT NULL, " +
                                "PRIMARY KEY(packageName, permissionName) ON CONFLICT REPLACE)";
                try
                {
                    db.execSQL(CREATE_TABLE_PERMISSIONS_MANAGER);
                } catch (SQLException e)
                {
                }
            }
            if(null != db)
            {
                String strInsert = "insert into " + strTable_Name +
                        "(packageName, permissionName, granted, create_time, update_time) " +
                        "values(" +
                        "'" + "com.android.settings" + "', " +
                        //"'" + "com.txtw.green.one" + "', " +
                        "'" + "android.permission.RECEIVE_BOOT_COMPLETED" + "', " +
                        "'" + "1" + "', " +
                        "'" + "2" + "', " +
                        "'" + "0" + "')";

                Log.e(TAG, " strInsert : " + strInsert);

                try
                {
                    db.execSQL(strInsert);
                } catch (SQLException e)
                {
                }
                db.close();
            }
        }
        else if(strCmd.equals("1031") )
        {
            String strTable_Name = "permissions_manager";
            File file = new File("/data/data/android/PermissionsManager.db");
            SQLiteDatabase db = null;
            if(file.exists() )
            {
                Log.e(TAG, "exists : " + file + "   ok");
                db = SQLiteDatabase.openDatabase("/data/data/android/PermissionsManager.db",
                        null, SQLiteDatabase.OPEN_READWRITE);
            }
            else
            {
                Log.e(TAG, "exists : " + file + "   fail");
            }
            if(null != db)
            {
                String strSelect = "select granted from " + strTable_Name + " where " +
                        "packageName = 'com.android.settings' and permissionName = 'android.permission.RECEIVE_BOOT_COMPLETED' ";

                Log.e(TAG, " strSelect : " + strSelect);

                Cursor cursor = db.rawQuery(strSelect, null);
                if(cursor == null || cursor.getCount() == 0)
                {
                    return;
                }
                if(cursor.moveToFirst() )
                {
                    int granted = -1;
                    granted = cursor.getInt(cursor.getColumnIndex("granted") );

                    Log.e(TAG, "granted : " + granted);
                }
            }
        }
        else if(strCmd.equals("1032") )
        {
            PersistentDataBlockManager manager =(PersistentDataBlockManager)getSystemService(Context.PERSISTENT_DATA_BLOCK_SERVICE);
            manager.setOemUnlockEnabled(true);
        }
        else if(strCmd.equals("1033") )
        {
            Log.e(TAG, "LwGlobal.strPackageName : " + LwGlobal.strPackageName);
            Log.e(TAG, "LwGlobal.nPackage : " + LwGlobal.nPackage);
        }
        else if(strCmd.equals("1034") )
        {
            //system root inet
            int gid = android.os.Process.getGidForName("inet");
            Log.e(TAG, "gid : " + gid);

            //system root media
            int uid = android.os.Process.getUidForName("media");
            Log.e(TAG, "uid : " + uid);
        }
    }

    class PackageDeleteObserver extends IPackageDeleteObserver.Stub {
        public void packageDeleted(String packageName, int returnCode) {
            Log.e(TAG, "packageName : " + packageName);
            Log.e(TAG, "returnCode : " + returnCode);
            Log.e(TAG, "DELETE_SUCCEEDED : " + PackageManager.DELETE_SUCCEEDED);
        }
    }

    class PackageInstallObserver extends IPackageInstallObserver.Stub {
        public void packageInstalled(String packageName, int returnCode) {
            Log.e(TAG, "packageName : " + packageName);
            Log.e(TAG, "returnCode : " + returnCode);
            Log.e(TAG, "INSTALL_SUCCEEDED : " + PackageManager.INSTALL_SUCCEEDED);
        }
    }
}
