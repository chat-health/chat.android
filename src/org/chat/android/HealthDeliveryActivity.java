package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.HealthPage;
import org.chat.android.models.HealthTopic;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class HealthDeliveryActivity extends Activity {
	
	Context context = null;
	
	List<HealthPage> pages = new ArrayList<HealthPage>();
	int pageCounter = 0;
	int lastPage = 0;
	
	TextView paginationTextField = null;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    context = getApplicationContext();
	    setContentView(R.layout.activity_health_delivery);
	    
        String topic = null;
		Bundle b = getIntent().getExtras();
		topic = b.getString("topic");
		
		// get the required pages for the topic
		populatePagesArray(topic);
		
	    paginationTextField = (TextView) findViewById(R.id.paginationTextField);
	    
	    // update the fragment in the UI to the first page
	    updatePageCounter("next");
	    updateDisplayedFragment(pageCounter);
    }
	
	public void updateDisplayedFragment(int pageNum) {
		HealthPage p = pages.get(pageNum - 1);
	    String type = p.getType();
	    type = type.substring (0,1).toUpperCase() + type.substring (1).toLowerCase();			// cap the first letter so we can use it as a class name
	    
	    // oh the joys of static typing. This nonsense allows us to 'cast' the type to a fragment
	    Fragment newFrag = null;
		try {
			newFrag = (Fragment) Class.forName("org.chat.android.pages." + type + "Fragment").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// bundle in the unique page parameters to the correct fragment
		Bundle bundle = new Bundle();
		bundle.putString("type",p.getType());
		bundle.putInt("id",p.getPageContentId());
		newFrag.setArguments(bundle);
		
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    ft.replace(R.id.placeholder, newFrag);
	    ft.commit();
	}
	
	public void moveNext(View v) {
		// check if this page is within bounds (1 to lastPage)
		if (pageCounter + 1 <= lastPage) {
			updatePageCounter("next");
			updateDisplayedFragment(pageCounter);	
		} else {
			Toast.makeText(getApplicationContext(),"Last page reached",Toast.LENGTH_SHORT).show();
		}
	}
	
	public void moveBack(View v) {
		// check if this page is within bounds (1 to lastPage)
		if (pageCounter - 1 >= 1) {
			updatePageCounter("back");
			updateDisplayedFragment(pageCounter);
		} else {
			Toast.makeText(getApplicationContext(),"First page reached",Toast.LENGTH_SHORT).show();
		}
	}
	
	public void updatePageCounter(String m) {
		if (m.equals("next")) {
			pageCounter++;
			String p = pageCounter + "/" + lastPage;
			paginationTextField.setText(p);	
		} else if (m.equals("back")) {
			pageCounter--;
			String p = pageCounter + "/" + lastPage;
			paginationTextField.setText(p);
		} else {
			Log.e("Incorrect parameter passed to updatePageCounter: ", m);
		}
	}
	
	public void populatePagesArray(String topic) {
		int topicId = 0;
		
		// get the topic Id based on the topic name
		Dao<HealthTopic, Integer> sDao;		
		DatabaseHelper sDbHelper = new DatabaseHelper(context);
		try {
			sDao = sDbHelper.getHealthTopicsDao();
			List<HealthTopic> sList = sDao.queryBuilder().where().eq("name",topic).query();
			Iterator<HealthTopic> iter = sList.iterator();
			while (iter.hasNext()) {
				HealthTopic s = iter.next();
				topicId = s.getId();
			}
		} catch (SQLException e2) {
			Log.e("Topic does not exist in the DB: ", topic);
			e2.printStackTrace();
		}
		
		// populate the pages array based on the topic Id and determine number of pages
		Dao<HealthPage, Integer> pDao;		
		DatabaseHelper pDbHelper = new DatabaseHelper(context);
		try {
			pDao = pDbHelper.getHealthPagesDao();
			List<HealthPage> pList = pDao.queryBuilder().where().eq("topic_id",topicId).query();
			// clears out the null junk values - there is likely a better way to do this
			for (HealthPage p : pList) {
    			pages.add(p);
        	}
			lastPage = pages.size();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
}