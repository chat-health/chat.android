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
import org.chat.android.models.Client;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.Household;
import org.chat.android.models.PageAssessment1;
import org.chat.android.models.Resource;
import org.chat.android.models.Video;
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
	public void onPerformSync(Account arg0, Bundle arg1, String arg2,
			ContentProviderClient arg3, SyncResult arg4) {
		Log.i("SyncAdapter", "sync adapter running :)");

		//retrieveDataFromServer();
		pushDataToServer();
	}
	
	private void retrieveDataFromServer() {
		Log.i("SyncAdapter", "=================== DATA PULL ===================");
			
		retrieveModel("clients");
		retrieveModel("households");
		retrieveModel("services");
		retrieveModel("workers");
		retrieveModel("videos");
		retrieveModel("resources");
		
		retrieveModel("health_themes");
		
		retrieveModel("health_selects");
		retrieveModel("page_assessment1");
		
        
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
	
	private void pushDataToServer() {
		Log.i("SyncAdapter", "=================== DATA PUSH ===================");
		JSONArray visitsJson = createJsonArrayOf("visits");
		if (visitsJson.length() > 0) {
			pushModel("visits", visitsJson);
		}
		
		JSONArray attendanceJson = createJsonArrayOf("attendance");
		if (attendanceJson.length() > 0) {
			pushModel("attendance", attendanceJson);
		}
		
		if (pushSuccess == true) {
			try {
				ModelHelper.setLastSyncedAt(appContext, new Date(), "push");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.e("SyncAdapter", "Push is failing and we are not moving last_synced_at date.");
		}
	}
	
	private void retrieveModel(String modelName) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
        	// Executing a get request
        	String baseUrl = appContext.getResources().getString(R.string.base_url);
        	String url = baseUrl.concat(modelName);
        	
        	// concat so that only changed documents are getted from the collection
    		GregorianCalendar gc = new GregorianCalendar(2001, 2, 8);
    		Date d = gc.getTime();
    		String lastSync = "?last_synced_at=" + formatDateToJsonDate(d);
    		
    		//TODO: FIXME for PROD - this is for testing only, pulls everything instead of changed things
        	//url = url.concat(lastSync);
        	
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
	                	Client client = new Client(c.getInt("_id"), c.getString("first_name"), c.getString("last_name"), c.getInt("hh_id"), c.getString("gender"), parseBirthDateString(c.getString("date_of_birth")));
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
	                	JSONObject h = jsonArray.getJSONObject(i);
	                	Household household = new Household(h.getInt("_id"), h.getString("hh_name"), h.getString("community"), h.getInt("worker_id"));
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
	                	JSONObject h = jsonArray.getJSONObject(i);
	                	Service service = new Service (h.getInt("_id"), h.getString("name"), h.getString("type"), h.getString("role"), h.getString("instructions"));
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
	            	    Worker worker = new Worker(w.getInt("_id"), w.getString("first_name"), w.getString("last_name"), w.getString("password"), w.getString("role_name"), w.getString("assigned_community"));
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
	                	Video o = new Video(jo.getInt("_id"), jo.getString("name"), jo.getString("uri"), jo.getString("screenshot"));
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
	                	Resource o = new Resource(jo.getInt("_id"), jo.getString("name"), jo.getString("uri"));
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
	                	HealthSelect o = new HealthSelect(jo.getInt("_id"), jo.getInt("subject_id"), jo.getString("en_content"), jo.getString("zu_content"));
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
	                	JSONObject h = jsonArray.getJSONObject(i);
	                	PageAssessment1 pa1 = new PageAssessment1 (h.getInt("_id"), h.getString("type"), h.getString("en_content1"), h.getString("zu_content1"), h.getString("en_content2"), h.getString("zu_content2"), h.getString("en_content3"), h.getString("zu_content3"));
	                	paDao.create(pa1);
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
					
					//addOtherTablesToVisitObject(temp);
					List<Attendance> attList = ModelHelper.getAttendanceForVisitId(appContext, temp.getId());
					JSONArray jsonArrayAtt = new JSONArray();
					for (Attendance a : attList) {
						jsonArrayAtt.put(a.getClientId());
						//Log.i("SyncAdapter", "ClientId: "+ String.valueOf(a.getClientId()));
					}
					json.put("attendance",jsonArrayAtt);
					
					// put object into array
					jsonArray.put(json);
				}
				Log.i("SyncAdapter", "Created visits jsonArray: "+jsonArray.toString());
			}
			
			else if ("attendance" == modelName) {
				Dao<Attendance, Integer> aDao;
				aDao = dbHelper.getAttendanceDao();
				
				List<Attendance> attendanceList = aDao.queryForEq("dirty", true);
				Iterator<Attendance> iterator = attendanceList.iterator();
				
				while (iterator.hasNext()) {
					Attendance a = iterator.next();
					//Log.i("SyncAdapter", a.getVisitId()+", "+a.getClientId());
					JSONObject json = new JSONObject();
					json.put("_id", a.getId());
					json.put("visit_id", a.getVisitId());
					json.put("client_id", a.getClientId());
					// put object into array
					jsonArray.put(json);
				}
				Log.i("SyncAdapter", "Created attendance jsonArray: "+jsonArray.toString());
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
	            
	            Log.i("SyncAdapter", "1: "+jsonObj.toString());
	            
	            if (jsonObj.getBoolean("newly_created") == true) {
	            	Log.i("SyncAdapter", "Boolean is true");
	            } else if (jsonObj.getBoolean("newly_created") == false) {
	            	Log.i("SyncAdapter", "Boolean is false");
	            } else {
	            	Log.i("SyncAdapter", "Boolean is unknown");
	            }
            	
            	if (jsonObj.getBoolean("newly_created") == true) {
    	            Log.i("SyncAdapter", "2");
            		// sets the post request as the resulting string
    	            httpPost.setEntity(se);
    	            // sets a request header so the page receiving the request will know what to do with it
    	            httpPost.setHeader("Accept", "application/json");
    	            httpPost.setHeader("Content-type", "application/json");
    	            response = httpclient.execute(httpPost);
            	} else if (jsonObj.getBoolean("newly_created") == false) {
            		Log.i("SyncAdapter", "2");
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
	            	Log.i("SyncAdapter", "3");
	            	// all that obvious receiving business
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	                
	                if ("attendance" == modelName) {
	                	try {
	                		Dao<Attendance, Integer> dao;
	        				dao = dbHelper.getAttendanceDao();
	        				
	        				Attendance doc = dao.queryForId(jsonObj.getInt("_id"));
	        				doc.makeClean();
	        				dao.update(doc);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                } else if ("visits" == modelName) {
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
	                } else {
	                	Log.e("SyncAdapter", "Well we pushed "+modelName+" but forgot to write the dirty bit reset code :(");
	                }
	                
	                Log.i("SyncAdapter", "Response text: \n"+responseString);
	            } else{
	                // closes the connection.
	            	// set the flag to false so we don't update the lastPulledAt date
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
