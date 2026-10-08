package aarogya.dashBoard.dataModificationLog.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EyeDataPopUp extends EyeDataPopUp_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

//    interface CallBack {
//
//        public void onClose();
//    }
//    private CallBack callBack;
//    public void addCallBack(CallBack callBack) {
//        this.callBack = callBack;
//    }
    public EyeDataPopUp() {
        super();
        popupPanel.addAttachHandler(this);
        manageEvents();
    }

    public void show(String oldData, String newData, String moduleName, String menuName, String recordNameOne) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        setData(formatJSON(oldData), formatJSON(newData));
        if (!DmShared.xNull(moduleName).equalsIgnoreCase("")) {
            lblModuleName.setText(moduleName);
        }
        if (!DmShared.xNull(menuName).equalsIgnoreCase("")) {
            lblDashOne.setText(" - ");
            lblMenuName.setText(menuName);
        }
        if (!DmShared.xNull(recordNameOne).equalsIgnoreCase("")) {
            lblDashTwo.setText(" - ");
            lblRecordName1.setText(recordNameOne);
        }

    }

    public String formatJSON(String input) {
        int tabCount = 0;

        StringBuilder inputBuilder = new StringBuilder();
        char[] inputChar = input.toCharArray();

        for (int i = 0; i < inputChar.length; i++) {
            String charI = String.valueOf(inputChar[i]);
            if (charI.equals("}") || charI.equals("]")) {
                tabCount--;
                if (!String.valueOf(inputChar[i - 1]).equals("[") && !String.valueOf(inputChar[i - 1]).equals("{")) {
                    inputBuilder.append(newLine(tabCount));
                }
            }
            inputBuilder.append(charI);

            if (charI.equals("{") || charI.equals("[")) {
                tabCount++;
                if (String.valueOf(inputChar[i + 1]).equals("]") || String.valueOf(inputChar[i + 1]).equals("}")) {
                    continue;
                }

                inputBuilder.append(newLine(tabCount));
            }

            if (charI.equals(",")) {
                inputBuilder.append(newLine(tabCount));
            }
        }

        return inputBuilder.toString();
    }

    private String newLine(int tabCount) {
        StringBuilder builder = new StringBuilder();

        builder.append("\n");
        for (int j = 0; j < tabCount; j++) {
            builder.append("  ");
        }

        return builder.toString();
    }

    private void clear() {
        txtOldData.setText("");
        txtNewData.setText("");

        lblMenuName.setText("");
        lblModuleName.setText("");
        lblRecordName1.setText("");
        lblDashOne.setText("");
        lblDashTwo.setText("");
    }

    private void setData(String oldData, String newData) {
        clear();
        txtOldData.setText(oldData);
        txtNewData.setText(newData);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void manageEvents() {

        btnCancel.addClickHandler(this);

    }

}
