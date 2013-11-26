package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.Visit;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ServiceOverviewActivity extends Activity {
	int visitId = 0;
	int hhId = 0;
	String role = null;
	
	// TODO - consider if we want a serviceType class. And via ORM layer as well?
	List<String> backgroundURIList = new ArrayList<String>(
		Arrays.asList("children_play_screenshot", "children_play_screenshot")
	);
	String fakeURI = "children_play_screenshot";
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        
		setContentView(R.layout.activity_service_overview);

		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		Visit v = determineRole();
		role = v.getRole();
		

		// display the list of types of services based on the user's role - TODO: move this into the DB at some point?
		String[] serviceTypes;
		String[] roleArray = getResources().getStringArray(R.array.role_array);
		if (role.equals(roleArray[0])) {
			serviceTypes = getResources().getStringArray(R.array.volunteer_service_type_array);
		} else if (role.equals(roleArray[1])) {
			serviceTypes = getResources().getStringArray(R.array.counsellor_service_type_array);
		} else if (role.equals(roleArray[2])) {
			serviceTypes = getResources().getStringArray(R.array.welfare_service_type_array);
		} else {
			// TODO: expand me? Also throw a proper error here
			serviceTypes = getResources().getStringArray(R.array.volunteer_service_type_array);
			Toast.makeText(getApplicationContext(),"Role is undefined",Toast.LENGTH_LONG).show();
		}
		
		// cycle through the serviceTypes, populate the button labels and tags
		for (int i = 0; i < serviceTypes.length; i++) {
			String buttonID = "service_subtype_" + i;
			int resID = getResources().getIdentifier(buttonID, "id", "org.chat.android");
		    Button btn = ((Button) findViewById(resID));
			btn.setText(serviceTypes[i]);
		}
	}
    
    // get the visitObject - going to be using this a lot. Maybe better to abstract it, make it public, have it return the object?
	public Visit determineRole() {
		Dao<Visit, Integer> vDao;		
		DatabaseHelper vDbHelper = new DatabaseHelper(getBaseContext());
		Visit v = null;
		try {
			vDao = vDbHelper.getVisitsDao();
			List<Visit> vList = vDao.queryBuilder().where().eq("id",visitId).query();
			Iterator<Visit> iter = vList.iterator();
			while (iter.hasNext()) {
				v = iter.next();
				role = v.getRole();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}			
		return v;
	}
	
	public void openServiceDetails(View v) {
		Button btn = (Button)v;
		String subtype = btn.getText().toString();
		//Toast.makeText(getApplicationContext(),"You pressed: "+subtype,Toast.LENGTH_LONG).show();
		
    	Intent i = new Intent(ServiceOverviewActivity.this, ServiceDetailsActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	b.putString("subtype",subtype);
    	i.putExtras(b);
    	startActivity(i);
	}
	
}
