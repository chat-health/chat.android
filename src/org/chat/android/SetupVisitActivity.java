package org.chat.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.chat.android.models.Household;
import org.chat.android.models.Service;
import org.chat.android.models.Worker;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SetupVisitActivity extends Activity {
	// since we aren't OrmLiteBaseActivity or BaseActivity we can't use getHelper()
    // so we use OpenHelperManager
    private DatabaseHelper databaseHelper = null;
	
	private String workerName;
	private String role;
	private double latitude = 0.0;
    private double longitude = 0.0;
	
	GPSTracker gps;
	
	private Spinner visitTypeSpinner;
	private Spinner householdSpinner;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Context context = getApplicationContext();
		
		Bundle b = getIntent().getExtras();
		workerName = b.getString("workerName");
		role = b.getString("role");

		setContentView(R.layout.activity_setup_visit);
		
		// visit type selection spinner - TODO: how can we put these if conditions in the strings file?
		visitTypeSpinner = (Spinner) findViewById(R.id.visit_type_spinner);
		ArrayAdapter<CharSequence> typeArrayAdapter;
		String[] roleArray = getResources().getStringArray(R.array.role_array);
		if (role.equals(roleArray[0])) {
			typeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.volunteer_visit_type_array, R.layout.login_spinner_item);
		} else if (role.equals(roleArray[1])) {
			typeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.counsellor_visit_type_array, R.layout.login_spinner_item);
		} else {
			// TODO: expand? Also add proper error here
			typeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.volunteer_visit_type_array, R.layout.login_spinner_item);
			BaseActivity.toastHelper(this, "Role is undefined");
		}
		typeArrayAdapter.setDropDownViewResource(R.layout.login_spinner_item);
		visitTypeSpinner.setAdapter(typeArrayAdapter);
		
		// household selections spinner
		householdSpinner = (Spinner) findViewById(R.id.household_spinner);
		ArrayList<String> householdNames = new ArrayList<String>();
		
		// for volunteers
		if (role.equals(roleArray[0])) {
			int workerId = ModelHelper.getWorkerForUsername(getHelper(), workerName).getId();
			List<Household> hList = ModelHelper.getHouseholdsForWorkerId(getHelper(), workerId);
			for (Household h : hList) {
				householdNames.add(h.getHhName());
			}
		}
		// for lay counsellors
		else if (role.equals(roleArray[1])) {
			List<Household> hList = ModelHelper.getAllHouseholds(getHelper());
			for (Household h : hList) {
				householdNames.add(h.getHhName());
			}
		} else {
			Log.e("SetupVisitActivity", "Missing role");
		}

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.login_spinner_item, householdNames);
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
					confirmGPS();
				}
			});
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
    
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
	
	private void confirmGPS() {
		if (latitude == 0.0 && longitude == 0.0) {
			String msg = getResources().getString(getResources().getIdentifier("warning_text", "string", getPackageName()));				// GET THIS REDONE WITH NEW TRANSLATIONS
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("GPS has not been recorded. Start visit anyways?")
	    	       .setCancelable(false)
	    	       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   startVisit();
	    	           }
	    	       })
	    	       .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                dialog.cancel();
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	alert.show();
		} else {
			startVisit();
		}
	}
	
	private void startVisit() {
		Intent myIntent = new Intent(SetupVisitActivity.this, HomeActivity.class);
		Bundle b = new Bundle();			
		b.putString("hhName",householdSpinner.getSelectedItem().toString());
		b.putString("workerName", workerName);
		b.putString("role", role);
		
		b.putString("type",visitTypeSpinner.getSelectedItem().toString());
		b.putDouble("lat", latitude);
		b.putDouble("lon", longitude);
		myIntent.putExtras(b);
		startActivity(myIntent);
		finish();
	}
	
	public void getGPSLocation() {
		// it's possible that this should be done on a different thread - is this an example of 'working on the UI thread'? Maybe disable the Start new visit button
		String msg = getResources().getString(getResources().getIdentifier("determining_location_text", "string", getPackageName()));
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        // create class object
        gps = new GPSTracker(SetupVisitActivity.this);

        // check if GPS enabled      
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            String msgCurLoc = getResources().getString(getResources().getIdentifier("current_loc_text", "string", getPackageName()));
            BaseActivity.toastHelper(this, msgCurLoc + " - \nLat: " + latitude + "\nLong: " + longitude);
            
        } else{
            // can't determine location because GPS or Network is not enabled
            gps.showSettingsAlert();
        }
	}

}
