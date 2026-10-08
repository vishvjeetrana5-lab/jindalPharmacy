package dataman.dmToolkit.eGstPopup.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.eGstPopup.shared.TxtHIrpInfo_Mdl;

abstract class Setting_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHEwayIRP">
    interface IrpEway_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHIrpInfo_Mdl> {

        @Editor.Path("Url")
        ValueProvider<TxtHIrpInfo_Mdl, String> url();

    }
    final DmTextHelp<TxtHIrpInfo_Mdl, IrpEway_PrpAcs> txtHEwayIRP = new DmTextHelp(GWT.create(IrpEway_PrpAcs.class), 5, true);

    private void iniTxtHEwayBaseUrl() {
        txtHEwayIRP.getDmGrid().addTextColumn(txtHEwayIRP.getDmGrid().getPropertyAccess().name(), "Name", 80, "irpName");
        txtHEwayIRP.getDmGrid().addTextColumn(txtHEwayIRP.getDmGrid().getPropertyAccess().url(), "URL", 200, "url");
        txtHEwayIRP.getDmGrid().initializeGrid(txtHEwayIRP.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 200);
        txtHEwayIRP.getHelper().addSort("irpName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEinvIRP">
    interface IrpEinv_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHIrpInfo_Mdl> {

        @Editor.Path("Url")
        ValueProvider<TxtHIrpInfo_Mdl, String> url();

    }
    final DmTextHelp<TxtHIrpInfo_Mdl, IrpEinv_PrpAcs> txtHEinvIRP = new DmTextHelp(GWT.create(IrpEinv_PrpAcs.class), 5, true);

    private void iniTxtHEinvBaseUrl() {
        txtHEinvIRP.getDmGrid().addTextColumn(txtHEinvIRP.getDmGrid().getPropertyAccess().name(), "Name", 80, "irpName");
        txtHEinvIRP.getDmGrid().addTextColumn(txtHEinvIRP.getDmGrid().getPropertyAccess().url(), "URL", 200, "url");
        txtHEinvIRP.getDmGrid().initializeGrid(txtHEinvIRP.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 200);
        txtHEinvIRP.getHelper().addSort("irpName", DmEnum.DmSortType.ASC);
    }

//</editor-fold>
    public Setting_Dsg() {
        ini();
        iniTxtHEwayBaseUrl();
        iniTxtHEinvBaseUrl();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("300px");
        popupPanel.setHeight("90px");
        iniToolControls();
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolControls() {
        InlineLabel label;
        Anchor aBtn;
        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Cancel");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "50px", null, null, DmPosition.CENTER, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "95%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("E-Way IRP");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "66%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEwayIRP));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//   
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "95%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("E-Invoice IRP");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "66%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEinvIRP));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//     
        return flwTable;
    }

}
