package dataman.dmToolkit.self.server;

import dataman.dmBase.self.server.DmFontResourceProvider_Srvlt;
import dataman.dmToolkit.accountAdjustment.client.AccountAdjustment;
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_SrvImpl;
import dataman.dmToolkit.dmSnapshot.client.DmSnapshot;
import dataman.dmToolkit.dmSnapshot.server.DmSnapshot_SrvImpl;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_SrvImpl;
import dataman.dmToolkit.dmFileServlets.DmFileDownload_Srvlt;
import dataman.dmToolkit.dmFileServlets.DmFileUpload_Srvlt;
import dataman.dmToolkit.dmFileServlets.DmImageFileThumbnail_Srvlt;
import dataman.dmToolkit.dmFileUploadButton.servlets.DmFileDownloader_Srvlt;
import dataman.dmToolkit.dmFileUploadButton.servlets.DmFileUploadButton_Srvlt;
import dataman.dmToolkit.dmFileUploadButton.servlets.DmFileViewer_Srvlt;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.server.DmGUI_SrvImpl;
import dataman.dmToolkit.dmImageCanvas.client.DmImageCanvas;
import dataman.dmToolkit.dmImageCanvas.server.DmImageCanvas_SrvImpl;
import dataman.dmToolkit.dmNotifications.client.DmNotificationReceiver;
import dataman.dmToolkit.dmNotifications.server.DmNotificationSender_SrvImpl;
import dataman.dmToolkit.dmReportLayout.server.DmReportLayout_Srvlt;
import dataman.dmToolkit.dmReportLayout.shared.DmReportLayout_SrvAsync;
import dataman.dmToolkit.eGstPopup.client.EGstPopup;
import dataman.dmToolkit.eGstPopup.server.EGstPopup_SrvImpl;
import dataman.dmToolkit.itemAdjustment.client.ItemAdjustment;
import dataman.dmToolkit.itemAdjustment.server.ItemAdjustment_SrvImpl;
import dataman.dmToolkit.ledgerPostingDisplayPopup.client.LedgerPostingDisplayPopup;
import dataman.dmToolkit.ledgerPostingDisplayPopup.server.LedgerPostingDisplayPopup_SrvImpl;
import dataman.dmToolkit.manageSite.client.ManageSite;
import dataman.dmToolkit.manageSite.server.ManageSite_SrvImpl;
import dataman.dmToolkit.modifyReason.client.ModifyReason;
import dataman.dmToolkit.modifyReason.server.ModifyReason_SrvImpl;
import dataman.dmToolkit.narrationSearch.client.NarrationSearch;
import dataman.dmToolkit.narrationSearch.server.NarrationSearch_SrvImpl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.openSourceReport.server.OpenSourceReport_SrvImpl;
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.server.OverHead_SrvImpl;
import dataman.dmToolkit.overHeadLine.client.OverHeadLineHandler;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_SrvImpl;
import dataman.dmToolkit.printerMapping.client.PrinterMapping;
import dataman.dmToolkit.printerMapping.server.PrinterMapping_SrvImpl;
import dataman.dmToolkit.singleLineMaster.client.SingleLineMaster;
import dataman.dmToolkit.singleLineMaster.server.SingleLineMaster_SrvImpl;
import dataman.dmToolkit.tdsCategoryPopup.client.TdsCategoryPopup;
import dataman.dmToolkit.tdsCategoryPopup.server.TdsCategoryPopup_SrvImpl;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import dataman.dmToolkit.walkThrough.server.WalkThrough_SrvImpl;
import dataman.dmToolkit.webService.server.UploadFile_Srvlt;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class DmToolkit_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {
            strPath = SingleLineMaster.class.getName().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SingleLineMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OverHead.class.getName().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OverHead_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OverHeadLineHandler.class.getName().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OverHeadLine_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DmFileManager.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DmFileManager_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DmGUI.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DmGUI_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ManageSite.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ManageSite_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DmNotificationReceiver.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DmNotificationSender_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/dmFileUploadButton/servlets/DmFileUploadButton_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmFileUploadButton_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = DmReportLayout_SrvAsync.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DmReportLayout_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/dmFileServlets/DmFileUpload_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmFileUpload_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/dmFileServlets/DmFileDownload_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmFileDownload_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/dmFileServlets/DmImageFileThumbnail_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmImageFileThumbnail_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/self/server/DmPlugin_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmPlugin_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/dmFileUploadButton/servlets/DmFileViewer_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmFileViewer_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmFileDownloader_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmBase/self/server/DmFontResourceProvider_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmFontResourceProvider_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = DmSnapshot.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DmSnapshot_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DmImageCanvas.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DmImageCanvas_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WalkThrough.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WalkThrough_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OpenSourceReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OpenSourceReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PrinterMapping.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PrinterMapping_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/self/server/DmPACSetup_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmPACSetup_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/self/server/DmPACUpdateSetup_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, DmPACUpdateSetup_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = AccountAdjustment.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AccountAdjustment_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemAdjustment.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemAdjustment_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = "dataman/dmToolkit/webService/server/UploadFile_Srvlt";
            dyn = sce.getServletContext().addServlet(strPath, UploadFile_Srvlt.class);
            dyn.addMapping("/" + strPath);

            strPath = TdsCategoryPopup.class.getName().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TdsCategoryPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ModifyReason.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ModifyReason_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EGstPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EGstPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = NarrationSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, NarrationSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            
            strPath = LedgerPostingDisplayPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LedgerPostingDisplayPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
