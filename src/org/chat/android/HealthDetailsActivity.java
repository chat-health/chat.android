package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.Attendance;
import org.chat.android.models.HealthPage;
import org.chat.android.models.HealthTopic;
import org.chat.android.models.HealthTopicAccessed;
import org.chat.android.models.Household;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HealthDetailsActivity extends Activity {
	Context context;
	int hhId = 0;
	int visitId = 0;
	String healthTheme = null;
	Boolean largeTopicScreen = false;
	String[] themesArray;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        
		Bundle b = getIntent().getExtras();
		hhId = b.getInt("hhId");
		visitId = b.getInt("visitId");
		healthTheme = b.getString("healthTheme");
		themesArray = getResources().getStringArray(R.array.themes_array);
		
		// sloppy/hacky way of choosing whether the view is going to have 4 or 6 topics (disease and nutrition have 6, so largeTopicScreen == true)
		if (healthTheme.equals(themesArray[1]) || healthTheme.equals(themesArray[2])) {
			largeTopicScreen = true;
			setContentView(R.layout.activity_health_details_large);
		} else {
			setContentView(R.layout.activity_health_details_small);
		}
		
		setupTopicButtons(healthTheme);
    }
    
//    public void onResume(Bundle savedInstanceState) {
//    	super.onCreate(savedInstanceState);
//    	setupTopicButtons(healthTheme);
//    }
    
    public void openHealthDelivery(View v) {
    	String topic = null;
        topic = (String) v.getTag();
    	
    	Intent i = new Intent(HealthDetailsActivity.this, HealthDeliveryActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("hhId",hhId);
    	b.putInt("visitId",visitId);
    	b.putString("healthTheme",healthTheme);
    	b.putString("topic",topic);
    	i.putExtras(b);
    	finish();
    	startActivity(i);	
    }
    
    public void setupTopicButtons(String healthTheme) {
		// set the topics based on the health theme that was passed into this activity
    	// these get put in ArrayLists so that they can easily be referred in conditional statements without having to refer to the actual string values (which could change)
    	List<TextView> topicTitle = new ArrayList<TextView>();
    	topicTitle.add((TextView) findViewById(R.id.health_topic1_title_field));
    	topicTitle.add((TextView) findViewById(R.id.health_topic2_title_field));
    	topicTitle.add((TextView) findViewById(R.id.health_topic3_title_field));
    	topicTitle.add((TextView) findViewById(R.id.health_topic4_title_field));
    	
    	List<View> divider = new ArrayList<View>();
    	divider.add((View) findViewById(R.id.health_topic1_divider));
    	divider.add((View) findViewById(R.id.health_topic2_divider));
    	divider.add((View) findViewById(R.id.health_topic3_divider));
    	divider.add((View) findViewById(R.id.health_topic4_divider));
    	
    	List<ImageView> imgView = new ArrayList<ImageView>();
    	imgView.add((ImageView) findViewById(R.id.health_topic1_button_img));
    	imgView.add((ImageView) findViewById(R.id.health_topic2_button_img));
    	imgView.add((ImageView) findViewById(R.id.health_topic3_button_img));
    	imgView.add((ImageView) findViewById(R.id.health_topic4_button_img));
    	
    	List<ImageButton> imgBtn = new ArrayList<ImageButton>();
    	imgBtn.add((ImageButton) findViewById(R.id.health_topic1_button));
    	imgBtn.add((ImageButton) findViewById(R.id.health_topic2_button));
    	imgBtn.add((ImageButton) findViewById(R.id.health_topic3_button));
    	imgBtn.add((ImageButton) findViewById(R.id.health_topic4_button));
    	
    	// if we're using the large layout, we need the additional elements
    	if (largeTopicScreen == true) {
    		topicTitle.add((TextView) findViewById(R.id.health_topic5_title_field));
        	topicTitle.add((TextView) findViewById(R.id.health_topic6_title_field));
        	divider.add((View) findViewById(R.id.health_topic5_divider));
        	divider.add((View) findViewById(R.id.health_topic6_divider));
        	imgView.add((ImageView) findViewById(R.id.health_topic5_button_img));
        	imgView.add((ImageView) findViewById(R.id.health_topic6_button_img));
        	imgBtn.add((ImageButton) findViewById(R.id.health_topic5_button));
        	imgBtn.add((ImageButton) findViewById(R.id.health_topic6_button));
    	}
    	
    	String[] topicArray = null;
    	int colorRef = 0;
    	
    	// setup up the titles for each of the topics (UI), from strings_health arrays
		if (healthTheme.equals(themesArray[0])) {
			topicArray = getResources().getStringArray(R.array.theme1_array);					// TODO update to pull from DB instead of values file
			colorRef = getResources().getColor(getResources().getIdentifier("theme1", "color", getPackageName()));							// yes, this is seriously how you have to reference it :/   I heart you Android
			for (int i = 0; i < topicArray.length; i++) {
				topicTitle.get(i).setTextColor(colorRef);
				divider.get(i).setBackgroundColor(colorRef);				
				topicTitle.get(i).setText(topicArray[i]);
				imgView.get(i).setTag(topicArray[i]);
				imgBtn.get(i).setTag(topicArray[i]);
				imgBtn.get(i).setImageResource(R.drawable.hivgobutton);
			}
		} else if (healthTheme.equals(themesArray[1])) {
			topicArray = getResources().getStringArray(R.array.theme2_array);
			colorRef = getResources().getColor(getResources().getIdentifier("theme2", "color", getPackageName()));
			for (int i = 0; i < topicArray.length; i++) {
				topicTitle.get(i).setTextColor(colorRef);
				divider.get(i).setBackgroundColor(colorRef);
				topicTitle.get(i).setText(topicArray[i]);
				imgView.get(i).setTag(topicArray[i]);
				imgBtn.get(i).setTag(topicArray[i]);
				imgBtn.get(i).setImageResource(R.drawable.childhooddiseasesgobutton);
			}
		} else if (healthTheme.equals(themesArray[2])) {
			topicArray = getResources().getStringArray(R.array.theme3_array);
			colorRef = getResources().getColor(getResources().getIdentifier("theme3", "color", getPackageName()));			
			for (int i = 0; i < topicArray.length; i++) {
				topicTitle.get(i).setTextColor(colorRef);
				divider.get(i).setBackgroundColor(colorRef);				
				topicTitle.get(i).setText(topicArray[i]);
				imgView.get(i).setTag(topicArray[i]);
				imgBtn.get(i).setTag(topicArray[i]);
				imgBtn.get(i).setImageResource(R.drawable.nutritiongobutton);
			}
		} else if (healthTheme.equals(themesArray[3])) {
			topicArray = getResources().getStringArray(R.array.theme4_array);
			colorRef = getResources().getColor(getResources().getIdentifier("theme4", "color", getPackageName()));
			for (int i = 0; i < topicArray.length; i++) {
				topicTitle.get(i).setTextColor(colorRef);
				divider.get(i).setBackgroundColor(colorRef);				
				topicTitle.get(i).setText(topicArray[i]);
				imgView.get(i).setTag(topicArray[i]);
				imgBtn.get(i).setTag(topicArray[i]);
				imgBtn.get(i).setImageResource(R.drawable.developmentgobutton);
			}
		} else {
			Log.e("Error, healthTopic is set to: ",healthTheme);
		}
		

		// **grey out the buttons that have already been accessed**
		
		// pull all of the topics accessed for this household
		List<HealthTopicAccessed> topicsAccessed = new ArrayList<HealthTopicAccessed>();
		Dao<HealthTopicAccessed, Integer> htaDao;		
		DatabaseHelper htaDbHelper = new DatabaseHelper(context);
		try {
			htaDao = htaDbHelper.getHealthTopicsAccessed();
			List<HealthTopicAccessed> htaList = htaDao.queryBuilder().where().eq("hh_id",hhId).query();
			Iterator<HealthTopicAccessed> iter = htaList.iterator();
			while (iter.hasNext()) {
				HealthTopicAccessed hta = iter.next();
				topicsAccessed.add(hta);
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// for each health topic that has been accessed by this household
		for (HealthTopicAccessed hta : topicsAccessed) {
			// for each button on this page
			// for (ImageView iv : imgView) {
			for (int i = 0; i < imgView.size(); i++) {
				// if the button's tag is the topic accessed (ie if this button should be greyed out)
				if (imgView.get(i).getTag().equals(hta.getTopicName())) {
					// Log.d("The result is: ", (String) imgView.get(i).getTag());
					imgView.get(i).setAlpha(90);
					// imgBtn.get(i).setAlpha(90);
					// topicTitle.get(i).setTextColor(Color.argb(90, 255, 0, 0));
					// divider.get(i).setAlpha(90);
				}
			}			
		}
	
    }

}
