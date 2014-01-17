package com.knowtime;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.text.format.DateFormat;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by aeisses on 2013-11-19.
 */

public class WebApiService {
    private static final String SANGSTERBASEURL = "http://api.knowtime.ca/alpha_1/";
    private static final String STOPS           = "stops";
    private static final String ROUTES          = "routes/";
    private static final String NAMES           = "names";
    private static final String SHORTS          = "short:";
    private static final String PATHS           = "paths/";
    private static final String STOPTIME        = "stoptimes/";
    private static final String HEADSIGNS       = "headsigns/";
    private static final String USERS           = "users/";
    private static final String NEW             = "new/";
    private static final String ESTIMATE        = "estimates/";
    private static final String POLLRATE        = "pollrate";

    private static JSONArray stopsJSONArray;
//    private static Thread locationsThread;
    
    public static void fetchAllRoutes()
    {
//        Thread thread = new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
                try
                {
                	JSONArray routesJSONArray = getJSONArrayFromUrl(SANGSTERBASEURL + ROUTES + NAMES);
                    for (int i=0; i<routesJSONArray.length(); i++)
                    {
                    	JSONObject routeJSON = routesJSONArray.getJSONObject(i);
                    	Route route = new Route(routeJSON.getString("longName"),routeJSON.getString("shortName"));
                    	if (DatabaseHandler.getInstance().getRoute(route.getShortName()) == null)
                    	{
                    		DatabaseHandler.getInstance().addRoute(route);
                    	}
                    }
                    Log.d("com.knowtime","Routes Loaded");
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            }
//        });
//        thread.start();
    }
	
	public static List<Stop> fetchAllStops() {
		List<Stop> stopList = new ArrayList<Stop>();
		SQLiteDatabase db = DatabaseHandler.getInstance().getWritableDatabase();
		try {
			stopsJSONArray = getJSONArrayFromUrl(SANGSTERBASEURL + STOPS);
			db.beginTransaction();
			for (int i = 0; i < stopsJSONArray.length(); i++) {
				JSONObject stopJSON = stopsJSONArray.getJSONObject(i);
				JSONObject locationJSON = stopJSON.getJSONObject("location");
            	Stop stop = new Stop(
            			stopJSON.getString("stopNumber"),
            			stopJSON.getString("name"),
            			Double.parseDouble(locationJSON.getString("lat")),
            			Double.parseDouble(locationJSON.getString("lng")));
            	stopList.add(stop);
            	db.execSQL(DatabaseHandler.getInstance().createStopInsertQuery(stop));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		Log.d("com.knowtime", "Loaded stops");
		return stopList;
	}

    public static JSONArray getEstimatesForRoute(final int routeId)
    {
        try
        {
            return getJSONArrayFromUrl(SANGSTERBASEURL+ESTIMATE+SHORTS+routeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getPollRate()
    {
    	return getJSONObjectFromUrl(SANGSTERBASEURL+POLLRATE);
    }

    public static void sendLocationToServer(final String locationURL, final Location location)
    {
//        locationsThread = new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
            	try {
            		HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(locationURL);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("lat",location.getLatitude());
                    jsonParam.put("lng",location.getLongitude());
                    StringEntity se = new StringEntity(jsonParam.toString());
                    post.setEntity(se);
                    HttpResponse responsePost = client.execute(post);
                    if (responsePost.getStatusLine().getStatusCode() != 200)
                    {
                        return;
                    }
                } catch (Exception e) {
                   e.printStackTrace();
                }
//            }
//        });
//        locationsThread.start();
    }

    public static String createNewUser(final int routeId)
    {
        String returnString = "";
        try {
        	HttpClient client = new DefaultHttpClient();
        	HttpPost post = new HttpPost(SANGSTERBASEURL+USERS+NEW+routeId);
        	post.setHeader("Content-type", "application/json");
        	HttpResponse responsePOST = client.execute(post);
        	if (responsePOST.getStatusLine().getStatusCode() == 201)
        	{
        		Header[] h = responsePOST.getAllHeaders();
        		for (int i=0; i<h.length; i++)
        		{
        			if (h[i].getName().equals("Location"))
        			{
        				returnString = h[i].getValue().replaceAll("buserver", "api");
        			}
        		}
        	}
        	else
        	{
        		returnString = "";
        	}            
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return returnString;
    }

    public static JSONArray getRouteForIdent(final int ident)
    {
    	try
        {
    		return getJSONArrayFromUrl(SANGSTERBASEURL+STOPTIME+ident+"/"+DateFormat.format("yyyy-MM-dd", new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return null;
    }

    public static JSONArray getPathForRouteId(final String routeId)
    {
    	try
    	{
    		return getJSONArrayFromUrl(SANGSTERBASEURL+PATHS+DateFormat.format("yyyy-MM-dd", new Date())+"/"+routeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return null;
    }

    public static JSONArray loadPathForRoute(final String shortName)
    {
    	try
        {
    		SimpleDateFormat formatterIn = new SimpleDateFormat("yyyy-MM-dd");
    		SimpleDateFormat formatterOut = new SimpleDateFormat("HH:mm");
    		return getJSONArrayFromUrl(SANGSTERBASEURL+ROUTES+SHORTS+shortName+"/"+HEADSIGNS+formatterIn.format(new Date())+"/"+formatterOut.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return null;
    }

    public static String sendUrlRequest(String url)
    {
        HttpClient client = new DefaultHttpClient();
        HttpGet post = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    private static String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static JSONObject getJSONObjectFromUrl(String url) {
        JSONObject jObj = null;
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(WebApiService.getResponseFromUrl(url));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    private static JSONArray getJSONArrayFromUrl(String url) {
        JSONArray jObj = null;
        Log.d("com.knowtime","URL: "+url);
        // try parse the string to a JSON object
        try {
            jObj = new JSONArray(WebApiService.getResponseFromUrl(url));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    private static String getResponseFromUrl(String url)
    {
        InputStream is = null;
        String response = "";

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            response = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return response;
    }

    public static List<Route> getRoutesList() {
        return DatabaseHandler.getInstance().getAllRoutes();
    }

    private static Boolean isStringInt(String value)
    {
    	try
    	{
    		Integer.parseInt(value);
    	}
    	catch (Exception e)
    	{
    		return false;
    	}
    	return true;
    }
    
    public static Object[] getRoutesArray() {
		Vector<String> returnVector = new Vector<String>();
		List<Route> routes = DatabaseHandler.getInstance().getAllRoutes();
    	for (int i=0; i<routes.size(); i++)
    	{
    		Route route = (Route)routes.get(i);
    		if (WebApiService.isStringInt(route.getShortName()))
    		{
    			returnVector.add(routes.get(i).getShortName());
    		}
    	}	
    	return returnVector.toArray();
    }
    
    public static List<Stop> getStopsList() {
        return DatabaseHandler.getInstance().getAllStops();
    }
    
    public static JSONArray getStopsJSONArray()
    {
    	return stopsJSONArray;
    }
}
