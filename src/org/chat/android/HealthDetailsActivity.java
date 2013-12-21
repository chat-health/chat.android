package org.chat.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_details);
        
        String healthTheme = null;
		Bundle b = getIntent().getExtras();
		healthTheme = b.getString("healthTheme");
		
		setupTopicButtons(healthTheme);
    }
    
    public void openHealthDelivery(View v) {
    	String topic = null;
        topic = (String) v.getTag();
    	
    	Intent i = new Intent(HealthDetailsActivity.this, HealthDeliveryActivity.class);
    	Bundle b = new Bundle();
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
    	
    	String[] themesArray = getResources().getStringArray(R.array.themes_array);
    	String[] topicArray = null;
    	String colorName = null;
    	int colorRef = 0;
    	
    	// setup up the titles for each of the topics (UI), from strings_health arrays
		if (healthTheme.equals(themesArray[0])) {
			topicArray = getResources().getStringArray(R.array.theme1_array);					// TODO move me to the DB - best as health_theme table instead splitting it out into arrays here
			colorName = themesArray[0];
			// this awkwardness because xml can't deal with uppercase :/
			if (colorName.equals("HIV"))
				colorName = "hiv";
			colorRef = getResources().getColor(getResources().getIdentifier(colorName, "color", getPackageName()));							// yes, this is seriously how you have to reference it :/   I heart you Android
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
			colorName = themesArray[1];
			colorRef = getResources().getColor(getResources().getIdentifier(colorName, "color", getPackageName()));
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
			colorName = themesArray[2];
			colorRef = getResources().getColor(getResources().getIdentifier(colorName, "color", getPackageName()));			
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
			colorName = themesArray[3];
			colorRef = getResources().getColor(getResources().getIdentifier(colorName, "color", getPackageName()));
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
    }

}
