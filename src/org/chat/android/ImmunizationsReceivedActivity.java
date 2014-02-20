package org.chat.android;

import static org.chat.android.R.id.vaccine_age1;
import static org.chat.android.R.id.vaccine_date_select1;
import static org.chat.android.R.id.vaccine_name1;
import static org.chat.android.R.id.vaccine_age2;
import static org.chat.android.R.id.vaccine_date_select2;
import static org.chat.android.R.id.vaccine_name2;

import java.util.List;

import org.chat.android.models.Client;
import org.chat.android.models.Vaccine;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ImmunizationsReceivedActivity extends BaseActivity {
	private int visitId = 0;
	private int hhId = 0;
	private int clientId = 0;
	Client client = null;
	TextView vAge1 = null;
    TextView vName1 = null;
    Button dateBtn1 = null;
    TextView vAge2 = null;
    TextView vName2 = null;
    Button dateBtn2 = null;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_immunizations_received);
        
        Button dateBtn1 = (Button)findViewById(vaccine_date_select1);
        Button dateBtn2 = (Button)findViewById(vaccine_date_select2);
        
		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		clientId = b.getInt("clientId");
		
		client = ModelHelper.getClientForId(context, clientId);
		
		TextView tv = (TextView) findViewById(R.id.immunization_title_field);
		tv.setText("Which vaccines has " + client.getFirstName() +  " " + client.getLastName() + " received?");
		
		populateVaccineList();
    }
    
    private void populateVaccineList() {
    	List<Vaccine> vList = ModelHelper.getVaccinesForAge(context, client.getAge());
    	
//        TextView vAge = null;
//        TextView vName = null;
        //Button dateBtn = (Button)findViewById(vaccine_date_select);
    	vAge1 = (TextView)findViewById(vaccine_age1);
        vAge1.setText(vList.get(0).getDisplayAge());
        vName1 = (TextView)findViewById(vaccine_name1);
        vName1.setText(vList.get(0).getLongName());
        vAge2 = (TextView)findViewById(vaccine_age2);
        vAge2.setText(vList.get(1).getDisplayAge());
        vName2 = (TextView)findViewById(vaccine_name2);
        vName2.setText(vList.get(1).getLongName());
    	
        
        
//    	ImmunizationAdapter iAdapter = null;
//    	Client client = ModelHelper.getClientForId(context, clientId);
//    	List<Vaccine> vList = ModelHelper.getVaccinesForAge(context, client.getAge());
//    	
//    	ListView lv = (ListView) findViewById(R.id.immunization_listview);
//		iAdapter = new ImmunizationAdapter(context, android.R.layout.simple_list_item_1, vList);
//	    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//	    lv.setAdapter(iAdapter);	
    }
    
    public void completeImmunizationReceived(View v) {
    	Boolean missingVaccineFlag = true;			// TODO: make this check if there are necessary vaccines that have not yet been administered
    	if (missingVaccineFlag == true) {
        	Intent i = new Intent(ImmunizationsReceivedActivity.this, ImmunizationsSummaryActivity.class);
        	Bundle b = new Bundle();
        	b.putInt("visitId",visitId);
        	b.putInt("hhId",hhId);
        	b.putInt("clientId",clientId);
        	i.putExtras(b);
        	startActivity(i); 
    	} else {
    		// this will put us back on the CHA Overview screen
    		finish();
    	}
    }
    
    public void showDatePickerDialog(View v) {
    	DialogFragment newFragment = new DatePickerFragment(v);
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
