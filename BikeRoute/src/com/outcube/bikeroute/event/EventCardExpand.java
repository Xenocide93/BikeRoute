package com.outcube.bikeroute.event;

import java.util.ArrayList;

import com.outcube.bikeroute.EventsFragment;
import com.outcube.bikeroute.R;
import com.outcube.bikeroute.utility.BlurBackgroundDialog;
import com.outcube.bikeroute.utility.SquareImageButton;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import it.gmariotti.cardslib.library.internal.CardExpand;

public class EventCardExpand extends CardExpand {
	private  ArrayList<Events> events;
	private TextView day, month, name, place, time, joinBtnText;
	private SquareImageButton joinBtn;
	public LinearLayout linearLayoutListView;

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
		if(linearLayoutListView == null)linearLayoutListView = (LinearLayout) view.findViewById(R.id.linear_layout_listview_replacement);
		setupLinearLayoutListView(linearLayoutListView, events);
	}

	public void refreshInnerViewElements(ArrayList<Events> events){
		linearLayoutListView.removeAllViews();
		this.events = events;
		setupLinearLayoutListView(linearLayoutListView, events);
		linearLayoutListView.requestLayout();
		linearLayoutListView.invalidate();
	}

	private void setupLinearLayoutListView(LinearLayout layout, ArrayList<Events> events){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < events.size(); i++) {
			final Events pack = events.get(i);
			View row = inflater.inflate(R.layout.event_listview_item, linearLayoutListView, false);
			findAllById(row);
			day.setText(pack.getDate());
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
				@Override
				public void onClick(View v) {
					EventsFragment.updateJoinBtnStatus(pack.getEvent_id());
				}
			});

			row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					BlurBackgroundDialog dialog = new BlurBackgroundDialog(mContext);
					dialog.setContentView(R.layout.event_popup);
					dialog.setCanceledOnTouchOutside(true);
					dialog.setCancelable(true);

					TextView dayTitle,month,title,place,date,time,description;
					final TextView joinBtnTextPopup;
					ImageView image = (ImageView) dialog.findViewById(R.id.image);
					dayTitle = (TextView) dialog.findViewById(R.id.text_day);
					month = (TextView) dialog.findViewById(R.id.text_month);
					title = (TextView) dialog.findViewById(R.id.name);
					place = (TextView) dialog.findViewById(R.id.place);
					date = (TextView) dialog.findViewById(R.id.date);
					time = (TextView) dialog.findViewById(R.id.time);
					description = (TextView) dialog.findViewById(R.id.description);
					joinBtnTextPopup = (TextView) dialog.findViewById(R.id.join_btn_text);
					final ImageButton joinBtnPopup = (ImageButton) dialog.findViewById(R.id.join_btn);

					dayTitle.setText(pack.getDate());
					month.setText(pack.getMonth());
					title.setText(pack.getName());
					place.setText(pack.getLocation());
					date.setText(pack.getEventDate());
					time.setText(pack.getTime());
					description.setText(pack.getDescription());
					image.setImageBitmap(decodeByteArray(pack.getPhoto()));

					dayTitle.setText(pack.getDate());
					
					if(pack.isJoin()){
						joinBtnTextPopup.setText("CANCEL");
						joinBtnPopup.setBackgroundResource(R.drawable.curve_background_orange_selector);
					} else {
						joinBtnTextPopup.setText("JOIN");
						joinBtnPopup.setBackgroundResource(R.drawable.curve_background_purple_selector);
					}
					
					joinBtnPopup.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(pack.isJoin()){
								joinBtnTextPopup.setText("JOIN");
								joinBtnPopup.setBackgroundResource(R.drawable.curve_background_purple_selector);
							} else {
								joinBtnTextPopup.setText("CANCEL");
								joinBtnPopup.setBackgroundResource(R.drawable.curve_background_orange_selector);
							}
							EventsFragment.updateJoinBtnStatus(pack.getEvent_id());
						}
					});
					
					dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					dialog.show();
				}
			});

			linearLayoutListView.addView(row);
		}
	}
	
	private Bitmap decodeByteArray(String image) {
		byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
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
