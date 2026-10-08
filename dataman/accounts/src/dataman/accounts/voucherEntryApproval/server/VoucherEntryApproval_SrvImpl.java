package dataman.accounts.voucherEntryApproval.server;

import dataman.accounts.voucherEntryApproval.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VoucherEntryApproval_SrvImpl extends DmRSSPrivate implements VoucherEntryApproval_Srv {

    LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    String voucherCategory = "" + lvc.JOURNAL + "," + lvc.CONTRA + "," + lvc.BANK_PAYMENT + "," + lvc.BANK_RECEIPT + "";

    @Override
    public DmResultPageing<List<GrdVoucherEntryApproval_Mdl>> getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdVoucherEntryApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdVoucherEntryApproval_Mdl> lstTemp = new ArrayList<>();
        GrdVoucherEntryApproval_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition;

        try {
            rstTemp.setData(lstTemp);

            condition = " ";

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  LM.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  LM.v_Date <= :tov_Date ";
            }

            if (!DmShared.xNull(param.getCostCenterCode()).equalsIgnoreCase("")) {
                condition += " AND LC.CostCenter IN(" + DmServer.getParamList(":costCenter", param.getCostCenterCode()) + " )";
            }
            if (!DmShared.xNull(param.getDocId()).equalsIgnoreCase("")) {
                condition += " AND LM.docId  IN(" + DmServer.getParamList(":docId", param.getDocId()) + " )";
            }
            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(LM.postedBy,'') = ''";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(LM.postedBy,'') <> '' ";

            }

            if (!DmShared.xNull(param.getVoucherTypeCode()).equalsIgnoreCase("")) {
                condition += " And LM.V_Type In (" + DmServer.getParamList(":voucherType", param.getVoucherTypeCode()) + ") ";
            }

            condition += " AND LM.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select LM.DocId,LM.V_Type,lg.refDate,lg.refNo,lg.subcode,lg.name AS FirstActName,lg.Amt,LM.Site_Code,LM.V_Date,SG.Name As HeaderActName,LM.RecId, "
                    + " LM.narration,LC.Cnt_CCM,LC.CCMName,LM.postedBy,FORMAT(LM.postedByDateTime,'dd-MMM-yyyy HH:mm') AS postedByDateTime,sm.name AS siteName,"
                    + " VT.Category, vt.short_Name as vtypeShortName, sgn.manualCode As SGNature,LM.wtaCode,LC.CostCenter "
                    + " From LedgerM LM "
                    + " Left Join SubGroup SG On LM.SubCode = SG.SubCode "
                    + " Left Join Voucher_Type VT On VT.V_Type = LM.V_Type "
                    + " left join siteMast as sm on sm.code = LM.site_Code "
                    + " LEFT JOIN subGroupNature as sgn on sg.nature = sgn.code "
                    + " LEFT JOIN (select  ld.docId,MAX(ld.refDate) AS refDate,MAX(ld.refNo) AS refNo,MAX(ld.subcode) AS subcode,MAX(SG2.name) AS name,"
                    + "		MAX(ld.amtDr-ld.amtCr) AS Amt  "
                    + " 	from Ledger ld"
                    + "		Left Join SubGroup SG2 On ld.SubCode = SG2.SubCode"
                    + "         group by docId "
                    + "		)as lg ON lg.docId=LM.docId"
                    + " Left Join ("
                    + "	Select  LGC.DocId,Count(Distinct LGC.CostCenter) As Cnt_CCM,Max(LGC.CostCenter) As CostCenter,Max(CCM.Name) As CCMName "
                    + "	From LedgerCostCenter LGC "
                    + "	Left Join CostCenterMast CCM On CCM.Code = LGC.CostCenter "
                    + "	Group By LGC.DocId "
                    + "	) As LC On LC.DocId = LM.DocId "
                    + " where VT.Category In ('" + lvc.BANK_PAYMENT + "','" + lvc.BANK_RECEIPT + "','" + lvc.JOURNAL + "','" + lvc.CONTRA + "')" + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getCostCenterCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("costCenter", DmShared.xNull(param.getCostCenterCode()));
            }
            if (!DmShared.xNull(param.getDocId()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(param.getDocId()));
            }
            if (!DmShared.xNull(param.getVoucherTypeCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("voucherType", DmShared.xNull(param.getVoucherTypeCode()));
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdVoucherEntryApproval_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("vtypeShortName")));
                listNode.setVoucherCategory(DmShared.xNull(rsTemp.getString("Category")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("postedBy")));
                if (!DmShared.xNull(rsTemp.getString("postedBy")).equalsIgnoreCase("")) {
                    listNode.setIsChecked("1");
                } else {
                    listNode.setIsChecked("0");
                }
                listNode.setApproval(DmShared.xNull(rsTemp.getString("postedBy")) + " " + DmShared.xNull(rsTemp.getString("postedByDateTime")));
                listNode.setCostCenter(DmShared.xNull(rsTemp.getString("CCMName")));
                listNode.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                listNode.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                listNode.setFirstActName(DmShared.xNull(rsTemp.getString("FirstActName")));
                listNode.setHeaderActName(DmShared.xNull(rsTemp.getString("HeaderActName")));
                listNode.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                listNode.setWtaCode(DmShared.iNull(rsTemp.getString("wtaCode")).toString());
                listNode.setAmount(DmServer.vNull(rsTemp.getString("Amt"), "0.00"));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Pending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHVoucherNo_Mdl>> getTxtHVoucherNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucherNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherNo_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(voucherCategory).trim().equalsIgnoreCase("")) {
                condition += " And vt.category In ( " + DmServer.getParamList(":category", voucherCategory) + ") ";
            }
            sqlQuery = "SELECT    "
                    + " (lm.docId) as code, (lm.recId) as Name "
                    + "  FROM ledgerM AS lm  "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = lm.v_Type "
                    + " where lm.site_Code = '" + DmServer.getSsnSiteCode(this) + "' " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(voucherCategory).trim().equalsIgnoreCase("")) {
                sqlWithParam.setStringList("category", voucherCategory);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, StrCCenter;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        //ravi//  If Not BlnInsert Then StrSQL += "And isNull(CCM.Site_Code,'" & LIPublic.StrPubSiteCode & "') = '" & LIPublic.StrPubSiteCode & "' "
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            StrCCenter = "";
            if (!DmServer.getSsnCostCenterList(this).equalsIgnoreCase("")) {
                StrCCenter = "And Code In (" + DmServer.getSsnCostCenterList(this) + ")";
            }

            sqlQuery = "Select Code,Name,ManualCode,Site_Code "
                    + "From CostCenterMast "
                    + "Where isNull(UnderCostCenter,'') = '' And isActive = 1 " + StrCCenter + "";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(voucherCategory).trim().equalsIgnoreCase("")) {
                condition += " And vt.category In ( " + DmServer.getParamList(":category", voucherCategory) + ") ";
            }
            sqlWithParam.setSQL("SELECT VT.V_Type AS CODE, VT.Description As name "
                    + " FROM Voucher_Type VT "
                    + " WHERE VT.V_Type IN ("
                    + "     SELECT V_Type FROM Ledger "
                    + "     WHERE Site_code IN (" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + "  )"
                    + "     ) " + condition);
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            if (!DmShared.xNull(voucherCategory).trim().equalsIgnoreCase("")) {
                sqlWithParam.setStringList("category", voucherCategory);
            }

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

}
