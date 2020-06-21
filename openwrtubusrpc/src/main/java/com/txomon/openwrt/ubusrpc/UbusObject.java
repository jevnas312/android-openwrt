package com.txomon.openwrt.ubusrpc;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UbusObject {
    private final static String TAG = "UbusObject";
    public String object;
    public Map<String, Map<String, Class>> spec;

    public UbusObject(String object, Map<String, Map<String, String>> objectSpec) {
        this.object = object;
        spec = new HashMap<String, Map<String, Class>>();
        for (Map.Entry<String, Map<String, String>> methodEntry : objectSpec.entrySet()) {
            String specMethod = methodEntry.getKey();
            Map<String, String> specArgs = methodEntry.getValue();
            Map<String, Class> arguments = new HashMap<String, Class>();

            for (Map.Entry<String, String> argumentEntry : specArgs.entrySet()) {
                String specArg = argumentEntry.getKey();
                Class type;
                String value = argumentEntry.getValue();
                if ("boolean".equals(value)) {
                    type = Boolean.class;
                } else if ("string".equals(value)) {
                    type = String.class;
                } else if ("number".equals(value)) {
                    type = Number.class;
                } else if ("array".equals(value)) {
                    type = List.class;
                } else if ("object".equals(value)) {
                    type = Map.class;
                } else {
                    Log.wtf(TAG, "Error in initialization, arg " + specArg +
                            " has unknown type " + argumentEntry.getValue());
                    continue;
                }
                arguments.put(specArg, type);
            }
            spec.put(specMethod, arguments);
        }
    }

    public static UbusObject fromSpec(String object, Map<String, Map<String, String>> objectSpec) {
        return new UbusObject(object, objectSpec);
    }

    public static Map<String, UbusObject> fromList(Map<String, Map<String, Map<String, String>>> list) {
        Map<String, UbusObject> objects = new HashMap<String, UbusObject>();
        for (Map.Entry<String, Map<String, Map<String, String>>> entry : list.entrySet()) {
            objects.put(entry.getKey(), UbusObject.fromSpec(entry.getKey(), entry.getValue()));
        }
        return objects;
    }

}
