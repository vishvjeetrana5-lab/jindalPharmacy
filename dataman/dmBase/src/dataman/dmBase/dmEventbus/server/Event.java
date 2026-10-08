package dataman.dmBase.dmEventbus.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Event {

    private final String type;

    private final Map<String, Object> properties = new HashMap<>();

    private boolean locked;

    public Event(String type) {
        this.type = Objects.requireNonNull(type);
    }

    public Event(String type, Map<String, Object> properties) {
        this.type = Objects.requireNonNull(type);;
        this.properties.putAll(Objects.requireNonNull(properties));
    }

    public String getType() {
        return type;
    }

    protected void lock() {
        this.locked = true;
    }

    private void isLocked() {
        if (locked) {
            throw new UnsupportedOperationException("You can not call any setters on "
                + getClass().getSimpleName() + " instance after event was published.");
        }
    }

    public boolean hasProperties() {
        return properties.isEmpty();
    }

    public Collection<String> getKeys() {
        return properties.keySet();
    }

    public void set(String key, Object value) throws UnsupportedOperationException {
        isLocked();
        properties.put(key, value);
    }

    public <T> Optional<T> getValue(String key, Class<T> cls) {
        return Optional.ofNullable(get(key, cls));
    }

    public <T> T get(String key, Class<T> cls) {
        Object val = properties.get(key);
        if (val == null) {
            return null;
        }

        if (cls.isInstance(val)) {
            return cls.cast(val);
        }
        return null;
    }

    public <T> T getOr(String key, Class<T> cls, T defaultValue) {
        T val = get(key, cls);
        return val == null ? defaultValue : val;
    }

    public String getString(String key) {
        Object val = properties.get(key);

        return val == null ? null : val.toString();
    }

    public String getStringOr(String key, String defaultValue) {
        String str = getString(key);
        return str == null ? defaultValue : str;
    }
}
