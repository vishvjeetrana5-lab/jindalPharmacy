package dataman.accounts.voucherAdjustmentEntry.server;

import dataman.accounts.voucherAdjustmentEntry.shared.Filter_Mdl;
import dataman.accounts.voucherAdjustmentEntry.shared.GrdLedger_Mdl;
import dataman.accounts.voucherAdjustmentEntry.shared.TxtHLedger_Mdl;
import dataman.accounts.voucherAdjustmentEntry.shared.VoucherAdjustmentEntry_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_Server;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.itemAdjustment.server.ItemAdjustment_SrvImpl;
import dataman.dmToolkit.itemAdjustment.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VoucherAdjustmentEntry_SrvImpl extends DmRSSPrivate implements VoucherAdjustmentEntry_Srv {

    @Override
    public DmResultPageing<List<GrdLedger_Mdl>> getLedger(Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdLedger_Mdl>> dmrTemp = new DmResultPageing<List<GrdLedger_Mdl>>();
        List<GrdLedger_Mdl> lstTemp = new ArrayList<GrdLedger_Mdl>();
        GrdLedger_Mdl lstNode = null;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(param.getVoucherNo()).trim().equals("")) {
                condition += " AND l.recId LIKE :recId";
            }
            if (!DmShared.dNull(param.getVoucherDate()).trim().equals("")) {
                condition += " AND l.v_Date = :v_Date";
            }
            if (!DmShared.xNull(param.getRefNo()).trim().equals("")) {
                condition += " AND l.refNo LIKE :refNo";
            }
            if (!DmShared.xNull(param.getVoucherTypeCode()).trim().equals("")) {
                condition += " AND l.v_Type IN (" + DmServer.getParamList(":v_Type", param.getVoucherTypeCode()) + ")";
            }
            if (!DmShared.xNull(param.getAcCode()).trim().equals("")) {
                condition += " AND l.subCode IN (" + DmServer.getParamList(":subCode", param.getAcCode()) + ")";
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
                    + " , l.amtDr"
                    + " , l.amtCr"
                    + " , l.v_Type AS v_TypeCode"
                    + " , vt.short_Name AS v_TypeName"
                    + " , l.subCode AS acCode"
                    + " , sg.name AS acName"
                    + " , sg.manualCode AS acManualCode"
                    + " , sg.cityCode"
                    + " , ct.cityName"
                    + " FROM ledger l"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = l.v_Type"
                    + " LEFT JOIN subGroup sg ON sg.subcode = l.subCode"
                    + " LEFT JOIN city ct ON ct.cityCode = sg.cityCode"
                    + " WHERE l.site_Code = :site_Code" + condition;

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(param.getVoucherNo()).trim().equals("")) {
                sqlWithParam.setString("recId", param.getVoucherNo() + "%");
            }
            if (!DmShared.dNull(param.getVoucherDate()).trim().equals("")) {
                sqlWithParam.setDate("v_Date", DmShared.convertDate(param.getVoucherDate()));
            }
            if (!DmShared.xNull(param.getRefNo()).trim().equals("")) {
                sqlWithParam.setString("refNo", param.getRefNo() + "%");
            }
            if (!DmShared.xNull(param.getVoucherTypeCode()).trim().equals("")) {
                sqlWithParam.setStringList("v_Type", param.getVoucherTypeCode());
            }
            if (!DmShared.xNull(param.getAcCode()).trim().equals("")) {
                sqlWithParam.setStringList("subCode", param.getAcCode());
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new GrdLedger_Mdl();
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                lstNode.setV_No(DmShared.xNull(rsTemp.getString("v_No")));
                lstNode.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                lstNode.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                lstNode.setV_TypeCode(DmShared.xNull(rsTemp.getString("v_TypeCode")));
                lstNode.setV_TypeName(DmShared.xNull(rsTemp.getString("v_TypeName")));
                lstNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                lstNode.setRefDate(DmShared.dNull(rsTemp.getString("refDate")));
                lstNode.setAcCode(DmShared.xNull(rsTemp.getString("acCode")));
                lstNode.setAcName(DmShared.xNull(rsTemp.getString("acName")));
                lstNode.setAcManualCode(DmShared.xNull(rsTemp.getString("acManualCode")));
                lstNode.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                lstNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                lstNode.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                lstNode.setDebit(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                lstNode.setCredit(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
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
    public DmResult<List<GrdLedger_Mdl>> getLedgerByDocId(String voucherDocId) {
        DmResult<List<GrdLedger_Mdl>> dmrTemp = new DmResult<List<GrdLedger_Mdl>>();
        List<GrdLedger_Mdl> lstTemp = new ArrayList<GrdLedger_Mdl>();
        GrdLedger_Mdl lstNode;
        GrdAdjustment_Mdl lstNodeAdj;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   l.docId"
                    + " , l.recId"
                    + " , l.v_SNo"
                    + " , l.v_No"
                    + " , l.v_Date"
                    + " , l.refNo"
                    + " , l.refDate"
                    + " , l.narration"
                    + " , l.amtDr"
                    + " , l.amtCr"
                    + " , l.v_Type AS v_TypeCode"
                    + " , vt.short_Name AS v_TypeName"
                    + " , l.subCode AS acCode"
                    + " , sg.name AS acName"
                    + " , sg.manualCode AS acManualCode"
                    + " , sg.cityCode"
                    + " , ct.cityName"
                    + " FROM ledger l"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = l.v_Type"
                    + " LEFT JOIN subGroup sg ON sg.subcode = l.subCode"
                    + " LEFT JOIN city ct ON ct.cityCode = sg.cityCode \n"
                    + " WHERE l.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(voucherDocId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new GrdLedger_Mdl();
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                lstNode.setV_No(DmShared.xNull(rsTemp.getString("v_No")));
                lstNode.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                lstNode.setV_Date(DmShared.dNull(rsTemp.getString("v_Date")));
                lstNode.setV_TypeCode(DmShared.xNull(rsTemp.getString("v_TypeCode")));
                lstNode.setV_TypeName(DmShared.xNull(rsTemp.getString("v_TypeName")));
                lstNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                lstNode.setRefDate(DmShared.dNull(rsTemp.getString("refDate")));
                lstNode.setAcCode(DmShared.xNull(rsTemp.getString("acCode")));
                lstNode.setAcName(DmShared.xNull(rsTemp.getString("acName")));
                lstNode.setAcManualCode(DmShared.xNull(rsTemp.getString("acManualCode")));
                lstNode.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                lstNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                lstNode.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                lstNode.setDebit(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                lstNode.setCredit(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));

                //======================================< Get Adjustments >======================================//
                double totalAdjustment = 0;
                List<GrdAdjustment_Mdl> lstAdjustment = AccountAdjustment_Server.getAdjustments(lstNode.getSharedKeyFieldCode(), lstNode.getV_SNo(), cnTemp);
                if (lstAdjustment != null) {
                    lstNode.getLstAdjustment().clear();
                    lstNode.getLstAdjustment().addAll(lstAdjustment);
                    for (GrdAdjustment_Mdl gaMdl : lstAdjustment) {
                        totalAdjustment += DmShared.vNull(gaMdl.getAdjustment());
                    }
                }
                if (totalAdjustment != 0) {
                    lstNode.setAdjustment(DmServer.vNull(totalAdjustment, "0.00"));
                }
                //======================================< *************** >======================================//
                //======================================< Get Item Adjustments >======================================//
                List<GrdItemAdjustment_Mdl> lstItemAdjustment = ItemAdjustment_SrvImpl.getItemAdjustments(lstNode.getSharedKeyFieldCode(), lstNode.getV_SNo(), cnTemp);
                if (lstItemAdjustment != null) {
                    lstNode.setLstItemAdjustment(lstItemAdjustment);
                }
                //======================================< *************** >======================================//

                lstTemp.add(lstNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            dmrTemp.setData(lstTemp);
            dmrTemp.setStatus(true);
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
    public DmResult<String> saveRecord(List<GrdLedger_Mdl> lstRecords) {
        DmResult<String> dmrTemp = new DmResult<String>();
        AdjustmentSave_Mdl asMdl;
        ItemAdjustmentSave_Mdl iasMdl;
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        String vr_DocId = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (lstRecords != null && lstRecords.size() > 0) {
                AccountAdjustment_Server.deleteRecord(lstRecords.get(0).getSharedKeyFieldCode(), cnTemp);
                ItemAdjustment_SrvImpl.deleteRecord(lstRecords.get(0).getSharedKeyFieldCode(), cnTemp);

                vr_DocId = lstRecords.get(0).getSharedKeyFieldCode();

                for (GrdLedger_Mdl glMdl : lstRecords) {
                    if (glMdl != null) {
                        if (!DmShared.xNull(glMdl.getSharedKeyFieldCode()).trim().equals("")) {

                            //Save Adjustment
                            if (glMdl.getLstAdjustment() != null) {
                                if (!glMdl.getLstAdjustment().isEmpty()) {
                                    asMdl = new AdjustmentSave_Mdl();
                                    asMdl.setVr_DocId(glMdl.getSharedKeyFieldCode());
                                    asMdl.setVr_V_SNo(glMdl.getV_SNo());
                                    asMdl.setVr_V_Date(glMdl.getV_Date());
                                    asMdl.setVr_SubCode(glMdl.getAcCode());
                                    asMdl.setLstAdjustments(glMdl.getLstAdjustment());
                                    AccountAdjustment_Server.saveRecord(asMdl, cnTemp);
                                }
                            }

                            //Save Item Adjustment
                            if (glMdl.getLstItemAdjustment() != null) {
                                if (!glMdl.getLstItemAdjustment().isEmpty()) {
                                    iasMdl = new ItemAdjustmentSave_Mdl();
                                    iasMdl.setVr_DocId(glMdl.getSharedKeyFieldCode());
                                    iasMdl.setVr_V_SNo(glMdl.getV_SNo());
                                    iasMdl.setVr_V_Date(glMdl.getV_Date());
                                    iasMdl.setVr_SubCode(glMdl.getAcCode());
                                    iasMdl.setLstItemAdjustments(glMdl.getLstItemAdjustment());
                                    ItemAdjustment_SrvImpl.saveRecord(iasMdl, cnTemp);
                                }
                            }

                        }
                    }
                }
            }

            AccountAdjustment_Server.validateAdjustment(vr_DocId, cnTemp);
            ItemAdjustment_SrvImpl.validateItemAdjustment(vr_DocId, cnTemp);

            spBegin.commit();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

}
