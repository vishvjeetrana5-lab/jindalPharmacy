package dataman.dmToolkitR.dmPageHelp.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkitR.dmGridHelp.client.DmGridCell;
import dataman.dmToolkitR.dmGridHelp.client.DmGridRow;
import dataman.dmToolkitR.dmGridPaging.client.DmGridPaging;
import java.util.List;

public class DmPageHelp<T extends KeyField> extends DmPageHelp_Dsg implements ClickHandler, AttachEvent.Handler {

    @Override
    public Widget asWidget() {
        return helpModal;
    }

    public interface RowConfigCallBack<T extends KeyField> {

        public void rowConfig(DmGridRow<T> row, T tRow);
    }

    public interface PageHelpCallBack<T extends KeyField> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);

        public void getSelectedRow(T tRow);
    }

    private PageHelpCallBack<T> pageHelpCallBack;
    private RowConfigCallBack<T> rowConfig;

    public DmPageHelp(String caption, String dbField, int rowsPerPage) {
        super(dbField, rowsPerPage);
        captionSpan.setText(caption);
        addEvents();
    }

    public void addPageHelpCallBack(PageHelpCallBack<T> pageHelpCallBack) {
        this.pageHelpCallBack = pageHelpCallBack;
    }

    public void addRowConfig(RowConfigCallBack<T> rowConfig) {
        this.rowConfig = rowConfig;
    }

    public DmGridCell<T> addColumn(String dbField, String caption, String width) {
        return grid.addColumn(dbField, caption, width);
    }

    private void addEvents() {
        helpModal.addAttachHandler(this);
        backIcn.addClickHandler(this);

        grid.addRowConfig(new DmGridPaging.RowConfigCallBack<T>() {
            @Override
            public void rowConfig(DmGridRow<T> row, T tRow) {
                if (rowConfig != null) {
                    rowConfig.rowConfig(row, tRow);
                }
            }
        });

        grid.addGridPagingCallBack(new DmGridPaging.GridPagingCallBack<T>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (pageHelpCallBack != null) {
                    pageHelpCallBack.populateData(helper, blnToEndOfFile, blnCallBackWait);
                }
            }

            @Override
            public void getSelectedRow(T tRow, DmGridRow<T> row) {
                if (pageHelpCallBack != null) {
                    pageHelpCallBack.getSelectedRow(tRow);
//                    helpModal.hide();
                    helpModal.removeFromParent();
                }
            }
        });
    }

    public DmHelper getHelper() {
        return grid.getHelper();
    }

    public void setGridSize(int size) {
        grid.getGrid().setDmGridSize(size);
    }

    public void setAllData(List<T> lstData, DmPaging pageingVariables) {
        grid.setAllData(lstData, pageingVariables);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(backIcn)) {
            helpModal.removeFromParent();
        }
    }

    public void show(String searchText) {
        RootPanel.get().add(helpModal);
        grid.reQuery(searchText);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                //    grid.setFocus();
            }
        });
    }

    public void hide() {
        helpModal.removeFromParent();
//        helpModal.hide();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            RootPanel.get().addStyleName("dm-modal-open");
            helpModal.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        } else {
            RootPanel.get().removeStyleName("dm-modal-open");
            helpModal.getElement().getStyle().setDisplay(Style.Display.NONE);
        }

    }

}
