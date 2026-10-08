package aarogya.master.categoryPoolMaster.server;

import aarogya.master.categoryPoolMaster.shared.Find_Mdl;
import aarogya.master.categoryPoolMaster.shared.CategoryPoolMaster_Mdl;
import aarogya.master.categoryPoolMaster.shared.CategoryPoolMaster_Vld;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.master.categoryPoolMaster.shared.CategoryPoolMaster_Srv;

public class CategoryPoolMaster_SrvImpl extends DmRSSPrivate implements CategoryPoolMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT pm.code FROM poolMast pm order by pm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
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
    public DmResult<CategoryPoolMaster_Mdl> getMoveRec(String strKey) {
        DmResult<CategoryPoolMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CategoryPoolMaster_Mdl mrTemp = new CategoryPoolMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select	pm.organizationCode, org.name as organizationName, pm.code, pm.name, pm.serviceCategoryCode,pm.headerConsultantIsAmount, "
                    + "          pm.roomCategoryCode, pscm.name as serviceCategoryName, pm.serviceCode,psm.name as serviceName, "
                    + "         pm.serviceGroupCode, psg.name as groupName, pm.headerConsultantAmount, pm.headerReferredIsAmount, pm.headerReferredAmount, "
                    + "		pm.lineConsultantIsAmount, pm.lineConsultantAmount, pm.lineReferredIsAmount, pm.lineReferredAmount ,"
                    + "		pm.moduleName, rcm.name roomCategoryName, pm.preparedBy, pm.u_Name "
                    + "		from poolMast as pm "
                    + "		left join roomCatMast rcm ON pm.roomCategoryCode = rcm.code  "
                    + "		left join subGroup as org on pm.organizationCode = org.subCode "
                    + "		left join serviceGroup as psg on pm.serviceGroupCode = psg.code "
                    + "		left join patientServiceMast as psm on pm.serviceCode = psm.code "
                    + "		left join patientServiceCatMast as pscm on pm.serviceCategoryCode = pscm.code "
                    + "		where pm.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setServiceCatCode(DmShared.xNull(rsTemp.getString("serviceCategoryCode")));
                mrTemp.setServiceCatName(DmShared.xNull(rsTemp.getString("serviceCategoryName")));
                mrTemp.setCategoryTag(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
                mrTemp.setCategoryText(DmShared.xNull(rsTemp.getString("roomCategoryName")));
                mrTemp.setOrgCode(DmShared.xNull(rsTemp.getString("organizationCode")));
                mrTemp.setOrgName(DmShared.xNull(rsTemp.getString("organizationName")));
                mrTemp.setModuleName(DmShared.xNull(rsTemp.getString("moduleName")));
                mrTemp.setServiceCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                mrTemp.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                mrTemp.setGroupCode(DmShared.xNull(rsTemp.getString("serviceGroupCode")));
                mrTemp.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));

                mrTemp.setHeaderConTypeCode(DmShared.xNull(rsTemp.getString("headerConsultantIsAmount")));
                mrTemp.setHeaderConTypeName(DmShared.vNull(rsTemp.getString("headerConsultantIsAmount")).doubleValue() == 1 ? "Fix" : "%");
                mrTemp.setHeaderConAmount(DmServer.vNull(rsTemp.getString("headerConsultantAmount"), "0.00"));

                mrTemp.setHeaderRefTypeCode(DmShared.xNull(rsTemp.getString("headerReferredIsAmount")));
                mrTemp.setHeaderRefTypeName(DmShared.vNull(rsTemp.getString("headerReferredIsAmount")).doubleValue() == 1 ? "Fix" : "%");
                mrTemp.setHeaderRefAmount(DmServer.vNull(rsTemp.getString("headerReferredAmount"), "0.00"));

                mrTemp.setLineConTypeCode(DmShared.xNull(rsTemp.getString("lineConsultantIsAmount")));
                mrTemp.setLineConTypeName(DmShared.vNull(rsTemp.getString("lineConsultantIsAmount")).doubleValue() == 1 ? "Fix" : "%");
                mrTemp.setLineConAmount(DmServer.vNull(rsTemp.getString("lineConsultantAmount"), "0.00"));

                mrTemp.setLineRefTypeCode(DmShared.xNull(rsTemp.getString("lineReferredIsAmount")));
                mrTemp.setLineRefTypeName(DmShared.vNull(rsTemp.getString("lineReferredIsAmount")).doubleValue() == 1 ? "Fix" : "%");
                mrTemp.setLineRefAmount(DmServer.vNull(rsTemp.getString("lineReferredAmount"), "0.00"));

                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
            }
            rsTemp.close();
            psTemp.close();

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
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            //**********************************< Delete record from roomMast >***********************************
            sqlQuery = " delete from poolMast where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResult<String> saveRecord(CategoryPoolMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        String condition = "";

        try {
            CategoryPoolMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
//            mrTemp.setRoomDesc(DmShared.removeSpace(mrTemp.getRoomDesc()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "poolMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

//            sqlQuery = " SELECT COUNT(name) AS cnt FROM poolMast "
//                    + " WHERE serviceCategoryCode =:serviceCategoryCode AND roomCategoryCode=:roomCategoryCode AND code <> :code ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("serviceCategoryCode", DmShared.xNull(mrTemp.getServiceTag()));
//            psTemp.setString("roomCategoryCode", DmShared.xNull(mrTemp.getCategoryTag()));
//            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//            rsTemp = psTemp.executeQuery();
//            rsTemp.next();
//            if (rsTemp.getLong("cnt") > 0) {
//                throw new UnsupportedOperationException("Record already exists for " + mrTemp.getServiceText() + " and " + mrTemp.getCategoryText() + ".");
//
//            }
//            rsTemp.close();
//            psTemp.close();
            if (DmShared.iNull(mrTemp.getServiceCatCode()).longValue() > 0) {
                condition += " AND  pm.serviceCategoryCode = :serviceCategoryCode ";
            }
            if (DmShared.iNull(mrTemp.getCategoryTag()).longValue() > 0) {
                condition += " AND  pm.roomCategoryCode = :roomCategoryCode ";
            }
            if (DmShared.iNull(mrTemp.getOrgCode()).longValue() > 0) {
                condition += " AND  pm.organizationCode = :organizationCode ";
            }
            if (DmShared.xNull(mrTemp.getModuleName()).equalsIgnoreCase("")) {
                condition += " AND  pm.moduleName = :moduleName ";
            }
            if (DmShared.iNull(mrTemp.getGroupCode()).longValue() > 0) {
                condition += " AND  pm.serviceGroupCode = :serviceGroupCode ";
            }
            if (DmShared.iNull(mrTemp.getServiceCode()).longValue() > 0) {
                condition += " AND  pm.serviceCode = :serviceCode ";
            }

            sqlQuery = " SELECT COUNT(*) AS cnt FROM poolMast pm "
                    + "  WHERE  code <> :code  " + condition;
            //                    serviceCategoryCode = :serviceCategoryCode "
            //                    + "  AND roomCategoryCode = :roomCategoryCode "
            //                    + "  AND organizationCode = :organizationCode "
            //                    + "  AND moduleName = :moduleName "
            //                    + "  AND serviceGroupCode = :serviceGroupCode "
            //                    + "  AND serviceCode = :serviceCode "
//                    + "  code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            if (DmShared.iNull(mrTemp.getGroupCode()).longValue() > 0) {
                psTemp.setString("serviceGroupCode", DmShared.foriegnKey(mrTemp.getGroupCode()));
            }
            if (DmShared.iNull(mrTemp.getServiceCode()).longValue() > 0) {
                psTemp.setString("serviceCode", DmShared.foriegnKey(mrTemp.getServiceCode()));
            }
            if (DmShared.iNull(mrTemp.getServiceCatCode()).longValue() > 0) {
                psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(mrTemp.getServiceCatCode()));
            }
            if (DmShared.iNull(mrTemp.getCategoryTag()).longValue() > 0) {
                psTemp.setString("roomCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
            }
            if (DmShared.iNull(mrTemp.getOrgCode()).longValue() > 0) {
                psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrgCode()));
            }
            if (DmShared.xNull(mrTemp.getModuleName()).equalsIgnoreCase("")) {
                psTemp.setString("moduleName", DmShared.xNull(mrTemp.getModuleName()));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();
            //**********************************< Insert & Update record into roomMast >***********************************
            mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getServiceCatName() + "  " + mrTemp.getCategoryText()));

            if (blnAdd) {
                sqlQuery = "insert into poolMast(code, name, serviceCategoryCode, roomCategoryCode,organizationCode,headerConsultantIsAmount, headerConsultantAmount, headerReferredIsAmount, headerReferredAmount, "
                        + " lineConsultantIsAmount, lineConsultantAmount, lineReferredIsAmount, lineReferredAmount,moduleName,serviceGroupCode, serviceCode,  u_EntDt, preparedBy)  "
                        + "values(:code, :name, :serviceCategoryCode, :roomCategoryCode,:organizationCode,:headerConsultantIsAmount, :headerConsultantAmount, :headerReferredIsAmount, :headerReferredAmount, "
                        + " :lineConsultantIsAmount, :lineConsultantAmount, :lineReferredIsAmount, :lineReferredAmount,:moduleName,:serviceGroupCode, :serviceCode,:u_EntDt, :userName) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(mrTemp.getServiceCatCode()));
                psTemp.setString("roomCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
                psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrgCode()));
                psTemp.setString("headerConsultantIsAmount", DmShared.iNull(mrTemp.getHeaderConTypeCode()).toString());
                psTemp.setString("headerConsultantAmount", DmServer.vNull(mrTemp.getHeaderConAmount(), "0.00"));
                psTemp.setString("headerReferredIsAmount", DmShared.iNull(mrTemp.getHeaderRefTypeCode()).toString());
                psTemp.setString("headerReferredAmount", DmServer.vNull(mrTemp.getHeaderRefAmount(), "0.00"));
                psTemp.setString("lineConsultantIsAmount", DmShared.iNull(mrTemp.getLineConTypeCode()).toString());
                psTemp.setString("lineConsultantAmount", DmServer.vNull(mrTemp.getLineConAmount(), "0.00"));
                psTemp.setString("lineReferredIsAmount", DmShared.iNull(mrTemp.getLineRefTypeCode()).toString());
                psTemp.setString("lineReferredAmount", DmServer.vNull(mrTemp.getLineRefAmount(), "0.00"));
                psTemp.setString("moduleName", DmShared.xNull(mrTemp.getModuleName()));
                psTemp.setString("serviceGroupCode", DmShared.foriegnKey(mrTemp.getGroupCode()));
                psTemp.setString("serviceCode", DmShared.foriegnKey(mrTemp.getServiceCode()));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "update poolMast set "
                        + " name = :name, "
                        + " serviceCategoryCode = :serviceCategoryCode, "
                        + " roomCategoryCode = :roomCategoryCode, "
                        + " organizationCode = :organizationCode, "
                        + " headerConsultantIsAmount = :headerConsultantIsAmount, "
                        + " headerConsultantAmount = :headerConsultantAmount, "
                        + " headerReferredIsAmount = :headerReferredIsAmount, "
                        + " headerReferredAmount = :headerReferredAmount, "
                        + " lineConsultantIsAmount = :lineConsultantIsAmount, "
                        + " lineConsultantAmount = :lineConsultantAmount, "
                        + " lineReferredIsAmount = :lineReferredIsAmount, "
                        + " lineReferredAmount = :lineReferredAmount, "
                        + " moduleName = :moduleName, "
                        + " serviceGroupCode = :serviceGroupCode, "
                        + " serviceCode = :serviceCode, "
                        + " u_Name = :userName, "
                        + " u_EntDt = :u_EntDt "
                        + " where code = :code ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(mrTemp.getServiceCatCode()));
                psTemp.setString("roomCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
                psTemp.setString("organizationCode", DmShared.foriegnKey(mrTemp.getOrgCode()));
                psTemp.setString("headerConsultantIsAmount", DmShared.iNull(mrTemp.getHeaderConTypeCode()).toString());
                psTemp.setString("headerConsultantAmount", DmServer.vNull(mrTemp.getHeaderConAmount(), "0.00"));
                psTemp.setString("headerReferredIsAmount", DmShared.iNull(mrTemp.getHeaderRefTypeCode()).toString());
                psTemp.setString("headerReferredAmount", DmServer.vNull(mrTemp.getHeaderRefAmount(), "0.00"));
                psTemp.setString("lineConsultantIsAmount", DmShared.iNull(mrTemp.getLineConTypeCode()).toString());
                psTemp.setString("lineConsultantAmount", DmServer.vNull(mrTemp.getLineConAmount(), "0.00"));
                psTemp.setString("lineReferredIsAmount", DmShared.iNull(mrTemp.getLineRefTypeCode()).toString());
                psTemp.setString("lineReferredAmount", DmServer.vNull(mrTemp.getLineRefAmount(), "0.00"));
                psTemp.setString("moduleName", DmShared.xNull(mrTemp.getModuleName()));
                psTemp.setString("serviceGroupCode", DmShared.foriegnKey(mrTemp.getGroupCode()));
                psTemp.setString("serviceCode", DmShared.foriegnKey(mrTemp.getServiceCode()));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            }
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile
    ) {
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
            sqlWithParam.setSQL("select pm.code, pm.name, pm.organizationCode, isnull(org.name, '') as organizationName, pm.serviceCategoryCode, pm.u_Name,\n"
                    + "		isnull(pscm.name, '') as serviceCategoryName, pm.serviceCode, isnull(psm.name, '') as serviceName, pm.serviceGroupCode, \n"
                    + "		isnull(psg.name, '') as groupName, pm.headerConsultantAmount, pm.headerReferredAmount, pm.lineConsultantAmount, pm.preparedBy, \n"
                    + "		pm.lineReferredAmount, isnull(pm.moduleName, '') as moduleName, pm.roomCategoryCode, isnull(rcm.name, '') as roomCategoryName, \n"
                    + "		case pm.headerConsultantIsAmount when '1' then 'Fix' else '%' end as headerConsultantIsAmount, \n"
                    + "		case pm.headerReferredIsAmount when '1' then 'Fix' else '%' end as headerReferredIsAmount, \n"
                    + "		case pm.lineConsultantIsAmount when '1' then 'Fix' else '%' end as lineConsultantIsAmount, \n"
                    + "		case pm.lineReferredIsAmount when '1' then 'Fix' else '%' end as lineReferredIsAmount \n"
                    + "		from poolMast pm \n"
                    + "		left join roomCatMast rcm ON pm.roomCategoryCode = rcm.code \n"
                    + "		left join subGroup as org on pm.organizationCode = org.subCode \n"
                    + "		left join serviceGroup as psg on pm.serviceGroupCode = psg.code \n"
                    + "		left join patientServiceMast as psm on pm.serviceCode = psm.code \n"
                    + "		left join patientServiceCatMast as pscm on pm.serviceCategoryCode = pscm.code ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setServiceText(DmShared.xNull(rsTemp.getString("serviceCategoryName")));
                listNode.setCategoryText(DmShared.xNull(rsTemp.getString("roomCategoryName")));
                listNode.setOrgName(DmShared.xNull(rsTemp.getString("organizationName")));
                listNode.setModuleName(DmShared.xNull(rsTemp.getString("moduleName")));
                listNode.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                listNode.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setHeaderConTypeName(DmShared.xNull(rsTemp.getString("headerConsultantIsAmount")));
                listNode.setHeaderRefTypeName(DmShared.xNull(rsTemp.getString("headerReferredIsAmount")));
                listNode.setLineConTypeName(DmShared.xNull(rsTemp.getString("lineConsultantIsAmount")));
                listNode.setLineRefTypeName(DmShared.xNull(rsTemp.getString("lineReferredIsAmount")));

//                  mrTemp.setHeaderConAmount(DmServer.vNull(rsTemp.getString("headerConsultantAmount"), "0.00"));
                listNode.setHeaderConsultantAmount(DmServer.vNull(rsTemp.getString("headerConsultantAmount"), "0.00"));
                listNode.setHeaderReferredAmount(DmServer.vNull(rsTemp.getString("headerReferredAmount"), "0.00"));
                listNode.setLineConsultantAmount(DmServer.vNull(rsTemp.getString("lineConsultantAmount"), "0.00"));
                listNode.setLineReferredAmount(DmServer.vNull(rsTemp.getString("lineReferredAmount"), "0.00"));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile
    ) {
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
            sqlWithParam.setSQL(" select code, name from roomCatMast ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPoolPer(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("% On Amount");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Fix");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCat(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, name from patientServiceCatMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("%");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Fix");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEmpanelment(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  om.subCode as code, sg.name "
                    + "         from organizationMast as om "
                    + "         left join subGroup as sg on sg.subCode = om.subCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("OPD");
            listNode.setSharedKeyFieldName("OPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("IPD");
            listNode.setSharedKeyFieldName("IPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Day Care");
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Emergency");
            listNode.setSharedKeyFieldName("Emergency");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Diagnostic");
            listNode.setSharedKeyFieldName("Diagnostic");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceGroup(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, name from serviceGroup ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHService(DmHelper helper, boolean blnToEndOfFile
    ) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, name from patientServiceMast where isActive = 1 ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
