package dataman.dmBase.dmSession.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import dataman.dmBase.dmBaseStatic.client.DmBaseClient;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.SetWidgetInvokerExtended;

public class DmRequestBuilder extends RpcRequestBuilder {

    private static final String DATAMAN_SESSION_NAME = "DTMN_SESSIONID";
    private static String DATAMAN_SESSION_VALUE = "BLANK";

    private static final String DATAMAN_SERVICE_CALLED = "DTMN_SERVICE";
    private static String DATAMAN_SERVICE_CALLED_VALUE = "TRUE";

    private static final String DATAMAN_SESSION_EXPIRED = "DTMN_SESSION_EXPIRED";
    private static String DATAMAN_SESSION_EXPIRED_VALUE = "FALSE";

    private static final String DATAMAN_SESSION_LOCK = "DTMN_SESSION_LOCK";
    private static String DATAMAN_SESSION_LOCK_VALUE = "FALSE";

    private Image img = null;
//    private FlowPanel flwTemp;
    private boolean blnCallBackWait = true;
    private PopupPanel ppCallBackWait = null;

    private static boolean serviceCalling = false;

    public DmRequestBuilder() {
    }

    public DmRequestBuilder(boolean blnCallBackWait) {
        this.blnCallBackWait = blnCallBackWait;
    }

    @Override
    protected RequestBuilder doCreate(String serviceEntryPoint) {
        if (this.blnCallBackWait) {
            if (ppCallBackWait == null) {
                serviceCalling = true;
                ppCallBackWait = new PopupPanel(false, true);
                ppCallBackWait.setGlassStyleName("dm-PopupPanelGlass");
                ppCallBackWait.getElement().setAttribute("style", "border-radius: 50px 50px 50px 50px;background:none;border:none;rgb(59, 153, 217);padding:0px;");
                ppCallBackWait.add(DmBaseClient.createProgressBar());
                ppCallBackWait.setGlassEnabled(true);
                ppCallBackWait.show();
                ppCallBackWait.center();
            }
        }

        RequestBuilder builder = super.doCreate(serviceEntryPoint);
        return builder;
    }

    @Override
    protected void doFinish(RequestBuilder rb) {
        rb.setHeader(STRONG_NAME_HEADER, GWT.getPermutationStrongName());
        rb.setHeader(MODULE_BASE_HEADER, GWT.getModuleBaseURL());
        rb.setHeader(DATAMAN_SERVICE_CALLED, DATAMAN_SERVICE_CALLED_VALUE);
        rb.setHeader(DATAMAN_SESSION_NAME, DATAMAN_SESSION_VALUE);
    }

    @Override
    protected void doSetCallback(RequestBuilder rb, final RequestCallback callback) {

        super.doSetCallback(rb, new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                try {
                    serviceCalling = false;
                    boolean isServiceCalled = false;
                    boolean sessionExpired = false;
                    boolean sessionLoggedOff = false;
                    String tmpvar;

                    tmpvar = response.getHeader(DATAMAN_SESSION_EXPIRED);
                    if (tmpvar != null) {
                        if (tmpvar.equalsIgnoreCase("true") || tmpvar.equalsIgnoreCase("false")) {
                            DATAMAN_SESSION_EXPIRED_VALUE = tmpvar.toUpperCase();
                            if (DATAMAN_SESSION_EXPIRED_VALUE.equalsIgnoreCase("true")) {
                                sessionExpired = true;
                                DmBaseClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(DmEnum.PCSEnum.Login);
                                flushSessionId();
                            }
                        }
                    } else {
                        tmpvar = response.getHeader(DATAMAN_SESSION_LOCK);
                        if (tmpvar != null) {
                            if (tmpvar.equalsIgnoreCase("true") || tmpvar.equalsIgnoreCase("false")) {
                                DATAMAN_SESSION_LOCK_VALUE = tmpvar.toUpperCase();
                                if (DATAMAN_SESSION_LOCK_VALUE.equalsIgnoreCase("true")) {
                                    sessionLoggedOff = true;
                                    DmBaseClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(DmEnum.PCSEnum.LOCK);
                                }
                            }
                        }
                    }

                    if (!sessionExpired && !sessionLoggedOff) {
                        if (response.getHeader(DATAMAN_SERVICE_CALLED) != null) {
                            isServiceCalled = Boolean.valueOf(response.getHeader(DATAMAN_SERVICE_CALLED));
                        }

                        if (isServiceCalled == true) {
                            tmpvar = response.getHeader(DATAMAN_SESSION_NAME);

                            if (DATAMAN_SESSION_VALUE.equalsIgnoreCase("blank") == true) {
                                DATAMAN_SESSION_VALUE = tmpvar;
                            }
                        }
                        callback.onResponseReceived(request, response);
                    }
                    closeWait();

                } catch (Exception e) {
                    e.printStackTrace();
                    Window.alert(e.getMessage());
                } finally {
                    closeWait();
                }
            }

            @Override
            public void onError(Request request, Throwable exception) {
                try {
                    serviceCalling = false;
                    closeWait();
                    callback.onError(request, exception);
                } catch (Exception e) {
                    Window.alert(e.getMessage());
                } finally {
                    closeWait();
                }
            }
        });
    }

    public static boolean isServiceCalling() {
        return serviceCalling;
    }

    private void closeWait() {
        if (this.blnCallBackWait) {
            if (ppCallBackWait != null) {
                ppCallBackWait.hide(true);
                ppCallBackWait = null;
            }
        }
    }

    public static String getSessionId() {
        return DATAMAN_SESSION_VALUE;
    }

    public static void flushSessionId() {
        DATAMAN_SESSION_VALUE = "BLANK";
        DATAMAN_SESSION_EXPIRED_VALUE = "false";
    }

}
