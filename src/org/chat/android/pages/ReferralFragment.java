package org.chat.android.pages;

import java.util.List;
import java.util.Locale;

import org.chat.android.BaseActivity;
import org.chat.android.Mail;
import org.chat.android.ModelHelper;
import org.chat.android.R;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.Household;
import org.chat.android.models.Worker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ReferralFragment extends Fragment {
	private Mail m;
	private Context context;
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
    	
    	int visitId = getArguments().getInt("visitId");
    	int clientId = getArguments().getInt("clientId");
    	int hhId = getArguments().getInt("hhId");
    	
    	Household hh = ModelHelper.getHouseholdForId(context, hhId);
    	String hhName = hh.getHhName();
    	int workerId = hh.getWorkerId();
    	Worker worker = ModelHelper.getWorkerForId(context, workerId);
    	String fName = worker.getFirstName();
    	String lName = worker.getLastName();
    	Log.i("Related Info", "household name:"+hhName+",Volunteer Name:"+fName+" "+lName);
    	
    	// int phoneNum = worker.getPhoneNumber();
    	String phoneNum = "5556";
    	m = new Mail("chatreferral@gmail.com", "health001"); 
    	String[] toArr = {"victor.chen@mail.utoronto.ca"}; // This is an array, you can add more emails, just separate them with a coma
    	
    	//send sms
    	String smsMessage="Urgent health referral for Household [Household name] by Volunteer [Volunteer Name].  See email for details or phone volunteer at: [Phone Number]";
    	smsMessage.replace("[Household name]", hhName);
    	smsMessage.replace("[Volunteer Name]", fName+", "+lName);
    	smsMessage.replace("[Phone Number]", phoneNum);
//    	this.sendSMSMessage(phoneNum, smsMessage);
    	
    	//send email
    	StringBuilder strBuilder = new StringBuilder();
    	String templateStr = "Health referral for Household [Household name] Child name [Child name] by Home visitors [Home Visitor Name].  Important Details about Health Assessment";
    	templateStr = templateStr.replace("[Household name]", hhName);
    	templateStr = templateStr.replace("[Home Visitor Name]", fName+", "+lName);
//    	templateStr.replace("[Child name]", childName);
    	strBuilder.append(templateStr);
    	
    	new SendMail().execute(toArr, "chatreferral@gmail.com", "Health referral", strBuilder.toString());
    	
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
    
    protected void sendSMSMessage(String phoneNo, String message) {
        Log.i("Send SMS", "");
        
        try {
           SmsManager smsManager = SmsManager.getDefault();
           smsManager.sendTextMessage(phoneNo, null, message, null, null);
           Toast.makeText(context.getApplicationContext(), "SMS sent.",
           Toast.LENGTH_LONG).show();
        } catch (Exception e) {
           Toast.makeText(context.getApplicationContext(),
           "SMS faild, please try again.",
           Toast.LENGTH_LONG).show();
           String warningStr = "Unable to send SMS automatically.  Please send a PlsCall SMS to Fikile at 0812567890 to explain the serious health condition";
           showAlertDialog("Send SMS failed",warningStr);
           e.printStackTrace();
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
				String warningStr = "Unable to send Email automatically.  Please send a PlsCall SMS to Fikile at 0812567890 to explain the serious health condition";
				showAlertDialog("Send Email failed",warningStr);
			}
			else
				BaseActivity.toastHelper(getActivity(), "Email was sent successfully.");
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