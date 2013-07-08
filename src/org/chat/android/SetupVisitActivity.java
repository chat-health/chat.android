package org.chat.android;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetupVisitActivity extends Activity {
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
		
		// new visit button
		findViewById(R.id.new_visit_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO do some checks to make sure dropdowns have ben selected 
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
}
