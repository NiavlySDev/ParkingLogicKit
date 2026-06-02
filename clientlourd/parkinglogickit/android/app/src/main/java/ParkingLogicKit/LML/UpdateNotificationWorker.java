package ParkingLogicKit.LML;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateNotificationWorker extends Worker {
    private static final String CHANNEL_ID = "plk_update_notifications";
    private static final int NOTIFICATION_ID = 23023;
    private static final String[] MANIFEST_URLS = {
            "https://raw.githubusercontent.com/NiavlySDev/ParkingLogicKit/android-update-manifest/update-manifest.json",
            "https://raw.githubusercontent.com/NiavlySDev/ParkingLogicKit/main/docs/update-manifest.json",
            "https://github.com/NiavlySDev/ParkingLogicKit/releases/latest/download/update-manifest.json"
    };

    public UpdateNotificationWorker(@NonNull Context context, @NonNull WorkerParameters parameters) {
        super(context, parameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        if (!UpdateNotificationScheduler.isEnabled(context)) {
            return Result.success();
        }

        try {
            String latestVersion = fetchLatestVersion();
            String currentVersion = getCurrentVersion(context);

            if (latestVersion == null || !isNewerVersion(latestVersion, currentVersion)) {
                return Result.success();
            }

            SharedPreferences preferences = UpdateNotificationScheduler.getPreferences(context);
            String lastNotifiedVersion = preferences.getString(
                    UpdateNotificationScheduler.LAST_NOTIFIED_VERSION_KEY,
                    ""
            );
            if (latestVersion.equals(lastNotifiedVersion)) {
                return Result.success();
            }

            showNotification(context, currentVersion, latestVersion);
            preferences.edit()
                    .putString(UpdateNotificationScheduler.LAST_NOTIFIED_VERSION_KEY, latestVersion)
                    .apply();

            return Result.success();
        } catch (Exception exception) {
            return Result.retry();
        }
    }

    private String fetchLatestVersion() throws Exception {
        Exception lastException = null;

        for (String manifestUrl : MANIFEST_URLS) {
            try {
                JSONObject manifest = new JSONObject(fetch(manifestUrl));
                JSONObject latest = manifest.optJSONObject("latest");
                if (latest != null) {
                    String version = latest.optString("version", "");
                    if (!version.trim().isEmpty()) {
                        return version;
                    }
                }

                JSONArray releases = manifest.optJSONArray("releases");
                if (releases != null && releases.length() > 0) {
                    String version = releases.getJSONObject(0).optString("version", "");
                    if (!version.trim().isEmpty()) {
                        return version;
                    }
                }
            } catch (Exception exception) {
                lastException = exception;
            }
        }

        if (lastException != null) {
            throw lastException;
        }

        return null;
    }

    private String fetch(String urlValue) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlValue).openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestProperty("Accept", "application/json");

        try {
            int statusCode = connection.getResponseCode();
            if (statusCode < 200 || statusCode >= 300) {
                throw new IllegalStateException("HTTP " + statusCode);
            }

            StringBuilder body = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            )) {
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            }
            return body.toString();
        } finally {
            connection.disconnect();
        }
    }

    private String getCurrentVersion(Context context) throws Exception {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionName != null ? packageInfo.versionName : "0.0.0";
    }

    private boolean isNewerVersion(String candidateVersion, String currentVersion) {
        int[] candidate = parseVersion(candidateVersion);
        int[] current = parseVersion(currentVersion);
        int length = Math.max(candidate.length, current.length);

        for (int index = 0; index < length; index++) {
            int candidatePart = index < candidate.length ? candidate[index] : 0;
            int currentPart = index < current.length ? current[index] : 0;

            if (candidatePart > currentPart) {
                return true;
            }

            if (candidatePart < currentPart) {
                return false;
            }
        }

        return false;
    }

    private int[] parseVersion(String version) {
        String[] parts = version.replaceFirst("^[vV]", "").split("\\.");
        int[] parsed = new int[parts.length];

        for (int index = 0; index < parts.length; index++) {
            try {
                parsed[index] = Integer.parseInt(parts[index].replaceAll("[^0-9].*$", ""));
            } catch (Exception exception) {
                parsed[index] = 0;
            }
        }

        return parsed;
    }

    private void showNotification(Context context, String currentVersion, String latestVersion) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        createNotificationChannel(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Mise à jour disponible")
                .setContentText(currentVersion + " -> " + latestVersion)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Mises à jour ParkingLogicKit",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Notifications lorsqu'une nouvelle version est disponible.");

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }
}
