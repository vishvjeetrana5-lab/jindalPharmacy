package dataman.dmToolkitR.dmGridHelp.client;

import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import org.gwtbootstrap3.client.ui.html.Span;

public class DmGridCell<T extends KeyField> extends Span {

    private String value = "", dbField = "", cellWidth = "";
    private DmGridCellCaption<T> cellCaption = new DmGridCellCaption<T>(this);
    private DmGridCellValue<T> cellValue = new DmGridCellValue<T>(this);
    private DmGridRow<T> row;
    private Span cellGroup = new Span();
    private Span cellCaptionGroup = new Span();

    public DmGridCell() {
        super();
        this.setStyleName("dmCell");
        this.cellGroup.setStyleName("dmCellContent");
        this.cellGroup.add(cellCaptionGroup);
//        this.cellGroup.add(cellCaption);
        this.cellGroup.add(cellValue);
        this.add(cellGroup);

    }

    protected void setRow(DmGridRow<T> row) {
        this.row = row;
    }

    public DmGridRow<T> getRow() {
        return this.row;
    }

    public String getCaption() {
        return cellCaption.getText();
    }

    public void setCaption(String caption) {
        cellCaption.setText(caption);
        this.cellCaption.removeFromParent();
        this.cellValue.addStyleName("dmNoCaption");
        if (!DmShared.xNull(caption).trim().equalsIgnoreCase("")) {
            this.cellCaptionGroup.add(cellCaption);
            this.cellValue.removeStyleName("dmNoCaption");
        }
    }

    public String getValue() {
        return cellValue.getText();
    }

    public void setValue(String value) {
        cellValue.setText(value);
    }

    public String getDBField() {
        return dbField;
    }

    public void setDBField(String dbField) {
        this.dbField = dbField;
    }

    public DmGridCellCaption<T> getCaptionCell() {
        return cellCaption;
    }

    public DmGridCellValue<T> getValueCell() {
        return cellValue;
    }

    @Override
    public void setWidth(String width) {
        cellWidth = width;
        super.setWidth(cellWidth);
    }

    public String getWidth() {
        return cellWidth;
    }

}
