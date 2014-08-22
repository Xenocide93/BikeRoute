package com.outcube.bikeroute.event;

import java.util.ArrayList;

import com.outcube.bikeroute.R;
import com.outcube.bikeroute.utility.SquareImageButton;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.CardExpand;

public class EventCardExpand extends CardExpand {
	private  ArrayList<Events> events;
	private TextView day, month, name, place, time, joinBtnText;
	private SquareImageButton joinBtn;
	
	public EventCardExpand(Context context, ArrayList<Events> events) {
		super(context,R.layout.event_expand);
		this.events = events;
	}
	
	public EventCardExpand(Context context, int innerLayout) {
		super(context,R.layout.event_expand);
	}
	
	@Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
    	parent.setBackgroundColor(Color.TRANSPARENT);
    	
    	LinearLayout linearLayoutListView = (LinearLayout) view.findViewById(R.id.linear_layout_listview_replacement);
    	
//    	HistoryAdapter adapter = new HistoryAdapter(mContext, R.layout.history_listview_item, histories);
//    	listView.setAdapter(adapter);
    	
    	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	for (int i = 0; i < events.size(); i++) {
    		final Events pack = events.get(i);
    		
    		View row = inflater.inflate(R.layout.event_listview_item, linearLayoutListView, false);
    		findAllById(row);
    		if(pack.getDate() < 10) day.setText("0"+pack.getDate());
    		else day.setText(""+pack.getDate());
    		month.setText(pack.getMonth());
    		name.setText(pack.getName());
    		place.setText(pack.getLocation());
    		time.setText(pack.getTime());
    		
    		
    		if(pack.isJoin()){
    			joinBtnText.setText("CANCEL");
    			joinBtn.setBackgroundResource(R.drawable.curve_background_orange_selector);
    		} else {
    			joinBtnText.setText("JOIN");
    			joinBtn.setBackgroundResource(R.drawable.curve_background_purple_selector);
    		}
    		
    		joinBtn.setOnClickListener(new OnClickListener() {
				@SuppressWarnings("unused")
				@Override
				public void onClick(View v) {
					if(pack.isJoin()){
						pack.setJoin(false);
						joinBtnText.setText("JOIN");
		    			joinBtn.setBackgroundResource(R.drawable.curve_background_purple_selector);
					} else {
						pack.setJoin(true);
						joinBtnText.setText("CANCEL");
		    			joinBtn.setBackgroundResource(R.drawable.curve_background_orange_selector);
					}
				}
			});
    		
    		linearLayoutListView.addView(row);
		}
    	
    }
	
	private void findAllById(View v){
		day = (TextView) v.findViewById(R.id.text_day);
		month = (TextView) v.findViewById(R.id.text_month);
		name = (TextView) v.findViewById(R.id.name);
		place = (TextView) v.findViewById(R.id.place);
		time = (TextView) v.findViewById(R.id.time);
		joinBtnText = (TextView) v.findViewById(R.id.join_btn_text);
		joinBtn = (SquareImageButton) v.findViewById(R.id.join_btn);
	}

}
