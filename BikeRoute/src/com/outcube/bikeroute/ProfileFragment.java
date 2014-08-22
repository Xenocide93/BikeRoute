package com.outcube.bikeroute;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.outcube.bikeroute.database.ProfileForDB;
import com.outcube.bikeroute.profile.History;
import com.outcube.bikeroute.profile.HistoryCard;
import com.outcube.bikeroute.profile.HistoryCardExpand;
import com.outcube.bikeroute.profile.HistoryCardHeader;
import com.outcube.bikeroute.profile.Stat;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
	
	private boolean syncComplete;
	public static Stat todayStat;
	public static Stat weeklyStat;
	public static ArrayList<History> todayHistory;
	public static ArrayList<History> weeklyHistory;
	public static ArrayList<History> monthlyHistory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeHistoryAndStat();
		new syncProfile().execute();
		syncComplete = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		return rootView;
	}
	
	private boolean compareTime(String web) {
		String[] webtemp = web.split(" ");
		String[] webtemp2 = webtemp[0].split("-");
		int[] webarray = new int[webtemp2.length];
		for (int i = 0; i < webtemp2.length;i++) {
			webarray[i] = Integer.parseInt(webtemp2[i]);
		}
		if (MainActivity.nowTimeYear != webarray[0]) return false;
		if (MainActivity.nowTimeMonth != webarray[1]) return false;
		return true;
	}
	
	
	private void initializeHistoryAndStat() {
		List<ProfileForDB> temp = MainActivity.databaseProfile.getAllProfiles();
		float todayDistance = 0;
		float todayCalorieBurn = 0;
		float todaySpeedMax = 0;
		float todayTime = 0;
		long todayTotalTime = 0;
		float weeklyDistance = 0;
		float weeklyCalorieBurn = 0;
		float weeklySpeedMax = 0;
		float weeklyTime = 0;
		long weeklyTotalTime = 0;
		todayHistory = new ArrayList<History>();
		weeklyHistory = new ArrayList<History>();
		monthlyHistory = new ArrayList<History>();
		for (int i = 0; i < temp.size(); i++) {
			ProfileForDB temp2 = temp.get(i);
			String[] stopTimeTemp = temp2.getJour_stopdatetime().split(" ");
			String[] stopDate = stopTimeTemp[0].split("-");
			int stopDay = Integer.parseInt(stopDate[2]);
			String totalTime = calculateTime(temp2.getJour_startdatetime(),temp2.getJour_stopdatetime());
			monthlyHistory.add(new History(stopDay, MainActivity.whatMonth(MainActivity.nowTimeMonth),temp2.getJour_startpoint(), temp2.getJour_endpoint(), totalTime, temp2.getJour_dist()));
			if (stopDay == MainActivity.nowTimeDay) {
				// today
				todayDistance += temp2.getJour_dist();
				todayCalorieBurn += temp2.getJour_cal();
				todaySpeedMax = Math.max(todaySpeedMax, temp2.getJour_maxspeed());
				todayTime += calculateHours(temp2.getJour_startdatetime(),temp2.getJour_stopdatetime());
				todayTotalTime += calculateTotalTime(temp2.getJour_startdatetime(),temp2.getJour_stopdatetime());
				todayHistory.add(new History(stopDay, MainActivity.whatMonth(MainActivity.nowTimeMonth),temp2.getJour_startpoint(), temp2.getJour_endpoint(), totalTime, temp2.getJour_dist()));
			} 
			if (stopDay > MainActivity.nowTimeDay - MainActivity.dayOfWeek) {
				// this week
				weeklyDistance += temp2.getJour_dist();
				weeklyCalorieBurn += temp2.getJour_cal();
				weeklySpeedMax = Math.max(weeklySpeedMax, temp2.getJour_maxspeed());
				weeklyTime += calculateHours(temp2.getJour_startdatetime(),temp2.getJour_stopdatetime());
				weeklyTotalTime += calculateTotalTime(temp2.getJour_startdatetime(),temp2.getJour_stopdatetime());
				weeklyHistory.add(new History(stopDay, MainActivity.whatMonth(MainActivity.nowTimeMonth),temp2.getJour_startpoint(), temp2.getJour_endpoint(), totalTime, temp2.getJour_dist()));
			}
			
		}
		todayStat = new Stat(todayDistance, todayCalorieBurn, (todayDistance/todayTime), todaySpeedMax, timeToString(todayTotalTime));
		weeklyStat = new Stat(weeklyDistance, weeklyCalorieBurn, (weeklyDistance/weeklyTime), weeklySpeedMax, timeToString(weeklyTotalTime));
		if (syncComplete) {
			refreshTodayStat();
		}
	}


	
	private String timeToString(long time) {
		long diffSeconds = time / 1000 % 60;
		long diffMinutes = time / (60 * 1000) % 60;
		long diffHours = time / (60 * 60 * 1000) % 24;
		long diffDays = time / (24 * 60 * 60 * 1000);
		diffHours = diffHours + 24*diffDays;
		return (int)diffHours + ":" + (int)diffMinutes + ":" + (int)diffSeconds;
	}
	
	private long calculateTotalTime(String startTime, String stopTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(startTime);
			d2 = format.parse(stopTime);
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
			return diff;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private float calculateHours(String startTime, String stopTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(startTime);
			d2 = format.parse(stopTime);
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			return diffHours + 24*diffDays;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private String calculateTime(String startTime, String stopTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(startTime);
			d2 = format.parse(stopTime);
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			diffHours = diffHours + 24*diffDays;
			return diffHours + ":" + diffMinutes + ":" + diffSeconds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "00:00:00";
	}
	
    private class syncProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... param) {
    		//Create AsycHttpClient object
    		AsyncHttpClient client = new AsyncHttpClient();
    		RequestParams params = new RequestParams();
    		ArrayList<HashMap<String, Integer>> toGson = new ArrayList<HashMap<String, Integer>>();
    		HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put("user_id", MainActivity.user_id);
            toGson.add(map);
            Gson gson = new GsonBuilder().create();
			params.put("usersJSON", gson.toJson(toGson));
			client.post("http://128.199.216.175/sqlitemysqlsync/getjourneys.php",params ,new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					try {
						MainActivity.databaseProfile.deleteAll();
						JSONArray arr = new JSONArray(response);
						for(int i=0; i<arr.length();i++){
							JSONObject obj = (JSONObject)arr.get(i);
							String tempDay = obj.get("jour_startdatetime").toString();
		                    if (compareTime(tempDay)) {
								ProfileForDB profile = new ProfileForDB();
								profile.setJour_cal(Integer.parseInt(obj.get("jour_cal").toString()));
								profile.setJour_dist(Integer.parseInt(obj.get("jour_dist").toString()));
								profile.setJour_endpoint(obj.get("jour_endpoint").toString());
								profile.setJour_maxspeed(Integer.parseInt(obj.get("jour_maxspeed").toString()));
								profile.setJour_startdatetime(obj.get("jour_startdatetime").toString());
								profile.setJour_startpoint(obj.get("jour_startpoint").toString());
								profile.setJour_stopdatetime(obj.get("jour_stopdatetime").toString());
								MainActivity.databaseProfile.addProfile(profile);
		                    }
						}
						syncComplete = true;
						initializeHistoryAndStat();
					} catch (JSONException e) {
						Log.i("Mint", "Error Occured [Server's JSON response might be invalid]!");
						e.printStackTrace();
					}
				}
				@Override
				public void onFailure(int statusCode, Throwable error,String content) {
					if(statusCode == 404){
						Log.i("Mint", "Requested resource not found");
					}else if(statusCode == 500){
						Log.i("Mint", "Something went wrong at server end");
					}else{
						Log.i("Mint", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]");
					}
				}
			});
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

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
	
	private void refreshTodayStat() {
		Log.i("Mint", "refresh fragment");
		StatCard card = new StatCard(getActivity());
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		StatCardHeader header = new StatCardHeader(getActivity(), "Today", todayStat.getTotalTime());
		card.addCardHeader(header);

		StatCardExpand expand = new StatCardExpand(getActivity(), (int)todayStat.getDistance(), (int)todayStat.getCalorieBurn(), (int)todayStat.getSpeedAvg(), (int)todayStat.getSpeedMax());
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.stat_card_expand_today);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		card.setExpanded(true);

		cardView.replaceCard(card);
		header.arrow.setRotation(-90f);
		header.arrow.setAlpha(0.0f);
		header.totalTime.setAlpha(1.0f);
		refreshWeeklyStat();
	}
	
	private void refreshWeeklyStat() {
		StatCard card = new StatCard(getActivity());
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		StatCardHeader header = new StatCardHeader(getActivity(), "Weelky", weeklyStat.getTotalTime());
		card.addCardHeader(header);

		StatCardExpand expand = new StatCardExpand(getActivity(), (int)weeklyStat.getDistance(), (int)weeklyStat.getCalorieBurn(), (int)weeklyStat.getSpeedAvg(), (int)weeklyStat.getSpeedMax());
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.stat_card_expand_weekly);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		cardView.replaceCard(card);
		header.arrow.setAlpha(1.0f);
		header.totalTime.setAlpha(0.0f);
		refreshTodayHistory();
	}
	
	private void refreshTodayHistory() {
		HistoryCard card = new HistoryCard(this.getActivity(), R.layout.expand_outside_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		int historyNumToday = todayHistory.size();
		HistoryCardHeader header = new HistoryCardHeader(getActivity(), "Today ("+historyNumToday+")");
		card.addCardHeader(header);

		HistoryCardExpand expand = new HistoryCardExpand(getActivity(), todayHistory);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_today);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		card.setExpanded(true);

		cardView.replaceCard(card);
		header.arrow.setRotation(-90f);
		refreshWeeklyHistory();
	}
	
	private void refreshWeeklyHistory() {
		HistoryCard card = new HistoryCard(this.getActivity(), R.layout.expand_outside_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		int historyNumWeekly = weeklyHistory.size();
		HistoryCardHeader header = new HistoryCardHeader(getActivity(), "Weekly ("+historyNumWeekly+")");
		card.addCardHeader(header);

		HistoryCardExpand expand = new HistoryCardExpand(getActivity(), weeklyHistory);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_weekly);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		cardView.replaceCard(card);
		refreshMonthlyHistory();
	}
	
	private void refreshMonthlyHistory() {
		HistoryCard card = new HistoryCard(this.getActivity(), R.layout.expand_outside_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		int historyNumMonthly = monthlyHistory.size();
		HistoryCardHeader header = new HistoryCardHeader(getActivity(), "Monthly ("+historyNumMonthly+")");
		card.addCardHeader(header);

		HistoryCardExpand expand = new HistoryCardExpand(getActivity(), monthlyHistory);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_monthly);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		cardView.replaceCard(card);
		Log.i("Mint", "refresh done");
	}

	private void initializeTodayStat(){
		StatCard card = new StatCard(getActivity());
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		StatCardHeader header = new StatCardHeader(getActivity(), "Today", todayStat.getTotalTime());
		card.addCardHeader(header);

		StatCardExpand expand = new StatCardExpand(getActivity(), (int)todayStat.getDistance(), (int)todayStat.getCalorieBurn(), (int)todayStat.getSpeedAvg(), (int)todayStat.getSpeedMax());
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

		StatCardHeader header = new StatCardHeader(getActivity(), "Weelky", weeklyStat.getTotalTime());
		card.addCardHeader(header);

		StatCardExpand expand = new StatCardExpand(getActivity(), (int)weeklyStat.getDistance(), (int)weeklyStat.getCalorieBurn(), (int)weeklyStat.getSpeedAvg(), (int)weeklyStat.getSpeedMax());
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.stat_card_expand_weekly);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		cardView.setCard(card);
		header.arrow.setAlpha(1.0f);
		header.totalTime.setAlpha(0.0f);
	}

	private void initializeTodayHistory(){
		

		HistoryCard card = new HistoryCard(this.getActivity(), R.layout.expand_outside_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		int historyNumToday = todayHistory.size();
		HistoryCardHeader header = new HistoryCardHeader(getActivity(), "Today ("+historyNumToday+")");
		card.addCardHeader(header);

		HistoryCardExpand expand = new HistoryCardExpand(getActivity(), todayHistory);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_today);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		card.setExpanded(true);

		cardView.setCard(card);
		header.arrow.setRotation(-90f);
	}

	private void initializeWeeklyHistory(){
		

		HistoryCard card = new HistoryCard(this.getActivity(), R.layout.expand_outside_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		int historyNumWeekly = weeklyHistory.size();
		HistoryCardHeader header = new HistoryCardHeader(getActivity(), "Weekly ("+historyNumWeekly+")");
		card.addCardHeader(header);

		HistoryCardExpand expand = new HistoryCardExpand(getActivity(), weeklyHistory);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_weekly);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		cardView.setCard(card);
	}

	private void initializeMonthlyHistory(){


		HistoryCard card = new HistoryCard(this.getActivity(), R.layout.expand_outside_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);

		int historyNumMonthly = monthlyHistory.size();
		HistoryCardHeader header = new HistoryCardHeader(getActivity(), "Monthly ("+historyNumMonthly+")");
		card.addCardHeader(header);

		HistoryCardExpand expand = new HistoryCardExpand(getActivity(), monthlyHistory);
		card.addCardExpand(expand);

		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_monthly);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);

		cardView.setCard(card);
	}

}
