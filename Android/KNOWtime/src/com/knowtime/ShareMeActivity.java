package com.knowtime;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
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
	
	private boolean isSendingLocations()
	{
		Date currentDate = new Date();
		long diffTime = currentDate.getTime() - startTime.getTime();
		return isSharing && diffTime < 108000000;
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
					shareMyLocation();
					loopCounter = 0;
				}
				mHandler.postDelayed(mUpdateUI, (pollRate*1000)/3); // 1 second
			}
        }
    };
    
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
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
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
					mHandler.post(mUpdateUI);
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
    		}
    	}
    }
}
