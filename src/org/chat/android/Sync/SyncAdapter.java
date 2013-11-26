package org.chat.android.Sync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.chat.android.DatabaseHelper;
import org.chat.android.R;
import org.chat.models.Attendance;
import org.chat.models.Worker;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
		String baseUrl = appContext.getResources().getString(R.string.base_url);
		retrieveDataFromServer(baseUrl);
		
	}
	
	private void retrieveDataFromServer(String baseUrl) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
        	// Executing a get request
        	String url = baseUrl.concat("workers");
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
                JSONArray workersJson = new JSONArray(responseString);
                // Database helper for Worker table
                Dao<Worker, Integer> wDao;
                DatabaseHelper workerDbHelper = new DatabaseHelper(appContext);
                wDao = workerDbHelper.getWorkersDao();
                
                // delete all entries
                if (workersJson.length() > 0) {
	                DeleteBuilder<Worker, Integer> deleteWorker = wDao.deleteBuilder();
	                deleteWorker.delete();
                }
                
                // add new entries received via REST call
                for (int i=0; i < workersJson.length(); i++) {
                	JSONObject w = workersJson.getJSONObject(i);
                	// WORKERS
            	    Worker worker = new Worker(w.getInt("_id"), w.getString("first_name"), w.getString("last_name"), w.getString("password"), w.getString("role_name"), w.getString("assigned_community"));
            	    wDao.create(worker);
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

}
