package dataman.dmToolkitR.dmGridEditPanel.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkitR.dmGrid.client.DmGrid;

public abstract class DmGridEditPanel<T> extends DmGridEditPanel_Dsg implements DmRootEvents, AttachEvent.Handler, ClickHandler {

    public abstract void onOk();

    public abstract void onNew();

    public abstract void onDelete();

    public abstract void onClose();

    public abstract void manageOk(T row);

    public abstract void manageDelete();

    public abstract T getNewRow();

    public abstract void displayRow(T row);

    private DmGrid<T> grid;
    int currentRowIndex = 0;

    public DmGrid<T> getDmGrid() {
        return grid;
    }

    public int getCurrentRowIndex() {
        return currentRowIndex;
    }

    public DmGridEditPanel(DmGrid<T> grid, String title, String width) {
        super(title, width);
        modal.addAttachHandler(this);
        this.grid = grid;
        handlerBtnClick(new Button[]{btnOk, btnNew, btnDelete, btnClose});
    }

    private void handlerBtnClick(Button[] btns) {
        for (Button btn : btns) {
            btn.addClickHandler(this);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
            onOk();
        } else if (event.getSource() == btnNew) {
            onNew();
        } else if (event.getSource() == btnDelete) {
            onDelete();
        } else if (event.getSource() == btnClose) {
            onClose();
            grid.setFocus(true);
        }
    }

    protected void add(IsWidget widget) {
        flwContent.add(widget);
    }

    protected void showCenter(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
//TBC//        modal.center();
        modal.show();
        onActive(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
    }

    private void onActive(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        T row;
        this.currentRowIndex = currentRowIndex;

        setVisibleOk(okNeeded);
        setVisibleNew(newNeeded);
        setVisibleDelete(deleteNeeded);
        row = grid.getData().get(currentRowIndex);
        displayRow(row);
    }

    public void hide(boolean withFocusOnGrid) {
        Integer defaultColumnIndex = null;
        modal.hide();

        if (withFocusOnGrid) {
//            if (grid.getGrid().getSelectionModel() instanceof CellSelectionModel) {
//                defaultColumnIndex = grid.getGrid().<Integer>getData(DmGrid.defaultColumnIndexLabel);
//                if (grid.getGrid().getStore().size() > 0 && defaultColumnIndex != null) {
//                    ((CellSelectionModel) grid.getGrid().getSelectionModel()).selectCell(currentRowIndex, defaultColumnIndex);
//                }
//            } else {
//                grid.getGrid().getSelectionModel().select(currentRowIndex, false);
//            }
        }
    }

    public void hide() {
        modal.hide();
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
//TBC//        modal.setPopupPosition(left, top);        
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

    private T addNewRow() {
        T item = null;
        if (isNewEnable()) {
            currentRowIndex = grid.getData().size();
            item = getNewRow();
            grid.getData().add(currentRowIndex, item);
            grid.commitChanges();
        }
        return item;
    }

    public void moveToNextRow() {
        if ((grid.getData().size() - 1) == currentRowIndex) {
            addNewRow();
        } else {
            currentRowIndex++;
        }
    }

    public void proceedOk() {
        manageOk(grid.getData().get(currentRowIndex));
        moveToNextRow(); //Call From ManageOk before callback
        hide();
    }

    public void proceedNew() {
        displayRow(addNewRow());
    }

    public void proceedDelete() {
        if (currentRowIndex > (grid.getData().size() - 1)) {
            currentRowIndex = grid.getData().size() - 1;
        } else if (currentRowIndex == 0 && (grid.getData().size() - 1) == 0) {
            grid.getData().remove(currentRowIndex);
            grid.getData().add(getNewRow());
        } else if (currentRowIndex == (grid.getData().size() - 1)) {
            grid.getData().remove(currentRowIndex);
            currentRowIndex--;
        } else {
            grid.getData().remove(currentRowIndex);
        }

        manageDelete();

        displayRow(grid.getData().get(currentRowIndex));
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
//        DmRequestBuilder.flagClientSideWorking = true;
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
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
}
