package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.chat.android.Auth.AccountGeneral;
import org.chat.android.models.Household;
import org.chat.android.models.Util;
import org.chat.android.models.Visit;
import org.chat.android.models.Worker;

import com.j256.ormlite.dao.Dao;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUserName;
	private String mPassword;

	// UI references.
	private EditText mUserNameView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private Spinner roleSpinner;

    // Create the account type and default account
    static Account newAccount;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		newAccount = new Account(AccountGeneral.ACCOUNT_NAME, AccountGeneral.ACCOUNT_TYPE);
		
		initDBIfRequired();

		// Set up the login form.
		mUserName = getIntent().getStringExtra("some string");
		mUserNameView = (EditText) findViewById(R.id.user_name);
		mUserNameView.setText(mUserName);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					attemptLogin();
			}
		});
		
		// TODO: match this up with the UN/PW/ROLE on authentication, maybe thing about other error messages 
		roleSpinner = (Spinner) findViewById(R.id.role_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.role_array, R.layout.login_spinner_item);
		adapter.setDropDownViewResource(R.layout.login_spinner_item);
		roleSpinner.setAdapter(adapter);

		// Create the dummy account (needed for sync adapter)
		newAccount = CreateSyncAccount(this);
	}
	
	
	private void initDBIfRequired() {
        Date d = new Date();
        Util u1 = new Util(1, d, d);
        Dao<Util, Integer> utilDao;
        DatabaseHelper utilDbHelper = new DatabaseHelper(getBaseContext());
        try {
        	utilDao = utilDbHelper.getUtilDao();
        	utilDao.createIfNotExists(u1);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	}


	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUserNameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUserName = mUserNameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// ******************** Check for a valid password - comment out for testing, use for PROD ********************
		
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 2) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		// Check for a valid user ID.
		if (TextUtils.isEmpty(mUserName)) {
			mUserNameView.setError(getString(R.string.error_field_required));
			focusView = mUserNameView;
			cancel = true;
		}
		// Check if the user exists
		Worker w = ModelHelper.getWorkerForUsername(getApplicationContext(), mUserNameView.getText().toString());
		if (w == null) {
			mUserNameView.setError(getString(R.string.error_invalid_user_name));
			focusView = mUserNameView;
			cancel = true;
		} else {
			String role = roleSpinner.getSelectedItem().toString();
			if (!w.getRoleName().equals(role)) {
				mUserNameView.setError(getString(R.string.error_invalid_role));
				focusView = mUserNameView;
				cancel = true;
			}
		}

		// /comment
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {

			// ******************** COMMENT BACK IN FOR PROD ********************
			Worker w = ModelHelper.getWorkerForUsername(getApplicationContext(), mUserNameView.getText().toString());
			if (w != null) {
				return w.getPassword().equals(mPassword);
			} else {
				return false;
			}
			//return true;
		}

		// @Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);
			
			if (success) {
				int vId = 0;
				// check if there are previously uncompleted visits for this worker. NB: this assumes there is only ever one unrestored visit (TESTME)
				String workerName = mUserNameView.getText().toString();

				Worker w = ModelHelper.getWorkerForUsername(getApplicationContext(), workerName);

				if (w == null) {
					Toast.makeText(getApplicationContext(), "ERROR: That user does not exist in the database. Please check spelling, and the problem persists, contact technical support", Toast.LENGTH_SHORT).show();
				} else {
					int workerId = w.getId();
					
					Dao<Visit, Integer> vDao;		
					DatabaseHelper vDbHelper = new DatabaseHelper(getApplicationContext());
					try {
						vDao = vDbHelper.getVisitsDao();
						List<Visit> vList = vDao.queryBuilder().where().eq("worker_id",workerId).and().isNull("end_time").query();
						Iterator<Visit> iter = vList.iterator();
						while (iter.hasNext()) {
							Visit v = iter.next();
							vId = v.getId();
						}
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					// figure out what activity to launch next 
					Intent i;
					Bundle b = new Bundle();
					
					// if there is no uncompleted visit, go to SetupVisitActivity, else go to RestoreVisitActivity
					if (vId == 0) {
						i = new Intent(LoginActivity.this, SetupVisitActivity.class);
					} else {
						i = new Intent(LoginActivity.this, RestoreVisitActivity.class);
						b.putInt("visitId",vId);
					}
					
					b.putString("workerName",workerName);
					b.putString("role",roleSpinner.getSelectedItem().toString());
					i.putExtras(b);
					startActivity(i);
					finish();
				}	

			 } else {
				 mPasswordView
				 .setError(getString(R.string.error_auth));
				 mPasswordView.requestFocus();
			 }
		 }

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_resources:
	    	Intent i = new Intent(LoginActivity.this, ResourcesActivity.class);
	    	Bundle b = new Bundle();
	    	i.putExtras(b);  
	    	startActivity(i);
	        return true;
	    case R.id.menu_device_id:
	        try {
				String deviceSerial = (String) Build.class.getField("SERIAL").get(null);
				//Toast.makeText(getApplicationContext(),"Device ID: "+deviceSerial,Toast.LENGTH_LONG).show();
	        	BaseActivity.toastHelper(this, "Device ID: "+deviceSerial);				
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return true;
	    case R.id.menu_sync:
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("WARNING: Are you sure you want to pull all data to this tablet?")
	    	       .setCancelable(false)
	    	       .setPositiveButton("Yes, I know what I am doing", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   triggerSyncAdaper();
	    	           }
	    	       })
	    	       .setNegativeButton("No, this is a bad idea", new DialogInterface.OnClickListener() {
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
		Intent i = new Intent(LoginActivity.this, SetupDB.class);
		startActivity(i);
    }
    
    public void triggerSyncAdaper() {
    	BaseActivity.toastHelper(this, "Triggering sync with server...");
    	Account mAccount = newAccount;
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settingsBundle.putString("syncType", "pullAll");
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, AccountGeneral.AUTHORITY, settingsBundle);
    }
    
    public static Account CreateSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        // TODO: This is clearly wrong and is the exampley sync adapter that doesn't user any account
        // beyond a dummy account. I think we need to access the account information to call this correctly
        // The if fails and this returns null. Therefore the CHAT account is not set for automatic syncing
        // and I am unsure if the sync can ever work like this.
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        	ContentResolver.setSyncAutomatically(newAccount,  AccountGeneral.AUTHORITY, true); //this programmatically turns on the sync for new sync adapters.
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
