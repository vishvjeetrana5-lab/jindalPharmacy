package dataman.dmToolkit.dmRemarkTextBox.client;

import com.google.gwt.user.client.ui.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

class DmRemarkPopup_Dsg {

    public static final String widgetName = "Remark";
    final DmToolContainer toolContainer = new DmToolContainer();
    final PopupPanel popupPanel = new PopupPanel();

    final DmTextArea txtAreaRemark = new DmTextArea();

    final Button btnOk = new Button();
    final FlowPanel flwOk = new FlowPanel();

    final Button btnClose = new Button();
    final FlowPanel flwClose = new FlowPanel();

    public DmRemarkPopup_Dsg() {
        ini();
    }

    private void ini() {
        VerticalPanel vrtInnerForm = new VerticalPanel();
        FlowPanel flw = new FlowPanel();

        popupPanel.setWidth("980px");
        popupPanel.setHeight("400px");
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
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.setTitle("Ok");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("50px");
        flwOk.add(btnOk);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("60px");
        flwClose.add(btnClose);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "360px", null, null, DmPosition.CENTER, true);
        txtAreaRemark.setHeight("100%");
//        flwTable.add(vl.add(txtAreaRemark));
        flwTable.add(txtAreaRemark);

        return flwTable;
    }
}
