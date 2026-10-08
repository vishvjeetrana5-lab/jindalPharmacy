package aarogya.company.client;

import aarogya.company.shared.Company_Srv;
import aarogya.company.shared.Company_SrvAsync;
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

public class Company extends Company_Dsg implements IsWidget {

    ServiceInvoker<Company_SrvAsync> SrvAsync;

    public Company(List<dataman.dmBase.self.shared.Company> lstCompany) {
        super();
        SrvAsync = new ServiceInvoker<Company_SrvAsync>(GWT.create(Company_Srv.class), Company.class.getName());
        grdCompany_events();
        moveRec(lstCompany);
    }

    private void initializeRibbon() {
        DmClient.<Ribbon>getStoreItem("Ribbon").setCompany(grdCompany.getGrid().getSelectionModel().getSelectedItem().getCurrentYear(), grdCompany.getGrid().getSelectionModel().getSelectedItem().getCompanyCode());
        DmClient.setVoucherPrefix(grdCompany.getGrid().getSelectionModel().getSelectedItem().getVPrefix());
        DmClient.setStartDate(grdCompany.getGrid().getSelectionModel().getSelectedItem().getStartDate());
        DmClient.setEndDate(grdCompany.getGrid().getSelectionModel().getSelectedItem().getEndDate());
    }

    private void grdCompany_events() {
        grdCompany.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()) {
                    next();
                }
            }
        }, KeyDownEvent.getType());

        ((CellSelectionModel) grdCompany.getGrid().getSelectionModel()).addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<Company>() {
            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent<Company> event) {
                initializeRibbon();
            }
        });
    }

    public void moveRec(List<dataman.dmBase.self.shared.Company> lstCompany) {
        grdCompany.getGrid().getStore().clear();

        for (int i = 0; i < lstCompany.size(); i++) {
            dataman.dmBase.self.shared.Company cTemp = new dataman.dmBase.self.shared.Company();
            cTemp.setCompanyCode(lstCompany.get(i).getCompanyCode());
            cTemp.setCurrentYear(lstCompany.get(i).getCurrentYear());
            cTemp.setStartDate(lstCompany.get(i).getStartDate());
            cTemp.setEndDate(lstCompany.get(i).getEndDate());
            cTemp.setVPrefix(lstCompany.get(i).getVPrefix());
            grdCompany.getGrid().getStore().add(cTemp);
        }
        ((CellSelectionModel) grdCompany.getGrid().getSelectionModel()).selectCell(0, 1);
        initializeRibbon();
    }

    @Override
    public void onBtnClick(ClickEvent event) {
        Button btnClicked = (Button) event.getSource();

        if (btnClicked.equals(btnNext)) {
            next();
        } else if (btnClicked.equals(btnPerevious)) {
            DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(PCSEnum.PLT);
        }
    }

    private void next() {
        String strCompanyCode;
        strCompanyCode = grdCompany.getGrid().getSelectionModel().getSelectedItem().getCompanyCode();

        SrvAsync.invokeRPC(true).getSites(strCompanyCode.getBytes(), new AsyncCallback<DmResult<PCSData>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<PCSData> result) {
                if (result.isStatus()) {
                    DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").next(PCSEnum.Site, result.getData());
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
