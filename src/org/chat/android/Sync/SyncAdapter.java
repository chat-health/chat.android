package org.chat.android.Sync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.chat.android.DatabaseHelper;
import org.chat.android.ModelHelper;
import org.chat.android.R;
import org.chat.android.models.Attendance;
import org.chat.android.models.CHAAccessed;
import org.chat.android.models.Client;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.HealthTopic;
import org.chat.android.models.HealthTopicAccessed;
import org.chat.android.models.Household;
import org.chat.android.models.PageAssessment1;
import org.chat.android.models.Resource;
import org.chat.android.models.ResourceAccessed;
import org.chat.android.models.ServiceAccessed;
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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

	// Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Context appContext;
    // if we get any error codes back in any of the pushes, set to false (very strict, but necessary)
    Boolean pullSuccess = true;
    Boolean pushSuccess = true;
    DatabaseHelper dbHelper;
    
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
        dbHelper = new DatabaseHelper(appContext);
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
        dbHelper = new DatabaseHelper(appContext);
    }
    
	@Override
	public void onPerformSync(Account arg0, Bundle arg1, String arg2, ContentProviderClient arg3, SyncResult arg4) {
		Log.i("SyncAdapter", "sync adapter running :)");
		
		// if syncType is pullAll we want to trigger a 'pull all of everything ever' sync
		// TODO: clean me up
		String syncType = "standard";
		Bundle b = arg1;
		syncType = b.getString("syncType");
		if (syncType == null) {
			syncType = "standard";
		}
		Log.i("SyncAdapter", "syncType: "+syncType);
		
		if (syncType.equals("pullAll")) {
			Log.i("SyncAdapter", "We are going nuclear - pulling everything");
			pullAllData();
		} else if (syncType.equals("standard")) {
			Log.i("SyncAdapter", "Just a regular pull");
			//pullNewData();
			//pushNewData();
		} else {
			Log.e("SyncAdapter", "Impossible error. syncType str is unknown");
		}
	}
	
	private void pullNewData() {
		Log.i("SyncAdapter", "=================== DATA PULL ===================");
			
		retrieveModel("clients", false);
		retrieveModel("households", false);
		retrieveModel("services", false);
		retrieveModel("workers", false);
		retrieveModel("videos", false);
		retrieveModel("resources", false);
		
		retrieveModel("health_themes", false);
		retrieveModel("health_topics", false);
		
		retrieveModel("health_selects", false);
		retrieveModel("page_assessment1", false);
		
		retrieveModel("vaccines", false);
		
        
        // change last pull date to current date
		if (pullSuccess == true) {
			try {
				ModelHelper.setLastSyncedAt(appContext, new Date(), "pull");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void pushNewData() {
		Log.i("SyncAdapter", "=================== DATA PUSH ===================");
		JSONArray visitsJson = createJsonArrayOf("visits");
		if (visitsJson.length() > 0) {
			pushModel("visits", visitsJson);
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
		
		if (pushSuccess == true) {
			try {
				ModelHelper.setLastSyncedAt(appContext, new Date(), "push");
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
		
		
		retrieveModel("clients", true);
		retrieveModel("households", true);
		retrieveModel("services", true);
		retrieveModel("workers", true);
		retrieveModel("videos", true);
		retrieveModel("resources", true);
		
		retrieveModel("health_themes", true);
		retrieveModel("health_topics", true);
		
		retrieveModel("health_selects", true);
		retrieveModel("page_assessment1", true);
		
		retrieveModel("vaccines", true);
		
        
        // change last pull date to current date
		if (pullSuccess == true) {
			try {
				ModelHelper.setLastSyncedAt(appContext, new Date(), "pull");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void retrieveModel(String modelName, Boolean pullAll) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
        	// Executing a get request
        	String baseUrl = appContext.getResources().getString(R.string.base_url);
        	String url = baseUrl.concat(modelName);
        	
        	if (pullAll == false) {
        		// concat so that only changed documents are getted from the collection
            	Date d = ModelHelper.getLastSyncedAt(appContext, "pull");
        		//GregorianCalendar gc = new GregorianCalendar(2001, 2, 8);
        		//Date d = gc.getTime();
        		String lastSync = "?last_synced_at=" + formatDateToJsonDate(d);
        		
        		//TODO: CHANGE for PROD - this is for testing only, pulls everything instead of changed things
            	url = url.concat(lastSync);
        	}
        	
        	Log.i("SyncAdapter", "Get to URL: "+url);
        	
            response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();            
            Log.i("SyncAdapter","Status code: "+statusLine.getStatusCode()+" and status line: "+statusLine.getReasonPhrase());
            
            // checking response to see if it worked ok
            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
            	// all that obvious receiving business
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                Log.i("SyncAdapter", "Response text: \n"+responseString);
                
                // transform response into a JSONArray object
                JSONArray jsonArray = new JSONArray(responseString);
                
                if ("clients" == modelName) {
	                Dao<Client, Integer> clientDao;
	                clientDao = dbHelper.getClientsDao();
	                
	                // delete all entries
	                if (jsonArray.length() > 0) {
		                DeleteBuilder<Client, Integer> deleteWorker = clientDao.deleteBuilder();
		                deleteWorker.delete();
	                }
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject c = jsonArray.getJSONObject(i);
	                	// Clients
	                	Client client = new Client(c.getInt("_id"), c.getString("first_name"), c.getString("last_name"), c.getInt("hh_id"), c.getString("gender"), parseBirthDateString(c.getString("date_of_birth")), parseDateString(c.getString("created_at")), parseDateString(c.getString("modified_at")));
	            	    int numCreated = clientDao.create(client);
	            	    
	            	    if (numCreated != 1) {
	            	    	Log.i("SyncAdapter", "create seem to have failed trying update ");
	            	    	clientDao.update(client);
	            	    }
	                }
                } else if ("households" == modelName) {
	                Dao<Household, Integer> householdsDao;
	                householdsDao = dbHelper.getHouseholdsDao();
	                
	                // delete all entries
	                if (jsonArray.length() > 0) {
		                DeleteBuilder<Household, Integer> deleteHousehold = householdsDao.deleteBuilder();
		                deleteHousehold.delete();
	                }
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Household household = new Household(jo.getInt("_id"), jo.getString("hh_name"), jo.getString("community"), jo.getInt("worker_id"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	householdsDao.create(household);
	                }
                } else if ("services" == modelName) {
	                Dao<Service, Integer> servicesDao;
	                servicesDao = dbHelper.getServicesDao();
	                
	                // delete all entries
	                if (jsonArray.length() > 0) {
		                DeleteBuilder<Service, Integer> deleteHousehold = servicesDao.deleteBuilder();
		                deleteHousehold.delete();
	                }
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Service service = new Service (jo.getInt("_id"), jo.getString("name"), jo.getString("type"), jo.getString("role"), jo.getString("instructions"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	servicesDao.create(service);
	                }
                } if ("workers" == modelName) {
	                Dao<Worker, Integer> wDao;
	                wDao = dbHelper.getWorkersDao();
	                
	                // delete all entries
	                if (jsonArray.length() > 0) {
		                DeleteBuilder<Worker, Integer> deleteWorker = wDao.deleteBuilder();
		                deleteWorker.delete();
	                }
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject w = jsonArray.getJSONObject(i);
	                	// WORKERS
	            	    Worker worker = new Worker(w.getInt("_id"), w.getString("first_name"), w.getString("last_name"), w.getString("password"), w.getString("role_name"), w.getString("assigned_community"), parseDateString(w.getString("created_at")), parseDateString(w.getString("modified_at")));
	            	    wDao.create(worker);
	                }
                } else if ("videos" == modelName) {
	                Dao<Video, Integer> dao;
	                dao = dbHelper.getVideosDao();
	                
	                // delete all entries
	                if (jsonArray.length() > 0) {
		                DeleteBuilder<Video, Integer> deleter = dao.deleteBuilder();
		                deleter.delete();
	                }
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Video o = new Video(jo.getInt("_id"), jo.getString("name"), jo.getString("uri"), jo.getString("screenshot"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.create(o);
	                }
                } else if ("resources" == modelName) {
	                Dao<Resource, Integer> dao;
	                dao = dbHelper.getResourcesDao();
	                
	                // delete all entries
	                if (jsonArray.length() > 0) {
		                DeleteBuilder<Resource, Integer> deleter = dao.deleteBuilder();
		                deleter.delete();
	                }
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Resource o = new Resource(jo.getInt("_id"), jo.getString("name"), jo.getString("uri"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.create(o);
	                }
                } else if ("health_themes" == modelName) {
	                Dao<HealthTheme, Integer> dao;
	                dao = dbHelper.getHealthThemeDao();
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Log.i("SyncAdapter","JSON object: "+jo.toString());
	                	HealthTheme o = new HealthTheme(jo.getInt("_id"), jo.getString("name"), jo.getString("en_observe_content"), jo.getString("en_record_content"), jo.getString("zu_observe_content"), jo.getString("zu_record_content"), jo.getString("color"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	// create or update data sets received from backend server
	                	dao.createOrUpdate(o);
	                }
                } else if ("health_topics" == modelName) {
	                Dao<HealthTopic, Integer> dao;
	                dao = dbHelper.getHealthTopicsDao();
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Log.i("SyncAdapter","JSON object: "+jo.toString());
	                	HealthTopic o = new HealthTopic(jo.getInt("_id"), jo.getString("name"), jo.getString("theme"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	// create or update data sets received from backend server
	                	dao.createOrUpdate(o);
	                }
                } else if ("health_selects" == modelName) {
	                Dao<HealthSelect, Integer> dao;
	                dao = dbHelper.getHealthSelectDao();
	                
	                // delete all entries
	                if (jsonArray.length() > 0) {
		                DeleteBuilder<HealthSelect, Integer> deleter = dao.deleteBuilder();
		                deleter.delete();
	                }
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	HealthSelect o = new HealthSelect(jo.getInt("_id"), jo.getInt("subject_id"), jo.getString("en_content"), jo.getString("zu_content"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	dao.create(o);
	                }
                } else if ("page_assessment1" == modelName) {
	                Dao<PageAssessment1, Integer> paDao;
	                paDao = dbHelper.getPageAssessment1Dao();
	                
	                // delete all entries
	                if (jsonArray.length() > 0) {
		                DeleteBuilder<PageAssessment1, Integer> deletePA1 = paDao.deleteBuilder();
		                deletePA1.delete();
	                }
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	PageAssessment1 pa1 = new PageAssessment1 (jo.getInt("_id"), jo.getString("type"), jo.getString("en_content1"), jo.getString("zu_content1"), jo.getString("en_content2"), jo.getString("zu_content2"), jo.getString("en_content3"), jo.getString("zu_content3"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	paDao.create(pa1);
	                }
                } else if ("vaccines" == modelName) {
	                Dao<Vaccine, Integer> vacDao;
	                vacDao = dbHelper.getVaccineDao();
	                
	                // delete all entries
	                if (jsonArray.length() > 0) {
		                DeleteBuilder<Vaccine, Integer> deleteVac = vacDao.deleteBuilder();
		                deleteVac.delete();
	                }
	                
	                // add new entries received via REST call
	                for (int i=0; i < jsonArray.length(); i++) {
	                	JSONObject jo = jsonArray.getJSONObject(i);
	                	Vaccine vac = new Vaccine (jo.getInt("_id"), jo.getDouble("age"), jo.getString("display_age"), jo.getString("short_name"), jo.getString("long_name"), parseDateString(jo.getString("created_at")), parseDateString(jo.getString("modified_at")));
	                	vacDao.create(vac);
	                }
                }
            } else{
                //Closes the connection.
            	Log.e("SyncAdapter", statusLine.getReasonPhrase());
            	// set the flag to false so we don't update the lastPulledAt date
            	pullSuccess = false;
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        	e.printStackTrace();
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
				vDao = dbHelper.getVisitsDao();
				
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
				Log.i("SyncAdapter", "Created visits jsonArray: "+jsonArray.toString());
			} else if ("health_topics_accessed" == modelName) {
				Dao<HealthTopicAccessed, Integer> htaDao;
				htaDao = dbHelper.getHealthTopicsAccessedDao();
				
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
				Log.i("SyncAdapter", "Created servicesAccessed jsonArray: "+jsonArray.toString());
			} else if ("services_accessed" == modelName) {
				Dao<ServiceAccessed, Integer> saDao;
				saDao = dbHelper.getServiceAccessedDao();
				
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
				Log.i("SyncAdapter", "Created servicesAccessed jsonArray: "+jsonArray.toString());
			} else if ("resources_accessed" == modelName) {
				Dao<ResourceAccessed, Integer> raDao;
				raDao = dbHelper.getResourceAccessedDao();
				
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
				Log.i("SyncAdapter", "Created resourcesAccessed jsonArray: "+jsonArray.toString());
			} else if ("vaccines_recorded" == modelName) {
				Dao<VaccineRecorded, Integer> vrDao;
				vrDao = dbHelper.getVaccineRecordedDao();
				
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
				Log.i("SyncAdapter", "Created vaccineRecorded jsonArray: "+jsonArray.toString());
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
	
	private void pushModel(String modelName, JSONArray jsonArray) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        String responseString = null;
        try {
        	// executing a get request
        	String baseUrl = appContext.getResources().getString(R.string.base_url);
        	String url = baseUrl.concat(modelName);
        	Log.i("SyncAdapter", "Push to URL: "+url);
        	
        	// url with the post data
            HttpPost httpPost = new HttpPost(url);
            HttpPut httpPut = new HttpPut(url);
            
            // Go over all objects in jsonArray and POST them individually
            for (int i=0; i<jsonArray.length(); i++) {
            	// retrieve object
            	JSONObject jsonObj = jsonArray.getJSONObject(i);
            	
            	// pass the results to a string builder/entity
	            StringEntity se = new StringEntity(jsonObj.toString());
	            
	            // handles what is returned from the page 
	            ResponseHandler responseHandler = new BasicResponseHandler();
            	
	            // decide on POST or PUT
            	if (jsonObj.getBoolean("newly_created") == true) {
            		// sets the post request as the resulting string
    	            httpPost.setEntity(se);
    	            // sets a request header so the page receiving the request will know what to do with it
    	            httpPost.setHeader("Accept", "application/json");
    	            httpPost.setHeader("Content-type", "application/json");
    	            response = httpclient.execute(httpPost);
            	} else if (jsonObj.getBoolean("newly_created") == false) {
            		// sets the post request as the resulting string
    	            httpPut.setEntity(se);
    	            // sets a request header so the page receiving the request will know what to do with it
    	            httpPut.setHeader("Accept", "application/json");
    	            httpPut.setHeader("Content-type", "application/json");
    	            response = httpclient.execute(httpPut);
            	} else {
            		Log.e("SyncAdapter","jsonObj does not contain a valid posted/unposted attribute");
            	}
	            
	            // checking response to see if it worked ok
	            StatusLine statusLine = response.getStatusLine();
	            String statusCode = String.valueOf(statusLine.getStatusCode());
	            
	            Log.i("SyncAdapter","Status code of post to url "+url+": "+statusCode);
	            
	            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
	            	// all that obvious receiving business
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	                
	                if ("visits" == modelName) {
	                	try {
	                		Dao<Visit, Integer> dao;
	        				dao = dbHelper.getVisitsDao();
	        				
	        				Visit doc = dao.queryForId(jsonObj.getInt("_id"));
	        				doc.makeClean();
	        				dao.update(doc);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                } else if ("health_topics_accessed" == modelName) {
	                	try {
	                		Dao<HealthTopicAccessed, Integer> htaDao;
	        				htaDao = dbHelper.getHealthTopicsAccessedDao();
	        				
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
	        				saDao = dbHelper.getServiceAccessedDao();
	        				
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
	        				raDao = dbHelper.getResourceAccessedDao();
	        				
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
	        				vrDao = dbHelper.getVaccineRecordedDao();
	        				
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
	                // closes the connection.
	            	// set the flag to false so we don't update the lastPulledAt date
	            	Log.e("SyncAdapter", "pushSuccess set to false");
	            	pushSuccess = false;
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
        	}
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        } catch (JSONException e) {
        	//TODO Handle problems..
        }
	}
	
	private void updateVisitforOtherModels(JSONObject json, Visit v) throws JSONException, SQLException {
		List<Attendance> attList = ModelHelper.getAttendanceForVisitId(appContext, v.getId());
		JSONArray jsonArrAtt = new JSONArray();
		for (Attendance a : attList) {
			jsonArrAtt.put(a.getClientId());
			//Log.i("SyncAdapter", "ClientId: "+ String.valueOf(a.getClientId()));
		}
		json.put("attendance",jsonArrAtt);
		
//		Dao<HealthTopicAccessed, Integer> htaDao;
//		htaDao = dbHelper.getHealthTopicsAccessedDao();
//		List<HealthTopicAccessed> htaList = htaDao.queryForEq("visit_id", v.getId());
//		JSONArray jsonArrHTA = new JSONArray();
//		for (HealthTopicAccessed hta : htaList) {
//			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("topic_id", hta.getTopicId());
//			jsonObj.put("topic_name", hta.getTopicName());
//			jsonObj.put("start_time", formatDateToJsonDate(hta.getStartTime()));
//			jsonObj.put("end_time", formatDateToJsonDate(hta.getEndTime()));
//			jsonArrHTA.put(jsonObj);
//		}
//		json.put("health_topics_accessed",jsonArrHTA);
		
		Dao<VideoAccessed, Integer> vaDao;
		vaDao = dbHelper.getVideosAccessedDao();
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
		hsrDao = dbHelper.getHealthSelectRecordedDao();
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
		chaaDao = dbHelper.getCHAAccessedDao();
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
		
//		Dao<VaccineRecorded, Integer> vrDao;
//		vrDao = dbHelper.getVaccineRecordedDao();
//		List<VaccineRecorded> vrList = vrDao.queryForEq("visit_id", v.getId());
//		JSONArray jsonArrVR = new JSONArray();
//		for (VaccineRecorded vr : vrList) {
//			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("vaccine_id", vr.getVaccineId());
//			jsonObj.put("client_id", vr.getClientId());
//			jsonObj.put("date", formatDateToJsonDate(vr.getDate()));
//			jsonArrCHAA.put(jsonObj);
//		}
//		json.put("vaccines_recorded",jsonArrVR);

	}
	
	private Date parseDateString(String input) throws ParseException {
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

}
