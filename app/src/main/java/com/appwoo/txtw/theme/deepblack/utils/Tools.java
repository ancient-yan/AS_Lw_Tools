package com.appwoo.txtw.theme.deepblack.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class Tools {
    private final static String TAG = "my_log";

    public static void parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));

            MessageDigest md = null;
            MessageDigest md2 = null;
            MessageDigest md3 = null;
            try {
                md = MessageDigest.getInstance("MD5");
                md2 = MessageDigest.getInstance("SHA1");
                md3 = MessageDigest.getInstance("SHA256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            byte[] b = md.digest(cert.getEncoded());
            String key = byte2HexFormatted(b);//.replace(":", "");
            Log.e(TAG, "key : " + key);

            byte[] b2 = md2.digest(cert.getEncoded());
            String key2 = byte2HexFormatted(b2);
            Log.e(TAG, "key2 : " + key2);

            byte[] b3 = md3.digest(cert.getEncoded());
            String key3 = byte2HexFormatted(b3);
            Log.e(TAG, "key3 : " + key3);

        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    private static String byte2HexFormatted(byte[] arr)
    {
        StringBuilder str = new StringBuilder(arr.length * 2);

        for (int i = 0; i < arr.length; i++)
        {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();

            if (l == 1)
                h = "0" + h;

            if (l > 2)
                h = h.substring(l - 2, l);

            str.append(h.toUpperCase());

            if (i < (arr.length - 1))
                str.append(':');
        }

        return str.toString();
    }

    public static boolean copyFile(InputStream in, String destFileName) {
        FileOutputStream out = null;
        byte[] buffer = new byte[10240];
        try {
            File destFile = new File(destFileName);
            if(destFile.exists()){
                return true;
            }
            destFile.delete();
            destFile.getParentFile().mkdirs();
            destFile.createNewFile();
            out = new FileOutputStream(destFile);
            int num = 0;
            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAppName(Context context)
    {
        int pid = android.os.Process.myPid();

        try
        {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List list = activityManager.getRunningAppProcesses();
            Iterator i = list.iterator();
            while (i.hasNext() )
            {
                ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next() );

                if (info.pid == pid) return info.processName;
            }
        }
        catch (Exception e)
        {
        }

        return null;
    }

    public static void getInstalled3PApp(Context context) {
        if (null == context) return;
        PackageManager packageManager = context.getPackageManager();
        ArrayList<PackageInfo> packages = (ArrayList<PackageInfo>) packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo pak : packages) {
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                Log.d(TAG, "packageName : " + pak.packageName);
                Log.d(TAG, "\t versionCode : " + pak.versionCode + "\t\t versionName : " + pak.versionName + "\t\t\t label : " + packageManager.getApplicationLabel(pak.applicationInfo));
            }
        }
    }
}