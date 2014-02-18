package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.chat.android.models.Attendance;
import org.chat.android.models.Client;

import com.j256.ormlite.dao.Dao;

import android.os.Bundle;
import android.widget.ListView;

public class CHASelectChildActivity extends BaseActivity {
	int visitId = 0;
	int hhId = 0;
	List<Client> presentClients = new ArrayList<Client>();	
	CHASelectChildAdapter adapter = null;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_cha_select_child);
        
		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		
		// grab list of present clients to show, based on the attendance
		populateMembersList();
		
		ListView lv = (ListView) findViewById(R.id.attending_children_listview);
		adapter = new CHASelectChildAdapter(context, android.R.layout.simple_list_item_1, presentClients, visitId);
	    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    lv.setAdapter(adapter);		
    }
    
    private void populateMembersList() {
    	// create the list of attending Clients
    	List<Integer> presentHHMembers = new ArrayList<Integer>();
        Dao<Attendance, Integer> aDao;
        List<Attendance> allAttendees = new ArrayList<Attendance>();
        DatabaseHelper attDbHelper = new DatabaseHelper(getApplicationContext());
        try {
			aDao = attDbHelper.getAttendanceDao();
			allAttendees = aDao.query(aDao.queryBuilder().prepare());
        	for (Attendance a : allAttendees) {
    			if (a.getVisitId() == visitId) {
    				presentHHMembers.add(a.getClientId());
    			}
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        Dao<Client, Integer> cDao;
        List<Client> allClients = new ArrayList<Client>();
        DatabaseHelper cDbHelper = new DatabaseHelper(getApplicationContext());
        try {
			cDao = cDbHelper.getClientsDao();
			allClients = cDao.query(cDao.queryBuilder().prepare());
        	for (Client c : allClients) {
        		for (Integer i : presentHHMembers) {
        			if (i == c.getId() && c.getAge() <= 5) {
        				presentClients.add(c);
        			}        			
        		}
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
    }
}
