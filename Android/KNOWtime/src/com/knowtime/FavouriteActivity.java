package com.knowtime;

import java.util.List;

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
		stopTable = (TableLayout)this.findViewById(R.id.stopsTable);
		routeTable = (TableLayout)this.findViewById(R.id.routesTable);
		loadFavouriteStops();
		loadFavouriteRoutes();
	}
	
	private void loadFavouriteStops()
	{
		List<Stop> stops = DatabaseHandler.getInstance().getAllFavouriteStops();
		for (int i=0; i<stops.size(); i++)
		{
			final Stop stop = stops.get(i);
			// Get favourite stops some how
			LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View stopRow = li.inflate(R.layout.favouritecellview, null);
			final String stopNumber = stop.getCode();
			final String stopName = stop.getName();
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
			stopTable.addView(stopRow,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			TextView stopNumberTextView = (TextView)stopRow.findViewById(R.id.favouriteId);
			stopNumberTextView.setText(stopNumber);
			TextView stopNameTextView = (TextView)stopRow.findViewById(R.id.favouriteDescription);
			stopNameTextView.setText(stopName);
			final ImageButton favouriteButton = (ImageButton)stopRow.findViewById(R.id.favouriteButton);
			favouriteButton.setSelected(true);
			favouriteButton.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					stop.setFavourite(!favouriteButton.isSelected());
					DatabaseHandler.getInstance().updateStop(stop);
					favouriteButton.setSelected(!favouriteButton.isSelected());
					return false;
				}
			});
		}
	}
	
	private void loadFavouriteRoutes()
	{
		List<Route> routes = DatabaseHandler.getInstance().getAllFavouriteRoutes();
		for (int i=0; i<routes.size(); i++)
		{
			final Route route = routes.get(i);
			// Get favourite routes some how
			LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View stopRow = li.inflate(R.layout.favouritecellview, null);
			final String routeNumber = route.getShortName();
			String routeName = route.getLongName();
			final String routeId = route.getId();
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
			routeTable.addView(stopRow,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			TextView routeNumberTextView = (TextView)stopRow.findViewById(R.id.favouriteId);
			routeNumberTextView.setText(routeNumber);
			TextView routeNameTextView = (TextView)stopRow.findViewById(R.id.favouriteDescription);
			routeNameTextView.setText(routeName);
			final ImageButton favouriteButton = (ImageButton)stopRow.findViewById(R.id.favouriteButton);
			favouriteButton.setSelected(true);
			favouriteButton.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					route.setFavourite(!favouriteButton.isSelected());
					DatabaseHandler.getInstance().updateRoute(route);
					favouriteButton.setSelected(!favouriteButton.isSelected());
					return false;
				}
			});
		}
	}
	
	public void touchBackButton(View view)
	{
		this.finish();
	}
}
