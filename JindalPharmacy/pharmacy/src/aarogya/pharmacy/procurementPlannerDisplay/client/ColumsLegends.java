package aarogya.pharmacy.procurementPlannerDisplay.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ColumsLegends<T1, T2> extends ColumsLegends_Dsg implements ClickHandler {

    public ColumsLegends() {
        super();
        btnClose.addClickHandler(this);
        toolContainer.setTitle("Colums Legends");
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            popupPanel.hide();
        }
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
    }
}
