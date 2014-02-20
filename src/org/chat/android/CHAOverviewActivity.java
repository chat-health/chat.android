package org.chat.android;

import org.chat.android.models.Client;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CHAOverviewActivity extends BaseActivity {
	private int visitId = 0;
	private int hhId = 0;
	private int clientId = 0;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_cha_overview);
        
        Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");
		hhId = b.getInt("hhId");
		clientId = b.getInt("clientId");
    }
    
    public void openCHADeliveryAsk(View v) {
    	Intent i = new Intent(CHAOverviewActivity.this, CHADeliveryAsk.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	b.putInt("clientId",clientId);
    	i.putExtras(b);
    	startActivity(i);    	
    }
    
    public void openImmunizationsReceived(View v) {
//    	TODO: put this back in for PROD
//    	Client child = ModelHelper.getClientForId(context, clientId);
//        new AlertDialog.Builder(this)
//        .setMessage("Is " + child.getFirstName() + " " + child.getLastName() + "'s health card present?")
//        .setNegativeButton(R.string.action_no, new OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
//            	// TODO: Zulu here
//            	Toast.makeText(getApplicationContext(),"If possible please have the health card available at our next visit",Toast.LENGTH_LONG).show();
//            }
//        })
//        .setPositiveButton(R.string.action_yes, new OnClickListener() {
//            public void onClick(DialogInterface arg0, int arg1) {
            	Intent i = new Intent(CHAOverviewActivity.this, ImmunizationsReceivedActivity.class);
            	Bundle b = new Bundle();
            	b.putInt("visitId",visitId);
            	b.putInt("hhId",hhId);
            	b.putInt("clientId",clientId);
            	i.putExtras(b);
            	startActivity(i); 
//            }
//        }).create().show();
    }
}
