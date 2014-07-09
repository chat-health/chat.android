package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.chat.android.models.Service;

import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ServiceDetailsActivity extends BaseActivity {
	private int visitId = 0;
	private int hhId = 0;
	private List<Service> servicesList = new ArrayList<Service>();
	ServicesAdapter sAdapter = null;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        setContentView(R.layout.activity_service_details);

		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		int tag = b.getInt("serviceTag") - 1;
		
		// YUCKY HACK ALERT. WE'RE RUNNING OUT OF TIME, AND IT LOOKS LIKE WE WON'T BE DOING COUNSELLORS. THE BELOW ONLY WORKS FOR VOLUNTEERS, WAS AN EASY WAY TO DEAL WITH THE MULTIPLE LANGUAGES
		
		TextView tv = (TextView) findViewById(R.id.service_details_title_field);
		String[] serviceNames = getResources().getStringArray(R.array.volunteer_service_type_names);
		tv.setText(serviceNames[tag]);
		
		// grab list of services to show, based on the service subtype
		String[] serviceTags = getResources().getStringArray(R.array.volunteer_service_type_tags);
		populateServicesList(serviceTags[tag]);
		
		ListView lv = (ListView) findViewById(R.id.service_details_listview);
		sAdapter = new ServicesAdapter(context, android.R.layout.simple_list_item_multiple_choice, servicesList);
	    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    lv.setAdapter(sAdapter);         
    }
    
    // used to generate the list of services for serviceDelivery
    private void populateServicesList(String type) {
        Dao<Service, Integer> sDao;
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        try {
			sDao = dbHelper.getServicesDao();
			servicesList = sDao.queryBuilder().orderBy("en_name", true).where().eq("type",type).query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      	
    }
    
    public void selectServiceTargets(View v) {
    	ArrayList<String> serviceNames = sAdapter.getSelectedServices();
    	if (serviceNames.size() > 0) {
    		Intent i = new Intent(ServiceDetailsActivity.this, ServiceDeliveryActivity.class);
        	Bundle b = new Bundle();
        	b.putInt("visitId",visitId);
        	b.putInt("hhId",hhId);
        	b.putStringArrayList("serviceNames", serviceNames);
        	b.putBoolean("adInfoFlag",false);
        	i.putExtras(b);
        	startActivity(i);
    	} else {
    		BaseActivity.toastHelper(this, "You must select at least one service (or hit the back button to cancel)");
    	}
    }
    
    
}