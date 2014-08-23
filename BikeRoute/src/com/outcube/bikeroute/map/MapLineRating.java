package com.outcube.bikeroute.map;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class MapLineRating {


	private ArrayList<LatLng> points;
	private Polyline polyline;
	private int rating;
	private int startPercent;
	private int endPercent;
	
	public MapLineRating(ArrayList<LatLng> points, Polyline polyline, int rating, int startPercent, int endPercent){
		this.points = points;
		this.polyline = polyline;
		this.rating = rating;
		this.startPercent = startPercent;
		this.endPercent = endPercent;
	}

	public ArrayList<LatLng> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<LatLng> points) {
		this.points = points;
	}

	public Polyline getPolyline() {
		return polyline;
	}

	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	public int getStartPercent() {
		return startPercent;
	}

	public void setStartPercent(int startPercent) {
		this.startPercent = startPercent;
	}

	public int getEndPercent() {
		return endPercent;
	}

	public void setEndPercent(int endPercent) {
		this.endPercent = endPercent;
	}
	
	private JSONObject latLngToJSON(LatLng x){
		JSONObject object = new JSONObject();
		try {
			object.put("lat", Double.valueOf(x.latitude));
			object.put("lng", Double.valueOf(x.longitude));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public ArrayList<JSONObject> toJSONList() {
		ArrayList<JSONObject> jsonArray = new ArrayList<JSONObject>();
		for(int i=1;i<points.size();i++){
			LatLng startPoint = points.get(i-1);
			LatLng endPoint = points.get(i);
			
			JSONObject object = new JSONObject();
			try {
				object.put("start", latLngToJSON(startPoint));
				object.put("end", latLngToJSON(endPoint));
				object.put("rating", Integer.valueOf(rating));
				jsonArray.add(object);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
		return jsonArray;
	}
}


