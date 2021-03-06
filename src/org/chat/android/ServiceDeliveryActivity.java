package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.chat.android.models.Client;
import org.chat.android.models.ServiceAccessed;

import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


public class ServiceDeliveryActivity extends BaseActivity {
	Context context;
	private int visitId = 0;
	private int hhId = 0;
	Boolean adInfoFlag;
	List<Client> presentClients = new ArrayList<Client>();
	//ArrayList<String> serviceNames = new ArrayList<String>();
	String sName = null;
	//ArrayList<String> serviceAdInfo = new ArrayList<String>();
	String serviceAdInfo = null;
	ServiceDeliveryAdapter sdAdapter = null;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_service_delivery);

		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		sName = b.getString("serviceName");
		adInfoFlag = b.getBoolean("adInfoFlag");
		if (adInfoFlag == true) {
			serviceAdInfo = b.getString("serviceAdInfo");
		}
		
		// grab list of present clients to show, based on the attendance
		presentClients = ModelHelper.getAttendingClientsForVisitIdUnderAge(getHelper(), visitId, 999);
		
		ListView lv = (ListView) findViewById(R.id.service_delivery_listview);
		sdAdapter = new ServiceDeliveryAdapter(context, android.R.layout.simple_list_item_multiple_choice, presentClients, visitId);
	    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    lv.setAdapter(sdAdapter);
    }
    
    public void selectServiceDeliveryClients(View v) {
    	List<Client> attendingClients = sdAdapter.getSelectedClients();
    	
    	if (attendingClients.size() > 0) {
    		try {
	    		Dao<ServiceAccessed, Integer> saDao = getHelper().getServiceAccessedDao();
	    		// for each attending hh member
	    		for (Client client : attendingClients) {
	    			ServiceAccessed sa = null;
	    			// decide whether there is ad_info (ie it's an outlier type service that is not a simple checkbox)
	    			Date time = new Date();
	    			int serviceId = 0;
	    			serviceId = ModelHelper.getServiceForName(getHelper(), sName, "en").getId();
	    			if (adInfoFlag == true) {
	    				sa = new ServiceAccessed(serviceId, visitId, client.getId(), serviceAdInfo, time);
	    			} else {
	    				sa = new ServiceAccessed(serviceId, visitId, client.getId(), null, time);
	    			}

	    		    saDao.create(sa);
	    		}
    		} catch (SQLException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }

	    	
	    	String deliveredStr = getResources().getString(getResources().getIdentifier("service_delivered_text", "string", getPackageName()));
	    	BaseActivity.toastHelper(this, deliveredStr);
//	    	Intent intent = new Intent(ServiceDeliveryActivity.this, ServiceOverviewActivity.class);
//	    	Bundle b = new Bundle();
//	    	b.putInt("visitId",visitId);
//	    	b.putInt("hhId",hhId);
//	    	intent.putExtras(b);
//	    	startActivity(intent);
	    	finish();
    	} else {
    		String msg = getResources().getString(getResources().getIdentifier("service_select_one_client_text", "string", getPackageName()));
    		BaseActivity.toastHelper(this, msg);
    	}
    }
    
    public void cancelServiceDelivery(View v) {
    	finish();
    }
}
