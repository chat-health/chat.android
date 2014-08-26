package org.chat.android;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.chat.android.Auth.AccountGeneral;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SyncResourcesActivity extends Activity {
	private TextView textView1;
	private EditText editText1;
	private String clientToken;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syncresources);
        textView1 = (TextView) findViewById(R.id.textView1);
        editText1 = (EditText) findViewById(R.id.editText1);
    }
    
    /** Called when the user selects the Send button */
    public void sendReq(View view) {
    	textView1.setText("");
    	
    	ConnectivityManager connMgr = (ConnectivityManager) 
    	        getSystemService(Context.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    
	    if (networkInfo != null && networkInfo.isConnected()) {
	    	// ensure we only download big files if we are connected via WiFi
	    	// http://developer.android.com/reference/android/net/NetworkInfo.html#getType%28%29
	    	if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
		        // fetch data
	    		if (editText1.getText().toString().isEmpty()) {
	    			new RequestTask().execute(getApplicationContext().getResources().getString(R.string.base_url));
	    		} else {
	    			new RequestTask().execute(editText1.getText().toString());	    			
	    		}
	    	} else {
	    		textView1.setText("Not connected via WiFi. Aborting download");
	    	}
	    } else {
	        // display error
	    	textView1.setText("No network connection active");
	    } 
    }
    
    private class RequestTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... uri) {
//        	String responseString = "";
        	boolean downloadSuccess = false;
        	String urlAssets = uri[0] + "/assets/";
        	urlAssets = urlAssets.replaceAll("(?<!(http:|https:))//", "/");
        	
        	String paramToken = "?client_access_token="+clientToken;
        	urlAssets = urlAssets.concat(paramToken);
        	
        	try {
        		// retrieve list of all videos
        		HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlAssets).openConnection();       		       		
        		// unsure connection times out after 10 seconds
                urlConnection.setConnectTimeout(10000);
                ArrayList<String> videoFileNames = new ArrayList<String>();
                
                // checking response to see if it worked ok
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
//                	Log.i("SyncAdapter","Status code: "+urlConnection.getResponseCode()+" and status line: "+urlConnection.getResponseMessage());
                	try {
                		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                		String videos = convertStreamToString(in);
                		JSONArray jsonArray = new JSONArray(videos);
                		// put JSONArray into a ArrayList
                    	for(int i = 0, count = jsonArray.length(); i< count; i++)
                    	{
                    	    try {
                    	    	videoFileNames.add((String)jsonArray.get(i));
                    	    }
                    	    catch (JSONException e) {
                    	        e.printStackTrace();
                    	    }
                    	}
//                        videoFileNames = convertJSONtoStringArray(jsonArray);
                    } catch (JSONException e) {						
						e.printStackTrace();
					} 
                	finally {
                    	urlConnection.disconnect();
                    }
                }
                
                downloadSuccess = true;
                Iterator<String> iter = videoFileNames.iterator();
                // download all video files in the list of video file names
                while (iter.hasNext() && downloadSuccess) {
                	String videoFile = iter.next();
                	
                	String urlAsset = uri[0] + "/asset/"+videoFile;
                	urlAsset = urlAsset.replaceAll("(?<!(http:|https:))//", "/");
                	urlAsset = urlAsset.concat(paramToken);
                	
                	urlConnection = (HttpURLConnection) new URL(urlAsset).openConnection();
    	            // make connection time out after 10 seconds
    	            urlConnection.setConnectTimeout(10000);
    	            
    	            // checking response to see if it worked ok
    	            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
    	            	Log.i("SyncResourcesActivity","Status code: "+urlConnection.getResponseCode()+" and status line: "+urlConnection.getResponseMessage());
    	            	try {
    	            		InputStream in = new BufferedInputStream(urlConnection.getInputStream(), 8192);	            	
//    	            		responseString = convertStreamToString(in);
//    	            		Log.i("File Download", urlConnection.getHeaderField("Content-Disposition"));
    	            		downloadSuccess = storeInputStreamInFile(in, videoFile,urlConnection.getContentLength());
    	                }
    	                finally {	                	
    	                	urlConnection.disconnect();
    	                }
    	            }
                }
                
                
                if (downloadSuccess) {
            		return "Download success";
            	} else {
            		return "Download failure";
            	}
        	} catch (IOException e) {
        		e.printStackTrace();
        		return "Download failure";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            textView1.setText(result);

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
    	
    	// this will be useful so that you can show a tipical 0-100%
        // progress bar
    	private boolean storeInputStreamInFile(InputStream input, String fileName, int lengthOfFile) {
    		int count;
            try {
            	File videoFile = new File(Environment.getExternalStorageDirectory().toString() +"/chat/" +fileName);
            	
                // Output stream
                OutputStream output = new FileOutputStream(videoFile);

                byte data[] = new byte[8192];
                long downloadedSize = 0;
                int percentDownloaded;
                int previous = -1;
                
                Log.i("SyncResources", "Downloading to file: "+videoFile.getAbsolutePath());

                while ((count = input.read(data)) > 0) {
                	downloadedSize += count;
                    //this is where you would do something to report the prgress, like this maybe
                	percentDownloaded = (int) ((downloadedSize * 100) / lengthOfFile);
                	if (percentDownloaded % 10 == 0 && percentDownloaded > previous) {
                		Log.d("Progress: ","downloaded "+percentDownloaded+" percent of "+ lengthOfFile + " Bytes");
                		previous = percentDownloaded;
                	}

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                
                Log.i("SyncResources", "Finished downloading to file: "+videoFile.getAbsolutePath());
                
                return true;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return false;
            }
    	}
    }
    
    private ArrayList<String> convertJSONtoStringArray (JSONArray jsonArray) {
    	ArrayList<String> stringArray = new ArrayList<String>();
    	for(int i = 0, count = jsonArray.length(); i< count; i++)
    	{
    	    try {
    	        stringArray.add((String)jsonArray.get(i));
    	    }
    	    catch (JSONException e) {
    	        e.printStackTrace();
    	    }
    	}
    	return stringArray;
    }
}


