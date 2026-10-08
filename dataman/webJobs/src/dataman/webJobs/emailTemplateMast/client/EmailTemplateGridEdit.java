package dataman.webJobs.emailTemplateMast.client;

import dataman.webJobs.emailTemplateMast.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EmailTemplateGridEdit<T1, T2> extends EmailTemplateGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdEmail_Mdl opdService);

        public void onDelete();
    }

    private final ServiceInvoker<EmailTemplateMast_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdEmail_Mdl emailTempGrid;
    private EmailTemplateMast emailTemp;
    private int hashCode = 0;

    private void textHelpEvents() {

    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, EmailTemplateMast emailEntry) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.emailTemp = emailEntry;
    }

    @Override
    public void displayRow(T1 row) {
        GrdEmail_Mdl ourRow = (GrdEmail_Mdl) row;
        emailTempGrid = new GrdEmail_Mdl();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtFileName.setText(DmShared.xNull(ourRow.getFileName()));
        txtProcName.setText(DmShared.xNull(ourRow.getProcName()));
        txtRptName.setText(DmShared.xNull(ourRow.getRptName()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdEmail_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtProcName.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Procedure is mandatory", txtProcName).show();
            return;
        }
        if (DmShared.xNull(txtRptName.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Rpt name is mandatory", txtRptName).show();
            return;
        }
        if (DmShared.xNull(txtFileName.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("File name is mandatory", txtFileName).show();
            return;
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdEmail_Mdl ourRow = (GrdEmail_Mdl) row;

        ourRow.setProcName(DmShared.xNull(txtProcName.getText()));
        ourRow.setRptName(DmShared.xNull(txtRptName.getText()));
        ourRow.setFileName(DmShared.xNull(txtFileName.getText()));

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
        pnlGrd2.hide();
        pnlGrd3.hide();
        hide();
    }

    private void buttonClick() {
        btnProcedure.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {

                    pnlGrd2.clear();
                    pnlGrd2.showRelativeTo(btnProcedure);
                    pnlGrd2.add(txtProcedure);
                    txtProcedure.setText("1. Create procedure same as document print With One Varchar Parameter.\n"
                            + "\n"
                            + "   Example :- Create procedure  [dbo].[TestProcedure](@UniqueId varchar(21)) \n"
                            + "              As \n"
                            + "              Begin \n"
                            + "              Declare @SqlQry as varchar(max) \n"
                            + "              Declare @Condition as varchar(max) \n"
                            + "              Set @Condition = 'Where SG.SubCode In (' + @UniqueId +') '  \n"
                            + "              Set @SqlQry = 'Select * From SubGroup SG ' + @Condition \n"
                            + "              Exec (@sqlqry)\n"
                            + "              End ");
                    txtProcedure.setReadOnly(true);

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), btnProcedure).show();
                }
            }
        });
        btnRpt.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {

                    pnlGrd3.clear();
                    pnlGrd3.showRelativeTo(btnRpt);
                    pnlGrd3.add(txtRpt);
                    txtRpt.setText("1. Create RPT same as document print.");
                    txtRpt.setReadOnly(true);

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), btnRpt).show();
                }
            }
        });
    }

    private void handlerEvents() {

        btnProcedure.addClickHandler(this);
        btnRpt.addClickHandler(this);

    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public EmailTemplateGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Email", "400px", "120px");
        SrvAsync = new ServiceInvoker<>(GWT.create(EmailTemplateMast_Srv.class), EmailTemplateMast.class.getName());
        textHelpEvents();
        handlerEvents();
        buttonClick();
    }

}
