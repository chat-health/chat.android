package org.chat.android;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: make resource class and dump this all in the DB
		ArrayList<String> rArray = new ArrayList<String>();
        rArray.add("0-6mos.pdf");
        rArray.add("6-12mos.pdf");
	
		setListAdapter(new ArrayAdapter<String>(this, R.layout.resource_listview_row, rArray));
	
		ListView rList = getListView();
		
		rList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String res = (String)parent.getItemAtPosition(position);
				openResource(res);
			
			}
	    });
	}
	
	public void openResource(String res) {
        // determining path to sdcard (readable by video player)
        File sdCard = Environment.getExternalStorageDirectory();
        // adding chat dir to path (copyAsset func ensures dir exists)
	    File dir = new File (sdCard.getAbsolutePath() + "/chat");
	    
	    // create file that points at the resource in the sdcard dir (to retrieve URI)
	    File resFile = new File(dir, res);
	    
	    Intent i = new Intent();
	    i.setAction(Intent.ACTION_VIEW);
	    i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	    i.setDataAndType(Uri.fromFile(resFile), "application/pdf");
	    startActivity(i);
	}
}