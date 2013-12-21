package com.knowtime;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import java.util.HashMap;

public class MainActivity extends Activity implements GoogleMap.OnCameraChangeListener, LoaderManager.LoaderCallbacks<HashMap<String, MarkerOptions>> {

    private GoogleMap mMap;
    private Context mContext;
    public static final LatLng DEFAULT_HALIFAX_LAT_LNG = new LatLng(44.67600, -63.60800);
    public static final int DEFAULT_HALIFAX_LAT_LNG_ZOOM = 14;
    private HashMap<String, Marker> busStopMarkers = new HashMap<String, Marker>();


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();

        mMap.setMyLocationEnabled(true);
//        Location myLocation = mMap.getMyLocation();
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), DEFAULT_HALIFAX_LAT_LNG_ZOOM));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_HALIFAX_LAT_LNG, DEFAULT_HALIFAX_LAT_LNG_ZOOM));
        mMap.setOnCameraChangeListener(this);

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
	
	public void touchHamburgerMenuButton(View view)
	{
		
	}
	
    @Override
    public Loader<HashMap<String, MarkerOptions>> onCreateLoader(final int id, final Bundle position) {
        return new StopsMarkerLoader(mContext,
                position.getDouble("bottomLat"),
                position.getDouble("bottomLog"), position.getDouble("topLat"),
                position.getDouble("topLog"), position.getFloat("zoom"));
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, MarkerOptions>> loader,
                               HashMap<String, MarkerOptions> marker) {
        addItemsToMap(marker);

    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, MarkerOptions>> hashMapLoader) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
	
	 private void addItemsToMap(HashMap<String,MarkerOptions> result) {
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
}
