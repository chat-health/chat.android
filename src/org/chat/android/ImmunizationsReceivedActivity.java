package org.chat.android;

import java.util.List;

import org.chat.android.models.Client;
import org.chat.android.models.Vaccine;

import android.os.Bundle;
import android.widget.ListView;

public class ImmunizationsReceivedActivity extends BaseActivity {
	private int visitId = 0;
	private int hhId = 0;
	private int clientId = 0;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_immunizations_received);
        
		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		clientId = b.getInt("clientId");
		
		populateVaccineList();
    }
    
    private void populateVaccineList() {
    	ImmunizationAdapter iAdapter = null;
    	Client client = ModelHelper.getClientForId(context, clientId);
    	List<Vaccine> vList = ModelHelper.getVaccinesForAge(context, client.getAge());
    	
    	ListView lv = (ListView) findViewById(R.id.immunization_listview);
		iAdapter = new ImmunizationAdapter(context, android.R.layout.simple_list_item_1, vList);
	    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    lv.setAdapter(iAdapter);	
    }
}
