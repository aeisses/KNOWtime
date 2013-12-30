package com.knowtime;

public class Route {
	boolean isFavourite;
	String longName;
	String shortName;
	
	public Route()
	{
		
	}
	
	public Route(String longName, String shortName)
	{
		this.longName = longName;
		this.shortName = shortName;
	}
	
	public Route(String longName, String shortName, boolean isFavourite)
	{
		this.longName = longName;
		this.shortName = shortName;
		this.isFavourite = isFavourite;
	}
	
	public String getLongName()
	{
		return this.longName;
	}
	
	public void setLongName(String longName)
	{
		this.longName = longName;
	}
	
	public String getShortName()
	{
		return this.shortName;
	}
	
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
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
