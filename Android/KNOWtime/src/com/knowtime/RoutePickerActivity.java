package com.knowtime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TableLayout;

public class RoutePickerActivity extends Activity {

	TableLayout routeSelectionTable;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_picker);
		routeSelectionTable = (TableLayout) findViewById(R.id.routePickerTable);
		Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
            	getRoutes();
            }
        });
		thread.start();
	}
	
	private void getRoutes()
	{
		final Object[] routes = WebApiService.getRoutesArray();
		Log.d("com.timeplay",""+routes);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				parseRoutes(routes);
			}
		});
	}
	
	private void parseRoutes(Object[] routes)
	{
		for (int i=0; i<routes.length; i+=6)
		{
			LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
			View routeRow = li.inflate(R.layout.routeselectorcell, null);
			routeSelectionTable.addView(routeRow,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			if (i<routes.length)
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton1);
				String routeNumber = (String)routes[i];
				createButton(stopButton,routeNumber);
				stopButton.setVisibility(View.VISIBLE);
			}
			else
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton1);
				stopButton.setVisibility(View.INVISIBLE);
			}
			if (i+1<routes.length)
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton2);
				String routeNumber = (String)routes[i+1];
				createButton(stopButton,routeNumber);
				stopButton.setVisibility(View.VISIBLE);
			}
			else
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton2);
				stopButton.setVisibility(View.INVISIBLE);
			}
			if (i+2<routes.length)
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton3);
				String routeNumber = (String)routes[i+2];
				createButton(stopButton,routeNumber);
				stopButton.setVisibility(View.VISIBLE);
			}
			else
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton3);
				stopButton.setVisibility(View.INVISIBLE);
			}
			if (i+3<routes.length)
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton4);
				String routeNumber = (String)routes[i+3];
				createButton(stopButton,routeNumber);
				stopButton.setVisibility(View.VISIBLE);
			}
			else
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton4);
				stopButton.setVisibility(View.INVISIBLE);
			}
			if (i+4<routes.length)
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton5);
				String routeNumber = (String)routes[i+4];
				createButton(stopButton,routeNumber);
				stopButton.setVisibility(View.VISIBLE);
			}
			else
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton5);
				stopButton.setVisibility(View.INVISIBLE);
			}
			if (i+5<routes.length)
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton6);
				String routeNumber = (String)routes[i+5];
				createButton(stopButton,routeNumber);
				stopButton.setVisibility(View.VISIBLE);
			}
			else
			{
				Button stopButton = (Button)routeRow.findViewById(R.id.selectorButton6);
				stopButton.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	private void createButton(Button stopButton, final String routeNumber)
	{
		stopButton.setText(routeNumber);
		stopButton.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					Intent resultIntent = new Intent();
					resultIntent.putExtra("routeNumber",routeNumber);
					setResult(Activity.RESULT_OK,resultIntent);
					finish();
				}
				return false;
			}
		});
	}
	
	public void touchBackButton(View view)
	{
		Intent resultIntent = new Intent();
		resultIntent.putExtra("routeNumber","-1");
		setResult(Activity.RESULT_OK,resultIntent);
		this.finish();
	}
}
