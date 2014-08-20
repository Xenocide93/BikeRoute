package com.outcube.bikeroute.profile;

import com.outcube.bikeroute.R;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

public class HistoryCard extends Card {

	public HistoryCard(Context context) {
		this(context, R.layout.history_card_layout);
		initialize();
	}
	
	public HistoryCard(Context context, int innerLayout) {
		super(context, innerLayout);
		initialize();
	}
	
	private void initialize(){
		this.setOnExpandAnimatorStartListener(new OnExpandAnimatorStartListener() {
			@Override
			public void onExpandStart(Card card) {
				((HistoryCardHeader)card.getCardHeader()).arrow.animate().rotationBy(-90f).setDuration(300).start();
			}
		});
		this.setOnCollapseAnimatorStartListener(new OnCollapseAnimatorStartListener() {
			@Override
			public void onCollapseStart(Card card) {
				((HistoryCardHeader)card.getCardHeader()).arrow.animate().rotationBy(90f).setDuration(300).start();
			}
		});
	}
	
}
