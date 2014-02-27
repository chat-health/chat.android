package org.chat.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.chat.android.models.Client;
import org.chat.android.models.Vaccine;
import org.chat.android.models.VaccineRecorded;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImmunizationsReceivedActivity extends BaseActivity {
	private int visitId = 0;
	private int hhId = 0;
	private int clientId = 0;
	Client client = null;
	List<Vaccine> vList = null;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_immunizations_received);
        
		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		clientId = b.getInt("clientId");
		
		client = ModelHelper.getClientForId(context, clientId);
	
//		TextView tv = (TextView) findViewById(R.id.immunization_title_field);
//		tv.setText("Which vaccines has " + client.getFirstName() +  " " + client.getLastName() + " received?");
		
		populateVaccineList();
    }
    
    // TODO: we need someway to update the UI when the fragment is complete
//    @Override
//    public void onResume() {
//    	super.onResume();
//    	Toast.makeText(getApplicationContext(),"onResume triggered",Toast.LENGTH_SHORT).show();
//    }
    
    private void populateVaccineList() {
    	List<LinearLayout> rows = new ArrayList<LinearLayout>();
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row1));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row2));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row3));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row4));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row5));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row6));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row7));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row8));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row9));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row10));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row11));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row12));
    	rows.add((LinearLayout) findViewById(R.id.vaccine_row13));

    	vList = ModelHelper.getVaccinesForAge(context, client.getAge());
    	
    	for (int i = 0; i < vList.size(); i++) {
    		Vaccine v = vList.get(i);
    		// make the headers visible - TODO, finish these, find a cleaner way to do this
    		// 14 months
    		if (v.getAge() >= 0.2692) {
    			TextView tv = (TextView) findViewById(R.id.age_header4);
    			tv.setVisibility(View.VISIBLE);
    		}
    		// 10 months
    		else if (v.getAge() >= 0.1923) {
    			TextView tv = (TextView) findViewById(R.id.age_header3);
    			tv.setVisibility(View.VISIBLE);
    		}
    		// 6 months
    		else if (v.getAge() >= 0.1154) {
    			TextView tv = (TextView) findViewById(R.id.age_header2);
    			tv.setVisibility(View.VISIBLE);
    		}
    		
    		// make the rows visible, and populate.

			//LinearLayout row = (LinearLayout) shortNames.get(i).getParent();
			LinearLayout row = rows.get(i);
			row.setVisibility(View.VISIBLE);
			ImageView flag = (ImageView)row.getChildAt(0);
			TextView shortName = (TextView)row.getChildAt(1);
			TextView longName = (TextView)row.getChildAt(2);
			Button dateBtn = (Button)row.getChildAt(3);
			
			shortName.setText(v.getShortName());
			longName.setText(v.getLongName());
			dateBtn.setTag(v.getId());
			
			VaccineRecorded vr = ModelHelper.getVaccineRecordedForClientIdAndVaccineId(context, client.getId(), v.getId());
			// if there is a vaccineRecorded object for this vaccine and this client - think about if there are multiple vaccineRecordeds. This seems to work, tho just by default - takes the most recent VR as the correct one, which is the behaviour that we want
			if (vr != null) {
				Date date = vr.getDate();
				dateBtn.setText(date.getYear() + "/" + date.getMonth() + "/" + date.getDay());
			} else {
				flag.setVisibility(View.VISIBLE);
				shortName.setTextColor(Color.parseColor("#EB2695"));
				longName.setTextColor(Color.parseColor("#EB2695"));
			}
    	}
    	
    }

    
    public void completeImmunizationReceived(View v) {
    	// check if there are missing immunizations
    	Boolean missingVaccineFlag = false;
    	for (Vaccine vaccine : vList) {
    		// will return null if it doesn't exist
    		VaccineRecorded vr = ModelHelper.getVaccineRecordedForClientIdAndVaccineId(context, client.getId(), vaccine.getId());
    		if (vr == null) {
    			missingVaccineFlag = true;
    		}
    	}
    	
    	// if there are missing immunizations, direct user to ImmunizationSummary page
    	if (missingVaccineFlag == true) {
        	Intent i = new Intent(ImmunizationsReceivedActivity.this, ImmunizationsSummaryActivity.class);
        	Bundle b = new Bundle();
        	b.putInt("visitId",visitId);
        	b.putInt("hhId",hhId);
        	b.putInt("clientId",clientId);
        	i.putExtras(b);
        	startActivity(i); 
    	} else {
    		// else put user back on the CHA Overview screen
    		finish();
    	}
    }
    
    public void showDatePickerDialog(View v) {
    	DialogFragment newFragment = new DatePickerFragment(v);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("clientId",clientId);
    	newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
