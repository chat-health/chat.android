package org.chat.android.Auth;

import org.chat.android.R;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class Authenticator extends AbstractAccountAuthenticator {

    private final Context mContext;
    private String TAG = "Authenticator";
    private ServerAuthenticate sServerAuthenticate;
    
	// Simple constructor
    public Authenticator(Context context) {
        super(context);
        this.mContext = context;
        sServerAuthenticate = new ChatAuthServerAuthenticate(context.getString(R.string.base_url));
    }

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
		Log.d(TAG, "> addAccount");

        final Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(AccountGeneral.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountGeneral.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AccountGeneral.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		Log.d(TAG, "> getAuthToken");
		// Extract the google account from the Account Manager, and ask
	    // the server for an appropriate AuthToken.
	    final AccountManager am = AccountManager.get(mContext);
	 
	    String authToken = am.peekAuthToken(account, authTokenType);
	    Log.e("clientToken", "> " + authToken);
	    // Lets give another try to authenticate the user
	    if (TextUtils.isEmpty(authToken)) {
	        final String password = am.getPassword(account);
	        // got the device id
			String deviceid = android.os.Build.SERIAL;
	        if (password != null) {
	            authToken = sServerAuthenticate.getAccessToken(account.name, password,deviceid);
	        }
	    }
	 
	    // If we get an authToken - we return it
	    if (!TextUtils.isEmpty(authToken)) {
	        final Bundle result = new Bundle();
	        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
	        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
	        result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
	        return result;
	    }
	    
	    // If we get here, then we couldn't access the user's google-access-token 
	    // so we need to re-prompt them for their credentials. We do that by creating
	    // an intent to display our MainActivity screen.
	    final Intent intent = new Intent(mContext, MainActivity.class);
	    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
	    intent.putExtra(AccountGeneral.ARG_ACCOUNT_TYPE, account.type);
	    intent.putExtra(AccountGeneral.ARG_AUTH_TYPE, authTokenType);
	    final Bundle bundle = new Bundle();
	    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
	    return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}


}
