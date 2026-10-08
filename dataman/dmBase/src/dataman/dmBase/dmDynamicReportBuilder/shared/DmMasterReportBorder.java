package dataman.dmBase.dmDynamicReportBuilder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmMasterReportBorder implements Serializable, IsSerializable {

    public static final long serialVersionUID = 1L;

    public enum LineStyle implements Serializable, IsSerializable {

        SOLID((byte) 0), DASHED((byte) 1), DOTTED((byte) 2), DOUBLE((byte) 3);

        private byte value;

        public byte getValue() {
            return this.value;
        }

        private LineStyle(byte lstyle) {
            this.value = lstyle;
        }
    }

    private LineStyle lstyle = LineStyle.SOLID;

    private DmMasterReportStyle.Colors color = DmMasterReportStyle.Colors.BLACK;

    private float borWid = 0f;

    public DmMasterReportBorder() {
    }

    public LineStyle getLineStyle() {
        return lstyle;
    }

    public void setLineStyle(LineStyle lstyle) {
        this.lstyle = lstyle;
    }

    public DmMasterReportStyle.Colors getColor() {
        return color;
    }

    public void setColor(DmMasterReportStyle.Colors color) {
        this.color = color;
    }

    public float getBorderWidth() {
        return borWid;
    }

    public void setBorderWidth(float borWid) {
        this.borWid = borWid;
    }

}
