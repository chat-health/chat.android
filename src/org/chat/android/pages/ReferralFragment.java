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
	Context context;
    TextView content1 = null;
    TextView content2 = null;
    TextView content3 = null;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_referral, container, false);
    	context = getActivity();
    	
    	content1 = (TextView) view.findViewById(R.id.r1tv1);
		content2 = (TextView) view.findViewById(R.id.r1tv2);
		content3 = (TextView) view.findViewById(R.id.r1tv3);
    	
    	// determine language from current tablet settings
    	String lang = Locale.getDefault().getLanguage();
    	
    	int visitId = getArguments().getInt("visitId");
    	int clientId = getArguments().getInt("clientId");
    	List<HealthSelectRecorded> selects = ModelHelper.getHealthSelectRecordedsForVisitIdAndTopicNameAndClientId(context, visitId, "assessment", clientId);
    	
    	for (HealthSelectRecorded hsr : selects) {
    		if (hsr.getSelectId() == 10005) {
    			content1.setVisibility(View.VISIBLE);
    		}
    		if (hsr.getSelectId() == 10008) {
    			content2.setVisibility(View.VISIBLE);
    		}
    		if (hsr.getSelectId() == 10025 || hsr.getSelectId() == 10027) {
    			content3.setVisibility(View.VISIBLE);
    		}
    	}
    	
    	return view;
    }
}