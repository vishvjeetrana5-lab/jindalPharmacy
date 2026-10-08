package aarogya.plt.client;

import aarogya.plt.shared.PLT_SrvAsync;
import aarogya.plt.shared.PLT_Model;
import aarogya.plt.shared.PLT_Srv;
import aarogya.ribbon.client.Ribbon;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.PCSData;
import java.util.List;

public class PLT extends PLT_Dsg implements IsWidget {

    List<PLT_Model> lstMaster;
    ServiceInvoker<PLT_SrvAsync> SrvAsync;

    public PLT(List<dataman.dmBase.self.shared.PLT> lstPLT) {
        super();
        SrvAsync = new ServiceInvoker<PLT_SrvAsync>(GWT.create(PLT_Srv.class), PLT.class.getName());
        grdPLT_events();
        moveRec(lstPLT);
    }

    private void grdPLT_events() {
        grdPLT.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()) {
                    next();
                }
            }
        }, KeyDownEvent.getType());

        ((CellSelectionModel) grdPLT.getGrid().getSelectionModel()).addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<PLT>() {
            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent<PLT> event) {
                initializeRibbon();
            }
        });
    }

    public void moveRec(List<dataman.dmBase.self.shared.PLT> lstPLT) {
        grdPLT.getGrid().getStore().clear();

        for (int i = 0; i < lstPLT.size(); i++) {
            dataman.dmBase.self.shared.PLT p = new dataman.dmBase.self.shared.PLT();
            p.setPLTName(lstPLT.get(i).getPLTName());
            p.setPLTCode(lstPLT.get(i).getPLTCode());

            grdPLT.getGrid().getStore().add(p);
        }

        ((CellSelectionModel) grdPLT.getGrid().getSelectionModel()).selectCell(0, 1);
        initializeRibbon();
    }

    private void initializeRibbon() {
        DmClient.<Ribbon>getStoreItem("Ribbon").setPLT(grdPLT.getGrid().getSelectionModel().getSelectedItem().getPLTName());
    }

    @Override
    public void onBtnClick(ClickEvent event) {
        Button btnClicked = (Button) event.getSource();

        if (btnClicked.equals(btnNext)) {
            next();
        } else if (btnClicked.equals(btnPerevious)) {
            DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(PCSEnum.Login);
        }
    }

    private void next() {
        String strPLTCode;
        strPLTCode = grdPLT.getGrid().getSelectionModel().getSelectedItem().getPLTCode();

        SrvAsync.invokeRPC(true).getCompanys(strPLTCode.getBytes(), new AsyncCallback<DmResult<PCSData>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onSuccess(DmResult<PCSData> result) {
                if (result.isStatus()) {
                    DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").next(PCSEnum.Company, result.getData());
                } else {
                    Window.alert(result.getErrorMessage());
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

}
