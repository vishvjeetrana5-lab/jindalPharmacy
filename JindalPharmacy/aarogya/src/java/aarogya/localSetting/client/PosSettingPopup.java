package aarogya.localSetting.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Event;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class PosSettingPopup extends PosSettingPopup_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void hide(boolean isCancel);
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        btnOk.setFocus(true);
        getPOSSettings();
    }

    private void getPOSSettings() {
        try {

            String url = "http://localhost:1111/getMac?cmd=read";

            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        txtMainBody.setText(DmShared.xNull(response.getText()));
                        txtMachineDetails.setText(DmShared.xNull(response.getText()));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {

                }
            });

            requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded");
            requestBuilder.send();
        } catch (Exception ex) {

        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void hide(boolean isCancel) {
        hide();
        callBack.hide(isCancel);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            hide(true);
        } else if (event.getSource() == btnSaveSetting) {
            savePOSSettings();
        }

    }

    private void savePOSSettings() {

        try {

            String requestBody = DmShared.xNull(txtMainBody.getText());

            String url = "http://localhost:1111/getMac?cmd=write&data=" + requestBody;// Static

            // Create a new RequestBuilder with POST method
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);

            // Set the content type to indicate JSON data
            requestBuilder.setRequestData(requestBody);
//            requestBuilder.setHeader("Accept", "*/*");
//            requestBuilder.setHeader("Content-Type", "application/json");
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        new DmAlertMessageBox("POS Machine mapped successfully.", (DmTextBox) null).show();
                        txtMachineDetails.setText("");
                        txtMachineDetails.setText(DmShared.xNull(response.getText()));
                    } else {
                        System.out.println("Request failed with status code: " + response.getStatusCode());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    System.out.println("Request failed: " + exception.getMessage());
                }
            });
            requestBuilder.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(false);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public PosSettingPopup() {
        super();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnSaveSetting.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        txtExampleBody.setText("{\n"
                + "  \"configList\": [\n"
                + "    {\n"
                + "      \"siteCode\": \"1\",\n"
                + "      \"siteShortName\": \"OP\",\n"
                + "      \"appKey\": \"ddf2aba0-8bb3-47b1-8d92-5ffc9296hd66\",\n"
                + "      \"userName\": \"9878471100\",\n"
                + "      \"deviceId\": \"1492732581|ezetap_android\",\n"
                + "      \"requestURL\": \"https://demo.ezetap.com/api/3.0/p2padapter/pay\",\n"
                + "      \"responseURL\": \"https://demo.ezetap.com/api/3.0/p2padapter/status\",\n"
                + "      \"cancelURL\": \"https://demo.ezetap.com/api/3.0/p2padapter/cancel\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"siteCode\": \"2\",\n"
                + "      \"siteShortName\": \"NC\",\n"
                + "      \"appKey\": \"ddf2aba0-8bb3-475-8d92-5ffc9296hd66\",\n"
                + "      \"userName\": \"9878471104\",\n"
                + "      \"deviceId\": \"1492732551|ezetap_android\",\n"
                + "      \"requestURL\": \"https://demo.ezetap.com/api/3.0/p2padapter/pay\",\n"
                + "      \"responseURL\": \"https://demo.ezetap.com/api/3.0/p2padapter/status\",\n"
                + "      \"cancelURL\": \"https://demo.ezetap.com/api/3.0/p2padapter/cancel\"\n"
                + "    }\n"
                + "  ]\n"
                + "}");
        txtMachineDetails.setEnabled(false);

    }

}
