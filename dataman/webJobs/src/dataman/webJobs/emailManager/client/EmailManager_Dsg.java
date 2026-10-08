package dataman.webJobs.emailManager.client;

import dataman.libraryMenu.self.shared.lmWebJobs;
import dataman.webJobs.emailManager.shared.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class EmailManager_Dsg extends IsWidgetExtended implements IFileUploadNotifier {

    final String widgetName = new lmWebJobs().EMAIL_MANAGER.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final Button btnSend = new Button();
    final Button btnClose = new Button();
    final Button btnFilter = new Button();
    final Button btnDraft = new Button();
    final Button btnClearFilter = new Button();
    final Button btnClearAttachment = new Button();

    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwGrdDynamic = new FlowPanel();
    final FlowPanel flwTdcheck = new FlowPanel();

    final DmTextBox txtDataSourceName = new DmTextBox(30, true);
    final DmTextBox txtFunFile = new DmTextBox(30, true);
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final DmTextBox txtUploadFile = new DmTextBox(30, true);
    final DmTextBox txtTemplateName = new DmTextBox(50, false);
    DmFileUploadButton btnUpload;

    final FlowPanel flwChkRecords = new FlowPanel();
    final DmCheckBox chkRecordsFilter = new DmCheckBox();

    final DmLabel lblDraftedRecords = new DmLabel("");
    final DmLabel lblFilteredRecords = new DmLabel("");
    final DmLabel lblSelectedRecords = new DmLabel("");
    final DmLabel lblSentRecords = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="Grid">
    DmGridPaging<DmDynamicGridRow, String, SearchValue_Mdl> dmEmailGrd = new DmGridPaging<>(null, 16, true);
    //</editor-fold>

    public EmailManager_Dsg() {
        ini();

    }

    private void ini() {
        final FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("498px");
        vrtInnerForm.add(toolContainer);
        iniToolContainer();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "470px", null, null, DmPosition.CENTER, true);

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "55px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        //Find And Grid
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "415px", "left", null, DmPosition.NONE, true);
        flwTr.add(findArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "43px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Template Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTemplateName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(vl.add(lblTemp));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Drafted Records : ");
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblTemp.getElement().getStyle().setFontSize(13, Style.Unit.PX);
        flwTd.add(vl.add(lblTemp));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(lblDraftedRecords));
        lblDraftedRecords.getElement().getStyle().setMarginTop(6, Style.Unit.PCT);
        lblDraftedRecords.getElement().getStyle().setFontSize(11.5, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Filtered Records : ");
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblTemp.getElement().getStyle().setFontSize(13, Style.Unit.PX);
        flwTd.add(vl.add(lblTemp));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(lblFilteredRecords));
        lblFilteredRecords.getElement().getStyle().setMarginTop(6, Style.Unit.PCT);
        lblFilteredRecords.getElement().getStyle().setFontSize(11.5, Style.Unit.PX);
        lblFilteredRecords.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2ndt Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Data Source / Function / File Name");
        lblTemp.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDataSourceName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFunFile));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(vl.add(lblTemp));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Selected Records : ");
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblTemp.getElement().getStyle().setFontSize(13, Style.Unit.PX);
        flwTd.add(vl.add(lblTemp));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(lblSelectedRecords));
        lblSelectedRecords.getElement().getStyle().setMarginTop(6, Style.Unit.PCT);
        lblSelectedRecords.getElement().getStyle().setFontSize(11.5, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Send Records : ");
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblTemp.getElement().getStyle().setFontSize(13, Style.Unit.PX);
        flwTd.add(vl.add(lblTemp));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(lblSentRecords));
        lblSentRecords.getElement().getStyle().setMarginTop(6, Style.Unit.PCT);
        lblSentRecords.getElement().getStyle().setFontSize(11.5, Style.Unit.PX);
        lblSentRecords.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel findArea() {
        FlowPanel flwTable, flwTr, flwTd;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "415px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.5%", "20px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40.3%", null, "left", null, DmPosition.NONE, false);
        txtSearchValue.getElement().setPropertyString("placeholder", "Search Value");
        flwTd.add(vl.add(txtSearchValue));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "56.1%", null, "left", null, DmPosition.NONE, false);
        txtUploadFile.setEnabled(false);
        txtUploadFile.getElement().setPropertyString("placeholder", "Attachment");
        flwTd.add(vl.add(txtUploadFile));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-paperclip");
        aBtn.getElement().getStyle().setColor("black");
        DmFileRuleType fileRuleType = new DmFileRuleType();
        fileRuleType.setFilters("txt,rtf,doc,docx,xlsx,xls,pdf,gif,jpeg,jpg,png,rar,zip,mp3,mp4,flv,mkv");
        fileRuleType.setFileSizeLimit("10240");
        btnUpload = new DmFileUploadButton(this, fileRuleType);
        btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        btnUpload.getButton().getElement().getStyle().setBorderStyle(Style.BorderStyle.NONE);

        btnUpload.setTitle("Attachment");
        btnUpload.setSize("22px", "21px");

        flwTd.add(vl.add(btnUpload));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmPosition.NONE, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("red");
        btnClearAttachment.getElement().appendChild(aBtn.getElement());
