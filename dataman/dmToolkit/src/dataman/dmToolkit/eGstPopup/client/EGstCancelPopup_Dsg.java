package dataman.dmToolkit.eGstPopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class EGstCancelPopup_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    
    //<editor-fold defaultstate="collapsed" desc="POPUP">
    final PopupPanel popupPanel = new PopupPanel();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="PANELS">
    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwOk = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BUTTONS">
    final Button btnCancel = new Button();
    final Button btnOk = new Button();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TextHelp">
    //<editor-fold defaultstate="collapsed" desc="txtCancelRemark">
    final DmTextBox txtCancelRemark = new DmTextBox(250, false);
    //<editor-fold defaultstate="collapsed" desc="txtHCancelReason">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCancelReason = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHCancelReason() {
        txtHCancelReason.getDmGrid().addTextColumn(txtHCancelReason.getDmGrid().getPropertyAccess().name(), "Reason", 0, "name");
        txtHCancelReason.getDmGrid().initializeGrid(txtHCancelReason.getDmGrid().getPropertyAccess().code(), 340, 0, 1);
        txtHCancelReason.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
//</editor-fold>
    //</editor-fold>
    //</editor-fold>

    public EGstCancelPopup_Dsg() {
        ini();
        iniTxtHCancelReason();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("250px");
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

    }

    private FlowPanel workingArea() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.CENTER_HORIZONTAL, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reason");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >======================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCancelReason));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
         /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >======================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCancelRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ********** >====================================*/
        return flwTable;
    }

}
