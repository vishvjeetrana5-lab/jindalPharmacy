package dataman.utility.dataBackup.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class AddDirectory_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final PopupPanel popupPanel = new PopupPanel();
    final DmTextBox txtFileName = new DmTextBox(100, true);
    final DmTextBox txtFileLocation = new DmTextBox(1000, true);
    final DmTextBox txtBackupLocation = new DmTextBox(1000, true);
    final FlowPanel flwElementName = new FlowPanel();
    final FlowPanel flwTextElement = new FlowPanel();
    final FlowPanel flwHelpElement = new FlowPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHElement">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHElement = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniElement() {
        txtHElement.getDmGrid().addTextColumn(txtHElement.getDmGrid().getPropertyAccess().name(), "Element", 0, "elementType");
        txtHElement.getDmGrid().initializeGrid(txtHElement.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHElement.getHelper().addSort("elementType", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDBName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDatabase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniDatabase() {
        txtHDatabase.getDmGrid().addTextColumn(txtHDatabase.getDmGrid().getPropertyAccess().name(), "Database", 0, "dbName");
        txtHDatabase.getDmGrid().initializeGrid(txtHDatabase.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDatabase.getHelper().addSort("dbName", DmSortType.ASC);
    }
    //</editor-fold>

    public AddDirectory_Dsg() {
        iniElement();
        iniDatabase();
        ini();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("120px");
        vrtInnerForm.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(section());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolControls() {
        InlineLabel label;
        Anchor aBtn;
        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.setTitle("Ok");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle("Add Element");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel section() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "90px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHElement));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        DmClient.setStyle(flwElementName, "70%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwHelpElement, "100%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwTextElement, "100%", null, "left", null, DmPosition.NONE, false);
        flwTextElement.add(vl.add(txtFileName));
        flwHelpElement.add(vl.add(txtHDatabase));
        flwElementName.add(flwTextElement);
        flwTr.add(flwElementName);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Element Location");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFileLocation));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Backup Location");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBackupLocation));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
