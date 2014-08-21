package com.outcube.bikeroute;

import com.outcube.bikeroute.profile.History;
import com.outcube.bikeroute.profile.HistoryCard;
import com.outcube.bikeroute.profile.HistoryCardExpand;
import com.outcube.bikeroute.profile.HistoryCardHeader;
import com.outcube.bikeroute.profile.StatCard;
import com.outcube.bikeroute.profile.StatCardExpand;
import com.outcube.bikeroute.profile.StatCardHeader;
import com.outcube.bikeroute.utility.SquareView;

import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.view.CardView;
import it.gmariotti.cardslib.library.internal.Card;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment{
	SquareView profileView;
	TextView name, address;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initializeRadioBtn();
		initializeProfile();

		initializeTodayHistory();
		initializeWeeklyHistory();
		initializeMonthlyHistory();

		initializeTodayStat();
		initializeWeeklyStat();

		super.onActivityCreated(savedInstanceState);
	}

	private void initializeRadioBtn(){
		final View view = getView();
		final View historyLayout = view.findViewById(R.id.history_layout);
		final View statLayout = view.findViewById(R.id.stat_layout);
		RadioGroup radioGroupHisStat = (RadioGroup) view.findViewById(R.id.radioGroupHisStat);
		radioGroupHisStat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.radioHistory){
					statLayout.animate().alpha(0.0f).setDuration(300).start();
					new Handler().postDelayed(new Runnable(){
						@Override
						public void run() {
							statLayout.setVisibility(View.GONE);
							historyLayout.setVisibility(View.VISIBLE);
							historyLayout.setAlpha(0.0f);
						}
					}, 300);
					historyLayout.animate().alpha(1.0f).setStartDelay(300).setDuration(300).start();
				} else {
					historyLayout.animate().alpha(0.0f).setDuration(300).start();
					new Handler().postDelayed(new Runnable(){
						@Override
						public void run() {
							historyLayout.setVisibility(View.GONE);
							statLayout.setVisibility(View.VISIBLE);
							statLayout.setAlpha(0.0f);
						}
					}, 300);
					statLayout.animate().alpha(1.0f).setStartDelay(300).setDuration(300).start();
				}
			}
		});

	}

	private void initializeProfile(){
		View view = getView();
		profileView = (SquareView) view.findViewById(R.id.profile);
		name = (TextView) view.findViewById(R.id.name);
		address = (TextView) view.findViewById(R.id.address);

		//TODO set name
		//TODO set address
		//TODO set profile
		profileView.setBackgroundResource(R.drawable.profile_mock);
	}

	private void initializeTodayStat(){
		StatCard card = new StatCard(getActivity());
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		StatCardHeader header = new StatCardHeader(getActivity(), "Today", "00:45:29");
		card.addCardHeader(header);

		StatCardExpand expand = new StatCardExpand(getActivity(), 2.1f, 430f, 3.6f, 4.8f);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.stat_card_expand_today);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		card.setExpanded(true);

		cardView.setCard(card);
		header.arrow.setRotation(-90f);
		header.arrow.setAlpha(0.0f);
		header.totalTime.setAlpha(1.0f);
	}
	
	private void initializeWeeklyStat(){
		StatCard card = new StatCard(getActivity());
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		StatCardHeader header = new StatCardHeader(getActivity(), "Weelky", "00:45:29");
		card.addCardHeader(header);

		StatCardExpand expand = new StatCardExpand(getActivity(), 15.6f, 9430f, 3.2f, 5.1f);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.stat_card_expand_weekly);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		cardView.setCard(card);
		header.arrow.setAlpha(1.0f);
		header.totalTime.setAlpha(0.0f);
	}

	private void initializeTodayHistory(){
		History[] histories = {
				new History("08", "JAN", "Paragon", "The Mall Bangkapi", "01:53:12", 20),
				new History("13", "MAR", "Chulalongkorn", "Central World", "00:22:45", 1),
				new History("25", "DEC", "MBK", "Victory Monument ", "00:38:10", 7)
		};

		HistoryCard card = new HistoryCard(this.getActivity(), R.layout.expand_outside_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		//TODO set number of history today
		int historyNumToday = histories.length;
		HistoryCardHeader header = new HistoryCardHeader(getActivity(), "Today ("+historyNumToday+")");
		card.addCardHeader(header);

		HistoryCardExpand expand = new HistoryCardExpand(getActivity(), histories);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_today);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		card.setExpanded(true);

		cardView.setCard(card);
		header.arrow.setRotation(-90f);
	}

	private void initializeWeeklyHistory(){
		History[] histories = {
				new History("08", "JAN", "Paragon", "The Mall Bangkapi", "01:53:12", 20)
		};

		HistoryCard card = new HistoryCard(this.getActivity(), R.layout.expand_outside_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		//TODO set number of history this week
		int historyNumWeekly = histories.length;
		HistoryCardHeader header = new HistoryCardHeader(getActivity(), "Weekly ("+historyNumWeekly+")");
		card.addCardHeader(header);

		HistoryCardExpand expand = new HistoryCardExpand(getActivity(), histories);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_weekly);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		cardView.setCard(card);
	}

	private void initializeMonthlyHistory(){
		History[] histories = {
				new History("08", "JAN", "Paragon", "The Mall Bangkapi", "01:53:12", 20),
				new History("13", "MAR", "Chulalongkorn", "Central World", "00:22:45", 1)
		};

		HistoryCard card = new HistoryCard(this.getActivity(), R.layout.expand_outside_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		//TODO set number of history this month
		int historyNumMonthly = histories.length;
		HistoryCardHeader header = new HistoryCardHeader(getActivity(), "Monthly ("+historyNumMonthly+")");
		card.addCardHeader(header);

		HistoryCardExpand expand = new HistoryCardExpand(getActivity(), histories);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_monthly);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		cardView.setCard(card);
	}

}
