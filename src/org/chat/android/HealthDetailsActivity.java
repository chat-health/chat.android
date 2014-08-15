package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.HealthTopic;
import org.chat.android.models.HealthTopicAccessed;

import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthDetailsActivity extends BaseActivity {
	Context context;
	int hhId = 0;
	int visitId = 0;
	String healthTheme = null;
	Boolean largeTopicScreen = false;
	String[] themesArray;
	
	List<ImageView> imgView = null;
	List<ImageView> checkmark = null;
	
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
		
		// create UI elements
		setupTopicButtons(healthTheme);
		// grey out elements based on topic completion
		updateUIElements();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	//Toast.makeText(getApplicationContext(),"onResume triggered",Toast.LENGTH_SHORT).show();
		updateUIElements();
    }
    
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
    	
    	imgView = new ArrayList<ImageView>();
    	imgView.add((ImageView) findViewById(R.id.health_topic1_button_img));
    	imgView.add((ImageView) findViewById(R.id.health_topic2_button_img));
    	imgView.add((ImageView) findViewById(R.id.health_topic3_button_img));
    	imgView.add((ImageView) findViewById(R.id.health_topic4_button_img));
    	
    	checkmark = new ArrayList<ImageView>();
    	checkmark.add((ImageView) findViewById(R.id.health_topic1_checkmark));
    	checkmark.add((ImageView) findViewById(R.id.health_topic2_checkmark));
    	checkmark.add((ImageView) findViewById(R.id.health_topic3_checkmark));
    	checkmark.add((ImageView) findViewById(R.id.health_topic4_checkmark));
    	
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
        	checkmark.add((ImageView) findViewById(R.id.health_topic5_checkmark));
        	checkmark.add((ImageView) findViewById(R.id.health_topic6_checkmark));
        	imgBtn.add((ImageButton) findViewById(R.id.health_topic5_button));
        	imgBtn.add((ImageButton) findViewById(R.id.health_topic6_button));
    	}
    	
    	List<HealthTopic> topicList = ModelHelper.getTopicsForThemeName(getHelper(), healthTheme);
		String color = ModelHelper.getThemeForName(getHelper(), healthTheme).getColor();
		int colorRef = Color.parseColor(color);
		int imageRef = 0;
    	
    	// setup up the drawable depending on the theme - TODO: maybe move the resource reference to the DB
		if (healthTheme.equals(themesArray[0])) {
			imageRef = R.drawable.hivgobutton;
		} else if (healthTheme.equals(themesArray[1])) {
			imageRef = R.drawable.childhooddiseasesgobutton;
		} else if (healthTheme.equals(themesArray[2])) {
			imageRef = R.drawable.nutritiongobutton;
		} else if (healthTheme.equals(themesArray[3])) {
			imageRef = R.drawable.developmentgobutton;
		} else {
			Log.e("Error, can't find drawable for theme: ",healthTheme);
		}
		
		for (int i = 0; i < topicList.size(); i++) {
			topicTitle.get(i).setTextColor(colorRef);
			divider.get(i).setBackgroundColor(colorRef);				
			topicTitle.get(i).setText(topicList.get(i).getName());
			
			int screenshotId = getResources().getIdentifier(topicList.get(i).getScreenshot(), "drawable", context.getPackageName());
			imgView.get(i).setImageResource(screenshotId);
			
			imgView.get(i).setTag(topicList.get(i).getName());
			imgBtn.get(i).setTag(topicList.get(i).getName());
			imgBtn.get(i).setImageResource(imageRef);
		}
    }

	// grey out the buttons that have already been accessed
    public void updateUIElements() {
		// pull all of the completed topics accessed for this household
		List<HealthTopicAccessed> topicsAccessed = new ArrayList<HealthTopicAccessed>();
		try {
			Dao<HealthTopicAccessed, Integer> htaDao = getHelper().getHealthTopicAccessedDao();
			topicsAccessed = htaDao.queryBuilder().where().eq("hh_id",hhId).and().isNotNull("end_time").query();
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
					imgView.get(i).setAlpha(90);
					checkmark.get(i).setVisibility(View.VISIBLE);
					// imgBtn.get(i).setAlpha(90);
					// topicTitle.get(i).setTextColor(Color.argb(90, 255, 0, 0));
					// divider.get(i).setAlpha(90);
				}
			}			
		}
    }

}
