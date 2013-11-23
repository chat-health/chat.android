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
	        
		// confirm service delivered button
//   		Button serviceDeliveredBtn = (Button) findViewById(R.id.deliver_services_button);
//   		serviceDeliveredBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				selectServiceTargets();
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
    }
    
    public void selectServiceTargets(View v) {
    	
    	//getSelectedServices()
    	Toast.makeText(getApplicationContext(),"Service(s) marked as delivered to client(s)",Toast.LENGTH_LONG).show();
		//finish();
    }
    
    
}