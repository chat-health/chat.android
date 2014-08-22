package org.chat.android.Sync;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import org.chat.android.DatabaseHelper;
import org.chat.android.ModelHelper;
import org.chat.android.R;
import org.chat.android.Auth.AccountGeneral;
import org.chat.android.Auth.MainActivity;
import org.chat.android.models.Attendance;
import org.chat.android.models.CHAAccessed;
import org.chat.android.models.Client;
import org.chat.android.models.HealthPage;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.HealthTopic;
import org.chat.android.models.HealthTopicAccessed;
import org.chat.android.models.Household;
import org.chat.android.models.PageAssessment1;
import org.chat.android.models.PageSelect1;
import org.chat.android.models.PageText1;
import org.chat.android.models.PageVideo1;
import org.chat.android.models.Resource;
import org.chat.android.models.ResourceAccessed;
import org.chat.android.models.ServiceAccessed;
import org.chat.android.models.TopicVideo;
import org.chat.android.models.Vaccine;
import org.chat.android.models.VaccineRecorded;
import org.chat.android.models.Video;
import org.chat.android.models.VideoAccessed;
import org.chat.android.models.Visit;
import org.chat.android.models.Worker;
import org.chat.android.models.Service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
	// since we aren't OrmLiteBaseActivity or BaseActivity we can't use getHelper()
    // so we use OpenHelperManager
    private DatabaseHelper databaseHelper = null;

	// Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Context appContext;
    // if we get any error codes back in any of the pushes, set to false (very strict, but necessary)
    Boolean pullSuccess = true;
    Boolean pushSuccess = true;
//    DatabaseHelper dbHelper;
    
    // every time sync should use this token
    private String clientToken = "";
    
    // using the account manager to access to authenticator
    private AccountManager mAccountManager;
    
    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        appContext = context;
//        dbHelper = new DatabaseHelper(appContext);
        mAccountManager = AccountManager.get(context);
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        appContext = context;
//        dbHelper = new DatabaseHelper(appContext);
        mAccountManager = AccountManager.get(context);
    }
    
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        }
        return databaseHelper;
    }
    
