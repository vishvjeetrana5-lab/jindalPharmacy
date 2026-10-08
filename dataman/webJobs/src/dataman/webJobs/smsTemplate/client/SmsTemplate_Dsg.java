package dataman.webJobs.smsTemplate.client;

import dataman.libraryMenu.self.shared.lmWebJobs;
import dataman.webJobs.smsTemplate.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

abstract class SmsTemplate_Dsg extends IsWidgetExtended implements IFileUploadNotifier {

    final String widgetName = new lmWebJobs().SMS_TEMPLATE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    DmFileUploadButton btnUpload;
    final PopupPanel pnlGrd1 = new PopupPanel(true);

    final DmTextArea txtHelp = new DmTextArea();
    final Button btnHelp = new Button();

    final DmSuggestBox txtTempName = new DmSuggestBox(100, 7, true, "name", DmDataType.STRING);
    final DmTextBox txtRefV_Type = new DmTextBox(11, false);
    final DmTextArea txtMsg = new DmTextArea(500, false);
    final DmTextBox txtCSVFunction = new DmTextBox();
    final DmTextBox txtTemplateId = new DmTextBox(30, false);
    final FlowPanel flwFunction = new FlowPanel();
    final DmLabel lblFn = new DmLabel("Function Name");
    final HorizontalPanel hrtPnlCSV = new HorizontalPanel();
    final FlowPanel flwStaffInfo = new FlowPanel();
    final Button btnStaffInfo = new Button("Staff Info");

    final Menu menu = new Menu();
    MenuItem mnuExportData, mnuImportData;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    Style stl;
    //<editor-fold defaultstate="collapsed" desc="TxtHTempGroupName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTemplateGroupName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHUndTempName() {
        txtHTemplateGroupName.getDmGrid().addTextColumn(txtHTemplateGroupName.getDmGrid().getPropertyAccess().name(), "Template Group Name", 0, "name");
        txtHTemplateGroupName.getDmGrid().initializeGrid(txtHTemplateGroupName.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHTemplateGroupName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHFunctionName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFunctionName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHFunctionName() {
        txtHFunctionName.getDmGrid().addTextColumn(txtHFunctionName.getDmGrid().getPropertyAccess().name(), "Function Name", 0, "name");
        txtHFunctionName.getDmGrid().initializeGrid(txtHFunctionName.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHFunctionName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHDataSource">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDataSource = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHDataSource() {
        txtHDataSource.getDmGrid().addTextColumn(txtHDataSource.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHDataSource.getDmGrid().initializeGrid(txtHDataSource.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("TempName")
        ValueProvider<Find_Mdl, String> name();

        @Editor.Path("TemplateGroupName")
        ValueProvider<Find_Mdl, String> templateGroupName();

        @Editor.Path("DataSourceValue")
        ValueProvider<Find_Mdl, String> dataSource();

        @Editor.Path("FunctionName")
        ValueProvider<Find_Mdl, String> functionName();

        @Editor.Path("Msg")
        ValueProvider<Find_Mdl, String> message();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Template Name", 250, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().templateGroupName(), "Template Group Name", 150, "templateGroupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dataSource(), "Data Source", 150, "dataSource");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().functionName(), "Function Name", 150, "functionName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().message(), "Message", 200, "message");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 5);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Field GRid">
    interface FieldGrdDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFieldName_Mdl> {

        @Editor.Path("FieldName")
        ValueProvider<GrdFieldName_Mdl, String> fieldName();

        @Editor.Path("FormattedFieldName")
        ValueProvider<GrdFieldName_Mdl, String> formateField();

    }

    final DmGrid<GrdFieldName_Mdl, FieldGrdDetail_PrpAcs> grdFieldDetail = new DmGrid(GWT.create(FieldGrdDetail_PrpAcs.class));

    private Grid iniFieldGrid() {
        grdFieldDetail.addRowNumberColumn();
        grdFieldDetail.addTextColumn(grdFieldDetail.getPropertyAccess().fieldName(), "Field Name", 200, null);
        grdFieldDetail.addTextColumn(grdFieldDetail.getPropertyAccess().formateField(), "Format", 200, null);

        grdFieldDetail.initializeGrid(grdFieldDetail.getPropertyAccess().fieldHashCode(), 640, 206, 2);

        return grdFieldDetail.getGrid();
    }
    //</editor-fold>

    public SmsTemplate_Dsg() {
        DmFileRuleType fileRuleType = new DmFileRuleType();
        fileRuleType.setFilters("csv");
        fileRuleType.setFileSizeLimit("10240");
        btnUpload = new DmFileUploadButton(this, fileRuleType);
        ini();
        pnlGrd1.setWidth("515px");
        pnlGrd1.setHeight("250px");
        pnlGrd1.hide();
    }

    private Menu iniMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        mnuExportData = new MenuItem("Export Data", selectionHandler);
        menu.add(mnuExportData);

        mnuImportData = new MenuItem("Import Data", selectionHandler);
        menu.add(mnuImportData);

        return menu;
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHUndTempName();
        iniTxtHDataSource();
        iniTxtHFunctionName();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        iniMenu();
        vrtInnerForm.add(flw);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);
        btnStaffInfo.setWidth("110px");
        btnStaffInfo.addStyleName("dm-button-grad");
        flwStaffInfo.add(btnStaffInfo);
        userControl.addGeneralBlog(flwStaffInfo);
        userControl.addGap(50);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "325px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "325px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "325px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftArea());
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "325px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightArea());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "325px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "325px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "110px", "left", null, DmPosition.NONE, true);
        flwTd.add(entryArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "210px", "left", null, DmPosition.NONE, true);
        flwTd.add(iniFieldGrid());
        flwTd.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "325px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", "20px", null, null, DmPosition.CENTER, false);
        lbl = new DmLabel("Message");
        lbl.getElement().getStyle().setHeight(13, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMsg));
        txtMsg.getElement().getStyle().setWidth(635, Style.Unit.PX);
        txtMsg.getElement().getStyle().setHeight(295, Style.Unit.PX);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;

        HorizontalPanel hrtPnl = new HorizontalPanel();
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "90px", null, null, DmPosition.CENTER, true);

