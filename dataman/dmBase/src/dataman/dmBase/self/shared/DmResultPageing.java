package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmResult;
import java.io.Serializable;

public class DmResultPageing<T> extends DmResult<T> implements Serializable, IsSerializable {

    private DmPaging pageing;

    public DmResultPageing() {
    }

    public DmPaging getPaging() {
        return pageing;
    }

    public void setPaging(DmPaging pageing) {
        this.pageing = pageing;
    }
}
