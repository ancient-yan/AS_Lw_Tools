package com.appwoo.txtw.theme.deepblack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class PackageVerifyReceiver extends BroadcastReceiver {
    private static final  String Tag = "my_log";
    private final String PackageVerificationResultAction = "android.intent.action.package_verification.result";

    public void onReceive(Context context, Intent intent) {
        int i = -1;
        PackageManager mPackageManager = context.getPackageManager();
        Log.v(Tag, "PackageVerifyReceiver");
        Log.v(Tag, "PackageVerifyReceiver intent.getAction() " + intent.getAction());
        if ("android.intent.action.PACKAGE_NEEDS_VERIFICATION".equals(intent.getAction())) {
            int verifiedId = intent.getIntExtra("android.content.pm.extra.VERIFICATION_ID", -1);
            String packageName = intent.getStringExtra("android.content.pm.extra.VERIFICATION_PACKAGE_NAME");
            Log.v(Tag, "PackageVerifyReceiver packageName=" + packageName + "   verifiedId=" + verifiedId);
            Intent intentLw = new Intent("com.txtw.package.verify.receiver");
            intentLw.setClassName("com.appwoo.txtw.activity", "com.appwoo.txtw.launcher.receiver.FilterSoftForZTEBroadcastReceiver");
            intentLw.putExtra("android.content.pm.extra.VERIFICATION_ID", verifiedId);
            intentLw.putExtra("android.content.pm.extra.VERIFICATION_PACKAGE_NAME", packageName);
            intentLw.putExtra("apk_install_path", intent.getDataString());

            Log.e(Tag, "apk_install_path : " + intent.getDataString() );

            context.sendBroadcast(intentLw);
            Log.v(Tag, "PackageVerifyReceiver android5.1.1 verifiedId=android.content.pm.extra.VERIFICATION_ID   packageName=android.content.pm.extra.VERIFICATION_PACKAGE_NAME");
        }
        if ("android.intent.action.package_verification.result".equals(intent.getAction())) {
            int verifiedId = intent.getIntExtra("android.content.pm.extra.VERIFICATION_ID", -1);
            boolean isAllowInstall = intent.getBooleanExtra("isAllowInstall", false);
            Log.v(Tag, "PackageVerificationResultAction isAllowInstall=" + isAllowInstall);
            if (isAllowInstall) {
                i = 1;
            }
            mPackageManager.verifyPendingInstall(verifiedId, i);
            Log.v(Tag, "PackageVerificationResultAction complete");
        }
    }
}
