package dataman.dmToolkit.dmGrid.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class GridValueChangeHandler implements ValueChangeHandler<String> {

    public interface CommitChangesCallBack {

        public void onCommitChanges(int columnIndex, ValueChangeEvent<String> event);
    }
    private int columnIndex = 0;
    private CommitChangesCallBack commitChangesCallBack;

    public GridValueChangeHandler(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        if (commitChangesCallBack != null) {
            commitChangesCallBack.onCommitChanges(columnIndex, event);
        }
    }
}
