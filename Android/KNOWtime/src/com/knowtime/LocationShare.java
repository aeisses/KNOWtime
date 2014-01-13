package com.knowtime;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.knowtime.ShareMeActivity.ResponseReceiver;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
//import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class LocationShare extends IntentService {
//	private static LocationShare instance;
	public static final String serviceName = "com.knowtime.locationservice";
	public static final String PARAM_IN_MSG = "imsg";
	public static final String PARAM_OUT_MSG = "omsg";
	public static final String ROUTE_IN_MSG = "route";
	int loopCounter;
	public Boolean isSharing = false;
	private String locationUrl;
	private int pollRate;
//	private final Handler mHandler = new Handler();
	Date startTime;

	private static LocationShare sInstance;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		sInstance = this;
	}
	
	public LocationShare() {
		super(serviceName);
	}
	
	public static LocationShare getInstance()
	{
		return sInstance;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
//		sInstance = null;
//		isSharing = false;
	}
	
	private boolean isSendingLocations()
	{
		Date currentDate = new Date();
		long diffTime = currentDate.getTime() - startTime.getTime();
		return isSharing && diffTime < 108000000;
	}
	
//	private final Runnable mUpdateUI = new Runnable() {
//		public void run() {
//			if (isSendingLocations())
//			{
//				shareMyLocation();
//				mHandler.postDelayed(mUpdateUI, (pollRate*1000)/3); // 1 second
//			}
//        }
//    };
	
	private void createNewUser(final String route)
	{
//		Thread thread = new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
    			locationUrl = WebApiService.createNewUser(Integer.parseInt(route));
    			if (!locationUrl.equals(""))
    			{
    				final JSONObject jsonPollRate = WebApiService.getPollRate();
    				try 
    				{
    	                 pollRate = jsonPollRate.getInt("rate");
    				}
    				catch (JSONException e)
    				{
    					e.printStackTrace();
    				}
    				isSharing = true;
    		    	startTime = new Date();
//					mHandler.post(mUpdateUI);
    				Intent broadcastIntent = new Intent();
    				broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
    				broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
    				broadcastIntent.putExtra(PARAM_OUT_MSG, "sendingLocations");	
    				sendBroadcast(broadcastIntent);
    				while (isSharing)
    				{
    					shareMyLocation();
    		            SystemClock.sleep(pollRate*1000);
    				}
    			}
    			else
    			{
    				Intent broadcastIntent = new Intent();
    				broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
    				broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
    				broadcastIntent.putExtra(PARAM_OUT_MSG, "notSendingLocation");	
    				sendBroadcast(broadcastIntent);
    			}
//            }
//        });
//      	thread.start();
	}
	
	public void startLocationShare()
	{
		WebApiService.createNewUser(1);
	}

    private void shareMyLocation()
    {
    	Log.d("com.knowtime","Sharing location");
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		WebApiService.sendLocationToServer(locationUrl,lastKnownLocation);
    }
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("com.knowtime","here we are");
		String msg = intent.getStringExtra(PARAM_IN_MSG);
		if (msg.equals("start"))
		{
			createNewUser(intent.getStringExtra(ROUTE_IN_MSG));
		}
		else if (msg.equals("stop"))
		{
			
		}
		else if (msg.equals("isRunning"))
		{
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			if (isSendingLocations())
			{
				broadcastIntent.putExtra(PARAM_OUT_MSG, "sendingLocations");
			}
			else
			{
				broadcastIntent.putExtra(PARAM_OUT_MSG, "notSendingLocation");	
			}
			sendBroadcast(broadcastIntent);
		}
	}
}
