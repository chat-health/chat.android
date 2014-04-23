package org.chat.android.Auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
	// Instance field that stores the authenticator object
    private Authenticator mAuthenticator;
    
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mAuthenticator.getIBinder();
	}
}
