package aarogya.dashBoard.dashBoardCard.client;

import aarogya.projectMenu.self.shared.pmDashBoard;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DashBoardCard_Dsg {

    final FlowPanel cardFlowPanel = new FlowPanel();
    final protected FlowPanel expandCollapseFlowPanel = new FlowPanel();
    final public Anchor expandAnchor = new Anchor();
    final public Anchor collapseAnchor = new Anchor();
    final private FlowPanel externalFlowPanel = new FlowPanel();
    final public Anchor externalLinkAnchor = new Anchor();

    final private FlowPanel topFlowPanel = new FlowPanel();
    final private FlowPanel bottomFlowPanel = new FlowPanel();
    final private FlowPanel chartFlowPanel = new FlowPanel();
    final private FlowPanel countFlowPanel = new FlowPanel();
    final protected DmLabel lblName = new DmLabel("-");

    public DashBoardCard_Dsg() {
        cardFlowPanel.addStyleName("dashBoard-box");
        cardFlowPanel.add(topSection());
        cardFlowPanel.add(countSection());
    }

    private FlowPanel topSection() {
        topFlowPanel.addStyleName("dashBoard-top");

        HorizontalPanel hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(lblName);
        hrtPnl.setCellWidth(lblName, "100%");

        externalLinkAnchor.addStyleName("fa fa-external-link");
        externalLinkAnchor.getElement().setAttribute("style", "color: white;font-weight: bold;padding-right: 10px; cursor: pointer;");
        externalFlowPanel.add(externalLinkAnchor);
        hrtPnl.add(externalFlowPanel);

        expandAnchor.addStyleName("fa fa-expand");
        expandAnchor.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");

        collapseAnchor.addStyleName("fa fa-compress");
        collapseAnchor.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");

        expandCollapseFlowPanel.add(expandAnchor);
        hrtPnl.add(expandCollapseFlowPanel);

        topFlowPanel.add(hrtPnl);

        return topFlowPanel;
    }

    protected FlowPanel countSection() {
        bottomFlowPanel.clear();
        bottomFlowPanel.add(countFlowPanel);

        return bottomFlowPanel;
    }

    protected FlowPanel chartSection() {
        bottomFlowPanel.clear();
        
        bottomFlowPanel.add(chartFlowPanel);

        return bottomFlowPanel;
    }

    protected void setExpandWidget(Widget expandWgt) {
        chartFlowPanel.clear();
        chartFlowPanel.add(expandWgt);
    }

    protected void setCollapseWidget(Widget collapseWgt) {
        countFlowPanel.clear();
        countFlowPanel.add(collapseWgt);
    }

    public void setColor(String color) {
        topFlowPanel.getElement().getStyle().setBackgroundColor(DmShared.xNull(color));
        cardFlowPanel.getElement().setAttribute("style", "border-bottom: 3px solid " + DmShared.xNull(color) + ";");
    }

    public void setCaption(String caption) {
        lblName.setText(DmShared.xNull(caption).trim());
        lblName.setTitle(DmShared.xNull(caption).trim());
    }

    public void isExternalLinkRequired(boolean isRequired) {
        if (!isRequired) {
            externalFlowPanel.clear();
        }
    }

    public void isExpandLinkRequired(boolean isRequired) {
        if (!isRequired) {
            expandCollapseFlowPanel.clear();
        }
    }
}
