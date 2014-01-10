/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.knowtime;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Service that requests a list of nearby locations from the underlying web service.
 * TODO Update the URL and XML parsing to correspond with your underlying web service.
 */
public class LocationUpdateService extends IntentService {  

  protected static String TAG = "PlacesUpdateService";
  
  protected ContentResolver contentResolver;
  protected SharedPreferences prefs;
  protected Editor prefsEditor;
  protected ConnectivityManager cm;
  protected boolean lowBattery = false;
  protected boolean mobileData = false;
  protected int prefetchCount = 0;
 
  public LocationUpdateService() {
    super(TAG);
    setIntentRedeliveryMode(false);
  }
  
  /**
   * Set the Intent Redelivery mode to true to ensure the Service starts "Sticky"
   * Defaults to "true" on legacy devices.
   */
  protected void setIntentRedeliveryMode(boolean enable) {}

  /**
   * Returns battery status. True if less than 10% remaining.
   * @param battery Battery Intent
   * @return Battery is low
   */
  protected boolean getIsLowBattery(Intent battery) {
    float pctLevel = (float)battery.getIntExtra(BatteryManager.EXTRA_LEVEL, 1) / 
    battery.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
    return pctLevel < 0.15;
  }
  
  @Override
  public void onCreate() {
    super.onCreate();
    cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    contentResolver = getContentResolver();
    prefs = getSharedPreferences(PlacesConstants.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
    prefsEditor = prefs.edit();
  }

  /**
   * {@inheritDoc}
   * Checks the battery and connectivity state before removing stale venues
   * and initiating a server poll for new venues around the specified 
   * location within the given radius.
   */
  @Override
  protected void onHandleIntent(Intent intent) {
  	// Check if we're running in the foreground, if not, check if
  	// we have permission to do background updates.
  	boolean backgroundAllowed = cm.getBackgroundDataSetting();
  	boolean inBackground = prefs.getBoolean(PlacesConstants.EXTRA_KEY_IN_BACKGROUND, true);
  	
  	if (!backgroundAllowed && inBackground) return;
	
    // Extract the location and radius around which to conduct our search.
    Location location = new Location(PlacesConstants.CONSTRUCTED_LOCATION_PROVIDER);
    String url = "";
    
    Bundle extras = intent.getExtras();
    if (intent.hasExtra(PlacesConstants.EXTRA_KEY_LOCATION)) {
      location = (Location)(extras.get(PlacesConstants.EXTRA_KEY_LOCATION));
      url = (String)(extras.get(PlacesConstants.EXTRA_URL_STRING));
    }
    
    // Check if we're in a low battery situation.
    IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent battery = registerReceiver(null, batIntentFilter);
    lowBattery = getIsLowBattery(battery);
    
    // Check if we're connected to a data network, and if so - if it's a mobile network.
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    mobileData = activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

    // If we're not connected, enable the connectivity receiver and disable the location receiver.
    // There's no point trying to poll the server for updates if we're not connected, and the 
    // connectivity receiver will turn the location-based updates back on once we have a connection.
    if (!isConnected) {
      PackageManager pm = getPackageManager();
      
      ComponentName connectivityReceiver = new ComponentName(this, ConnectivityChangedReceiver.class);
      ComponentName locationReceiver = new ComponentName(this, LocationChangedReceiver.class);
      ComponentName passiveLocationReceiver = new ComponentName(this, PassiveLocationChangedReceiver.class);

      pm.setComponentEnabledSetting(connectivityReceiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 
        PackageManager.DONT_KILL_APP);
            
      pm.setComponentEnabledSetting(locationReceiver,
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 
        PackageManager.DONT_KILL_APP);
      
      pm.setComponentEnabledSetting(passiveLocationReceiver,
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 
        PackageManager.DONT_KILL_APP);
    }
    else {
      // If we are connected check to see if this is a forced update (typically triggered
      // when the location has changed).
      boolean doUpdate = intent.getBooleanExtra(PlacesConstants.EXTRA_KEY_FORCEREFRESH, false);
      
      // If it's not a forced update (for example from the Activity being restarted) then
      // check to see if we've moved far enough, or there's been a long enough delay since
      // the last update and if so, enforce a new update.
      if (!doUpdate) {
        // Retrieve the last update time and place.
        long lastTime = prefs.getLong(PlacesConstants.SP_KEY_LAST_LIST_UPDATE_TIME, Long.MIN_VALUE);
        long lastLat = prefs.getLong(PlacesConstants.SP_KEY_LAST_LIST_UPDATE_LAT, Long.MIN_VALUE);
        long lastLng = prefs.getLong(PlacesConstants.SP_KEY_LAST_LIST_UPDATE_LNG, Long.MIN_VALUE);
        Location lastLocation = new Location(PlacesConstants.CONSTRUCTED_LOCATION_PROVIDER);
        lastLocation.setLatitude(lastLat);
        lastLocation.setLongitude(lastLng);
        
        // If update time and distance bounds have been passed, do an update.
        if ((lastTime < System.currentTimeMillis()-PlacesConstants.MAX_TIME) ||
           (lastLocation.distanceTo(location) > PlacesConstants.MAX_DISTANCE))
          doUpdate = true;
      }
      
      if (doUpdate) {
        // Refresh the prefetch count for each new location.
        prefetchCount = 0;
        // Remove the old locations
        // Hit the server for new venues for the current location.
        sendLocation(url, location);
      }
      else
        Log.d(TAG, "Place List is fresh: Not refreshing");
      
      // Retry any queued checkins. Do not need this
//      Intent checkinServiceIntent = new Intent(this, PlaceCheckinService.class);
//      startService(checkinServiceIntent);
    }
    Log.d(TAG, "Place List Download Service Complete");
  }
  
  /**
   * Polls the underlying service to return a list of places within the specified
   * radius of the specified Location. 
   * @param location Location
   * @param radius Radius
   */
  protected void sendLocation(String url, Location location) {   
    // Log to see if we'll be prefetching the details page of each new place.
    if (mobileData) {
      Log.d(TAG, "Not prefetching due to being on mobile");
    } else if (lowBattery) {
      Log.d(TAG, "Not prefetching due to low battery");
    }
    WebApiService.sendLocationToServer(url,location);
  }
  
}