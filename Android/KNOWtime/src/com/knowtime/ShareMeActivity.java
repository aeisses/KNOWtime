package com.knowtime;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
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
	private String locationUrl;
	private final Handler mHandler = new Handler();
	private int pollRate;
	private ImageView sendingLineImage;
	Date startTime;
	int loopCounter;
	Messenger mService = null;
    boolean mIsBound;
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
//			switch (msg.what) {
			
//			}
			Log.d("com.knowtime"," "+msg);
			super.handleMessage(msg);
		}
	}
	
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
//            textStatus.setText("Attached.");
            try {
                Message msg = Message.obtain(null, PassiveLocationChangedReceiver.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mService = null;
//            textStatus.setText("Disconnected.");
        }
    };
    
//	private boolean isSendingLocations()
//	{
//		Date currentDate = new Date();
//		long diffTime = currentDate.getTime() - startTime.getTime();
//		return isSharing && diffTime < 108000000;
//	}
	
//	private final Runnable mUpdateUI = new Runnable() {
//		public void run() {
//			if (isSendingLocations())
//			{
//				if (loopCounter == 0)
//				{
//					sendingLineImage.setImageResource(R.drawable.sending1);
//					loopCounter++;
//				}
//				else if (loopCounter == 1)
//				{
//					sendingLineImage.setImageResource(R.drawable.sending2);
//					loopCounter++;
//				}
//				else
//				{
//					sendingLineImage.setImageResource(R.drawable.sending3);
//					shareMyLocation();
//					loopCounter = 0;
//				}
//				mHandler.postDelayed(mUpdateUI, (pollRate*1000)/3); // 1 second
//			}
//        }
//    };
    
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_me);
		locationUrl = "";
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
		    			startSharing();
					}
				}
				return false;
			}
		});
		CheckIfServiceIsRunning();
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
		FlurryAgent.onEndSession(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			doUnbindService();
		} catch (Throwable t) {
			Log.e("ShareMeActivity","Failed to unbind from the service",t);
		}
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
	}
	
	private void CheckIfServiceIsRunning() {
		// If the service is running when the activity starts, we want to automatically bind to it.
		if (PassiveLocationChangedReceiver.isRunning()) {
			doBindService();
			startSharing();
		} else {
			stopSharing();
		}
	}
	
	private void startSharing()
	{
		onMyWayButton.setBackgroundResource(R.drawable.stop);
		isSharing = true;
		connectToServerImage.setVisibility(View.VISIBLE);
		sendingImage.setVisibility(View.VISIBLE);
		loopCounter = 0;
	}
	
	private void stopSharing()
	{
		onMyWayButton.setBackgroundResource(R.drawable.onmywaybutton);
		isSharing = false;
		connectToServerImage.setVisibility(View.INVISIBLE);
		sendingImage.setVisibility(View.INVISIBLE);
		sendingLineImage.setImageResource(R.drawable.sendingline);
	}
	
	private void createNewUser(final String route)
	{
		Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
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
    		    	startTime = new Date();
//					mHandler.post(mUpdateUI);
    			}
    			else
    			{
    				stopSharing();
    			}
            }
        });
      	thread.start();
	}
	
    private void shareMyLocation()
    {
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		WebApiService.sendLocationToServer(locationUrl,lastKnownLocation);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == 1 && resultCode == RESULT_OK && data != null)
    	{
			String routeNumber = data.getStringExtra("routeNumber");
    		if (Integer.parseInt(routeNumber) == -1)
    		{
    			stopSharing();
    		}
    		else
    		{
    			createNewUser(routeNumber);
    			FlurryAgent.logEvent("Touched the tracking button for Route: "+routeNumber);
    		}
    	}
    }
    
    void doBindService() {
        bindService(new Intent(this, PassiveLocationChangedReceiver.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
//        textStatus.setText("Binding.");
    }
    void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, PassiveLocationChangedReceiver.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }
}