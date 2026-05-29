package ParkingLogicKit.LML;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.FileProvider;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@CapacitorPlugin(name = "ApkInstaller")
public class ApkInstallerPlugin extends Plugin {

    @PluginMethod
    public void installFromUrl(PluginCall call) {
        String url = call.getString("url");
        String fileName = call.getString("fileName", "ParkingLogicKit-update.apk");

        if (url == null || url.trim().isEmpty()) {
            call.reject("URL APK manquante.");
            return;
        }

        getBridge().execute(() -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        && !getContext().getPackageManager().canRequestPackageInstalls()) {
                    Intent settingsIntent = new Intent(
                            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                            Uri.parse("package:" + getContext().getPackageName())
                    );
                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(settingsIntent);
                    call.reject("Autorise l'installation depuis cette source, puis relance la mise a jour.");
                    return;
                }

                File apkFile = downloadApk(url, fileName);
                Uri apkUri = FileProvider.getUriForFile(
                        getContext(),
                        getContext().getPackageName() + ".fileprovider",
                        apkFile
                );

                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getContext().startActivity(installIntent);

                JSObject result = new JSObject();
                result.put("status", "install_intent_started");
                call.resolve(result);
            } catch (Exception exception) {
                call.reject("Installation impossible : " + exception.getMessage(), exception);
            }
        });
    }

    private File downloadApk(String urlValue, String fileName) throws Exception {
        URL url = new URL(urlValue);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(true);
        connection.connect();

        int statusCode = connection.getResponseCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IllegalStateException("telechargement refuse par GitHub (" + statusCode + ")");
        }

        File outputFile = new File(getContext().getCacheDir(), sanitizeFileName(fileName));

        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            connection.disconnect();
        }

        return outputFile;
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
