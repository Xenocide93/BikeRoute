package com.outcube.bikeroute.database;

public class JoinForDB {
	
	private int event_id;
	private int join_status; // 1 = join, 2 = not join
	private String status;
	
	public JoinForDB() {
		
	}
	
	public JoinForDB(int event_id,int join_status,String status) {
		this.event_id = event_id;
		this.join_status = join_status;
		this.status = status;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public int getJoin_status() {
		return join_status;
	}

	public void setJoin_status(int join_status) {
		this.join_status = join_status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
