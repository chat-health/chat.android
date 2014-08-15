package org.chat.android;

import static org.chat.android.R.id.client_row;
import static org.chat.android.R.id.client_name;
import static org.chat.android.R.id.client_metadata;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.chat.android.R;
import org.chat.android.models.Attendance;
import org.chat.android.models.Client;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClientsAdapter extends ArrayAdapter<Client> {
	private LayoutInflater mInflater;
    private List<Client> clientsArray;
    private int visitId = 0;
    List<Client> presenceArrayList = new ArrayList<Client>();
    static String lang = Locale.getDefault().getLanguage();
    // since we aren't OrmLiteBaseActivity or BaseActivity we can't use getHelper()
    // so we use OpenHelperManager
    private DatabaseHelper databaseHelper = null;


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
        TextView metadataTv = null;
        CheckBox cb = null;
        String metadata = null;
        if (convertView != null) {
            name = (TextView)convertView.findViewById(client_name);
            name.setText(c.getFirstName() + " " + c.getLastName());
            metadataTv = (TextView)convertView.findViewById(client_metadata);
            // gender.setText(c.getGender());
            if (c.getGender().equals("male")) {
            	if (lang.equals("zu")) {
            		metadata = "owesilisa, ";
            	} else {
            		metadata = "male, ";
            	}
            	name.setTextColor(Color.parseColor("#0071bc"));
            } else if (c.getGender().equals("female")) {
            	if (lang.equals("zu")) {
            		metadata = "owesifazane, ";
            	} else {
            		metadata = "female, ";
            	}
            	name.setTextColor(Color.parseColor("#93278f"));
            } else {
            	Log.e("No gender assigned for", c.getFirstName()+" "+c.getLastName());
            }
            metadata += c.getAgeString();
            metadataTv.setText(metadata);
            cb = (CheckBox) convertView.findViewById(R.id.checkbox);
            cb.setTag(c);
        }

        LinearLayout row = (LinearLayout)convertView.findViewById(client_row);

        // hacky way to override standard Android behaviour. Checkboxes have been made unclickable in the xml. presenceArrayList holds the checked (ie attending) client objects
        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
                Client c = (Client) cb.getTag();
                if (cb.isChecked()) {
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
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        try {
			cpDao = databaseHelper.getAttendanceDao();
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

	public List<Client> getSelectedClients() {
		return presenceArrayList;
	}
}
