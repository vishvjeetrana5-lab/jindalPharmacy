package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmBaseURL implements Serializable,IsSerializable{
    String url,protocol;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}   
