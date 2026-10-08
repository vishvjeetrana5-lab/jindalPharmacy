package dataman.accounts.accountGroup.server;

import dataman.dmBase.self.shared.*;
import dataman.accounts.accountGroup.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmImportData.server.ImportAccountGroup;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountGroup_SrvImpl extends DmRSSPrivate implements AccountGroup_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("Select GroupCode From AcGroup Order By GroupName;");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
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
            sqlWithParam.setSQL(" Select groupName From AcGroup ");
            helper.addSort("groupName", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("GroupName")));
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
    public DmResult<AccountGroup_Mdl> getMoveRec(String strKey) {
        DmResult<AccountGroup_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        AccountGroup_Mdl mrTemp = new AccountGroup_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = " Select AG.GroupCode,AG.isSysGroup,AG.GroupName,AG.GroupUnder,AG.Nature as NatureCode,sgn.name as NatureName,"
                    + " AG1.GroupName As GrUnderName,AG.GroupNature,AG.ContraGroupName,AG.preparedby,AG.u_name "
                    + " From AcGroup AG "
                    + " Left Join AcGroup AG1 On AG.GroupUnder = AG1.GroupCode "
                    + "  left join subGroupNature as sgn on AG.nature=sgn.code "
                    + " Where AG.GroupCode = :groupCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("groupCode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                mrTemp.setSysGroup(DmShared.xNull(rsTemp.getString("isSysGroup")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
                mrTemp.setGroupUnder(DmShared.xNull(rsTemp.getString("GroupUnder")));
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("NatureCode")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("NatureName")));
                mrTemp.setGroupUnderName(DmShared.xNull(rsTemp.getString("GrUnderName")));

                mrTemp.setGroupNature(DmShared.xNull(rsTemp.getString("GroupNature")));

                mrTemp.setGroupNatureName(
                        DmShared.iNull(rsTemp.getString("GroupNature")).longValue() == 0 ? "Liabilities"
                        : DmShared.iNull(rsTemp.getString("GroupNature")).longValue() == 1 ? "Assets"
                        : DmShared.iNull(rsTemp.getString("GroupNature")).longValue() == 2 ? "Expenses Direct"
                        : DmShared.iNull(rsTemp.getString("GroupNature")).longValue() == 3 ? "Expenses Indirect"
                        : DmShared.iNull(rsTemp.getString("GroupNature")).longValue() == 4 ? "Revenue Direct"
                        : DmShared.iNull(rsTemp.getString("GroupNature")).longValue() == 5 ? "Revenue Indirect" : ""
                );

                mrTemp.setContraGroupName(DmShared.xNull(rsTemp.getString("ContraGroupName")));

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

            strSQL = "Select Count(*) As cnt From AcGroup Where GroupCode = :groupcode And isSysGroup = '1' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("groupcode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("You can not edit / delete system define group.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Select Count(GroupCode) As Cnt From AcGroupPath Where GroupUnder = :groupcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("groupcode", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Record is in use. You can not delete it.");
            }
            rsTemp.close();
            psTemp.close();

            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From AcGroupPositioning Where GroupCode = :groupcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("groupcode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

            strSQL = "Delete From AcGroupPath Where GroupCode = :groupcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("groupcode", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From AcGroup Where GroupCode = :groupcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("groupcode", DmShared.iNull(strKey).toString());
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
    public DmResult<String> saveRecord(AccountGroup_Mdl mrTemp) {
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
            AccountGroup_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "AcGroup", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            strSQL = "Select Count(GroupName) As Cnt From AcGroup Where GroupName = :groupName And GroupCode <> :groupCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("groupName", mrTemp.getSharedKeyFieldName());
            psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("A/c Group Name Already Exists.");
            }
            rsTemp.close();
            psTemp.close();

            //* Fetching Group Path Of Particular Group Under For Later Use ********************
            strSQL = "Select GroupCode,SNo,GroupUnder From AcGroupPath Where GroupCode = :groupCode Order By SNo ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp1.setString("groupCode", mrTemp.getGroupUnder());
            rsTemp1 = psTemp1.executeQuery();

            strSQL = "Delete From AcGroupPath Where GroupCode = :groupCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From AcGroupPositioning Where GroupCode = :groupCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();

            if (blnAdd) {
                strSQL = "Insert Into AcGroup(GroupCode,GroupName, "
                        + "GroupUnder, "
                        + "Nature,preparedBY,isSysGroup, "
                        + "U_EntDt,GroupNature,ContraGroupName) Values  "
                        + "(:groupCode,:groupName,:groupUnder,:nature,:preparedBY,:sysgroup,getdate(),:groupNature,:contraGroupName) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("groupName", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupUnder", DmShared.foriegnKey(mrTemp.getGroupUnder()));
                psTemp.setString("nature", mrTemp.getNatureCode());
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));
                psTemp.setString("sysgroup", "0");
                psTemp.setString("groupNature", mrTemp.getGroupNature());
                psTemp.setString("contraGroupName", mrTemp.getContraGroupName());

            } else {
                strSQL = "Update AcGroup Set "
                        + "GroupName = :groupName, "
                        + "ContraGroupName = :contraGroupName, "
                        + "GroupUnder = :groupUnder, "
                        + "GroupNature = :groupNature, "
                        + "Nature = :nature, "
                        + "transferDt = :transferDt, "
                        + "U_Name = :u_name, "
                        + "U_EntDt = getdate() "
                        + "Where GroupCode = :groupCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("groupName", mrTemp.getSharedKeyFieldName());
                psTemp.setString("contraGroupName", mrTemp.getContraGroupName());
                psTemp.setString("groupUnder", DmShared.foriegnKey(mrTemp.getGroupUnder()));
                psTemp.setString("groupNature", mrTemp.getGroupNature());
                psTemp.setString("nature", mrTemp.getNatureCode());
                psTemp.setString("transferDt", null);
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
            }
            psTemp.executeUpdate();
            psTemp.close();

            intSNo = 0;
            //* Inserting Path For Particular Group Under Which Is Already Fetched Above ************
            if (!DmShared.xNull(mrTemp.getGroupUnder()).equalsIgnoreCase("")) {
                while (rsTemp1.next()) {
                    intSNo++;
                    strSQL = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("groupUnder", rsTemp1.getString("GroupUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                //* Inserting Path For Particular Group Under *******************
                intSNo++;
                strSQL = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setInt("sno", intSNo);
                psTemp.setString("groupUnder", mrTemp.getGroupUnder());
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp1.close();
            psTemp1.close();

            //* Updating Nature In SubGroup Same As Group Nature *****************
//            strSQL = "Update SubGroup Set Nature = :nature Where GroupCode = :groupCode ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//            psTemp.setString("nature", mrTemp.getNatureCode());
//            psTemp.setString("groupCode", mrTemp.getSharedKeyFieldCode());
//            psTemp.executeUpdate();
//            psTemp.close();
            //* Updating Child Records Also [Begin] **********************************
            //* Fetching Group Path Of Particular Group For Later Use ****************
            strSQL = "Select GroupCode,SNo,GroupUnder From AcGroupPath Where GroupCode = :groupCode Order By SNo ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp1.setString("groupCode", mrTemp.getSharedKeyFieldCode());
            rsTemp1 = psTemp1.executeQuery();

            //* Fetching All Child Groups Of Particular Record *********************
            strSQL = "Select GroupCode,SNo,GroupUnder From AcGroupPath Where GroupUnder = :groupCode Order By SNo ";
            psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp2.setString("groupCode", mrTemp.getSharedKeyFieldCode());
            rsTemp2 = psTemp2.executeQuery();

            while (rsTemp2.next()) {
                //* Fetching Group Path Whose Level Is Greater Than Or Equal To *********************
                strSQL = "Select GroupCode,SNo,GroupUnder From AcGroupPath Where GroupCode = :groupCode And SNo >= :sno Order By SNo ";
                psTemp3 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp3.setString("groupCode", rsTemp2.getString("GroupCode"));
                psTemp3.setInt("sno", rsTemp2.getInt("SNo"));
                rsTemp3 = psTemp3.executeQuery();

                //* Deleting Particular Group Path ***************************
                strSQL = "Delete From AcGroupPath Where GroupCode = :groupCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("groupCode", rsTemp2.getString("GroupCode"));
                psTemp.executeUpdate();
                psTemp.close();

                //* Updating Group Path Of Child Record ****************************
                intSNo = 0;
                while (rsTemp1.next()) {
                    intSNo++;
                    strSQL = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("groupCode", rsTemp2.getString("GroupCode"));
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("groupUnder", rsTemp1.getString("GroupUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                while (rsTemp3.next()) {
                    intSNo++;
                    strSQL = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("groupCode", rsTemp2.getString("GroupCode"));
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("groupUnder", rsTemp3.getString("GroupUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                strSQL = "Update AcGroup Set GroupNature = :groupNature "
                        + "Where GroupCode = :groupCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("groupNature", mrTemp.getGroupNature());
                psTemp.setString("groupCode", rsTemp2.getString("GroupCode"));
                psTemp.executeUpdate();
                psTemp.close();

                psTemp3.close();
                rsTemp3.close();
            }
            psTemp1.close();
            rsTemp1.close();
            psTemp2.close();
            rsTemp2.close();
            //* Updating Child Records Also [End] **********************************

//            //* Updating GroupNature In SubGroup Same As GroupNature ***************************
//            strSQL = "Update SG  Set SG.GroupNature = AG.GroupNature from  SubGroup SG "
//                    + "Left Join AcGroup AG On SG.GroupCode = AG.GroupCode ";
//
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//            psTemp.executeUpdate();
//            psTemp.close();
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
    public DmResultPageing<List<TxtHGroupUnder_Mdl>> getTxtHGroupUnder(DmHelper helper, boolean blnToEndOfFile, String strCode) {
        DmResultPageing<List<TxtHGroupUnder_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHGroupUnder_Mdl> lstTemp = new ArrayList<>();
        TxtHGroupUnder_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select ag.GroupCode,ag.GroupName,ag.GroupNature as GroupNatureCode,  "
                    + " (Case ag.GroupNature   "
                    + " When '0' Then 'Liabilities'   "
                    + " When '1' Then 'Assets'   "
                    + " When '2' Then 'Expenses Direct'   "
                    + " When '3' Then 'Expenses Indirect'   "
                    + " When '4' Then 'Revenue Direct'   "
                    + " When '5' Then 'Revenue Indirect'   "
                    + " End) As GroupNatureName,ag.Nature  as  NatureCode, sgn.name as NatureName  "
                    + " From AcGroup as ag "
                    + " left join subGroupNature as sgn on ag.nature=sgn.code "
                    + "where ag.GroupCode <> :groupCode "
                    + "And ag.GroupCode Not In (Select GroupCode From AcGroupPath Where GroupUnder = :groupCode) ");
            sqlWithParam.setString("groupCode", DmShared.iNull(strCode).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHGroupUnder_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setMainGroupCode(DmShared.xNull(rsTemp.getString("GroupNatureCode")));
                listNode.setMainGroupName(DmShared.xNull(rsTemp.getString("GroupNatureName")));
                listNode.setNatureCode(DmShared.xNull(rsTemp.getString("NatureCode")));
                listNode.setNatureName(DmShared.xNull(rsTemp.getString("NatureName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMainGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Liabilities");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Assets");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Expenses Direct");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("3");
            listNode.setSharedKeyFieldName("Expenses Indirect");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("4");
            listNode.setSharedKeyFieldName("Revenue Direct");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("5");
            listNode.setSharedKeyFieldName("Revenue Indirect");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select  Code ,Name as Nature  From subGroupNature ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Nature")));
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
            sqlWithParam.setSQL("Select AC.GroupCode,AC.GroupName, "
                    + " (Case AC.GroupNature  "
                    + " When '0' Then 'Liabilities'  "
                    + " When '1' Then 'Assets'  "
                    + " When '2' Then 'Expenses Direct'  "
                    + " When '3' Then 'Expenses Indirect'  "
                    + " When '4' Then 'Revenue Direct'  "
                    + " When '5' Then 'Revenue Indirect'  "
                    + " End)As GroupNature,sgn.name as Nature,AC.ContraGroupName,"
                    + " (Case  isnull(AC.isSysGroup,'0')"
                    + " When '0' Then 'N' "
                    + " When '1' Then 'Y'  "
                    + " End) as  isSysGroup "
                    + " ,AC1.GroupName As GroupUnderName  "
                    + " From AcGroup AC  "
                    + " Left Join AcGroup AC1 On AC.GroupUnder = AC1.GroupCode "
                    + "  left join subGroupNature as sgn on AC.nature=sgn.code ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setContraGroupName(DmShared.xNull(rsTemp.getString("ContraGroupName")));
                listNode.setGroupUnderName(DmShared.xNull(rsTemp.getString("GroupUnderName")));
                listNode.setGroupNatureName(DmShared.xNull(rsTemp.getString("GroupNature")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                listNode.setSysGroup(DmShared.xNull(rsTemp.getString("isSysGroup")));
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = DmImportData_Server.getCSVFormats(new ImportAccountGroup().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "accountGroup";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  ag.groupName, ag.contraGroupName, ag1.groupName groupUnder, sgn.name as nature "
                    + "		from acGroup ag "
                    + "		left join acGroup ag1 on ag1.groupCode = ag.groupUnder "
                    + "		left join subGroupNature sgn on sgn.code = ag.nature"
                    + "		order by ag.groupName, ag.contraGroupName, ag1.groupName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("groupName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("contraGroupName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("groupUnder")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("nature")) + "\"";
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
}
