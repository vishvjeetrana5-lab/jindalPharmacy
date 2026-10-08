package aarogya.dashBoard.favorite.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class Favorite_Dsg extends IsWidgetExtended {

    final FlowPanel flwForm = new FlowPanel();

    final FlowPanel settingFlowPanel = new FlowPanel();
    final Anchor settingAnchor = new Anchor();
    final FlowPanel containerFlowPanel = new FlowPanel();
    final FlowPanel container = new FlowPanel();

    public Favorite_Dsg(int height) {
        flwForm.add(favoriteArea(height));
        container.setStyleName("favorite-container");
    }

    private FlowPanel favoriteArea(int height) {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "35px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(topSection());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", (height - 35) + "px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setOverflowY(Style.Overflow.AUTO);
        flwTr.add(container);
        flwTable.add(flwTr);

        return flwTable;
    }

    private HorizontalPanel topSection() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.getElement().setAttribute("style", "background-color: #fff; border-bottom: 1px solid gray; padding: 8px 5px;");

        DmLabel lbl = new DmLabel("Favorite");
        lbl.getElement().setAttribute("style", "font-size: 14px; font-weight: bold; color: rgb(81, 99, 149);");
        horizontalPanel.add(lbl);
        horizontalPanel.setCellWidth(lbl, "100%");

        settingAnchor.addStyleName("fa fa-cogs");
        settingAnchor.getElement().setAttribute("style", "cursor: pointer; color: rgb(81, 99, 149); font-size: 17px;");
        settingFlowPanel.add(settingAnchor);
        horizontalPanel.add(settingFlowPanel);

        return horizontalPanel;
    }
}
