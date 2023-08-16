package fr.agiltech.plugins.bluetooth;

import com.getcapacitor.JSObject;



public class BluetoothStatusChange {
    String _address;
    BluetoothStatus _status;

    BluetoothStatusChange(String address, BluetoothStatus status) {
        _address = address;
        _status = status;
    }

    JSObject toObject() {
        JSObject res = new JSObject();
        JSObject data = new JSObject();
        data.put("address", _address);
        data.put("status", _status == BluetoothStatus.CONNECTED ? 1 : (_status == BluetoothStatus.CONNECTING ? 2 : 0));
        res.put("data", data);
        return res;
    }
}
