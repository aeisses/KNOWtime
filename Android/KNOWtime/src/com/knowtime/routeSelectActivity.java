package com.knowtime;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RouteSelectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routeselect);
	}
	
	public void touchRouteButton(View view)
	{
		Intent intent = new Intent(RouteSelectActivity.this,RoutePickerActivity.class);
		startActivityForResult(intent,1);
	}
	
	public void touchSubmitButton(View view)
	{
		TextView routeNumberTextView = (TextView)findViewById(R.id.routeNumber);
		if (!routeNumberTextView.equals(""))
		{
			Route route = DatabaseHandler.getInstance().getRoute((String)routeNumberTextView.getText());
			Intent intent = new Intent(RouteSelectActivity.this,RouteMapActivity.class);
			intent.putExtra("ROUTE_ID", route.getId());
			intent.putExtra("ROUTE_NUMBER", route.getLongName());
			startActivity(intent);
		}
		else
		{
			
		}
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == 1 && resultCode == RESULT_OK && data != null)
    	{
			String routeNumber = data.getStringExtra("routeNumber");
    		if (Integer.parseInt(routeNumber) == -1)
    		{

    		}
    		else
    		{
    			TextView routeNumberTextView = (TextView)findViewById(R.id.routeNumber);
    			routeNumberTextView.setText(routeNumber);
    		}
    	}
    }
}
