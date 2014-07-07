package org.chat.android;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.chat.android.Auth.AccountGeneral;
import org.chat.android.models.CHAAccessed;
import org.chat.android.models.Client;
import org.chat.android.models.ServiceAccessed;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {
	Context context;
	Visit visit = null;
	int visitId = 0;
	int workerId = 0;
	
	// step aside I am here on official sync adapter business
	// Constants
    // The authority for the sync adapter's content provider
//    public static final String AUTHORITY = "org.chat.provider";
    // An account type, in the form of a domain name
//    public static final String ACCOUNT_TYPE = "chat.org";
    // The account name
//    public static final String ACCOUNT = "chat-tablet";
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
//	    case R.id.menu_settings:
//	        Toast.makeText(getApplicationContext(), "Running setupDB...", Toast.LENGTH_SHORT).show();
//	        prepopulateDB();
//	        return true;
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
	
    // overriding the standard toast layout START HERE
    public static void toastHelper(Activity activity, String message) {
    	Context context = activity.getApplicationContext();
    	//LayoutInflater inflater = getLayoutInflater();
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	
    	View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) activity.findViewById(R.id.toast_layout_root));

    	TextView text = (TextView) layout.findViewById(R.id.text);
    	text.setText(message);

    	Toast toast = new Toast(context);
    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    	toast.setDuration(Toast.LENGTH_LONG);
    	toast.setView(layout);
    	toast.show();   	
    }
    
    private void prepopulateDB() {
		Intent i = new Intent(this, SetupDB.class);
		startActivity(i);
    }
    
    // NB: duplication of some of this functionality in HomeActivity
    private void checkVisitCompleteStatus() {
    	Boolean completeFlag = true;
    	List<Client> clientsForHealthAssessment = ModelHelper.getAttendingClientsForVisitIdUnderAge(context, visitId, 5);
    	
    	// check for completion of CHA
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
    	
    	// check for completion of service requirements
    	if (ModelHelper.getServicesAccessedForVisitId(context, visitId).size() == 0) {
    		completeFlag = false;
    		Toast.makeText(getApplicationContext(),"Visit not marked as complete - no services delivered",Toast.LENGTH_LONG).show();
    	}
    	
    	// check for completion of health ed requirements
    	if (ModelHelper.getHealthTopicsAccessedForVisitId(context, visitId).size() == 0) {
    		completeFlag = false;
    		Toast.makeText(getApplicationContext(),"Visit not marked as complete - no health topic education delivered",Toast.LENGTH_LONG).show();
    	}

    	if (completeFlag == true) {
    		// add the visit type services, etc
    		updateVisitObjectforExtras();
    	}
    }
    
	private void updateVisitObjectforExtras() {
    	// set the Visit type services (since these will not be checked off in the standard way)
    	// kinda ugly :/    but relevant services are ids: 1 and 27 for Vol / 71 and 72 for LCs
    	
		Visit v = ModelHelper.getVisitForId(context, visitId);
		// get the visit type
		String type = v.getType();
		// get the role
		String role = v.getRole();
		// get the attending clients - all clients under the age of 999
		List<Client> cList = ModelHelper.getAttendingClientsForVisitIdUnderAge(context, visitId, 999);
		
		// decide which serviceId to mark off based on type and role (gross!)
		int serviceId = 0;
    	if (type.equals("Home Visit")) {
    		if (role.equals("Home Care Volunteer")) {
    			serviceId = 1;
    		} else if (role.equals("Lay Counsellor")) {
    			serviceId = 71;
    		} else {
    			Toast.makeText(getApplicationContext(),"Error: unknown role in HomeActivity updateVisitObjectforExtras. Please contact technical support.",Toast.LENGTH_LONG).show();
    		}
    	} else if (type.equals("School Visit")) {
    		if (role.equals("Home Care Volunteer")) {
    			serviceId = 27;
    		} else if (role.equals("Lay Counsellor")) {
    			serviceId = 72;
    		} else {
    			Toast.makeText(getApplicationContext(),"Error: unknown role in HomeActivity updateVisitObjectforExtras. Please contact technical support.",Toast.LENGTH_LONG).show();
    		}
    	}
    	
    	// set serviceAccessed
    	for (Client c : cList) {
    		Date time = new Date();
        	ServiceAccessed sa = new ServiceAccessed(serviceId, visitId, c.getId(), null, time);
        	Dao<ServiceAccessed, Integer> saDao;
    	    DatabaseHelper saDbHelper = new DatabaseHelper(context);
    	    try {
    	        saDao = saDbHelper.getServiceAccessedDao();
    	        saDao.create(sa);
    	    } catch (SQLException e) {
    	        // TODO Auto-generated catch block
    	        e.printStackTrace();
    	    }
    	}
    	
    	markVisitComplete();
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
    	ContentResolver.requestSync(mAccount, AccountGeneral.AUTHORITY, settingsBundle);
	 }
    
    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(AccountGeneral.ACCOUNT_NAME, AccountGeneral.ACCOUNT_TYPE);
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
        	ContentResolver.setSyncAutomatically(newAccount, AccountGeneral.AUTHORITY, true); //this programmatically turns on the sync for new sync adapters.
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