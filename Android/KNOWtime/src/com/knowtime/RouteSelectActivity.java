package com.knowtime;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RouteSelectActivity extends Activity {
	ExecutorService thread = Executors.newSingleThreadExecutor();
	Future<ArrayList<Integer>> futureResultRoutes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routeselect);
		
		 futureResultRoutes= thread.submit(new Callable<ArrayList<Integer>>() {
			@Override
			public ArrayList<Integer> call() throws Exception {
				ArrayList<Integer> ListRoutes = WebApiService.fetchAllLiveRoutes();
				return ListRoutes;
			}
		});		
		
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//        		activeRoute = WebApiService.fetchAllLiveRoutes();
//			}
//		}).start();
	}
	
	public void touchRouteButton(View view)
	{
		Intent intent = new Intent(RouteSelectActivity.this, RoutePickerActivity.class);
		try {
			intent.putIntegerArrayListExtra("liveRoute", futureResultRoutes.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		startActivityForResult(intent,1);
	}
	
	public void touchSubmitButton(View view)
	{
		TextView routeNumberTextView = (TextView)findViewById(R.id.routeNumber);
		if (!routeNumberTextView.equals(""))
		{
			Route route = DatabaseHandler.getInstance().getRoute((String) routeNumberTextView.getText());
			if (route != null) {
				Intent intent = new Intent(RouteSelectActivity.this, RouteMapActivity.class);
				intent.putExtra("ROUTE_ID", route.getId());
				intent.putExtra("ROUTE_NUMBER", route.getShortName());
				startActivity(intent);
			}
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
