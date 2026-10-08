package dataman.dmBase.dmSession.server;

import javax.servlet.http.HttpServletResponseWrapper;

public interface IAppServiceResponseHandler {
    public void handleResponse(HttpServletResponseWrapper httpResponse);
}
