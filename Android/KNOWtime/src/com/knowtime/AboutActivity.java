package com.knowtime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
	}
	
	public void touchTwitterButton(View view)
	{
		
	}
	
	public void touchFacebookButton(View view)
	{
		
	}
	
	public void touchFeedbackButton(View view)
	{
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		email.setType("text/html");
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { "feedback@knowtime.ca" } );
		startActivity(Intent.createChooser(email, "Choose an Email client:"));
	}
}
