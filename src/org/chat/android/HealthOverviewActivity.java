package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.HealthTopic;
import org.chat.android.models.HealthTopicAccessed;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HealthOverviewActivity extends BaseActivity {
	Context context = null;
	int hhId = 0;
	int visitId = 0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
		setContentView(R.layout.activity_health_overview);
		
		Bundle b = getIntent().getExtras();
		hhId = b.getInt("hhId");
		visitId = b.getInt("visitId");
		
		checkThemeComplete();
    }
	
	public void onResume() {
		super.onResume();
		checkThemeComplete();
	}
   
	public void openHealthOverviewRecord(View v) {
		String healthTheme = null;
		healthTheme = (String) v.getTag();
		Boolean previouslyAccessedFlag = false;
		
		Dao<HealthSelectRecorded, Integer> hsrDao;		
		DatabaseHelper hsrDbHelper = new DatabaseHelper(getApplicationContext());
		try {
			hsrDao = hsrDbHelper.getHealthSelectRecordedDao();
			List<HealthSelectRecorded> hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("theme", healthTheme).query();			
			Iterator<HealthSelectRecorded> iter = hsrList.iterator();
			while (iter.hasNext()) {
				HealthSelectRecorded hsr = iter.next();
				previouslyAccessedFlag = true;
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		Intent i;
		if (previouslyAccessedFlag == true) {
			i = new Intent(HealthOverviewActivity.this, HealthDetailsActivity.class);
		} else {
			i = new Intent(HealthOverviewActivity.this, HealthOverviewRecordActivity.class);
		}
		
		Bundle b = new Bundle();
		b.putString("healthTheme",healthTheme);
		b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
		i.putExtras(b);
		startActivity(i);
	}
	
	public void checkThemeComplete() {
    	List<ImageView> imgViewList = new ArrayList<ImageView>();
    	imgViewList.add((ImageView)findViewById(R.id.health_HIV_button_img));
    	imgViewList.add((ImageView)findViewById(R.id.health_disease_button_img));
    	imgViewList.add((ImageView)findViewById(R.id.health_nutrition_button_img));
    	imgViewList.add((ImageView)findViewById(R.id.health_development_button_img));
    	
    	List<ImageView> checkmarkList = new ArrayList<ImageView>();
    	checkmarkList.add((ImageView) findViewById(R.id.health_HIV_checkmark));
    	checkmarkList.add((ImageView) findViewById(R.id.health_disease_checkmark));
    	checkmarkList.add((ImageView) findViewById(R.id.health_nutrition_checkmark));
    	checkmarkList.add((ImageView) findViewById(R.id.health_development_checkmark));
    	
		// pull all of the topics accessed for this household, then clear those with duplicate 
		List<HealthTopicAccessed> topicsAccessed = new ArrayList<HealthTopicAccessed>();
		Dao<HealthTopicAccessed, Integer> htaDao;		
		DatabaseHelper htaDbHelper = new DatabaseHelper(context);
		try {
			htaDao = htaDbHelper.getHealthTopicsAccessed();
			List<HealthTopicAccessed> htaList = htaDao.queryBuilder().where().eq("hh_id",hhId).and().isNotNull("end_time").query();
			Iterator<HealthTopicAccessed> iter = htaList.iterator();
			while (iter.hasNext()) {
				HealthTopicAccessed hta = iter.next();
				topicsAccessed.add(hta);
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// for each theme (ie button)
		for (int i = 0; i < imgViewList.size(); i++) {
			List<HealthTopic> healthTopics = new ArrayList<HealthTopic>();
			Dao<HealthTopic, Integer> topicDao;
			
			// pull all topics related to that theme
			DatabaseHelper topicDbHelper = new DatabaseHelper(context);
			try {
				topicDao = topicDbHelper.getHealthTopicsDao();
				List<HealthTopic> tList = topicDao.queryBuilder().where().eq("theme",imgViewList.get(i).getTag()).query();
				Iterator<HealthTopic> iter = tList.iterator();
				while (iter.hasNext()) {
					HealthTopic t = iter.next();
					healthTopics.add(t);
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			// iterate over the topics list to see if the topic_id is in the hta_list
			int compareCounter = 0;
			for (HealthTopic ht : healthTopics) {
				for (HealthTopicAccessed hta : topicsAccessed) {
					if (ht.getId() == hta.getTopicId()) {
						compareCounter++;
						break;						// user can redo the same topic again, so there may be multiple htas with the same name - break to avoid double counting
					}	
				}
			}
			
			// mark as complete the button if all of the topic_ids are in the hta list (for this household)
			if (healthTopics.size() > 0 && compareCounter == healthTopics.size()) {
				imgViewList.get(i).setAlpha(90);
				checkmarkList.get(i).setVisibility(View.VISIBLE);
			}
			
		}
	}	
}

// TODO: clean this class up so that it uses themes pulled from the health_themes table as opposed to hardcoded

