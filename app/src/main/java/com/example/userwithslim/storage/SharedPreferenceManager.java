package com.example.userwithslim.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.userwithslim.domain.User;

/**
 *  A singleton class for storing information and managing session
 */
public class SharedPreferenceManager
{
    private static SharedPreferenceManager mInstance;
    public static final String SHARED_PREF_NAME="my_shared_pref";

    private Context mContext;

    private SharedPreferenceManager(Context mContext)
    {
        this.mContext=mContext;
    }

    public static synchronized SharedPreferenceManager getInstance(Context mContext)
    {
        if (mInstance==null)
        {
            mInstance=new SharedPreferenceManager(mContext);
        }
        return mInstance;
    }

    /**
     * saving user into shared preferences
     * @param user
     */
    public void saveUser(User user)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("id",user.getId());
        editor.putString("email",user.getEmail());
        editor.putString("name",user.getName());
        editor.putString("school",user.getSchool());

        editor.apply();
    }

    /**
     * This method is used for maintaining the session
     * @return true if logged in
     */
    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("id",-1)!=-1)
        {
            return  true;
        }
        return false;
    }

    /**
     * this method will retrieve the value of user from shared preferences
     * @return user
     */
    public User getUser()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        User user=new User(
                sharedPreferences.getInt("id",-1),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("school",null)
        );

        return user;
    }

    /**
     *  this method will act as a log out function
     */
    public void clear()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear().apply();
    }
}
