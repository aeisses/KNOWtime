package com.knowtime;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class LocationShare extends IntentService {
	private static LocationShare instance;
	private static final String serviceName = "locationshare";

	public static void initInstance()
	{
		if (instance == null)
		{
			instance = new LocationShare(serviceName);
		}
	}
	
	public static LocationShare getInstance()
	{
		return instance;
	}
	
	private LocationShare(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d("Aaron","here we are");
	}

}
