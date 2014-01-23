package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.Attendance;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.VideoAccessed;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class HealthOverviewRecordActivity extends Activity {
	Context context;
	int hhId = 0;
	int visitId = 0;
	String healthThemeName = null;
	HealthTheme theme = null;
	TextView obsTitle = null;
	TextView obsTv = null;
	TextView recTitle = null;
	TextView recTv = null;
	RadioGroup rbg = null;
	RadioButton r1 = null;
	RadioButton r2 = null;
	RadioButton r3 = null;
	RadioButton r4 = null;
	ImageButton continueBtn = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

		setContentView(R.layout.activity_health_overview_record);
		obsTitle = (TextView)findViewById(R.id.overviewRecordTitle1);
		obsTv = (TextView)findViewById(R.id.overviewRecordContent1);
		recTitle = (TextView)findViewById(R.id.overviewRecordTitle2);
		recTv = (TextView)findViewById(R.id.overviewRecordContent2);
		rbg = (RadioGroup)findViewById(R.id.overviewRecordRadio);
		r1 = (RadioButton)findViewById(R.id.overviewRecordRadioButton1);
		r2 = (RadioButton)findViewById(R.id.overviewRecordRadioButton2);
		r3 = (RadioButton)findViewById(R.id.overviewRecordRadioButton3);
		r4 = (RadioButton)findViewById(R.id.overviewRecordRadioButton4);
		continueBtn = (ImageButton)findViewById(R.id.continueButton);
		
		Bundle b = getIntent().getExtras();
		hhId = b.getInt("hhId");
		visitId = b.getInt("visitId");
		healthThemeName = b.getString("healthTheme");
		
		populateThemeContent();
		updateUIColors();
    }
	
	public void populateThemeContent() {
		Dao<HealthTheme, Integer> tDao;		
		DatabaseHelper tDbHelper = new DatabaseHelper(context);
		try {
			tDao = tDbHelper.getHealthThemeDao();
			List<HealthTheme> tList = tDao.queryBuilder().where().eq("name",healthThemeName).query();
			Iterator<HealthTheme> iter = tList.iterator();
			while (iter.hasNext()) {
				HealthTheme t = iter.next();
				theme = t;
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		List<HealthSelect> selects = new ArrayList<HealthSelect>();
		Dao<HealthSelect, Integer> hsDao;		
		DatabaseHelper hsDbHelper = new DatabaseHelper(context);
		try {
			hsDao = hsDbHelper.getHealthSelectDao();
			List<HealthSelect> hsList = hsDao.queryBuilder().where().eq("subject_id",theme.getId()).query();
			Iterator<HealthSelect> iter = hsList.iterator();
			while (iter.hasNext()) {
				HealthSelect hs = iter.next();
				selects.add(hs);
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// TODO set these up to work with Zulu (in the model)
		if (theme != null) {
			obsTv.setText(theme.getEnObserveContent());
			recTv.setText(theme.getEnRecordContent());
		}
		
		if (selects.size() == 4) {
			// set up the radio buttons, tagged with ID (to be used when saving)
			r1.setText(selects.get(0).getEnContent());
			r1.setTag(selects.get(0).getId());
			r2.setText(selects.get(1).getEnContent());
			r2.setTag(selects.get(1).getId());
			r3.setText(selects.get(2).getEnContent());
			r3.setTag(selects.get(2).getId());
			r4.setText(selects.get(3).getEnContent());
			r4.setTag(selects.get(3).getId());
		}

	}
	
	public void updateUIColors() {
		int colorRef = Color.parseColor(theme.getColor());
		
		obsTitle.setTextColor(colorRef);
		recTitle.setTextColor(colorRef);
		
		// button color
		if (theme.getId() == 1) {
			continueBtn.setImageResource(R.drawable.hivcontinuebutton);
		} else if (theme.getId() == 2) {
			continueBtn.setImageResource(R.drawable.childhooddiseasescontinuebutton);
		} else if (theme.getId() == 3) {
			continueBtn.setImageResource(R.drawable.nutritioncontinuebutton);
		} else if (theme.getId() == 4) {
			continueBtn.setImageResource(R.drawable.developmentcontinuebutton);
		} else {
			Log.e("Specified themeId is no in DB for: ", theme.getName());
		}
	}
   
	public void openHealthDetails(View v) {
		// save radio value
		int selectResp = 0;
		int radioButtonID = rbg.getCheckedRadioButtonId();
		View rb = rbg.findViewById(radioButtonID);		
		
		// TODO: temporary until the DB has been filled with all of the theme content - after that is done, use this to force user to select before proceeding
		if (rb != null) {
			selectResp = Integer.parseInt(rb.getTag().toString());
		}
			
		HealthSelectRecorded hsr = new HealthSelectRecorded(visitId, selectResp, healthThemeName);
    	Dao<HealthSelectRecorded, Integer> hsrDao;
    	DatabaseHelper hsrDbHelper = new DatabaseHelper(context);
    	try {
    		hsrDao = hsrDbHelper.getHealthSelectRecordedDao();
    		hsrDao.create(hsr);
    	} catch (SQLException e) {
    	    // TODO Auto-generated catch block
    	    e.printStackTrace();
    	}
		
		// open new intent
		Intent i = new Intent(HealthOverviewRecordActivity.this, HealthDetailsActivity.class);
		Bundle b = new Bundle();
		b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	b.putString("healthTheme",healthThemeName);
		i.putExtras(b);
		startActivity(i);
		finish();
	}	   
}

