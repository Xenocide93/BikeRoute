package com.outcube.bikeroute.profile;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.outcube.bikeroute.R;

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
	private History[] histories;
	private TextView day, month, placeFrom, placeTo, time, distance;

	private static MapFragment map;

	public HistoryCardExpand(Context context, History[] histories) {
		super(context,R.layout.history_expand);
		this.histories = histories;
	}

	public HistoryCardExpand(Context context, int innerLayout) {
		super(context,R.layout.history_expand);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		parent.setBackgroundColor(Color.TRANSPARENT);

		LinearLayout linearLayoutListView = (LinearLayout) view.findViewById(R.id.linear_layout_listview_replacement);

		//    	HistoryAdapter adapter = new HistoryAdapter(mContext, R.layout.history_listview_item, histories);
		//    	listView.setAdapter(adapter);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < histories.length; i++) {
			final History pack = histories[i];
			View row = inflater.inflate(R.layout.history_listview_item, linearLayoutListView, false);
			findAllById(row);
			day.setText(pack.getDate());
			month.setText(pack.getMonth());
			placeFrom.setText(pack.getStart_location());
			placeTo.setText(pack.getFinal_location());
			time.setText(pack.getTotalTime());
			distance.setText(pack.getDistance()+"");

			row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Dialog dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
					dialog.setContentView(R.layout.history_popup_layout);
					dialog.setCanceledOnTouchOutside(true);
					dialog.setCancelable(true);
					
					TextView day, month, placeFrom, placeTo, time;
					day = (TextView) dialog.findViewById(R.id.text_day);
					month = (TextView) dialog.findViewById(R.id.text_month);
					placeFrom = (TextView) dialog.findViewById(R.id.place_from);
					placeTo = (TextView) dialog.findViewById(R.id.place_to);
					time = (TextView) dialog.findViewById(R.id.time);

					day.setText(pack.getDate());
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

					dialog.show();
				}
			});

			linearLayoutListView.addView(row);
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
