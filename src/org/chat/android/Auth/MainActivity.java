package org.chat.android.Auth;

import com.crashlytics.android.Crashlytics;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.chat.android.HomeActivity;
import org.chat.android.LoginActivity;
import org.chat.android.R;
import org.chat.android.SetupDB;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AccountAuthenticatorActivity {

	private Button btn_greeting;
	private TextView tv_hello;
	private MainActivity demo;
	private String mEmail;
	private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
	private static final String TAG = "TokenInfoTask";
	private String mAuthTokenType;
	private AccountManager mAccountManager;
	private ServerAuthenticate sServerAuthenticate;

	public static final String EXTRA_ACCOUNTNAME = "extra_accountname";

	static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

	static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_main);
		demo = this;
		sServerAuthenticate= new ChatAuthServerAuthenticate(this.getApplicationContext().getString(R.string.base_url));
		mAccountManager = AccountManager.get(demo);
		btn_greeting = (Button) this.findViewById(R.id.btn_greeting);
		tv_hello = (TextView) this.findViewById(R.id.tv_hello);
		addListener();
		Bundle extras = getIntent().getExtras();

		mAuthTokenType = getIntent().getStringExtra(
				AccountGeneral.ARG_AUTH_TYPE);
		if (mAuthTokenType == null)
			mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

		if (extras != null && extras.containsKey(EXTRA_ACCOUNTNAME)) {
			mEmail = extras.getString(EXTRA_ACCOUNTNAME);
			new GetTokenTask(demo).execute();
		}
		Account mAccount = new Account(AccountGeneral.ACCOUNT_NAME,AccountGeneral.ACCOUNT_TYPE);
		if(extras!=null&&extras.getBoolean(AccountGeneral.ARG_INTENT_REAUTH))
				return;
		if(null!=mAccountManager.peekAuthToken(mAccount, mAuthTokenType))
		{
			finish();
			Intent intent = new Intent(demo, LoginActivity.class);
			startActivity(intent);
		}
		else
		{
			if (mEmail == null) {
				demo.pickUserAccount();
			} else
				new GetTokenTask(demo).execute();
		}
	}

	/**
	 * Starts an activity in Google Play Services so the user can pick an
	 * account
	 */
	private void pickUserAccount() {
		String[] accountTypes = new String[] { "com.google" };
		Intent intent = AccountPicker.newChooseAccountIntent(null, null,
				accountTypes, false, null, null, null, null);
		startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
	}

	/**
	 * Attempt to get the user name. If the email address isn't known yet, then
	 * call pickUserAccount() method so the user can pick an account.
	 */
	private void getUsername() {
		if (mEmail == null) {
			pickUserAccount();
		} else {
			new GetTokenTask(demo).execute();
		}
	}

	/** Checks whether the device currently has a network connection */
	private boolean isDeviceOnline() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
			if (resultCode == RESULT_OK) {
				mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				getUsername();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "You must pick an account",
						Toast.LENGTH_SHORT).show();
			}
		}
		if (requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
			handleAuthorizeResult(resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void handleAuthorizeResult(int resultCode, Intent data) {
		if (data == null) {
			show("Unknown error, click the button again");
			return;
		}
		if (resultCode == RESULT_OK) {
			Log.i(TAG, "Retrying");
			new GetTokenTask(demo).execute();
			return;
		}
		if (resultCode == RESULT_CANCELED) {
			show("User rejected authorization.");
			return;
		}
		show("Unknown error, click the button again");
	}

	public void show(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tv_hello.setText(message);
			}
		});
	}

	private void addListener() {
		btn_greeting.setOnClickListener(new OnClickListener() {

			private final String NAME_KEY = "given_name";

			/**
			 * Reads the response from the input stream and returns it as a
			 * string.
			 */
			private String readResponse(InputStream is) throws IOException {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] data = new byte[2048];
				int len = 0;
				while ((len = is.read(data, 0, data.length)) >= 0) {
					bos.write(data, 0, len);
				}
				return new String(bos.toByteArray(), "UTF-8");
			}

			/**
			 * Parses the response and returns the first name of the user.
			 * 
			 * @throws JSONException
			 *             if the response is not JSON or if first name does not
			 *             exist in response
			 */
			private String getFirstName(String jsonResponse)
					throws JSONException {
				JSONObject profile = new JSONObject(jsonResponse);
				return profile.getString(NAME_KEY);
			}

			@Override
			public void onClick(View arg0) {
				if (mEmail == null) {
					demo.pickUserAccount();
				} else
					new GetTokenTask(demo).execute();
			}

		});
	}

	public class GetTokenTask extends AsyncTask<Void, Void, Void> {

		private final String NAME_KEY = "given_name";
		private final MainActivity context;

		public GetTokenTask(MainActivity mActivity) {
			context = mActivity;
		}
		
	    public void triggerSyncAdaper(Account mAccount) {
	        // Pass the settings flags by inserting them in a bundle
	        Bundle settingsBundle = new Bundle();
	        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
	        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
	        /*
	         * Request the sync for the default account, authority, and
	         * manual sync settings
	         */
	        ContentResolver.requestSync(mAccount, AccountGeneral.AUTHORITY, settingsBundle);
	    }
	    
	    public void prepopulateDB() {
			Intent i = new Intent(MainActivity.this, SetupDB.class);
			startActivity(i);
	    }

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				
				// got the token from google auth service with email account
				String googleAccessToken = GoogleAuthUtil.getToken(
						getApplicationContext(), mEmail, SCOPE);
				Log.e("google access token", "> " + googleAccessToken);
				// got the device id
				String ts = Context.TELEPHONY_SERVICE;
				TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
				String deviceid = android.os.Build.SERIAL;
				// got the clientToken from server with googleAccessToken
				String clientToken = sServerAuthenticate.getAccessToken(mEmail,
						googleAccessToken,deviceid);
				Log.e("client access token", "> " + clientToken);
				
				if(null==clientToken)
				{
					Log.e("client access token", "> The return token is empty");
					Intent reAuthIntent = new Intent(demo, MainActivity.class);
					reAuthIntent.putExtra(AccountGeneral.ARG_INTENT_REAUTH, true);
					throw new UserRecoverableAuthException("client access token is unavailable",reAuthIntent);
				}
				
				final Intent res = new Intent();
				res.putExtra(AccountManager.KEY_ACCOUNT_NAME, mEmail);
				res.putExtra(AccountManager.KEY_ACCOUNT_TYPE,
						AccountGeneral.ACCOUNT_TYPE);
				res.putExtra(AccountManager.KEY_AUTHTOKEN, clientToken);
				// in our case, the googleAccessToken is our password
				res.putExtra(AccountGeneral.ARG_ACCOUNT_PASSWORD,
						googleAccessToken);

				String accountName = AccountGeneral.ACCOUNT_NAME;
				String accountPassword = googleAccessToken;
				Account account = new Account(accountName,
						AccountGeneral.ACCOUNT_TYPE);

				String authtoken = clientToken;
				String authtokenType = mAuthTokenType;
				// Creating the account on the device and setting the auth
				// token we got
				// (Not setting the auth token will cause another call to
				// the server to authenticate the user)
				mAccountManager.addAccountExplicitly(account,
						accountPassword, null);
				mAccountManager.setAuthToken(account, authtokenType,
						authtoken);
				setAccountAuthenticatorResult(res.getExtras());
				setResult(RESULT_OK, res);
				finish();
				// prepare the database
//				prepopulateDB();
				// jump to login activity
//				triggerSyncAdaper(new Account(accountName,AccountGeneral.ACCOUNT_TYPE));
				Intent intent = new Intent(demo, LoginActivity.class);
				startActivity(intent);
				
			} catch (UserRecoverableAuthException userRecoverableException) {
				// TODO Auto-generated catch block
				userRecoverableException.printStackTrace();
				demo.handleException(userRecoverableException);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GoogleAuthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	/**
	 * This method is a hook for background threads and async tasks that need to
	 * provide the user a response UI when an exception occurs.
	 */
	public void handleException(final Exception e) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (e instanceof GooglePlayServicesAvailabilityException) {
					// The Google Play services APK is old, disabled, or not
					// present.
					// Show a dialog created by Google Play services that allows
					// the user to update the APK
					int statusCode = ((GooglePlayServicesAvailabilityException) e)
							.getConnectionStatusCode();
					Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
							statusCode, MainActivity.this,
							REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
					dialog.show();
				} else if (e instanceof UserRecoverableAuthException) {
					// Unable to authenticate, such as when the user has not yet
					// granted
					// the app access to the account, but the user can fix this.
					// Forward the user to an activity in Google Play services.
					Intent intent = ((UserRecoverableAuthException) e)
							.getIntent();
					startActivityForResult(intent,
							REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
