package jacek.brzezinski.piktogramy;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.BatteryManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BatteryReceiver extends BroadcastReceiver {
    private int status;
    private float percent = 100;
    private static final String TAG = "BatteryReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        TextView batteryCharging = ((MainActivity) context).findViewById(R.id.action_battery_charging);
        TextView batteryPercent = ((MainActivity) context).findViewById(R.id.action_battery_percent);

        if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            percent = level * 100 / scale;
            if (batteryCharging != null) {
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        batteryCharging.setVisibility(View.VISIBLE);
                        break;
                    default:
                        batteryCharging.setVisibility(View.INVISIBLE);
//                case BatteryManager.BATTERY_STATUS_FULL:
//                    break;
//
//                case BatteryManager.BATTERY_STATUS_DISCHARGING:
//                    break;
//                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
//                    break;
                }
                batteryPercent.setText("Bat. " + Math.round(percent) + "%");
            }
        }
    }

    public int getStatus() {
        return status;
    }

    public float getPercent() {
        return percent;
    }
}