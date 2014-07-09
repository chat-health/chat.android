package org.chat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceOverviewActivity extends BaseActivity {
	Context context;
	int visitId = 0;
	int hhId = 0;
	String role = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_service_overview);
		
		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		//Visit v = ModelHelper.getVisitForId(context, visitId);
		role = ModelHelper.getVisitForId(context, visitId).getRole();
		
		setupServiceTypeButtons(role);
	}
    
    public void setupServiceTypeButtons(String role) {
    	// TODO: put this all in a new DB table, instead of relying on strings array and ifs
    	String[] serviceNames = null;
		String[] serviceTags = null;
		String[] serviceImages = null;
		String[] roleArray = getResources().getStringArray(R.array.role_array);
		if (role.equals(roleArray[0])) {
			serviceNames = getResources().getStringArray(R.array.volunteer_service_type_names);
			serviceTags = getResources().getStringArray(R.array.volunteer_service_type_tags);
			serviceImages = getResources().getStringArray(R.array.volunteer_service_type_images);
		} else if (role.equals(roleArray[1])) {
			serviceTags = getResources().getStringArray(R.array.counsellor_service_type_array);
			serviceImages = getResources().getStringArray(R.array.counsellor_service_type_images);
		} else if (role.equals(roleArray[2])) {
			// looks like we're not doing welfare - incorporate images later as necessary
			serviceTags = getResources().getStringArray(R.array.welfare_service_type_array);
		} else {
			// TODO: expand me? Also throw a proper error here
			serviceTags = getResources().getStringArray(R.array.volunteer_service_type_names);
			BaseActivity.toastHelper(this, "Role is undefined. Contact technical support.");
		}
		
		// cycle through the serviceTypes, populate the labels and tags
		for (int i = 1; i <= serviceTags.length; i++) {
			// set the text
			String tvId = "service_subtype" + i + "_text_field";
			int resId = getResources().getIdentifier(tvId, "id", "org.chat.android");
		    TextView tv = (TextView) findViewById(resId);
			tv.setText(serviceNames[i-1]);
			tv.setTag(i);
			
			// tag the button
			String imgId = "service_subtype" + i + "_button";
			resId = getResources().getIdentifier(imgId, "id", "org.chat.android");
			ImageView iv = (ImageView) findViewById(resId);
			iv.setTag(i);
			
			// set the correct image
			if (serviceImages[i-1] != null) {
				int imageResId = getResources().getIdentifier("drawable/"+serviceImages[i-1], null, getPackageName());
				iv.setImageDrawable(getResources().getDrawable(imageResId));
			} else {
		         Log.e("Missing resource", "serviceImages");
			}
			
		}
    }
	
	public void openServiceDetails(View v) {
		Integer serviceTag = 0;
		serviceTag = (Integer) v.getTag();
		Intent i = null;
        
        if (serviceTag == 6) {
        	i = new Intent(ServiceOverviewActivity.this, ServiceOtherActivity.class);
        } else {
        	i = new Intent(ServiceOverviewActivity.this, ServiceDetailsActivity.class);
        }
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	b.putString("role",role);
    	b.putInt("serviceTag",serviceTag);
    	i.putExtras(b);
    	startActivity(i);
	}
	
	@Override
    public void onBackPressed() {
		Intent i = new Intent(ServiceOverviewActivity.this, HomeActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putBoolean("fromBack", true);
    	i.putExtras(b);
    	startActivity(i);
    }
	
}
