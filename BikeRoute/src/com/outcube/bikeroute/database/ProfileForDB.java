package com.outcube.bikeroute.database;

public class ProfileForDB {
	
	private int jour_dist;
	private int jour_maxspeed; 	
	private int jour_cal;
	private String jour_startdatetime; 	
	private String jour_stopdatetime;	
	private String jour_startpoint;
	private String jour_endpoint;

	
	public ProfileForDB(int jour_dist,int jour_maxspeed,int jour_cal,String jour_startdatetime,
			String jour_stopdatetime,String jour_startpoint,String jour_endpoint) {
		this.jour_dist = jour_dist;
		this.jour_maxspeed = jour_maxspeed;
		this.jour_cal = jour_cal;
		this.jour_startdatetime = jour_startdatetime;
		this.jour_stopdatetime = jour_stopdatetime;
		this.jour_startpoint = jour_startpoint;
		this.jour_endpoint = jour_endpoint;
	}


	public int getJour_dist() {
		return jour_dist;
	}


	public void setJour_dist(int jour_dist) {
		this.jour_dist = jour_dist;
	}


	public int getJour_maxspeed() {
		return jour_maxspeed;
	}


	public void setJour_maxspeed(int jour_maxspeed) {
		this.jour_maxspeed = jour_maxspeed;
	}


	public int getJour_cal() {
		return jour_cal;
	}


	public void setJour_cal(int jour_cal) {
		this.jour_cal = jour_cal;
	}


	public String getJour_startdatetime() {
		return jour_startdatetime;
	}


	public void setJour_startdatetime(String jour_startdatetime) {
		this.jour_startdatetime = jour_startdatetime;
	}


	public String getJour_stopdatetime() {
		return jour_stopdatetime;
	}


	public void setJour_stopdatetime(String jour_stopdatetime) {
		this.jour_stopdatetime = jour_stopdatetime;
	}


	public String getJour_startpoint() {
		return jour_startpoint;
	}


	public void setJour_startpoint(String jour_startpoint) {
		this.jour_startpoint = jour_startpoint;
	}


	public String getJour_endpoint() {
		return jour_endpoint;
	}


	public void setJour_endpoint(String jour_endpoint) {
		this.jour_endpoint = jour_endpoint;
	}
	
}
