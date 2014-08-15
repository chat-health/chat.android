package org.chat.android;

import java.util.ArrayList;
import java.util.List;

import org.chat.android.models.Client;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class CHASelectChildActivity extends BaseActivity {
	int visitId = 0;
	int hhId = 0;
	List<Client> clientsForHealthAssessment = new ArrayList<Client>();	
	CHASelectChildAdapter adapter = null;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	
    }
    
    protected void onResume() {
    	super.onResume();
    	
        context = getApplicationContext();
        setContentView(R.layout.activity_cha_select_child);
        
		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		
		// grab list of present clients to show, based on the attendance
		clientsForHealthAssessment = ModelHelper.getAttendingClientsForVisitIdUnderAge(getHelper(), visitId, 5);
		
		if (clientsForHealthAssessment.size() == 0) {
			String msg = getResources().getString(getResources().getIdentifier("select_child_no_child_text", "string", getPackageName()));
			BaseActivity.toastHelper(this, msg);
		}
		
		ListView lv = (ListView) findViewById(R.id.attending_children_listview);
		adapter = new CHASelectChildAdapter(context, android.R.layout.simple_list_item_1, clientsForHealthAssessment, visitId, hhId);
	    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    lv.setAdapter(adapter);	
    }
}
