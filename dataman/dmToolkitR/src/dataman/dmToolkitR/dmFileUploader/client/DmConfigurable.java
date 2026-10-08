package dataman.dmToolkitR.dmFileUploader.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.*;

/**
 * A common base class that any of the objects which support configuration
 * options will extend to allow the caller to set the options on them. Provides
 * a convenient {@link #setOption(String, Object)} method that will allow for a
 * configuration option to be set at any level.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public abstract class DmConfigurable<T> {

    private JSONObject options;

    
    public T setOption(String path, Object value) {
        if (options == null) {
            options = new JSONObject();
        }
        setOption(options, path, value);
        @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
        final T instance = (T) this;
        return instance;
    }

    /**
     * Retrieve all of the options that have been configured for this instance
     * as a JSONObject.
     *
     * @return A JSONObject representing all of the configuration options that
     * have been set on the instance (will be null if no options have been set)
     */
    public JSONObject getOptions() {
        return options;
    }

    // Internal...
    private void setOption(JSONObject rootObject, String path, Object value) {
        if (path == null) {
            return;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.length() <= 0) {
            return;
        }
        String nodeName = path;
        if (nodeName.contains("/")) {
            nodeName = nodeName.substring(0, nodeName.indexOf("/"));
            JSONValue objectAsValue = rootObject.get(nodeName);
            if (objectAsValue == null || objectAsValue.isObject() == null) {
                rootObject.put(nodeName, new JSONObject());
            }
            JSONObject object = (JSONObject) rootObject.get(nodeName);
            setOption(object, path.substring(path.indexOf("/") + 1), value);
        } else {
            rootObject.put(nodeName, convertToJSONValue(value));
        }
    }

    private JSONValue convertToJSONValue(Object value) {
        if (value == null) {
            return JSONNull.getInstance();
        } else if (value instanceof JSONValue) {
            return (JSONValue) value;
        }
        if (value instanceof Boolean) {
            return JSONBoolean.getInstance((Boolean) value);
        } else if (value instanceof Number) {
            return new JSONNumber(((Number) value).doubleValue());
        } else if (value instanceof String) {
            return new JSONString((String) value);
        } else if (value instanceof JavaScriptObject) {
            return new JSONObject((JavaScriptObject) value);
        } else if (value instanceof DmConfigurable) {
            return ((DmConfigurable) value).getOptions();
        } else if (value.getClass().isArray()) {
            JSONArray jsonArray = new JSONArray();
            Object[] valueArray = (Object[]) value;
            for (int i = 0, valueArrayLength = valueArray.length; i < valueArrayLength; i++) {
                Object arrayValue = valueArray[i];
                jsonArray.set(i, convertToJSONValue(arrayValue));
            }
            return jsonArray;
        }
        return null;
    }

}
