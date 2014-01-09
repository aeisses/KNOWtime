package com.knowtime;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.AsyncTaskLoader;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TwitterLoader extends AsyncTaskLoader<ArrayList<HashMap<String, String>>>
{

	SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd"); 
	SimpleDateFormat sdf3 = new SimpleDateFormat("HH"); 
	
	static String TWITTER_CONSUMER_KEY = "4nCDEefDi54ErZd3qtpZ7g";
    static String TWITTER_CONSUMER_SECRET = "q4Fq2xLFFEh7TPfTTVPGGRivx99BD0Hjzou7HieECI";
    static String TWITTER_OAUTH_TOKEN = "1969214929-dWw75ztFDOXTzlTvDxtkdpUZC2PuRSiEdYFkcZI";
    static String TWITTER_OAUTH_TOKEN_SECRET = "3HRCrTu0tN4M4IqpbRJMxk5YLNqDaA77ywibDTPH8wHvK";
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    // Login button
    Button btnLoginTwitter;
    // Update status button
    Button btnUpdateStatus;
    Button btnGetTimeLine;

    // Logout button
    Button btnLogoutTwitter;
    // EditText for update
    EditText txtUpdate;
    // lbl update
    TextView lblUpdate;
    TextView lblUserName;

    // Progress dialog
    ProgressDialog pDialog;

    // Twitter
//    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    // Internet Connection detector
    private ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    
    private ConfigurationBuilder builder;
    static private Twitter twitter;
    private TwitterFactory factory;
	private ArrayList<HashMap<String, String>> twitterList;
    
    
	public TwitterLoader(Context context) {
		super(context);
		twitterList = new ArrayList<HashMap<String, String>>();

//			builder = new ConfigurationBuilder();
//			builder.setUseSSL(true);
//			builder.setApplicationOnlyAuthEnabled(true);
//			builder.setOAuthConsumerKey(TWITTER_OAUTH_TOKEN).setOAuthConsumerSecret(TWITTER_OAUTH_TOKEN_SECRET);
			
		if (twitter == null) {
			twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
			twitter.setOAuthAccessToken(new AccessToken(TWITTER_OAUTH_TOKEN, TWITTER_OAUTH_TOKEN_SECRET));
		}
	}
	
	@Override
	public ArrayList<HashMap<String, String>> loadInBackground() {
		try {
//			OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();
//			
//			ConfigurationBuilder cb = new ConfigurationBuilder();
//			cb.setUseSSL(true);
//			cb.setApplicationOnlyAuthEnabled(true);
//			cb.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
//			cb.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//			cb.setOAuth2TokenType(token.getTokenType());
//			cb.setOAuth2AccessToken(token.getAccessToken());
//			
//			twitter = new TwitterFactory(cb.build()).getInstance();
			
			
			
			ResponseList<twitter4j.Status> statuses = twitter.getUserTimeline("@hfxtransit");
			for(twitter4j.Status status: statuses){
				twitterList.add(putData(status));
			}
//			Log.d("com.knowtime","Statuses "+statuses);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		return twitterList;
	}

	private HashMap<String, String> putData(Status status) {
		HashMap<String, String> map = new HashMap<String, String>();
//		Bitmap icon = getBitmapFromURL(status.getUser().getProfileImageURL());
//		map.put("twitterIcon", icon.toString());
		map.put("twitterIcon",Integer.toString(R.drawable.metro_halifax_icon));
		map.put("twitterName", status.getUser().getName().toString());
		map.put("twitterScreenName", "@"+status.getUser().getScreenName().toString());
		map.put("twitterDate", convertToTwitterDateFormate(status.getCreatedAt()));
		map.put("twitterMainText", status.getText().toString());
		
		return map;
	}
	
	private String convertToTwitterDateFormate(Date date){
		String returnDate = null;

		Calendar calendarNow = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		if (calendarNow.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
			returnDate = sdf3.format(date)+" h";
		} else {
			returnDate = sdf2.format(date);
		}
		return returnDate;
	}
	
	private static Bitmap getBitmapFromURL(String link) {
	    /*--- this method downloads an Image from the given URL, 
	     *  then decodes and returns a Bitmap object
	     ---*/
	    try {
	        URL url = new URL(link);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);

	        return myBitmap;

	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("getBmpFromUrl error: ", e.getMessage().toString());
	        return null;
	    }
	}
    

	
//	private boolean isNeededTwitterAuth() {
//	    SharedPreferences settings = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
//	    String twitterAccesToken = settings.getString("bearerAccessToken", "");
//	    String twitterTokenType = settings.getString("bearerTokenType", "");
//	    return ((twitterAccesToken.length() == 0) && (twitterTokenType.length() == 0));
//	}
	
//    protected OAuth2Token doInBackground(Void... params) {
//        OAuth2Token bearerToken = null;
//
//        try {
//            bearerToken = twitter.getOAuth2Token();
//        } catch (TwitterException e) {
//            e.printStackTrace();
//        }
//        return bearerToken;
//    }
	

	
	/**
	 * Function to login twitter
	 * */
//	private void loginToTwitter() {
//	    // Check if already logged in
//	    if (!isTwitterLoggedInAlready()) {
//	        ConfigurationBuilder builder = new ConfigurationBuilder();
//	        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
//	        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//	        Configuration configuration = builder.build();
//	
//	        TwitterFactory factory = new TwitterFactory(configuration);
//	        twitter = factory.getInstance();
//	
//	        try {
//	            requestToken = twitter
//	                    .getOAuthRequestToken(TWITTER_CALLBACK_URL);
//	            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
//	                    .parse(requestToken.getAuthenticationURL())));
//	        } catch (TwitterException e) {
//	            e.printStackTrace();
//	        }
//	    } else {
//	        // user already logged into twitter
//	        Toast.makeText(getApplicationContext(),
//	                "Already Logged into twitter", Toast.LENGTH_LONG).show();
//	    }
//	}
	
	/**
	 * Function to update status
	 * */
//	class updateTwitterStatus extends AsyncTask<String, String, String> {
//	
//	    /**
//	     * Before starting background thread Show Progress Dialog
//	     * */
//	    @Override
//	    protected void onPreExecute() {
//	        super.onPreExecute();
//	        pDialog = new ProgressDialog(mContext);
//	        pDialog.setMessage("Updating to twitter...");
//	        pDialog.setIndeterminate(false);
//	        pDialog.setCancelable(false);
//	        pDialog.show();
//	    }
//	
//	    /**
//	     * getting Places JSON
//	     * */
//	    protected String doInBackground(String... args) {
//	        Log.d("Tweet Text", "> " + args[0]);
//	        String status = args[0];
//	        try {
//	            ConfigurationBuilder builder = new ConfigurationBuilder();
//	            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
//	            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//	
//	            // Access Token
//	            String access_token = mSharedPreferences.getString(
//	                    PREF_KEY_OAUTH_TOKEN, "");
//	            // Access Token Secret
//	            String access_token_secret = mSharedPreferences.getString(
//	                    PREF_KEY_OAUTH_SECRET, "");
//	
//	            AccessToken accessToken = new AccessToken(access_token,
//	                    access_token_secret);
//	            Twitter twitter = new TwitterFactory(builder.build())
//	                    .getInstance(accessToken);
//	
//	            // Update status
//	            twitter4j.Status response = twitter.updateStatus(status);
//	
//	            Log.d("Status", "> " + response.getText());
//	        } catch (TwitterException e) {
//	            // Error in updating status
//	            Log.d("Twitter Update Error", e.getMessage());
//	        }
//	        return null;
//	    }
//	
//	    /**
//	     * After completing background task Dismiss the progress dialog and show
//	     * the data in UI Always use runOnUiThread(new Runnable()) to update UI
//	     * from background thread, otherwise you will get error
//	     * **/
//	    protected void onPostExecute(String file_url) {
//	        // dismiss the dialog after getting all products
//	        pDialog.dismiss();
//	        // updating UI from Background Thread
//	        runOnUiThread(new Runnable() {
//	            @Override
//	            public void run() {
//	                Toast.makeText(getApplicationContext(),
//	                        "Status tweeted successfully", Toast.LENGTH_SHORT)
//	                        .show();
//	                // Clearing EditText field
//	                txtUpdate.setText("");
//	            }
//	        });
//	    }
//	}
	
	/**
	 * Function to logout from twitter It will just clear the application shared
	 * preferences
	 * */
//	private void logoutFromTwitter() {
//	    // Clear the shared preferences
//	    Editor e = mSharedPreferences.edit();
//	    e.remove(PREF_KEY_OAUTH_TOKEN);
//	    e.remove(PREF_KEY_OAUTH_SECRET);
//	    e.remove(PREF_KEY_TWITTER_LOGIN);
//	    e.commit();
//	
//	    // After this take the appropriate action
//	    // I am showing the hiding/showing buttons again
//	    // You might not needed this code
//	    btnGetTimeLine.setVisibility(View.GONE);
//	    btnLogoutTwitter.setVisibility(View.GONE);
//	    btnUpdateStatus.setVisibility(View.GONE);
//	    txtUpdate.setVisibility(View.GONE);
//	    lblUpdate.setVisibility(View.GONE);
//	    lblUserName.setText("");
//	    lblUserName.setVisibility(View.GONE);
//	
//	    btnLoginTwitter.setVisibility(View.VISIBLE);
//	}
//	
//	/**
//	 * Check user already logged in your application using twitter Login flag is
//	 * fetched from Shared Preferences
//	 * */
//	private boolean isTwitterLoggedInAlready() {
//	    // return twitter login status from Shared Preferences
//	    return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
//	}
//	
//	protected void onResume() {
//	    super.onResume();
//	}
	
	/**
	 * Function to get timeline
	 * */
//	class getTimeLine extends AsyncTask<Void, Void, String> {
//	
//	    /**
//	     * Before starting background thread Show Progress Dialog
//	     * */
//	    @Override
//	    protected void onPreExecute() {
//	        super.onPreExecute();
//	        pDialog = new ProgressDialog(mContext);
//	        pDialog.setMessage("Getting Timeline from Twitter...");
//	        pDialog.setIndeterminate(false);
//	        pDialog.setCancelable(false);
//	        pDialog.show();
//	    }
//	
//	    /**
//	     * getting Places JSON
//	     * */
//	    protected String doInBackground(Void... args) {
//	        try {
//	            ConfigurationBuilder builder = new ConfigurationBuilder();
//	            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
//	            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//	
//	            // Access Token
//	            String access_token = mSharedPreferences.getString(
//	                    PREF_KEY_OAUTH_TOKEN, "");
//	            // Access Token Secret
//	            String access_token_secret = mSharedPreferences.getString(
//	                    PREF_KEY_OAUTH_SECRET, "");
//	
//	            AccessToken accessToken = new AccessToken(access_token,
//	                    access_token_secret);
//	            Twitter twitter = new TwitterFactory(builder.build())
//	                    .getInstance(accessToken);
//	
//	
//	            //Twitter twitter = new TwitterFactory().getInstance();
//	            User user = twitter.verifyCredentials();
//	            List<twitter4j.Status> statuses = twitter.getHomeTimeline();
//	            System.out.println("Showing @" + user.getScreenName()
//	                    + "'s home timeline.");
//	            for (twitter4j.Status status : statuses) {
//	                Log.d("Twitter","@" + status.getUser().getScreenName()
//	                        + " - " + status.getText());
//	            }
//	        } catch (TwitterException e) {
//	            // Error in updating status
//	            Log.d("Twitter Update Error", e.getMessage());
//	        }
//	        return null;
//	    }
//	
//	    protected void onPostExecute(String file_url) {
//	        // dismiss the dialog after getting all products
//	        pDialog.dismiss();
//	        // updating UI from Background Thread
//	        runOnUiThread(new Runnable() {
//	            @Override
//	            public void run() {
//	                Toast.makeText(getApplicationContext(),
//	                        "Retrieving TimeLine Done..", Toast.LENGTH_SHORT)
//	                        .show();
//	            }
//	        });
//	    }
//	}
}