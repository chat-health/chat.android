package org.chat.android;

import static org.chat.android.R.id.client_name;
import static org.chat.android.R.id.attendance_age;

import java.util.ArrayList;
import java.util.GregorianCalendar;

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
    private ArrayList<Client> clientsArray;

    public ClientsAdapter(Context context, int layoutResourceId, int checkboxId, ArrayList<Client> clientsArray, Visit visit) {
        super(context, layoutResourceId, checkboxId, clientsArray);

        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.clientsArray = clientsArray;
    }
    
//    private class ViewHolder {
//	   TextView code;
//	   CheckBox name;
//    }    

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
            tv.setText(c.getFirstname() + " " + c.getLastname());
            
            age = (TextView)convertView.findViewById(attendance_age);
            age.setText(calculateAge(c.getBirthday()));
        }
        
//        ViewHolder holder = new ViewHolder();
//        holder.code = (TextView) convertView.findViewById(R.id.client_name);
//        holder.name = (CheckBox) convertView.findViewById(R.id.checkbox);
//        convertView.setTag(holder);
//        
//        //CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox);
//        holder.name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Do your logic here eg. if ((CheckBox)v).isChecked()...
//            	CheckBox cb = (CheckBox) v;
//            	Client client = (Client) cb.getTag(); 
//                Toast.makeText(v.getContext(),
//                	       "Clicked on Checkbox: " + cb.getText() +					// cb.getText not working...
//                	       " is " + cb.isChecked(),
//                	       Toast.LENGTH_LONG).show();
//            }
//         });  
        
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
