package aarogya.dashBoard.walkThrough.client;

import aarogya.dashBoard.walkThrough.shared.GrdWalkThrough_Mdl;
import aarogya.dashBoard.walkThrough.shared.SearchValue_Mdl;
import aarogya.projectMenu.self.shared.pmDashBoard;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;

abstract class WalkThrough_Dsg extends IsWidgetExtended {

    final String widgetName = new pmDashBoard().WALK_THROUGH.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwRefresh = new FlowPanel();
    final Button btnRefresh = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();

    public WalkThrough_Dsg() {
        ini();
        iniGrdWalk();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("513px");
        vrtInnerForm.add(toolContainer);
        vrtInnerForm.add(iniToolContorls());

        flw = new FlowPanel();
        flw.setWidth("100%");
        flw.getElement().getStyle().setBackgroundColor("#eff");
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("50px");
        btnRefresh.setTitle("Refresh");
        flwRefresh.add(btnRefresh);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("50px");
        btnClose.setTitle("Close");
        flwClose.add(btnClose);
        toolContainer.addWidget(txtSearchValue);
        txtSearchValue.setWidth("350px");
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwClose);

        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "486px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "486px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "484px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(gridWalkThrough);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

    //<editor-fold defaultstate="collapsed" desc="gridWalkThrough">
    interface GridWalk_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdWalkThrough_Mdl> {

        @Editor.Path("Pending")
        ValueProvider<GrdWalkThrough_Mdl, String> pending();

    }
    final DmGridPaging<GrdWalkThrough_Mdl, GridWalk_PrpAcs, SearchValue_Mdl> gridWalkThrough = new DmGridPaging(GWT.create(GridWalk_PrpAcs.class), 20, true);

    private void iniGrdWalk() {
        gridWalkThrough.getDmGrid().addTextColumn(gridWalkThrough.getDmGrid().getPropertyAccess().name(), "Description", 850, "description");
        gridWalkThrough.getDmGrid().addTextColumn(gridWalkThrough.getDmGrid().getPropertyAccess().pending(), "Pending", 1, "cnt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        gridWalkThrough.getDmGrid().initializeGrid(gridWalkThrough.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 2);
        gridWalkThrough.getHelper().addSort("description", DmSortType.ASC);
    }
    //</editor-fold>

}
