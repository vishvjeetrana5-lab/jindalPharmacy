package dataman.accounts.budgetHeadMaster.server;

import dataman.dmBase.self.shared.*;
import dataman.accounts.budgetHeadMaster.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BudgetHeadMaster_SrvImpl extends DmRSSPrivate implements BudgetHeadMaster_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("Select headCode From budgetHeadMast Order By headName;");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("headCode")));
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
            sqlWithParam.setSQL(" Select headName From budgetHeadMast ");
            helper.addSort("headName", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("headName")));
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
    public DmResult<BudgetHeadMaster_Mdl> getMoveRec(String strKey) {
        DmResult<BudgetHeadMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        BudgetHeadMaster_Mdl mrTemp = new BudgetHeadMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = " Select bhm.headCode,bhm.headName,bhm.headUnder,bhm.manualCode\n"
                    + " ,bhm1.headName As headUnderName,bhm.preparedby,bhm.u_name\n"
                    + " From budgetHeadMast bhm\n"
                    + " Left Join budgetHeadMast bhm1 On bhm.headUnder = bhm1.headCode\n"
                    + " Where bhm.headCode = :headCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("headCode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("headCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("headName")));
                mrTemp.setHeadUnder(DmShared.xNull(rsTemp.getString("headUnder")));
                mrTemp.setHeadUnderName(DmShared.xNull(rsTemp.getString("headUnderName")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));

                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedby")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
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

            strSQL = "Select Count(headCode) As Cnt From budgetHeadPath Where headUnder = :headCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("headCode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Record is in use. You can not delete it.");
            }
            rsTemp.close();
            psTemp.close();

            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From budgetHeadPath Where headCode = :headCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("headCode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From budgetHeadMast Where headCode = :headCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("headCode", DmShared.iNull(strKey).toString());
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
    public DmResult<String> saveRecord(BudgetHeadMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null, psTemp2 = null, psTemp3 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL;
        ResultSet rsTemp = null;
        ResultSet rsTemp1 = null;
        ResultSet rsTemp2 = null;
        ResultSet rsTemp3 = null;
        int intSNo;
        dmrTemp.setData("");

        try {
            BudgetHeadMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "budgetHeadMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            strSQL = "Select Count(headName) As Cnt From budgetHeadMast Where headName = :headName And headCode <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("headName", mrTemp.getSharedKeyFieldName());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Head Name Already Exists.");
            }
            rsTemp.close();
            psTemp.close();
            
            //================================<< CHECK DUPLICATE FOR MANUAL CODE >>================================
            strSQL = "Select Count(manualCode) As Cnt From budgetHeadMast Where LOWER(manualCode) = :manualCode And headCode <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("manualCode", mrTemp.getManualCode());
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Manual Code already exists.");
            }
            rsTemp.close();
            psTemp.close();
            //========================================<< END >>=====================================================

            //* Fetching Group Path Of Particular Group Under For Later Use ********************
            strSQL = "Select headCode,headUnder From budgetHeadPath Where headCode = :headCode Order By SNo";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp1.setString("headCode", mrTemp.getHeadUnder());
            rsTemp1 = psTemp1.executeQuery();

            strSQL = "Delete From budgetHeadPath Where headCode = :headCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("headCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {
                strSQL = "Insert Into budgetHeadMast(headCode,headName, headUnder, manualCode, preparedBY, U_EntDt) Values \n"
                        + "(:headCode, :headName, :headUnder, :manualCode, :preparedBY, getDate()) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("headCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("headName", mrTemp.getSharedKeyFieldName());
                psTemp.setString("headUnder", DmShared.foriegnKey(mrTemp.getHeadUnder()));
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));
            } else {
                strSQL = "Update budgetHeadMast Set "
                        + "headName = :headName, "
                        + "manualCode = :manualCode, "
                        + "headUnder = :headUnder, "
                        + "transferDt = :transferDt, "
                        + "U_Name = :u_name, "
                        + "U_EntDt = getdate() "
                        + "Where headCode = :headCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("headName", mrTemp.getSharedKeyFieldName());
                psTemp.setString("headUnder", DmShared.foriegnKey(mrTemp.getHeadUnder()));
                psTemp.setString("manulCode", mrTemp.getManualCode());
                psTemp.setString("transferDt", null);
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setString("headCode", mrTemp.getSharedKeyFieldCode());
            }
            psTemp.executeUpdate();
            psTemp.close();

            intSNo = 0;
            //* Inserting Path For Particular Group Under Which Is Already Fetched Above ************
            if (!DmShared.xNull(mrTemp.getHeadUnder()).equalsIgnoreCase("")) {
                while (rsTemp1.next()) {
                    intSNo++;
                    strSQL = "Insert Into budgetHeadPath(headCode,SNo,headUnder) Values (:headCode,:sno,:headUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("headCode", mrTemp.getSharedKeyFieldCode());
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("headUnder", rsTemp1.getString("headUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                //* Inserting Path For Particular Group Under *******************
                intSNo++;
                strSQL = "Insert Into budgetHeadPath(headCode,SNo,headUnder) Values (:headCode,:sno,:headUnder) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("headCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setInt("sno", intSNo);
                psTemp.setString("headUnder", mrTemp.getHeadUnder());
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp1.close();
            psTemp1.close();

            //* Updating Child Records Also [Begin] **********************************
            //* Fetching Group Path Of Particular Head For Later Use ****************
            strSQL = "Select headCode,SNo,headUnder From budgetHeadPath Where headCode = :headCode Order By SNo ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp1.setString("headCode", mrTemp.getSharedKeyFieldCode());
            rsTemp1 = psTemp1.executeQuery();

            //* Fetching All Child Groups Of Particular Record *********************
            strSQL = "Select headCode,SNo,headUnder From budgetHeadPath Where headUnder = :headCode Order By SNo ";
            psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp2.setString("headCode", mrTemp.getSharedKeyFieldCode());
            rsTemp2 = psTemp2.executeQuery();

            while (rsTemp2.next()) {
                //* Fetching Group Path Whose Level Is Greater Than Or Equal To *********************
                strSQL = "Select headCode,SNo,headUnder From budgetHeadPath Where headCode = :headCode And SNo >= :sno Order By SNo ";
                psTemp3 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp3.setString("headCode", rsTemp2.getString("headCode"));
                psTemp3.setInt("sno", rsTemp2.getInt("SNo"));
                rsTemp3 = psTemp3.executeQuery();

                //* Deleting Particular Group Path ***************************
                strSQL = "Delete From budgetHeadPath Where headCode = :headCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("headCode", rsTemp2.getString("headCode"));
                psTemp.executeUpdate();
                psTemp.close();

                //* Updating Group Path Of Child Record ****************************
                intSNo = 0;
                while (rsTemp1.next()) {
                    intSNo++;
                    strSQL = "Insert Into budgetHeadPath(headCode,SNo,headUnder) Values (:headCode,:sno,:headUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("headCode", rsTemp2.getString("headCode"));
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("headUnder", rsTemp1.getString("headUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                while (rsTemp3.next()) {
                    intSNo++;
                    strSQL = "Insert Into budgetHeadPath(headCode,SNo,headUnder) Values (:headCode,:sno,:headUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("headCode", rsTemp2.getString("headCode"));
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("headUnder", rsTemp3.getString("headUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                psTemp3.close();
                rsTemp3.close();
            }
            psTemp1.close();
            rsTemp1.close();
            psTemp2.close();
            rsTemp2.close();
            //* Updating Child Records Also [End] **********************************

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
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (rsTemp2 != null) {
                    rsTemp2.close();
                }
                if (rsTemp3 != null) {
                    rsTemp3.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp2 != null) {
                    psTemp2.close();
                }
                if (psTemp3 != null) {
                    psTemp3.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHHeadUnder(DmHelper helper, boolean blnToEndOfFile, String strCode) {
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
            sqlWithParam.setSQL("Select bhm.headCode as code,bhm.headName\n"
                    + " From budgetHeadMast bhm \n"
                    + "where bhm.headCode <> :headCode "
                    + "And bhm.headCode Not In (Select headCode From budgetHeadPath Where headUnder = :headCode) ");
            sqlWithParam.setString("headCode", DmShared.iNull(strCode).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("headName")));
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
            sqlWithParam.setSQL("Select bhm.headCode as code,bhm.headName,bhm1.headName As headUnderName\n"
                    + "     From budgetHeadMast bhm"
                    + "     Left Join budgetHeadMast bhm1 On bhm.headUnder = bhm1.headCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("headName")));
                listNode.setHeadUnderName(DmShared.xNull(rsTemp.getString("headUnderName")));
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
