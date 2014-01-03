package com.knowtime;

import java.util.List;

import com.flurry.android.FlurryAgent;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TwitterActivity extends Activity
{
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
    
    Context mContext;
    private ConfigurationBuilder builder;
    private Twitter twitter;
    private TwitterFactory factory;
    
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "54VCHBRBYDDP5VT63WFB");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);
//		mContext = getApplicationContext();
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		try
		{
//		cd = new ConnectionDetector(getApplicationContext());
		builder = new ConfigurationBuilder();
	    builder.setUseSSL(true);
	    builder.setApplicationOnlyAuthEnabled(true);
	    builder.setOAuthConsumerKey(TWITTER_OAUTH_TOKEN).setOAuthConsumerSecret(TWITTER_OAUTH_TOKEN_SECRET);
        OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();
        
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setUseSSL(true);
        cb.setApplicationOnlyAuthEnabled(true);
        cb.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
        cb.setOAuth2TokenType(token.getTokenType());
        cb.setOAuth2AccessToken(token.getAccessToken());
        
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        
        List<twitter4j.Status> statuses = twitter.getUserTimeline("@hfxtransit");
        Log.d("com.knowtime","Statuses "+statuses);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
//        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
//	    builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

//	    Configuration configuration = builder.build();
//	    factory = new TwitterFactory(configuration);
//	    ((MyApp) (MyApp.getApp())).setTFactory(factory);

//	    if (isNeededTwitterAuth()) {
//	        twitter = factory.getInstance();
	        
	        //Get the token async and save it
//	        OAuth2Token bearerToken = new OAuth2Token(bearerTokenType, bearerAccesstoken);
//	        twitter.setOAuth2Token(bearerToken);
	        
//	        SharedPreferences appSettings = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
//            SharedPreferences.Editor prefEditor = appSettings.edit();
//            prefEditor.putString("bearerAccessToken", bearerToken.getAccessToken());
//            prefEditor.putString("bearerTokenType", bearerToken.getTokenType());
//            prefEditor.commit();
//	    }
	    
		// Check if Internet present
//		if (!cd.isConnectingToInternet()) {
//			// Internet Connection is not present
//			alert.showAlertDialog(mContext,
//					"Internet Connection Error",
//					"Please connect to working Internet connection", false);
//			// stop executing code by return
//			return;
//		}
//
//		// Check if twitter keys are set
//		if (TWITTER_CONSUMER_KEY.trim().length() == 0
//				|| TWITTER_CONSUMER_SECRET.trim().length() == 0) {
//			// Internet Connection is not present
//			alert.showAlertDialog(mContext, "Twitter oAuth tokens",
//					"Please set your twitter oauth tokens first!", false);
//			// stop executing code by return
//			return;
//		}
//
//		// All UI elements
//		btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
//		btnUpdateStatus = (Button) findViewById(R.id.btnUpdateStatus);
//
//		btnGetTimeLine = (Button) findViewById(R.id.btnGetTimeLine);
//
//		btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);
//		txtUpdate = (EditText) findViewById(R.id.txtUpdateStatus);
//		lblUpdate = (TextView) findViewById(R.id.lblUpdate);
//		lblUserName = (TextView) findViewById(R.id.lblUserName);
//
//		// Shared Preferences
//		mSharedPreferences = getApplicationContext().getSharedPreferences(
//				"MyPref", 0);
//
//		/**
//		 * Twitter login button click event will call loginToTwitter() function
//		 **/
//		btnLoginTwitter.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// Call login twitter function
//				loginToTwitter();
//			}
//		});
//
//	    /**
//	     * Button click event to Update Status, will call updateTwitterStatus()
//	     * function
//	     * */
//	    btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
//	
//	        @Override
//	        public void onClick(View v) {
//	            // Call update status function
//	            // Get the status from EditText
//	            String status = txtUpdate.getText().toString();
//	
//	            // Check for blank text
//	            if (status.trim().length() > 0) {
//	                // update status
//	                new updateTwitterStatus().execute(status);
//	            } else {
//	                // EditText is empty
//	                Toast.makeText(getApplicationContext(),
//	                        "Please enter status message", Toast.LENGTH_SHORT)
//	                        .show();
//	            }
//	        }
//	    });
//	
//	    /**
//	     * Button click event for logout from twitter
//	     * */
//	    btnLogoutTwitter.setOnClickListener(new View.OnClickListener() {
//	
//	        @Override
//	        public void onClick(View arg0) {
//	            // Call logout twitter function
//	            logoutFromTwitter();
//	        }
//	    });
//	
//	    /**
//	     * Button click event for getting timeline from twitter *
//	     **/
//	    btnGetTimeLine.setOnClickListener(new View.OnClickListener() {
//	
//	        @Override
//	        public void onClick(View v) {
//	            // call getTimeLine function
//	            new getTimeLine().execute();
//	        }
//	    });
//	
//	    /**
//	     * This if conditions is tested once is redirected from twitter page.
//	     * Parse the uri to get oAuth Verifier
//	     * */
//	    if (!isTwitterLoggedInAlready()) {
//	        Uri uri = getIntent().getData();
//	        if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
//	            // oAuth verifier
//	            String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
//	
//	            try {
//	                // Get the access token
//	                AccessToken accessToken = twitter.getOAuthAccessToken(
//	                        requestToken, verifier);
//	
//	                // Shared Preferences
//	                Editor e = mSharedPreferences.edit();
//	
//	                // After getting access token, access token secret
//	                // store them in application preferences
//	                e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
//	                e.putString(PREF_KEY_OAUTH_SECRET,
//	                        accessToken.getTokenSecret());
//	                // Store login status - true
//	                e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
//	                e.commit(); // save changes
//	
//	                Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
//	
//	                // Hide login button
//	                btnLoginTwitter.setVisibility(View.GONE);
//	
//	                // Show Update Twitter
//	                lblUpdate.setVisibility(View.VISIBLE);
//	                txtUpdate.setVisibility(View.VISIBLE);
//	                btnUpdateStatus.setVisibility(View.VISIBLE);
//	
//	                btnGetTimeLine.setVisibility(View.VISIBLE);
//	
//	                btnLogoutTwitter.setVisibility(View.VISIBLE);
//	
//	                // Getting user details from twitter
//	                // For now i am getting his name only
//	                long userID = accessToken.getUserId();
//	                User user = twitter.showUser(userID);
//	                String username = user.getName();
//	
//	                // Displaying in xml ui
//	                lblUserName.setText(Html.fromHtml("<b>Welcome " + username
//	                        + "</b>"));
//	            } catch (Exception e) {
//	                // Check log for login errors
//	                Log.e("Twitter Login Error", "> " + e.getMessage());
//	            }
//	        }
//	    }
	}
	
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