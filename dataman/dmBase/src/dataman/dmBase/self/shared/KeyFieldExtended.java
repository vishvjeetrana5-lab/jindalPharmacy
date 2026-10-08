package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public final class KeyFieldExtended extends KeyField implements Serializable, IsSerializable {

    public KeyFieldExtended() {

    }

    public KeyFieldExtended(String KeyFieldCode, String KeyFieldName) {
        super(KeyFieldCode, KeyFieldName);
    }
}
