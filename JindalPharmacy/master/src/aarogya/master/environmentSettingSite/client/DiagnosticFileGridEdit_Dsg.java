package aarogya.master.environmentSettingSite.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public abstract class DiagnosticFileGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextBox txtNature = new DmTextBox(10, false);
    final DmTextBox txtUploadedFileName = new DmTextBox(10, false);
    DmFileRuleType fileRuleTypeDocument;
    DmFileUploadButton btnUploadDocument;

    final FlowPanel flwUploadDocument = new FlowPanel();

    final Button btnDownloadDocument = new Button("Download Radiology Header Document");
    final FlowPanel flwDownloadDocument = new FlowPanel();

    public DiagnosticFileGridEdit_Dsg(DmFileRuleType fileRuleTypeDocument, DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        this.fileRuleTypeDocument = fileRuleTypeDocument;
        container.setStyleName("dm-FlwWorkingArea");
        container.add(workingArea());
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;
        InlineLabel label;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "70px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNature));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Uploded File Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUploadedFileName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwUploadDocument, "50%", "25px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", "25px", "left", "relative", DmPosition.NONE, true);
        label = new InlineLabel("Upload Radiology Header Document");
        label.getElement().setAttribute("style", label.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;width:78px;");
//        fileRuleTypeDocument = new DmFileRuleType();
//        fileRuleTypeDocument.setRuleCode(ProjectRuleCode.CAMERA.getRuleCode());
//        fileRuleTypeDocument.setFileSizeLimit("5120");
//        fileRuleTypeDocument.setFilters("docx");
//        fileRuleTypeDocument.setFileTypeName("Report");
        btnUploadDocument = new DmFileUploadButton(null, fileRuleTypeDocument);
        aBtn = new Anchor();

        aBtn.getElement().getStyle().setFloat(Style.Float.LEFT);
        btnUploadDocument.getButton().addStyleName("dm-button-grad");
        btnUploadDocument.getButton().getElement().appendChild(aBtn.getElement());
        btnUploadDocument.getButton().getElement().appendChild(label.getElement());
        btnUploadDocument.getButton().setSize("215px", "24px");
        btnUploadDocument.setTitle("File Type: DGRAD");
        btnUploadDocument.getButton().getElement().getStyle().setPadding(0, Style.Unit.PX);
        flwUploadDocument.add(vl.add(btnUploadDocument));
        flwTd.add(flwUploadDocument);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", null, "left", null, DmPosition.NONE, false);
        btnDownloadDocument.addStyleName("dm-button-grad");
        btnDownloadDocument.setTitle("File Type: DGRAD");
        flwDownloadDocument.add(btnDownloadDocument);
        flwTd.add(flwDownloadDocument);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
