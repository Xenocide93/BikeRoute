package com.outcube.bikeroute;


import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.outcube.bikeroute.database.EventsForDB;
import com.outcube.bikeroute.database.JoinForDB;
import com.outcube.bikeroute.event.EventCard;
import com.outcube.bikeroute.event.EventCardExpand;
import com.outcube.bikeroute.event.EventCardHeader;
import com.outcube.bikeroute.event.Events;
import com.outcube.bikeroute.utility.SquareImageButton;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventsFragment extends Fragment{
	
	public static ArrayList<Events> thisMonth;
	public static ArrayList<Events> nextMonth;
	public static ArrayList<Events> overAll;
	private ArrayList<EventsForDB> thisMonthEvents;
	private ArrayList<EventsForDB> nextMonthEvents;
	private ArrayList<EventsForDB> overAllEvents;
	private int totalEvents;
	private int countEvents;
	private boolean syncDone;
	private static CardView thisMonthCardView, nextMonthCardView, overallCardView;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		totalEvents = 0;
		countEvents = 0;
		syncDone = false;
		new AsyncCallerEventsFeed().execute(); 
		initializeEventsArrayList();
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initializeThisMonth();
		initializeNextMonth();
		initializeOverall();
		super.onActivityCreated(savedInstanceState);
	}
	
	private void initializeThisMonth(){
		EventCard card = new EventCard(this.getActivity(), R.layout.event_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);
		
		EventCardHeader header = new EventCardHeader(getActivity(), "THIS MONTH");
		card.addCardHeader(header);
		
		EventCardExpand expand = new EventCardExpand(getActivity(), thisMonth);
		card.addCardExpand(expand);
		
		thisMonthCardView = (CardView) getActivity().findViewById(R.id.card_expand_this_month1);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(thisMonthCardView);
		card.setViewToClickToExpand(viewToClickToExpand);
		
//		card.setExpanded(true);
		
		thisMonthCardView.setCard(card);
//		header.arrow.setRotation(-90f);
	}
	
	private void initializeNextMonth(){
		EventCard card = new EventCard(this.getActivity(), R.layout.event_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);
		
		EventCardHeader header = new EventCardHeader(getActivity(), "NEXT MONTH");
		card.addCardHeader(header);
		
		EventCardExpand expand = new EventCardExpand(getActivity(), nextMonth);
		card.addCardExpand(expand);
		
		nextMonthCardView = (CardView) getActivity().findViewById(R.id.card_expand_next_month);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(nextMonthCardView);
		card.setViewToClickToExpand(viewToClickToExpand);
		
		nextMonthCardView.setCard(card);
	}
	
	private void initializeOverall(){
		EventCard card = new EventCard(this.getActivity(), R.layout.event_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);
		
		EventCardHeader header = new EventCardHeader(getActivity(), "OVERALL");
		card.addCardHeader(header);
		
		EventCardExpand expand = new EventCardExpand(getActivity(), overAll);
		card.addCardExpand(expand);
		
		overallCardView = (CardView) getActivity().findViewById(R.id.card_expand_overall);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(overallCardView);
		card.setViewToClickToExpand(viewToClickToExpand);
		
		overallCardView.setCard(card);
	}
	
	
	private void refreshFragment() {
		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_this_month1);
		CardView cardView2 = (CardView) getActivity().findViewById(R.id.card_expand_next_month);
		CardView cardView3 = (CardView) getActivity().findViewById(R.id.card_expand_overall);
		
		EventCard card = new EventCard(this.getActivity(), R.layout.event_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);
		EventCardHeader header = new EventCardHeader(getActivity(), "THIS MONTH");
		card.addCardHeader(header);
		EventCardExpand expand = new EventCardExpand(getActivity(), thisMonth);
		card.addCardExpand(expand);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);
//		card.setExpanded(true);
		cardView.replaceCard(card);
