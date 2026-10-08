package aarogya.ribbon.client;

import aarogya.common.shared.Common_Model;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

abstract class Ribbon_Dsg {

    final VerticalPanel flwForm = new VerticalPanel();
    final FlowPanel flwInnerForm = new FlowPanel();
    final FlowPanel flwMenu = new FlowPanel();
    final FlowPanel flwtmpHeader = new FlowPanel();
    final FlowPanel flwPLTContainer = new FlowPanel();
    final FlowPanel flwaPLT = new FlowPanel();
    final Anchor aPLT = new Anchor();
    final FlowPanel flwCompanyContainer = new FlowPanel();
    final FlowPanel flwaCompany = new FlowPanel();
    final Anchor aCompany = new Anchor();
    final FlowPanel flwBranchNameContainer = new FlowPanel();
    final FlowPanel flwaBranchName = new FlowPanel();
    final Anchor aBranchName = new Anchor();
    final FlowPanel flwMoudelNameContainer = new FlowPanel();
    final FlowPanel flwMoudelName = new FlowPanel();
    final Anchor aMoudelName = new Anchor();
    final Anchor aLock = new Anchor();
    final Anchor aSearch = new Anchor();
    final Anchor aAlert = new Anchor();
    final Anchor aSetting = new Anchor();
    final Anchor aSkype = new Anchor();
    final FlowPanel flwOther = new FlowPanel();
    final FlowPanel flwCompanyInfo = new FlowPanel();
    final PopupPanel flwMoludePopup = new PopupPanel(true);
    final FlowPanel flwMenuShortHand = new FlowPanel();
    final FlowPanel flwMenuIconStyle = new FlowPanel();
    final PopupPanel flwAlertBox = new PopupPanel(true);
    final FlowPanel flwAlertBoxContainer = new FlowPanel();
    final PopupPanel flwShowTempPopup = new PopupPanel();
    final PopupPanel flwLoginUserBox = new PopupPanel(true);
    final DmLabel userNameLabel = new DmLabel("");
    final DmLabel userDescriptionLabel = new DmLabel("");
    final DmLabel emailLabel = new DmLabel("");
    final DmLabel buildDetail = new DmLabel("");
    final Anchor aLogout = new Anchor();
    final Anchor aPassChange = new Anchor();
    final Anchor aLogoutIcon = new Anchor();
    final FlowPanel flwCurrentList = new FlowPanel();
    final Anchor aAlertCount = new Anchor();
    final DmTextBox txtMenuShortHand = new DmTextBox(6, false);
    final FlowPanel pnlMoludeIcons = new FlowPanel();
    CheckBox viewReportInNewWindow = new CheckBox("View Reports in New Window");
    Button pacHandShakeBtn = new Button();
    Button repairTransactionDBButton = new Button();
    Button repairCompanyDBButton = new Button();
    Button repairMenu = new Button();
    Button localSetting = new Button();
    Button initializeDefaults = new Button();
    int intPadLeft = 0, intPadRight = 0;
    final InlineLabel ilblLogo = new InlineLabel();

    final PopupPanel flwSetting = new PopupPanel(true);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    public abstract void onBtnClick(ClickEvent event);

    public Ribbon_Dsg() {
        ini();
        handlerBtnClick(new InlineLabel[]{ilblLogo});
        handlerBtnClick(new Anchor[]{aAlertCount, aLogoutIcon, aSearch, aLock, aAlert, aSkype, aSetting, aPLT, aCompany, aBranchName, aMoudelName, aLogout, aPassChange});
        handlerBtnClick(new Button[]{repairTransactionDBButton, initializeDefaults, repairCompanyDBButton, repairMenu, pacHandShakeBtn, localSetting});
    }

