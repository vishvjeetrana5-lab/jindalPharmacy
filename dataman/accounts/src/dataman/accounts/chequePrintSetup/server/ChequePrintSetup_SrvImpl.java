package dataman.accounts.chequePrintSetup.server;

import dataman.accounts.chequePrintSetup.shared.*;
import dataman.accounts.chequePrintSetup.shared.Find_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChequePrintSetup_SrvImpl extends DmRSSPrivate implements ChequePrintSetup_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("Select cps.Code  From chequePrintingSetup as cps "
                    + " Left join subgroup as sg on cps.bankCode =sg.subCode  "
                    + " Order By sg.name ");
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
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
    public DmResult<ChequePrintSetup_Mdl> getMoveRec(String strKey, String bankCode, boolean isCopy) {
        DmResult<ChequePrintSetup_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        ChequePrintSetup_Mdl mrTemp = new ChequePrintSetup_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "  SELECT "
                    + "   cps.code"
                    + " , cps.bankCode"
                    + " , sg.name as bankName"
                    + " , cps.chequeDateTop"
                    + " , cps.chequeDateLeft"
                    + " , cps.favourOfTop"
                    + " , cps.favourOfLeft"
                    + " , cps.AmtInWordsTop"
                    + " , cps.AmtInWordsLeft"
                    + " , cps.AmtTop"
                    + " , cps.AmtLeft"
                    + " , cps.crossChequeTop"
                    + " , cps.crossChequeLeft"
                    + " , cps.authSignatory"
                    + " , cps.authSignatoryTop"
                    + " , cps.authSignatoryLeft"
                    + " , cps.companyName"
                    + " , cps.companyNameTop"
                    + " , cps.companyNameLeft"
                    + " , cps.paperWidth"
                    + " , cps.paperHeight"
                    + " , cps.u_Name"
                    + " , cps.preparedBy "
                    + " FROM chequePrintingSetup as cps "
                    + " LEFT JOIN subgroup AS sg ON cps.bankCode=sg.subCode ";
            if (isCopy) {
                strSQL += " where cps.bankCode= :bankCode ";
            } else {
                strSQL += " where cps.code= :code ";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            if (isCopy) {
                psTemp.setString("bankCode", DmShared.iNull(bankCode).toString());
            } else {
                psTemp.setString("code", strKey);
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                mrTemp.setBankCode(DmShared.xNull(rsTemp.getString("bankCode")));
                mrTemp.setBankName(DmShared.xNull(rsTemp.getString("bankName")));
                mrTemp.setChequeDateTop(DmServer.vNull(rsTemp.getString("chequeDateTop"), "0.00"));
                mrTemp.setChequeDateLeft(DmServer.vNull(rsTemp.getString("chequeDateLeft"), "0.00"));
                mrTemp.setFavourOfTop(DmServer.vNull(rsTemp.getString("favourOfTop"), "0.00"));
                mrTemp.setFavourOfLeft(DmServer.vNull(rsTemp.getString("favourOfLeft"), "0.00"));
                mrTemp.setAmtInWordsTop(DmServer.vNull(rsTemp.getString("AmtInWordsTop"), "0.00"));
                mrTemp.setAmtInWordsLeft(DmServer.vNull(rsTemp.getString("AmtInWordsLeft"), "0.00"));
                mrTemp.setAmtTop(DmServer.vNull(rsTemp.getString("AmtTop"), "0.00"));
                mrTemp.setAmtLeft(DmServer.vNull(rsTemp.getString("AmtLeft"), "0.00"));
                mrTemp.setAuthSign(DmShared.xNull(rsTemp.getString("authSignatory")));
                mrTemp.setAuthSignTop(DmServer.vNull(rsTemp.getString("authSignatoryTop"), "0.00"));
                mrTemp.setAuthSignLeft(DmServer.vNull(rsTemp.getString("authSignatoryLeft"), "0.00"));
                mrTemp.setCompanyName(DmShared.xNull(rsTemp.getString("companyName")));
                mrTemp.setCompanyNameTop(DmServer.vNull(rsTemp.getString("companyNameTop"), "0.00"));
                mrTemp.setCompanyNameLeft(DmServer.vNull(rsTemp.getString("companyNameLeft"), "0.00"));
                mrTemp.setCrossChequeTop(DmServer.vNull(rsTemp.getString("crossChequeTop"), "0.00"));
                mrTemp.setCrossChequeLeft(DmServer.vNull(rsTemp.getString("crossChequeLeft"), "0.00"));
                mrTemp.setPaperWidth(DmServer.vNull(rsTemp.getString("paperWidth"), "0.00"));
                mrTemp.setPaperHeight(DmServer.vNull(rsTemp.getString("paperHeight"), "0.00"));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
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

            strSQL = "Delete From chequePrintingSetup Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
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
    public DmResult<String> saveRecord(ChequePrintSetup_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            ChequePrintSetup_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "chequePrintingSetup", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            strSQL = " SELECT COUNT(bankCode) AS cnt FROM chequePrintingSetup  "
                    + " WHERE LOWER(bankCode) = :bankCode AND Code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("bankCode", DmShared.xNull(mrTemp.getBankCode()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(100, " Bank Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {

                strSQL = " INSERT INTO "
                        + " chequePrintingSetup"
                        + " ("
                        + "       code"
                        + "     , bankCode"
                        + "     , chequeDateTop"
                        + "     , chequeDateLeft"
                        + "     , favourOfTop"
                        + "     , favourOfLeft"
                        + "     , crossChequeTop"
                        + "     , crossChequeLeft"
                        + "     , AmtInWordsTop"
                        + "     , AmtInWordsLeft"
                        + "     , AmtTop"
                        + "     , AmtLeft"
                        + "     , authSignatory"
                        + "     , authSignatoryTop"
                        + "     , authSignatoryLeft"
                        + "     , companyName"
                        + "     , companyNameTop"
                        + "     , companyNameLeft"
                        + "     , paperWidth"
                        + "     , paperHeight"
                        + "     , preparedBy"
                        + "     , U_EntDt"
                        + "     , transferDt"
                        + " ) VALUES  "
                        + " ("
                        + "       :code"
                        + "     , :bankCode"
                        + "     , :chequeDateTop"
                        + "     , :chequeDateLeft"
                        + "     , :favourOfTop"
                        + "     , :favourOfLeft"
                        + "     , :crossChequeTop"
                        + "     , :crossChequeLeft"
                        + "     , :AmtInWordsTop "
                        + "     , :AmtInWordsLeft"
                        + "     , :AmtTop"
                        + "     , :AmtLeft"
                        + "     , :authSignatory"
                        + "     , :authSignatoryTop"
                        + "     , :authSignatoryLeft"
                        + "     , :companyName"
                        + "     , :companyNameTop "
                        + "     , :companyNameLeft"
                        + "     , :paperWidth"
                        + "     , :paperHeight"
                        + "     , :uName"
                        + "     , :uEntDt "
                        + "     , NULL) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("bankCode", DmShared.foriegnKey(mrTemp.getBankCode()));
                psTemp.setString("chequeDateTop", DmServer.vNull(mrTemp.getChequeDateTop(), "0.00"));
                psTemp.setString("chequeDateLeft", DmServer.vNull(mrTemp.getChequeDateLeft(), "0.00"));
                psTemp.setString("favourOfTop", DmServer.vNull(mrTemp.getFavourOfTop(), "0.00"));
                psTemp.setString("favourOfLeft", DmServer.vNull(mrTemp.getFavourOfLeft(), "0.00"));
                psTemp.setString("crossChequeTop", DmServer.vNull(mrTemp.getCrossChequeTop(), "0.00"));
                psTemp.setString("crossChequeLeft", DmServer.vNull(mrTemp.getCrossChequeLeft(), "0.00"));
                psTemp.setString("AmtInWordsTop", DmServer.vNull(mrTemp.getAmtInWordsTop(), "0.00"));
                psTemp.setString("AmtInWordsLeft", DmServer.vNull(mrTemp.getAmtInWordsLeft(), "0.00"));
                psTemp.setString("AmtTop", DmServer.vNull(mrTemp.getAmtTop(), "0.00"));
                psTemp.setString("AmtLeft", DmServer.vNull(mrTemp.getAmtLeft(), "0.00"));
                psTemp.setString("authSignatory", DmShared.xNull(mrTemp.getAuthSign()));
                psTemp.setString("authSignatoryTop", DmServer.vNull(mrTemp.getAuthSignTop(), "0.00"));
                psTemp.setString("authSignatoryLeft", DmServer.vNull(mrTemp.getAuthSignLeft(), "0.00"));
                psTemp.setString("companyName", DmShared.xNull(mrTemp.getCompanyName()));
                psTemp.setString("companyNameTop", DmServer.vNull(mrTemp.getCompanyNameTop(), "0.00"));
                psTemp.setString("companyNameLeft", DmServer.vNull(mrTemp.getCompanyNameLeft(), "0.00"));
                psTemp.setString("paperWidth", DmServer.vNull(mrTemp.getPaperWidth(), "0.00"));
                psTemp.setString("paperHeight", DmServer.vNull(mrTemp.getPaperHeight(), "0.00"));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.executeUpdate();
                psTemp.close();

            } else {

                strSQL = " Update chequePrintingSetup Set "
                        + "	bankCode = :bankCode,"
                        + "	chequeDateTop = :chequeDateTop,"
                        + "	chequeDateLeft = :chequeDateLeft,"
                        + "	favourOfTop = :favourOfTop,"
                        + "	favourOfLeft = :favourOfLeft,"
                        + "	AmtInWordsTop = :AmtInWordsTop,"
                        + "	AmtInWordsLeft = :AmtInWordsLeft,"
                        + "     crossChequeTop=:crossChequeTop,"
                        + "     crossChequeLeft=:crossChequeLeft,"
                        + "	AmtTop = :AmtTop,"
                        + "	AmtLeft = :AmtLeft,"
                        + "	authSignatory = :authSignatory,"
                        + "	authSignatoryTop = :authSignatoryTop,"
                        + "	authSignatoryLeft = :authSignatoryLeft,"
                        + "	companyName = :companyName,"
                        + "	companyNameTop = :companyNameTop,"
                        + "	companyNameLeft = :companyNameLeft,"
                        + "	paperWidth = :paperWidth,"
                        + "	paperHeight = :paperHeight,"
                        + "     U_Name = :uName, "
                        + "     U_EntDt = GETDATE() "
                        + "  Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("bankCode", DmShared.foriegnKey(mrTemp.getBankCode()));
                psTemp.setString("chequeDateTop", DmServer.vNull(mrTemp.getChequeDateTop(), "0.00"));
                psTemp.setString("chequeDateLeft", DmServer.vNull(mrTemp.getChequeDateLeft(), "0.00"));
                psTemp.setString("favourOfTop", DmServer.vNull(mrTemp.getFavourOfTop(), "0.00"));
                psTemp.setString("favourOfLeft", DmServer.vNull(mrTemp.getFavourOfLeft(), "0.00"));
                psTemp.setString("AmtInWordsTop", DmServer.vNull(mrTemp.getAmtInWordsTop(), "0.00"));
                psTemp.setString("AmtInWordsLeft", DmServer.vNull(mrTemp.getAmtInWordsLeft(), "0.00"));
                psTemp.setString("crossChequeTop", DmServer.vNull(mrTemp.getCrossChequeTop(), "0.00"));
                psTemp.setString("crossChequeLeft", DmServer.vNull(mrTemp.getCrossChequeLeft(), "0.00"));
                psTemp.setString("AmtTop", DmServer.vNull(mrTemp.getAmtTop(), "0.00"));
                psTemp.setString("AmtLeft", DmServer.vNull(mrTemp.getAmtLeft(), "0.00"));
                psTemp.setString("authSignatory", DmShared.xNull(mrTemp.getAuthSign()));
                psTemp.setString("authSignatoryTop", DmServer.vNull(mrTemp.getAuthSignTop(), "0.00"));
                psTemp.setString("authSignatoryLeft", DmServer.vNull(mrTemp.getAuthSignLeft(), "0.00"));
                psTemp.setString("companyName", DmShared.xNull(mrTemp.getCompanyName()));
                psTemp.setString("companyNameTop", DmServer.vNull(mrTemp.getCompanyNameTop(), "0.00"));
                psTemp.setString("companyNameLeft", DmServer.vNull(mrTemp.getCompanyNameLeft(), "0.00"));
                psTemp.setString("paperWidth", DmServer.vNull(mrTemp.getPaperWidth(), "0.00"));
                psTemp.setString("paperHeight", DmServer.vNull(mrTemp.getPaperHeight(), "0.00"));
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.executeUpdate();
                psTemp.close();

            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
            sqlWithParam.setSQL(" SELECT cps.Code,sg.name AS bankName ,cps.bankCode,cps.authSignatory as authSign, cps.companyName, "
                    + " cps.paperWidth,cps.paperHeight "
                    + " FROM chequePrintingSetup AS cps "
                    + " LEFT JOIN subgroup AS sg ON cps.bankCode=sg.subCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setBankCode(DmShared.xNull(rsTemp.getString("bankCode")));
                listNode.setBankName(DmShared.xNull(rsTemp.getString("bankName")));
                listNode.setAuthSign(DmShared.xNull(rsTemp.getString("authSign")));
                listNode.setCompanyName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setPaperWidth(DmServer.vNull(rsTemp.getString("paperWidth"), "0.00"));
                listNode.setPaperHeight(DmServer.vNull(rsTemp.getString("paperHeight"), "0.00"));
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
    public DmResultPageing<List<BankName_Mdl>> getTxtHBankName(String docId, boolean isCopy, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<BankName_Mdl>> rstTemp = new DmResultPageing<>();
        List<BankName_Mdl> lstTemp = new ArrayList<>();
        BankName_Mdl listNode;
        Connection cnTemp = null;
        String strSql = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSql = " SELECT "
                    + "   sg.subCode"
                    + " , sg.name as bankName "
                    + " , sg.manualCode "
                    + " , sg.cityCode "
                    + " , c.cityName "
                    + "   FROM subgroup as sg "
                    + " LEFT JOIN subgroupNature as sgn on sg.nature = sgn.code "
                    + " LEFT JOIN city c on c.cityCode = sg.cityCode ";
            if (isCopy) {
                strSql += " INNER JOIN chequePrintingSetup cps ON cps.bankCode=sg.subCode ";
            } else {
                strSql += " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode ";
            }
            strSql += " WHERE "
                    + " sgn.manualCode = 'BK' "
                    + " and sg.isActive = 1 ";
            if (isCopy) {
                strSql += " and sg.subCode <> :subCode ";
            } else {
                strSql += " AND sgsl.site_Code = :site_Code ";
            }
            sqlWithParam.setSQL(strSql);
            if (isCopy) {
                sqlWithParam.setString("subCode", DmShared.iNull(docId).toString());
            } else {
                sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new BankName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("bankName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityCode(DmShared.iNull(rsTemp.getString("cityCode")).toString());
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
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
