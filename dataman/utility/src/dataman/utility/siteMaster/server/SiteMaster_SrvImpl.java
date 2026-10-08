package dataman.utility.siteMaster.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.utility.siteMaster.shared.*;
import dataman.utility.siteMaster.shared.SiteMaster_Vld.RegisteredType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SiteMaster_SrvImpl extends DmRSSPrivate implements SiteMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            rsTemp = cnTemp.createStatement().executeQuery("Select Code As SearchCode From SiteMast order By name");

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
    public DmResult<SiteMaster_Mdl> getMoveRec(String strKey) {
        DmResult<SiteMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        SiteMaster_Mdl mrTemp = new SiteMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQuery = " Select S.Code, S.Name,isNull(S.isHO,'') AS ho_Yn,S.Address1, "
                    + " S.Address2,S.City,S.TinNo,S.NotificationNo,S.PanNo,S.PreparedBy,S.u_name, "
                    + " S.pin,S.ManualCode,S.regionalLanguage,"
                    + " S.phone, S.fax, S.email, S.gram, S.Country,S.TinDate,S.SiteDesc,s.gsTin,s.cin, "
                    + " S.BranchCode As BCode,S.PLTCode, B.Name As BName, SLT.Comp_Name,SLT.SName, S.gstinDate,S.registrationType,"
                    + " S.stateNumericCode,S.stateName,S.drugLicenseNo,s.clientCode,s.serialKey,"
                    + " s.siteDisplayName,s.IECode,s.RBICode "
                    + " From SiteMast S "
                    + " Left Join  BranchMast b on s.BranchCode = b.Code"
                    + " left join  ProductLicensedTo SLT on SLT.PLTCode = S.PLTCode"
                    + " Where S.Code= :code order by S.Name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(strKey);
                mrTemp.setRegionalLanguageCode(DmShared.xNull(rsTemp.getString("regionalLanguage")));
                mrTemp.setRegionalLanguageName(DmShared.xNull(rsTemp.getString("regionalLanguage")));
                mrTemp.setBranchCode(DmShared.xNull(rsTemp.getString("BCode")));
                mrTemp.setBranchName(DmShared.xNull(rsTemp.getString("Bname")));
                mrTemp.setPltCode(DmShared.xNull(rsTemp.getString("PLTCode")));
                mrTemp.setPltName(DmShared.xNull(rsTemp.getString("Comp_name")));
                mrTemp.setPltShortName(DmShared.xNull(rsTemp.getString("SName")));
                mrTemp.setSiteName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSiteDescription(DmShared.xNull(rsTemp.getString("SiteDesc")));
                mrTemp.setCode(DmShared.xNull(rsTemp.getString("Code")));

                if (DmShared.iNull(rsTemp.getString("Ho_YN")).intValue() == 1) {
                    mrTemp.setHoCode(DmShared.xNull(rsTemp.getString("Ho_YN")));
                    mrTemp.setHoName(DmShared.xNull("Yes"));
                } else {
                    mrTemp.setHoCode(DmShared.xNull("0"));
                    mrTemp.setHoName(DmShared.xNull("No"));
                }

                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("manualcode")));
                mrTemp.setSiteName(DmShared.xNull(rsTemp.getString("sitedesc")));
                mrTemp.setAddress1(DmShared.xNull(rsTemp.getString("address1")));
                mrTemp.setAddress2(DmShared.xNull(rsTemp.getString("address2")));
                mrTemp.setCity(DmShared.xNull(rsTemp.getString("city")));
                mrTemp.setPinNO(DmShared.xNull(rsTemp.getString("pin")));
                mrTemp.setPanNo(DmShared.xNull(rsTemp.getString("panno")));
                mrTemp.setNotificationNo(DmShared.xNull(rsTemp.getString("notificationNo")));
                mrTemp.setTinNo(DmShared.xNull(rsTemp.getString("tinno")));
                mrTemp.setPhone(DmShared.xNull(rsTemp.getString("phone")));
                mrTemp.setFaxNo(DmShared.xNull(rsTemp.getString("fax")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("email")));
                mrTemp.setGram(DmShared.xNull(rsTemp.getString("gram")));
                mrTemp.setCountry(DmShared.xNull(rsTemp.getString("country")));
                mrTemp.setTinDate(DmShared.dNull(rsTemp.getDate("Tindate")));
                mrTemp.setGsTin(DmShared.xNull(rsTemp.getString("gstin")));
                mrTemp.setCin(DmShared.xNull(rsTemp.getString("cin")));
                mrTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                mrTemp.setStateNumericCode(DmShared.xNull(rsTemp.getString("stateNumericCode")));
                mrTemp.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                mrTemp.setRegistrationTypeCode(DmShared.xNull(rsTemp.getString("registrationType")));
                if (!DmShared.xNull(rsTemp.getString("registrationType")).trim().equalsIgnoreCase("")) {
                    mrTemp.setRegistrationTypeName(RegisteredType.getKeyOf(DmShared.xNull(rsTemp.getString("registrationType")).trim()).getValue());
                }
                mrTemp.setClientCode(DmShared.xNull(rsTemp.getString("clientCode")));
                mrTemp.setSerialKey(DmShared.xNull(rsTemp.getString("serialKey")));
                mrTemp.setSiteDisplayName(DmShared.xNull(rsTemp.getString("siteDisplayName")));
                mrTemp.setDrugLicenseNo(DmShared.xNull(rsTemp.getString("drugLicenseNo")));
                mrTemp.setIeCode(DmShared.xNull(rsTemp.getString("IECode")));
                mrTemp.setRbiCode(DmShared.xNull(rsTemp.getString("RBICode")));
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
    public DmResult<String> deleteRecord(String strKey, String pltCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            strSQL = " SELECT * FROM usersite WHERE sitelist LIKE :siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("siteCode", "%" + strKey + "%");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                throw new UnsupportedOperationException("Could not delete, Site is in use.");
            }

            strSQL = " Select CentralData_Path From Company C left Join sys.databases as SD on SD.name = RTrim(LTrim(C.CentralData_Path)) "
                    + " Where PLTCode = :PLTCode And isNull(SD.name,'')<>'' Group By CentralData_Path ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("PLTCode", pltCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                strSQL = " Delete From  " + rsTemp.getString("CentralData_Path") + ".dbo.SiteMast Where Code= :Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("Code", strKey);
                psTemp.executeUpdate();
                psTemp.close();

                strSQL = " Delete From  siteMast Where Code= :Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("Code", strKey);
                psTemp.executeUpdate();
                psTemp.close();

            }

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
    public DmResult<String> saveRecord(SiteMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null, psTemp2 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null, rsTemp1 = null, rsTemp2 = null;
        dmrTemp.setData("");
        Boolean flag = false;
        try {
            SiteMaster_Vld.chkSave(mrTemp);

            if (DmServer.localeCountry.equalsIgnoreCase(DmServer.baseDmLocaleCountry.INDIA.getDmCode())) {
                if (!DmShared.xNull(mrTemp.getPanNo()).trim().equalsIgnoreCase("")) {
                    if (DmShared.xNull(mrTemp.getPanNo()).length() != 10) {
                        throw new DmException(850, "PAN should be of 10(Ten) characters.");
                    } else {
                        DmShared.isValidPanNo(mrTemp.getPanNo(), 850);
                    }
                }

                if (!DmShared.xNull(mrTemp.getGsTin()).equalsIgnoreCase("")) {
                    DmShared.isValidText(mrTemp.getPanNo(), "PAN", 850);
                }

                if (!DmShared.xNull(mrTemp.getTinNo()).equalsIgnoreCase("")) {
                    if (DmShared.xNull(mrTemp.getTinDate()).equalsIgnoreCase("")) {
                        flag = true;
                    }
                } else {
                    if (DmShared.xNull(mrTemp.getTinNo()).equalsIgnoreCase("")) {
                        if (!DmShared.xNull(mrTemp.getTinDate()).equalsIgnoreCase("")) {
                            flag = true;
                        }
                    }
                }
                if (flag) {
                    throw new DmException(900, "TIN No. and TIN date both are required.");
                }
                if (DmShared.xNull(mrTemp.getRegistrationTypeCode()).equalsIgnoreCase((RegisteredType.REGISTERED.getId()))
                        || DmShared.xNull(mrTemp.getRegistrationTypeCode()).equalsIgnoreCase((RegisteredType.COMPOSITE.getId()))) {
                    DmShared.isValidText(mrTemp.getGsTin(), "GSTIN No.", 1000);
                    DmShared.isValidText(mrTemp.getGstinDate(), "GSTIN Date", 1100);
                }
                if (!DmShared.xNull(mrTemp.getGsTin()).equalsIgnoreCase("")) {
                    DmShared.isValidGSTIN(mrTemp.getStateNumericCode(), mrTemp.getPanNo(), mrTemp.getGsTin(), 1000);
                }
                if (DmShared.xNull(mrTemp.getRegistrationTypeCode()).equalsIgnoreCase((RegisteredType.UNREGISTERED.getId()))) {
                    if (!DmShared.xNull(mrTemp.getGsTin()).equalsIgnoreCase("") || !DmShared.xNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
                        throw new DmException(1000, "In case of unregistered party type, you should not fill GSTIN No. and GSTIN Date.");
                    }
                }
                if (!DmShared.xNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
                    if (DmShared.convertDate(mrTemp.getGstinDate()).getTime() > (DmShared.convertDate(mrTemp.getCurrentDate())).getTime()) {
                        throw new DmException(1100, "GSTIN Date should not be greater than current date.");
                    }
                }

            }
            if (!DmShared.xNull(mrTemp.getManualCode()).trim().matches("[a-z A-Z]+")) {
                throw new UnsupportedOperationException("Manual Code must be alphabet only.");
            }

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                sqlQuery = " Select Count(Code) As Cnt From SiteMast S Where S.Code = :Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("Code", mrTemp.getCode());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Code Already Exists.");
                }
                rsTemp.close();
                psTemp.close();
                mrTemp.setSharedKeyFieldCode(mrTemp.getCode());
                blnAdd = true;
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select Count(Name) As Cnt From SiteMast S Where S.PLTCode = :PLTCode And S.BranchCode = :BranchCode And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("PLTCode", mrTemp.getPltCode());
            psTemp.setString("BranchCode", mrTemp.getBranchCode());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Combination Of [Product Licensed To + Branch Name] Already Exists.");
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.setManualCode(DmShared.xNull(mrTemp.getManualCode()).replace(" ", ""));
            sqlQuery = " Select Count(Name) As Cnt From SiteMast S Where S.Name = :Name And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Name", mrTemp.getSiteName());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Site Name Already Exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " Select Count(ManualCode) As Cnt From SiteMast S Where S.ManualCode = :ManualCode And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("ManualCode", mrTemp.getManualCode());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Manual Code Already Exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getHoCode()).equalsIgnoreCase("Y")) {
                sqlQuery = " Select Count(isHO) as Cnt From SiteMast where isHO='Y' And Code <>  :Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("There Should Be Only One Head Office.");
                }
                rsTemp.close();
                psTemp.close();
            }

            if (blnAdd) {
                sqlQuery = "Insert Into " + DmServer.dbCompany + ".dbo.SiteMast(Code,BranchCode, PLTCode, Name,isHO,SiteDesc, "
                        + " address1,address2,city,TinNo, NotificationNo,PanNo,pin,ManualCode,"
                        + " Phone, Fax, Email, Gram, Country, TinDate,PreparedBy,regionalLanguage,gstin,cin,gstinDate,stateNumericCode,"
                        + " stateName,registrationType,drugLicenseNo,clientCode,serialKey,IECode,RBICode,siteDisplayName)"
                        + " Values ( :Code,:BranchCode, :PLTCode, :Name,:isHO,:SiteDesc, "
                        + " :address1,:address2,:city,:TinNo, :NotificationNo,:PanNo,:pin,:ManualCode,"
                        + " :Phone, :Fax, :Email, :Gram, :Country, :TinDate,:PreparedBy,:regionalLanguage,:gstin,:cin,:gstinDate,:stateNumericCode, "
                        + " :stateName,:registrationType,:drugLicenseNo,:clientCode,:serialKey,:IECode,:RBICode,:siteDisplayName)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("BranchCode", mrTemp.getBranchCode());
                psTemp.setString("PLTCode", mrTemp.getPltCode());
                psTemp.setString("Name", DmShared.xNull(mrTemp.getSiteName()));
                psTemp.setString("isHO", (DmShared.iNull(mrTemp.getHoCode()).intValue() == 0 ? "0" : "1"));
                psTemp.setString("SiteDesc", DmShared.xNull(mrTemp.getSiteDescription()));
                psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("address2", DmShared.xNull(mrTemp.getAddress2()));
                psTemp.setString("city", DmShared.xNull(mrTemp.getCity()));
                psTemp.setString("TinNo", DmShared.xNull(mrTemp.getTinNo()));
                psTemp.setString("NotificationNo", DmShared.xNull(mrTemp.getNotificationNo()));
                psTemp.setString("PanNo", DmShared.xNull(mrTemp.getPanNo()));
                psTemp.setString("NotificationNo", DmShared.xNull(mrTemp.getNotificationNo()));
                psTemp.setString("PanNo", DmShared.xNull(mrTemp.getPanNo()));
                psTemp.setString("pin", DmShared.xNull(mrTemp.getPinNO()));
                psTemp.setString("ManualCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("Phone", DmShared.xNull(mrTemp.getPhone()));
                psTemp.setString("Fax", DmShared.xNull(mrTemp.getFaxNo()));
                psTemp.setString("Email", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("Gram", DmShared.xNull(mrTemp.getGram()));
                psTemp.setString("Country", DmShared.xNull(mrTemp.getCountry()));
                psTemp.setString("regionalLanguage", mrTemp.getRegionalLanguageCode());
                psTemp.setDate("TinDate", DmServer.convertDate(mrTemp.getTinDate()));
                psTemp.setString("gstin", DmShared.xNull(mrTemp.getGsTin().toUpperCase()));
                psTemp.setString("cin", DmShared.xNull(mrTemp.getCin()));
                psTemp.setString("PreparedBy", DmServer.getSsnUserName(this));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getGstinDate()));
                psTemp.setString("stateNumericCode", DmShared.xNull(mrTemp.getStateNumericCode()));
                psTemp.setString("stateName", DmShared.xNull(mrTemp.getStateName()));
                psTemp.setString("registrationType", DmShared.xNull(mrTemp.getRegistrationTypeCode()));
                psTemp.setString("drugLicenseNo", DmShared.xNull(mrTemp.getDrugLicenseNo()));
                psTemp.setString("clientCode", DmShared.xNull(mrTemp.getClientCode()));
                psTemp.setString("serialKey", DmShared.xNull(mrTemp.getSerialKey()));
                psTemp.setString("IECode", DmShared.xNull(mrTemp.getIeCode()));
                psTemp.setString("RBICode", DmShared.xNull(mrTemp.getRbiCode()));
                psTemp.setString("siteDisplayName", DmShared.xNull(mrTemp.getSiteDisplayName()));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                sqlQuery = " Update " + DmServer.dbCompany + ".dbo.SiteMast Set  PLTCode = :PLTCode ,BranchCode= :BranchCode ,Name= :Name ,"
                        + " isHO= :isHO ,Pin= :Pin, PanNo= :PanNo ,NotificationNo= :NotificationNo ,tinno= :tinno ,City= :City ,address2= :address2 ,"
                        + " ManualCode= :ManualCode ,phone= :phone ,fax= :fax ,email= :email ,gram= :gram, Country= :Country,TinDate= :TinDate ,"
                        + " u_name= :u_name ,address1= :address1 ,SiteDesc= :SiteDesc,regionalLanguage= :regionalLanguage,gstin= :gstin,cin= :cin,"
                        + " gstinDate=:gstinDate,stateNumericCode=:stateNumericCode,stateName=:stateName,registrationType=:registrationType,"
                        + " drugLicenseNo=:drugLicenseNo,rbicode=:rbicode,iecode=:iecode,serialKey=:serialKey,"
                        + " siteDisplayName=:siteDisplayName,clientCode=:clientCode"
                        + " Where Code = :Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("PLTCode", mrTemp.getPltCode());
                psTemp.setString("BranchCode", mrTemp.getBranchCode());
                psTemp.setString("Name", DmShared.xNull(mrTemp.getSiteName()));
                psTemp.setString("isHO", (DmShared.iNull(mrTemp.getHoCode()).intValue() == 0 ? "0" : "1"));
                psTemp.setString("Pin", DmShared.xNull(mrTemp.getPinNO()));
                psTemp.setString("PanNo", DmShared.xNull(mrTemp.getPanNo().toUpperCase()));
                psTemp.setString("NotificationNo", DmShared.xNull(mrTemp.getNotificationNo()));
                psTemp.setString("tinno", DmShared.xNull(mrTemp.getTinNo()));
                psTemp.setString("City", DmShared.xNull(mrTemp.getCity()));
                psTemp.setString("address2", DmShared.xNull(mrTemp.getAddress2()));
                psTemp.setString("ManualCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("phone", DmShared.xNull(mrTemp.getPhone()));
                psTemp.setString("fax", DmShared.xNull(mrTemp.getFaxNo()));
                psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()));
                psTemp.setString("gram", DmShared.xNull(mrTemp.getGram()));
                psTemp.setString("Country", DmShared.xNull(mrTemp.getCountry()));
                psTemp.setDate("TinDate", DmServer.convertDate(mrTemp.getTinDate()));
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress1()));
                psTemp.setString("SiteDesc", DmShared.xNull(mrTemp.getSiteDescription()));
                psTemp.setString("regionalLanguage", DmShared.xNull(mrTemp.getRegionalLanguageCode()));
                psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("gstin", DmShared.xNull(mrTemp.getGsTin().toUpperCase()));
                psTemp.setString("cin", DmShared.xNull(mrTemp.getCin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getGstinDate()));
                psTemp.setString("stateNumericCode", DmShared.xNull(mrTemp.getStateNumericCode()));
                psTemp.setString("stateName", DmShared.xNull(mrTemp.getStateName()));
                psTemp.setString("registrationType", DmShared.xNull(mrTemp.getRegistrationTypeCode()));
                psTemp.setString("drugLicenseNo", DmShared.xNull(mrTemp.getDrugLicenseNo()));
                psTemp.setString("clientCode", DmShared.xNull(mrTemp.getClientCode()));
                psTemp.setString("serialKey", DmShared.xNull(mrTemp.getSerialKey()));
                psTemp.setString("iecode", DmShared.xNull(mrTemp.getIeCode()));
                psTemp.setString("rbicode", DmShared.xNull(mrTemp.getRbiCode()));
                psTemp.setString("siteDisplayName", DmShared.xNull(mrTemp.getSiteDisplayName()));
                psTemp.executeUpdate();
                psTemp.close();
            }

            sqlQuery = " Select CentralData_Path From Company C left Join sys.databases as SD on SD.name = RTrim(LTrim(C.CentralData_Path)) "
                    + " Where PLTCode = :PLTCode And isNull(SD.name,'')<>'' Group By CentralData_Path ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("PLTCode", mrTemp.getPltCode());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                sqlQuery = " SELECT COUNT(*) AS cnt "
                        + "	FROM " + rsTemp.getString("CentralData_Path") + ".sys.objects T  "
                        + "	WHERE  T.name = 'SiteMast' ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp1 = psTemp1.executeQuery();
                rsTemp1.next();

                sqlQuery = " Select Count(Code) as cnt From " + rsTemp.getString("CentralData_Path") + ".dbo.SiteMast  Where Code = :Code  ";
                psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp2.setString("Code", mrTemp.getSharedKeyFieldCode());
                rsTemp2 = psTemp2.executeQuery();
                rsTemp2.next();
                if (rsTemp1.getLong("cnt") > 0 && rsTemp2.getLong("cnt") > 0) {
                    sqlQuery = " Update " + rsTemp.getString("CentralData_Path") + ".dbo.SiteMast Set  PLTCode = :PLTCode ,BranchCode= :BranchCode ,Name= :Name ,isHO= :isHO ,Pin= :Pin ,"
                            + " PanNo= :PanNo ,NotificationNo= :NotificationNo ,tinno= :tinno ,City= :City ,address2= :address2 ,ManualCode= :ManualCode ,"
                            + " phone= :phone , fax= :fax ,email= :email ,gram= :gram, Country= :Country,TinDate= :TinDate  ,address1= :address1 ,"
                            + " SiteDesc= :SiteDesc,regionalLanguage= :regionalLanguage,gstin= :gstin,cin= :cin,"
                            + " gstinDate=:gstinDate,stateNumericCode=:stateNumericCode,stateName=:stateName,registrationType=:registrationType,"
                            + " drugLicenseNo=:drugLicenseNo,clientCode=:clientCode,serialKey=:serialKey,IECode= :iecode ,RBICode= :rbicode,"
                            + " siteDisplayName=:siteDisplayName "
                            + " Where Code = :Code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("PLTCode", mrTemp.getPltCode());
                    psTemp.setString("BranchCode", mrTemp.getBranchCode());
                    psTemp.setString("Name", DmShared.xNull(mrTemp.getSiteName()));
                    psTemp.setString("isHO", mrTemp.getHoCode());
                    psTemp.setString("Pin", DmShared.xNull(mrTemp.getPinNO()));
                    psTemp.setString("PanNo", DmShared.xNull(mrTemp.getPanNo().toUpperCase()));
                    psTemp.setString("NotificationNo", DmShared.xNull(mrTemp.getNotificationNo()));
                    psTemp.setString("tinno", DmShared.xNull(mrTemp.getTinNo()));
                    psTemp.setString("City", DmShared.xNull(mrTemp.getCity()));
                    psTemp.setString("address2", DmShared.xNull(mrTemp.getAddress2()));
                    psTemp.setString("ManualCode", DmShared.xNull(mrTemp.getManualCode()));
                    psTemp.setString("phone", DmShared.xNull(mrTemp.getPhone()));
                    psTemp.setString("fax", DmShared.xNull(mrTemp.getFaxNo()));
                    psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()));
                    psTemp.setString("gram", DmShared.xNull(mrTemp.getGram()));
                    psTemp.setString("Country", DmShared.xNull(mrTemp.getCountry()));
                    psTemp.setDate("TinDate", DmServer.convertDate(mrTemp.getTinDate()));
                    psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress1()));
                    psTemp.setString("SiteDesc", DmShared.xNull(mrTemp.getSiteDescription()));
                    psTemp.setString("regionalLanguage", mrTemp.getRegionalLanguageCode());
                    psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("gstin", DmShared.xNull(mrTemp.getGsTin().toUpperCase()));
                    psTemp.setString("cin", DmShared.xNull(mrTemp.getCin()));
                    psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getGstinDate()));
                    psTemp.setString("stateNumericCode", DmShared.xNull(mrTemp.getStateNumericCode()));
                    psTemp.setString("stateName", DmShared.xNull(mrTemp.getStateName()));
                    psTemp.setString("registrationType", DmShared.xNull(mrTemp.getRegistrationTypeCode()));
                    psTemp.setString("drugLicenseNo", DmShared.xNull(mrTemp.getDrugLicenseNo()));
                    psTemp.setString("clientCode", DmShared.xNull(mrTemp.getClientCode()));
                    psTemp.setString("serialKey", DmShared.xNull(mrTemp.getSerialKey()));
                    psTemp.setString("iecode", DmShared.xNull(mrTemp.getIeCode()));
                    psTemp.setString("rbicode", DmShared.xNull(mrTemp.getRbiCode()));
                    psTemp.setString("siteDisplayName", DmShared.xNull(mrTemp.getSiteDisplayName()));
                    psTemp.executeUpdate();
                    psTemp.close();
                } else if (rsTemp1.getLong("cnt") > 0) {
                    sqlQuery = "Insert Into " + rsTemp.getString("CentralData_Path") + ".dbo.SiteMast(Code,BranchCode, PLTCode, Name,isHO,SiteDesc, "
                            + " address1,address2,city,TinNo, NotificationNo,PanNo,pin,ManualCode,"
                            + " Phone, Fax, Email, Gram, Country, TinDate,PreparedBy,regionalLanguage,gstin,cin,gstinDate,stateNumericCode,"
                            + " stateName,registrationType,drugLicenseNo,clientCode,serialKey,iecode,rbicode,siteDisplayName)"
                            + " Values ( :Code,:BranchCode, :PLTCode, :Name,:isHO,:SiteDesc, "
                            + " :address1,:address2,:city,:TinNo, :NotificationNo,:PanNo,:pin,:ManualCode,"
                            + " :Phone, :Fax, :Email, :Gram, :Country, :TinDate,:PreparedBy,:regionalLanguage,:gstin,:cin,:gstinDate,:stateNumericCode,"
                            + "  :stateName,:registrationType,:drugLicenseNo,:clientCode,:serialKey,:iecode,:rbicode,:siteDisplayName)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("BranchCode", mrTemp.getBranchCode());
                    psTemp.setString("PLTCode", mrTemp.getPltCode());
                    psTemp.setString("Name", mrTemp.getSiteName());
                    psTemp.setString("isHO", mrTemp.getHoCode());
                    psTemp.setString("SiteDesc", mrTemp.getSiteDescription());
                    psTemp.setString("address1", mrTemp.getAddress1());
                    psTemp.setString("address2", mrTemp.getAddress2());
                    psTemp.setString("city", mrTemp.getCity());
                    psTemp.setString("TinNo", mrTemp.getTinNo());
                    psTemp.setString("NotificationNo", mrTemp.getNotificationNo());
                    psTemp.setString("PanNo", mrTemp.getPanNo().toUpperCase());
                    psTemp.setString("NotificationNo", mrTemp.getNotificationNo());
                    psTemp.setString("pin", mrTemp.getPinNO());
                    psTemp.setString("ManualCode", mrTemp.getManualCode());
                    psTemp.setString("Phone", mrTemp.getPhone());
                    psTemp.setString("Fax", mrTemp.getFaxNo());
                    psTemp.setString("Email", mrTemp.getEmail());
                    psTemp.setString("Gram", mrTemp.getGram());
                    psTemp.setString("Country", mrTemp.getCountry());
                    psTemp.setDate("TinDate", DmServer.convertDate(mrTemp.getTinDate()));
                    psTemp.setString("regionalLanguage", mrTemp.getRegionalLanguageCode());
                    psTemp.setString("PreparedBy", "");
                    psTemp.setString("gstin", DmShared.xNull(mrTemp.getGsTin().toUpperCase()));
                    psTemp.setString("cin", DmShared.xNull(mrTemp.getCin()));
                    psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getGstinDate()));
                    psTemp.setString("stateNumericCode", DmShared.xNull(mrTemp.getStateNumericCode()));
                    psTemp.setString("stateName", DmShared.xNull(mrTemp.getStateName()));
                    psTemp.setString("registrationType", DmShared.xNull(mrTemp.getRegistrationTypeCode()));
                    psTemp.setString("drugLicenseNo", DmShared.xNull(mrTemp.getDrugLicenseNo()));
                    psTemp.setString("clientCode", DmShared.xNull(mrTemp.getClientCode()));
                    psTemp.setString("serialKey", DmShared.xNull(mrTemp.getSerialKey()));
                    psTemp.setString("iecode", DmShared.xNull(mrTemp.getIeCode()));
                    psTemp.setString("rbicode", DmShared.xNull(mrTemp.getRbiCode()));
                    psTemp.setString("siteDisplayName", DmShared.xNull(mrTemp.getSiteDisplayName()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

            }
            rsTemp.close();
            psTemp.close();

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
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (rsTemp2 != null) {
                    rsTemp2.close();
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<TxtHBranch_Mdl>> getTxtHBranch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBranch_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHBranch_Mdl> lstTemp = new ArrayList<>();
        TxtHBranch_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("SELECT B.Code, B.Name, B.City From BranchMast B ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBranch_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("City")));
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
    public DmResultPageing<List<TxtHPLT_Mdl>> getTxtHPLT(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPLT_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPLT_Mdl> lstTemp = new ArrayList<>();
        TxtHPLT_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("SELECT PLTCode, Comp_Name,SName From ProductLicensedTo ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHPLT_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("PLTCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Comp_Name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("SName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHHeadOffice(DmHelper helper, boolean blnToEndOfFile) {

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
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("Select S.Code,S.Name,SLT.Comp_Name as CompanyName,B.Name as BranchName,SLT.SName "
                    + "From SiteMast S  "
                    + "Left Join  BranchMast B on S.BranchCode = B.Code "
                    + "Left join  ProductLicensedTo SLT on SLT.PLTCode = S.PLTCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            int srlNo = 0;
            while (rsTemp.next()) {
                srlNo = srlNo + 1;
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSrlNo(String.valueOf(srlNo));
                listNode.setCodeValue(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSiteName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setBranchName(DmShared.xNull(rsTemp.getString("BranchName")));
                listNode.setCompanyName(DmShared.xNull(rsTemp.getString("CompanyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("SName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRegionalLanguage(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmEnum.RegionalLanguage.ENGLISH.toString());
            listNode.setSharedKeyFieldName(DmEnum.RegionalLanguage.ENGLISH.toString());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmEnum.RegionalLanguage.HINDI.toString());
            listNode.setSharedKeyFieldName(DmEnum.RegionalLanguage.HINDI.toString());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmEnum.RegionalLanguage.BANGALI.toString());
            listNode.setSharedKeyFieldName(DmEnum.RegionalLanguage.BANGALI.toString());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmEnum.RegionalLanguage.MARATHI.toString());
            listNode.setSharedKeyFieldName(DmEnum.RegionalLanguage.MARATHI.toString());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmEnum.RegionalLanguage.PUNJABI.toString());
            listNode.setSharedKeyFieldName(DmEnum.RegionalLanguage.PUNJABI.toString());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRegistrationType(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (RegisteredType nature : RegisteredType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(nature.getId());
                listNode.setSharedKeyFieldName(nature.getValue());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHState(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT Name,numericCode from stateMast  where isnull(numericCode,'0') <> '0' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("numericCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("numericCode")));
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
