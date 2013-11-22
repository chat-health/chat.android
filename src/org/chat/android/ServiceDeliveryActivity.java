package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ServiceDeliveryActivity extends Activity {
	private int visitId = 0;
	private int hhId = 0;
	private List<Service> servicesList = new ArrayList<Service>();
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        setContentView(R.layout.activity_service_delivery);

		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		String type = b.getString("subtype");
		
		// grab list of services to show, based on the service subtype
		populateServiceList(type);
		
		ListView lv = (ListView) findViewById(R.id.service_delivery_listview);
		ServicesAdapter sAdapter = new ServicesAdapter(context, android.R.layout.simple_list_item_multiple_choice, servicesList);
	    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    lv.setAdapter(sAdapter);
//	        
//		// confirm service delivered button - open for next revision
//   		Button serviceDeliveredBtn = (Button) findViewById(R.id.service_details_button);
//   		serviceDeliveredBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(),"Service(s) marked as delivered to client(s)",Toast.LENGTH_LONG).show();
//				finish();				// lolololol this can't be a good idea
//			}
//	    });           
    }
    
    private void populateServiceList(String type) {
        Dao<Service, Integer> sDao;
        List<Service> allServices = new ArrayList<Service>();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        try {
			sDao = dbHelper.getServicesDao();
			allServices = sDao.query(sDao.queryBuilder().prepare());
        	for (Service s : allServices) {
    			if (s.getType().equals(type)) {
    				servicesList.add(s);
    			}
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      	
    	
    	// move categories to the DB?
//	    if (subtype.equals("B - Material Well Being")) {
//	    	serviceList = getResources().getStringArray(R.array.material_well_being_service_array);
//	    } else if (subtype.equals("C - Cognitive Well Being")) {
//	    	serviceList = getResources().getStringArray(R.array.cognitive_well_being_service_array);
//	    } else if (subtype.equals("D - Emotional Well Being")) {
//	    	serviceList = getResources().getStringArray(R.array.emotional_well_being_service_array);
//	    } else if (subtype.equals("E - Physical Well Being")) {
//	    	serviceList = getResources().getStringArray(R.array.physical_well_being_service_array);
//	    } else if (subtype.equals("S - Documents and Grants")) {
//	    	serviceList = getResources().getStringArray(R.array.documents_and_grants_service_array);
//	    } else if (subtype.equals("T - Change of Status")) {
//	    	serviceList = getResources().getStringArray(R.array.change_of_status_service_array);
//	    } else {
//	    	Toast.makeText(getApplicationContext(), "ERROR: Unknown service group", Toast.LENGTH_SHORT).show();
//	    	// TODO: set up proper error handling here
//	    	serviceList = getResources().getStringArray(R.array.material_well_being_service_array);
//	    }
    }
}