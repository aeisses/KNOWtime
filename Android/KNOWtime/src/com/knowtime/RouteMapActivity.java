package com.knowtime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class RouteMapActivity extends Activity {

	private String routeNumber;
	private String routeId;
    private GoogleMap mMap;
	private ProgressBar progressBar;
	private final Handler mHandler = new Handler();
	private Marker[] markers = null;
	private Context mContext;
	private boolean isUpdatingLocations;
	private ImageButton favouriteButton;
	private Route route;
	
	private final Runnable mUpdateUI = new Runnable() {
		public void run() {
			if (!isUpdatingLocations)
			{
				updateBusesLocation();
			}
			mHandler.postDelayed(mUpdateUI, 3000); // 1 second
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		mContext = this;
		isUpdatingLocations = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			routeId = extras.getString("ROUTE_ID");
			routeNumber = extras.getString("ROUTE_NUMBER");
			route = DatabaseHandler.getInstance().getRoute(routeNumber);
			route.setId(routeId);
			DatabaseHandler.getInstance().updateRoute(route);
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
		favouriteButton = (ImageButton)this.findViewById(R.id.favouritebutton);
		favouriteButton.setSelected(route.getFavourite());
		getRoute();
	}
	
	private void getRoute()
	{
		Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
            	JSONArray pathArray = WebApiService.getPathForRouteId(routeId);
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
        						mHandler.post(mUpdateUI);
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
		route.setFavourite(favouriteButton.isSelected());
		DatabaseHandler.getInstance().updateRoute(route);
		this.finish();
	}
	
	public void touchFavouriteButton(View view)
	{
		view.setSelected(!view.isSelected());
	}
	
	public void updateBusesLocation()
	{	
		Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
            	isUpdatingLocations = true;
            	final JSONArray routes = WebApiService.getEstimatesForRoute(Integer.parseInt(routeNumber));
            	if (routes != null)
            	{
            		runOnUiThread(new Runnable() {
            			@Override
            			public void run() {
            				if (markers != null)
            				{
            					clearMarkers();
            				}
            				markers = new Marker[routes.length()];
            			}
            		});
            		try
            		{
            			for (int i=0; i<routes.length(); i++)
            			{
            				final JSONObject busJSON = routes.getJSONObject(i);
            				final JSONObject busLocationJSON = busJSON.getJSONObject("location");
            				final float lat = Float.parseFloat(busLocationJSON.getString("lat"));
            				final float lng = Float.parseFloat(busLocationJSON.getString("lng")); 
            				if (busLocationJSON == null || lat != 0 || lng != 0)
            				{
            					final int markerCounter = i;
            					runOnUiThread(new Runnable() {
            						@Override
            						public void run() {
            							markers[markerCounter] = mMap.addMarker(new MarkerOptions()
            							.position(new LatLng(lat,lng))
            							.anchor(0.5f,0.5f)
            							.icon(BitmapDescriptorFactory.fromResource(R.drawable.busicon)));
            						}
            					});
            				} 
            				else if (i != 0)
            				{
            					break;
            				}
            				else
            				{
            					runOnUiThread(new Runnable() {
            						@Override
            						public void run() {
            							loadAlertDialog();
            						}
            					});
            					break;
            				}
            			}
            		}
            		catch (JSONException e)
            		{
            			e.printStackTrace();
            			runOnUiThread(new Runnable() {
            				@Override
            				public void run() {
            					loadAlertDialog();
            				}
            			});
            		}
            	}
            	else
            	{
            		runOnUiThread(new Runnable() {
            			@Override
            			public void run() {
            				loadAlertDialog();
            			}
            		});
            	}
            	isUpdatingLocations = false;
            }
        });
		thread.start();
	}
	
	private void loadAlertDialog()
	{
    	isUpdatingLocations = false;
		mHandler.removeCallbacks(mUpdateUI);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		alertDialog.setTitle("Alert");
		alertDialog.setMessage("No buses can currently be found. This can be because no one is sending a signal or a server issue.");
		alertDialog.setNegativeButton("ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		alertDialog.show();
	}
	
	private void clearMarkers() {
		for (int i = 0; i < markers.length; i++ ) {
			if (markers[i] != null)
			{
				markers[i].remove();
				markers[i] = null;
			}
		}
		markers = null;
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		// Need to save the favourites information here, some how, some way
		
	}
}
