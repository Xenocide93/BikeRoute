package com.outcube.bikeroute.profile;

public class History {

	private String date;
	private String month;
	private String start_location;
	private String final_location;
	private String totalTime;
	private int distance;

	public History(String date, String month, String start_location, String final_location, String totalTime,int distance) {
		this.date = date;
		this.month = month;
		this.start_location = start_location;
		this.final_location = final_location;
		this.totalTime = totalTime;
		this.distance = distance;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getStart_location() {
		return start_location;
	}
	public void setStart_location(String start_location) {
		this.start_location = start_location;
	}

	public String getFinal_location() {
		return final_location;
	}
	public void setFinal_location(String final_location) {
		this.final_location = final_location;
	}

	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
}
