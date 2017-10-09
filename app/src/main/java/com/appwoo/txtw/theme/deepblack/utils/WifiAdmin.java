package com.appwoo.txtw.theme.deepblack.utils;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiAdmin {
    private static final int WIFICIPHER_NOPASS = 1122;
    private static final int WIFICIPHER_WEP = WIFICIPHER_NOPASS + 1;
    private static final int WIFICIPHER_WPA = WIFICIPHER_NOPASS + 2;

	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private List<WifiConfiguration> mWifiConfiguration;
	private List<ScanResult> mWifiList;
	
    public WifiAdmin(Context context) {
        mWifiManager = (WifiManager)context.getSystemService("wifi");
        mWifiInfo = mWifiManager.getConnectionInfo();
    }
    
    public void openWifi() {
        if(!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }
    
    public void closeWifi() {
        if(mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }
    
    public void startScan() {
        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }
    
    public List getWifiList() {
        return mWifiList;
    }
    
    public int getRssi() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        return mWifiInfo.getRssi();
    }

    public int  Connect(String ssid)
    {
        int netId = mWifiManager.addNetwork(createWifiConfig(ssid, "", WIFICIPHER_NOPASS) );

        boolean enable = mWifiManager.enableNetwork(netId, true);

        return 0;
    }

    private WifiConfiguration createWifiConfig(String ssid, String password, int type)
    {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        config.SSID = "\"" + ssid + "\"";

        WifiConfiguration tempConfig = isExist(ssid);
        if(tempConfig != null)
        {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if(type == WIFICIPHER_NOPASS)
        {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        else if(type == WIFICIPHER_WEP)
        {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        else if(type == WIFICIPHER_WPA)
        {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    private WifiConfiguration isExist(String ssid)
    {
        List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();

        if((configs != null) && (configs.size() > 0) ) {
            for (WifiConfiguration config : configs) {
                if (config.SSID.equals("\"" + ssid + "\"")) {
                    return config;
                }
            }
        }

        return null;
    }
}