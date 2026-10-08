package dataman.dmToolkit.ledgerPostingDisplayPopup.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.ledgerPostingDisplayPopup.shared.GrdLedgerDisplayDetail_Mdl;
import dataman.dmToolkit.ledgerPostingDisplayPopup.shared.LedgerPostingDisplayPopup_Srv;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LedgerPostingDisplayPopup_SrvImpl extends DmRSSPrivate implements LedgerPostingDisplayPopup_Srv {

    @Override
    public DmResult<List<GrdLedgerDisplayDetail_Mdl>> getLedgerPosting(String docid) {
        DmResult<List<GrdLedgerDisplayDetail_Mdl>> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdLedgerDisplayDetail_Mdl> mrTemp = new ArrayList();
        String strSQLQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            //========== Head Query Date Wise Grouping ====================================
            strSQLQuery = "SELECT LG.Amtcr,LG.AmtDr, "
                    + "LG.Docid,SG.Name AS NAME "
                    + "FROM Ledger LG LEFT  JOIN  LedgerM ON LG.DocId = LedgerM.DocId  "
                    + "LEFT JOIN Subgroup SG ON SG.Subcode=LG.Subcode  "
                    + "WHERE LG.Docid = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setString("docId", DmShared.xNull(docid));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                GrdLedgerDisplayDetail_Mdl grdLedgerDisplayDetail_Mdl = new GrdLedgerDisplayDetail_Mdl();
                grdLedgerDisplayDetail_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                grdLedgerDisplayDetail_Mdl.setSharedKeyFieldName(rsTemp.getString("NAME"));
//                grdLedgerDisplayDetail_Mdl.setAmtCr(DmServer.vNull(rsTemp.getString("Amtcr")."0.00"));
                grdLedgerDisplayDetail_Mdl.setAmtCr(DmServer.vNull(rsTemp.getString("Amtcr"), "0.00"));
                grdLedgerDisplayDetail_Mdl.setAmtDr(DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
//                grdLedgerDisplayDetail_Mdl.setAmtDr(DmShared.xNull(rsTemp.getString("AmtDr")));
                mrTemp.add(grdLedgerDisplayDetail_Mdl);
            }
            psTemp.close();
            rsTemp.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setStatus(false);
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
