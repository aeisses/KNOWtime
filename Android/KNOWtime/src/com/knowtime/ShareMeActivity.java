package com.knowtime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ShareMeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_me);
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
	}
}
