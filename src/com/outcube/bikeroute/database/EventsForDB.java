package com.outcube.bikeroute.database;

public class EventsForDB {
	
	private int event_id;
	private String event_name;
	private String event_desc;
	private byte[] event_photo;
	private String event_location;
	private String event_startdate;
	private String event_enddate;
	
	public EventsForDB() {
		
	}
	
	public EventsForDB(int id, String event_name, String event_desc, byte[] event_photo,
		String event_location, String event_startdate, String event_enddate) {
		this.event_id = id;
		this.event_desc = event_desc;
		this.event_enddate = event_enddate;
		this.event_location = event_location;
		this.event_name = event_name;
		this.event_photo = event_photo;
		this.event_startdate = event_startdate;
	}

	public int getId() {
		return event_id;
	}

	public void setId(int id) {
		this.event_id = id;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public String getEvent_desc() {
		return event_desc;
	}

	public void setEvent_desc(String event_desc) {
		this.event_desc = event_desc;
	}

	public byte[] getEvent_photo() {
		return event_photo;
	}

	public void setEvent_photo(byte[] event_photo) {
		this.event_photo = event_photo;
	}

	public String getEvent_location() {
		return event_location;
	}

	public void setEvent_location(String event_location) {
		this.event_location = event_location;
	}

	public String getEvent_startdate() {
		return event_startdate;
	}

	public void setEvent_startdate(String event_startdate) {
		this.event_startdate = event_startdate;
	}

	public String getEvent_enddate() {
		return event_enddate;
	}

	public void setEvent_enddate(String event_enddate) {
		this.event_enddate = event_enddate;
	}
	
	

}
