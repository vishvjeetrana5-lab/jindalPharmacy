package dataman.dmToolkit.self.shared;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.MenuItem;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class MenuClick implements Scheduler.ScheduledCommand {

    public interface InitializeWidgetInvoker {

        public void initializeWidget(String strMenuText);
    }
    MenuItem mnuItem = null;
    InitializeWidgetInvoker iwiMain = null;

    public MenuClick() {
    }

    public MenuClick(MenuItem mnuItemVar, InitializeWidgetInvoker iwiMainVar) {
        mnuItem = mnuItemVar;
        iwiMain = iwiMainVar;
    }

    @Override
    public void execute() {
        if (mnuItem == null) {
            new DmAlertMessageBox("Invalid request (null).", (DmTextBox) null).show();

        } else {
            if (iwiMain != null) {
                iwiMain.initializeWidget(mnuItem.getText());
            }
        }
    }
}
