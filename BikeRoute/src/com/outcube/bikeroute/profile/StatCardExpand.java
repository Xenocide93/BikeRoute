package com.outcube.bikeroute.profile;

import com.outcube.bikeroute.R;

import android.content.Context;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.CardExpand;

public class StatCardExpand extends CardExpand {

	private TextView distance, calorie, speedAvg, speedMax;
	private float distanceNum, calorieNum, speedAvgNum, speedMaxNum;

	public StatCardExpand(Context context, float distance, float cal, float avg, float max) {
		super(context,R.layout.stat_card_expand);
		this.distanceNum = distance;
		this.calorieNum = cal;
		this.speedAvgNum = avg;
		this.speedMaxNum = max;
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		parent.setBackgroundColor(Color.TRANSPARENT);
		findAllById(view);
		distance.setText(getProcessedFloatString(distanceNum));
		calorie.setText((int)(calorieNum)+"");
		speedAvg.setText(getProcessedFloatString(speedAvgNum));
		speedMax.setText(getProcessedFloatString(speedMaxNum));
	}

	private String getProcessedFloatString(float f){
		return ((float) ((int)(f * 10)/10.0))+"";
	}

	private void findAllById(View v){
		distance = (TextView) v.findViewById(R.id.distance);
		calorie = (TextView) v.findViewById(R.id.calorie);
		speedAvg = (TextView) v.findViewById(R.id.speed_avg);
		speedMax = (TextView) v.findViewById(R.id.speed_max);
	}

}
