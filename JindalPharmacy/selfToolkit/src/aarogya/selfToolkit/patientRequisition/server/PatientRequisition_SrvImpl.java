package aarogya.selfToolkit.patientRequisition.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.patientRequisition.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
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

public class PatientRequisition_SrvImpl extends DmRSSPrivate implements PatientRequisition_Srv {

    private String vCat = "'" + new ProjectVoucherCategory().PHARMACY_REQUISITION + "'";

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getDepartmentBy(DmHelper helper, boolean blnEndofFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String ondition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT sdm.subCODE, sdm.NAME "
                    + "FROM departmentMast dm "
                    + "left join subgroup sdm on dm.subcode = sdm.subcode "
                    + " left join subGroupNature as sgn on sgn.code = sdm.nature "
                    + "WHERE sgn.manualCode IN ('CM','PH') ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnEndofFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnEndofFile);
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
    public DmResultPageing<List<KeyFieldExtended>> getDepartmentTo(DmHelper helper, boolean blnEndofFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null, rsTemp1 = null, rsTemp2 = null;
        DmPreparedStatement psTemp = null, psTemp1 = null, psTemp2 = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String departmentCode = "", sqlQuery = "", sqlQuery1 = "";
        String[] deaprtmentCodeArray;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT  ula.allowedDepartmentCodeList,sg.name "
                    + " FROM " + DmServer.dbCompany + ".dbo.userLinkAccount AS ula "
                    + " LEFT JOIN " + DmServer.getSsnDBName(this) + ".dbo.subGroup AS sg ON sg.subCode = ula.subCode "
                    + " WHERE ula.user_Name = :userName ");
            sqlWithParam.setString("userName", DmServer.getSsnUserName(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnEndofFile, this);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                departmentCode = DmShared.xNull(rsTemp.getString("allowedDepartmentCodeList"));
                if (!DmShared.xNull(departmentCode).equalsIgnoreCase("")) {
                    deaprtmentCodeArray = departmentCode.split(",");
                    if (deaprtmentCodeArray != null) {
                        for (String code : deaprtmentCodeArray) {
                            if (DmShared.iNull(code).longValue() > 0) {
                                sqlQuery = "SELECT dm.subCode,sg.name,envDept.link_Site_Code,sm.name as siteName"
                                        + " FROM departmentMast dm "
                                        + " left join subgroup sg on dm.subcode = sg.subcode "
                                        + " left join subGroupNature as sgn on sgn.code = sdm.nature"
                                        + " left join enviroDepartmentSiteLink as envDept on dm.subcode = envDept.departmentCode and envDept.site_Code = :siteCode "
                                        + " left join siteMast as sm on envDept.link_Site_Code=sm.code "
                                        + " where  sgn.manualCode IN ('CM','PH')  and dm.subCode=:subcode ";
                                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp1.setString("subcode", DmShared.iNull(code).toString());
                                psTemp1.setString("siteCode", DmServer.getSsnSiteCode(this));
                                rsTemp1 = psTemp1.executeQuery();
                                while (rsTemp1.next()) {
                                    listNode = new KeyFieldExtended();
                                    listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp1.getString("subCode")));
                                    listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp1.getString("name")));
                                    listNode.setSharedModifiedBy(DmShared.xNull(rsTemp1.getString("link_Site_Code")));
                                    listNode.setSharedPreparedBy(DmShared.xNull(rsTemp1.getString("siteName")));
                                    lstTemp.add(listNode);
                                }
                                rsTemp1.close();
                                psTemp1.close();
                            }
                        }
                    }
                }
            } else {
                sqlQuery1 = "SELECT dm.subCODE, sdm.NAME , envDept.link_Site_Code,sm.name as siteName"
                        + " FROM departmentMast dm "
                        + " left join subgroup sdm on dm.subcode = sdm.subcode "
                        + " left join subGroupNature as sgn on sgn.code = sdm.nature"
                        + " left join enviroDepartmentSiteLink as envDept on dm.subcode = envDept.departmentCode and envDept.site_Code = :siteCode "
                        + " left join siteMast as sm on envDept.link_Site_Code=sm.code "
                        + " WHERE sgn.manualCode IN ('CM','PH') ";
                psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery1);
                psTemp2.setString("siteCode", DmServer.getSsnSiteCode(this));
                rsTemp2 = psTemp2.executeQuery();
                while (rsTemp2.next()) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp2.getString("subCODE")));
                    listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp2.getString("NAME")));
                    listNode.setSharedModifiedBy(DmShared.xNull(rsTemp2.getString("link_Site_Code")));
                    listNode.setSharedPreparedBy(DmShared.xNull(rsTemp2.getString("siteName")));
                    lstTemp.add(listNode);
                }
                rsTemp2.close();
                psTemp2.close();

            }
            helper.positioning(lstTemp.size(), blnEndofFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (rsTemp2 != null) {
                    rsTemp2.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp2 != null) {
                    psTemp2.close();
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
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
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
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("departmentName")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnEndofFile);
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
    public DmResult<DefaultValue_Mdl> getDefalutValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
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

            sqlQuery = " select sgMDept.name as MainDeptName,nsm.MainPharmacyDepartmentCode as MainDept,envDept1.link_Site_Code ,sm1.name as siteName,"
                    + " sgLDept.name as localDeptName,nsm.localPharmacyDepartmentCode as localDept "
                    + " from nursingStationMast as nsm "
                    + " left join subgroup as sgMDept on nsm.MainPharmacyDepartmentCode=sgMDept.subCode"
                    + " left join subgroup as sgLDept on nsm.localPharmacyDepartmentCode=sgLDept.subCode"
                    + " left join enviroDepartmentSiteLink as envDept1 on nsm.MainPharmacyDepartmentCode = envDept1.departmentCode and envDept1.site_Code = :siteCode "
                    + " left join siteMast as sm1 on envDept1.link_Site_Code=sm1.code "
                    + " where nsm.code=:nursingStation ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("nursingStation", STServer.getSsnNursingStationCode(this));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dvm.setDepartmentTag(DmShared.iNull(rsTemp.getString("MainDept")).toString());
                dvm.setDepartmentText(DmShared.xNull(rsTemp.getString("MainDeptName")));
                dvm.setSiteCodeToTag(DmShared.iNull(rsTemp.getString("link_Site_Code")).toString());
                dvm.setSiteCodeToText(DmShared.xNull(rsTemp.getString("siteName")));
                dvm.setDepartmentByTag(DmShared.iNull(rsTemp.getString("localDept")).toString());
                dvm.setDepartmentByText(DmShared.xNull(rsTemp.getString("localDeptName")));

            }
            rsTemp.close();
            psTemp.close();
            if (DmShared.iNull(dvm.getSiteCodeToTag()).intValue() == 0) {
                sqlQuery = " select code, name from siteMast where code=:siteCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    dvm.setSiteCodeToTag(DmShared.iNull(rsTemp.getString("code")).toString());
                    dvm.setSiteCodeToText(DmShared.xNull(rsTemp.getString("name")));
                }
                rsTemp.close();
                psTemp.close();
            }

            sqlQuery = "SELECT vt.v_Type, vt.description FROM voucher_Type vt WHERE vt.category in (" + vCat + ")  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dvm.setVoucherTag(DmShared.iNull(rsTemp.getString("v_Type")).toString());
                dvm.setVoucherText(DmShared.xNull(rsTemp.getString("description")));
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicineName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<>();
        TxtHMedicine_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT mm.code, mm.name, mg.name medicineGroup, mc.name as medicinecategory,"
                    + " ump.unit as unitName "
                    + " FROM itemMast mm "
                    + " LEFT JOIN itemGroup mg ON mg.code = mm.itemGroup "
                    + " LEFT JOIN itemCat mc ON mc.code = mm.itemCategoryCode "
                    + " LEFT JOIN unitMast ump ON ump.code = mm.sku "
                    + " WHERE mm.filterItemType = :filterItemType");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setMedicineCategory(DmShared.xNull(rsTemp.getString("medicinecategory")));
                listNode.setMedicineGroup(DmShared.xNull(rsTemp.getString("medicineGroup")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
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
    public DmResult<String> saveRecord(PatientRequisition_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult<String>();
        String docId = "";
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(mrTemp.getDefaultValue().getVoucherTag(), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdRequisition(DmServer.getRecId("Requisition", "docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdRequisition(), mrTemp.getDefaultValue().getDtVoucher(), DmShared.xNull(mrTemp.getDefaultValue().getVoucherTag()), voucherPrefix, cnTemp, this, true));
                blnAdd = true;
            }

            docId = saveRequisition(mrTemp, voucherPrefix, blnAdd, true, cnTemp, this);
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

    public String saveRequisition(PatientRequisition_Mdl mrTemp, String voucherPrefix, boolean blnAdd, boolean isAutoApprove, Connection cnTemp, DmRSSPrivate rstTemp) {
        String rtnValue = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, vNo, approvalDateTime;

        try {
            if (isAutoApprove) {
                approvalDateTime = ",:u_EntDt ";
            } else {
                approvalDateTime = ", NULL ";
            }
            sqlQuery = "SELECT u_EntDt_LatestLine FROM Requisition WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    if (DmShared.iNull(mrTemp.getUserEntDtLatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
                        throw new DmException(0, "Record has been changed, can't update record, refresh record then update");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            vNo = DmShared.iNull(mrTemp.getRecIdRequisition().getCounter()).toString();

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (blnAdd) {
                sqlQuery = "INSERT INTO Requisition (docId, v_Type, v_No,recId, recIdPrefix, v_Date, v_Prefix, departmentCode, requestedByCode, site_Code,"
                        + " preparedBy,  u_EntDt, u_EntDt_LatestLine, departmentToCode, regDocId, priorityTypeCode , "
                        + " approvedBy, approvalDateTime, site_Code_To)"
                        + " VALUES (:docid, :v_type,:v_No, :recid,:recIdPrefix, :v_date, :v_prefix, :departmentcode, :requestedbycode, :site_code, :preparedby,"
                        + " :u_EntDt, :u_EntDt, :departmentToCode, :regDocId, :priorityTypeCode ,:approvedBy " + approvalDateTime + ",:site_Code_To) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("v_type", DmShared.xNull(mrTemp.getDefaultValue().getVoucherTag()));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("recid", DmShared.xNull(mrTemp.getRecIdRequisition().getRecIdValue()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdRequisition().getPrefix()));
                psTemp.setDate("v_date", DmServer.convertDate(mrTemp.getDefaultValue().getDtVoucher()));
                psTemp.setString("v_prefix", DmShared.xNull(voucherPrefix));
                psTemp.setString("requestedbycode", DmShared.foriegnKey(mrTemp.getRequisitionByTag()));
                psTemp.setString("departmentcode", DmShared.foriegnKey(mrTemp.getDepartmentByTag()));
                psTemp.setString("departmentToCode", DmShared.foriegnKey(mrTemp.getDepartmentToTag()));
//                psTemp.setString("presDocId", DmShared.foriegnKey(mrTemp.getDocIdPrescription()));
                psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getDocIdRegistration()));
                psTemp.setString("site_code", DmShared.xNull(DmServer.getSsnSiteCode(rstTemp)));
                psTemp.setString("preparedby", DmShared.xNull(DmServer.getSsnUserName(rstTemp)));
                psTemp.setString("approvedBy", isAutoApprove ? DmServer.getSsnUserName(rstTemp) : null);
                psTemp.setString("priorityTypeCode", DmShared.foriegnKey(DmShared.iNull(mrTemp.getPriorityTypeCode()).toString()));
                if (!DmShared.xNull(mrTemp.getDeptToSiteCode()).equalsIgnoreCase("")) {
                    psTemp.setString("site_Code_To", DmShared.foriegnKey(mrTemp.getDeptToSiteCode()));
                } else {
                    psTemp.setString("site_Code_To", DmServer.getSsnSiteCode(this));
                }
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            //==================================================================
            sqlQuery = "Delete From requisitionDetail where docid= :docid and (isStatus) not in ('1','2') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (int i = 0; i < mrTemp.getLstMedicine().size(); i++) {
                GrdRequisitionMedicine_Mdl grmm = mrTemp.getLstMedicine().get(i);
                if (DmShared.iNull(grmm.getSharedKeyFieldCode()).longValue() > 0) {
                    sqlQuery = "INSERT INTO requisitionDetail (docId, v_SNo, itemCode, reqQty, reqDate, reqTime, remark)"
                            + " VALUES (:docid, :v_sno, :itemCode, :reqqty, :reqdate, :reqTime, :remark) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_sno", DmShared.xNull(Integer.toString(i + 1)));
                    psTemp.setString("itemCode", DmShared.foriegnKey(grmm.getSharedKeyFieldCode()));
                    psTemp.setString("reqqty", DmServer.vNull(grmm.getQty(), "0.00"));
                    psTemp.setDate("reqdate", DmServer.convertDate(grmm.getReqDate()));
                    psTemp.setString("reqTime", DmServer.vNull(grmm.getReqTime(), "00.00"));
                    psTemp.setString("remark", DmShared.xNull(grmm.getRemark()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            rtnValue = mrTemp.getSharedKeyFieldCode();
        } catch (Exception e) {
            rtnValue = "";
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
        return rtnValue;
    }

    @Override
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteTo(DmHelper helper, boolean blnToEndOfFile) {
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
    public String[] getServerDateTime() {
        return DmServer.getServerDateTime();
    }

}
