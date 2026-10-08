package aarogya.selfToolkit.investigationAdvise.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdFavorite_Mdl extends KeyField implements Serializable, IsSerializable {

    private String categoryName;
    private boolean pkg;
    private String image;

    public GrdFavorite_Mdl() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isPkg() {
        return pkg;
    }

    public void setPkg(boolean pkg) {
        this.pkg = pkg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
