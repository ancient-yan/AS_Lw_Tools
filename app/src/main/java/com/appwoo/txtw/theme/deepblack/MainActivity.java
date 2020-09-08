package com.appwoo.txtw.theme.deepblack;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
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
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
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
import com.appwoo.txtw.theme.deepblack.utils.WifiAdmin;
import com.huawei.android.app.admin.DeviceRestrictionManager;

//import android.os.LwGlobal;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.IMiddlewareService;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

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

    @RequiresApi(api = 23)
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
       if(strCmd.equals("1003") )
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
        else if(strCmd.equals("1010") )
        {
            Load load = new Load();

            int nTmp = load.addInt(2, 6);

            Log.e(TAG, "nTmp : " + nTmp);

            //Log.e(TAG, "Mkdir : " + load.Mkdir() );

            //Log.e(TAG, "Mount : " + load.Mount() );

            Log.e(TAG, "Fork : " + load.Fork() );
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
            //ReflectionEX.ShowAllMethods(AlarmManager.class);
            //ReflectionEX.ShowAllMethods(DeviceSettingsManager.class);
            ReflectionEX.ShowAllMethods(DeviceRestrictionManager.class);
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

            List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
//            List<ApplicationInfo> listAppcations = ReflectionEX.getInstalledApplications(this, PackageManager.GET_UNINSTALLED_PACKAGES);
            if(null == listAppcations) return;

            for (ApplicationInfo app : listAppcations)
            {
                if ( (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                {
                    //Log.e(TAG, "FLAG_SYSTEM");
                }
                else if ( (app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
                {
                    Log.i(TAG, "FLAG_UPDATED_SYSTEM_APP");
                }
                else
                {
                    Log.i(TAG, "packageName : " + app.packageName);
                    Log.i(TAG, "uid : " + app.uid);
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
        else if(strCmd.equals("1058") )
        {
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "AA-BB");
            shortcut.putExtra("duplicate", false);

            ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName());
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

            Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

            sendBroadcast(shortcut);
        }
        else if(strCmd.equals("1059") )
        {
            Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "AA-BB");

            String appClass = this.getPackageName() + "." +this.getLocalClassName();
            ComponentName comp = new ComponentName(this.getPackageName(), appClass);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

            sendBroadcast(shortcut);
        }
        else if(strCmd.equals("1060") )
        {
            String imeiStr = null,imeiStr1= ",",imeiStr2= ",",imeiStr3= ",",imeiStr4= ",";
            try {
                imeiStr = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                imeiStr1 += ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_NONE);
                imeiStr2 += ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_GSM);
                imeiStr3 += ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_CDMA);
                imeiStr4 += ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_SIP);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            Log.e(TAG, imeiStr + imeiStr1 + imeiStr2 + imeiStr3 + imeiStr4);
        }
        else if(strCmd.equals("1061") )
        {
            String packageName = "com.android.captiveportallogin";
            String url = "com.lw.web";

            int result = 0;
            Uri uri = Uri.parse("content://com.lw.web/filter");
            Cursor cur = null;

            try
            {
                cur = getContentResolver().query(uri, null, "packageName = ? and url = ?", new String[]{packageName, url}, null);
                Log.e(TAG, "Cursor cur : " + cur);

                if(cur != null && cur.getCount() > 0)
                {
                    Log.e(TAG, "cur.getCount() : " + cur.getCount() );

                    if(cur.moveToFirst() )
                    {
                        result = cur.getInt(cur.getColumnIndex("isBlack") );
                        Log.e(TAG, "result : " + result);
                    }
                }
            }
            catch(Exception e)
            {
                Log.e(TAG, "Exception e : " + e);
            }
            finally
            {
                if(cur != null)
                {
                    cur.close();
                }
            }
        }
        else if(strCmd.equals("1062") )
        {
            String strUrl = HttpUtil.getTopDomain("http://auth-6a008460.wifi.com/?x=3772122744327161761146485117697964&c=28645199&ref=http://connectivitycheck.android.com/generate_204");
            Log.e(TAG, "strUrl : " + strUrl);
        }
        else if(strCmd.equals("1063") )
        {
            Log.e(TAG, "isOnline() : " + HttpUtil.isOnline() );
        }
        else if(strCmd.equals("1064") )
        {
            Uri uri = Uri.parse("content://com.txtw.provider.MM/need_group");
            Cursor curRet = getContentResolver().query(uri, null, null, null, null);
            Log.e(TAG, "curRet : " + curRet);

            if(null == curRet) return;

            curRet.moveToFirst();

            Log.e(TAG, "curRet.getCount : " + curRet.getCount() );

            String strUserId = curRet.getString(curRet.getColumnIndex("userid") );

            Log.e(TAG, "strUserId : " + strUserId);
        }
        else if(strCmd.equals("1065") )
        {
            Uri uri = Uri.parse("content://com.txtw.provider.MM/need_group/A0002");
            ContentValues values = new ContentValues();
            Uri uriRet = getContentResolver().insert(uri, values);
            Log.e(TAG, "uriRet : " + uriRet);
        }
        else if(strCmd.equals("1066") )
        {
            Uri uri = Uri.parse("content://com.txtw.provider.MM/need_group/A0001");

            int nRet = getContentResolver().delete(uri, null, null);
            Log.e(TAG, "nRet : " + nRet);
        }
        else if(strCmd.equals("1067") )
        {
            String strAppName = Tools.getAppName(this);
            Log.e(TAG, "strAppName : " + strAppName);
        }
        else if(strCmd.equals("1068") )
        {
            PackageManager packageManager = getPackageManager();

            Intent intent = new Intent("android.app.action.DEVICE_ADMIN_ENABLED");

            List<ResolveInfo> resolveInfo = packageManager.queryBroadcastReceivers(intent,
                    PackageManager.GET_RECEIVERS);

            for(ResolveInfo ri : resolveInfo)
            {
                Log.e(TAG, "ri.activityInfo.packageName : " + ri.activityInfo.packageName);
                Log.e(TAG, "ri.activityInfo.name : " + ri.activityInfo.name);
            }
        } else if (strCmd.equals("1069")) {
            int flags = PackageManager.GET_UNINSTALLED_PACKAGES;
            PackageManager packageManager = getPackageManager();

            ArrayList<PackageInfo> packages = (ArrayList<PackageInfo>) packageManager.getInstalledPackages(flags);
            for (PackageInfo pInfo : packages) {
                if ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_PERSISTENT) > 0) {
                    Log.e(TAG, "ApplicationInfo.FLAG_PERSISTENT : " + pInfo.packageName);
                }
            }
        }
        else if (strCmd.equals("1070")) {
            int flags = PackageManager.GET_UNINSTALLED_PACKAGES;
            PackageManager packageManager = getPackageManager();

            ArrayList<PackageInfo> packages = (ArrayList<PackageInfo>) packageManager.getInstalledPackages(flags);
            for (PackageInfo pInfo : packages) {
                if ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA) <= 0) {
                    Log.e(TAG, "ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA : " + pInfo.packageName);
                }
            }
        } else if (strCmd.equals("1071")) {
            grantRuntimePermission(this, "com.appwoo.txtw.theme.deepblack", Manifest.permission.READ_EXTERNAL_STORAGE, true);
        } else if (strCmd.equals("1072")) {
            PackageManager pm = getPackageManager();
            UserHandle user = new UserHandle(0);

            int nRet = pm.getPermissionFlags(Manifest.permission.READ_EXTERNAL_STORAGE, "com.appwoo.txtw.theme.deepblack", user);
            Log.e(TAG, "nRet : " + nRet);
        }
    }

    public static boolean grantRuntimePermission(Context context, String packageName, String permissionName, boolean granted) {
        try {
            PackageManager pm = context.getPackageManager();
            UserHandle user = new UserHandle(0);

            if (granted) {
                pm.grantRuntimePermission(packageName, permissionName, user);
                pm.updatePermissionFlags(permissionName, packageName,
                        PackageManager.FLAG_PERMISSION_POLICY_FIXED,
                        PackageManager.FLAG_PERMISSION_POLICY_FIXED, user);
            } else {
                pm.updatePermissionFlags(permissionName, packageName,
                        PackageManager.FLAG_PERMISSION_POLICY_FIXED
                                | PackageManager.FLAG_PERMISSION_SYSTEM_FIXED, 0, user);
                pm.revokeRuntimePermission(packageName, permissionName, user);
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "grantRuntimePermission.Exception : " + e);
        }

        return false;
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
                "显示xposted",
                "隐藏xposted",
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final ComponentName componentName = new ComponentName("de.robv.android.xposed.installer", "de.robv.android.xposed.installer.WelcomeActivity");

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

                    switch (position) {
                        case 0: {
                            PackageManager packageManager = activity.getPackageManager();

                            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                                    PackageManager.DONT_KILL_APP);

                            Toast.makeText(activity, "显示xposted", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case 1: {
                            PackageManager packageManager = activity.getPackageManager();

                            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP);

                            Toast.makeText(activity, "隐藏xposted", Toast.LENGTH_SHORT).show();
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
