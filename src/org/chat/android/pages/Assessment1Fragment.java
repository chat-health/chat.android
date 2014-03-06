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
import org.chat.android.models.PageAssessment1;
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

public class Assessment1Fragment extends Fragment {
	Context context;
    TextView title = null;
    TextView content1 = null;
    TextView content2 = null;
    RadioButton answer1_1 = null;
    RadioButton answer1_2 = null;
    RadioButton answer2_1 = null;
    RadioButton answer2_2 = null;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_assessment1, container, false);
    	context = getActivity();
    	
		title = (TextView) view.findViewById(R.id.a1title);
		content1 = (TextView) view.findViewById(R.id.a1content1);
		content2 = (TextView) view.findViewById(R.id.a1content2);
		answer1_1 = (RadioButton) view.findViewById(R.id.a1rb1_1);
		answer1_2 = (RadioButton) view.findViewById(R.id.a1rb1_2);
		answer2_1 = (RadioButton) view.findViewById(R.id.a1rb2_1);
		answer2_2 = (RadioButton) view.findViewById(R.id.a1rb2_2);
		
		// determine language from current tablet settings
		String lang = Locale.getDefault().getLanguage();
		
		int visitId = getArguments().getInt("visitId");
		int pageContentId = getArguments().getInt("id");
        
		populateDisplayedFragment(pageContentId, lang);
	    populateClickedRadio(view, visitId);
    	
    	return view;
    }
    
    public void populateDisplayedFragment(int pageContentId, String lang) {
    	PageAssessment1 pa1 = ModelHelper.getPageAssessment1ForId(context, pageContentId);
    	
    	// title
    	title.setText(pa1.getType());
    	
		// question
		content1.setText(pa1.getContent(lang, "content1"));
		
		// responses/selects
		List<HealthSelect> selects = new ArrayList<HealthSelect>();
		//selects = ModelHelper.getSelectsForSubjectId(context, ps.getId());
		selects = ModelHelper.getSelectsForSubjectId(context, pageContentId);

		// set up the radio buttons, tagged with ID (to be used when saving) - TODO: make me work with Zulu (in the model)
		if (selects.size() > 0) {
			answer1_1.setVisibility(View.VISIBLE);						// just need these until all the data is populated
			answer1_1.setText(selects.get(0).getEnContent());
			answer1_1.setTag(selects.get(0).getId());
			answer1_2.setVisibility(View.VISIBLE);
			answer1_2.setText(selects.get(1).getEnContent());
			answer1_2.setTag(selects.get(1).getId());
		}
		
		if (selects.size() > 2) {
			content2.setText(pa1.getContent(lang, "content2"));
			answer2_1.setText(selects.get(2).getEnContent());
			answer2_1.setTag(selects.get(2).getId());
			answer2_2.setText(selects.get(3).getEnContent());
			answer2_2.setTag(selects.get(3).getId());
		}
		
//		if (selects.size() > 4) {
//			content3.setText(pa1.getContent(lang, "content3"));
//			answer2_1.setText(selects.get(2).getEnContent());
//			answer2_1.setTag(selects.get(2).getId());
//			answer2_2.setText(selects.get(3).getEnContent());
//			answer2_2.setTag(selects.get(3).getId());
//		}

    }
	
	// if the user has navigated back/forward to this page after previously having selected a radio
	public void populateClickedRadio(View view, int visitId) {
//		HealthSelectRecorded hsr = ModelHelper.getHealthSelectRecordedsForVisitIdAndTopicName(context, visitId, "assessment");
//		if (hsr != null) {
//			int selectId = hsr.getSelectId();
//			HealthSelect hs = ModelHelper.getHealthSelectForId(context, selectId);
//			int radioId = hs.getId();
//			RadioButton rb = (RadioButton)view.findViewWithTag(radioId);
//			rb.setChecked(true);
//		}
	}
}