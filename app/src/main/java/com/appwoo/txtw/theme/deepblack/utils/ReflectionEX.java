package com.appwoo.txtw.theme.deepblack.utils;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.ParceledListSlice;
import android.os.RemoteException;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionEX
{
    private final static String TAG = "my_log";

    public static void ShowAllFields(Class reClass)
    {
        Field[] fs = reClass.getDeclaredFields();
        for (Field f : fs)
        {
            String strType = f.getType().toString();
            String strName = f.getName();

            Log.e(TAG, "Field : " + strType + " -> " + strName);
        }
    }

    public static void ShowAllMethods(Class reClass)
    {
        Method[] methods = reClass.getMethods();

        for (Method method : methods)
        {
            String strName = method.getName();
            Log.e(TAG, "Method :" + strName);

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> clas : parameterTypes)
            {
                String parameterName = clas.getName();
                Log.e(TAG, "parameterName : " + parameterName);
            }
        }
    }

    public static void SetEnable(Field reField)
    {
        reField.setAccessible(true);
    }

    public static void SetEnable(Method reMethod)
    {
        reMethod.setAccessible(true);
    }

    public static List<ApplicationInfo> getInstalledApplications(Context context, int flags)
    {
        if(null == context) return null;

        IPackageManager mPM = ActivityThread.getPackageManager();
        final int userId = context.getUserId();

        try {
            ParceledListSlice<ApplicationInfo> slice = mPM.getInstalledApplications(flags, 999);
            return slice.getList();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException : " + e);
        }

        return null;
    }
}
