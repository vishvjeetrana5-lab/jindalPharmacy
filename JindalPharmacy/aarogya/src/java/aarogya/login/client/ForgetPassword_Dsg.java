
package aarogya.login.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;


public abstract class ForgetPassword_Dsg {
    
    final String widgetName = "Forget Password";

    final PopupPanel popupPanel = new PopupPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final Button btnSubmit = new Button("Submit");

    final DmTextBox txtUserName = new DmTextBox(10, true);
    final DmTextBoxNumeric txtMobile = new DmTextBoxNumeric(0, false, 10, true);
    final DmTextBox txtEmail = new DmTextBox(100, true);

    //<editor-fold defaultstate="collapsed" desc="txtHPltCode">
    protected final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPltCode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtProductLicensedTo() {
        txtHPltCode.getDmGrid().addTextColumn(txtHPltCode.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPltCode.getDmGrid().initializeGrid(txtHPltCode.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHPltCode.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHFinancialYear">
    protected final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFinancialYear = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtFinancialYear() {
        txtHFinancialYear.getDmGrid().addTextColumn(txtHFinancialYear.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHFinancialYear.getDmGrid().initializeGrid(txtHFinancialYear.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHFinancialYear.getHelper().addSort("v_Prefix", DmEnum.DmSortType.DESC, "v_Prefix");
    }
    //</editor-fold>

    public ForgetPassword_Dsg() {
        ini();
        iniTxtProductLicensedTo();
        iniTxtFinancialYear();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("550px");
        popupPanel.setHeight("300px");
        vrtInnerForm.add(iniToolControls());

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControls() {
        InlineLabel label;
        Anchor aBtn;
        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("40px");
        flwClose.add(btnClose);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%%", "160px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Product Licesed To");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        /*====================================< New Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPltCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Financial Year");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);


        /*====================================< New Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHFinancialYear));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("User Name");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        /*====================================< New Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtUserName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Mobile");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);


        /*====================================< New Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        /*====================================< New Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmail));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(30, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        btnSubmit.setStyleName("dm-Login-Button " + "dm-next-button-style next-color");
        btnSubmit.setHeight("30px");
        flwTd.add(vl.add(btnSubmit));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
    
}
