package org.chat.android.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.chat.android.ModelHelper;
import org.chat.android.R;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.PageAssessment1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

public class Assessment1Fragment extends BaseFragment {
	Context context;
	int clientId = 0;
	
    TextView title = null;
    TextView content1 = null;
    TextView content2 = null;
    TextView content3 = null;
    RadioButton answer1_1 = null;
    RadioButton answer1_2 = null;
    RadioButton answer2_1 = null;
    RadioButton answer2_2 = null;
    RadioButton answer3_1 = null;
    RadioButton answer3_2 = null;
    List<HealthSelect> selects = null;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_assessment1, container, false);
    	context = getActivity();
    	
		title = (TextView) view.findViewById(R.id.a1title);
		content1 = (TextView) view.findViewById(R.id.a1content1);
		content2 = (TextView) view.findViewById(R.id.a1content2);
		content3 = (TextView) view.findViewById(R.id.a1content3);
		List<RadioButton> rbList = new ArrayList<RadioButton>();
		answer1_1 = (RadioButton) view.findViewById(R.id.a1rb1_1);
		rbList.add(answer1_1);
		answer1_2 = (RadioButton) view.findViewById(R.id.a1rb1_2);
		rbList.add(answer1_2);
		answer2_1 = (RadioButton) view.findViewById(R.id.a1rb2_1);
		rbList.add(answer2_1);
		answer2_2 = (RadioButton) view.findViewById(R.id.a1rb2_2);
		rbList.add(answer2_2);
		answer3_1 = (RadioButton) view.findViewById(R.id.a1rb3_1);
		rbList.add(answer3_1);
		answer3_2 = (RadioButton) view.findViewById(R.id.a1rb3_2);
		rbList.add(answer3_2);
		
		// determine language from current tablet settings
		String lang = Locale.getDefault().getLanguage();
		
		int visitId = getArguments().getInt("visitId");
		clientId = getArguments().getInt("clientId");
		int pageContentId = getArguments().getInt("id");
		
		selects = new ArrayList<HealthSelect>();
		selects = ModelHelper.getSelectsForSubjectId(getHelper(), pageContentId);
        
		populateDisplayedFragment(pageContentId, lang);
	    populateClickedRadio(view, visitId, rbList);
    	
    	return view;
    }
    
    public void populateDisplayedFragment(int pageContentId, String lang) {
    	PageAssessment1 pa1 = ModelHelper.getPageAssessment1ForId(getHelper(), pageContentId);
    	
    	// title
    	title.setText(pa1.getType());
    	
		// question
		content1.setText(pa1.getContent(lang, "content1"));

		// set up the radio buttons, tagged with ID (to be used when saving)
		if (selects.size() > 0) {
			answer1_1.setVisibility(View.VISIBLE);						// just need these until all the data is populated
			answer1_1.setText(selects.get(0).getContent(lang));
			answer1_1.setTag(selects.get(0).getId());
			answer1_2.setVisibility(View.VISIBLE);
			answer1_2.setText(selects.get(1).getContent(lang));
			answer1_2.setTag(selects.get(1).getId());
		}
		
		if (selects.size() > 2) {
			content2.setText(pa1.getContent(lang, "content2"));
			answer2_1.setText(selects.get(2).getContent(lang));
			answer2_1.setTag(selects.get(2).getId());
			answer2_2.setText(selects.get(3).getContent(lang));
			answer2_2.setTag(selects.get(3).getId());
		}
		
		if (selects.size() > 4) {
			content3.setText(pa1.getContent(lang, "content3"));
			answer3_1.setText(selects.get(4).getContent(lang));
			answer3_1.setTag(selects.get(4).getId());
			answer3_2.setText(selects.get(5).getContent(lang));
			answer3_2.setTag(selects.get(5).getId());
		}

    }
	
	// if the user has navigated back/forward to this page after previously having selected a radio
	public void populateClickedRadio(View view, int visitId, List<RadioButton> rbList) {
		// for each select element on the page
		for (RadioButton rb : rbList) {
			// possibly excessive checks to avoid nullpointerexceptions
			if (rb != null && rb.getTag() != null) {
				int selectId = (Integer) rb.getTag();
				// get the recorded select
				HealthSelectRecorded hsr = ModelHelper.getHealthSelectRecordedsForVisitIdAndTopicNameAndSelectIdAndClientId(getHelper(), visitId, "assessment", selectId, clientId);
				// if it exists, check it
				if (hsr != null) {
					rb.setChecked(true);
				}
			}
			
		}
		// if there is a second set of selects, and yes/first select is checked
		if (selects.size() > 2 && answer1_1.isChecked()) {
			content2.setVisibility(View.VISIBLE);
			answer2_1.setVisibility(View.VISIBLE);
			answer2_2.setVisibility(View.VISIBLE);
		}
		// if there is a second set of selects, and yes/first select is checked
		if (selects.size() > 4 && answer1_1.isChecked()) {
			content3.setVisibility(View.VISIBLE);
			answer3_1.setVisibility(View.VISIBLE);
			answer3_2.setVisibility(View.VISIBLE);
		}
	}
}