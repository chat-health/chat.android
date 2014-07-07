package org.chat.android.pages;

import java.util.List;
import java.util.Locale;

import org.chat.android.ModelHelper;
import org.chat.android.R;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ReferralFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_referral, container, false);
    	Context context = getActivity();
    	
    	TextView referalTextBox = (TextView) view.findViewById(R.id.referal);
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
    		if (id == 1003 || id == 1007 || id == 1009 || id == 1011 || id == 1013 || id == 1015 || id == 1020 || id == 1023 || id == 1028 || id == 1029 || id == 1031 || id == 1033 || id == 1035 || id == 1037) {
    			referalTextBox.setVisibility(View.VISIBLE);
    			//sendReferral();			// maybe pass a param here? also need to have a true flag set in the if
    		}
    		if (id == 1004 || id == 1008 || id == 1010 || id == 1012 || id == 1014 || id == 1016) {
    			content1.setVisibility(View.VISIBLE);
    		}
    		if (id == 1007 || id == 1008) {
    			content2.setVisibility(View.VISIBLE);
    		}
    		if (id == 1008) {
    			content3.setVisibility(View.VISIBLE);
    		}
    		if (id == 1029 || id == 1031) {
    			content4.setVisibility(View.VISIBLE);
    		}
    	}
    	
    	return view;
    }
    
    private void sendReferral() {
    	try {
    		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("sms_body", "default content"); 
			sendIntent.setType("vnd.android-dir/mms-sms");			
			startActivity(sendIntent);
			Toast.makeText(getActivity(),
					"I sent, in theory",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getActivity(),
				"SMS failed, please try again later!",
				Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

    }
}


/*
Class ReferralTrigger
int id: 1
int triggerId: 10005
String dangerSign: cough
String enContent: some stuff about ors
*/