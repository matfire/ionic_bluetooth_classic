package fr.agiltech.plugins.bluetooth;

import android.util.Log;

import androidx.annotation.NonNull;
import com.getcapacitor.JSObject;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

public class BluetoothReadResult {
    public int size;
    public byte[] bytes;

    public BluetoothReadResult(int size, byte[] bytes) {
        this.size = size;
        this.bytes = Arrays.copyOf(bytes, size);
        Log.i("read data serialization", this.toString());
    }

    @NonNull
    public String toString() {
        return String.format(Locale.ENGLISH,"Size: %d, data: %s", size, new String(bytes, StandardCharsets.UTF_8));
    }
    public JSObject toObject() {
        JSObject res = new JSObject();
        JSObject data = new JSObject();
        data.put("size", size);
        data.put("bytes", new String(bytes, StandardCharsets.UTF_8));
        res.put("data", data);
        return res;
    }
}
