package org.chat.android;

import static org.chat.android.R.id.service_row;

import java.util.List;
import java.util.Locale;

import org.chat.android.models.Service;
import org.chat.android.models.ServiceAccessed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
	private List<ServiceAccessed> saList;
	int visitId = 0;
	int hhId = 0;
	
	String lang = Locale.getDefault().getLanguage();

    public ServicesAdapter(Context context, int layoutResourceId, List<Service> servicesArray, List<ServiceAccessed> sal, int vId, int hId) {
        super(context, layoutResourceId, servicesArray);
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.servicesArray = servicesArray;
        saList = sal;
        visitId = vId;
        hhId = hId;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	convertView = this.mInflater.inflate(R.layout.service_details_listview_row, null);
  
    	Service s = servicesArray.get(position);

    	TextView name = null;
    	if (convertView != null) {
    		LinearLayout row = (LinearLayout)convertView.findViewById(service_row);
    		row.setTag(s);
    		name = (TextView)convertView.findViewById(R.id.service_subtype_name);
    		name.setText(s.getName(lang));
    	}
    	
    	final CheckBox cb = (CheckBox) convertView.findViewById(R.id.service_subtype_checkbox);
        // go through the list of services delivered to this household on this visit
        for (ServiceAccessed sa : saList) {
            // if service matches tag, check off cb
        	if (sa.getServiceId() == s.getId()) {
        		cb.setChecked(true);
        	}
        }
    	
        LinearLayout row = (LinearLayout)convertView.findViewById(service_row);
        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	cb.setChecked(true);					// checking it off here, cause we never hit the onCreate/onResume to redraw the UI after delivering the service
            	
            	final Intent i = new Intent(getContext(), ServiceDeliveryActivity.class);
            	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	    	Bundle b = new Bundle();
    	    	Service s = (Service) v.getTag();
    	    	b.putString("serviceName",s.getName("en"));
    	    	b.putInt("visitId",visitId);
    	    	b.putInt("hhId",hhId);
    	    	b.putBoolean("adInfoFlag",false);
    	    	i.putExtras(b);    	
    	    	v.getContext().startActivity(i);
            }
        });
        
        
        

        

        // hacky way to override standard Android behaviour. Checkboxes have been made unclickable in the xml. presenceArrayList holds the checked (ie attending) client objects
//        row.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // maybe just do this with Strings instead of the whole object
//                Service s = (Service) cb.getTag();
//                if (cb.isChecked()) {
//                	cb.setChecked(false);
//                	selectedServices.remove(s);
//                } else {
//                	cb.setChecked(true);
//                	selectedServices.add(s);             	
//                }
//            }
//        });
        

    	return convertView;
    }
    

    
//	public ArrayList<String> getSelectedServices() {
//		ArrayList<String> sNames = new ArrayList<String>();
//		for (int i = 0; i < selectedServices.size(); i++) {
//			Service s = selectedServices.get(i);
//			sNames.add(s.getName(lang));
//		}
//		
//		return sNames;
//	}    
}
