package org.chat.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
//import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HealthDeliveryActivity extends Activity {
	
	//List<HealthPage> pages = new ArrayList<View>();
	int pageCounter = 1;
	int lastPage = 4;				// this will be set dynamically in the future
	
	TextView paginationTextField = null;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_health_delivery);
        String subTopic = null;
		Bundle b = getIntent().getExtras();
		subTopic = b.getString("subTopic");
		
		// get the required pages for the subtopic
		//createPageArray();
		
	    paginationTextField = (TextView) findViewById(R.id.paginationTextField);
	       
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    ft.replace(R.id.placeholder, new Text1Fragment());
	    // or ft.add(R.id.placeholder, new FooFragment());
	    ft.commit();
    }
	

	public void moveNext(View v) {
		// check if this page is within bounds
		if (pageCounter + 1 < lastPage) {
			updatePageCounter("next");
			
		    FragmentTransaction ft = getFragmentManager().beginTransaction();
		    ft.replace(R.id.placeholder, new Select1Fragment());
		    ft.commit();			
		} else {
			Toast.makeText(getApplicationContext(),"Reached last page",Toast.LENGTH_SHORT).show();
		}

	}
	
	public void moveBack(View v) {
		// check if this page is within bounds
		if (pageCounter - 1 > 0) {
			updatePageCounter("back");
		
		    FragmentTransaction ft = getFragmentManager().beginTransaction();
		    ft.replace(R.id.placeholder, new Video1Fragment());
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
			Log.e("Incorrect parameter passed to updatePageCounter: ", m);
		}

		

	}
}