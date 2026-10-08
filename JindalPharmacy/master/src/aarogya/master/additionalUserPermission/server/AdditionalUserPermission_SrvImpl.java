package aarogya.master.additionalUserPermission.server;

import aarogya.master.additionalUserPermission.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdditionalUserPermission_SrvImpl extends DmRSSPrivate implements AdditionalUserPermission_Srv {

    private String serviceName = "", serviceCatName = "";

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLinkedAc(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT sdm.subCODE, sdm.NAME "
                    + " FROM subgroup as sdm "
                    + " left join subGroupSiteList as sgsl on sdm.subCode=sgsl.subCode"
                    + " where  sgsl.site_Code=:siteCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));

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

            sqlWithParam.setSQL("SELECT sdm.subCODE, sdm.NAME "
                    + " FROM departmentMast dm "
                    + " left join subgroup as sdm on dm.subcode = sdm.subcode "
                    + " left join subGroupSiteList as sgsl on sdm.subCode=sgsl.subCode"
                    + " where  sgsl.site_Code=:siteCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNursingStation(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("Select code, name "
                    + " From nursingStationMast where site_Code=:siteCode"
                    + " union all  "
                    + " select -1 as code, '<RESTRICTED>' as name");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
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

    private void getdepartmentList(GrdAdditionalUserPermission_Mdl departmentMappingMdl, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        try {
            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            sqlQuery = "select AllowedDepartment = STUFF((SELECT ', ' + sg.name  "
                    + " FROM subGroup sg  "
                    + " WHERE sg.subCode in (" + DmServer.getParamList(":departmentCode", DmShared.xNull(departmentMappingMdl.getAllowDepartmentCode())) + ") "
                    + " FOR XML PATH(''), TYPE ).value('.[1]','nvarchar(max)'), 1,1,'') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("departmentCode", DmShared.xNull(departmentMappingMdl.getAllowDepartmentCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                departmentMappingMdl.setAllowDepartmentName(DmShared.xNull(rsTemp.getString("AllowedDepartment")));
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.setCatalog(DmServer.dbCompany);
                }
            } catch (Exception e) {
            }
        }

    }

    @Override
    public DmResultPageing<List<GrdAdditionalUserPermission_Mdl>> getFillGrid(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdAdditionalUserPermission_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdAdditionalUserPermission_Mdl> listUser = new ArrayList<GrdAdditionalUserPermission_Mdl>();
        GrdAdditionalUserPermission_Mdl node;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            /*====================================<Get Grid >====================================*/
            sqlQuery = "select	um.user_Name, um.code as userCode, um.description, ual.subCode as linkedAc, sgLA.name as linkedAcName, "
                    + " ual.departmentCode,sgDep.name as departmentName,ual.nursingStationCodeList, "
                    + " ual.allowedDepartmentCodeList as OthDeparmentCode,ual.allowedLocationCodeList, ual.allowedDiscountPercentUpto, ual.allowDiscForNonDiscServ "
                    + " from userMast um "
                    + " left join userLinkAccount ual on um.user_Name = ual.user_Name and ual.pltCode = :pltcode "
                    + " left Join " + DmServer.getSsnDBName(this) + ".dbo.subGroup sgDep on sgdep.subcode= ual.departmentCode  "
                    + " left Join " + DmServer.getSsnDBName(this) + ".dbo.subGroup sgLA on sgLA.subCode = ual.subCode  "
                    //                    + " left Join " + DmServer.getSsnDBName(this) + ".dbo.billHandleLocationMaster bhlm on bhlm.code = ual.allowedDepartmentCodeList  "
                    + " where um.user_Name not in ('SA','SUPER') ";
//                    + " and um.user_Name not in (select distinct umr.user_Name from userMast umr where isNull(umr.user_Role,'') <> '') ";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("pltcode", DmShared.xNull(DmServer.getSsnPLTCode(this)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAdditionalUserPermission_Mdl();
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("User_Name")));
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("userCode")));
                node.setUserDescription(DmShared.xNull(rsTemp.getString("description")));
                node.setLinkedAcCode(DmShared.xNull(rsTemp.getString("linkedAc")));
                node.setLinkedAcName(DmShared.xNull(rsTemp.getString("linkedAcName")));
                node.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                node.setNursingStationCode(DmShared.xNull(rsTemp.getString("nursingStationCodeList")));
                node.setAllowDiscountPercent(DmServer.vNull(rsTemp.getString("allowedDiscountPercentUpto"), "0.00"));
                node.setAllowDiscForNDSCode(DmShared.xNull(rsTemp.getString("allowDiscForNonDiscServ")));
                if (DmShared.iNull(node.getAllowDiscForNDSCode()).intValue() == 0) {
                    node.setAllowDiscForNDSName("No");
                } else {
                    node.setAllowDiscForNDSName("Yes");
                }

                if (!DmShared.xNull(node.getNursingStationCode()).equalsIgnoreCase("")) {
                    node.setNursingStationName(getNursingStationName(node.getNursingStationCode(), cnTemp));
                }

                node.setAllowDepartmentCode(DmShared.xNull(rsTemp.getString("OthDeparmentCode")));
                node.setAllowLocationCode(DmShared.xNull(rsTemp.getString("allowedLocationCodeList")));
