package fr.agiltech.plugins.bluetooth;

import com.getcapacitor.JSObject;

public class Response {
    ResponseStatus _status;
    String _message;

    Response(ResponseStatus status, String message) {
        _status = status;
        _message = message;
    }

    public JSObject toObject() {
        JSObject res = new JSObject();
        res.put("status", _status.toString());
        res.put("message", _message);
        return res;
    }
}
