package dataman.dmToolkit.dmMasterReportLayout.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmPaging;
import dataman.dmBase.self.shared.SetWidgetInvoker;

public class DmMasterReportLayout extends DmReportLayout implements IsWidget, BlurHandler {

    private DmPaging paging;

    public DmMasterReportLayout(DmPaging paging, String widgetName) {
        super();
        this.paging = paging;
        setTitle(this.asWidget(), widgetName);
    }

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        getReport(this.paging.getReport().getResultFileName());
    }

    public void setDefault(SetWidgetInvoker swi) {
        if (this.paging.getResultFormat() == DmPaging.Result.PRINT) {
            if (!this.paging.getReport().getReportProperty().isPrintToPrinter()) {
                swi.setWidget(this);
                onView();
            }

            this.paging.setResultFormat(DmPaging.Result.DEFAULT);
            this.paging.setReport(null);
        }
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

}
