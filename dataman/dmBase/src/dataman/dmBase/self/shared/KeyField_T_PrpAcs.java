package dataman.dmBase.self.shared;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface KeyField_T_PrpAcs<T> extends PropertyAccess<T> {

    @Editor.Path("SharedKeyFieldCode")
    ModelKeyProvider<T> code();

    @Editor.Path("SharedKeyFieldName")
    ValueProvider<T, String> name();
}