        //Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Template Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "63.4%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTempName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //Short Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Group Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "63%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTemplateGroupName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Category Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Data Source");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        aBtn = new Anchor();
        aBtn.setStyleName("fa fa-question-circle");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnHelp.setTitle("Help");
        btnHelp.addStyleName("dm-button-grad");
        btnHelp.getElement().getStyle().setWidth(23, Style.Unit.PX);
        btnHelp.getElement().getStyle().setHeight(20, Style.Unit.PX);
        btnHelp.getElement().appendChild(aBtn.getElement());
        hrtPnl.add((btnHelp));
        hrtPnl.add(vl.add(txtHDataSource));
        hrtPnl.setCellWidth(txtHDataSource, "100%");
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblFn);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "63.4%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwFunction, "100%", null, "left", null, DmPosition.NONE, true);
        flwFunction.add(vl.add(txtHFunctionName));
        flwTd.add(vl.add(flwFunction));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        ////////////////////// Template Id /////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("API Template Id");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "63.4%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTemplateId));
        txtTemplateId.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        txtTemplateId.getElement().setAttribute("style", txtTemplateId.getElement().getAttribute("style") + ";");
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        DmClient.setStyle(hrtPnlCSV, "100%", "100%", null, null, DmPosition.NONE, true);
        txtCSVFunction.setMandatory(true);
        txtCSVFunction.setEnabled(false);
        hrtPnlCSV.add(txtCSVFunction);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-paperclip");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnUpload.addStyleName("dm-button-grad");
        btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        btnUpload.getButton().setSize("25px", "22px");
        hrtPnlCSV.add(btnUpload);
        hrtPnlCSV.setCellWidth(txtCSVFunction, "100%");
        txtCSVFunction.getElement().getStyle().setHeight(18, Style.Unit.PX);

        return flwTable;
    }
}
