package dataman.dmToolkit.dmChart.shared;

import java.util.HashMap;
import java.util.Map;

public class ItemDetail_Mdl {

    private Map<String, Double> data = new HashMap<String, Double>();
    private String name;

    public Double get(Object key) {
        return data.get(key);
    }

    public Double put(String key, Double value) {
        return data.put(key, value);
    }

    public Map<String, Double> getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
