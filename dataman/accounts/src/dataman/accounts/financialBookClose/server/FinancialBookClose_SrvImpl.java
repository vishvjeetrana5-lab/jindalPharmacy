package dataman.accounts.financialBookClose.server;

import dataman.accounts.financialBookClose.shared.DefaultValue_Mdl;
import dataman.accounts.financialBookClose.shared.FinancialBookClose_Mdl;
import dataman.accounts.financialBookClose.shared.FinancialBookClose_Srv;
import dataman.accounts.financialBookClose.shared.FinancialBookClose_Vld;
import dataman.accounts.financialBookClose.shared.Find_Mdl;
import dataman.accounts.financialBookClose.shared.TxtHAccountName_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.accountPosting.server.AccountPosting_Server;
import dataman.dmToolkit.accountPosting.shared.AccountPosting_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FinancialBookClose_SrvImpl extends DmRSSPrivate implements FinancialBookClose_Srv {

    private final String vCat = "'FBC'", voucherTypefieldName = "lm.V_Type", voucherDateFieldName = "lm.V_Date";

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        Connection cnTemp1 = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTemp1 = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = " select v_Type as code, description as name from voucher_Type where category = " + vCat + " and isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT c.end_Dt FROM company c"
                    + "  WHERE c.pltCode = :pltCode"
                    + "  AND c.comp_Code = :comp_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp1, sqlQuery);
            psTemp.setString("pltCode", DmServer.getSsnPLTCode(this));
            psTemp.setString("comp_Code", DmServer.getSsnCompanyCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherDate(DmShared.dNull(rsTemp.getDate("end_Dt")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(defaultValue);
            cnTemp.close();
            cnTemp1.close();
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

                if (cnTemp1 != null) {
                    cnTemp1.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

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
            sqlWithParam.setSQL("SELECT vt.V_type, vt.Description "
                    + " FROM Voucher_Type vt  "
                    + " WHERE vt.isActive = 1 "
                    + " and vt.Category = " + vCat + "");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_type")));
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
                    + " Where SBSL.Site_Code = :siteCode "
                    + " And SG.isActive = 1 "
                    + " And AG.groupName = 'Profit & Loss A/c'");
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
    public DmResult<FinancialBookClose_Mdl> getMoveRec(String strKey) {
        DmResult<FinancialBookClose_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        FinancialBookClose_Mdl mrTemp = new FinancialBookClose_Mdl();
        List<FinancialBookClose_Mdl> lstTemp = new ArrayList<>();
        dmrTemp.setData(mrTemp);
        mrTemp.setContraTextAccDesc("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT LM.docId, LM.v_No, LM.recIdPrefix, LM.RecId, LM.v_Type, LM.v_Prefix, LM.subcode, "
                    + " Lm.V_Date As lmVDate, LM.PostedBy,LM.PreparedBy,LM.U_Name AS ModifiedBy, "
                    + " SG.Name AS DrAcName,VT.Description, SG.Nature "
                    + " FROM LedgerM LM "
                    + " LEFT JOIN SubGroup SG ON SG.SubCode = LM.SubCode "
                    + " LEFT JOIN Voucher_Type VT ON VT.V_Type = LM.V_Type "
                    + " WHERE LM.DocId = :docId ";
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
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("lmVDate")));
                mrTemp.setContraText(DmShared.xNull(rsTemp.getString("DrAcName")));
                mrTemp.setContraCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setvPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                mrTemp.setContraTextAccDesc(DmShared.xNull(rsTemp.getString("Nature")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("ModifiedBy")));
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
    public DmResult<String> saveRecord(FinancialBookClose_Mdl mrTemp) {
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
            FinancialBookClose_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            
            DmServer.checkForwordVoucherDateTime(mrTemp.getVoucherDate(), "");

            sqlQuery = "select docid FROM LedgerM WHERE  site_Code = :siteCode and v_prefix= :vPrefix and v_Type= :vType";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp.setString("vType", DmShared.xNull(mrTemp.getVoucherTypeCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("docid")).longValue() > 0) {
                    throw new DmException(0, "Record Already exist.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getvPrefix(), cnTemp, this));
                mrTemp.setObjRecId(DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getvPrefix(), cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
            } else {
                DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getvPrefix(), cnTemp, this, true);
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

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
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getContraCode()));
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
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getContraCode()));
                psTemp.setString("uName", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setString("PostedBy", (autoApprove ? DmServer.getSsnUserName(this) : null));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////

            AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTemp.getSharedKeyFieldCode(), mrTemp.getContraCode(), mrTemp.getvPrefix(), cnTemp), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getObjRecId(), mrTemp.getVoucherTypeCode(), mrTemp.getvPrefix(), DmServer.getSsnSiteCode(this), mrTemp.getVoucherDate(), "", "", "", "", "", true, true, cnTemp);

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
        String strVCategory = "FBC", sqlQuery, sqlCondition;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select LM.DocId As SearchCode,LM.V_Type As V_Type,VT.Description As Voucher_Type, "
                    + " LM.RecId As Voucher_No, VT.short_Name,LM.V_No, "
                    + " LM.V_Date As Voucher_Date, "
                    + " SG.Name, "
                    + " LM.PreparedBy,LM.U_Name As ModifiedBy,LM.PostedBy, FORMAT(lm.u_EntDt,'dd-MMM-yyyy HH.mm') AS entryDateTime "
                    + " From LedgerM LM   "
                    + " Left Join SubGroup SG On SG.SubCode = LM.SubCode "
                    + " Left Join Voucher_Type VT On VT.V_Type = LM.V_Type "
                    + " Where LM.V_Prefix = :vPrefix  "
                    + " And LM.Site_Code = :siteCode And VT.Category In (" + DmServer.getParamList(":vCategory", strVCategory) + " ) ";
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
                listNode.setvType(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setVoucherTypeName(DmShared.xNull(rsTemp.getString("Voucher_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Voucher_No")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("Voucher_Date")));
                listNode.setAccountName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("PostedBy")));
                listNode.setPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                listNode.setModifiedBy(DmShared.xNull(rsTemp.getString("ModifiedBy")));
                listNode.setEntryDtTime(DmShared.xNull(rsTemp.getString("entryDateTime")));
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

    //<editor-fold defaultstate="collapsed" desc="Account Posting">
    private List<AccountPosting_Mdl> iniAcPostList(String docId, String contraCode, String vPrefix, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<AccountPosting_Mdl> lstApm = new ArrayList<>();
        AccountPosting_Mdl apm;
        String sqlQuery = "", expenseRevenueSql = "", profitLossAcSql = "", baseSql = "";
        boolean flag = false;
        int sr = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="Prepare Account Posting SQL">
            ++sr;
            expenseRevenueSql = "SELECT lg.subcode \n"
                    + "		-- Expense Revenue A/c Posting \n"
                    + "	, " + contraCode + " AS contraSub \n"
                    + "	, -(ISNULL(SUM(lg.amtDr), 0) - ISNULL(SUM(lg.amtCr), 0)) amount \n"
                    + "	, " + sr + " AS sr \n"
                    + "FROM ledger lg \n"
                    + "LEFT JOIN subGroup sg on sg.subCode=lg.subcode \n"
                    + "LEFT JOIN acGroup acg on acg.groupCode=sg.groupCode \n"
                    + "WHERE acg.groupNature IN (2,3,4,5) \n"
                    + "		AND lg.isPosted = 1 \n"
                    + "		AND lg.site_Code = :site_Code \n"
                    + "		AND lg.v_Prefix = :v_Prefix \n"
                    + "GROUP BY lg.subcode \n"
                    + "HAVING (ISNULL(SUM(lg.amtDr), 0) - ISNULL(SUM(lg.amtCr), 0))  <> 0 \n";
            ++sr;
            profitLossAcSql = "SELECT t.subCode\n"
                    + "		, CASE WHEN ISNULL(SUM(t.amount), 0) > 0 THEN MAX(t.drContraSub) \n"
                    + "				WHEN ISNULL(SUM(t.amount), 0) < 0 THEN MAX(t.crContraSub)\n"
                    + "				END AS contraSub \n"
                    + "		, SUM(t.amount) AS amount, " + sr + " AS sr \n"
                    + "FROM ( \n"
                    + "		-- PL A/c Posting \n"
                    + "		SELECT " + contraCode + " AS subCode \n"
                    + "			, (ISNULL(SUM(lg.amtDr), 0) - ISNULL(SUM(lg.amtCr), 0)) amount \n"
                    + "			, CASE WHEN ((ISNULL(SUM(lg.amtDr), 0) - ISNULL(SUM(lg.amtCr), 0))) > 0 THEN lg.subCode ELSE NULL END AS drContraSub \n"
                    + "			, CASE WHEN ((ISNULL(SUM(lg.amtDr), 0) - ISNULL(SUM(lg.amtCr), 0))) < 0 THEN lg.subCode ELSE NULL END AS crContraSub \n"
                    + "		FROM ledger lg \n"
                    + "		LEFT JOIN subGroup sg on sg.subCode=lg.subcode \n"
                    + "		LEFT JOIN acGroup acg on acg.groupCode=sg.groupCode \n"
                    + "		WHERE acg.groupNature IN (2,3,4,5) \n"
                    + "				AND lg.isPosted = 1  \n"
                    + "				AND lg.site_Code = :site_Code \n"
                    + "				AND lg.v_Prefix = :v_Prefix \n"
                    + "		GROUP BY lg.subcode \n"
                    + "		HAVING (ISNULL(SUM(lg.amtDr), 0) - ISNULL(SUM(lg.amtCr), 0))  <> 0 \n"
                    + "	) t \n"
                    + "GROUP BY t.subCode \n";

            baseSql = expenseRevenueSql
                    + "UNION ALL\n"
                    + "\n"
                    + profitLossAcSql;

            sqlQuery = "SELECT CAST(SUM(t.amount) AS DECIMAL(18,2)) AS amount\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("v_Prefix", DmShared.xNull(vPrefix));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() != 0) {
                    throw new DmException(0, "Error in ledger posting.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT t.subCode, t.contraSub, t.amount, sgc.name as contraSubName\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n"
                    + "LEFT JOIN subGroup sgc ON sgc.subCode = t.contraSub\n"
                    + "ORDER BY sr";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("v_Prefix", DmShared.xNull(vPrefix));
            rsTemp = psTemp.executeQuery();
            //</editor-fold>

            flag = false;
            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("subCode")).longValue() > 0) {
                    if (!flag) {
                        flag = true;
                    }
                    apm = new AccountPosting_Mdl();
                    apm.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                    apm.setContraCode(DmShared.xNull(rsTemp.getString("contraSub")));
                    apm.setContraText(DmShared.xNull(rsTemp.getString("contraSubName")));
                    if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() > 0) {
                        apm.setAmtDr(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                        apm.setAmtCr("");
                    } else if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() < 0) {
                        apm.setAmtCr(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                        apm.setAmtDr("");
                    }
                    lstApm.add(apm);
                }
            }
            rsTemp.close();
            psTemp.close();

            if (!flag) {
                throw new DmException(0, "Error in ledger posting.");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return lstApm;
    }
    //</editor-fold>

}
