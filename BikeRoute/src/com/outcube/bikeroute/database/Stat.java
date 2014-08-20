package com.outcube.bikeroute.database;

public class Stat {
	private float distance;
	private float calorieBurn;
	private float speedAvg;
	private float speedMax;
	
	public Stat(float dis, float cal, float avg, float max) {
		distance = dis;
		calorieBurn = cal;
		speedAvg = avg;
		speedMax = max;
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
