package com.outcube.bikeroute.profile;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.outcube.bikeroute.R;
import com.outcube.bikeroute.utility.BlurBackgroundDialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import it.gmariotti.cardslib.library.internal.CardExpand;

public class HistoryCardExpand extends CardExpand {
	private ArrayList<History> histories;
	private TextView day, month, placeFrom, placeTo, time, distance;

	private static MapFragment map;

	public HistoryCardExpand(Context context, ArrayList<History> histories) {
		super(context,R.layout.history_expand);
		this.histories = histories;
	}

	public HistoryCardExpand(Context context, int innerLayout) {
		super(context,R.layout.history_expand);
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		parent.setBackgroundColor(Color.TRANSPARENT);
		LinearLayout linearLayoutListView = (LinearLayout) view.findViewById(R.id.linear_layout_listview_replacement);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(histories.size()==0){
			View row = inflater.inflate(R.layout.history_listview_item_empty, linearLayoutListView, false);
			linearLayoutListView.addView(row);
		} else {
			for (int i = 0; i < histories.size(); i++) {
				final History pack = histories.get(i);
				View row = inflater.inflate(R.layout.history_listview_item, linearLayoutListView, false);
				findAllById(row);
				day.setText(pack.getDate()+"");
				month.setText(pack.getMonth());
				placeFrom.setText(pack.getStart_location());
				placeTo.setText(pack.getFinal_location());
				time.setText(pack.getTotalTime());
				distance.setText(pack.getDistance()+"");

				row.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						BlurBackgroundDialog dialog = new BlurBackgroundDialog(mContext);
						dialog.setContentView(R.layout.history_popup_layout);
						dialog.setCanceledOnTouchOutside(true);
						dialog.setCancelable(true);

						TextView day, month, placeFrom, placeTo, time;
						day = (TextView) dialog.findViewById(R.id.text_day);
						month = (TextView) dialog.findViewById(R.id.text_month);
						placeFrom = (TextView) dialog.findViewById(R.id.place_from);
						placeTo = (TextView) dialog.findViewById(R.id.place_to);
						time = (TextView) dialog.findViewById(R.id.time);

						day.setText(pack.getDate()+"");
						month.setText(pack.getMonth());
						placeFrom.setText(pack.getStart_location());
						placeTo.setText(pack.getFinal_location());
						time.setText(pack.getTotalTime());

						dialog.setOnDismissListener(new OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								FragmentTransaction ft2 = ((Activity)HistoryCardExpand.this.getContext()).getFragmentManager().beginTransaction();
								ft2.remove( ((Activity)HistoryCardExpand.this.getContext()).getFragmentManager().findFragmentByTag("map"));
								ft2.commit();
							}
						});

						GoogleMap googleMap = ((MapFragment) ((Activity)HistoryCardExpand.this.getContext()).getFragmentManager().findFragmentById(R.id.map)).getMap();
						
						//TODO setup map and route

						dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						dialog.show();
					}
				});
				linearLayoutListView.addView(row);
			}
		}
	}

	private void findAllById(View v){
		day = (TextView) v.findViewById(R.id.text_day);
		month = (TextView) v.findViewById(R.id.text_month);
		placeFrom = (TextView) v.findViewById(R.id.place_from);
		placeTo = (TextView) v.findViewById(R.id.place_to);
		time = (TextView) v.findViewById(R.id.time);
		distance = (TextView) v.findViewById(R.id.distance);
	}

}
