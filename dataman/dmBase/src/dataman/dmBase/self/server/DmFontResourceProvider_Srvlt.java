package dataman.dmBase.self.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum.RegionalLanguage;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmFontResourceProvider_Srvlt extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String resType = DmBaseShared.xNull(request.getParameter("type"));
            if (resType.equalsIgnoreCase("regionalCss")) {
                String cUrl = DmBaseShared.xNull(request.getParameter("u"));
                String regionalCss = prepareRegionalCss(cUrl).toString();
                response.setContentType("text/css");
                response.getWriter().write(regionalCss);
            } else if (resType.equalsIgnoreCase("font")) {
                String fontName = DmBaseShared.xNull(request.getParameter("value"));
                File fontFile = searchFileFromDirectory(new File(DmBaseServer.Path.fonts), fontName);
                if (fontFile != null) {
                    response.setContentType("application/font-sfnt");
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(fontFile);
                        byte[] BUFFER = new byte[1024 * 1024];
                        int rLen = fis.read(BUFFER);
                        while (rLen != -1) {
                            response.getOutputStream().write(BUFFER, 0, rLen);
                            rLen = fis.read(BUFFER);
                        }
                        BUFFER = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        if (fis != null) {
                            fis.close();
                            fis = null;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private File searchFileFromDirectory(File parentDirectory, String fontName) throws Exception {
        File searchedFile = null;
        try {
            if (parentDirectory.isDirectory()) {
                String[] listFiles = parentDirectory.list();
                for (int i = 0; i < listFiles.length; i++) {
                    File tempFile = new File(parentDirectory, listFiles[i].trim());
                    if (tempFile.isFile()) {
                        if (tempFile.getName().toLowerCase().endsWith(".ttf")) {//.equalsIgnoreCase(fontName + ".ttf")) {
                            try {
                                Font font = Font.createFont(Font.TRUETYPE_FONT, tempFile);
                                if (font.getFamily().equalsIgnoreCase(fontName)) {
                                    searchedFile = tempFile;
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }
                    } else if (tempFile.isDirectory()) {
                        searchedFile = searchFileFromDirectory(tempFile, fontName);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return searchedFile;
    }

    private StringBuffer prepareRegionalCss(String clientUrl) {
        StringBuffer buffer = new StringBuffer();
        try {
            RegionalLanguage[] languages = RegionalLanguage.values();
            for (int i = 0; i < languages.length; i++) {
                RegionalLanguage lang = languages[i];
                String queryParam = "type=font&value=" + lang.getFontName();
                buffer.append("@font-face {\n"
                        + "    font-family: '" + lang.getFontFamilyName() + "';\n"
                        + "    src: url('" + clientUrl + "dataman/dmBase/self/server/DmFontResourceProvider_Srvlt?" + queryParam + "') format('truetype');\n"
                        + "}\n\n");
                buffer.append("." + lang.getCssStyleName() + "{\n"
                        + "    font-family: '" + lang.getFontFamilyName() + "';\n"
                        + "    font-Size: " + lang.getCssFontSize() + ";\n"
                        + "}\n\n");
            }
        } catch (Exception ex) {
            buffer.delete(0, buffer.length());
            ex.printStackTrace();
        }
        return buffer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

}
