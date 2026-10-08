package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmPageSize implements Serializable,IsSerializable{

    private int widthInMM = 0;
    private int heightInMM = 0;
    private String pageName = "";

    public DmPageSize() {
    }

    public DmPageSize(String pageNam, int widInMM, int heiInMM) {
        this.pageName = pageNam.trim();
        this.widthInMM = widInMM;
        this.heightInMM = heiInMM;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public int getWidthInMM() {
        return widthInMM;
    }

    public String getPageId() {
        return this.pageName.trim().toLowerCase();
    }

    public String getDisplayName() {
        return this.pageName.trim().toUpperCase().replace("_", " ");
    }

    public void setWidthInMM(int widthInMM) {
        this.widthInMM = widthInMM;
    }

    public int getHeightInMM() {
        return heightInMM;
    }

    public void setHeightInMM(int heightInMM) {
        this.heightInMM = heightInMM;
    }

}
