package com.knowtime;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends ActionBarActivity {

	private GoogleMap map;
    LatLng defaultHalifaxLatLng = new LatLng(44.67600, -63.60800);
    int defaultHalifaxLatLngZoom = 7;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		setBehindContentView(R.layout.menu_frame);
		
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultHalifaxLatLng, defaultHalifaxLatLngZoom));
        
//        SlidingMenu sm = getSlidingMenu();
//        sm.setBehindOffsetRes(30);
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.github:
//			Util.goToGitHub(this);
//			return true;
//		case R.id.about:
//			new AlertDialog.Builder(this)
//			.setTitle(R.string.about)
//			.setMessage(Html.fromHtml(getString(R.string.about_msg)))
//			.show();
//			break;
//		case R.id.licenses:
//			new AlertDialog.Builder(this)
//			.setTitle(R.string.licenses)
//			.setMessage(Html.fromHtml(getString(R.string.apache_license)))
//			.show();
//			break;
//		case R.id.contact:
//			final Intent email = new Intent(android.content.Intent.ACTION_SENDTO);
//			String uriText = "mailto:jfeinstein10@gmail.com" +
//					"?subject=" + URLEncoder.encode("SlidingMenu Demos Feedback"); 
//			email.setData(Uri.parse(uriText));
//			try {
//				startActivity(email);
//			} catch (Exception e) {
//				Toast.makeText(this, R.string.no_email, Toast.LENGTH_SHORT).show();
//			}
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
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
