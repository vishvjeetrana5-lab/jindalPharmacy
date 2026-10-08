package dataman.webJobs.emailTemplateMast.client;

import dataman.libraryMenu.self.shared.lmWebJobs;
import dataman.webJobs.emailTemplateMast.shared.*;
import dataman.dmBase.self.shared.*;
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

abstract class EmailTemplateMast_Dsg extends IsWidgetExtended implements IFileUploadNotifier {

    final String widgetName = new lmWebJobs().EMAIL_TEMPLATE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    DmFileUploadButton btnUpload;
    final DmSuggestBox txtTempName = new DmSuggestBox(100, 7, true, "name", DmDataType.STRING);
    final Button btnGrd = new Button();
    final PopupPanel pnlGrd = new PopupPanel(true);
    final PopupPanel pnlGrd1 = new PopupPanel(true);
    final DmTextBox txtSubject = new DmTextBox(250, true);
    final DmTextBox txtCSVFunction = new DmTextBox();
    final FlowPanel flwFunction = new FlowPanel();
    final DmTextArea txtMsg = new DmTextArea();
    final Button btnHelp = new Button();
    final DmTextArea txtHelp = new DmTextArea();
    final DmLabel lblFn = new DmLabel("Function Name");
    final HorizontalPanel hrtPnlCSV = new HorizontalPanel();
    final FlowPanel flwStaffInfo = new FlowPanel();
    final Button btnStaffInfo = new Button("Staff Info");

