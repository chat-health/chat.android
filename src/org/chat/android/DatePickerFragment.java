package org.chat.android;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.chat.android.models.VaccineRecorded;
import org.chat.android.models.Visit;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	int visitId = 0;
	int clientId = 0;
	public Button activity_date_selected;

	public DatePickerFragment(View v) {
		activity_date_selected = (Button)v;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle b = this.getArguments();
        visitId = b.getInt("visitId");
        clientId = b.getInt("clientId");
        
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
	
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		int vaccineId = (Integer) activity_date_selected.getTag();
		
		GregorianCalendar gc = new GregorianCalendar(year, month, day);
		Date d = gc.getTime();
//		Date d = new Date(year, month, day);
		VaccineRecorded vr = new VaccineRecorded(vaccineId, clientId, visitId, d);
    	
    	Dao<VaccineRecorded, Integer> vrDao;
    	DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
    	try {
    		vrDao = dbHelper.getVaccineRecordedDao();
    		vrDao.create(vr);
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} 
    	
    	// wish we had a backbone style MVC thing going. So UI gets updated separately from the save. They'll be unsynced until next 
		activity_date_selected.setText(String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(day));
		LinearLayout row = (LinearLayout) activity_date_selected.getParent();
		row.getChildAt(0).setVisibility(View.INVISIBLE);
		int c = getResources().getColor(android.R.color.black);					// an attempt to get the original color. TODO: get the right color here
		((TextView) row.getChildAt(1)).setTextColor(c);
		((TextView) row.getChildAt(2)).setTextColor(c);
		
	}
}