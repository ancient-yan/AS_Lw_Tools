package com.appwoo.txtw.theme.deepblack;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.widget.Toast;
public class OneShotAlarm extends BroadcastReceiver
{
    private static final String TAG = "my_log";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e(TAG, "OneShotAlarm");
        Toast.makeText(context, "lw lw lw OneShotAlarm", Toast.LENGTH_SHORT).show();
    }
}

