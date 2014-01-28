package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.Visit;

import com.j256.ormlite.dao.Dao;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceOverviewActivity extends Activity {
	Context context;
	int visitId = 0;
	int hhId = 0;
	String role = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        
		setContentView(R.layout.activity_service_overview);

		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		//Visit v = ModelHelper.getVisitForId(context, visitId);
		role = ModelHelper.getVisitForId(context, visitId).getRole();
		
		setupServiceTypeButtons(role);
	}
    
    // using this override cause we're on an old android version and I can't get the support libraries to work correctly. TODO - look into this and expand to other pages?
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	Intent i = new Intent(ServiceOverviewActivity.this, HomeActivity.class);
        	Bundle b = new Bundle();
        	b.putInt("visitId",visitId);
        	b.putBoolean("fromBack", true);
        	i.putExtras(b);
        	startActivity(i);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void setupServiceTypeButtons(String role) {
    	// figure out which button names we need for this screen
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
		
		// cycle through the serviceTypes, populate the labels and tags
		for (int i = 1; i <= serviceTypes.length; i++) {
			String tvId = "service_subtype" + i + "_text_field";
			int resId = getResources().getIdentifier(tvId, "id", "org.chat.android");
		    TextView tv = (TextView) findViewById(resId);
			tv.setText(serviceTypes[i-1]);
			tv.setTag(serviceTypes[i-1]);
			
			String imgId = "service_subtype" + i + "_button";
			resId = getResources().getIdentifier(imgId, "id", "org.chat.android");
			ImageView iv = (ImageView) findViewById(resId);
			iv.setTag(serviceTypes[i-1]);
		}
    }
    
    // get the visitObject - going to be using this a lot. Maybe better to abstract it, make it public, have it return the object?
//	public Visit determineRole() {
//		Dao<Visit, Integer> vDao;		
//		DatabaseHelper vDbHelper = new DatabaseHelper(getBaseContext());
//		Visit v = null;
//		try {
//			vDao = vDbHelper.getVisitsDao();
//			List<Visit> vList = vDao.queryBuilder().where().eq("id",visitId).query();
//			Iterator<Visit> iter = vList.iterator();
//			while (iter.hasNext()) {
//				v = iter.next();
//			}
//		} catch (SQLException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}			
//		return v;
//	}
	
	public void openServiceDetails(View v) {
		String subtype = null;
        subtype = (String) v.getTag();
		Intent i = null;
        
        if (subtype.equals("Other")) {
        	i = new Intent(ServiceOverviewActivity.this, ServiceOtherActivity.class);
        } else {
        	i = new Intent(ServiceOverviewActivity.this, ServiceDetailsActivity.class);
        }
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	b.putString("role",role);
    	b.putString("subtype",subtype);
    	i.putExtras(b);
    	startActivity(i);
	}
	
//	@Override
//    public void onBackPressed() {
//		Intent i = new Intent(ServiceOverviewActivity.this, HomeActivity.class);
//    	Bundle b = new Bundle();
//    	b.putInt("visitId",visitId);
//    	b.putBoolean("fromBack", true);
//    	i.putExtras(b);
//    	startActivity(i);
//    }
	
}
