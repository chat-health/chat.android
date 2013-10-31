package org.chat.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HealthDetailsActivity extends Activity {
	String healthTopic = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		healthTopic = b.getString("healthTopic");
		
		setContentView(R.layout.activity_health_details);
        
	        
		// confirm service delivered button - open for next revision
//   		Button serviceDeliveredBtn = (Button) findViewById(R.id.service_details_button);
//   		serviceDeliveredBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(),"Service(s) marked as delivered to client(s)",Toast.LENGTH_LONG).show();
//				finish();				// lolololol this can't be a good idea
//			}
//	    });           
    }
}
