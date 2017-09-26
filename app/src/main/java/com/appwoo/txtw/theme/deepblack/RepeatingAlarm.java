package com.appwoo.txtw.theme.deepblack;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

public class RepeatingAlarm extends BroadcastReceiver
{
    private static final String TAG = "my_log";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e(TAG, "RepeatingAlarm");
        Toast.makeText(context, "lw lw lw lw lw lw  RepeatingAlarm", Toast.LENGTH_SHORT).show();
    }
}

