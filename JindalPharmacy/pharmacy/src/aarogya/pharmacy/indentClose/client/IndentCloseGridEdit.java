package aarogya.pharmacy.indentClose.client;


import aarogya.pharmacy.indentClose.shared.GrdIndentClose_Mdl;
import aarogya.pharmacy.indentClose.shared.IndentClose_Srv;
import aarogya.pharmacy.indentClose.shared.IndentClose_SrvAsync;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.Date;
import java.util.List;

public class IndentCloseGridEdit<T1, T2> extends IndentCloseGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdIndentClose_Mdl sampleDetails_Mdl);

        public void onDelete();

        public void onRefresh();
    }

    private final ServiceInvoker<IndentClose_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdIndentClose_Mdl parentRow;
    private IndentClose clsParent;

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource() == txtSampleDate) {
//            txtSampleDate.setText(DmClient.returnDate(txtSampleDate.getText()));
//        }
    }

  

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, IndentClose indentClose) {
        this.clsParent = indentClose;
//        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, false);
        super.showCenter(currentRowIndex, okNeeded, false, false);

   
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
              
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdIndentClose_Mdl ourRow = (GrdIndentClose_Mdl) row;
        parentRow = ourRow;


        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtRemark.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdIndentClose_Mdl();
    }

    @Override
    public void onOk() {
        if (txtRemark.getText().trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Remark is mandatory.", txtRemark).show();
            return;
        }
       
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdIndentClose_Mdl ourRow = (GrdIndentClose_Mdl) row;

        ourRow = parentRow;

        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
  

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public IndentCloseGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Reason", "400px", "62px");
        SrvAsync = new ServiceInvoker<IndentClose_SrvAsync>(GWT.create(IndentClose_Srv.class), IndentClose.class.getName());
    
    }



}