    private void handlerBtnClick(InlineLabel[] btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    onBtnClick(event);
                }
            });
        }
    }

    private void handlerBtnClick(Anchor[] btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    onBtnClick(event);
                }
            });
        }
    }

    private void handlerBtnClick(Button[] btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    onBtnClick(event);
                }
            });
        }
    }

    private void ini() {
        FlowPanel flwMenuContainer = new FlowPanel();
        flwForm.setWidth("100%");

        flwInnerForm.setStyleName("TopHeader");
        flwInnerForm.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        flwInnerForm.add(createLogo());
        flwInnerForm.add(createRoute());

        flwMenu.getElement().setAttribute("style", "position: relative;top: 0px;max-width:500px; z-index:100; overflow-x:auto;");
        flwMenuContainer.getElement().getStyle().setFloat(Style.Float.LEFT);
        flwMenuContainer.add(flwMenu);
        flwInnerForm.add(flwMenuContainer);

        flwInnerForm.add(createModulePopup());
        flwInnerForm.add(createOther());
        flwInnerForm.add(createCompanyInfo());
        flwInnerForm.setWidth("100%");

//        flwDownArrow.setVisible(false);
    }

    private FlowPanel createRoute() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setStyleName("breadcrumb flat");

        flwMoudelName.add(aMoudelName);
        flwMoudelName.addStyleName("CurrMoudeNameHeader");
        flwTemp.add(flwMoudelNameContainer);

        return flwTemp;
    }

    private PopupPanel createModulePopup() {
        FlowPanel flwTempMain = new FlowPanel();
        FlowPanel flwTemp = new FlowPanel();
        flwMoludePopup.setVisible(false);
        flwMoludePopup.setStyleName("modulelistMenu");
        flwMoludePopup.getElement().getStyle().setDisplay(Style.Display.NONE);
        pnlMoludeIcons.addStyleName("modulelist");
        flwTemp.add(pnlMoludeIcons);
        flwTemp.setStyleName("moduleContainer");
        flwTempMain.add(flwTemp);
        flwMoludePopup.add(flwTempMain);
        return flwMoludePopup;
    }

    public void setModuleIcon(List<Common_Model.dmImgResourse> lstImgRes, ClickHandler clickHandler) {

        FlowPanel grdModuleIcons = new FlowPanel();
        InlineLabel lbl;
        FlowPanel flwTemp, flwImage;

        for (int intColIndex = 0; intColIndex < lstImgRes.size(); intColIndex++) {
            flwTemp = new FlowPanel();

            flwImage = new FlowPanel();
            flwImage.getElement().setAttribute("Style", "width:100%;");
            flwTemp.getElement().setAttribute("Style", "background-color:white; width:100px;height:64px; cursor: pointer; text-align:center; padding:5px; float:left;");
            lbl = new InlineLabel(DmShared.xNull(lstImgRes.get(intColIndex).getName()).trim());
            lbl.getElement().setAttribute("Style", "background-color:white;width:100px;white-space: nowrap;  display: inline-block;float:left;");
            Image image = new Image(lstImgRes.get(intColIndex).getImg());
            image.getElement().getStyle().setWidth(42, Style.Unit.PX);
            image.getElement().getStyle().setHeight(42, Style.Unit.PX);
            flwImage.add(image);
            flwTemp.add(flwImage);
            flwTemp.add(lbl);
            flwTemp.setTitle(DmShared.xNull(lstImgRes.get(intColIndex).getName()).trim());
            flwTemp.addDomHandler(clickHandler, ClickEvent.getType());
            grdModuleIcons.add(flwTemp);
        }

        pnlMoludeIcons.clear();
        pnlMoludeIcons.add(grdModuleIcons);
    }

    private FlowPanel createLogo() {
        FlowPanel flwTemp = new FlowPanel();

        flwTemp.setStyleName("dmLogo");
        ilblLogo.setStyleName("fa fa-home " + "LoginImage");
        ilblLogo.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        flwTemp.add(ilblLogo);

        return flwTemp;
    }

    public FlowPanel createMenuShortHand() {
//        flwMenuShortHand.setVisible(true);
        flwMenuShortHand.getElement().setAttribute("style", "float: right; width: 40px;position:relative;height:34px;");

        flwMenuIconStyle.getElement().setAttribute("style", "position: absolute; background: transparent;width: 100%;height: 98%;");
        aSearch.setStyleName("fa fa-search ");
        aSearch.getElement().setAttribute("style", "padding-top: 10px; padding-left: 15px;font-size:16px; top:9px;color: rgb(125, 125, 125);width: 100%;height: 100%;");
        flwMenuIconStyle.setStyleName("dm-mouse-hovering");
        flwMenuIconStyle.add(aSearch);
        flwMenuShortHand.add(flwMenuIconStyle);
        txtMenuShortHand.getElement().setPropertyString("placeholder", "");
        txtMenuShortHand.getElement().setAttribute("style", "height: 32px; border: medium none ! important; width: 100%;");
        flwMenuShortHand.add(txtMenuShortHand);

        return flwMenuShortHand;
    }

    public PopupPanel createAlertBox() {
        flwAlertBox.setVisible(false);
        flwAlertBox.addStyleName("notificationBox");
        flwAlertBox.getElement().getStyle().setPosition(Style.Position.FIXED);
        flwAlertBox.add(flwAlertBoxContainer);

        return flwAlertBox;
    }

    public PopupPanel createLoginUserBox() {
        flwLoginUserBox.setVisible(false);
        flwLoginUserBox.addStyleName("dm-login-user-box");
        flwLoginUserBox.getElement().getStyle().setPosition(Style.Position.FIXED);

        FlowPanel flwPanel = new FlowPanel();
        /* Main Body Container For Logout Section */
        FlowPanel flwUpPanel = new FlowPanel();
        flwUpPanel.setStyleName("dm-body-row-container");
        /* Main Icon Section For Logout Section*/
        FlowPanel flwUserIcon = new FlowPanel();
        FlowPanel flwUserIconContainer = new FlowPanel();
        flwUserIconContainer.getElement().setAttribute("style", "width: 30%; display: inline-block;");
        flwUserIcon.setStyleName("fa fa-user dm-user-icon");
        flwUserIcon.getElement().setAttribute("style", "font-size: 35px;text-align: left;");
        flwUserIconContainer.add(flwUserIcon);
        flwUpPanel.add(flwUserIconContainer);
        /* Main User Description Section For Logout Section*/

        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setStyleName("dm-user-name-style");
        flwTemp.getElement().setAttribute("style", "width: 70% ! important;");
//        userNameLabel.setStyleName("dm-user-name-style");
        flwTemp.add(userDescriptionLabel);

        userNameLabel.getElement().setAttribute("style", userNameLabel.getElement().getAttribute("style")
                + "color: #777;font-weight: normal;");
        flwTemp.add(userNameLabel);

        emailLabel.getElement().setAttribute("style", emailLabel.getElement().getAttribute("style")
                + "color: rgb(0, 102, 255); font-weight: normal;");
        flwTemp.add(emailLabel);

        buildDetail.getElement().setAttribute("style", buildDetail.getElement().getAttribute("style")
                + "color: rgb(170, 63, 252); font-weight: normal;");
        flwTemp.add(buildDetail);

//        flwUpPanel.add(flwTemp);
//        flwTemp = new FlowPanel();
//        emailLabel.setStyleName("dm-user-name-style");
        flwUpPanel.add(flwTemp);

        flwPanel.add(flwUpPanel);
        /* Main User Footer with Logout Button For Logout*/
        FlowPanel flwDownPanel = new FlowPanel();
        flwDownPanel.setStyleName("dm-footer-logout-style");
        aPassChange.setText("Change Password");
        aPassChange.setStyleName("ChngPass");
        flwDownPanel.add(aPassChange);
        aLogout.setText("Log Out");
        aLogout.setStyleName("LogOut");
        flwDownPanel.add(aLogout);
        flwPanel.add(flwDownPanel);

//        flwTemp.getElement().setAttribute("style", "width:100%;");
//        FlowPanel flwUserIcon = new FlowPanel();
//        flwLogout.setStyleName("LogOutDiv");
//        flwTemp.add(flwUserIcon);
//        flwUserIcon.setStyleName("fa fa-user dm-user-icon");
//        flwUserIcon.getElement().setAttribute("style", "font-size: 35px;text-align: center;");
//        flwPanel.add(flwTemp);
//        flwTemp = new FlowPanel();
//        flwTemp.getElement().setAttribute("style", "width:100%;");
//        flwLogout.setStyleName("LogOutDiv");
//        flwTemp.add(userNameLabel);
//        userNameLabel.getElement().setAttribute("style", "text-align:center;float:none;padding:11px 45px;");
//        userNameLabel.setStyleName("dm-user-name-style");
//        flwPanel.add(flwTemp);
//        flwTemp = new FlowPanel();
//        flwTemp.getElement().setAttribute("style", "width: 100%; margin-top: 11px; margin-bottom: 7px;");
//        aLogout.setText("Log Out");
//        aLogout.setStyleName("LogOut");
//        flwTemp.add(aLogout);
//        flwPanel.add(flwTemp);
        flwLoginUserBox.add(flwPanel);

        return flwLoginUserBox;
    }

    FlowPanel flwSkypeContainer = new FlowPanel();

    public FlowPanel createCompanyInfo() {
        flwCompanyInfo.setStyleName("divRightHeader breadcrumb flat");

        flwBranchNameContainer.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwBranchNameContainer.addStyleName("dm-parent-top-header");
        flwaBranchName.add(aBranchName);
        aBranchName.addStyleName("SiteNameHeader");
        flwCompanyInfo.add(flwBranchNameContainer);

        flwCompanyContainer.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwCompanyContainer.addStyleName("dm-parent-top-header");
        flwaCompany.add(aCompany);
        aCompany.addStyleName("FinancialNameHeader");
        flwCompanyInfo.add(flwCompanyContainer);

        flwPLTContainer.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwPLTContainer.addStyleName("dm-parent-top-header");
        flwaPLT.add(aPLT);
        aPLT.addStyleName("CompanyNameHeader");
        flwCompanyInfo.add(flwPLTContainer);

        flwSkypeContainer.setStyleName("dmSkype");
        flwCompanyInfo.add(flwSkypeContainer);
        return flwCompanyInfo;
    }

    public FlowPanel createOther() {
        flwOther.add(createLoginUserBox());
        flwOther.add(createAlertBox());
        flwOther.add(createSettingPopup());

        aSetting.setStyleName("fa fa-ellipsis-v " + "SettingImage dm-mouse-hovering");
        flwOther.add(aSetting);

//        aLogoutIcon.addStyleName("loginUserName");
        aLogoutIcon.setStyleName("fa fa-power-off " + "UserImage dm-mouse-hovering");
//        aLogoutIcon.setText("");sdfsdf
        flwOther.add(aLogoutIcon);

        aLock.setStyleName("fa fa-lock " + "flag dm-mouse-hovering");
        aLock.getElement().setAttribute("style", "font-size: 19px; top: 0px; width: auto; padding: 8px 10px 10px;");
        flwOther.add(aLock);

        aAlertCount.addStyleName("notificationCount");
        aAlertCount.setText("0");
        flwOther.add(aAlertCount);
        aAlert.setStyleName("fa fa-bell-o " + "notification dm-mouse-hovering");
//        aAlert.getElement().setAttribute("style", "padding-left: 10px;");
        flwOther.add(aAlert);

        aSkype.setStyleName("fa fa-skype " + "SkypeImage dm-mouse-hovering");
        flwOther.add(aSkype);

        flwOther.add(createMenuShortHand());

        flwOther.setVisible(false);
        return flwOther;
    }

    public PopupPanel createSettingPopup() {
        flwSetting.setVisible(false);
        flwSetting.getElement().getStyle().setDisplay(Style.Display.NONE);
        flwSetting.add(tabArea());

        return flwSetting;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "500px", null, null, DmPosition.NONE, true);

        tabPanel.add(flwCurrentList, "Recent Activities");
        tabPanel.add(generalSettingArea(), "General Setting");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    public VerticalPanel generalSettingArea() {
        VerticalPanel verticalPanel = new VerticalPanel();
        Anchor aBtn;
        InlineLabel label;

        verticalPanel.setWidth("100%");

        viewReportInNewWindow.addStyleName("dm-right-corner-header");
        verticalPanel.add(viewReportInNewWindow);

        label = new InlineLabel("PAC Handshake");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-link");
        aBtn.setWidth("30px");
        aBtn.getElement().getStyle().setColor("gray");
        pacHandShakeBtn.getElement().appendChild(aBtn.getElement());
        pacHandShakeBtn.getElement().appendChild(label.getElement());
        pacHandShakeBtn.addStyleName("dm-right-corner-style");
        verticalPanel.add(pacHandShakeBtn);

        label = new InlineLabel("Local Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-cog");
        aBtn.setWidth("30px");
        aBtn.getElement().getStyle().setColor("gray");
        localSetting.getElement().appendChild(aBtn.getElement());
        localSetting.getElement().appendChild(label.getElement());
        localSetting.addStyleName("dm-right-corner-style");
        verticalPanel.add(localSetting);

//        label = new InlineLabel("Show Camera");
//        aBtn = new Anchor();
//        aBtn.addStyleName("fa fa-video-camera");
//        aBtn.setWidth("30px");
//        aBtn.getElement().getStyle().setColor("gray");
//        showCameraBtn.getElement().appendChild(aBtn.getElement());
//        showCameraBtn.getElement().appendChild(label.getElement());
//        showCameraBtn.addStyleName("dm-right-corner-style");
//        verticalPanel.add(showCameraBtn);
        label = new InlineLabel("Repair transaction db");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-database");
        aBtn.setWidth("30px");
        aBtn.getElement().getStyle().setColor("gray");
        repairTransactionDBButton.getElement().appendChild(aBtn.getElement());
        repairTransactionDBButton.getElement().appendChild(label.getElement());
        repairTransactionDBButton.addStyleName("dm-right-corner-style");
        verticalPanel.add(repairTransactionDBButton);

        label = new InlineLabel("Repair company db");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-database");
        aBtn.setWidth("30px");
        aBtn.getElement().getStyle().setColor("gray");
        repairCompanyDBButton.getElement().appendChild(aBtn.getElement());
        repairCompanyDBButton.getElement().appendChild(label.getElement());
        repairCompanyDBButton.addStyleName("dm-right-corner-style");
        verticalPanel.add(repairCompanyDBButton);

        label = new InlineLabel("Repair Menu");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-bars");
        aBtn.setWidth("30px");
        aBtn.getElement().getStyle().setColor("gray");
        repairMenu.getElement().appendChild(aBtn.getElement());
        repairMenu.getElement().appendChild(label.getElement());
        repairMenu.addStyleName("dm-right-corner-style");
        verticalPanel.add(repairMenu);

        label = new InlineLabel("Initialize Defaults");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plug");
        aBtn.setWidth("30px");
        aBtn.getElement().getStyle().setColor("gray");
        initializeDefaults.getElement().appendChild(aBtn.getElement());
        initializeDefaults.getElement().appendChild(label.getElement());
        initializeDefaults.addStyleName("dm-right-corner-style");
        verticalPanel.add(initializeDefaults);

        return verticalPanel;
    }

}
