package aarogya.pharmacy.walkInSearch.client;

import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;

abstract class WalkInSearch_Dsg {

    final String widgetName = "Walk-In Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public WalkInSearch_Dsg() {
        iniWalkInSearch();
        ini();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("960px");
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.getElement().setAttribute("Style", "float:left;");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;
        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; border-top: 1px solid gray;");
        flwTr.add(grdWalkInSearch);
        vl.add(grdWalkInSearch.getDmGrid().getGrid());
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.7%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Manual Code");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grid Patient Search">
    interface DmLedgerMastSearch_PrpAcs extends KeyField_T_PrpAcs<LedgerMaster_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<LedgerMaster_Mdl, String> manualCode();

        @Editor.Path("DisplayName")
        ValueProvider<LedgerMaster_Mdl, String> displayName();

        @Editor.Path("Mobile")
        ValueProvider<LedgerMaster_Mdl, String> mobile();

        @Editor.Path("Address")
        ValueProvider<LedgerMaster_Mdl, String> address();

        @Editor.Path("CityName")
        ValueProvider<LedgerMaster_Mdl, String> cityName();

        @Editor.Path("IdTypeName")
        ValueProvider<LedgerMaster_Mdl, String> idTypeName();

        @Editor.Path("IdNo")
        ValueProvider<LedgerMaster_Mdl, String> idNo();

    }

    final DmGridPaging<LedgerMaster_Mdl, DmLedgerMastSearch_PrpAcs, LedgerMaster_Mdl> grdWalkInSearch = new DmGridPaging(GWT.create(DmLedgerMastSearch_PrpAcs.class), 10);

    private void iniWalkInSearch() {
        grdWalkInSearch.getDmGrid().addTextColumn(grdWalkInSearch.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        grdWalkInSearch.getDmGrid().addTextColumn(grdWalkInSearch.getDmGrid().getPropertyAccess().displayName(), "Display Name", 200, "displayName");
        grdWalkInSearch.getDmGrid().addTextColumn(grdWalkInSearch.getDmGrid().getPropertyAccess().mobile(), "Mobile", 100, "mobile");
        grdWalkInSearch.getDmGrid().addTextColumn(grdWalkInSearch.getDmGrid().getPropertyAccess().address(), "Address", 250, "address");
        grdWalkInSearch.getDmGrid().addTextColumn(grdWalkInSearch.getDmGrid().getPropertyAccess().cityName(), "City", 100, "cityName");
        grdWalkInSearch.getDmGrid().addTextColumn(grdWalkInSearch.getDmGrid().getPropertyAccess().idTypeName(), "ID Type", 100, "idTypeName");
        grdWalkInSearch.getDmGrid().addTextColumn(grdWalkInSearch.getDmGrid().getPropertyAccess().idNo(), "ID No.", 100, "idNo");
        grdWalkInSearch.getDmGrid().initializeGrid(grdWalkInSearch.getDmGrid().getPropertyAccess().code(), 970, 200);
        grdWalkInSearch.getHelper().addSort("displayName", DmSortType.ASC);
    }
    //</editor-fold>
}
