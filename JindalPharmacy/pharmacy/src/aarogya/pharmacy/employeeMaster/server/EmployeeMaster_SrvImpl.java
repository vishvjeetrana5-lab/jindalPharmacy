package aarogya.pharmacy.employeeMaster.server;

import aarogya.pharmacy.employeeMaster.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeMaster_SrvImpl extends DmRSSPrivate implements EmployeeMaster_Srv {

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
            rsTemp = cnTemp.createStatement().executeQuery("SELECT sg.subCode FROM subgroup sg WHERE sg.nature IN ('Employee') ORDER BY sg.name");
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
    public DmResult<Employee_Mdl> getMoveRec(String strKey) {
        DmResult<Employee_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        Employee_Mdl mrTemp = new Employee_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT sg.subCode, sg.name, sg.groupCode, sg.groupNature, sg.manualCode, sg.nature, sg.add1, sg.add2, "
                    + "  sg.cityCode, cit.cityName, sg.countryCode, cou.name AS country, sg.pin, sg.phone, sg.mobile, sg.eMail, "
                    + "  sg.pan, sg.isActive, sg2.dob, sg2.maritalStatus, sg2.religion, sg2.departmentCode, dept.name AS department, "
                    + "  sg2.designationCode, des.name AS designation, sg2.doj, sg2.doa, sg2.tADD1, sg2.tADD2, sg2.tCityCode, "
                    + "  cit1.cityName AS tCityName, sg2.tCountryCode, cou.name AS tCountry, sg2.tPIN, sg.u_Name, sg.preparedBy, "
                    + "  ag.groupName, sg2.sex "
                    + "FROM subgroup2 AS sg2  "
                    + "  RIGHT JOIN subgroup AS sg ON (sg2.subCode = sg.subCode)  "
                    + "  LEFT JOIN city AS cit ON (sg.cityCode = cit.cityCode)  "
                    + "  LEFT JOIN country AS cou ON (sg.countryCode = cou.code)  "
                    + "  LEFT JOIN city AS cit1 ON (sg2.tCityCode = cit1.cityCode)  "
                    + "  LEFT JOIN country AS cou1 ON (sg2.tCountryCode = cou1.code)  "
                    + "  LEFT JOIN subgroup AS dept ON (sg2.departmentCode = dept.subcode)  "
                    + "  LEFT JOIN designationmast AS des ON (sg2.designationCode = des.code) "
                    + "  LEFT JOIN acgroup AS ag ON (sg.groupCode=ag.groupCode) "
                    + "WHERE sg.subCode = :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setAccountGroupDesc(DmShared.xNull(rsTemp.getString("groupNature")));
                mrTemp.setAccountGroupTag(DmShared.xNull(rsTemp.getString("groupCode")));
                mrTemp.setAccountGroupText(DmShared.xNull(rsTemp.getString("groupName")));
                mrTemp.setActiveTag(DmShared.vNull(rsTemp.getString("isActive")).toString());
                mrTemp.setActiveText(DmShared.vNull(rsTemp.getString("isActive")) == 0 ? "No" : "Yes");
                mrTemp.setAnvDate(DmShared.dNull(rsTemp.getDate("doa")));
                mrTemp.setDob(DmShared.dNull(rsTemp.getDate("dob")));
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("department")));
                mrTemp.setEmailId(DmShared.xNull(rsTemp.getString("eMail")));
                mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setNature(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("add1")));
                mrTemp.setAddress2(DmShared.xNull(rsTemp.getString("add2")));
                mrTemp.setCityTag(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setCityText(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setCountryTag(DmShared.xNull(rsTemp.getString("countryCode")));
                mrTemp.setpCountryText(DmShared.xNull(rsTemp.getString("country")));
                mrTemp.setDesignationTag(DmShared.xNull(rsTemp.getString("designationCode")));
                mrTemp.setDesignationText(DmShared.xNull(rsTemp.getString("designation")));
                mrTemp.setEmployeeID(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.setGenderTag(DmShared.xNull(rsTemp.getString("sex")));
                mrTemp.setGenderText(DmShared.xNull(rsTemp.getString("sex")).equalsIgnoreCase("M") ? "Male"
                        : (DmShared.xNull(rsTemp.getString("sex")).equalsIgnoreCase("F") ? "Female"
                        : (DmShared.xNull(rsTemp.getString("sex")).equalsIgnoreCase("I") ? "Inter Sex" : "")));
                mrTemp.setJoinDate(DmShared.dNull(rsTemp.getDate("doj")));
                mrTemp.setMaritalStatusTag(DmShared.xNull(rsTemp.getString("maritalStatus")));
                mrTemp.setMaritalStatusText(DmShared.xNull(rsTemp.getString("maritalStatus")).equalsIgnoreCase("S") ? "Single"
                        : (DmShared.xNull(rsTemp.getString("maritalStatus")).equalsIgnoreCase("M") ? "Married"
                        : (DmShared.xNull(rsTemp.getString("maritalStatus")).equalsIgnoreCase("D") ? "Divorced"
                        : (DmShared.xNull(rsTemp.getString("maritalStatus")).equalsIgnoreCase("W") ? "Widow" : ""))));
                mrTemp.setMobileNo(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setNature(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setPaddress1(DmShared.xNull(rsTemp.getString("tADD1")));
                mrTemp.setPaddress2(DmShared.xNull(rsTemp.getString("tADD2")));
                mrTemp.setPanNo(DmShared.xNull(rsTemp.getString("pan")));
                mrTemp.setPhoneNo(DmShared.xNull(rsTemp.getString("phone")));
                mrTemp.setPinCode(DmShared.xNull(rsTemp.getString("pin")));
                mrTemp.setPpinCode(DmShared.xNull(rsTemp.getString("tPIN")));
                mrTemp.setReligionTag(DmShared.xNull(rsTemp.getString("religion")));
                mrTemp.setReligionText(DmShared.xNull(rsTemp.getString("religion")));
                mrTemp.setpCityTag(DmShared.xNull(rsTemp.getString("tCityCode")));
                mrTemp.setpCityText(DmShared.xNull(rsTemp.getString("tCityName")));
                mrTemp.setpCountryText(DmShared.xNull(rsTemp.getString("tCountry")));
                mrTemp.setpCountryTag(DmShared.xNull(rsTemp.getString("tCountryCode")));
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
    public DmResult<String> saveRecord(Employee_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;

        dmrTemp.setData("");
        try {
            EmployeeMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            siteCode = DmServer.getSsnSiteCode(this);
            userName = DmServer.getSsnUserName(this);

            sqlQuery = "SELECT COUNT(sg.manualCode) AS cnt FROM subgroup sg WHERE sg.manualCode = :manualCode and sg.subCode <> :subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", DmShared.xNull(mrTemp.getEmployeeID()));
            psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Employee ID already exists.");
            }

            mrTemp.setEmployeeID(DmShared.removeSpace(mrTemp.getEmployeeID()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "subGroup", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (blnAdd) {
                sqlQuery = "INSERT INTO subgroup (subCode, NAME, groupCode, groupNature, manualCode, nature, add1, add2, cityCode, countryCode, "
                        + "  pin, phone, mobile, eMail, pan, site_Code, u_EntDt, preparedBy, isActive)  "
                        + "VALUES (:subCode, :NAME, :groupCode, :groupNature, :manualCode, :nature, :add1, :add2, :cityCode, :countryCode, "
                        + "    :pin, :phone, :mobile, :eMail, :pan, :site_Code, :uEntDt, :preparedBy, :isActive) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getAccountGroupTag()));
                psTemp.setString("groupNature", DmShared.foriegnKey(mrTemp.getAccountGroupDesc()));
                psTemp.setString("manualCode", mrTemp.getEmployeeID());
                psTemp.setString("nature", mrTemp.getNature());
                psTemp.setString("add1", mrTemp.getAddress1());
                psTemp.setString("add2", mrTemp.getAddress2());
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityTag()));
                psTemp.setString("countryCode", DmShared.foriegnKey(mrTemp.getCountryTag()));
                psTemp.setString("pin", mrTemp.getPinCode());
                psTemp.setString("phone", mrTemp.getPhoneNo());
                psTemp.setString("mobile", mrTemp.getMobileNo());
                psTemp.setString("eMail", mrTemp.getEmailId());
                psTemp.setString("pan", mrTemp.getPanNo());
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("preparedBy", userName);
                psTemp.setString("isActive", DmShared.vNull(mrTemp.getActiveTag()) == 0 ? "0" : "1");
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "INSERT INTO subgroupsitelist (subCode, site_Code) "
                        + "VALUES (:subCode, :siteCode) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(siteCode));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "insert into subgroup2 (subCode, dob, maritalStatus, religion, departmentCode, designationCode, doj, doa, tADD1, tADD2, "
                        + "  tCityCode, tCountryCode, tPIN, sex)  "
                        + "values ( :subCode, :dob, :maritalStatus, :religion, :departmentCode, :designationCode, :doj, :doa, :tADD1, :tADD2, "
                        + "    :tCityCode, :tCountryCode, :tPIN, :sex) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setString("maritalStatus", mrTemp.getMaritalStatusTag());
                psTemp.setString("religion", mrTemp.getReligionTag());
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
                psTemp.setString("designationCode", DmShared.foriegnKey(mrTemp.getDesignationTag()));
                psTemp.setDate("doj", DmServer.convertDate(mrTemp.getJoinDate()));
                psTemp.setDate("doa", DmServer.convertDate(mrTemp.getAnvDate()));
                psTemp.setString("tADD1", mrTemp.getPaddress1());
                psTemp.setString("tADD2", mrTemp.getPaddress2());
                psTemp.setString("tCityCode", DmShared.foriegnKey(mrTemp.getpCityTag()));
                psTemp.setString("tCountryCode", DmShared.foriegnKey(mrTemp.getpCountryTag()));
                psTemp.setString("tPIN", mrTemp.getPpinCode());
                psTemp.setString("sex", mrTemp.getGenderTag());

            } else {
                sqlQuery = "UPDATE subgroup SET "
                        + "  NAME = :NAME, "
                        + "  groupCode = :groupCode, "
                        + "  groupNature = :groupNature, "
                        + "  manualCode = :manualCode, "
                        + "  nature = :nature, "
                        + "  add1 = :add1, "
                        + "  add2 = :add2, "
                        + "  cityCode = :cityCode, "
                        + "  countryCode = :countryCode, "
                        + "  pin = :pin, "
                        + "  phone = :phone, "
                        + "  mobile = :mobile, "
                        + "  eMail = :eMail, "
                        + "  pan = :pan, "
                        + "  u_Name = :userName, "
                        + "  site_Code = :site_Code, "
                        + "  u_EntDt = :uEntDt, "
                        + "  isActive = :isActive "
                        + "WHERE subCode = :subCode ;";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getAccountGroupTag()));
                psTemp.setString("groupNature", DmShared.foriegnKey(mrTemp.getAccountGroupDesc()));
                psTemp.setString("manualCode", mrTemp.getEmployeeID());
                psTemp.setString("nature", mrTemp.getNature());
                psTemp.setString("add1", mrTemp.getAddress1());
                psTemp.setString("add2", mrTemp.getAddress2());
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityTag()));
                psTemp.setString("countryCode", DmShared.foriegnKey(mrTemp.getCountryTag()));
                psTemp.setString("pin", mrTemp.getPinCode());
                psTemp.setString("phone", mrTemp.getPhoneNo());
                psTemp.setString("mobile", mrTemp.getMobileNo());
                psTemp.setString("eMail", mrTemp.getEmailId());
                psTemp.setString("pan", mrTemp.getPanNo());
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("userName", userName);
                psTemp.setString("isActive", DmShared.vNull(mrTemp.getActiveTag()) == 0 ? "0" : "1");
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "DELETE FROM subgroupsitelist WHERE site_Code = :siteCode;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("siteCode", siteCode);
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "INSERT INTO subgroupsitelist (subCode, site_Code) "
                        + "VALUES (:subCode, :siteCode) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("siteCode", DmShared.foriegnKey(siteCode));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE subgroup2 SET"
                        + "  sex = :sex,"
                        + "  dob = :dob,"
                        + "  maritalStatus = :maritalStatus,"
                        + "  religion = :religion,"
                        + "  departmentCode = :departmentCode,"
                        + "  designationCode = :designationCode,"
                        + "  doj = :doj,"
                        + "  doa = :doa,"
                        + "  tADD1 = :tADD1,"
                        + "  tADD2 = :tADD2,"
                        + "  tCityCode = :tCityCode,"
                        + "  tCountryCode = :tCountryCode,"
                        + "  tPIN = :tPIN "
                        + "WHERE subCode = :subCode ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setString("maritalStatus", mrTemp.getMaritalStatusTag());
                psTemp.setString("religion", mrTemp.getReligionTag());
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
                psTemp.setString("designationCode", DmShared.foriegnKey(mrTemp.getDesignationTag()));
                psTemp.setDate("doj", DmServer.convertDate(mrTemp.getJoinDate()));
                psTemp.setDate("doa", DmServer.convertDate(mrTemp.getAnvDate()));
                psTemp.setString("tADD1", mrTemp.getPaddress1());
                psTemp.setString("tADD2", mrTemp.getPaddress2());
                psTemp.setString("tCityCode", DmShared.foriegnKey(mrTemp.getpCityTag()));
                psTemp.setString("tCountryCode", DmShared.foriegnKey(mrTemp.getpCountryTag()));
                psTemp.setString("tPIN", mrTemp.getPpinCode());
                psTemp.setString("sex", mrTemp.getGenderTag());
            }
            psTemp.executeUpdate();
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

            sqlQuery = "Delete From SubGroup2 Where subCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From subgroupsitelist Where subCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From SubGroup Where subCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
            sqlWithParam.setSQL("SELECT sg.subCode, sg.name, ag.groupName, sg.manualCode, sg.mobile, sg2.doj, sg2.sex, dept.name AS department, "
                    + "  desi.name AS designation  "
                    + "FROM subgroup sg  "
                    + "  LEFT JOIN acgroup ag ON sg.groupCode = ag.groupCode  "
                    + "  LEFT JOIN subgroup2 sg2 ON sg2.subCode = sg.subCode  "
                    + "  LEFT JOIN subgroup dept ON sg2.departmentCode = dept.subcode  "
                    + "  LEFT JOIN designationmast desi ON sg2.designationCode = desi.code "
                    + "  WHERE sg.nature IN ('Employee') ");
            //   + "ORDER BY sg.name");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setEmpID(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setAccountGroup(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setJoinDate(DmShared.dNull(rsTemp.getDate("doj")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("department")));
                listNode.setDesignation(DmShared.xNull(rsTemp.getString("designation")));
                listNode.setGender(DmShared.xNull(rsTemp.getString("sex")).equalsIgnoreCase("M") ? "Male"
                        : (DmShared.xNull(rsTemp.getString("sex")).equalsIgnoreCase("F") ? "Female"
                        : (DmShared.xNull(rsTemp.getString("sex")).equalsIgnoreCase("I") ? "Inter Sex" : "")));
                listNode.setMobileNo(DmShared.xNull(rsTemp.getString("mobile")));
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
            //sqlWithParam.setSQL("Select Name From subgroup Order By Name");
            sqlWithParam.setSQL("Select Name From subgroup");
            helper.addSort("name", DmEnum.DmSortType.ASC);
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

    @Override
    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHAccountGroup_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountGroup_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountGroup_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            // sqlWithParam.setSQL("SELECT ag.groupCode, ag.groupName, ag.nature, ag.groupNature FROM acGroup ag WHERE ag.Nature IN ('Employee') ORDER BY ag.groupName");
            sqlWithParam.setSQL("SELECT ag.groupCode, ag.groupName, ag.nature, ag.groupNature FROM acGroup ag WHERE ag.Nature IN ('Employee')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountGroup_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setGroupNature(DmShared.xNull(rsTemp.getString("groupNature")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("Nature")));
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
    public DmResultPageing<List<KeyFieldExtended>> getCity(DmHelper helper, boolean blnToEndOfFile) {
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
            // sqlWithParam.setSQL("SELECT CityCode , CityName , State FROM city ORDER BY CityName");
            sqlWithParam.setSQL("SELECT CityCode , CityName , State FROM city");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CityName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getCountry(DmHelper helper, boolean blnToEndOfFile) {
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
            //  sqlWithParam.setSQL("SELECT CODE, NAME FROM country ORDER BY NAME");
            sqlWithParam.setSQL("SELECT CODE, NAME FROM country");
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
                    + "FROM departmentMast dm "
                    + "left join subgroup sdm on dm.subcode = sdm.subcode ");

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDesignation(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code, name "
                    + "FROM designationmast ");
            //   + "ORDER BY name ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGender(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("M");
            listNode.setSharedKeyFieldName("Male");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("F");
            listNode.setSharedKeyFieldName("Female");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("I");
            listNode.setSharedKeyFieldName("Inter Sex");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMaritalStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Single");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("M");
            listNode.setSharedKeyFieldName("Married");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Divorced");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("W");
            listNode.setSharedKeyFieldName("Widow");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReligion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Hindu");
            listNode.setSharedKeyFieldName("Hindu");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Muslim");
            listNode.setSharedKeyFieldName("Muslim");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Christiantiy");
            listNode.setSharedKeyFieldName("Christianity");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Sikhism");
            listNode.setSharedKeyFieldName("Sikhism");
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
}
