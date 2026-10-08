package dataman.payroll.leaveEntryApproval.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;
import dataman.payroll.leaveEntryApproval.shared.GrdLeaveEntryApproval_Mdl;
import dataman.payroll.leaveEntryApproval.shared.Search_Mdl;
import dataman.payroll.self.shared.PREnum;

public abstract class LeaveEntryApproval_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().LEAVE_ENTRY_APPROVAL.caption;

    public final static String saveLeaveStatus = "Leave Status";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwSearch = new FlowPanel();

    final Button btnCancel = new Button();

    final Button btnFilter = new Button();
    final Button btnRemoveFilter = new Button();

    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final DmLabel lblOrganization = new DmLabel("");

    int colLeaveStatus;

    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLeaveEntryApproval_Mdl> {

        @Editor.Path("EntryNo")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> entryNo();

        @Editor.Path("EmployeeName")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> employeeName();

        @Editor.Path("Status")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> status();

        @Editor.Path("ApplyDate")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> applyDate();

        @Editor.Path("FromDate")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> toDate();

        @Editor.Path("LeaveName")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> leaveName();

        @Editor.Path("Duration")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> duration();

        @Editor.Path("Reason")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> reason();

//        @Editor.Path("ApprovedBy")
//        ValueProvider<GrdLeaveEntryApproval_Mdl, String> approvedBy();
        @Editor.Path("Department")
        ValueProvider<GrdLeaveEntryApproval_Mdl, String> department();

    }
    final DmGridPaging<GrdLeaveEntryApproval_Mdl, Grd_PrpAcs, Search_Mdl> dmGrd = new DmGridPaging(GWT.create(Grd_PrpAcs.class), 23, false);

    private void iniGrd() {

        int index = 0;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().entryNo(), "Entry No", 80, "EntryNo");
        index++;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().employeeName(), "Employee Name", 100, "EmployeeName");
        index++;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().applyDate(), "Apply Date", 100, "applyDate");
        index++;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().fromDate(), "From Date", 100, "fromDate");
        index++;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().toDate(), "To Date", 100, "toDate");
        index++;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().leaveName(), "Leave Name", 100, "LeaveNature");
        index++;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().duration(), "Duration", 100, "duration");
        index++;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().reason(), "Reason", 250, "reason");
        index++;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().department(), "Department", 100, "Department");
        index++;
        colLeaveStatus = index;
        dmGrd.getDmGrid().addTextColumn(dmGrd.getDmGrid().getPropertyAccess().status(), "Leave Status", 100, "isStatus");

        dmGrd.getDmGrid().initializeGrid(dmGrd.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 500, 2);

        dmGrd.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdLeaveEntryApproval_Mdl>() {

            @Override
            public String getColStyle(GrdLeaveEntryApproval_Mdl model, ValueProvider<? super GrdLeaveEntryApproval_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String style = "dm-GridCellFormat ";

                if (Integer.valueOf(model.getStatusCode()) == PREnum.LEAVE_STATUS.APPROVED.getId()) {

                    style = " cpoeOPD-Genearal";

                } else if (Integer.valueOf(model.getStatusCode()) == PREnum.LEAVE_STATUS.CANCELED.getId()) {
                    style = "dm-GridCellDairyCancel ";
                }

                return style;
            }

            @Override
            public String getRowStyle(GrdLeaveEntryApproval_Mdl model, int rowIndex) {
                return "";
            }
        });

        dmGrd.getHelper().addSort("applyDate", DmSortType.DESC);
        dmGrd.getHelper().addSort("EmployeeName", DmSortType.ASC);

    }

    public LeaveEntryApproval_Dsg() {
        ini();

        iniGrd();
    }

    private void ini() {

        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("590px");

        vrtInnerForm.add(iniToolContorls());

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        // aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");

        flwCancel.add(btnCancel);

        txtSearchValue.setWidth("350px");

        label = new InlineLabel("Filter");
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        // aBtn.getElement().getStyle().setColor("green");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFilter.addStyleName("dm-top-control-btnStyle");
        btnFilter.setTitle("Filter");
        btnFilter.getElement().appendChild(aBtn.getElement());
        btnFilter.getElement().appendChild(label.getElement());
        btnFilter.setWidth("70px");

        toolContainer.addWidget(txtSearchValue);
        toolContainer.addWidget(btnFilter);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwCancel);

        lblOrganization.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblOrganization.setHeight("20px");
        lblOrganization.setWidth("270px");
        lblOrganization.getElement().setAttribute("style", lblOrganization.getElement().getAttribute("style") + " line-height: 20px !important; font-size: 16px;");
        toolContainer.addWidget(lblOrganization);

        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "563px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "470px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(dmGrd));
        flwTable.add(flwTr);

        return flwTable;

    }

}
