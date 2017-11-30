package heinhtetoo.yuelibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import heinhtetoo.yuelibrary.R;

/**
 * Created by myolwin00 on 10/4/17.
 */

public class PrefUtils {

    public static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences(context.getString(R.string.user_pref_file_key),
                Context.MODE_PRIVATE);
    }
}
