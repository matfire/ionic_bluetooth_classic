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
        res.put("size", size);
        res.put("bytes", Arrays.toString(bytes));
        return res;
    }
}
