package org.chat.android.pages;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.chat.android.DatabaseHelper;
import org.chat.android.ModelHelper;
import org.chat.android.R;
import org.chat.android.R.layout;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.PageSelect1;
import org.chat.android.models.PageText1;

import com.j256.ormlite.dao.Dao;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Select1Fragment extends Fragment {
	Context context;
    TextView title = null;
    TextView content1 = null;
    RadioButton answer1 = null;
    RadioButton answer2 = null;
    RadioButton answer3 = null;
    RadioButton answer4 = null;
    //ImageView image1 = null;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_select1, container, false);
    	context = getActivity();
    	
		title = (TextView) view.findViewById(R.id.s1title);
		content1 = (TextView) view.findViewById(R.id.s1content1);
		answer1 = (RadioButton) view.findViewById(R.id.s1rb1);
		answer2 = (RadioButton) view.findViewById(R.id.s1rb2);
		answer3 = (RadioButton) view.findViewById(R.id.s1rb3);
		answer4 = (RadioButton) view.findViewById(R.id.s1rb4);
		//image1 = (ImageView) view.findViewById(R.id.s1i1);
		
		// determine language from current tablet settings
		String lang = Locale.getDefault().getLanguage();
		
		int visitId = getArguments().getInt("visitId");
		String themeName = getArguments().getString("themeName");
		String topicName = getArguments().getString("topicName");
		int id = getArguments().getInt("id");
        
	    populateDisplayedFragment(themeName, topicName, id, lang);
	    populateClickedRadio(view, visitId, topicName);
    	
    	return view;
    }
    
	public void populateDisplayedFragment(String themeName, String topicName, int pageContentId, String lang) {
		// question
		PageSelect1 ps = ModelHelper.getPageSelect1ForId(context, pageContentId);
		content1.setText(ps.getContent(lang, "content1"));
		
		// responses/selects
		List<HealthSelect> selects = new ArrayList<HealthSelect>();
		selects = ModelHelper.getSelectsForSubjectId(context, ps.getId());
		if (selects.size() == 4) {
			// set up the radio buttons, tagged with ID (to be used when saving) - TODO: make me work with Zulu (in the model)
			answer1.setText(selects.get(0).getEnContent());
			answer1.setTag(selects.get(0).getId());
			answer2.setText(selects.get(1).getEnContent());
			answer2.setTag(selects.get(1).getId());
			answer3.setText(selects.get(2).getEnContent());
			answer3.setTag(selects.get(2).getId());
			answer4.setText(selects.get(3).getEnContent());
			answer4.setTag(selects.get(3).getId());
		}
		
		// colors
		HealthTheme theme = ModelHelper.getThemeForName(context, themeName);
		int colorRef = Color.parseColor(theme.getColor());
		title.setTextColor(colorRef);
    }
	
	
	
	// NOTE THIS SHOULDNT WORK - WILL RETURN MULTIPLE IDS, SEE BELOW
	// if the user has navigated back/forward to this page after previously having selected a radio
	public void populateClickedRadio(View view, int visitId, String topicName) {
		HealthSelectRecorded hsr = ModelHelper.getHealthSelectRecordedForVisitIdAndTopicName(context, visitId, topicName);
		if (hsr != null) {
			int selectId = hsr.getSelectId();
			HealthSelect hs = ModelHelper.getHealthSelectForId(context, selectId);
			int radioId = hs.getId();
			RadioButton rb = (RadioButton)view.findViewWithTag(radioId);
			rb.setChecked(true);
		}
	}
	
//	public void populateClickedRadio(View view, int visitId, List<RadioButton> rbList) {
//		// for each select element on the page
//		for (RadioButton rb : rbList) {
//			// possibly excessive checks to avoid nullpointerexceptions
//			if (rb != null && rb.getTag() != null) {
//				int selectId = (Integer) rb.getTag();
//				// get the recorded select
//				HealthSelectRecorded hsr = ModelHelper.getHealthSelectRecordedsForVisitIdAndTopicNameAndSelectId(context, visitId, "assessment", selectId);
//				// if it exists, check it
//				if (hsr != null) {
//					rb.setChecked(true);
//				}
//			}
//			
//		}
//		// if there is a second set of selects, and yes/first select is checked
//		if (selects.size() > 2 && answer1_1.isChecked()) {
//			content2.setVisibility(View.VISIBLE);
//			answer2_1.setVisibility(View.VISIBLE);
//			answer2_2.setVisibility(View.VISIBLE);
//		}
//	}
	
	
	
	
	
}