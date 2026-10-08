package aarogya.site.client;

import aarogya.site.shared.Site_Srv;
import aarogya.site.shared.Site_SrvAsync;
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

public class Site extends Site_Dsg implements IsWidget {

    ServiceInvoker<Site_SrvAsync> SrvAsync;
    boolean isAarogyaLite;

    public Site(List<dataman.dmBase.self.shared.Site> lstSite, boolean isAarogyaLite) {
        super();
        SrvAsync = new ServiceInvoker<Site_SrvAsync>(GWT.create(Site_Srv.class), Site.class.getName());
        this.isAarogyaLite = isAarogyaLite;
        grdPLT_events();
        moveRec(lstSite);
    }

    private void grdPLT_events() {
        grdSite.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()) {
                    next();
                }
            }
        }, KeyDownEvent.getType());

        ((CellSelectionModel) grdSite.getGrid().getSelectionModel()).addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<Site>() {
            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent<Site> event) {
                initializeRibbon();
            }
        });
    }

    public void moveRec(List<dataman.dmBase.self.shared.Site> lstSite) {
        grdSite.getGrid().getStore().clear();

        for (int i = 0; i < lstSite.size(); i++) {
            dataman.dmBase.self.shared.Site p = new dataman.dmBase.self.shared.Site();
            p.setSiteName(lstSite.get(i).getSiteName());
            p.setBranchName(lstSite.get(i).getBranchName());
            p.setSiteCode(lstSite.get(i).getSiteCode());

            grdSite.getGrid().getStore().add(p);
        }
        ((CellSelectionModel) grdSite.getGrid().getSelectionModel()).selectCell(0, 1);
        initializeRibbon();
    }

    private void initializeRibbon() {
        if (isAarogyaLite) {
//            DmClient.<aarogyaLite.ribbon.client.Ribbon>getStoreItem("Ribbon").setSiteName(grdSite.getGrid().getSelectionModel().getSelectedItem().getSiteName());
        } else {
            DmClient.<aarogya.ribbon.client.Ribbon>getStoreItem("Ribbon").setBranchName(grdSite.getGrid().getSelectionModel().getSelectedItem().getBranchName());
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onBtnClick(ClickEvent event) {
        Button btnClicked = (Button) event.getSource();
        if (btnClicked.equals(btnNext)) {
            next();
        }
        if (btnClicked.equals(btnPerevious)) {
            if (isAarogyaLite) {
                DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(PCSEnum.Login);
            } else {
                DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(PCSEnum.Company);
            }
        }
    }

    private void next() {
        String strSiteCode;
        strSiteCode = grdSite.getGrid().getSelectionModel().getSelectedItem().getSiteCode();

        SrvAsync.invokeRPC(true).validateSite(strSiteCode.getBytes(), new AsyncCallback<DmResult<PCSData>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<PCSData> result) {
                if (result.isStatus()) {
                    DmClient.setLocaleCountry(result.getData().getLocaleCountry());
                    DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").next(PCSEnum.WorkArea, result.getData());
                } else {
                    Window.alert(result.getErrorMessage());
                }
            }
        });

    }
}
