package dataman.dmToolkit.dmChart.client;

import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmChart.shared.ItemDetail_Mdl;

public class DynamicValueProvider implements ValueProvider<ItemDetail_Mdl, Double> {

    private String field;

    public DynamicValueProvider(String field) {
        this.field = field;
    }

    @Override
    public Double getValue(ItemDetail_Mdl object) {
        return object.get(field);
    }

    @Override
    public void setValue(ItemDetail_Mdl object, Double value) {
        object.put(field, value);
    }

    @Override
    public String getPath() {
        return field;
    }

}
