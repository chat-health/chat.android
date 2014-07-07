package org.chat.android;

import static org.chat.android.R.id.client_row;
import static org.chat.android.R.id.client_name;
import static org.chat.android.R.id.client_metadata;

import java.util.ArrayList;
import java.util.List;

import org.chat.android.R;
import org.chat.android.models.Client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CHASelectChildAdapter extends ArrayAdapter<Client> {
	private LayoutInflater mInflater;
    private List<Client> clientsArray;
    private int visitId = 0;
    private int hhId = 0;
    List<Client> presenceArrayList = new ArrayList<Client>();

    
    public CHASelectChildAdapter(Context context, int layoutResourceId, List<Client> clientsArray, int vId, int hId) {
        super(context, layoutResourceId, clientsArray);
        visitId = vId;
        hhId = hId;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.clientsArray = clientsArray;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = this.mInflater.inflate(R.layout.cha_select_child_row, null);
        final Context context = getContext();
        
        final Client c = clientsArray.get(position);

        TextView name = null;
        TextView metadataTv = null;
        String metadata = null;
        if (convertView != null) {
            name = (TextView)convertView.findViewById(client_name);
            name.setText(c.getFirstName() + " " + c.getLastName());
            metadataTv = (TextView)convertView.findViewById(client_metadata);
            if (c.getGender().equals("male")) {
            	metadata = "male, ";
            	name.setTextColor(Color.parseColor("#0071bc"));
            } else if (c.getGender().equals("female")) {
            	metadata = "female, ";
            	name.setTextColor(Color.parseColor("#93278f"));
            } else {
            	Log.e("No gender assigned for", c.getFirstName()+" "+c.getLastName());
            }
            metadata += c.getAgeString();
            metadataTv.setText(metadata);
        }
        
        LinearLayout row = (LinearLayout)convertView.findViewById(client_row);

        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final Intent i = new Intent(context, CHAOverviewActivity.class);
            	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	    	Bundle b = new Bundle();
    	    	b.putInt("visitId",visitId);
    	    	b.putInt("hhId",hhId);
    	    	b.putInt("clientId",c.getId());
    	    	i.putExtras(b);    	
    	    	v.getContext().startActivity(i);
            }
        });

        return convertView;
    }
    
    

	public List<Client> getSelectedClients() {
		return presenceArrayList;
	}
}
