package com.appwoo.txtw.theme.deepblack.utils;

import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        }
    }

    public static void SetEnable(Field reField)
    {
        reField.setAccessible(true);
    }
}
