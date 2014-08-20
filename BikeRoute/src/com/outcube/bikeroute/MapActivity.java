package com.outcube.bikeroute;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.outcube.bikeroute.utility.SquareView;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends Activity{
	private GoogleMap googleMap;
	private ImageButton rate3Btn, rate2Btn, rate1Btn, rateResetBtn; 
	private Button searchBtn;
	private EditText idEditText;
	private SeekBar seekbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		findAllById();
		initilizeMap();

		OnClickListener L = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.rate3:
					
					break;
				case R.id.rate2:
					
					break;
				case R.id.rate1:
					
					break;
				case R.id.rate0:
					
					break;
				default:
					break;
				}
			}
		};

		seekbar.setMax(100);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {

			}
		});

		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int searchId = Integer.parseInt(idEditText.getText().toString());

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

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}

		// latitude and longitude
		double latitude = 13.788293;
		double longitude = 100.653023;

		// create marker
		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");

		// adding marker
		googleMap.addMarker(marker);

	}

	private void findAllById(){
		rate1Btn = (ImageButton) findViewById(R.id.rate1);
		rate2Btn = (ImageButton) findViewById(R.id.rate2);
		rate3Btn = (ImageButton) findViewById(R.id.rate3);
		rateResetBtn = (ImageButton) findViewById(R.id.rate0);
		searchBtn = (Button) findViewById(R.id.search);
		idEditText = (EditText) findViewById(R.id.id);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
	};


}
