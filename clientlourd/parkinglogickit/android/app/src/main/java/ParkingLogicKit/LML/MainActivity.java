package ParkingLogicKit.LML;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(ApkInstallerPlugin.class);
        registerPlugin(UpdateNotificationPlugin.class);
        super.onCreate(savedInstanceState);
        UpdateNotificationScheduler.scheduleIfEnabled(this);
    }
}
