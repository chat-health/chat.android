package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.chat.android.Auth.AccountGeneral;
import org.chat.android.models.Attendance;
import org.chat.android.models.Client;
import org.chat.android.models.Household;
import org.chat.android.models.ServiceAccessed;
import org.chat.android.models.Visit;
import org.chat.android.models.Worker;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import android.os.Build;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class HomeActivity extends Activity {
	// since we aren't OrmLiteBaseActivity or BaseActivity we can't use getHelper()
    // so we use OpenHelperManager
    private DatabaseHelper databaseHelper = null;

	String TAG = "INFO"; //Log.i(TAG, "Testing: "+somevar);
	Context context = null;
	private Visit visit;
	public int workerId = 0;
	public int visitId = 0;
	public int hhId = 0;
	
	public ListView lv = null;
	public ClientsAdapter clAdapter = null;
	ImageButton attendanceBtn = null;
	ImageButton servicesBtn = null;
	ImageButton healthBtn = null;
	ImageButton chaBtn = null;
	ImageButton resourcesBtn = null;
	ImageView servicesBtnImg = null;
	ImageView healthBtnImg = null;
	ImageView chaBtnImg = null;
	ImageView resourcesBtnImg = null;
	TextView servicesTitle = null;
	TextView healthTitle = null;
	TextView chaTitle = null;
	TextView resourcesTitle = null;
	View servicesDivider = null;
	View healthDivider = null;
	View chaDivider = null;
	View resourcesDivider = null;
	ImageView servicesChk = null;
	ImageView healthChk = null;
	ImageView chaChk = null;
	
	// step aside I am here on official sync adapter business

    // Create the account type and default account
    static Account mAccount;

    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_home);
        mAccount = new Account(AccountGeneral.ACCOUNT_NAME, AccountGeneral.ACCOUNT_TYPE);
        
        // the best we can currently do for actually killing all activities - will land us on the LoginActivity
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();  
        }
        
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Black.ttf");
//        servicesTitle = (TextView) findViewById(R.id.services_title_field);
//        servicesTitle.setTypeface(tf);
        
        Locale locale = getResources().getConfiguration().locale;
        locale.getLanguage();
        
        setupUIElements();
        
        // Create the dummy account (needed for sync adapter)
