package org.chat.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HealthOverviewActivity extends Activity {
	int hhId = 0;
	int visitId = 0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_health_overview);
		
		Bundle b = getIntent().getExtras();
		hhId = b.getInt("hhId");
		visitId = b.getInt("visitId");
    }
   
	public void openHealthOverviewRecord(View v) {
		String healthTheme = null;
		healthTheme = (String) v.getTag();
		
		Intent i = new Intent(HealthOverviewActivity.this, HealthOverviewRecordActivity.class);
		Bundle b = new Bundle();
		b.putString("healthTheme",healthTheme);
		b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
		i.putExtras(b);
		startActivity(i);
	}	   
}

// TODO: clean this up so that it uses themes pulled from the health_themes table as opposed to hardcoded

