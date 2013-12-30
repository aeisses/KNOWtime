package com.knowtime;

public class Stop {

	boolean isFavourite;
	String code;
	String name;
	double lat;
	double lng;
	
	public Stop()
	{
		
	}
	
	public Stop(String code, String name, double lat, double lng)
	{
		this.code = code;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}
	
	public Stop(String code, String name, double lat, double lng, boolean isFavourite)
	{
		this.code = code;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.isFavourite = isFavourite;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getCode()
	{
		return this.code;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public double getLat()
	{
		return this.lat;
	}
	
	public void setLat(double lat)
	{
		this.lat = lat;
	}
	
	public double getLng()
	{
		return this.lng;
	}
	
	public void setLng(double lng)
	{
		this.lng = lng;
	}
	
	public boolean getFavourite()
	{
		return this.isFavourite;
	}
	
	public void setFavourite(Boolean isFavourite)
	{
		this.isFavourite = isFavourite;
	}
}

