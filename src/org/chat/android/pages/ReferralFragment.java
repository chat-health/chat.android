package org.chat.android.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.chat.android.BaseActivity;
import org.chat.android.Mail;
import org.chat.android.ModelHelper;
import org.chat.android.R;
import org.chat.android.models.Client;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.Household;
import org.chat.android.models.Worker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReferralFragment extends Fragment {
	private Mail m;
	private Context context;
	
	int visitId = 0;
	int clientId = 0;
	int hhId = 0;
	String emailContentStr = "";
	List<String> hsrContent = new ArrayList<String>();
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_referral, container, false);
    	context = getActivity();
    	
    	TextView referalTextBox = (TextView) view.findViewById(R.id.referal);
    	TextView content1 = (TextView) view.findViewById(R.id.r1tv1);
    	TextView content2 = (TextView) view.findViewById(R.id.r1tv2);
    	TextView content3 = (TextView) view.findViewById(R.id.r1tv3);
    	TextView content4 = (TextView) view.findViewById(R.id.r1tv4);
    	
    	// determine language from current tablet settings
    	String lang = Locale.getDefault().getLanguage();
    	
    	visitId = getArguments().getInt("visitId");
    	clientId = getArguments().getInt("clientId");
    	hhId = getArguments().getInt("hhId");
    	Boolean referalFlag = false;
    	
    	List<HealthSelectRecorded> selects = ModelHelper.getHealthSelectRecordedsForVisitIdAndTopicNameAndClientId(context, visitId, "assessment", clientId);
    	
    	
    	// if this gets any more complicated (waiting on Lisa for design), create class/model for this - see below
    	for (HealthSelectRecorded hsr : selects) {
    		int id = hsr.getSelectId();
    		if (id == 1003 || id == 1007 || id == 1009 || id == 1011 || id == 1013 || id == 1015 || id == 1020 || id == 1023 || id == 1028 || id == 1029 || id == 1031 || id == 1033 || id == 1035 || id == 1037) {
    			referalTextBox.setVisibility(View.VISIBLE);
    			referalFlag = true;
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
    		
    		emailContentStr = emailContentStr + " " + String.valueOf(id);
    	}
    	
    	if (referalFlag == true) {
    		sendReferral();
    	}

    	return view;
    }
    
    private void sendReferral() {
    	Client c = ModelHelper.getClientForId(context, clientId);
    	String clientFName = c.getFirstName();
    	String clientLName = c.getLastName();
    	Household hh = ModelHelper.getHouseholdForId(context, hhId);
    	String hhName = hh.getHhName();
    	int workerId = hh.getWorkerId();
    	Worker worker = ModelHelper.getWorkerForId(context, workerId);
    	String fName = worker.getFirstName();
    	String lName = worker.getLastName();
    	Log.i("Related Info", "household name:"+hhName+",volunteer Name:"+fName+" "+lName);
    	
    	String phoneNum = Integer.toString(worker.getPhoneNumber());
    	m = new Mail("chatreferral@gmail.com", "health001"); 
    	String[] toArr = {"lmbutler.ssa@gmail.com"}; // This is an array, you can add more emails, just separate them with a coma    	
    	
    	//send sms
    	String smsMessage="Urgent health referral for - Household [Household name] by Volunteer [Volunteer Name].  See email for details or phone volunteer at: [Phone Number]";
    	smsMessage = smsMessage.replace("[Household name]", hhName);
    	smsMessage = smsMessage.replace("[Volunteer Name]", fName+" "+lName);
    	smsMessage = smsMessage.replace("[Phone Number]", phoneNum);
    	PackageManager pm = context.getPackageManager();
    	if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
    		new SendSMS().execute(phoneNum, smsMessage);
    	} else {
    		BaseActivity.toastHelper(getActivity(), "This device does not seem to be equipped with SMS capabilities. Please send a PlsCall SMS to Fikile at 0812567890 to explain the serious health condition.");
    	}
    	
    	// this is all pretty gross, but I'm assuming this section will get cut anyways
    	emailContentStr = emailContentStr + ("\n\n\n") + "First attempt at HSR display: " + ("\n\n");
    	hsrContent = ModelHelper.getAllHealthSelectContentForVisitIdAndClientId(context, visitId, clientId);
    	int i = 1;
    	for (String s : hsrContent) {
    		emailContentStr += s;
    		emailContentStr += " ";
    		if ((i % 2) == 0) {
    			emailContentStr += "\n";
    		}
    		i++;
    	}
    	
    	//send email
    	StringBuilder strBuilder = new StringBuilder();
    	String emailTitleStr = "Health referral for - Child name: [Child name] at Household: [Household name]; assessment performed by [Home Visitor Name]. Important details about Health Assessment.";
    	emailTitleStr = emailTitleStr.replace("[Household name]", hhName);
    	emailTitleStr = emailTitleStr.replace("[Child name]", clientFName+" "+clientLName);
    	emailTitleStr = emailTitleStr.replace("[Home Visitor Name]", fName+" "+lName);

    	strBuilder.append(emailTitleStr);
    	strBuilder.append("\n\n");
    	strBuilder.append(emailContentStr);
    	new SendMail().execute(toArr, "chatreferral@gmail.com", "Health referral", strBuilder.toString());
    }
    
    protected boolean sendSMSMessage(String phoneNo, String message) {
        Log.i("Send SMS", "");
        
        try {
           SmsManager smsManager = SmsManager.getDefault();
           smsManager.sendTextMessage(phoneNo, null, message, null, null);
           BaseActivity.toastHelper(getActivity(), "SMS sent");
           return true;
        } catch (Exception e) {
           BaseActivity.toastHelper(getActivity(), "SMS failed, please try again");
           String warningStr = "Unable to send SMS automatically. Please send a PlsCall SMS to Fikile at 0812567890 to explain the serious health condition";
           showAlertDialog("Send SMS failed",warningStr);
           e.printStackTrace();
           return false;
        }
     }
     
    private class SendMail extends AsyncTask<Object, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Object... params) {
			return ReferralFragment.this.sendEmail((String[])params[0],String.valueOf(params[1]),String.valueOf(params[2]),String.valueOf(params[3]));
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(!result)
			{
				BaseActivity.toastHelper(getActivity(), "Email was not sent.");
				String warningStr = "Unable to send Email automatically. Please send a PlsCall SMS to Fikile at 0812567890 to explain the serious health condition";
				showAlertDialog("Send Email failed",warningStr);
			}
			else
				BaseActivity.toastHelper(getActivity(), "Email was sent successfully.");
		}
     }
    
    private class SendSMS extends AsyncTask<Object, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Object... params) {
			return ReferralFragment.this.sendSMSMessage(String.valueOf(params[0]),String.valueOf(params[1]));
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(!result)
			{
				BaseActivity.toastHelper(getActivity(), "SMS was not sent.");
				String warningStr = "Unable to send SMS automatically.  Please send a PlsCall SMS to Fikile at 0812567890 to explain the serious health condition";
				showAlertDialog("Send SMS failed",warningStr);
			}
			else
				BaseActivity.toastHelper(getActivity(), "SMS was sent successfully.");
		}
     }
    
     public boolean sendEmail(String[] toArr, String fromArr, String subject, String message){
  		m.setTo(toArr); // load array to setTo function
  		m.setFrom(fromArr); // who is sending the email 
  		m.setSubject(subject); 
  		m.setBody(message); 
  		boolean isSuccess=false;

  		try { 
  			//m.addAttachment("/sdcard/myPicture.jpg");  // path to file you want to attach
  			if(m.send()) { 
  				// success
  				Log.i("Send Email", "send email successful");
  				isSuccess=true;
  			} else { 
  				// failure
  				Log.i("Send Email", "send email failed");
  				isSuccess=false;
  			} 
  		} catch(Exception e) { 
  			// some other problem
  			Log.i("Send Email", "send email failed becaues of other problem");
  			e.printStackTrace();
  			isSuccess=false;
  		} 
  		return isSuccess;

  	}
     
     private void showAlertDialog(String title, String message)
     {
    	 AlertDialog.Builder alert = new AlertDialog.Builder(context);

    	 alert.setTitle(title);
    	 alert.setMessage(message);

    	 alert.setPositiveButton(context.getText(R.string.action_confirm), new DialogInterface.OnClickListener() {
    	 public void onClick(DialogInterface dialog, int whichButton) {
	    	   // Do something with value!
    	   }
    	 });

    	 alert.show();
     }
}


/*
Class ReferralTrigger
int id: 1
int triggerId: 10005
String dangerSign: cough
String enContent: some stuff about ors
*/