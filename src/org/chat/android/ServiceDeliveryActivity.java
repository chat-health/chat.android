package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.chat.android.models.Attendance;
import org.chat.android.models.Client;
import org.chat.android.models.Service;
import org.chat.android.models.ServiceAccessed;
import org.chat.android.models.Visit;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class ServiceDeliveryActivity extends BaseActivity {
	Context context;
	private int visitId = 0;
	private int hhId = 0;
	Boolean adInfoFlag;
	List<Client> presentClients = new ArrayList<Client>();
	ArrayList<String> serviceNames = new ArrayList<String>();
	ArrayList<String> serviceAdInfo = new ArrayList<String>();
	ServiceDeliveryAdapter sdAdapter = null;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_service_delivery);

		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		serviceNames = b.getStringArrayList("serviceNames");
		adInfoFlag = b.getBoolean("adInfoFlag");
		if (adInfoFlag == true) {
			serviceAdInfo = b.getStringArrayList("serviceAdInfo");
		}
		
		// grab list of present clients to show, based on the attendance
		populateMembersList();
		
		ListView lv = (ListView) findViewById(R.id.service_delivery_listview);
		sdAdapter = new ServiceDeliveryAdapter(context, android.R.layout.simple_list_item_multiple_choice, presentClients, visitId);
	    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    lv.setAdapter(sdAdapter);
    }
    
    private void populateMembersList() {
    	// create the list of attending Clients
    	List<Integer> presentHHMembers = new ArrayList<Integer>();
        Dao<Attendance, Integer> aDao;
        List<Attendance> allAttendees = new ArrayList<Attendance>();
        DatabaseHelper attDbHelper = new DatabaseHelper(getApplicationContext());
        try {
			aDao = attDbHelper.getAttendanceDao();
			allAttendees = aDao.query(aDao.queryBuilder().prepare());
        	for (Attendance a : allAttendees) {
    			if (a.getVisitId() == visitId) {
    				presentHHMembers.add(a.getClientId());
    			}
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        Dao<Client, Integer> cDao;
        List<Client> allClients = new ArrayList<Client>();
        DatabaseHelper cDbHelper = new DatabaseHelper(getApplicationContext());
        try {
			cDao = cDbHelper.getClientsDao();
			allClients = cDao.query(cDao.queryBuilder().prepare());
        	for (Client c : allClients) {
        		for (Integer i : presentHHMembers) {
        			if (i == c.getId()) {
        				presentClients.add(c);
        			}        			
        		}
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
    }
    

    public void selectServiceDeliveryClients(View v) {
    	List<Client> attendingClients = sdAdapter.getSelectedClients();
    	if (attendingClients.size() > 0) {
	    	// for each checked service
	    	int i = 0;
	    	for (String sName : serviceNames) {
	    		// for each attending hh member
	    		for (Client client : attendingClients) {
	    			ServiceAccessed sa = null;
	    			// decide whether there is ad_info (ie it's an outlier type service that is not a simple checkbox)
	    			Date time = new Date();
	    			int serviceId = 0;
	    			serviceId = ModelHelper.getServiceForName(context, sName).getId();
	    			if (adInfoFlag == true) {
	    				sa = new ServiceAccessed(serviceId, visitId, client.getId(), serviceAdInfo.get(i), time);
	    			} else {
	    				sa = new ServiceAccessed(serviceId, visitId, client.getId(), null, time);
	    			}
	    		    Dao<ServiceAccessed, Integer> saDao;
	    		    DatabaseHelper saDbHelper = new DatabaseHelper(context);
	    		    try {
	    		        saDao = saDbHelper.getServiceAccessedDao();
	    		        saDao.create(sa);
	    		    } catch (SQLException e) {
	    		        // TODO Auto-generated catch block
	    		        e.printStackTrace();
	    		    }
	    		}
	    		i++;
	    	}
	    	
	    	Toast.makeText(getApplicationContext(),"Service(s) marked as delivered to client(s)",Toast.LENGTH_LONG).show();
	    	Intent intent = new Intent(ServiceDeliveryActivity.this, ServiceOverviewActivity.class);
	    	Bundle b = new Bundle();
	    	b.putInt("visitId",visitId);
	    	b.putInt("hhId",hhId);
	    	intent.putExtras(b);
	    	startActivity(intent);
	    	finish();
    	} else {
    		Toast.makeText(getApplicationContext(),"You must select at least one client to mark this service as delivered",Toast.LENGTH_LONG).show();
    	}
    }
    
    
    public void cancelServiceDelivery(View v) {
    	finish();
    }
}
