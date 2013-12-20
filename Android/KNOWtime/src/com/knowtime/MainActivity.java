package com.knowtime;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private GoogleMap map;
    LatLng defaultHalifaxLatLng = new LatLng(44.67600, -63.60800);
    int defaultHalifaxLatLngZoom = 7;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultHalifaxLatLng, defaultHalifaxLatLngZoom));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void touchHamburgerMenuButton(View view)
	{
		
	}
}
