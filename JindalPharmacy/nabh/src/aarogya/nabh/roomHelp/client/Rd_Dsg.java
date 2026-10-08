package aarogya.nabh.roomHelp.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;

abstract class Rd_Dsg {

    public final String widgetName = "";
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";

    public final static String deleteFileForPreAnesBlog = "Delete Files";
    public final static String deleteFileForPostAnesBlog = "Delete Files";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final FlowPanel flwSave = new FlowPanel();
    final Button btnSave = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final FlowPanel flwManageFile = new FlowPanel();
    final Button btnClose = new Button();
    final Button btnManageFile = new Button();
    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtVoucherNo = new DmTextBox();
    final DmTextBox txtGUIName = new DmTextBox(150, false);
    final DmTextBox txtRefNo = new DmTextBox();
    final DmTextBox txtVoucherDate = new DmTextBox(150, false);
    final DmTextBox txtVoucherTime = new DmTextBox(150, false);
    final DmTextBox txtVoucherType = new DmTextBox(150, false);

    final Button btnPrint = new Button();
    final FlowPanel flwPrint = new FlowPanel();

    final DmGUI gui = new DmGUI(new DmTopControl(vl, userControl), 372);
    final DmLabel cancelBy = new DmLabel("");

    public Rd_Dsg() {
        ini();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight("500px");
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmUserControl createBottom() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);

        userControl.addGeneralBlog(cancelBy);
        cancelBy.setHeight("15Px");
        cancelBy.getElement().getStyle().setColor("red");
        cancelBy.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        cancelBy.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        userControl.addGap(100);

        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        return userControl;

    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("black");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("55px");
        flwSave.add(btnSave);

        label = new InlineLabel("Discard");
        label.setWidth("41px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o fa fa-ban");
        aBtn.getElement().getStyle().setColor("red");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("64px");
        flwDelete.add(btnDelete);

        label = new InlineLabel("Manage File");
        label.setWidth("62px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-paperclip");
        aBtn.getElement().getStyle().setColor("black");
        btnManageFile.getElement().appendChild(aBtn.getElement());
        btnManageFile.getElement().appendChild(label.getElement());
        btnManageFile.setWidth("83px");
        flwManageFile.add(btnManageFile);

        label = new InlineLabel("Print");
        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("tomato");
        btnPrint.setTitle("Print: (Ctrl + P)");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("55px");
        flwPrint.add(btnPrint);

        label = new InlineLabel("Close");
        label.setWidth("35px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("red");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("60px");
        flwClose.add(btnClose);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "434px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "border-bottom: 1px solid gray;");
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "30px", "left", null, DmPosition.NONE, false);;
        flwTd.add(topSection());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "404px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "404px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(prescriptionArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topSection() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GUI Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGUIName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref. No.");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11.2%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(26, Style.Unit.PCT);
        lbl = new DmLabel("Date" + " / " + "Time");
        lbl.getElement().getStyle().setMarginLeft(17, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);

        hrtPnl.add(vl.add(txtVoucherDate));
        hrtPnl.setCellWidth(txtVoucherDate, "70%");

        hrtPnl.add(vl.add(txtVoucherTime));
        hrtPnl.setCellWidth(txtVoucherTime, "30%");

        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel prescriptionArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();

        DmClient.setStyle(flwTable, "100%", "382px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "382px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(gui));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
