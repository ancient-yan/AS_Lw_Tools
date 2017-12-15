package com.appwoo.txtw.theme.deepblack;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class myAccessibilityService extends AccessibilityService {
    private static final  String TAG = "my_log";

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e(TAG, AccessibilityEvent.eventTypeToString(event.getEventType() ) );
    }
}
