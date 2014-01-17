package com.knowtime;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

import com.flurry.android.FlurryAgent;

public class MainActivity extends Activity implements GoogleMap.OnCameraChangeListener, LoaderManager.LoaderCallbacks<HashMap<String, MarkerOptions>> {

    private GoogleMap mMap;
    private Context mContext;
    public static final LatLng DEFAULT_HALIFAX_LAT_LNG = new LatLng(44.67600, -63.60800);
    public static final int DEFAULT_HALIFAX_LAT_LNG_ZOOM = 15;
    private HashMap<String, Marker> busStopMarkers = new HashMap<String, Marker>();
    private Boolean showStops;
    private ProgressBar mapMarkerProgressBar;
	private TextView mapMarkerProgressBarText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
    	mapMarkerProgressBar = (ProgressBar) findViewById(R.id.mapMarkerProgressBar);
    	mapMarkerProgressBarText = (TextView) findViewById(R.id.mapMarkerProgressBarText);
    	showStops = true;
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
				mMap.setOnInfoWindowClickListener(getInfoWindowClickListener());
			}
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
		FlurryAgent.onEndSession(this);
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
                position.getDouble("topLog"), position.getFloat("zoom"),
                showStops);
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
        if (showStops)
        {
        	for (String newStop : result.keySet()) {
        		busStopMarkers.put(newStop, mMap.addMarker(result.get(newStop)));
        	}
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
        b.putBoolean("showStops", showStops);
        getLoaderManager().restartLoader(0, b, this).forceLoad();
    }
    
    public OnInfoWindowClickListener getInfoWindowClickListener()
    {
    	return new OnInfoWindowClickListener()
    	{
    		@Override
    		public void onInfoWindowClick(Marker marker)
    		{
    			Intent intent = new Intent(MainActivity.this,StopsActivity.class);
    			intent.putExtra("STOP_NUMBER", marker.getSnippet());
    			intent.putExtra("STOP_NAME", marker.getTitle());
    			startActivity(intent);
    		}
    	};
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
}
