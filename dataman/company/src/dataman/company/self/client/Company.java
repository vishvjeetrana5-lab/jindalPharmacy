package dataman.company.self.client;

import dataman.libraryMenu.self.shared.lmCompany;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.company.self.shared.Company_Model;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.self.shared.MenuClick;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import java.util.List;

public class Company extends Company_Dsg implements IsWidget {
    lmCompany pmMenu = new lmCompany();
    DmMenuItem.InitializeWidgetInvoker iwiTemp;
    DmMenuBar menu;

    public Company(final SetWidgetInvoker swiMain) {
        super();

        iwiTemp = new DmMenuItem.InitializeWidgetInvoker() {
            @Override
            public void initializeWidget(String menuText) {
                invokeSetWidget(menuText, swiMain);
            }
        };

        menu = new DmMenuBar();
//        menu.setHeight("25px");
    }

    public void addMenu(List<UserMenu> menuList) {
        DmClient.addMenuBarItem(menuList, menu, iwiTemp);
    }

    @Override
    public Widget asWidget() {
        return menu.asWidget();
    }

    public void invokeSetWidget(String menuText, SetWidgetInvoker swiMain) {
        UserMenu menuPermission;
        final PopupPanel ppCallBackWait = new PopupPanel(false, true);
        FlowPanel flwTemp = new FlowPanel();
        try {
            ppCallBackWait.setGlassStyleName("dm-PopupPanelGlass");
            flwTemp.setWidth("100%");
            ppCallBackWait.getElement().setAttribute("style", "border-radius: 50px 50px 50px 50px;background:none;border:none;rgb(59, 153, 217);padding:0px;");
            ppCallBackWait.add(DmClient.createProgressBar());
            ppCallBackWait.setGlassEnabled(true);
            ppCallBackWait.show();
            ppCallBackWait.center();
            menuPermission = DmClient.searchMenu(pmMenu.moduleName, menuText);

            if (menuPermission == null) {
                return;
            }

            if (swiMain == null) {
                return;
            }

            switch (menuText) {

//                case productLicensedTo:
//                    ppCallBackWait.hide(true);
//                    break;

                //==================== Reports =================================
                default:
                    ppCallBackWait.hide(true);
                    break;
            }
        } catch (Exception e) {
            ppCallBackWait.hide(true);
            Window.alert(e.getMessage());
        }
    }
}
