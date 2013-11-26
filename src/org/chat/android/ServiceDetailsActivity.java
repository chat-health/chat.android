package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.chat.models.Service;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ServiceDetailsActivity extends Activity {
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
		String type = b.getString("subtype");
		TextView tv = (TextView) findViewById(R.id.service_details_title_field);
		tv.setText(type);
		
		// grab list of services to show, based on the service subtype
		populateServiceList(type);
		
		ListView lv = (ListView) findViewById(R.id.service_details_listview);
		sAdapter = new ServicesAdapter(context, android.R.layout.simple_list_item_multiple_choice, servicesList);
	    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    lv.setAdapter(sAdapter);         
    }
    
    // used to generate the list of services for serviceDelivery
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
    	ArrayList<String> serviceNames = sAdapter.getSelectedServices();
    	Intent i = new Intent(ServiceDetailsActivity.this, ServiceDeliveryActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	b.putStringArrayList("serviceNames", serviceNames);
    	i.putExtras(b);
    	startActivity(i);
    }
    
    
}