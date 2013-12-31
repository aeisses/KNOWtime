package com.knowtime;

public class Route {
	boolean isFavourite;
	String id;
	String longName;
	String shortName;
	
	public Route()
	{
		
	}
	
	public Route(String longName, String shortName)
	{
		this.id = "";
		this.longName = longName;
		this.shortName = shortName;
		this.isFavourite = false;
	}
	
	public Route(String longName, String shortName, String id, boolean isFavourite)
	{
		this.id = "";
		this.longName = longName;
		this.shortName = shortName;
		this.isFavourite = isFavourite;
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public void setId(String id)
	{
		this.id = id;
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
