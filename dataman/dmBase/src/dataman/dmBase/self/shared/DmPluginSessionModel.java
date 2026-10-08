package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmPluginSessionModel implements Serializable,IsSerializable{

    private String tcpSessionId;

    
    public DmPluginSessionModel() {
        this.tcpSessionId = "";
    }

    public String getTCPSessionId() {
        return this.tcpSessionId;
    }

    public void setTCPSessionId(String sessionId) {
        this.tcpSessionId = sessionId.toLowerCase().trim();
    }
}
