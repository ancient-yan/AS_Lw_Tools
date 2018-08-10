package com.appwoo.txtw.theme.deepblack.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;


public class HttpUtil {
    private static final String TAG = "my_log";
    private static final String CONTENT_TYPE_URL = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    private static final int HTTP_OK = 200;

    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener) {
        new Thread() {
            private HttpURLConnection connection;

            public void run() {
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(builder.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            };
        }.start();
    }

    public static void sendHttpRequest(final Network network, final String address,
                                       final HttpCallbackListener listener) {
        new Thread() {
            private HttpURLConnection connection;

            public void run() {
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) network.openConnection(url);
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(builder.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            };
        }.start();
    }

    public static void sendHttpRequestPost(final String address,
                                           final Map<String, String> params,
                                           final String encode,
                                           final HttpCallbackListener listener)
    {
        sendHttpRequestPost(address, params, encode, listener, CONTENT_TYPE_URL);
    }

    public static void sendHttpRequestPost(final String address,
                                           final Map<String, String> params,
                                           final String encode,
                                           final HttpCallbackListener listener,
                                           final String strContentType)
    {
        new Thread() {
            private HttpURLConnection connection;

            public void run() {
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    connection.setRequestProperty("Content-Type", strContentType);
                    if(CONTENT_TYPE_URL.equals(strContentType) )
                    {
                        byte[] data = getRequestData(params, encode).toString().getBytes();
                        connection.setRequestProperty("Content-Length", String.valueOf(data.length) );
                        //获得输出流，向服务器写入数据
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(data);
                    }
                    else if(CONTENT_TYPE_JSON.equals(strContentType) )
                    {
                        connection.setRequestProperty("Accept", "application/json");

                        DataOutputStream wr = new DataOutputStream(connection.getOutputStream() );

                        String jsonString = getJsonData(params);
                        wr.writeBytes(jsonString);
                        wr.flush();
                        wr.close();
                    }

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(builder.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            };
        }.start();
    }

    public static StringBuffer getRequestData(Map<String, String> params, String encode)
    {
        StringBuffer stringBuffer = new StringBuffer();
        try
        {
            for(Map.Entry<String, String> entry : params.entrySet() )
            {
                stringBuffer.append(entry.getKey() )
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode) )
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Loge(TAG, "stringBuffer : " + stringBuffer);

        return stringBuffer;
    }

    public static String getJsonData(Map<String, String> params)
    {
        String strJsonData = "";
        try
        {
            JSONObject jsonObj = new JSONObject();
            for(Map.Entry<String, String> entry : params.entrySet() )
            {
                try
                {
                    jsonObj.put(entry.getKey(), new JSONObject(entry.getValue() ) );
                }
                catch (Exception e)
                {
                    jsonObj.put(entry.getKey(), entry.getValue() );
                }
            }

            strJsonData = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Loge(TAG, "strJsonData : " + strJsonData);

        return strJsonData;
    }

    public interface HttpCallbackListener {

        void onFinish(String response);

        void onError(Exception e);
    }

    public static void GetIP()
    {
        String address = "http://pv.sohu.com/cityjson?ie=utf-8";
        Log.e(TAG, "address = " + address);

        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener()
        {
            @Override
            public void onFinish(String response)
            {
                Log.e(TAG, "response = " + response);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError = " + e);
            }
        });
    }

    private static void GetIP(Network network)
    {
        String address = "http://pv.sohu.com/cityjson?ie=utf-8";
        Log.e(TAG, "address = " + address);

        HttpUtil.sendHttpRequest(network, address, new HttpUtil.HttpCallbackListener()
        {
            @Override
            public void onFinish(String response)
            {
                Log.e(TAG, "response = " + response);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError = " + e);
            }
        });
    }

    public static void GetIP_wifi(Context context)
    {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

        NetworkRequest request = builder.build();

        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback()
        {
            @Override
            public void onAvailable(Network network)
            {
                super.onAvailable(network);

                Log.e(TAG, "network : " + network);

                connMgr.unregisterNetworkCallback(this);
                GetIP(network);
            }
        };

        connMgr.registerNetworkCallback(request, callback);
    }

    private static String toURLEncoded(String paramString)
    {
        if (paramString == null || paramString.equals("") )
        {
            Log.e(TAG, "toURLEncoded error : " + paramString);
            return "";
        }

        try
        {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        }
        catch (Exception localException)
        {
            Log.e(TAG, "toURLEncoded error : " + localException);
        }

        return "";
    }

    public static void Loge(String tag, String msg)
    {
        if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0) return;

        int segmentSize = 3 * 1024;
        long length = msg.length();

        if (length <= segmentSize )
        {
            Log.e(tag, msg);
        }
        else
        {
            while (msg.length() > segmentSize )
            {
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.e(tag, logContent);
            }
            Log.e(tag, msg);
        }
    }

    public static final boolean ping(final Handler handler)
    {
        final String TAG_PING = "ping";

        String result = null;
        try
        {
            String ip = "14.215.177.39";
            Process p = Runtime.getRuntime().exec(" ping -c 10 -W 1 " + ip);

            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input) );
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ( (content = in.readLine() ) != null)
            {
                stringBuffer.append(content);
            }

            Log.e(TAG_PING, "result content : " + stringBuffer.toString() );

            int status = p.waitFor();
            if (status == 0)
            {
                result = "success";
                //handler.sendEmptyMessage(AccessibilityServiceEx.nPING_SUCCESS);

                return true;
            }
            else
            {
                result = "failed";
                //handler.sendEmptyMessage(AccessibilityServiceEx.nPING_ERROR);
            }
        }
        catch (IOException e)
        {
            result = "IOException";
        }
        catch (InterruptedException e)
        {
            result = "InterruptedException";
        }
        finally
        {
            Log.e(TAG_PING, "result = " + result);
        }
        return false;
    }

    public static final boolean isVpnConnected()
    {
        try
        {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if(null == niList) return false;

            for (NetworkInterface intf : Collections.list(niList) )
            {
                if(null == intf) continue;

                String strNiName = intf.getName();
                Log.e(TAG, "UP : " + intf.isUp() + "\tstrNiName : " + strNiName);

                if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0)
                {
                    continue;
                }

                if ("tun0".equals(intf.getName() ) || "ppp0".equals(intf.getName() ) )
                {
                    return true;
                }
            }
        } catch (Throwable e)
        {
            e.printStackTrace();
        }

        return false;
    }
}

