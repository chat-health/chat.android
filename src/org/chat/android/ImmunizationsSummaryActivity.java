package org.chat.android;

import java.util.List;

import org.chat.android.models.Client;
import org.chat.android.models.Household;
import org.chat.android.models.Vaccine;
import org.chat.android.models.VaccineRecorded;
import org.chat.android.models.Worker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ImmunizationsSummaryActivity extends BaseActivity {
	private int visitId = 0;
	private int hhId = 0;
	private int clientId = 0;
	private Client client = null;
	private Mail m;
	String emailContentStr = "";
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_immunization_summary);
        
        Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		clientId = b.getInt("clientId");
		client = ModelHelper.getClientForId(context, clientId);
		String missingVaccines = getMissingVaccines(client);
		
		TextView tv = (TextView) findViewById(R.id.missing_vaccines_box);
		// String msg = getResources().getString(getResources().getIdentifier("immunization_missing_vaccines_text", "string", getPackageName()));
		tv.setText("Refer " + client.getFirstName() + " " + client.getLastName() + " for the required immunizations:" + missingVaccines);
    }
    
    private String getMissingVaccines(Client client) {
    	Boolean referralFlag = false;
    	String mv = "";
    	List<Vaccine> vList = ModelHelper.getVaccinesForAge(context, client.getAge());
    	
    	for (Vaccine vaccine : vList) {
    		// will return null if it doesn't exist
    		VaccineRecorded vr = ModelHelper.getVaccineRecordedForClientIdAndVaccineId(context, client.getId(), vaccine.getId());
    		if (vr == null) {
    			mv += "\n- ";
    			mv += vaccine.getShortName();
    			referralFlag = true;
    		}
    	}
    	
    	if (referralFlag == true) {
    		sendReferral(mv);
    	}
    	
    	return mv;
    }
    
    public void completeImmunizationSummary(View v) {
    	// CHAAccessed object is updated as completed (ie end_time is added) in ImmunizationReceivedActivity
    	String msg = getResources().getString(getResources().getIdentifier("immunization_completed_text", "string", getPackageName()));
    	BaseActivity.toastHelper(this,msg +" " + client.getFirstName() + " " + client.getLastName() );
    	finish();
    }
    
    
    // semi-duplicated from ReferralFragment. TODO - abstract me and set logical params
    private void sendReferral(String mv) {
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
    	
    	// currently have the phone_numbers saved as ints, may be better to change to string (would prevent the 0 at the beginning of the number from getting dropped)
    	String workerPhoneNum = "0" + Integer.toString(worker.getPhoneNumber());
    	m = new Mail("chatreferral@gmail.com", "health001"); 
    	String[] toArr = {"lmbutler.ssa@gmail.com"}; // This is an array, you can add more emails, just separate them with a coma    	
    	
    	//send sms
    	String nursePhoneNum = "0721103157";					// this is Jim's number as a placeholder
    	String smsMessage = "Vaccination referral for - Household [Household name] by Volunteer [Volunteer Name].  See email for details or phone volunteer at: [Phone Number]";
    	smsMessage = smsMessage.replace("[Household name]", hhName);
    	smsMessage = smsMessage.replace("[Volunteer Name]", fName+" "+lName);
    	smsMessage = smsMessage.replace("[Phone Number]", workerPhoneNum);
    	
    	PackageManager pm = context.getPackageManager();
    	if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
    		new SendSMS().execute(nursePhoneNum, smsMessage);
    	} else {
    		BaseActivity.toastHelper(this, "This device does not seem to be equipped with SMS capabilities. Please send a PlsCall SMS to Fikile at 0812567890 to explain the serious health condition.");
    	}
    	
    	emailContentStr = emailContentStr + ("\n") + "Missing vaccinations: " + mv;
    	
    	//send email
    	StringBuilder strBuilder = new StringBuilder();
    	String emailTitleStr = "Vaccination referral for - Child name: [Child name] at Household: [Household name]; assessment performed by [Home Visitor Name]. Child is missing vaccinations";
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
           Log.i("Send SMS", "SMS sent");
           return true;
        } catch (Exception e) {
        	Log.i("Send SMS", "SMS failed, please try again");
        	e.printStackTrace();
        	return false;
        }
     }
     
    private class SendMail extends AsyncTask<Object, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Object... params) {
			return ImmunizationsSummaryActivity.this.sendEmail((String[])params[0],String.valueOf(params[1]),String.valueOf(params[2]),String.valueOf(params[3]));
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(!result)
			{
				showToast("Email was not sent.");
				String warningStr = "Unable to send Email automatically. Please send a PlsCall SMS to Fikile at 0812567890 to explain the serious health condition";
				showAlertDialog("Send Email failed",warningStr);
			}
			else
				showToast("Email was sent successfully.");
		}
     }
    
    private class SendSMS extends AsyncTask<Object, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Object... params) {
			return ImmunizationsSummaryActivity.this.sendSMSMessage(String.valueOf(params[0]),String.valueOf(params[1]));
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(!result)
			{
				showToast("SMS was not sent.");
				String warningStr = "Unable to send SMS automatically.  Please send a PlsCall SMS to Fikile at 0812567890 to explain the serious health condition";
				showAlertDialog("Send SMS failed",warningStr);
			}
			else
				showToast("SMS was sent successfully.");
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
 
     private void showToast(String s) {
    	 BaseActivity.toastHelper(this, s);
     }
}
