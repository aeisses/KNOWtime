package com.knowtime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

public class StopsActivity extends Activity {
	String stopNumber = "";
	String stopName = "";
	ProgressBar progressBar;
	TableLayout stopTable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stops);
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			stopNumber = extras.getString("STOP_NUMBER");
			stopName = extras.getString("STOP_NAME");
		}
		progressBar = (ProgressBar) findViewById(R.id.stopsProgressBar);
		stopTable = (TableLayout) findViewById(R.id.stopTable);
		TextView stopsHeaderTextView = (TextView)stopTable.findViewById(R.id.stopsHeaderText);
		stopsHeaderTextView.setText(stopName);
		// Determine if this is a favorite stop
		
		getStops();
	}
	
	private Date getStopDate(String departTime, SimpleDateFormat formatter, SimpleDateFormat currentTimeFormatter)
	{
		Date returnDate = null;
		String[] components = departTime.split(":");
		if (components.length >=2 && Integer.parseInt(components[0]) >= 24)
		{
			try {
				Date tempDate = formatter.parse(currentTimeFormatter.format(new Date())+" 00:"+components[1]);
				returnDate = new Date(tempDate.getTime()+86400000);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try {
				returnDate = formatter.parse(currentTimeFormatter.format(new Date())+" "+departTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnDate;
	}
	
	private void getStops()
	{
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
            	final JSONArray jsonArray = WebApiService.getRouteForIdent(Integer.parseInt(stopNumber));
        		runOnUiThread(new Runnable() {
        			@Override
        			public void run() {
        				for (int i=0; i<jsonArray.length(); i++)
        				{
            				try 
            				{
                        		final JSONObject routeItem = jsonArray.getJSONObject(i);
                    			LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    			View stopRow = li.inflate(R.layout.stopscellview, null);
                    			final String routeId = routeItem.getString("routeId");
                    			final String routeNumber = routeItem.getString("shortName");
                    			stopRow.setOnTouchListener(new OnTouchListener()
                    			{
                    				@Override
                    				public boolean onTouch(View v, MotionEvent event) {
                    					if (event.getAction() == MotionEvent.ACTION_DOWN)
                    					{
                    		    			Intent intent = new Intent(StopsActivity.this,RouteMapActivity.class);
                    		    			intent.putExtra("ROUTE_NUMBER", routeNumber);
                    		    			intent.putExtra("ROUTE_ID", routeId);
                    		    			startActivity(intent);
                    					}
                    					return false;
                    				}
                    			});
                    			stopTable.addView(stopRow,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    			TextView routeNumberTextView = (TextView)stopRow.findViewById(R.id.routenumber);
                    			routeNumberTextView.setText(routeNumber);
                    			TextView routeNameTextView = (TextView)stopRow.findViewById(R.id.routeName);
                    			routeNameTextView.setText(routeItem.getString("longName"));
                    			
                    			SimpleDateFormat displayFormatter = new SimpleDateFormat("h:mm", Locale.US);
                    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
                    			SimpleDateFormat currentTimeFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    			
                    			JSONArray stopTimes = routeItem.getJSONArray("stopTimes");
                    			int counter = -1;
                    			long minDiff = 0;
                    			String departTime = "unknown";
                				Date currentDate = new Date();
                    			for (int j=0; j<stopTimes.length(); j++)
                    			{
                    				JSONObject stopItem = stopTimes.getJSONObject(j);
                    				Date stopDate = getStopDate(stopItem.getString("departure"),formatter,currentTimeFormatter);
                    				long diff = stopDate.getTime() - currentDate.getTime();
                    				if (diff > 0 && (minDiff <= 0 || minDiff > diff))
                    				{
                    					minDiff = diff;
                    					departTime = displayFormatter.format(stopDate);
                    					counter = j;
                    				}
                    			}
                    			if (minDiff != 0)
                    			{
                    				TextView time1 = (TextView)stopRow.findViewById(R.id.time1);
                    				TextView eta1 = (TextView)stopRow.findViewById(R.id.eta1);
                    				time1.setText(departTime);
                    				eta1.setText(minDiff/60000+" min");
                    			}
                    			if (counter+1 < stopTimes.length() || counter != -1)
                    			{
                    				Date nextStopDate = getStopDate(stopTimes.getJSONObject(counter+1).getString("departure"),formatter,currentTimeFormatter);
                    				TextView time2 = (TextView)stopRow.findViewById(R.id.time2);
                    				TextView eta2 = (TextView)stopRow.findViewById(R.id.eta2);
                    				time2.setText(displayFormatter.format(nextStopDate));
                    				eta2.setText((nextStopDate.getTime()-currentDate.getTime())/60000+" min");
                    				if (counter+2 < stopTimes.length())
                    				{
                        				Date nextNextStopDate = getStopDate(stopTimes.getJSONObject(counter+2).getString("departure"),formatter,currentTimeFormatter);
                        				TextView time3 = (TextView)stopRow.findViewById(R.id.time3);
                        				TextView eta3 = (TextView)stopRow.findViewById(R.id.eta3);
                        				time3.setText(displayFormatter.format(nextNextStopDate));
                        				eta3.setText((nextNextStopDate.getTime()-currentDate.getTime())/60000+" min");
                    				}
                    			}
            				}
            				catch (JSONException e)
            				{
            					e.printStackTrace();
            				}
        				}
        				progressBar.setVisibility(View.INVISIBLE);
            		}
        		});
            }
        });
        thread.start();
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
	}
	
	public void touchFavouriteButton(View view)
	{
		view.setSelected(!view.isSelected());
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		// Need to save the favourites information here, some how, some way
		
	}
}
