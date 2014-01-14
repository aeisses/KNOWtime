package com.knowtime;

import java.util.Date;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ShareMeActivity extends Activity 
{
	private ImageButton onMyWayButton;
	private ImageView connectToServerImage;
	private ImageView sendingImage;
	private Boolean isSharing = false;
	private final Handler mHandler = new Handler();
	private ImageView sendingLineImage;
	Date startTime;
	int loopCounter;
	private ResponseReceiver receiver;
	private Intent locationShareIntent;
	private Context mContext;
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder mNotification;
	private int notifyId = 1;
	
	private boolean isSendingLocations()
	{
		return isSharing;
	}
	
	private final Runnable mUpdateUI = new Runnable() {
		public void run() {
			if (isSendingLocations())
			{
				if (loopCounter == 0)
				{
					sendingLineImage.setImageResource(R.drawable.sending1);
					loopCounter++;
				}
				else if (loopCounter == 1)
				{
					sendingLineImage.setImageResource(R.drawable.sending2);
					loopCounter++;
				}
				else
				{
					sendingLineImage.setImageResource(R.drawable.sending3);
					loopCounter = 0;
				}
				mHandler.postDelayed(mUpdateUI, 500);
			}
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_me);
		mContext = getApplicationContext();
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		connectToServerImage = (ImageView)findViewById(R.id.connecttoserverimage);
		connectToServerImage.setVisibility(View.INVISIBLE);
		sendingImage = (ImageView)findViewById(R.id.sendingimage);
		sendingImage.setVisibility(View.INVISIBLE);
		sendingLineImage = (ImageView)findViewById(R.id.sendinglineimage);
		onMyWayButton = (ImageButton)findViewById(R.id.onmywaybutton);
		onMyWayButton.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					if (isSharing)
					{
						onMyWayButton.setBackgroundResource(R.drawable.onmywaybutton);
					}
					else
					{
						onMyWayButton.setBackgroundResource(R.drawable.stop);
					}
				}
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if (isSharing)
					{
						stopSharing();
					}
					else
					{
						Intent intent = new Intent(ShareMeActivity.this,RoutePickerActivity.class);
						startActivityForResult(intent,1);
					}
				}
				return false;
			}
		});

		//Adding Sharing me notification
		Intent notificationIntent = new Intent(mContext, ShareMeActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

		mNotification = new NotificationCompat.Builder(this)
	    	.setContentTitle("Sharing My Ride")
	    	.setSmallIcon(R.drawable.ic_launcher)
	    	.setContentIntent(contentIntent);

//		locationShareIntent = LocationShare.getInstance();
		if (LocationShare.getInstance() != null && LocationShare.getInstance().isSharing)
		{
			startSharing();
		}
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "54VCHBRBYDDP5VT63WFB");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		isSharing = false;
		FlurryAgent.onEndSession(this);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	    unregisterReceiver(receiver);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
	}
	
	private void startSharing()
	{
		onMyWayButton.setBackgroundResource(R.drawable.stop);
		isSharing = true;
		connectToServerImage.setVisibility(View.VISIBLE);
		sendingImage.setVisibility(View.VISIBLE);
		loopCounter = 0;
		mHandler.post(mUpdateUI);
		mNotificationManager.notify(notifyId, mNotification.build());
	}
	
	private void stopSharing()
	{
		onMyWayButton.setBackgroundResource(R.drawable.onmywaybutton);
		isSharing = false;
		connectToServerImage.setVisibility(View.INVISIBLE);
		sendingImage.setVisibility(View.INVISIBLE);
		sendingLineImage.setImageResource(R.drawable.sendingline);
 	   	LocationShare.getInstance().isSharing = false;
		mNotificationManager.cancel(notifyId);


//		stopService();
	}
	
//	private void createNewUser(final String route)
//	{
//		Thread thread = new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//    			locationUrl = WebApiService.createNewUser(Integer.parseInt(route));
//    			if (!locationUrl.equals(""))
//    			{
//    				final JSONObject jsonPollRate = WebApiService.getPollRate();
//    				try 
//    				{
//    	                 pollRate = jsonPollRate.getInt("rate");
//    				}
//    				catch (JSONException e)
//    				{
//    					e.printStackTrace();
//    				}
//    		    	startTime = new Date();
//					mHandler.post(mUpdateUI);
//    			}
//    			else
//    			{
//    				stopSharing();
//    			}
//            }
//        });
//      	thread.start();
//	}
	
//    private void shareMyLocation()
//    {
//    	Log.d("com.knowtime","Sharing location");
//		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//		String locationProvider = LocationManager.NETWORK_PROVIDER;
//		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
//		WebApiService.sendLocationToServer(locationUrl,lastKnownLocation);
//    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == 1 && resultCode == RESULT_OK && data != null)
    	{
			String routeNumber = data.getStringExtra("routeNumber");
    		if (Integer.parseInt(routeNumber) == -1)
    		{
//    			stopSharing();
 	    	   LocationShare.getInstance().isSharing = false;
    		}
    		else
    		{
    			locationShareIntent = new Intent(this, LocationShare.class);
    			locationShareIntent.putExtra(LocationShare.PARAM_IN_MSG, "start");
    			locationShareIntent.putExtra(LocationShare.ROUTE_IN_MSG, routeNumber);
    			this.startService(locationShareIntent);
    			FlurryAgent.logEvent("Touched the tracking button for Route: "+routeNumber);
    		}
    	}
    }
    
    public class ResponseReceiver extends BroadcastReceiver {
    	public static final String ACTION_RESP = "com.knowtime.RESPONSE";
    	   @Override
    	    public void onReceive(Context context, Intent intent) {
    	       String text = intent.getStringExtra(LocationShare.PARAM_OUT_MSG);
    	       if (text.equals("sendingLocations"))
    	       {
    	    	   startSharing();
    	       }
    	       else if (text.equals("notSendingLocation"))
    	       {
    	    	   LocationShare.getInstance().isSharing = false;
//    	    	   stopSharing();
    	       }
    	    }
    	}
}

