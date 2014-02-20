package org.chat.android;

import static org.chat.android.R.id.vaccine_age;
import static org.chat.android.R.id.vaccine_name;
import static org.chat.android.R.id.vaccine_date_btn;
import static org.chat.android.R.id.vaccine_row;

import java.util.List;

import org.chat.android.models.Vaccine;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImmunizationAdapter extends ArrayAdapter<Vaccine> {
	private LayoutInflater mInflater;
    private List<Vaccine> vaccineArray;
    Context c;

    public ImmunizationAdapter(Context context, int layoutResourceId, List<Vaccine> vaccineArray) {
        super(context, layoutResourceId, vaccineArray);
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.vaccineArray = vaccineArray;
        c = context;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = this.mInflater.inflate(R.layout.vaccine_listview_row, null);
        //final Context context = getContext();
        
        Vaccine v = vaccineArray.get(position);

        TextView vAge = null;
        TextView vName = null;
        Button dateBtn = (Button)convertView.findViewById(vaccine_date_btn);
        if (convertView != null) {
        	vAge = (TextView)convertView.findViewById(vaccine_age);
            vAge.setText(v.getDisplayAge());
            vName = (TextView)convertView.findViewById(vaccine_name);
            vName.setText(v.getLongName());
        }
        
//        dateBtn.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//            	showDatePickerDialog();
//            }
//        });
        
        LinearLayout row = (LinearLayout)convertView.findViewById(vaccine_row);
        
        return convertView;
    }
    
//    public void showDatePickerDialog(View v) {
//    	DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "datePicker");
//
//    }
}
