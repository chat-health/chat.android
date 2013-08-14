package org.chat.android;

import static org.chat.android.R.id.client_name;
import static org.chat.android.R.id.attendance_age;
import java.util.GregorianCalendar;
import java.util.List;
import org.chat.android.Client;
import org.chat.android.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do your logic here eg. if ((CheckBox)v).isChecked()...
            	CheckBox checked = (CheckBox) v;
                Toast.makeText(v.getContext(),
                	       "Clicked on Checkbox: " + checked.getText() +
                	       " is " + checked.isChecked(),
                	       Toast.LENGTH_LONG).show();
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
