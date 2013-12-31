package com.knowtime;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;

public class StopsMarkerLoader extends AsyncTaskLoader<HashMap<String, MarkerOptions>> {

    private double bottomLat;
    private double bottomLng;
    private double topLat;
    private double topLng;
    private float zoom;
    private static HashMap<String, MarkerOptions> storedStopMarkers = new HashMap<String, MarkerOptions>();

    public StopsMarkerLoader(Context context, double bottomLat, double bottomLng, double topLat, double topLng, float zoom) {
        super(context);
        this.bottomLat = bottomLat;
        this.bottomLng = bottomLng;
        this.topLat = topLat;
        this.topLng = topLng;
        this.zoom = zoom;
    }

    @Override
    public HashMap<String, MarkerOptions> loadInBackground() {
        if (storedStopMarkers.size() == 0)
        {
        	storedStopMarkers = WebApiService.fetchAllStops();
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

                if (this.topLat > lat && lat > this.bottomLat ) {
                    if (this.topLng > lng && lng > this.bottomLng ){
                        resultMarker.put((String)currentStop, busMarker.get(currentStop));
                    }
                }
            }
        }
        Log.d(getClass().getCanonicalName(), "limited result size :" + resultMarker.size());
        return resultMarker;
    }
}
