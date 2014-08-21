package com.outcube.bikeroute.profile;

import com.outcube.bikeroute.R;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

public class StatCard extends Card {

	public StatCard(Context context) {
		this(context, R.layout.expand_outside_card_layout);
		initialize();
	}
	
	public StatCard(Context context, int innerLayout) {
		super(context, innerLayout);
		initialize();
	}
	
	private void initialize(){
		this.setOnExpandAnimatorStartListener(new OnExpandAnimatorStartListener() {
			@Override
			public void onExpandStart(Card card) {
				((StatCardHeader)card.getCardHeader()).arrow.animate().alpha(0.0f).rotation(-90f).setDuration(300).start();
				((StatCardHeader)card.getCardHeader()).totalTime.animate().alpha(1.0f).setDuration(300).start();
			}
		});
		this.setOnCollapseAnimatorStartListener(new OnCollapseAnimatorStartListener() {
			@Override
			public void onCollapseStart(Card card) {
				((StatCardHeader)card.getCardHeader()).arrow.animate().alpha(1.0f).rotation(0f).setDuration(300).start();
				((StatCardHeader)card.getCardHeader()).totalTime.animate().alpha(0.0f).setDuration(300).start();
			}
		});
	}
	
}
