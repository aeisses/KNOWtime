package com.knowtime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ShareMeActivity extends Activity {

	ImageView connectToServerImage;
	ImageView sendingImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_me);
		connectToServerImage = (ImageView)findViewById(R.id.connecttoserverimage);
		connectToServerImage.setVisibility(View.INVISIBLE);
		sendingImage = (ImageView)findViewById(R.id.sendingimage);
		sendingImage.setVisibility(View.INVISIBLE);
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
	}
}
