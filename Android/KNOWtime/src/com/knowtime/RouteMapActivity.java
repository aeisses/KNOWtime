package com.knowtime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class RouteMapActivity extends Activity {

	private String routeNumber;
    private GoogleMap mMap;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			routeNumber = extras.getString("ROUTE_NUMBER");
			Log.d("com.timeplay","RouteNumber: "+routeNumber);
		}
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();

			if (mMap != null) {
//				mMap.setMyLocationEnabled(true);

				LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
				String locationProvider = LocationManager.NETWORK_PROVIDER;
				Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()),
						MainActivity.DEFAULT_HALIFAX_LAT_LNG_ZOOM));
			}
		}
		// Load the route
		progressBar = (ProgressBar) findViewById(R.id.routesProgressBar);
		getRoute();
	}
	
	private void getRoute()
	{
		Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
            	JSONArray pathArray = WebApiService.getPathForRouteId(routeNumber);
            	if (pathArray.length() > 1)
            	{
            		try
                	{
            			float latPointMin = 0.0f;
            			float latPointMax = 0.0f;
            			float lngPointMin = 0.0f;
            			float lngPointMax = 0.0f;
            			for (int j=0; j<pathArray.length(); j++)
            			{
            				JSONObject path = pathArray.getJSONObject(j);
            				JSONArray routePoints = path.getJSONArray("pathPoints");
            				PolylineOptions routePointsValues = new PolylineOptions().width(5).color(Color.BLACK);
            				for (int i=0; i<routePoints.length(); i++)
            				{
            					JSONObject point = routePoints.getJSONObject(i);
            					float latPoint = Float.parseFloat(point.getString("lat"));
            					float lngPoint = Float.parseFloat(point.getString("lng"));
            					if (latPoint < latPointMin || latPointMin == 0) latPointMin = latPoint;
            					if (latPoint > latPointMax || latPointMax == 0) latPointMax = latPoint;
            					if (lngPoint < lngPointMin || lngPointMin == 0) lngPointMin = lngPoint;
            					if (lngPoint > lngPointMax || lngPointMax == 0) lngPointMax = lngPoint;
            					routePointsValues.add(new LatLng(latPoint,lngPoint));
            				}
            				final PolylineOptions routeLine = routePointsValues;
            				runOnUiThread(new Runnable() {
            					@Override
            					public void run() {
            						mMap.addPolyline(routeLine);
            					}
            				});
            			}
            			Log.d("com.knowtime","LatMin: "+latPointMin);
            			Log.d("com.knowtime","LatMax: "+latPointMax);
            			Log.d("com.knowtime","LngMin: "+lngPointMin);
            			Log.d("com.knowtime","LngMax: "+lngPointMax);
            			final float latPointMinFinal = latPointMin-0.001f;
            			final float latPointMaxFinal = latPointMax+0.001f;
            			final float lngPointMinFinal = lngPointMin-0.001f;
            			final float lngPointMaxFinal = lngPointMax+0.001f;
        				runOnUiThread(new Runnable() {
        					@Override
        					public void run() {
        						mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
        								new LatLngBounds(
        										new LatLng(latPointMinFinal,lngPointMinFinal),
        										new LatLng(latPointMaxFinal,lngPointMaxFinal)),0));
        						progressBar.setVisibility(View.INVISIBLE);
        					}
        				});
                	}
                	catch (JSONException e)
                	{
                		e.printStackTrace();
                	}
            	}            	
            }
        });
		thread.start();
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
	}
	
	public void touchFavouriteButton(View view)
	{
		view.setSelected(!view.isSelected());
	}
}
