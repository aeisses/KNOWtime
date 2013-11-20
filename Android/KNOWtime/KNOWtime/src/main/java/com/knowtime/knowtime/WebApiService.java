package com.knowtime.knowtime;

import android.text.format.DateFormat;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by aeisses on 2013-11-19.
 */

public class WebApiService {
    private static final String SANGSTERBASEURL = "http://knowtime.ca/api/alpha_1/";
    private static final String ENDURL          = "&all_routes=yes";
    private static final String STOPS           = "stops";
    private static final String ROUTES          = "routes/";
    private static final String NAMES           = "names";
    private static final String SHORTS          = "short/";
    private static final String PATHS           = "paths/";
    private static final String STOPTIME        = "stoptimes/";
    private static final String HEADSIGNS       = "headsigns/";
    private static final String USERS           = "users/";
    private static final String NEW             = "new/";
    private static final String ESTIMATE        = "estimates/";

    public static void fetchAllRoutes()
    {
        String result = sendUrlRequest(SANGSTERBASEURL+ROUTES+NAMES);
    }

    public static void fetchAllStops()
    {
        String result = sendUrlRequest(SANGSTERBASEURL+STOPS);
    }

    public static void getRouteForIdent(int ident)
    {
        String result = sendUrlRequest(SANGSTERBASEURL+STOPTIME+ident+"/"+DateFormat.format("yyyy-MM-dd", new Date()));
    }

    public static void getPathForRouteId(String routeId)
    {
        String result = sendUrlRequest(SANGSTERBASEURL+PATHS+DateFormat.format("yyyy-MM-dd", new Date())+"/"+routeId);
    }

    public static void loadPathForRoute(String shortName)
    {
        String result = sendUrlRequest(SANGSTERBASEURL+ROUTES+SHORTS+shortName+HEADSIGNS+DateFormat.format("yyyy-MM-dd", new Date())+DateFormat.format("HH:MM", new Date()));
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
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type","application/json");
        HttpResponse response;
        try {
            response = client.execute(post);
            Log.i("Praeda",response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                Log.i("Praeda",result);
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
}
