package com.appwoo.txtw.theme.deepblack;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.IContentProvider;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
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
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.UserHandle;
//import android.service.persistentdata.PersistentDataBlockManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import android.os.SystemProperties;
import android.provider.Settings;

//import com.android.internal.widget.LockPatternUtils;
import com.appwoo.txtw.theme.deepblack.utils.HttpUtil;
import com.appwoo.txtw.theme.deepblack.utils.ReflectionEX;
import com.appwoo.txtw.theme.deepblack.utils.Tools;
import com.appwoo.txtw.theme.deepblack.utils.FieldUtils;
import com.appwoo.txtw.theme.deepblack.utils.Utils;
import com.appwoo.txtw.theme.deepblack.utils.WifiAdmin;

//import android.os.LwGlobal;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.IMiddlewareService;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText m_editText_Input;
    private Button m_button_Run;
    private final static String TAG = "my_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        m_editText_Input = (EditText)findViewById(R.id.editText_Input);
        m_button_Run = (Button)findViewById(R.id.button_Run);

        m_button_Run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                OnClick_run();
            }
        });

        m_editText_Input.setEnabled(false);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void OnClick_run()
    {
        if(null == m_editText_Input) return;

        String strCmd = m_editText_Input.getText().toString();
        m_editText_Input.setText("");

        if(null == strCmd) return;

        Log.e(TAG, " strCmd : " + strCmd);

        if("".equals(strCmd) )
        {
            Bundle bundle = new Bundle(1);
            bundle.putInt("layout", R.layout.package_list);
            MyDialogFragment.newInstance(bundle).show(getFragmentManager(), "fragment");

            return;
        }

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
        else if(strCmd.equals("5") )
        {
            SystemProperties.set("persist.sys.root_switch", "1");
        }
        else if(strCmd.equals("6") )
        {
            SystemProperties.set("persist.sys.usb.config", "mtp,adb");
        }
        else if(strCmd.equals("100") )
        {
/*            Intent intent = new Intent(Intent.ACTION_MASTER_CLEAR);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            intent.putExtra(Intent.EXTRA_REASON, "Lw_Tools_diff");
            sendBroadcast(intent);
 */       }
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
             //   if (am != null) am.removeTask(rt.persistentId);
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
                //String cm = "su 1000,1000 top";
                String cm = "pm enable com.freeme.freemelite";

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
                {/*
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
               */ }

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
        {/*
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
       */ }
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
        {/*
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
        */}
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
            //nRet = packageManager.LwGetPermission("com.lw.permissionsmanager", "android.permission.READ_CONTACTS");

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
            //PersistentDataBlockManager manager =(PersistentDataBlockManager)getSystemService(Context.PERSISTENT_DATA_BLOCK_SERVICE);
            //manager.setOemUnlockEnabled(true);
        }
        else if(strCmd.equals("1033") )
        {
           // Log.e(TAG, "LwGlobal.strPackageName : " + LwGlobal.strPackageName);
            //Log.e(TAG, "LwGlobal.nPackage : " + LwGlobal.nPackage);
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
        else if(strCmd.equals("1035") )
        {
            MyActivityManager activityManager = new MyActivityManager();
            Class oriClass = null;
            try {
                oriClass = activityManager.getOrignalClass();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Object obj = FieldUtils.readStaticField(oriClass, "gDefault");
                final Object oriObj = FieldUtils.readField(obj, "mInstance");
                activityManager.orignal = oriObj;
                List<Class<?>> interfaces = Utils.getAllInterfaces(oriObj.getClass());
                Class[] ifs = interfaces != null && interfaces.size() > 0 ? interfaces.toArray(new Class[interfaces.size()]) : new Class[0];
                final Object object = Proxy.newProxyInstance(oriObj.getClass().getClassLoader(),ifs, activityManager);

                FieldUtils.writeStaticField(oriClass, "gDefault", new android.util.Singleton<Object>() {
                    @Override
                    protected Object create() {
                        return object;
                    }
                });

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        else if(strCmd.equals("1036") )
        {
            try {
                Class viewClass = Class.forName("android.view.View");
                Method method = viewClass.getDeclaredMethod("getListenerInfo");
                method.setAccessible(true);
                Object listenerInfoInstance = method.invoke(m_button_Run);

                Class listenerInfoClass = Class.forName("android.view.View$ListenerInfo");
                Field onClickListerField = listenerInfoClass.getDeclaredField("mOnClickListener");
                onClickListerField.setAccessible(true);
                View.OnClickListener onClickListerObj = (View.OnClickListener) onClickListerField.get(listenerInfoInstance);

                Log.e(TAG, "onClickListerObj : " + onClickListerObj);

                onClickListerField.set(listenerInfoInstance, null);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        else if(strCmd.equals("1037") )
        {
            Intent intent = new Intent(this, OneShotAlarm.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

            // We want the alarm to go off 10 seconds from now.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 30);

            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
        else if(strCmd.equals("1038") )
        {
            Intent intent = new Intent(this, RepeatingAlarm.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

            // We want the alarm to go off 10 seconds from now.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 10);

            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), 10 * 1000, sender);
        }
        else if(strCmd.equals("1039") )
        {
            ReflectionEX.ShowAllMethods(AlarmManager.class);
        }
        else if(strCmd.equals("1040") )
        {
            registerBroadcastReceiver();

            WifiAdmin localWifiAdmin  =  new WifiAdmin (this);
            localWifiAdmin.openWifi();
            localWifiAdmin.startScan();
            List<ScanResult> wifiList = localWifiAdmin.getWifiList();
            if((wifiList != null) && (wifiList.size() > 0) )
            {
                int nSize = wifiList.size();
                Log.e(TAG, "nSize : " + nSize);

                for(ScanResult result : wifiList)
                {
                    Log.e(TAG, "result.SSID : " + result.SSID);
                }
            }
        }
        else if(strCmd.equals("1041") )
        {
            WifiAdmin localWifiAdmin  =  new WifiAdmin (this);
            localWifiAdmin.openWifi();

            localWifiAdmin.Connect("LWTX");
        }
        else if(strCmd.equals("1042") )
        {/*
            LockPatternUtils mLockPatternUtils;
            mLockPatternUtils = new LockPatternUtils(this);
            mLockPatternUtils.setLockScreenDisabled(true, getUserId());
        */}
        else if(strCmd.equals("1043") )
        {
            Uri uri = Uri.parse("content://com.lw.permissionsmanager.provider/package_permission");
            //Uri uri = Uri.parse("content://com.lw.permissionsmanager.providerDDD/package_permission");
            IContentProvider provider = getContentResolver().acquireProvider(uri);
            //IContentProvider provider = getContentResolver().acquireExistingProvider(uri);

            Log.e(TAG, "provider : " + provider);

            if(null != provider) getContentResolver().releaseProvider(provider);
        }
        else if(strCmd.equals("1044") )
        {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);

            registerReceiverAsUser(mReceiver, UserHandle.OWNER, filter, null, null);
        }
        else if(strCmd.equals("1045") )
        {
            try {
                System.out.printf("my_log : App : %s : %d \n", "www.baidu.com", 80);

                Socket socket = new Socket("www.baidu.com", 80);
                InetAddress.getAllByName("www.baidu.com");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(strCmd.equals("1046") )
        {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://www.baidu.com")
                        .build();
                Response response = null;
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.e(TAG, "response.code()==" + response.code());
                    Log.e(TAG, "response.message()==" + response.message());
                    Log.e(TAG, "res==" + response.body().string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(strCmd.equals("1047") )
        {
            Intent intent = new Intent("com.txtw.lwmiddleware.action.MiddlewareService");
            intent.setPackage("com.txtw.lwmiddleware");

            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
        else if(strCmd.equals("1048") )
        {
            try {
                //mService.powerOff();
                //mService.reboot();
                //mService.recoverySystem("//sdcard/update.zip");
                //ComponentName name = mService.getTopActivity();
                //Log.e(TAG, "name : " + name.toString() );
                mService.resetFactory();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else if(strCmd.equals("1049") )
        {
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String mac = wifiInfo.getMacAddress();

            Log.e(TAG, "mac : " + mac);
        }
        else if(strCmd.equals("1050") )
        {
            String strSN = SystemProperties.get("persist.sys.sn");

            Log.e(TAG, "strSN : " + strSN);
        }
        else if(strCmd.equals("1051") )
        {
            PackageManager pm = getPackageManager();

            //List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            List<ApplicationInfo> listAppcations = ReflectionEX.getInstalledApplications(this, PackageManager.GET_UNINSTALLED_PACKAGES);
            if(null == listAppcations) return;

            for (ApplicationInfo app : listAppcations)
            {
                if ( (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                {
                    //Log.e(TAG, "FLAG_SYSTEM");
                }
                else if ( (app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
                {
                    Log.e(TAG, "FLAG_UPDATED_SYSTEM_APP");
                }
                else
                {
                    Log.e(TAG, "packageName : " + app.packageName);
                }
            }
        }
        else if(strCmd.equals("1052") )
        {
            ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> AppList = activityManager.getRunningAppProcesses();

            for(ActivityManager.RunningAppProcessInfo info: AppList)
            {
                Log.e(TAG, "processName : " + info.processName);
            }
        }
        else if(strCmd.equals("1053") )
        {
            try
            {
                Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
                if(null == niList) return;

                for (NetworkInterface intf : Collections.list(niList) )
                {
                    if(null == intf) continue;

                    String strNiName = intf.getName();
                    Log.e(TAG, "UP : " + intf.isUp() + "\tstrNiName : " + strNiName);

                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0)
                    {
                        continue;
                    }

                    if ("tun0".equals(intf.getName() ) || "ppp0".equals(intf.getName() ) )
                    {
                    }
                }
            } catch (Throwable e)
            {
                Log.e(TAG, "Throwable : " + e);
            }
        }
        else if(strCmd.equals("1054") )
        {
            connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

            NetworkRequest request = builder.build();

            ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback()
            {
                @Override
                public void onAvailable(Network network)
                {
                    super.onAvailable(network);

                    Log.e(TAG, "network : " + network);

                    connMgr.unregisterNetworkCallback(this);
                    ConnectivityManager.setProcessDefaultNetwork(network);
                }
            };

            connMgr.registerNetworkCallback(request, callback);
        }
        else if(strCmd.equals("1055") )
        {
            HttpUtil.GetIP();
        }
        else if(strCmd.equals("1056") )
        {
            ConnectivityManager.setProcessDefaultNetwork(null);
        }
        else if(strCmd.equals("1057") )
        {
            HttpUtil.GetIP_wifi(this);
        }
    }

    ConnectivityManager connMgr = null;

    IMiddlewareService mService = null;

    private ServiceConnection mConnection = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            mService = IMiddlewareService.Stub.asInterface(service);
            Log.e(TAG, "onServiceConnected");
        }

        public void onServiceDisconnected(ComponentName className)
        {
            mService = null;
            Log.e(TAG, "onServiceDisconnected");
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, " action = " + action);

            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.d(TAG, " screen on ");
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.d(TAG, " screen off ");
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver;
    private void registerBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                switch(state)
                {
                    case WifiManager.WIFI_STATE_DISABLING:
                    {
                        Log.e(TAG, "WIFI_STATE_DISABLING : " + state);
                    }
                    break;
                    case WifiManager.WIFI_STATE_DISABLED:
                    {
                        Log.e(TAG, "WIFI_STATE_DISABLED : " + state);
                    }
                    break;
                    case WifiManager.WIFI_STATE_ENABLING:
                    {
                        Log.e(TAG, "WIFI_STATE_ENABLING : " + state);
                    }
                    break;
                    case WifiManager.WIFI_STATE_ENABLED:
                    {
                        Log.e(TAG, "WIFI_STATE_ENABLED : " + state);
                    }
                    break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                    {
                        Log.e(TAG, "WIFI_STATE_UNKNOWN : " + state);
                    }
                    break;
                    default: {
                        Log.e(TAG, "wifi_state : " + state);
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        this.registerReceiver(mBroadcastReceiver, intentFilter);
    }


    public class MyActivityManager  implements InvocationHandler
    {
        public Object orignal ;

        public Class<?> getOrignalClass() throws ClassNotFoundException {
            return Class.forName("android.app.ActivityManagerNative");
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.d(TAG, "before method called:" + method.getName());
            final Object obj =  method.invoke(orignal, args);
            Log.d(TAG, "after method called:" + method.getName());
            return obj;
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

    public static class MyDialogFragment extends DialogFragment {
        private String[] mListStr = {
                "禁用WebView",
                "启用WebView",
                "启用adb",
                "禁用adb",
                "禁用系统锁屏",
                "启用系统锁屏",
                "Input Code"};

     public static DialogFragment newInstance(Bundle bundle)
     {
         DialogFragment fragment = new MyDialogFragment();
         fragment.setArguments(bundle);

         return fragment;
     }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setStyle(STYLE_NO_TITLE, 0);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            Bundle bundle = getArguments();
            final int layoutId = bundle.getInt("layout", -1);
            final Activity activity = getActivity();
            View  root = inflater.inflate(layoutId, container, false);
            ListView listView = (ListView) root.findViewById(R.id.package_list);
            listView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1,
                        mListStr) );

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    dismiss();

                    switch(position)
                    {
                        case 0:
                        {
                            SystemProperties.set("persist.sys.webviewdisablelw", "1");

                            Toast.makeText(activity, "禁用WebView", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case 1:
                        {
                            SystemProperties.set("persist.sys.webviewdisablelw", "0");

                            Toast.makeText(activity, "启用WebView", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case 2:
                        {
                            SystemProperties.set("persist.sys.usbdebugdisablelw", "1");
                            Settings.Global.putInt(activity.getContentResolver(), Settings.Global.ADB_ENABLED, 1);
                        }
                        break;

                        case 3:
                        {
                            Settings.Global.putInt(activity.getContentResolver(), Settings.Global.ADB_ENABLED, 0);
                            SystemProperties.set("persist.sys.usbdebugdisablelw", "0");
                        }
                        break;

                        case 4:
                        {
                            SystemProperties.set("persist.sys.lockscreendisablelw", "1");
                            Toast.makeText(activity, "禁用系统锁屏", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case 5:
                        {
                            SystemProperties.set("persist.sys.lockscreendisablelw", "0");
                            Toast.makeText(activity, "启用系统锁屏", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        default:
                            ( (MainActivity)activity).m_editText_Input.setEnabled(true);
                    }
                }
            });

            return root;
        }
    }
}
