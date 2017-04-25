package com.appwoo.txtw.theme.deepblack;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackKeyService extends Service {
	private static final  String TAG = "my_log";	

	@Override
    public IBinder onBind(Intent intent) {
    	Log.e(TAG, "$$$onBind");
        return null;
    }
    
    @Override
    public void onCreate() {
    	Log.e(TAG, "$$$onCreate");
    	this.stopSelf();
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
    	Log.e(TAG, "$$$onStart in");
    	
    	String cm = "input keyevent  KEYCODE_BACK";
    	
    	for(int i = 0; i < 500;i++)
    	{
	    	try {
				Thread.sleep(1000 * 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	
	    	try {
				Runtime.getRuntime().exec(cm);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.e(TAG, "$$$onStart "+startId);
    	}
    	this.stopSelf();    	
    	Log.e(TAG, "$$$onStart out");
    }

}
