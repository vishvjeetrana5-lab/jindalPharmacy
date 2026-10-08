package dataman.dmToolkitR.dmReportLayout.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
//import dataman.dmToolkitR.self.client.DmToolkit_Res;
import dataman.dmToolkitR.dmLabel.client.DmLabel;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmToolkitR.dmTextHelp.client.DmTextHelp;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Span;
//import dataman.dmToolkitR.dmTextHelpMulti.client.DmTex1tHelpMulti;
//import dataman.dmToolkit.dmToolContainer.client.DmToolCon1tainer;

public class DmReportLayout_Dsg {

    // final DmToolkit_Res.dmCss dcStyle = DmToolkit_Res.rsObj.dcStyle();
    //  DmClient.ViaList vl = new DmClient.ViaList();
    final public FlowPanel form = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final FlowPanel flwDirectPrint = new FlowPanel();
    //  final DmToolContainer toolContainer = new DmToolContainer();
    final Button buttonFilter = new Button();
    final Button buttonClose = new Button();
    final Button buttonView = new Button();
    final Button buttonExcel = new Button();
    final Button buttonWord = new Button();
    final Button buttonDirectPrint = new Button();
    final FlowPanel flwToolContainer = new FlowPanel();
    final FlowPanel flwToolContainerMob = new FlowPanel();
    final FlowPanel flwInput = new FlowPanel();

    //Popup panel related controls
    final PopupPanel popupPanel = new PopupPanel(true);
    final VerticalPanel ppForm = new VerticalPanel();
    final FlowPanel ppBody = new FlowPanel();
    final Button ppButtonBack = new Button();
    final Frame frame = new Frame();
    Icon iClose = new Icon();
    Span spanDeckle = new Span();

    private int height = 0;

    public DmReportLayout_Dsg() {
        height = Window.getClientHeight() - 35;
        //   dcStyle.ensureInjected();
        initialize();
    }

    private void initialize() {
        VerticalPanel vrtTemp = new VerticalPanel();

        vrtTemp.setWidth("100%");
        vrtTemp.getElement().setAttribute("style", "float:left; width:100%");

        vrtTemp.add(ToolContainer());
        //  vrtTemp.add(toolContainer);
        initializeTop();
        vrtTemp.add(createBody());

        form.addStyleName("dm-report-container");
        form.add(vrtTemp);
        initializePopup();
    }

    private void initializePopup() {
        ppForm.add(ppBody);
        ppForm.add(createPopupBottom());

        popupPanel.setWidth("400px");
        popupPanel.add(ppForm);
    }

    private FlowPanel createPopupBottom() {
        FlowPanel flwTR, flwTD;
        Anchor aBtn;
        InlineLabel label;

        flwTR = new FlowPanel();
        flwTR.getElement().setAttribute("style", flwTR.getElement().getAttribute("style")
                + ";margin-top: 5px;"
        );

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        label = new InlineLabel("Back");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-undo");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        ppButtonBack.setTitle("Back");
        ppButtonBack.addStyleName("dm-button-grad");
        ppButtonBack.getElement().appendChild(aBtn.getElement());
        ppButtonBack.getElement().appendChild(label.getElement());
        ppButtonBack.setWidth("100%");
        flwTD.add(ppButtonBack);
        flwTR.add(flwTD);

        flwTR.add(flwTD);

        return flwTR;
    }

    protected FlowPanel addRow(String fieldCode, String fieldCaption, IsWidget isWidget) {
        FlowPanel flwTR, flwTD;
        DmLabel lbl;

        flwTR = new FlowPanel();
        flwTR.setStyleName(fieldCode);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel(fieldCaption);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        flwTD.setStyleName("fieldContainer");
        if (isWidget instanceof DmTextHelp) {
            DmClient.setStyle(flwTD, "66.7%", null, "left", null, DmPosition.NONE, false);
        } else {
            DmClient.setStyle(flwTD, "66%", null, "left", null, DmPosition.NONE, false);
        }

//        if (isWidget instanceof DmTextHelp || isWidget instanceof DmTextHelpMulti) {
//            DmClient.setStyle(flwTD, "66.7%", null, "left", null, DmPosition.NONE, false);
//        } else {
//            DmClient.setStyle(flwTD, "66%", null, "left", null, DmPosition.NONE, false);
//        }
        flwTD.add(isWidget);
        flwTR.add(flwTD);

        return flwTR;
    }

