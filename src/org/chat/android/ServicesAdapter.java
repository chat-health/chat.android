package org.chat.android;

import static org.chat.android.R.id.service_row;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ServicesAdapter extends ArrayAdapter<Service> {
	private LayoutInflater mInflater;
	private List<Service> servicesArray;
	List<Service> selectedServices = new ArrayList<Service>();
	//String selectedServices[];
    //private int visitId = 0;    , int vId

    public ServicesAdapter(Context context, int layoutResourceId, List<Service> servicesArray) {
        super(context, layoutResourceId, servicesArray);
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.servicesArray = servicesArray;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	convertView = this.mInflater.inflate(R.layout.service_details_listview_row, null);
    	final Context context = getContext();
  
    	Service s = servicesArray.get(position);

    	TextView name = null;
    	CheckBox cb = null;
    	if (convertView != null) {
    		name = (TextView)convertView.findViewById(R.id.service_subtype_name);
    		name.setText(s.getName());
    		cb = (CheckBox) convertView.findViewById(R.id.service_subtype_checkbox);
    		cb.setTag(s);
    	}
    	
        LinearLayout row = (LinearLayout)convertView.findViewById(service_row);
        
        // hacky way to override standard Android behaviour. Checkboxes have been made unclickable in the xml. presenceArrayList holds the checked (ie attending) client objects
        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v.findViewById(R.id.service_subtype_checkbox);
                // maybe just do this with Strings instead of the whole object
                Service s = (Service) cb.getTag();
                if(cb.isChecked()) {
                	cb.setChecked(false);
                	selectedServices.remove(s);
                } else {
                	cb.setChecked(true);
                	selectedServices.add(s);             	
                }
            }
        });    	
  
    	//LinearLayout row = (LinearLayout)convertView.findViewById(client_row);    
    	return convertView;
    }
    
    
    // TODO!
    // check all boxes for hh members that are already marked as present for this visit (restore state, essentially)    
//	List<Attendance> cpList = new ArrayList<Attendance>();
//    Dao<Attendance, Integer> cpDao;
//    DatabaseHelper cpHelper = new DatabaseHelper(context);
//    try {
//		cpDao = cpHelper.getAttendanceDao();
//		cpList = cpDao.query(cpDao.queryBuilder().prepare());
//    	for (Attendance a : cpList) {
//			if (a.getVisitId() == visitId && a.getClientId() == c.getId()) {
//				cb.setChecked(true);
//				presenceArrayList.add(c);
//			}
//    	}
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}      
    
	public ArrayList<String> getSelectedServices() {
		ArrayList<String> sNames = new ArrayList<String>();
		for (int i = 0; i < selectedServices.size(); i++) {
			Service s = selectedServices.get(i);
			sNames.add(s.getName());
		}
		
		return sNames;
	}    
}
