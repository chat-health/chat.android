package org.chat.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.chat.android.models.Attendance;
import org.chat.android.models.Client;
import org.chat.android.models.Household;
import org.chat.android.models.Video;
import org.chat.android.models.VideoAccessed;
import org.chat.android.models.Visit;
import org.chat.android.models.Worker;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends Activity {

	String TAG = "INFO"; //Log.i(TAG, "Testing: "+somevar);

	private Visit visit;
	public int workerId = 0;
	public int visitId = 0;
	public int hhId = 0;
	
	public ListView lv = null;
	public ClientsAdapter clAdapter = null;
	ImageButton servicesBtn = null;
	ImageButton healthBtn = null;
	ImageView servicesBtnImg = null;
	ImageView healthBtnImg = null;
	TextView servicesTitle = null;
	TextView healthTitle = null;
	View servicesDivider = null;
	View healthDivider = null;		
	
	
	// step aside I am here on official sync adapter business
	// Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "org.chat.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "chat.org";
    // The account name
    public static final String ACCOUNT = "chat-tablet";
    // Instance fields
    Account mAccount;

    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        setContentView(R.layout.activity_home);
        
        Locale locale = getResources().getConfiguration().locale;
        locale.getLanguage();
        // TODO - set some global value for language, that we'll use later (ie for Health Education Delivery). It's also possible (likely) we'll need to pass it around with intents :/
        
    	servicesTitle = (TextView)findViewById(R.id.services_title_field);
    	healthTitle = (TextView)findViewById(R.id.health_education_title_field);
    	servicesDivider = (View)findViewById(R.id.services_divider);
    	healthDivider = (View)findViewById(R.id.health_education_divider);
        
        // set the services and health branch buttons to disabled (until user has submitted attendance)
    	// SWTICH FOR PROD
        servicesBtn = (ImageButton)findViewById(R.id.services_button);
        //servicesBtn.setEnabled(false);
        servicesBtnImg = (ImageView)findViewById(R.id.services_button_img);
        //servicesBtnImg.setEnabled(false);
        healthBtn = (ImageButton)findViewById(R.id.health_education_button);
        //healthBtn.setEnabled(false);
        healthBtnImg = (ImageView)findViewById(R.id.health_education_button_img);
        //healthBtnImg.setEnabled(false);

        //FOR TESTING, SWITCH FOR PROD
		//Bundle b = getIntent().getExtras();
		//setupVisitObject(b.getString("hhName"), b.getString("workerName"), b.getString("role"), b.getString("type"), b.getDouble("lat"), b.getDouble("lon"));				
		//setupVisitObject(b.getString("hhName"), "colin", b.getString("role"), b.getString("type"), b.getDouble("lat"), b.getDouble("lon"));
        //setupVisitObject("John Doe", "colin", "Home Care Volunteer", "home", 11.11, 12.12);
        setupVisitObject("John Doe", "colin", "Lay Counsellor", "home", 11.11, 12.12);
    	
    	List<Client> cList = new ArrayList<Client>();
    	// get visit object and get the family, then use that to select TODO: yuck - FIXME (figure out the proper selector with ORM layer)
        List<Client> hhCList = new ArrayList<Client>();
        Dao<Client, Integer> clientDao;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        try {
			clientDao = dbHelper.getClientsDao();
			cList = clientDao.query(clientDao.queryBuilder().prepare());
        	for (Client c : cList) {
        		if (c.getHhId() == visit.getHhId()) {
        			hhCList.add(c);
        		}
//        		Log.d("HH members: ", c.getFirstName());
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        lv = (ListView) findViewById(R.id.attendance_listview);
        clAdapter = new ClientsAdapter(context, android.R.layout.simple_list_item_multiple_choice, hhCList, visitId);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(clAdapter);
        
        // Create the dummy account (needed for sync adapter)
        mAccount = CreateSyncAccount(this);
    }
    
    public void submitAttendance(View v) {
    	ImageButton b = (ImageButton)v;
        String bText = b.getTag().toString();
        
        // check if any checkboxes are checked
        if (clAdapter.getSelectedClients().size() > 0) {
            if (bText.equals("Done")) {
            	Toast.makeText(getApplicationContext(),"Attendance submitted",Toast.LENGTH_LONG).show();
            	b.setTag("Update");
            	updateUIElementsForSubmission(b);
            	saveAttendanceList();
            } else {
            	Toast.makeText(getApplicationContext(),"Attendance updated",Toast.LENGTH_LONG).show();
            	deleteCurrentAttendance();			// saveAttendanceList() is called from the finally in deleteCurrentAttendance()
            }        	
        } else {
        	Toast.makeText(getApplicationContext(),"Attendance not submitted. Click on the above list of clients to select attending household members",Toast.LENGTH_LONG).show();
        }

    }
    
    public void openServiceOverview(View v) {
    	Intent i = new Intent(HomeActivity.this, ServiceOverviewActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	i.putExtras(b);
    	startActivity(i);
    }

    public void openHealthOverview(View v) {
    	Intent i = new Intent(HomeActivity.this, HealthOverviewActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	i.putExtras(b);    	
    	startActivity(i);
    }
    
    public void updateUIElementsForSubmission(ImageButton b) {
    	// switch the Done button to the Update button
    	b.setImageResource(R.drawable.updatebutton);
    	
    	// enable the Service and Health branches, update the colors
    	servicesBtn.setImageResource(R.drawable.servicesgobutton);
    	servicesBtn.setEnabled(true);
    	servicesBtnImg.setImageResource(R.drawable.thandananilogo);
    	servicesBtnImg.setEnabled(true);
    	int c = getResources().getColor(getResources().getIdentifier("services", "color", getPackageName()));
    	servicesTitle.setTextColor(c);
    	servicesDivider.setBackgroundColor(c);
    	
    	healthBtn.setImageResource(R.drawable.healthedgobutton);
    	healthBtn.setEnabled(true);
    	healthBtnImg.setImageResource(R.drawable.healthedimage);
    	healthBtnImg.setEnabled(true);
    	c = getResources().getColor(getResources().getIdentifier("health_education", "color", getPackageName()));
    	healthTitle.setTextColor(c);
    	healthDivider.setBackgroundColor(c);
    }


    
    
    ////////// HELPER FUNCTIONS //////////
    private void setupVisitObject(String hhName, String workerName, String role, String type, Double lat, Double lon) {
        Context context = getApplicationContext();

        // get the workerId
		Dao<Worker, Integer> wDao;		
		DatabaseHelper wDbHelper = new DatabaseHelper(context);
		try {
			wDao = wDbHelper.getWorkersDao();
			List<Worker> wList = wDao.queryBuilder().where().eq("first_name",workerName).query();
			Iterator<Worker> iter = wList.iterator();
			while (iter.hasNext()) {
				Worker worker = iter.next();
				workerId = worker.getId();
			}
//			OTHER OPTION:		
//			CloseableIterator<Worker> iterator = wDao.iterator(preparedQuery);
//			 try {
//			     while (iterator.hasNext()) {
//			    	 Worker worker = iterator.next();
//			     }
//			 } finally {
//			     iterator.close();
//			 }
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// get the householdId
		Dao<Household, Integer> hDao;		
		DatabaseHelper hDbHelper = new DatabaseHelper(context);
		try {
			hDao = hDbHelper.getHouseholdsDao();
			List<Household> hList = hDao.queryBuilder().where().eq("hh_name",hhName).query();
			Iterator<Household> iter = hList.iterator();
			while (iter.hasNext()) {
				Household hh = iter.next();
				hhId = hh.getId();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Date date  = new Date();
		Date startTime = new Date();	

		// create a new Visit object to be used for this visit - TODO: make sure that onCreate is only called once (ie not every time we return from the ServiceDeliveryActivity)
    	visit = new Visit(workerId, role, date, hhId, type, lat, lon, startTime);
    	
    	Dao<Visit, Integer> vDao;
    	DatabaseHelper dbHelper = new DatabaseHelper(context);
    	try {
    		vDao = dbHelper.getVisitsDao();
    		vDao.create(visit);
    		visitId = visit.getId();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}    	
//    	OTHER OPTION?
//    	DatabaseHelper helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
//    	try {
//    		Dao vDao = helper.getDao(Visit.class);
//    		vDao = helper.getClientsDao();
//    		vDao.create(visit);
//    	} catch (SQLException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
		
	}
    
    
    public void saveAttendanceList() {
    	//final SparseBooleanArray checkedItems = lv.getCheckedItemPositions();
    	List<Client> pArray = clAdapter.getSelectedClients();
    	int len = pArray.size();
    	
    	// TODO: need to check if this visit already has attendance saved, then overwrite as necessary - maybe ask Armin?
    	for (int i = 0; i < len; i++) {
    		Client c = pArray.get(i);
        	Attendance a = new Attendance(visitId, c.getId());
        	Dao<Attendance, Integer> aDao;
        	DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        	try {
        		aDao = dbHelper.getAttendanceDao();
        		aDao.create(a);
        	} catch (SQLException e) {
        	    // TODO Auto-generated catch block
        	    e.printStackTrace();
        	}    	
    	}
    }

	public void deleteCurrentAttendance() {
    	DatabaseHelper helper = OpenHelperManager.getHelper(getApplicationContext(), DatabaseHelper.class);
    	Dao aDao;
	    try {
		    aDao = helper.getDao(Attendance.class);
		    DeleteBuilder<Attendance, Integer> deleteBuilder = aDao.deleteBuilder();
		    deleteBuilder.where().eq("visit_id", visitId);
		    deleteBuilder.delete(); 
    	} catch (SQLException e) {
    	  	// TODO Auto-generated catch block
    	  	e.printStackTrace();
    	} finally {
    		saveAttendanceList();
    	}
	}    
    
    public void openResource(String r) {
    	// determining path to sdcard (readable by video player)
    	File sdCard = Environment.getExternalStorageDirectory();
    	// adding chat dir to path (copyAsset func ensures dir exists)
        File dir = new File (sdCard.getAbsolutePath() + "/chat");
        
        // create file that points at video in sdcard dir (to retrieve URI)
        File myvid = new File(dir, r);
        
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(Uri.fromFile(myvid), "application/pdf");
        startActivity(intent);
    }

	public void playVideo (View v) {
		Context context = getApplicationContext();
		
		// mark that a video was accessed
		visit.setVideoAccessed(true);
		Dao<Visit, Integer> vDao;
		DatabaseHelper vDbHelper = new DatabaseHelper(context);
		try {
			vDao = vDbHelper.getVisitsDao();
			UpdateBuilder<Visit, Integer> updateBuilder = vDao.updateBuilder();
			updateBuilder.updateColumnValue("video_accessed", true);
			updateBuilder.where().eq("id",visitId);
			updateBuilder.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	// figure out which video to play by determining which button was pressed
    	int chosenVideoId = 0;
    	int buttonId = v.getId();
//    	switch (buttonId) {
//	        case R.id.button_video_1:
//	        	chosenVideoId = 1;
//	            break;
//	        case R.id.button_video_2:
//	        	chosenVideoId = 2;
//	            break;
//	        case R.id.button_video_3:
//	        	chosenVideoId = 3;
//	            break;
//	        case R.id.button_video_4:
//	        	chosenVideoId = 4;
//	            break;
//	        case R.id.image_video_1:
//	        	chosenVideoId = 1;
//	            break;
//	        case R.id.image_video_2:
//	        	chosenVideoId = 2;
//	            break;
//	        case R.id.image_video_3:
//	        	chosenVideoId = 3;
//	            break;
//	        case R.id.image_video_4:
//	        	chosenVideoId = 4;
//	            break;	            
//	        default:
//	        	chosenVideoId = 1;
//	            break;
//	    }
    	
    	// record which video was played in videos_accessed table
	    VideoAccessed va = new VideoAccessed(chosenVideoId, visitId);
	    Dao<VideoAccessed, Integer> vaDao;
	    DatabaseHelper vaDbHelper = new DatabaseHelper(context);
	    try {
	        vaDao = vaDbHelper.getVideosAccessedDao();
	        vaDao.create(va);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
    	
		// get the video_uri
	    String videoURI = "";
		Dao<Video, Integer> vidDao;		
		DatabaseHelper vidDbHelper = new DatabaseHelper(context);
		try {
			vidDao = vidDbHelper.getVideosDao();
			List<Video> vidList = vidDao.queryBuilder().where().eq("id",chosenVideoId).query();
			Iterator<Video> iter = vidList.iterator();
			while (iter.hasNext()) {
				Video vid = iter.next();
				videoURI = vid.getURI();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	// determining path to sdcard (readable by video player)
    	File sdCard = Environment.getExternalStorageDirectory();
    	// adding chat dir to path (copyAsset func ensures dir exists)
        File dir = new File (sdCard.getAbsolutePath() + "/chat");
        // copy video from within the APK to the sdcard/chat dir
        // copyAsset(videoName, dir);
        
        // create file that points at video in sdcard dir (to retrieve URI)
        File myvid = new File(dir, videoURI);
        
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(Uri.fromFile(myvid), "video/*");
        startActivity(intent);
    }
    
    private void copyAsset(String fileToCopy, File targetDir) {
        InputStream in = null;
        OutputStream out = null;
        try {
          in = getAssets().open(fileToCopy);
          
          if(targetDir.isDirectory() != true) {
        	  targetDir.mkdirs();
          }
          
          out = new FileOutputStream(new File(targetDir, fileToCopy));
          copyFile(in, out);
          in.close();
          in = null;
          out.flush();
          out.close();
          out = null;
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + fileToCopy, e);
        }   
    }
    
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
        	out.write(buffer, 0, read);
        }
    }
    
    
    ////////// OVERFLOW MENU ////////// 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_settings:
	        Toast.makeText(getApplicationContext(), "These are not the Droids you are looking for. Move along!", Toast.LENGTH_SHORT).show();
	        prepopulateDB();
	        return true;
	    case R.id.menu_sync:
	        Toast.makeText(getApplicationContext(), "Triggering sync adapter to sync with server...", Toast.LENGTH_LONG).show();
	        triggerSyncAdaper();
	        return true;
	    case R.id.menu_logout:
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("Confirm finalization of visit")
	    	       .setCancelable(false)
	    	       .setPositiveButton("Yes, mark this visit as complete and log me out", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   //triggerSyncAdapter();
	    	        	   finish();
	    	           }
	    	       })
	    	       .setNegativeButton("No, cancel and return to the visit in progress", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                dialog.cancel();
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	alert.show();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
    
    private void prepopulateDB() {
		Intent i = new Intent(HomeActivity.this, SetupDB.class);
		startActivity(i);
    }
    
    
//	private RequestQueue mRequestQueue;
//	private JSONObject jsonObj;
//	private JSONObject jsonToPost;
//    private void trySync() {
//    	//Toast.makeText(getApplicationContext(), "I'm just a placeholder", Toast.LENGTH_SHORT).show();
//
//
////    	    @Override
////    	    public void onCreate(Bundle savedInstanceState) {
////    	        super.onCreate(savedInstanceState);
////    	        setContentView(R.layout.activity_main);
//    	        
//        mRequestQueue = Volley.newRequestQueue(this);
//        mRequestQueue.add(new JsonObjectRequest(Method.GET, "http://drowsy.badger.encorelab.org/rollcall/users/52110ef654a6e433a2000006", null, new MyResponseListener(), new MyErrorListener()));
//        try {
//         jsonToPost = new JSONObject("{'username':'testy the clown', 'tags':[]}");
//        } catch (JSONException e) {
//         Log.e("JSON object creation", e.getLocalizedMessage());
//        }
//        mRequestQueue.add(new JsonObjectRequest(Method.POST, "http://drowsy.badger.encorelab.org/rollcall/users/", jsonToPost, new MyPostResponseListener(), new MyErrorListener()));
//
////    	    }
//
//
//    }
//    
//	@Override
//	protected void onStop() {
//    	if (mRequestQueue != null)
//    		mRequestQueue.cancelAll(this);
//    	super.onStop();
//	}
//	    
//	    
//	class MyResponseListener implements Listener<JSONObject> {
//    	@Override
//    	public void onResponse(JSONObject response) {
//    		MainActivity.this.jsonObj = response;
//    		try {
//    			String result = MainActivity.this.jsonObj.getString("username");
//    			Log.d("The result is: ", result);
//    			// String timestamp = result.getString("Timestamp");
//    			// MainActivity.this.resultTxt.setText(new Date(Long.parseLong(timestamp.trim())).toString());
//    		} catch (Exception e) {
//    			Log.e("ErrorListener", e.getLocalizedMessage());
//    		}
//    	}
//	}
//
//	class MyPostResponseListener implements Listener<JSONObject> {
//    	@Override
//    	public void onResponse(JSONObject response) {
//	    	try {
//		    	Log.d("The result is: ", response.toString());
//		    	// String timestamp = result.getString("Timestamp");
//		    	// MainActivity.this.resultTxt.setText(new Date(Long.parseLong(timestamp.trim())).toString());
//		    } catch (Exception e) {
//		    	Log.e("ErrorListener", e.getLocalizedMessage());
//		    }
//    	}
//	}
//
//	class MyErrorListener implements ErrorListener {
//    	@Override
//    	public void onErrorResponse(VolleyError error) {
//    		Log.e("ErrorListener", error.getCause() + "");
//    		if (error.getCause() != null) {
//    			Log.e("ErrorListener", error.getLocalizedMessage());
//    		}
//    	// MainActivity.this.resultTxt.setText(error.toString());
//    		mRequestQueue.cancelAll(this);
//    	}
//	}
    
    ////////// DEFAULT BUTTON BEHAVIOUR OVERRIDES //////////
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("Confirmation exit")
            .setMessage("Are you sure you want to exit without completing this visit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    HomeActivity.super.onBackPressed();
                }
            }).create().show();
    }
    
    /////// Sync Adapter stuff (hello boiler plate)
    /**
     * Respond to a menu click by calling requestSync(). This is an
     * asynchronous operation.
     *
     *
     * @param v The View associated with the method call,
     * in this case a Button
     */
    public void triggerSyncAdaper() {
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }
	
	/**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        	ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true); //this programmatically turns on the sync for new sync adapters.
        	return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        	return null;
        }
    }

}
