package org.chat.android;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.Resource;
import org.chat.android.models.ResourceAccessed;

import com.j256.ormlite.dao.Dao;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ResourcesActivity extends BaseActivity {
	Context context = null;
	Bundle bundle;
	ArrayList<Resource> resources;
	ArrayList<String> resourceNames;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.activity_resources);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		bundle = getIntent().getExtras();
		resources = new ArrayList<Resource>();
		resourceNames = new ArrayList<String>();

		// grab all the resources
		Dao<Resource, Integer> resDao;		
		DatabaseHelper resDbHelper = new DatabaseHelper(context);
		try {
			resDao = resDbHelper.getResourcesDao();
			List<Resource> resList = resDao.queryBuilder().query();
			Iterator<Resource> iter = resList.iterator();
			while (iter.hasNext()) {
				Resource res = iter.next();
				resources.add(res);
				resourceNames.add(res.getName());
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	
//		setListAdapter(new ArrayAdapter<String>(this, R.layout.resource_listview_row, resourceNames));
//		ListView rList = getListView();
		
		ListView lv = (ListView) findViewById(R.id.resources_listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.resource_listview_row, resourceNames);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//String res = (String)parent.getItemAtPosition(position);
				Resource res = resources.get(position);
				markResourceAccessed(res);
				openResource(res.getURI());
			}
	    });
	}
	
	public void openResource(String resURI) {
        // determining path to sdcard (readable by video player)
        File sdCard = Environment.getExternalStorageDirectory();
        // adding chat dir to path (copyAsset func ensures dir exists)
	    File dir = new File (sdCard.getAbsolutePath() + "/chat");
	    
	    // create file that points at the resource in the sdcard dir (to retrieve URI)
	    File resFile = new File(dir, resURI);
	    
	    Intent i = new Intent();
	    i.setAction(Intent.ACTION_VIEW);
	    i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	    i.setDataAndType(Uri.fromFile(resFile), "application/pdf");
	    startActivity(i);
	}
	
	public void markResourceAccessed(Resource res) {
		int workerId = 0;
		int visitId = 0;
		Date date = new Date();
		if (bundle.getInt("workerId") != 0) {
			workerId = bundle.getInt("workerId");
		}
		if (bundle.getInt("visitId") != 0) {
			visitId = bundle.getInt("visitId");
		}
		
		ResourceAccessed ra = new ResourceAccessed(res.getId(), visitId, workerId, date);
	    Dao<ResourceAccessed, Integer> raDao;
	    DatabaseHelper raDbHelper = new DatabaseHelper(context);
	    try {
	    	raDao = raDbHelper.getResourceAccessedDao();
	    	raDao.create(ra);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }	
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	Intent homeI = new Intent(this, HomeActivity.class);
        	Bundle homeB = new Bundle();
        	homeB.putInt("visitId",bundle.getInt("visitId"));
        	homeB.putBoolean("fromBack", true);
        	homeI.putExtras(homeB);
        	startActivity(homeI);
            return true;          
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}