package aarogya.lock.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;

abstract class Lock_Dsg {

    final PopupPanel popupPanel = new PopupPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PasswordTextBox txtpass = new PasswordTextBox();
    final DmLabel lblUserName = new DmLabel("");
    final Button btnUnlock = new Button("Unlock");
    final Button btnSwitchUser = new Button("Switch User");

    public Lock_Dsg() {
        ini();
    }

    private void ini() {
        popupPanel.setGlassEnabled(true);
        popupPanel.setGlassStyleName("gwt-PopupPanelGlass dm-Lock-Glass");
        popupPanel.addStyleName("dm-lock-panel-container");
        popupPanel.setWidth("818px");
        FlowPanel flwLock = new FlowPanel();
        flwLock.setStyleName("dm-FlwForm-Login " + "dm-Login-container");
        flwLock.add(createBannerDesign());
        flwLock.add(createLoginDesign());
        popupPanel.add(flwLock);
    }

    public FlowPanel createBannerDesign() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setStyleName("dm-bg-banner");
        flwTemp.setHeight("100%");
        return flwTemp;
    }

    public FlowPanel createLoginDesign() {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setStyleName("dm-Login-style");
//        flwTemp.setHeight("350px");
        flwTemp.add(createUserheader());
        flwTemp.add(createLoginContent());
        flwTemp.add(createNextPreviousButton());

        return flwTemp;
    }

    public FlowPanel createUserheader() {
        FlowPanel userTmp = new FlowPanel();
        userTmp.setStyleName("dm-User-Heading");
        return userTmp;
    }

    public FlowPanel createLoginheader() {
        FlowPanel tmp = new FlowPanel();

        tmp.setStyleName("dm-Login-Heading");
        tmp.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");
        tmp.setHeight("40px");
        Label lblheading = new Label();

        lblheading.setText("User Authentication");
        lblheading.setStyleName("dm-Login-LblHeading");

        tmp.add(lblheading);

        return tmp;
    }

    public FlowPanel createLoginContent() {
        FlowPanel tmp = new FlowPanel();
        tmp.setStyleName("dm-FlwLeft");
//        tmp.setHeight("255px");
        tmp.setWidth("100%");
        tmp.add(createLoginInput());
        return tmp;
    }

    public FlowPanel createLoginInput() {
        FlowPanel vTmp = new FlowPanel();

        vTmp.setStyleName("dm-input-controls " + "dm-padding30px");
//        vTmp.setWidth("300px");
        lblUserName.setStyleName("dm-Login-TextBox");
        lblUserName.setWidth("100%");

        txtpass.setStyleName("dm-Login-TextBox");
        txtpass.setWidth("100%");
        txtpass.getElement().setPropertyString("placeholder", "Password");

        vTmp.add(vl.add(lblUserName));
        vTmp.add(vl.add(txtpass));

        return vTmp;
    }

    public FlowPanel createNextPreviousButton() {

        FlowPanel tmp = new FlowPanel();
//        tmp.setHeight("60px");
        tmp.setWidth("100%");
        tmp.setStyleName("dm-FlwLeft");
//        FlowPanel flwLine = new FlowPanel();
//        flwLine.setStyleName("dm-Line-Horizontal");
//        tmp.add(flwLine);

//        FlowPanel flwblank;
//        flwblank = new FlowPanel();
//        flwblank.setStyleName("dm-FlwLeft");
//        flwblank.setHeight("5px");
//        flwblank.setWidth("100%");
//        tmp.add(flwblank);
        FlowPanel divcontent = new FlowPanel();

//        btnUnlock.setHeight("30px");
//        btnUnlock.setWidth("48%");
        btnSwitchUser.setStyleName("dm-Login-Button " + "dm-lock-button-style next-color dm-swtchusr-btn-style");

//        btnSwitchUser.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");
//        btnSwitchUser.setHeight("30px");
//        btnSwitchUser.setWidth("48%");
        btnUnlock.setStyleName("dm-Login-Button " + "dm-lock-button-style next-color");
//        btnUnlock.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");
//        divcontent.add(flwblank);
        divcontent.add(vl.add(btnUnlock));

        divcontent.add(vl.add(btnSwitchUser));

        tmp.add(divcontent);

        return tmp;

    }

}
