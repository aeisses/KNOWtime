package com.knowtime;

import android.app.Activity;
import android.content.Intent;
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
	private String currentUser;
	private final Handler mHandler = new Handler();
	private boolean isSendingLocations;

	private final Runnable mUpdateUI = new Runnable() {
		public void run() {
			if (!isSendingLocations)
			{
				shareMyLocation();
			}
			mHandler.postDelayed(mUpdateUI, 3000); // 1 second
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_me);
		currentUser = "";
		connectToServerImage = (ImageView)findViewById(R.id.connecttoserverimage);
		connectToServerImage.setVisibility(View.INVISIBLE);
		sendingImage = (ImageView)findViewById(R.id.sendingimage);
		sendingImage.setVisibility(View.INVISIBLE);
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
//						LocationShare.getInstance().stopService(new Intent(LocationShare.serviceName));
//						stopService(LocationShare.serviceName);
						stopSharing();
					}
					else
					{
						Intent intent = new Intent(ShareMeActivity.this,RoutePickerActivity.class);
						startActivityForResult(intent,1);
//						LayoutInflater li = getLayoutInflater();
//						View collectionView = li.inflate(R.layout.routecollection, (ViewGroup)findViewById(R.id.routeCollection));
//						routePickerPopup = new PopupWindow();
//						if (LocationShare.getInstance() == null)
//						{
//						Intent locationIntent = new Intent(myContext,LocationShare.class);
//						startService(locationIntent);
//							LocationShare.getInstance().startService(new Intent(LocationShare.serviceName));
//						startSharing();
//						createNewUser();
//						}
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
		// Start the animation here
	}
	
	private void stopSharing()
	{
		onMyWayButton.setBackgroundResource(R.drawable.onmywaybutton);
		isSharing = false;
		connectToServerImage.setVisibility(View.INVISIBLE);
		sendingImage.setVisibility(View.INVISIBLE);
		// Stop the animation here
	}
	
	private void createNewUser(final String route)
	{
		Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
    			currentUser = WebApiService.createNewUser(Integer.parseInt(route));
    			if (!currentUser.equals(""))
    			{
    				// Need to figure out what is going to happen here
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
    	
    }
    
//	public class ResponseReceiver extends BroadcastReceiver
//	{
//		public static final String ACTION_RESP = "";
//		
//		@Override
//		public void onReceive(Context context, Intent intent)
//		{
//		}
//	}
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == 1 && resultCode == RESULT_OK && data != null)
    	{
    		String routeNumber = data.getStringExtra("routeNumber");
    		createNewUser(routeNumber);
    	}
    }
}
