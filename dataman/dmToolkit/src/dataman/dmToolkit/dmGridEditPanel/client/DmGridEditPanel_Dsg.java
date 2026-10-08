package dataman.dmToolkit.dmGridEditPanel.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

class DmGridEditPanel_Dsg {

    final public DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel ppForm = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final public FlowPanel container = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final InlineLabel deleteLabel = new InlineLabel("Delete");
    final Anchor deleteAnchor = new Anchor();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwNew = new FlowPanel();
    final Button btnNew = new Button();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final Button btnDiscard = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();

    public DmGridEditPanel_Dsg() {
        DmClient.setStyle(vrtInnerForm, null, null, "left", null, DmPosition.NONE, true);
        vrtInnerForm.add(toolContainer);
        container.setHeight("100%");
        container.setWidth("100%");
        vrtInnerForm.add(container);
        vrtInnerForm.setCellHeight(container, "100%");
        vrtInnerForm.setCellWidth(container, "100%");
        initialzeTop();
        ppForm.setGlassEnabled(true);
        ppForm.add(vrtInnerForm);
    }

    private void initialzeTop() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok: (Alt + K)");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("50px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("New");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnNew.setTitle("New");
        btnNew.addStyleName("dm-top-control-btnStyle");
        btnNew.getElement().appendChild(aBtn.getElement());
        btnNew.getElement().appendChild(label.getElement());
        btnNew.setWidth("50px");
        flwNew.add(btnNew);

        deleteAnchor.setStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.setTitle("Delete: (Ctrl + D)");
        btnDelete.getElement().appendChild(deleteAnchor.getElement());
        btnDelete.getElement().appendChild(deleteLabel.getElement());
        btnDelete.setWidth("65px");
        flwDelete.add(btnDelete);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escape)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("60px");
        flwClose.add(btnClose);
    }
}
