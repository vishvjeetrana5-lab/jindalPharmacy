package aarogya.selfToolkit.permanentAddressPopup.client;

import aarogya.selfToolkit.permanentAddressPopup.shared.TxtHCity_Mdl;
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
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class PermanentAddressPopup_Dsg {

    public final static String widgetName = "Permanent Address";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final FlowPanel flwCopy = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnCopy = new Button();
    final DmTextBox txtAddress1 = new DmTextBox(50, false);
    final DmTextBox txtAddress2 = new DmTextBox(50, false);
    final DmTextBoxNumeric txtPINCode = new DmTextBoxNumeric(0, true, 6, false);
    final DmLabel lblCity = new DmLabel("City");
    final DmLabel lblArea = new DmLabel("Area");

    //<editor-fold defaultstate="collapsed" desc="TxtHCity">
    interface TxtHCity_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

        @Editor.Path("CityName")
        ValueProvider<TxtHCity_Mdl, String> cityName();

        @Editor.Path("StdCode")
        ValueProvider<TxtHCity_Mdl, String> stdCode();

        @Editor.Path("CountryCode")
        ValueProvider<TxtHCity_Mdl, String> CountryCode();

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> CountryName();
    }
    final DmTextHelp<TxtHCity_Mdl, TxtHCity_PrpAcs> txtHCity = new DmTextHelp(GWT.create(TxtHCity_PrpAcs.class), 7, true);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().cityName(), "Name", 0, "cityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stdCode(), "STD", 70, "stdCode");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().CountryName(), "Country", 100, "CountryName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 420, 0, 1);
        txtHCity.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
        txtHCity.getHelper().addSort("stateName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHArea">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHArea = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHArea() {
        txtHArea.getDmGrid().addTextColumn(txtHArea.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHArea.getDmGrid().initializeGrid(txtHArea.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHArea.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PermanentAddressPopup_Dsg() {
        ini();
        iniTxtHCity();
        iniTxtHArea();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("120px");

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

        label = new InlineLabel("Copy");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-copy");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCopy.addStyleName("dm-top-control-btnStyle");
        btnCopy.getElement().appendChild(aBtn.getElement());
        btnCopy.getElement().appendChild(label.getElement());
        btnCopy.setWidth("70px");
        btnCopy.setTitle("Copy Address");
        flwCopy.add(vl.add(btnCopy));

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

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCopy);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "90px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
//
//        btnCopy.setWidth("100%");
//        btnCopy.addStyleName("dm-button-grad");
//        btnCopy.setTitle("Copy Details");
//
//        flwTd.add(btnCopy);
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress1));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(14, Style.Unit.PCT);
        flwTd.add(vl.add(txtAddress2));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblCity);
//        lblCity.getElement().getStyle().setCursor(Style.Cursor.POINTER);
//        lblCity.getElement().getStyle().setColor("blue");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity, "City"));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblArea.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        lblArea.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
//        lblArea.getElement().getStyle().setCursor(Style.Cursor.POINTER);
//        lblArea.getElement().getStyle().setColor("blue");
        flwTd.add(lblArea);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHArea, "Area"));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PIN Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPINCode, "PIN Code"));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
