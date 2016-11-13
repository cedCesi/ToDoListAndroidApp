package android.cesi.todo.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private static final String MY_PREF = "PREF";
    private static final String TOKEN_KEY = "TOKEN";

    public static void setToken(final Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

}
