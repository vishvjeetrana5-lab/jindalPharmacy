package aarogya.login.server;

import aarogya.login.shared.ForgetPassword_Mdl;
import aarogya.login.shared.Initialze_Mdl;
import dataman.dmBase.self.shared.*;
import aarogya.login.shared.Login_Model;
import aarogya.login.shared.Login_Srv;
import aarogya.login.shared.Login_Validation;
import aarogya.selfToolkit.stStatic.server.STManageUser;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmSession.server.DmRSSPublic;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.PCSData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Login_SrvImpl extends DmRSSPublic implements Login_Srv {

    @Override
    public DmResult<PCSData> validateUser(Login_Model.Body bTemp) {
        DmResult<PCSData> dmrTemp = new DmResult();
        PCSData pcsMain = new PCSData();
        PCSData pcsTemp = new PCSData();
        PCSData pcsSite = new PCSData();
        dmrTemp.setData(pcsMain);

        try {
            Login_Validation.chkLogin(bTemp);
            if (DmServer.validateUser(this, DmEnum.DmUserValidationMode.USERNAME, bTemp.getUserName(), bTemp.getPassword())) {
                ((DmRequestWrapper) this.getLocalRequest()).getDatamanSession().setSessionStatus(DmEnum.DmSessionStatus.LOGGED_IN);

                pcsTemp = DmServer.getPLTs(this, null);
                pcsMain.setPLT(pcsTemp);
                pcsMain.setLoginDate(pcsTemp.getLoginDate());
                pcsMain.setUserDescription(DmServer.getSsnUserDescription(this));
                pcsMain.setUserName(DmServer.getSsnUserName(this));
                if (pcsMain.pltSelected != null) {
                    pcsMain.setCompany(DmServer.getCompanys(this, null));
                    if (pcsMain.cSelected != null) {
                        try {
                            pcsSite = DmServer.getSites(this, null);
                            pcsMain.setRegionalLanguage(pcsSite.getRegionalLanguage());
                            pcsMain.setSite(pcsSite);
                        } catch (Exception e) {
                            pcsMain.cSelected = null;
                            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
                        }
                    }
                }
                dmrTemp.setStatus(true);
            } else {
                dmrTemp.setErrorMessage("Access denied");
            }
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            System.out.println("user Logged In exception: " + DmShared.xNull(bTemp.getUserName()));
        }
        return dmrTemp;
    }

    @Override
    public DmResult<Boolean> validateDBPassword(String password) {
        DmResult<Boolean> dmrTemp = new DmResult();

        try {
            dmrTemp.setStatus(DmServer.compareDBPassword(password));
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> lockUser() {
        DmResult<String> dmrTemp = new DmResult();
        dmrTemp.setData("");

        try {
            ((DmRequestWrapper) this.getLocalRequest()).getDatamanSession().setSessionStatus(DmEnum.DmSessionStatus.LOCK);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    @Override
    public DmResult<Initialze_Mdl> initialize() {
        DmResult<Initialze_Mdl> dmrTemp = new DmResult();
        Initialze_Mdl im = new Initialze_Mdl();
        try {
            dmrTemp.setData(im);
            im.setMobileDigit(DmShared.mobileDigit);
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> forgetPassword(ForgetPassword_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        dmrTemp.setData("");

        try {
            if (DmShared.iNull(mrTemp.getPltCode()).intValue() == 0) {
                throw new DmException(100, "Product Licesed is mandatory.");
            }
            if (DmShared.iNull(mrTemp.getCompanyCode()).intValue() == 0) {
                throw new DmException(200, "Financial Year is mandatory.");
            }
            if (DmShared.xNull(mrTemp.getUserName()).trim().equalsIgnoreCase("")) {
                throw new DmException(300, "User Name is mandatory.");
            }
            if (DmShared.xNull(mrTemp.getMobile()).trim().equalsIgnoreCase("")) {
                throw new DmException(400, "Mobile is mandatory.");
            }
            if (DmShared.xNull(mrTemp.getEmail()).trim().equalsIgnoreCase("")) {
                throw new DmException(500, "Email is mandatory.");
            } else {
                DmShared.isValidEMail(DmShared.xNull(mrTemp.getEmail()), 500);
            }

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            /*=======================< Get database name & other detail >======================*/
            sqlQuery = "Select	plt.pltCode, plt.latestCompCode as companyCode, plt.comp_Name as companyName, "
                    + "		com.centralData_Path, com.centralFileData_Path, com.v_Prefix "
                    + "		from ProductLicensedTo as plt "
                    + "		left join company as com on plt.pltCode = com.pltCode and com.comp_Code = :compCode "
                    + "		where plt.pltCode = :pltCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setInt("pltCode", DmShared.iNull(mrTemp.getPltCode()).intValue());
            psTemp.setInt("compCode", DmShared.iNull(mrTemp.getCompanyCode()).intValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                ((DmRequestWrapper) this.getLocalRequest()).getDatamanSession().setSessionStatus(DmEnum.DmSessionStatus.LOGGED_IN);

                DmServer.setSsnDBName(this, DmShared.xNull(rsTemp.getString("centralData_Path")));
                DmServer.setSsnFileDBName(this, DmShared.xNull(rsTemp.getString("centralFileData_Path")));
                DmServer.setSsnVoucherPrefix(this, DmShared.xNull(rsTemp.getString("v_Prefix")));
                DmServer.setSsnData(this, "PLTCode", DmShared.xNull(rsTemp.getString("pltCode")));
                DmServer.setSsnData(this, "CompanyCode", DmShared.xNull(rsTemp.getString("companyCode")));
                DmServer.setSsnData(this, "CompanyName", DmShared.xNull(rsTemp.getString("companyName")));

                DmServer.setSsnData(this, "UserName", "SA");
                DmServer.setSsnData(this, "UserRole", "SA");
            } else {
                throw new DmException(0, "Invalid request.");
            }
            rsTemp.close();
            psTemp.close();

            /*=======================< Check wether user is valid or not >======================*/
            sqlQuery = "Select  um.mobile, um.email"
//                    + " , us.defaultUnitCode"
                    + " , um.isActive "
                    + "         from userMast as um "
                    + "         left join userSite as us on lower(um.user_name) = lower(us.user_name) and us.compCode = :compCode "
                    + "         where lower(um.user_Name) = :userName; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", DmShared.xNull(mrTemp.getUserName()).toLowerCase());
            psTemp.setInt("compCode", DmShared.iNull(mrTemp.getCompanyCode()).intValue());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(mrTemp.getMobile()).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("mobile")))) {
                    throw new DmException(400, "Mobile not matched.");
                }
                if (!DmShared.xNull(mrTemp.getEmail()).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("email")))) {
                    throw new DmException(500, "Email not matched.");
                }
                if (DmShared.iNull(rsTemp.getString("isActive")).intValue() != 1) {
                    throw new DmException(500, "In-active user.");
                }
//                DmServer.setSsnData(this, "SiteCode", DmShared.xNull(rsTemp.getString("defaultUnitCode")));
            } else {
                throw new DmException(300, "Invalid User Name.");
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            /*=======================< Send mail >======================*/
            DmServer.setManageUser(new STManageUser());
            DmServer.getManageUser().sendMail(DmShared.xNull(mrTemp.getUserName()),  this);

            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            dmrTemp.setErrorMessage(ex.getMessage());
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
    public DmResult<ForgetPassword_Mdl> getDefaultValues() {
        DmResult<ForgetPassword_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        ForgetPassword_Mdl mrTemp = new ForgetPassword_Mdl();
        String sqlQuery;
        dmrTemp.setData(mrTemp);

        try {
            ((DmSession) this.getThreadLocalRequest().getSession()).setSessionStatus(DmEnum.DmSessionStatus.PUBLIC);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "Select  plt.pltCode, plt.comp_Name, plt.latestCompCode, cmp.v_Prefix, cmp.cYear "
                    + "         from productlicensedto as plt  "
                    + "         left join company as cmp on cmp.comp_Code = plt.latestCompCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.iNull(mrTemp.getPltCode()).intValue() != 0) {
                    mrTemp = new ForgetPassword_Mdl();
                    break;
                } else {
                    mrTemp.setPltCode(DmShared.xNull(rsTemp.getString("pltCode")));
                    mrTemp.setPltName(DmShared.xNull(rsTemp.getString("comp_Name")));
                    mrTemp.setCompanyCode(DmShared.xNull(rsTemp.getString("latestCompCode")));
                    mrTemp.setFyName(DmShared.xNull(rsTemp.getString("cYear")));
                }
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setStatus(true);
            dmrTemp.setData(mrTemp);
        } catch (Exception ex) {
            ex.printStackTrace();
            dmrTemp.setErrorMessage(ex.getMessage());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPltCode(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> listNode = new ArrayList<>();
        KeyFieldExtended node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("Select pltCode as code, comp_Name as name, latestCompCode from productLicensedTo ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("latestCompCode")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHFinancialYear(String pltCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> listNode = new ArrayList<>();
        KeyFieldExtended node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            if (DmShared.iNull(pltCode).intValue() == 0) {
                throw new DmException(0, "Please select Product Licensed To.");
            }

            sqlWithParam.setSQL("Select comp_code as code, cyear as name, v_Prefix from company where pltCode = :pltCode ");
            sqlWithParam.setInteger("pltCode", DmShared.iNull(pltCode).intValue());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
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
    public DmResult<String> setLocalIPAddress(List<String> ipAddresses) {
        DmResult<String> dmrTemp = new DmResult();
        String localIPAddress = "";
        dmrTemp.setData(localIPAddress);

        try {
            for (String ipAddresse : ipAddresses) {
                if (!DmShared.xNull(ipAddresse).trim().equalsIgnoreCase("")) {
                    if (!DmShared.xNull(localIPAddress).trim().equalsIgnoreCase("")) {
                        localIPAddress += ", ";
                    }
                    localIPAddress += DmShared.xNull(ipAddresse).trim();
                }
            }
            DmServer.setSsnData(this, "LocalIPAddress", DmShared.xNull(localIPAddress));

            dmrTemp.setStatus(true);
            dmrTemp.setData(localIPAddress);
        } catch (Exception e) {
            e.printStackTrace();
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }
}
