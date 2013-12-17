package com.knowtime;

import android.app.Activity;
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
		
	}
}
