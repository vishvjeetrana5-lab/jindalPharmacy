package dataman.accounts.defineCostCenter.server;

import dataman.accounts.defineCostCenter.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DefineCostCenter_SrvImpl extends DmRSSPrivate implements DefineCostCenter_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("SELECT  CODE As SearchCode,NAME FROM CostCenterMast WHERE isNull(CType,'C') = 'C' ORDER BY NAME");
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
    public DmResult<DefineCostCenter_Mdl> getMoveRec(String strKey) {
        DmResult<DefineCostCenter_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        DefineCostCenter_Mdl mrTemp = new DefineCostCenter_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT CCM.Code, CCM.Name, CCM.UndercostCenter, CCM1.Name AS UnderCostCenterName, CCM.ManualCode, CCM.Address, CCM.isActive, CCM.preparedBy, CCM.u_Name "
                    + "FROM CostCenterMast CCM "
                    + "LEFT JOIN CostCenterMast CCM1 ON CCM.UnderCostCenter = CCM1.Code "
                    + "WHERE isNull(CCM.CType, 'C') = 'C' AND CCM.Code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));

                mrTemp.setActiveCode(DmShared.vNull(rsTemp.getString("isActive")).toString());
                mrTemp.setActiveName(DmShared.vNull(mrTemp.getActiveCode()) == 0 ? "No" : "Yes");
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                mrTemp.setDescription(DmShared.xNull(rsTemp.getString("Address")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.setUnderCostCode(DmShared.xNull(rsTemp.getString("UndercostCenter")));
                mrTemp.setUnderCostName(DmShared.xNull(rsTemp.getString("UnderCostCenterName")));
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
    public DmResult<String> saveRecord(DefineCostCenter_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null, psTemp2 = null, psTemp3 = null, psTemp4 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL, userName, siteCode;
        ResultSet rsTemp = null, rsTemp1 = null, rsTemp2 = null, rsTemp3 = null, rsTemp4 = null;
        dmrTemp.setData("");
        int intSNo = 0;
        try {
            DefineCostCenter_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            siteCode = DmServer.getSsnSiteCode(this);
            userName = DmServer.getSsnUserName(this);
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setManualCode(DmShared.removeSpace(mrTemp.getManualCode()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "costcentermast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            strSQL = "SELECT COUNT(CCM.Code) AS Cnt FROM CostCenterMast CCM "
                    + " WHERE CCM.Name = :name AND CCM.Code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Cost Center Name Already Exists.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(CCM.Code) AS Cnt FROM CostCenterMast CCM "
                    + " WHERE CCM.ManualCode = :manualCode AND CCM.Code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Manual Code Already Exists.");
            }
            rsTemp.close();
            psTemp.close();

            //* Fetching Group Path Of Particular Group Under For Later Use ********************
            strSQL = " SELECT CODE,SNo,UnderCostCenter FROM CostCenterPath WHERE CODE= :code ORDER BY SNo ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp1.setString("code", DmShared.xNull(mrTemp.getUnderCostCode()));
            rsTemp1 = psTemp1.executeQuery();

            strSQL = " DELETE FROM CostCenterPath WHERE CODE = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {
                strSQL = " INSERT INTO costcentermast ( CODE, NAME, ManualCode, Address,  U_EntDt, Site_Code, UnderCostCenter, isActive, CType, preparedBy )  "
                        + " VALUES ( :code, :name, :manualCode, :address,  getdate(), :site_Code, :underCostCenter, :isActive, 'C', :u_Name) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("address", DmShared.xNull(mrTemp.getDescription()));
                psTemp.setString("u_Name", userName);
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("underCostCenter", DmShared.foriegnKey(mrTemp.getUnderCostCode()));
                psTemp.setString("isActive", DmShared.vNull(mrTemp.getActiveCode()) == 0 ? "0" : "1");
            } else {
                strSQL = "Update CostCenterMast Set "
                        + "Name = :name , "
                        + "UnderCostCenter = :underCostCenter ,"
                        + "U_Name = :userName ,"
                        + "U_EntDt = getdate() ,"
                        + "ManualCode = :manualCode , "
                        + "Address = :address,"
                        + "isActive = :isActive "
                        + "Where Code = :code ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("underCostCenter", DmShared.foriegnKey(mrTemp.getUnderCostCode()));
                psTemp.setString("userName", userName);
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("address", DmShared.xNull(mrTemp.getDescription()));
                psTemp.setString("isActive", DmShared.vNull(mrTemp.getActiveCode()) == 0 ? "0" : "1");
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());

            }
            psTemp.executeUpdate();
            psTemp.close();

            intSNo = 0;
            //======== Inserting Path For Particular Group Under Which Is Already Fetched Above ====
            if (!DmShared.xNull(mrTemp.getUnderCostCode()).equals("")) {
                while (rsTemp1.next()) {
                    intSNo++;
                    strSQL = " INSERT INTO CostCenterPath(CODE,SNo,UnderCostCenter) VALUES (:code,:sno,:uCostCenter)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("uCostCenter", DmShared.foriegnKey(rsTemp1.getString("UnderCostCenter")));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                intSNo = intSNo + 1;
                strSQL = " INSERT INTO CostCenterPath(CODE,SNo,UnderCostCenter) VALUES (:code,:sno,:uCostCenter) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setInt("sno", intSNo);
                psTemp.setString("uCostCenter", DmShared.foriegnKey(mrTemp.getUnderCostCode()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            //============================================
            //========= Updating Child Records Also ======
            //================ Begin =====================
            //============================================
            //============= Fetching Group Path Of Particular Group For Later Use ==========
            strSQL = " SELECT CODE,SNo,UnderCostCenter FROM CostCenterPath WHERE CODE = :code ORDER BY SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();

            //==============================================================================
            //============= Fetching All Child Groups Of Particular Record =================
            strSQL = " SELECT CODE,SNo,UnderCostCenter FROM CostCenterPath WHERE UnderCostCenter = :code ORDER BY SNo ";
            psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp2.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp2 = psTemp2.executeQuery();

            //==============================================================================
            while (rsTemp2.next()) {
                //============= Fetching Group Path Whose Level Is Greater Than Or Equal To ========
                //============= Particular Group For Later Use =====================================
                strSQL = " SELECT CODE,SNo,UnderCostCenter FROM CostCenterPath "
                        + " WHERE CODE = :code AND SNo >= :sno ORDER BY SNo ";
                psTemp3 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp3.setString("code", rsTemp2.getString("UnderCostCenter"));
                psTemp3.setInt("sno", rsTemp2.getInt("SNo"));
                rsTemp3 = psTemp3.executeQuery();
                //==================================================================================

                //============= Deleting Particular Group Path =====================================
                psTemp4 = DmPreparedStatement.prepareStatement(cnTemp, " DELETE FROM CostCenterPath WHERE CODE = :code ");
                psTemp4.setString("code", rsTemp2.getString("CODE"));
                psTemp4.executeUpdate();
                psTemp4.close();

                //======= Updating Group Path Of Child Record =======
                intSNo = 0;
                while (rsTemp.next()) {
                    intSNo++;
                    strSQL = " INSERT INTO CostCenterPath (CODE, SNo, UnderCostCenter) "
                            + " VALUES ( :code, :sno, :uCostCenter ) ;";
                    psTemp4 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp4.setString("code", DmShared.xNull(rsTemp2.getString("CODE")));
                    psTemp4.setInt("sno", intSNo);
                    psTemp4.setString("uCostCenter", DmShared.xNull(rsTemp.getString("UnderCostCenter")));
                    psTemp4.executeUpdate();
                    psTemp4.close();
                }

                while (rsTemp3.next()) {
                    intSNo++;
                    strSQL = " INSERT INTO CostCenterPath (CODE, SNo, UnderCostCenter) "
                            + " VALUES ( :code, :sno, :uCostCenter) ;";
                    psTemp4 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp4.setString("code", DmShared.xNull(rsTemp2.getString("CODE")));
                    psTemp4.setInt("sno", intSNo);
                    psTemp4.setString("uCostCenter", DmShared.xNull(rsTemp3.getString("UnderCostCenter")));
                    psTemp4.executeUpdate();
                    psTemp4.close();
                }
                rsTemp3.close();
                psTemp3.close();
            }

            rsTemp2.close();
            psTemp2.close();

            rsTemp1.close();
            psTemp1.close();

            rsTemp.close();
            psTemp.close();

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
                if (rsTemp4 != null) {
                    rsTemp4.close();
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
                if (psTemp4 != null) {
                    psTemp4.close();
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

            strSQL = "Select Count(UnderCostCenter) As Cnt From CostCenterPath Where UnderCostCenter= :UnderCostCenter ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("UnderCostCenter", strKey);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Record Is In Use. You Cannot Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Delete From CostCenterPath Where Code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            strSQL = "Delete From CostCenterMast Where Code = :code ";
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
            sqlWithParam.setSQL("SELECT CCM.Code, '' AS V_Type, CCM.Name AS Cost_Center_Name, CCM1.Name AS Under_Cost_Center, CCM.U_Name, CCM.U_EntDt, "
                    + "  CCM.ManualCode, CCM.Address AS Description, CCM.isActive "
                    + " FROM CostCenterMast CCM  "
                    + " LEFT JOIN CostCenterMast CCM1 ON CCM.UnderCostCenter = CCM1.Code "
                    + " WHERE isNull(CCM.CType, 'C') = 'C' ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setActiveName(DmShared.vNull(rsTemp.getString("isActive")) == 0 ? "No" : "Yes");
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Cost_Center_Name")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setUCostCenter(DmShared.xNull(rsTemp.getString("Under_Cost_Center")));
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
    public DmResultPageing<List<TxtHUnderCostCenter_Mdl>> getTxtHUnderCostCenter(DmHelper helper, boolean blnToEndOfFile, String strCode) {
        DmResultPageing<List<TxtHUnderCostCenter_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHUnderCostCenter_Mdl> lstTemp = new ArrayList<>();
        TxtHUnderCostCenter_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT  CCM.Code,CCM.Name AS NAME, CCM1.Name AS UName "
                    + "FROM costcentermast CCM "
                    + "LEFT JOIN costcentermast CCM1 ON CCM.UnderCostCenter = CCM1.Code "
                    + "WHERE isNull(CCM.CType,'C')='C' AND CCM.Code <> :code AND "
                    + "CCM.Code NOT IN (SELECT CODE FROM costcenterpath WHERE UnderCostCenter = :code) ");
            sqlWithParam.setString("code", DmShared.iNull(strCode).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHUnderCostCenter_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setUnderCostCenter(DmShared.xNull(rsTemp.getString("UName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<List<KeyFieldExtended>> getGrdUnderCostCenter(String strCode) {
        DmResult<List<KeyFieldExtended>> rstTemp = new DmResult<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = "SELECT CCM.Code,CCm.Name "
                    + "FROM CostCenterPath CCP  "
                    + "LEFT JOIN CostCenterMast CCM ON CCM.Code = CCP.UnderCostCenter  "
                    + "WHERE CCP.Code = :code "
                    + "ORDER BY CCP.SNo DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<String>> getSuggestionCostCenter(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT CODE,NAME FROM CostCenterMast  WHERE isNull(CType,'C') = 'C' ");
            helper.addSort("NAME", DmEnum.DmSortType.ASC);
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
}
