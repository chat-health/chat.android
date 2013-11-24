package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class ServiceDeliveryActivity extends Activity {
	Context context;
	private int visitId = 0;
	private int hhId = 0;
	List<Client> presentClients = new ArrayList<Client>();
	ArrayList<String> serviceNames = new ArrayList<String>();
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
    	// for each checked service
    	for (String sName : serviceNames) {
    		//int serviceId = getServiceIdFromName(sName);
    		// for each attending hh member
    		for (Client client : attendingClients) {
    			ServiceAccessed sa = new ServiceAccessed(getServiceIdFromName(sName), visitId, client.getId());
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
    	}
    	
    	// update the visit object to reflect that at least one service was accessed
		Dao<Visit, Integer> vDao;
		DatabaseHelper vDbHelper = new DatabaseHelper(context);
		try {
			vDao = vDbHelper.getVisitsDao();
			UpdateBuilder<Visit, Integer> updateBuilder = vDao.updateBuilder();
			updateBuilder.updateColumnValue("service_accessed", true);
			updateBuilder.where().eq("id",visitId);
			updateBuilder.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Toast.makeText(getApplicationContext(),"Service(s) marked as delivered to client(s)",Toast.LENGTH_LONG).show();
    	finish();
    }
    
    
    public void cancelServiceDelivery(View v) {
    	finish();
    }
    
    
    private int getServiceIdFromName(String sName) {
    	int sId = 0;
        Dao<Service, Integer> sDao;
        List<Service> allServices = new ArrayList<Service>();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        try {
			sDao = dbHelper.getServicesDao();
			allServices = sDao.query(sDao.queryBuilder().prepare());
        	for (Service s : allServices) {
    			if (s.getName().equals(sName)) {
    				sId = s.getId();
    			}
        	}
		} catch (SQLException e) {
			Log.e("Unable to locate service id from name:", sName);
			e.printStackTrace();
		}
        return sId;
    }
    
}
