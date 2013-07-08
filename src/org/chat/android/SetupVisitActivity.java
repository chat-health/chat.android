package org.chat.android;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SetupVisitActivity extends Activity {
	GPSTracker gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup_visit);
		
		// visit type selection
		Spinner visitTypeSpinner = (Spinner) findViewById(R.id.visit_type_spinner);
		ArrayList<String> visittypelist = new ArrayList<String>();
		// TODO: take role passed in from login activity, add different visit types based on that
		visittypelist.add("Home visit");
		visittypelist.add("School visit");
		ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, visittypelist);
		visitTypeSpinner.setAdapter(typeArrayAdapter);
		
		// household selection
		Spinner householdSpinner = (Spinner) findViewById(R.id.household_spinner);
		ArrayList<String> householdList = new ArrayList<String>();
		// TODO: DUMMY DATA. Instead, pull these from DB, based on user_name and role passed from login activity 
		householdList.add("Dlamini Thandiwe");
		householdList.add("Dlamini Nokuthula Princess");
		householdList.add("Qwabe Wiennfred Thlolakele");
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, householdList);
		householdSpinner.setAdapter(arrayAdapter);
		
		// gps location button
		findViewById(R.id.get_gps_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {	
						getGPSLocation();
					}
				});
		
		// new visit button
		findViewById(R.id.new_visit_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO do some checks to make sure dropdowns have been selected 
						Intent myIntent = new Intent(SetupVisitActivity.this, HomeActivity.class);
						SetupVisitActivity.this.startActivity(myIntent);
					}
				});
		
		
		// set up new visit object, save to DB... also pass role and household
		createNewVisit();
	}

	
	public void createNewVisit() {
		// create new visit object
		// populate new visit object with name, date, role, household, etc
		// save to ORM layer
	}
	
	public void getGPSLocation() {
		Toast.makeText( getApplicationContext(), "Determining location...", Toast.LENGTH_SHORT).show();
        // create class object
        gps = new GPSTracker(SetupVisitActivity.this);

        // check if GPS enabled      
        if(gps.canGetLocation()){
          
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
          
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();  
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
	}
	

}
