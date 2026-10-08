package dataman.accounts.bankAccountDetail.server;

import dataman.accounts.bankAccountDetail.shared.*;
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

public class BankAccountDetail_SrvImpl extends DmRSSPrivate implements BankAccountDetail_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String strSQL;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = " select concat(cast(bd.subcode as varchar(25)),'-',cast(bd.sNo as varchar(15)))  as subcode  "
                    + " from bankdetail bd"
                    + " left join subgroup as sg on bd.subcode=sg.subcode"
                    + " order by sg.name,bd.sNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subcode")));
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
    public DmResult<BankAccountDetail_Mdl> getMoveRec(String strKey) {
        DmResult<BankAccountDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        BankAccountDetail_Mdl mrTemp = new BankAccountDetail_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "  SELECT bd.subCode,sg.name as accountName,bd.sNo,bd.bankName,bd.accountNo,bd.branch,"
                    + " bd.swiftCode,bd.isDefault,bd.ifscCode,bd.branchAddress "
                    + " FROM bankDetail as bd "
                    + " left join subgroup as sg on bd.subCode=sg.subcode"
                    + "	WHERE concat(cast(bd.subcode as varchar(25)),'-',cast(bd.sNo as varchar(15)))=:code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("accountName")));
                mrTemp.setsNo(DmShared.xNull(rsTemp.getString("sNo")));
                mrTemp.setAccountNo(DmShared.xNull(rsTemp.getString("accountNo")));
                mrTemp.setBankName(DmShared.xNull(rsTemp.getString("bankName")));
                mrTemp.setBankBranch(DmShared.xNull(rsTemp.getString("branch")));
                mrTemp.setBankBranchAddress(DmShared.xNull(rsTemp.getString("branchAddress")));
                mrTemp.setSwiftCode(DmShared.xNull(rsTemp.getString("swiftCode")));
                mrTemp.setIFSCCode(DmShared.xNull(rsTemp.getString("ifscCode")));
                mrTemp.setDefaultTag(DmShared.xNull(rsTemp.getString("isDefault")));
                mrTemp.setDefaultText(DmShared.vNull(rsTemp.getString("isDefault")).doubleValue() == 1 ? "Yes" : "No");

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
    public DmResult<String> saveRecord(BankAccountDetail_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL, accountCode = "", sNo = "";
        ResultSet rsTemp = null;

        dmrTemp.setData("");
        try {
            BankAccountDetail_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getsNo()).equalsIgnoreCase("")) {
                blnAdd = true;
            }
            if (!blnAdd) {
                String[] accountCodewithSNo = DmShared.xNull(mrTemp.getSharedKeyFieldCode()).split("-");
                accountCode = accountCodewithSNo[0];
                sNo = accountCodewithSNo[1];

            }
            //----------------------- GET SNO--------------------------------------------------------------------
            if (blnAdd) {

                strSQL = "SELECT isNull(MAX(sNo),0) + 1 AS Mx FROM bankDetail WHERE subCode=:subCode and  sNo <> '0' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("Mx") > 0) {
                    mrTemp.setsNo(DmShared.xNull(rsTemp.getString("Mx")));
                }
                rsTemp.close();
                psTemp.close();
            }
            //-----------------------------------------------<< END >>---------------------------------------------------------
            //--------------------------------<< CHECK FOR DUPLICATE DEFAULT YES >>--------------------------------------------
            strSQL = "select count(*) as cnt from bankDetail   "
                    + "	   where subCode =:subCode and sNo <> :sNo and isDefault=1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);

            if (blnAdd) {
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("sNo", DmShared.iNull(mrTemp.getsNo()).toString());
            } else {
                psTemp.setString("subCode", DmShared.iNull(accountCode).toString());
                psTemp.setString("sNo", DmShared.iNull(sNo).toString());
            }
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (DmShared.vNull(mrTemp.getDefaultTag()).intValue() == 1) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new DmException(500, "Default 'Yes' for " + DmShared.xNull(mrTemp.getSharedKeyFieldName()) + " Link With A/c is already exists");
                }
            }
            rsTemp.close();
            psTemp.close();
            //-----------------------------------------------<< END >>---------------------------------------------------------
            //-------------------------------------<< CHECK FOR DUPLICATE ACCOUNT NO. >>----------------------------------------

            strSQL = "select count(*) as cnt from bankDetail   "
                    + "	   where accountNo = :accountNo and subCode = :subCode and bankName= :bank and sNo<>:sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("accountNo", DmShared.xNull(mrTemp.getAccountNo().trim()));
            psTemp.setString("bank", DmShared.xNull(mrTemp.getBankName().trim()));
            if (blnAdd) {
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("sNo", DmShared.iNull(mrTemp.getsNo()).toString());
            } else {
                psTemp.setString("subCode", DmShared.iNull(accountCode).toString());
                psTemp.setString("sNo", DmShared.iNull(sNo).toString());
            }
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(200, DmShared.xNull(mrTemp.getAccountNo()) + " Account No. is already exists for " + DmShared.xNull(mrTemp.getSharedKeyFieldName()) + " Link With A/c");
            }
            rsTemp.close();
            psTemp.close();
            //-----------------------------------------------<< END >>---------------------------------------------------------
            if (blnAdd) {
                strSQL = "Select Count(*) As Cnt From bankDetail where sNo = :sNo  and SubCode=:subCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("sNo", mrTemp.getsNo());
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Duplicate Record Not Allowed !!!");
                }
                rsTemp.close();
                psTemp.close();

                strSQL = "INSERT INTO bankDetail (subCode, sNo, bankName, accountNo, branch,swiftCode,isDefault,branchAddress,ifscCode) "
                        + "VALUES (:subCode, :sNo, :bankName, :accountNo, :branch,:swiftCode,:isDefault,:branchAddress,:ifscCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("sNo", mrTemp.getsNo());
                psTemp.setString("accountNo", DmShared.xNull(mrTemp.getAccountNo()));
                psTemp.setString("bankName", DmShared.xNull(mrTemp.getBankName()));
                psTemp.setString("branch", DmShared.xNull(mrTemp.getBankBranch()));
                psTemp.setString("swiftCode", DmShared.xNull(mrTemp.getSwiftCode()));
                psTemp.setString("isDefault", DmShared.xNull(mrTemp.getDefaultTag()));
                psTemp.setString("branchAddress", DmShared.xNull(mrTemp.getBankBranchAddress()));
                psTemp.setString("ifscCode", DmShared.xNull(mrTemp.getIFSCCode()));

            } else {
                strSQL = "UPDATE bankDetail SET  "
                        + " accountNo = :accountNo "
                        + ",bankName = :bankName "
                        + ",branch = :branch "
                        + ",swiftCode = :swiftCode "
                        + ",isDefault = :isDefault "
                        + ",branchAddress = :branchAddress "
                        + ",ifscCode = :ifscCode "
                        + "  WHERE concat(cast(subcode as varchar(25)),'-',cast(sNo as varchar(15))) =:code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("accountNo", DmShared.xNull(mrTemp.getAccountNo()));
                psTemp.setString("bankName", DmShared.xNull(mrTemp.getBankName()));
                psTemp.setString("branch", DmShared.xNull(mrTemp.getBankBranch()));
                psTemp.setString("swiftCode", DmShared.xNull(mrTemp.getSwiftCode()));
                psTemp.setString("isDefault", DmShared.xNull(mrTemp.getDefaultTag()));
                psTemp.setString("branchAddress", DmShared.xNull(mrTemp.getBankBranchAddress()));
                psTemp.setString("ifscCode", DmShared.xNull(mrTemp.getIFSCCode()));

            }
            psTemp.executeUpdate();
            psTemp.close();
            if (blnAdd) {
                dmrTemp.setData(mrTemp.getSharedKeyFieldCode() + "-" + mrTemp.getsNo());
            } else {
                dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            }
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

            strSQL = "DELETE FROM bankDetail WHERE concat(cast(subcode as varchar(25)),'-',cast(sNo as varchar(15))) = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.xNull(strKey));
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
            sqlWithParam.setSQL(" SELECT bd.subCode,sg.name as accountName,bd.sNo,bd.bankName,bd.accountNo,bd.branch,"
                    + " bd.swiftCode,bd.isDefault,bd.ifscCode,bd.branchAddress "
                    + " FROM bankDetail as bd "
                    + " left join subgroup as sg on bd.subCode=sg.subcode");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")) + "-" + DmShared.xNull(rsTemp.getString("sNo")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("accountName")));
                listNode.setAccountNo(DmShared.xNull(rsTemp.getString("accountNo")));
                listNode.setBankName(DmShared.xNull(rsTemp.getString("bankName")));
                listNode.setBankBranch(DmShared.xNull(rsTemp.getString("branch")));
                listNode.setBankBranchAddress(DmShared.xNull(rsTemp.getString("branchAddress")));
                listNode.setSwiftCode(DmShared.xNull(rsTemp.getString("swiftCode")));
                listNode.setIFSCCode(DmShared.xNull(rsTemp.getString("ifscCode")));
                listNode.setDefaultText(DmShared.vNull(rsTemp.getString("isDefault")).doubleValue() == 1 ? "Yes" : "No");

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
    public DmResultPageing<List<TxtHLinkWithAccount_Mdl>> getTxtHLinkWithAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLinkWithAccount_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLinkWithAccount_Mdl> lstTemp = new ArrayList<>();
        TxtHLinkWithAccount_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode, SG.Name, isNull(CT.CityName, '') AS City, SG.manualCode "
                    + "FROM   SubGroup SG "
                    + "LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                    + "WHERE SG.isActive = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLinkWithAccount_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("City")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
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
