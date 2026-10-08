package aarogya.selfToolkit.fifoPopUp.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class FifoPopUp_Dsg {

    public final static String widgetName = "FIFO Process";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();

//<editor-fold defaultstate="collapsed" desc="FLOWPANELS">
    final FlowPanel flwOk = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    FlowPanel flwSettings = new FlowPanel();
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="BUTTON">
    final Button btnClose = new Button();
    final Button btnOk = new Button();
    protected final Button btnSettings = new Button();
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="LABEL">
    final DmLabel labelLogFile = new DmLabel("Logs : ");
    final DmLabel labelStatus = new DmLabel("");
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="TEXT BOX">
    final DmTextArea txtLog = new DmTextArea();
//</editor-fold>

    public FifoPopUp_Dsg(boolean isOpenedAsForm, String moduleType) {
        ini(isOpenedAsForm);
    }

    private void ini(boolean isOpenedAsForm) {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("725px");
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setWidth("725px");
//        popupPanel.setHeight("450px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        if (isOpenedAsForm) {
            flw.setStyleName("dm-FlwWorkingArea");
        }
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
//        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
//        btnOk.getElement().appendChild(label.getElement());
        btnOk.setTitle("Ok");
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Settings");
//        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSettings.addStyleName("dm-top-control-btnStyle");
        btnSettings.getElement().appendChild(aBtn.getElement());
//        btnSettings.getElement().appendChild(label.getElement());
        btnSettings.setTitle("Filter");
        btnSettings.setWidth("70px");
        flwSettings.add(vl.add(btnSettings));

        label = new InlineLabel("Close");
//        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
//        btnClose.getElement().appendChild(label.getElement());
        btnClose.setTitle("Close: (Esc)");
        btnClose.setWidth("70px");
        flwClose.add(vl.add(btnClose));

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwSettings);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl = new DmLabel("");

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        //////////////////////FIRST ROW///////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(labelLogFile);
        labelLogFile.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        labelLogFile.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        labelLogFile.getElement().getStyle().setHeight(15, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Status :");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(labelStatus);
        labelStatus.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        labelStatus.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        labelStatus.getElement().getStyle().setHeight(15, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //////////////////////NEXT ROW///////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtLog));
        txtLog.setHeight("354px");
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