    private void initializeTop() {
        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("PDF");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-file-pdf-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        //aBtn.getElement().getStyle().setColor("maroon");
        buttonView.setTitle("PDF");
        buttonView.addStyleName("dm-top-control-btnStyle");
        buttonView.getElement().appendChild(aBtn.getElement());
        buttonView.getElement().appendChild(label.getElement());
        buttonView.setWidth("70px");

        label = new InlineLabel("Print");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        aBtn.getElement().setAttribute("style", "padding: 5px;");
        //aBtn.getElement().getStyle().setColor("red");
//        buttonDirectPrint.setTitle("Print: (Ctrl+P)");
        buttonDirectPrint.addStyleName("dm-top-control-btnStyle");
        buttonDirectPrint.getElement().appendChild(aBtn.getElement());
        buttonDirectPrint.getElement().appendChild(label.getElement());
        buttonDirectPrint.getElement().setAttribute("style", "width: 70px;height: auto;");

        label = new InlineLabel("Excel");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-table");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        //aBtn.getElement().getStyle().setColor("green");
        buttonExcel.setTitle("Excel");
        buttonExcel.addStyleName("dm-top-control-btnStyle");
        buttonExcel.getElement().appendChild(aBtn.getElement());
        buttonExcel.getElement().appendChild(label.getElement());
        buttonExcel.setWidth("70px");

        label = new InlineLabel("Word");
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-file-word-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        // aBtn.getElement().getStyle().setColor("rgb(0, 0, 255)");
        buttonWord.setTitle("Word");
        buttonWord.addStyleName("dm-top-control-btnStyle");
        buttonWord.getElement().appendChild(aBtn.getElement());
        buttonWord.getElement().appendChild(label.getElement());
        buttonWord.setWidth("70px");

        label = new InlineLabel("Filter");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        // aBtn.getElement().getStyle().setColor("rgb(146, 112, 250)");
        buttonFilter.setTitle("Filter");
        buttonFilter.addStyleName("dm-top-control-btnStyle");
        buttonFilter.getElement().appendChild(aBtn.getElement());
        buttonFilter.getElement().appendChild(label.getElement());
        buttonFilter.setWidth("70px");

        label = new InlineLabel("Close");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        //aBtn.getElement().getStyle().setColor("red");
        buttonClose.setTitle("Close: (Escap)");
        buttonClose.addStyleName("dm-top-control-btnStyle");
        buttonClose.getElement().appendChild(aBtn.getElement());
        buttonClose.getElement().appendChild(label.getElement());
        buttonClose.setWidth("70px");
    }

    private Frame createBody() {
        frame.getElement().setAttribute("style", frame.getElement().getAttribute("style")
                + "; border:none;width:100%;height:" + (height + 6) + "px !important;position: absolute;background:whitesmoke;margin-top: -48px"
        //   + "; border:none;width:100%;position: absolute;background:whitesmoke;"
        );

        // frame.setHeight((height - 26) + "px");
        return frame;
    }

    private FlowPanel ToolContainer() {
        FlowPanel flwForm;

        flwForm = new FlowPanel();
        flwInput.setStyleName("white-body-color");
        flwInput.getElement().setAttribute("style", "width:100% !important;background-color:#f0f0f0");
        spanDeckle.setStyleName("top-Design");
        flwForm.add(spanDeckle);

        flwForm.addStyleName("topcontrol");
        flwForm.getElement().setAttribute("style", "height: 30px;");
        //flwToolContainer.addStyleName("pull-right margin-right10px");

        flwDirectPrint.add(buttonDirectPrint);

        iClose.addStyleName("opd-form-controls");
        iClose.setType(IconType.CLOSE);

        iClose.getElement().setAttribute("style", "padding-left: 25px;margin-left: 3px");
        flwDirectPrint.add(iClose);

        flwToolContainerMob.add(flwDirectPrint);

        flwToolContainer.getElement().setAttribute("style", "float: right;");
        flwToolContainer.add(flwToolContainerMob);

        flwForm.add(flwToolContainer);
        flwInput.add(flwForm);
        return flwInput;
    }

}
