package org.chat.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HealthDetailsActivity extends Activity {
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String healthTopic = null;
		Bundle b = getIntent().getExtras();
		healthTopic = b.getString("healthTopic");
		
		// if HIV, then set button texts and button tags etc.
		
		setContentView(R.layout.activity_health_details);
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
}
