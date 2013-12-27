package com.knowtime;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class LocationShare extends IntentService {
//	private static LocationShare instance;
	public static final String serviceName = "com.knowtime.locationservice";

	
//	public static LocationShare getInstance()
//	{
//		if (instance == null)
//		{
//			instance = new LocationShare(serviceName);
//		}
//		return instance;
//	}
//	
	public LocationShare() {
		super(serviceName);
	}
	
	public void startLocationShare()
	{
		WebApiService.createNewUser(1);
	}

//	public void setLocationCall(String locationCall)
//	{
//		if (locationCall.equals(""))
//		{
//			// This is a bad string return false
//			
//		}
//		else
//		{
//			// A good string
//			
//		}
//	}
	
	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d("com.knowtime","here we are");
		
	}

//	public boolean stopService()
//	{
//		return super.stopService(null)
//	}
}
