package aarogya.common.server;

import dataman.dmBase.self.shared.*;
import aarogya.common.shared.Common_Srv;
import aarogya.selfToolkit.stStatic.server.STJasperVariables;
import aarogya.selfToolkit.stStatic.server.STManageUser;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.PCSData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;

public class Common_SrvImpl extends DmRSSPrivate implements Common_Srv {

    @Override
    public DmResult<PCSData> reInitialize(PCSEnum pcsEnum) {
        DmResult<PCSData> dmrTemp = new DmResult();
        PCSData pcsMain = new PCSData();
        dmrTemp.setData(pcsMain);

        try {
            if (pcsEnum == PCSEnum.Login) {
                ((DmSession) this.getThreadLocalRequest().getSession()).setSessionStatus(DmEnum.DmSessionStatus.LOGGED_OUT);
//                ((DmRequestWrapper) this.getLocalRequest()).getDatamanSession().setSessionStatus(DmEnum.DmSessionStatus.LOGGED_OUT);
//                ((DmRequestWrapper) this.getLocalRequest()).getDatamanSession().setSessionStatus(DmEnum.DmSessionStatus.LOGGED_IN);
            } else if (pcsEnum == PCSEnum.LOCK) {
                ((DmRequestWrapper) this.getLocalRequest()).getDatamanSession().setSessionStatus(DmEnum.DmSessionStatus.LOCK);
            } else {
                pcsMain.setPLT(DmServer.getPLTs(this, DmServer.getSsnPLTCode(this)));
                if (pcsMain.pltSelected != null) {
                    pcsMain.setCompany(DmServer.getCompanys(this, DmServer.getSsnCompanyCode(this)));
                    if (pcsMain.cSelected != null) {
                        pcsMain.setSite(DmServer.getSites(this, DmServer.getSsnSiteCode(this)));
                    }
                }
            }
            dmrTemp.setStatus(true);
            dmrTemp.setErrorMessage(pcsEnum.toString());
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    //<editor-fold defaultstate="collapsed" desc="WithKey">
//    @Override
//    public DmResult<UserPermission> getUserPermission() {
//        DmResult<UserPermission> dmrTemp = new DmResult();
//        DmResult<String> dmResult;
//        try {
//            STServer.setSsnUserService(this);
//            STServer.setSsnUserLinkDetails(this);
//            STServer.setSsnEnviro(this);
//            if (DmServer.getManageUser() == null) {
//                DmServer.setManageUser(new STManageUser());
//            }
//            if (DmServer.getDMJasperVariables() == null) {
//                DmServer.setDMJasperVariables(new STJasperVariables());
//            }
//            dmResult = validateKey();
//            dmrTemp.setData(DmServer.getUserPermission(this, dmResult.getData()));
//            dmrTemp.setErrorNumber(dmResult.getErrorNumber());
//            dmrTemp.setErrorMessage(dmResult.getErrorMessage());
//            dmrTemp.setStatus(true);
//        } catch (Exception ex) {
//            dmrTemp.setErrorMessage(ex.getMessage());
//        }
//        return dmrTemp;
//    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="WithOutKey">
    @Override
    public DmResult<UserPermission> getUserPermission() {
        DmResult<UserPermission> dmrTemp = new DmResult();
        try {
            STServer.setSsnUserService(this);
            STServer.setSsnUserLinkDetails(this);
            STServer.setSsnEnviro(this);
            if (DmServer.getManageUser() == null) {
                DmServer.setManageUser(new STManageUser());
            }
            if (DmServer.getDMJasperVariables() == null) {
                DmServer.setDMJasperVariables(new STJasperVariables());
            }
            dmrTemp.setData(DmServer.getUserPermission(this, ""));
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
        }
        return dmrTemp;
    }

    //</editor-fold>
    @Override
    public DmResult<Integer> getPACWebPort() {
        DmResult<Integer> result = new DmResult<>();
        try {
            System.out.println("Request found to get pacwebport");
            result.setData(DmShared.iNull(DmServer.pacPortWeb).intValue());
            result.setStatus(true);
        } catch (Exception ex) {
            result.setStatus(false);
            ex.printStackTrace();
        }
        return result;
    }

//    private KEY_OUTPUT_PARAMS dataman() {
//        cls_NativeMethods dmkv = new cls_NativeMethods();
//        KEY_INPUT_PARAMS keyInputParams = new KEY_INPUT_PARAMS();
//        KEY_OUTPUT_PARAMS keyOutputParams = new KEY_OUTPUT_PARAMS();
//
//        keyInputParams.PartyName = "DATAMAN Hospital";
//        keyInputParams.ClientCode = "";
//        keyInputParams.ProductName = "";
//        keyInputParams.City = "KANPUR";
//        keyInputParams.GSTIN = "09AAACD5213H1ZY";
//        keyInputParams.MacAddress = "";
//        keyInputParams.ServerID = "";
//        keyOutputParams = dmkv.ValidateKey(keyInputParams, "", keyOutputParams);
//
//        return keyOutputParams;
//    }
//
//    private KEY_OUTPUT_PARAMS sukh() {
//        cls_NativeMethods dmkv = new cls_NativeMethods();
//        KEY_INPUT_PARAMS keyInputParams = new KEY_INPUT_PARAMS();
//        KEY_OUTPUT_PARAMS keyOutputParams = new KEY_OUTPUT_PARAMS();
//
//        keyInputParams.PartyName = "SUKHDA MULtISPECIALITY HOSPITAL";
//        keyInputParams.ClientCode = "SUKH001";
//        keyInputParams.ProductName = "Aarogya";
//        keyInputParams.City = "HIsAR";
//        keyInputParams.GSTIN = "GST";
//        keyInputParams.MacAddress = "";
//        keyInputParams.ServerID = "";
//
//        keyOutputParams = dmkv.ValidateKey(keyInputParams, "ECCJMCUBD-CJDH3UJN8-SE", keyOutputParams);
//
//        return keyOutputParams;
//    }
//
//    private KEY_OUTPUT_PARAMS abc() {
//        cls_NativeMethods dmkv = new cls_NativeMethods();
//        KEY_INPUT_PARAMS keyInputParams = new KEY_INPUT_PARAMS();
//        KEY_OUTPUT_PARAMS keyOutputParams = new KEY_OUTPUT_PARAMS();
//
//        keyInputParams.PartyName = "ABC MEDICOSE";
//        keyInputParams.ClientCode = "SUKH0002";
//        keyInputParams.ProductName = "AAROGYA";
//        keyInputParams.City = "HISAR";
//        keyInputParams.GSTIN = "06AASCS5741H1ZY";
//        keyInputParams.MacAddress = "";
//        keyInputParams.ServerID = "";
//
//        keyOutputParams = dmkv.ValidateKey(keyInputParams, "MBBA8UL7T-ETT3JBGB1-KS", keyOutputParams);
//
//        return keyOutputParams;
//    }
//
//    private KEY_OUTPUT_PARAMS JEEBAN() {
//        cls_NativeMethods dmkv = new cls_NativeMethods();
//        KEY_INPUT_PARAMS keyInputParams = new KEY_INPUT_PARAMS();
//        KEY_OUTPUT_PARAMS keyOutputParams = new KEY_OUTPUT_PARAMS();
//
//        keyOutputParams.ModList = new short[24];
//        for (short i = 0; i < 24; i++) {
//            keyOutputParams.ModList[i] = -1;
//        }
//
//        keyInputParams.PartyName = "JEEBAN SURAKSHA HOSPITAL";
//        keyInputParams.ClientCode = "424455";
//        keyInputParams.ProductName = "AAROGYA";
//        keyInputParams.City = "BANKURA";
//        keyInputParams.GSTIN = "";
//        keyInputParams.MacAddress = "M002";
//        keyInputParams.ServerID = "";
//
//        keyOutputParams = dmkv.ValidateKey(keyInputParams, "DWLLHCAFV-A9VY1DJAB-XW", keyOutputParams);
//
//        return keyOutputParams;
//    }
//
//    private KEY_OUTPUT_PARAMS KK() {
//        cls_NativeMethods dmkv = new cls_NativeMethods();
//        KEY_INPUT_PARAMS keyInputParams = new KEY_INPUT_PARAMS();
//        KEY_OUTPUT_PARAMS keyOutputParams = new KEY_OUTPUT_PARAMS();
//
//        keyInputParams.PartyName = "K.K HOSPITAL KIDNEY CENTRE";
//        keyInputParams.ClientCode = "302326";
//        keyInputParams.ProductName = "AAROGYA";
//        keyInputParams.City = "BAREILLY";
//        keyInputParams.GSTIN = "";
//        keyInputParams.MacAddress = "";
//        keyInputParams.ServerID = "S001";
//
//        keyOutputParams = dmkv.ValidateKey(keyInputParams, "BACFMC86K-UCCCOBV3C-VO", keyOutputParams);
//
//        return keyOutputParams;
//    }
//
//    private KEY_OUTPUT_PARAMS NAVJEEVAN() {
//        cls_NativeMethods dmkv = new cls_NativeMethods();
//        KEY_INPUT_PARAMS keyInputParams = new KEY_INPUT_PARAMS();
//        KEY_OUTPUT_PARAMS keyOutputParams = new KEY_OUTPUT_PARAMS();
//
//        keyInputParams.PartyName = "NAVJEEVAN HOSPITAL";
//        keyInputParams.ClientCode = "N001";
//        keyInputParams.ProductName = "AAROGYA";
//        keyInputParams.City = "GWALIOR";
//        keyInputParams.GSTIN = "1234";
//        keyInputParams.MacAddress = "M001";
//        keyInputParams.ServerID = "";
//
//        keyOutputParams = dmkv.ValidateKey(keyInputParams, "BWFLI6CFC-C9U18UKCB-IJ", keyOutputParams);
//
//        return keyOutputParams;
//    }
    public DmResult<String> validateKey() {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmResult<String> dmrTemp = new DmResult<String>();
        KeyParam keyInputParams;
        KeyParam keyOutputParams;
        KeyCommunicator kc;
        String sqlQuery = "", errorMessag = "", moduleList = "'Master', 'NABH', 'MIS', 'Dashboard', 'Utility', 'Web Jobs'";

        int moduleCode = 0;

        try {
            kc = new KeyCommunicator();
            keyInputParams = new KeyParam();
            keyOutputParams = new KeyParam();

            keyInputParams.ModList = new short[24];
            for (short i = 0; i < 24; i++) {
                keyInputParams.ModList[i] = -1;
            }

            keyInputParams.PartyName = kc.createSpace(DmServer.getSsnSiteDisplayName(this), 50);
            keyInputParams.ClientCode = kc.createSpace(DmServer.getSsnSiteClientCode(this), 10); //"SMHOS";
            keyInputParams.ProductName = kc.createSpace("WEBAAROGYA", 40);
            keyInputParams.City = kc.createSpace(DmShared.xNull(DmServer.getSsnSiteCityName(this)).trim(), 40);
            keyInputParams.SiteCode = kc.createSpace(DmShared.iNull(DmServer.getSsnSiteCode(this)).toString(), 5);
            keyInputParams.GSTIN = kc.createSpace(DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim(), 15);
            // DmServer.getSsnSiteGSTIN(this);
            keyInputParams.MacAddress = kc.createSpace("", 40);
            keyInputParams.ServerID = kc.createSpace("", 40);
            keyInputParams.LicenceKey = kc.createSpace(DmServer.getSsnSiteSerialKey(this), 40);

            keyOutputParams = kc.getMyScoket(keyInputParams);
            //keyOutputParams = dmkv.ValidateKey(keyInputParams, DmServer.getSsnSiteSerialKey(this), keyOutputParams);

            System.err.println("RetVal: " + keyOutputParams.RetVal);
            if (keyOutputParams.RetVal != 1) {
                throw new DmException(111, "Invalid Key.");
            }

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            System.out.println("Module2: " + keyOutputParams.Module2);
            switch (keyOutputParams.Module2) {
                case 0:
                    DmServer.setSsnNoOfUser(this, "0");
                    break;
                case 1:
                    DmServer.setSsnNoOfUser(this, "5");
                    break;
                case 2:
                    DmServer.setSsnNoOfUser(this, "10");
                    break;
                case 3:
                    DmServer.setSsnNoOfUser(this, "15");
                    break;
                case 4:
                    DmServer.setSsnNoOfUser(this, "20");
                    break;
                case 5:
                    DmServer.setSsnNoOfUser(this, "25");
                    break;
                case 6:
                    DmServer.setSsnNoOfUser(this, "30");
                    break;
                case 7:
                    DmServer.setSsnNoOfUser(this, "40");
                    break;
                case 8:
                    DmServer.setSsnNoOfUser(this, "50");
                    break;
                case 9:
                    DmServer.setSsnNoOfUser(this, "60");
                    break;
                case 10:
                    DmServer.setSsnNoOfUser(this, "75");
                    break;
                case 20:
                    DmServer.setSsnNoOfUser(this, "100");
                    break;
                case 50:
                    DmServer.setSsnNoOfUser(this, "150");
                    break;
                case 100:
                    DmServer.setSsnNoOfUser(this, "200");
                    break;
                case 200:
                    DmServer.setSsnNoOfUser(this, "250");
                    break;
                case 300:
                    DmServer.setSsnNoOfUser(this, "300");
                    break;
                case 500:
                    DmServer.setSsnNoOfUser(this, "350");
                    break;
                case 1000:
                    DmServer.setSsnNoOfUser(this, "400");
                    break;
                case 10000:
                    DmServer.setSsnNoOfUser(this, "500");
                    break;
                default:
                    DmServer.setSsnNoOfUser(this, "-1");
                    break;
            }

//            sqlQuery = "select count(user_Name) as cnt from userMast where isActive = 1 ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > DmShared.iNull(DmServer.getSsnNoOfUser(this)).intValue()) {
//                    throw new DmException(111, "Invalid Key for No. of Users.");
//                }
//            }
//            rsTemp.close();
//            psTemp.close();
            System.out.println("Module5: " + keyOutputParams.Module5);
            switch (keyOutputParams.Module5) {
                case 0:
                    STServer.setSsnNoOfMachine(this, "0");
                    break;
                case 1:
                    STServer.setSsnNoOfMachine(this, "1");
                    break;
                case 2:
                    STServer.setSsnNoOfMachine(this, "2");
                    break;
                case 3:
                    STServer.setSsnNoOfMachine(this, "3");
                    break;
                case 4:
                    STServer.setSsnNoOfMachine(this, "4");
                    break;
                case 5:
                    STServer.setSsnNoOfMachine(this, "5");
                    break;
                case 6:
                    STServer.setSsnNoOfMachine(this, "6");
                    break;
                case 7:
                    STServer.setSsnNoOfMachine(this, "7");
                    break;
                case 8:
                    STServer.setSsnNoOfMachine(this, "8");
                    break;
                case 9:
                    STServer.setSsnNoOfMachine(this, "9");
                    break;
                case 10:
                    STServer.setSsnNoOfMachine(this, "10");
                    break;
                case 20:
                    STServer.setSsnNoOfMachine(this, "11");
                    break;
                case 50:
                    STServer.setSsnNoOfMachine(this, "12");
                    break;
                case 100:
                    STServer.setSsnNoOfMachine(this, "15");
                    break;
                case 200:
                    STServer.setSsnNoOfMachine(this, "18");
                    break;
                case 300:
                    STServer.setSsnNoOfMachine(this, "20");
                    break;
                case 500:
                    STServer.setSsnNoOfMachine(this, "22");
                    break;
                case 1000:
                    STServer.setSsnNoOfMachine(this, "25");
                    break;
                case 10000:
                    STServer.setSsnNoOfMachine(this, "30");
                    break;
                default:
                    STServer.setSsnNoOfMachine(this, "-1");
                    break;
            }

//            sqlQuery = "select count(machineCode) as cnt from machineInterfaceConfig ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > DmShared.iNull(STServer.getSsnNoOfMachine(this)).intValue()) {
//                    throw new DmException(111, "Invalid Key for No. of Machine Interface.");
//                }
//            }
//            rsTemp.close();
//            psTemp.close();
            System.out.println("Module4 " + keyOutputParams.Module4);
            switch (keyOutputParams.Module4) {
                case 0:
                    STServer.setSsnNoOfDiagnostic(this, "0");
                    break;
                case 1:
                    STServer.setSsnNoOfDiagnostic(this, "1");
                    break;
                case 2:
                    STServer.setSsnNoOfDiagnostic(this, "2");
                    break;
                case 3:
                    STServer.setSsnNoOfDiagnostic(this, "3");
                    break;
                case 4:
                    STServer.setSsnNoOfDiagnostic(this, "4");
                    break;
                case 5:
                    STServer.setSsnNoOfDiagnostic(this, "5");
                    break;
                case 6:
                    STServer.setSsnNoOfDiagnostic(this, "6");
                    break;
                case 7:
                    STServer.setSsnNoOfDiagnostic(this, "7");
                    break;
                case 8:
                    STServer.setSsnNoOfDiagnostic(this, "8");
                    break;
                case 9:
                    STServer.setSsnNoOfDiagnostic(this, "9");
                    break;
                case 10:
                    STServer.setSsnNoOfDiagnostic(this, "10");
                    break;
                case 20:
                    STServer.setSsnNoOfDiagnostic(this, "12");
                    break;
                case 50:
                    STServer.setSsnNoOfDiagnostic(this, "15");
                    break;
                case 100:
                    STServer.setSsnNoOfDiagnostic(this, "18");
                    break;
                case 200:
                    STServer.setSsnNoOfDiagnostic(this, "20");
                    break;
                case 300:
                    STServer.setSsnNoOfDiagnostic(this, "25");
                    break;
                case 500:
                    STServer.setSsnNoOfDiagnostic(this, "30");
                    break;
                case 1000:
                    STServer.setSsnNoOfDiagnostic(this, "35");
                    break;
                case 10000:
                    STServer.setSsnNoOfDiagnostic(this, "40");
                    break;
                default:
                    STServer.setSsnNoOfDiagnostic(this, "-1");
                    break;
            }

//            STServer.setSsnNoOfDiagnostic(this, "5");
//            STServer.setSsnNoOfMachine(this, "1");
//            DmServer.setSsnNoOfUser(this, "5");
//            cnTemp.setCatalog(DmServer.getSsnDBName(this));
//            sqlQuery = "select count(code) as cnt from patientServiceCatMast where filterNature = 1 ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > DmShared.iNull(STServer.getSsnNoOfDiagnostic(this)).intValue()) {
//                    throw new DmException(111, "Invalid Key for No. of Diagnostics.");
//                }
//            }
//            rsTemp.close();
//            psTemp.close();
            if (keyOutputParams.ExpiryDay != 0 && keyOutputParams.ExpiryMonth != 0 && keyOutputParams.ExpiryYear != 0) {
                Calendar expiryDate, warningDate, currentDate, tempDate;

                currentDate = Calendar.getInstance();
                DmShared.dNull(currentDate.getTime());
//                String expiryDate = "";
//                expiryDate = DmShared.dNull(keyOutputParams.ExpiryDay + "-" + keyOutputParams.ExpiryMonth + "-" + keyOutputParams.ExpiryYear);
//                Date expiryDate = new Date((keyOutputParams.ExpiryYear - 1900), (keyOutputParams.ExpiryMonth - 1), keyOutputParams.ExpiryDay);
                expiryDate = Calendar.getInstance();
                expiryDate.set(keyOutputParams.ExpiryYear, keyOutputParams.ExpiryMonth - 1, keyOutputParams.ExpiryDay, 00, 00, 01);
                System.out.println(DmShared.dNull(expiryDate.getTime()));
//                DmServer.getServerDateTime()
//                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

                warningDate = (Calendar) expiryDate.clone();
                warningDate.add(Calendar.DAY_OF_MONTH, 10);
                DmShared.dNull(warningDate.getTime());

                tempDate = (Calendar) expiryDate.clone();
                tempDate.add(Calendar.DAY_OF_MONTH, -currentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                DmShared.dNull(tempDate.getTime());

                if (currentDate.getTimeInMillis() >= tempDate.getTimeInMillis() && currentDate.getTimeInMillis() < expiryDate.getTimeInMillis()) {
                    errorMessag = "License key will be expiring on " + DmShared.dNull(expiryDate.getTime()) + ". Please contact DATAMAN before it gets expired.";
                } else if (DmShared.dNull(currentDate.getTime()).equalsIgnoreCase(DmShared.dNull(expiryDate.getTime()))) {
                    errorMessag = "License key is expire today " + DmShared.dNull(expiryDate.getTime()) + ". Please contact DATAMAN for renewal.";
                } else if (currentDate.getTimeInMillis() > warningDate.getTimeInMillis()) {
                    throw new DmException(111, "License key is expire on " + DmShared.dNull(expiryDate.getTime()) + ". Please contact DATAMAN for renew License.");
                }

//        currentTime = dateFormat.format(cal.getTime());
//        arrSrvDatetime = currentTime.split(" ");
//                Calendar calWarningDate = Calendar.getInstance();
//                calWarningDate.add(Calendar.DAY_OF_MONTH, 40);
//
//                if (calWarningDate.getTime().getTime() >= expiryDate.getTime().getTime()) {
////                    throw new DmException(111, "License key will be expiring on " + DmShared.dNull(expiryDate.getTime()) + ". Please contact DATAMAN before it gets expired.");
//                    errorMessag = "License key will be expiring on " + DmShared.dNull(expiryDate.getTime()) + ". Please contact DATAMAN before it gets expired.";
//                }
            }
            if (!DmShared.xNull(errorMessag).equalsIgnoreCase("")) {
                dmrTemp.setErrorMessage(errorMessag);
            }
            for (int i = 0; i < 24; i++) {
                moduleCode = keyOutputParams.ModList[i];
//                System.err.println("Modules: " + moduleCode);
                switch (moduleCode) {
                    case 11:
                        moduleList += ",'OPD' ";
                        break;
                    case 12:
                        moduleList += ",'IPD' ";
                        break;
                    case 13:
                        moduleList += ",'OT' ";
                        break;
                    case 14:
                        moduleList += ",'TPA' ";
                        break;
                    case 15:
                        moduleList += ",'House Keeping' ";
                        break;
                    case 16:
                        moduleList += ",'MRD' ";
                        break;
                    case 17:
                        moduleList += ",'Nursing Station' ";
                        break;
                    case 18:
                        moduleList += ",'Diagnostic' ";
                        break;
                    case 19:
                        moduleList += ",'Pharmacy' ";
                        break;
                    case 20:
                        moduleList += ",'Inventory' ";
                        break;
                    case 21:
                        moduleList += ",'Accounts' ";
                        break;
                    case 22:
                        moduleList += ",'CRM' ";
                        break;
                    case 23:
                        moduleList += ",'Emergency' ";
                        break;
                    case 24:
                        moduleList += ",'Assets' ";
                        break;
                    case 25:
                        moduleList += ",'Dietary' ";
                        break;
                    case 26:
                        moduleList += ",'Blood Bank' ";
                        break;
                    case 27:
                        moduleList += ",'Ambulance' ";
                        break;
                    case 28:
//                        Tally Export
                        break;
                    case 29:
                        moduleList += ",'Day Care' ";
                        break;
                    case 30:
                        moduleList += ",'Bio-Waste' ";
                        break;
                    case 31:
                        moduleList += ",'Payroll' ";
                        break;
                    case 32:
//                      PACS
                        break;
                    case 33:
                        moduleList += ",'CSSD' ";
                        break;

                    case 34:
                        moduleList += ",'Gate' ";
                        break;

                }
            }

//            System.err.println("License Key: " + keyOutputParams.LicenceKey);
//            System.err.println("Expiry: " + keyOutputParams.ExpiryDay + "/" + keyOutputParams.ExpiryMonth + "/" + keyOutputParams.ExpiryYear);
//            System.err.println("Module-1: " + keyOutputParams.Module1);
//            System.err.println("Module-2: " + keyOutputParams.Module2);
//            System.err.println("Module-3: " + keyOutputParams.Module3);
//            System.err.println("Module-4: " + keyOutputParams.Module4);
//            System.err.println("RetVal: " + keyOutputParams.RetVal);
//            System.err.println("PartyName: " + keyOutputParams.PartyName);
//            System.err.println("ClientCode: " + keyOutputParams.ClientCode);
//            System.err.println("ProductName: " + keyOutputParams.ProductName);
//            System.err.println("City: " + keyOutputParams.City);
//            System.err.println("GSTIN: " + keyOutputParams.GSTIN);
//            System.err.println("MacAddress: " + keyOutputParams.MacAddress);
//            System.err.println("ServerID: " + keyOutputParams.ServerID);
            dmrTemp.setData(moduleList);
        } catch (Exception e) {
            e.printStackTrace();
            dmrTemp.setData(moduleList);
            if (e instanceof DmException) {
                dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                dmrTemp.setErrorMessage(e.getMessage());
            }
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

}
