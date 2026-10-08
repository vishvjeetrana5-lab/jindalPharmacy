package dataman.accounts.accountOpeningEntry.server;

import dataman.accounts.accountOpeningEntry.client.AccountOpeningEntry;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.accountOpeningEntry.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AccountOpeningEntry_SrvImpl extends DmRSSPrivate implements AccountOpeningEntry_Srv {

    private final String vCat = "'OPBAL'", voucherTypefieldName = "lm.V_Type", voucherDateFieldName = "lm.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String vPrefix) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended lstNavigatorTmp;
        String sqlQuery, sqlCondition;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            sqlQuery = "Select LM.DocId As SearchCode "
                    + "From LedgerM LM "
                    + "Left Join Voucher_Type VT ON VT.V_Type = LM.V_Type "
                    + "Where LM.Site_Code = :siteCode And "
                    + "LM.V_Prefix = :vPrefix And "
                    + "VT.Category In (" + vCat + ") ";
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += " Order By LM.V_Date,VT.short_Name,LM.V_No,LM.recID ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", vPrefix);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<AccountOpeningEntry_Mdl> getMoveRec(String strKey) {
        DmResult<AccountOpeningEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        String accountName = "";
        String voucherCategory = "OPBAL";
        String pubCompVPrefix = "";
        String siteCode = DmServer.getSsnSiteCode(this);
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        AccountOpeningEntry_Mdl mrTemp = new AccountOpeningEntry_Mdl();
        List<GrdAccountOpeningEntry_Mdl> lstTemp = new ArrayList<>();
        dmrTemp.setData(mrTemp);
        mrTemp.setAccountNameAccDesc("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT lg.docId, lg.v_SNo, lg.v_No, lg.recIdPrefix, lg.recId, lg.v_Type, lg.v_Prefix, lg.v_Date, lg.refNo, lg.refDate,"
                    + " lg.subcode, lg.contraSub, lg.amtDr, lg.amtCr, lg.chq_No, lg.chq_Date, lg.clg_Date, lg.favourOf, lg.narration, "
                    + " Lm.V_Date As lmVDate, LM.PostedBy,LM.PreparedBy,LM.U_Name AS ModifiedBy,CCM.Code AS CCMCode,CCM.Name AS CCMName, "
                    + " SG.Name AS DrAcName,VT.Description, SG.Nature "
                    + " FROM LedgerM LM "
                    + " LEFT JOIN Ledger LG ON LM.DocID = LG.DocId "
                    + " LEFT JOIN LedgerCostCenter LGC ON LM.DocID = LGC.DocId "
                    + " LEFT JOIN CostCenterMast CCM ON CCM.Code = LGC.CostCenter  "
                    + " LEFT JOIN SubGroup SG ON SG.SubCode = LG.SubCode "
                    + " LEFT JOIN Voucher_Type VT ON VT.V_Type = LM.V_Type "
                    + " WHERE LG.DocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("Description")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("V_Type")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("RecId")));
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setDate(DmShared.dNull(rsTemp.getDate("lmVDate")));
                mrTemp.setLedgerVoucherDate(DmShared.dNull(rsTemp.getDate("V_Date")));
                mrTemp.setAccountName(DmShared.xNull(rsTemp.getString("DrAcName")));
                accountName = DmShared.xNull(rsTemp.getString("SubCode"));
                mrTemp.setAccountNameCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setCostCenterName(DmShared.xNull(rsTemp.getString("CCMName")));
                mrTemp.setCostCenterCode(DmShared.xNull(rsTemp.getString("CCMCode")));
                mrTemp.setNarration(DmShared.xNull(rsTemp.getString("Narration")));
                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("RefNo")));
                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("RefDate")));
                if (DmShared.vNull(rsTemp.getString("AmtCr")) > 0) {
                    mrTemp.setCredit(DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                } else {
                    mrTemp.setDebit(DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
                }
                mrTemp.setChqNo(DmShared.xNull(rsTemp.getString("Chq_No")));
                mrTemp.setChqDate(DmShared.dNull(rsTemp.getDate("Chq_Date")));
                mrTemp.setFavourOf(DmShared.xNull(rsTemp.getString("Favourof")));
                mrTemp.setvPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                mrTemp.setContraSub(DmShared.xNull(rsTemp.getString("ContraSub")));
                mrTemp.setChqClearDate(DmShared.dNull(rsTemp.getDate("clg_Date")));
                mrTemp.setAccountNameAccDesc(DmShared.xNull(rsTemp.getString("Nature")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("ModifiedBy")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("PostedBy")));
                pubCompVPrefix = DmShared.xNull(rsTemp.getString("V_Prefix"));
            }

            psTemp.close();
            rsTemp.close();

            strSQL = "   Select LG.DocId, VT.short_Name as  V_Type,LG.RecId,LG.V_Date,LG.RefNo,LG.RefDate,CCM.Name As CCMName, "
                    + " LG.Chq_No,LG.Chq_Date,LG.Narration,LG.AmtDr As AmtDr,LG.AmtCr as AmtCr "
                    + " From Ledger LG "
                    + " Left Join Voucher_Type VT On VT.V_Type = LG.V_Type "
                    + " Left Join LedgerCostCenter LGC On LG.DocID = LGC.DocId "
                    + " Left Join CostCenterMast CCM On CCM.Code = LGC.CostCenter "
                    + " Where LG.SubCode = :textAccountName And LG.V_Prefix = :compVPrefix "
                    + " And LG.Site_Code = :siteCode And VT.Category In ( " + DmServer.getParamList(":vCategory", voucherCategory) + " ) "
                    + " Order By LG.V_Date,LG.V_Type";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("textAccountName", DmShared.iNull(accountName).toString());
            psTemp.setString("CompVPrefix", DmShared.iNull(pubCompVPrefix).toString());
            psTemp.setString("SiteCode", siteCode);
            psTemp.setStringList("vCategory", voucherCategory);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdAccountOpeningEntry_Mdl objListTemp = new GrdAccountOpeningEntry_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("RecID")));
                objListTemp.setVType(DmShared.xNull(rsTemp.getString("V_Type")));
                objListTemp.setDate(DmShared.dNull(rsTemp.getDate("V_Date")));
                objListTemp.setRefNo(DmShared.xNull(rsTemp.getString("RefNo")));
                objListTemp.setRefDate(DmShared.dNull(rsTemp.getDate("RefDate")));
                objListTemp.setChqNo(DmShared.xNull(rsTemp.getString("Chq_No")));
                objListTemp.setChqDate(DmShared.dNull(rsTemp.getDate("Chq_Date")));
                objListTemp.setNarration(DmShared.xNull(rsTemp.getString("Narration")));
                if (DmShared.vNull(rsTemp.getString("AmtCr")) > 0) {
                    objListTemp.setCredit(DmShared.xNull(rsTemp.getString("AmtCr")));
                } else {
                    objListTemp.setDebit(DmShared.xNull(rsTemp.getString("AmtDr")));
                }
                objListTemp.setCostCenterName(DmShared.xNull(rsTemp.getString("CCMName")));

                lstTemp.add(objListTemp);
            }
            mrTemp.getStrGrdData().addAll(lstTemp);

            psTemp.close();
            rsTemp.close();

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

    public boolean haveControlPermission(UserPermission userPermission, String moduleName, String menuName, String controlCaption) {
        boolean have = false;
        List<UserMenu> userMenuList;
        List<String> controlPermissionList;

        if (userPermission != null) {
            if (userPermission.getUserMenu() != null) {
                userMenuList = userPermission.getUserMenu().get(DmShared.xNull(moduleName));
                if (userMenuList != null) {
                    for (int i = 0; i < userMenuList.size(); i++) {
                        if (userMenuList.get(i) != null) {
                            if (DmShared.xNull(userMenuList.get(i).getMenuName()).trim().equalsIgnoreCase(DmShared.xNull(menuName).trim())) {
                                controlPermissionList = userMenuList.get(i).getControlPermission();
                                if (controlPermissionList != null) {
                                    if (controlPermissionList.contains(controlCaption)) {
                                        have = true;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        return have;
    }

    @Override
    public DmResult<String> saveRecord(UserPermission userPermission, AccountOpeningEntry_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        boolean autoApprove = false;

        dmrTemp.setData("");

        try {
            AccountOpeningEntry_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            autoApprove = haveControlPermission(userPermission, "", "", AccountOpeningEntry.approvedByBlog);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getvPrefix(), cnTemp, this));
                mrTemp.setObjRecId(DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getvPrefix(), cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
            } else {
                DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getvPrefix(), cnTemp, this, true);
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DmShared.convertDate(mrTemp.getDate()));
            calendar.add(Calendar.DATE, -1);
            mrTemp.setLedgerVoucherDate(DmShared.dNull(calendar.getTime()));

            sqlQuery = "select postedBy FROM LedgerM WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("postedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't edit this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (!blnAdd) {
                if (!"".equals(DmShared.xNull(mrTemp.getSharedKeyFieldCode()))) {
                    if (!"".equals(DmShared.xNull(mrTemp.getContraSub()))) {
                        throw new UnsupportedOperationException("This entry has been done from [Journal Voucher]. You can not edit/delete this entry.");
                    }

                    sqlQuery = "Select Count(*) as  Cnt From DataAudit Where DocId= :docId ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        if (rsTemp.getLong("cnt") > 0) {
                            throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
                        }
                    }
                    rsTemp.close();
                    psTemp.close();

                    sqlQuery = "Select Count(*) as cnt From LedgerAdj Where Adj_DocID= :docId ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        if (rsTemp.getLong("cnt") > 0) {
                            throw new UnsupportedOperationException("Record Has Been Adjusted  You Can Not Edit/Delete This Bill.");
                        }
                    }
                    rsTemp.close();
                    psTemp.close();

                } else {
                    throw new UnsupportedOperationException("No Record Found.");
                }
            }
            //Delete BankReco Record
            /////////////////////////////////////////////////////////////////////////////////////
//            sqlQuery = "Delete From BankReco Where DocId= :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//            psTemp.executeUpdate();
//            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////

            //Delete LedgerCostCenter Record
            /////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Delete From LedgerCostCenter Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////

            //Delete Ledger Record
            /////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Delete From Ledger Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////

            // Insert Record in LedgerM Table 
            /////////////////////////////////////////////////////////////////////////////////////
            if (blnAdd) {
                sqlQuery = "Insert Into LedgerM(docId, V_Type, V_Prefix, Site_Code, RecId, recIdPrefix, v_No, V_Date, SubCode,  "
                        + "U_EntDt,PreparedBy,PostedBy) Values "
                        + "(:docId,:vType,:vPrefix,:siteCode,:recId, :recIdPrefix, :v_No,:vDate,:subCode,:uEntDt,:preparedBy,:PostedBy) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("vType", DmShared.xNull(mrTemp.getVoucherTypeCode()));
                psTemp.setString("vPrefix", DmShared.xNull(mrTemp.getvPrefix()));
                psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getDate()));
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getAccountNameCode()));
                psTemp.setString("PostedBy", (autoApprove ? DmServer.getSsnUserName(this) : null));
                psTemp.setString("preparedBy", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "Update LedgerM Set "
                        + "V_Date = :vDate, "
                        + "SubCode = :subCode, "
                        + "transferDt = null, "
                        + "U_Name = :uName , "
                        + "PostedBy = :PostedBy , "
                        + "U_EntDt = :uEntDt "
                        + "Where DocId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getDate()));
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getAccountNameCode()));
                psTemp.setString("uName", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setString("PostedBy", (autoApprove ? DmServer.getSsnUserName(this) : null));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////

            // Insert Record in Ledger Table 
            /////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Insert Into Ledger(DocId,RecId, recIdPrefix, v_No,V_Date,SubCode,AmtDr,AmtCr,Chq_No,Chq_Date,Clg_Date,favourOf,  "
                    + "Narration,V_Type,V_SNo,V_Prefix,Site_Code,isPosted,RefNo,RefDate) Values "
                    + "(:docId,:recId, :recIdPrefix, :v_No,:vDate,:subCode,:amtDr,:amtCr,:chq_No,:chq_Date,:clg_Date,:favourOf,:narration,:vType,:vSNo,:vPrefix,:siteCode,:posted,:refNo,:refDate) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
            psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
            psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getLedgerVoucherDate()));
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getAccountNameCode()));
            psTemp.setString("amtDr", DmServer.vNull(mrTemp.getDebit(), "0.00"));
            psTemp.setString("amtCr", DmServer.vNull(mrTemp.getCredit(), "0.00"));
            psTemp.setString("chq_No", DmShared.xNull(mrTemp.getChqNo()));
            psTemp.setDate("chq_Date", DmServer.convertDate(mrTemp.getChqDate()));
            psTemp.setDate("clg_Date", DmServer.convertDate(mrTemp.getChqClearDate()));
            psTemp.setString("favourOf", DmShared.xNull(mrTemp.getFavourOf()));
            psTemp.setString("narration", DmShared.xNull(mrTemp.getNarration()));
            psTemp.setString("vType", DmShared.xNull(mrTemp.getVoucherTypeCode()));
            psTemp.setString("vSNo", "1");
            psTemp.setString("vPrefix", DmShared.xNull(mrTemp.getvPrefix()));
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            psTemp.setString("posted", "1");
            psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
            psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
            psTemp.executeUpdate();
            psTemp.close();

            // Insert Record in LedgerCostCenter Table 
            /////////////////////////////////////////////////////////////////////////////////////
            if (!mrTemp.getCostCenterCode().equalsIgnoreCase("")) {
                sqlQuery = "Insert Into LedgerCostCenter (DocId, V_Sno, Sno, CostCenter, AmtDr,AmtCr) Values"
                        + "(:docId,'1','1',:costCenter,:amtDr,:amtCr) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("costCenter", DmShared.foriegnKey(mrTemp.getCostCenterCode()));
                psTemp.setString("amtDr", DmServer.vNull(mrTemp.getDebit(), "0.00"));
                psTemp.setString("amtCr", DmServer.vNull(mrTemp.getCredit(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }
            /////////////////////////////////////////////////////////////////////////////////////

//            // Insert Record in BankReco Table 
//            /////////////////////////////////////////////////////////////////////////////////////
//            if (DmShared.xNull(mrTemp.getAccountNameAccDesc()).equalsIgnoreCase("BANK")) {
//                sqlQuery = "insert Into BankReco(DocID,V_SNo,Bank_V_SNo,Favourof,Chq_No,Chq_Date,Clg_Date,AmtDr,AmtCr,Remark) Values"
//                        + "(:docID,'1','1',:favourOf,:chqNo,:chqDate,:clgDate,:amtDr,:amtCr,:remark) ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("docID", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//                psTemp.setString("favourOf", DmShared.xNull(mrTemp.getFavourOf()));
//                psTemp.setString("chqNo", DmShared.xNull(mrTemp.getChqNo()));
//                psTemp.setDate("chqDate", DmServer.convertDate(mrTemp.getChqDate()));
//                psTemp.setDate("clgDate", DmServer.convertDate(mrTemp.getChqClearDate()));
//                psTemp.setString("amtDr", DmServer.vNull(mrTemp.getDebit(), "0.00"));
//                psTemp.setString("amtCr", DmServer.vNull(mrTemp.getCredit(), "0.00"));
//                psTemp.setString("remark", DmShared.xNull(mrTemp.getReconcilationRemark()));
//                psTemp.executeUpdate();
//                psTemp.close();
//            }
//            /////////////////////////////////////////////////////////////////////////////////////
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);

            strSQL = "select postedBy FROM LedgerM WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("postedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Select Count(*) as  Cnt From DataAudit Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Select Count(*) as cnt From LedgerAdj Where Adj_DocID= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Record Has Been Adjusted  You Can Not Edit/Delete This Bill.");
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Delete From LedgerCostCenter Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

//            strSQL = "Delete From BankReco Where DocId= :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//            psTemp.setString("docId", DmShared.iNull(strKey).toString());
//            psTemp.executeUpdate();
//            psTemp.close();
            strSQL = "Delete From Ledger Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From LedgerM Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, String vPrfix) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strVCategory = "OPBAL", sqlQuery, sqlCondition;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select LM.DocId As SearchCode,LM.V_Type As V_Type,VT.Description As Voucher_Type, "
                    + " LM.RecId As Voucher_No, VT.short_Name,LM.V_No, LM.recID, "
                    + " LM.V_Date As Voucher_Date,CCM.Name As Cost_Center, "
                    + " SG.Name,LG.RefNo As Ref_No,LG.RefDate As Ref_Date,LG.Narration, "
                    + " LG.AmtDr As Debit,LG.AmtCr As Credit,LM.PreparedBy,LM.U_Name As ModifiedBy,LM.PostedBy, FORMAT(lm.u_EntDt,'dd-MMM-yyyy HH.mm') AS entryDateTime "
                    + " From LedgerM LM   "
                    + " Left Join Ledger LG On LG.DocId = LM.DocId  "
                    + " Left Join SubGroup SG On LG.SubCode = SG.SubCode "
                    + " Left Join Voucher_Type VT On LG.V_Type = VT.V_Type "
                    + " Left Join LedgerCostCenter LGC On LG.DocID = LGC.DocId "
                    + " Left Join CostCenterMast CCM On CCM.Code = LGC.CostCenter "
                    + " Where LM.V_Prefix = :vPrefix And isNull(LG.isSystem_Generated,'0')='0' And "
                    + " LM.Site_Code = :siteCode And VT.Category In (" + DmServer.getParamList(":vCategory", strVCategory) + " ) ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);

            sqlWithParam.setString("vPrefix", vPrfix);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setStringList("vCategory", strVCategory);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
                listNode.setVType(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setVoucherTypeName(DmShared.xNull(rsTemp.getString("Voucher_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Voucher_No")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("Voucher_Date")));
                listNode.setCostCenterName(DmShared.xNull(rsTemp.getString("Cost_Center")));
                listNode.setAccountName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("PostedBy")));
                listNode.setPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                listNode.setModifiedBy(DmShared.xNull(rsTemp.getString("ModifiedBy")));
                listNode.setEntryDtTime(DmShared.xNull(rsTemp.getString("entryDateTime")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("Ref_No")));
                listNode.setRefDate(DmShared.dNull(rsTemp.getDate("Ref_Date")));
                listNode.setNarration(DmShared.xNull(rsTemp.getString("Narration")));
                listNode.setDebit(DmServer.vNull(rsTemp.getString("Debit"), "0.00"));
                listNode.setCredit(DmServer.vNull(rsTemp.getString("Credit"), "0.00"));
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
    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHAccountName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountName_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select SG.SubCode,SG.Name As PartyName,SG.ManualCode As Code,AG.GroupName, "
                    + " CT.CityName,SG.Add1,SG.Add2,SG.Nature "
                    + " From Subgroup SG "
                    + " Left Join AcGroup AG On AG.GroupCode = SG.GroupCode "
                    + " Left Join City CT On CT.CityCode = SG.CityCode "
                    + " LEFT JOIN subgroupsitelist SBSL ON SG.subCode=SBSL.subCode "
                    + " Where   SBSL.Site_Code = :siteCode And SG.isActive = 1 ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountName_Mdl();
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setAccountNameAccDesc(DmShared.xNull(rsTemp.getString("Nature")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("PartyName")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setGroupName(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setAddress1(DmShared.xNull(rsTemp.getString("Add1")));
                listNode.setAddress2(DmShared.xNull(rsTemp.getString("Add2")));
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
    public DmResultPageing<List<TxtHCostCenter_Mdl>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile) {
        return getTxtHCostCenter(helper, blnToEndOfFile, false);
    }

    @Override
    public DmResultPageing<List<TxtHCostCenter_Mdl>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile, boolean BlnInsert) {
        DmResultPageing<List<TxtHCostCenter_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCostCenter_Mdl> lstTemp = new ArrayList<>();
        TxtHCostCenter_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String strSQL;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = "SELECT CCM.Code, CCM.Name, CCM.ManualCode, CCM.Site_Code,sm.name as siteName  "
                    + "  FROM costcentermast CCM   "
                    + "  left join siteMast as sm on sm.code = ccm.site_Code "
                    + "  WHERE isNull(CCM.UnderCostCenter, '') = '' AND CCM.isActive = 1 ";

            if (!"".equals(DmServer.getSsnCostCenterList(this))) {
                strSQL += "  And CCM.Code In (" + DmServer.getParamList(":costCenter", DmServer.getSsnCostCenterList(this)) + ") ";
            }

            if (!BlnInsert) {
                strSQL += "And isNull(CCM.Site_Code,:siteCode) = :siteCode ";
            }
            sqlWithParam.setSQL(strSQL);

            if (!"".equals(DmServer.getSsnCostCenterList(this))) {
                sqlWithParam.setStringList("costCenter", DmServer.getSsnCostCenterList(this));
            }

            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCostCenter_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setMaualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setSite(DmShared.xNull(rsTemp.getString("siteName")));
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
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select VT.V_Type, VT.Description "
                    + " From Voucher_Type VT "
                    + " Where VT.Category In (" + vCat + ") ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResultPageing<List<GrdAccountOpeningEntry_Mdl>> getFrmGrdData(String accountNameCode, String VPrefix) {
        DmResultPageing<List<GrdAccountOpeningEntry_Mdl>> dmrTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL = "";
        String strTextAccountCode = accountNameCode;
        String strVCategory = "OPBAL";
        String strPubCompVPrefix = VPrefix;
        String strPubSiteCode = DmServer.getSsnSiteCode(this);

        //AccountOpeningEntry_Model.AccountOpeningEntry mrTemp = new AccountOpeningEntry_Mdl();
        List<GrdAccountOpeningEntry_Mdl> lstTemp = new ArrayList<>();
        dmrTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "   Select LG.DocId, VT.short_Name as  V_Type,LG.RecId,LG.V_Date,LG.RefNo,LG.RefDate,CCM.Name As CCMName, "
                    + " LG.Chq_No,LG.Chq_Date,LG.Narration,LG.AmtDr,LG.AmtCr "
                    + " From Ledger LG "
                    + " Left Join Voucher_Type VT On VT.V_Type = LG.V_Type "
                    + " Left Join LedgerCostCenter LGC On LG.DocID = LGC.DocId "
                    + " Left Join CostCenterMast CCM On CCM.Code = LGC.CostCenter "
                    + " Where LG.SubCode = :textAccountName And LG.V_Prefix = :compVPrefix "
                    + " And LG.Site_Code = :siteCode And VT.Category In ( " + DmServer.getParamList(":vCategory", strVCategory) + " ) "
                    + " Order By LG.V_Date,LG.V_Type";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("textAccountName", strTextAccountCode);
            psTemp.setString("compVPrefix", strPubCompVPrefix);
            psTemp.setString("siteCode", strPubSiteCode);
            psTemp.setStringList("vCategory", strVCategory);
            rsTemp = psTemp.executeQuery();
            int SrNo = 1;
            while (rsTemp.next()) {
                GrdAccountOpeningEntry_Mdl objListTemp = new GrdAccountOpeningEntry_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("RecID")));
                objListTemp.setVType(DmShared.xNull(rsTemp.getString("V_Type")));
                objListTemp.setDate(DmShared.dNull(rsTemp.getDate("V_Date")));
                objListTemp.setRefNo(DmShared.xNull(rsTemp.getString("RefNo")));
                objListTemp.setRefDate(DmShared.dNull(rsTemp.getDate("RefDate")));
                objListTemp.setChqNo(DmShared.xNull(rsTemp.getString("Chq_No")));
                objListTemp.setChqDate(DmShared.dNull(rsTemp.getDate("Chq_Date")));
                objListTemp.setNarration(DmShared.xNull(rsTemp.getString("Narration")));
                if (DmShared.vNull(rsTemp.getString("AmtCr")) > 0) {
                    objListTemp.setCredit(DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                } else {
                    objListTemp.setDebit(DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
                }
                objListTemp.setCostCenterName(DmShared.xNull(rsTemp.getString("CCMName")));

                lstTemp.add(objListTemp);
            }

            psTemp.close();
            rsTemp.close();

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

    @Override
    public DmResult<String> setApproval(String approvedBy, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        int approved;
        String strSQL = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(docId);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (DmShared.xNull(approvedBy).trim().equalsIgnoreCase("")) {
                strSQL = "UPDATE LedgerM SET  "
                        + "		postedBy = :userName "
                        + "        WHERE docid = :docId "
                        + "        AND  isNull(postedBy,'') = ''";
            } else {
                strSQL = "UPDATE LedgerM SET  "
                        + "		postedBy = NULL "
                        + "        WHERE docid = :docId "
                        + "        AND  :userName IN (isNull(postedBy,''),'SA','SUPER')";

            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", docId);
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            approved = psTemp.executeUpdate();

            psTemp.close();
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select v_Type as code, description as name from voucher_Type where category = " + vCat + " and isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            defaultValue.setBankNatureCode(DmServer.getCodeViaManualCode("BK", "manualCode", "subgroupNature", "Code", cnTemp));

            dmrTemp.setData(defaultValue);
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
