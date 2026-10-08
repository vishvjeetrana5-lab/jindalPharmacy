package dataman.dmToolkit.responsive.dmTopControl.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;

public class DmTopControl extends DmTopControl_Dsg implements IsWidget, DmRootEvents, AttachEvent.Handler {

    public interface DmTopControlCallBack {

        public void onClose();
    }

    DmTopControlCallBack callBack;

    public void addCallBack(DmTopControlCallBack callBack) {
        this.callBack = callBack;
    }

    public void setFormVisible(boolean visibleFormName) {
        columnFormName.setVisible(visibleFormName);
    }

    public void setCloseButtonVisible(boolean visibleCloseButton) {
        btnClose.setVisible(visibleCloseButton);
    }

    public void setFormName(String formName) {
        widgetName.setText(formName);
    }

    public void setToogleButtonVisible(boolean showButton) {
        btnTooogle.setVisible(showButton);
    }

    public DmTopControl(ColumnSize fixedColumnSize, ColumnSize collapseColumnSize) {
        super();
        flwFixedBox.setSize(ColumnSize.XX_12, fixedColumnSize);
        flwCollapseBox.setSize(ColumnSize.XX_12, collapseColumnSize);
        flwMenuDiv.setSize(ColumnSize.XX_12, collapseColumnSize);
        iniEvents();

    }

    private void iniEvents() {
        lblCloseSlider.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hideSlider();
            }
        });

        btnClose.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                callBack.onClose();
            }
        });

        btnTooogle.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                openSlider();
            }
        });
    }

    private void openSlider() {
        if (flwSilder.isVisible() && flwSilder.isAttached()) {
            hideSlider();
        } else {
            flwSilder.show();
            flwTempSilder.show();
            flwSilder.getElement().setAttribute("Style", "padding:0px; color:white;z-index:201; position:absolute; right:0px; top:40px; bottom:0px; display:block; border: solid 1px black;");
            flwTempSilder.getElement().setAttribute("Style", "width:100%; padding:0px; color:white; background-color:rgb(0, 0, 0);opacity:0.85;position:absolute; right:0px; top:40px; bottom:0px; display:block; border: solid 1px black;");
        }
    }

    public void hideSlider() {
//        btnTooogle.getElement().setAttribute("aria-expanded", "false");
//        flwCollapseBox.setStyleName("navbar-collapse collapse col-xx-12 col-xs-5 col-sm-5 col-md-5 col-lg-5");

        flwSilder.hide();
        flwSilder.getElement().setAttribute("Style", "display:none");

        flwTempSilder.hide();
        flwTempSilder.getElement().setAttribute("Style", "display:none");
    }

    public void addWidget(Column column, boolean isFixed) {
        if (isFixed) {
            flwFixedBox.add(column);
        } else {
            flwMenuDiv.add(column);
        }
    }

    public void addWidgetInSliderBox(Column column) {
        flwSliderWorkingArea.add(column);
    }

    @Override
    public Widget asWidget() {
        return hrPnl;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

    }
}
