package dataman.dmBase.dmSession.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmRequestFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        DmResponseWrapper mresp = null;
        DmRequestWrapper hreq = null;
        boolean isGwtRequestCall = false;
        try {
            hreq = new DmRequestWrapper((HttpServletRequest) req);
            isGwtRequestCall = hreq.getIsGwtServiceCall();

            HttpServletResponse hres = null;

            System.out.println("Incoming Request found for : " + hreq.getRequestURL().toString());

            String pathExtension = "";
            if (hreq.getRequestURI().lastIndexOf(".") > 0) {
                pathExtension = hreq.getRequestURI().substring(hreq.getRequestURI().lastIndexOf("."));
            }

            if (res instanceof HttpServletResponse) {
                hres = (HttpServletResponse) res;
//                //---------------by pawan
//                hres.addHeader("Access-Control-Allow-Origin", "*");
//                hres.addHeader("Access-Control-Allow-Methods", "POST, GET, UPDATE, OPTIONS");
//                hres.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type, X-Auth-Token,DTMN_SERVICE,DTMN_SESSIONID");
//                hres.addHeader("Acccess-Control-Expose-Headers", "DTMN_SESSION_EXPIRED,DTMN_SERVICE,DTMN_SESSIONID");
//                //---------------by pawan
                mresp = new DmResponseWrapper(hres, pathExtension);

            }

            if (hres != null) {
                if (isGwtRequestCall) {

//                    mresp = new DmResponseWrapper(hres);
                    mresp.addHeader("DTMN_SERVICE", String.valueOf(isGwtRequestCall));

                    if (hreq.getIsNew() == true) {
                        hreq.assignUniqueDatamanSessionId();
                    }
                    mresp.addHeader("DTMN_SESSIONID", hreq.getDatamanSessionId());
                    DmBaseServer.createRepositorySsn(hreq.getDatamanSessionId());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            System.out.println("Request found..");
//            if (mresp != null) {
            if (isGwtRequestCall) {
                fc.doFilter(hreq, mresp);
            } else {
//                fc.doFilter(req, res);
                fc.doFilter(req, mresp);
            }
        }
    }

    @Override
    public void destroy() {
    }
}
