package dataman.payroll.attendanceImport.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;

abstract class AttendanceImport_Dsg extends IsWidgetExtended implements IFileUploadNotifier {

    final String widgetName = new lmPayroll().ATTENDANCE_IMPORT.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    public DmTextBox txtFromDate = new DmTextBox(11, true);
    public DmTextBox txtToDate = new DmTextBox(11, true);
    final DmTextBox txtFile = new DmTextBox(200, true);
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    private FlowPanel flwUpload = new FlowPanel();
    private FlowPanel flwImport = new FlowPanel();
    final DmFileRuleType fileRuleType = new DmFileRuleType();
    final DmFileUploadButton btnUpload = new DmFileUploadButton(this, fileRuleType);
    final Button  btnImport = new Button();

    public AttendanceImport_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("200px");

        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "55%", null, null, DmPosition.CENTER, true);

        //FromDate
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "right", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //ToDate
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "right", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //FilePath
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "right", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("File Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtFile));
        hrtPnl.setCellWidth(txtFile, "100%");
        DmClient.setStyle(flwUpload, "100%", "20px", "left", null, DmPosition.NONE, true);

        fileRuleType.setFilters("csv");
        fileRuleType.setFileSizeLimit("102400");

        Anchor aBtn;
        aBtn = new Anchor();
        aBtn.addStyleName("fa  fa-paperclip");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        btnUpload.addStyleName("dm-button-grad");
        btnUpload.getButton().setSize("25px", "20px");
        flwUpload.add(vl.add(btnUpload));
        hrtPnl.add(flwUpload);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
//        InlineLabel label;
//        label = new InlineLabel("Import");
        aBtn = new Anchor();
        aBtn.addStyleName("fa  fa-download");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnImport.addStyleName("dm-button-grad");
        btnImport.getElement().appendChild(aBtn.getElement());
//        btnImport.setTitle("Import");
        
        btnImport.setSize("25px", "20px");
        flwImport.add(vl.add(btnImport));

        hrtPnl.add(flwImport);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl iniUserControl() {
        userControl.addGap(50);
        return userControl;
    }

}