    final Menu menu = new Menu();
    MenuItem mnuExportData, mnuImportData, mnuManageFile;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="TxtHTemplateGroupName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTemplateGroupName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHUndTempName() {
        txtHTemplateGroupName.getDmGrid().addTextColumn(txtHTemplateGroupName.getDmGrid().getPropertyAccess().name(), "Under Template Name", 0, "name");
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
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 19);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Template Name", 200, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().templateGroupName(), "Template Group Name", 200, "templateGroupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dataSource(), "Data Source", 150, "dataSource");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().functionName(), "Function Name", 150, "functionName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().message(), "Message", 200, "message");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 2);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="File GRid">
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

        grdFieldDetail.initializeGrid(grdFieldDetail.getPropertyAccess().fieldHashCode(), 490, 191, 2);

        return grdFieldDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Email GRid">
    interface EmailGrid_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdEmail_Mdl> {

        @Editor.Path("ProcName")
        ValueProvider<GrdEmail_Mdl, String> procName();

        @Editor.Path("RptName")
        ValueProvider<GrdEmail_Mdl, String> rptName();

        @Editor.Path("FileName")
        ValueProvider<GrdEmail_Mdl, String> fileName();

    }

    final DmGrid<GrdEmail_Mdl, EmailGrid_PrpAcs> grdEmailDetail = new DmGrid(GWT.create(EmailGrid_PrpAcs.class));

    private Grid iniEmailGrid() {
        grdEmailDetail.addRowNumberColumn();
        grdEmailDetail.addTextColumn(grdEmailDetail.getPropertyAccess().procName(), "Procedure Name", 100, null);
        grdEmailDetail.addTextColumn(grdEmailDetail.getPropertyAccess().rptName(), "RPT Name", 80, null);
        grdEmailDetail.addTextColumn(grdEmailDetail.getPropertyAccess().fileName(), "File Name", 80, null);

        grdEmailDetail.initializeGrid(grdEmailDetail.getPropertyAccess().fieldHashCode(), 300, 84, 2);

        return grdEmailDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Image Detail">
    interface ImageGrid_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdImage_Mdl> {

        @Editor.Path("ImageCode")
        ValueProvider<GrdImage_Mdl, String> imageCode();

        @Editor.Path("RuleCode")
        ValueProvider<GrdImage_Mdl, String> ruleCode();

    }

    final DmGrid<GrdImage_Mdl, ImageGrid_PrpAcs> grdImageDetail = new DmGrid(GWT.create(ImageGrid_PrpAcs.class));

    private Grid iniImageGrid() {
        grdImageDetail.addRowNumberColumn();
        grdImageDetail.addTextColumn(grdImageDetail.getPropertyAccess().imageCode(), "Image Code", 1, "");
        grdImageDetail.addTextColumn(grdImageDetail.getPropertyAccess().ruleCode(), "Rule Code", 75, "");
        grdImageDetail.initializeGrid(grdImageDetail.getPropertyAccess().fieldHashCode(), 180, 84, 2);

        return grdImageDetail.getGrid();
    }
    //</editor-fold>

    public EmailTemplateMast_Dsg() {

        DmFileRuleType fileRuleType = new DmFileRuleType();
        fileRuleType.setFilters("csv");
        fileRuleType.setFileSizeLimit("10240");
        btnUpload = new DmFileUploadButton(this, fileRuleType);
        ini();
        pnlGrd.add(iniFieldGrid());
        pnlGrd.setWidth("490px");
        pnlGrd.setHeight("191px");
        pnlGrd.hide();

        pnlGrd1.setWidth("515px");
        pnlGrd1.setHeight("250px");
        pnlGrd1.hide();

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

        mnuManageFile = new MenuItem("Manage File", selectionHandler);
        menu.add(mnuManageFile);

        return menu;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "405px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "87px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "85px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        /*====================================< Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", "85px", "left", null, DmPosition.NONE, true);
        flwTd.add(iniEmailGrid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", "85px", "left", null, DmPosition.NONE, true);
        flwTd.add(iniImageGrid());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "23px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12.75%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Subject");
        lbl.getElement().getStyle().setMarginLeft(4, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "86.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSubject));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "298px", "left", null, DmPosition.NONE, true);
        flwTr.add(bottomArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        HorizontalPanel hrtPnl = new HorizontalPanel();
        DmLabel lbl;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "85px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Template Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTempName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Group Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTemplateGroupName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Data Source");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        aBtn = new Anchor();
        aBtn.setStyleName("fa fa-question-circle");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnHelp.setTitle("Help");
        btnHelp.addStyleName("dm-button-grad");
        btnHelp.getElement().appendChild(aBtn.getElement());
        btnHelp.getElement().getStyle().setWidth(24, Style.Unit.PX);
        btnHelp.getElement().getStyle().setHeight(20, Style.Unit.PX);
        hrtPnl.add(btnHelp);
        hrtPnl.add(vl.add(txtHDataSource));
        hrtPnl.setCellWidth(txtHDataSource, "100%");
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblFn);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwFunction, "100%", null, "left", null, DmPosition.NONE, true);
        flwFunction.add(vl.add(txtHFunctionName));
        flwTd.add(vl.add(flwFunction));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel bottomArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "298px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", "25px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(btnGrd));
        btnGrd.setHeight("24px");
        btnGrd.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        btnGrd.setWidth("24px");
        btnGrd.getElement().setAttribute("style", btnGrd.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnGrd.setText("...");
        btnGrd.addStyleName("dm-button-grad");
        btnGrd.setTitle("Formula");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", "25px", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Message");
        DmClient.setStyle(lbl, "100%", "25px", null, null, DmPosition.NONE, true);
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99.9%", null, "left", null, DmPosition.NONE, false);
        txtMsg.setHeight("269px");
        flwTd.add(vl.add(txtMsg));
        flwTr.add(flwTd);

        DmClient.setStyle(hrtPnlCSV, "100%", "100%", null, null, DmPosition.NONE, true);
        txtCSVFunction.setMandatory(true);
        txtCSVFunction.setEnabled(false);
        hrtPnlCSV.add(txtCSVFunction);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-paperclip");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        btnUpload.getButton().setSize("25px", "22px");
        btnUpload.addStyleName("dm-button-grad");
        hrtPnlCSV.add(btnUpload);
        hrtPnlCSV.setCellWidth(txtCSVFunction, "100%");
        txtCSVFunction.getElement().getStyle().setHeight(18, Style.Unit.PX);

        flwTable.add(flwTr);

        return flwTable;
    }

}
