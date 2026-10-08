package aarogya.localSetting.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class PosSettingPopup_Dsg {

    public final static String widgetName = "POS Setting";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final DmRemarkTextBox txtExampleBody = new DmRemarkTextBox(5000, false);
    final DmRemarkTextBox txtMainBody = new DmRemarkTextBox(5000, false);
    final DmRemarkTextBox txtMachineDetails = new DmRemarkTextBox(5000, false);
    final Button btnSaveSetting = new Button("Save");

    public PosSettingPopup_Dsg() {
        ini();
        iniToolContorls();

    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("350px");
        popupPanel.setHeight("150px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("40px");
        btnOk.setTitle("Ok");
        flwOk.add(btnOk);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("40px");
        btnCancel.setTitle("Close");
        flwCancel.add(vl.add(btnCancel));
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "106px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("JSON Demmo");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtExampleBody));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        lbl = new DmLabel("Main JSON Budy");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtMainBody));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "89px", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        btnSaveSetting.addStyleName("dm-button-grad");
        btnSaveSetting.setWidth("75px");
        btnSaveSetting.setHeight("20px");
        btnSaveSetting.getElement().setAttribute("style", btnSaveSetting.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;");
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        btnSaveSetting.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        btnSaveSetting.setTitle("Save Setting");
        flwTd.add(btnSaveSetting);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl = new DmLabel("Machine Details ");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtMachineDetails));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

}
