package org.chat.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONObject;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class HomeActivity extends Activity {

	String TAG = "INFO"; //Log.i(TAG, "Testing: "+somevar);

	private Visit visit;
	public int visitId = 0;
	public int hhId = 0;

    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        
		Bundle b = getIntent().getExtras();
		String hhName = b.getString("hhName");
		String workerName = b.getString("workerName");
		// TODO: grab the workerId from the DB based on the above - for now, dummy. Also, redo the hhId right
		int workerId = 123;
		if (hhName.equals("Ball Bella")) {
			hhId = 322;
		} else if (hhName.equals("Ziqubu Hleziphi")) {
			hhId = 1670;
		} else {
			Toast.makeText(getApplicationContext(), "ERROR: unknown household", Toast.LENGTH_LONG).show();
		}

		String role = b.getString("role");
		Date date  = new Date();

		String type = b.getString("type");
		double lat = b.getDouble("lat");
		double lon = b.getDouble("lon");
		Date startTime = new Date();	

		// create a new Visit object to be used for this visit - TODO: make sure that onCreate is only called once (ie not every time we return from the ServiceDeliveryActivity)
    	visit = new Visit(workerId, role, date, hhId, type, lat, lon, startTime);
    	
    	Dao<Visit, Integer> vDao;
    	DatabaseHelper dbHelper = new DatabaseHelper(context);
    	try {
    		vDao = dbHelper.getVisitsDao();
    		vDao.create(visit);
    		visitId = visit.get_id();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}    	
    	
//    	DatabaseHelper helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
//    	try {
//    		Dao vDao = helper.getDao(Visit.class);
//    		vDao = helper.getClientsDao();
//    		vDao.create(visit);
//    	} catch (SQLException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
    	

        setContentView(R.layout.activity_home);
 
        ActionBar actionbar = getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab AttendanceTab = actionbar.newTab().setText("Attendance");
        ActionBar.Tab ServicesTab = actionbar.newTab().setText("Services");
        ActionBar.Tab HealthEducationTab = actionbar.newTab().setText("Health Education");
        ActionBar.Tab ResourcesTab = actionbar.newTab().setText("Resources");
 
        // creating the four fragments we want to use for display content
        Fragment AttendanceFragment = new AttendanceFragment();
        Fragment ServicesFragment = new ServicesFragment();
        Fragment HealthEducationFragment = new HealthEducationFragment();
        Fragment ResourcesFragment = new ResourcesFragment();
 
        // setting up the tab click listeners
        AttendanceTab.setTabListener(new MyTabsListener(AttendanceFragment));
        ServicesTab.setTabListener(new MyTabsListener(ServicesFragment));
        HealthEducationTab.setTabListener(new MyTabsListener(HealthEducationFragment));
        ResourcesTab.setTabListener(new MyTabsListener(ResourcesFragment));
 
        // adding the two tabs to the actionbar
        actionbar.addTab(AttendanceTab);
        actionbar.addTab(ServicesTab);
        actionbar.addTab(HealthEducationTab);
        actionbar.addTab(ResourcesTab);
    }
    
    class MyTabsListener implements ActionBar.TabListener {
    	public Fragment fragment;
    	 
    	public MyTabsListener(Fragment fragment) {
    		this.fragment = fragment;
    	}
    	@Override
    	public void onTabReselected(Tab tab, FragmentTransaction ft) {
    		//Toast.makeText(getApplicationContext(), "Reselected!", Toast.LENGTH_LONG).show();
    	}
    	@Override
    	public void onTabSelected(Tab tab, FragmentTransaction ft) {
    		ft.replace(R.id.fragment_container, fragment);
    	}
    	@Override
    	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    		ft.remove(fragment);
    	}
    }    

    
    ////////// ATTENDANCE TAB //////////
    public class AttendanceFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	View attendanceFragmentView = inflater.inflate(R.layout.fragment_attendance, container, false);
        	ListView lv = (ListView) attendanceFragmentView.findViewById(R.id.attendance_listview);
        	Context context = getActivity();
        	
        	List<Client> cList = new ArrayList<Client>();
        	
        	// get visit object and get the family, then use that to select TODO: yuck - FIXME (figure out the proper selector with ORM layer)
            List<Client> hhCList = new ArrayList<Client>();
            
            Dao<Client, Integer> clientDao;
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            try {
				clientDao = dbHelper.getClientsDao();
				cList = clientDao.query(clientDao.queryBuilder().prepare());
	        	for (Client c : cList) {
	        		if (c.getHhId() == visit.getHhId()) {
	        			hhCList.add(c);
	        		}
	        		Log.d("el test", c.getFirstName());
	        	}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	ClientsAdapter adapter = new ClientsAdapter(context, android.R.layout.simple_list_item_multiple_choice, R.id.checkbox, hhCList, visitId);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setAdapter(adapter);
            
            // inflate the layout for this fragment
            return attendanceFragmentView;
        }
             
    }
    
    ////////// SERVICES TAB //////////
    public class ServicesFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	View servicesFragmentView = inflater.inflate(R.layout.fragment_services_overview, container, false);
        	Context context = getActivity();

        	
    		// SERVICES LIST
        	ListView sList = (ListView) servicesFragmentView.findViewById(R.id.services_overview_listview);

        	// display the list of types of services based on the user's role (visit.getRole()) - we need to be careful that the visit object is in sync btw this and the DB. Think about this more!
        	String[] serviceTypes;
        	String[] roleArray = getResources().getStringArray(R.array.role_array);
        	if (visit.getRole().equals(roleArray[0])) {
        		serviceTypes = getResources().getStringArray(R.array.volunteer_service_type_array);
        	} else if (visit.getRole().equals(roleArray[1])) {
        		serviceTypes = getResources().getStringArray(R.array.councelor_service_type_array);
        	} else {
        		// TODO: expand me? Also throw a proper error here
        		serviceTypes = getResources().getStringArray(R.array.volunteer_service_type_array);
        		Toast.makeText(getApplicationContext(),"Role is undefined",Toast.LENGTH_LONG).show();
        	}		
        	ArrayList<String> sArray = new ArrayList<String>(Arrays.asList(serviceTypes));

        	ServicesOverviewAdapter sAdapter = new ServicesOverviewAdapter(context, android.R.layout.simple_list_item_1, sArray);
            sList.setAdapter(sAdapter);
    		sList.setOnItemClickListener(new OnItemClickListener() {
    			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    				String serviceCategory = (String)parent.getItemAtPosition(position);
    				Toast.makeText(getApplicationContext(), serviceCategory, Toast.LENGTH_SHORT).show();
    				Intent i = new Intent(HomeActivity.this, ServiceDeliveryActivity.class);
    				// TODO: again, decide if maybe we want to just re-grab this stuff from the DB instead of passing it? If no, we may to need a bunch more stuff bundled in
    				Bundle b = new Bundle();
    				b.putString("serviceCategory",serviceCategory);
    				b.putInt("visitId", visitId);
    				b.putInt("hhId", visit.getHhId());						// for convenience, would be cleaner to remove
    				i.putExtras(b);
					startActivity(i);
    			}
    		});
    		
    		
    		// NOTES LIST
    		ListView nList = (ListView) servicesFragmentView.findViewById(R.id.notes_listview);
    		final ArrayList<String> nArray = new ArrayList<String>();
    		ServicesOverviewAdapter nAdapter = new ServicesOverviewAdapter(context, android.R.layout.simple_list_item_1, nArray);
    		nList.setAdapter(nAdapter);
    		
    		
    		// NEW NOTES
        	final EditText newNoteField = (EditText) servicesFragmentView.findViewById(R.id.notes_edittext);
        	//String newNote = newNoteField.getText().toString();
    		Button submitNoteBtn = (Button) servicesFragmentView.findViewById(R.id.notes_button);
    		submitNoteBtn.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				String newNote = newNoteField.getText().toString();
    				Toast.makeText(getApplicationContext(),"Added note",Toast.LENGTH_LONG).show();
    				nArray.add(newNote);						// THIS MAY NEED TO GET SAVED TO THE DB
    				newNoteField.setText("");
    			}
    	    });
            
            // inflate the layout for this fragment
            return servicesFragmentView;
        }
    }
    

    
    ////////// HEALTH EDUCATION TAB //////////
    public class HealthEducationFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_health_education, container, false);
        }
    }
    
    
    ////////// RESOURCES TAB //////////
    public class ResourcesFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	View resourcesFragmentView = inflater.inflate(R.layout.fragment_resources, container, false);
        	Context context = getActivity();
        	
    		ListView rList = (ListView) resourcesFragmentView.findViewById(R.id.resources_listview);
    		ArrayList<String> rArray = new ArrayList<String>();																		// TODO: fill me (from db) with resource titles
    		ServicesOverviewAdapter rAdapter = new ServicesOverviewAdapter(context, android.R.layout.simple_list_item_1, rArray);
    		rList.setAdapter(rAdapter);
        	
        	
            // inflate the layout for this fragment
            return resourcesFragmentView;
        }
    }
    
    
    
    
    public void playVideo (View v) {
    	// figure out which video to play by determining which button was pressed
    	String videoName = "";
    	int buttonId = v.getId();
    	switch (buttonId) {
	        case R.id.button_video_1:
	        	videoName = "pss_animatic.mp4";
	            break;
	        case R.id.button_video_2:
	        	videoName = "nutrition_animatic.mp4";
	            break;
	        case R.id.button_video_3:
	        	videoName = "nutrition_0-9_months.mp4";
	            break;
	        case R.id.button_video_4:
	        	videoName = "nutrition_2_years_up.mp4";
	            break;
	        case R.id.image_video_1:
	        	videoName = "pss_animatic.mp4";
	            break;
	        case R.id.image_video_2:
	        	videoName = "nutrition_animatic.mp4";
	            break;
	        case R.id.image_video_3:
	        	videoName = "nutrition_0-9_months.mp4";
	            break;
	        case R.id.image_video_4:
	        	videoName = "nutrition_2_years_up.mp4";
	            break;	            
	        default:
	        	videoName = "pss_animatic.mp4";
	            break;
	    }
        
    	// determining path to sdcard (readable by video player)
    	File sdCard = Environment.getExternalStorageDirectory();
    	// adding chat dir to path (copyAsset func ensures dir exists)
        File dir = new File (sdCard.getAbsolutePath() + "/chat");
        // copy video from within the APK to the sdcard/chat dir
        // copyAsset(videoName, dir);
        
        // create file that points at video in sdcard dir (to retrieve URI)
        File myvid = new File(dir, videoName);
        
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(Uri.fromFile(myvid), "video/*");
        startActivity(intent);
    }
    
    private void copyAsset(String fileToCopy, File targetDir) {
        InputStream in = null;
        OutputStream out = null;
        try {
          in = getAssets().open(fileToCopy);
          
          if(targetDir.isDirectory() != true) {
        	  targetDir.mkdirs();
          }
          
          out = new FileOutputStream(new File(targetDir, fileToCopy));
          copyFile(in, out);
          in.close();
          in = null;
          out.flush();
          out.close();
          out = null;
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + fileToCopy, e);
        }   
    }
    
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }
    
    
    // OVERFLOW MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_settings:
	        Toast.makeText(getApplicationContext(), "Settings is under construction, currently being used to prepopulate the DB", Toast.LENGTH_SHORT).show();
	        prepopulateDB();
	        return true;
	    case R.id.menu_logout:
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("Are you sure you want to do this?")
	    	       .setCancelable(false)
	    	       .setPositiveButton("Yes, log out", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   finish();
	    	           }
	    	       })
	    	       .setNegativeButton("No, cancel", new DialogInterface.OnClickListener() {
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

}
