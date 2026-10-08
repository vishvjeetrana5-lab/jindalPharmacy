package aarogya.dashBoard.self.server;

import aarogya.dashBoard.advancesPendingForBillSettlement.client.AdvancesPendingForBillSettlement;
import aarogya.dashBoard.advancesPendingForBillSettlement.server.AdvancesPendingForBillSettlement_SrvImpl;
import aarogya.dashBoard.billDueNotPaid.client.BillDueNotPaid;
import aarogya.dashBoard.billDueNotPaid.server.BillDueNotPaid_SrvImpl;
import aarogya.dashBoard.billPendingForApproval.client.BillPendingForApproval;
import aarogya.dashBoard.billPendingForApproval.server.BillPendingForApproval_SrvImpl;
import aarogya.dashBoard.billPendingForGRN.client.BillPendingForGRN;
import aarogya.dashBoard.billPendingForGRN.server.BillPendingForGRN_SrvImpl;
import aarogya.dashBoard.favorite.client.Favorite;
import aarogya.dashBoard.favorite.server.Favorite_SrvImpl;
import aarogya.dashBoard.dataModificationLog.client.DataModificationLog;
import aarogya.dashBoard.dataModificationLog.server.DataModificationLog_SrvImpl;
import aarogya.dashBoard.fastMovingMedicine.client.FastMovingMedicine;
import aarogya.dashBoard.fastMovingMedicine.server.FastMovingMedicine_SrvImpl;
import aarogya.dashBoard.inventoryIndentPendingForApproval.client.InventoryIndentPendingForApproval;
import aarogya.dashBoard.inventoryIndentPendingForApproval.server.InventoryIndentPendingForApproval_SrvImpl;
import aarogya.dashBoard.inventoryPendingIndent.client.InventoryPendingIndent;
import aarogya.dashBoard.inventoryPendingIndent.server.InventoryPendingIndent_SrvImpl;
import aarogya.dashBoard.inventoryPendingPO.client.InventoryPendingPO;
import aarogya.dashBoard.inventoryPendingPO.server.InventoryPendingPO_SrvImpl;
import aarogya.dashBoard.medicineStockOut.client.MedicineStockOutCard;
import aarogya.dashBoard.medicineStockOut.server.MedicineStockOut_SrvImpl;
import aarogya.dashBoard.nearExpiryMedicine.client.NearExpiryMedicine;
import aarogya.dashBoard.nearExpiryMedicine.server.NearExpiryMedicine_SrvImpl;
import aarogya.dashBoard.nonSoldMedicine.client.NonSoldMedicine;
import aarogya.dashBoard.nonSoldMedicine.server.NonSoldMedicine_SrvImpl;
import aarogya.dashBoard.requisitionInventory.client.RequisitionInventory;
import aarogya.dashBoard.requisitionInventory.server.RequisitionInventory_SrvImpl;
import aarogya.dashBoard.requisitionPharmacy.client.RequisitionPharmacy;
import aarogya.dashBoard.requisitionPharmacy.server.RequisitionPharmacy_SrvImpl;
import aarogya.dashBoard.totalCashCollection.client.TotalCashCollection;
import aarogya.dashBoard.totalCashCollection.server.TotalCashCollection_SrvImpl;
import aarogya.dashBoard.totalRefundForDate.client.TotalRefundForDate;
import aarogya.dashBoard.totalRefundForDate.server.TotalRefundForDate_SrvImpl;
import aarogya.dashBoard.walkThrough.client.WalkThrough;
import aarogya.dashBoard.walkThrough.server.WalkThrough_SrvImpl;
import aarogya.dashBoard.whCollectionDashBoard.client.WHCollectionDashBoard;
import aarogya.dashBoard.whCollectionDashBoard.server.WHCollectionDashBoard_SrvImpl;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class DashBoard_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {

            strPath = Favorite.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Favorite_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RequisitionPharmacy.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RequisitionPharmacy_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RequisitionInventory.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RequisitionInventory_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WalkThrough.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WalkThrough_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DataModificationLog.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DataModificationLog_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = InventoryPendingPO.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, InventoryPendingPO_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = InventoryIndentPendingForApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, InventoryIndentPendingForApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = InventoryPendingIndent.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, InventoryPendingIndent_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = NearExpiryMedicine.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, NearExpiryMedicine_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineStockOutCard.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineStockOut_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = NonSoldMedicine.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, NonSoldMedicine_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = FastMovingMedicine.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, FastMovingMedicine_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            //
            strPath = AdvancesPendingForBillSettlement.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AdvancesPendingForBillSettlement_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BillDueNotPaid.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BillDueNotPaid_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BillPendingForApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BillPendingForApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BillPendingForGRN.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BillPendingForGRN_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TotalCashCollection.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TotalCashCollection_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TotalRefundForDate.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TotalRefundForDate_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = WHCollectionDashBoard.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WHCollectionDashBoard_SrvImpl.class);
            dyn.addMapping("/" + strPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
