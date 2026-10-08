package dataman.dmToolkitR.dmGUI.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkitR.dmAlertBox.client.DmAlertBox;
import dataman.dmToolkitR.dmGUI.shared.*;
import dataman.dmToolkitR.dmGridHelp.client.DmGridRow;
import dataman.dmToolkitR.dmLabel.client.DmLabel;
import dataman.dmToolkitR.dmPageHelp.client.DmPageHelp;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextArea.client.DmTextArea;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import dataman.dmToolkitR.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkitR.dmTextHelp.client.DmTextHelp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class DmGUI extends DmGUI_Dsg implements IsWidget {

    public interface Callback {

        public void initialized();

        public String getGUITemplate();

        public Object addCustom(DmGUIControl_Mdl dmGUIControl_Mdl, Row flwTr, int allowColumns);

        public void guiDataCopied(DmGUI_Mdl gui_Mdl, List<String> listCustomWidgetToCopy, String copyFromDocId, HashMap<String, String> param);
    }

    private Callback callback;
    private final ServiceInvoker<DmGUI_SrvAsync> srvAsync;
    private DmGUI_Mdl gui_Mdl;
    private LinkedHashMap<String, Object> controlList;
    private Object tabIndexAfter = null;

    public DmGUI(int height) {
        super(height);
        srvAsync = new ServiceInvoker<>(GWT.create(DmGUI_Srv.class), DmGUI.class.getName());
    }

    public void setFocus() {
        if (controlList != null) {
            for (Object object : controlList.values()) {
//                if (topControl.viaList.get().contains(object)) {
//                    if (focus(object)) {
//                        break;
//                    }
//                }
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
        } else if (object instanceof DmTextHelp) {
            ((DmTextHelp<KeyFieldExtended>) object).setFocus(true);
            return true;
        } else if (object instanceof DmTextHelp) {
            ((DmTextHelp<KeyFieldExtended>) object).setFocus(true);
            return true;
        } else if (object instanceof CheckBox) {
            ((CheckBox) object).setFocus(true);
            return true;
        }

        return false;
    }

    public void setFocus(String saveInField) {
        Object object;

        if (controlList != null) {
            if (controlList.containsKey(saveInField)) {
                object = controlList.get(saveInField);
//                if (topControl.viaList.get().contains(object)) {
//                    focus(object);
//                }
            }
        }
    }

    public void setTabIndexAfter(Object tabIndexAfter) {
        this.tabIndexAfter = tabIndexAfter;
    }

    public void clear() {
        if (controlList != null) {
            for (Object object : controlList.values()) {
                // topControl.viaList.getViaListWidget().remove(new ViaListWidget("", (IsWidget) object));
            }
        }
        if (callback != null) {
            //callback.removeCustom(topControl);
        }
        controlList = new LinkedHashMap<>();
        mainContainer.clear();
    }

    public void initializeViaTemplateCode(final boolean withFocus) {
        initialize("", callback.getGUITemplate(), withFocus);
    }

    public void initializeViaConfigCode(String guiConfigCode, final boolean withFocus) {
        initialize(guiConfigCode, "", withFocus);
    }

    private void initialize(String guiConfigCode, String guiTemplateCode, final boolean withFocus) {
        srvAsync.invokeRPC(true).getGUI(guiConfigCode, guiTemplateCode, new AsyncCallback<DmResult<DmGUI_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DmGUI_Mdl> result) {
                if (result.isStatus()) {
                    initialize(result.getData());
                    if (withFocus) {
                        setFocus();
                    }
                } else {
                    new DmAlertBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void initializeViaTemplateCode() {
        initializeViaTemplateCode(false);
    }

    public void initialize(DmGUI_Mdl gui_Mdl) {
        String cssStyle = "";
        FlowPanel flwTd = null, flwTr = null;
        Row mainRow;
        DmLabel lable = null;
        DmTextArea textArea;
        DmTextBox textBox;
        CheckBox chkBox;
        DmTextBoxNumeric textBoxNumeric;
        DmTextHelp<KeyFieldExtended> textHelp;
        DmTextHelp<KeyFieldExtended> txtHelpMulti;
        Object tabIndexAfter = this.tabIndexAfter;

        this.gui_Mdl = gui_Mdl;
        clear();
        if (this.gui_Mdl != null) {
            for (DmGUIRow_Mdl guiRowMdl : this.gui_Mdl.getGuiRowList()) {

                mainRow = new Row();
                mainRow.setPull(Pull.LEFT);
                mainRow.setWidth("100%");
//                DmClient.setStyle(flwTr, "100%", null, "", null, DmPosition.NONE, true);
                for (DmGUIControl_Mdl guiControl : guiRowMdl.getGUIControlList()) {
                    switch (guiRowMdl.getGUIControlList().size()) {
                        case 1:
                            cssStyle = "col-lg-12 col-md-12 col-sm-12 col-xs-12 col-xx-12";
                            break;
                        case 2:
                            cssStyle = "col-lg-6 col-md-6 col-sm-6 col-xs-12 col-xx-12";
                            break;
                        case 3:
                            cssStyle = "col-lg-4 col-md-4 col-sm-6 col-xs-12 col-xx-12";
                            break;
                        default:
                            cssStyle = "col-lg-3 col-md-3 col-sm-6 col-xs-12 col-xx-12";
                            break;
                    }
                    if (guiControl.getDataType() != DmDataType.CUSTOM) {
                        flwTr=new FlowPanel();
                        flwTr.setStyleName(cssStyle);
                        
                        flwTd = new FlowPanel();
//                        DmClient.setStyle(flwTd, DmShared.xNull(guiControl.getCaptionWidth()).trim() + "%", null, "left", null, DmPosition.NONE, false);
                        lable = new DmLabel(guiControl.getFieldCaption());
                        lable.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                        lable.getElement().setAttribute("style", lable.getElement().getAttribute("style") + "; text-align: left !important");
                        if (!DmShared.xNull(guiControl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                            lable.getElement().setAttribute("style", lable.getElement().getAttribute("style") + ";" + guiControl.getCaptionStyle());
                        }
                        flwTd.add(lable);
                        flwTr.add(flwTd);

                        flwTd = new FlowPanel();
//                        DmClient.setStyle(flwTd, DmShared.xNull(guiControl.getControlWidth()).trim() + "%", null, "left", null, DmPosition.NONE, false);
                        flwTr.add(flwTd);
                        mainRow.add(flwTr);
                    }

                    switch (guiControl.getDataType()) {
                        case DATE:
                            final DmTextBox textBoxDate = new DmTextBox(Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
                            textBoxDate.addBlurHandler(new BlurHandler() {
                                @Override
                                public void onBlur(BlurEvent event) {
                                    textBoxDate.setText(DmClient.returnDate(textBoxDate.getText()));
                                }
                            });
                            flwTd.add(textBoxDate);
                            tabIndexAfter = textBoxDate;
                            textBoxDate.setText(DmShared.dNull(guiControl.getDefaultText()));
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textBoxDate.getElement().setAttribute("style", textBoxDate.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textBoxDate);
                            //mainRow.add(flwTd);
                            break;

                        case DOUBLE:
                            textBoxNumeric = new DmTextBoxNumeric(Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getAllowDecimalPlaces()).intValue())), guiControl.isAllowNegative(), Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
                            flwTd.add(textBoxNumeric);
                            tabIndexAfter = textBoxNumeric;
                            if (DmShared.vNull(guiControl.getDefaultText()).doubleValue() != 0) {
                                textBoxNumeric.setText(DmShared.vNull(guiControl.getDefaultText()).toString());
                            }
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textBoxNumeric.getElement().setAttribute("style", textBoxNumeric.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textBoxNumeric);
                            //mainRow.add(flwTd);
                            break;

                        case HELPSINGLE:
                            textHelp = new DmTextHelp<KeyFieldExtended>(DmShared.xNull(guiControl.getFieldCaption()), "Name", DmShared.iNull(guiControl.getHelpRowPerPage()).intValue(), guiControl.isMandatory());
                            textHelp.getHelp().addColumn("Name", "", "100%");
                            textHelp.getHelper().addSort("name", DmSortType.ASC);
                            //textHelp.initializeGrid(textHelp.getPropertyAccess().code(), 200, 0, 1);
                            textHelp.getHelp().addRowConfig(new DmPageHelp.RowConfigCallBack<KeyFieldExtended>() {

                                @Override
                                public void rowConfig(DmGridRow<KeyFieldExtended> row, KeyFieldExtended tRow) {
                                    row.getCell("Name").setValue(tRow.getSharedKeyFieldName());
                                }
                            });

                            helpEvents(textHelp, guiControl.getHelpSQLQueryBytes());
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textHelp.asWidget().getElement().setAttribute("style", textHelp.asWidget().getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }

                            flwTd.add(textHelp);
                            tabIndexAfter = textHelp;
                            textHelp.setText(DmShared.xNull(guiControl.getDefaultText()));
                            textHelp.setTag(DmShared.xNull(guiControl.getDefaultCode()));
                            controlList.put(guiControl.getSaveInFieldName(), textHelp);
                            //mainRow.add(flwTd);
                            break;

                        case HELPMULTI:
                            txtHelpMulti = new DmTextHelp<KeyFieldExtended>( DmShared.xNull(guiControl.getFieldCaption()),"name", DmShared.iNull(guiControl.getHelpRowPerPage()).intValue(), guiControl.isMandatory());
                            txtHelpMulti.getHelp().addColumn("Name", "", "100%");
                            txtHelpMulti.getHelper().addSort("name", DmSortType.ASC);
                            txtHelpMulti.getHelp().addRowConfig(new DmPageHelp.RowConfigCallBack<KeyFieldExtended>() {

                                @Override
                                public void rowConfig(DmGridRow<KeyFieldExtended> row, KeyFieldExtended tRow) {
                                    row.getCell("Name").setValue(tRow.getSharedKeyFieldName());
                                }
                            });

                            helpMultiEvents(txtHelpMulti, guiControl.getHelpSQLQueryBytes());
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                txtHelpMulti.asWidget().getElement().setAttribute("style", txtHelpMulti.asWidget().getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            flwTd.add(txtHelpMulti);
                            tabIndexAfter = txtHelpMulti;
                            txtHelpMulti.setText(DmShared.xNull(guiControl.getDefaultText()));
                            txtHelpMulti.setTag(DmShared.xNull(guiControl.getDefaultCode()));
                            controlList.put(guiControl.getSaveInFieldName(), txtHelpMulti);
                            //mainRow.add(flwTd);
                            break;

                        case INTEGER:
                            textBoxNumeric = new DmTextBoxNumeric(0, guiControl.isAllowNegative(), Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
                            flwTd.add(textBoxNumeric);
                            tabIndexAfter = textBoxNumeric;
                            if (DmShared.vNull(guiControl.getDefaultText()).doubleValue() != 0) {
                                textBoxNumeric.setText(DmShared.vNull(guiControl.getDefaultText()).toString());
                            }
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textBoxNumeric.getElement().setAttribute("style", textBoxNumeric.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textBoxNumeric);
                            //mainRow.add(flwTd);
                            break;

                        case STRING:
                            textBox = new DmTextBox(Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
                            flwTd.add(textBox);
                            tabIndexAfter = textBox;
                            textBox.setText(DmShared.xNull(guiControl.getDefaultText()));
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textBox.getElement().setAttribute("style", textBox.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textBox);
                            //mainRow.add(flwTd);
                            break;

                        case STRING_MULTILINE:
                             textArea = new DmTextArea(Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
                            textArea = new DmTextArea();
                             textArea.setReadOnly(true);
                             textArea.setHeight("34px");
                            textArea.getElement().getStyle().setColor("rgb(100, 0, 0)");

                            lable.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                            lable.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                            lable.getElement().getStyle().setColor("rgb(2, 136, 209)");
                            lable.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                            new DmGUI_MultiLine().addCallBack(textArea, lable, new DmGUI_MultiLine.CallBack() {

                                @Override
                                public void onOk(String textVal, DmTextArea textArea) {
                                    textArea.setText(DmShared.xNull(textVal).trim());
//                                    textArea.setTag(DmShared.xNull(textVal).trim());
                                    textArea.setFocus(true);
                                }

                                @Override
                                public void onClose(DmTextArea textArea) {
                                    textArea.setFocus(true);
                                }
                            });

                            flwTd.add(textArea);
                            tabIndexAfter = textArea;
                            textArea.setText(DmShared.xNull(guiControl.getDefaultText()));
//                            textArea.setTag(DmShared.xNull(guiControl.getDefaultText()));
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textArea.getElement().setAttribute("style", textArea.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textArea);
                            //mainRow.add(flwTd);
                            break;

                        case CHECKBOX:
                            chkBox = new CheckBox();
                            flwTd.add(chkBox);
                            tabIndexAfter = chkBox;
                            chkBox.setValue(DmShared.xNull(guiControl.getDefaultCode()).equalsIgnoreCase("1") ? true : false);
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                chkBox.getElement().setAttribute("style", chkBox.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), chkBox);
                            //mainRow.add(flwTd);
                            break;

                        case CUSTOM:
                            Object obj = callback.addCustom(guiControl, mainRow, guiRowMdl.getGUIControlList().size());
                            if (obj != null) {
                                tabIndexAfter = obj;
                                controlList.put(String.valueOf(obj.hashCode()), obj);
                            }
                            break;

                        default:
                            lable = new DmLabel(guiControl.getDefaultText());
                            flwTd.add(lable);
                            //mainRow.add(flwTd);
                            break;
                    }

                    mainContainer.add(mainRow);
                }
            }
            mainRow = new Row();
            mainRow.setPull(Pull.LEFT);
            mainRow.setWidth("100%");

//            DmClient.setStyle(flwTr,
//                    "100%", "20px", "left", null, DmPosition.NONE, true);
//            mainRow.add(flwTr);
//
////            mainRow.setCellHeight(flwTr,
////                    "100%");
            callback.initialized();
        }
    }

    private void helpEvents(final DmTextHelp<KeyFieldExtended> txtHelp, final byte[] helpSQLQuery) {
        txtHelp.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHelp(helpSQLQuery, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertBox(caught.getMessage(), txtHelp).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHelp.setAllData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertBox(result.getErrorMessage(), txtHelp).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(KeyFieldExtended tRow) {

            }
        });
    }

    private void helpMultiEvents(final DmTextHelp<KeyFieldExtended> txtHelpMulti, final byte[] helpSQLQuery) {
        txtHelpMulti.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHelp(helpSQLQuery, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertBox(caught.getMessage(), txtHelpMulti).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHelpMulti.setAllData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertBox(result.getErrorMessage(), txtHelpMulti).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(KeyFieldExtended tRow) {

            }
        });
    }

    public DmGUI_Mdl getGUI_Mdl() {
        prepareRowList();
        return gui_Mdl;
    }

    private void prepareRowList() {
        DmTextArea textArea;
        DmTextBox textBox;
        CheckBox chkBox;
        DmTextBoxNumeric textBoxNumeric;
        DmTextHelp<KeyFieldExtended> textHelp;
        DmTextHelp<KeyFieldExtended> txtHelpMulti;

        if (this.gui_Mdl != null) {
            if (this.gui_Mdl.getGuiRowList() != null) {
                for (DmGUIRow_Mdl guiRow : this.gui_Mdl.getGuiRowList()) {
                    for (DmGUIControl_Mdl guiControl : guiRow.getGUIControlList()) {

                        switch (guiControl.getDataType()) {
                            case DATE:
                                textBox = (DmTextBox) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultText(textBox.getText());
                                break;

                            case DOUBLE:
                                textBoxNumeric = (DmTextBoxNumeric) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultText(textBoxNumeric.getText());
                                break;

                            case HELPSINGLE:
                                textHelp = (DmTextHelp) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultText(textHelp.getText());
                                guiControl.setDefaultCode(textHelp.getTag());
                                break;

                            case HELPMULTI:
                                txtHelpMulti = (DmTextHelp) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultText(txtHelpMulti.getText());
                                guiControl.setDefaultCode(txtHelpMulti.getTag());
                                break;

                            case INTEGER:
                                textBoxNumeric = (DmTextBoxNumeric) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultText(textBoxNumeric.getText());
                                break;

                            case STRING:
                                textBox = (DmTextBox) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultText(textBox.getText());
                                break;

                            case STRING_MULTILINE:
                                textArea = (DmTextArea) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultText(textArea.getText());
                                break;

                            case CHECKBOX:
                                chkBox = (CheckBox) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultCode(chkBox.getValue() == true ? "1" : "0");
                                guiControl.setDefaultText(chkBox.getValue() == true ? "1" : "0");
                                break;

                            case CUSTOM:
                                break;

                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public Widget asWidget() {
        return mainContainer;
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

//        if (aSource.equals(btnMenu)) {
//            if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
//                repairOverHead.setEnabled(false);
//            } else {
//                repairOverHead.setEnabled(true);
//            }
//            menu.show(btnMenu);
//        }
    }

//    @Override
//    void menuClick(SelectionEvent<MenuItem> event) {
//        if (event.getSelectedItem().getText().equalsIgnoreCase(repairOverHead.getText())) {
////            if (!topControl.getMode().trim().equalsIgnoreCase("Browse")) {
////
////            }
//        }
//    }
    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public String getConfigCode() {
        String code = "";
        if (this.gui_Mdl != null) {
            if (this.gui_Mdl.getGuiRowList() != null && this.gui_Mdl.getGuiRowList().size() > 0) {
                code = this.gui_Mdl.getGuiRowList().get(0).getConfigCode();
            }
        }
        return code;
    }

    public String getReportNamePrefix() {
        String code = "";
        if (this.gui_Mdl != null) {
            code = gui_Mdl.getReportNamePrefix();
        }
        return code;
    }

    public void copyGUI(String guiCodeFrom, final String copyFromDocId, final HashMap<String, String> param) {
        srvAsync.invokeRPC(true).getCopyGUIDetails(guiCodeFrom, copyFromDocId, new AsyncCallback<DmResult<DmGUI_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DmGUI_Mdl> result) {
                if (result.isStatus()) {
                    copyGUIData(getGUI_Mdl(), result.getData(), copyFromDocId, param);
                } else {
                    new DmAlertBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void copyGUIData(DmGUI_Mdl guiTo_Mdl, DmGUI_Mdl guiFrom_Mdl, String copyFromDocId, HashMap<String, String> param) {
        if (guiFrom_Mdl != null && guiTo_Mdl != null) {
            boolean isTableSame = false, isCopy = false;

            /*=========================< Check whether Table is same or not >============================*/
            if (DmShared.xNull(guiFrom_Mdl.getTableName()).trim().equalsIgnoreCase(DmShared.xNull(guiTo_Mdl.getTableName()).trim())) {
                isTableSame = true;
            }

            /*=========================< Clear Custom Widgets list >============================*/
            guiTo_Mdl.getListCustomWidget().clear();

            for (DmGUIRow_Mdl guiRowTo : guiTo_Mdl.getGuiRowList()) {
                if (DmShared.iNull(guiRowTo.getConfigCode()).longValue() != 0) {

                    for (DmGUIControl_Mdl guiControlTo : guiRowTo.getGUIControlList()) {
                        if (guiControlTo.getDataType() != null) {

                            for (DmGUIRow_Mdl guiRowFrom : guiFrom_Mdl.getGuiRowList()) {
                                if (DmShared.iNull(guiRowFrom.getConfigCode()).longValue() != 0) {

                                    for (DmGUIControl_Mdl guiControlFrom : guiRowFrom.getGUIControlList()) {
                                        if (guiControlTo.getDataType() == guiControlFrom.getDataType()) {
                                            if (guiControlTo.isCopy()) {

                                                if (guiControlTo.getDataType() == DmDataType.CUSTOM) {
                                                    if (!DmShared.xNull(guiControlTo.getCustomFieldName()).trim().equalsIgnoreCase("")) {
                                                        if (DmShared.xNull(guiControlFrom.getCustomFieldName()).trim().equalsIgnoreCase(DmShared.xNull(guiControlTo.getCustomFieldName()).trim())) {
                                                            if (!guiTo_Mdl.getListCustomWidget().contains(DmShared.xNull(guiControlTo.getCustomFieldName()).trim())) {
                                                                guiTo_Mdl.getListCustomWidget().add(DmShared.xNull(guiControlTo.getCustomFieldName()).trim());
                                                            }
                                                        }
                                                    }
                                                } else {

                                                    isCopy = false;
                                                    if (isTableSame) {
                                                        if (!DmShared.xNull(guiControlTo.getSaveInFieldName()).trim().equalsIgnoreCase("")) {
                                                            if (DmShared.xNull(guiControlFrom.getSaveInFieldName()).trim().equalsIgnoreCase(DmShared.xNull(guiControlTo.getSaveInFieldName()).trim())) {
                                                                isCopy = true;
                                                            }
                                                        }
                                                    } else {
                                                        if (!DmShared.xNull(guiControlTo.getFieldName()).trim().equalsIgnoreCase("")) {
                                                            if (DmShared.xNull(guiControlFrom.getFieldName()).trim().equalsIgnoreCase(DmShared.xNull(guiControlTo.getFieldName()).trim())) {
                                                                isCopy = true;
                                                            }
                                                        }
                                                    }
                                                    if (isCopy) {
                                                        guiControlTo.setDefaultCode(DmShared.xNull(guiControlFrom.getDefaultCode()));
                                                        guiControlTo.setDefaultText(DmShared.xNull(guiControlFrom.getDefaultText()));
                                                        break;
                                                    }
                                                }
                                            }

                                        }
                                    }

                                }
                            }

                        }
                    }

                }
            }
            if (callback != null) {
                callback.guiDataCopied(guiTo_Mdl, guiTo_Mdl.getListCustomWidget(), copyFromDocId, param);
            }
        }
    }

}
