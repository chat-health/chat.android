package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
	String role = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View servicesFragmentView = inflater.inflate(R.layout.activity_service_overview, container, false);
		Context context = getApplicationContext();
		
		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		
//		// get the visitObject
//		Dao<Visit, Integer> vDao;		
//		DatabaseHelper hDbHelper = new DatabaseHelper(context);
//		try {
//			vDao = vDbHelper.getVisitsDao();
//			List<Visit> vList = vDao.queryBuilder().where().eq("visit_id",visitId).query();
//			Iterator<Visit> iter = vList.iterator();
//			while (iter.hasNext()) {
//				Visit v = iter.next();
//				role = v.getRole();
//			}
//		} catch (SQLException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		role = "Home Care Volunteer";
		
		
		// SERVICES LIST
		ListView sList = (ListView) findViewById(R.id.service_overview_listview);

		// display the list of types of services based on the user's role
		String[] serviceTypes;
		String[] roleArray = getResources().getStringArray(R.array.role_array);
		if (role.equals(roleArray[0])) {
			serviceTypes = getResources().getStringArray(R.array.volunteer_service_type_array);
		} else if (role.equals(roleArray[1])) {
			serviceTypes = getResources().getStringArray(R.array.councelor_service_type_array);
		} else {
			// TODO: expand me? Also throw a proper error here
			serviceTypes = getResources().getStringArray(R.array.volunteer_service_type_array);
			Toast.makeText(getApplicationContext(),"Role is undefined",Toast.LENGTH_LONG).show();
		}		

		
		
//		// NOTES LIST
//		ListView nList = (ListView) servicesFragmentView.findViewById(R.id.notes_listview);
//		final ArrayList<String> nArray = new ArrayList<String>();
//		ServiceOverviewAdapter nAdapter = new ServiceOverviewAdapter(context, android.R.layout.simple_list_item_1, nArray);
//		nList.setAdapter(nAdapter);
//		
//		
//		// NEW NOTES
//		final EditText newNoteField = (EditText) servicesFragmentView.findViewById(R.id.notes_edittext);
//		//String newNote = newNoteField.getText().toString();
//		Button submitNoteBtn = (Button) servicesFragmentView.findViewById(R.id.notes_button);
//		submitNoteBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String newNote = newNoteField.getText().toString();
//				Toast.makeText(getApplicationContext(),"Added note",Toast.LENGTH_LONG).show();
//				nArray.add(newNote);						// THIS MAY NEED TO GET SAVED TO THE DB
//				newNoteField.setText("");
//			}
//		});
	  
	  // inflate the layout for this fragment
	  return servicesFragmentView;
	}


}