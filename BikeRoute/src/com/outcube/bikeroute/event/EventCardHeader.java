package com.outcube.bikeroute.event;

import com.outcube.bikeroute.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.CardHeader;

public class EventCardHeader extends CardHeader {
	private TextView title;
	private String titleText;
	public View arrow;

	public EventCardHeader(Context context, int innerLayout) {
		super(context, innerLayout);
	}
	
	public EventCardHeader(Context context, String title) {
		super(context, R.layout.event_card_header);
		this.titleText = title;
	}
	
	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		title = (TextView) view.findViewById(R.id.header_title);
		arrow = view.findViewById(R.id.card_arrow);
		
		title.setText(titleText);
		
		super.setupInnerViewElements(parent, view);
	}
	
	

}
