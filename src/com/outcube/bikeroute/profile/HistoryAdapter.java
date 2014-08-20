package com.outcube.bikeroute.profile;

import com.outcube.bikeroute.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoryAdapter extends ArrayAdapter<History> {
	private LayoutInflater inflater;
	private int resourceId;

	public HistoryAdapter(Context context, int resource, History[] objects) {
		super(context, resource, objects);
		this.resourceId = resource;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ItemHolder holder = null;
		History pack = getItem(position);
		
		if(row == null){
			inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	row = inflater.inflate(resourceId, parent, false);
        	holder = new ItemHolder(row);
        	row.setTag(holder);
		} else {
			holder = (ItemHolder)row.getTag();
		}
		
		holder.day.setText(pack.getDate());
		holder.month.setText(pack.getMonth());
		holder.placeFrom.setText(pack.getStart_location());
		holder.placeTo.setText(pack.getFinal_location());
		holder.time.setText(pack.getTotalTime());
		holder.distance.setText(pack.getDistance()+"");
		
		return row;
	}

	private class ItemHolder{
		public TextView day, month, placeFrom, placeTo, time, distance;

		public ItemHolder(View v) {
			day = (TextView) v.findViewById(R.id.text_day);
			month = (TextView) v.findViewById(R.id.text_month);
			placeFrom = (TextView) v.findViewById(R.id.place_from);
			placeTo = (TextView) v.findViewById(R.id.place_to);
			time = (TextView) v.findViewById(R.id.time);
			distance = (TextView) v.findViewById(R.id.distance);

		}
	}

}
