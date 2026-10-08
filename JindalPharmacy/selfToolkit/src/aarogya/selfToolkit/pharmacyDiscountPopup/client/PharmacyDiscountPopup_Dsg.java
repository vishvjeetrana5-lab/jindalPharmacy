package aarogya.selfToolkit.pharmacyDiscountPopup.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class PharmacyDiscountPopup_Dsg {

    public final static String widgetName = "Pharmacy Discount";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwClear = new FlowPanel();
    final Button btnClear = new Button();

    final DmTextBoxNumeric txtDiscountPer = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, false, 8, false);
//    final DmTextBox txtDisReason = new DmTextBox(100, true);

    //<editor-fold defaultstate="collapsed" desc="FlowPanels">
    final FlowPanel flwGroup = new FlowPanel();
    final FlowPanel flwGivenBy = new FlowPanel();
//</editor-fold>

    private boolean isOpenedFromPurchase = false;

    //<editor-fold defaultstate="collapsed" desc="txtHDisConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDisConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHDisConsultant() {
        txtHDisConsultant.getDmGrid().addTextColumn(txtHDisConsultant.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDisConsultant.getDmGrid().initializeGrid(txtHDisConsultant.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDisConsultant.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGroupName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGroupName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHGroupName() {
        txtHGroupName.getDmGrid().addTextColumn(txtHGroupName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGroupName.getDmGrid().initializeGrid(txtHGroupName.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHGroupName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDiscountReason">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDisReason = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHDiscountReason() {
        txtHDisReason.getDmGrid().addTextColumn(txtHDisReason.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDisReason.getDmGrid().initializeGrid(txtHDisReason.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDisReason.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PharmacyDiscountPopup_Dsg(boolean isOpenedFromPurchase) {
        this.isOpenedFromPurchase = isOpenedFromPurchase;
        ini();
        iniTxtHDisConsultant();
        iniTxtHGroupName();
        iniTxtHDiscountReason();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("350px");
//        popupPanel.setHeight("190px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        btnOk.setTitle("Ok");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        btnCancel.setTitle("Close");
        flwCancel.add(vl.add(btnCancel));

        label = new InlineLabel("Clear");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClear.addStyleName("dm-top-control-btnStyle");
        btnClear.getElement().appendChild(aBtn.getElement());
        btnClear.getElement().appendChild(label.getElement());
        btnClear.setWidth("70px");
        btnClear.setTitle("Clear");
        flwClear.add(vl.add(btnClear));

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwClear);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "", "left", null, DmPosition.NONE, true);

        if (!isOpenedFromPurchase) {

            /*====================================< 1st Row >====================================*/
            flwTr = new FlowPanel();
            DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
            lbl = new DmLabel("Group");
            flwTd.add(lbl);
            flwTr.add(flwTd);

            flwTable.add(flwTr);

            /*====================================< 4th Row >====================================*/
            flwTr = new FlowPanel();
            DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
            flwTd.add(vl.add(txtHGroupName));
            flwTr.add(flwTd);

            flwTable.add(flwTr);
        }

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscountPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        if (!isOpenedFromPurchase) {
            /*====================================< 3rd Row >====================================*/
            flwTr = new FlowPanel();
            DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
            lbl = new DmLabel("Given By");
            flwTd.add(lbl);
            flwTr.add(flwTd);

            flwTable.add(flwTr);

            /*====================================< 4th Row >====================================*/
            flwTr = new FlowPanel();
            DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
            flwTd.add(vl.add(txtHDisConsultant));
            flwTr.add(flwTd);

            flwTable.add(flwTr);

            /*====================================< 5th Row >====================================*/
            flwTr = new FlowPanel();
            DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
            lbl = new DmLabel("Dis. Reason");
            flwTd.add(lbl);
            flwTr.add(flwTd);

            flwTable.add(flwTr);

            /*====================================< 6th Row >====================================*/
            flwTr = new FlowPanel();
            DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
            flwTd.add(vl.add(txtHDisReason));
            flwTr.add(flwTd);

            flwTable.add(flwTr);

            ///////////////////////////////////////////////////////////////////////////////////////
        }

        return flwTable;
    }
}
