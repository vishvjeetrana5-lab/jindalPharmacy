package dataman.dmToolkitR.dmPopupPanel.client;

import com.google.gwt.user.client.ui.PopupPanel;

public class DmPopupPanel extends PopupPanel {

    private static final String DEFAULT_WIDTH_SMALL = "200px";
    private static final String DEFAULT_WIDTH_MEDIUM = "500px";
    private static final String DEFAULT_WIDTH_LARGE = "800px";

    public DmPopupPanel() {
        this.setStyleName("responsivePopup");
    }

    public void showWithDefaultLayout() {
        this.setWidth(DEFAULT_WIDTH_MEDIUM);
        this.setAutoHideEnabled(true);
        this.setGlassEnabled(true);
        this.center();
    }

}
