package dataman.dmToolkit.walkThrough.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WalkThroughApplicable_Mdl implements IsSerializable {

    String authority, linkUser;

    public WalkThroughApplicable_Mdl() {
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getLinkUser() {
        return linkUser;
    }

    public void setLinkUser(String linkUser) {
        this.linkUser = linkUser;
    }

}
