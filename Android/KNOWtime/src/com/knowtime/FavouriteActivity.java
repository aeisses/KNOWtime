package com.knowtime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

public class FavouriteActivity extends Activity {

	TableLayout stopTable;
	TableLayout routeTable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourite);
		
		loadFavouriteStops();
		loadFavouriteRoutes();
	}
	
	private void loadFavouriteStops()
	{
		// Get favourite stops some how
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View stopRow = li.inflate(R.layout.favouritecellview, null);
		final String stopNumber = "";
		final String stopName = "";
		stopRow.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
	    			Intent intent = new Intent(FavouriteActivity.this,StopsActivity.class);
	    			intent.putExtra("STOP_NUMBER", stopNumber);
	    			intent.putExtra("STOP_NAME", stopName);
	    			startActivity(intent);
				}
				return false;
			}
		});
		stopTable.addView(stopRow,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		TextView stopNumberTextView = (TextView)stopRow.findViewById(R.id.favouriteId);
		stopNumberTextView.setText(stopNumber);
		TextView stopNameTextView = (TextView)stopRow.findViewById(R.id.routeName);
		stopNameTextView.setText(stopName);
		final ImageButton favouriteButton = (ImageButton)stopRow.findViewById(R.id.favouriteButton);
		favouriteButton.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				favouriteButton.setSelected(!favouriteButton.isSelected());
				return false;
			}
		});
	}
	
	private void loadFavouriteRoutes()
	{
		// Get favourite routes some how
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View stopRow = li.inflate(R.layout.favouritecellview, null);
		final String routeNumber = "";
		String routeName = "";
		final String routeId = "";
		stopRow.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
	    			Intent intent = new Intent(FavouriteActivity.this,RouteMapActivity.class);
	    			intent.putExtra("ROUTE_ID", routeId);
	    			intent.putExtra("ROUTE_NUMBER", routeNumber);
	    			startActivity(intent);
				}
				return false;
			}
		});
		stopTable.addView(stopRow,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		TextView routeNumberTextView = (TextView)stopRow.findViewById(R.id.favouriteId);
		routeNumberTextView.setText(routeNumber);
		TextView routeNameTextView = (TextView)stopRow.findViewById(R.id.routeName);
		routeNameTextView.setText(routeName);
		final ImageButton favouriteButton = (ImageButton)stopRow.findViewById(R.id.favouriteButton);
		favouriteButton.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				favouriteButton.setSelected(!favouriteButton.isSelected());
				return false;
			}
		});
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		// Need to save the favourites information here, some how, some way
		
	}
}
