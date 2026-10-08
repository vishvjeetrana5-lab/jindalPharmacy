package dataman.dmPlugin.querySession;

import dataman.dmPlugin.server.NanoHTTPD;
import dataman.dmPlugin.server.router.RouterNanoHTTPD;
import java.util.Map;

public class QueryToClient extends RouterNanoHTTPD.DefaultHandler {

    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.Response response=NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), getText());
        response.addHeader("Access-Control-Allow-Origin","*");
        return response;
    }

    @Override
    public String getText() {        
        return "withoutTCP";
    }

    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.OK;
    }

    @Override
    public String getMimeType() {
        return "text/html";
    }
}
