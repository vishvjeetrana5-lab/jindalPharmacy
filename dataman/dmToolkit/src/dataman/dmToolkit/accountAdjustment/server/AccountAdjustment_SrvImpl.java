package dataman.dmToolkit.accountAdjustment.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountAdjustment_SrvImpl extends DmRSSPrivate implements AccountAdjustment_Srv {

    @Override
    public DmResultPageing<List<GrdAdjustment_Mdl>> getLedger(Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdAdjustment_Mdl>> dmrTemp = new DmResultPageing<List<GrdAdjustment_Mdl>>();
        List<GrdAdjustment_Mdl> lstTemp = new ArrayList<GrdAdjustment_Mdl>();
        GrdAdjustment_Mdl lstNode = null;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(param.getVoucherNo()).trim().equalsIgnoreCase("")) {
                condition += " AND l.recId LIKE :recId";
            }
            if (!DmShared.dNull(param.getVoucherDate()).trim().equalsIgnoreCase("")) {
                condition += " AND l.v_Date = :v_Date";
            }
            if (!DmShared.xNull(param.getRefNo()).trim().equalsIgnoreCase("")) {
                condition += " AND l.refNo LIKE :refNo";
            }
            if (!DmShared.xNull(param.getVoucherTypeCode()).trim().equalsIgnoreCase("")) {
                condition += " AND l.v_Type IN (" + DmServer.getParamList(":v_Type", param.getVoucherTypeCode()) + ")";
            }
            if (!DmShared.xNull(param.getAcCode()).trim().equalsIgnoreCase("")) {
                condition += " AND l.subCode IN (" + DmServer.getParamList(":subCode", param.getAcCode()) + ")";
            }
            if (param.isShowDebit()) {
                condition += " AND ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0) > 0";
            } else {
                condition += " AND ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0) < 0";
            }

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
                    + " FROM ledger l"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = l.v_Type"
                    + " LEFT JOIN subGroup sg ON sg.subcode = l.subCode"
                    + " LEFT JOIN city ct ON ct.cityCode = sg.cityCode"
                    + " LEFT JOIN ("
                    + "     SELECT la.adj_DocId, la.adj_V_SNo, SUM(la.adjustment) AS adjusted \n"
                    + "     FROM ledgerAdj la"
                    + "     GROUP By la.adj_DocId, la.adj_V_SNo"
                    + " ) vLa ON vLa.adj_DocId = l.docId AND vLa.adj_V_SNo = l.v_SNo"
                    + " WHERE l.isPosted = 1" + condition;

            sqlWithParam.setSQL(sqlQuery);

            if (!DmShared.xNull(param.getVoucherNo()).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("recId", param.getVoucherNo() + "%");
            }
            if (!DmShared.dNull(param.getVoucherDate()).trim().equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmShared.convertDate(param.getVoucherDate()));
            }
            if (!DmShared.xNull(param.getRefNo()).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("refNo", param.getRefNo() + "%");
            }
            if (!DmShared.xNull(param.getVoucherTypeCode()).trim().equalsIgnoreCase("")) {
                sqlWithParam.setStringList("v_Type", param.getVoucherTypeCode());
            }
            if (!DmShared.xNull(param.getAcCode()).trim().equalsIgnoreCase("")) {
                sqlWithParam.setStringList("subCode", param.getAcCode());
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
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
                lstNode.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                lstNode.setAdjusted(DmServer.vNull(rsTemp.getString("adjusted"), "0.00"));
                lstTemp.add(lstNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(helper.getPaging());
            dmrTemp.setData(lstTemp);
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
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
            } catch (Exception ex) {

            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<TxtHLedger_Mdl>> getTxtHLedger(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLedger_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLedger_Mdl> lstTemp = new ArrayList<>();
        TxtHLedger_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   sg.subCode"
                    + " , sg.name"
                    + " , sg.manualCode"
                    + " , ct.cityName"
                    + " FROM subGroup sg"
                    + " LEFT JOIN city ct ON ct.cityCode = sg.cityCode"
                    + " LEFT JOIN subGroupSiteList sgl ON sgl.subCode = sg.subCode"
                    + " WHERE sgl.site_Code = :site_Code"
                    + " AND sg.isActive = 1");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLedger_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   vt.v_Type as code"
                    + " , vt.description as name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getDrCr(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Dr");
            listNode.setSharedKeyFieldName("Dr");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Cr");
            listNode.setSharedKeyFieldName("Cr");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {

        }
        return rstTemp;
    }

    @Override
    public DmResult<List<GrdHistory_Mdl>> getHistory(String adjDocId, String adjV_Sno) {
        DmResult<List<GrdHistory_Mdl>> dmrTemp = new DmResult<List<GrdHistory_Mdl>>();
        List<GrdHistory_Mdl> lstTemp = new ArrayList<GrdHistory_Mdl>();
        GrdHistory_Mdl lstNode;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT "
                    + "   l.docId AS main_DocId"
                    + " , l.recId AS main_VNo"
                    + " , vt.short_Name AS main_VType"
                    + " , vt.mnuText AS main_MnuText"
                    + " , vt.mnuAttachedInModule AS main_MnuAttachedInModule"
                    + " , l.v_Date AS main_VDate"
                    + " , NULL AS adj_DocId"
                    + " , NULL AS adj_VNo"
                    + " , NULL AS adj_VType"
                    + " , NULL AS adj_MnuText"
                    + " , NULL AS adj_MnuAttachedInModule"
                    + " , NULL AS adj_VDate"
                    + " , sg.name AS main_LgName"
                    + " , l.narration"
                    + " , ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0) AS amount"
                    + " , NULL AS adjustment"
                    + " FROM ledger l "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = l.v_Type"
                    + " LEFT JOIN subGroup sg ON sg.subCode = l.subCode"
                    + " WHERE docId = :adjDocId"
                    + ""
                    + " UNION ALL"
                    + ""
                    + " SELECT"
                    + "   b.docId AS main_DocId"
                    + " , b.recId AS main_VNo"
                    + " , vtb.short_Name AS main_VType"
                    + " , vtb.mnuText AS main_MnuText"
                    + " , vtb.mnuAttachedInModule AS main_MnuAttachedInModule"
                    + " , b.v_Date AS main_VDate"
                    + " , l.docId AS adj_DocId"
                    + " , l.recId AS adj_VNo"
                    + " , vtl.short_Name AS adj_VType"
                    + " , vtl.mnuText AS adj_MnuText"
                    + " , vtl.mnuAttachedInModule AS adj_MnuAttachedInModule"
                    + " , l.v_Date AS adj_VDate"
                    + " , sgb.name AS main_LgName"
                    + " , l.narration"
                    + " , NULL AS amount"
                    + " , la.adjustment"
                    + " FROM ledgerAdj la"
                    + " LEFT JOIN ledger l ON l.docId = la.vr_DocId AND l.v_SNo = la.vr_V_SNo"
                    + " LEFT JOIN voucher_Type vtl ON vtl.v_Type = l.v_Type"
                    + " LEFT JOIN subGroup sgl ON sgl.subCode = l.subCode"
                    + " LEFT JOIN ledger b ON b.docId = la.adj_DocId AND b.v_SNo = la.adj_V_SNo"
                    + " LEFT JOIN voucher_Type vtb ON vtb.v_Type = b.v_Type"
                    + " LEFT JOIN subGroup sgb ON sgb.subCode = b.subCode"
                    + " WHERE la.adj_docId = :adjDocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("adjDocId", DmShared.xNull(adjDocId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new GrdHistory_Mdl();
                lstNode.setMain_DocId(DmShared.xNull(rsTemp.getString("main_DocId")));
                lstNode.setMain_VNo(DmShared.xNull(rsTemp.getString("main_VNo")));
                lstNode.setMain_VType(DmShared.xNull(rsTemp.getString("main_VType")));
                lstNode.setMain_VDate(DmShared.dNull(rsTemp.getDate("main_VDate")));
                lstNode.setMain_LgName(DmShared.xNull(rsTemp.getString("main_LgName")));
                lstNode.setMain_MnuText(DmShared.xNull(rsTemp.getString("main_MnuText")));
                lstNode.setMain_MnuAttachedInModule(DmShared.xNull(rsTemp.getString("main_MnuAttachedInModule")));
                lstNode.setAdj_DocId(DmShared.xNull(rsTemp.getString("adj_DocId")));
                lstNode.setAdj_VNo(DmShared.xNull(rsTemp.getString("adj_VNo")));
                lstNode.setAdj_VType(DmShared.xNull(rsTemp.getString("adj_VType")));
                lstNode.setAdj_VDate(DmShared.dNull(rsTemp.getDate("adj_VDate")));
                lstNode.setAdj_MnuText(DmShared.xNull(rsTemp.getString("adj_MnuText")));
                lstNode.setAdj_MnuAttachedInModule(DmShared.xNull(rsTemp.getString("adj_MnuAttachedInModule")));
                lstNode.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                lstNode.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                lstNode.setAdjustment(DmServer.vNull(rsTemp.getString("adjustment"), "0.00"));
                lstTemp.add(lstNode);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setStatus(true);
            dmrTemp.setData(lstTemp);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception ex) {
            }
        }

        return dmrTemp;
    }
    ////////////////////////////////////////////////////

}
