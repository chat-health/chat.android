package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.chat.android.models.CHAAccessed;
import org.chat.android.models.Client;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.PageAssessment1;

import com.j256.ormlite.dao.Dao;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CHADelivery extends BaseActivity {
	Context context = null;
	private int visitId = 0;
	private int hhId = 0;
	private int clientId = 0;
	int pageCounter = 0;
	int lastPage = 0;
	
	List<PageAssessment1> pages = new ArrayList<PageAssessment1>();
	
	TextView paginationTextField = null;
	ImageButton backBtn = null;
	ImageButton nextBtn = null;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_cha_delivery);
        
		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		clientId = b.getInt("clientId");
		
		paginationTextField = (TextView)findViewById(R.id.cha_pagination_text_field);
		backBtn = (ImageButton)findViewById(R.id.cha_back_button);
		nextBtn = (ImageButton)findViewById(R.id.cha_next_button);
		
		// get the required pages for the topic
		populatePagesArray();
	    
	    // update the fragment in the UI to the first page
	    updateNonFragmentUIElements("next");
	    updateDisplayedFragment(pageCounter);		
    }
        
    
    public void moveNext(View v) {
    	// check if this page is within bounds (1 to lastPage)
    	if (pageCounter == lastPage) {
    		updateNonFragmentUIElements("done");
    	} else {
    		updateNonFragmentUIElements("next");
    		updateDisplayedFragment(pageCounter);	
    	}
    }

    public void moveBack(View v) {
    	// check if this page is within bounds (1 to lastPage)
    	if (pageCounter - 1 >= 1) {
    		updateNonFragmentUIElements("back");
    		updateDisplayedFragment(pageCounter);
    	} else {
    		Toast.makeText(getApplicationContext(),"First page reached",Toast.LENGTH_SHORT).show();
    	}
    }    

	public void updateDisplayedFragment(int pageNum) {
		PageAssessment1 pa1 = pages.get(pageNum - 1);
	    
	    Fragment newFrag = null;
		try {
			newFrag = (Fragment) Class.forName("org.chat.android.pages.Assessment1Fragment").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// bundle in the unique page parameters to the correct fragment
		Bundle bundle = new Bundle();
		bundle.putInt("visitId",visitId);
		//bundle.putInt("id",p.getPageContentId());
		bundle.putInt("id",pa1.getId());
		newFrag.setArguments(bundle);
		
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    ft.replace(R.id.placeholder, newFrag);
	    ft.commit();
	}
	
	public void updateNonFragmentUIElements(String m) {
		// update the page number
		if (m.equals("next")) {
			pageCounter++;
			String p = pageCounter + "/" + lastPage;
			paginationTextField.setText(p);
		} else if (m.equals("back")) {
			pageCounter--;
			String p = pageCounter + "/" + lastPage;
			paginationTextField.setText(p);
		} else if (m.equals("done")) {
			markTopicComplete();
		} else {
			Log.e("Incorrect parameter passed to updateNonFragmentUIElements: ", m);
		}
		
		// remove Back button if it's the first page (note this must happen after the pageCounter incr/decr)
		if (pageCounter == 1) {
			backBtn.setVisibility(View.GONE);
		} else {
			backBtn.setVisibility(View.VISIBLE);
		}
		// update source for next/back buttons (again, after incr/decr of pageCounter)
		if (pageCounter == lastPage) {
			nextBtn.setImageResource(R.drawable.healthdonebutton);
		} else {
			nextBtn.setImageResource(R.drawable.healthnextbutton);
		}	
	}	
    
	public void populatePagesArray() {
		// populate the pages array based on the topic Id and determine number of pages
		Dao<PageAssessment1, Integer> paDao;		
		DatabaseHelper paDbHelper = new DatabaseHelper(context);
		try {
			paDao = paDbHelper.getPageAssessment1Dao();
			List<PageAssessment1> pList = paDao.queryBuilder().query();
			// clears out the null junk values - there is likely a better way to do this
			for (PageAssessment1 p : pList) {
				pages.add(p);
	    	}
			lastPage = pages.size();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void markTopicComplete() {
		Client c = ModelHelper.getClientForId(context, clientId);
		Toast.makeText(getApplicationContext(),"Health assessment completed for " + c.getFirstName() + " " + c.getLastName(),Toast.LENGTH_LONG).show();
		
		// update the HealthTopicAccessed object and save to DB
		Date endTime = new Date();

		CHAAccessed chaa = ModelHelper.getCHAAccessedForVisitIdAndClientIdAndType(context, visitId, clientId, "health");
		chaa.setEndTime(endTime);	
				
	    Dao<CHAAccessed, Integer> chaaDao;
	    DatabaseHelper chaaDbHelper = new DatabaseHelper(context);
	    try {
	    	chaaDao = chaaDbHelper.getCHAAccessedDao();
	    	chaaDao.update(chaa);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	
	    finish();
	}


	public void recordSelect(View v) {
		int selectResp = 0;
		selectResp = (Integer) v.getTag();
		
		HealthSelectRecorded hsr = new HealthSelectRecorded(visitId, selectResp, null, "assessment");
		Dao<HealthSelectRecorded, Integer> hsrDao;
		DatabaseHelper hsrDbHelper = new DatabaseHelper(context);
		try {
    		hsrDao = hsrDbHelper.getHealthSelectRecordedDao();
    		hsrDao.create(hsr);
    	} catch (SQLException e) {
    	    // TODO Auto-generated catch block
    	    e.printStackTrace();
    	}
	}
	
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Exit health assessment delivery?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
		
		
		
		
		
//		HealthSelectRecorded hsr = new HealthSelectRecorded(visitId, selectResp, null, "assessment");
//		
//		HealthSelectRecorded prevHsr = null;
//		prevHsr = ModelHelper.getHealthSelectRecordedsForVisitIdAndTopicName(context, visitId, "assessment");
//		// this nonsense is necessary because there is no 'submit' button... we can't rely on the Next in the Activity, so we just update the row each time a select is made
//		// if this has not already been done this visit and this topic
//		Dao<HealthSelectRecorded, Integer> hsrDao;
//		DatabaseHelper hsrDbHelper = new DatabaseHelper(context);
//		if (prevHsr == null) {
//			try {
//	    		hsrDao = hsrDbHelper.getHealthSelectRecordedDao();
//	    		hsrDao.create(hsr);
//	    	} catch (SQLException e) {
//	    	    // TODO Auto-generated catch block
//	    	    e.printStackTrace();
//	    	}
//		} else {
//			prevHsr.setSelectId(selectResp);
//		    try {
//		    	hsrDao = hsrDbHelper.getHealthSelectRecordedDao();
//		    	hsrDao.update(prevHsr);
//		    } catch (SQLException e1) {
//		        // TODO Auto-generated catch block
//		        e1.printStackTrace();
//		    }
//		}





}


