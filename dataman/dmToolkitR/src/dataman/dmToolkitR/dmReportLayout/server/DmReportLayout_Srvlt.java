package dataman.dmToolkitR.dmReportLayout.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmSession.server.DmServletPrivate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmReportLayout_Srvlt extends DmServletPrivate {

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletOutputStream out = response.getOutputStream();
        String filePath, fileName;
        FileInputStream fileStream;
        int readLenth;
        byte[] byteArray;

        try {
            fileName = request.getParameter("f").trim();
            filePath = DmBaseServer.Path.sessions + "/" + request.getParameter("u").trim() + "/" + fileName;
            fileStream = new FileInputStream(filePath);

            byteArray = new byte[1024 * 1024];
            readLenth = fileStream.read(byteArray);
            String contentType = this.getServletContext().getMimeType(fileName);
            response.setContentType(contentType + ";name=\"" + fileName + "\"");
            response.setHeader("Accept", contentType);
            response.setHeader("Content-Disposition", "inline;filename=" + "\"" + fileName + "\"; filename*=UTF-8''" + fileName);
            while (readLenth != -1) {
                out.write(byteArray, 0, readLenth);
                readLenth = fileStream.read(byteArray);
            }
            fileStream.close();

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }
}
