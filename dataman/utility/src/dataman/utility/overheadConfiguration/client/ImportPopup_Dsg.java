package dataman.utility.overheadConfiguration.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class ImportPopup_Dsg implements IFileUploadNotifier {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnYes = new Button();
    final Button btnNo = new Button();
    DmFileUploadButton btnUpload;
    DmFileRuleType fileRuleType;
    final DmLabel lblFileName = new DmLabel("");
    final DmLabel lblMessage = new DmLabel("");
    Style style;

    public ImportPopup_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("350px");
        popupPanel.setHeight("110px");
        iniToolControls();
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolControls() {
        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        flwCancel.add(btnCancel);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "70px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "80%", "70px", "left", null, DmPosition.NONE, true);
        flwTr.add(rightArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "20%", "70px", "left", null, DmPosition.NONE, true);
        flwTr.add(leftArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "70px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", "", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("File Name");
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", "", "left", null, DmPosition.NONE, true);
        flwTd.add(lblFileName);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "45px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", "", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Message");
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", "", "left", null, DmPosition.NONE, true);
        flwTd.add(lblMessage);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        InlineLabel label;
        Anchor aBtn;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "70px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "25px", "left", "relative", DmPosition.NONE, true);
        label = new InlineLabel("Choose File");
        label.getElement().setAttribute("style", label.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;width:70px;");
        fileRuleType = new DmFileRuleType();
        fileRuleType.setFilters("json");
        fileRuleType.setFileSizeLimit("1024");
        btnUpload = new DmFileUploadButton(this, fileRuleType);

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-envelope-open-o");
        aBtn.getElement().getStyle().setFloat(Style.Float.LEFT);
        btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        btnUpload.getButton().getElement().appendChild(label.getElement());
        btnUpload.getButton().setSize("70px", "24px");
        btnUpload.getButton().getElement().getStyle().setPadding(0, Style.Unit.PX);
        btnUpload.addStyleName("dm-button-grad");
        flwTd.add(btnUpload);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "40px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "46%", "", "left", null, DmPosition.NONE, true);
        flwTd.add(btnYes);
        label = new InlineLabel("Yes");
        label.getElement().setAttribute("style", label.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;width:33px;");
        btnYes.addStyleName("dm-button-grad");
        btnYes.getElement().appendChild(label.getElement());
        btnYes.setHeight("24px");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "46%", "", "left", null, DmPosition.NONE, true);
        flwTd.add(btnNo);
        flwTd.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        label = new InlineLabel("No");
        label.getElement().setAttribute("style", label.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;width:33px;");
        btnNo.addStyleName("dm-button-grad");
        btnNo.getElement().appendChild(label.getElement());
        btnNo.setHeight("24px");
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