//    private void releaseHelper() {
//    	if (databaseHelper != null) {
//            OpenHelperManager.releaseHelper();
//            databaseHelper = null;
//        }
//    }
    
	@Override
	public void onPerformSync(Account arg0, Bundle arg1, String arg2, ContentProviderClient arg3, SyncResult arg4) {
		Log.i("SyncAdapter", "sync adapter running :)");
		// every time perform sync should check the client token is exist
		try {
			clientToken = mAccountManager.blockingGetAuthToken(arg0, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
			if(null==clientToken||clientToken.isEmpty())
			{
				Log.e("SyncAdapter", "<---------The token is missing---------->");
				// TODO 
			}
			
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// if syncType is pullAll we want to trigger a 'pull all of everything ever' sync
		// TODO: clean me up
		String syncType = "standard";
		Bundle b = arg1;
		syncType = b.getString("syncType");
		if (syncType == null) {
			syncType = "standard";
		}
		Log.i("SyncAdapter", "syncType: "+syncType);
		
		// I think we need to set this to true here
		pullSuccess = true;
	    pushSuccess = true;
		
		if (syncType.equals("pullAll")) {
			Log.i("SyncAdapter", "We are going nuclear - pulling everything");
			pullAllData();
		} else if (syncType.equals("standard")) {
			Log.i("SyncAdapter", "Just a regular pull");
			pullNewData();
			pushNewData();
		} else {
			Log.e("SyncAdapter", "Impossible error. syncType str is unknown");
		}
	}
	
	private void pullNewData() {
		Log.i("SyncAdapter", "=================== DATA PULL ===================");
			
		try
		{
			retrieveModel("clients", false);
			retrieveModel("households", false);
			retrieveModel("services", false);
			retrieveModel("workers", false);
			retrieveModel("videos", false);
			retrieveModel("resources", false);
			
			retrieveModel("health_themes", false);
			retrieveModel("health_topics", false);
			
			retrieveModel("health_pages", false);
			retrieveModel("health_selects", false);
			retrieveModel("topic_videos", false);
			retrieveModel("page_text1", false);
			retrieveModel("page_select1", false);
			retrieveModel("page_video1", false);
			retrieveModel("page_assessment1", false);
			
			retrieveModel("vaccines", false);
		} catch(UserRecoverableAuthException e){
			Intent intent = e.getIntent();
			appContext.startActivity(intent);
		} catch(Exception e) {
			pullSuccess = false;
			e.printStackTrace();
		}
        
        // change last pull date to current date
		if (pullSuccess == true) {
//			try {
				Log.i("SyncAdapter", "Pull succeeded. Moving last_synced_at date");
//				ModelHelper.setLastSyncedAt(getHelper(), new Date(), "pull");
				ModelHelper.setLastSyncedAtPull(appContext, new Date());
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		} else {
			Log.e("SyncAdapter", "Pull failed. Not moving last_synced_at date");
		}
		
		
	}
	
	private void pushNewData() {
		Log.i("SyncAdapter", "=================== DATA PUSH ===================");
		try {
			JSONArray visitsJson = createJsonArrayOf("visits");
			if (visitsJson.length() > 0) {
				pushModel("visits", visitsJson);
			}
			
			JSONArray attJson = createJsonArrayOf("attendance");
			if (attJson.length() > 0) {
				pushModel("attendance", attJson);
			}
			
			JSONArray htaJson = createJsonArrayOf("health_topics_accessed");
			if (htaJson.length() > 0) {
				pushModel("health_topics_accessed", htaJson);
			}
			
			JSONArray saJson = createJsonArrayOf("services_accessed");
			if (saJson.length() > 0) {
				pushModel("services_accessed", saJson);
			}
			
			JSONArray raJson = createJsonArrayOf("resources_accessed");
			if (raJson.length() > 0) {
				pushModel("resources_accessed", raJson);
			}
			
			JSONArray vrJson = createJsonArrayOf("vaccines_recorded");
			if (vrJson.length() > 0) {
				pushModel("vaccines_recorded", vrJson);
			}
		} catch(UserRecoverableAuthException e){
			Intent intent = e.getIntent();
			appContext.startActivity(intent);
		} catch (Exception e) {
			pushSuccess = false;
			e.printStackTrace();
		}
		
		if (pushSuccess == true) {
			try {
				ModelHelper.setLastSyncedAt(getHelper(), new Date(), "push");
				Log.i("SyncAdapter", "Push succeeded. Moving last_synced_at date");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e("SyncAdapter", "Push failed. Not moving last_synced_at date");
		}
	}
	
	private void pullAllData() {
		Log.i("SyncAdapter", "=================== SYNC ALL ===================");
		
		// TODO: finish me - I need every model
		
		// pull tables
		// true value for pullAll
		try
		{
			retrieveModel("clients", true);
			retrieveModel("households", true);
			retrieveModel("services", true);
			retrieveModel("workers", true);
			retrieveModel("videos", true);
			retrieveModel("resources", true);
			
			retrieveModel("health_themes", true);
			retrieveModel("health_topics", true);
			
			retrieveModel("health_pages", true);
			retrieveModel("health_selects", true);
			retrieveModel("topic_videos", true);
			retrieveModel("page_text1", true);
			retrieveModel("page_select1", true);
			retrieveModel("page_video1", true);
			retrieveModel("page_assessment1", true);
			
			retrieveModel("vaccines", true);
			
			// push tables
			retrieveModel("visits", true);
			retrieveModel("health_topics_accessed", true);
			retrieveModel("services_accessed", true);
			retrieveModel("resources_accessed", true);
			retrieveModel("vaccines_recorded", true);
		} catch(UserRecoverableAuthException e){
//			Log.e("SyncAdapter", "in pullAllData uesrexception");
			ContentResolver.cancelSync(null, null);
			Intent intent = e.getIntent();
			appContext.startActivity(intent);
		} catch(Exception e) {
			pullSuccess = false;
			e.printStackTrace();
		}
        
        // change last pull date to current date
		if (pullSuccess == true) {
//			try {
				Log.i("SyncAdapter", "Pull all succeeded. Moving last_synced_at date");
//				ModelHelper.setLastSyncedAt(getHelper(), new Date(), "pull");
				ModelHelper.setLastSyncedAtPush(appContext, new Date());
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		} else {
			Log.e("SyncAdapter", "Pull failed. Not moving last_synced_at date");
		}
		
	}
	
	private void retrieveModel(String modelName, Boolean pullAll) throws UserRecoverableAuthException, Exception {
//		HttpClient httpclient = new DefaultHttpClient();
//        HttpResponse response;
        String responseString = null;
        try {
        	// Executing a get request
        	String baseUrl = appContext.getResources().getString(R.string.base_url);
        	String url = baseUrl.concat(modelName);
        	
        	// append the last_synced_at in the event that we don't want to pull all (the most common use case)
        	if (pullAll == false) {
        		// concat so that only changed documents are getted from the collection
//            	Date d = ModelHelper.getLastSyncedAt(getHelper(), "pull");
            	Date d = ModelHelper.getLastSyncedAtPull(appContext);
        		String lastSync = "?last_synced_at=" + formatDateToJsonDate(d);
        		
        		//TODO: CHANGE for PROD - this is for testing only, pulls everything instead of changed things
            	url = url.concat(lastSync);
            	
            	// modification: for authorization, send every request with parameter clientToken
            	String paramToken = "&client_access_token="+clientToken;
        		url = url.concat(paramToken);
        	}
        	else
        	{
	        	// modification: for authorization, send every request with parameter clientToken
	        	String paramToken = "?client_access_token="+clientToken;
	    		url = url.concat(paramToken);
        	}
        	
        	Log.i("SyncAdapter", "Get to URL: "+url);
        	
//            response = httpclient.execute(new HttpGet(url));
//            StatusLine statusLine = response.getStatusLine();            
//            Log.i("SyncAdapter","Status code: "+statusLine.getStatusCode()+" and status line: "+statusLine.getReasonPhrase());
            
            
            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
            // unsure connection times out after 10 seconds
            urlConnection.setConnectTimeout(10000);
            
            // checking response to see if it worked ok
//            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
            	Log.i("SyncAdapter","Status code: "+urlConnection.getResponseCode()+" and status line: "+urlConnection.getResponseMessage());
            	try {
            		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            		responseString = convertStreamToString(in);
                } finally {
                	urlConnection.disconnect();
                }
            	// all that obvious receiving business
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                response.getEntity().writeTo(out);
//                out.close();
//                responseString = out.toString();
//                Log.i("SyncAdapter", "Response text: \n"+responseString);
                
                // transform response into a JSONArray object
                JSONArray jsonArray = new JSONArray(responseString);
                
                if ("clients" == modelName) {
	                Dao<Client, Integer> clientDao;
	                clientDao = getHelper().getClientsDao();
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject c = jsonArray.getJSONObject(i);
	                	//Log.i("SyncAdapter","JSON object: "+c.toString());
	                	Client client = new Client(c.getInt("_id"), c.getString("first_name"), c.getString("last_name"), c.getInt("hh_id"), c.getString("gender"), parseBirthDateString(c.getString("date_of_birth")), parseDateString(c.getString("created_at")), parseDateString(c.getString("modified_at")));
	                	clientDao.createOrUpdate(client);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more CLIENTS pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new CLIENTS");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
	                
                } else if ("households" == modelName) {
	                Dao<Household, Integer> householdsDao;
	                householdsDao = getHelper().getHouseholdsDao();
	                
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	//Log.i("SyncAdapter","JSON object: "+jo.toString());
	                	Household household = new Household(jo.getInt("_id"), jo.getString("hh_name"), jo.getString("community"), jo.getInt("worker_id"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	householdsDao.createOrUpdate(household);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more HOUSEHOLDS pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new HOUSEHOLDS");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("services" == modelName) {
	                Dao<Service, Integer> servicesDao;
	                servicesDao = getHelper().getServicesDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	//Log.i("SyncAdapter","JSON object: "+jo.toString());
	                	Service service = new Service (jo.getInt("_id"), jo.getString("en_name"), jo.getString("zu_name"), jo.getString("type"), jo.getString("role"), jo.getString("instructions"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	servicesDao.createOrUpdate(service);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more SERVICES pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new SERVICES");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("workers" == modelName) {
	                Dao<Worker, Integer> wDao;
	                wDao = getHelper().getWorkersDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject w = jsonArray.getJSONObject(i);
	                	//Log.i("SyncAdapter","JSON object: "+w.toString());
	            	    Worker worker = new Worker(w.getInt("_id"), w.getString("first_name"), w.getString("last_name"), w.getString("username"), w.getString("password"), w.getString("role_name"), w.getString("assigned_community"), w.getInt("phone_number"), parseDateString(w.getString("created_at")), parseDateString(w.getString("modified_at")));
	            	    wDao.createOrUpdate(worker);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more WORKERS pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new WORKERS");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("videos" == modelName) {
	                Dao<Video, Integer> dao;
	                dao = getHelper().getVideosDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Video o = new Video(jo.getInt("_id"), jo.getString("name"), jo.getString("uri"), jo.getString("screenshot"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(o);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more VIDEOS pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new VIDEOS");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("resources" == modelName) {
	                Dao<Resource, Integer> dao;
	                dao = getHelper().getResourcesDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Resource o = new Resource(jo.getInt("_id"), jo.getString("name"), jo.getString("uri"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(o);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more RESOURCES pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new RESOURCES");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("health_themes" == modelName) {
	                Dao<HealthTheme, Integer> dao;
	                dao = getHelper().getHealthThemesDao();
	                
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	HealthTheme o = new HealthTheme(jo.getInt("_id"), jo.getString("name"), jo.getString("en_observe_content"), jo.getString("en_record_content"), jo.getString("zu_observe_content"), jo.getString("zu_record_content"), jo.getString("color"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(o);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more HEALTH THEMES pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new HEALTH THEMES");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("health_topics" == modelName) {
	                Dao<HealthTopic, Integer> dao;
	                dao = getHelper().getHealthTopicsDao();
	                
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	HealthTopic o = new HealthTopic(jo.getInt("_id"), jo.getString("name"), jo.getString("theme"), jo.getString("screenshot"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(o);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more HEALTH TOPICS pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new HEALTH TOPICS");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("health_pages" == modelName) {
	                Dao<HealthPage, Integer> dao;
	                dao = getHelper().getHealthPagesDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	HealthPage o = new HealthPage(jo.getInt("_id"), jo.getInt("topic_id"), jo.getInt("page_number"), jo.getString("type"), jo.getInt("page_content_id"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(o);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more HEALTH PAGES pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new HEALTH PAGES");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("health_selects" == modelName) {
	                Dao<HealthSelect, Integer> dao;
	                dao = getHelper().getHealthSelectsDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	HealthSelect o = new HealthSelect(jo.getInt("_id"), jo.getInt("subject_id"), jo.getString("en_content"), jo.getString("zu_content"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(o);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more HEALTH SELECTS pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new HEALTH SELECTS");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("topic_videos" == modelName) {
	                Dao<TopicVideo, Integer> dao;
	                dao = getHelper().getTopicVideosDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	TopicVideo o = new TopicVideo(jo.getInt("_id"), jo.getInt("page_video1_id"), jo.getInt("video_id"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(o);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more TOPIC VIDEOS pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new TOPIC VIDEOS");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("page_text1" == modelName) {
	                Dao<PageText1, Integer> dao;
	                dao = getHelper().getPageText1Dao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	PageText1 pa1 = new PageText1(jo.getInt("_id"), jo.getString("en_content1"), jo.getString("zu_content1"), jo.getString("en_content2"), jo.getString("zu_content2"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(pa1);
	                }  
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more PAGE TEXT 1 pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new PAGE TEXT 1");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("page_select1" == modelName) {
	                Dao<PageSelect1, Integer> dao;
	                dao = getHelper().getPageSelect1Dao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	PageSelect1 pa1 = new PageSelect1(jo.getInt("_id"), jo.getString("en_content"), jo.getString("zu_content"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(pa1);
	                }  
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more PAGE SELECT 1 pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new PAGE SELECT 1");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("page_video1" == modelName) {
	                Dao<PageVideo1, Integer> dao;
	                dao = getHelper().getPageVideo1Dao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	PageVideo1 pa1 = new PageVideo1(jo.getInt("_id"), jo.getString("en_content"), jo.getString("zu_content"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(pa1);
	                }  
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more PAGE VIDEO 1 pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new PAGE VIDEO 1");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("page_assessment1" == modelName) {
	                Dao<PageAssessment1, Integer> dao;
	                dao = getHelper().getPageAssessment1Dao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	PageAssessment1 pa1 = new PageAssessment1 (jo.getInt("_id"), jo.getString("type"), jo.getString("en_content1"), jo.getString("zu_content1"), jo.getString("en_content2"), jo.getString("zu_content2"), jo.getString("en_content3"), jo.getString("zu_content3"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(pa1);
	                }  
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more PAGE ASSESSMENT 1 pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new PAGE ASSESSMENT 1");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("vaccines" == modelName) {
	                Dao<Vaccine, Integer> dao;
	                dao = getHelper().getVaccinesDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Vaccine vac = new Vaccine (jo.getInt("_id"), jo.getDouble("age"), jo.getString("display_age"), jo.getString("short_name"), jo.getString("long_name"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.createOrUpdate(vac);
	                }
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more VACCINES pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new VACCINES");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                // the push tables
                } else if ("health_topics_accessed" == modelName) {
	                Dao<HealthTopicAccessed, Integer> dao;
	                dao = getHelper().getHealthTopicAccessedDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	HealthTopicAccessed hta = new HealthTopicAccessed (jo.getInt("_id"), jo.getInt("topic_id"), jo.getInt("visit_id"), jo.getInt("hh_id"), jo.getString("topic_name"), parseDateString(jo.getString("start_time")), parseDateString(jo.getString("end_time")), false);
	                	dao.createOrUpdate(hta);
	                }
	                
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more HEALTH TOPICS ACCESSED pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new HEALTH TOPICS ACCESSED");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
	                
	                // the following are handled by deconstructing the visit object
//                } else if ("services_accessed" == modelName) {
//	                Dao<ServiceAccessed, Integer> dao;
//	                dao = getHelper().getServiceAccessedDao();
//
//	                for (int i=0; i < jsonArray.length(); i++) {
//	                	JSONObject jo = jsonArray.getJSONObject(i);
//	                	ServiceAccessed vac = new Vaccine (jo.getInt("_id"), jo.getInt("service_id"), jo.getInt("visit_id"), jo.getString("client_id"), jo.getString("ad_info"), parseDateString(jo.getString("date")), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
//	                	dao.createOrUpdate(vac);
//	                }
//                } else if ("resources_accessed" == modelName) {
//	                Dao<Vaccine, Integer> dao;
//	                dao = getHelper().getVaccinesDao();
//
//	                for (int i=0; i < jsonArray.length(); i++) {
//	                	JSONObject jo = jsonArray.getJSONObject(i);
//	                	Vaccine vac = new Vaccine (jo.getInt("_id"), jo.getDouble("age"), jo.getString("display_age"), jo.getString("short_name"), jo.getString("long_name"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
//	                	dao.createOrUpdate(vac);
//	                }
                } else if ("vaccines_recorded" == modelName) {
	                Dao<VaccineRecorded, Integer> dao;
	                dao = getHelper().getVaccineRecordedDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	VaccineRecorded vr = new VaccineRecorded (jo.getInt("_id"), jo.getInt("vaccine_id"), jo.getInt("client_id"), jo.getInt("visit_id"), parseDateString(jo.getString("date")), false);
	                	dao.createOrUpdate(vr);
	                }
	                
	                if (jsonArray.length() > 0) {
	                	Log.i("SyncAdapter", "One or more VACCINES RECORDED pulled and created/updated");
	                } else if (jsonArray.length() == 0) {
	                	Log.i("SyncAdapter", "No new VACCINES RECORDED");
	                } else {
	                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
	                }
                } else if ("visits" == modelName) {
	                Dao<Visit, Integer> dao;
	                dao = getHelper().getVisitsDao();

	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	//(int id, int hh_id, int worker_id, String role, String type, double lat, double lon, Date start_time, Date end_time, Boolean newly_created, Boolean dirty)
//	                	Date startTime = null;
	                	Date endTime = null;
	                	if (jo.getString("end_time") != null) {
	                		endTime = parseDateString(jo.getString("end_time"));
	                	}
	                	Visit v = new Visit (jo.getInt("_id"), jo.getInt("hh_id"), jo.getInt("worker_id"), jo.getString("role"), jo.getString("type"), jo.getDouble("lat"), jo.getDouble("lon"), parseDateString(jo.getString("start_time")), endTime, false, false);
	                	populateTablesFromVisitObject(jo);
	                	dao.createOrUpdate(v);
	                	
	                	if (jsonArray.length() > 0) {
		                	Log.i("SyncAdapter", "One or more VISIT OBJECTS pulled and created/updated");
		                } else if (jsonArray.length() == 0) {
		                	Log.i("SyncAdapter", "No new VISIT OBJECTS");
		                } else {
		                	Log.i("SyncAdapter", "jsonArray is null. Could be trouble");
		                }
	                }
                }
            } else{
            	//Closes the connection.
//            	ByteArrayOutputStream out = new ByteArrayOutputStream();
//                response.getEntity().writeTo(out);
//                out.close();
//                responseString = out.toString();
            	int resCode = urlConnection.getResponseCode();
            	String errorMessage = convertStreamToString(urlConnection.getErrorStream());
                Log.e("SyncAdapter", ((Integer)resCode).toString());
                Log.e("SyncAdapter", errorMessage);
                //Closes the connection.
            	urlConnection.disconnect();
            	// set the flag to false so we don't update the lastPulledAt date
            	pullSuccess = false;
//            	response.getEntity().getContent().close();
                
//            	if(urlConnection.getResponseCode() == HttpStatus.SC_UNAUTHORIZED)
            	if(resCode == HttpURLConnection.HTTP_UNAUTHORIZED)
            	{
//            		Log.e("SyncAdapter", "in unauthorized");
            		Intent reAuthIntent = new Intent(appContext,MainActivity.class);
            		reAuthIntent.putExtra(AccountGeneral.ARG_INTENT_REAUTH, true);
            		reAuthIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            		throw new UserRecoverableAuthException("",reAuthIntent);
            	}
            	else
            	{
//            		Log.e("SyncAdapter", "in other status");
            		throw new IOException(errorMessage);
            	}
            }
        } catch (IOException e) {
            //TODO Handle problems..
        	e.printStackTrace();
        } catch (JSONException e) {
        	//TODO Handle problems..
        	e.printStackTrace();
        } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JSONArray createJsonArrayOf(String modelName) {
		JSONArray jsonArray = new JSONArray();
		
		try {
			if ("visits" == modelName) {
				Dao<Visit, Integer> vDao;
				vDao = getHelper().getVisitsDao();
				
				List<Visit> visitsList = vDao.queryBuilder().where().eq("dirty", true).query();
				Iterator<Visit> iterator = visitsList.iterator();
				
				while (iterator.hasNext()) {
					Visit temp = iterator.next();
					//Log.i("SyncAdapter", temp.getType()+", "+temp.getId());
					JSONObject json = new JSONObject();
					json.put("_id", temp.getId());
					json.put("worker_id", temp.getWorkerId());
					json.put("role", temp.getRole());
					json.put("hh_id", temp.getHhId());
					json.put("lat", temp.getLat());
					json.put("lon", temp.getLon());
					json.put("start_time", formatDateToJsonDate(temp.getStartTime()));
					json.put("end_time", formatDateToJsonDate(temp.getEndTime()));
					json.put("type", temp.getType());
					json.put("newly_created", temp.getNewlyCreatedStatus());
					
					updateVisitforOtherModels(json, temp);
					
					// put object into array
					jsonArray.put(json);
				}
				//Log.i("SyncAdapter", "Created visits jsonArray: "+jsonArray.toString());
			} else if ("attendance" == modelName) {
				Dao<Attendance, Integer> attDao;
				attDao = getHelper().getAttendanceDao();
				
				List<Attendance> attList = attDao.queryBuilder().where().eq("newly_created", true).query();
				Iterator<Attendance> iterator = attList.iterator();
				
				while (iterator.hasNext()) {
					Attendance temp = iterator.next();
					JSONObject json = new JSONObject();
					json.put("_id", temp.getId());
					json.put("visit_id", temp.getVisitId());
					json.put("client_id", temp.getClientId());
					json.put("newly_created", temp.getNewlyCreatedStatus());
					
					// put object into array
					jsonArray.put(json);
				}
				//Log.i("SyncAdapter", "Created attendance jsonArray: "+jsonArray.toString());
			} else if ("health_topics_accessed" == modelName) {
				Dao<HealthTopicAccessed, Integer> htaDao;
				htaDao = getHelper().getHealthTopicAccessedDao();
				
				List<HealthTopicAccessed> htaList = htaDao.queryBuilder().where().eq("newly_created", true).query();
				Iterator<HealthTopicAccessed> iterator = htaList.iterator();
				
				while (iterator.hasNext()) {
					HealthTopicAccessed temp = iterator.next();
					JSONObject json = new JSONObject();
					json.put("_id", temp.getId());
					json.put("topic_id", temp.getTopicId());
					json.put("visit_id", temp.getVisitId());
					json.put("hh_id", temp.getHouseholdId());
					json.put("topic_name", temp.getTopicName());
					json.put("start_time", formatDateToJsonDate(temp.getStartTime()));
					json.put("end_time", formatDateToJsonDate(temp.getEndTime()));
					json.put("newly_created", temp.getNewlyCreatedStatus());
					
					// put object into array
					jsonArray.put(json);
				}
				//Log.i("SyncAdapter", "Created healthTopicsAccessed jsonArray: "+jsonArray.toString());
			} else if ("services_accessed" == modelName) {
				Dao<ServiceAccessed, Integer> saDao;
				saDao = getHelper().getServiceAccessedDao();
				
				List<ServiceAccessed> saList = saDao.queryBuilder().where().eq("newly_created", true).query();
				Iterator<ServiceAccessed> iterator = saList.iterator();
				
				while (iterator.hasNext()) {
					ServiceAccessed temp = iterator.next();
					JSONObject json = new JSONObject();
					json.put("_id", temp.getId());
					json.put("service_id", temp.getServiceId());
					json.put("visit_id", temp.getVisitId());
					json.put("client_id", temp.getClientId());
					json.put("ad_info", temp.getAdInfo());
					json.put("date", formatDateToJsonDate(temp.getDate()));
					json.put("newly_created", temp.getNewlyCreatedStatus());
					
					// put object into array
					jsonArray.put(json);
				}
				//Log.i("SyncAdapter", "Created servicesAccessed jsonArray: "+jsonArray.toString());
			} else if ("resources_accessed" == modelName) {
				Dao<ResourceAccessed, Integer> raDao;
				raDao = getHelper().getResourceAccessedDao();
				
				List<ResourceAccessed> raList = raDao.queryBuilder().where().eq("newly_created", true).query();
				Iterator<ResourceAccessed> iterator = raList.iterator();
				
				while (iterator.hasNext()) {
					ResourceAccessed temp = iterator.next();
					JSONObject json = new JSONObject();
					json.put("_id", temp.getId());
					json.put("resource_id", temp.getResourceId());
					json.put("visit_id", temp.getVisitId());
					json.put("worker_id", temp.getWorkerId());
					json.put("date", formatDateToJsonDate(temp.getDate()));
					json.put("newly_created", temp.getNewlyCreatedStatus());
					
					// put object into array
					jsonArray.put(json);
				}
				//Log.i("SyncAdapter", "Created resourcesAccessed jsonArray: "+jsonArray.toString());
			} else if ("vaccines_recorded" == modelName) {
				Dao<VaccineRecorded, Integer> vrDao;
				vrDao = getHelper().getVaccineRecordedDao();
				
				List<VaccineRecorded> raList = vrDao.queryBuilder().where().eq("newly_created", true).query();
				Iterator<VaccineRecorded> iterator = raList.iterator();
				
				while (iterator.hasNext()) {
					VaccineRecorded temp = iterator.next();
					JSONObject json = new JSONObject();
					json.put("_id", temp.getId());
					json.put("vaccine_id", temp.getVaccineId());
					json.put("client_id", temp.getClientId());
					json.put("visit_id", temp.getVisitId());
					json.put("date", formatDateToJsonDate(temp.getDate()));
					json.put("newly_created", temp.getNewlyCreatedStatus());
					
					// put object into array
					jsonArray.put(json);
				}
				//Log.i("SyncAdapter", "Created vaccineRecorded jsonArray: "+jsonArray.toString());
			}
			
		} catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
		} catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		
		return jsonArray;
	}
	
	private void pushModel(String modelName, JSONArray jsonArray) throws UserRecoverableAuthException, Exception {
//		HttpClient httpclient = new DefaultHttpClient();
//        HttpResponse response = null;
        String responseString = null;
        try {
        	// executing a get request
        	String baseUrl = appContext.getResources().getString(R.string.base_url);
        	String url = baseUrl.concat(modelName);
        	// modification: for authorization, send every request with parameter clientToken
        	String paramToken = "?client_access_token="+clientToken;
    		url = url.concat(paramToken);
        	Log.i("SyncAdapter", "Push to URL: "+url);
        	
        	// url with the post data
//            HttpPost httpPost = new HttpPost(url);
//            HttpPut httpPut = new HttpPut(url);
            
            // Go over all objects in jsonArray and POST them individually
            for (int i=0; i<jsonArray.length(); i++) {
            	// retrieve object
            	JSONObject jsonObj = jsonArray.getJSONObject(i);
            	
            	// pass the results to a string builder/entity
//	            StringEntity se = new StringEntity(jsonObj.toString());
	            
	            // handles what is returned from the page 
//	            ResponseHandler responseHandler = new BasicResponseHandler();
	            
            	HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
            	// unsure connection times out after 10 seconds
                urlConnection.setConnectTimeout(10000);
            	
	            // decide on POST or PUT
            	if (jsonObj.getBoolean("newly_created") == true) {
            		// sets the post request as the resulting string
//    	            httpPost.setEntity(se);
//    	            // sets a request header so the page receiving the request will know what to do with it
//    	            httpPost.setHeader("Accept", "application/json");
//    	            httpPost.setHeader("Content-type", "application/json");
//    	            response = httpclient.execute(httpPost);
            		urlConnection.setRequestMethod("POST");
            	} else if (jsonObj.getBoolean("newly_created") == false) {
            		// sets the post request as the resulting string
//    	            httpPut.setEntity(se);
//    	            // sets a request header so the page receiving the request will know what to do with it
//    	            httpPut.setHeader("Accept", "application/json");
//    	            httpPut.setHeader("Content-type", "application/json");
//    	            response = httpclient.execute(httpPut);
            		urlConnection.setRequestMethod("PUT");
            	} else {
            		Log.e("SyncAdapter","jsonObj does not contain a valid posted/unposted attribute");
            	}
	            
	            // checking response to see if it worked ok
//	            StatusLine statusLine = response.getStatusLine();
//	            String statusCode = String.valueOf(statusLine.getStatusCode());
	            	         
            	urlConnection.setDoInput (true);
            	urlConnection.setDoOutput (true);
            	urlConnection.setUseCaches (false);
            	urlConnection.setRequestProperty("Content-Type","application/json");
            	urlConnection.setRequestProperty("Accept", "application/json");
	            
	            //Send request
	            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream ());
	            wr.writeBytes(jsonObj.toString());
	            wr.flush();
	            wr.close();
	            
	            Log.i("SyncAdapter","Status code of post to url "+url+": "+urlConnection.getResponseCode());
	            
//	            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
	            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
	            	Log.i("SyncAdapter","Status code: "+urlConnection.getResponseCode()+" and status line: "+urlConnection.getResponseMessage());
	            	try {
	            		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
	            		responseString = convertStreamToString(in);
	                } finally {
	                	urlConnection.disconnect();
	                }
	            	// all that obvious receiving business
//	                ByteArrayOutputStream out = new ByteArrayOutputStream();
//	                response.getEntity().writeTo(out);
//	                out.close();
//	                responseString = out.toString();
	                
	                if ("visits" == modelName) {
	                	try {
	                		Dao<Visit, Integer> dao;
	        				dao = getHelper().getVisitsDao();
	        				
	        				Visit doc = dao.queryForId(jsonObj.getInt("_id"));
	        				doc.makeClean();
	        				dao.update(doc);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                } else if ("attendance" == modelName) {
	                	try {
	                		Dao<Attendance, Integer> attDao;
	                		attDao = getHelper().getAttendanceDao();
	        				
	                		Attendance doc = attDao.queryForId(jsonObj.getInt("_id"));
	        				doc.makeClean();
	        				attDao.update(doc);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                } else if ("health_topics_accessed" == modelName) {
	                	try {
	                		Dao<HealthTopicAccessed, Integer> htaDao;
	        				htaDao = getHelper().getHealthTopicAccessedDao();
	        				
	        				HealthTopicAccessed doc = htaDao.queryForId(jsonObj.getInt("_id"));
	        				doc.makeClean();
	        				htaDao.update(doc);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                } else if ("services_accessed" == modelName) {
	                	try {
	                		Dao<ServiceAccessed, Integer> saDao;
	        				saDao = getHelper().getServiceAccessedDao();
	        				
	        				ServiceAccessed doc = saDao.queryForId(jsonObj.getInt("_id"));
	        				doc.makeClean();
	        				saDao.update(doc);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                } else if ("resources_accessed" == modelName) {
	                	try {
	                		Dao<ResourceAccessed, Integer> raDao;
	        				raDao = getHelper().getResourceAccessedDao();
	        				
	        				ResourceAccessed doc = raDao.queryForId(jsonObj.getInt("_id"));
	        				doc.makeClean();
	        				raDao.update(doc);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                } else if ("vaccines_recorded" == modelName) {
	                	try {
	                		Dao<VaccineRecorded, Integer> vrDao;
	        				vrDao = getHelper().getVaccineRecordedDao();
	        				
	        				VaccineRecorded doc = vrDao.queryForId(jsonObj.getInt("_id"));
	        				doc.makeClean();
	        				vrDao.update(doc);
	        				
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                } else {
	                	Log.e("SyncAdapter", "Well we pushed "+modelName+" but forgot to write the dirty bit reset code :(");
	                }
	                
	                Log.i("SyncAdapter", "Response text: \n"+responseString);
	            } else {
	            	//Closes the connection.
		            pushSuccess = false;
		            Log.e("SyncAdapter", "pushSuccess set to false");
//	            	ByteArrayOutputStream out = new ByteArrayOutputStream();
//	                response.getEntity().writeTo(out);
//	                out.close();
//	                responseString = out.toString();
//	                Log.e("SyncAdapter", statusLine.getReasonPhrase());
//	                Log.e("SyncAdapter", responseString);
////	            	response.getEntity().getContent().close();
	                
	                int resCode = urlConnection.getResponseCode();
	            	String errorMessage = convertStreamToString(urlConnection.getErrorStream());
	                Log.e("SyncAdapter", ((Integer)resCode).toString());
	                Log.e("SyncAdapter", errorMessage);
	                //Closes the connection.
	                urlConnection.disconnect();
	                
//	            	if(statusLine.getStatusCode() == HttpStatus.SC_UNAUTHORIZED)
	                if(resCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
	            		Intent reAuthIntent = new Intent(appContext,MainActivity.class);
	            		reAuthIntent.putExtra(AccountGeneral.ARG_INTENT_REAUTH, true);
	            		reAuthIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            		throw new UserRecoverableAuthException("",reAuthIntent);
	            	} else {
	            		throw new IOException(errorMessage);
	            	}
	            }

        	}
        } catch (IOException e) {
            //TODO Handle problems..
        } catch (JSONException e) {
        	//TODO Handle problems..
        }
	}
	
	private void updateVisitforOtherModels(JSONObject json, Visit v) throws JSONException, SQLException {
		List<Attendance> attList = ModelHelper.getAttendanceForVisitId(getHelper(), v.getId());
		JSONArray jsonArrAtt = new JSONArray();
		for (Attendance a : attList) {
			jsonArrAtt.put(a.getClientId());
			//Log.i("SyncAdapter", "ClientId: "+ String.valueOf(a.getClientId()));
		}
		json.put("attendance",jsonArrAtt);
		
		Dao<VideoAccessed, Integer> vaDao;
		vaDao = getHelper().getVideosAccessedDao();
		List<VideoAccessed> vaList = vaDao.queryForEq("visit_id", v.getId());
		JSONArray jsonArrVA = new JSONArray();
		for (VideoAccessed va : vaList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("video_id", va.getVideoId());
			jsonObj.put("date", formatDateToJsonDate(va.getDate()));
			jsonArrVA.put(jsonObj);
		}
		json.put("videos_accessed",jsonArrVA);
		
		Dao<HealthSelectRecorded, Integer> hsrDao;
		hsrDao = getHelper().getHealthSelectRecordedDao();
		List<HealthSelectRecorded> hsrList = hsrDao.queryForEq("visit_id", v.getId());
		JSONArray jsonArrHsr = new JSONArray();
		for (HealthSelectRecorded hsr : hsrList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("select_id", hsr.getSelectId());
			jsonObj.put("client_id", hsr.getClientId());
			jsonObj.put("theme", hsr.getTheme());
			jsonObj.put("topic", hsr.getTopic());
			jsonObj.put("date", formatDateToJsonDate(hsr.getDate()));
			jsonArrHsr.put(jsonObj);
		}
		json.put("health_selects_recorded",jsonArrHsr);
		
		Dao<CHAAccessed, Integer> chaaDao;
		chaaDao = getHelper().getCHAAccessedDao();
		List<CHAAccessed> chaaList = chaaDao.queryForEq("visit_id", v.getId());
		JSONArray jsonArrCHAA = new JSONArray();
		for (CHAAccessed chaa : chaaList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("client_id", chaa.getClientId());
			jsonObj.put("type", chaa.getType());
			jsonObj.put("start_time", formatDateToJsonDate(chaa.getStartTime()));
			jsonObj.put("end_time", formatDateToJsonDate(chaa.getEndTime()));			
			jsonArrCHAA.put(jsonObj);
		}
		json.put("cha_accessed",jsonArrCHAA);
		
		Dao<HealthTopicAccessed, Integer> htaDao;
		htaDao = getHelper().getHealthTopicAccessedDao();
		List<HealthTopicAccessed> htaList = htaDao.queryForEq("visit_id", v.getId());
		JSONArray jsonArrHTA = new JSONArray();
		for (HealthTopicAccessed hta : htaList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("topic_id", hta.getTopicId());
			jsonObj.put("topic_name", hta.getTopicName());
			jsonObj.put("start_time", formatDateToJsonDate(hta.getStartTime()));
			jsonObj.put("end_time", formatDateToJsonDate(hta.getEndTime()));			
			jsonArrHTA.put(jsonObj);
		}
		json.put("health_topics_accessed",jsonArrHTA);
	}
	
	private void populateTablesFromVisitObject(JSONObject jo) {
		// maybe we don't need this?
//		Attendance
//		Videos_accessed
//		health_selects_recorded
//		cha_accessed
	}
	
	private Date parseDateString(String input) throws ParseException {
		if (input == null || input.isEmpty()) {
			return null;
		} else {
			//JSON: 2014-02-18T18:04:39.546Z
			//ORM Date: 2014-02-18 18:04:39.555
			//Log.i("SyncAdapter", "dateStr: "+input);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	
			formatter.setTimeZone(TimeZone.getTimeZone("GMT-00:00"));
			//Log.i("SyncAdapter", "formatter: "+formatter);
	        Date convertedDate =  formatter.parse(input);
	        //Log.i("SyncAdapter", "date obj: "+convertedDate.toString());
			return convertedDate;
		}
	}
	
	private Date parseBirthDateString(String input) throws ParseException {
		//JSON: 2014-02-18T18:04:39.546Z
		//ORM Date: 2014-02-18 18:04:39.555
		//Log.i("SyncAdapter", "dateStr: "+input);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	
		formatter.setTimeZone(TimeZone.getDefault());
		//Log.i("SyncAdapter", "formatter: "+formatter);
        Date convertedDate =  formatter.parse(input);
        //Log.i("SyncAdapter", "date obj: "+convertedDate.toString());
		return convertedDate;
	}
	
	private static String formatDateToJsonDate(Date date) {
		String output = "";
		if (date != null) {
			//Log.i("SyncAdapter", "date to convert to string is: "+date.toString());
			
	        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
	        
	        TimeZone tz = TimeZone.getTimeZone( "UTC" );
	        
	        df.setTimeZone( tz );
	
	        output = df.format( date );
	        
	        //Log.i("SyncAdapter", "date String is: "+output);
	
	//        int inset0 = 9;
	//        int inset1 = 6;
	//        
	//        String s0 = output.substring( 0, output.length() - inset0 );
	//        String s1 = output.substring( output.length() - inset1, output.length() );
	//
	//        String result = s0 + s1;
	//
	//        result = result.replaceAll( "UTC", "+00:00" );
	//        
	        return output;
		} else {
			Log.i("SyncAdapter", "Date is empty so return empty string");
			return output;
		}
        
    }
	
	// http://stackoverflow.com/questions/10752919/how-can-i-convert-inputstream-data-to-string-in-android-soap-webservices
	private String convertStreamToString(InputStream is) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}

}
