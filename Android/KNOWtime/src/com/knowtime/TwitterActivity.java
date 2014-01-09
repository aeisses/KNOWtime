package com.knowtime;

import android.app.Activity;
import android.app.LoaderManager;
import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.flurry.android.FlurryAgent;

import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.knowtime.TwitterLoader;

public class TwitterActivity extends Activity implements LoaderManager.LoaderCallbacks<ArrayList<HashMap<String, String>>> {
	
    Context mContext;
    private ConfigurationBuilder builder;
    private Twitter twitter;
    private TwitterFactory factory;
	private ListView listView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);
		mContext = getApplicationContext();
		
		listView1 = (ListView) findViewById(R.id.listView1);
		getLoaderManager().initLoader(0, null, this).forceLoad();
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//      builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
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



	@Override
	public Loader<ArrayList<HashMap<String, String>>> onCreateLoader(int arg0, Bundle arg1) {
//		return null;
		return new TwitterLoader(getApplicationContext());
	}



	@Override
	public void onLoadFinished(Loader<ArrayList<HashMap<String, String>>> arg0, ArrayList<HashMap<String, String>> result) {
		
		if (result.size() > 0) {
			SimpleAdapter adapter = new SimpleAdapter(this, result, R.layout.twitter, new String[] {"twitterIcon", "twitterName",
					"twitterScreenName", "twitterDate", "twitterMainText" }, new int[] {R.id.twitterIcon, R.id.twitterName,
					R.id.twitterScreenName, R.id.twitterDate, R.id.twitterMainText });

			listView1.setAdapter(adapter);
		} else {
			listView1.setEmptyView(findViewById(R.id.emptyTwitterText));
		}
	}



	@Override
	public void onLoaderReset(Loader<ArrayList<HashMap<String, String>>> arg0) {
		// TODO Auto-generated method stub
		
	}
}
