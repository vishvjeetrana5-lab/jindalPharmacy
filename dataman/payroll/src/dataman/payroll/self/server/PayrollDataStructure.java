package dataman.payroll.self.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmRefreshDataStructure.server.DmConfigDataStructure;
import dataman.dmBase.dmRefreshDataStructure.server.DmRefreshDataStructure;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;

public final class PayrollDataStructure {

    private final DmRefreshDataStructure refreshDataStructure;

    public PayrollDataStructure(DmRefreshDataStructure refreshDataStructure) throws Exception {
        this.refreshDataStructure = refreshDataStructure;

    }

    private void hl7Mast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("tableName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("shortName", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("priorityIndex", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("colorName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
    }

    private void initialMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    public void initilizer() throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = "", codeValue = "";
        LibraryVoucherCategory lvc = new LibraryVoucherCategory();

        try {
            cnTemp = DmServer.getConnection(refreshDataStructure.getDatabaseName());
            spBegin = new DmSavePoint(cnTemp);

//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.FUND_TRANSFER + "'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
//                        + "VALUES ('FA', '" + lvc.FUND_TRANSFER + "', '" + codeValue + "', 'Fund Transfer', '" + lvc.FUND_TRANSFER + "', 1, 'SA', 'SA', getdate(), 'Fund Transfer Entry','Accounts') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
            spBegin.commit();
        } catch (Exception ex) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception e) {
            }
            refreshDataStructure.addResultLog("initilizer", "", this.getClass().getName(), sqlQuery, ex.getMessage());
            throw new DmException(0, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
            if (cnTemp != null && !cnTemp.isClosed()) {
                cnTemp.close();
            }
        }
    }

}
