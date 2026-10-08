package dataman.dmToolkitR.dmFileManager.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkitR.dmStatic.shared.DmShared;

public class DmMultiLineModel extends DmMultiLineModel_Dsg implements IsWidget, ClickHandler {

  

    public interface CallBack {

        public void onOk(String textVal);

    }

    public CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public void show(String pText, String pTitle, int maxLength) {

        this.txtRemark.setMaxLength(maxLength);
        this.txtRemark.setText(pText);
        modal.setTitle(pTitle);

        modal.show();

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtRemark.setFocus(true);
            }
        });
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isOk) {
        if (isOk) {
            this.callBack.onOk(DmShared.removeSpace(DmShared.xNull(txtRemark.getText())));
        }

        this.modal.hide();
    }

    public DmMultiLineModel() {
        super();
        btnOk.addClickHandler(this);
    }

    @Override
    public Widget asWidget() {
        return modal;
    }



    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            hide(true);
        }
    }

}
