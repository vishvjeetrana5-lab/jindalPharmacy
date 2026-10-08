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
import dataman.utility.overheadLineConfiguration.shared.GrdOverheadLine_Mdl;
import dataman.utility.overheadLineConfiguration.shared.OverheadLineConfiguration_Mdl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class OverheadLineConfigurationIni implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    public OverheadLineConfigurationIni() {
    }

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File sessionDir = null;
        BufferedReader br = null;
        String fileName;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            Gson gson = new Gson();
            this.rssTemp = rssTemp;
            this.refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            sessionDir = new File(model.getDataPath());
            if (!sessionDir.exists()) {
                sessionDir.mkdir();
            }

            if (sessionDir.list() != null) {
                for (int i = 0; i < sessionDir.list().length; i++) {
                    fileName = sessionDir.list()[i];
                    String tempReadPath = model.getDataPath() + File.separatorChar + fileName;
                    br = new BufferedReader(new FileReader(tempReadPath));
                    status = save(gson.fromJson(br, OverheadLineConfiguration_Mdl.class), model.getYearStart());
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

    private int save(OverheadLineConfiguration_Mdl mrTemp, String applicableDate) {
        List<GrdOverheadLine_Mdl> grd = mrTemp.getGrdManageLine();
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

            mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(rssTemp, "overHeadLineConfigName", cnTemp));

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

            sqlQuery = "SELECT COUNT(name) AS cnt FROM overHeadLineConfigName WHERE name = :name AND CODE  <> :code ";
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

            for (int i = 0; i < mrTemp.getGrdManageLine().size(); i++) {
                GrdOverheadLine_Mdl gml = mrTemp.getGrdManageLine().get(i);
                if (!DmShared.xNull(gml.getManualCode()).trim().equalsIgnoreCase("")) {
                    gml.setOverheadCode(DmServer.getCodeViaManualCode(gml.getManualCode(), "manualCode", "overheadMast", "code", cnTemp));
                    if (DmShared.xNull(gml.getOverheadCode()).trim().equalsIgnoreCase("")) {
                        status = DmImportStatus.ERROR.getId();
                        throw new UnsupportedOperationException("OverheadCode for :" + gml.getOverhead() + " not found for" + mrTemp.getVoucherTypeShortName());
                    }
                }

                if (!DmShared.xNull(gml.getPostAC()).trim().equalsIgnoreCase("")) {
                    gml.setPostACCode(DmServer.getCodeViaManualCode(gml.getPostAC(), "name", "subGroup", "subCode", cnTemp));
                    if (DmShared.xNull(gml.getPostACCode()).trim().equalsIgnoreCase("")) {
                        status = DmImportStatus.ERROR.getId();
                        throw new UnsupportedOperationException(mrTemp.getVoucherTypeShortName() + ": Where Subgroup -> " + gml.getPostAC() + " not found.");
                    }
                }
                if (!DmShared.xNull(gml.getOverheadCode()).equalsIgnoreCase("")) {

                    sqlQuery = "INSERT INTO overheadlineconfig"
                            + " (code,isAcEditable,applicableFrom,applicableTo, overHeadCode, sno,atRate,"
                            + "  nature, formula, atRateSaveIn,"
                            + " amountSaveIn, overHeadSaveIn, acSaveIn,"
                            + " tableName, isatRateEditable, isamountEditable,"
                            + " isoverHeadAliasEditable, isshowFormulaValue,"
                            + " roundOff,uniqueId, isatRateMandatory, isamountMandatory,subCode,isPrintable)"
                            + "VALUES (:code,:isAcEditable,:applicableFrom,:applicableTo, :overHeadCode, :sno, :atRate, :nature,"
                            + " :formula, :atRateSaveIn, :amountSaveIn,"
                            + " :overHeadSaveIn, :acSaveIn, :tableName,"
                            + " :isatRateEditable, :isamountEditable, :isoverHeadAliasEditable,"
                            + " :isshowFormulaValue, :roundOff,:uniqueId, :isatRateMandatory,"
                            + " :isamountMandatory,:subCode,:isPrintable) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setDate("applicableTo", DmServer.convertDate("31-Dec-9999"));
                    psTemp.setString("sNo", String.valueOf(i + 1));
                    psTemp.setString("overHeadCode", gml.getOverheadCode());
                    psTemp.setString("subCode", DmShared.foriegnKey(gml.getPostACCode()));
                    psTemp.setString("atRate", gml.getAtRate());
                    psTemp.setString("nature", gml.getNatureCode());
                    psTemp.setString("formula", gml.getFormula());
                    psTemp.setString("atRateSaveIn", gml.getAtRateSaveInCode());
                    psTemp.setString("amountSaveIn", gml.getAmtSaveInCode());
                    psTemp.setString("overHeadSaveIn", gml.getOverheadSaveInCode());
                    psTemp.setString("acSaveIn", gml.getAcSaveCode());
                    psTemp.setString("tableName", mrTemp.getTableAffectedText());
                    psTemp.setString("isatRateEditable", DmServer.vNull(gml.getAtRateEditableCode(), "0"));
                    psTemp.setString("isamountEditable", DmServer.vNull(gml.getAmtEditableCode(), "0"));
                    psTemp.setString("isPrintable", DmServer.vNull(gml.getPrintableCode(), "0"));
                    psTemp.setString("isAcEditable", DmServer.vNull(gml.getaCEditableCode(), "0"));

                    psTemp.setString("isoverHeadAliasEditable", DmServer.vNull(gml.getOverheadEditableCode(), "0"));
                    psTemp.setString("isshowFormulaValue", DmServer.vNull(gml.getShowFormulaValueCode(), "0"));
                    psTemp.setString("roundoff", gml.getRoundOffCode());
                    psTemp.setString("UniqueId", gml.getUniqueID());
                    psTemp.setString("isatRateMandatory", DmServer.vNull(gml.getAtRateMandatoryCode(), "0"));
                    psTemp.setString("isamountMandatory", DmServer.vNull(gml.getAmtMandatoryCode(), "0"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

            }

            sqlQuery = "INSERT INTO overHeadLineConfigSite "
                    + " (code, site_Code)"
                    + " VALUES (:code, :site_Code) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("site_Code", null);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Insert Into overHeadLineConfigName(code, name, v_type, preparedBy, U_EntDt, transferDt) "
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
