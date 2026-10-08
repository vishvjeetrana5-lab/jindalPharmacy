package dataman.utility.walkThroughMaster.server;

import dataman.utility.walkThroughMaster.shared.GrdAuthority_Mdl;
import dataman.utility.walkThroughMaster.shared.Find_Mdl;
import dataman.utility.walkThroughMaster.shared.GrdDefaultDetail_Mdl;
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_Mdl;
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_Vld;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_Srv;

public class WalkThroughMaster_SrvImpl extends DmRSSPrivate implements WalkThroughMaster_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("SELECT code From walkThroughMast order by name;");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResult<WalkThroughMaster_Mdl> getMoveRec(String strKey) {
        DmResult<WalkThroughMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL = null;
        WalkThroughMaster_Mdl mrTemp = new WalkThroughMaster_Mdl();
        List<GrdAuthority_Mdl> auth_Mdl = new ArrayList<>();

        List<GrdDefaultDetail_Mdl> defaultDetail_Mdl = new ArrayList<>();
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = "select code, name,preparedBy,u_Name from walkThroughMast where code=:code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
            }
            psTemp.close();
            rsTemp.close();

            /////Bind Authority Grid
            strSQL = " select authority,linkedUser,v_SNo from walkThroughDetail where code=:code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdAuthority_Mdl authority_Mdll = new GrdAuthority_Mdl();
                authority_Mdll.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("authority")));
                authority_Mdll.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("authority")));
                authority_Mdll.setLinkedUserName(DmShared.xNull(rsTemp.getString("linkedUser")));
                authority_Mdll.setSrlNo(DmShared.xNull(rsTemp.getString("v_SNo")));

                auth_Mdl.add(authority_Mdll);
            }
            mrTemp.getGrdAuthority_Mdl().addAll(auth_Mdl);

            //===============================================================================
            /////Bind Default Detail Grid
            strSQL = "select masd.code,masd.userName,masd.v_Type as voucherCode,vt.description as voucherName,"
                    + "masd.site_Code ,sm.name as siteName,sg.name as departmentName,masd.departmentCode ,"
                    + " masd.valueUpto "
                    + " from walkThroughDefault AS masd "
                    + " LEFT JOIN voucher_type AS vt ON masd.v_Type=vt.v_Type "
                    + " LEFT JOIN siteMast AS sm ON masd.site_Code=sm.code "
                    + " LEFT JOIN departmentMast AS dm ON masd.departmentCode=dm.subCode "
                    + " LEFT JOIN subGroup AS sg ON dm.subCode=sg.subCode "
                    + "  where masd.code=:code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdDefaultDetail_Mdl grdDefault_Mdl = new GrdDefaultDetail_Mdl();
                grdDefault_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("userName")));
                grdDefault_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("userName")));
                grdDefault_Mdl.setV_TypeCode(DmShared.xNull(rsTemp.getString("voucherCode")));
                grdDefault_Mdl.setV_TypeName(DmShared.xNull(rsTemp.getString("voucherName")));
                grdDefault_Mdl.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                grdDefault_Mdl.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                grdDefault_Mdl.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                grdDefault_Mdl.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                grdDefault_Mdl.setValueUpto(DmServer.vNull(rsTemp.getString("valueUpto"), "0.00"));

                defaultDetail_Mdl.add(grdDefault_Mdl);
            }
            mrTemp.getGrdDefaultMdl().addAll(defaultDetail_Mdl);

            //=============================================================================
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT dm.subCode,sg.name "
                    + " FROM departmentMast AS dm "
                    + " LEFT JOIN subGroup AS sg ON dm.subCode=sg.subCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHUser(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("select code,user_Name as name from userMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
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

    @Override
    public DmResult<String> saveRecord(WalkThroughMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL = null;
        ResultSet rsTemp = null;

        dmrTemp.setData("");
        try {
            WalkThroughMaster_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "walkThroughMast", cnTemp));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            strSQL = " SELECT COUNT(name) AS cnt FROM walkThroughMast WHERE LOWER(name) = :Name AND code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

