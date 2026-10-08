package dataman.dmToolkitR.dmGridHelp.client;

import dataman.dmBase.self.shared.KeyField;
import org.gwtbootstrap3.client.ui.html.Span;

public class DmGridCellValue<T extends KeyField> extends Span {

    DmGridCell<T> cell;
    private String value = "";

    public DmGridCellValue(DmGridCell<T> cell) {
        this.cell = cell;
        this.setStyleName("dmValue");
    }

    @Override
    public String getText() {
        return this.value;
    }

    @Override
    public void setText(String value) {
        this.value = value;
        super.setText(value);
    }

    public DmGridCell<T> getCell() {
        return cell;
    }
}