//		header.arrow.setRotation(-90f);
		
		EventCard card2 = new EventCard(this.getActivity(), R.layout.event_card_layout);
		card2.setBackgroundResourceId(R.drawable.curve_background_gray);
		card2.setShadow(false);
		EventCardHeader header2 = new EventCardHeader(getActivity(), "NEXT MONTH");
		card2.addCardHeader(header2);
		EventCardExpand expand2 = new EventCardExpand(getActivity(), nextMonth);
		card2.addCardExpand(expand2);
		ViewToClickToExpand viewToClickToExpand2 = ViewToClickToExpand.builder().highlightView(false).setupView(cardView2);
		card2.setViewToClickToExpand(viewToClickToExpand2);
		cardView2.replaceCard(card2);
		
		EventCard card3 = new EventCard(this.getActivity(), R.layout.event_card_layout);
		card3.setBackgroundResourceId(R.drawable.curve_background_gray);
		card3.setShadow(false);
		EventCardHeader header3 = new EventCardHeader(getActivity(), "OVERALL");
		card3.addCardHeader(header3);
		EventCardExpand expand3 = new EventCardExpand(getActivity(), overAll);
		card3.addCardExpand(expand3);
		ViewToClickToExpand viewToClickToExpand3 = ViewToClickToExpand.builder().highlightView(false).setupView(cardView3);
		card3.setViewToClickToExpand(viewToClickToExpand3);
		cardView3.replaceCard(card3);
	}
	
	private void initializeEventsArrayList() {
		Log.i("Mint", "initializeEventsArrayList");
		thisMonthEvents = new ArrayList<EventsForDB>();
		nextMonthEvents = new ArrayList<EventsForDB>();
		overAllEvents = new ArrayList<EventsForDB>();
		thisMonth = new ArrayList<Events>();
		nextMonth = new ArrayList<Events>();
		overAll = new ArrayList<Events>();
		List<EventsForDB> events = MainActivity.databaseEvents.getAllEvents();
		for (int i = 0; i < events.size(); i++) {
			overAllEvents.add(events.get(i));
			String[] temp = events.get(i).getEvent_startdate().split(" ");
			String[] temp2 = temp[0].split("-");
			int eventMonth = Integer.parseInt(temp2[1]);
			if (eventMonth == MainActivity.nowTimeMonth) thisMonthEvents.add(events.get(i));
			if (eventMonth == MainActivity.nowTimeMonth+1) nextMonthEvents.add(events.get(i));
		}
		toEventsPage();
	}
	
	
	private void toEventsPage() {
		for (int i = 0; i < thisMonthEvents.size(); i++) {
			EventsForDB temp = thisMonthEvents.get(i);
			String[] startDateTime = temp.getEvent_startdate().split(" ");
			String[] endDateTime = temp.getEvent_enddate().split(" ");
			String[] startDate = startDateTime[0].split("-");
			int startDay = Integer.parseInt(startDate[2]);
			int startMonth = Integer.parseInt(startDate[1]);
			Events event = new Events();
			event.setDate(startDay);
			event.setMonth(MainActivity.whatMonth(startMonth));
			event.setDescription(temp.getEvent_desc());
			event.setLocation(temp.getEvent_location());
			event.setName(temp.getEvent_name());
			event.setTime(startDateTime[1] + "-" + endDateTime[1]);
			JoinForDB jointemp = MainActivity.databaseEventsJoined.getEventJoined(temp.getId());
			if (jointemp == null) event.setJoin(false);
			else {
				if (jointemp.getJoin_status() == 1) event.setJoin(true);
				else event.setJoin(false);
			}
			if (temp.getEvent_photo() != null && temp.getEvent_photo().length() != 0) event.setPhoto(temp.getEvent_photo());
			event.setEventDate(startDateTime[0] + " to " + endDateTime[0]);
			event.setEvent_id(temp.getId());
			thisMonth.add(event);
		}
		for (int i = 0; i < nextMonthEvents.size(); i++) {
			EventsForDB temp = nextMonthEvents.get(i);
			String[] startDateTime = temp.getEvent_startdate().split(" ");
			String[] endDateTime = temp.getEvent_enddate().split(" ");
			String[] startDate = startDateTime[0].split("-");
			int startDay = Integer.parseInt(startDate[2]);
			int startMonth = Integer.parseInt(startDate[1]);
			Events event = new Events();
			event.setDate(startDay);
			event.setMonth(MainActivity.whatMonth(startMonth));
			event.setDescription(temp.getEvent_desc());
			event.setLocation(temp.getEvent_location());
			event.setName(temp.getEvent_name());
			event.setTime(startDateTime[1] + "-" + endDateTime[1]);
			JoinForDB jointemp = MainActivity.databaseEventsJoined.getEventJoined(temp.getId());
			if (jointemp == null) event.setJoin(false);
			else {
				if (jointemp.getJoin_status() == 1) event.setJoin(true);
				else event.setJoin(false);
			}
			if (temp.getEvent_photo() != null && temp.getEvent_photo().length() != 0) event.setPhoto(temp.getEvent_photo());
			event.setEventDate(startDateTime[0] + " to " + endDateTime[0]);
			event.setEvent_id(temp.getId());
			nextMonth.add(event);
		}
		for (int i = 0; i < overAllEvents.size(); i++) {
			EventsForDB temp = overAllEvents.get(i);
			String[] startDateTime = temp.getEvent_startdate().split(" ");
			String[] endDateTime = temp.getEvent_enddate().split(" ");
			String[] startDate = startDateTime[0].split("-");
			int startDay = Integer.parseInt(startDate[2]);
			int startMonth = Integer.parseInt(startDate[1]);
			Events event = new Events();
			event.setDate(startDay);
			event.setMonth(MainActivity.whatMonth(startMonth));
			event.setDescription(temp.getEvent_desc());
			event.setLocation(temp.getEvent_location());
			event.setName(temp.getEvent_name());
			event.setTime(startDateTime[1] + "-" + endDateTime[1]);
			JoinForDB jointemp = MainActivity.databaseEventsJoined.getEventJoined(temp.getId());
			if (jointemp == null) event.setJoin(false);
			else {
				if (jointemp.getJoin_status() == 1) event.setJoin(true);
				else event.setJoin(false);
			}
			if (temp.getEvent_photo() != null && temp.getEvent_photo().length() != 0) event.setPhoto(temp.getEvent_photo());
			event.setEventDate(startDateTime[0] + " to " + endDateTime[0]);
			event.setEvent_id(temp.getId());
			overAll.add(event);
		}
		if (syncDone) {
			refreshFragment();
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_events, container, false);
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
		if (MainActivity.nowTimeMonth == webarray[1]) {
			if (MainActivity.nowTimeDay > webarray[2]) return false;
		} 
		if (MainActivity.nowTimeMonth > webarray[1]) return false;
		return true;
	}
	
	private void initialEvents(String response) {
		MainActivity.databaseEvents.deleteAll();
        Gson gson = new GsonBuilder().create();
        try {
            JSONArray arr = new JSONArray(response);
            if(arr.length() != 0){
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    String tempDay = obj.get("event_enddate").toString();
                    if (compareTime(tempDay)) {
	                    EventsForDB event = new EventsForDB();
	                    event.setId(Integer.parseInt(obj.get("event_id").toString()));
	                    event.setEvent_name(obj.get("event_name").toString());
	                    event.setEvent_desc(obj.get("event_desc").toString());
	                    event.setEvent_enddate(tempDay);
	                    event.setEvent_startdate(obj.get("event_startdate").toString());
	                    event.setEvent_location(obj.get("event_location").toString());
	                    MainActivity.databaseEvents.addEvent(event);
	                    totalEvents++;
	                    Log.i("Mint", "add event to sqlite successfully");
	                    new DownloadImageTask().execute(obj.get("event_photo").toString(),event);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
		Log.i("Mint", "initialComplete!");
	}
	
    private class AsyncCallerEventsFeed extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {

        	Log.i("Mint", "AsyncTaskStart");
        	
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams paramss = new RequestParams();
            client.post("http://128.199.216.175/sqlitemysqlsync/getevents.php", paramss, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                    	Log.i("Mint", "syncComplete!");
                    	initialEvents(response);
                    }
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        if (statusCode == 404) {
                        	Log.i("Mint", "Requested resource not found");
                        } else if (statusCode == 500) {
                        	Log.i("Mint", "Something went wrong at server end");
                        } else {
                        	Log.i("Mint", "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]");
                        }
                    }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.i("Mint", "onPostExecute!");
        }

    }
	
	private class DownloadImageTask extends AsyncTask<Object, Void, Void> {
		
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
	    protected Void doInBackground(Object... urls) {
	        String urldisplay = (String) urls[0];
	        EventsForDB event = (EventsForDB) urls[1];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	            String encodedImage;
				int decrease = 0;
				do {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					mIcon11.compress(Bitmap.CompressFormat.JPEG, 100-decrease, baos);
					byte[] imageBytes = baos.toByteArray();
					try {
						baos.close();
						baos = null;
					} catch (IOException e) {
					}
					encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
					decrease += 1;
				} while (encodedImage.length() > 400000);
	            event.setEvent_photo(encodedImage);
	            MainActivity.databaseEvents.updateEvent(event);
	            countEvents++;
	            Log.i("Mint", "update event to sqlite successfully");
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return null;
	    }

	    protected void onPostExecute(Void result) {
	    	super.onPostExecute(result);
	    	if (totalEvents == countEvents) {
	    		syncDone = true;
	    		initializeEventsArrayList();
	    	}
	    }
	}
	
	
	public static void updateJoinBtnStatus(int eventId){
		boolean isThisMonthEvent = false;
		for (int i = 0; i < thisMonth.size(); i++) {
			if(thisMonth.get(i).getEvent_id() == eventId){
				Events pack = thisMonth.get(i);
				View row = ((EventCardExpand)thisMonthCardView.getCard().getCardExpand()).linearLayoutListView.getChildAt(i);
				if(pack.isJoin()){
					pack.setJoin(false);
					if (MainActivity.databaseEventsJoined.getEventJoined(pack.getEvent_id()) != null) {
						MainActivity.databaseEventsJoined.updateEventJoined(new JoinForDB(pack.getEvent_id(), 2, "no"));
					} else {
						MainActivity.databaseEventsJoined.addEventJoined(new JoinForDB(pack.getEvent_id(), 2, "no"));
					}
					((SquareImageButton)row.findViewById(R.id.join_btn)).setBackgroundResource(R.drawable.curve_background_purple_selector);
					((TextView)row.findViewById(R.id.join_btn_text)).setText("JOIN");
				} else {
					pack.setJoin(true);
					if (MainActivity.databaseEventsJoined.getEventJoined(pack.getEvent_id()) != null) {
						MainActivity.databaseEventsJoined.updateEventJoined(new JoinForDB(pack.getEvent_id(), 1, "no"));
					} else {
						MainActivity.databaseEventsJoined.addEventJoined(new JoinForDB(pack.getEvent_id(), 1, "no"));
					}
					((SquareImageButton)row.findViewById(R.id.join_btn)).setBackgroundResource(R.drawable.curve_background_orange_selector);
					((TextView)row.findViewById(R.id.join_btn_text)).setText("CANCEL");
				}
				row.requestLayout();
				row.invalidate();
				isThisMonthEvent = false;
			}
		}
		if(!isThisMonthEvent){
			for (int i = 0; i < nextMonth.size(); i++) {
				if(nextMonth.get(i).getEvent_id() == eventId){
					Events pack = nextMonth.get(i);
					View row = ((EventCardExpand)nextMonthCardView.getCard().getCardExpand()).linearLayoutListView.getChildAt(i);
					if(pack.isJoin()){
						pack.setJoin(false);
						if (MainActivity.databaseEventsJoined.getEventJoined(pack.getEvent_id()) != null) {
							MainActivity.databaseEventsJoined.updateEventJoined(new JoinForDB(pack.getEvent_id(), 2, "no"));
						} else {
							MainActivity.databaseEventsJoined.addEventJoined(new JoinForDB(pack.getEvent_id(), 2, "no"));
						}
						((SquareImageButton)row.findViewById(R.id.join_btn)).setBackgroundResource(R.drawable.curve_background_purple_selector);
						((TextView)row.findViewById(R.id.join_btn_text)).setText("JOIN");
					} else {
						pack.setJoin(true);
						if (MainActivity.databaseEventsJoined.getEventJoined(pack.getEvent_id()) != null) {
							MainActivity.databaseEventsJoined.updateEventJoined(new JoinForDB(pack.getEvent_id(), 1, "no"));
						} else {
							MainActivity.databaseEventsJoined.addEventJoined(new JoinForDB(pack.getEvent_id(), 1, "no"));
						}
						((SquareImageButton)row.findViewById(R.id.join_btn)).setBackgroundResource(R.drawable.curve_background_orange_selector);
						((TextView)row.findViewById(R.id.join_btn_text)).setText("CANCEL");
					}
					row.requestLayout();
					row.invalidate();
				}
			}
		}
		for (int i = 0; i < overAll.size(); i++) {
			if(overAll.get(i).getEvent_id() == eventId){
				Events pack = overAll.get(i);
				View row = ((EventCardExpand)overallCardView.getCard().getCardExpand()).linearLayoutListView.getChildAt(i);
				if(pack.isJoin()){
					pack.setJoin(false);
					if (MainActivity.databaseEventsJoined.getEventJoined(pack.getEvent_id()) != null) {
						MainActivity.databaseEventsJoined.updateEventJoined(new JoinForDB(pack.getEvent_id(), 2, "no"));
					} else {
						MainActivity.databaseEventsJoined.addEventJoined(new JoinForDB(pack.getEvent_id(), 2, "no"));
					}
					((SquareImageButton)row.findViewById(R.id.join_btn)).setBackgroundResource(R.drawable.curve_background_purple_selector);
					((TextView)row.findViewById(R.id.join_btn_text)).setText("JOIN");
				} else {
					pack.setJoin(true);
					if (MainActivity.databaseEventsJoined.getEventJoined(pack.getEvent_id()) != null) {
						MainActivity.databaseEventsJoined.updateEventJoined(new JoinForDB(pack.getEvent_id(), 1, "no"));
					} else {
						MainActivity.databaseEventsJoined.addEventJoined(new JoinForDB(pack.getEvent_id(), 1, "no"));
					}
					((SquareImageButton)row.findViewById(R.id.join_btn)).setBackgroundResource(R.drawable.curve_background_orange_selector);
					((TextView)row.findViewById(R.id.join_btn_text)).setText("CANCEL");
				}
				row.requestLayout();
				row.invalidate();
			}
		}
		new syncJOIN().execute();
	}

	
	
    private static class syncJOIN extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
        }
        @Override
        protected Void doInBackground(Void... param) {
    		//Create AsycHttpClient object
    		AsyncHttpClient client = new AsyncHttpClient();
    		RequestParams params = new RequestParams();
    		List<JoinForDB> joinList = MainActivity.databaseEventsJoined.getAllEventsJoined();
    		ArrayList<JoinForDB> joinLists = new ArrayList<JoinForDB>();
    		for (int i = 0; i < joinList.size(); i++) {
    			joinLists.add(joinList.get(i));
    		}
    		if(joinLists.size()!=0){
    			if(MainActivity.databaseEventsJoined.dbSyncCount() != 0){
    				params.put("usersJSON", MainActivity.databaseEventsJoined.composeJSONfromSQLite());
    				client.post("http://128.199.216.175/sqlitemysqlsync/insertjoin.php",params ,new AsyncHttpResponseHandler() {
    					@Override
    					public void onSuccess(String response) {
    						try {
    							JSONArray arr = new JSONArray(response);
    							for(int i=0; i<arr.length();i++){
    								JSONObject obj = (JSONObject)arr.get(i);
    								MainActivity.databaseEventsJoined.updateSyncStatus(obj.get("event_id").toString(),obj.get("status").toString());
    							}
    							Log.i("Mint", "DB Sync completed!");
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
    			}else{
    				Log.i("Mint", "SQLite and Remote MySQL DBs are in Sync!");
    			}
    		}else{
    			Log.i("Mint", "No data in SQLite DB, please do enter data to perform Sync action");
    		}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
	
    public static void expandThisMonth(){
    	if(thisMonthCardView == null) return;
    	thisMonthCardView.setExpanded(true);
    }

}
