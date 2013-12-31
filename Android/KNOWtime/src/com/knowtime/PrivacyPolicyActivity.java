package com.knowtime;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class PrivacyPolicyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacypolicy);
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
}
