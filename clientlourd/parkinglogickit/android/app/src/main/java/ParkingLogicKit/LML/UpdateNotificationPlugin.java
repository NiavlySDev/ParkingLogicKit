package ParkingLogicKit.LML;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "UpdateNotification")
public class UpdateNotificationPlugin extends Plugin {
    @PluginMethod
    public void setEnabled(PluginCall call) {
        boolean enabled = Boolean.TRUE.equals(call.getBoolean("enabled", false));
        UpdateNotificationScheduler.setEnabled(getContext(), enabled);

        JSObject result = new JSObject();
        result.put("enabled", enabled);
        call.resolve(result);
    }
}
