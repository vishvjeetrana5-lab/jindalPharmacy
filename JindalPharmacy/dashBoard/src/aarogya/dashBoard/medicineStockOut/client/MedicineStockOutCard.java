package aarogya.dashBoard.medicineStockOut.client;

import aarogya.dashBoard.medicineStockOut.shared.*;
import aarogya.dashBoard.dashBoardCard.client.DashBoardCard;
import aarogya.projectMenu.self.shared.pmDashBoard;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum.DmChartType;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmChart.client.DmChart;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.ArrayList;
import java.util.HashMap;

public class MedicineStockOutCard extends MedicineStockOutCard_Dsg {

    private final ServiceInvoker<MedicineStockOut_SrvAsync> srvAsync;
//    private final DmLabel lblCount = new DmLabel("0");
//    private final DmChart chart = new DmChart(170, 380, DmChartType.Gauge);
    CardDetail_Mdl cdr_Mdl;

    public MedicineStockOutCard() {
        super();
        srvAsync = new ServiceInvoker<MedicineStockOut_SrvAsync>(GWT.create(MedicineStockOut_Srv.class), MedicineStockOutCard.class.getName());
//        setCaption(new pmDashBoard().MEDICINE_STOCK_OUT.caption);
//        setColor("rgb(216, 27, 96)");
//        setCollapseWidget(lblCount);
//        setExpandWidget(chart.asWidget());
//        lblCount.addStyleName("dashBoard-count");
    }

    @Override
    public void initialze() {
        srvAsync.invokeRPC(false).getCardDetail("90", new AsyncCallback<DmResult<CardDetail_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<CardDetail_Mdl> result) {
                if (result.isStatus()) {
                    setValue(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return this.getWidget();
    }

    @Override
    public void anchorClicked(ClickEvent event) {
        if (event.getSource().equals(this.externalLinkAnchor)) {

            HashMap hm = new HashMap();

            hm.put("departmentCode", cdr_Mdl.getDepartmentCode());
            hm.put("departmentName", cdr_Mdl.getDepartmentName());
            hm.put("callFromDashboar", "true");

            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmPharmacy().moduleName, new pmPharmacy().PROCUREMENT_PLANNER_DISPLAY.caption, hm, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            });

        }
    }

    private void setValue(CardDetail_Mdl cardDetailMdl) {
        if (cardDetailMdl != null) {
            lblCount.setText(DmShared.xNull(cardDetailMdl.getCount()).trim());
            chart.drawChart(cardDetailMdl.getListChardData());
            cdr_Mdl = cardDetailMdl;

        } else {
            lblCount.setText("0");
            chart.drawChart(new ArrayList<DmChart_Mdl>());
        }
    }

}
