package aarogya.login.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmBaseURL;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import java.util.HashMap;

abstract class Login_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTextBox txtUserName = new DmTextBox();
    final PasswordTextBox txtpass = new PasswordTextBox();
    final FlowPanel flwForm = new FlowPanel();
    final Button btnPerevious = new Button("<< Previous");
    final Button btnNext = new Button("Next");
    final DmLabel lblForgetPassword = new DmLabel("Forget Password?");
//    final DmLabel repairCompany = new DmLabel("Repair Company");

    public abstract void onBtnClick(ClickEvent event);

    public Login_Dsg() {
        DmBaseURL pageUrl = DmClient.getHostPageBaseURL();

        HashMap<String, String> params = new HashMap<>();
        params.put("type", "regionalCss");
        params.put("u", pageUrl.getProtocol() + "://" + pageUrl.getUrl());
        String urlString = DmClient.concatURL("dataman/dmToolkit/self/server/DmFontResourceProvider_Srvlt", params);
        DmClient.loadCss(urlString);

        ini();
        fHandlerBtnClick(new Button[]{btnPerevious, btnNext});
    }

    private void fHandlerBtnClick(Button[] btn) {
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
        flwForm.setStyleName("dm-FlwForm-Login " + "dm-Login-container");
        flwForm.setWidth("818px");
        flwForm.add(createBannerDesign());
        flwForm.add(createLoginDesign());
        flwForm.getElement().setId("divlogin");
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
        //flwTemp.setHeight("100%");
        flwTemp.add(createUserheader());
        flwTemp.add(createLoginContent());
        flwTemp.add(createNextPreviousButton());
        flwTemp.add(createForgetPassword());

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
        //tmp.setHeight("255px");
        tmp.setWidth("100%");
        tmp.add(createLoginInput());
        return tmp;
    }

    public FlowPanel createLoginInput() {
        FlowPanel vTmp = new FlowPanel();

        vTmp.setStyleName("dm-input-controls " + "dm-padding30px");
        //vTmp.setWidth("300px");
        txtUserName.setStyleName("dm-Login-TextBox");
        txtUserName.setWidth("100%");
        txtUserName.getElement().setPropertyString("placeholder", "User Name");

        txtpass.setStyleName("dm-Login-TextBox");
        txtpass.setWidth("100%");
        txtpass.getElement().setPropertyString("placeholder", "Password");

        vTmp.add(vl.add(txtUserName));
        vTmp.add(vl.add(txtpass));

//        vTmp.add(createTemp());
        return vTmp;
    }

    public FlowPanel createNextPreviousButton() {

        FlowPanel tmp = new FlowPanel();
        //tmp.setHeight("60px");
        tmp.setWidth("100%");
        tmp.setStyleName("dm-FlwLeft");
        //FlowPanel flwLine = new FlowPanel();
        //flwLine.setStyleName("dm-Line-Horizontal");
        //tmp.add(flwLine);

        //  FlowPanel flwblank;
        // flwblank = new FlowPanel();
        // flwblank.setStyleName("dm-FlwLeft");
        // flwblank.setHeight("5px");
        //flwblank.setWidth("100%");
        //tmp.add(flwblank);
        FlowPanel divcontent = new FlowPanel();
        //btnPerevious.setEnabled(false);
        //btnPerevious.setHeight("30px");
        //btnPerevious.setWidth("48%");

        btnNext.setStyleName("dm-Login-Button " + "dm-next-button-style next-color");
        //btnNext.getElement().getStyle().setBackgroundImage("url('images/bgBlue.png')");
        btnNext.setHeight("30px");
        //btnNext.setWidth("48%");

        //btnPerevious.setStyleName("dm-Login-Button-Disable");
        //btnPerevious.getElement().getStyle().setBackgroundImage("url('images/bgGray.png')");
        //divcontent.add(flwblank);
        //divcontent.add(btnPerevious);
        divcontent.add(vl.add(btnNext));

        tmp.add(divcontent);

        //FlowPanel flwTemp = new FlowPanel();
        //flwTemp.setWidth("100%");
        //flwTemp.setHeight("20px");
        //repairCompany.getElement().setAttribute("style", repairCompany.getElement().getAttribute("style") + ";float:right;margin-right:6px;margin-top:2px;color:blue;cursor:pointer;");
        // flwTemp.add(repairCompany);
        // tmp.add(flwTemp);
        return tmp;

    }
    public FlowPanel createForgetPassword() {
        FlowPanel tmp = new FlowPanel();
        tmp.setWidth("100%");
        tmp.setStyleName("dm-FlwLeft");

        FlowPanel divcontent = new FlowPanel();
        lblForgetPassword.setStyleName("dmHyperLink");
        lblForgetPassword.getElement().setAttribute("style", lblForgetPassword.getElement().getAttribute("style")
                + "; margin-left: 80px; margin-top: 10px; color: #777777");
        lblForgetPassword.setWidth("40%");
        lblForgetPassword.setHeight("20px");
        divcontent.add(lblForgetPassword);
        tmp.add(divcontent);

        return tmp;
    }
//    static int s = 2;
//    private FlowPanel createTemp () {
//        FlowPanel flw = new FlowPanel();
//        final DmCheckBox cb = new DmCheckBox();
//        Button enable = new Button("enable");
//        Button disable = new Button("disable");
//        Button foc = new Button("focus");
//        Button sizeB = new Button("focus");
//        
//        
//        flw.add(cb);
//        flw.add(enable);
//        flw.add(disable);
//        flw.add(foc);
//        flw.add(sizeB);
//        
//        enable.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                cb.setEnabled(true);
//            }
//        });
//        
//        disable.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                cb.setEnabled(false);
//            }
//        });
//        
//        foc.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                cb.setFocus(true);
//            }
//        });
//        
//         sizeB.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                cb.setSize(s);
//                s++;
//            }
//        });
//        
//        return flw;
//    }
}
