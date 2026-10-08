package aarogya.ribbon.server;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmSkypeIni_Srvlt extends HttpServlet {

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
//        String value = "", action = "";

        resp.setContentType("text/html;charset=UTF-8");
        try {
//            action = req.getParameter("a");
//
//            if (DmShared.xNull(action).trim().equalsIgnoreCase(DmEnum.DmPluginQueryActions.PACPortWeb.toString())) {
//                value = DmServer.pacPortWeb;
//            }
//
//            resp.getOutputStream().write(value.getBytes());

//            try (PrintWriter out = resp.getWriter()) {
            PrintWriter out = resp.getWriter();
            /* TODO output your page here. You may use following sample code. */
            String userName = req.getParameter("user");
            if ((userName != null) && (!userName.trim().equals(""))) {
                out.println("<html>");
                out.println("   <head>");
                out.println("       <title>TODO supply a title</title>");
                out.println("       <script src='https://swc.cdn.skype.com/sdk/v1/sdk.min.js'></script>");
                out.println("       <meta http-equiv='Content-Type' content='text/html;charset=utf-8'/>");
                out.println("       <script type='text/javascript'>");
                out.println("           function iniSkype() { ");
                out.println("               SkypeWebControl.SDK.Chat.startChat({ ");
                out.println("                   ConversationId: '" + userName + "', ");
                out.println("                   ConversationType: 'agent' ");
                out.println("               }); ");
//                out.println("           alert('ok'); ");
                out.println("           } ");
                out.println("           window.onload = iniSkype;");
                out.println("       </script>");
//                out.println("<meta charset=\"UTF-8\">");
//                out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                out.println("   </head>");
                out.println("   <body>");
//                out.println("<div class=\"skype-button bubble\" data-contact-id=\"" + userName + "\"></div>");
                out.println("   </body>");
                out.println("</html>");
            }
//        }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
