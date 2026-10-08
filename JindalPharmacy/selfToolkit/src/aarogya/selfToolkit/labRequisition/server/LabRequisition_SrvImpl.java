package aarogya.selfToolkit.labRequisition.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.labRequisition.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
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

public class LabRequisition_SrvImpl extends DmRSSPrivate implements LabRequisition_Srv {

    private String vCat = "'" + new ProjectVoucherCategory().LAB_REQUISITION + "'";

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getDepartment(DmHelper helper, boolean blnEndofFile) {
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

            sqlWithParam.setSQL("SELECT sdm.subCODE, sdm.NAME "
                    + "                 FROM departmentMast dm "
                    + "                 left join subgroup sdm on dm.subcode = sdm.subcode "
                    + "                 WHERE dm.nature IN ('Pharmacy') ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnEndofFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnEndofFile);
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
    public DmResultPageing<List<KeyFieldExtended>> getRequisitionBy(DmHelper helper, boolean blnEndofFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" SELECT sg.subCode as code, sg.name, sg1.departmentCode, sdm.name as departmentName "
                    + "        FROM subGroup AS sg "
                    + "        LEFT JOIN subgroupNature AS sgn ON sg.nature = sgn.code "
                    + "        left join subGroupSiteList as sgsl on sg.subCode = sgsl.subCode "
                    + "	       LEFT JOIN subGroup2 AS sg1 ON sg.subCode=sg1.subCode "
                    + "        LEFT JOIN departmentMast AS dm ON sg1.departmentCode=dm.subCode "
                    + "        left join subgroup sdm on dm.subcode = sdm.subcode "
                    + "        WHERE sgn.manualCode='EM' and sgsl.site_Code = :site_Code and sg.isActive = 1 ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnEndofFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("departmentName")));
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnEndofFile);
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
    public DmResult<DefaultValue_Mdl> getDefalutValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        String[] dateTime;

        try {
            dvm.setDtVoucher(DmServer.getServerDateTime()[0]);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT sg.name, ula.subCode "
                    + " FROM " + DmServer.dbCompany + ".dbo.userLinkAccount AS ula "
                    + " LEFT JOIN " + DmServer.getSsnDBName(this) + ".dbo.subGroup AS sg ON sg.subCode = ula.subCode "
                    + " WHERE ula.user_Name = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dvm.setRequestedByTag(DmShared.iNull(rsTemp.getString("subCode")).toString());
                dvm.setRequestedByText(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select  sgLDept.name as localDeptName, nsm.localPharmacyDepartmentCode as localDept "
                    + "         from nursingStationMast as nsm "
                    + "         left join subgroup as sgLDept on nsm.localPharmacyDepartmentCode=sgLDept.subCode "
                    + "         where nsm.code=:nursingStation ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("nursingStation", STServer.getSsnNursingStationCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dvm.setDepartmentByTag(DmShared.iNull(rsTemp.getString("localDept")).toString());
                dvm.setDepartmentByText(DmShared.xNull(rsTemp.getString("localDeptName")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select code, name from siteMast where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dvm.setDefaultSiteCode(DmShared.iNull(rsTemp.getString("code")).toString());
                dvm.setDefaultSiteName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT vt.v_Type, vt.description FROM voucher_Type vt WHERE vt.category in (" + vCat + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dvm.setVoucherTag(DmShared.iNull(rsTemp.getString("v_Type")).toString());
                dvm.setVoucherText(DmShared.xNull(rsTemp.getString("description")));
            }
            rsTemp.close();
            psTemp.close();

            dateTime = DmServer.getServerDateTime();

            dvm.setCurrTime(dateTime[1]);

            dmrTemp.setData(dvm);
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResultPageing<List<TxtHTest_Mdl>> getTxtHTest(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTest_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTest_Mdl> lstTemp = new ArrayList<TxtHTest_Mdl>();
        TxtHTest_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select	psm.code, psm.name, psm.categoryCode, pscm.name as categoryName,pscm.defaultDepartmentCode,sg.name as defaultDepartmentName,"
                    + "         envDept.link_Site_Code,sm1.name as siteName "
                    + "		from patientServiceMast as psm "
                    + "		left join patientServiceCatMast as pscm on psm.categoryCode = pscm.code"
                    + " 	left join subgroup as sg   on pscm.defaultDepartmentCode = sg.subcode"
                    + " 	left join enviroDepartmentSiteLink as envDept   on pscm.defaultDepartmentCode = envDept.departmentCode and envDept.site_Code = :siteCode "
                    + "         left join siteMast as sm1 on  envDept.link_Site_Code=sm1.code "
                    + "		where psm.isActive = 1 and psm.serviceNature in(" + ServiceNature.PATHOLOGY_TEST.getId() + "," + ServiceNature.RADIOLOGY_TEST.getId() + ") ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHTest_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setCategoryTag(DmShared.xNull(rsTemp.getString("categoryCode")));
                listNode.setCategoryText(DmShared.xNull(rsTemp.getString("categoryName")));
                listNode.setDefaultDepartmentCode(DmShared.xNull(rsTemp.getString("defaultDepartmentCode")));
                listNode.setDefaultDepartmentName(DmShared.xNull(rsTemp.getString("defaultDepartmentName")));
                listNode.setSiteCode(DmShared.xNull(rsTemp.getString("link_Site_Code")));
                listNode.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
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
    public DmResult<String> saveRecord(LabRequisition_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult<String>();
        String docId = "", sqlQuery;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        Integer srNo = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (mrTemp.getLstRequisition() != null) {//check whether model is empty then only insertion in grid is possible
                String departmentCode = mrTemp.getLstRequisition().get(0).getDefaultDepartmentCode();
                String siteCode = mrTemp.getLstRequisition().get(0).getSiteCode();
                String departmentCodeNew = departmentCode;

                while (DmShared.iNull(departmentCodeNew).longValue() > 0) {
                    /*==============================<< Generate docId for new record >>====================================*/
                    mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this));
                    mrTemp.setRecIdRequisition(DmServer.getRecId("requisitionService", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdRequisition(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true));
                    mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdRequisition().getRecIdValue()));

                    if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {//incase failure in generating docId
                        throw new UnsupportedOperationException("Invalid key.");
                    }

                    sqlQuery = "Delete From requisitionServiceDetail Where docId= :docId ";//delete values from table
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "Insert into requisitionService (docId, v_Type, v_No, recId,recidPrefix, v_Date, v_Prefix, site_Code, preparedBy,"
                            + " u_EntDt, u_EntDt_LatestLine, approvedBy, approvalDateTime, regDocId, transferDt, "
                            + " departmentcode,requestedbycode,departmentToCode,site_Code_To,priorityTypeCode)"
                            + " values (:docId, :v_Type, :v_No, :recId, :recidPrefix, :v_Date, :v_Prefix, :site_Code, :preparedBy,"
                            + " :u_EntDt, :u_EntDt, :approvedBy, :u_EntDt, :regDocId, null, :departmentcode, "
                            + " :requestedbycode, :departmentToCode, :site_Code_To, :priorityTypeCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_Type", DmShared.xNull(mrTemp.getDefaultValue().getVoucherTag()));
                    psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdRequisition().getCounter()).toString());
                    psTemp.setString("recId", DmShared.xNull(mrTemp.getRecIdRequisition().getRecIdValue()));
                    psTemp.setString("recidPrefix", DmShared.xNull(mrTemp.getRecIdRequisition().getPrefix()));
                    psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDefaultValue().getDtVoucher()));
                    psTemp.setString("v_Prefix", DmShared.xNull(voucherPrefix));
//                    psTemp.setString("presDocId", DmShared.foriegnKey(mrTemp.getDocIdPrescription()));
                    psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getDocIdRegistration()));
                    psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
                    psTemp.setString("preparedby", DmShared.xNull(DmServer.getSsnUserName(this)));
                    psTemp.setString("approvedBy", DmServer.getSsnUserName(this));
                    psTemp.setString("requestedbycode", DmShared.foriegnKey(mrTemp.getRequisitionByTag()));
                    psTemp.setString("departmentcode", DmShared.foriegnKey(mrTemp.getDepartmentByTag()));
                    psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                    psTemp.setString("departmentToCode", departmentCodeNew);
                    if (!DmShared.xNull(siteCode).equalsIgnoreCase("")) {
                        psTemp.setString("site_Code_To", DmShared.foriegnKey(siteCode));
                    } else {
                        psTemp.setString("site_Code_To", DmServer.getSsnSiteCode(this));
                    }
                    psTemp.setString("priorityTypeCode", DmShared.foriegnKey(DmShared.iNull(mrTemp.getPriorityTypeCode()).toString()));
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();