//        btnClearAttachment.getElement().getStyle().setBorderStyle(Style.BorderStyle.NONE);
        btnClearAttachment.setTitle("Clear Attachment");
        btnClearAttachment.setSize("22px", "21px");
        flwTd.add(vl.add(btnClearAttachment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "390px", "left", null, DmPosition.NONE, false);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top: 1px solid gray;");
        DmClient.setStyle(flwGrdDynamic, "100%", "390px", "left", null, DmPosition.NONE, true);
        flwTr.add(flwGrdDynamic);
        flwTable.add(flwTr);

        return flwTable;
    }

    private DmToolContainer iniToolContainer() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Send");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-send-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSend.setTitle("Send");
        btnSend.addStyleName("dm-top-control-btnStyle");
        btnSend.getElement().appendChild(aBtn.getElement());
        btnSend.getElement().appendChild(label.getElement());
        btnSend.setWidth("70px");

        label = new InlineLabel("Filter");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFilter.setTitle("Filter");
        btnFilter.addStyleName("dm-top-control-btnStyle");
        btnFilter.getElement().appendChild(aBtn.getElement());
        btnFilter.getElement().appendChild(label.getElement());
        btnFilter.setWidth("70px");

        label = new InlineLabel("Clear Filter");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-eraser");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClearFilter.setTitle("Clear Filter");
        btnClearFilter.addStyleName("dm-top-control-btnStyle");
        btnClearFilter.getElement().appendChild(aBtn.getElement());
        btnClearFilter.getElement().appendChild(label.getElement());
        btnClearFilter.setWidth("79px");

        label = new InlineLabel("Draft");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-table");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDraft.setTitle("Draft");
        btnDraft.addStyleName("dm-top-control-btnStyle");
        btnDraft.getElement().appendChild(aBtn.getElement());
        btnDraft.getElement().appendChild(label.getElement());
        btnDraft.setWidth("70px");

        label = new InlineLabel("Close");
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        label = new InlineLabel("Show Selected Records Only");
        label.setWidth("90px");
        flwChkRecords.add(label);
        flwChkRecords.getElement().setAttribute("style", "min-width:191px;padding:6px");
        chkRecordsFilter.getElement().setAttribute("style", "margin-right:6px;margin-top:-2px");
        flwChkRecords.add(chkRecordsFilter);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(btnDraft);
        toolContainer.addWidget(btnFilter);
        toolContainer.addWidget(btnClearFilter);
        toolContainer.addWidget(btnSend);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        toolContainer.addWidget(flwChkRecords);
//        toolContainer.addGap();

        return toolContainer;
    }
}
