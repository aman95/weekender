package codemsit.weekender;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "MyPrefs";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	
	// User name (make variable public to access from outside)
	public static final String KEY_Email = "email";
	
	// Email address (make variable public to access from outside)
	public static final String KEY_MerchantName = "mername";

	public static final String KEY_MID = "mid";

	//Url of the merchant logo
	public static final String KEY_LogoUrl = "logourl";

	//Registration ID of device
	public static final String KEY_REGEDIT = "regid";


	public static final String KEY_Sign = "signature";

	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String email, String mername){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing name in pref
		editor.putString(KEY_Email, email);
		
		// Storing email in pref
		editor.putString(KEY_MerchantName, mername);


		// commit changes
		editor.commit();
	}

	public void storeregid(String id){
		// Storing login value as TRUE

		editor.putString(KEY_REGEDIT, id);

		// commit changes
		editor.commit();
	}

	public void storesignature(String id){
		// Storing login value as TRUE

		editor.putString(KEY_Sign, id);

		// commit changes
		editor.commit();
	}
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public boolean checkLogin(){
		// Check login status
		boolean flag = false;
		if(!this.isLoggedIn()){
			flag = true;
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, CustomerLogin.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Staring Login Activity
			_context.startActivity(i);
		}
		return flag;
	}
	
	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user email
		user.put(KEY_Email, pref.getString(KEY_Email, null));
		
		// user name
		user.put(KEY_MerchantName, pref.getString(KEY_MerchantName, null));

		//user logo
		user.put(KEY_LogoUrl, pref.getString(KEY_LogoUrl, null));

		//merchant id
		user.put(KEY_MID, pref.getString(KEY_MID, null));

		//reg id
		user.put(KEY_REGEDIT, pref.getString(KEY_REGEDIT, null));


		user.put(KEY_Sign, pref.getString(KEY_Sign, null));

		// return user
		return user;
	}


	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, CustomerLogin.class);
		// Closing all the Activities



		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring Login Activity
		_context.startActivity(i);
	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
