package aarogya.master.importData.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class ImportDataGridEdit_Dsg implements IFileUploadNotifier {

    public final static String widgetName = "Import Data Popup";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final FlowPanel flwDiscard = new FlowPanel();
    final Button btnDiscard = new Button();

    final DmTextBox txtImportTo = new DmTextBox(50, false);
    final DmTextBox txtImportFrom = new DmTextBox(50, false);
    final DmTextArea txtLog = new DmTextArea();
    final DmTextBox txtFileFormat = new DmTextBox(500, false);

    final DmFileRuleType fileRuleType = new DmFileRuleType();
    final DmFileUploadButton btnUpload = new DmFileUploadButton(this, fileRuleType);

    final FlowPanel flwBtnUpload = new FlowPanel();
    final DmLabel labelLogFile = new DmLabel("");

    final FlowPanel flwBtnDownload = new FlowPanel();
    final Button btnDownload = new Button();

    public ImportDataGridEdit_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("725px");
        popupPanel.setHeight("450px");

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
        flwClose.add(vl.add(btnClose));

        label = new InlineLabel("Discard");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDiscard.setTitle("Discard");
        btnDiscard.addStyleName("dm-top-control-btnStyle");
        btnDiscard.getElement().appendChild(aBtn.getElement());
        btnDiscard.getElement().appendChild(label.getElement());
        btnDiscard.setWidth("70px");
        flwDiscard.add(vl.add(btnDiscard));

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwDiscard);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "420px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Import To");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "37%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtImportTo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Import From");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "37%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtImportFrom));
        hrtPnl.setCellWidth(txtImportFrom, "100%");
        DmClient.setStyle(flwBtnUpload, "100%", "20px", "left", null, DmPosition.NONE, true);

        fileRuleType.setFilters("csv");
        fileRuleType.setFileSizeLimit("102400");

        aBtn = new Anchor();
        aBtn.addStyleName("fa  fa-paperclip");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        btnUpload.addStyleName("dm-button-grad");
        btnUpload.getButton().setSize("25px", "20px");
        flwBtnUpload.add(vl.add(btnUpload));
        hrtPnl.add(flwBtnUpload);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("File Format");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "89%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtFileFormat));
        hrtPnl.setCellWidth(txtFileFormat, "100%");
        DmClient.setStyle(flwBtnDownload, "100%", "20px", "left", null, DmPosition.NONE, true);
        aBtn = new Anchor();
        aBtn.addStyleName("fa  fa-download");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDownload.addStyleName("dm-button-grad");
        btnDownload.getElement().appendChild(aBtn.getElement());
        btnDownload.setSize("25px", "20px");
        flwBtnDownload.add(vl.add(btnDownload));
        hrtPnl.add(flwBtnDownload);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "20px", "left", null, DmPosition.NONE, false);

        flwTd.add(labelLogFile);
        labelLogFile.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        labelLogFile.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        labelLogFile.getElement().getStyle().setHeight(15, Style.Unit.PX);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLog));
        txtLog.setHeight("354px");
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

}
