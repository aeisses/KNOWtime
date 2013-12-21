package com.knowtime;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONObject;

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
        if (storedStopMarkers.size() == 0) {
            WebApiService wb = new WebApiService();
            wb.fetchAllStops();

            JSONArray jsonArray = null;
            while (jsonArray == null) {
                jsonArray = wb.getStopsJSONArray();
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    Log.e(getClass().getCanonicalName(), "ERROR: " + e);
                }
            }
            Log.d(getClass().getCanonicalName(), "JSON Array size:" + jsonArray.length());
            storedStopMarkers = serlizeJsonArray(jsonArray);
        }
        return limitMarkerByBounds(storedStopMarkers);
    }

    private HashMap<String, MarkerOptions> serlizeJsonArray(JSONArray jArray) {
        HashMap<String, MarkerOptions> hashMap = new HashMap<String, MarkerOptions>();
        MarkerOptions markerStops;
        String stopNumber;
        String name;
        JSONObject location;
        double lat;
        double lng;

        try {
            for (int i = 0; i < jArray.length(); i++) {
                markerStops = new MarkerOptions();
                JSONObject jsonData = jArray.getJSONObject(i);
                stopNumber = jsonData.getString("stopNumber");
                name = jsonData.getString("name");
                location = jsonData.getJSONObject("location");
                lat = location.getDouble("lat");
                lng = location.getDouble("lng");

                markerStops.draggable(false);
                markerStops.anchor(.6f, .6f);

                markerStops.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop));
                markerStops.position(new LatLng(lat, lng));

                markerStops.snippet(stopNumber);
                markerStops.title(name);

                // Adding marker to the result HashMap.
                hashMap.put(stopNumber, markerStops);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
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
