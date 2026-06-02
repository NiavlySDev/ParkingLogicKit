package ParkingLogicKit.LML;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public final class UpdateNotificationScheduler {
    static final String PREFS_NAME = "plk_update_notifications";
    static final String ENABLED_KEY = "enabled";
    static final String LAST_NOTIFIED_VERSION_KEY = "last_notified_version";
    private static final String WORK_NAME = "plk_update_check_worker";

    private UpdateNotificationScheduler() {}

    public static void setEnabled(Context context, boolean enabled) {
        getPreferences(context).edit().putBoolean(ENABLED_KEY, enabled).apply();

        if (enabled) {
            schedule(context);
        } else {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME);
        }
    }

    public static boolean isEnabled(Context context) {
        return getPreferences(context).getBoolean(ENABLED_KEY, false);
    }

    public static void scheduleIfEnabled(Context context) {
        if (isEnabled(context)) {
            schedule(context);
        }
    }

    static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static void schedule(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                UpdateNotificationWorker.class,
                6,
                TimeUnit.HOURS
        )
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
        );
    }
}
