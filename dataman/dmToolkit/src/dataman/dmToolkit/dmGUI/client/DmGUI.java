package dataman.dmToolkit.dmGUI.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmGUI.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.self.shared.ViaListWidget;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DmGUI extends DmGUI_Dsg implements IsWidget {

    public interface Callback {

        public void initialized();

        public String getGUITemplate();

        public Object addCustom(DmGUIControl_Mdl dmGUIControl_Mdl, FlowPanel flwTr, Object tabIndexAfter);

        public void removeCustom(DmTopControl topControl);

        public void guiDataCopied(DmGUI_Mdl gui_Mdl, List<String> listCustomWidgetToCopy, String copyFromDocId, HashMap<String, String> param);
    }

    private Callback callback;
    private DmTopControl topControl;
    private ServiceInvoker<DmGUI_SrvAsync> srvAsync;
    private DmGUI_Mdl gui_Mdl;
    private LinkedHashMap<String, Object> controlList;
    private Object tabIndexAfter = null;

    private void btnEvents(Button[] btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    btnClick((Button) event.getSource());
                }
            });
        }
    }

    public DmGUI(DmTopControl topcontrol, int height) {
        super(height);
        this.topControl = topcontrol;
        btnEvents(new Button[]{btnMenu});
        srvAsync = new ServiceInvoker<>(GWT.create(DmGUI_Srv.class), DmGUI.class.getName());
    }

    public void setBannerVisible(boolean isVisible) {
        hrtBanner.setVisible(isVisible);
    }

    public void setFocus() {
        if (controlList != null) {
            for (Object object : controlList.values()) {
                if (topControl.viaList.get().contains(object)) {
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
        } else if (object instanceof DmTextHelp) {
            ((DmTextHelp<KeyField, KeyField_PrpAcs>) object).setFocus(true);
            return true;
        } else if (object instanceof DmTextHelpMulti) {
            ((DmTextHelpMulti<KeyField, KeyField_PrpAcs>) object).setFocus(true);
            return true;
        } else if (object instanceof DmCheckBox) {
            ((DmCheckBox) object).setFocus(true);
            return true;
        }

        return false;
    }

    public void setFocus(String saveInField) {
        Object object;

        if (controlList != null) {
            if (controlList.containsKey(saveInField)) {
                object = controlList.get(saveInField);
                if (topControl.viaList.get().contains(object)) {
                    focus(object);
                }
            }
        }
    }

    public void setTabIndexAfter(Object tabIndexAfter) {
        this.tabIndexAfter = tabIndexAfter;
    }

    public void clear() {
        if (controlList != null) {
            for (Object object : controlList.values()) {
                topControl.viaList.getViaListWidget().remove(new ViaListWidget("", (IsWidget) object));
            }
        }
        if (callback != null) {
            callback.removeCustom(topControl);
        }
        controlList = new LinkedHashMap<>();
        workingAreaVrt.clear();
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
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DmGUI_Mdl> result) {
                if (result.isStatus()) {
                    initialize(result.getData());
                    if (withFocus) {
                        setFocus();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void initializeViaTemplateCode() {
        initializeViaTemplateCode(false);
    }

    public void initialize(DmGUI_Mdl gui_Mdl) {
        FlowPanel flwTd = null, flwTr;
        DmLabel lable = null;
        DmTextArea textArea;
        DmTextBox textBox;
        DmCheckBox chkBox;
        DmTextBoxNumeric textBoxNumeric;
        DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> textHelp;
        DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHelpMulti;
        Object tabIndexAfter = this.tabIndexAfter;

        this.gui_Mdl = gui_Mdl;
        clear();
        if (this.gui_Mdl != null) {
            for (DmGUIRow_Mdl guiRowMdl : this.gui_Mdl.getGuiRowList()) {

                flwTr = new FlowPanel();
                DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
                for (DmGUIControl_Mdl guiControl : guiRowMdl.getGUIControlList()) {

                    if (guiControl.getDataType() != DmDataType.CUSTOM) {
                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, DmShared.xNull(guiControl.getCaptionWidth()).trim() + "%", null, "left", null, DmPosition.NONE, false);
                        lable = new DmLabel(guiControl.getFieldCaption());
                        lable.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                        if (!DmShared.xNull(guiControl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                            lable.getElement().setAttribute("style", lable.getElement().getAttribute("style") + ";" + guiControl.getCaptionStyle());
                        }
                        flwTd.add(lable);
                        flwTr.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, DmShared.xNull(guiControl.getControlWidth()).trim() + "%", null, "left", null, DmPosition.NONE, false);
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
                            flwTd.add(topControl.viaList.add(textBoxDate, tabIndexAfter));
                            tabIndexAfter = textBoxDate;
                            textBoxDate.setText(DmShared.dNull(guiControl.getDefaultText()));
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textBoxDate.getElement().setAttribute("style", textBoxDate.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textBoxDate);
                            flwTr.add(flwTd);
                            break;

                        case DOUBLE:
                            textBoxNumeric = new DmTextBoxNumeric(Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getAllowDecimalPlaces()).intValue())), guiControl.isAllowNegative(), Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
                            flwTd.add(topControl.viaList.add(textBoxNumeric, tabIndexAfter));
                            tabIndexAfter = textBoxNumeric;
                            if (DmShared.vNull(guiControl.getDefaultText()).doubleValue() != 0) {
                                textBoxNumeric.setText(DmShared.vNull(guiControl.getDefaultText()).toString());
                            }
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textBoxNumeric.getElement().setAttribute("style", textBoxNumeric.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textBoxNumeric);
                            flwTr.add(flwTd);
                            break;

                        case HELPSINGLE:
                            textHelp = new DmTextHelp<KeyFieldExtended, KeyField_PrpAcs>(GWT.create(KeyField_PrpAcs.class), Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getHelpRowPerPage()).intValue())), guiControl.isMandatory());
                            textHelp.getDmGrid().addTextColumn(textHelp.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
                            textHelp.getDmGrid().initializeGrid(textHelp.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
                            textHelp.getHelper().addSort("name", DmSortType.ASC);
                            helpEvents(textHelp, guiControl.getHelpSQLQueryBytes());
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textHelp.asWidget().getElement().setAttribute("style", textHelp.asWidget().getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            flwTd.add(topControl.viaList.add(textHelp, tabIndexAfter));
                            tabIndexAfter = textHelp;
                            textHelp.setText(DmShared.xNull(guiControl.getDefaultText()));
                            textHelp.setTag(DmShared.xNull(guiControl.getDefaultCode()));
                            controlList.put(guiControl.getSaveInFieldName(), textHelp);
                            flwTr.add(flwTd);
                            break;

                        case HELPMULTI:
                            txtHelpMulti = new DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs>(GWT.create(KeyField_PrpAcs.class), Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getHelpRowPerPage()).intValue())), guiControl.isMandatory());
                            txtHelpMulti.getDmGrid().addTextColumn(txtHelpMulti.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
                            txtHelpMulti.getDmGrid().initializeGrid(txtHelpMulti.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
                            txtHelpMulti.getHelper().addSort("name", DmSortType.ASC);
                            helpMultiEvents(txtHelpMulti, guiControl.getHelpSQLQueryBytes());
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                txtHelpMulti.asWidget().getElement().setAttribute("style", txtHelpMulti.asWidget().getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            flwTd.add(topControl.viaList.add(txtHelpMulti, tabIndexAfter));
                            tabIndexAfter = txtHelpMulti;
                            txtHelpMulti.setText(DmShared.xNull(guiControl.getDefaultText()));
                            txtHelpMulti.setTag(DmShared.xNull(guiControl.getDefaultCode()));
                            controlList.put(guiControl.getSaveInFieldName(), txtHelpMulti);
                            flwTr.add(flwTd);
                            break;

                        case INTEGER:
                            textBoxNumeric = new DmTextBoxNumeric(0, guiControl.isAllowNegative(), Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
                            flwTd.add(topControl.viaList.add(textBoxNumeric, tabIndexAfter));
                            tabIndexAfter = textBoxNumeric;
                            if (DmShared.vNull(guiControl.getDefaultText()).doubleValue() != 0) {
                                textBoxNumeric.setText(DmShared.vNull(guiControl.getDefaultText()).toString());
                            }
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textBoxNumeric.getElement().setAttribute("style", textBoxNumeric.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textBoxNumeric);
                            flwTr.add(flwTd);
                            break;

                        case STRING:
                            textBox = new DmTextBox(Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
                            flwTd.add(topControl.viaList.add(textBox, tabIndexAfter));
                            tabIndexAfter = textBox;
                            textBox.setText(DmShared.xNull(guiControl.getDefaultText()));
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textBox.getElement().setAttribute("style", textBox.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textBox);
                            flwTr.add(flwTd);
                            break;

                        case HINDI:
                            textBox = new DmTextBox(Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
                            flwTd.add(topControl.viaList.add(textBox, tabIndexAfter));
                            tabIndexAfter = textBox;
                            textBox.setText(DmShared.xNull(guiControl.getDefaultText()));
                            //==================< Regional Language >=================
                            textBox.addStyleName(DmEnum.RegionalLanguage.getValueFrom(DmClient.getRegionalLanguage()).getCssStyleName());
                            //=========================================================
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textBox.getElement().setAttribute("style", textBox.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textBox);
                            flwTr.add(flwTd);
                            break;

                        case STRING_MULTILINE:
                            textArea = new DmTextArea(Integer.valueOf(String.valueOf(DmShared.vNull(guiControl.getMaxLength()).intValue())), guiControl.isMandatory());
//                            textArea.setReadOnly(true);
//                            textArea.getElement().getStyle().setColor("rgb(100, 0, 0)");

                            lable.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                            lable.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                            lable.getElement().getStyle().setColor("blue");
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

                            flwTd.add(topControl.viaList.add(textArea, tabIndexAfter));
                            tabIndexAfter = textArea;
                            textArea.setText(DmShared.xNull(guiControl.getDefaultText()));
//                            textArea.setTag(DmShared.xNull(guiControl.getDefaultText()));
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                textArea.getElement().setAttribute("style", textArea.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), textArea);
                            flwTr.add(flwTd);
                            break;

                        case CHECKBOX:
                            chkBox = new DmCheckBox();
                            flwTd.add(topControl.viaList.add(chkBox, tabIndexAfter));
                            tabIndexAfter = chkBox;
                            chkBox.setValue(DmShared.xNull(guiControl.getDefaultCode()).equalsIgnoreCase("1") ? true : false);
                            if (!DmShared.xNull(guiControl.getControlStyle()).trim().equalsIgnoreCase("")) {
                                chkBox.getElement().setAttribute("style", chkBox.getElement().getAttribute("style") + ";" + guiControl.getControlStyle());
                            }
                            controlList.put(guiControl.getSaveInFieldName(), chkBox);
                            flwTr.add(flwTd);
                            break;

                        case CUSTOM:
                            Object obj = callback.addCustom(guiControl, flwTr, tabIndexAfter);
                            if (obj != null) {
                                tabIndexAfter = obj;
                                controlList.put(String.valueOf(obj.hashCode()), obj);
                            }
                            break;

                        default:
                            lable = new DmLabel(guiControl.getDefaultText());
                            flwTd.add(lable);
                            flwTr.add(flwTd);
                            break;
                    }

                    workingAreaVrt.add(flwTr);
                }
            }
        }

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        workingAreaVrt.add(flwTr);
        workingAreaVrt.setCellHeight(flwTr, "100%");
        callback.initialized();
    }

    private void helpEvents(final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHelp, final byte[] helpSQLQuery) {
        txtHelp.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHelp(helpSQLQuery, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void helpMultiEvents(final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHelpMulti, final byte[] helpSQLQuery) {
        txtHelpMulti.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHelp(helpSQLQuery, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHelpMulti).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHelpMulti.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHelpMulti).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
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
        DmCheckBox chkBox;
        DmTextBoxNumeric textBoxNumeric;
        DmTextHelp<KeyField, KeyField_PrpAcs> textHelp;
        DmTextHelpMulti<KeyField, KeyField_PrpAcs> txtHelpMulti;

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
                                txtHelpMulti = (DmTextHelpMulti) controlList.get(guiControl.getSaveInFieldName());
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
                                
                            case HINDI:
                                textBox = (DmTextBox) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultText(textBox.getText());
                                break;

                            case STRING_MULTILINE:
                                textArea = (DmTextArea) controlList.get(guiControl.getSaveInFieldName());
                                guiControl.setDefaultText(textArea.getText());
                                break;

                            case CHECKBOX:
                                chkBox = (DmCheckBox) controlList.get(guiControl.getSaveInFieldName());
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
        return vrtForm;
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

        if (aSource.equals(btnMenu)) {
            if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
                repairOverHead.setEnabled(false);
            } else {
                repairOverHead.setEnabled(true);
            }
            menu.show(btnMenu);
        }
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(repairOverHead.getText())) {
            if (!topControl.getMode().trim().equalsIgnoreCase("Browse")) {

            }
        }
    }

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
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DmGUI_Mdl> result) {
                if (result.isStatus()) {
                    copyGUIData(getGUI_Mdl(), result.getData(), copyFromDocId, param);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
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
