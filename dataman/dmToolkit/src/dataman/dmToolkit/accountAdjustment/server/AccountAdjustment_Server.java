package dataman.dmToolkit.accountAdjustment.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.accountAdjustment.shared.AdjustmentSave_Mdl;
import dataman.dmToolkit.accountAdjustment.shared.GrdAdjustment_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountAdjustment_Server {

    public static DmResult<String> saveRecord(AdjustmentSave_Mdl mrTemp, Connection cnTemp) {
        DmResult<String> dmrTemp = new DmResult<>();
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {

            for (GrdAdjustment_Mdl gaMdl : mrTemp.getLstAdjustments()) {
                if (gaMdl != null) {
                    if (!DmShared.xNull(gaMdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                        if (DmShared.vNull(gaMdl.getAdjustment()) != 0) {
                            sqlQuery = "INSERT INTO ledgerAdj (vr_DocId, vr_V_SNo, vr_SubCode, vr_V_Date, adj_DocId, adj_V_SNo, adj_SubCode, adj_V_Date, adjustment)"
                                    + " VALUES (:vr_DocId, :vr_V_SNo, :vr_SubCode, :vr_V_Date, :adj_DocId, :adj_V_SNo, :adj_SubCode, :adj_V_Date, :adjustment)";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("vr_DocId", DmShared.foriegnKey(mrTemp.getVr_DocId()));
                            psTemp.setString("vr_V_SNo", DmShared.foriegnKey(mrTemp.getVr_V_SNo()));
                            psTemp.setString("vr_SubCode", DmShared.foriegnKey(mrTemp.getVr_SubCode()));
                            psTemp.setDate("vr_V_Date", DmServer.convertDate(mrTemp.getVr_V_Date()));
                            psTemp.setString("adj_DocId", DmShared.foriegnKey(gaMdl.getSharedKeyFieldCode()));
                            psTemp.setString("adj_V_SNo", DmShared.foriegnKey(gaMdl.getV_SNo()));
                            psTemp.setString("adj_SubCode", DmShared.foriegnKey(gaMdl.getAcCode()));
                            psTemp.setDate("adj_V_Date", DmServer.convertDate(gaMdl.getV_Date()));
                            if (DmShared.xNull(gaMdl.getAdjustmentDrCr()).trim().equalsIgnoreCase("Cr")) {
                                psTemp.setString("adjustment", DmServer.vNull(-1 * DmShared.vNull(gaMdl.getAdjustment())));
                            } else {
                                psTemp.setString("adjustment", DmServer.vNull(gaMdl.getAdjustment()));
                            }
                            psTemp.executeUpdate();
                        }
                    }
                }
            }

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            System.out.println("AccountAdjustment_Server -> saveRecord(): Exception");
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    public static List<GrdAdjustment_Mdl> getAdjustments(String vr_DocId, String vr_V_SNo, Connection cnTemp) {
        List<GrdAdjustment_Mdl> lstTemp = new ArrayList<GrdAdjustment_Mdl>();
        GrdAdjustment_Mdl lstNode;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "SELECT"
                    + "   l.docId"
                    + " , l.recId"
                    + " , l.v_SNo"
                    + " , l.v_No"
                    + " , l.v_Date"
                    + " , l.refNo"
                    + " , l.refDate"
                    + " , l.narration"
                    + " , ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0) AS amount"
                    + " , l.v_Type AS v_TypeCode"
                    + " , vt.short_Name AS v_TypeName"
                    + " , l.subCode AS acCode"
                    + " , sg.name AS acName"
                    + " , sg.manualCode AS acManualCode"
                    + " , sg.cityCode"
                    + " , ct.cityName"
                    + " , vLa.adjusted"
                    + " , la.adjustment"
                    + " FROM ledgerAdj la"
                    + " LEFT JOIN ledger l ON l.docId = la.adj_DocId AND l.v_SNo = la.adj_V_SNo and l.subcode = la.adj_SubCode"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = l.v_Type"
                    + " LEFT JOIN subGroup sg ON sg.subcode = l.subCode"
                    + " LEFT JOIN city ct ON ct.cityCode = sg.cityCode"
                    + " LEFT JOIN ("
                    + "     SELECT la.adj_DocId, la.adj_V_SNo, SUM(la.adjustment) AS adjusted"
                    + "     FROM ledgerAdj la"
                    + "     WHERE NOT (la.vr_docId = :vr_docId AND la.vr_V_SNo = :vr_V_SNo)"
                    + "     GROUP By la.adj_DocId, la.adj_V_SNo"
                    + " ) vLa ON vLa.adj_DocId = l.docId AND vLa.adj_V_SNo = l.v_SNo"
                    + " WHERE la.vr_docId = :vr_docId AND la.vr_V_SNo = :vr_V_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vr_docId", DmShared.xNull(vr_DocId));
            psTemp.setString("vr_V_SNo", DmShared.xNull(vr_V_SNo));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new GrdAdjustment_Mdl();
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                lstNode.setV_No(DmShared.xNull(rsTemp.getString("v_No")));
                lstNode.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                lstNode.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                lstNode.setV_TypeCode(DmShared.xNull(rsTemp.getString("v_TypeCode")));
                lstNode.setV_TypeName(DmShared.xNull(rsTemp.getString("v_TypeName")));
                lstNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                lstNode.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                lstNode.setAcCode(DmShared.xNull(rsTemp.getString("acCode")));
                lstNode.setAcName(DmShared.xNull(rsTemp.getString("acName")));
                lstNode.setAcManualCode(DmShared.xNull(rsTemp.getString("acManualCode")));
                lstNode.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                lstNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                lstNode.setNarration(DmShared.xNull(rsTemp.getString("narration")));

                lstNode.setAmount(DmServer.vNull(Math.abs(DmShared.vNull(rsTemp.getString("amount"))), "0.00"));
                if (DmShared.vNull(rsTemp.getString("amount")) < 0) {
                    lstNode.setAmountDrCr("Cr");
                } else {
                    lstNode.setAmountDrCr("Dr");
                }

                lstNode.setAdjusted(DmServer.vNull(Math.abs(DmShared.vNull(rsTemp.getString("adjusted"))), "0.00"));
                if (DmShared.vNull(rsTemp.getString("adjusted")) < 0) {
                    lstNode.setAdjustedDrCr("Cr");
                } else {
                    lstNode.setAdjustedDrCr("Dr");
                }

                lstNode.setAdjustment(DmServer.vNull(Math.abs(DmShared.vNull(rsTemp.getString("adjustment"))), "0.00"));
                if (DmShared.vNull(rsTemp.getString("adjustment")) < 0) {
                    lstNode.setAdjustmentDrCr("Cr");
                } else {
                    lstNode.setAdjustmentDrCr("Dr");
                }

                lstTemp.add(lstNode);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            System.out.println("AccountAdjustment_Server -> getAdjustments(): Exception");
            ex.printStackTrace();
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }

        return lstTemp;
    }

    public static void validateAdjustment(String vr_DocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempInner = null;
        ResultSet rsTemp = null;
        ResultSet rsTempInner = null;
        String sqlQuery = "";

        try {
            sqlQuery = "SELECT"
                    + " COUNT(*) AS cnt"
                    + " FROM ledger l"
                    + " LEFT JOIN ("
                    + "     SELECT la.vr_DocId, la.vr_V_SNo, ISNULL(SUM(la.adjustment), 0) AS totalAdjustment"
                    + "     FROM ledgerAdj la "
                    + "     WHERE la.vr_DocId = :vr_DocId"
                    + "     GROUP BY la.vr_DocId, la.vr_V_SNo"
                    + " ) vLa ON vLa.vr_DocId = l.docId AND vLa.vr_V_SNo = l.v_SNo"
                    + " WHERE l.docId = :vr_DocId"
                    + " AND ABS(ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0)) < ABS(vLa.totalAdjustment)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vr_DocId", DmShared.xNull(vr_DocId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0000, "You cannot adjust excess amount.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT la.adj_DocId, la.adj_V_SNo"
                    + " FROM ledgerAdj la"
                    + " WHERE la.vr_DocId = :vr_DocId"
                    + " GROUP BY la.adj_DocId, la.adj_V_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vr_DocId", DmShared.xNull(vr_DocId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                sqlQuery = "SELECT"
                        + "     CASE "
                        + "     WHEN "
                        + "		    ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0) > 0 "
                        + "		AND (ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0)) - ISNULL(vLa.totalAdjustment, 0) < 0"
                        + "     THEN 1"
                        + "     WHEN "
                        + "		    ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0) < 0 "
                        + "		AND (ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0)) + ISNULL(vLa.totalAdjustment, 0) > 0"
                        + "     THEN 1"
                        + "     ELSE 0"
                        + "     END AS error"
                        + " FROM ledger l"
                        + " LEFT JOIN ("
                        + "     SELECT la.adj_DocId, la.adj_V_SNo, SUM(la.adjustment) AS totalAdjustment "
                        + "     FROM ledgerAdj la"
                        + "     WHERE la.adj_DocId = :adj_DocId AND la.adj_V_SNo = :adj_V_SNo"
                        + "     GROUP BY la.adj_DocId, la.adj_V_SNo"
                        + " ) vLa ON vLa.adj_DocId = l.docId AND vLa.adj_V_SNo = l.v_SNo"
                        + " WHERE l.docId = :adj_DocId AND l.v_SNo = :adj_V_SNo";
                psTempInner = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempInner.setString("adj_DocId", DmShared.xNull(rsTemp.getString("adj_DocId")));
                psTempInner.setString("adj_V_SNo", DmShared.xNull(rsTemp.getString("adj_V_SNo")));
                rsTempInner = psTempInner.executeQuery();
                if (rsTempInner.next()) {
                    if (rsTempInner.getInt("error") == 1) {
                        throw new DmException(0000, "You cannot adjust excess amount.");
                    }
                }
                rsTempInner.close();
                psTempInner.close();
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            System.out.println("AccountAdjustment_Server -> validateAdjustment(): Exception");
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (rsTempInner != null) {
                    rsTempInner.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTempInner != null) {
                    psTempInner.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public static DmResult<String> deleteRecord(String vrDocId, Connection cnTemp) {
        DmResult<String> dmrTemp = new DmResult<>();
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "DELETE FROM ledgerAdj WHERE vr_DocId = :vr_DocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("vr_DocId", DmShared.iNull(vrDocId).longValue());
            psTemp.executeUpdate();

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            System.out.println("AccountAdjustment_Server -> deleteRecord(): Exception");
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    public static DmResult<String> deleteAdjustment(String vrDocId, String vrVSNo, String adjDocId, String adjVSNo, Connection cnTemp) {
        DmResult<String> dmrTemp = new DmResult<>();
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "";

        try {
            if (DmShared.xNull(vrDocId).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Voucher ID cannot be blank!");
            }
            if (DmShared.xNull(adjDocId).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Adjustment ID cannot be blank!");
            }

            if (!DmShared.xNull(vrVSNo).trim().equalsIgnoreCase("")) {
                condition += "AND vr_V_SNo = :vr_V_SNo\n";
            }

            if (!DmShared.xNull(adjVSNo).trim().equalsIgnoreCase("")) {
                condition += "AND adj_V_SNo = :adj_V_SNo\n";
            }

            sqlQuery = "DELETE FROM ledgerAdj \n"
                    + "WHERE vr_DocId = :vr_DocId\n"
                    + "AND adj_DocId = :adj_DocId\n" + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("vr_DocId", DmShared.iNull(vrDocId).longValue());
            psTemp.setLong("adj_DocId", DmShared.iNull(adjDocId).longValue());
            if (!DmShared.xNull(vrVSNo).trim().equalsIgnoreCase("")) {
                psTemp.setInt("vr_V_SNo", DmShared.iNull(vrVSNo).intValue());
            }
            if (!DmShared.xNull(adjVSNo).trim().equalsIgnoreCase("")) {
                psTemp.setInt("adj_V_SNo", DmShared.iNull(adjVSNo).intValue());
            }
            psTemp.executeUpdate();

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            System.out.println("AccountAdjustment_Server -> deleteAdjustment(): Exception");
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

}
