package org.chat.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HealthDetailsActivity extends Activity {
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_details);
        
        String healthTopic = null;
		Bundle b = getIntent().getExtras();
		healthTopic = b.getString("healthTopic");
		
		setupSubTopicButtons(healthTopic);
		
    }
    
    public void openHealthDelivery(View v) {
    	String subTopic = null;
        subTopic = (String) v.getTag();
    	
    	Intent i = new Intent(HealthDetailsActivity.this, HealthDeliveryActivity.class);
    	Bundle b = new Bundle();
    	b.putString("subTopic",subTopic);
    	i.putExtras(b);
    	startActivity(i);	
    }
    
    public void setupSubTopicButtons(String healthTopic) {
    	String[] healthTopicArray = getResources().getStringArray(R.array.topics_array);
    	String[] subTopicArray = null;
    	
		// set the subtopics based on the healthTopic that was passed into this activity
    	List<TextView> subTitle = new ArrayList<TextView>();
    	subTitle.add((TextView) findViewById(R.id.health_sub1_title_field));
    	subTitle.add((TextView) findViewById(R.id.health_sub2_title_field));
    	subTitle.add((TextView) findViewById(R.id.health_sub3_title_field));
    	subTitle.add((TextView) findViewById(R.id.health_sub4_title_field));
    	
    	// setup up the titles for each of the subtopics (UI), from strings_health arrays
		if (healthTopic.equals(healthTopicArray[0])) {
			subTopicArray = getResources().getStringArray(R.array.topic1_array);
			for (int i = 0; i < subTopicArray.length; i++) {
				subTitle.get(i).setText(subTopicArray[i]);
			}
		} else if (healthTopic.equals(healthTopicArray[1])) {
			subTopicArray = getResources().getStringArray(R.array.topic2_array);
			for (int i = 0; i < subTopicArray.length; i++) {
				subTitle.get(i).setText(subTopicArray[i]);
			}
		} else if (healthTopic.equals(healthTopicArray[2])) {
			subTopicArray = getResources().getStringArray(R.array.topic3_array);
			for (int i = 0; i < subTopicArray.length; i++) {
				subTitle.get(i).setText(subTopicArray[i]);
			}
		} else if (healthTopic.equals(healthTopicArray[3])) {
			subTopicArray = getResources().getStringArray(R.array.topic4_array);
			for (int i = 0; i < subTopicArray.length; i++) {
				subTitle.get(i).setText(subTopicArray[i]);
			}
		} else {
			Log.e("healthTopic is set to: ",healthTopic);
		}
    }
}