//              strSQL = "select count(v_SNo) as cnt"
//                    + "from walkThroughDetail  "
//                    + "where code = :code ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                if (rsTemp.getLong("cnt") > 1) {
//                    throw new DmException(0, "S.No." + DmShared.iNull(rsTemp.getString("v_SNo")).toString() + " is duplicate");
//                }
//            }
            if (blnAdd) {

                strSQL = "Insert Into walkThroughMast(code ,Name,  "
                        + " preparedBy, u_EntDt) Values  "
                        + "(:code ,:Name,:preparedBy,getdate()) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));

            } else {
                strSQL = "Update walkThroughMast Set "
                        + "Name = :Name, "
                        + "U_Name = :u_Name, "
                        + "U_EntDt = getdate() "
                        + "Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("Name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));

            }
            psTemp.executeUpdate();
            psTemp.close();

            //===================== Approval Grid =========================================
            strSQL = " delete from walkThroughDetail Where Code= :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            List<GrdAuthority_Mdl> listDetail = mrTemp.getGrdAuthority_Mdl();
            for (GrdAuthority_Mdl authority_Mdl : listDetail) {
                if (!DmShared.xNull(authority_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {

                    strSQL = "Insert Into walkThroughDetail(Code,v_SNo,authority,linkedUser) Values  "
                            + "(:Code,:v_SNo,:authority,:linkedUser) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(authority_Mdl.getSrlNo()).toString());
                    psTemp.setString("authority", DmShared.xNull(authority_Mdl.getSharedKeyFieldName()));
                    psTemp.setString("linkedUser", DmShared.xNull(authority_Mdl.getLinkedUserName()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

//             strSQL = "select count(v_SNo) as cnt"
//                    + "from walkThroughDetail  "
//                    + "where code = :code ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                if (rsTemp.getLong("cnt") > 1) {
//                    throw new DmException(0, "S.No." + DmShared.iNull(rsTemp.getString("v_SNo")).toString() + " is duplicate");
//                }
//            }
            //===================================================================================
            // ===================== Default Detail Grid =========================================
            strSQL = " delete from walkThroughDefault Where Code= :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();
            List<GrdDefaultDetail_Mdl> defaultDetail = mrTemp.getGrdDefaultMdl();
            for (GrdDefaultDetail_Mdl detail_Mdl : defaultDetail) {
                if ((!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase(""))
                        || (!DmShared.xNull(detail_Mdl.getV_TypeCode()).trim().equalsIgnoreCase(""))
                        || (!DmShared.xNull(detail_Mdl.getSiteCode()).trim().equalsIgnoreCase(""))
                        || (!DmShared.xNull(detail_Mdl.getDepartmentCode()).trim().equalsIgnoreCase(""))) {
                    strSQL = "Insert Into walkThroughDefault(Code,userName,v_Type,site_Code,departmentCode,valueUpto) Values  "
                            + "(:Code,:userName,:v_Type,:site_Code,:departmentCode,:valueUpto) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("userName", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldName()));
                    psTemp.setString("v_Type", DmShared.foriegnKey(detail_Mdl.getV_TypeCode()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(detail_Mdl.getSiteCode()));
                    psTemp.setString("departmentCode", DmShared.foriegnKey(detail_Mdl.getDepartmentCode()));
                    psTemp.setString("valueUpto", DmServer.vNull(detail_Mdl.getValueUpto(), "0.00"));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            //===================================================================================

//            strSQL = "select count(*) as cnt "
//                    + "from walkThroughDefault   "
//                    + "where code = :code and userName = :userName and v_Type=:v_Type "
//                    + " and site_Code=:site_Code and departmentCode=:departmentCode  ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//
//            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//            List<GrdDefaultDetail_Mdl> defaultDetail1 = mrTemp.getGrdDefaultMdl();
//            for (GrdDefaultDetail_Mdl detail_Mdl1 : defaultDetail1) {
//                psTemp.setString("userName", DmShared.xNull(detail_Mdl1.getSharedKeyFieldName()));
//                psTemp.setString("v_Type", DmShared.foriegnKey(detail_Mdl1.getV_TypeCode()));
//                psTemp.setString("site_Code", DmShared.foriegnKey(detail_Mdl1.getSiteCode()));
//                psTemp.setString("departmentCode", DmShared.foriegnKey(detail_Mdl1.getDepartmentCode()));
//
//                rsTemp = psTemp.executeQuery();
//                while (rsTemp.next()) {
//                    if (rsTemp.getLong("cnt") > 1) {
//                        throw new DmException(0, "default detail  is duplicate");
//                    }
//                }
//            }
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "delete from walkThroughDefault Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from walkThroughDetail Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from walkThroughMast Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
            sqlWithParam.setSQL(" select code, name from walkThroughMast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();

                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT v_Type as code , description as name from voucher_Type ");
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT code, name FROM siteMast ");
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

    @Override
    public DmResultPageing<List<String>> getSuggestionSetupName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select  name From walkThroughMast ");
            helper.addSort("name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
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
