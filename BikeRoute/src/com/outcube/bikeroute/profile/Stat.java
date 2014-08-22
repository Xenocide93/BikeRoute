package com.outcube.bikeroute.profile;

public class Stat {
	private float distance;
	private float calorieBurn;
	private float speedAvg;
	private float speedMax;
	private String totalTime;
	
	public Stat(float dis, float cal, float avg, float max, String totalTime) {
		distance = dis;
		calorieBurn = cal;
		speedAvg = avg;
		speedMax = max;
		this.totalTime = totalTime;
	}
	
	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getCalorieBurn() {
		return calorieBurn;
	}

	public void setCalorieBurn(float calorieBurn) {
		this.calorieBurn = calorieBurn;
	}

	public float getSpeedAvg() {
		return speedAvg;
	}

	public void setSpeedAvg(float speedAvg) {
		this.speedAvg = speedAvg;
	}

	public float getSpeedMax() {
		return speedMax;
	}

	public void setSpeedMax(float speedMax) {
		this.speedMax = speedMax;
	}
}
