package com.knowtime;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

import org.w3c.dom.Text;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity implements GoogleMap.OnCameraChangeListener, LoaderManager.LoaderCallbacks<HashMap<String, MarkerOptions>> {

    private GoogleMap mMap;
    private Context mContext;
    public static final LatLng DEFAULT_HALIFAX_LAT_LNG = new LatLng(44.67600, -63.60800);
    public static final int DEFAULT_HALIFAX_LAT_LNG_ZOOM = 14;
    private HashMap<String, Marker> busStopMarkers = new HashMap<String, Marker>();
    private SlidingMenu hamburgerMenu;
    
    private ProgressBar mapMarkerProgressBar;
	private TextView mapMarkerProgressBarText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
    	mapMarkerProgressBar = (ProgressBar) findViewById(R.id.mapMarkerProgressBar);
    	mapMarkerProgressBarText = (TextView) findViewById(R.id.mapMarkerProgressBarText);

		mContext = getApplicationContext();

		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();

			if (mMap != null) {
				mMap.setMyLocationEnabled(true);

				LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
				String locationProvider = LocationManager.NETWORK_PROVIDER;
				Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()),
						DEFAULT_HALIFAX_LAT_LNG_ZOOM));

//				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_HALIFAX_LAT_LNG, DEFAULT_HALIFAX_LAT_LNG_ZOOM));
				mMap.setOnCameraChangeListener(this);
			}
		}
		hamburgerMenu = new SlidingMenu(this);
		hamburgerMenu.setMode(SlidingMenu.LEFT);
		hamburgerMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// sm.setShadowWidthRes(100);
		// sm.setBehindOffsetRes(100);
		hamburgerMenu.setBehindWidth(250);
		hamburgerMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		hamburgerMenu.setMenu(R.layout.menu);

		// sm.setBehindOffsetRes(30);

		refreshBusStopMarkers(null);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        refreshBusStopMarkers(cameraPosition);
    }

    @Override
    public Loader<HashMap<String, MarkerOptions>> onCreateLoader(final int id, final Bundle position) {
    	setStopMarkerProgressBar(true);
        return new StopsMarkerLoader(mContext,
                position.getDouble("bottomLat"),
                position.getDouble("bottomLog"), position.getDouble("topLat"),
                position.getDouble("topLog"), position.getFloat("zoom"));
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, MarkerOptions>> loader,
                               HashMap<String, MarkerOptions> marker) {
    	setStopMarkerProgressBar(false);
    	addItemsToMap(marker);
    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, MarkerOptions>> hashMapLoader) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    private void addItemsToMap(HashMap<String, MarkerOptions> result) {
        final Object[] currentStops = busStopMarkers.keySet().toArray();
        Marker marker;
        for (Object currentStop : currentStops) {
            marker = busStopMarkers.get(currentStop);

            if (!result.containsKey(currentStop)
                    && !marker.isInfoWindowShown()) {
                marker.remove();
                busStopMarkers.remove(currentStop);
            } else {
                result.remove(currentStop);
            }
        }
        for (String newStop : result.keySet()) {
            busStopMarkers.put(newStop, mMap.addMarker(result.get(newStop)));
        }
    }

    private void refreshBusStopMarkers(CameraPosition position) {

        if (position == null) {
            position = mMap.getCameraPosition();
        }

        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        LatLng bottom = bounds.southwest;
        LatLng top = bounds.northeast;
        float zoom = position.zoom;

        Bundle b = new Bundle();
        b.putDouble("bottomLat", bottom.latitude);
        b.putDouble("bottomLog", bottom.longitude);
        b.putDouble("topLat", top.latitude);
        b.putDouble("topLog", top.longitude);
        b.putFloat("zoom", zoom);

        getLoaderManager().restartLoader(0, b, this).forceLoad();

    }
    
	private void setStopMarkerProgressBar(boolean b) {
		if (b) {
			mapMarkerProgressBar.setVisibility(View.VISIBLE);
			mapMarkerProgressBarText.setVisibility(View.VISIBLE);
		} else {
			mapMarkerProgressBar.setVisibility(View.INVISIBLE);
			mapMarkerProgressBarText.setVisibility(View.INVISIBLE);
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
