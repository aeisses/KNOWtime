package com.knowtime;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class StopsMarkerLoader extends AsyncTaskLoader<HashMap<String, MarkerOptions>> {

    private double bottomLat;
    private double bottomLng;
    private double topLat;
    private double topLng;
    private float zoom;
    private boolean showStops;
    private static HashMap<String, MarkerOptions> storedStopMarkers = new HashMap<String, MarkerOptions>();

    public StopsMarkerLoader(Context context, double bottomLat, double bottomLng, double topLat, double topLng, float zoom, boolean showStops) {
        super(context);
        this.bottomLat = bottomLat;
        this.bottomLng = bottomLng;
        this.topLat = topLat;
        this.topLng = topLng;
        this.zoom = zoom;
        this.showStops = showStops;
    }

	@Override
	public HashMap<String, MarkerOptions> loadInBackground() {
		if (storedStopMarkers.size() == 0) {
			List<Stop> stops;
			if (DatabaseHandler.getInstance().getStopsCount() > 0) {
				 stops =  DatabaseHandler.getInstance().getAllStops();
				storedStopMarkers = createStopMarker(stops);
				Log.d(getClass().getCanonicalName(), "DB Stop Size:"+stops.size());
			} else {
				stops =  WebApiService.fetchAllStops();
				storedStopMarkers = createStopMarker(stops);
				//adding to Database on a background thread
				Log.d(getClass().getCanonicalName(), "External DB Stop Size:"+stops.size());
			}
		}
		return limitMarkerByBounds(storedStopMarkers);
	}

    private HashMap<String, MarkerOptions> limitMarkerByBounds(HashMap<String, MarkerOptions> busMarker) {
        HashMap<String, MarkerOptions> resultMarker = new HashMap<String, MarkerOptions>();
        LatLng latlng;
        double lat;
        double lng;

        Log.d(getClass().getCanonicalName(), "current zoom :" + zoom + "Default zoom:" + MainActivity.DEFAULT_HALIFAX_LAT_LNG_ZOOM);

        if (this.zoom >= MainActivity.DEFAULT_HALIFAX_LAT_LNG_ZOOM) {
            final Object[] currentStops = busMarker.keySet().toArray();
            MarkerOptions m;
            for (Object currentStop : currentStops) {
                m = busMarker.get(currentStop);
                latlng = m.getPosition();
                lat = latlng.latitude;
                lng = latlng.longitude;

                if (showStops)
                {
                	if (this.topLat > lat && lat > this.bottomLat ) {
                		if (this.topLng > lng && lng > this.bottomLng ){
                			resultMarker.put((String)currentStop, busMarker.get(currentStop));
                		}
                	}
                }
            }
        }
        Log.d(getClass().getCanonicalName(), "limited result size :" + resultMarker.size());
        return resultMarker;
    }
    
	private HashMap<String, MarkerOptions> createStopMarker(List<Stop> stops) {
		HashMap<String, MarkerOptions> hashMap = new HashMap<String, MarkerOptions>();

		MarkerOptions markerStops;

		for (Stop stop : stops) {
			markerStops = new MarkerOptions();
			markerStops.draggable(false);
			markerStops.anchor(.6f, .6f);

			markerStops.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop));
			markerStops.position(new LatLng(stop.getLat(), stop.getLng()));

			markerStops.snippet(stop.getCode());
			markerStops.title(stop.getName());

			// Adding marker to the result HashMap.
			hashMap.put(stop.getCode(), markerStops);

		}
		return hashMap;
	}
}
