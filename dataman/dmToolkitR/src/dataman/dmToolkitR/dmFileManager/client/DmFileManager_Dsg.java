package dataman.dmToolkitR.dmFileManager.client;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavTabs;
import org.gwtbootstrap3.client.ui.TabContent;
import org.gwtbootstrap3.client.ui.TabPane;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

abstract class DmFileManager_Dsg {

    final NavTabs navTabs = new NavTabs();
    final TabContent tabContent = new TabContent();
    final TabPane tabPaneCurrent = new TabPane();
    final TabPane tabPaneHistory = new TabPane();
    final AnchorListItem tabCurrent = new AnchorListItem();
    final AnchorListItem tabHistory = new AnchorListItem();
    final FlowPanel flwFileManager = new FlowPanel();
    final FlowPanel flwFileHistory = new FlowPanel();
    final FlowPanel flwFileCurrent = new FlowPanel();
    
    
    public DmFileManager_Dsg() {
        ini();
    }

    private void ini() {
        flwFileManager.add(workingArea());
        tabCurrent.getWidget(0).getElement().setAttribute("Style", "margin-left: 5px;padding: 5px 12px !important;color: black !important;");
        tabHistory.getWidget(0).getElement().setAttribute("Style", "margin-left: 5px;padding: 5px 12px !important;color: black !important;");
        navTabs.getElement().setAttribute("Style", " background-color: rgb(231, 231, 231);  margin-bottom: 5px;");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTr = new FlowPanel();
        tabCurrent.setText("Current");
        tabCurrent.setHref("#current");
        tabCurrent.setDataToggle(Toggle.TAB);
        navTabs.add(tabCurrent);

        tabHistory.setText("History");
        tabHistory.setHref("#hisory");
        tabHistory.setDataToggle(Toggle.TAB);
        navTabs.add(tabHistory);

        tabPaneCurrent.setId("current");
        tabPaneCurrent.setFade(true);

        tabPaneCurrent.add(flwFileCurrent);
        tabContent.add(tabPaneCurrent);

        tabPaneHistory.setId("template");
        tabPaneHistory.setFade(true);
        tabPaneHistory.add(flwFileHistory);
        tabContent.add(tabPaneHistory);

        flwTr.add(navTabs);
        flwTr.add(tabContent);
        return flwTr;
    }
}
