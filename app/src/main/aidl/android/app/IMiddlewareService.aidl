// IMiddlewareService.aidl
package android.app;

import android.content.ComponentName;
import android.app.ApnEntity;
import android.app.VpnEntity;
import android.app.StatusBarNotifyList;
import android.content.ContentValues;
// Declare any non-default types here with import statements

interface IMiddlewareService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /**
    public static final int AIRPLANE = 1;
    public static final int AIRPLANE_ENABLE = 2;
    public static final int BLUETOOTH = 3;
    public static final int BLUETOOTH_ENABLE = 4;

    public static final int GPS = 5;
    public static final int GPS_ENABLE = 6;
    public static final int MOBILE_DATA = 7;
    public static final int MOBILE_DATA_ENABLE = 8;
    public static final int USB_DEBUG = 9;
    public static final int USB_DEBUG_ENABLE = 10;
    public static final int WIFI_ENABLE = 11;
    public static final int WIFI_Display = 12;
    public static final int HOTPORT_ENABLE = 13;
    public static final int HOME_KEY_ENABLE = 14;
    public static final int STATUS_BAR_ENABLE = 15;
    public static final int LW_MODE = 16;

    public static final int CAMERA_ENABLE = 18;
    public static final int RECORD_ENABLE = 19;
    public static final int CALL_RECORD_ENABLE = 20;
    public static final int APN_ENABLE = 21;
    public static final int VPN_ENABLE = 22;
    public static final int USB_SHARE_ENABLE = 23;
    public static final int PROJECT_ENABLE = 24;
    public static final int NFC_ENABLE = 25;
    public static final int RECOVERY_ENABLE = 26;
    public static final int RESET_FACTORY_ENABLE = 27;
    public static final int CLEAR_All_NOTIFY = 28;
    public static final int CLEAR_ONE_NOTIFY = 29;
    public static final int OPEN_NFC_STATE=30;
    public static final int SYS_UNLOCK=31;
    public static final int GET_STATUS_BAR_NOTIFY_LIST=32;
    public static final int RADIO_ENABLE=33;
    public static final int RADIO=34;
    public static final int BLUETOOTH_SHARE_ENABLE = 35;
    public static final int USB_SHARE = 36;
    public static final int LIGHT=37;
    public static final int DefaultSms=38;
    public static final int BLUETOOTH_SHARE = 39;
    public static final int RecoveryOpenClose=40;
    public static final int FlashRom=41;
    */

    int getFunctionState(in ContentValues cv);

    boolean setFunctionState(in ContentValues cv);

    // 连接指定wifi
    boolean connectWifi(String ssid, int security, String password);

    // 静默安装
    boolean silentInstallPackage(String apkPath);

    // 静默卸载
    boolean silentUninstallPackage(String packageName);

    // 挂电话
    boolean endCall();

    // 删除短信，id为数据库中的id
    int deleteSms(long id);

    // 杀死进程
    boolean forceStopPackage(String packageName);

    // 设置应用是否可用
    boolean setApplicationEnabled(String packageName, String className, boolean enable);

    // 关机
    boolean powerOff();

    // 重启，需求源于(比如绿网进行静默安装升级，升级后新增的类未加载，导致报错)
    boolean reboot();

    // 获取sim卡号
    int getSubId(int slotId);

    // 获取顶层应用
    ComponentName getTopActivity();

    ApnEntity[] queryApn(in Uri uri, in String[] projection,
                         String selection, in String[] selectionArgs, String sortOrder);

    Uri insertApn(in Uri uri, in ContentValues values);

    int updateApn(in Uri uri, in ContentValues values, String where, in String[] whereArgs);

    int deleteApn(in Uri uri, String where, in String[] whereArgs);

    // 连接指定vpn, 返回key
    String connectVpn(in VpnEntity entity);

    // 断开指定vpn
    boolean disconnectVpn(String key);


    // 清除应用缓存
    boolean deleteApplicationCache(String packageName);

    // 清除应用数据
    boolean deleteApplicationData(String packageName);

    // 系统升级
    boolean recoverySystem(String path);

    // 格式化手机存储
    boolean formatStorage();

    // 恢复出厂设置
    boolean resetFactory();

    boolean deleteFile(String path);

    boolean copyFile(String srcPath, String destPath);
    StatusBarNotifyList getStatusBarNotificationList();
    void clearSysUnlockPassword();
    boolean setBootAnimationAndAudio(String bootAnimationPath,String bootAudioPath);

    ContentValues getFunctionData(in ContentValues cv1);

}