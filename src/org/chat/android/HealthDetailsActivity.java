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
        
        String healthTopic = null;
		Bundle b = getIntent().getExtras();
		healthTopic = b.getString("healthTopic");
		
		setupSubtopicButtons(healthTopic);
    }
    
    public void openHealthDelivery(View v) {
    	String subtopic = null;
        subtopic = (String) v.getTag();
    	
    	Intent i = new Intent(HealthDetailsActivity.this, HealthDeliveryActivity.class);
    	Bundle b = new Bundle();
    	b.putString("subtopic",subtopic);
    	i.putExtras(b);
    	startActivity(i);	
    }
    
    public void setupSubtopicButtons(String healthTopic) {
		// set the subtopics based on the healthTopic that was passed into this activity
    	// these get put in ArrayLists so that they can easily be referred in conditional statements without having to refer to the actual string values (which could change)
    	List<TextView> subTitle = new ArrayList<TextView>();
    	subTitle.add((TextView) findViewById(R.id.health_sub1_title_field));
    	subTitle.add((TextView) findViewById(R.id.health_sub2_title_field));
    	subTitle.add((TextView) findViewById(R.id.health_sub3_title_field));
    	subTitle.add((TextView) findViewById(R.id.health_sub4_title_field));
    	
    	List<View> divider = new ArrayList<View>();
    	divider.add((View) findViewById(R.id.health_sub1_divider));
    	divider.add((View) findViewById(R.id.health_sub2_divider));
    	divider.add((View) findViewById(R.id.health_sub3_divider));
    	divider.add((View) findViewById(R.id.health_sub4_divider));
    	
    	List<ImageView> imgView = new ArrayList<ImageView>();
    	imgView.add((ImageView) findViewById(R.id.health_sub1_button_img));
    	imgView.add((ImageView) findViewById(R.id.health_sub2_button_img));
    	imgView.add((ImageView) findViewById(R.id.health_sub3_button_img));
    	imgView.add((ImageView) findViewById(R.id.health_sub4_button_img));
    	
    	List<ImageButton> imgBtn = new ArrayList<ImageButton>();
    	imgBtn.add((ImageButton) findViewById(R.id.health_sub1_button));
    	imgBtn.add((ImageButton) findViewById(R.id.health_sub2_button));
    	imgBtn.add((ImageButton) findViewById(R.id.health_sub3_button));
    	imgBtn.add((ImageButton) findViewById(R.id.health_sub4_button));    	
    	
    	String[] healthTopicArray = getResources().getStringArray(R.array.topics_array);
    	String[] subTopicArray = null;
    	String colorName = null;
    	int colorRef = 0;
    	
    	// setup up the titles for each of the subtopics (UI), from strings_health arrays
		if (healthTopic.equals(healthTopicArray[0])) {
			subTopicArray = getResources().getStringArray(R.array.topic1_array);
			colorName = healthTopicArray[0];
			// this awkwardness because xml can't deal with uppercase :/
			if (colorName.equals("HIV"))
				colorName = "hiv";
			colorRef = getResources().getColor(getResources().getIdentifier(colorName, "color", getPackageName()));							// yes, this is seriously how you have to reference it :/   I heart you Android
			for (int i = 0; i < subTopicArray.length; i++) {
				subTitle.get(i).setTextColor(colorRef);
				divider.get(i).setBackgroundColor(colorRef);				
				subTitle.get(i).setText(subTopicArray[i]);
				imgView.get(i).setTag(subTopicArray[i]);
				imgBtn.get(i).setTag(subTopicArray[i]);
				imgBtn.get(i).setImageResource(R.drawable.hivgobutton);
			}
		} else if (healthTopic.equals(healthTopicArray[1])) {
			subTopicArray = getResources().getStringArray(R.array.topic2_array);
			colorName = healthTopicArray[1];
			colorRef = getResources().getColor(getResources().getIdentifier(colorName, "color", getPackageName()));
			for (int i = 0; i < subTopicArray.length; i++) {
				subTitle.get(i).setTextColor(colorRef);
				divider.get(i).setBackgroundColor(colorRef);
				subTitle.get(i).setText(subTopicArray[i]);
				imgView.get(i).setTag(subTopicArray[i]);
				imgBtn.get(i).setTag(subTopicArray[i]);
				imgBtn.get(i).setImageResource(R.drawable.childhooddiseasesgobutton);
			}
		} else if (healthTopic.equals(healthTopicArray[2])) {
			subTopicArray = getResources().getStringArray(R.array.topic3_array);
			colorName = healthTopicArray[2];
			colorRef = getResources().getColor(getResources().getIdentifier(colorName, "color", getPackageName()));			
			for (int i = 0; i < subTopicArray.length; i++) {
				subTitle.get(i).setTextColor(colorRef);
				divider.get(i).setBackgroundColor(colorRef);				
				subTitle.get(i).setText(subTopicArray[i]);
				imgView.get(i).setTag(subTopicArray[i]);
				imgBtn.get(i).setTag(subTopicArray[i]);
				imgBtn.get(i).setImageResource(R.drawable.nutritiongobutton);
			}
		} else if (healthTopic.equals(healthTopicArray[3])) {
			subTopicArray = getResources().getStringArray(R.array.topic4_array);
			colorName = healthTopicArray[3];
			colorRef = getResources().getColor(getResources().getIdentifier(colorName, "color", getPackageName()));
			for (int i = 0; i < subTopicArray.length; i++) {
				subTitle.get(i).setTextColor(colorRef);
				divider.get(i).setBackgroundColor(colorRef);				
				subTitle.get(i).setText(subTopicArray[i]);
				imgView.get(i).setTag(subTopicArray[i]);
				imgBtn.get(i).setTag(subTopicArray[i]);
				imgBtn.get(i).setImageResource(R.drawable.developmentgobutton);
			}
		} else {
			Log.e("Error, healthTopic is set to: ",healthTopic);
		}
    }

}
