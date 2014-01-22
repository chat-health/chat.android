package org.chat.android;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.Household;
import org.chat.android.models.Resource;

import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ResourcesActivity extends ListActivity {
	Context context = null;
	ArrayList<Resource> resources;
	ArrayList<String> resourceNames;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		
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
	
		setListAdapter(new ArrayAdapter<String>(this, R.layout.resource_listview_row, resourceNames));
		ListView rList = getListView();
		
		rList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//String res = (String)parent.getItemAtPosition(position);
				Resource res = resources.get(position);
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
}