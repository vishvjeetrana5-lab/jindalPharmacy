package dataman.webJobs.emailManager.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.webJobs.emailManager.shared.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.LinkedHashMap;
import java.util.List;

public class FilterDataPopup extends FilterDataPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(List<FilterDataPopup_Mdl> filterDataList);

    }

    private CallBack callBack;
    private List<FilterDataPopup_Mdl> filterDataList;
    LinkedHashMap<String, Object> controlList;
    private final ServiceInvoker<EmailManager_SrvAsync> SrvAsync;
    private Template_Mdl templateMdl;
    private int height = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(List<FilterDataPopup_Mdl> data_Mdls, Template_Mdl template_Mdl) {
        this.filterDataList = data_Mdls;
        this.templateMdl = template_Mdl;
        initialize(filterDataList);
        setFocus();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

//        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//            @Override
//            public void execute() {
//                
//            }
//        });
    }

    private void manageEvent() {
        btnRefresh.addClickHandler(this);
        btnOk.addClickHandler(this);
        chkCheckAll.addClickHandler(this);

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
            try {
                callBack.onOk(getFilterDataList());
                popupPanel.hide();
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnOk).show();
            }
        } else if (event.getSource() == btnRefresh) {
            refreshFilter();
        }
    }

    public void refreshFilter() {
        for (FilterDataPopup_Mdl filterData_Mdl : filterDataList) {
            filterData_Mdl.setHelpTag("");
            filterData_Mdl.setHelpText("");
            filterData_Mdl.setValueFrom("");
            filterData_Mdl.setValueTo("");
            filterData_Mdl.setContains("");
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.onOk(getFilterDataList());
            popupPanel.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            popupPanel.hide();
        }
    }

    public FilterDataPopup() {
        super();
        toolContainer.setTitle(widgetName);
        SrvAsync = new ServiceInvoker<EmailManager_SrvAsync>(GWT.create(EmailManager_Srv.class), EmailManager.class.getName());
        manageEvent();
        popupPanel.addAttachHandler(this);
    }

    private void setFocus() {
        if (controlList != null) {
            for (Object object : controlList.values()) {
                if (vl.get().contains(object)) {
                    if (focus(object)) {
                        break;
                    }
                }
            }
        }
    }

    private boolean focus(Object object) {

        if (object instanceof DmTextBox) {
            ((DmTextBox) object).setFocus(true);
            return true;
        } else if (object instanceof DmTextBoxNumeric) {
            ((DmTextBoxNumeric) object).setFocus(true);
            return true;
        } else if (object instanceof DmTextHelpMulti) {
            ((DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs>) object).setFocus(true);
            return true;
        }
        return false;
    }

    public void setFocus(String columnName) {
        Object object;

        if (controlList != null) {
            if (controlList.containsKey(columnName)) {
                object = controlList.get(columnName);
                if (vl.get().contains(object)) {
                    focus(object);
                }
            }
        }
    }

    private void clear() {
        if (controlList != null) {
            for (Object object : controlList.values()) {
                vl.get().remove(object);
            }
        }
        controlList = new LinkedHashMap<>();
        flwworkingArea.clear();
    }

    private void initialize(List<FilterDataPopup_Mdl> filterDataList) {
        FlowPanel flwTable, flwTd, flwTr1, flwTr2;
        DmLabel lable;
        DmTextBox textBox;
        DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> textHelpMulti;
        this.filterDataList = filterDataList;
        clear();
        int noOfRow = 0;
        flwTable = new FlowPanel();
        boolean isEven = false;

        flwTr1 = new FlowPanel();
        flwTr2 = new FlowPanel();
        for (FilterDataPopup_Mdl filterData_Mdl : this.filterDataList) {
            if (!(DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("isChecked") || DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("UniqueId")
                    || DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("formatMsg") || DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("isFiltered")
                    || DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("SubCode"))) {
                noOfRow++;

                if (noOfRow % 2 == 0) {
                    isEven = true;
                } else {
                    isEven = false;
                }

                if (!isEven) {
                    flwTr1 = new FlowPanel();
                    DmClient.setStyle(flwTr1, "100%", null, "left", null, DmPosition.NONE, true);
                }

                flwTd = new FlowPanel();
                DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
                lable = new DmLabel(filterData_Mdl.getColumnName());
                flwTd.add(lable);
                flwTd.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
                flwTd.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
                flwTr1.add(flwTd);

                if (!isEven) {
                    flwTd.getElement().getStyle().setMarginRight(4, Style.Unit.PCT);
                }

                flwTable.add(flwTr1);

                if (!isEven) {
                    flwTr2 = new FlowPanel();
                    DmClient.setStyle(flwTr2, "100%", null, "left", null, DmPosition.NONE, true);
                }

                switch (DmShared.xNull(filterData_Mdl.getDataType()).toUpperCase()) {
                    case "DATE":
                    case "DATETIME":
                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "29%", null, "left", null, DmPosition.NONE, false);
                        final DmTextBox textBoxDate1 = new DmTextBox(11, false);
                        flwTd.add(vl.add(textBoxDate1));
                        controlList.put(filterData_Mdl.getColumnName() + "from", textBoxDate1);

                        if (!DmShared.xNull(filterData_Mdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                            textBoxDate1.setText(DmShared.xNull(filterData_Mdl.getValueFrom()));
                        }
                        textBoxDate1.getElement().setPropertyString("placeholder", "Equal To");
                        flwTr2.add(flwTd);
                        textBoxDate1.addBlurHandler(new BlurHandler() {

                            @Override
                            public void onBlur(BlurEvent event) {
                                textBoxDate1.setText(DmClient.returnDate(textBoxDate1.getText()));
                            }
                        });

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
                        final DmTextBox textBoxDate2 = new DmTextBox(11, false);
                        flwTd.add(vl.add(textBoxDate2));
                        controlList.put(filterData_Mdl.getColumnName() + "to", textBoxDate2);
                        if (!DmShared.xNull(filterData_Mdl.getValueTo()).trim().equalsIgnoreCase("")) {
                            textBoxDate2.setText(DmShared.xNull(filterData_Mdl.getValueTo()));
                        }
                        textBoxDate2.getElement().setPropertyString("placeholder", "To");
                        flwTr2.add(flwTd);
                        if (!isEven) {
                            flwTd.getElement().getStyle().setMarginRight(4, Style.Unit.PCT);
                        }
                        textBoxDate2.addBlurHandler(new BlurHandler() {

                            @Override
                            public void onBlur(BlurEvent event) {
                                textBoxDate2.setText(DmClient.returnDate(textBoxDate2.getText()));
                            }
                        });

                        break;
                    case "SMALLINT":
                    case "INT":
                    case "TINYINT":
                    case "BIGINT":
                        numericDataTypeColumn(flwTr2, filterData_Mdl, 0, isEven);
                        break;
                    case "FLOAT":
                    case "DOUBLE":
                        numericDataTypeColumn(flwTr2, filterData_Mdl, 2, isEven);
                        break;
                    case "VARCHAR":
                    case "NVARCHAR":
                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
                        textHelpMulti = new DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs>(GWT.create(KeyField_PrpAcs.class), 7, false);
                        textHelpMulti.getDmGrid().addTextColumn(textHelpMulti.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
                        textHelpMulti.getDmGrid().initializeGrid(textHelpMulti.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
                        textHelpMulti.getHelper().addSort("name", DmSortType.ASC);
                        helpEvents(textHelpMulti, filterData_Mdl.getColumnName(), templateMdl.getSessionTable());
                        flwTd.add(vl.add(textHelpMulti));
                        textHelpMulti.setPlaceHolder("Equal To");
                        controlList.put(filterData_Mdl.getColumnName() + "Help", textHelpMulti);

                        if (!DmShared.xNull(filterData_Mdl.getHelpTag()).trim().equalsIgnoreCase("")) {
                            textHelpMulti.setTag(DmShared.xNull(filterData_Mdl.getHelpTag()));
                            textHelpMulti.setText(DmShared.xNull(filterData_Mdl.getHelpText()));
                        }
                        flwTr2.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
                        textBox = new DmTextBox(50, false);
                        flwTd.add(vl.add(textBox));
                        controlList.put(filterData_Mdl.getColumnName() + "Contains", textBox);
                        if (!DmShared.xNull(filterData_Mdl.getContains()).trim().equalsIgnoreCase("")) {
                            textBox.setText(DmShared.xNull(filterData_Mdl.getContains()));
                        }
                        textBox.getElement().setPropertyString("placeholder", "Begins With");

                        if (!isEven) {
                            flwTd.getElement().getStyle().setMarginRight(4, Style.Unit.PCT);
                        }

                        flwTr2.add(flwTd);
                        break;
                    default:
                        break;
                }
                flwTable.add(flwTr2);
            }
        }
        flwworkingArea.add(flwTable);
        if (noOfRow % 2 != 0) {
            noOfRow++;
        }
        height = noOfRow * 20;
        DmClient.setStyle(flwTable, "98%", "100%", null, null, DmPosition.CENTER, true);

        flwworkingArea.getElement().setAttribute("style", ";overflow-y: auto;");
        if (height > 450) {
            flwworkingArea.setHeight("470px");
        } else {
            flwworkingArea.setHeight(height + "px");
        }

//        flwworkingArea.getElement().setAttribute("style", "max-height:400px; overflow-y: auto;");
        
    }

    private void numericDataTypeColumn(FlowPanel flwTr, FilterDataPopup_Mdl filterData_Mdl, int noDecimalPlace, boolean isEven) {
        FlowPanel flwTd;
        DmTextBoxNumeric textBoxNumeric;

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", null, "left", null, DmPosition.NONE, false);
        textBoxNumeric = new DmTextBoxNumeric(noDecimalPlace, true, 10, false);
        flwTd.add(vl.add(textBoxNumeric));
        controlList.put(filterData_Mdl.getColumnName() + "from", textBoxNumeric);
        if (!DmShared.xNull(filterData_Mdl.getValueFrom()).trim().equalsIgnoreCase("")) {
            textBoxNumeric.setText(DmShared.xNull(filterData_Mdl.getValueFrom()));
        }
        textBoxNumeric.getElement().setPropertyString("placeholder", "Equal To");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        textBoxNumeric = new DmTextBoxNumeric(noDecimalPlace, true, 10, false);
        flwTd.add(vl.add(textBoxNumeric));
        controlList.put(filterData_Mdl.getColumnName() + "to", textBoxNumeric);
        if (!DmShared.xNull(filterData_Mdl.getValueTo()).trim().equalsIgnoreCase("")) {
            textBoxNumeric.setText(DmShared.xNull(filterData_Mdl.getValueTo()));
        }
        textBoxNumeric.getElement().setPropertyString("placeholder", "To");

        if (!isEven) {
            flwTd.getElement().getStyle().setMarginRight(4, Style.Unit.PCT);
        }
        flwTr.add(flwTd);
    }

    private List<FilterDataPopup_Mdl> getFilterDataList() {
        prepareRowList();
        return filterDataList;
    }

    private void prepareRowList() {
        DmTextBox textBox;
        DmTextBoxNumeric textBoxNumeric;
        DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> textHelpMulti;

        if (filterDataList != null) {
            for (FilterDataPopup_Mdl filterData_Mdl : filterDataList) {
                if (!(DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("isChecked") || DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("UniqueId")
                        || DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("formatMsg") || DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("isFiltered")
                        || DmShared.xNull(filterData_Mdl.getColumnName()).equalsIgnoreCase("SubCode"))) {
                    switch (DmShared.xNull(filterData_Mdl.getDataType()).toUpperCase()) {
                        case "DATE":
                        case "DATETIME":
                            textBox = (DmTextBox) controlList.get(filterData_Mdl.getColumnName() + "from");
                            filterData_Mdl.setValueFrom(textBox.getText());

                            textBox = (DmTextBox) controlList.get(filterData_Mdl.getColumnName() + "to");
                            filterData_Mdl.setValueTo(textBox.getText());
                            break;
                        case "VARCHAR":
                            textHelpMulti = (DmTextHelpMulti) controlList.get(filterData_Mdl.getColumnName() + "Help");
                            filterData_Mdl.setHelpTag(textHelpMulti.getTag());
                            filterData_Mdl.setHelpText(textHelpMulti.getText());

                            textBox = (DmTextBox) controlList.get(filterData_Mdl.getColumnName() + "Contains");
                            filterData_Mdl.setContains(textBox.getText());
                            break;
                        case "INT":
                        case "SMALLINT":
                        case "TINYINT":
                        case "BIGINT":
                            textBoxNumeric = (DmTextBoxNumeric) controlList.get(filterData_Mdl.getColumnName() + "from");
                            filterData_Mdl.setValueFrom(textBoxNumeric.getText());

                            textBoxNumeric = (DmTextBoxNumeric) controlList.get(filterData_Mdl.getColumnName() + "to");
                            filterData_Mdl.setValueTo(textBoxNumeric.getText());
                        case "FLOAT":

                        case "DOUBLE":
                            textBoxNumeric = (DmTextBoxNumeric) controlList.get(filterData_Mdl.getColumnName() + "from");
                            filterData_Mdl.setValueFrom(textBoxNumeric.getText());

                            textBoxNumeric = (DmTextBoxNumeric) controlList.get(filterData_Mdl.getColumnName() + "to");
                            filterData_Mdl.setValueTo(textBoxNumeric.getText());

                        default:
                            break;
                    }
                }
            }
        }
    }

    private void helpEvents(final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHelp, final String columnName, final String tableName) {
        txtHelp.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getHelp(columnName, tableName, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHelp).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHelp.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHelp).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }

        });
    }
}
