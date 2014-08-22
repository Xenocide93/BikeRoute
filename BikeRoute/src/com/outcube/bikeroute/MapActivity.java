package com.outcube.bikeroute;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds.*;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.outcube.bikeroute.map.MapLineRating;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MapActivity extends Activity{
	private GoogleMap googleMap;
	private ImageButton rate3Btn, rate2Btn, rate1Btn, rateResetBtn; 
	private Button searchBtn;
	private Button completeBtn;
	private EditText idEditText;
	private SeekBar seekbar;
	private ArrayList<LatLng> grayPoint;
	private double[] grayDistance;
	private Polyline grayLine;
	private Marker grayMarker;
	private double totalgrayDistance;
	public LatLngBounds grayBounds;
	private ArrayList<MapLineRating> ratings;
	private int startPercent, endPercent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		findAllById();
		initilizeMap();
		//Everything will be enabled when the route is loaded
		setEnabledEverything(false);
		completeBtn.setEnabled(false);

		
		OnClickListener L = new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Press reset button
				if(v.getId()==R.id.rate0){
					undoFillColor();
					return;
				}
				//Press color button
				if(endPercent == startPercent){
					Toast.makeText(getApplicationContext(), "Please move the marker forward", Toast.LENGTH_SHORT).show();
					return;
				}
				switch (v.getId()) {
				case R.id.rate3:
					//Toast.makeText(getApplicationContext(), "Rate: 3", Toast.LENGTH_SHORT).show();
					drawPolylineFromPtoP(startPercent, endPercent, 3);
					break;
				case R.id.rate2:
					//Toast.makeText(getApplicationContext(), "Rate: 2", Toast.LENGTH_SHORT).show();
					drawPolylineFromPtoP(startPercent, endPercent, 2);
					break;
				case R.id.rate1:
					//Toast.makeText(getApplicationContext(), "Rate: 1", Toast.LENGTH_SHORT).show();
					drawPolylineFromPtoP(startPercent, endPercent, 1);
					break;
				default:
					break;
				}
				startPercent = endPercent;
			}
		};

		rate3Btn.setOnClickListener(L);
		rate2Btn.setOnClickListener(L);
		rate1Btn.setOnClickListener(L);
		rateResetBtn.setOnClickListener(L);

		seekbar.setMax(100);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if(seekBar.getProgress()<startPercent){
					seekBar.setProgress(startPercent);
					return;
				}
				//Toast.makeText(getApplicationContext(), "Progress: "+seekBar.getProgress(), Toast.LENGTH_SHORT).show();
				movePointerToPercent(seekBar.getProgress());
				endPercent = seekBar.getProgress();
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				movePointerToPercent(seekBar.getProgress());
			}
		});

		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int searchId = Integer.parseInt(idEditText.getText().toString());
				Toast.makeText(getApplicationContext(), "Search ID: "+searchId, Toast.LENGTH_SHORT).show();
				//Disable every button until the data is loaded
				setEnabledEverything(false);
				findRoute(searchId);
			}
		});

		completeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				complete();
				Toast.makeText(getApplicationContext(), "Complete!", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}


	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void findAllById(){
		rate1Btn = (ImageButton) findViewById(R.id.rate1);
		rate2Btn = (ImageButton) findViewById(R.id.rate2);
		rate3Btn = (ImageButton) findViewById(R.id.rate3);
		rateResetBtn = (ImageButton) findViewById(R.id.rate0);
		searchBtn = (Button) findViewById(R.id.search);
		completeBtn = (Button) findViewById(R.id.complete);
		idEditText = (EditText) findViewById(R.id.id);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
	};

	private void calculateDistance(){
		int size = grayPoint.size();
		grayDistance = new double[size-1];
		totalgrayDistance = 0;
		for(int i=1;i<size;i++){
			float results[] = new float[1];
			LatLng a = grayPoint.get(i-1);
			LatLng b = grayPoint.get(i);
			Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results);
			grayDistance[i-1] = results[0];
			totalgrayDistance += results[0];
		}
	}
	
	private void movePointerToPercent(int percent){
		int results[] = new int[1];
		LatLng btwn = getLatLngFromPercent(percent, results);
		if(grayMarker!=null){
			grayMarker.remove();
		}
		grayMarker = googleMap.addMarker(new MarkerOptions().position(btwn));
		seekbar.setProgress(percent);
	}
	
	private LatLng getLatLngFromPercent(int percent, int[] btwnPercent){
		double distance  = totalgrayDistance*(double)percent/100;
		double accumulateDistance = 0;
		int i=0;
		for(; i<grayDistance.length; i++){
			if(accumulateDistance+grayDistance[i] < distance){
				accumulateDistance+=grayDistance[i];
			} else{
				break;
			}
		}
		btwnPercent[0] = i;
		LatLng begin = grayPoint.get(i);
		LatLng end = grayPoint.get(i+1);
		LatLng btwn = new LatLng(
				begin.latitude + (end.latitude-begin.latitude)*(distance-accumulateDistance)/grayDistance[i],
				begin.longitude + (end.longitude-begin.longitude)*(distance-accumulateDistance)/grayDistance[i]
				);
		return btwn;
	}
	
	private void drawPolylineFromPtoP(int startP, int endP, int rating){
		//Should not happen, but just in case
		if(startP>endP) drawPolylineFromPtoP(endP,startP, rating);
		else if (startP==endP) return;
		
		ArrayList<LatLng> pointList = new ArrayList<LatLng>();
		int results1[] = new int[1];
		int results2[] = new int[1];
		LatLng startPosition = getLatLngFromPercent(startP, results1);
		LatLng endPosition = getLatLngFromPercent(endP, results2);
		int startIndex = results1[0];
		int endIndex = results2[0];
		pointList.add(startPosition);
		while(startIndex<endIndex){
			startIndex++;
			pointList.add(grayPoint.get(startIndex));
		}
		pointList.add(endPosition);
		
		// Instantiates a new Polyline object and adds points to define a rectangle
		PolylineOptions rectOptions = new PolylineOptions();
		for(LatLng p:pointList){
			rectOptions.add(p);
		}
		int lineColor=0;
		switch (rating) {
		case 1:
			lineColor = Color.RED;
			break;
		case 2:
			lineColor = Color.YELLOW;
			break;
		case 3:
			lineColor = Color.GREEN;
			break;
		default:
			lineColor = Color.GRAY;
			break;
		}
		rectOptions.color(lineColor);
		rectOptions.width(5);
		Polyline line = googleMap.addPolyline(rectOptions);
		ratings.add(new MapLineRating(pointList, line, rating, startP, endP));
		if(endP==100){
			completeBtn.setEnabled(true);
		}
	}
	
	private void undoFillColor(){
		if(ratings==null || ratings.isEmpty()){
			resetFillColor();
			return;
		}
		MapLineRating lastLine = ratings.get(ratings.size()-1);
		startPercent = lastLine.getStartPercent();
		lastLine.getPolyline().remove();
		ratings.remove(ratings.size()-1);
		completeBtn.setEnabled(false);
	}
	
	private void resetFillColor(){
		if(ratings==null) ratings = new ArrayList<MapLineRating>();
		if(!ratings.isEmpty()){
			for (MapLineRating s : ratings) s.getPolyline().remove();
			ratings.clear();
		}
		ratings.clear();
		startPercent = 0;
		movePointerToPercent(0);
	}
	
	private void fillRoute(){
		// Instantiates a new Polyline object and adds points to define a rectangle
		PolylineOptions rectOptions = new PolylineOptions();
	    Builder builder= new LatLngBounds.Builder();
		for(LatLng p: grayPoint){
			rectOptions.add(p);
			builder.include(p);
		}
		grayLine = googleMap.addPolyline(rectOptions);
		//Fit map with the bounds
		grayBounds = builder.build();
		googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
		    @Override
		    public void onMapLoaded() {
		    	DisplayMetrics displaymetrics = new DisplayMetrics();
		    	getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		    	int width = displaymetrics.widthPixels;
				googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(grayBounds, width/10));
		    }
		});
		calculateDistance();
		resetFillColor();
	}
	
	private void setEnabledEverything(boolean value){
		rate3Btn.setEnabled(value);
		rate2Btn.setEnabled(value);
		rate1Btn.setEnabled(value);
		rateResetBtn.setEnabled(value);
		seekbar.setEnabled(value);
	}
	
	private void findRoute(int routeId){
		//TODO: fill grayPoint with LatLng data from backend, not these dummy data
		//No need to specify the line colors, it will be painted gray anyway.
		//http://128.199.216.175/cms/service/getRouteFromJourney.php?jour_id=routeId
		grayPoint = new ArrayList<LatLng>();
		grayPoint.add(new LatLng(37.35, -122.0));
        grayPoint.add(new LatLng(37.45, -122.0));  // North of the previous point, but at the same longitude
        grayPoint.add(new LatLng(37.45, -122.2));  // Same latitude, and 30km to the west
        grayPoint.add(new LatLng(37.35, -122.2));  // Same longitude, and 16km to the south
        fillRoute();
        setEnabledEverything(true);
	}
	
	private void complete(){
		if(ratings==null) return;
		JSONArray jsonArray = new JSONArray();
		for(MapLineRating m:ratings){
			ArrayList<JSONObject> arr = m.toJSONList();
			for(JSONObject j:arr){
				jsonArray.put(j);
			}
		}
		JSONObject jsonData = new JSONObject();
		try {
			jsonData.put("data", jsonArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jsonStr = jsonData.toString();
	    System.out.println(jsonStr);
	    //TODO: send these data to backend
	}
}