//        mAccount = CreateSyncAccount(this);
    }
    
    @Override    
    protected void onResume() {
    	super.onResume();
    	
    	//setupUIElements();				CAREFUL HERE - does the visit need to be repulled?
    	// if this is coming from a back or home click in another activity, then update
    	// TODO: this is kind of sloppy - better to have a checkAttendanceSubmitted() then conditional update
    	if (getIntent().getBooleanExtra("fromBack", false)) {
    		updateUIElements();
    	}
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseHelper();
    }
    
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
    
    private void releaseHelper() {
    	if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
    
    public void setupUIElements() {
    	attendanceBtn = (ImageButton)findViewById(R.id.attendance_submission_button);
    	
    	servicesTitle = (TextView)findViewById(R.id.services_title_field);
    	healthTitle = (TextView)findViewById(R.id.health_education_title_field);
    	chaTitle = (TextView)findViewById(R.id.child_health_assessment_title_field);
    	resourcesTitle = (TextView)findViewById(R.id.resources_title_field);
    	servicesDivider = (View)findViewById(R.id.services_divider);
    	healthDivider = (View)findViewById(R.id.health_education_divider);
    	chaDivider = (View)findViewById(R.id.child_health_assessment_divider);
    	resourcesDivider = (View)findViewById(R.id.resources_divider);
        
        // set the services and health branch buttons to disabled (until user has submitted attendance)
        servicesBtn = (ImageButton)findViewById(R.id.services_button);
        servicesBtn.setEnabled(false);
        servicesBtnImg = (ImageView)findViewById(R.id.services_button_img);
        servicesBtnImg.setEnabled(false);
        healthBtn = (ImageButton)findViewById(R.id.health_education_button);
        healthBtn.setEnabled(false);
        healthBtnImg = (ImageView)findViewById(R.id.health_education_button_img);
        healthBtnImg.setEnabled(false);
        chaBtn = (ImageButton)findViewById(R.id.child_health_assessment_button);
        chaBtn.setEnabled(false);
        chaBtnImg = (ImageView)findViewById(R.id.child_health_assessment_button_img);
        chaBtnImg.setEnabled(false);
        resourcesBtn = (ImageButton)findViewById(R.id.resources_button);
        resourcesBtn.setEnabled(false);
        resourcesBtnImg = (ImageView)findViewById(R.id.resources_button_img);
        resourcesBtnImg.setEnabled(false);
        
		Bundle b = getIntent().getExtras();
		if (b != null) {
			visitId = b.getInt("visitId");
		}

        // visitId is 0 when starting new visit, else not 0
        if (visitId == 0) {
    		setupVisitObject(b.getString("hhName"), b.getString("workerName"), b.getString("role"), b.getString("type"), b.getDouble("lat"), b.getDouble("lon"));				
        } else if (visitId != 0) {
        	// pull the uncompleted visit object
        	visit = ModelHelper.getVisitForId(getHelper(), visitId);
        	workerId = visit.getWorkerId();
        	hhId = visit.getHhId();
    		// TODO? update UI - ie attendance - likely needs a refactor
        } else {
        	Log.e("Neither a new visit or a resume visit. Something is definitely up. VisitId: ", "");
        }
    	
        List<Client> hhCList = new ArrayList<Client>();
        hhCList = ModelHelper.getClientsForHousehold(getHelper(), hhId);
        
        lv = (ListView) findViewById(R.id.attendance_listview);
        clAdapter = new ClientsAdapter(context, android.R.layout.simple_list_item_multiple_choice, hhCList, visitId);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(clAdapter);
    }
    
    public void submitAttendance(View v) {
        String bText = attendanceBtn.getTag().toString();
        
        // check if any checkboxes are checked
        String msg = null;
        if (clAdapter.getSelectedClients().size() > 0) {
            if (bText.equals("Done")) {
            	msg = getResources().getString(getResources().getIdentifier("att_submitted_text", "string", getPackageName()));
            	BaseActivity.toastHelper(this, msg);
            	saveAttendanceList();
            	updateUIElements();
            } else {
            	msg = getResources().getString(getResources().getIdentifier("att_updated_text", "string", getPackageName()));
            	BaseActivity.toastHelper(this, msg);
            	deleteCurrentAttendance();			// saveAttendanceList() is called from the finally in deleteCurrentAttendance()
            }        	
        } else {
        	msg = getResources().getString(getResources().getIdentifier("att_not_submitted_text", "string", getPackageName()));
        	BaseActivity.toastHelper(this, msg);
        }
    }
    
    public void openServiceOverview(View v) {
    	Intent i = new Intent(HomeActivity.this, ServiceOverviewActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	i.putExtras(b);
    	startActivity(i);
    }

    public void openHealthOverview(View v) {
    	Intent i = new Intent(HomeActivity.this, HealthOverviewActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	i.putExtras(b);    	
    	startActivity(i);
    }
    
    public void openCHA(View v) {
    	Intent i = new Intent(HomeActivity.this, CHASelectChildActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	i.putExtras(b);    	
    	startActivity(i);
    }
    
    public void openResources(View v) {
    	Intent i = new Intent(HomeActivity.this, ResourcesActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("workerId",workerId);
    	i.putExtras(b);    	
    	startActivity(i);
    }
    
    public void updateUIElements() {
    	// check if there is an attendance object for this visitId
    	List<Attendance> attendance = ModelHelper.getAttendanceForVisitId(getHelper(), visitId);
    	if (attendance.size() > 0) {
        	// switch the Done button to the Update button
        	attendanceBtn.setTag("Update");
        	attendanceBtn.setImageResource(R.drawable.updatebutton);
        	
        	// enable the Service and Health branches, update the colors
        	servicesBtn.setImageResource(R.drawable.servicesgobutton);
        	servicesBtn.setEnabled(true);
        	servicesBtnImg.setImageResource(R.drawable.servicesimage);
        	servicesBtnImg.setEnabled(true);
        	int c = getResources().getColor(getResources().getIdentifier("services", "color", getPackageName()));
        	servicesTitle.setTextColor(c);
        	servicesDivider.setBackgroundColor(c);
        	
        	healthBtn.setImageResource(R.drawable.healthedgobutton);
        	healthBtn.setEnabled(true);
        	healthBtnImg.setImageResource(R.drawable.healthedimage);
        	healthBtnImg.setEnabled(true);
        	c = getResources().getColor(getResources().getIdentifier("health_education", "color", getPackageName()));
        	healthTitle.setTextColor(c);
        	healthDivider.setBackgroundColor(c);
        	
        	chaBtn.setImageResource(R.drawable.childhealthassessmentgobutton);
        	chaBtn.setEnabled(true);
        	chaBtnImg.setImageResource(R.drawable.childhealthassessmentimage);
        	chaBtnImg.setEnabled(true);
        	c = getResources().getColor(getResources().getIdentifier("child_health_assessment", "color", getPackageName()));
        	chaTitle.setTextColor(c);
        	chaDivider.setBackgroundColor(c);
        	
        	resourcesBtn.setImageResource(R.drawable.resourcesgobutton);
        	resourcesBtn.setEnabled(true);
        	resourcesBtnImg.setImageResource(R.drawable.resourcesimage);
        	resourcesBtnImg.setEnabled(true);
        	c = getResources().getColor(getResources().getIdentifier("resources", "color", getPackageName()));
        	resourcesTitle.setTextColor(c);
        	resourcesDivider.setBackgroundColor(c);    	
        	
        	// update checkmarks and borders to denote current completed sections
            Animation animPulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
        	servicesChk = (ImageView)findViewById(R.id.services_checkmark);
        	healthChk = (ImageView)findViewById(R.id.health_education_checkmark);
        	chaChk = (ImageView)findViewById(R.id.child_health_assessment_checkmark);
        	RelativeLayout servicesBrd = (RelativeLayout)findViewById(R.id.services_border);
        	RelativeLayout healthBrd = (RelativeLayout)findViewById(R.id.healthed_border);
        	RelativeLayout chaBrd = (RelativeLayout)findViewById(R.id.health_assessment_border);
        	
        	
        	// mmmmm, nesty - good luck parsing this, future code viewers
        	// jk you guys. The idea here is that order now matters. So, if services is done, hide its border, add its checkmark and then show health assessment's border, etc
            if (checkServiceRequirements() == true) {
            	servicesChk.setVisibility(View.VISIBLE);
            	if (checkCHARequirements() == true) {
                	chaChk.setVisibility(View.VISIBLE);
                	servicesBrd.setVisibility(View.INVISIBLE);
	            	if (checkHealthEducationRequirements() == true) {
	                	healthChk.setVisibility(View.VISIBLE);
	                	healthBrd.setVisibility(View.INVISIBLE);
                    	String msg = getResources().getString(getResources().getIdentifier("visit_complete", "string", getPackageName()));				// ADD ME TO TRANSLATIONS DOC
                    	BaseActivity.toastHelper(this, msg);
	                } else {
	                	healthChk.setVisibility(View.INVISIBLE);
	                	healthBrd.setVisibility(View.VISIBLE);
	                	healthBrd.startAnimation(animPulse);
	                }
            	} else {
            		chaChk.setVisibility(View.INVISIBLE);
	             	chaBrd.setVisibility(View.VISIBLE);
	             	chaBrd.startAnimation(animPulse);
                }
            } else {
            	servicesChk.setVisibility(View.INVISIBLE);
            	servicesBrd.setVisibility(View.VISIBLE);
            	servicesBrd.startAnimation(animPulse);
            }
    	}
        		
    }
    
    
    ////////// HELPER FUNCTIONS //////////
    private void setupVisitObject(String hhName, String workerName, String role, String type, Double lat, Double lon) {
        // get the workerId
    	Worker worker = ModelHelper.getWorkerForUsername(getHelper(), workerName);
    	if (worker != null) {
    		workerId = worker.getId();
    	} else {
    		BaseActivity.toastHelper(this, "Error: unknown workerId in HomeActivity. Please contact technical support.");
    		workerId = 1001;
    	}

		// get the householdId
    	Household household = ModelHelper.getHouseholdForName(getHelper(), hhName);
    	if (household != null) {
    		hhId = household.getId();
    	} else {
    		BaseActivity.toastHelper(this, "Error: unknown householdId in HomeActivity. Please contact technical support.");
    		hhId = 1;
    	}

		Date date  = new Date();
		Date startTime = new Date();

		// create a new Visit object to be used for this visit
    	visit = new Visit(hhId, workerId, role, type, lat, lon, startTime);
    	
    	try {
    		Dao<Visit, Integer> vDao = getHelper().getVisitsDao();
    		vDao.create(visit);
    		visitId = visit.getId();
    	} catch (SQLException e) {
    		// Auto-generated catch block
    		e.printStackTrace();
    	}
	}
    
    public void saveAttendanceList() {
    	List<Client> pArray = clAdapter.getSelectedClients();
    	int len = pArray.size();
    	
    	for (int i = 0; i < len; i++) {
    		Client c = pArray.get(i);
        	Attendance a = new Attendance(visitId, c.getId(), getHelper());
        	try {
        		Dao<Attendance, Integer>aDao = getHelper().getAttendanceDao();
        		aDao.create(a);
        	} catch (SQLException e) {
        	    // Auto-generated catch block
        	    e.printStackTrace();
        	}	
    	}
    }
// TODO Armin
	public void deleteCurrentAttendance() {
    	DatabaseHelper helper = OpenHelperManager.getHelper(getApplicationContext(), DatabaseHelper.class);
    	Dao<Attendance, Integer> aDao;
	    try {
		    aDao = getHelper().getDao(Attendance.class);
		    DeleteBuilder<Attendance, Integer> deleteBuilder = aDao.deleteBuilder();
		    deleteBuilder.where().eq("visit_id", visitId);
		    deleteBuilder.delete(); 
    	} catch (SQLException e) {
    	  	// Auto-generated catch block
    	  	e.printStackTrace();
    	} finally {
    		saveAttendanceList();
    	}
	}
	
    private Boolean checkVisitCompleteStatus() {
    	Boolean completeFlag = true;
    	List<Client> clientsForHealthAssessment = ModelHelper.getAttendingClientsForVisitIdUnderAge(getHelper(), visitId, 5);
    	
    	// check for completion of CHA
    	for (Client c : clientsForHealthAssessment) {
        	Boolean healthFlag = false;
        	Boolean immunizationFlag = false;
        	if (ModelHelper.getCHAAccessedCompleteForVisitIdAndClientIdAndType(getHelper(), visitId, c.getId(), "health") == true) {
        		healthFlag = true;
        	} else {
        		BaseActivity.toastHelper(this, "Child Health Assessment section still needs to be completed for " + c.getFirstName() + " " + c.getLastName());
        	}
    		Boolean allVaccinesAdministered = ModelHelper.getVaccineRecordedCompleteForClientId(getHelper(), c.getId());
    		Boolean chaImmunizationComplete = ModelHelper.getCHAAccessedCompleteForVisitIdAndClientIdAndType(getHelper(), visitId, c.getId(), "immunization");
    		if (allVaccinesAdministered || chaImmunizationComplete) {
    			immunizationFlag = true;
    		} else {
    			BaseActivity.toastHelper(this, "Immunization section still needs to be completed for " + c.getFirstName() + " " + c.getLastName());
    		}
    		if (healthFlag == false || immunizationFlag == false) {
    			completeFlag = false;
    		}
    	}
    	
    	// check for completion of service requirements
    	if (checkServiceRequirements() == false) {
    		completeFlag = false;
    		BaseActivity.toastHelper(this, "No services delivered");
    	}
    	
    	// check for completion of health ed requirements
    	if (checkHealthEducationRequirements() == false) {
    		completeFlag = false;
    		BaseActivity.toastHelper(this,"No health topic education delivered" );
    	}

    	return completeFlag;
    }
    
    private Boolean checkServiceRequirements() {
    	Boolean f = null;
    	if (ModelHelper.getServicesAccessedForVisitId(getHelper(), visitId).size() == 0) {
    		f = false;
    	} else {
    		f = true;
    	}
    	return f;
    }
    
    private Boolean checkHealthEducationRequirements() {
    	Boolean f = null;
    	if (ModelHelper.getHealthTopicsAccessedForVisitId(getHelper(), visitId).size() == 0) {
    		f = false;
    	} else {
    		f = true;
    	}
    	return f;
    }
    
    // TODO - this is a duplication of code in checkVisitCompleteStatus function
    private Boolean checkCHARequirements() {
    	Boolean f = true;
    	List<Client> clientsForHealthAssessment = ModelHelper.getAttendingClientsForVisitIdUnderAge(getHelper(), visitId, 5);
    	
    	// check for completion of CHA
    	for (Client c : clientsForHealthAssessment) {
        	Boolean healthFlag = false;
        	Boolean immunizationFlag = false;
        	if (ModelHelper.getCHAAccessedCompleteForVisitIdAndClientIdAndType(getHelper(), visitId, c.getId(), "health") == true) {
        		healthFlag = true;
        	} else {
        		healthFlag = false;
        	}
    		Boolean allVaccinesAdministered = ModelHelper.getVaccineRecordedCompleteForClientId(getHelper(), c.getId());
    		Boolean chaImmunizationComplete = ModelHelper.getCHAAccessedCompleteForVisitIdAndClientIdAndType(getHelper(), visitId, c.getId(), "immunization");
    		if (allVaccinesAdministered || chaImmunizationComplete) {
    			immunizationFlag = true;
    		} else {
    			immunizationFlag = false;
    		}
    		
    		if (healthFlag == false || immunizationFlag == false) {
    			f = false;
    		}
    	}
    	
    	return f;
    }
	
	private void updateVisitObjectforExtras(Boolean completeFlag) {
    	// set the Visit type services (since these will not be checked off in the standard way)
    	// kinda ugly :/    but relevant services are ids: 1 and 27 for Vol / 71 and 72 for LCs
    	
		// get the visit type
		String type = visit.getType();
		// get the role
		String role = visit.getRole();
		// get the attending clients - all clients under the age of 999
		List<Client> cList = ModelHelper.getAttendingClientsForVisitIdUnderAge(getHelper(), visitId, 999);
		
		// decide which serviceId to mark off based on type and role (gross!)
		int serviceId = 0;
    	if (type.equals("Home Visit")) {
    		if (role.equals("Home Care Volunteer")) {
    			serviceId = 1;
    		} else if (role.equals("Lay Counsellor")) {
    			serviceId = 71;
    		} else {
    			BaseActivity.toastHelper(this, "Error: unknown role in HomeActivity updateVisitObjectforExtras. Please contact technical support.");
    		}
    	} else if (type.equals("School Visit")) {
    		if (role.equals("Home Care Volunteer")) {
    			serviceId = 27;
    		} else if (role.equals("Lay Counsellor")) {
    			serviceId = 72;
    		} else {
    			BaseActivity.toastHelper(this, "Error: unknown role in HomeActivity updateVisitObjectforExtras. Please contact technical support.");
    		}
    	}
    	
    	// set serviceAccessed
    	for (Client c : cList) {
    		Date time = new Date();
        	ServiceAccessed sa = new ServiceAccessed(serviceId, visitId, c.getId(), null, time);
    	    try {
    	    	Dao<ServiceAccessed, Integer> saDao = getHelper().getServiceAccessedDao();
    	        saDao.create(sa);
    	    } catch (SQLException e) {
    	        // Auto-generated catch block
    	        e.printStackTrace();
    	    }
    	}
    	
    	markVisitComplete(completeFlag);
	}
	
    public void markVisitComplete(Boolean completeFlag) {
		BaseActivity.toastHelper(this, "Visit saved and marked as complete");
		
		// update the Visit object and save to DB
		Date endTime = null;
		if (completeFlag == true) {
			endTime = new Date();
		} else {
			// instead of rebuilding the data structures all over the place, we'll go with this implicitness for now.
			// if startTime == endTime, then the visit has been finished (not avail for resume) but is not "complete"
			endTime = visit.getStartTime();
		}
		visit.setEndTime(endTime);
		
	    try {
	    	Dao<Visit, Integer> vDao = getHelper().getVisitsDao();
	    	vDao.update(visit);
	    } catch (SQLException e1) {
	        // Auto-generated catch block
	        e1.printStackTrace();
	    }
		
	    // official end of visit - TODO, decide if this is what we want
	    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.putExtra("EXIT", true);
	    startActivity(intent);
	}
    
    
    ////////// OVERFLOW MENU ////////// 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_base, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_about:
	    	String versionName = "";
			try {
				versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (NameNotFoundException e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}
	    	BaseActivity.toastHelper(this, "Version number: " + versionName);	
	        return true;
	    case R.id.menu_resources:
	    	Intent i = new Intent(HomeActivity.this, ResourcesActivity.class);
	    	Bundle b = new Bundle();
	    	b.putInt("visitId",visitId);
	    	b.putInt("workerId",workerId);
	    	i.putExtras(b);
	    	startActivity(i);
	        return true;
	    case R.id.menu_device_id:
	        try {
				String deviceSerial = (String) Build.class.getField("SERIAL").get(null);
				BaseActivity.toastHelper(this, "Device ID: "+deviceSerial);
			} catch (IllegalArgumentException e) {
				// Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
	        return true;
	    case R.id.menu_sync:
	        BaseActivity.toastHelper(this, "Triggering sync adapter to sync with server");
	        triggerSyncAdapter();
	        return true;
	    case R.id.menu_logout:
	    	final Boolean completeFlag = checkVisitCompleteStatus();
	    	String msgFinConf = "";
	    	String msgFinNo = getResources().getString(getResources().getIdentifier("finalize_visit_cancel_text", "string", getPackageName()));
	    	String msgFinYes = getResources().getString(getResources().getIdentifier("finalize_visit_finish_text", "string", getPackageName()));
	    	if (completeFlag == true) {
	    		msgFinConf = getResources().getString(getResources().getIdentifier("finalize_visit_complete_text", "string", getPackageName()));
	    	} else {
	    		msgFinConf = getResources().getString(getResources().getIdentifier("finalize_visit_incomplete_text", "string", getPackageName()));
	    	}
	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage(msgFinConf)
	    	       .setCancelable(false)
	    	       .setPositiveButton(msgFinYes, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   updateVisitObjectforExtras(completeFlag);
	    	        	   triggerSyncAdapter();
	    	           }
	    	       })
	    	       .setNegativeButton(msgFinNo, new DialogInterface.OnClickListener() {
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
		Intent i = new Intent(HomeActivity.this, SetupDB.class);
		startActivity(i);
    }
    

    
    ////////// DEFAULT BUTTON BEHAVIOUR OVERRIDES //////////
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("Confirmation exit")
            .setMessage("Are you sure you want to exit without completing this visit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    HomeActivity.super.onBackPressed();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            	    intent.putExtra("EXIT", true);
            	    startActivity(intent);
                }
            }).create().show();
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
    public void triggerSyncAdapter() {
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
//    public static Account CreateSyncAccount(Context context) {
//        // Create the account type and default account
//        //Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
//        // Get an instance of the Android account manager
//        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
//        /*
//         * Add the account and account type, no password or user data
//         * If successful, return the Account object, otherwise report an error.
//         */
//        if (accountManager.addAccountExplicitly(mAccount, null, null)) {
//            /*
//             * If you don't set android:syncable="true" in
//             * in your <provider> element in the manifest,
//             * then call context.setIsSyncable(account, AUTHORITY, 1)
//             * here.
//             */
//        	ContentResolver.setSyncAutomatically(mAccount, AccountGeneral.AUTHORITY, true); //this programmatically turns on the sync for new sync adapters.
//        	return mAccount;
//        } else {
//            /*
//             * The account exists or some other error occurred. Log this, report it,
//             * or handle it internally.
//             */
//        	return null;
//        }
//    }

}
