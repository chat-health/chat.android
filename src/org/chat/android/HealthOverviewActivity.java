package org.chat.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HealthOverviewActivity extends Activity {
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_health_overview);
    }
   
   public void openHealthDetails(View v) {
		String healthTopic = null;
		healthTopic = (String) v.getTag();
		
		Intent i = new Intent(HealthOverviewActivity.this, HealthDetailsActivity.class);
		Bundle b = new Bundle();
		b.putString("healthTopic",healthTopic);
		i.putExtras(b);
		startActivity(i);
	}	   
}



