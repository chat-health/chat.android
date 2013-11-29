package org.chat.android;

import android.app.Activity;
//import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HealthDeliveryActivity extends Activity {
	
	TextView paginationTextField = null;
	int pageCounter = 1;
	int lastPage = 4;				// this will be set dynamically in the future
	
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_health_delivery);
	    
	    paginationTextField = (TextView) findViewById(R.id.paginationTextField);
	    
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    ft.replace(R.id.your_placeholder, new FooFragment());
	    // or ft.add(R.id.your_placeholder, new FooFragment());
	    ft.commit();
	    
    }
	

	public void moveNext(View v) {
		if (pageCounter + 1 < lastPage) {
			updatePageCounter("next");
			
		    FragmentTransaction ft = getFragmentManager().beginTransaction();
		    ft.replace(R.id.your_placeholder, new BarFragment());
		    ft.commit();			
		} else {
			Toast.makeText(getApplicationContext(),"Reached last page",Toast.LENGTH_SHORT).show();
		}

	}
	
	public void moveBack(View v) {
		if (pageCounter - 1 > 0) {
			updatePageCounter("back");
		
		    FragmentTransaction ft = getFragmentManager().beginTransaction();
		    ft.replace(R.id.your_placeholder, new FooFragment());
		    ft.commit();
			// also use popBackStack() here?
		} else {
			Toast.makeText(getApplicationContext(),"Reached first page",Toast.LENGTH_SHORT).show();
		}
	}
	
	public void updatePageCounter(String m) {
		if (m.equals("next")) {
			pageCounter++;
			String p = pageCounter + "/4";
			paginationTextField.setText(p);	
		} else if (m.equals("back")) {
			pageCounter--;
			String p = pageCounter + "/4";
			paginationTextField.setText(p);
		} else {
			Toast.makeText(getApplicationContext(),"Reached first page",Toast.LENGTH_SHORT).show();			// switch me to an error log
		}

		

	}
}





//public class HealthDeliveryActivity extends Activity {
//
//  public void onCreate(Bundle savedInstanceState) {
//      super.onCreate(savedInstanceState);
//
//      String subTopic = null;
//		Bundle b = getIntent().getExtras();
//		subTopic = b.getString("subTopic");
//		
//		setContentView(R.layout.activity_health_delivery);
//  }
//}