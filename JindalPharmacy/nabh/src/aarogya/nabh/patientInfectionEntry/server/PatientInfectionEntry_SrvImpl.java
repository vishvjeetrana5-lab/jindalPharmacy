package aarogya.nabh.patientInfectionEntry.server;

import aarogya.nabh.patientInfectionEntry.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientInfectionEntry_SrvImpl extends DmRSSPrivate implements PatientInfectionEntry_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("select(pir.docId) from nabhPatientInfection as pir Order By docId");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
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
    public DmResult<PatientInfectionEntry_Mdl> getMoveRec(String strKey) {
        DmResult<PatientInfectionEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PatientInfectionEntry_Mdl mrTemp = new PatientInfectionEntry_Mdl();
        GrdPatientInfectionEntry_Mdl patientInfectionEntry;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  pir.docId,pir.regDocId,pu.recId AS uhidNo,pu.v_Date as regDate ,pu.patientName,pu.guardianName,"
                    + " hl.name as gender,pu.address1,pu.dobDisplay as age,pu.mobile,psm.name as serviceName,pir.servicecode, "
                    + " pir.infectionType,pir.v_Date,pir.v_Time,pr.recId as regNo,pir.u_Name,pir.preparedBy,vt.category,pir.remark "
                    + " from nabhPatientInfection as pir "
                    + " left join patientRegistration as pr on pr.regDocId =  pir.regDocId "
                    + " left join patientUHID as pu on pu.docId =  pr.uhid "
                    + " left join patientUHID2 as pu2 on pu2.uhid = pu.docId "
                    + " left join hl7Mast as hl on hl.code =  pu2.genderCode "
                    + " left join patientServiceMast as psm on psm.code = pir.serviceCode "
                    + " left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + " where pir.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setRegDocId(DmShared.iNull(rsTemp.getString("regDocId")).toString());
                mrTemp.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setGuardian(DmShared.xNull(rsTemp.getString("guardianName")));
                mrTemp.setGender(DmShared.xNull(rsTemp.getString("gender")));

                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address1")));
                mrTemp.setDisplayDOB(DmShared.xNull(rsTemp.getString("age")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));

                mrTemp.setUhid(DmShared.xNull(rsTemp.getString("uhidNo")));
                mrTemp.setInfectionType(DmShared.xNull(rsTemp.getString("infectionType")));
                mrTemp.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                mrTemp.setServiceCode(DmShared.xNull(rsTemp.getString("servicecode")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                mrTemp.setRegType(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setvDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setvTime(DmShared.xNull(rsTemp.getString("v_Time")));

                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getGrdPatientInfection().addAll(getGridValues(mrTemp.getRegDocId(), cnTemp));

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
    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey) {
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

            sqlQuery = "Delete From nabhPatientInfection Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
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
    public DmResult<String> saveRecord(UserPermission userPermission, PatientInfectionEntry_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            PatientInfectionEntry_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(DmShared.xNull(mrTemp.getVtType())), voucherPrefix, cnTemp, this));

                sqlQuery = "SELECT COUNT(pir.infectionType) AS infectionType FROM nabhPatientInfection AS pir WHERE (pir.regDocId) = :regDocId AND pir.infectionType=:infectionType"
                        + " and serviceCode != :serviceCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getRegDocId()).toString());
                psTemp.setString("infectionType", DmShared.xNull(mrTemp.getInfectionType()));
                psTemp.setString("serviceCode", DmShared.iNull(mrTemp.getServiceCode()).toString());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("infectionType") > 0) {
                    throw new DmException(200, "Infection Type already exists.");
                }
                rsTemp.close();
                psTemp.close();

                blnAdd = true;
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (blnAdd) {
                sqlQuery = "Insert Into nabhPatientInfection (docId,regDocId,v_Type,v_Prefix,infectionType,serviceCode,v_Date,v_Time,u_EntDt,preparedBy,remark) Values  "
                        + "(:docId,:regDocId,:v_Type,:v_Prefix,:infectionType,:serviceCode,:v_Date,:v_Time,:u_EntDt,:preparedBy,:remark) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getRegDocId()).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVtType()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("infectionType", DmShared.xNull(mrTemp.getInfectionType()));
                psTemp.setString("serviceCode", DmShared.foriegnKey(mrTemp.getServiceCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getvDate()));
                psTemp.setString("v_Time", DmShared.xNull(mrTemp.getvTime()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));

            } else {
                sqlQuery = "Update nabhPatientInfection Set "
                        + " infectionType = :infectionType, "
                        + " serviceCode = :serviceCode, "
                        + " u_Name = :u_Name, "
                        + " U_EntDt = :U_EntDt, "
                        + " remark = :remark"
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("infectionType", mrTemp.getInfectionType());
                psTemp.setString("serviceCode", mrTemp.getServiceCode());
                psTemp.setString("remark", mrTemp.getRemark());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());

            }
            psTemp.executeUpdate();
            psTemp.close();

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select  pir.docId,pu.recId AS uhidRecId,pu.v_Date as regDate ,pu.patientName,pir.infectionType,pir.v_Date,pir.v_Time,pir.regDocId,pir.remark "
                    + " from nabhPatientInfection as pir "
                    + " left join patientRegistration as pr on pr.regDocId =  pir.regDocId "
                    + " left join patientUHID as pu on pu.docId =  pr.uhid ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setRegDocId(DmShared.iNull(rsTemp.getString("regDocId")).toString());
                listNode.setRegNo(DmShared.xNull(rsTemp.getString("uhidRecId")));
                listNode.setRegDate(DmShared.dNull(rsTemp.getDate("regDate")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setInfectionType(DmShared.xNull(rsTemp.getString("infectionType")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setTime(DmShared.xNull(rsTemp.getString("v_Time")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
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
    public DmResult<PatientDetail_Mdl> getUHIDDetails(String regNo, String regDocId, String type) {
        DmResult<PatientDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String[] currentDateTime;
        PatientDetail_Mdl mrTemp = new PatientDetail_Mdl();
        String sqlQuery, conditionPR = "", conditionPU = "";
        dmrTemp.setData(mrTemp);

        try {
            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(type).equalsIgnoreCase(pvc.IPD)) {
                type = "'" + pvc.IPD + "'";
            } else if (DmShared.xNull(type).equalsIgnoreCase(pvc.OPD)) {
                type = "'" + pvc.OPD + "'" + "," + "'" + pvc.EMERGENCY_OPD + "'";
            } else if (DmShared.xNull(type).equalsIgnoreCase(pvc.DAY_CARE)) {
                type = "'" + pvc.DAY_CARE + "'";
            } else if (DmShared.xNull(type).equalsIgnoreCase(pvc.EMERGENCY)) {
                type = "'" + pvc.EMERGENCY + "'";
            }
            if (!DmShared.xNull(type).trim().equalsIgnoreCase("")) {
                conditionPR += " and (vt.category in (" + type + ")) ";
            } else {
                conditionPR += " and (vt.category = '" + type + "') ";
            }

            if (DmShared.iNull(regDocId).longValue() != 0) {
                conditionPR += " and pr.regDocId = :regDocId ";
                conditionPU += " and pr.regDocId = :regDocId ";
            } else if (!DmShared.xNull(regNo).trim().equalsIgnoreCase("")) {
                conditionPR += " and pr.recId = :regNo ";
                conditionPU += " and pr.recId = :regNo ";
            }
            sqlQuery = "Select  top 1 pu.docId, pu.recId as uhidNo, pr.regDocId, pr.recId "
                    + "         from patientUHID as pu "
                    + "         left join ( "
                    + "                     Select  top 1 pr.regDocId, pr.recId, pr.uhid "
                    + "                             from patientRegistration as pr "
                    + "                             left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                    + "                             where pr.site_Code = :siteCode and pr.isCancelled = 0 " + conditionPR
                    + "                             order by pr.v_Date desc, pr.regTime desc, vt.short_Name, pr.v_No desc, pr.recId "
                    + "         ) as pr on pu.docId = pr.uhid "
                    + "         where pu.isCancelled = 0 " + conditionPU
                    + "         and isNull(pu.approvedBy,'') <> '' and pu.isExpired = 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(regDocId).longValue() != 0) {
                psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            } else if (!DmShared.xNull(regNo).trim().equalsIgnoreCase("")) {
                psTemp.setString("regNo", DmShared.xNull(regNo).trim());
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                regDocId = DmShared.iNull(rsTemp.getString("regDocId")).toString();
                mrTemp.setDocIdUHID(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setDocIdRegistration(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setRecIdRegistration(DmShared.xNull(rsTemp.getString("recId")));
            } else {
                throw new DmException(0, "No record found. Try again...");
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(regDocId).longValue() <= 0) {
                throw new DmException(0, "No record found. Try Again.");
            } else {
                getPatientDetail(mrTemp, cnTemp);
            }

            dmrTemp.setValue1(currentDateTime[0]);
            dmrTemp.setValue2(currentDateTime[1]);
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (SQLException | DmException e) {
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT VT.V_Type, VT.Description  FROM voucher_type VT WHERE  VT.short_Name='NABIT' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeTag(DmShared.xNull(rsTemp.getString("V_Type")));
                defaultValue.setVoucherTypeText(DmShared.xNull(rsTemp.getString("Description")));
            }
            rsTemp.close();
            psTemp.close();

            defaultValue.setServerDate(DmServer.getServerDateTime()[0]);
            defaultValue.setServerTime(DmServer.getServerDateTime()[1]);
            dmrTemp.setData(defaultValue);

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRegType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.IPD);
            listNode.setSharedKeyFieldName(pvc.IPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.OPD);
            listNode.setSharedKeyFieldName(pvc.OPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.EMERGENCY);
            listNode.setSharedKeyFieldName("Emergency");
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

    private void getPatientDetail(PatientDetail_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        String sqlQuery;
        try {
            sqlQuery = "select  pu.docId as uhidDocId, pu.recId AS uhidRecId,pu.guardianName, pu.patientName,pu.cityCode,pu.eMail, "
                    + "         pu2.genderCode, hl7gen.name as gender,  pu.idNo, pu.dobDisplay, pu.v_No AS uhidCounter, pu.mobile, "
                    + "         CONCAT( pu.address1, ' ', pu.address2) AS address, "
                    + "         c.cityCode , c.cityName "
                    + "         from patientUHID AS pu "
                    + "         left join patientUHID2 as pu2 on pu.docId = pu2.uhid "
                    + "         left join subGroup AS sg ON pu.organizationCode = sg.subCode "
                    + "         LEFT JOIN hl7Mast  as hl7gen on hl7gen.code  = pu2.genderCode "
                    + "         left join city as c on c.cityCode = pu.cityCode "
                    + "         Left join patientRegistration pr on pr.uhid = pu.docId "
                    + "         where pr.regDocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdRegistration()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDocIdUHID(DmShared.xNull(rsTemp.getString("uhidDocID")));
                mrTemp.setRecIdUHID(DmShared.xNull(rsTemp.getString("uhidRecID")));
                mrTemp.setAge(DmShared.xNull(rsTemp.getString("dobDisplay")));
                mrTemp.setGender(DmShared.xNull(rsTemp.getString("gender")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setGuardian(DmShared.xNull(rsTemp.getString("guardianName")));
                mrTemp.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
            }
            psTemp.close();
            rsTemp.close();

            //fill Grid
            mrTemp.getGrdPatientInfection().addAll(getGridValues(mrTemp.getDocIdRegistration(), cnTemp));

        } catch (SQLException | DmException e) {
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
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select psm.name,psm.code "
                    + " from patientServiceMast as psm "
                    + " left join patientServiceSiteList as pssl on pssl.code = psm.code "
                    + " where isNull(psm.serviceNature,0) in (" + ServiceNature.OPERATION.getId() + "," + ServiceNature.SERVICE.getId() + ") "
                    + " and site_Code = :siteCode");

            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHInfectionType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Catheter associated urinary tract infection");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Ventilator associated pneumonia");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Central line associated bloodstream infection");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("3");
            listNode.setSharedKeyFieldName("Surgical site infection");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("4");
            listNode.setSharedKeyFieldName("Incidence of blood body fluid exposures");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("5");
            listNode.setSharedKeyFieldName("Incidence of needle stick injuries");
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

    private List<GrdPatientInfectionEntry_Mdl> getGridValues(String regDocId, Connection cnTemp) {
        List<GrdPatientInfectionEntry_Mdl> lstTemp = new ArrayList<>();
        GrdPatientInfectionEntry_Mdl node;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  psm.name as serviceName, pir.infectionType, pir.v_Date, pir.v_Time "
                    + "         from nabhPatientInfection as pir "
                    + "         left join patientServiceMast as psm on psm.code = pir.serviceCode "
                    + "         where pir.regDocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPatientInfectionEntry_Mdl();
                node.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setTime(DmServer.vNull(rsTemp.getString("v_Time"), "00.00"));
                node.setPatientInfectionReaction(DmShared.xNull(rsTemp.getString("infectionType")));
                node.setService(DmShared.xNull(rsTemp.getString("serviceName")));
                lstTemp.add(node);
            }
            psTemp.close();
            rsTemp.close();
        } catch (SQLException | DmException e) {
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
        return lstTemp;
    }

}
