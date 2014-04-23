package org.chat.android.Auth;

/**
 * User: udinic
 * Date: 3/27/13
 * Time: 2:35 AM
 */
public interface ServerAuthenticate {
    public String getAccessToken(final String email, final String password, final String deviceid);
}
