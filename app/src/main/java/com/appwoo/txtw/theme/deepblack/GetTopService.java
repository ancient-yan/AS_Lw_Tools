package com.appwoo.txtw.theme.deepblack;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GetTopService extends Service {
	private static final  String TAG = "my_log";	

	@Override
    public IBinder onBind(Intent intent) {
    	Log.e(TAG, "$$$onBind");
        return null;
    }
    
    @Override
    public void onCreate() {
    	Log.e(TAG, "$$$onCreate");
    //	this.stopSelf();
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
    	Log.e(TAG, "$$$onStart in");
    	    	
    	for(int i = 0; i < 500000000;i++)
    	{
	    	try {
				Thread.sleep(1000 * 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	
	        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);    
	        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();  
	        
			Log.e(TAG, "$$$onStart " + runningActivity);
    	}
    	this.stopSelf();    	
    	Log.e(TAG, "$$$onStart out");
    }

}
