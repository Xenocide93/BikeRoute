package com.outcube.bikeroute.profile;

import com.outcube.bikeroute.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.CardHeader;

public class StatCardHeader extends CardHeader {
	private TextView title;
	private String titleText, totalTimeText;
	public View arrow;
	public TextView totalTime;
	
	public StatCardHeader(Context context, int innerLayout) {
		super(context, innerLayout);
		// TODO Auto-generated constructor stub
	}

	public StatCardHeader(Context context, String title, String totalTime) {
		super(context, R.layout.stat_card_header);
		this.titleText = title;
		this.totalTimeText = totalTime;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		title = (TextView) view.findViewById(R.id.header_title);
		totalTime = (TextView) view.findViewById(R.id.total_time);
		arrow = view.findViewById(R.id.card_arrow);
		
		totalTime.setText(totalTimeText);
		title.setText(titleText);

		super.setupInnerViewElements(parent, view);
	}



}
