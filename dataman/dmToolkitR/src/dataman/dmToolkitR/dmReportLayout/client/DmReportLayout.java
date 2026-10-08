package dataman.dmToolkitR.dmReportLayout.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
//import com.sencha.gxt.widget.core.client.Dialog;
//import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkitR.dmReportLayout.shared.DmReportLayout_SrvAsync;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
//import dataman.dmToolkitR.dmTextBoxNumeric.client.DmTextBoxNumeric;
//import dataman.dmToolkitR.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkitR.dmTextHelp.client.DmTextHelp;
//import dataman.dmToolkitR.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.DmBaseURL;
import dataman.dmBase.self.shared.DmEnum;
//import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import java.util.LinkedHashMap;

public abstract class DmReportLayout extends DmReportLayout_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    public abstract void onClose();

    public abstract void onView();

    public DmReportProperty_Mdl reportProperty = new DmReportProperty_Mdl();
    private final LinkedHashMap<String, FieldUDT_Mdl> filter = new LinkedHashMap();
    private final LinkedHashMap<String, Object> controls = new LinkedHashMap();
    private final UrlBuilder urlBuilder = new UrlBuilder();
    private String widgetName;

    public void setTitle(Widget widget, String widgetName) {
        widget.setTitle(widgetName);
        this.widgetName = widgetName;
//       toolContainer.setTitle(widgetName);
    }

//    public String getTitle() {
////        return toolContainer.getTitle();
//    }
    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            popupPanel.removeFromParent();
        }
    }

    public void addControl(String fieldCode, String fieldCaption, IsWidget isWidget) {
        FieldUDT_Mdl filterTemp = new FieldUDT_Mdl();
        ppBody.add(addRow(fieldCode, fieldCaption, isWidget));
        controls.put(fieldCode, isWidget);
        filterTemp.setFieldCaption(fieldCaption);
        filter.put(fieldCode, filterTemp);
    }

    public DmReportLayout(String widgetName) {
        super();
        this.widgetName = widgetName;
        spanDeckle.getElement().setInnerHTML(widgetName);
//        toolContainer.addWidget(buttonView);
//        toolContainer.addWidget(buttonExcel);
//        toolContainer.addWidget(buttonWord);
//        toolContainer.addWidget(buttonFilter);
//        toolContainer.addWidget(buttonClose);
//        toolContainer.addGap();
        form.addAttachHandler(this);
        iClose.addClickHandler(this);
        buttonFilter.addClickHandler(this);
        buttonClose.addClickHandler(this);
        buttonView.addClickHandler(this);
        buttonExcel.addClickHandler(this);
        buttonWord.addClickHandler(this);
        buttonDirectPrint.addClickHandler(this);
        ppButtonBack.addClickHandler(this);
        DmBaseURL baseURL = DmClient.getHostPageBaseURL();
        urlBuilder.setProtocol(baseURL.getProtocol());
        urlBuilder.setPath(baseURL.getUrl() + DmShared.classPathToPtah(DmReportLayout_SrvAsync.class.getName()));
    }

    private void btnClick(Button aSource) {
        if (!aSource.isVisible()) {
            return;
        }
        if (!aSource.isEnabled()) {
            return;
        }
        if (!aSource.isAttached()) {
            return;
        }

        if (aSource.equals(buttonFilter)) {
            popupPanel.showRelativeTo(buttonFilter);
        } else if (aSource.equals(buttonClose)) {
            onClose();
        } else if (aSource.equals(ppButtonBack)) {
            popupPanel.hide();
        } else if (aSource.equals(buttonView)) {
            reportProperty.setExportType(DmEnum.DmExportType.PDF);
            onView();
        } else if (aSource.equals(buttonExcel)) {
            reportProperty.setExportType(DmEnum.DmExportType.EXCEL);
            onView();
        } else if (aSource.equals(buttonWord)) {
            reportProperty.setExportType(DmEnum.DmExportType.WORD);
            onView();
        } else if (aSource.equals(buttonDirectPrint)) {
            reportProperty.setPrintToPrinter(true);
            onView();
            //popupPanel.hide();
        }
    }

    public LinkedHashMap<String, FieldUDT_Mdl> getFilter() {
        FieldUDT_Mdl filterTemp;
        DmTextBox dmTextBox;
        //     DmTextBoxTime dmTextBoxTime;
        DmTextHelp dmTextHelp;
        //      DmTextHelpMulti dmTextHelpMulti;
        //     DmTextBoxNumeric dmTextBoxNumeric;

        for (String k : controls.keySet()) {
            filterTemp = new FieldUDT_Mdl();

            if (controls.get(k) instanceof DmTextBox) {
                dmTextBox = (DmTextBox) controls.get(k);
                filterTemp = filter.get(k);
                filterTemp.setText(dmTextBox.getText());
                filterTemp.setTag(dmTextBox.getTag());
            } else if (controls.get(k) instanceof DmTextHelp) {
                dmTextHelp = (DmTextHelp) controls.get(k);
                filterTemp = filter.get(k);
                filterTemp.setText(dmTextHelp.getText());
                filterTemp.setTag(dmTextHelp.getTag());
//            } else if (controls.get(k) instanceof DmTextHelpMulti) {
//                dmTextHelpMulti = (DmTextHelpMulti) controls.get(k);
//                filterTemp = filter.get(k);
//                filterTemp.setText(dmTextHelpMulti.getText());
//                filterTemp.setTag(dmTextHelpMulti.getTag());
//            } else if (controls.get(k) instanceof DmTextBoxNumeric) {
//                dmTextBoxNumeric = (DmTextBoxNumeric) controls.get(k);
//                filterTemp = filter.get(k);
//                filterTemp.setText(dmTextBoxNumeric.getText());
//                filterTemp.setTag(dmTextBoxNumeric.getTag());
//            } else if (controls.get(k) instanceof DmTextBoxTime) {
//                dmTextBoxTime = (DmTextBoxTime) controls.get(k);
//                filterTemp = filter.get(k);
//                filterTemp.setText(dmTextBoxTime.getText());
//                filterTemp.setTag(dmTextBoxTime.getText());
//            
            } else {
                throw new UnsupportedOperationException("Invalid control.");
            }
            filter.remove(k);
            filter.put(k, filterTemp);
        }

        return filter;
    }

