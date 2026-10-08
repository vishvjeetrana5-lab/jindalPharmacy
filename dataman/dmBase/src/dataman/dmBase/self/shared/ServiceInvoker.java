package dataman.dmBase.self.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import dataman.dmBase.dmSession.client.DmRequestBuilder;

public class ServiceInvoker<T> {

    private T SrvAsync;
    ServiceDefTarget SdtTemp;

    public ServiceInvoker(Object tSrv, String strModuleName, String strWidgetName) {
        SrvAsync = (T) tSrv;
        SdtTemp = (ServiceDefTarget) SrvAsync;
        SdtTemp.setServiceEntryPoint(GWT.getHostPageBaseURL() + strModuleName.trim() + "/" + strWidgetName.trim());
    }

    public ServiceInvoker(Object tSrv, String url) {
        SrvAsync = (T) tSrv;
        SdtTemp = (ServiceDefTarget) SrvAsync;
        SdtTemp.setServiceEntryPoint(GWT.getHostPageBaseURL() + url.trim().replace(".", "/"));
    }

    public T invokeRPC() {
        return RPC(true);
    }

    public T invokeRPC(boolean blnCallBackWait) {
        return RPC(blnCallBackWait);
    }

    private T RPC(boolean blnCallBackWait) {
        SdtTemp.setRpcRequestBuilder(new DmRequestBuilder(blnCallBackWait));
        return (T) this.SrvAsync;
    }
}
