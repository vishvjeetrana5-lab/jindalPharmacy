package aarogya.master.referredMaster.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.referredMaster.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_Server;
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
import java.util.UUID;

public class ReferredMaster_SrvImpl extends DmRSSPrivate implements ReferredMaster_Srv {

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

            sqlQuery = " SELECT cm.subCode FROM consultantmast cm "
                    + "  Left Join subGroup sg on cm.subCode = sg.subCode where sg.filterNature = :filterNature "
                    + "  ORDER BY sg.name  ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterNature", DmShared.xNull(DmServer.getCodeViaManualCode("RC", "manualCode", "subgroupNature", "Code", cnTemp)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
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
    public DmResult<ReferredMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ReferredMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ReferredMaster_Mdl mrTemp = new ReferredMaster_Mdl();
        GrdClinicDetail_Mdl clinicDetail;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT cm.subCode, sg.name,sg.displayName, sg.initial,  sg.manualCode, cm.consultantSpeciCode, spec.name AS specility, sg2.departmentCode, "
                    + "  sdm.name AS department, sg.nature as NatureCode,sgn.name as NatureName, sg.mobile,cm.mobileHome, sg.eMail, sg2.dob, sg2.doa, sg.isActive, "
                    + "  cm.mciReg, sg.remark, sg.u_Name, sg.preparedBy,sg.TDS_Catg,TC.Name AS TCName "
                    + " FROM consultantMast cm  "
                    + "  LEFT JOIN subgroup sg ON cm.subCode = sg.subcode  "
                    + "  LEFT JOIN subgroup2 sg2 ON cm.subCode = sg2.subcode  "
                    + "  left join subGroupNature as sgn on sg.nature=sgn.code  "
                    + "  LEFT JOIN consultantspecimast spec ON cm.consultantSpeciCode = spec.code  "
                    + "  LEFT JOIN subgroup sdm ON sg2.departmentCode = sdm.subcode "
                    + "  LEFT JOIN TDSCat TC ON TC.Code=sg.TDS_Catg  "
                    + "  WHERE cm.subcode= :code and sg.filterNature =:filterNature ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterNature", DmShared.xNull(DmServer.getCodeViaManualCode("RC", "manualCode", "subgroupNature", "Code", cnTemp)));
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setInitial(DmShared.xNull(rsTemp.getString("initial")));
                mrTemp.setActiveTag(DmShared.vNull(rsTemp.getString("isActive")).toString());
                mrTemp.setActiveText(DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 0 ? "No" : "Yes");
                mrTemp.setDoa(DmShared.dNull(rsTemp.getDate("doa")));
                mrTemp.setDob(DmShared.dNull(rsTemp.getDate("dob")));
                mrTemp.setDeptTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDeptText(DmShared.xNull(rsTemp.getString("department")));
                mrTemp.seteMail(DmShared.xNull(rsTemp.getString("eMail")));
                mrTemp.setMciReg(DmShared.xNull(rsTemp.getString("mciReg")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setMobileHome(DmShared.xNull(rsTemp.getString("mobileHome")));
                mrTemp.setNatureCode(DmShared.xNull(rsTemp.getString("natureCode")));
                mrTemp.setNatureName(DmShared.xNull(rsTemp.getString("natureName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.setSpeciTag(DmShared.xNull(rsTemp.getString("consultantSpeciCode")));
                mrTemp.setSpeciText(DmShared.xNull(rsTemp.getString("specility")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setTdsCategory(DmShared.xNull(rsTemp.getString("TDS_Catg")));
                mrTemp.setTdsCategoryName(DmShared.xNull(rsTemp.getString("TCName")));

            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "SELECT ccd.consultantCode, ccd.sNo, ccd.visitSite, ccd.timingShedule, ccd.Phone, ccd.address1, "
                    + "  ccd.address2, ccd.citycode, ct.cityName  "
                    + "FROM consultantclinicdetail ccd  "
                    + "  LEFT JOIN city ct ON ccd.citycode = ct.cityCode  "
                    + "WHERE ccd.consultantCode = :code ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                clinicDetail = new GrdClinicDetail_Mdl();
                clinicDetail.setAddress(DmShared.xNull(rsTemp.getString("address1")));
                clinicDetail.setAddress1(DmShared.xNull(rsTemp.getString("address2")));
                clinicDetail.setCityTag(DmShared.xNull(rsTemp.getString("citycode")));
                clinicDetail.setCityText(DmShared.xNull(rsTemp.getString("cityName")));
                clinicDetail.setPhone(DmShared.xNull(rsTemp.getString("Phone")));
                clinicDetail.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("VisitSite")));
                clinicDetail.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("VisitSite")));
                clinicDetail.setTiming(DmShared.xNull(rsTemp.getString("timingShedule")));
                mrTemp.getGrdClinic().add(clinicDetail);
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
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From consultantclinicdetail Where consultantCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From subGroupSiteList Where subCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From consultantmast Where subcode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From subGroup2 Where subcode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From subGroup Where subcode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

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
    public DmResult<String> saveRecord(ReferredMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, siteCode, filterNature = "", acGroupConsCode = "";
        ResultSet rsTemp = null;
        Integer srNo = 0;

        dmrTemp.setData("");

        try {
            ReferredMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            siteCode = DmServer.getSsnSiteCode(this);

            sqlQuery = " SELECT COUNT(sg.name) AS cnt FROM subgroup sg "
                    + " WHERE sg.name = :name AND sg.subCode <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Referred name is already exists.");
                }
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " SELECT COUNT(sg.manualCode) AS cnt FROM subgroup sg "
                    + " WHERE sg.manualCode = :shortName AND sg.subCode <> :code AND IsNULL(sg.manualCode, '') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).replace(" ", "").trim());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Short Name is already exists.");
                }
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " select acGroupReferredCode from enviroCompany ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                acGroupConsCode = DmShared.xNull(rsTemp.getString("acGroupReferredCode"));

            }
            psTemp.close();
            rsTemp.close();

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "subGroup", cnTemp));
                DmFileManager_Server.manageLinkFileCollection(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmShared.xNull(mrTemp.getTempKeyCode()), this, cnTemp);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            filterNature = DmServer.getCodeViaManualCode("RC", "manualCode", "subgroupNature", "Code", cnTemp);

            if (blnAdd) {
                sqlQuery = "INSERT INTO subGroup(subCode,initial,NAME,displayName, manualCode,email, mobile, isActive, remark, u_EntDt,"
                        + " preparedBy,nature,filterNature,groupCode,site_Code,TDS_Catg) "
                        + " VALUES (:subCode,:initial,:name,:displayName, :manualCode,:email, :mobile, :isActive, :remark, :u_EntDt, "
                        + ":preparedBy,:nature,:filterNature,:groupCode,:site_Code,:tdsCatg);";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("initial", DmShared.foriegnKey(mrTemp.getInitial()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("eMail", DmShared.xNull(mrTemp.geteMail()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("isActive", DmShared.vNull(mrTemp.getActiveTag()).doubleValue() == 0 ? "0" : "1");
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("nature", DmShared.foriegnKey(filterNature));
                psTemp.setString("groupCode", DmShared.foriegnKey(acGroupConsCode));
                psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("tdsCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "INSERT INTO subGroup2(subCode, departmentCode, dob, doa) "
                        + " VALUES (:subCode, :departmentCode, :dob, :doa );";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setDate("doa", DmServer.convertDate(mrTemp.getDoa()));
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDeptTag()));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "INSERT INTO consultantMast (SubCode, consultantSpeciCode, mobileHome,  mciReg,nature) "
                        + " VALUES (:subCode, :consultantSpeciCode,:mobileHome, :mciReg,:nature) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("mobileHome", DmShared.xNull(mrTemp.getMobileHome()));
                psTemp.setString("consultantSpeciCode", DmShared.foriegnKey(mrTemp.getSpeciTag()));
                psTemp.setString("mciReg", DmShared.xNull(mrTemp.getMciReg()));
                psTemp.setString("nature", DmShared.xNull("2"));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "INSERT INTO subGroupSiteList(subCode, site_Code) "
                        + " VALUES (:subcode, :siteCode );";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(siteCode));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = "UPDATE subGroup SET"
                        + "  NAME = :name,"
                        + "  displayName = :displayName,"
                        + "  manualCode = :manualCode,"
                        + "  initial = :initial,"
                        + "  nature = :nature,"
                        + "  mobile = :mobile,"
                        + "  eMail = :eMail,"
                        + "  isActive = :isActive,"
                        + "  remark = :remark,"
                        + "  u_Name = :userName,"
                        + "  filterNature = :filterNature,"
                        + " TDS_Catg=:TDSCatg, "
                        + "  u_EntDt = :u_EntDt  "
                        + "WHERE subCode = :subCode ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("initial", DmShared.foriegnKey(mrTemp.getInitial()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("eMail", DmShared.xNull(mrTemp.geteMail()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("isActive", DmShared.vNull(mrTemp.getActiveTag()).doubleValue() == 0 ? "0" : "1");
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setString("nature", DmShared.foriegnKey(filterNature));
                psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
                psTemp.setString("TDSCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE subGroup2 SET "
                        + "  departmentCode = :departmentCode, "
                        + "  dob = :dob, "
                        + "  doa = :doa "
                        + " WHERE subCode = :subcode ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setDate("doa", DmServer.convertDate(mrTemp.getDoa()));
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDeptTag()));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE consultantMast SET "
                        + "  consultantSpeciCode = :consultantSpeciCode, "
                        + "  mobileHome = :mobileHome, "
                        + "  mciReg = :mciReg "
                        + " WHERE subCode = :subCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("mobileHome", DmShared.xNull(mrTemp.getMobileHome()));
                psTemp.setString("consultantSpeciCode", DmShared.foriegnKey(mrTemp.getSpeciTag()));
                psTemp.setString("mciReg", DmShared.xNull(mrTemp.getMciReg()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            sqlQuery = "Delete From consultantclinicdetail Where consultantCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            srNo = 0;
            for (int i = 0; i < mrTemp.getGrdClinic().size(); i++) {
                ++srNo;
                GrdClinicDetail_Mdl lst = mrTemp.getGrdClinic().get(i);
                if (!DmShared.xNull(lst.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = "INSERT INTO consultantclinicdetail (consultantCode, sNo, visitSite, timingShedule, Phone, address1, address2, citycode) "
                            + " VALUES (:doctorCode, :srNo, :visitSite, :timingShedule, :Phone, :address, :address1, :citycode);";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setInt("srNo", srNo);
                    psTemp.setString("doctorCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("visitSite", DmShared.xNull(lst.getSharedKeyFieldCode()));
                    psTemp.setString("timingShedule", DmShared.xNull(lst.getTiming()));
                    psTemp.setString("Phone", DmShared.xNull(lst.getPhone()));
                    psTemp.setString("address", DmShared.xNull(lst.getAddress()));
                    psTemp.setString("address1", DmShared.xNull(lst.getAddress1()));
                    psTemp.setString("citycode", DmShared.foriegnKey(lst.getCityTag()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

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
    public String getTempKeyCode() {

        return UUID.randomUUID().toString();
    }

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT cm.subCode, sg.name, sg.initial, sg.name AS ConsName , sg.manualCode,  "
                    + "  spec.name AS specility, sdm.name AS department, "
                    + "  sg.mobile,sg.eMail, sg2.dob, sg2.doa,  "
                    + "  CASE sg.isActive  "
                    + "  WHEN 0 THEN 'No'  "
                    + "  WHEN 1 THEN 'Yes'   "
                    + "  ELSE 'No' "
                    + "  END as isActive , "
                    + "  cm.mciReg, sg.remark  "
                    + "  FROM consultantMast cm  "
                    + "  LEFT JOIN subgroup sg ON cm.subCode = sg.subcode  "
                    + "  LEFT JOIN subgroup2 sg2 ON cm.subCode = sg2.subcode  "
                    + "  LEFT JOIN consultantspecimast spec ON cm.consultantSpeciCode = spec.code  "
                    + "  LEFT JOIN subgroup sdm ON sg2.departmentCode = sdm.subcode "
                    + " where sg.filterNature = :filterNature  ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterNature", DmShared.xNull(DmServer.getCodeViaManualCode("RC", "manualCode", "subgroupNature", "Code", cnTemp)));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ConsName")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("department")));
                listNode.setIsActive(DmShared.xNull(rsTemp.getString("isActive")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSpecialization(DmShared.xNull(rsTemp.getString("specility")));
                listNode.setAnnivDate(DmShared.dNull(rsTemp.getDate("doa")));
                listNode.setDob(DmShared.dNull(rsTemp.getDate("dob")));
                listNode.setMciRegNo(DmShared.xNull(rsTemp.getString("mciReg")));
                listNode.setEmail(DmShared.xNull(rsTemp.getString("eMail")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());

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
                    + " left join subgroup sdm on dm.subcode = sdm.subcode "
                    + " Left join subGroupSiteList as sgsl on sdm.subCode=sgsl.subCode "
                    + " WHERE dm.nature in ('Hospital','Common') and sdm.isActive = 1 and sgsl.site_Code=:siteCode");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSpecialization(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT csm.code, csm.name, csm.guiTemplateCodePost, gtm.name AS guiTemplate "
                    + "  FROM consultantSpeciMast AS csm "
                    + "  LEFT JOIN guiTemplateMast AS gtm ON gtm.code = csm.guiTemplateCodePost ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("guiTemplateCodePost")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("guiTemplate")));
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
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select c.cityCode, c.cityName, sm.name as state  "
                    + "		from city as c "
                    + "		left join stateMast as sm on c.stateCode = sm.code");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("state")));
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
    public DmResult<List<String>> getSuggetionConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT sg.name FROM consultantmast cm "
                    + " LEFT JOIN subgroup sg ON cm.subCode = sg.subcode ");
            helper.addSort("name", DmSortType.ASC);
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

    @Override
    public DmResult<List<String>> getSuggetionShortName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT sg.manualCode as shortName FROM consultantmast cm "
                    + " LEFT JOIN subgroup sg ON cm.subCode = sg.subcode ");
            helper.addSort("shortName", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("shortName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHInitial(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT name AS code, name FROM initialmast ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CODE,NAME FROM TdsCat ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
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

}
