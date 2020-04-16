package com.store;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesApp extends Application {
    private Context context;
    private String username;
    private String password;
    private String userFirstName;
    private String userLastName;
    private String artistUsername;
    private String artistFirstName;
    private String userType;
    /*
    SharedPreferences are related to context,
    without context of the activity it will not work
    */
    public SharedPreferencesApp (Context context) {
        this.context = context;
    }

    public void saveLoginData(String userFirstName, String userLastName, String username, String password, String userType){
        SharedPreferences preferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        if (userType.equals("user")){
            editor.putString("username", username);
            editor.putString("userPassword", password);
            editor.putString("userFirstName", userFirstName);
            editor.putString("userType", "user");
            editor.putString("userLastName", userLastName);
        } else {
            editor.putString("artistUsername", username);
            editor.putString("artistPassword", password);
            editor.putString("artistFirstName", userFirstName);
            editor.putString("userType", "artist");
        }
        editor.commit();
    }

    public void loadPreferences(){
        SharedPreferences preferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        userType = preferences.getString("userType", null);
        //This handle errors of type null, or app closes
        try {
            if(userType.equals("user")){
                username = preferences.getString("username",null);
                password = preferences.getString("userPassword",null);
                userFirstName = preferences.getString("userFirstName", null);
                userLastName = preferences.getString("userLastName", null);
            } else if(userType.equals("artist")){
                password = preferences.getString("artistPassword",null);
                artistUsername = preferences.getString("artistUsername", null);
                artistFirstName = preferences.getString("artistFirstName", null);
            }
        } catch (Exception e){ }
    }

    public void cleanPreferences() {
        SharedPreferences preferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getArtistUsername() {
        return artistUsername;
    }

    public String getArtistFirstName() {
        return artistFirstName;
    }

    public String getUserType() {
        return userType;
    }
}
