package org.chat.android;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.HealthTopic;

import com.j256.ormlite.dao.Dao;

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

		Boolean previouslyAccessedFlag = false;
		
		Dao<HealthSelectRecorded, Integer> hsrDao;		
		DatabaseHelper hsrDbHelper = new DatabaseHelper(getApplicationContext());
		try {
			hsrDao = hsrDbHelper.getHealthSelectRecordedDao();
			List<HealthSelectRecorded> hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("theme", healthTheme).query();			
			Iterator<HealthSelectRecorded> iter = hsrList.iterator();
			while (iter.hasNext()) {
				HealthSelectRecorded hsr = iter.next();
				previouslyAccessedFlag = true;
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		Intent i;
		if (previouslyAccessedFlag == true) {
			i = new Intent(HealthOverviewActivity.this, HealthDetailsActivity.class);
		} else {
			i = new Intent(HealthOverviewActivity.this, HealthOverviewRecordActivity.class);
		}
		
		Bundle b = new Bundle();
		b.putString("healthTheme",healthTheme);
		b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
		i.putExtras(b);
		startActivity(i);
	}	   
}

// TODO: clean this up so that it uses themes pulled from the health_themes table as opposed to hardcoded

