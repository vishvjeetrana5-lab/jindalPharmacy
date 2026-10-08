package aarogya.pharmacy.irpInfoPopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class IrpInfoPopup_Dsg {

    public static final String widgetName = "IRP Info";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final FlowPanel topContainer = new FlowPanel();
    final FlowPanel flwMode = new FlowPanel();

    final DmRemarkTextBox txtUrl = new DmRemarkTextBox(250, true);
    final DmLabel lblMode = new DmLabel("New"); 
    
    //<editor-fold defaultstate="collapsed" desc="txtHIrpName">
    interface TxtHIrpName_PrpAcs extends KeyField_T_PrpAcs<IrpInfoPopup_Mdl> {

        @Editor.Path("Url")
        ValueProvider<IrpInfoPopup_Mdl, String> url();

        @Editor.Path("IsActiveText")
        ValueProvider<IrpInfoPopup_Mdl, String> isActive();

    }

    final DmTextHelp<IrpInfoPopup_Mdl, TxtHIrpName_PrpAcs> txtHIrpName = new DmTextHelp(GWT.create(TxtHIrpName_PrpAcs.class), 6, true);

    private void iniTxtHIrpName() {
        txtHIrpName.setAppendable(true);
        txtHIrpName.getDmGrid().addTextColumn(txtHIrpName.getDmGrid().getPropertyAccess().name(), "Name", 80, "name");
        txtHIrpName.getDmGrid().addTextColumn(txtHIrpName.getDmGrid().getPropertyAccess().url(), "URL", 200, "url");
        txtHIrpName.getDmGrid().addTextColumn(txtHIrpName.getDmGrid().getPropertyAccess().isActive(), "Active", 50, "isActiveName");
        txtHIrpName.getDmGrid().initializeGrid(txtHIrpName.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHIrpName.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIrpType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIrpType = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 5, true, true);

    private void iniTxtHIrpType() {
        txtHIrpType.getDmGrid().addTextColumn(txtHIrpType.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIrpType.getDmGrid().initializeGrid(txtHIrpType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIsActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, true);

    public void iniTxtHIsActive() {
        txtHIsActive.getDmGrid().addTextColumn(txtHIsActive.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHIsActive.getDmGrid().initializeGrid(txtHIsActive.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
        txtHIsActive.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold> 

    public IrpInfoPopup_Dsg() {
        ini();
        iniTxtHIsActive();
        iniTxtHIrpType();
        iniTxtHIrpName();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
        popupPanel.setHeight("130px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());

        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", null, null, null, DmEnum.DmPosition.CENTER_HORIZONTAL, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(9, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(new DmLabel(""));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "78%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIrpType));
        txtHIrpType.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(new DmLabel(""));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "78%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIrpName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("URL");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(new DmLabel(""));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "78%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtUrl));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(new DmLabel(""));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtHIsActive);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        DmClient.setStyle(flwOk, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        DmClient.setStyle(flwClose, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwClose.add(btnClose);

        label = new InlineLabel("Delete");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.setTitle("Delete");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("70px");
        DmClient.setStyle(flwDelete, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwDelete.add(btnDelete);

        lblMode.getElement().setAttribute("style", "max-width: 60px; color: rgb(255, 255, 255); float: right; padding: 3px 9px; margin-top: 5px; font-family: arial; font-weight: bold; font-size: 12px; margin-right: 5px; background-color: rgb(251, 140, 0);");
        flwMode.add(lblMode);

        DmClient.setStyle(topContainer, "100%", null, null, null, DmEnum.DmPosition.NONE, true);
        toolContainer.addWidget(topContainer);

        return toolContainer;
    }

}
