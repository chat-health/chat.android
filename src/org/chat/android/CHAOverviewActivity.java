package org.chat.android;

import java.sql.SQLException;
import java.util.Date;

import org.chat.android.models.CHAAccessed;
import org.chat.android.models.Client;
import org.chat.android.models.Visit;

import com.j256.ormlite.dao.Dao;

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
    	setupCHAAccessedObject("health");
    	
    	Intent i = new Intent(CHAOverviewActivity.this, CHADelivery.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	b.putInt("clientId",clientId);
    	i.putExtras(b);
    	startActivity(i);    	
    }
    
    public void openImmunizationsReceived(View v) {
//    	TODO: put this back in for PROD
    	Client child = ModelHelper.getClientForId(getHelper(), clientId);
        new AlertDialog.Builder(this)
        .setMessage("Is " + child.getFirstName() + " " + child.getLastName() + "'s health card present?")
        .setNegativeButton(R.string.action_no, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	alertUserToHealthCard();
            }
        })
        .setPositiveButton(R.string.action_yes, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
    			setupCHAAccessedObject("immunization");
            	
    			Intent i = new Intent(CHAOverviewActivity.this, ImmunizationsReceivedActivity.class);
            	Bundle b = new Bundle();
            	b.putInt("visitId",visitId);
            	b.putInt("hhId",hhId);
            	b.putInt("clientId",clientId);
            	i.putExtras(b);
            	startActivity(i); 
            }
        }).create().show();
    }
    
    private void setupCHAAccessedObject(String type) {
		Date startTime = new Date();

    	CHAAccessed chaAccessed = new CHAAccessed(clientId, visitId, type, startTime, getHelper());
    	
    	try {
    		Dao<CHAAccessed, Integer> chaaDao = getHelper().getCHAAccessedDao();
    		chaaDao.create(chaAccessed);
    		// FIXME: Colin Where does the visit object come from and why do we use the ID
    		visitId = visit.getId();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} 
    }
    
    private void alertUserToHealthCard() {
    	// TODO: Zulu here
    	BaseActivity.toastHelper(this,"If possible please have the health card available at our next visit");
    }
}
