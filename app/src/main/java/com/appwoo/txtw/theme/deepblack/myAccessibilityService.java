package com.appwoo.txtw.theme.deepblack;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class myAccessibilityService extends AccessibilityService {
    private static final  String TAG = "my_log";

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String strPackageName = "" + event.getPackageName();
        Log.e(TAG, "getPackageName : " +  strPackageName);
        Log.e(TAG, AccessibilityEvent.eventTypeToString(event.getEventType() ) );

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if(null != nodeInfo)
            Log.e(TAG, "getClassName : " + nodeInfo.getClassName() );

        if("com.mediatek.filemanager".equals(strPackageName) )
        {
            //performGlobalAction(GLOBAL_ACTION_BACK);
            //performGlobalAction(GLOBAL_ACTION_RECENTS);
            //performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS);
            performGlobalAction(GLOBAL_ACTION_HOME);
        }
    }

    @Override
    public void onServiceConnected() {
        AccessibilityServiceInfo info = getServiceInfo();
        info.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
//        info.flags |= AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        setServiceInfo(info);
    }
}
