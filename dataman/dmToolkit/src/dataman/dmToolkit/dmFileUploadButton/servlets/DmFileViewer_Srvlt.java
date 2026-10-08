package dataman.dmToolkit.dmFileUploadButton.servlets;

import dataman.dmBase.dmSession.server.DmServletPrivate;
import dataman.dmToolkit.self.server.DmImageResolver;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmFileViewer_Srvlt extends DmServletPrivate {

    public static final String SERVLET_PATH = "/" + DmFileViewer_Srvlt.class.getCanonicalName().replace(".", "/");

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String sessionId = "", fileName = "", directoryPath = "", width = "", height = "", mongoFileLink = "", fileRuleCode = "";
            sessionId = req.getParameter("u");
            directoryPath = req.getParameter("p");
            fileName = req.getParameter("f");
            fileRuleCode = req.getParameter("r");
            mongoFileLink = req.getParameter("m");
            width = req.getParameter("w");
            height = req.getParameter("h");

            DmImageResolver resolver = new DmImageResolver();
            resolver.setSessionId(sessionId);
            resolver.setDirectoryPath(directoryPath);
            resolver.setFileName(fileName);
            resolver.setRuleCode(fileRuleCode);
            resolver.setMongoFileLink(mongoFileLink);

            resolver.processImageForServlet(Integer.parseInt(width), Integer.parseInt(height), req, resp);


        } catch (Exception ex) {
//            ex.printStackTrace();
        } finally {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
