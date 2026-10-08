package aarogya.selfToolkit.remarkPopup.client;

import com.google.gwt.user.client.ui.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public class RemarkPopup_Dsg {

    public static final String widgetName = "Remark";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final PopupPanel popupPanel = new PopupPanel();

    final DmTextArea txtRemark = new DmTextArea();

    final Button btnOk = new Button();
    final FlowPanel flwOk = new FlowPanel();

    public RemarkPopup_Dsg() {
        ini();
    }

    private void ini() {
        VerticalPanel vrtInnerForm = new VerticalPanel();
        FlowPanel flw = new FlowPanel();

        popupPanel.setWidth("600px");
        popupPanel.setHeight("135px");
        popupPanel.setGlassEnabled(true);
        vrtInnerForm.add(iniToolControl());

        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("50px");
        btnOk.setTitle("Ok");
        flwOk.add(btnOk);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "105px", null, null, DmPosition.CENTER, true);
        txtRemark.setHeight("100px");
        flwTable.add(vl.add(txtRemark));

        return flwTable;
    }
}
