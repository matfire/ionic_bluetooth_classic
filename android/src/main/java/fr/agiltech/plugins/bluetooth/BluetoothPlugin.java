package fr.agiltech.plugins.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;

import org.json.JSONArray;

import java.nio.charset.StandardCharsets;

@RequiresApi(api = Build.VERSION_CODES.S)
@CapacitorPlugin(name = "Bluetooth", permissions = {
        @Permission(alias = "location", strings = {Manifest.permission.ACCESS_FINE_LOCATION}),
        @Permission(alias = "bluetooth", strings = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_CONNECT}),
})
public class BluetoothPlugin extends Plugin {

    private final Bluetooth implementation = new Bluetooth(this::notifyListeners);
    private final IntentFilter filter =  new IntentFilter(BluetoothDevice.ACTION_FOUND);


    @PluginMethod
    public void startListening(PluginCall call) {
        getActivity().registerReceiver(discoveryReceiver, filter);
        implementation.startListening(call);
        call.resolve();
    }

    private final BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("Device Discovery", device.toString());
                var res = new JSObject();
                var obj = new JSObject();
                obj.put("address", device.getAddress());
                obj.put("name", device.getName());
                obj.put("type", device.getType());
                res.put("device", obj);
                notifyListeners("deviceDiscovered", res);
            }
        }
    };

    @PluginMethod
    public void stopListening(PluginCall call) {
        getActivity().unregisterReceiver(discoveryReceiver);
        call.resolve();
    }

    @PluginMethod
    public void getPairedDevices(PluginCall call) {
        try {
            var devices = implementation.getPairedDevices();
            var devicesArray = new JSONArray();
            for (int i = 0; i < devices.size(); i++) {
                devicesArray.put(devices.toArray()[i]);
            }
            var res = new JSObject();
            res.put("devices", devicesArray);
            call.resolve(res);
        } catch (Exception e) {
            Log.i("Bluetooth", e.getMessage());
            call.reject("something went wrong, " + e.getMessage());
        }
    }

    @PluginMethod
    public void connect(PluginCall call) {
        String deviceId = call.getString("deviceId");
        String appUUID = call.getString("appUUID");
        implementation.connect(deviceId, appUUID);
        call.resolve();
    }

    @PluginMethod
    public void disconnect(PluginCall call) {
        String deviceId = call.getString("deviceId");
        implementation.disconnect(deviceId);
        call.resolve();
    }

    @PluginMethod()
    public void write(PluginCall call) {
        String message = call.getString("message");
        Log.i("Bluetooth write", "before checking message");
        try {
            assert message != null;
            implementation.write(message.getBytes(StandardCharsets.UTF_8));
            call.resolve(new Response(ResponseStatus.OK, "data sent successfully").toObject());
        } catch (Exception e) {
            Log.e("Bluetooth Write", "could not send data", e);
            call.reject("could not send data");
        }
    }
}