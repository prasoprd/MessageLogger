package cz.prasoprd.msglogger.ui.main;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceClass {

    public static final String PREFERENCE_NAME = "cz.prasoprd.msglogger";
    private final SharedPreferences sharedpreferences;

    public PreferenceClass(Context context) {
        sharedpreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getText() {
        if (sharedpreferences != null) {
            return sharedpreferences.getString("token","");
        } else {
            return "";
        }
    }

    public void setText(String text) {
        if (sharedpreferences != null) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("token", text);
            editor.apply();
        }
    }
}
