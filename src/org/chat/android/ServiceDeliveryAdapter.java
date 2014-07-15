package org.chat.android;

import static org.chat.android.R.id.service_delivery_row;
import static org.chat.android.R.id.service_delivery_client_name;
import static org.chat.android.R.id.service_delivery_checkbox;

import java.util.ArrayList;
import java.util.List;

import org.chat.android.R;
import org.chat.android.models.Client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceDeliveryAdapter extends ArrayAdapter<Client> {
	private LayoutInflater mInflater;
    private List<Client> clientsArray;
    private int visitId = 0;
    List<Client> presenceArrayList = new ArrayList<Client>();

    public ServiceDeliveryAdapter(Context context, int layoutResourceId, List<Client> clientsArray, int vId) {
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
        convertView = this.mInflater.inflate(R.layout.service_delivery_listview_row, null);
        
        Client c = clientsArray.get(position);

        TextView name = null;
        final CheckBox cb = (CheckBox) convertView.findViewById(service_delivery_checkbox);
        if (convertView != null) {
            name = (TextView)convertView.findViewById(service_delivery_client_name);
            name.setText(c.getFirstName() + " " + c.getLastName());
            cb.setTag(c);
            // removed this because we don't want all of the clients selected by default
            //cb.setChecked(true);
            //presenceArrayList.add(c);
        }
        
        LinearLayout row = (LinearLayout)convertView.findViewById(service_delivery_row);
        
        // hacky way to override standard Android behaviour. Checkboxes have been made unclickable in the xml. presenceArrayList holds the checked (ie attending) client objects
        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
        
        return convertView;
    }

    
	public List<Client> getSelectedClients() {
		return presenceArrayList;
	}
}
