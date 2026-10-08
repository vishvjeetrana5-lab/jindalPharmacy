package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.utility.overheadConfiguration.shared.GrdOverhead_Mdl;
import dataman.utility.overheadConfiguration.shared.OverheadConfiguration_Mdl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class OverheadConfigurationIni implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    public OverheadConfigurationIni() {
    }

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File sessionDir = null;
        BufferedReader br = null;
        String fileName;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            this.rssTemp = rssTemp;
            this.refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            Gson gson = new Gson();

            sessionDir = new File(model.getDataPath());
            if (!sessionDir.exists()) {
                sessionDir.mkdir();
            }

            if (sessionDir.list() != null) {
                for (int i = 0; i < sessionDir.list().length; i++) {
                    fileName = sessionDir.list()[i];
                    String tempReadPath = model.getDataPath() + File.separatorChar + fileName;
                    br = new BufferedReader(new FileReader(tempReadPath));
                    status = save(gson.fromJson(br, OverheadConfiguration_Mdl.class), model.getYearStart());
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                    br.close();
                    br = null;
                }
            }
        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                throw new Exception(e);
            } catch (Exception ex) {
            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (refreshDataStructure != null) {
                    refreshDataStructure.close();
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (sessionDir != null) {
                    sessionDir = null;
                }
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    private int save(OverheadConfiguration_Mdl mrTemp, String applicableDate) {
        List<GrdOverhead_Mdl> grd = mrTemp.getGrdManageHeader();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        int status = 0, rtnStatus = 0;
        try {
            mrTemp.setApplicableFrom(applicableDate);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(rssTemp, "overHeadConfigName", cnTemp));

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }
            mrTemp.setVoucherTypeCode(DmServer.getVType(mrTemp.getVoucherTypeShortName(), cnTemp));

            if (mrTemp.getVoucherTypeCode().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("VoucherTypeCode for :" + mrTemp.getVoucherTypeShortName() + " not found.");
            }

            sqlQuery = "select count(*) as cnt from voucher_type where V_Type = :v_Type";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_Type", DmShared.xNull(mrTemp.getVoucherTypeCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 1) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException(" VoucherTypeCode for :" + mrTemp.getVoucherTypeShortName() + " already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT COUNT(name) AS cnt FROM overHeadConfigName WHERE name = :name AND CODE  <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getOverheadName()).toLowerCase());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, mrTemp.getOverheadName() + " overhead is already exists.");
            }
            rsTemp.close();
            psTemp.close();

            for (int i = 0; i < mrTemp.getGrdManageHeader().size(); i++) {
                GrdOverhead_Mdl gmh = mrTemp.getGrdManageHeader().get(i);
                if (!DmShared.xNull(gmh.getManualCode()).trim().equalsIgnoreCase("")) {
                    gmh.setOverheadCode(DmServer.getCodeViaManualCode(gmh.getManualCode(), "manualCode", "overheadMast", "code", cnTemp));
                    if (DmShared.xNull(gmh.getOverheadCode()).trim().equalsIgnoreCase("")) {
                        status = DmImportStatus.ERROR.getId();
                        throw new UnsupportedOperationException("OverheadCode for :" + gmh.getOverhead() + " not found for" + mrTemp.getVoucherTypeShortName());
                    }
                }

                if (!DmShared.xNull(gmh.getPostAC()).trim().equalsIgnoreCase("")) {
                    gmh.setPostACCode(DmServer.getCodeViaManualCode(gmh.getPostAC(), "name", "subGroup", "subCode", cnTemp));
                    if (DmShared.xNull(gmh.getPostACCode()).trim().equalsIgnoreCase("")) {
                        status = DmImportStatus.ERROR.getId();
                        throw new UnsupportedOperationException(mrTemp.getVoucherTypeShortName() + ": Where Subgroup -> " + gmh.getPostAC() + " not found.");
                    }
                }
                if (!DmShared.xNull(gmh.getOverheadCode()).equalsIgnoreCase("")) {

                    sqlQuery = "INSERT INTO overheadconfig "
                            + " (code,applicableFrom,applicableTo, overHeadCode, sno, atRate, subCode, contraOhMCode,"
                            + "  nature, formula, drCr, atRateSaveIn,"
                            + " amountSaveIn, tableName, isatRateEditable, isamountEditable, isacEditable,"
                            + " isshowFormulaValue, roundOff, ohSetUp,isPrintable,calLValOn,"
                            + " UniqueId, isatRateMandatory, isamountMandatory, isoverHeadAliasEditable)"
                            + " VALUES (:code,:applicableFrom ,:applicableTo,:overHeadCode, :sno, :atRate, :subCode, :contraOhMCode, :nature,"
                            + " :formula, :drCr, :atRateSaveIn, :amountSaveIn,"
                            + " :tableName, :isatRateEditable, :isamountEditable,"
                            + " :isacEditable, :isshowFormulaValue, :roundOff,"
                            + " :ohSetUp,:isPrintable, :calLValOn, :UniqueId, :isatRateMandatory,"
                            + " :isamountMandatory, :isoverHeadAliasEditable) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setDate("applicableTo", DmServer.convertDate("31-Dec-9999"));
                    psTemp.setString("overHeadCode", gmh.getOverheadCode());
                    psTemp.setString("sNo", String.valueOf(i + 1));
                    psTemp.setString("atRate", DmServer.vNull(gmh.getAtRate(), "0"));
                    psTemp.setString("subCode", DmShared.foriegnKey(gmh.getPostACCode()));
                    psTemp.setString("contraOhMCode", DmShared.foriegnKey(gmh.getContraExpText()));
                    psTemp.setString("nature", gmh.getNatureCode());
                    psTemp.setString("formula", gmh.getFormula());
                    psTemp.setString("drCr", gmh.getDrCrCode());
                    psTemp.setString("atRateSaveIn", gmh.getAtRateSaveIn());
                    psTemp.setString("amountSaveIn", gmh.getAmtSaveIn());
                    psTemp.setString("tableName", mrTemp.getTableAffectedText());
                    psTemp.setString("isatRateEditable", DmServer.vNull(gmh.getAtRateEditableCode(), "0"));
                    psTemp.setString("isamountEditable", DmServer.vNull(gmh.getAmtEditableCode(), "0"));
                    psTemp.setString("isacEditable", DmServer.vNull(gmh.getAcEditableCode(), "0"));
                    psTemp.setString("isshowFormulaValue", DmServer.vNull(gmh.getShowFormulaValueCode(), "0"));
                    psTemp.setString("isPrintable", DmServer.vNull(gmh.getPrintableCode(), "0"));
                    psTemp.setString("calLValOn", DmServer.vNull(gmh.getCalLValOnCode(), "0"));
                    psTemp.setString("roundoff", gmh.getRoundOffCode());
                    psTemp.setString("ohSetUp", gmh.getOhSetupCode());
                    psTemp.setString("UniqueId", gmh.getUniqueID());
                    psTemp.setString("isatRateMandatory", DmServer.vNull(gmh.getAtRateMandatoryCode(), "0"));
                    psTemp.setString("isamountMandatory", DmServer.vNull(gmh.getAmtMandatoryCode(), "0"));
                    psTemp.setString("isoverHeadAliasEditable", DmServer.vNull(gmh.getOverheadEditableCode(), "0"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            sqlQuery = "INSERT INTO overHeadConfigSite "
                    + " (code, site_Code)"
                    + " VALUES (:code, :site_Code) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("site_Code", null);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Insert Into overHeadConfigName(code, name, v_type, preparedBy, U_EntDt, transferDt) "
                    + " Values (:code, :name, :v_type, :uName, :uEntDt, :transferDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("name", mrTemp.getOverheadName());
            psTemp.setString("transferDt", null);
            psTemp.setString("v_type", mrTemp.getVoucherTypeCode());
            psTemp.setString("uName", DmServer.getSsnUserName(rssTemp));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                refreshDataStructure.writeLine(e.getMessage());
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
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
        return rtnStatus;
    }
}
