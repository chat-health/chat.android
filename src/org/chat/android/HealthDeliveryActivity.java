package org.chat.android;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.chat.android.models.HealthPage;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.HealthTopicAccessed;
import org.chat.android.models.Video;
import org.chat.android.models.VideoAccessed;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class HealthDeliveryActivity extends BaseActivity {
	Context context = null;
	
	List<HealthPage> pages = new ArrayList<HealthPage>();
	int hhId = 0;
	int visitId = 0;
	String healthTheme = null;
	String topicName = null;
	int topicId = 0;
	int pageCounter = 0;
	int lastPage = 0;
	HealthTopicAccessed healthTopicAccessed;
	
	TextView paginationTextField = null;
	ImageButton backBtn = null;
	ImageButton nextBtn = null;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    context = getApplicationContext();
	    setContentView(R.layout.activity_health_delivery);
	    
		Bundle b = getIntent().getExtras();
		hhId = b.getInt("hhId");
		visitId = b.getInt("visitId");
		healthTheme = b.getString("healthTheme");
		topicName = b.getString("topic");
		
		paginationTextField = (TextView)findViewById(R.id.paginationTextField);
		backBtn = (ImageButton)findViewById(R.id.backButton);
		nextBtn = (ImageButton)findViewById(R.id.nextButton);
		
		topicId = ModelHelper.getTopicForName(getHelper(), topicName).getId();
		
		// create the health topic accessed object
		createHTAObject();
		
		// get the required pages for the topic
		populatePagesArray();
	    
	    // update the fragment in the UI to the first page
	    updateNonFragmentUIElements("next");
	    updateDisplayedFragment(pageCounter);
    }
	
	public void updateDisplayedFragment(int pageNum) {
		HealthPage p = pages.get(pageNum - 1);
	    String type = p.getType();
	    type = type.substring (0,1).toUpperCase() + type.substring (1).toLowerCase();			// cap the first letter so we can use it as a class name
	    
	    // oh the joys of static typing. This nonsense allows us to 'cast' the type to a fragment
	    Fragment newFrag = null;
		try {
			newFrag = (Fragment) Class.forName("org.chat.android.pages." + type + "Fragment").newInstance();
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
		bundle.putInt("id",p.getPageContentId());
		bundle.putString("topicName",topicName);
		bundle.putString("themeName",healthTheme);
		newFrag.setArguments(bundle);
		
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    // type is added as a tag to be used later in identifying which fragment we're in
	    ft.replace(R.id.placeholder, newFrag, type);
	    ft.commit();
	}
	
	public void moveNext(View v) {
		// check if user has selected a radio button
		Boolean proceedFlag = true;
		// check if this is a radio button page
		HealthPage p = pages.get(pageCounter - 1);
		if (p.getType().equals("select1")) {
			// check if there is a HSR for this topic
			HealthSelectRecorded hsr = ModelHelper.getHealthSelectRecordedForVisitIdAndTopicName(getHelper(), visitId, topicName);
			if (hsr == null) {
				BaseActivity.toastHelper(this, "Please select an answer to proceed");
				proceedFlag = false;
			}
		}

		// check if this page is within bounds (1 to lastPage)
		if (proceedFlag == true) {
			if (pageCounter == lastPage) {
				updateNonFragmentUIElements("done");
			} else {
				updateNonFragmentUIElements("next");
				updateDisplayedFragment(pageCounter);	
			}
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
			// TODO: fetch theme from db, use ids instead of names 
			if (healthTheme.equals("HIV")) {
				nextBtn.setImageResource(R.drawable.hivdonebutton);
			} else if (healthTheme.equals("Childhood Illness")) {
				nextBtn.setImageResource(R.drawable.childhooddiseasesdonebutton);
			} else if (healthTheme.equals("Nutrition")) {
				nextBtn.setImageResource(R.drawable.nutritiondonebutton);
			} else if (healthTheme.equals("Psychosocial Support")) {
				nextBtn.setImageResource(R.drawable.developmentdonebutton);
			} else {
				Log.e("Specified themeId is no in DB for: ", healthTheme);
			}
		} else {
			if (healthTheme.equals("HIV")) {
				nextBtn.setImageResource(R.drawable.hivnextbutton);
			} else if (healthTheme.equals("Childhood Illness")) {
				nextBtn.setImageResource(R.drawable.childhooddiseasesnextbutton);
			} else if (healthTheme.equals("Nutrition")) {
				nextBtn.setImageResource(R.drawable.nutritionnextbutton);
			} else if (healthTheme.equals("Psychosocial Support")) {
				nextBtn.setImageResource(R.drawable.developmentnextbutton);
			} else {
				Log.e("Specified themeId is not in DB for: ", healthTheme);
			}
		}	
	}
	
	public void populatePagesArray() {
		// populate the pages array based on the topic Id and determine number of pages	
		try {
			Dao<HealthPage, Integer> pDao = getHelper().getHealthPagesDao();
			List<HealthPage> pList = pDao.queryBuilder().where().eq("topic_id",topicId).query();
			// clears out the null junk values - there is likely a better way to do this
			for (HealthPage p : pList) {
    			pages.add(p);
        	}
			lastPage = pages.size();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	public void createHTAObject() {
		// update the HealthTopicAccessed object and save to DB
		Date startTime = new Date();
		healthTopicAccessed = new HealthTopicAccessed(topicId, visitId, hhId, topicName, startTime);
	    try {
	    	Dao<HealthTopicAccessed, Integer> htaDao = getHelper().getHealthTopicAccessedDao();
	    	htaDao.create(healthTopicAccessed);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	}
	
	public void markTopicComplete() {
		String msg = getResources().getString(getResources().getIdentifier("health_ed_delivered_text", "string", getPackageName()));
		BaseActivity.toastHelper(this, msg);
		
		// update the HealthTopicAccessed object and save to DB
		Date endTime = new Date();
		healthTopicAccessed.setEndTime(endTime);
		
	    try {
	    	Dao<HealthTopicAccessed, Integer>htaDao = getHelper().getHealthTopicAccessedDao();
	    	htaDao.update(healthTopicAccessed);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }

		finishHealthDelivery();
	}
	
	public void finishHealthDelivery() {
		// finish this activity (will bump the last activity to the front - HealthDetails - which is what we want)
		finish();
	}

	public void onBackPressed() {
		String msg = getResources().getString(getResources().getIdentifier("health_ed_exit_text", "string", getPackageName()));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(msg)
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   finishHealthDelivery();
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
	
	
	// **The following methods are for the fragments**
	public void recordSelect(View v) {
		int selectResp = 0;
		selectResp = (Integer) v.getTag();
		// using 0 for clientId here - no specific client
		HealthSelectRecorded hsr = new HealthSelectRecorded(visitId, selectResp, 0, null, topicName, new Date(), getHelper());
		
		HealthSelectRecorded prevHsr = null;
		prevHsr = ModelHelper.getHealthSelectRecordedForVisitIdAndTopicName(getHelper(), visitId, topicName);
		// this nonsense is necessary because there is no 'submit' button... we can't rely on the Next in the Activity, so we just update the row each time a select is made
		// if this has not already been done this visit and this topic
		Dao<HealthSelectRecorded, Integer> hsrDao;
		if (prevHsr == null) {
			try {
	    		hsrDao = getHelper().getHealthSelectRecordedDao();
	    		hsrDao.create(hsr);
	    	} catch (SQLException e) {
	    	    // TODO Auto-generated catch block
	    	    e.printStackTrace();
	    	}
		} else {
			prevHsr.setSelectId(selectResp);
			prevHsr.setDate(new Date());
		    try {
		    	hsrDao = getHelper().getHealthSelectRecordedDao();
		    	hsrDao.update(prevHsr);
		    } catch (SQLException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    }
		}
	}
	
	public void playVideo (View v) {
    	// figure out which video to play by determining which button was pressed
    	int chosenVideoId = 0;
    	chosenVideoId = (Integer) v.getTag();
    	
    	Video chosenVideo = ModelHelper.getVideoForId(getHelper(), chosenVideoId);
    	if (chosenVideo == null) {
    		Toast.makeText(getApplicationContext(),"Error: video does not exist. Please contact technical support",Toast.LENGTH_LONG).show();
    	}
    	
    	// record which video was played in videos_accessed table
    	Date date = new Date();
	    VideoAccessed va = new VideoAccessed(chosenVideoId, visitId, date, getHelper());
	    try {
	    	Dao<VideoAccessed, Integer> vaDao = getHelper().getVideosAccessedDao();
	        vaDao.create(va);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
		
    	// determining path to sdcard (readable by video player)
    	File sdCard = Environment.getExternalStorageDirectory();
    	// adding chat dir to path (copyAsset func ensures dir exists)
        File dir = new File (sdCard.getAbsolutePath() + "/chat");
        // copy video from within the APK to the sdcard/chat dir
        // copyAsset(videoName, dir);
        
        // create file that points at video in sdcard dir (to retrieve URI)
        File myvid = new File(dir, chosenVideo.getURI());
        
        if (myvid.exists()) {
        	Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(Uri.fromFile(myvid), "video/*");
            startActivity(intent);
        } else {
        	Toast.makeText(getApplicationContext(),"This tablet does not have the selected video. Please sync videos or contact technical support",Toast.LENGTH_LONG).show();
        }
        
    }
	
}