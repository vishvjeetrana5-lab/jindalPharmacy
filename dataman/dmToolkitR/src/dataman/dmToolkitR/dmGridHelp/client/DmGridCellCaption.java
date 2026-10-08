package dataman.dmToolkitR.dmGridHelp.client;

import dataman.dmBase.self.shared.KeyField;
import org.gwtbootstrap3.client.ui.html.Span;

public class DmGridCellCaption<T extends KeyField> extends Span {

    DmGridCell<T> cell;
    private String caption = "";

    public DmGridCellCaption(DmGridCell<T> cell) {
        this.cell = cell;
        this.setStyleName("dmCaption");
    }

    @Override
    public String getText() {
        return caption;
    }

    @Override
    public void setText(String caption) {
        this.caption = caption;
        super.setText(caption);
    }

    public DmGridCell<T> getCell() {
        return cell;
    }
}
