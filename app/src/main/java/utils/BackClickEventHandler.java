package utils;

import android.app.Activity;
import android.widget.Toast;

import com.disaster.message.R;

public class BackClickEventHandler {
	private static long backKeyPressedTime = 0;
	private static Toast toast;

	public static void onBackPressed(Activity activity) {
		if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
			backKeyPressedTime = System.currentTimeMillis();
			toast = Toast.makeText(activity, activity.getString(R.string.backpressed_finish), Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {

			try {
				activity.finish();
				toast.cancel();
			} catch (Exception e) {
			}
		}
	}
}
