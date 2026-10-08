package dataman.dmToolkit.dmTopControl.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.VoucherPermission;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class DmTopControl extends DmTopControl_Dsg implements IsWidget, DmRootEvents, AttachEvent.Handler, ClickHandler {

    public interface iMoveRec {

        public void moveRec(String strFetchCode);
    }

    public interface eNew {

        public void newClicked();
    }

    public interface eEdit {

        public void editClicked();
    }

    public interface eDelete {

        public void deleteClicked();
    }

    public interface eCard {

        public void cardClicked();
    }

    public interface eFind {

        public void findClicked();
    }

    public interface ePrint {

        public void printClicked();
    }

    public interface eSave {

        public void saveClicked();
    }

    public interface eRefersh {

        public void refreshClicked();
    }

    public interface eOther {

        public void otherClicked();
    }

    public interface eClose {

        public void closeClicked();
    }

    private iMoveRec onMoveRec;
    private eNew onNew;
    private eEdit onEdit;
    private eDelete onDelete;
    private eCard onCard;
    private eFind onFind;
    private ePrint onPrint;
    private eSave onSave;
    private eRefersh onRefersh;
    private eOther onOther;
    private eClose onClose;

    private DmUserControl userControl;
    private UserMenu menuPermission;
    //private String userPermission;
    //private List<String> userControlPermission;
    private VoucherPermissionHandler voucherPermissionHandler;
    private boolean cardViewRequired = true, refreshRequired = true, otherRequired = true;
    private int currentRecordPosition = -1;
    private int totalRecords;
    private String docId;
    private List<? extends KeyField> keyFieldList;
    public DmClient.ViaList viaList;
    
    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    public boolean isCardViewRequired() {
        return cardViewRequired;
    }

    public void setCardViewRequired(boolean cardViewRequired) {
        this.cardViewRequired = cardViewRequired;
    }

    public boolean isRefreshRequired() {
        return refreshRequired;
    }

    public void setRefreshRequired(boolean refreshRequired) {
        this.refreshRequired = refreshRequired;
    }

    public boolean isOtherRequired() {
        return otherRequired;
    }

    public void setOtherRequired(boolean otherRequired) {
        this.otherRequired = otherRequired;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
//        if (btnNew.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_N) {
        if (btnNew.isAttached() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F2) {
            rootEvent.stopPropagation();
            btnClick(btnNew);
        } else if (btnEdit.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_E) {
            rootEvent.stopPropagation();
            btnClick(btnEdit);
        } else if (btnDelete.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_D) {
            rootEvent.stopPropagation();
            btnClick(btnDelete);
        } else if (btnCard.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_T) {
            rootEvent.stopPropagation();
            btnClick(btnCard);
        } else if (btnFirst.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
            rootEvent.stopPropagation();
            btnClick(btnFirst);
        } else if (btnPrevious.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_LEFT) {
            rootEvent.stopPropagation();
            btnClick(btnPrevious);
        } else if (btnNext.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_RIGHT) {
            rootEvent.stopPropagation();
            btnClick(btnNext);
        } else if (btnLast.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
            rootEvent.stopPropagation();
            btnClick(btnLast);
        } else if (btnFind.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F) {
            rootEvent.stopPropagation();
            btnClick(btnFind);
        } else if (btnPrint.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
            btnClick(btnPrint);
        } else if (btnSave.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S) {
            rootEvent.stopPropagation();
            btnClick(btnSave);
        } else if (btnCancel.isAttached() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            rootEvent.stopPropagation();
            btnClick(btnCancel);
        } else if (btnRefresh.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_R) {
            rootEvent.stopPropagation();
            btnClick(btnRefresh);
        } else if (btnOther.isAttached() && rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_O) {
            rootEvent.stopPropagation();
            btnClick(btnOther);
        } else if (btnClose.isAttached() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            rootEvent.stopPropagation();
            btnClick(btnClose);
        }
    }

    public Widget getButton(String buttonCaption) {
 
        if (btnPrintLabel.equalsIgnoreCase(buttonCaption)) {
            return btnPrint;
        } else if (btnOtherLabel.equalsIgnoreCase(buttonCaption)) {
            return btnOther;
        } else {
            return null;
        }
    }

    private void btnClickHandlers(Button[] btns) {
        for (Button btn : btns) {
            btn.addClickHandler(this);
        }
    }

    public DmTopControl(DmClient.ViaList viaList, DmUserControl userControl) {
        super();
        this.viaList = viaList;
        this.userControl = userControl;
        toolContainer.asWidget().addAttachHandler(this);
        toolContainer.addWidget(flwNew);
        toolContainer.addWidget(flwEdit);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(flwCard);
        toolContainer.addWidget(flwFirst);
        toolContainer.addWidget(flwPrevious);
        toolContainer.addWidget(flwNext);
        toolContainer.addWidget(flwLast);
        toolContainer.addWidget(flwFind);
        toolContainer.addWidget(flwPrint);
        toolContainer.addWidget(flwSave);
        toolContainer.addWidget(flwCancel);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwOther);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        toolContainer.addWidget(lblRecords);
        btnClickHandlers(new Button[]{btnNew, btnEdit, btnDelete, btnCard, btnFirst, btnPrevious, btnNext, btnLast, btnFind, btnPrint, btnSave, btnCancel, btnRefresh, btnOther, btnClose});
    }

    @Override
    public Widget asWidget() {
        return toolContainer.asWidget();
    }

    public void addMoveRecHandeller(iMoveRec onMoveRecVar) {
        onMoveRec = onMoveRecVar;
    }

    public void addNewHandeller(eNew onNewVar) {
        onNew = onNewVar;
    }

    public void addEditHandeller(eEdit onEditVar) {
        onEdit = onEditVar;
    }

    public void addDeleteHandeller(eDelete onDeleteVar) {
        onDelete = onDeleteVar;
    }

    public void addCardHandeller(eCard onCardVar) {
        onCard = onCardVar;
    }

    public void addFindHandeller(eFind onFindVar) {
        onFind = onFindVar;
    }

    public void addPrintHandeller(ePrint onPrintVar) {
        onPrint = onPrintVar;
    }

    public void addSaveHandeller(eSave onSaveVar) {
        onSave = onSaveVar;
    }

    public void addRefershHandeller(eRefersh onRefershVar) {
        onRefersh = onRefershVar;
    }

    public void addOtherHandeller(eOther onOtherVar) {
        onOther = onOtherVar;
    }

    public void addCloseHandeller(eClose onCloseVar) {
        onClose = onCloseVar;
    }

    public void setDisplay(boolean isBrowseMode) {
        if (!refreshRequired) {
            deAttachButton(btnRefresh);
        }

        if (!otherRequired) {
            deAttachButton(btnOther);
        }

        if (isBrowseMode) {
            if (menuPermission.getUserPermission().indexOf("A") >= 0) {
                attachButton(btnNew);
            } else {
                deAttachButton(btnNew);;
            }
            if (menuPermission.getUserPermission().indexOf("E") >= 0) {
                attachButton(btnEdit);
            } else {
                deAttachButton(btnEdit);;
            }
            if (menuPermission.getUserPermission().indexOf("D") >= 0) {
                attachButton(btnDelete);
            } else {
                deAttachButton(btnDelete);;
            }
            if (menuPermission.getUserPermission().indexOf("P") >= 0) {
                attachButton(btnPrint);
            } else {
                deAttachButton(btnPrint);;
            }

            if (getDocId() == "") {
                deAttachButton(btnEdit);
                deAttachButton(btnDelete);
                deAttachButton(btnPrint);
            }

            if (cardViewRequired) {
                attachButton(btnCard);
            } else {
                deAttachButton(btnCard);
            }
            deAttachButton(btnSave);
            deAttachButton(btnCancel);
        } else {
            deAttachButton(btnNew);
            deAttachButton(btnEdit);
            deAttachButton(btnDelete);
            deAttachButton(btnCard);
            deAttachButton(btnFirst);
            deAttachButton(btnPrevious);
            deAttachButton(btnNext);
            deAttachButton(btnLast);
            deAttachButton(btnFind);
            deAttachButton(btnPrint);

            attachButton(btnSave);
            attachButton(btnCancel);
        }

        if (getMode().equalsIgnoreCase("New") || getMode().equalsIgnoreCase("Edit") || getMode().equalsIgnoreCase("")) {
            setMode("Browse");
        }

        if (isBrowseMode) {
            manageCard();
            if (userControl != null) {
//                if (userControlPermission != null) {
//                    userControl.setVisible(userControlPermission.toArray(new String[userControlPermission.size()]));
//                }
                if (menuPermission.getControlPermission() != null) {
                    userControl.setVisible(menuPermission.getControlPermission().toArray(new String[menuPermission.getControlPermission().size()]));
                }
            }
        } else {
            if (userControl != null) {
                userControl.disableAll();
            }
        }

        changeState(!isBrowseMode);
    }

    public void btnClick(Button button) {

        if (button == btnNew) {
            if (menuPermission.getUserPermission().indexOf("A") < 0) {
                throw new DmException(0, "Permission denied.");
            }
            setDisplay(false);
            setTextBoxBlank();
            changeState(true);
            setMode("New");
            if (onNew != null) {
                onNew.newClicked();
            }
        } else if (button == btnEdit) {
            if (menuPermission.getUserPermission().indexOf("E") < 0) {
                throw new DmException(0, "Permission denied.");
            }
            if (!chkVoucherPermission(btnEditLabel)) {
                new DmAlertMessageBox("Permission denied. Due to voucher permission.", btnEdit).show();
            } else {
                setDisplay(false);
                changeState(true);
                setMode("Edit");
                if (onEdit != null) {
                    onEdit.editClicked();
                }
            }
        } else if (button == btnDelete) {
            if (menuPermission.getUserPermission().indexOf("D") < 0) {
                throw new DmException(0, "Permission denied.");
            }
            if (!chkVoucherPermission(btnDeleteLabel)) {
                new DmAlertMessageBox("Permission denied. Due to voucher permission.", btnDelete).show();
            } else {
                if (onDelete != null) {
                    onDelete.deleteClicked();
                }
                setDisplay(true);
            }
        } else if (button == btnCard) {
            if (isCardOn()) {
                aBtnCard.removeStyleName("fa fa-toggle-on");
                aBtnCard.addStyleName("fa fa-toggle-off");
            } else {
                aBtnCard.removeStyleName("fa fa-toggle-off");
                aBtnCard.addStyleName("fa fa-toggle-on");
            }

            if (isCardOn()) {
                manageCard();
            } else {
                if (onCard != null) {
                    onCard.cardClicked();
                }
            }
        } else if (button == btnFirst) {
            if (currentRecordPosition < 0) {
                return;
            }
            currentRecordPosition = 0;
            navigate();
        } else if (button == btnPrevious) {
            if (currentRecordPosition < 0) {
                return;
            }
            currentRecordPosition -= 1;
            navigate();
        } else if (button == btnNext) {
            if (currentRecordPosition < 0) {
                return;
            }
            currentRecordPosition += 1;
            navigate();
        } else if (button == btnLast) {
            if (currentRecordPosition < 0) {
                return;
            }
            currentRecordPosition = totalRecords - 1;
            navigate();
        } else if (button == btnFind) {
            if (onFind != null) {
                onFind.findClicked();
            }
        } else if (button == btnPrint) {
            if (menuPermission.getUserPermission().indexOf("P") < 0) {
                throw new DmException(0, "Permission denied.");
            }
            if (onPrint != null) {
                onPrint.printClicked();
            }
        } else if (button == btnSave) {
            if (!chkVoucherPermission(btnSaveLabel)) {
                new DmAlertMessageBox("Permission denied. Due to voucher permission.", btnSave).show();

            } else {
                if (onSave != null) {
                    onSave.saveClicked();
                }
            }
        } else if (button == btnCancel) {

            new DmConfirmMessageBox("Are you sure? You want to cancel the changes.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {

                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        changeState(false);
                        setTextBoxBlank();
                        setDisplay(true);
                        setMode("Browse");
                        if (onMoveRec != null) {
                            onMoveRec.moveRec(getDocId());
                        }
                    }
                }
            }).show();

        } else if (button == btnRefresh) {
            if (onRefersh != null) {
                onRefersh.refreshClicked();
            }
        } else if (button == btnOther) {
            if (onOther != null) {
                onOther.otherClicked();
            }
        } else if (button == btnClose) {
            if (onClose != null) {
                new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {

                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            onClose.closeClicked();
                        }
                    }

                }).show();

            }
        } else {
            setDisplay(true);
        }
    }

    public void btnClick(String buttonCaption) {

        if (buttonCaption.trim().equalsIgnoreCase(btnNewLabel)) {
            btnClick(btnNew);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnEditLabel)) {
            btnClick(btnEdit);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnDeleteLabel)) {
            btnClick(btnDelete);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnCardLabel)) {
            btnClick(btnCard);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnFirstLabel)) {
            btnClick(btnFirst);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnPreviousLabel)) {
            btnClick(btnPrevious);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnNextLabel)) {
            btnClick(btnNext);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnLastLabel)) {
            btnClick(btnLast);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnFindLabel)) {
            btnClick(btnFind);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnPrintLabel)) {
            btnClick(btnPrint);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnSaveLabel)) {
            btnClick(btnSave);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnCancelLabel)) {
            btnClick(btnCancel);

        } else if (buttonCaption.trim().equalsIgnoreCase(btnRefreshLabel)) {
            btnClick(btnRefresh);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnOtherLabel)) {
            btnClick(btnOther);
            
        } else if (buttonCaption.trim().equalsIgnoreCase(btnCloseLabel)) {
            btnClick(btnClose);
            
        } else {
            setDisplay(true);
        }
    }

    private boolean isCardOn() {
        String styleName;
        styleName = aBtnCard.getStyleName().replace(aBtnCard.getStylePrimaryName(), "").trim();

        if (styleName.trim().equalsIgnoreCase("fa fa-toggle-on")) {
            return true;
        } else {
            return false;
        }
    }

    public void setNavigator(List<? extends KeyField> lstKey) {
        keyFieldList = lstKey;
        totalRecords = keyFieldList.size();
        if (!getDocId().equals("")) {
            currentRecordPosition = getRowNumber();
        }
        navigate();
    }

    public void attachButton(Button btn) {
        deAttachButton(btn);

        if (btn == btnNew) {
            flwNew.add(btn);
            flwNew.setVisible(true);
        } else if (btn == btnEdit) {
            flwEdit.add(btn);
            flwEdit.setVisible(true);
        } else if (btn == btnDelete) {
            flwDelete.add(btn);
            flwDelete.setVisible(true);
        } else if (btn == btnCard) {
            flwCard.add(btn);
            flwCard.setVisible(true);
        } else if (btn == btnFirst) {
            flwFirst.add(btn);
            flwFirst.setVisible(true);
        } else if (btn == btnPrevious) {
            flwPrevious.add(btn);
            flwPrevious.setVisible(true);
        } else if (btn == btnNext) {
            flwNext.add(btn);
            flwNext.setVisible(true);
        } else if (btn == btnLast) {
            flwLast.add(btn);
            flwLast.setVisible(true);
        } else if (btn == btnFind) {
            flwFind.add(btn);
            flwFind.setVisible(true);
        } else if (btn == btnSave) {
            flwSave.add(btn);
            flwSave.setVisible(true);
        } else if (btn == btnCancel) {
            flwCancel.add(btn);
            flwCancel.setVisible(true);
        } else if (btn == btnPrint) {
            flwPrint.add(btn);
            flwPrint.setVisible(true);
        } else if (btn == btnOther) {
            flwOther.add(btn);
            flwOther.setVisible(true);
        } else if (btn == btnRefresh) {
            flwRefresh.add(btn);
            flwRefresh.setVisible(true);
        } else if (btn == btnClose) {
            flwClose.add(btn);
            flwClose.setVisible(true);
        }
    }

    public void deAttachButton(Button btn) {
        if (btn == btnNew) {
            flwNew.clear();
            flwNew.setVisible(false);
        } else if (btn == btnEdit) {
            flwEdit.clear();
            flwEdit.setVisible(false);
        } else if (btn == btnDelete) {
            flwDelete.clear();
            flwDelete.setVisible(false);
        } else if (btn == btnCard) {
            flwCard.clear();
            flwCard.setVisible(false);
        } else if (btn == btnFirst) {
            flwFirst.clear();
            flwFirst.setVisible(false);
        } else if (btn == btnPrevious) {
            flwPrevious.clear();
            flwPrevious.setVisible(false);
        } else if (btn == btnNext) {
            flwNext.clear();
            flwNext.setVisible(false);
        } else if (btn == btnLast) {
            flwLast.clear();
            flwLast.setVisible(false);
        } else if (btn == btnFind) {
            flwFind.clear();
            flwFind.setVisible(false);
        } else if (btn == btnSave) {
            flwSave.clear();
            flwSave.setVisible(false);
        } else if (btn == btnCancel) {
            flwCancel.clear();
            flwCancel.setVisible(false);
        } else if (btn == btnPrint) {
            flwPrint.clear();
            flwPrint.setVisible(false);
        } else if (btn == btnOther) {
            flwOther.clear();
            flwOther.setVisible(false);
        } else if (btn == btnRefresh) {
            flwRefresh.clear();
            flwRefresh.setVisible(false);
        } else if (btn == btnClose) {
            flwClose.clear();
            flwClose.setVisible(false);
        }
    }

    public void enableButton(Button btn, boolean isEnable) {
        btn.setEnabled(isEnable);

        if (btn == btnFirst) {
            if (isEnable) {
                aFirst.getElement().getStyle().setColor("black");
            } else {
                aFirst.getElement().getStyle().setColor("gray");
            }
        } else if (btn == btnPrevious) {
            if (isEnable) {
                aPrevious.getElement().getStyle().setColor("black");
            } else {
                aPrevious.getElement().getStyle().setColor("gray");
            }
        } else if (btn == btnNext) {
            if (isEnable) {
                aNext.getElement().getStyle().setColor("black");
            } else {
                aNext.getElement().getStyle().setColor("gray");
            }
        } else if (btn == btnLast) {
            if (isEnable) {
                aLast.getElement().getStyle().setColor("black");
            } else {
                aLast.getElement().getStyle().setColor("gray");
            }
        }
    }

    public String getDocId() {
        return DmShared.xNull(docId);
    }

    public void setDocId(String strDocIdVar) {
        docId = DmShared.xNull(strDocIdVar).trim();
    }

    public String getMode() {
        return toolContainer.getMode();
    }

    public void setMode(String strMode) {
        toolContainer.setMode(strMode);
    }

    public String getUserPermission() {
        return menuPermission.getUserPermission();
    }

    public void setUserPermission(String userPermission) {
        menuPermission.setUserPermission(userPermission);
    }

    public void setUserPermission(UserMenu menuPermission, VoucherPermissionHandler voucherPermissionHandler) {
        this.menuPermission = menuPermission;
        if (userControl != null) {
            userControl.disableAll();
        }
        this.voucherPermissionHandler = voucherPermissionHandler;
    }

    public UserPermission getPermissions() {
        LinkedHashMap<String, List<UserMenu>> userMenuHashList = new LinkedHashMap<>();
        List<UserMenu> userMenuList = new ArrayList<>();
        UserPermission userPermission = new UserPermission();

        userMenuList.add(menuPermission);
        userMenuHashList.put("", userMenuList);
        userPermission.setUserMenu(userMenuHashList);

        if (voucherPermissionHandler != null) {
            userPermission.setVoucherType(voucherPermissionHandler.getVoucherType());
            userPermission.setVoucherDate(voucherPermissionHandler.getVoucherDate());
            userPermission.setVoucherValue(voucherPermissionHandler.getVoucherValue());
            userPermission.setVoucherPermission(voucherPermissionHandler.getVoucherPermission());
        }

        return userPermission;
    }

    public void setTitle(String title) {
        toolContainer.setTitle(title);
    }

    public String getTitle() {
        return toolContainer.getTitle();
    }

    public void setFocus() {
        if (btnNew.isAttached()) {
            btnNew.setFocus(true);
        } else if (btnEdit.isAttached()) {
            btnEdit.setFocus(true);
        } else if (btnDelete.isAttached()) {
            btnDelete.setFocus(true);
        } else if (btnCard.isAttached()) {
            btnCard.setFocus(true);
        } else if (btnFind.isAttached()) {
            btnFind.setFocus(true);
        } else if (btnSave.isAttached()) {
            btnSave.setFocus(true);
        }
    }

    private boolean chkVoucherPermission(String buttonCaption) {
        List<VoucherPermission> voucherPermission;
        boolean isVerified = true;
        Date tmpDate;

        if (voucherPermissionHandler != null) {
            if (buttonCaption.trim().equalsIgnoreCase(btnDeleteLabel)) {
                tmpDate = DmShared.convertDate(menuPermission.getDeleteDate());

                if (tmpDate != null) {
                    if (DmShared.xNull(voucherPermissionHandler.getVoucherDate()).trim().equalsIgnoreCase("")
                            || (tmpDate.getTime() <= DmShared.convertDate(voucherPermissionHandler.getVoucherDate()).getTime())) {
                        isVerified = true;
                    } else {
                        isVerified = false;
                        return isVerified;
                    }
                }

            } else {
                tmpDate = DmShared.convertDate(menuPermission.getSaveDate());

                if (tmpDate != null) {
                    if (DmShared.xNull(voucherPermissionHandler.getVoucherDate()).trim().equalsIgnoreCase("")
                            || (tmpDate.getTime() <= DmShared.convertDate(voucherPermissionHandler.getVoucherDate()).getTime())) {
                        isVerified = true;
                    } else {
                        isVerified = false;
                        return isVerified;
                    }
                }
            }

            if (isVerified) {
                voucherPermission = voucherPermissionHandler.getVoucherPermission();
                if (voucherPermission != null) {
                    for (int i = 0; i < voucherPermission.size(); i++) {
                        isVerified = false;
                        if (voucherPermission.get(i) != null) {
                            if (DmShared.vNull(voucherPermission.get(i).getVoucherType()).doubleValue() == DmShared.vNull(voucherPermissionHandler.getVoucherType()).doubleValue()) {
                                if (DmShared.xNull(voucherPermissionHandler.getVoucherDate()).trim().equalsIgnoreCase("")) {
                                    isVerified = true;
                                    break;
                                } else {
                                    if (buttonCaption.trim().equalsIgnoreCase(btnDeleteLabel)) {
                                        if (DmShared.xNull(voucherPermission.get(i).getDeleteDate()).trim().equalsIgnoreCase("")
                                                || (DmShared.convertDate(voucherPermission.get(i).getDeleteDate()).getTime() <= DmShared.convertDate(voucherPermissionHandler.getVoucherDate()).getTime()
                                                && DmShared.convertDate(DmClient.getLoginDate()).getTime() >= DmShared.convertDate(voucherPermissionHandler.getVoucherDate()).getTime())) {
                                            isVerified = true;
                                            break;
                                        }
                                    } else {
                                        if (DmShared.xNull(voucherPermission.get(i).getSaveDate()).trim().equalsIgnoreCase("")
                                                || (DmShared.convertDate(voucherPermission.get(i).getSaveDate()).getTime() <= DmShared.convertDate(voucherPermissionHandler.getVoucherDate()).getTime()
                                                && DmShared.convertDate(DmClient.getLoginDate()).getTime() >= DmShared.convertDate(voucherPermissionHandler.getVoucherDate()).getTime())) {
                                            isVerified = true;
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

        return isVerified;
    }

    private void changeState(boolean isEnable) {
        int intIndex;
        List<IsWidget> lstWdg;
        IsWidget wdgObject;
        TextBox textBox;
        SuggestBox suggestBox;
        DmTextHelp textHelp;
        DmTextHelpMulti textHelpMulti;
        TextArea textArea;
        DmTextBoxMobile textBoxMobile;
        DmCheckBox checkBox;
        DmRemarkTextBox remarkTextBox;

        try {
            if (viaList != null) {
                lstWdg = viaList.get();
                for (intIndex = 0; intIndex < lstWdg.size(); intIndex++) {
                    wdgObject = lstWdg.get(intIndex);
                    if (wdgObject instanceof TextBox) {
                        textBox = (TextBox) wdgObject;
                        textBox.setEnabled(isEnable);
                    } else if (wdgObject instanceof SuggestBox) {
                        suggestBox = (SuggestBox) wdgObject;
                        suggestBox.setEnabled(isEnable);
                    } else if (wdgObject instanceof DmTextHelp) {
                        textHelp = (DmTextHelp) wdgObject;
                        textHelp.setEnabled(isEnable);
                    } else if (wdgObject instanceof DmTextHelpMulti) {
                        textHelpMulti = (DmTextHelpMulti) wdgObject;
                        textHelpMulti.setEnabled(isEnable);
                    } else if (wdgObject instanceof TextArea) {
                        textArea = (TextArea) wdgObject;
                        textArea.setEnabled(isEnable);
                    } else if (wdgObject instanceof DmTextBoxMobile) {
                        textBoxMobile = (DmTextBoxMobile) wdgObject;
                        textBoxMobile.setEnabled(isEnable);
                    } else if (wdgObject instanceof DmCheckBox) {
                        checkBox = (DmCheckBox) wdgObject;
                        checkBox.setEnabled(isEnable);
                    } else if (wdgObject instanceof DmRemarkTextBox) {
                        remarkTextBox = (DmRemarkTextBox) wdgObject;
                        remarkTextBox.setEnabled(isEnable);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void setTextBoxBlank() {
        int intIndex;
        List<IsWidget> lstWdg;
        IsWidget wdgObject;
        DmTextBox textBox;
        DmTextBoxNumeric textBoxNumeric;
        DmTextBoxTime textBoxTime;
        DmSuggestBox suggestBox;
        DmTextHelp textHelp;
        DmTextHelpMulti textHelpMulti;
        DmTextArea textArea;
        DmTextBoxMobile textBoxMobile;
        DmCheckBox checkBox;
        DmRemarkTextBox remarkTextBox;

        try {
            if (viaList != null) {
                lstWdg = viaList.get();
                for (intIndex = 0; intIndex < lstWdg.size(); intIndex++) {
                    wdgObject = lstWdg.get(intIndex);
                    if (wdgObject instanceof DmTextBox) {
                        textBox = (DmTextBox) wdgObject;
                        textBox.setText("");
                        textBox.setTag(null);
                        textBox.setTag2nd(null);
                    } else if (wdgObject instanceof DmTextBoxNumeric) {
                        textBoxNumeric = (DmTextBoxNumeric) wdgObject;
                        textBoxNumeric.setText("");
                        textBoxNumeric.setTag(null);
                        textBoxNumeric.setTag2nd(null);
                    } else if (wdgObject instanceof DmSuggestBox) {
                        suggestBox = (DmSuggestBox) wdgObject;
                        suggestBox.setText("");
                    } else if (wdgObject instanceof DmTextHelp) {
                        textHelp = (DmTextHelp) wdgObject;
                        textHelp.setText("");
                        textHelp.setTag(null);
                        textHelp.setTag2nd(null);
                    } else if (wdgObject instanceof DmTextHelpMulti) {
                        textHelpMulti = (DmTextHelpMulti) wdgObject;
                        textHelpMulti.setText("");
                        textHelpMulti.setTag(null);
                        textHelpMulti.setTag2nd(null);
                    } else if (wdgObject instanceof DmTextArea) {
                        textArea = (DmTextArea) wdgObject;
                        textArea.setText("");
                        textArea.setTag(null);
                        textArea.setTag2nd(null);
                    } else if (wdgObject instanceof DmTextBoxTime) {
                        textBoxTime = (DmTextBoxTime) wdgObject;
                        textBoxTime.setText("");
                        textBoxTime.setTag(null);
                        textBoxTime.setTag2nd(null);
                    } else if (wdgObject instanceof DmTextBoxMobile) {
                        textBoxMobile = (DmTextBoxMobile) wdgObject;
                        textBoxMobile.setText("");
                        textBoxMobile.setTag(null);
                        textBoxMobile.setTag2nd(null);
                    } else if (wdgObject instanceof DmTextBoxMobile) {
                        checkBox = (DmCheckBox) wdgObject;
                        checkBox.setValue(false);
                    } else if (wdgObject instanceof DmRemarkTextBox) {
                        remarkTextBox = (DmRemarkTextBox) wdgObject;
                        remarkTextBox.setText("");
                        remarkTextBox.setTag(null);
                        remarkTextBox.setTag2nd(null);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void manageCard() {

        if (!isCardOn()) {
            attachButton(btnFirst);
            attachButton(btnPrevious);
            attachButton(btnNext);
            attachButton(btnLast);

            deAttachButton(btnNew);
            deAttachButton(btnFind);
            lblRecords.setVisible(true);

            lblRecords.setText((currentRecordPosition + 1) + "/" + totalRecords);

            if (totalRecords == 0) {
//                deAttachButton(btnFirst);
//                deAttachButton(btnPrevious);
//                deAttachButton(btnNext);
//                deAttachButton(btnLast);
                enableButton(btnFirst, false);
                enableButton(btnPrevious, false);
                enableButton(btnNext, false);
                enableButton(btnLast, false);
            } else if (currentRecordPosition == 0) {
//                deAttachButton(btnFirst);
//                deAttachButton(btnPrevious);
//                attachButton(btnNext);
//                attachButton(btnLast);
                enableButton(btnFirst, false);
                enableButton(btnPrevious, false);
                enableButton(btnNext, true);
                enableButton(btnLast, true);
            } else if (currentRecordPosition == (totalRecords - 1)) {
//                attachButton(btnFirst);
//                attachButton(btnPrevious);
//                deAttachButton(btnNext);
//                deAttachButton(btnLast);
                enableButton(btnFirst, true);
                enableButton(btnPrevious, true);
                enableButton(btnNext, false);
                enableButton(btnLast, false);
            } else {
//                attachButton(btnFirst);
//                attachButton(btnPrevious);
//                attachButton(btnNext);
//                attachButton(btnLast);
                enableButton(btnFirst, true);
                enableButton(btnPrevious, true);
                enableButton(btnNext, true);
                enableButton(btnLast, true);
            }
        } else {
            deAttachButton(btnFirst);
            deAttachButton(btnPrevious);
            deAttachButton(btnNext);
            deAttachButton(btnLast);

            //testing start
            //attachButton(btnNew);
            if (menuPermission.getUserPermission().indexOf("A") >= 0) {
                attachButton(btnNew);
            } else {
                deAttachButton(btnNew);;
            }
            //testing End
            attachButton(btnFind);
            lblRecords.setVisible(false);
        }
    }

    private void navigate() {
        if (currentRecordPosition >= (totalRecords - 1) || currentRecordPosition < 0) {
            currentRecordPosition = (totalRecords - 1);
            setDocId("");
        }

        try {
            setDocId(keyFieldList.get(currentRecordPosition).getSharedKeyFieldCode());
        } catch (Exception e) {
            setDocId("");
        }

        manageCard();
        if (onMoveRec != null) {
            onMoveRec.moveRec(getDocId());
        }
    }

    private int getRowNumber() {
        int intRtn = -1;

        for (int i = 0; i < keyFieldList.size(); i++) {
            if (DmShared.xNull(keyFieldList.get(i).getSharedKeyFieldCode()).equals(getDocId())) {
                intRtn = i;
                break;
            }
        }

        return intRtn;
    }

    public void setDeleteCaptionToDiscard() {
        deleteLabel.setText("Discard");
        deleteAnchor.addStyleName("fa fa-ban");
        btnDelete.setTitle("Discard: (Ctrl + D)");
    }
}
