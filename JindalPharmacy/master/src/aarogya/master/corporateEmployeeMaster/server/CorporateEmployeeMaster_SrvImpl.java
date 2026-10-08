package aarogya.master.corporateEmployeeMaster.server;

import aarogya.master.corporateEmployeeMaster.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CorporateEmployeeMaster_SrvImpl extends DmRSSPrivate implements CorporateEmployeeMaster_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCat = pvc.CORPORATE_EMPLOYEE_MASTER;

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
            rsTemp = cnTemp.createStatement().executeQuery("Select code From corporateEmployeeMast order by empName");

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
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile, String corporateCode) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select cem.empName As Name from corporateEmployeeMast as cem  where corporateCode = :corporateCode");
            helper.addSort("Name", DmSortType.ASC);
            sqlWithParam.setString("corporateCode", DmShared.iNull(corporateCode).toString());
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
    public DmResult<CorporateEmployeeMaster_Mdl> getMoveRec(String strKey) {

        DmResult<CorporateEmployeeMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CorporateEmployeeMaster_Mdl mrTemp = new CorporateEmployeeMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select cem.code As empCode , cem.empName As empName , cem.empCode As empCorporateCode ,cm.name As corporateName,cm.code As CorporateCode,cem.policyNo As policyName , "
                    + " cem.u_Name,cem.preparedBy, cem.cardHolderName As cardHolderName ,cem.relationship As relationship ,cem.rank As rank,cem.address1,cem.address2,c.cityName,cem.citycode,sm.name as state, "
                    + " con.name as countryName,zm.name as zone,cem.zoneCode,hl7.name as gender, cem.genderCode,hl7BG.name as bloodGroup, "
                    + " cem.bloodGroupCode,cem.dob,cem.dobTemp,cem.pinCode,cem.mobile "
                    + " from corporateEmployeeMast as cem "
                    + " left join corporatemast as cm on cm.code=cem.corporatecode  "
                    + " left join zonemast AS zm ON cem.zoneCode = zm.code   "
                    + " left join city AS c ON cem.citycode = c.citycode "
                    + " Left join country con on c.countryCode=con.code "
                    + " Left join stateMast sm on c.stateCode=sm.code "
                    + " left join hl7Mast as hl7 on cem.genderCode= hl7.code "
                    + " left join hl7Mast as hl7BG on cem.bloodGroupCode= hl7BG.code "
                    + "where cem.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("empCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("empName")));
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("empCorporateCode")));
                mrTemp.setCorporateName(DmShared.xNull(rsTemp.getString("corporateName")));
                mrTemp.setCorporateCode(DmShared.xNull(rsTemp.getString("CorporateCode")));
                mrTemp.setPolicyNo(DmShared.xNull(rsTemp.getString("policyName")));
                mrTemp.setCardHolderName(DmShared.xNull(rsTemp.getString("cardHolderName")));
                mrTemp.setRelationship(DmShared.xNull(rsTemp.getString("relationship")));
                mrTemp.setRank(DmShared.xNull(rsTemp.getString("rank")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("address1")));
                mrTemp.setAddress2(DmShared.xNull(rsTemp.getString("address2")));
                mrTemp.setCityText(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setCityTag(DmShared.xNull(rsTemp.getString("citycode")));

                String St_Coun = DmShared.xNull(rsTemp.getString("state")) + " " + "-" + " " + DmShared.xNull(rsTemp.getString("countryName"));
                if (!DmShared.xNull(rsTemp.getString("state")).equalsIgnoreCase("")) {
                    mrTemp.setState(St_Coun);
                } else {
                    mrTemp.setState("");
                }

                mrTemp.setZoneText(DmShared.xNull(rsTemp.getString("zone")));
                mrTemp.setZoneTag(DmShared.xNull(rsTemp.getString("zoneCode")));
                mrTemp.setGenderText(DmShared.xNull(rsTemp.getString("gender")));
                mrTemp.setGenderTag(DmShared.xNull(rsTemp.getString("genderCode")));
                mrTemp.setBloodGroupText(DmShared.xNull(rsTemp.getString("bloodGroup")));
                mrTemp.setBloodGroupTag(DmShared.xNull(rsTemp.getString("bloodGroupCode")));
                mrTemp.setDob(DmShared.dNull(rsTemp.getDate("dob")));
                mrTemp.setTempDOB(DmShared.dNull(rsTemp.getDate("dobTemp")));
                mrTemp.setPin(DmShared.xNull(rsTemp.getString("pinCode")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));

                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));

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
    public DmResult<String> saveRecord(CorporateEmployeeMaster_Mdl mrTemp) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        CallableStatement csTemp = null;
        String vType = "";

        try {

            CorporateEmployeeMaster_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "corporateEmployeeMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            //==============Record Existance Check (Emp Name)==================================
            sqlQuery = " SELECT Count(empName) As Cnt  FROM corporateEmployeeMast "
                    + "WHERE empName = :empName and corporateCode = :corporateCode  and code <> :code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("empName", DmShared.xNull(mrTemp.getSharedKeyFieldName().trim()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("corporateCode", DmShared.xNull(mrTemp.getCorporateCode()));

            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(500, "Name  already exists.");
            }
            rsTemp.close();
            psTemp.close();

            //=======================================================================
            if (DmShared.dNull(mrTemp.getDob()).equalsIgnoreCase("")) {
                mrTemp.setTempDOB(STServer.getTempDOB(DmShared.iNull(mrTemp.getAgeDay()).intValue(), DmShared.iNull(mrTemp.getAgeMonth()).intValue(), DmShared.iNull(mrTemp.getAgeYear()).intValue(), mrTemp.getDtVoucher()));
            } else {
                mrTemp.setTempDOB(DmShared.dNull(mrTemp.getDob()));
            }
            mrTemp.setDisplayDOB(STServer.getAgeDisplay(mrTemp.getTempDOB(), mrTemp.getDtVoucher(), DmShared.iNull(mrTemp.getShowDDUptoMonth()).intValue(), DmShared.iNull(mrTemp.getShowMMUptoYear()).intValue()));

            //==========================================================================================//
            vType = DmServer.getVType(vCat, cnTemp);

            //=======================================Manual Code generate===============================//
            if (blnAdd) {
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("corporate", new FieldUDT_Mdl("corporate", DmShared.xNull(mrTemp.getCorporateName()), DmShared.xNull(mrTemp.getCorporateName()).trim()));
                fieldUDTHashMap.put("name", new FieldUDT_Mdl("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
                fieldUDTHashMap.put("cardHolder", new FieldUDT_Mdl("cardHolder", DmShared.xNull(mrTemp.getCardHolderName()), DmShared.xNull(mrTemp.getCardHolderName()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getCorporateEmployeeId(?, ?)}");
                if (csTemp instanceof SQLServerCallableStatement) {
                    ((SQLServerCallableStatement) csTemp).setString(1, DmShared.iNull(vType).toString());
                    ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
                }
                rsTemp = csTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString(1)));
                }
                rsTemp.close();
                csTemp.close();
            }

            //==============Record Existance Check (Manual Code)==================================
            sqlQuery = " SELECT Count(empCode) As Cnt  FROM corporateEmployeeMast "
                    + "WHERE empCode = :empCode and code <> :code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("empCode", DmShared.xNull(mrTemp.getManualCode().trim()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));

            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new DmException(400, "Duplicate Code Not Allowed !!!");
            }
            rsTemp.close();
            psTemp.close();

            //=======================================================================
            if (mrTemp.getManualCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Code is mandatory");
            }

            //==============Insert or Update Record================================== 
            if (blnAdd) {

                sqlQuery = "insert into corporateEmployeeMast(code,empName,empCode,corporateCode,policyNo,cardHolderName,relationship,rank,address1,address2,zoneCode,pinCode,"
                        + "cityCode,mobile,dob,dobTemp,dobDisplay,genderCode,bloodGroupCode,u_EntDt,preparedBy) "
                        + " Values (:code,:empName,:empCode,:corporateCode,:policyNo,:cardHolderName,:relationship,:rank,:address1,:address2,:zoneCode,:pinCode,"
                        + " :cityCode,:mobile,:dob,:dobTemp,:dobDisplay,:genderCode,:bloodGroupCode,:u_EntDt,:preparedBy)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("empName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("empCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("corporateCode", DmShared.foriegnKey(mrTemp.getCorporateCode()));
                psTemp.setString("policyNo", DmShared.xNull(mrTemp.getPolicyNo()));
                psTemp.setString("cardHolderName", DmShared.xNull(mrTemp.getCardHolderName()));
                psTemp.setString("relationship", DmShared.xNull(mrTemp.getRelationship()));
                psTemp.setString("rank", DmShared.xNull(mrTemp.getRank()));
                psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("address2", DmShared.xNull(mrTemp.getAddress2()));
                psTemp.setString("zoneCode", DmShared.foriegnKey(mrTemp.getZoneTag()));
                psTemp.setString("pinCode", DmShared.xNull(mrTemp.getPin()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityTag()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("dobDisplay", DmShared.xNull(mrTemp.getDisplayDOB()));
                psTemp.setDate("dobTemp", DmServer.convertDate(mrTemp.getTempDOB()));
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setString("genderCode", DmShared.foriegnKey(mrTemp.getGenderTag()));
                psTemp.setString("bloodGroupCode", DmShared.foriegnKey(mrTemp.getBloodGroupTag()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));

            } else {
                sqlQuery = "Update corporateEmployeeMast Set "
                        + " empName = :empName, "
                        + " empCode = :empCode, "
                        + " corporateCode = :corporateCode, "
                        + " policyNo = :policyNo, "
                        + " cardHolderName = :cardHolderName , "
                        + " relationship = :relationship ,"
                        + " rank = :rank , "
                        + " address1 = :address1 ,"
                        + " address2 = :address2 ,"
                        + " zoneCode = :zoneCode ,"
                        + " pinCode = :pinCode ,"
                        + " cityCode = :cityCode ,"
                        + " mobile =:mobile ,"
                        + " dob = :dob ,"
                        + " dobTemp = :dobTemp ,"
                        + " dobDisplay = :dobDisplay ,"
                        + " genderCode = :genderCode ,"
                        + " bloodGroupCode = :bloodGroupCode ,"
                        + " u_Name = :u_Name  ,"
                        + " u_EntDt = :u_EntDt "
                        + " where code = :code ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("empName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("empCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("corporateCode", DmShared.foriegnKey(mrTemp.getCorporateCode()));
                psTemp.setString("policyNo", DmShared.xNull(mrTemp.getPolicyNo()));
                psTemp.setString("cardHolderName", DmShared.xNull(mrTemp.getCardHolderName()));
                psTemp.setString("relationship", DmShared.xNull(mrTemp.getRelationship()));
                psTemp.setString("rank", DmShared.xNull(mrTemp.getRank()));
                psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("address2", DmShared.xNull(mrTemp.getAddress2()));
                psTemp.setString("zoneCode", DmShared.foriegnKey(mrTemp.getZoneTag()));
                psTemp.setString("pinCode", DmShared.xNull(mrTemp.getPin()));
                psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityTag()));
                psTemp.setString("genderCode", DmShared.foriegnKey(mrTemp.getGenderTag()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("dobDisplay", DmShared.xNull(mrTemp.getDisplayDOB()));
                psTemp.setDate("dobTemp", DmServer.convertDate(mrTemp.getTempDOB()));
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setString("genderCode", DmShared.foriegnKey(mrTemp.getGenderTag()));
                psTemp.setString("bloodGroupCode", DmShared.foriegnKey(mrTemp.getBloodGroupTag()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

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

            sqlQuery = "Delete From corporateEmployeeMast Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
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

            sqlWithParam.setSQL("Select cem.code, cem.empCode , cem.empName As empName , cm.name As corporateName,cem.policyNo As policyName ,  "
                    + "cem.cardHolderName As cardHolderName ,cem.relationship As relationship ,cem.rank As rank "
                    + "from corporateEmployeeMast as cem  "
                    + "Left Join corporatemast as cm on cm.code=cem.corporatecode  ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();

                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("empName")));
                listNode.setCorporateName(DmShared.xNull(rsTemp.getString("corporateName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("empCode")));
                listNode.setPolicyNo(DmShared.xNull(rsTemp.getString("policyName")));
                listNode.setCardHolderName(DmShared.xNull(rsTemp.getString("cardHolderName")));
                listNode.setRelationship(DmShared.xNull(rsTemp.getString("relationship")));
                listNode.setRank(DmShared.xNull(rsTemp.getString("rank")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTextHRelationship(DmHelper pageing, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Self");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Brother");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Sister");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("3");
            listNode.setSharedKeyFieldName("Mother");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("4");
            listNode.setSharedKeyFieldName("Father");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("5");
            listNode.setSharedKeyFieldName("Wife");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("6");
            listNode.setSharedKeyFieldName("Husband");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("7");
            listNode.setSharedKeyFieldName("Son");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("8");
            listNode.setSharedKeyFieldName("Doughter");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("9");
            listNode.setSharedKeyFieldName("Grand Son");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("10");
            listNode.setSharedKeyFieldName("Grand Doughter");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("11");
            listNode.setSharedKeyFieldName("Grand Father");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("12");
            listNode.setSharedKeyFieldName("Grand Mother");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(pageing, lstTemp));
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());

            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCorporate(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select cm.code as code ,cm.name as name "
                    + "from corporateMast as cm ";

            sqlWithParam.setSQL(sqlQuery);
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

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getHL7Mast(DmHelper helper, boolean blnToEndOfFile, String tableName) {
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
            sqlWithParam.setSQL("select code, name from hl7Mast where tableName='" + tableName + "' and isActive=1");
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
            sqlWithParam.setSQL("SELECT c.cityCode,c.cityName,sm.name AS stateName,cn.name AS countryName "
                    + " FROM city AS c "
                    + " LEFT JOIN stateMast AS sm ON c.stateCode = sm.code "
                    + " LEFT JOIN country AS cn ON c.countryCode = cn.Code");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHZone(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ZM.Code, ZM.Name FROM zonemast ZM ");

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
    public DmResult<KeyFieldExtended> getDefaultValues() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        KeyFieldExtended defaultValue = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select en.showDDUptoMonth ,en.showMMUptoYear from enviro en where en.site_Code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("showDDUptoMonth")));
                defaultValue.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("showMMUptoYear")));
                dmrTemp.setData(defaultValue);

            }
            defaultValue.setSharedKeyFieldCode(DmServer.getServerDateTime()[0]);
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;

    }

}
