package dataman.accounts.tdsCategoryMaster.server;

import dataman.dmBase.self.shared.*;
import dataman.accounts.tdsCategoryMaster.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TdsCategoryMaster_SrvImpl extends DmRSSPrivate implements TdsCategoryMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstKeyField = new ArrayList<>();
        KeyFieldExtended lstKeyFieldTmp;
        dmrTemp.setData(lstKeyField);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("SELECT CODE  FROM TDSCat Order By Name");

            while (rsTemp.next()) {
                lstKeyFieldTmp = new KeyFieldExtended();
                lstKeyFieldTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                lstKeyField.add(lstKeyFieldTmp);
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
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Code, Name From tdscat ");
            helper.addSort("Name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResult<TdsCategoryMaster_Mdl> getMoveRec(String strKey) {
        DmResult<TdsCategoryMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        TdsCategoryMaster_Mdl mrTemp = new TdsCategoryMaster_Mdl();

        List<GrdTdsCategory_Mdl> lstTemp = new ArrayList<>();

        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "Select tdsc.Code, max(tdsc.Name) as Name ,max( tdsc.preparedBy) as preparedBy ,max( tdsc.u_Name) as u_Name ,"
                    + "  max(tcd.applicableFrom) applicableFrom"
                    + ", max(tcd.applicableTo) applicableTo,max(tdsc.type) type,"
                    + "  max(section) section, max(tdsFor) tdsFor,max(tdsc.tdsOnPreviousTransactions) as tdsOnPreviousTransactions "
                    + " From tdscat tdsc "
                    + " left join tdscat_Det tcd on tcd.code = tdsc.code "
                    + "   left join ( "
                    + "         select  max(td.applicableFrom) applicableFrom"
                    + "                    , td.code"
                    + "                     from tdscat_Det td"
                    + "                     where td.code = :code "
                    + "                     and td.applicableFrom <= cast(:uEntDt as date)"
                    + "                     group by td.code"
                    + "	) id on id.code = tcd.code "
                    + "  Where tdsc.code = :code "
                    + "         group by tdsc.code "
                    + "         order by name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                mrTemp.setTypeCode(DmShared.xNull(rsTemp.getString("type")));
                mrTemp.setType(DmEnum.TDS_TCS_Type.getViaCode(DmShared.xNull(rsTemp.getString("type"))).getName());
                mrTemp.setSection(DmShared.xNull(rsTemp.getString("section")));
                mrTemp.setTdsForTag(DmShared.xNull(rsTemp.getString("tdsFor")));
                mrTemp.setTdsForText(DmShared.xNull(rsTemp.getString("tdsFor")).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(rsTemp.getString("tdsFor")).equalsIgnoreCase("R") ? "Receivable" : "");
                mrTemp.setTdsOnPreviousTransactionsTag(DmShared.xNull(rsTemp.getString("tdsOnPreviousTransactions")));
                mrTemp.setTdsOnPreviousTransactionsText(DmShared.xNull(rsTemp.getString("tdsOnPreviousTransactions")).trim().equalsIgnoreCase("1")?"Yes":"No");
            }
            psTemp.close();
            rsTemp.close();

//            strSQL = "SELECT  TD.FormulaString,TD.CODE AS CODE,T.Code"
//                    + " ,TD.SrNo,SG.Name AS AcName,TD.Percentage,TD.TDSDesc"
//                    + " ,TCD.Name AS Desc_Name,"
//                    + " TCD.manualCode,TD.AcCode "
//                    + " FROM  TDSCat_Det AS TD LEFT JOIN "
//                    + " SubGroup AS SG ON SG.SubCode=TD.AcCode LEFT JOIN "
//                    + " TDSCat AS T ON T.Code=TD.Code"
//                    + " LEFT JOIN  TDSCat_Description AS TCD ON TCD.Code =TD.TDSDesc "
//                    + " Where T.Code= :code";
//
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//            psTemp.setString("code", DmShared.iNull(strKey).toString());
//            rsTemp = psTemp.executeQuery();
            strSQL = "SELECT  TD.FormulaString,TD.CODE AS CODE,T.Code"
                    + " ,TD.SrNo,SG.Name AS AcName,TD.Percentage,TD.TDSDesc"
                    + " ,TCD.Name AS Desc_Name,"
                    + " TCD.manualCode,TD.AcCode, fromAmt, toAmt, penalityPercentage,initialTurnoverAmt    "
                    + " FROM  TDSCat_Det AS TD LEFT JOIN "
                    + " SubGroup AS SG ON SG.SubCode=TD.AcCode LEFT JOIN "
                    + " TDSCat AS T ON T.Code=TD.Code"
                    + " LEFT JOIN  TDSCat_Description AS TCD ON TCD.Code =TD.TDSDesc "
                    + " where TD.code =:code and TD.applicableFrom =:applicableFrom ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdTdsCategory_Mdl objListTemp = new GrdTdsCategory_Mdl();
                objListTemp.setDescription(DmShared.xNull(rsTemp.getString("Desc_Name")));
                objListTemp.setDescriptionCode(DmShared.xNull(rsTemp.getString("TDSDesc")));
                objListTemp.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                objListTemp.setPostingAccountCode(DmShared.xNull(rsTemp.getString("AcCode")));
                objListTemp.setPostingAccount(DmShared.xNull(rsTemp.getString("AcName")));
                objListTemp.setPercent(DmShared.xNull(rsTemp.getString("Percentage")));
                objListTemp.setCalculateOn(DmShared.xNull(rsTemp.getString("FormulaString")));
                objListTemp.setFromAmt(DmShared.xNull(rsTemp.getString("fromAmt")));
                objListTemp.setToAmt(DmShared.xNull(rsTemp.getString("toAmt")));
                objListTemp.setPenalityPercentage(DmShared.xNull(rsTemp.getString("penalityPercentage")));
                objListTemp.setInitialTurnoverAmount(DmShared.xNull(rsTemp.getString("initialTurnoverAmt")));
                lstTemp.add(objListTemp);
            }
            if (lstTemp.isEmpty()) {
                GrdTdsCategory_Mdl ObjListTemp = new GrdTdsCategory_Mdl();
                ObjListTemp.setDescription("");
                ObjListTemp.setDescriptionCode("");
                ObjListTemp.setManualCode("");
                ObjListTemp.setPostingAccountCode("");
                ObjListTemp.setPostingAccount("");
                ObjListTemp.setPercent("");
                ObjListTemp.setCalculateOn("");
                ObjListTemp.setFromAmt("");
                ObjListTemp.setToAmt("");
                ObjListTemp.setPenalityPercentage("");
                lstTemp.add(ObjListTemp);
                mrTemp.getGrdData().addAll(lstTemp);
            } else {
                mrTemp.getGrdData().addAll(lstTemp);
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
    public DmResult<String> saveRecord(TdsCategoryMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL;
        String maxApplicableDate = "", applicableTo = "31-Dec-9999";
        ResultSet rsTemp = null, rsTemp1 = null;
        int sNo_tdsCatDetail = 0;
        dmrTemp.setData("");

        try {
            TdsCategoryMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            strSQL = "Select Count(Name) As Cnt "
                    + " From TDSCAT TD "
                    + " Where TD.Name= :name ";
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                strSQL += " And TD.Code <> :code ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("name", mrTemp.getSharedKeyFieldName());
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            }
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("TDS Description Already Exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "TdsCat", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            spBegin = new DmSavePoint(cnTemp);
            // insert and Update Tds Applicable From Modle

            strSQL = "select MAX(id.applicableFrom) applicableFromMax, MIN(applicableFrom) applicableFromMin "
                    + "	from tdscat_Det id "
                    + "	where id.code = :code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFromMax"));
            }
            rsTemp.close();
            psTemp.close();

            if (!maxApplicableDate.equalsIgnoreCase("")) {
                if (DmServer.convertDate(mrTemp.getApplicableFrom()).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                    strSQL = "select applicableFrom , applicableTo "
                            + " from tdscat_Det "
                            + " where code = :code "
                            + " and applicableFrom < :applicableFromDate "
                            + " group by applicableFrom,applicableTo "
                            + " order by applicableFrom desc "
                            + " offset 0 rows fetch next 1 rows only ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
                        strSQL = " update tdscat_Det set "
                                + " applicableTo = :applicableToDate "
                                + " where applicableFrom = :applicableFromDate "
                                + " and code = :code ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp1.setDate("applicableToDate", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                        psTemp1.setDate("applicableFromDate", rsTemp.getDate("applicableFromDate"));
                        psTemp1.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp1.executeUpdate();
                        psTemp1.close();

                    } else {
                        strSQL = "select applicableFrom, applicableTo "
                                + "FROM tdscat_Det "
                                + "where applicableFrom > :applicableFromDate "
                                + "and code = :code "
                                + "group by applicableFrom, applicableTo "
                                + "order by applicableFrom asc offset 0 rows fetch next 1 rows only ";
                        // + "LIMIT 1 ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                        psTemp1.setDate("applicableFromDate", DmServer.convertDate(mrTemp.getApplicableFrom()));
                        psTemp1.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()).toString());
                        rsTemp1 = psTemp1.executeQuery();
                        if (rsTemp1.next()) {
                            applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFrom")));

                        }
                        psTemp1.close();
                        rsTemp1.close();
                    }

                    psTemp.close();
                    rsTemp.close();
                } else {
                    strSQL = "update tdscat_Det set "
                            + " applicableTo = :applicableToDate "
                            + " where applicableFrom = :applicableFromDate and code = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setDate("applicableFromDate", DmServer.convertDate(maxApplicableDate));
                    psTemp.setDate("applicableToDate", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            // Insert Record in TDSCAT Table 
            /////////////////////////////////////////////////////////////////////////////////////
            if (blnAdd) {
                strSQL = "Insert Into tdscat(code,name, "
                        + "preparedBy, "
                        + "U_EntDt ,type, section, tdsFor,tdsOnPreviousTransactions) Values  "
                        + "(:code,:name,:u_Name,:u_EntDt,:type,:section,:tdsFor,:tdsOnPreviousTransactions ) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("type", DmShared.xNull(mrTemp.getTypeCode()));
                psTemp.setString("section", DmShared.xNull(mrTemp.getSection()));
                psTemp.setString("tdsFor", DmShared.xNull(mrTemp.getTdsForTag()));
                psTemp.setString("tdsOnPreviousTransactions", DmShared.xNull(mrTemp.getTdsOnPreviousTransactionsTag()));
                //  psTemp.setString("msmeNumber", DmShared.xNull(mrTemp.getMsmeNumber()));

            } else {
                strSQL = "Update tdscat Set "
                        + "Name = :name, "
                        + "transferDt = null, "
                        + "U_Name = :uName, "
                        + "U_EntDt = :u_EntDt, "
                        + "type = :type, "
                        + " Section = :section, "
                        + " tdsFor = :tdsFor, "
                        + " tdsOnPreviousTransactions=:tdsOnPreviousTransactions "
                        + "Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("type", DmShared.xNull(mrTemp.getTypeCode()));
                psTemp.setString("section", DmShared.xNull(mrTemp.getSection()));
                psTemp.setString("tdsFor", DmShared.xNull(mrTemp.getTdsForTag()));
                psTemp.setString("tdsOnPreviousTransactions", DmShared.xNull(mrTemp.getTdsOnPreviousTransactionsTag()));
            }
            psTemp.executeUpdate();
            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////

            //Delete tdscat_DET Record
            /////////////////////////////////////////////////////////////////////////////////////
            strSQL = "Delete From tdscat_DET where Code= :code"
                    + "  and applicableFrom = :applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("applicableFrom", DmShared.xNull(mrTemp.getApplicableFrom()));
            psTemp.executeUpdate();
            psTemp.close();
            /////////////////////////////////////////////////////////////////////////////////////

            if (!blnAdd) {
                strSQL = " Select max(srNo) SrNo from TDSCat_Det where code=:code     ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    sNo_tdsCatDetail = DmShared.iNull(rsTemp.getString("SrNo")).intValue();
                }
            }

            int grdCount = mrTemp.getGrdData().size();
            int srNo = sNo_tdsCatDetail == 0 ? 0 : sNo_tdsCatDetail;
            for (int i = 0; i < grdCount; i++) {
                GrdTdsCategory_Mdl gtc = mrTemp.getGrdData().get(i);
                if (!DmShared.xNull(gtc.getDescriptionCode()).equalsIgnoreCase("")) {
                    strSQL = "Insert Into TDSCat_Det(code,SrNo, "
                            + "TDSDesc,AcCode,Percentage,FormulaString, "
                            + "U_Name,"
                            + "U_EntDt,applicableFrom,applicableTo, fromAmt, toAmt, penalityPercentage,initialTurnoverAmt ) Values  "
                            + "(:code,:srNo,:tDSDesc,:acCode,:percentage,:formulaString,:u_Name,:u_EntDt,"
                            + " :applicableFrom,:applicableTo, :fromAmt, :toAmt, :penalityPercentage,:initialTurnoverAmt) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("srNo", Integer.toString(++srNo));
                    psTemp.setString("tDSDesc", DmShared.foriegnKey(gtc.getDescriptionCode()));
                    psTemp.setString("acCode", DmShared.foriegnKey(gtc.getPostingAccountCode()));
                    psTemp.setString("percentage", DmServer.vNull(gtc.getPercent(), "0.00"));
                    psTemp.setString("formulaString", DmShared.xNull(gtc.getCalculateOn()));
                    psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setDate("applicableTo", DmServer.convertDate(applicableTo));
                    psTemp.setString("fromAmt", DmServer.vNull(gtc.getFromAmt(), "0.00"));
                    psTemp.setString("toAmt", DmServer.vNull(gtc.getToAmt(), "0.00"));
                    psTemp.setString("penalityPercentage", DmServer.vNull(gtc.getPenalityPercentage(), "0.00"));
                    psTemp.setString("initialTurnoverAmt", DmServer.vNull(mrTemp.getInitialTurnoverAmount(), "0.00"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

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
            strSQL = "SELECT COUNT(cnt) AS cnt FROM "
                    + "    (SELECT  td.applicableFrom AS cnt FROM  tdscat_Det td "
                    + "         WHERE td.code = :code GROUP BY td.applicableFrom ) AS tab ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 1) {
                    throw new UnsupportedOperationException("Record have multiple TDS List. Could not delete this record.");
                }
            }
            rsTemp.close();

            strSQL = "Delete From TDSCat_Det Where Code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From tdscat Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Code, Name,type,Section,tdsFor From tdscat");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setType(DmEnum.TDS_TCS_Type.getViaCode(DmShared.xNull(rsTemp.getString("type"))).getName());
                listNode.setSection(DmShared.xNull(rsTemp.getString("Section")));
                listNode.setTdsFor(DmShared.xNull(rsTemp.getString("tdsFor")).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(rsTemp.getString("tdsFor")).equalsIgnoreCase("R") ? "Receivable" : "");

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDescription(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select Code,Name,manualCode From TDSCat_Description ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<TxtHPostingAC_Mdl>> getTxtHPostingAC(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPostingAC_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPostingAC_Mdl> lstTemp = new ArrayList<>();
        TxtHPostingAC_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            sqlWithParam.setSQL("Select SubCode,Name,ManualCode From Subgroup ");

            sqlQuery = "SELECT SG.SubCode ,SG.Name,SG.ManualCode "
                    + "FROM SubGroup SG "
                    + "left join subGroupNature as sgn on sg.nature = sgn.code "
                    + "where SG.isActive = 1 and sgn.manualCode not in ('BK','CS') ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHPostingAC_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCopyFromTDS(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT tc.code, MAX(tc.name) AS name"
                    + " ,tcd.applicableFrom, max(tc.type) as type \n"
                    + "  FROM tdsCat tc \n"
                    + "  INNER JOIN tdsCat_Det tcd ON tcd.code = tc.code \n"
                    + "  GROUP BY tc.code, tcd.applicableFrom ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                if (!DmShared.xNull(rsTemp.getString("type")).equalsIgnoreCase("")) {
                    listNode.setSharedModifiedBy(DmEnum.TDS_TCS_Type.getViaCode(DmShared.xNull(rsTemp.getString("type"))).getName());
                }

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
    public DmResult<List<KeyFieldExtended>> fillGridApplicableDate(String code) {
        DmResult<List<KeyFieldExtended>> rstTemp = new DmResult<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sql = "";
        DmPreparedStatement psTemp = null;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sql = " SELECT MAX(code) AS code, applicableFrom \n"
                    + " , MAX(applicableTo) AS applicableTo \n"
                    + " FROM tdscat_Det \n"
                    + " WHERE (code = :code) "
                    + " GROUP BY applicableFrom ORDER BY applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("code", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableTo")));
                lstTemp.add(listNode);
            }
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
    public DmResult<KeyFieldExtended> getMaxApplicableDate(String code) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended listNode = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sql = "";
        DmPreparedStatement psTemp = null;
        try {
            rstTemp.setData(listNode);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sql = " select top 1  tds.code, max(tdsd.applicableFrom) as applicableFrom, MAX( tds.name) AS name "
                    + " FROM tdscat tds"
                    + " LEFT JOIN tdscat_Det tdsd ON tdsd.code =  tds.code "
                    + " WHERE ( tds.code = :code) "
                    + " GROUP BY  tds.code, tdsd.applicableFrom "
                    + " ORDER BY tdsd.applicableFrom desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("code", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.dNull(rsTemp.getDate("applicableFrom")));

            }
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
    public DmResult<String> deleteRecordApplicable(String strKey, String applicableDate) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL, applicableToDate = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = " SELECT applicableFrom , applicableTo  FROM tdscat_Det WHERE code= :code and applicableFrom= :applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                applicableToDate = rsTemp.getString("applicableTo");
            } else {
                throw new UnsupportedOperationException("ApplicableFrom Date is Invalid");
            }

            strSQL = " SELECT code, applicableFrom, applicableTo FROM tdscat_Det "
                    + " WHERE code= :code AND applicableFrom < :applicableFrom "
                    + " ORDER BY applicableFrom DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                strSQL = "update tdscat_Det set applicableTo=:applicableTo "
                        + " where code = :code and ApplicableFrom = :ApplicableFrom ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("applicableTo", applicableToDate);
                psTemp.setString("code", DmShared.iNull(strKey).toString());
                psTemp.setString("ApplicableFrom", rsTemp.getString("ApplicableFrom"));
                psTemp.executeUpdate();
                psTemp.close();
                rsTemp.close();
            }

            strSQL = " delete from tdscat_Det "
                    + " where code=:code and applicableFrom= :applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            psTemp.executeUpdate();

            rsTemp.close();
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
    public DmResult<List<GrdTdsCategory_Mdl>> fillGrid(String code, String applicableFromDate) {
        DmResult<List<GrdTdsCategory_Mdl>> rstTemp = new DmResult<>();
        List<GrdTdsCategory_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp.addAll(fillTDSGrid(code, applicableFromDate, cnTemp));
            rstTemp.setData(lstTemp);
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private List<GrdTdsCategory_Mdl> fillTDSGrid(String keyCode, String applicableDate, Connection cnTemp) {
        List<GrdTdsCategory_Mdl> lstTemp = new ArrayList<>();
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        try {

            sqlQuery = "SELECT  TD.FormulaString,TD.CODE AS CODE,T.Code"
                    + " ,TD.SrNo,SG.Name AS AcName,TD.Percentage,TD.TDSDesc"
                    + " ,TCD.Name AS Desc_Name,"
                    + " TCD.manualCode,TD.AcCode, TD.fromAmt, TD.toAmt, TD.penalityPercentage "
                    + " FROM  TDSCat_Det AS TD LEFT JOIN "
                    + " SubGroup AS SG ON SG.SubCode=TD.AcCode LEFT JOIN "
                    + " TDSCat AS T ON T.Code=TD.Code"
                    + " LEFT JOIN  TDSCat_Description AS TCD ON TCD.Code =TD.TDSDesc "
                    + " where TD.code=:code and TD.applicableFrom =:applicableFrom  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(keyCode).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdTdsCategory_Mdl objListTemp = new GrdTdsCategory_Mdl();
                objListTemp.setDescription(DmShared.xNull(rsTemp.getString("Desc_Name")));
                objListTemp.setDescriptionCode(DmShared.xNull(rsTemp.getString("TDSDesc")));
                objListTemp.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                objListTemp.setPostingAccountCode(DmShared.xNull(rsTemp.getString("AcCode")));
                objListTemp.setPostingAccount(DmShared.xNull(rsTemp.getString("AcName")));
                objListTemp.setPercent(DmShared.xNull(rsTemp.getString("Percentage")));
                objListTemp.setCalculateOn(DmShared.xNull(rsTemp.getString("FormulaString")));
                objListTemp.setFromAmt(DmShared.xNull(rsTemp.getString("fromAmt")));
                objListTemp.setToAmt(DmShared.xNull(rsTemp.getString("toAmt")));
                objListTemp.setPenalityPercentage(DmShared.xNull(rsTemp.getString("penalityPercentage")));
                lstTemp.add(objListTemp);
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
            } catch (Exception e) {
            }
        }
        return lstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (DmEnum.TDS_TCS_Type tdtc : DmEnum.TDS_TCS_Type.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode((tdtc.getCode()));
                listNode.setSharedKeyFieldName(tdtc.getName());
                lstTemp.add(listNode);
            }
            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;

    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsFor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Payable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("R");
            listNode.setSharedKeyFieldName("Receivable");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsOnPreviousTransactions(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }
}
