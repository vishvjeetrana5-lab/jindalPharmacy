package dataman.dmToolkit.dmToolContainer.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DmToolContainer extends DmToolContainer_Dsg implements IsWidget {

    String strMode;

    public void addWidget(IsWidget isWidget) {
        hrtBtnPnl.add(isWidget);
    }

    public void addGap() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setWidth("100%");
        hrtBtnPnl.add(flwTemp);
        hrtBtnPnl.setCellWidth(flwTemp, "100%");
    }

    public void addGap2() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setWidth("50%");
        hrtBtnPnl.add(flwTemp);
        hrtBtnPnl.setCellWidth(flwTemp, "50%");
    }

    public void addGap3() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setWidth("25%");
        hrtBtnPnl.add(flwTemp);
        hrtBtnPnl.setCellWidth(flwTemp, "25%");
    }

    public void removeWidget(IsWidget isWidget) {
        hrtBtnPnl.remove(isWidget);
    }

    public String getMode() {
        return DmShared.xNull(strMode);
    }

    public void setMode(String strMode) {
        lblMode.setText(DmShared.xNull(strMode).trim());

        if (DmShared.xNull(strMode).trim().equalsIgnoreCase("")) {
            lblMode.setVisible(false);
        } else {
            lblMode.setVisible(true);
        }

        this.strMode = lblMode.getText();
    }

    public void setTitle(String title) {
        lblTitle.setText(title);
    }

    public String getTitle() {
        return lblTitle.getText();
    }

    public void setTitleVisible(boolean visible) {
        lblTitle.setVisible(visible);
    }

    public void setModeVisible(boolean visible) {
        lblMode.setVisible(visible);
    }

    public void setFocus() {
//        if (btnNew.isAttached()) {
//            btnNew.setFocus(true);
//        } else if (btnEdit.isAttached()) {
//            btnEdit.setFocus(true);
//        } else if (btnDelete.isAttached()) {
//            btnDelete.setFocus(true);
//        } else if (btnCard.isAttached()) {
//            btnCard.setFocus(true);
//        } else if (btnFind.isAttached()) {
//            btnFind.setFocus(true);
//        } else if (btnSave.isAttached()) {
//            btnSave.setFocus(true);
//        }
    }

    public DmToolContainer() {
        super();
        lblMode.setVisible(false);
    }

    @Override
    public Widget asWidget() {
        return hrtMainPnl;
    }
}
