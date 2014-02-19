package org.chat.android;

import static org.chat.android.R.id.vaccine_name;
import static org.chat.android.R.id.vaccine_row;

import java.util.List;

import org.chat.android.models.Vaccine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImmunizationAdapter extends ArrayAdapter<Vaccine> {
	private LayoutInflater mInflater;
    private List<Vaccine> vaccineArray;

    public ImmunizationAdapter(Context context, int layoutResourceId, List<Vaccine> vaccineArray) {
        super(context, layoutResourceId, vaccineArray);
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.vaccineArray = vaccineArray;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = this.mInflater.inflate(R.layout.vaccine_listview_row, null);
        final Context context = getContext();
        
        Vaccine v = vaccineArray.get(position);

        TextView vName = null;
        if (convertView != null) {
            vName = (TextView)convertView.findViewById(vaccine_name);
            vName.setText(v.getLongName());
        }
        
        LinearLayout row = (LinearLayout)convertView.findViewById(vaccine_row);
        
        return convertView;
    }    
}
