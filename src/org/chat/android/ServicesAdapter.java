package org.chat.android;

import static org.chat.android.R.id.service_row;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.chat.android.models.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ServicesAdapter extends ArrayAdapter<Service> {
	private LayoutInflater mInflater;
	private List<Service> servicesArray;
	List<Service> selectedServices = new ArrayList<Service>();
	
	String lang = Locale.getDefault().getLanguage();

    public ServicesAdapter(Context context, int layoutResourceId, List<Service> servicesArray) {
        super(context, layoutResourceId, servicesArray);
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.servicesArray = servicesArray;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	convertView = this.mInflater.inflate(R.layout.service_details_listview_row, null);
  
    	Service s = servicesArray.get(position);

    	TextView name = null;
    	final CheckBox cb = (CheckBox) convertView.findViewById(R.id.service_subtype_checkbox);
    	if (convertView != null) {
    		name = (TextView)convertView.findViewById(R.id.service_subtype_name);
    		name.setText(s.getName(lang));
    		cb.setTag(s);
    	}
    	
        LinearLayout row = (LinearLayout)convertView.findViewById(service_row);
        
        // hacky way to override standard Android behaviour. Checkboxes have been made unclickable in the xml. presenceArrayList holds the checked (ie attending) client objects
        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // maybe just do this with Strings instead of the whole object
                Service s = (Service) cb.getTag();
                if (cb.isChecked()) {
                	cb.setChecked(false);
                	selectedServices.remove(s);
                } else {
                	cb.setChecked(true);
                	selectedServices.add(s);             	
                }
            }
        });    	
  
    	return convertView;
    }
    
	public ArrayList<String> getSelectedServices() {
		ArrayList<String> sNames = new ArrayList<String>();
		for (int i = 0; i < selectedServices.size(); i++) {
			Service s = selectedServices.get(i);
			sNames.add(s.getName(lang));
		}
		
		return sNames;
	}    
}
