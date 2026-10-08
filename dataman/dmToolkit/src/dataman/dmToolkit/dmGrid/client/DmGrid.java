package dataman.dmToolkit.dmGrid.client;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.state.client.GridStateHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;

public class DmGrid<T1, T2> {

    final public static String defaultColumnIndexLabel = "defaultColumnIndex";

    public interface BeforeInitialize<T1> {

        public void beforeInitialize(ColumnModel<T1> columnModel);
    }

    public interface InitializeGridCallBack {

        public void onInitialized();
    }

    public interface CompleteEditCallBack<T1> {

        public void onCompleteEdit(CompleteEditEvent<T1> event);
    }

    public interface AutoAddRow<T1> {

        public T1 addRow(StartEditEvent<T1> event);
    }

    private BeforeInitialize beforeInitialize;
    private InitializeGridCallBack initializeGridCallBack;
    private CompleteEditCallBack completeEditCallBack;
    private AutoAddRow<T1> autoAddRow;
    private T2 t2PrpAcs; //? extends PropertyAccess
    private List<ColumnConfig<T1, ?>> lstColConfig;
    private ColumnModel<T1> colModel;
    private Grid<T1> grid = null;
    private GridInlineEditing<T1> gridedit = null;
    private int width = 0;
    private int height = 0;
    private Integer defaultColumnIndex = null;
    private boolean scrollLock;
    private int scrollPos;

    public void addBeforeInitializeHandler(BeforeInitialize beforeInitialize) {
        this.beforeInitialize = beforeInitialize;
    }

    public void addInitializeGridCallBackHandler(InitializeGridCallBack initializeGridCallBack) {
        this.initializeGridCallBack = initializeGridCallBack;
    }

    public void addCompleteEditCallBack(CompleteEditCallBack completeEditCallBack) {
        DmGrid.this.completeEditCallBack = completeEditCallBack;

        gridedit.addCompleteEditHandler(new CompleteEditEvent.CompleteEditHandler<T1>() {
            @Override
            public void onCompleteEdit(CompleteEditEvent<T1> event) {
                if (DmGrid.this.completeEditCallBack != null) {
                    DmGrid.this.completeEditCallBack.onCompleteEdit(event);
                }
            }
        });
    }

    public void addAutoAddRowCallBack(AutoAddRow<T1> autoAddRow) {
        DmGrid.this.autoAddRow = autoAddRow;

        gridedit.addStartEditHandler(new StartEditEvent.StartEditHandler<T1>() {
            @Override
            public void onStartEdit(StartEditEvent<T1> event) {
                if ((grid.getStore().size() - 1) == event.getEditCell().getRow()) {
                    if (DmGrid.this.autoAddRow != null) {
                        grid.getStore().add(DmGrid.this.autoAddRow.addRow(event));
                    }
                }
            }
        });
    }

//    public DmGrid(){
//        lstColConfig = new ArrayList<ColumnConfig<T1, ?>>();
//    }
    public DmGrid(Object t2PrpAcs) {
        this.t2PrpAcs = (T2) t2PrpAcs;
        lstColConfig = new ArrayList<ColumnConfig<T1, ?>>();
    }

    public void initializeGrid(ModelKeyProvider<T1> mkpVar, int width, int height) {
        initializeGrid(mkpVar, width, height, 0);
    }

    public void initializeGrid(ModelKeyProvider<T1> mkpVar, int width, String widthUnit, int height, String heightUnit, int autoExpandColumn) {
        this.width = width;
        this.height = height;
        initializeGridLocal(mkpVar, width + widthUnit, height + heightUnit, autoExpandColumn);
    }

    public void initializeGrid(ModelKeyProvider<T1> mkpVar, int width, int height, int autoExpandColumn) {
        this.width = width;
        this.height = height;
        initializeGridLocal(mkpVar, this.width + "px", this.height + "px", autoExpandColumn);
    }
    
