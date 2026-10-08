package dataman.accounts.gstVoucherEntryApproval.server;

import dataman.accounts.gstVoucherEntryApproval.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GstVoucherEntryApproval_SrvImpl extends DmRSSPrivate implements GstVoucherEntryApproval_Srv {

    LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    String voucherCategory = "" + lvc.CREDIT_INVOICE_GST + "," + lvc.CREDIT_NOTE_GST + "," + lvc.DEBIT_NOTE_GST + "," + lvc.DEBIT_INVOICE_GST + "";

    @Override
    public DmResultPageing<List<GrdGstVoucherEntryApproval_Mdl>> getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdGstVoucherEntryApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdGstVoucherEntryApproval_Mdl> lstTemp = new ArrayList<>();
        GrdGstVoucherEntryApproval_Mdl listNode;
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

            sqlQuery = "Select "
                    + " LM.DocId"
                    + " ,LM.V_Type"
                    + " ,lg.refDate"
                    + " ,lg.refNo"
                    + " ,lg.Amt"
                    + " ,LM.Site_Code"
                    + " ,LM.V_Date"
                    + " ,SG.Name As partyName"
                    + " ,LM.RecId"
                    + " ,LM.narration"
                    + " ,LM.postedBy"
                    + " ,FORMAT(LM.postedByDateTime,'dd-MMM-yyyy HH:mm') AS postedByDateTime"
                    + " ,sm.name AS siteName"
                    + " ,VT.Category"
                    + " ,vt.short_Name as vtypeShortName"
                    + " ,sgn.manualCode As SGNature"
                    + " ,LM.wtaCode"
                    + " From LedgerM LM "
                    + " Left Join SubGroup SG On LM.SubCode = SG.SubCode "
                    + " Left Join Voucher_Type VT On VT.V_Type = LM.V_Type "
                    + " left join siteMast as sm on sm.code = LM.site_Code "
                    + " LEFT JOIN subGroupNature as sgn on sg.nature = sgn.code "
                    + " LEFT JOIN (select  ld.docId,MAX(ld.refDate) AS refDate,MAX(ld.refNo) AS refNo,"
                    + "		SUM(ld.amtDr+ld.amtCr) AS Amt  "
                    + " 	from Ledger ld"
                    + "		Left Join SubGroup SG2 On ld.SubCode = SG2.SubCode"
                    + "         WHERE ld.v_SNo = 1"
                    + "         group by ld.docId "
                    + "		)as lg ON lg.docId=LM.docId"
                    + " where VT.Category In ('" + lvc.CREDIT_INVOICE_GST + "','" + lvc.CREDIT_NOTE_GST + "', '" + lvc.DEBIT_NOTE_GST + "', '" + lvc.DEBIT_INVOICE_GST + "')" + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getDocId()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(param.getDocId()));
            }
            if (!DmShared.xNull(param.getVoucherTypeCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("voucherType", DmShared.xNull(param.getVoucherTypeCode()));
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdGstVoucherEntryApproval_Mdl();
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
                listNode.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                listNode.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                listNode.setHeaderActName(DmShared.xNull(rsTemp.getString("partyName")));
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
                    + " FROM ledgerM AS lm "
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", sqlQuery = "";
        try {

            rstTemp.setData(lstTemp);

            condition += " AND vt.category IN ('" + lvc.CREDIT_INVOICE_GST + "','" + lvc.CREDIT_NOTE_GST + "', '" + lvc.DEBIT_NOTE_GST + "', '" + lvc.DEBIT_INVOICE_GST + "')";

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT VT.V_Type,VT.Description,VT.Category "
                    + " FROM Voucher_Type VT "
                    + " WHERE VT.NCat = 'FA' " + condition;
            sqlWithParam.setSQL(sqlQuery);
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
