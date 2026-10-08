package dataman.dmToolkit.self.server;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmPlugin_Srvlt extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        String value = "", action = "";

        try {
            action = req.getParameter("a");

            if (DmShared.xNull(action).trim().equalsIgnoreCase(DmEnum.DmPluginQueryActions.PACPortWeb.toString())) {
                value = DmServer.pacPortWeb;
            }

            resp.getOutputStream().write(value.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
