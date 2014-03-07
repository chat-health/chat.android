package org.chat.android;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.chat.android.models.CHAAccessed;
import org.chat.android.models.Client;
import org.chat.android.models.Visit;

import com.j256.ormlite.dao.Dao;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity {
	Context context;
	Visit visit = null;
	int visitId = 0;
	int workerId = 0;
	
	// step aside I am here on official sync adapter business
	// Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "org.chat.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "chat.org";
    // The account name
    public static final String ACCOUNT = "chat-tablet";
    // Instance fields
    Account mAccount;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		visit = ModelHelper.getVisitForId(context, visitId);
		workerId = visit.getWorkerId();
		
		// Create the dummy account (needed for sync adapter)
        mAccount = CreateSyncAccount(this);
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_base, menu);
        return true;
    }

	// action bar items
	// using this override cause we're on an old android version and I can't get the support libraries to work correctly
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	Intent homeI = new Intent(this, HomeActivity.class);
        	Bundle homeB = new Bundle();
        	homeB.putInt("visitId",visitId);
        	homeB.putBoolean("fromBack", true);
        	homeI.putExtras(homeB);
        	startActivity(homeI);
            return true;
	    case R.id.menu_resources:
	    	Intent resourcesI = new Intent(this, ResourcesActivity.class);
	    	Bundle resourcesB = new Bundle();
	    	resourcesB.putInt("visitId",visitId);
	    	resourcesB.putInt("workerId",workerId);
	    	resourcesI.putExtras(resourcesB);    	
	    	startActivity(resourcesI);
	        return true;
	    case R.id.menu_settings:
	        Toast.makeText(getApplicationContext(), "Running setupDB...", Toast.LENGTH_SHORT).show();
	        prepopulateDB();
	        return true;
	    case R.id.menu_sync:
	        Toast.makeText(getApplicationContext(), "Triggering sync adapter to sync with server...", Toast.LENGTH_LONG).show();
	        triggerSyncAdaper();
	        return true;
	    case R.id.menu_logout:
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("Confirm finalization of visit")
	    	       .setCancelable(false)
	    	       .setPositiveButton("Yes, mark this visit as complete and log me out", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   checkVisitCompleteStatus();
	    	        	   //triggerSyncAdapter();
	    	           }
	    	       })
	    	       .setNegativeButton("No, cancel and return to the visit in progress", new DialogInterface.OnClickListener() {
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
		Intent i = new Intent(this, SetupDB.class);
		startActivity(i);
    }
    
    private void checkVisitCompleteStatus() {
    	Boolean completeFlag = true;
    	List<Client> clientsForHealthAssessment = ModelHelper.getAttendingClientsForVisitIdUnderAge(context, visitId, 5);
    	
    	for (Client c : clientsForHealthAssessment) {
        	Boolean healthFlag = false;
        	Boolean immunizationFlag = false;
        	if (ModelHelper.getCHAAccessedCompleteForVisitIdAndClientIdAndType(context, visitId, c.getId(), "health") == true) {
        		healthFlag = true;
        	} else {
        		Toast.makeText(getApplicationContext(),"Visit not marked as complete - Child Health Assessment section still needs to be completed for " + c.getFirstName() + " " + c.getLastName(),Toast.LENGTH_LONG).show();
        	}
    		Boolean allVaccinesAdministered = ModelHelper.getVaccineRecordedCompleteForClientId(context, c.getId());
    		Boolean chaImmunizationComplete = ModelHelper.getCHAAccessedCompleteForVisitIdAndClientIdAndType(context, visitId, c.getId(), "immunization");
    		if (allVaccinesAdministered || chaImmunizationComplete) {
    			immunizationFlag = true;
    		} else {
    			Toast.makeText(getApplicationContext(),"Visit not marked as complete - Immunization section still needs to be completed for " + c.getFirstName() + " " + c.getLastName(),Toast.LENGTH_LONG).show();
    		}
    		if (healthFlag == false || immunizationFlag == false) {
    			completeFlag = false;
    		}
    	}

    	if (completeFlag == true) {
    		markVisitComplete();
    	}
    }
    
    public void markVisitComplete() {
		Toast.makeText(getApplicationContext(),"Visit saved and marked as complete",Toast.LENGTH_LONG).show();
		
		// update the Visit object and save to DB
		Date endTime = new Date();
		visit.setEndTime(endTime);
		
	    Dao<Visit, Integer> vDao;
	    DatabaseHelper vDbHelper = new DatabaseHelper(context);
	    try {
	    	vDao = vDbHelper.getVisitsDao();
	    	vDao.update(visit);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
		
	    // official end of visit - TODO, decide if this is what we want
	    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.putExtra("EXIT", true);
	    startActivity(intent);
	}
    
    
    /////// Helper functions
    public Calendar dateToCal(Date d) {
    	Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        
        return cal;
    }
    
    public Date calToDate(Calendar c) {
    	GregorianCalendar gc = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		Date d = gc.getTime();
		
		return d;
    }
    
    public String getMonthForInt(Calendar cal) {
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM");
    	String monthName = sdf.format(cal.getTime());
    	
    	return monthName;
    }    
    
    
    
	/////// Sync Adapter stuff (hello boiler plate)
	  /**
	   * Respond to a menu click by calling requestSync(). This is an
	   * asynchronous operation.
	   *
	   *
	   * @param v The View associated with the method call,
	   * in this case a Button
	   */
    public void triggerSyncAdaper() {
    	// Pass the settings flags by inserting them in a bundle
    	Bundle settingsBundle = new Bundle();
    	settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
    	settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
    	/*
    	 * Request the sync for the default account, authority, and
    	 * manual sync settings
    	 */
    	ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
	 }
    
    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        	ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true); //this programmatically turns on the sync for new sync adapters.
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