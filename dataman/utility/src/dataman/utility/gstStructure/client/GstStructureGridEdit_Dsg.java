package dataman.utility.gstStructure.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
 
abstract public class GstStructureGridEdit_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmTextBox txtLocationCode = new DmTextBox();
    final DmTextBox txtGstTypeCode = new DmTextBox();
    final DmTextBox txtGstHeadCode = new DmTextBox();
    final DmTextBoxNumeric txtAtRate = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 5, false);
    final FlowPanel flwOk = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnOk = new Button();
    final Button btnClose = new Button();
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHPurchaseSalesAc">
    interface TxtHPurchaseSalesAc_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, TxtHPurchaseSalesAc_PrpAcs> txtHPurchaseSalesAc = new DmTextHelp(GWT.create(TxtHPurchaseSalesAc_PrpAcs.class), 7);

    private void iniTxtHPurchaseSalesAc() {
        txtHPurchaseSalesAc.getDmGrid().addTextColumn(txtHPurchaseSalesAc.getDmGrid().getPropertyAccess().name(), "Name", 220, "name");
        txtHPurchaseSalesAc.getDmGrid().initializeGrid(txtHPurchaseSalesAc.getDmGrid().getPropertyAccess().code(), 230, 1);
        txtHPurchaseSalesAc.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHInputTaxAc">
    interface TxtHInputTaxAc_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, TxtHInputTaxAc_PrpAcs> txtHInputTaxAc = new DmTextHelp(GWT.create(TxtHInputTaxAc_PrpAcs.class), 7);

    private void iniTxtHInputTaxAc() {
        txtHInputTaxAc.getDmGrid().addTextColumn(txtHInputTaxAc.getDmGrid().getPropertyAccess().name(), "Name", 220, "name");
        txtHInputTaxAc.getDmGrid().initializeGrid(txtHInputTaxAc.getDmGrid().getPropertyAccess().code(), 230, 1);
        txtHInputTaxAc.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOutputTaxAc">
    interface TxtHOutputTaxAc_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, TxtHOutputTaxAc_PrpAcs> txtHOutputTaxAc = new DmTextHelp(GWT.create(TxtHOutputTaxAc_PrpAcs.class), 7);

    private void iniTxtHOutputTaxAc() {
        txtHOutputTaxAc.getDmGrid().addTextColumn(txtHOutputTaxAc.getDmGrid().getPropertyAccess().name(), "Name", 220, "name");
        txtHOutputTaxAc.getDmGrid().initializeGrid(txtHOutputTaxAc.getDmGrid().getPropertyAccess().code(), 230, 0);
        txtHOutputTaxAc.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRcmAc">
    interface TxtHRcmAc_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, TxtHRcmAc_PrpAcs> txtHRcmAc = new DmTextHelp(GWT.create(TxtHRcmAc_PrpAcs.class), 7);

    private void iniTxtHRcmAc() {
        txtHRcmAc.getDmGrid().addTextColumn(txtHRcmAc.getDmGrid().getPropertyAccess().name(), "Name", 220, "name");
        txtHRcmAc.getDmGrid().initializeGrid(txtHRcmAc.getDmGrid().getPropertyAccess().code(), 230, 1);
        txtHRcmAc.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public GstStructureGridEdit_Dsg(String title, String width, String height) {

        iniTxtHPurchaseSalesAc();
        iniTxtHInputTaxAc();
        iniTxtHOutputTaxAc();
        iniTxtHRcmAc();

        FlowPanel flw;
        toolContainer.setTitle(title);
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth(width);
        popupPanel.setHeight(height);
        vrtInnerForm.add(initialzeTop());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);

    }

    private DmToolContainer initialzeTop() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok: (Alt + K)");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("50px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escape)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("60px");
        flwClose.add(btnClose);

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        return toolContainer;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "255px", null, null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl = new DmLabel("Location");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl = new DmLabel("GST Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTd.add(vl.add(txtLocationCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtGstTypeCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl = new DmLabel("GST Head");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl = new DmLabel("At Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTd.add(vl.add(txtGstHeadCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtAtRate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);


        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lbl = new DmLabel("Purchase/Sales A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTd.add(vl.add(txtHPurchaseSalesAc));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lbl = new DmLabel("Input Tax A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTd.add(vl.add(txtHInputTaxAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lbl = new DmLabel("Output Tax A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTd.add(vl.add(txtHOutputTaxAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);


        //====================================< ******* >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lbl = new DmLabel("RCM A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTd.add(vl.add(txtHRcmAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

}
