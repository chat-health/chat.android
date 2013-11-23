package org.chat.android;

import static org.chat.android.R.id.client_row;
import static org.chat.android.R.id.client_name;
import static org.chat.android.R.id.client_gender;
import static org.chat.android.R.id.client_age;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.chat.android.Client;
import org.chat.android.R;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClientsAdapter extends ArrayAdapter<Client> {
	private LayoutInflater mInflater;
    private List<Client> clientsArray;
    private int visitId = 0;
    List<Client> presenceArrayList = new ArrayList<Client>();

//    public ClientsAdapter(Context context, int layoutResourceId, int checkboxId, List<Client> clientsArray) {
//        super(context, layoutResourceId, checkboxId, clientsArray);
    public ClientsAdapter(Context context, int layoutResourceId, List<Client> clientsArray, int vId) {
        super(context, layoutResourceId, clientsArray);
        visitId = vId;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.clientsArray = clientsArray;
    }

    /*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = this.mInflater.inflate(R.layout.attendance_listview_row, null);
        final Context context = getContext();
        
        Client c = clientsArray.get(position);

        TextView name = null;
        TextView gender = null;
        TextView age = null;
        CheckBox cb = null;
        if (convertView != null) {
            name = (TextView)convertView.findViewById(client_name);
            name.setText(c.getFirstName() + " " + c.getLastName());
            gender = (TextView)convertView.findViewById(client_gender);
            gender.setText(c.getGender());
            age = (TextView)convertView.findViewById(client_age);
            //TODO: add ages to clients
            age.setText(", aged 22");
            //age.setText(", aged "+calculateAge(c.getBirthday()));
            cb = (CheckBox) convertView.findViewById(R.id.checkbox);
            cb.setTag(c);
        }
        
        LinearLayout row = (LinearLayout)convertView.findViewById(client_row);
        
        // hacky way to override standard Android behaviour. Checkboxes have been made unclickable in the xml. presenceArrayList holds the checked (ie attending) client objects
        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
                Client c = (Client) cb.getTag();
                if(cb.isChecked()) {
                	cb.setChecked(false);
                	presenceArrayList.remove(c);
                } else {
                	cb.setChecked(true);
                	presenceArrayList.add(c);             	
                }

            }
        });
        
        // check all boxes for hh members that are already marked as present for this visit (restore state, essentially)    
		List<Attendance> cpList = new ArrayList<Attendance>();
        Dao<Attendance, Integer> cpDao;
        DatabaseHelper cpHelper = new DatabaseHelper(context);
        try {
			cpDao = cpHelper.getAttendanceDao();
			cpList = cpDao.query(cpDao.queryBuilder().prepare());
        	for (Attendance a : cpList) {
    			if (a.getVisitId() == visitId && a.getClientId() == c.getId()) {
    				cb.setChecked(true);
    				presenceArrayList.add(c);
    			}
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

        return convertView;
    }
    
    
    private String calculateAge (GregorianCalendar dob) {
    	String age_string;
    	GregorianCalendar today = new GregorianCalendar();
    	int age = today.get(GregorianCalendar.YEAR) - dob.get(GregorianCalendar.YEAR);
    	if (today.get(GregorianCalendar.DAY_OF_YEAR) <= dob.get(GregorianCalendar.DAY_OF_YEAR))
    	age--;
    	age_string = Integer.toString(age);
    	
    	return age_string;
    }

    
	public List<Client> getSelectedClients() {
		return presenceArrayList;
	}
}
