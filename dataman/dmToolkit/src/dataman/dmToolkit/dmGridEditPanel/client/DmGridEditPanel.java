package dataman.dmToolkit.dmGridEditPanel.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;

public abstract class DmGridEditPanel<T1, T2> extends DmGridEditPanel_Dsg implements DmRootEvents, AttachEvent.Handler, ClickHandler {

    public abstract void onOk();

    public abstract void onNew();

    public abstract void onDelete();

    public abstract void onClose();

    public abstract void manageOk(T1 row);

    public abstract void manageDelete();

    public abstract T1 getNewRow();

    public abstract void displayRow(T1 row);

    private DmGrid<T1, T2> grid;
    int currentRowIndex = 0;

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_K) {
            if (isOkEnable()) {
                onOk();
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    protected void showCenter(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        ppForm.center();
        ppForm.show();
        onActive(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
    }

    protected void showRelativeTo(int currentRowIndex, int relatedToColumnIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        final Element target = grid.getGrid().getView().getCell(currentRowIndex, relatedToColumnIndex);

        ppForm.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                position(target, offsetWidth, offsetHeight);
            }
        });
        onActive(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
    }

    public void hide(boolean withFocusOnGrid) {
        Integer defaultColumnIndex = null;
        ppForm.hide();

        if (withFocusOnGrid) {
            if (grid.getGrid().getSelectionModel() instanceof CellSelectionModel) {
                defaultColumnIndex = grid.getGrid().<Integer>getData(DmGrid.defaultColumnIndexLabel);
                if (grid.getGrid().getStore().size() > 0 && defaultColumnIndex != null) {
                    ((CellSelectionModel) grid.getGrid().getSelectionModel()).selectCell(currentRowIndex, defaultColumnIndex);
                }
            } else {
                grid.getGrid().getSelectionModel().select(currentRowIndex, false);
            }
        }
    }

    public void hide() {
        hide(true);
//        Integer defaultColumnIndex;
//        ppForm.hide();
//
//        if (grid.getGrid().getSelectionModel() instanceof CellSelectionModel) {
//            defaultColumnIndex = grid.getGrid().<Integer>getData(DmGrid.defaultColumnIndexLabel);
//            if (grid.getGrid().getStore().size() > 0 && defaultColumnIndex != null) {
//                ((CellSelectionModel) grid.getGrid().getSelectionModel()).selectCell(currentRowIndex, defaultColumnIndex);
//            }
//        } else {
//            grid.getGrid().getSelectionModel().select(currentRowIndex, false);
//        }
    }

    private void position(final Element relativeObject, int offsetWidth, int offsetHeight) {
        // Calculate left position for the popup. The computation for
        // the left position is bidi-sensitive.

        int textBoxOffsetWidth = relativeObject.getOffsetWidth();

        // Compute the difference between the popup's width and the
        // textbox's width
        int offsetWidthDiff = offsetWidth - textBoxOffsetWidth;

        int left;

        if (LocaleInfo.getCurrentLocale().isRTL()) { // RTL case

            int textBoxAbsoluteLeft = relativeObject.getAbsoluteLeft();

            // Right-align the popup. Note that this computation is
            // valid in the case where offsetWidthDiff is negative.
            left = textBoxAbsoluteLeft - offsetWidthDiff;

            // If the suggestion popup is not as wide as the text box, always
            // align to the right edge of the text box. Otherwise, figure out whether
            // to right-align or left-align the popup.
            if (offsetWidthDiff > 0) {

                // Make sure scrolling is taken into account, since
                // box.getAbsoluteLeft() takes scrolling into account.
                int windowRight = Window.getClientWidth() + Window.getScrollLeft();
                int windowLeft = Window.getScrollLeft();

                // Compute the left value for the right edge of the textbox
                int textBoxLeftValForRightEdge = textBoxAbsoluteLeft
                        + textBoxOffsetWidth;

                // Distance from the right edge of the text box to the right edge
                // of the window
                int distanceToWindowRight = windowRight - textBoxLeftValForRightEdge;

                // Distance from the right edge of the text box to the left edge of the
                // window
                int distanceFromWindowLeft = textBoxLeftValForRightEdge - windowLeft;

                // If there is not enough space for the overflow of the popup's
                // width to the right of the text box and there IS enough space for the
                // overflow to the right of the text box, then left-align the popup.
                // However, if there is not enough space on either side, stick with
                // right-alignment.
                if (distanceFromWindowLeft < offsetWidth
                        && distanceToWindowRight >= offsetWidthDiff) {
                    // Align with the left edge of the text box.
                    left = textBoxAbsoluteLeft;
                }
            }
        } else { // LTR case

            // Left-align the popup.
            left = relativeObject.getAbsoluteLeft();

            // If the suggestion popup is not as wide as the text box, always align to
            // the left edge of the text box. Otherwise, figure out whether to
            // left-align or right-align the popup.
            if (offsetWidthDiff > 0) {
                // Make sure scrolling is taken into account, since
                // box.getAbsoluteLeft() takes scrolling into account.
                int windowRight = Window.getClientWidth() + Window.getScrollLeft();
                int windowLeft = Window.getScrollLeft();

                // Distance from the left edge of the text box to the right edge
                // of the window
                int distanceToWindowRight = windowRight - left;

                // Distance from the left edge of the text box to the left edge of the
                // window
                int distanceFromWindowLeft = left - windowLeft;

                // If there is not enough space for the overflow of the popup's
                // width to the right of hte text box, and there IS enough space for the
                // overflow to the left of the text box, then right-align the popup.
                // However, if there is not enough space on either side, then stick with
                // left-alignment.
                if (distanceToWindowRight < offsetWidth
                        && distanceFromWindowLeft >= offsetWidthDiff) {
                    // Align with the right edge of the text box.
                    left -= offsetWidthDiff;
                }
            }
        }

        // Calculate top position for the popup
        int top = relativeObject.getAbsoluteTop();

        // Make sure scrolling is taken into account, since
        // box.getAbsoluteTop() takes scrolling into account.
        int windowTop = Window.getScrollTop();
        int windowBottom = Window.getScrollTop() + Window.getClientHeight();

        // Distance from the top edge of the window to the top edge of the
        // text box
        int distanceFromWindowTop = top - windowTop;

        // Distance from the bottom edge of the window to the bottom edge of
        // the text box
        int distanceToWindowBottom = windowBottom
                - (top + relativeObject.getOffsetHeight());

        // If there is not enough space for the popup's height below the text
        // box and there IS enough space for the popup's height above the text
        // box, then then position the popup above the text box. However, if there
        // is not enough space on either side, then stick with displaying the
        // popup below the text box.
        if (distanceToWindowBottom < offsetHeight
                && distanceFromWindowTop >= offsetHeight) {
            top -= offsetHeight;
        } else {
            // Position above the text box
            top += relativeObject.getOffsetHeight();
        }
        ppForm.setPopupPosition(left, top);
    }

    private T1 addNewRow() {
        T1 item = null;
        if (isNewEnable()) {
            currentRowIndex++;
            item = getNewRow();
            grid.getGrid().getStore().add(currentRowIndex, item);
        }
        return item;
    }

    private void moveToNextRow() {
        if ((grid.getGrid().getStore().size() - 1) == currentRowIndex) {
            addNewRow();
        } else {
            currentRowIndex++;
        }
    }

    public void proceedOk() {
        manageOk(grid.getGrid().getStore().get(currentRowIndex));
        moveToNextRow();
        hide();
    }

    public void proceedNew() {
        displayRow(addNewRow());
    }

    public void proceedDelete() {
        if (currentRowIndex > (grid.getGrid().getStore().size() - 1)) {
            currentRowIndex = grid.getGrid().getStore().size() - 1;
        } else if (currentRowIndex == 0 && (grid.getGrid().getStore().size() - 1) == 0) {
            grid.getGrid().getStore().remove(currentRowIndex);
            grid.getGrid().getStore().add(getNewRow());
        } else if (currentRowIndex == (grid.getGrid().getStore().size() - 1)) {
            grid.getGrid().getStore().remove(currentRowIndex);
            currentRowIndex--;
        } else {
            grid.getGrid().getStore().remove(currentRowIndex);
        }

        manageDelete();

        displayRow(grid.getGrid().getStore().get(currentRowIndex));
    }

    public DmGrid<T1, T2> getDmGrid() {
        return grid;
    }

    public int getCurrentRowIndex() {
        return currentRowIndex;
    }

    public void setVisibleOk(boolean visible) {
        if (visible) {
            attachButton(btnOk);
        } else {
            deAttachButton(btnOk);
        }
    }

    public void setVisibleNew(boolean visible) {
        if (visible) {
            attachButton(btnNew);
        } else {
            deAttachButton(btnNew);
        }
    }

    public void setVisibleDelete(boolean visible) {
        if (visible) {
            attachButton(btnDelete);
        } else {
            deAttachButton(btnDelete);
        }
    }

    public void setDeleteCaptionToDiscard() {
        deleteLabel.setText("Discard");
        deleteAnchor.setStyleName("fa fa-ban");
    }

    public void setDiscardCaptionToDelete() {
        deleteLabel.setText("Delete");
        deleteAnchor.setStyleName("fa fa-trash-o");
    }

//    public void setDiscardCaptionToDelete() {
//        flwDelete.clear();
//        flwDelete.add(btnDelete);
//    }
    public boolean isOkEnable() {
        if (!btnOk.isAttached()) {
            return false;
        } else if (!btnOk.isVisible()) {
            return false;
        } else if (!btnOk.isEnabled()) {
            return false;
        }

        return true;
    }

    public boolean isNewEnable() {
        if (!btnNew.isAttached()) {
            return false;
        } else if (!btnNew.isVisible()) {
            return false;
        } else if (!btnNew.isEnabled()) {
            return false;
        }

        return true;
    }

    public DmGridEditPanel(DmGrid<T1, T2> grid, String title, String width, String height) {
        super();
        ppForm.addAttachHandler(this);
        this.grid = grid;
        setTitle(title);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwNew);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();

        ppForm.setWidth(width);
        ppForm.setHeight(height);

        handlerBtnClick(new Button[]{btnOk, btnNew, btnDelete, btnClose});
    }

    public void setTitle(String title) {
        toolContainer.setTitle(title);
    }

    private void handlerBtnClick(Button[] btns) {
        for (Button btn : btns) {
            btn.addClickHandler(this);
        }
    }

    private void attachButton(Button btn) {
        deAttachButton(btn);

        if (btn == btnNew) {
            flwNew.add(btn);
            flwNew.setVisible(true);
        } else if (btn == btnDelete) {
            flwDelete.add(btn);
            flwDelete.setVisible(true);
        } else if (btn == btnOk) {
            flwOk.add(btn);
            flwOk.setVisible(true);
        } else if (btn == btnClose) {
            flwClose.add(btn);
            flwClose.setVisible(true);
        }
    }

    private void deAttachButton(Button btn) {
        if (btn == btnNew) {
            flwNew.clear();
            flwNew.setVisible(false);
        } else if (btn == btnDelete) {
            flwDelete.clear();
            flwDelete.setVisible(false);
        } else if (btn == btnOk) {
            flwOk.clear();
            flwOk.setVisible(false);
        } else if (btn == btnClose) {
            flwClose.clear();
            flwClose.setVisible(false);
        }
    }

    private void onActive(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        T1 row;
        this.currentRowIndex = currentRowIndex;

        setVisibleOk(okNeeded);
        setVisibleNew(newNeeded);
        setVisibleDelete(deleteNeeded);
        row = grid.getGrid().getStore().get(currentRowIndex);
        displayRow(row);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            onClose();
            grid.getGrid().focus();
        } else if (event.getSource() == btnOk) {
            onOk();
        } else if (event.getSource() == btnNew) {
            onNew();
        } else if (event.getSource() == btnDelete) {
            onDelete();
        }
    }

}
