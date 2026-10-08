package dataman.dmToolkit.dmHelpGridMulti.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import com.google.gwt.core.client.Scheduler;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.ArrayList;
import java.util.List;

public class DmHelpGridMulti<T1, T2> extends DmHelpGridMulti_Dsg implements IsWidget, KeyDownHandler, DmRootEvents, AttachEvent.Handler, ClickHandler, KeyUpHandler {

    public interface HelpGridMultiCallBack<T1> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);

        public void getSelection(boolean blnStatus, String keyCode, String keyName);
    }
    private List<KeyField> listKeyFieldPreserveSelection;
    private CheckBoxSelectionModel checkBoxSelectionModel;
    private DmGrid<T1, T2> dmGrid;
    private HelpGridMultiCallBack helpGridMultiCallBack;
    private DmHelper helper;
    private String searchPrvValue = "";
    private boolean autoHeight = true;
    private String searchDbFieldName;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    private void gridEvents(Object t2PrpAcs) {
        dmGrid = new DmGrid<T1, T2>(t2PrpAcs);
        checkBoxSelectionModel = getDmGrid().addCheckSelectionColumn(SelectionMode.MULTI);
        dmGrid.addInitializeGridCallBackHandler(new DmGrid.InitializeGridCallBack() {
            @Override
            public void onInitialized() {
                flwGrid.clear();
                dmGrid.getGrid().getElement().getStyle().setBorderStyle(Style.BorderStyle.NONE);
                flwGrid.add(dmGrid.getGrid());
                dmGrid.getGrid().addDomHandler(DmHelpGridMulti.this, KeyDownEvent.getType());
                if (autoHeight) {
                    if (helper.getRowsPerPage() > 0) {
                        dmGrid.getGrid().setHeight(String.valueOf(((helper.getRowsPerPage() + 1) * 22) + 2) + "px");
                    }
                }
                ppForm.setWidth(dmGrid.getWidth() + 20 + "px");
                dmGrid.getGrid().setSelectionModel(checkBoxSelectionModel);

                setSearchColumn(1);

                dmGrid.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {

                    @Override
                    public void onHeaderClick(HeaderClickEvent event) {
                        setSearchColumn(event.getColumnIndex());
                    }
                });
            }
        });

        checkBoxSelectionModel.addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<KeyField>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<KeyField> event) {
                preserveSelection(event.getSelection());
            }
        });
    }

    private void setSearchColumn(int columnIndex) {
        if (columnIndex == 0) {
//            columnIndex = 1;
            return;
        }
        DmColumnConfig col = ((DmColumnConfig) dmGrid.getGrid().getColumnModel().getColumn(columnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        helper.getFilter().get(0).setColumnName(searchDbFieldName);
        helper.getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearch.setText("");

        for (int i = 0; i < dmGrid.getGrid().getColumnModel().getColumns().size(); i++) {
            if (i != 0) {
                ColumnConfig col1 = dmGrid.getGrid().getColumnModel().getColumns().get(i);
                col1.setColumnHeaderClassName("dm-Garbage");
            }
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        dmGrid.getGrid().getView().refresh(true);
        txtSearch.setFocus(true);
    }

    private void preserveSelection(List<KeyField> listKeyFieldsSelected) {
        List<KeyField> listGridModel;

        try {
            listGridModel = (List<KeyField>) dmGrid.getGrid().getStore().getAll();

            if (listKeyFieldPreserveSelection == null) {
                listKeyFieldPreserveSelection = new ArrayList();
            }

            for (int i = 0; i < listGridModel.size(); i++) {
                removePreserveSelection(listGridModel.get(i).getSharedKeyFieldCode());
            }

            if (listKeyFieldsSelected != null) {
                for (int i = 0; i < listKeyFieldsSelected.size(); i++) {
                    if (!DmShared.xNull(listKeyFieldsSelected.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                        if (containsPreserveSelection(listKeyFieldsSelected.get(i).getSharedKeyFieldCode()) < 0) {
                            listKeyFieldPreserveSelection.add(new KeyField(DmShared.xNull(listKeyFieldsSelected.get(i).getSharedKeyFieldCode()), DmShared.xNull(listKeyFieldsSelected.get(i).getSharedKeyFieldName())));
                        }
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            setSelectionToTextArea();
        }
    }

    private int containsPreserveSelection(String code) {
        int index = -1;
        if (listKeyFieldPreserveSelection != null) {
            for (int i = 0; i < listKeyFieldPreserveSelection.size(); i++) {
                if (DmShared.xNull(listKeyFieldPreserveSelection.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(code).trim())) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    private void removePreserveSelection(String code) {
        int index = -1;
        index = containsPreserveSelection(code);
        if (index >= 0) {
            listKeyFieldPreserveSelection.remove(index);
        }
    }

    private void applySelection() {
        List<KeyField> listGridModel;
        List<T1> listSelected = new ArrayList();

        try {
            listGridModel = (List<KeyField>) dmGrid.getGrid().getStore().getAll();

            for (int i = 0; i < listGridModel.size(); i++) {
                if (containsPreserveSelection(listGridModel.get(i).getSharedKeyFieldCode()) >= 0) {
                    listSelected.add(dmGrid.getGrid().getStore().get(i));
                }
            }

            dmGrid.getGrid().getSelectionModel().setSelection(listSelected);
            dmGrid.getGrid().getView().refresh(false);
        } catch (Exception e) {
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootKeyDown(rootEvent);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == dmGrid.getGrid()) {
            gridKeydown(event);
        } else if (event.getSource() == txtSearch) {
            txtKeyDown(event);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            if (dmGrid.getGrid().getStore().size() <= 0) {
                reQuery();
            }

            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtSearch.setFocus(true);
                }
            });
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        txtKeyUp(event);
    }

    private void btnEvents(Button[] btn) {
        for (Button btn1 : btn) {
            btn1.addClickHandler(this);
        }
    }

    private void txtKeyEvents() {
        txtSearch.addDomHandler(this, KeyDownEvent.getType());
        txtSearch.addKeyUpHandler(this);
    }

    public void addHelpGridMultiCallBack(HelpGridMultiCallBack helpGridCallBack) {
        this.helpGridMultiCallBack = helpGridCallBack;
    }

    public void setData(List<T1> lstData, DmPaging pageingVariables) {
        preserveSelection((List<KeyField>) dmGrid.getGrid().getSelectionModel().getSelectedItems());
        helper.setPaging(pageingVariables);
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        if (lstData.size() > 0) {
            dmGrid.getGrid().getStore().addAll(lstData);
        }
        setDisplay(pageingVariables);
        applySelection();
    }

    public void show(final int intLeft, final int intTop, final String strDefaultValue, final String tag, final String text) {
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        ppForm.setPopupPosition(intLeft, intTop);
        ppForm.show();
        flwTmpScrolling.setHeight(dmGrid.getGrid().getOffsetHeight() + "px");

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearch.setText(strDefaultValue);
                reQuery();
                txtSearch.setFocus(true);
                txtSearch.setSelectionRange(DmShared.xNull(txtSearch.getText()).length(), 0);
                setPreserveSelection(tag, text);
            }
        });
    }

    public void show(final UIObject uiObject, final String strDefaultValue, final String tag, final String text) {
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        ppForm.showRelativeTo(uiObject);
        ppForm.show();
        flwTmpScrolling.setHeight(dmGrid.getGrid().getOffsetHeight() + "px");

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearch.setText(strDefaultValue);
                reQuery();
                txtSearch.setFocus(true);
                setPreserveSelection(tag, text);
            }
        });
    }

    private void setPreserveSelection(String tag, String text) {
        String[] code, name;
        KeyField keyField = null;
        listKeyFieldPreserveSelection = new ArrayList<>();

        if (tag == null || text == null) {
            tag = "";
            text = "";
        }
        code = tag.split(",");
        name = text.split(",");
        for (int i = 0; i < code.length; i++) {
            keyField = new KeyField();
            keyField.setSharedKeyFieldCode(code[i]);
            keyField.setSharedKeyFieldName(name[i]);
            listKeyFieldPreserveSelection.add(keyField);
        }
        applySelection();
    }

    private void setSelectionToTextArea() {
        txtSelected.setText("");
        if (listKeyFieldPreserveSelection != null) {
            for (KeyField keyFieldMdl : listKeyFieldPreserveSelection) {
                if (!DmShared.xNull(keyFieldMdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    if (DmShared.xNull(txtSelected.getText()).trim().equalsIgnoreCase("")) {
                        txtSelected.setText(DmShared.xNull(keyFieldMdl.getSharedKeyFieldName()));
                    } else {
                        txtSelected.setText(DmShared.xNull(txtSelected.getText()) + ", " + DmShared.xNull(keyFieldMdl.getSharedKeyFieldName()).trim());
                    }
                }
            }
        }
    }

    public DmGrid<T1, T2> getDmGrid() {
        return dmGrid;
    }

    public DmHelper getHelper() {
        return helper;
    }

    public boolean isAutoHeight() {
        return autoHeight;
    }

    public void setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;
    }

    public DmHelpGridMulti(Object t2PrpAcs, int rowsPerPage) {
        super();
        gridEvents(t2PrpAcs);
        ppForm.addAttachHandler(this);
        txtKeyEvents();
        btnEvents(new Button[]{btnOk, btnCancel, btnFirst, btnPrevious, btnNext, btnLast, btnClearText});
        helper = new <T1> DmHelper(0, rowsPerPage);
        helper.addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
    }

    @Override
    public Widget asWidget() {
        return ppForm;
    }

    private void reQuery() {
        if (helpGridMultiCallBack != null) {
            helper.setRowsStartFrom(0);
            helper.getFilter().get(0).setValue1(txtSearch.getText());
            helpGridMultiCallBack.populateData(helper, false, false);
        }
    }

    private void clearSelectedtext() {
        listKeyFieldPreserveSelection = new ArrayList<KeyField>();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        dmGrid.getGrid().getView().refresh(true);
        txtSelected.setText("");
        txtSelected.setTag("");
    }

    private void txtKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearch.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearch.getText();
    }

    private void btnClick(Button aSource) {
        String keyCode, keyName;

        if (!aSource.isVisible()) {
            return;
        }
        if (!aSource.isEnabled()) {
            return;
        }
        if (!aSource.isAttached()) {
            return;
        }

        if (aSource.equals(btnOk)) {
            preserveSelection((List<KeyField>) dmGrid.getGrid().getSelectionModel().getSelectedItems());
            if (dmGrid.getGrid().getStore().size() > 0) {
                if (dmGrid.getGrid().getSelectionModel().getSelectedItem() == null) {
                    //checkBoxSelectionModel.setSelectAllChecked(true);
                }

                if (helpGridMultiCallBack != null) {
                    keyCode = "";
                    keyName = "";
                    if (listKeyFieldPreserveSelection != null) {
                        for (int i = 0; i < listKeyFieldPreserveSelection.size(); i++) {
                            if (!keyCode.equalsIgnoreCase("")) {
                                keyCode += ",";
                                keyName += ", ";
                            }

                            keyCode += listKeyFieldPreserveSelection.get(i).getSharedKeyFieldCode();
                            keyName += listKeyFieldPreserveSelection.get(i).getSharedKeyFieldName();
                        }
                    }

                    helpGridMultiCallBack.getSelection(true, keyCode, keyName);
                    //checkBoxSelectionModel.getSelectedItems()
                }
            }
            ppForm.hide();
        } else if (aSource.equals(btnCancel)) {
            helpGridMultiCallBack.getSelection(false, null, null);
            ppForm.hide();
        } else if (aSource.equals(btnFirst)) {
            navigation(btnFirst);
        } else if (aSource.equals(btnPrevious)) {
            navigation(btnPrevious);
        } else if (aSource.equals(btnNext)) {
            navigation(btnNext);
        } else if (aSource.equals(btnLast)) {
            navigation(btnLast);
        } else if (aSource.equals(btnClearText)) {
            clearSelectedtext();
        }
    }

    private void navigation(Button btnPressed) {
        boolean blnToEndOfFile = false;

        if (helpGridMultiCallBack != null) {
            if (btnFirst == btnPressed) {
                helper.setRowsStartFrom(0);
            } else if (btnPrevious == btnPressed) {
                helper.setRowsStartFrom(helper.getRowsStartFrom() - helper.getRowsPerPage());
            } else if (btnNext == btnPressed) {
                helper.setRowsStartFrom(helper.getRowsStartFrom() + helper.getRowsPerPage());
            } else if (btnLast == btnPressed) {
                blnToEndOfFile = true;
            }

            helper.getFilter().get(0).setValue1(txtSearch.getText());
            helpGridMultiCallBack.populateData(helper, blnToEndOfFile, false);
        }
    }

    private void setDisplay(DmPaging pageingVariables) {
        if (pageingVariables.isBeginOfFile()) {
            btnFirst.setVisible(false);
            btnPrevious.setVisible(false);
        } else {
            btnFirst.setVisible(true);
            btnPrevious.setVisible(true);
        }

        if (pageingVariables.isEndOfFile()) {
            btnNext.setVisible(false);
            btnLast.setVisible(false);

        } else {
            btnNext.setVisible(true);
            btnLast.setVisible(true);
        }

        if (dmGrid.getGrid().getStore().size() > 0) {
            btnOk.setVisible(true);
        } else {
            btnOk.setVisible(false);
        }
    }

    private void txtKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            btnClick(btnOk);
        } else if (event.isControlKeyDown() == false && event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            if (dmGrid.getGrid().getStore().size() > 0) {
                checkBoxSelectionModel.select(0, true);
                checkBoxSelectionModel.getGrid().focus();
            }
        }
    }

    private void rootKeyDown(DmRootEvent event) {
        event.stopPropagation();
        if (event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            btnClick(btnCancel);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
            btnClick(btnFirst);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_LEFT) {
            btnClick(btnPrevious);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_RIGHT) {
            btnClick(btnNext);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
            btnClick(btnLast);
        }
    }

    private void gridKeydown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            btnClick(btnOk);
            event.preventDefault();
            event.stopPropagation();
        }
    }
}
