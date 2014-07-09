package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.chat.android.models.Service;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ServiceOtherActivity extends BaseActivity {
	private int visitId = 0;
	private int hhId = 0;
	private List<Service> servicesList = new ArrayList<Service>();
	List<EditText> entry = new ArrayList<EditText>();
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        setContentView(R.layout.activity_service_other);

		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		String role = b.getString("role");
		int tag = b.getInt("serviceTag") - 1;				// "Other"
		
		String[] serviceNames = null;
		serviceNames = getResources().getStringArray(R.array.volunteer_service_type_names);
		TextView typeTitle = (TextView) findViewById(R.id.service_other_title_field);
		typeTitle.setText(serviceNames[tag]);
		
    	List<TextView> title = new ArrayList<TextView>();
    	title.add((TextView) findViewById(R.id.other_tv_1));
    	title.add((TextView) findViewById(R.id.other_tv_2));
    	title.add((TextView) findViewById(R.id.other_tv_3));
    	title.add((TextView) findViewById(R.id.other_tv_4));
    	title.add((TextView) findViewById(R.id.other_tv_5));
    	title.add((TextView) findViewById(R.id.other_tv_6));
    	title.add((TextView) findViewById(R.id.other_tv_7));
    	title.add((TextView) findViewById(R.id.other_tv_8));
    	title.add((TextView) findViewById(R.id.other_tv_9));
    	title.add((TextView) findViewById(R.id.other_tv_10));

    	entry.add((EditText) findViewById(R.id.other_et_1));
    	entry.add((EditText) findViewById(R.id.other_et_2));
    	entry.add((EditText) findViewById(R.id.other_et_3));
    	entry.add((EditText) findViewById(R.id.other_et_4));
    	entry.add((EditText) findViewById(R.id.other_et_5));
    	entry.add((EditText) findViewById(R.id.other_et_6));
    	entry.add((EditText) findViewById(R.id.other_et_7));
    	entry.add((EditText) findViewById(R.id.other_et_8));
    	entry.add((EditText) findViewById(R.id.other_et_9));
    	entry.add((EditText) findViewById(R.id.other_et_10));
		
		// grab list of services to show, based on the service subtype - subtype should always be "Other"
		populateServicesList(role, "Other");
		
		// determine language from current tablet settings
		String lang = Locale.getDefault().getLanguage();
		
		// for each service in the serviceList, set up the tv and et UI elements 
		for (int i = 0; i < servicesList.size(); i++) {
			title.get(i).setText(servicesList.get(i).getName(lang));
			title.get(i).setVisibility(View.VISIBLE);
			entry.get(i).setHint(servicesList.get(i).getInstructions());
			entry.get(i).setTag(servicesList.get(i).getName(lang));
			entry.get(i).setVisibility(View.VISIBLE);
		}       
    }
    
    // used to generate the list of services for serviceDelivery
    private void populateServicesList(String role, String type) {
        Dao<Service, Integer> sDao;
        List<Service> allServices = new ArrayList<Service>();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        try {
			sDao = dbHelper.getServicesDao();
			allServices = sDao.query(sDao.queryBuilder().prepare());
        	for (Service s : allServices) {
    			if (s.getRole().equals(role) && s.getType().equals(type)) {
    				servicesList.add(s);
    			}
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      	
    }
    
    public void selectServiceTargets(View v) {
    	ArrayList<String> sNames = new ArrayList<String>();
    	ArrayList<String> sAdInfo = new ArrayList<String>();
    	// if entry box is visible and is not empty, add the service name to the array
    	for (EditText et : entry) {
    		if (et.getVisibility() == View.VISIBLE && et.getText().toString().trim().length() != 0) {
    			sNames.add(et.getTag().toString());
    			sAdInfo.add(et.getText().toString());
    		}
    	}
    	
    	
    	Intent i = new Intent(ServiceOtherActivity.this, ServiceDeliveryActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	b.putStringArrayList("serviceNames", sNames);
    	b.putBoolean("adInfoFlag",true);
    	b.putStringArrayList("serviceAdInfo", sAdInfo);
    	i.putExtras(b);
    	startActivity(i);
    }
    
    
}