package com.knowtime;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity {

	private GoogleMap map;
    LatLng defaultHalifaxLatLng = new LatLng(44.67600, -63.60800);
    int defaultHalifaxLatLngZoom = 7;
    private SlidingMenu hamburgerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		setBehindContentView(R.layout.menu_frame);
		
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultHalifaxLatLng, defaultHalifaxLatLngZoom));
        
        hamburgerMenu = new SlidingMenu(this);
        hamburgerMenu.setMode(SlidingMenu.LEFT);
        hamburgerMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        sm.setShadowWidthRes(100);
//        sm.setBehindOffsetRes(100);
        hamburgerMenu.setBehindWidth(250);
        hamburgerMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        hamburgerMenu.setMenu(R.layout.menu);
        
//        sm.setBehindOffsetRes(30);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void touchHamburgerMenuButton(View view)
	{
		if (hamburgerMenu.isMenuShowing())
		{
			hamburgerMenu.showContent();
		} else {
			hamburgerMenu.showMenu();
		}
	}
	
	public void touchShareMeButton(View view)
	{
		Intent intent = new Intent(MainActivity.this,ShareMeActivity.class);
		startActivity(intent);
		hamburgerMenu.showContent(false);
	}
	
	public void touchAboutButton(View view)
	{
		Intent intent = new Intent(MainActivity.this,AboutActivity.class);
		startActivity(intent);
		hamburgerMenu.showContent(false);
	}
	
	public void touchStopsButton(View view)
	{
		Intent intent = new Intent(MainActivity.this,StopsActivity.class);
		startActivity(intent);
		hamburgerMenu.showContent(false);
	}
	
	public void touchFavouriteButton(View view)
	{
		Intent intent = new Intent(MainActivity.this,FavouriteActivity.class);
		startActivity(intent);
		hamburgerMenu.showContent(false);
	}
	
	public void touchHfxTransitButton(View view)
	{
		Intent intent = new Intent(MainActivity.this,TwitterActivity.class);
		startActivity(intent);
		hamburgerMenu.showContent(false);
	}
	
	public void privacyPolicyButton(View view)
	{
		Intent intent = new Intent(MainActivity.this,PrivacyPolicyActivity.class);
		startActivity(intent);
		hamburgerMenu.showContent(false);
	}
}
