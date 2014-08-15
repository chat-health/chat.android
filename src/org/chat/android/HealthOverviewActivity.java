package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.HealthTopic;
import org.chat.android.models.HealthTopicAccessed;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
		
		//setupUIElements();
		
		checkThemeComplete();
    }
	
	public void onResume() {
		super.onResume();
		checkThemeComplete();
	}
   
	public void openHealthDetails(View v) {
//		String healthTheme = null;
//		
//		Boolean previouslyAccessedFlag = false;
//		
//		try {
//			Dao<HealthSelectRecorded, Integer> hsrDao = getHelper().getHealthSelectRecordedDao();
//			List<HealthSelectRecorded> hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("theme", healthTheme).query();			
//			Iterator<HealthSelectRecorded> iter = hsrList.iterator();
//			while (iter.hasNext()) {
//				HealthSelectRecorded hsr = iter.next();
//				previouslyAccessedFlag = true;
//			}
//		} catch (SQLException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//		
//		
//		Intent i;
//		if (previouslyAccessedFlag == true) {
//			i = new Intent(HealthOverviewActivity.this, HealthDetailsActivity.class);
//		} else {
//			i = new Intent(HealthOverviewActivity.this, HealthOverviewRecordActivity.class);
//		}
		
		String healthTheme = (String) v.getTag();
		Intent i = new Intent(HealthOverviewActivity.this, HealthDetailsActivity.class);
		Bundle b = new Bundle();
		b.putString("healthTheme",healthTheme);
		b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
		i.putExtras(b);
		startActivity(i);
	}
	
	// TODO: remove from XML, finish me
//	public void setupUIElements() {
//		List <HealthTheme> themes = ModelHelper.getHealthThemes(context);
//		for (HealthTheme ht : themes) {
//			
//		}
//		((TextView)findViewById(R.id.health_HIV_title_field)).setText("HIV");
//		((ImageView)findViewById(R.id.health_HIV_button_img)).setTag("HIV");
//		((ImageView)findViewById(R.id.health_HIV_button)).setTag("HIV");
//	}
	
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
		try {
			Dao<HealthTopicAccessed, Integer> htaDao = getHelper().getHealthTopicAccessedDao();
			topicsAccessed = htaDao.queryBuilder().where().eq("hh_id",hhId).and().isNotNull("end_time").query();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// for each theme (ie button)
		for (int i = 0; i < imgViewList.size(); i++) {
			List<HealthTopic> healthTopics = new ArrayList<HealthTopic>();			
			// pull all topics related to that theme
			try {
				Dao<HealthTopic, Integer> topicDao = getHelper().getHealthTopicsDao();
				healthTopics = topicDao.queryBuilder().where().eq("theme",imgViewList.get(i).getTag()).query();
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

