package org.chat.android.pages;

import java.util.List;
import java.util.Locale;

import org.chat.android.ModelHelper;
import org.chat.android.R;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReferralFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_referral, container, false);
    	Context context = getActivity();
    	
    	TextView content1 = (TextView) view.findViewById(R.id.r1tv1);
    	TextView content2 = (TextView) view.findViewById(R.id.r1tv2);
    	TextView content3 = (TextView) view.findViewById(R.id.r1tv3);
    	TextView content4 = (TextView) view.findViewById(R.id.r1tv4);
    	
    	// determine language from current tablet settings
    	String lang = Locale.getDefault().getLanguage();
    	
    	int visitId = getArguments().getInt("visitId");
    	int clientId = getArguments().getInt("clientId");
    	List<HealthSelectRecorded> selects = ModelHelper.getHealthSelectRecordedsForVisitIdAndTopicNameAndClientId(context, visitId, "assessment", clientId);
    	
    	// if this gets any more complicated (waiting on Lisa for design), create class/model for this - see below
    	for (HealthSelectRecorded hsr : selects) {
    		int id = hsr.getSelectId();
    		if (id == 10003 || id == 10007 || id == 10009 || id == 10011 || id == 10016 || id == 10019 || id == 10024 || id == 10025 || id == 10027 || id == 10029 || id == 10031 || id == 10033) {
    			content1.setVisibility(View.VISIBLE);
    		}
    		if (id == 10005) {
    			content2.setVisibility(View.VISIBLE);
    		}
    		if (id == 10008) {
    			content3.setVisibility(View.VISIBLE);
    		}
    		if (id == 10025 || id == 10027) {
    			content4.setVisibility(View.VISIBLE);
    		}
    	}
    	
    	return view;
    }
}


/*
Class ReferralTrigger
int id: 1
int triggerId: 10005
String dangerSign: cough
String enContent: some stuff about ors
*/