//                node.setAllowLocationName(DmShared.xNull(rsTemp.getString("allowedDepartmentName")));
                getdepartmentList(node, cnTemp);
                getLocationList(node, cnTemp);
                serviceName = "";
                serviceCatName = "";
                node.setListServiceDetail(getServiceList(node.getSharedKeyFieldName(), cnTemp));
                node.setListPaymentMode(getPaymentMode(node.getSharedKeyFieldName(), cnTemp));
                if (!DmShared.xNull(serviceName).trim().equalsIgnoreCase("")) {
                    serviceName = serviceName.substring(0, serviceName.length() - 1);
                }
                if (!DmShared.xNull(serviceCatName).trim().equalsIgnoreCase("")) {
                    serviceCatName = serviceCatName.substring(0, serviceCatName.length() - 1);
                }
                node.setServiceName(serviceName);
                node.setServiceCatName(serviceCatName);
                listUser.add(node);
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

            helper.positioning(listUser.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(listUser);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    private String getNursingStationName(String nursingStationCodeList, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", nursingStation = "";
        try {
            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            sqlQuery = "select  code,name "
                    + "         from ("
                    + "             select  code,name "
                    + "                     from  nursingStationMast "
                    + "             union all "
                    + "             select  -1 as code, '<RESTRICTED>' as name "
                    + "         )  as nsm "
                    + "         where  code in (" + DmServer.getParamList(":code", nursingStationCodeList) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("code", nursingStationCodeList);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.xNull(nursingStation).equalsIgnoreCase("")) {
                    nursingStation = DmShared.xNull(rsTemp.getString("name"));
                } else {
                    nursingStation += ", " + DmShared.xNull(rsTemp.getString("name"));
                }
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

        return nursingStation;
    }

    private List<GrdService_Mdl> getServiceList(String userName, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        GrdService_Mdl node;
        List<GrdService_Mdl> listNode = new ArrayList<>();

        try {
            sqlQuery = "select	usp.userName, usp.serviceCode, psm.name as serviceName, usp.serviceCategoryCode, pscm.name as serviceCategoryName "
                    + "		from " + DmServer.getSsnDBName(this) + ".dbo.userServicePermission usp "
                    + "		left join " + DmServer.getSsnDBName(this) + ".dbo.patientServiceMast psm on psm.code=usp.serviceCode "
                    + "		left join " + DmServer.getSsnDBName(this) + ".dbo.patientServiceCatMast pscm on usp.serviceCategoryCode = pscm.code "
                    + "		where usp.userName = :userName "
                    + "		order by pscm.name, psm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(userName).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdService_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("serviceCategoryCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceCategoryName")));
                node.setServiceCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                node.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                if (!DmShared.xNull(rsTemp.getString("serviceName")).trim().equalsIgnoreCase("")) {
                    serviceName += DmShared.xNull(rsTemp.getString("serviceName")).trim() + ",";
                }
                if (!DmShared.xNull(rsTemp.getString("serviceCategoryName")).trim().equalsIgnoreCase("")) {
                    serviceCatName += DmShared.xNull(rsTemp.getString("serviceCategoryName")).trim() + ",";
                }

                listNode.add(node);
            }
            psTemp.close();
            rsTemp.close();

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
        return listNode;
    }

    private List<GridPaymentMode_Mdl> getPaymentMode(String userName, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GridPaymentMode_Mdl> lstNode = new ArrayList<GridPaymentMode_Mdl>();
        GridPaymentMode_Mdl node;
        String sqlQuery = "";

        try {
            /*===========================<< Get Payment Mode detail >>=============================*/
            sqlQuery = "Select	pym.paymentTypeCode, cdm.code,cdm.name AS paymentMode,pym.v_Type,vt.description,pym.isdefault "
                    + "		from userPayModePermission as pym "
                    + "		left join " + DmServer.getSsnDBName(this).trim() + ".dbo.cardTypeMast as cdm on cdm.code = pym.paymentTypeCode "
                    + "		left join " + DmServer.getSsnDBName(this).trim() + ".dbo.voucher_Type as vt on vt.v_Type = pym.v_Type "
                    + "		Where pym.userName = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(userName).trim());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GridPaymentMode_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("paymentTypeCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("paymentMode")));
                node.setVoucherTypeTag(DmShared.xNull(rsTemp.getString("v_Type")));
                node.setVoucherTypeText(DmShared.xNull(rsTemp.getString("description")));
                node.setIsDefaultTag(DmShared.xNull(rsTemp.getString("isdefault")));
                node.setIsDefaultText(DmShared.iNull(rsTemp.getString("isdefault")).intValue() == 1 ? "Yes" : "No");
                lstNode.add(node);
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
        return lstNode;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOtherDepartment(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT sdm.subCODE as code, sdm.NAME as name  "
                    + "                   FROM departmentMast dm   "
                    + "                   left join subgroup as sdm on dm.subcode = sdm.subcode ");

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
    public DmResult<String> saveRecord(GrdAdditionalUserPermission_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;

        dmrTemp.setData("");

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            String[] NursingStationCode = mrTemp.getNursingStationCode().split(",");

            /*====================================< Check whether Record Exist>====================================*/
            sqlQuery = "insert into " + DmServer.dbCompany + ".dbo.userLinkAccount(user_name,pltCode) "
                    + "select	um.user_Name,:pltCode as pltCode  "
                    + "from " + DmServer.dbCompany + ".dbo.userMast um  "
                    + "left join " + DmServer.dbCompany + ".dbo.userLinkAccount ula on um.user_Name = ula.user_Name and ula.pltCode = :pltCode "
                    + "where  isNull(ula.user_Name,'') = '' and um.user_Name = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("pltCode", DmShared.xNull(DmServer.getSsnPLTCode(this)));
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Update If Record Exist>====================================*/
            sqlQuery = "update " + DmServer.dbCompany + ".dbo.userLinkAccount set "
                    + "subCode = :subCode, "
                    + "allowedDiscountPercentUpto = :allowedDiscountPercentUpto, "
                    + "allowedLocationCodeList = :allowedLocationCodeList, "
                    + "departmentCode = :departmentCode, "
                    + "nursingStationCodeList = :nursingStationCodeList, "
                    + "allowedDepartmentCodeList = :allowedDepartmentCodeList, "
                    + "allowDiscForNonDiscServ = :allowDiscForNonDiscServ "
                    + "where pltCode = :pltCode and user_Name = :userName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("pltCode", DmShared.xNull(DmServer.getSsnPLTCode(this)));
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getLinkedAcCode()));
            psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
            psTemp.setString("allowedLocationCodeList", DmShared.foriegnKey(mrTemp.getAllowLocationCode()));
            psTemp.setString("nursingStationCodeList", DmShared.xNull(mrTemp.getNursingStationCode()));
            psTemp.setString("allowedDepartmentCodeList", DmShared.foriegnKey(mrTemp.getAllowDepartmentCode()));
            psTemp.setString("allowedDiscountPercentUpto", DmServer.vNull(mrTemp.getAllowDiscountPercent(), "0.00"));
            psTemp.setString("allowDiscForNonDiscServ", DmShared.xNull(mrTemp.getAllowDiscForNDSCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from userServicePermission >====================================*/
            sqlQuery = "Delete from userServicePermission where userName = :userName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert / Update records into userServicePermission >====================================*/
            for (GrdService_Mdl service_Mdl : mrTemp.getListServiceDetail()) {
                if (DmShared.iNull(service_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    sqlQuery = " Insert into userServicePermission(userName, serviceCode, serviceCategoryCode)  "
                            + " VALUES (:userName, :serviceCode, :serviceCategoryCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("userName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(service_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("serviceCode", DmShared.foriegnKey(service_Mdl.getServiceCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*====================================< Delete records from assignStaff_NS >====================================*/
            sqlQuery = "Delete From assignStaff_NS Where subCode= :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getLinkedAcCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*============================< Insert / Update records into assignStaff_NS >==============================*/
            for (String siteCode : NursingStationCode) {
                if (DmShared.iNull(siteCode).longValue() != 0) {
                    sqlQuery = "insert into assignStaff_NS (subcode,nursingStationCode,u_Name,u_EntDt)  "
                            + " VALUES (:subcode, :nursingStationCode,:u_Name,:u_EntDt)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    if (siteCode.equalsIgnoreCase("-1")) {
                        psTemp.setString("nursingStationCode", null);
                    } else {
                        psTemp.setString("nursingStationCode", DmShared.foriegnKey(siteCode));
                    }
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getLinkedAcCode()));
                    psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            /*======================================================================================================*/

            DmServer.getManageUser().saveRole(mrTemp.getSharedKeyFieldName(), mrTemp.getSharedKeyFieldName(), cnTemp);

            /*======================================================================================================*/
            savePayMode(mrTemp.getListPaymentMode(), mrTemp.getSharedKeyFieldName());

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
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

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

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<TxtHService_Mdl>> getTxtHService(String serviceCategoryCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHService_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHService_Mdl> lstTemp = new ArrayList<TxtHService_Mdl>();
        TxtHService_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = " ";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(serviceCategoryCode).trim().equalsIgnoreCase("")) {
                condition += " AND  pscm.code = :serviceCategoryCode";
            }
            sqlQuery = " select psm.code,psm.name as serviceName,pscm.code as categoryCode, pscm.name as categoryName "
                    + "      from  patientServiceMast psm "
                    + "      left join patientServiceCatMast pscm on psm.categoryCode = pscm.code"
                    + "      left join patientServiceSiteList as pssl on pssl.code=psm.code "
                    + "      where psm.isActive ='1' and  pssl.site_Code=:siteCode " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            if (!DmShared.xNull(serviceCategoryCode).trim().equalsIgnoreCase("")) {
                psTemp.setString("serviceCategoryCode", serviceCategoryCode);
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHService_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceName")));
                listNode.setServiceCatCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                listNode.setServiceCatName(DmShared.xNull(rsTemp.getString("categoryName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "select code, user_Name as name, description from userMast where isActive = 1 and user_Name not in ('SA','SUPER') and user_Name not in (select user_Role from userMast where isNull(user_Role,'') <> '')";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("description")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRole(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select User_Name As Code, User_Name as Name, description From UserMast Where User_Name not in ('SA','SUPER') and isNull(user_Role,'') = '' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("description")));
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
    public DmResult<List<GrdManageUserRole_Mdl>> getFillUserDetail(String roleType) {
        DmResult<List<GrdManageUserRole_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdManageUserRole_Mdl> listUser = new ArrayList<GrdManageUserRole_Mdl>();
        GrdManageUserRole_Mdl grdUser_Mdl;
        dmrTemp.setData(listUser);

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            /*====================================<Get Grid >====================================*/
            if (!DmShared.xNull(roleType).equalsIgnoreCase("<Blank>")) {

                sqlQuery = "select	um.user_Name,um.description "
                        + "from userMast um "
                        + "where  isNull(user_role,'') = :user_Role "
                        + "order by um.user_Name ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("user_Role", DmShared.xNull(roleType));
            } else {

                sqlQuery = "select	um.user_Name,um.description "
                        + "from userMast um "
                        + "where isNull(user_Role,'') = '' "
                        + "order by um.user_Name ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdUser_Mdl = new GrdManageUserRole_Mdl();
                grdUser_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("User_Name")));
                grdUser_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("User_Name")));
                grdUser_Mdl.setDescription(DmShared.xNull(rsTemp.getString("description")));
                listUser.add(grdUser_Mdl);
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

            dmrTemp.setStatus(true);
            dmrTemp.setData(listUser);

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
    public DmResult<String> updateUser(boolean isDelete, GrdManageUserRole_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData("");

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (!isDelete) {
                sqlQuery = "Select code from " + DmServer.dbCompany + ".dbo.userMast where user_Role = :user_Role ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("user_Role", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException("Roled User can not be under Role.");
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "Select code from " + DmServer.dbCompany + ".dbo.userMast where user_name = :user_Role and isNull(user_role,'') <> '' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("user_Role", DmShared.xNull(mrTemp.getRoleName()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException("Roled User can not be under Role.");
                }
                rsTemp.close();
                psTemp.close();
            }
            /*====================================< Update>====================================*/
            sqlQuery = "update " + DmServer.dbCompany + ".dbo.userMast set "
                    + "user_Role = :user_Role "
                    + "where user_name = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!isDelete) {
                psTemp.setString("userName", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("user_Role", DmShared.xNull(mrTemp.getRoleName()));
            } else {
                psTemp.setString("userName", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("user_Role", null);
            }

            psTemp.executeUpdate();
            psTemp.close();

            if (!isDelete) {
                DmServer.getManageUser().saveRole(mrTemp.getRoleName(), mrTemp.getRoleName(), cnTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscForNDS(DmHelper helper, boolean blnToEndOfFile) {
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocationAllowed(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT  bhlm.code, bhlm.name  "
                    + "                   FROM billHandleLocationMaster bhlm   "
            );

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

    private void getLocationList(GrdAdditionalUserPermission_Mdl locationMappingMdl, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        try {
            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            sqlQuery = "select AllowedLocation = STUFF((SELECT ', ' + sg.name  "
                    + " FROM billHandleLocationMaster sg  "
                    + " WHERE sg.code in (" + DmServer.getParamList(":locationCode", DmShared.xNull(locationMappingMdl.getAllowLocationCode())) + ") "
                    + " FOR XML PATH(''), TYPE ).value('.[1]','nvarchar(max)'), 1,1,'') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("locationCode", DmShared.xNull(locationMappingMdl.getAllowLocationCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                locationMappingMdl.setAllowLocationName(DmShared.xNull(rsTemp.getString("AllowedLocation")));
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.setCatalog(DmServer.dbCompany);
                }
            } catch (Exception e) {
            }
        }

    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentMode(DmHelper pageing, boolean blnToEndOfFile, String v_Type) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        String condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(v_Type).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Voucher Type is mandatory.");
            }

            String paymode = DmServer.executeScalar("select replace(attachPaymentMode,'|',',')  from voucher_Type where v_Type=" + v_Type + "", cnTemp);

            if (!paymode.equalsIgnoreCase("")) {
                condition = "and cdm.code in (" + paymode + ")";
            } else {
                condition = "";
            }

            sqlWithParam.setSQL("select cdm.code,cdm.name From cardTypeMast cdm where cdm.isActive = 1 " + condition + " ");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
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

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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

    private void savePayMode(List<GridPaymentMode_Mdl> mrTemp, String userName) throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

//            String prevVType = "";
//            String currVType = "";
            int isDefault = 0;
            List<String> vType = new ArrayList<>();
            List<String> FinalvType = new ArrayList<>();

            if (mrTemp.size() > 1) {
                for (GridPaymentMode_Mdl node : mrTemp) {

                    if (!DmShared.xNull(node.getVoucherTypeText()).equalsIgnoreCase("")) {

                        if (vType.size() == 0) {
                            vType.add(node.getVoucherTypeText());
                        } else {

                            for (String node1 : vType) {
                                if (!node1.equalsIgnoreCase(node.getVoucherTypeText())) {
                                    vType.add(node.getVoucherTypeText());
                                    break;
                                }

                            }

                        }

                    }

                }

                for (String node : vType) {
                    for (GridPaymentMode_Mdl node1 : mrTemp) {

                        if (!DmShared.xNull(node1.getVoucherTypeText()).equalsIgnoreCase("")) {

                            if (node.equalsIgnoreCase(node1.getVoucherTypeText())) {
                                if (DmShared.iNull(node1.getIsDefaultTag()).intValue() == 1) {
                                    FinalvType.add(node1.getVoucherTypeText());
                                }
                            }
                        }
                    }
                }

                boolean flag = false;
                if (vType.size() != FinalvType.size() && FinalvType.size() > 0) {

                    for (String node : vType) {

                        flag = false;

                        for (String node1 : FinalvType) {
                            if (node1.equalsIgnoreCase(node)) {
                                flag = true;

                            }
                        }

                        if (!flag) {
                            throw new UnsupportedOperationException("Default is not set for voucher Type " + node);
                        }
                    }

                } else if (FinalvType.size() == 0) {
                    throw new UnsupportedOperationException("One payment mode must be default.");
                }
            }
            sqlQuery = "Delete from userPayModePermission where userName = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(userName).trim());
            psTemp.executeUpdate();
            psTemp.close();

            /*=======================< Insert record into userPayModePermission >========================*/
            for (int i = 0; i < mrTemp.size(); i++) {
                if (!DmShared.xNull(mrTemp.get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {

                    sqlQuery = "Insert Into userPayModePermission(paymentTypeCode, userName,v_Type,isdefault) "
                            + " Values (:paymentTypeCode, :userName,:v_Type,:isdefault) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("paymentTypeCode", DmShared.xNull(mrTemp.get(i).getSharedKeyFieldCode()).toString());
                    psTemp.setString("userName", DmShared.xNull(DmShared.xNull(userName)));
                    psTemp.setString("v_Type", DmShared.xNull(DmShared.xNull(mrTemp.get(i).getVoucherTypeTag()).toString()));
                    psTemp.setString("isdefault", DmShared.xNull(DmShared.xNull(mrTemp.get(i).getIsDefaultTag()).toString()));

                    psTemp.executeUpdate();
                    psTemp.close();

                }
            }

            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            cnTemp.close();
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select vt.v_Type AS code ,vt.description as name from voucher_Type vt");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
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

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsDefault(DmHelper pageing, boolean blnToEndOfFile) {
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

            lstTemp = DmShared.filterKeyFieldList(pageing, lstTemp);
            rstTemp.setData(lstTemp);

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
