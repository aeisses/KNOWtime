package com.knowtime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends Activity {

	boolean buttonsEnabled;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		DatabaseHandler.getInstance(this);
		buttonsEnabled = false;
    	WebApiService.fetchAllRoutes();
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
            	WebApiService.fetchAllStops();
            	buttonsEnabled = true;
            }
        });
        thread.start();
	}
	
	public void touchAboutButton(View view)
	{
		if (buttonsEnabled)
		{
			Intent intent = new Intent(MenuActivity.this,AboutActivity.class);
			startActivity(intent);
		}
	}
	
	public void touchShareMeButton(View view)
	{
		if (buttonsEnabled)
		{
			Intent intent = new Intent(MenuActivity.this,ShareMeActivity.class);
			startActivity(intent);
		}
	}
	
	public void touchTrackMyBusButton(View view)
	{
		if (buttonsEnabled)
		{
			Intent intent = new Intent(MenuActivity.this,RouteSelectActivity.class);
			startActivity(intent);
		}
	}
	
	public void touchStopsButton(View view)
	{
		if (buttonsEnabled)
		{
			Intent intent = new Intent(MenuActivity.this,MainActivity.class);
			startActivity(intent);
		}
	}
	
	public void touchFavouriteButton(View view)
	{
		if (buttonsEnabled)
		{
			Intent intent = new Intent(MenuActivity.this,FavouriteActivity.class);
			startActivity(intent);
		}
	}
}
