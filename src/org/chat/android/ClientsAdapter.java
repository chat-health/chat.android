package org.chat.android;

import static org.chat.android.R.id.client_name;
import static org.chat.android.R.id.attendance_age;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClientsAdapter extends ArrayAdapter<Client> {
	private LayoutInflater mInflater;
    private List<Client> clientsArray;

    public ClientsAdapter(Context context, int layoutResourceId, int checkboxId, List<Client> clientsArray) {
        super(context, layoutResourceId, checkboxId, clientsArray);

        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.clientsArray = clientsArray;
    }

    /*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = this.mInflater.inflate(R.layout.attendance_listview_row, null);
        
        Client c = clientsArray.get(position);

        TextView tv = null;
        TextView age;
        if (convertView != null) {
            tv = (TextView)convertView.findViewById(client_name);
            tv.setText(c.getFirstName() + " " + c.getLastName());
            
//            age = (TextView)convertView.findViewById(attendance_age);
//            age.setText(calculateAge(c.getBirthday()));
        }      
        
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox);
        
        // add the current client object to the checkbox and set up the listener to save to the Attendace obj
        cb.setTag(c);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	CheckBox checked = (CheckBox) v;
                // Toast.makeText(v.getContext(), "Clicked on Checkbox: " + checked.getTag() + " is " + checked.isChecked(), Toast.LENGTH_LONG).show();
                
                // the client associated with the selected checkbox
                Client c = (Client) checked.getTag();
                int clientId = c.get_id();
                if (checked.isChecked() == true) {
                    // TODO: v.getVisitId()
                    Attendance a = new Attendance(1, clientId);
                    Dao<Attendance, Integer> aDao;
                    DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                    try {
                    	aDao = dbHelper.getAttendanceDao();
                    	aDao.create(a);
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }                	
                } else {
                    DatabaseHelper helper = OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);					// TODO: figure out more about this OpenHelperManager - could it replace the work of building everything manually in DatabaseHelper
                    Dao aDao;
				    try {
					    aDao = helper.getDao(Attendance.class);
					    DeleteBuilder<Attendance, Integer> deleteBuilder = aDao.deleteBuilder();
					    deleteBuilder.where().eq("client_id", clientId);
					    deleteBuilder.delete(); 
				    } catch (SQLException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				    }
                	
                }


            }
         });  
        
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
}
