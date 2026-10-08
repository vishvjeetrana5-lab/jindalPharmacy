package dataman.dmToolkit.dmGridPaging.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import java.util.List;

public class DmGridPaging<T1, T2, TypeParam> extends DmGridPaging_Dsg implements IsWidget, ClickHandler {

    public interface CallBack<TypeParam> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, TypeParam param);

        public TypeParam getParam();
    }

    private TypeParam param;
    private DmHelper helper;
    private DmGrid<T1, T2> dmGrid;
    private CallBack callBack;
    private int gridHeight = 0;

    private void gridEvents(Object t2PrpAcs) {
        dmGrid = new DmGrid<T1, T2>(t2PrpAcs);
        dmGrid.addInitializeGridCallBackHandler(new DmGrid.InitializeGridCallBack() {
            @Override
            public void onInitialized() {
                flwGrid.clear();
                dmGrid.getGrid().getElement().getStyle().setBorderStyle(Style.BorderStyle.NONE);
                flwGrid.add(dmGrid.getGrid());

                if (helper.getRowsPerPage() > 0) {
                    resizeHeight();
                }
            }
        });
    }

    public void resizeHeight() {
        dmGrid.getGrid().setHeight(String.valueOf(gridHeight) + "px");
        flwGridScroller.setHeight(gridHeight + "px");
    }

    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    private void btnEvents(Button[] btn) {
        for (Button btn1 : btn) {
            btn1.addClickHandler(this);
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setData(List<T1> lstData, DmPaging pageingVariables) {
        helper.setPaging(pageingVariables);
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        if (lstData.size() > 0) {
            dmGrid.getGrid().getStore().addAll(lstData);
        }
        setDisplay(pageingVariables);
    }

    public DmGrid<T1, T2> getDmGrid() {
        return dmGrid;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int rowsPerPage, boolean withHorizontalScroll) {
        gridHeight = ((rowsPerPage + 1) * 22) + 2;
        if (withHorizontalScroll) {
            gridHeight += 18;
        }
    }

    public DmHelper getHelper() {
        return helper;
    }

    public DmGridPaging(Object t2PrpAcs, int rowsPerPage, boolean withHorizontalScroll) {
        super();
        startUp(t2PrpAcs, rowsPerPage, withHorizontalScroll);
    }

    public DmGridPaging(Object t2PrpAcs, int rowsPerPage) {
        super();
        startUp(t2PrpAcs, rowsPerPage, false);
    }

    private void startUp(Object t2PrpAcs, int rowsPerPage, boolean withHorizontalScroll) {

        setGridHeight(rowsPerPage, withHorizontalScroll);
        gridEvents(t2PrpAcs);
        helper = new <T1> DmHelper(0, rowsPerPage);
        btnEvents(new Button[]{btnFirst, btnPrevious, btnNext, btnLast});
//        helper.addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
    }

    @Override
    public Widget asWidget() {
        return vrtForm;
    }

    private void setDisplay(DmPaging pageingVariables) {
        if (pageingVariables == null) {
            btnFirst.setVisible(false);
            btnPrevious.setVisible(false);

            btnNext.setVisible(false);
            btnLast.setVisible(false);
        } else {
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
        }
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

        if (aSource.equals(btnFirst)) {
            navigation(btnFirst);
        } else if (aSource.equals(btnPrevious)) {
            navigation(btnPrevious);
        } else if (aSource.equals(btnNext)) {
            navigation(btnNext);
        } else if (aSource.equals(btnLast)) {
            navigation(btnLast);
        }
    }

    public void reQuery() {
//        dmGrid.getGrid().getStore().clear();
//        dmGrid.getGrid().getSelectionModel().deselectAll();
//        if (callBack != null) {
//            helper.setRowsStartFrom(0);
//            param = (TypeParam) callBack.getParam();
//            callBack.populateData(helper, false, param);
//        }
        reQuery(0, (TypeParam) callBack.getParam());
    }

    public void reQuery(int rowStartFrom, TypeParam paramTemp) {
        dmGrid.getGrid().getStore().clear();
        dmGrid.getGrid().getSelectionModel().deselectAll();
        if (callBack != null) {
            helper.setRowsStartFrom(rowStartFrom);
            param = (paramTemp == null ? (TypeParam) callBack.getParam() : paramTemp);
            callBack.populateData(helper, false, param);
        }
    }

    public void clear() {
        if (dmGrid.getGrid() != null) {
            if (dmGrid.getGrid().getStore() != null) {
                dmGrid.getGrid().getStore().clear();
            }
        }
        setDisplay(null);
        param = null;
    }

    private void navigation(Button btnPressed) {
        boolean blnToEndOfFile = false;

        if (callBack != null) {
            if (btnFirst == btnPressed) {
                helper.setRowsStartFrom(0);
            } else if (btnPrevious == btnPressed) {
                helper.setRowsStartFrom(helper.getRowsStartFrom() - helper.getRowsPerPage());
            } else if (btnNext == btnPressed) {
                helper.setRowsStartFrom(helper.getRowsStartFrom() + helper.getRowsPerPage());
            } else if (btnLast == btnPressed) {
                blnToEndOfFile = true;
            }
            param = (TypeParam) callBack.getParam();
            callBack.populateData(helper, blnToEndOfFile, param);
        }
    }
}