                    departmentCode = departmentCodeNew;
                    departmentCodeNew = "";
                    siteCode = "";
                    srNo = 0;
                    for (GrdLabRequisition_Mdl grdReq : mrTemp.getLstRequisition()) {
                        if (DmShared.iNull(grdReq.getSharedKeyFieldCode()).longValue() > 0
                                && DmShared.iNull(grdReq.getDefaultDepartmentCode()).longValue() == DmShared.iNull(departmentCode).longValue()
                                && DmShared.iNull(grdReq.getReqDocId()).longValue() == 0) {

                            srNo++;
                            sqlQuery = "Insert into requisitionServiceDetail (docId, v_SNo, serviceCode, reqDate, reqTime, reqQty, remark, isStatus)"
                                    + " values (:docId, :v_sno, :serviceCode, :reqDate,:reqTime,:reqQty, :remark,:isStatus)";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setString("v_sno", DmShared.xNull(Integer.toString(srNo)));
                            psTemp.setString("serviceCode", DmShared.foriegnKey(grdReq.getSharedKeyFieldCode()));
                            psTemp.setDate("reqDate", DmServer.convertDate(grdReq.getReqDate()));
                            psTemp.setString("reqTime", DmServer.vNull(grdReq.getReqTime(), "00.00"));
                            psTemp.setString("reqQty", DmServer.vNull("1", "0.00"));
                            psTemp.setString("remark", DmShared.xNull(grdReq.getRemark()));
                            psTemp.setString("isStatus", DmShared.xNull("0"));
                            psTemp.executeUpdate();
                            psTemp.close();

                            grdReq.setReqDocId(mrTemp.getSharedKeyFieldCode());

                        } else if (DmShared.iNull(grdReq.getSharedKeyFieldCode()).longValue() > 0
                                && DmShared.iNull(grdReq.getReqDocId()).longValue() == 0) {
                            departmentCodeNew = DmShared.iNull(grdReq.getDefaultDepartmentCode()).toString();
                            siteCode = DmShared.iNull(grdReq.getSiteCode()).toString();
                        }
                    }
                    mrTemp.setRecIdRequisition(new RecId());
                }
            }

            dmrTemp.setData(docId);
            dmrTemp.setStatus(true);
            spBegin.commit();
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    public DmResultPageing<List<TxtHPriority_Mdl>> getTxtHPriority(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPriority_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPriority_Mdl> lstTemp = new ArrayList<TxtHPriority_Mdl>();
        TxtHPriority_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select code, name, shortName, priorityIndex from hl7Mast where tableName='priorityType' and isActive = 1 ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHPriority_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setPriorityFullName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setPriorityIndex(DmShared.xNull(rsTemp.getString("priorityIndex")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

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

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteTo(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT sm.code AS CODE,sm.name AS Name "
                    + "FROM sitemast sm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public String[] getServerDateTime() {
        return DmServer.getServerDateTime();
    }

}
