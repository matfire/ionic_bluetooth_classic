package fr.agiltech.plugins.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

public class Bluetooth {
    private final BiConsumer<String, JSObject> _notifier;
    private BluetoothAdapter _ba;
    private BluetoothSocket _socket;
    private BluetoothDevice _device;
    public ConnectedThread _thread;

    public class ConnectedThread extends Thread {
        private InputStream inputStream;
        private OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket) {
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                Log.e("Bluetooth Connect", "Error when creating input stream", e);
                return;
            }
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("Bluetooth Connect", "Error when creating output stream", e);
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            int numBytes;
            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    numBytes = inputStream.read(buffer);
                    _notifier.accept("deviceDataReceived", new BluetoothReadResult(numBytes, buffer).toObject());
                } catch (IOException e) {
                    Log.e("Bluetooth Read", "Input stream was disconnected", e);
                    _notifier.accept("deviceConnectionStatusChanged", new BluetoothStatusChange(_device.getAddress(), BluetoothStatus.DISCONNECTED).toObject());
                    break;
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void write(byte[] bytes) {
            Log.i("Bluetooth Write Info", bytes.toString());
            try {
                outputStream.write(bytes);
            } catch(IOException e) {
                Log.e("Bluetooth Write", "Error occurred when sending data", e);
                _notifier.accept("deviceConnectionStatusChanged", new BluetoothStatusChange(_device.getAddress(), BluetoothStatus.DISCONNECTED).toObject());
            }
        }
        public void cancel() {
            try {
                _socket.close();
            } catch (IOException e) {
                Log.e("Bluetooth Disconnect", "Could not close connection socket", e);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    public void connect(String address, String appUUID) {
        _ba.cancelDiscovery();
        try {
                _device = _ba.getRemoteDevice(address);
                _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(appUUID));
                _socket.connect();
                _notifier.accept("deviceConnectionStatusChanged", new BluetoothStatusChange(_device.getAddress(), BluetoothStatus.CONNECTING).toObject());
                _thread = new ConnectedThread(_socket);

        } catch (Exception e) {
            Log.e("Bluetooth Connection", "failed to connect to device", e);
            _notifier.accept("deviceConnectionStatusChanged", new BluetoothStatusChange(_device.getAddress(), BluetoothStatus.DISCONNECTED).toObject());
            return;
        }
        Log.i("Bluetooth Connection", "finished connecting");
        _notifier.accept("deviceConnectionStatusChanged", new BluetoothStatusChange(_device.getAddress(), BluetoothStatus.CONNECTED).toObject());
        _thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void write(byte[] message) {
        _thread.write(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void disconnect(String address) {
        if (Objects.equals(address, _device.getAddress())) {
            try {
                _socket.close();
                _thread.cancel();
                _notifier.accept("deviceConnectionStatusChanged", new BluetoothStatusChange(_device.getAddress(), BluetoothStatus.DISCONNECTED).toObject());
                _device = null;

            } catch (IOException e) {
                Log.e("Bluetooth Disconnection", "failed to disconnect from device", e);
            }
        }

    }

    Bluetooth(BiConsumer<String, JSObject> notifier) {
        _ba = BluetoothAdapter.getDefaultAdapter();
        _notifier = notifier;
    }

    @SuppressLint("MissingPermission")
    public void startListening(PluginCall call) {
        _ba.startDiscovery();
        call.resolve();
    }

    @SuppressLint("MissingPermission")
    public Set<BluetoothDevice> getPairedDevices() {
        return _ba.getBondedDevices();
    }
}
