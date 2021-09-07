package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.disaster.message.DMApplication;

import java.util.Random;

public class Utils {

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    public static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static Boolean isNetworkConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) DMApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
        }
        return false;
    }

    public static String getDisplayText(Double count) {
        if (count > 1000) {
            return String.format("%.1f", count / 1000) + "K";
        } else {
            return count.intValue() + "";
        }
    }
}
