package com.knowtime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}
	
	public void touchAboutButton(View view)
	{
		
	}
	
	public void touchShareMeButton(View view)
	{
		Intent intent = new Intent(MenuActivity.this,ShareMeActivity.class);
		startActivity(intent);
	}
	
	public void touchTrackMyBusButton(View view)
	{
		Intent intent = new Intent(MenuActivity.this,RouteSelectActivity.class);
		startActivity(intent);
	}
	
	public void touchStopsButton(View view)
	{
		Intent intent = new Intent(MenuActivity.this,StopsActivity.class);
		startActivity(intent);
	}
	
	public void touchFavouriteButton(View view)
	{
		Intent intent = new Intent(MenuActivity.this,FavouriteActivity.class);
		startActivity(intent);
	}
}
