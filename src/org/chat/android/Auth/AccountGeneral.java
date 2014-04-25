package org.chat.android.Auth;

public class AccountGeneral {
	/**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "chat.org";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "chat";
    
    /**
     * The authority for the sync adapter's content provider
     */
    public static final String AUTHORITY = "org.chat.provider";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an Chat account";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an Chat account";
    
    /**
     *  Arg name
     */
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_ACCOUNT_PASSWORD = "ACCOUNT_PASSWORD";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    
    /**
     * 
     */
    public final static String ARG_INTENT_REAUTH = "IS_REAUTH";

}
