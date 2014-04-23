package org.chat.android.Auth;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ChatAuthServerAuthenticate implements ServerAuthenticate {
	
	private static final String AUTH_PATH = "auth";
	private static final String ARG_CREDENTIAL="?google_access_token=";
	private static final String ARG_DEVICE_ID = "&deviceid=";
	private static final String ARG_CLIENT_ACCESS_TOKEN = "client_access_token";
	private String base_url;
	
	public ChatAuthServerAuthenticate(String base_url) {
		super();
		this.base_url = base_url;
	}



	@Override
	public String getAccessToken(String email, String password, String deviceid) {
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        HttpURLConnection conn = null;
        
        String url = base_url.concat(AUTH_PATH);
		url = url.concat(ARG_CREDENTIAL);
		url = url.concat(password);
		url = url.concat(ARG_DEVICE_ID);
		url = url.concat(deviceid);
		
		try {
			URL mUrl = new URL(url);

			Log.e("URL", "> " + url);
			conn = (HttpURLConnection) mUrl.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				//Closes the connection.
            	Log.e("ChatAuthServerAuthenticate", String.valueOf(status));
                throw new IOException();
			}
			else
			{
				InputStream in = new BufferedInputStream(
						conn.getInputStream());
				StringBuffer sb = new StringBuffer("");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String inputLine = "";
				while ((inputLine = reader.readLine()) != null) {
					sb.append(inputLine);
//						sb.append("\n");
				}
				in.close();
				responseString=sb.toString();
			}
			
//			response = httpclient.execute(new HttpGet(url));
//	        StatusLine statusLine = response.getStatusLine();
//	        
//	        // checking response to see if it worked ok
//            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
//            	// all that obvious receiving business
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                response.getEntity().writeTo(out);
//                out.close();
//                responseString = out.toString();
//                Log.i("SyncAdapter", "Response text: \n"+responseString);
                
                // transform response into a JSON object
                //JSONObject jsonObject = new JSONObject(responseString);
                //return jsonObject.getString(ARG_CLIENT_ACCESS_TOKEN);
                return responseString;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}

	

}
