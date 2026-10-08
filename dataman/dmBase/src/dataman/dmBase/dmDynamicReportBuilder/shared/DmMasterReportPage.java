package dataman.dmBase.dmDynamicReportBuilder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmMasterReportPage implements Serializable, IsSerializable {

    private int height, width;
    private boolean orientationPortrait = true;

    public DmMasterReportPage() {

    }

    public DmMasterReportPage(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public DmMasterReportPage(int height, int width, boolean portrait) {
        this.height = height;
        this.width = width;
        this.orientationPortrait = portrait;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isOrientationPortrait() {
        return orientationPortrait;
    }

    public void setOrientationPortrait(boolean orientationPortrait) {
        this.orientationPortrait = orientationPortrait;
    }

    public static DmMasterReportPage Page_A4_Portrait() {
        return new DmMasterReportPage(842, 595, true);
    }

    public static DmMasterReportPage Page_A4_Landscape() {
        return new DmMasterReportPage(595, 842, false);
    }

    public static DmMasterReportPage Page_Legal_Portrait() {
        return new DmMasterReportPage(1008, 612, true);
    }

    public static DmMasterReportPage Page_Legal_Landscape() {
        return new DmMasterReportPage(612, 1008, false);
    }

    public static DmMasterReportPage Page_Letter_Portrait() {
        return new DmMasterReportPage(792, 612, true);
    }

    public static DmMasterReportPage Page_Letter_Landscape() {
        return new DmMasterReportPage(612, 792, false);
    }

}
