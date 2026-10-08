package dataman.company.self.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

class Company_Dsg {

    DmMenuClick.InitializeWidgetInvoker iwiTemp;
    MenuBar menu;

    public Company_Dsg() {
    }

    public void ini(DmMenuClick.InitializeWidgetInvoker iwiTempVar) {
        iwiTemp = iwiTempVar;

        menu = new MenuBar();
        menu.setHeight("25px");
        menu.addItem(addGeneral());
    }

    public MenuItem addGeneral() {
        MenuBar mnuManageAccounts = new MenuBar(true);
        MenuItem mnuTemp = null;

//        mnuTemp = new MenuItem(productLicensedTo, new DmMenuClick());
//        mnuTemp.setScheduledCommand(new DmMenuClick(mnuTemp, iwiTemp));
//        mnuManageAccounts.addItem(mnuTemp);
//
//        mnuTemp = new MenuItem(manageCompany, mnuManageAccounts);
        return mnuTemp;
    }
}

class DmMenuClick implements Scheduler.ScheduledCommand {

    interface InitializeWidgetInvoker {

        public void initializeWidget(String strMenuText);
    }
    MenuItem mnuItem = null;
    InitializeWidgetInvoker iwiMain = null;

    public DmMenuClick() {
    }

    public DmMenuClick(MenuItem mnuItemVar, InitializeWidgetInvoker iwiMainVar) {
        mnuItem = mnuItemVar;
        iwiMain = iwiMainVar;
    }

    @Override
    public void execute() {
        if (mnuItem == null) {
            Window.alert("Invalid request (null).");
        } else {
            if (iwiMain != null) {
                iwiMain.initializeWidget(mnuItem.getText());
            }
        }
    }
}
