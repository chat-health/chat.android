package org.chat.android;

import java.util.List;

import org.chat.android.models.Client;
import org.chat.android.models.Vaccine;
import org.chat.android.models.VaccineRecorded;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ImmunizationsSummaryActivity extends BaseActivity {
	private int visitId = 0;
	private int hhId = 0;
	private int clientId = 0;
	private Client client = null;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_immunization_summary);
        
        Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		clientId = b.getInt("clientId");
		client = ModelHelper.getClientForId(context, clientId);
		String missingVaccines = getMissingVaccines(client);
		
		TextView tv = (TextView) findViewById(R.id.missing_vaccines_box);
		tv.setText(client.getFirstName() + " " + client.getLastName() + " is missing the following vaccines: " + missingVaccines);
    }
    
    private String getMissingVaccines(Client client) {
    	String mv = "";
    	List<Vaccine> vList = ModelHelper.getVaccinesForAge(context, client.getAge());
    	
    	for (Vaccine vaccine : vList) {
    		// will return null if it doesn't exist
    		VaccineRecorded vr = ModelHelper.getVaccineRecordedForClientIdAndVaccineId(context, client.getId(), vaccine.getId());
    		if (vr == null) {
    			mv += vaccine.getLongName();
    			mv += ", ";		
    		}
    	}
    	mv = mv.substring(0, mv.length() - 2);
    	
    	return mv;
    }
    
    public void completeImmunizationSummary(View v) {
    	// CHAAccessed object is updated as completed (ie end_time is added) in ImmunizationReceivedActivity
    	String msg = getResources().getString(getResources().getIdentifier("immunization_completed_text", "string", getPackageName()));
    	BaseActivity.toastHelper(this,msg +" " + client.getFirstName() + " " + client.getLastName() );
    	finish();
    }
}




