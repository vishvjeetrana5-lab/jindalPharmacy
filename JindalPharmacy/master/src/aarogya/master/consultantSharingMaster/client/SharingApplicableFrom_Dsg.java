package aarogya.master.consultantSharingMaster.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;

public abstract class SharingApplicableFrom_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnClose = new Button();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final DmTextBox txtDtNew = new DmTextBox(100, false);
    final RadioButton rbtnOldPrice = new RadioButton("PriceList", "Select Price List");
    final RadioButton rbtnNewPrice = new RadioButton("PriceList", "New Price List");
    final Button btnSearch = new Button("Search");
    final DmLabel lblCategory = new DmLabel("Category");
    final FlowPanel flwOldPriceList = new FlowPanel();
    final FlowPanel flwNewPriceList = new FlowPanel();
    final FlowPanel flw = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHDtApplicable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDtApplicable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHDtApplicable() {
        txtHDtApplicable.getDmGrid().addTextColumn(txtHDtApplicable.getDmGrid().getPropertyAccess().name(), "Name", 0, "applicableFrom", DmDataType.DATE);
        txtHDtApplicable.getDmGrid().initializeGrid(txtHDtApplicable.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDtApplicable.getHelper().addSort("applicableFrom", DmSortType.DESC);
    }
    //</editor-fold>

    public SharingApplicableFrom_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flwTemp;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("236px");
        popupPanel.setHeight("343px");

        vrtInnerForm.add(iniToolContorls());
        flwTemp = new FlowPanel();
        DmClient.setStyle(flwTemp, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTemp.add(rblArea());
        vrtInnerForm.add(flwTemp);
        vrtInnerForm.setCellHeight(flwTemp, "25px");

        iniTxtHDtApplicable();
        DmClient.setStyle(flwNewPriceList, "300px", "290px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwOldPriceList, "300px", "290px", null, null, DmPosition.NONE, true);
        flwOldPriceList.add(oldPriceList());
        flwNewPriceList.add(newPriceList());

        DmClient.setStyle(flw, "300px", null, null, null, DmPosition.NONE, true);
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(flwNewPriceList);
        flw.add(flwOldPriceList);
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
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        flwCancel.add(btnClose);

        label = new InlineLabel("Delete");
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.setTitle("Delete");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("70px");
        flwDelete.add(vl.add(btnDelete));

        toolContainer.setTitle("Price List");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;

    }

    private FlowPanel rblArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "25px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, null, "18px", null, null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(1, Style.Unit.PCT);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTr.setStyleName("dm-StripLabel");;
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(rbtnOldPrice));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(rbtnNewPrice));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel oldPriceList() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "288px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "269px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdApplicableDate()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel newPriceList() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "85%", "290px", null, null, DmPosition.CENTER, true);

        /*========================================================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("New Applicable Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDtNew));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*========================================================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Copy Applicable Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDtApplicable));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*========================================================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="GridApplicableDate">
    interface GrdApplicable_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }

    final DmGrid<KeyFieldExtended, GrdApplicable_PrpAcs> grdDtApplicable = new DmGrid(GWT.create(GrdApplicable_PrpAcs.class));

    private Grid iniGrdApplicableDate() {
        grdDtApplicable.addTextColumn(grdDtApplicable.getPropertyAccess().code(), "Applicable From", 150, "");
        grdDtApplicable.addTextColumn(grdDtApplicable.getPropertyAccess().name(), "Applicable To", 150, "");
        grdDtApplicable.initializeGrid(grdDtApplicable.getPropertyAccess().fieldHashCode(), 302, 288);
        return grdDtApplicable.getGrid();
    }
    //</editor-fold>

}
