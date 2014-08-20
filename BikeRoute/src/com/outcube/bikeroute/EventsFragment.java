package com.outcube.bikeroute;


import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.outcube.bikeroute.database.Events;
import com.outcube.bikeroute.database.EventsForDB;
import com.outcube.bikeroute.event.EventCard;
import com.outcube.bikeroute.event.EventCardExpand;
import com.outcube.bikeroute.event.EventCardHeader;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
	
	
	// TODO: กด join, cancel event ต่างๆ ได้ ส่ง user_id, event_id ไป table eventjoined
	// have another sqlite to store join history.

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new AsyncCallerEventsFeed().execute();
		thisMonthEvents = new ArrayList<EventsForDB>();
		nextMonthEvents = new ArrayList<EventsForDB>();
		overAllEvents = new ArrayList<EventsForDB>();
		thisMonth = new ArrayList<Events>();
		nextMonth = new ArrayList<Events>();
		overAll = new ArrayList<Events>();
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
		
		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_this_month1);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);
		
		card.setExpanded(true);
		
		cardView.setCard(card);
		header.arrow.setRotation(270f);
	}
	
	private void initializeNextMonth(){
		EventCard card = new EventCard(this.getActivity(), R.layout.event_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);
		
		EventCardHeader header = new EventCardHeader(getActivity(), "NEXT MONTH");
		card.addCardHeader(header);
		
		EventCardExpand expand = new EventCardExpand(getActivity(), nextMonth);
		card.addCardExpand(expand);
		
		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_next_month);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);
		
		cardView.setCard(card);
	}
	
	private void initializeOverall(){
		EventCard card = new EventCard(this.getActivity(), R.layout.event_card_layout);
		card.setBackgroundResourceId(R.drawable.curve_background_gray);
		card.setShadow(false);
		
		EventCardHeader header = new EventCardHeader(getActivity(), "OVERALL");
		card.addCardHeader(header);
		
		EventCardExpand expand = new EventCardExpand(getActivity(), overAll);
		card.addCardExpand(expand);
		
		CardView cardView = (CardView) getActivity().findViewById(R.id.card_expand_overall);
		ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().highlightView(false).setupView(cardView);
		card.setViewToClickToExpand(viewToClickToExpand);
		
		cardView.setCard(card);
	}
	
	private ArrayList<Events> getTestEventArrayList(){
		ArrayList<Events> array = new ArrayList<Events>();
		array.add(new Events(1, 12, "JAN", "event_name_1", "location_name_1", "07.30-11.00", "none", null, null));
		array.add(new Events(2, 21, "DEC", "event_name_2", "location_name_2", "08.30-12.00", "none", null, null));
		array.add(new Events(3, 30, "JUN", "event_name_3", "location_name_3", "09.30-13.00", "none", null, null));
		return array;
	}
	
	private void initializeEventsArrayList() {
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
			event.setPhoto(decodeByteArray(temp.getEvent_photo()));
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
			event.setPhoto(decodeByteArray(temp.getEvent_photo()));
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
			event.setPhoto(decodeByteArray(temp.getEvent_photo()));
			event.setEventDate(startDateTime[0] + " to " + endDateTime[0]);
			event.setEvent_id(temp.getId());
			overAll.add(event);
		}
	}
	
	private Bitmap decodeByteArray(byte[] image) {
		return BitmapFactory.decodeByteArray(image, 0, image.length);
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
		if (MainActivity.nowTimeMonth > webarray[1]) return false;
		if (MainActivity.nowTimeDay > webarray[2]) return false;
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
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            mIcon11.compress(Bitmap.CompressFormat.PNG, 100, out);
	            byte[] buffer = out.toByteArray();
	            event.setEvent_photo(buffer);
	            MainActivity.databaseEvents.addEvent(event);
	            Log.i("Mint", "add event to sqlite successfully");
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return null;
	    }

	    protected void onPostExecute(Void result) {
	    	super.onPostExecute(result);
	    }
	}

}