//    public void getReport(String fileName) {
//
//        if (!DmShared.xNull(fileName).trim().equalsIgnoreCase("")) {
//            urlBuilder.setParameter("u", DmRequestBuilder.getSessionId());
//            urlBuilder.setParameter("f", fileName);
//            if (DmClient.isViewReportInNewWindow()) {
//                Window.open(urlBuilder.buildString().replace("///", "//"), "_blank", "");
//            } else {
//                frame.setUrl(urlBuilder.buildString().replace("///", "//"));
//            }
//        }
//    }
    public String getReport(String fileName) {
        return getReport(fileName, DmClient.isViewReportInNewWindow());
    }

    public String getReport(String fileName, boolean isViewReportInNewWindow) {
        String reportUrl = null;
        if (!DmShared.xNull(fileName).trim().equalsIgnoreCase("")) {
            urlBuilder.setParameter("u", DmRequestBuilder.getSessionId());
            urlBuilder.setParameter("f", fileName);
            reportUrl = urlBuilder.buildString().replace("///", "//");
            if (isViewReportInNewWindow) {
                Window.open(reportUrl, "_blank", "");
            } else {
                frame.setUrl(reportUrl);
            }
        }
        return reportUrl;
    }

    public Frame getFrame() {
        return frame;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == iClose) {
            onClose();
        } else {
            btnClick((Button) event.getSource());
        }
    }

//    public void setPrintToPrinter(boolean printToPrinter) {
//        if (printToPrinter) {
////            if (reportProperty != null) {
//            reportProperty.setPrinterName(printToPrinter, reportProperty.getPrinterName(), reportProperty.getPrinterImageType());
////            }
//        } else {
//            reportProperty.setPrinterName(printToPrinter, "", DmEnum.DmPrinterImageType.RGB);
//        }
////        if (reportProperty != null) {
////            reportProperty.setTcpSessionId(DmClient.getTcpSessionId(), printToPrinter);
////            if (DmClient.getLocalSetting() != null) {
////                reportProperty.setPrinterName(DmShared.xNull(DmClient.getLocalSetting().getVoucherPrinter()));
////            }
////        }
//    }
    @Override
    public void rootKeyInvoked(final DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
//            new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {
//
//                @Override
//                public void onDialogHide(DialogHideEvent event) {
//                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                        rootEvent.stopPropagation();
//                        onClose();
//                    }
//                }
//            }).show();
        }
    }

    public void removePDFButton() {
        //  toolContainer.removeWidget(buttonView);
    }

    public void removeExcelButton() {
        // toolContainer.removeWidget(buttonExcel);
    }

    public void removeWordButton() {
        // toolContainer.removeWidget(buttonWord);
    }
}
