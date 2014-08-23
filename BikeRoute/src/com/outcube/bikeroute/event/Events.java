package com.outcube.bikeroute.event;

import android.graphics.Bitmap;

public class Events {
	
	private int event_id;
	private int date;
	private String month;
	private String name;
	private String location;
	private String time;
	private String description;
	private String photo;
	private String eventDate;
	private boolean isJoin;
	
	public Events() {
	}
	
	public Events(int event_id,int date,String month,String name,String location,String time,String description,String photo,String eventDate, boolean isJoin) {
		this.date = date;
		this.event_id = event_id;
		this.eventDate = eventDate;
		this.month = month;
		this.name = name;
		this.location = location;
		this.time = time;
		this.description = description;
		this.photo = photo;
		this.isJoin = isJoin;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getDate() {
		if(date<10) return "0"+date;
		return ""+date;
	}

	public void setDate(int date) {
		this.date = date;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean isJoin() {
		return isJoin;
	}

	public void setJoin(boolean isJoin) {
		this.isJoin = isJoin;
	}
	
	

}
