package org.chat.android;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ServiceDeliveryActivity extends Activity {
	int visitId = 0;
	int hhId = 0;
	String serviceCategory;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		serviceCategory = b.getString("serviceCategory");
		
		setContentView(R.layout.activity_service_delivery);
        
//		Fragment f = new ServiceDetailsFragment();
//		f.setArguments( getIntent().getExtras() );
	        
		// confirm service delivered button - open for next revision
   		Button serviceDeliveredBtn = (Button) findViewById(R.id.service_details_button);
   		serviceDeliveredBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),"Service(s) marked as delivered to client(s)",Toast.LENGTH_LONG).show();
				finish();				// lolololol this can't be a good idea
			}
	    });           
    }
   
}
