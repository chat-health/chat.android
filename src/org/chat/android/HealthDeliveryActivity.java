package org.chat.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class HealthDeliveryActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String subTopic = null;
		Bundle b = getIntent().getExtras();
		subTopic = b.getString("subTopic");
		
		Toast.makeText(getApplicationContext(), "Subtopic: "+subTopic, Toast.LENGTH_SHORT).show();
		
		setContentView(R.layout.activity_health_delivery);
    }
}
