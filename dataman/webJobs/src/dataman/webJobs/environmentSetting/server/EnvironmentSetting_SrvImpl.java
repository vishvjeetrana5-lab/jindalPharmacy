package dataman.webJobs.environmentSetting.server;

import dataman.webJobs.environmentSetting.shared.EnvironmentSetting_Mdl;
import dataman.webJobs.environmentSetting.shared.EnvironmentSetting_Vld;
import dataman.webJobs.environmentSetting.shared.EnvironmentSetting_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentSetting_SrvImpl extends DmRSSPrivate implements EnvironmentSetting_Srv {

//    @Override
//    public DmResult<String> saveRecord(EnvironmentSetting_Mdl mrTemp) {
//        DmResult<String> dmrTemp = new DmResult();
//        Connection cnTemp = null;
//        DmPreparedStatement psTemp = null;
//        DmSavePoint spBegin = null;
//        boolean blnAdd = false, flag = false;
//        String strSQL;
//        ResultSet rsTemp = null;
//        int openBrace = 0, closeBrace = 0;
//        dmrTemp.setData("");
//
//        try {
//            EnvironmentSetting_Vld.chkSave(mrTemp);
//
//            if (!DmShared.xNull(mrTemp.getFormat()).equalsIgnoreCase("")) {
//                if (DmShared.xNull(mrTemp.getFormat()).contains("{") || DmShared.xNull(mrTemp.getFormat()).contains("}")) {
//                    for (int i = 0; i < mrTemp.getFormat().length(); i++) {
//                        char msgchar = mrTemp.getFormat().charAt(i);
//                        if (msgchar == '{') {
//                            openBrace++;
//                        }
//                        if (msgchar == '}') {
//                            closeBrace++;
//                        }
//                    }
//                    if (openBrace != closeBrace) {
//                        throw new DmException(0, "Message is in incorrect format.Did you forget to open or close any curly braces? ");
//                    }
//                }
//
//                boolean format = mrTemp.getFormat().trim().contains("?");
//                if (format == true) {
//
//                    String[] arrURL = mrTemp.getFormat().trim().split("\\?");
//                    if (!arrURL[0].toLowerCase().contains("{url}")) {
//                        throw new DmException(0, "Please mention URL");
//                    }
//                    if (!arrURL[1].equalsIgnoreCase("")) {
//                        flag = false;
//                        String arrPara = arrURL[1].trim().toLowerCase();
//                        if ((arrPara.contains("{password}") && arrPara.contains("{user}")) && arrPara.contains("{mobile}") && arrPara.contains("{msg}")) {
//                            flag = true;
//                            String[] arr = arrURL[1].trim().split("}");
//                            for (int i = 1; i < arr.length; i++) {
//                                String arr1 = arr[i];
//                                if (!arr1.contains("&")) {
//                                    int a = arr1.indexOf("&");
//                                    if (a != 0) {
//                                        throw new DmException(0, "Msg fomat is incorrect. Did you forget to attach '&' with any field?");
//                                    }
//                                }
//                            }
//                        }
//                        if (!flag) {
//                            throw new DmException(0, "Please mention format according to given example.");
//                        }
//
//                    }
//                } else {
//                    throw new DmException(0, "Please mention '?' after url filed.");
//                }
//            }
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            spBegin = new DmSavePoint(cnTemp);
//
//            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
//            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
//                blnAdd = true;
//                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "enviro", cnTemp));
//            }
//            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
//                throw new UnsupportedOperationException("Invalid key.");
//            }
//            strSQL = "SELECT  site_Code FROM enviro where site_Code = :site_Code ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//            psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//            rsTemp = psTemp.executeQuery();
//            if (!rsTemp.next()) {
//                strSQL = "INSERT INTO enviro(site_Code) VALUES(:site_Code);";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//                psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//                psTemp.executeUpdate();
//                psTemp.close();
//            }
//            psTemp.close();
//
//            strSQL = "update enviro SET "
//                    + " wj_SmtpServer = :wj_SmtpServer,"
//                    + " wj_SmtpPort = :wj_SmtpPort,"
//                    + " wj_SmtpSsl = :wj_SmtpSsl,"
//                    + " wj_Url = :wj_Url,"
//                    + " wj_AcName = :wj_AcName,"
//                    + " wj_Password = :wj_Password,"
//                    + " wj_Format = :wj_Format,"
//                    + " wj_MsgLength = :wj_MsgLength,"
//                    + " u_Name = :userName,"
//                    + " u_EntDt = getdate()"
//                    + " where site_Code= :site_Code ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//
//            psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
//            psTemp.setString("wj_SmtpServer", mrTemp.getSmtpServer());
//            psTemp.setString("wj_SmtpPort", mrTemp.getSmtpPort());
//            psTemp.setString("wj_SmtpSsl", DmShared.vNull(mrTemp.getSmtpSslTag()).doubleValue() == 0 ? "0" : "1");
//            psTemp.setString("wj_Url", mrTemp.getUrl());
//            psTemp.setString("wj_AcName", mrTemp.getAccName());
//            psTemp.setString("wj_Password", mrTemp.getPassword());
//            psTemp.setString("wj_Format", mrTemp.getFormat());
//            psTemp.setString("wj_MsgLength", mrTemp.getMsgLength());
//            psTemp.setString("userName", DmServer.getSsnUserName(this));
//
//            psTemp.executeUpdate();
//            psTemp.close();
//            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
//            spBegin.commit();
//            cnTemp.close();
//            dmrTemp.setStatus(true);
//        } catch (Exception e) {
//            try {
//                if (spBegin != null) {
//                    spBegin.rollBack();
//                }
//            } catch (Exception ex) {
//            }
//            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//
//        return dmrTemp;
//    }
    @Override
    public DmResult<String> saveRecord(EnvironmentSetting_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, flag = false;
        String strSQL;
        ResultSet rsTemp = null;
        int openBrace = 0, closeBrace = 0;
        dmrTemp.setData("");

        try {
            EnvironmentSetting_Vld.chkSave(mrTemp);

            boolean format = false;
            if (!DmShared.xNull(mrTemp.getFormat()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getFormat()).contains("{") || DmShared.xNull(mrTemp.getFormat()).contains("}")) {
                    for (int i = 0; i < mrTemp.getFormat().length(); i++) {
                        char msgchar = mrTemp.getFormat().charAt(i);
                        if (msgchar == '{') {
                            openBrace++;
                        }
                        if (msgchar == '}') {
                            closeBrace++;
                        }
                    }
                    if (openBrace != closeBrace) {
                        throw new DmException(0, "Message is in incorrect format.Did you forget to open or close any curly braces? ");
                    }
                }
                if (mrTemp.getUrl().trim().contains("?")) {
                    if (mrTemp.getFormat().trim().contains("?")) {
                        throw new DmException(0, "Please mention '?' either in URL or format after url.");
                    } else {
                        format = true;
                    }
                } else if (mrTemp.getUrl().trim().contains("?")) {
                    format = true;
                }
                if (format) {
                    if ((mrTemp.getFormat().trim().contains("{url}") && mrTemp.getFormat().trim().contains("{user}")) && (mrTemp.getFormat().trim().contains("{password}") && mrTemp.getFormat().trim().contains("{user}")) && mrTemp.getFormat().trim().contains("{mobile}") && mrTemp.getFormat().trim().contains("{msg}")) {
                        flag = true;
                    }
                    if (!flag) {
                        throw new DmException(0, "Please mention format according to given example.");
                    }
                } else {
                    throw new DmException(0, "Please mention '?' after url filed.");
                }
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "enviro", cnTemp));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            strSQL = "SELECT  site_Code FROM enviro where site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                strSQL = "INSERT INTO enviro(site_Code) VALUES(:site_Code);";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            psTemp.close();

            strSQL = "update enviro SET "
                    + " wj_SmtpServer = :wj_SmtpServer,"
                    + " wj_SmtpPort = :wj_SmtpPort,"
                    + " wj_SmtpSsl = :wj_SmtpSsl,"
                    + " wj_Url = :wj_Url,"
                    + " wj_AcName = :wj_AcName,"
                    + " wj_Password = :wj_Password,"
                    + " wj_Format = :wj_Format,"
                    + " wj_MsgLength = :wj_MsgLength,"
                    + " wj_PayRefOtpMobile = :wj_PayRefOtpMobile,"
                    + " u_Name = :userName,"
                    + " u_EntDt = getdate()"
                    + " where site_Code= :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);

            psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("wj_SmtpServer", mrTemp.getSmtpServer());
            psTemp.setString("wj_SmtpPort", mrTemp.getSmtpPort());
            psTemp.setString("wj_SmtpSsl", DmShared.vNull(mrTemp.getSmtpSslTag()).doubleValue() == 0 ? "0" : "1");
            psTemp.setString("wj_Url", mrTemp.getUrl());
            psTemp.setString("wj_AcName", mrTemp.getAccName());
            psTemp.setString("wj_Password", mrTemp.getPassword());
            psTemp.setString("wj_Format", mrTemp.getFormat());
            psTemp.setString("wj_MsgLength", mrTemp.getMsgLength());
            psTemp.setString("wj_PayRefOtpMobile", mrTemp.getPayRefOtpMobile());
            psTemp.setString("userName", DmServer.getSsnUserName(this));

            psTemp.executeUpdate();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<EnvironmentSetting_Mdl> getMoveRec(String strKey) {
        DmResult<EnvironmentSetting_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        EnvironmentSetting_Mdl mrTemp = new EnvironmentSetting_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT sm.code AS site_code, sm.name AS site_name, "
                    + " env.wj_SmtpServer, env.wj_SmtpPort,env.wj_SmtpSsl, env.wj_Url, env.wj_AcName, env.wj_Password, "
                    + " env.wj_Format, env.wj_MsgLength, env.wj_PayRefOtpMobile, "
                    + " env.u_Name, FORMAT(env.u_EntDt,'dd-MMM-yyyy HH:mm') AS u_EntDt "
                    + " FROM  enviro AS env "
                    + "  LEFT JOIN sitemast AS sm ON (env.site_Code = sm.code) "
                    + " WHERE env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("site_Code", strKey);
            rsTemp = psTemp.executeQuery();
            mrTemp.setSharedKeyFieldCode(DmShared.xNull(strKey));
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("site_code")).equalsIgnoreCase("")) {
                    mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_code")));
                    mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("site_name")));
                } else {
                    mrTemp.setSharedKeyFieldCode(DmShared.xNull(strKey));
                }
                mrTemp.setSmtpServer(DmShared.xNull(rsTemp.getString("wj_SmtpServer")));
                mrTemp.setSmtpPort(DmShared.xNull(rsTemp.getString("wj_SmtpPort")));
                mrTemp.setSmtpSslTag(DmShared.vNull(rsTemp.getString("wj_SmtpSsl")).toString());
                mrTemp.setSmtpSslText(DmShared.vNull(rsTemp.getString("wj_SmtpSsl")).doubleValue() == 0 ? "No" : "Yes");
                mrTemp.setUrl(DmShared.xNull(rsTemp.getString("wj_Url")));
                mrTemp.setAccName(DmShared.xNull(rsTemp.getString("wj_AcName")));
                mrTemp.setPassword(DmShared.xNull(rsTemp.getString("wj_Password")));
                mrTemp.setFormat(DmShared.xNull(rsTemp.getString("wj_Format")));
                mrTemp.setPassword(DmShared.xNull(rsTemp.getString("wj_password")));
                mrTemp.setMsgLength(DmShared.xNull(rsTemp.getString("wj_MsgLength")));
                mrTemp.setPayRefOtpMobile(DmShared.xNull(rsTemp.getString("wj_PayRefOtpMobile")));
                mrTemp.setuName(DmShared.xNull(rsTemp.getString("u_Name")));

                if (!DmShared.xNull(rsTemp.getString("u_EntDt")).equalsIgnoreCase("")) {
                    String date = DmShared.xNull(rsTemp.getString("u_EntDt"));
                    String[] a = date.split(" ");
                    mrTemp.setDate(a[0]);
                    mrTemp.setTime(DmShared.xNull(a[1]));
                } else {
                    mrTemp.setDate("");
                    mrTemp.setTime("");
                }

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSmtpSsl(DmHelper helper, boolean blnToEndOfFile
    ) {
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
    public DmResult<EnvironmentSetting_Mdl> getSiteCode() {
        DmResultPageing<EnvironmentSetting_Mdl> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String siteCode, strSQL;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        EnvironmentSetting_Mdl mrTemp = new EnvironmentSetting_Mdl();
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            siteCode = DmServer.getSsnSiteCode(this);
            strSQL = "SELECT sm.name AS site_name from sitemast sm "
                    + " WHERE sm.Code=:site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("site_Code", siteCode);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(siteCode));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("site_name")));
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

}
