package org.chat.android;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SetupVisitActivity extends Activity {
	
	private String userName;
	private String role;
	private double latitude;
    private double longitude;
	
	GPSTracker gps;
	
	private Spinner visitTypeSpinner;
	private Spinner householdSpinner;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b = getIntent().getExtras();
		userName = b.getString("userName");
		role = b.getString("role");

		setContentView(R.layout.activity_setup_visit);
		
		// visit type selection spinner - TODO: how can we put these if conditions in the strings file?
		visitTypeSpinner = (Spinner) findViewById(R.id.visit_type_spinner);
		ArrayAdapter<CharSequence> typeArrayAdapter;
		if (role.equals("Home Care Volunteer")) {
			typeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.volunteer_visit_type_array, android.R.layout.simple_spinner_item);
		} else if (role.equals("Lay Councelor")) {
			typeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.councelor_visit_type_array, android.R.layout.simple_spinner_item);
		} else {
			// TODO: fix this when we know what's going on
			typeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.volunteer_visit_type_array, android.R.layout.simple_spinner_item);
		}
		typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		visitTypeSpinner.setAdapter(typeArrayAdapter);
		
		// household selections spinner
		householdSpinner = (Spinner) findViewById(R.id.household_spinner);
		ArrayList<String> householdList = new ArrayList<String>();
		// TODO: DUMMY DATA. Instead, pull these from DB, based on userName and role passed from login activity 
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
						// TODO do some checks to make sure dropdowns have been selected, GPS is done						
						Intent myIntent = new Intent(SetupVisitActivity.this, HomeActivity.class);
						Bundle b = new Bundle();			
						b.putString("userName", userName);
						b.putString("role", role);
						b.putString("HHName",householdSpinner.getSelectedItem().toString());
						b.putString("type",visitTypeSpinner.getSelectedItem().toString());
						b.putDouble("lat", latitude);
						b.putDouble("lon", longitude);
						myIntent.putExtras(b);
						startActivity(myIntent);
						finish();
					}
				});
	}

	
	public void getGPSLocation() {
		// it's possible that this should be done on a different thread - is this an example of 'working on the UI thread'? Maybe disable the Start new visit button
		Toast.makeText( getApplicationContext(), "Determining location...", Toast.LENGTH_SHORT).show();
        // create class object
        gps = new GPSTracker(SetupVisitActivity.this);

        // check if GPS enabled      
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Toast.makeText(getApplicationContext(), "Current location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            
        } else{
            // can't determine location because GPS or Network is not enabled
            gps.showSettingsAlert();
        }
	}
	

}
