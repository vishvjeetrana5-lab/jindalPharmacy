package aarogya.pharmacy.stockSummary.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Navigation_Mdl implements IsSerializable, Serializable {

    private String heading;
    private String particularCode;
    private String particularName;

    public Navigation_Mdl() {
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getParticularCode() {
        return particularCode;
    }

    public void setParticularCode(String particularCode) {
        this.particularCode = particularCode;
    }

    public String getParticularName() {
        return particularName;
    }

    public void setParticularName(String particularName) {
        this.particularName = particularName;
    }

}
