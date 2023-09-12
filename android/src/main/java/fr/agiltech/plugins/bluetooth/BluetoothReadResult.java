package fr.agiltech.plugins.bluetooth;

import androidx.annotation.NonNull;
import com.getcapacitor.JSObject;
import java.util.Arrays;
import java.util.Locale;

public class BluetoothReadResult {
    public int size;
    public byte[] bytes;

    public BluetoothReadResult(int size, byte[] bytes) {
        this.size = size;
        this.bytes = bytes;
    }

    @NonNull
    public String toString() {
        return String.format(Locale.ENGLISH,"Size: %d, data: %s", size, Arrays.toString(bytes));
    }
    public JSObject toObject() {
        JSObject res = new JSObject();
        JSObject data = new JSObject();
        data.put("size", size);
        data.put("bytes", Arrays.toString(bytes));
        res.put("data", data);
        return res;
    }
}
