package aarogya.pharmacy.self.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.sql.Connection;
import java.sql.ResultSet;

public class MedicineBatch_Server {

//    public static void manageBatchDetail(String docId, boolean isDelete, Connection cnTemp) throws DmException {
//        String sqlQuery, mbCode = "", medicineCode = "", site_Code = "", departmentCode = "";
//        double currentStock, stock, balance;
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//
//        try {
//            sqlQuery = "select	mbd.mbCode,mbd.medicineCode,mbd.site_Code,mbd.departmentCode, "
//                    + "mbd.currentStock,ms.stock,"
//                    + "ms.mbCode as msMBCode,ms.medicineCode as msMedicineCode,ms.site_Code as msSite_Code,ms.departmentCode as msDepartmentCode "
//                    + "from medicineBatchDetail mbd "
//                    + "right join ( "
//                    + "select  ms.medicineCode,ms.godownSubCode,ms.site_Code,ms.mbCode,isNull(sum(ms.recQty),0) - isNull(sum(ms.isuQty),0) as stock  "
//                    + "from medicineStock ms  "
//                    + "where ms.docId = :docId "
//                    + "group by ms.medicineCode,ms.godownSubCode,ms.site_Code,ms.mbCode "
//                    + ") ms on	ms.medicineCode = mbd.medicineCode  "
//                    + "and ms.godownSubCode = mbd.departmentCode "
//                    + "and ms.site_Code = mbd.site_Code "
//                    + "and ms.mbCode = mbd.mbCode ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            psTemp.setString("docId", DmShared.iNull(docId).toString());
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                currentStock = DmShared.vNull(rsTemp.getString("currentStock"));
//                stock = DmShared.vNull(rsTemp.getString("stock"));
//                mbCode = DmShared.iNull(rsTemp.getString("msMBCode")).toString();
//                medicineCode = DmShared.iNull(rsTemp.getString("msMedicineCode")).toString();
//                site_Code = DmShared.iNull(rsTemp.getString("msSite_Code")).toString();
//                departmentCode = DmShared.iNull(rsTemp.getString("msDepartmentCode")).toString();
//
//                if (isDelete) {
//                    balance = (currentStock) - (stock);
//                } else {
//                    balance = (currentStock) + (stock);
//                }
//
//                if (DmShared.iNull("mbCode").longValue() == 0) {
//                    rsTemp.moveToInsertRow();
//                    rsTemp.updateString("mbCode", mbCode);
//                    rsTemp.updateString("medicineCode", medicineCode);
//                    rsTemp.updateString("site_Code", site_Code);
//                    rsTemp.updateString("departmentCode", departmentCode);
//                    rsTemp.updateString("currentStock", DmServer.vNull(balance, "0.0000000"));
//                    rsTemp.insertRow();
//                } else {
//                    rsTemp.updateString("currentStock", DmServer.vNull(balance, "0.0000000"));
//                    rsTemp.updateRow();
//                }
//            }
//            rsTemp.close();
//            psTemp.close();
//
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//    }
    public static void manageBatchDetail(String docId, boolean isDelete, Connection cnTemp) throws DmException {
        String sqlQuery, mbCode = "", medicineCode = "", site_Code = "", departmentCode = "";
        double currentStock, stock, balance;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;

        try {
            sqlQuery = "select	mbd.ibCode, mbd.itemCode, mbd.site_Code, mbd.departmentCode, "
                    + "         mbd.currentStock, ms.stock, ms.ibCode as msMBCode, ms.itemCode as msMedicineCode, "
                    + "         ms.site_Code as msSite_Code, ms.departmentCode as msDepartmentCode "
                    + "         from itemBatchDetail mbd "
                    + "         right join ( "
                    + "                 select  ms.itemCode, ms.departmentCode, ms.site_Code, ms.ibCode, isNull(sum(ms.recQty),0) - isNull(sum(ms.isuQty),0) as stock  "
                    + "                 from stock ms  "
                    + "                 where ms.docId = :docId "
                    + "                 group by ms.itemCode, ms.departmentCode, ms.site_Code, ms.ibCode "
                    + "         ) ms on	ms.itemCode = mbd.itemCode and ms.departmentCode = mbd.departmentCode "
                    + "         and ms.site_Code = mbd.site_Code and ms.ibCode = mbd.ibCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                currentStock = DmShared.vNull(rsTemp.getString("currentStock"));
                stock = DmShared.vNull(rsTemp.getString("stock"));
                mbCode = DmShared.iNull(rsTemp.getString("msMBCode")).toString();
                medicineCode = DmShared.iNull(rsTemp.getString("msMedicineCode")).toString();
                site_Code = DmShared.iNull(rsTemp.getString("msSite_Code")).toString();
                departmentCode = DmShared.iNull(rsTemp.getString("msDepartmentCode")).toString();

                if (isDelete) {
                    balance = (currentStock) - (stock);
                } else {
                    balance = (currentStock) + (stock);
                }

                if (DmShared.iNull(rsTemp.getString("ibCode")).longValue() == 0) {
                    sqlQuery = "insert into itemBatchDetail(ibCode, itemCode, departmentCode, site_Code, currentStock) "
                            + " values(:ibCode, :itemCode, :departmentCode, :site_Code, :currentStock) ";

                } else {
                    sqlQuery = "update itemBatchDetail set "
                            + " currentStock = :currentStock "
                            + " where ibCode = :ibCode and "
                            + " itemCode = :itemCode and "
                            + " departmentCode = :departmentCode and "
                            + " site_Code = :site_Code ";
                }
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("ibCode", DmShared.iNull(mbCode).toString());
                psTemp1.setString("itemCode", DmShared.iNull(medicineCode).toString());
                psTemp1.setString("departmentCode", DmShared.iNull(departmentCode).toString());
                psTemp1.setString("site_Code", DmShared.iNull(site_Code).toString());
                psTemp1.setString("currentStock", DmServer.vNull(balance, "0.0000000"));
                psTemp1.executeUpdate();
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
            } catch (Exception e) {
            }
        }
    }

}
