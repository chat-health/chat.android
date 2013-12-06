package org.chat.android.Sync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.chat.android.DatabaseHelper;
import org.chat.android.R;
import org.chat.android.models.Attendance;
import org.chat.android.models.Client;
import org.chat.android.models.Household;
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
    }
    
	@Override
	public void onPerformSync(Account arg0, Bundle arg1, String arg2,
			ContentProviderClient arg3, SyncResult arg4) {
		Log.i("SyncAdapter", "sync adapter running :)");
		
		// 1) retrieve data from server, add new, handle conflict
		

		retrieveDataFromServer();
		pushDataToServer();
	}
	
	private void retrieveDataFromServer() {
		retrieveModel("workers");
		retrieveModel("clients");
		retrieveModel("households");
		retrieveModel("services");
	}
	
	private void pushDataToServer() {
		JSONArray visitsJson = createJsonArrayOf("visits");
		pushModel("visits", visitsJson);
	}
	
	private void retrieveModel(String modelName) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
        	// Executing a get request
        	String baseUrl = appContext.getResources().getString(R.string.base_url);
        	String url = baseUrl.concat(modelName);
        	Log.i("SyncAdapter", "Get to URL: "+url);
        	
            response = httpclient.execute(new HttpGet(url));
            // checking response to see if it worked ok
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
            	// all that obvious receiving business
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                Log.i("SyncAdapter", "Response text: \n"+responseString);
                
                // transform response into a JSONArray object
                JSONArray jsonArray = new JSONArray(responseString);
                // Database helper for Worker table
                DatabaseHelper dbHelper = new DatabaseHelper(appContext);
                
                if ("workers" == modelName) {
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
                } else if ("clients" == modelName) {
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
	                	Client client = new Client(c.getInt("_id"), c.getString("first_name"), c.getString("last_name"), c.getInt("hh_id"), c.getString("gender"));
	            	    clientDao.create(client);
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
	                	Service service = new Service (h.getInt("_id"), h.getString("name"), h.getString("type"), h.getString("role"));
	                	servicesDao.create(service);
	                }
                }
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        } catch (JSONException e) {
        	//TODO Handle problems..
        } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	}
	
	private JSONArray createJsonArrayOf(String modelName) {
		JSONArray jsonArray = new JSONArray();
		
		try {
			DatabaseHelper dbHelper = new DatabaseHelper(appContext);
			
			if ("visits" == modelName) {
				Dao<Visit, Integer> vDao;
				vDao = dbHelper.getVisitsDao();
				
				// TODO: Here we select all with type home. Of course we want everything that needs to be synced.
				// One way would be to set a 'dirty' flag whenever data is written and then query for this here. Then send only changed/ new stuff.
				List<Visit> visitsList = vDao.queryBuilder().where().eq("type", "home").query();
				Iterator<Visit> iterator = visitsList.iterator();
				
				while (iterator.hasNext()) {
					Visit temp = iterator.next();
					Log.i("SyncAdapter", temp.getType()+", "+temp.getId());
					JSONObject json = new JSONObject();
					json.put("id", temp.getId());
					json.put("type", temp.getType());
					// put object into array
					jsonArray.put(json);
				}
				Log.i("SyncAdapter", jsonArray.toString());
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
        HttpResponse response;
        String responseString = null;
        try {
        	// Executing a get request
        	String baseUrl = appContext.getResources().getString(R.string.base_url);
        	String url = baseUrl.concat(modelName);
        	Log.i("SyncAdapter", "Push to URL: "+url);
        	
        	//url with the post data
            HttpPost httpost = new HttpPost(url);
            
            //passes the results to a string builder/entity
            StringEntity se = new StringEntity(jsonArray.toString());

            //sets the post request as the resulting string
            httpost.setEntity(se);
            //sets a request header so the page receving the request
            //will know what to do with it
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            //Handles what is returned from the page 
            ResponseHandler responseHandler = new BasicResponseHandler();
//            return httpclient.execute(httpost, responseHandler);
        	
            response = httpclient.execute(httpost);
            // checking response to see if it worked ok
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
            	// all that obvious receiving business
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                Log.i("SyncAdapter", "Response text: \n"+responseString);
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
	}

}
