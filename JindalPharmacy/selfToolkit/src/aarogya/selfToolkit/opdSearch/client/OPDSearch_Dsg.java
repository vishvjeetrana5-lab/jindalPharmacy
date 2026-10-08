package aarogya.selfToolkit.opdSearch.client;

import aarogya.selfToolkit.opdSearch.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
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
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class OPDSearch_Dsg {

    final static DmLabel lblWidgetName = new DmLabel("");
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);

    public OPDSearch_Dsg() {
        ini();
        iniRegistrationSearch();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1000px");
        popupPanel.setHeight("385px");

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

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        btnCancel.setTitle("Close");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(lblWidgetName.getText());
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "355px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.8%", "20px", "left", null, DmPosition.NONE, true);
        txtSearchValue.getElement().setPropertyString("placeholder", "Search Value");
        flwTr.add(vl.add(txtSearchValue));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "335px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(registrationSearch));
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd OPD Search">
    interface RegistrationSearch_PrpAcs extends KeyField_T_PrpAcs<GrdRegistration_Mdl> {

        @Editor.Path("RegDate")
        ValueProvider<GrdRegistration_Mdl, String> date();

        @Editor.Path("UhidNo")
        ValueProvider<GrdRegistration_Mdl, String> uhidNo();

        @Editor.Path("PatientName")
        ValueProvider<GrdRegistration_Mdl, String> patientName();

        @Editor.Path("GuardianName")
        ValueProvider<GrdRegistration_Mdl, String> guardianName();

        @Editor.Path("Age")
        ValueProvider<GrdRegistration_Mdl, String> age();

        @Editor.Path("Gender")
        ValueProvider<GrdRegistration_Mdl, String> gender();

        @Editor.Path("Mobile")
        ValueProvider<GrdRegistration_Mdl, String> mobile();

        @Editor.Path("ConsultantName")
        ValueProvider<GrdRegistration_Mdl, String> consultantName();

        @Editor.Path("OrganizationName")
        ValueProvider<GrdRegistration_Mdl, String> organizationName();
    }
    final DmGridPaging<GrdRegistration_Mdl, RegistrationSearch_PrpAcs, SearchValue_Mdl> registrationSearch = new DmGridPaging(GWT.create(RegistrationSearch_PrpAcs.class), 14);

    private void iniRegistrationSearch() {
        DmColumnConfig<GrdRegistration_Mdl, String> colDateTime, colAgeGender;
        registrationSearch.getDmGrid().addTextColumn(registrationSearch.getDmGrid().getPropertyAccess().name(), "Reg No.", 110, "opdNo");
        registrationSearch.getDmGrid().addTextColumn(registrationSearch.getDmGrid().getPropertyAccess().uhidNo(), "UHID No.", 110, "uhidNo");
        registrationSearch.getDmGrid().addTextColumn(registrationSearch.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 110, "patientName");
        registrationSearch.getDmGrid().addTextColumn(registrationSearch.getDmGrid().getPropertyAccess().guardianName(), "Guardian Name", 100, "guardianName");
        colAgeGender = registrationSearch.getDmGrid().addTextColumn(registrationSearch.getDmGrid().getPropertyAccess().age(), "Age/Gender", 100, "age");
        registrationSearch.getDmGrid().addTextColumn(registrationSearch.getDmGrid().getPropertyAccess().mobile(), "Mobile", 70, "mobile");
        registrationSearch.getDmGrid().addTextColumn(registrationSearch.getDmGrid().getPropertyAccess().organizationName(), "Organization", 100, "organizationName");
        registrationSearch.getDmGrid().addTextColumn(registrationSearch.getDmGrid().getPropertyAccess().consultantName(), "Consultant", 130, "consultantName");
        colDateTime = registrationSearch.getDmGrid().addTextColumn(registrationSearch.getDmGrid().getPropertyAccess().date(), "Reg Date & Time", 100, "v_Date");
        registrationSearch.getDmGrid().initializeGrid(registrationSearch.getDmGrid().getPropertyAccess().code(), 980, 325, 3);

        registrationSearch.getHelper().addSort("v_Date", DmSortType.DESC);
        registrationSearch.getHelper().addSort("regTime", DmSortType.DESC);
        registrationSearch.getHelper().addSort("short_Name", DmSortType.ASC, "short_Name");
        registrationSearch.getHelper().addSort("v_No", DmSortType.ASC, "v_No");
        registrationSearch.getHelper().addSort("opdNo", DmSortType.ASC);

        colDateTime.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                StringBuilder cellValue = new StringBuilder("");
                cellValue.append("<div style='width:65%;float:left;'>");
                cellValue.append(DmShared.dNull(registrationSearch.getDmGrid().getGrid().getStore().get(context.getIndex()).getRegDate()));
                cellValue.append("</div>");
                cellValue.append("<div style='width:30%;float:left;margin-left: 5%'>");
                cellValue.append(DmClient.vNull(registrationSearch.getDmGrid().getGrid().getStore().get(context.getIndex()).getRegTime(), "00.00"));
                cellValue.append("</div>");
                sb.appendHtmlConstant(cellValue.toString());
            }
        });

        colAgeGender.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                StringBuilder cellValue = new StringBuilder("");
                cellValue.append("<div style='width:45%;float:left;'>");
                cellValue.append(DmShared.xNull(registrationSearch.getDmGrid().getGrid().getStore().get(context.getIndex()).getAge()));
                cellValue.append("</div>");
                cellValue.append("<div style='width:50%;float:left;margin-left: 5%'>");
                cellValue.append(DmShared.xNull(registrationSearch.getDmGrid().getGrid().getStore().get(context.getIndex()).getGender()));
                cellValue.append("</div>");
                sb.appendHtmlConstant(cellValue.toString());
            }
        });
    }
    //</editor-fold>
}
