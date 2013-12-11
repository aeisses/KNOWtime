package com.knowtime.knowtime;

import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;

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
import java.util.Date;

/**
 * Created by aeisses on 2013-11-19.
 */

public class WebApiService {
    private static final String SANGSTERBASEURL = "http://api.knowtime.ca/alpha_1/";
    private static final String ENDURL          = "&all_routes=yes";
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

    private static JSONArray routesJSONArray;
    private static JSONArray stopsJSONArray;
    private static Thread locationsThread;
    private static int pollRate = 3;
    private static Boolean sendingLocations = false;

    public static void fetchAllRoutes()
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    routesJSONArray = getJSONArrayFromUrl(SANGSTERBASEURL + ROUTES + NAMES);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void fetchAllStops()
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    stopsJSONArray = getJSONArrayFromUrl(SANGSTERBASEURL+STOPS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
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

    public static void getPollRate()
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    JSONObject returnObject = getJSONObjectFromUrl(SANGSTERBASEURL+POLLRATE);
                    pollRate = returnObject.getInt("rate");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private static void sendLocationToServer(final String locationURL)
    {
        locationsThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (sendingLocations)
                {
                    try {
                        HttpClient client = new DefaultHttpClient();
                        HttpPost post = new HttpPost(locationURL);
                        post.setHeader("Accept", "application/json");
                        post.setHeader("Content-type", "application/json");
                        JSONObject jsonParam = new JSONObject();

                        StringEntity se = new StringEntity(jsonParam.toString());
                        post.setEntity(se);
                        HttpResponse responsePost = client.execute(post);
                        if (responsePost.getStatusLine().getStatusCode() != 200)
                        {
                            return;
                        }
                        locationsThread.sleep(pollRate*1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        locationsThread.start();
    }

    public static String createNewUser(final int routeId)
    {
        String returnString = "";
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
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
                                sendingLocations = true;
                                WebApiService.sendLocationToServer(h[i].getValue().replaceAll("buserver", "api"));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return returnString;
    }

    public static void getRouteForIdent(final int ident)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONArray result = getJSONArrayFromUrl(SANGSTERBASEURL+STOPTIME+ident+"/"+DateFormat.format("yyyy-MM-dd", new Date()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void getPathForRouteId(final String routeId)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONArray result = getJSONArrayFromUrl(SANGSTERBASEURL+PATHS+DateFormat.format("yyyy-MM-dd", new Date())+"/"+routeId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void loadPathForRoute(final String shortName)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONArray result = getJSONArrayFromUrl(SANGSTERBASEURL+ROUTES+SHORTS+shortName+"/"+HEADSIGNS+DateFormat.format("yyyy-MM-dd", new Date())+"/"+DateFormat.format("HH:MM", new Date()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
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

    private static String sendUrlRequest(String url)
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

    public JSONArray getRoutesJSONArray() {
        return routesJSONArray;
    }

    public JSONArray getStopsJSONArray() {
        return stopsJSONArray;
    }

    public void setSendingLocations(Boolean _sendingLocations) {
        sendingLocations = _sendingLocations;
    }
}
