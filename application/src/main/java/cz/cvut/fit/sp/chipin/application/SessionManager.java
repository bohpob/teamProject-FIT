package cz.cvut.fit.sp.chipin.application;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences session;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGGED_IN = "isLoggedIn";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String IS_ENABLED = "isEnabled";

    public SessionManager(Context ctx) {
        context = ctx;
        session = context.getSharedPreferences("loginSession", Context.MODE_PRIVATE);
        editor = session.edit();
    }

    public void createSession(String id, String name, String email, boolean isEnabled) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(ID, id);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putBoolean(IS_ENABLED, isEnabled);

        editor.commit();
    }

    public String getId() {
        return session.getString(ID, null);
    }

    public String getName() {
        return session.getString(NAME, null);
    }

    public String getEmail() {
        return session.getString(EMAIL, null);
    }

    public boolean IsEnabled() {
        return session.getBoolean(IS_ENABLED, false);
    }

    public boolean isLoggedIn() {
        return session.getBoolean(IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

}
