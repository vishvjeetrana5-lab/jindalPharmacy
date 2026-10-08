package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmSize implements Serializable,IsSerializable{

    private int oWidth;
    private int oHeight;

    private int cWidth;
    private int cHeight;

    public DmSize() {
    }

    public DmSize(int expWid, int expHei) {
        this.oWidth = expWid;
        this.oHeight = expHei;
    }

    public void setOriginalWidth(int wid) {
        this.oWidth = wid;
    }

    public int getOriginalWidth() {
        return this.oWidth;
    }

    public void setOriginalHeight(int hei) {
        this.oHeight = hei;
    }

    public int getOriginalHeight() {
        return this.oHeight;
    }

    public void setChangedWidth(int wid) {
        this.cWidth = wid;
    }

    public void setChangedHeight(int hei) {
        this.cHeight = hei;
    }

    public int getChangedWidth() {
        return this.cWidth;
    }

    public int getChangedHeight() {
        return this.cHeight;
    }
}
