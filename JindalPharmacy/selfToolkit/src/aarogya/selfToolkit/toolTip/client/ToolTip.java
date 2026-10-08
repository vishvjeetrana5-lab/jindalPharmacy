/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.selfToolkit.toolTip.client;

import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class ToolTip extends ToolTip_Dsg implements AttachEvent.Handler, IsWidget, DmRootEvents {

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            popupPanel.hide();
        }
    }

    interface CallBack {

        public void onOk(ToolTip_Mdl value);
    }

    CallBack callBack;
    boolean isCloseNeeded = false;
    String caption = "Details";

    public ToolTip() {
        super();
        manageEvents();
    }

    public void show(List<ToolTip_Mdl> toolTip, Widget wdg) {
        popupPanel.hide();
        popupPanel.show();
        popupPanel.center();
        if (wdg != null) {
            popupPanel.showRelativeTo(wdg);
        }

        popupPanel.setGlassEnabled(false);
        popUpDetailsDisplay(toolTip);
    }

    public void show(List<ToolTip_Mdl> toolTip, Widget wdg, String caption, boolean isCloseBtnNeeded) {
        isCloseNeeded = isCloseBtnNeeded;
        if (!DmShared.xNull(caption).equalsIgnoreCase("")) {
            this.caption = caption;
        }
        lblCaption.setText(this.caption);
        show(toolTip, wdg);
    }

    public void show(List<ToolTip_Mdl> toolTip, DmGrid grid, int currentRowIndex, int relatedToColumnIndex, String caption, boolean isCloseBtnNeeded) {
        show(toolTip, null, caption, isCloseBtnNeeded);
        showRelativeToGridCell(currentRowIndex, relatedToColumnIndex, grid);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            popupPanel.hide();
        }
    }

    private void popUpDetailsDisplay(List<ToolTip_Mdl> data) {
        FlowPanel flwTable, flwTr = new FlowPanel(), flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "100%", null, null, DmEnum.DmPosition.CENTER, true);

        flwMain.clear();
        if (isCloseNeeded) {
            flwMain.add(flwHeader);
        }

        for (ToolTip_Mdl value : data) {
            flwTr = new FlowPanel();
            DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
            lblTemp = new DmLabel(value.getCaption());
            lblTemp.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            flwTd.add(lblTemp);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
            lblTemp = new DmLabel(": " + " " + value.getValue());
//            lblTemp.getElement().getStyle().setColor("Blue");
            flwTd.add(lblTemp);
            flwTr.add(flwTd);

            flwTable.add(flwTr);
        }

        flwMain.add(flwTable);
    }

    private void manageEvents() {
        btnClose.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popupPanel.hide();
            }
        });
        popupPanel.addAttachHandler(this);
    }

    private void showRelativeToGridCell(int currentRowIndex, int relatedToColumnIndex, DmGrid grid) {
        final Element target = grid.getGrid().getView().getCell(currentRowIndex, relatedToColumnIndex);

        popupPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                position(target, offsetWidth, offsetHeight);
            }
        });

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
        popupPanel.setPopupPosition(left, top);
    }

}
