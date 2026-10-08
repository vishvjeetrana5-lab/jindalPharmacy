package dataman.dmToolkit.dmDisplayStrip.client;

import com.google.gwt.user.client.ui.FlowPanel;

class DmDisplayStrip_Dsg {

    protected final FlowPanel flwWgt = new FlowPanel();
    public final FlowPanel flwBanner = new FlowPanel();
    boolean isBannerVisible = false;
    private final String tableStyle = "width : 100%;";

    public DmDisplayStrip_Dsg(int height, boolean isBannerVisible) {
        this.isBannerVisible = isBannerVisible;
        flwWgt.add(getDisplayPanel());
    }

    
    private FlowPanel getDisplayPanel() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        flwTable.getElement().setAttribute("style", tableStyle);

        if (isBannerVisible) {
            flwBanner.getElement().setAttribute("style", "width: 100%; float: left; background-color: rgb(81, 99, 149) ! important; color: rgb(255, 255, 255);");
            flwBanner.setStyleName("dm-StripLabel");
            flwTable.add(flwBanner);
        }

        return flwTable;
    }

}
