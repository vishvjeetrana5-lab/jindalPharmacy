package aarogya.dashBoard.home.client;

import aarogya.dashBoard.favorite.client.Favorite;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class Home_Dsg extends IsWidgetExtended {

    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwCardContainer = new FlowPanel();
    private int height = Window.getClientHeight() - 37;

    public Home_Dsg() {
        ini();
    }

    private void ini() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        FlowPanel flowPanel;

        DmClient.setStyle(horizontalPanel, "100%", "100%", null, null, DmPosition.NONE, true);

        /*====================================< New Colunm >====================================*/
        flowPanel = new FlowPanel();
        flowPanel.add(leftArea());
        horizontalPanel.add(flowPanel);
        horizontalPanel.setCellWidth(flowPanel, "100%");
        horizontalPanel.setCellHeight(flowPanel, "100%");

        /*====================================< New Colunm >====================================*/
        flowPanel = new FlowPanel();
        flowPanel.add(rightArea());
        horizontalPanel.add(flowPanel);
        horizontalPanel.setCellHeight(flowPanel, "100%");

        flwForm.add(horizontalPanel);
    }

    private FlowPanel leftArea() {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.getElement().getStyle().setBackgroundColor("#eff");
        flowPanel.getElement().getStyle().setHeight(height, Style.Unit.PX);
        flowPanel.getElement().getStyle().setOverflowY(Style.Overflow.AUTO);
        flowPanel.add(flwCardContainer);

        return flowPanel;
    }

    private FlowPanel rightArea() {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.getElement().setAttribute("style", "box-shadow: -2px 0px 3px #ccc; overflow-y:auto;background-color:white;width:210px");
        flowPanel.getElement().getStyle().setHeight(height, Style.Unit.PX);
        flowPanel.add(new Favorite(height).asWidget());

        return flowPanel;
    }
}
