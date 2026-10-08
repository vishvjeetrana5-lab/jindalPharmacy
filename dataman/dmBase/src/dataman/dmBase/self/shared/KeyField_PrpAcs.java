package dataman.dmBase.self.shared;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface KeyField_PrpAcs extends PropertyAccess<KeyFieldExtended> {

    @Editor.Path("SharedKeyFieldCode")
    ModelKeyProvider<KeyFieldExtended> code();

    @Editor.Path("SharedKeyFieldName")
    ValueProvider<KeyFieldExtended, String> name();
}
