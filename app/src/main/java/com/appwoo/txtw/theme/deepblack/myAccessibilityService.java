package com.appwoo.txtw.theme.deepblack;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class myAccessibilityService extends AccessibilityService {
    private static final  String TAG = "my_log";

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if(true) return;

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
            //performGlobalAction(GLOBAL_ACTION_HOME);
        }
    }

    @Override
    public void onServiceConnected() {
        AccessibilityServiceInfo info = getServiceInfo();
        info.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        //info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
        info.flags |= AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
        setServiceInfo(info);
    }

    @Override
    protected boolean onGesture(int gestureId) {
        Log.e(TAG, "onGesture");

        switch (gestureId) {
            case AccessibilityService.GESTURE_SWIPE_DOWN: {
                Log.e(TAG, "onGesture -> GESTURE_SWIPE_DOWN");
            } break;
            case AccessibilityService.GESTURE_SWIPE_UP: {
                Log.e(TAG, "onGesture -> GESTURE_SWIPE_UP");
            } break;
            case AccessibilityService.GESTURE_SWIPE_LEFT: {
                Log.e(TAG, "onGesture -> GESTURE_SWIPE_LEFT");
            } break;
            case AccessibilityService.GESTURE_SWIPE_RIGHT: {
                Log.e(TAG, "onGesture -> GESTURE_SWIPE_RIGHT");
            } break;
            case AccessibilityService.GESTURE_SWIPE_LEFT_AND_RIGHT: {
                Log.e(TAG, "onGesture -> GESTURE_SWIPE_LEFT_AND_RIGHT");
            } break;
            case AccessibilityService.GESTURE_SWIPE_RIGHT_AND_LEFT: {
                Log.e(TAG, "onGesture -> GESTURE_SWIPE_RIGHT_AND_LEFT");
            } break;
        }

        return super.onGesture(gestureId);
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        Log.e(TAG, "onKeyEvent : " + event);

        boolean result = true;
        return result;
    }
}
