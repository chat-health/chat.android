package org.chat.android;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
		    	new RequestTask().execute(editText1.getText().toString());
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
        	String responseString = "";
        	boolean downloadSuccess = false;
        	try {
        		HttpURLConnection urlConnection = (HttpURLConnection) new URL(uri[0]).openConnection();
	            // make connection time out after 10 seconds
	            urlConnection.setConnectTimeout(10000);
	            
	            // checking response to see if it worked ok
	            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
	            	Log.i("SyncResourcesActivity","Status code: "+urlConnection.getResponseCode()+" and status line: "+urlConnection.getResponseMessage());
	            	try {
	            		InputStream in = new BufferedInputStream(urlConnection.getInputStream(), 8192);	            	
//	            		responseString = convertStreamToString(in);
	            		downloadSuccess = storeInputStreamInFile(in, urlConnection.getContentLength());
	                }
	                finally {	                	
	                	urlConnection.disconnect();
	                }
	            }
        	} catch (IOException e) {
        		e.printStackTrace();
            }
        	
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpResponse response;
//            String responseString = null;
//            try {
//                response = httpclient.execute(new HttpGet(uri[0]));
//                StatusLine statusLine = response.getStatusLine();
//                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    response.getEntity().writeTo(out);
//                    out.close();
//                    responseString = out.toString();
//                } else{
//                    //Closes the connection.
//                    response.getEntity().getContent().close();
//                    throw new IOException(statusLine.getReasonPhrase());
//                }
//            } catch (ClientProtocolException e) {
//                //TODO Handle problems..
//            } catch (IOException e) {
//                //TODO Handle problems..
//            }
        	if (downloadSuccess) {
        		return "Download success";
        	} else {
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
    	private boolean storeInputStreamInFile(InputStream input, int lengthOfFile) {
    		int count;
            try {
                // download the file
//                InputStream input = new BufferedInputStream(url.openStream(),
//                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/testy.mp4");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                
                return true;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return false;
            }
    	}
    }
}


