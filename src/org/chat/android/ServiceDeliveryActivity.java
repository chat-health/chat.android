package org.chat.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class ServiceDeliveryActivity extends Activity {
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		Bundle b = getIntent().getExtras();
		String serviceType = b.getString("serviceType");
		String[] staffName = b.getStringArray("attendanceArray");
		Fragment fragobj=new Fragment();
		fragobj.setArguments(b);
		// OK, the fun ends here - it's just so much easier with an ORM layer. TODO: get this implemented next week
		
        setContentView(R.layout.activity_service_delivery);
    }
   
}
