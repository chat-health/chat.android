package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceDetailsFragment extends Fragment {
	
	
    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static ServiceDetailsFragment newInstance(int index) {
        ServiceDetailsFragment f = new ServiceDetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        if (container == null) {
            // Currently in a layout without a container, so no reason to create our view.
            return null;
        }
        
        int visitId = ((ServiceDeliveryActivity)getActivity()).visitId;
        int hhId = ((ServiceDeliveryActivity)getActivity()).hhId;

		View serviceDetailsFragment = inflater.inflate(R.layout.activity_home, container, false);
		ListView lv = (ListView) serviceDetailsFragment.findViewById(R.id.attendance_listview);
		Context context = getActivity();
		
		
		List<Client> hhCList = new ArrayList<Client>();
		// HHHAAACCCCCK
		//TextView list = (TextView) serviceDetailsFragment.findViewById(R.id.clients_attending_listview);
		TextView title = (TextView) serviceDetailsFragment.findViewById(R.id.attendance_list_title_field);
		title.setText("CHECK OFF NAMES OF CLIENTS TO WHOM THIS SERVICE WAS DELIVERED");
		

		// this is so gross - please fix
		List<Attendance> attList = new ArrayList<Attendance>();
		List<Attendance> attSubList = new ArrayList<Attendance>();
        Dao<Attendance, Integer> attendanceDao;
        DatabaseHelper attHelper = new DatabaseHelper(context);
        try {
			attendanceDao = attHelper.getAttendanceDao();
			attList = attendanceDao.query(attendanceDao.queryBuilder().prepare());
        	for (Attendance a : attList) {
    			if (a.getVisitId() == visitId) {
    				attSubList.add(a);
    			}
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Client> cList = new ArrayList<Client>();
    	// using HHID for now, there are cleaner versions than this, so... TODO, yuck
        Dao<Client, Integer> clientDao;
        DatabaseHelper cHelper = new DatabaseHelper(context);
        try {
			clientDao = cHelper.getClientsDao();
			cList = clientDao.query(clientDao.queryBuilder().prepare());
        	for (Client c : cList) {
    			for (Attendance a : attSubList) {
    				if (c.getId() == a.getClientId()) {
    					hhCList.add(c);
    				}
    			}
        	}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// ClientsAdapter adapter = new ClientsAdapter(context, android.R.layout.simple_list_item_multiple_choice, R.id.checkbox, cList, visitId);
    	ClientsAdapter adapter = new ClientsAdapter(context, android.R.layout.simple_list_item_multiple_choice, hhCList);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);
        
        // inflate the layout for this fragment
        return serviceDetailsFragment;		
    }


}