    private void initializeGridLocal(ModelKeyProvider<T1> mkpVar, String width, String height, int autoExpandColumn) {
        ListStore<T1> lstStore;
        try {
            colModel = new ColumnModel<T1>(lstColConfig);
            lstStore = new ListStore<T1>(mkpVar);
            if (beforeInitialize != null) {
                beforeInitialize.<T1>beforeInitialize(colModel);
            }
//            grid = new Grid(lstStore, colModel);
            grid = new Grid(lstStore, colModel);
//            grid.getElement().setAttribute("style", "border: 1px solid lightgray;float:left;margin-top:1px;margin-bottom:1px;");
            grid.getElement().setAttribute("style", "border: 1px solid #999;float:left;margin-bottom:1px;");
            grid.setWidth(width);
            grid.setHeight(height);

            if (DmShared.vNull(autoExpandColumn) > 0) {
                grid.getView().setAutoExpandColumn(lstColConfig.get(autoExpandColumn - 1));
            }

            grid.getView().setStripeRows(true);
            grid.getView().setColumnLines(true);
            grid.getView().setSortingEnabled(false);
//            grid.setStateful(true);
            
            grid.setSelectionModel(new CellSelectionModel<T1>());
            if (initializeGridCallBack != null) {
                initializeGridCallBack.onInitialized();
            }

            grid.setData(DmGrid.defaultColumnIndexLabel, defaultColumnIndex);
            
//            GridStateHandler<T1> gridStateHandler = new  GridStateHandler<T1>(grid);
//            gridStateHandler.loadState();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    public void setEditor(int intColumnIndex, IsField<String> field) {
        if (lstColConfig != null) {
            if (lstColConfig.size() >= intColumnIndex) {
                if (gridedit == null) {
                    gridedit = new GridInlineEditing<>(grid);
                }
                gridedit.addEditor((ColumnConfig<T1, String>) lstColConfig.get(intColumnIndex - 1), field);
            }
        }
    }

    public T2 getPropertyAccess() {
        return t2PrpAcs;
    }

    public CheckBoxSelectionModel addCheckSelectionColumn(Style.SelectionMode mode) {
        CheckBoxSelectionModel checkBoxSelectionModel = new CheckBoxSelectionModel<T1>();
        ColumnConfig<T1, T1> col;
        if (mode != null) {
            checkBoxSelectionModel.setSelectionMode(mode);
        }
        col = checkBoxSelectionModel.getColumn();
        col.setMenuDisabled(true);
        col.setSortable(false);
//        col.setWidth(40);
        lstColConfig.add(col);
        return checkBoxSelectionModel;
    }

//    public DmColumnConfig<T1, String> addTextColumn(ValueProvider vp, String strheader, int intWidth, String dbFieldName) {
//        DmColumnConfig<T1, String> col = new DmColumnConfig<T1, String>(vp, intWidth, strheader);
//        col.setDbFieldName(dbFieldName.toLowerCase());
////        col.setDbFieldDataType(dbFieldDataType);
//        col.setMenuDisabled(true);
//        col.setSortable(false);
//        lstColConfig.add(col);
//
//        if (defaultColumnIndex == null) {
//            defaultColumnIndex = lstColConfig.size() - 1;
//        }
//        
//        return col;
////        return addTextColumn(vp, strheader, intWidth);
////        , DmEnum.DmDataType dbFieldDataType
//        
//    }
    public DmColumnConfig<T1, String> addTextColumn(ValueProvider vp, String header, int width, String dbFieldName) {
        DmColumnConfig<T1, String> col;
        col = addTextColumn(vp, header, width, dbFieldName, DmEnum.DmDataType.STRING);

        return col;
    }

    public DmColumnConfig<T1, String> addTextColumn(ValueProvider vp, String header, int width, String dbFieldName, DmEnum.DmDataType dbFieldDataType) {
        DmColumnConfig<T1, String> col = new DmColumnConfig<T1, String>(vp, width, header);
        col.setDbFieldName(DmShared.xNull(dbFieldName).toLowerCase());
        col.setDbFieldDataType(dbFieldDataType);

        col.setMenuDisabled(true);
        col.setSortable(false);
        lstColConfig.add(col);

        if (defaultColumnIndex == null) {
            defaultColumnIndex = lstColConfig.size() - 1;
        }
        return col;
    }

//    public DmColumnConfig<T1, String> addTextColumn(ValueProvider vp, String header, int width) {
//        DmColumnConfig<T1, String> col = new DmColumnConfig<T1, String>(vp, width, header);
//        col.setMenuDisabled(true);
//        col.setSortable(false);
//        lstColConfig.add(col);
//
//        if (defaultColumnIndex == null) {
//            defaultColumnIndex = lstColConfig.size() - 1;
//        }
//        return col;
//    }
    public RowNumberer<T1> addRowNumberColumn() {
        RowNumberer<T1> col = new RowNumberer<T1>();
        lstColConfig.add(col);
        return col;
    }

    public Grid<T1> getGrid() {
        return grid;
    }

    public void commitChanges() {
        grid.getStore().commitChanges();
        grid.getView().refresh(false);
    }

    public CellSelection getSelectedCell() {
        return ((CellSelectionModel) grid.getSelectionModel()).getSelectCell();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDefaultColumnIndex(int defaultColumnIndex) {
        this.defaultColumnIndex = defaultColumnIndex;
    }

    public void lockScroll() {
        if (grid != null) {
            this.scrollLock = true;
            Element scroller = this.grid.getView().getEditorParent();
            if (scroller != null) {
                this.scrollPos = scroller.getScrollTop();
            }
        }
    }

    public void releaseScroll() {
        if (grid != null) {
            refreshGridScroll();
            this.scrollLock = false;
        }
    }

    private void refreshGridScroll() {
        if (grid != null) {
            Element scroller = grid.getView().getEditorParent();
            if (scroller != null) {
                if (scrollLock) {

                    grid.getView().getEditorParent().setScrollTop(scrollPos);
                }
            }
        }
    }

}
