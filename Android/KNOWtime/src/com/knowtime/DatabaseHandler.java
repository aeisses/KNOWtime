package com.knowtime;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "knowTimeData";
	
	private static final String TABLE_STOP = "stop";
	private static final String TABLE_ROUTE = "route";
	
	private static final String KEY_SHORTNAME = "shortName";
	private static final String KEY_LONGNAME = "longName";
	private static final String KEY_NAME = "name";
	private static final String KEY_CODE = "code";
	private static final String KEY_LATITUDE = "lat";
	private static final String KEY_LONGITUDE = "lng";
	private static final String KEY_FAVOURITE = "favourite";
	
	private static DatabaseHandler instance = null;
	
	public static DatabaseHandler getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new DatabaseHandler(context);
		}
		return instance;
	}
	
	private DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_ROUTE_TABLE = "CREATE TABLE " + TABLE_ROUTE + "(" + KEY_SHORTNAME + " TEXT PRIMARY KEY," + KEY_LONGNAME + " TEXT," + KEY_FAVOURITE + " BOOLEAN" + ")";
		db.execSQL(CREATE_ROUTE_TABLE);
		String CREATE_STOP_TABLE = "CREATE TABLE " + TABLE_STOP + "(" + KEY_CODE + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_LATITUDE + " DOUBLE," + KEY_LONGITUDE + " DOUBLE" + KEY_FAVOURITE + " BOOLEAN" + ")";
		db.execSQL(CREATE_STOP_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
		onCreate(db);
	}
	
	public void addStop(Stop stop)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_CODE, stop.getCode());
		values.put(KEY_NAME, stop.getName());
		values.put(KEY_LATITUDE, stop.getLat());
		values.put(KEY_LONGITUDE, stop.getLng());
		values.put(KEY_FAVOURITE, stop.getFavourite());
		
		db.insert(TABLE_STOP, null, values);
		db.close();
	}
	
	public void addRoute(Route route)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_SHORTNAME, route.getShortName());
		values.put(KEY_LONGNAME, route.getLongName());
		values.put(KEY_FAVOURITE, route.getFavourite());
		
		db.insert(TABLE_ROUTE, null, values);
		db.close();
	}
	
	public Stop getStop(int code)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_STOP, new String[] { KEY_CODE, KEY_NAME, KEY_LATITUDE, KEY_LONGITUDE, KEY_FAVOURITE }, KEY_CODE + "=?", new String[] { String.valueOf(code) }, null, null, null, null);
		if (cursor != null)
		{
			cursor.moveToFirst();
		}
		Stop stop = new Stop(cursor.getString(0),cursor.getString(1),Double.parseDouble(cursor.getString(2)),Double.parseDouble(cursor.getString(3)),Boolean.valueOf(cursor.getString(4)));
		return stop;
	}
	
	public Route getRoute(String shortName)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_ROUTE, new String[] { KEY_SHORTNAME, KEY_LONGNAME, KEY_FAVOURITE }, KEY_SHORTNAME + "=?", new String[] { shortName }, null, null, null, null);
		if (cursor != null)
		{
			cursor.moveToFirst();
		}
		Route route = new Route(cursor.getString(0),cursor.getString(1),Boolean.valueOf(cursor.getString(2)));
		return route;
	}
	
	private List<Stop> getStops(String selectQuery)
	{
		List<Stop> stopList = new ArrayList<Stop>();
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) 
		{
			do 
			{
				Stop stop = new Stop();
				stop.setCode(cursor.getString(0));
				stop.setName(cursor.getString(1));
				stop.setLat(Double.parseDouble(cursor.getString(2)));
				stop.setLng(Double.parseDouble(cursor.getString(3)));
				stop.setFavourite(Boolean.parseBoolean(cursor.getString(4)));
				stopList.add(stop);
			}
			while (cursor.moveToNext());
		}
		
		return stopList;
	}
	
	public List<Stop> getAllStops()
	{
		String selectQuery = "SELECT * FROM " + TABLE_STOP;
		return getStops(selectQuery);
	}
	
	public List<Stop> getAllFavouriteStops()
	{
		String selectQuery = "SELECT * FROM " + TABLE_STOP + " WHERE " + KEY_FAVOURITE + " = 1";
		return getStops(selectQuery);
	}
	
	public List<Route> getRoutes(String selectQuery)
	{
		List<Route> routeList = new ArrayList<Route>();		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst())
		{
			do
			{
				Route route = new Route();
				route.setShortName(cursor.getString(0));
				route.setLongName(cursor.getString(1));
				route.setFavourite(Boolean.parseBoolean(cursor.getString(2)));
			}
			while (cursor.moveToNext());
		}
		
		return routeList;
	}
	
	public List<Route> getAllRoutes()
	{
		String selectQuery = "SELECT * FROM " + TABLE_ROUTE;
		return getRoutes(selectQuery);
	}
	
	public List<Route> getAllFavouriteRoutes()
	{
		String selectQuery = "SELECT * FROM " + TABLE_ROUTE + " WHERE " + KEY_FAVOURITE + " = 1";
		return getRoutes(selectQuery);
	}
	
//	public int getStopCount()
//	{
//		
//	}
//	
//	public int getRouteCount()
//	{
//		
//	}
	
	public int updateStop(Stop stop)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_CODE, stop.getCode());
		values.put(KEY_NAME, stop.getName());
		values.put(KEY_LATITUDE, stop.getLat());
		values.put(KEY_LONGITUDE, stop.getLng());
		values.put(KEY_FAVOURITE, stop.getFavourite());
		
		return db.update(TABLE_STOP, values, KEY_CODE + " = ?", new String[] { String.valueOf(stop.getCode()) });
	}
	
	public int updateRoute(Route route)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_SHORTNAME, route.getShortName());
		values.put(KEY_LONGNAME, route.getLongName());
		values.put(KEY_FAVOURITE, route.getFavourite());
		
		return db.update(TABLE_ROUTE, values, KEY_SHORTNAME + " = ?", new String[] { route.getShortName() });
	}
	
	public void deleteStop(Stop stop)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_STOP, KEY_CODE + " = ?", new String[] { String.valueOf(stop.getCode()) });
		db.close();
	}
	
	public void deleteRoute(Route route)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ROUTE, KEY_SHORTNAME + " = ?", new String[] { route.getShortName() });
		db.close();
	}
}
