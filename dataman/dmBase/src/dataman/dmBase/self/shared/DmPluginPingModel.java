package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmPluginPingModel implements Serializable,IsSerializable{

    private int justForPing = 1000;
    private int statusOfPing = 0;

    public DmPluginPingModel() {
    }

    public int getPingRequest() {
        return justForPing;
    }

    public int getStatusOfPing() {
        return statusOfPing;
    }

    public void setStatusOfPing(int statusOfPing) {
        this.statusOfPing = statusOfPing;
    }
}
