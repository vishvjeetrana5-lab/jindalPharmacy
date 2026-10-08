package aarogya.selfToolkit.stStatic.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.server.DMJasperVariables;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;

public class STJasperVariables implements DMJasperVariables {

    @Override
    public HashMap setJasperVariables(DmRSS rssTemp, String reportTitle, JasperReport jasperReport, LinkedHashMap<String, FieldUDT_Mdl> filter, HashMap parameter) throws Exception {
        JRParameter jrParameter[];

        try {

            jrParameter = jasperReport.getParameters();
            for (int i = 0; i < jrParameter.length; i++) {
                if (jrParameter[i].getName().equalsIgnoreCase("dmDocumentHeader")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(STServer.getSsnDocumentHeader(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmDocumentFooter")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(STServer.getSsnDocumentFooter(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmDocumentFooterRegional")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(STServer.getSsnDocumentFooterRegional(rssTemp)));
                }
            }

        } finally {
        }
        return parameter;
    }

}
