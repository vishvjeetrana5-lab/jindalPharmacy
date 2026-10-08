package aarogya.selfToolkit.patientSearch.client;

import aarogya.selfToolkit.patientSearch.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;

abstract class PatientSearch_Dsg {

    public final static String widgetName = "Patient Search";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public PatientSearch_Dsg() {
        ini();
        iniPatientSearch();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1000px");
        popupPanel.setHeight("410px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
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
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        btnCancel.setTitle("Close");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "380px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.8%", "20px", "left", null, DmPosition.NONE, true);
        txtSearchValue.getElement().setPropertyString("placeholder", "Search Value");
        flwTr.add(vl.add(txtSearchValue));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "335px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(dmPatientSearch));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(legendSection());
        flwTr.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTable.add(flwTr);

        return flwTable;
    }
    
     private FlowPanel legendSection() {
        FlowPanel flwTable, flwTr, flwTd, flwTemp;
        Label lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "25px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "25px", "right", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 90%; display: block; float: left;");
        flwTemp = new FlowPanel();
        flwTemp.getElement().setAttribute("style", "width: 20px; height: 18px; background-color: rgb(184, 207, 238); float: left; margin-right: 10px; border: 1px solid rgb(251, 131, 52);");
        flwTd.add(flwTemp);
        lbl = new Label("Admitted");
        lbl.getElement().getStyle().setPaddingTop(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Patient Search">
    interface DmPatientSearch_PrpAcs extends KeyField_T_PrpAcs<GrdPatientDetails_Mdl> {

        @Editor.Path("VoucherDate")
        ValueProvider<GrdPatientDetails_Mdl, String> voucherDate();

        @Editor.Path("PatientName")
        ValueProvider<GrdPatientDetails_Mdl, String> patientName();

        @Editor.Path("GuardianName")
        ValueProvider<GrdPatientDetails_Mdl, String> guardianName();

        @Editor.Path("Mobile")
        ValueProvider<GrdPatientDetails_Mdl, String> mobile();

        @Editor.Path("IdNo")
        ValueProvider<GrdPatientDetails_Mdl, String> idNo();
    }

    final DmGridPaging<GrdPatientDetails_Mdl, DmPatientSearch_PrpAcs, SearchValue_Mdl> dmPatientSearch = new DmGridPaging(GWT.create(DmPatientSearch_PrpAcs.class), 14);

    public void iniPatientSearch() {
        dmPatientSearch.getDmGrid().addTextColumn(dmPatientSearch.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 1, "patientName");
        dmPatientSearch.getDmGrid().addTextColumn(dmPatientSearch.getDmGrid().getPropertyAccess().guardianName(), "Guardian Name", 175, "guardianName");
        dmPatientSearch.getDmGrid().addTextColumn(dmPatientSearch.getDmGrid().getPropertyAccess().voucherDate(), "Date", 85, "v_Date");
        dmPatientSearch.getDmGrid().addTextColumn(dmPatientSearch.getDmGrid().getPropertyAccess().mobile(), "Mobile", 100, "mobile");
        dmPatientSearch.getDmGrid().addTextColumn(dmPatientSearch.getDmGrid().getPropertyAccess().idNo(), "ID No.", 100, "idNo");
        dmPatientSearch.getDmGrid().addTextColumn(dmPatientSearch.getDmGrid().getPropertyAccess().name(), "UHID No.", 120, "uhidNo");
        dmPatientSearch.getDmGrid().initializeGrid(dmPatientSearch.getDmGrid().getPropertyAccess().code(), 980, 325, 1);

        dmPatientSearch.getHelper().addSort("v_Date", DmSortType.DESC);
        dmPatientSearch.getHelper().addSort("v_No", DmSortType.DESC);

        dmPatientSearch.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdPatientDetails_Mdl>() {
            @Override
            public String getColStyle(GrdPatientDetails_Mdl model, ValueProvider<? super GrdPatientDetails_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String rtnStyle = "dm-GridCellFormat ";
                if (model.isPatientAdmitted()) {
                    rtnStyle += "dm-GridCellSelected";
                }
                return rtnStyle;
            }

            @Override
            public String getRowStyle(GrdPatientDetails_Mdl model, int rowIndex) {
                return "";
            }
        });
    }
    //</editor-fold>
}
