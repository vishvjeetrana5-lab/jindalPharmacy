package dataman.dmToolkit.dmDisplayStrip.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmComprator;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.TreeMap;

public class DmDisplayStrip extends DmDisplayStrip_Dsg implements IsWidget {

    private CallBack callBack;
    private final TreeMap<String, String> tmDisplayData;

    public interface CallBack {

        public TreeMap<String, String> getDisplayValue(TreeMap<String, String> displayStrip);

        public String getTitleBanner();

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public DmDisplayStrip(int hieght, boolean isBannerVisible, TreeMap<String, String> tmDisplayData) {
        super(hieght, isBannerVisible);
        this.tmDisplayData = tmDisplayData;
    }

    @Override
    public Widget asWidget() {
        return flwWgt;
    }

    private void refreshBannerValue() {
        TreeMap<String, String> tempDisplatData = new TreeMap<String, String>(DmComprator.CASE_INSENSITIVE_ORDER);
        if (tmDisplayData != null
                && tmDisplayData.size() > 0
                && callBack != null) {
            tempDisplatData.putAll(callBack.getDisplayValue(tmDisplayData));
        }
        for (String key : tmDisplayData.keySet()) {
            if (tempDisplatData.containsKey(key)) {
                tmDisplayData.put(key, tempDisplatData.get(key));
            } else {
                tmDisplayData.put(key, null);
            }
        }
    }

    public void refeshBanner() {
        refreshBannerValue();
        displayBanner();
    }

    private void displayBanner() {
        FlowPanel flwTd;
        DmLabel lbl;
        flwBanner.clear();
        if (callBack != null) {
            String title = DmShared.xNull(callBack.getTitleBanner());
            if (!DmShared.xNull(title).trim().equalsIgnoreCase("")) {
                flwTd = new FlowPanel();
                flwTd.getElement().setAttribute("style", "float: left; padding: 3px 7px; font-weight: bold; margin-right:10px;");
                lbl = new DmLabel(DmShared.xNull(title));
                lbl.setStyleName("dm-HLabel");
                lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
                lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
                lbl.getElement().getStyle().setFontSize(14, Style.Unit.PX);
                lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
                flwTd.add(lbl);
                flwBanner.add(flwTd);
            }
            if (tmDisplayData != null && tmDisplayData.size() > 0) {
                for (String key : tmDisplayData.keySet()) {
                    if (!DmShared.xNull(tmDisplayData.get(key)).trim().equalsIgnoreCase("")) {
                        flwTd = new FlowPanel();
                        flwTd.getElement().setAttribute("style", "float: right; padding: 3px 7px; font-weight: bold; margin-left:10px;");
                        lbl = new DmLabel(DmShared.xNull(tmDisplayData.get(key)));
                        lbl.setStyleName("dm-HLabel");
                        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
                        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
                        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
                        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
                        flwTd.add(lbl);
                        flwBanner.add(flwTd);
                    }
                }
            }
        }
    }
}
