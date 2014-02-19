package org.chat.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    	Intent i = new Intent(CHAOverviewActivity.this, ImmunizationsReceivedActivity.class);
    	Bundle b = new Bundle();
    	b.putInt("visitId",visitId);
    	b.putInt("hhId",hhId);
    	b.putInt("clientId",clientId);
    	i.putExtras(b);
    	startActivity(i);   
    }
}
