package dataman.utility.overheadLineConfiguration.server;

import com.google.gson.Gson;
import dataman.utility.overheadLineConfiguration.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.ZipUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OverheadLineConfiguration_SrvImpl extends DmRSSPrivate implements OverheadLineConfiguration_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT ocn.code FROM overHeadLineConfigName AS ocn"
                    + " ORDER BY ocn.name ");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();
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
    public DmResult<OverheadLineConfiguration_Mdl> getMoveRec(String strKey) {
        DmResult<OverheadLineConfiguration_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(getMoveRec(strKey, cnTemp));
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private OverheadLineConfiguration_Mdl getMoveRec(String strKey, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, applicableDate = "";
        OverheadLineConfiguration_Mdl mrTemp = new OverheadLineConfiguration_Mdl();
        try {
            sqlQuery = "select  max(ocn.code) as code, max(ocn.v_Type) as type, max(oc.applicableFrom) as applicableFrom, "
                    + "         concat(max(vt.description), ' (', max(vt.category), ')' ) as name, max(vt.short_Name) as short_Name, "
                    + "         max(ocn.u_Name) as u_Name, max(ocn.preparedBy) as preparedBy, max(ocn.name) as overheadName "
                    + "         from overHeadLineConfig oc "
                    + "         left join overHeadLineConfigName ocn on oc.code = ocn.code "
                    + "         left join voucher_type vt on vt.v_Type = ocn.v_Type "
                    + "         where (ocn.code = :code) and (oc.applicableFrom <= cast(:uEntDt as date)) "
                    + "         group by oc.code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("TYPE")));
                mrTemp.setVoucherTypeShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                mrTemp.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setOverheadName(DmShared.xNull(rsTemp.getString("overheadName")));
            }
            psTemp.close();
            rsTemp.close();

            applicableDate = mrTemp.getApplicableFrom();
            sqlQuery = "SELECT ohc.overHeadCode, ohm.name, ohc.sno, ohc.atRate, ohc.roundOff, ohc.overHeadSaveIn, "
                    + "  ohc.isoverHeadAliasEditable AS overHeadEditable, ohc.isamountEditable, ohc.isamountMandatory, "
                    + "  ohc.amountSaveIn, ohc.formula, ohc.nature, ohc.isatRateEditable, ohc.isatRateMandatory,ohc.isPrintable, "
                    + "  ohc.atRateSaveIn, ohc.acSaveIn, ohc.isshowFormulaValue, ohm.manualCode,ohc.tableName, ohc.isAcEditable, "
                    + "  ohc.subCode,sb.name as displayName,ohc.UniqueId,isnull(ohc.atRateAmount,0) as  atRateAmount "
                    + " FROM overheadlineconfig AS ohc  "
                    + "  LEFT JOIN overheadmast AS ohm ON ohc.overHeadCode = ohm.code "
                    + " LEFT JOIN subgroup AS sb ON (ohc.subCode = sb.subCode)"
                    + " WHERE ohc.code = :code AND ohc.applicableFrom = :applicableFrom  "
                    + " ORDER BY ohc.sno ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                mrTemp.setTableAffectedText(DmShared.xNull(rsTemp.getString("tableName")));
                mrTemp.setTableAffectedTag(DmShared.xNull(rsTemp.getString("tableName")));

                GrdOverheadLine_Mdl node = new GrdOverheadLine_Mdl();
                node.setSrlNo(DmShared.xNull(rsTemp.getString("sno")));
                node.setOverheadCode(DmShared.xNull(rsTemp.getString("overHeadCode")));
                node.setOverhead(DmShared.xNull(rsTemp.getString("name")));
                node.setFormula(DmShared.xNull(rsTemp.getString("formula")));

                node.setAcEditable(DmShared.vNull(rsTemp.getString("isacEditable")).doubleValue() == 1 ? "Yes" : "No");
                node.setaCEditableCode(DmShared.xNull(rsTemp.getString("isacEditable")));

                node.setPrintable(DmShared.vNull(rsTemp.getString("isPrintable")).doubleValue() == 1 ? "Yes" : "No");
                node.setPrintableCode(DmShared.xNull(rsTemp.getString("isPrintable")));

                if (DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("")) {
                    node.setNature("");
                } else {
                    node.setNature(DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("A") ? "Add(+)" : "Ded(-)");
                }
                node.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                node.setAcSave(DmShared.xNull(rsTemp.getString("acSaveIn")));
                node.setAcSaveCode(DmShared.xNull(rsTemp.getString("acSaveIn")));
                node.setPostAC(DmShared.xNull(rsTemp.getString("displayName")));
                node.setPostACCode(DmShared.xNull(rsTemp.getString("subCode")));
                node.setOverheadSaveIn(DmShared.xNull(rsTemp.getString("overHeadSaveIn")));
                node.setOverheadSaveInCode(DmShared.xNull(rsTemp.getString("overHeadSaveIn")));
                node.setAtRateSaveIn(DmShared.xNull(rsTemp.getString("atRateSaveIn")));
                node.setAtRateSaveInCode(DmShared.xNull(rsTemp.getString("atRateSaveIn")));
                node.setAmtSaveIn(DmShared.xNull(rsTemp.getString("amountSaveIn")));
                node.setAmtSaveInCode(DmShared.xNull(rsTemp.getString("amountSaveIn")));

                node.setAtRateEditable(DmShared.vNull(rsTemp.getString("isatRateEditable")).doubleValue() == 1 ? "Yes" : "No");
                node.setAtRateEditableCode(DmShared.xNull(rsTemp.getString("isatRateEditable")));
                node.setAmtEditable(DmShared.vNull(rsTemp.getString("isamountEditable")).doubleValue() == 1 ? "Yes" : "No");
                node.setAmtEditableCode(DmShared.xNull(rsTemp.getString("isamountEditable")));
                node.setOverheadEditable(DmShared.vNull(rsTemp.getString("overHeadEditable")).doubleValue() == 1 ? "Yes" : "No");
                node.setOverheadEditableCode(DmShared.xNull(rsTemp.getString("overHeadEditable")));
                node.setShowFormulaValue(DmShared.vNull(rsTemp.getString("isshowFormulaValue")).doubleValue() == 1 ? "Yes" : "No");
                node.setShowFormulaValueCode(DmShared.xNull(rsTemp.getString("isshowFormulaValue")));
                node.setAtRateMandatory(DmShared.vNull(rsTemp.getString("isatRateMandatory")).doubleValue() == 1 ? "Yes" : "No");
                node.setAtRateMandatoryCode(DmShared.xNull(rsTemp.getString("isatRateMandatory")));
                node.setAmtMandatoryCode(DmShared.xNull(rsTemp.getString("isamountMandatory")));
                node.setAmtMandatory(DmShared.vNull(rsTemp.getString("isamountMandatory")).doubleValue() == 1 ? "Yes" : "No");

                node.setAtRateAmountTag(DmShared.xNull(rsTemp.getString("atRateAmount")));
                node.setAtRateAmountText(DmShared.iNull(rsTemp.getString("atRateAmount")).intValue() == 0 ? "Auto" : "Manual");

                node.setRoundOff(DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("Y") ? "Yes" : (DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("N") ? "No"
                        : (DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("D") ? "Disable" : DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("H") ? "Higher" : "Lower")));
                node.setRoundOffCode(DmShared.xNull(rsTemp.getString("roundOff")));
                node.setAtRate(DmShared.xNull(rsTemp.getString("atRate")));
                node.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                node.setUniqueID(DmShared.xNull(rsTemp.getString("UniqueId")));
                mrTemp.getGrdManageLine().add(node);
            }

            psTemp.close();
            rsTemp.close();

            List<KeyFieldExtended> kfList = new ArrayList<KeyFieldExtended>();

            sqlQuery = "SELECT ocs.site_Code, sm.name  "
                    + " FROM overHeadLineConfigSite AS ocs "
                    + " LEFT JOIN siteMast AS sm ON ocs.site_Code = sm.code "
                    + " WHERE ocs.code = :code AND isnull(ocs.site_Code,'') <>'' ORDER BY sm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                KeyFieldExtended kf = new KeyFieldExtended();
                kf.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_Code")));
                kf.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                kfList.add(kf);
            }
            mrTemp.getGrdSiteName().addAll(kfList);
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
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
        return mrTemp;
    }

    @Override
    public DmResult<String> saveRecord(OverheadLineConfiguration_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        List<GrdOverheadLine_Mdl> grd = mrTemp.getGrdManageLine();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, maxApplicableDate = "";
        ResultSet rsTemp = null;
        boolean blnAdd = false;

        dmrTemp.setData("");

        try {
            OverheadLineConfiguration_Vld.chkSave(mrTemp);
            OverheadLineConfiguration_Vld.checkGrid(grd);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            mrTemp.setOverheadName(DmShared.removeSpace(mrTemp.getOverheadName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "overHeadLineConfigName", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT COUNT(name) AS cnt FROM overHeadLineConfigName WHERE name = :name AND CODE  <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getOverheadName()).toLowerCase());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Overhead Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            int size = mrTemp.getGrdSiteName().size();

            if (size == 0) {
                size = 1;
                mrTemp.getGrdSiteName().add(new KeyFieldExtended());
            }

            for (int i = 0; i < size; i++) {
                KeyFieldExtended kf = mrTemp.getGrdSiteName().get(i);

                sqlQuery = "SELECT count(ocn.v_Type) AS cnt FROM overHeadLineConfigSite AS ocs "
                        + "LEFT JOIN overHeadLineConfigName AS ocn "
                        + "ON ocn.code = ocs.code  "
                        + "WHERE ocn.v_type = :vType AND isnull(ocs.site_Code,'') = :siteCode AND ocn.code <> :code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("vType", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("siteCode", DmShared.xNull(kf.getSharedKeyFieldCode()));
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    if (DmShared.xNull(kf.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        throw new UnsupportedOperationException("Overhead already exists for this site.");
                    } else {
                    }
                    throw new UnsupportedOperationException("Overhead already exists for "
                            + "" + DmShared.xNull(kf.getSharedKeyFieldName()) + ".");
                }
                rsTemp.close();
                psTemp.close();

            }

            //-------------------------------------ApplicableFrom-----------------------------------------
            sqlQuery = " SELECT isNull(MAX(applicableFrom),'') as applicableFrom "
                    + " FROM overheadlineconfig WHERE code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                maxApplicableDate = "";
                if (!DmShared.xNull(rsTemp.getString("applicableFrom")).equalsIgnoreCase("")) {
                    maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFrom"));
                }
            }
            if (!maxApplicableDate.equalsIgnoreCase("")) {
                if (DmServer.convertDate(mrTemp.getApplicableFrom()).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                    sqlQuery = " SELECT max(code) as code,applicableFrom,max(applicableTo) as applicableTo "
                            + "FROM overheadlineconfig "
                            + "WHERE code= :code AND applicableFrom < :applicableFrom "
                            + "GROUP BY applicableFrom "
                            + "ORDER BY applicableFrom DESC ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setDate("ApplicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        mrTemp.setApplicableTo(DmShared.dNull(rsTemp.getDate("applicableTo")));
                        sqlQuery = "update overheadlineconfig set applicableTo=:applicableTo where code = :code and ApplicableFrom = :ApplicableFrom ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                        psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("ApplicableFrom", rsTemp.getString("ApplicableFrom"));
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        sqlQuery = " SELECT max(code) as code,applicableFrom,max(applicableTo) as applicableTo "
                                + "FROM overheadlineconfig "
                                + "WHERE code= :code AND applicableFrom > :applicableFrom "
                                + "GROUP BY applicableFrom "
                                + "ORDER BY applicableFrom ASC  ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setDate("ApplicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                        rsTemp = psTemp.executeQuery();
                        if (rsTemp.next()) {
                            mrTemp.setApplicableTo(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp.getDate("applicableFrom"))));
                        }
                    }
                    psTemp.close();
                    rsTemp.close();
                } else {

                    mrTemp.setApplicableTo(DmShared.xNull("31-Dec-9999"));
                    sqlQuery = "update overheadlineconfig set applicableTo=:applicableTo where code = :code and ApplicableFrom = :ApplicableFrom ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setDate("ApplicableFrom", DmServer.convertDate(maxApplicableDate));
                    psTemp.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getApplicableFrom()))));
                    psTemp.executeUpdate();
                    psTemp.close();

                }
            } else {
                mrTemp.setApplicableTo(DmShared.xNull("31-Dec-9999"));
            }

            //-------------------------------------ApplicableFrom End-----------------------------------------
            sqlQuery = "Delete From overheadlineconfig where code = :code and ApplicableFrom = :ApplicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setDate("ApplicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
            psTemp.executeUpdate();
            psTemp.close();

            // Insert Record in OverHeadLineConfige Table 
            /////////////////////////////////////////////////////////////////////////////////////         
            for (int i = 0; i < grd.size(); i++) {
                GrdOverheadLine_Mdl gtc = mrTemp.getGrdManageLine().get(i);
                if (!DmShared.xNull(gtc.getOverheadCode()).equalsIgnoreCase("")) {

                    sqlQuery = "INSERT INTO overheadlineconfig"
                            + " (code,isAcEditable,applicableFrom,applicableTo, overHeadCode, sno,atRate,"
                            + "  nature, formula, atRateSaveIn,"
                            + " amountSaveIn, overHeadSaveIn, acSaveIn,"
                            + " tableName, isatRateEditable, isamountEditable,"
                            + " isoverHeadAliasEditable, isshowFormulaValue,"
                            + " roundOff, isatRateMandatory, isamountMandatory,subCode,isPrintable,UniqueId,atRateAmount)"
                            + "VALUES (:code,:isAcEditable,:applicableFrom,:applicableTo, :overHeadCode, :sno, :atRate, :nature,"
                            + " :formula, :atRateSaveIn, :amountSaveIn,"
                            + " :overHeadSaveIn, :acSaveIn, :tableName,"
                            + " :isatRateEditable, :isamountEditable, :isoverHeadAliasEditable,"
                            + " :isshowFormulaValue, :roundOff, :isatRateMandatory,"
                            + " :isamountMandatory,:subCode,:isPrintable,:UniqueId,:atRateAmount) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setDate("applicableTo", DmServer.convertDate(mrTemp.getApplicableTo()));
                    psTemp.setString("sNo", gtc.getSrlNo());
                    psTemp.setString("overHeadCode", DmShared.foriegnKey(grd.get(i).getOverheadCode()));
                    psTemp.setString("subCode", DmShared.foriegnKey(grd.get(i).getPostACCode()));
                    psTemp.setString("atRate", grd.get(i).getAtRate());
                    psTemp.setString("nature", grd.get(i).getNatureCode());
                    psTemp.setString("formula", grd.get(i).getFormula());
                    psTemp.setString("atRateSaveIn", grd.get(i).getAtRateSaveInCode());
                    psTemp.setString("amountSaveIn", grd.get(i).getAmtSaveInCode());
                    psTemp.setString("overHeadSaveIn", grd.get(i).getOverheadSaveInCode());
                    psTemp.setString("acSaveIn", grd.get(i).getAcSaveCode());
                    psTemp.setString("tableName", mrTemp.getTableAffectedText());
                    psTemp.setString("isatRateEditable", DmServer.vNull(grd.get(i).getAtRateEditableCode(), "0"));
                    psTemp.setString("isamountEditable", DmServer.vNull(grd.get(i).getAmtEditableCode(), "0"));
                    psTemp.setString("isPrintable", DmServer.vNull(grd.get(i).getPrintableCode(), "0"));
                    psTemp.setString("isAcEditable", DmServer.vNull(grd.get(i).getaCEditableCode(), "0"));
                    psTemp.setString("UniqueId", grd.get(i).getUniqueID());
                    psTemp.setString("isoverHeadAliasEditable", DmServer.vNull(grd.get(i).getOverheadEditableCode(), "0"));
                    psTemp.setString("isshowFormulaValue", DmServer.vNull(grd.get(i).getShowFormulaValueCode(), "0"));
                    psTemp.setString("roundoff", grd.get(i).getRoundOffCode());
                    psTemp.setString("isatRateMandatory", DmServer.vNull(grd.get(i).getAtRateMandatoryCode(), "0"));
                    psTemp.setString("isamountMandatory", DmServer.vNull(grd.get(i).getAmtMandatoryCode(), "0"));
                    psTemp.setString("atRateAmount", DmShared.xNull(grd.get(i).getAtRateAmountTag()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            sqlQuery = "Delete From overHeadLineConfigSite where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            for (int i = 0; i < size; i++) {
                KeyFieldExtended kf = mrTemp.getGrdSiteName().get(i);

                if (!DmShared.xNull(kf.getSharedKeyFieldCode()).equalsIgnoreCase("")) {

                    sqlQuery = "INSERT INTO overHeadLineConfigSite "
                            + " (code, site_Code)"
                            + " VALUES (:code, :site_Code) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("site_Code", DmShared.foriegnKey(kf.getSharedKeyFieldCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            sqlQuery = "SELECT COUNT(code) AS cnt FROM overHeadLineConfigSite WHERE code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") == 0) {
                sqlQuery = "INSERT INTO overHeadLineConfigSite "
                        + " (code, site_Code)"
                        + " VALUES (:code, :site_Code) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("site_Code", null);
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "Insert Into overHeadLineConfigName(code, name, v_type, preparedBy, U_EntDt, transferDt) "
                        + " Values (:code, :name, :v_type, :uName, getDate(), :transferDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", mrTemp.getOverheadName());
                psTemp.setString("transferDt", null);
                psTemp.setString("v_type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("uName", DmServer.getSsnUserName(this));
            } else {
                sqlQuery = " Update overHeadLineConfigName Set "
                        + " name = :name, "
                        + " v_type = :v_type, "
                        + " U_Name = :uName, "
                        + " transferDt = :transferDt, "
                        + " U_EntDt = getDate() "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", mrTemp.getOverheadName());
                psTemp.setString("v_type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("transferDt", null);
                psTemp.setString("uName", DmServer.getSsnUserName(this));
            }
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "select count(ohc.sno) as cnt, ohc.sno "
                    + "from overHeadLineConfig ohc "
                    + "where ohc.code = :code and ohc.applicableFrom = :applicableFrom "
                    + "group by ohc.sno";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableFrom", DmServer.convertDate(DmShared.dNull(mrTemp.getApplicableFrom())));
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 1) {
                    throw new DmException(0, "S.No." + DmShared.iNull(rsTemp.getString("sno")).toString() + " is duplicate");
                }
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            /////////////////////////////////////////////////////////////////////////////////////

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
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = " SELECT COUNT(*) as cnt FROM( SELECT applicableFrom FROM overHeadLineConfig WHERE code= :code GROUP BY applicableFrom) AS t ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 1) {
                throw new UnsupportedOperationException("Record Contains Multiple Price List. You Can't delete this Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Delete From overHeadLineConfigSite Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            rsTemp.close();
            psTemp.close();

            strSQL = "Delete From overHeadLineConfig Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            rsTemp.close();
            psTemp.close();

            strSQL = "Delete From overHeadLineConfigName Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            rsTemp.close();
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
        String strSQL;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT MAX(ocn.code) AS code, MAX(ocn.v_Type) AS TYPE, "
                    + "  CONCAT(MAX(vt.description), ' (', MAX(vt.category),')' ) AS NAME, "
                    + "  MAX(oc.applicableFrom) AS applicableFrom, MAX(ocn.name) AS overheadName  "
                    + " FROM overHeadLineConfigName AS ocn   "
                    + "  LEFT JOIN overHeadLineConfig AS oc ON ocn.code = oc.code "
                    + "  LEFT OUTER JOIN voucher_type AS vt ON vt.v_Type = ocn.v_Type  "
                    + " WHERE (oc.applicableFrom <= CAST(GETDATE () AS DATE)) "
                    + " GROUP BY oc.code  ";

            sqlWithParam.setSQL(strSQL);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setOverheadName(DmShared.xNull(rsTemp.getString("overheadName")));
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
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT v_Type, CONCAT(description, ' (', category, ')') AS NAME, short_Name, description "
                    + " FROM voucher_type ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("short_Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTableAffected(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sd.name FROM " + DmServer.getSsnDBName(this) + ".sys.objects AS sd "
                    + "WHERE (sd.type = 'U') ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoundOff(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Disable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("H");
            listNode.setSharedKeyFieldName("Higher");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Lower");
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
    public DmResultPageing<List<TxtHOverhead_Mdl>> getTxtHOverhead(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHOverhead_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHOverhead_Mdl> lstTemp = new ArrayList<>();
        TxtHOverhead_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT CODE, NAME, manualCode FROM overheadmast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHOverhead_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Add(+)");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Ded(-)");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHACSave(String tableName, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sc.name FROM " + DmServer.getSsnDBName(this) + ".sys.columns AS sc "
                    + " RIGHT OUTER JOIN " + DmServer.getSsnDBName(this) + ".sys.objects AS so ON so.object_id = sc.object_id "
                    + "WHERE (so.name = '" + tableName + "') AND (sc.name LIKE 'oh_Ac_%')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOverheadSaveIn(String tableName, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sc.name FROM " + DmServer.getSsnDBName(this) + ".sys.columns AS sc "
                    + " RIGHT OUTER JOIN " + DmServer.getSsnDBName(this) + ".sys.objects AS so ON so.object_id = sc.object_id "
                    + "WHERE (so.name = '" + tableName + "') AND (sc.name LIKE 'oh_OH_%')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResult<List<TxtHFormula_Mdl>> getTxtHFormula() {
        DmResult<List<TxtHFormula_Mdl>> rstTemp = new DmResult<>();
        List<TxtHFormula_Mdl> lstTemp = new ArrayList<>();
        TxtHFormula_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sql = "";
        DmPreparedStatement psTemp = null;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sql = " SELECT CODE, NAME, manualCode FROM overheadmast order by Name";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHFormula_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setOverheadCode(DmShared.xNull(rsTemp.getString("manualCode")));
                lstTemp.add(listNode);
            }
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
    public DmResult<List<KeyFieldExtended>> fillGridApplicableDate(String code) {
        DmResult<List<KeyFieldExtended>> rstTemp = new DmResult<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sql = "";
        DmPreparedStatement psTemp = null;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sql = " SELECT MAX(code) AS code, applicableFrom, MAX(applicableTo) AS applicableTo "
                    + "FROM overheadlineconfig "
                    + "WHERE (code = :code) "
                    + "GROUP BY applicableFrom ORDER BY applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("code", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableTo")));
                lstTemp.add(listNode);
            }
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
    public DmResult<List<GrdOverheadLine_Mdl>> fillGrid(String code, String applicableFromDate) {
        DmResult<List<GrdOverheadLine_Mdl>> rstTemp = new DmResult<>();
        List<GrdOverheadLine_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String strSQL = "";
        DmPreparedStatement psTemp = null;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQL = "SELECT ohc.code, ohc.overHeadCode, ohm.name, ohc.sno, ohc.subCode,sb.name as displayName,"
                    + "  ohc.atRate, ohc.roundOff, ohc.overHeadSaveIn, ohc.isoverHeadAliasEditable AS overHeadEditable, "
                    + "  ohc.isamountEditable, ohc.isamountMandatory, ohc.amountSaveIn, ohc.formula,ohc.isPrintable, "
                    + "  ohc.nature, ohc.isatRateEditable, ohc.isatRateMandatory, ohc.atRateSaveIn, "
                    + "  ohc.acSaveIn, ohc.isshowFormulaValue, ohm.manualCode,ohc.tableName ,ohc.isacEditable,ohc.UniqueId "
                    + " FROM overheadlineconfig AS ohc  "
                    + "  LEFT JOIN overheadmast AS ohm ON ohc.overHeadCode = ohm.code "
                    + " LEFT JOIN subgroup AS sb ON (ohc.subCode = sb.subCode) "
                    + " WHERE ohc.code = :CODE AND ohc.applicableFrom = :applicableFrom  "
                    + " ORDER BY ohc.sno  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(code).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFromDate));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdOverheadLine_Mdl objListTemp = new GrdOverheadLine_Mdl();
                objListTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("tableName")));
                objListTemp.setSrlNo(DmShared.xNull(rsTemp.getString("sno")));
                objListTemp.setOverheadCode(DmShared.xNull(rsTemp.getString("overHeadCode")));
                objListTemp.setOverhead(DmShared.xNull(rsTemp.getString("name")));
                objListTemp.setFormula(DmShared.xNull(rsTemp.getString("formula")));

                objListTemp.setAcEditable(DmShared.vNull(rsTemp.getString("isacEditable")).doubleValue() == 1 ? "Yes" : "No");
                objListTemp.setaCEditableCode(DmShared.xNull(rsTemp.getString("isacEditable")));

                objListTemp.setPrintable(DmShared.vNull(rsTemp.getString("isPrintable")).doubleValue() == 1 ? "Yes" : "No");
                objListTemp.setPrintableCode(DmShared.xNull(rsTemp.getString("isPrintable")));

                if (DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("")) {
                    objListTemp.setNature("");
                } else {
                    objListTemp.setNature(DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("A") ? "Add(+)" : "Ded(-)");
                }
                objListTemp.setNatureCode(DmShared.xNull(rsTemp.getString("nature")));
                objListTemp.setPostAC(DmShared.xNull(rsTemp.getString("displayName")));
                objListTemp.setPostACCode(DmShared.xNull(rsTemp.getString("subCode")));
                objListTemp.setAcSave(DmShared.xNull(rsTemp.getString("acSaveIn")));
                objListTemp.setAcSaveCode(DmShared.xNull(rsTemp.getString("acSaveIn")));
                objListTemp.setOverheadSaveIn(DmShared.xNull(rsTemp.getString("overHeadSaveIn")));
                objListTemp.setOverheadSaveInCode(DmShared.xNull(rsTemp.getString("overHeadSaveIn")));
                objListTemp.setAtRateSaveIn(DmShared.xNull(rsTemp.getString("atRateSaveIn")));
                objListTemp.setAtRateSaveInCode(DmShared.xNull(rsTemp.getString("atRateSaveIn")));
                objListTemp.setAmtSaveIn(DmShared.xNull(rsTemp.getString("amountSaveIn")));
                objListTemp.setAmtSaveInCode(DmShared.xNull(rsTemp.getString("amountSaveIn")));

                objListTemp.setAtRateEditable(DmShared.vNull(rsTemp.getString("isatRateEditable")).doubleValue() == 1 ? "Yes" : "No");
                objListTemp.setAtRateEditableCode(DmShared.xNull(rsTemp.getString("isatRateEditable")));

                objListTemp.setAmtEditable(DmShared.vNull(rsTemp.getString("isamountEditable")).doubleValue() == 1 ? "Yes" : "No");
                objListTemp.setAmtEditableCode(DmShared.xNull(rsTemp.getString("isamountEditable")));

                objListTemp.setOverheadEditable(DmShared.vNull(rsTemp.getString("overHeadEditable")).doubleValue() == 1 ? "Yes" : "No");
                objListTemp.setOverheadEditableCode(DmShared.xNull(rsTemp.getString("overHeadEditable")));

                objListTemp.setShowFormulaValue(DmShared.vNull(rsTemp.getString("isshowFormulaValue")).doubleValue() == 1 ? "Yes" : "No");
                objListTemp.setShowFormulaValueCode(DmShared.xNull(rsTemp.getString("isshowFormulaValue")));

                objListTemp.setAtRateMandatory(DmShared.vNull(rsTemp.getString("isatRateMandatory")).doubleValue() == 1 ? "Yes" : "No");
                objListTemp.setAtRateMandatoryCode(DmShared.xNull(rsTemp.getString("isatRateMandatory")));

                objListTemp.setAmtMandatory(DmShared.vNull(rsTemp.getString("isamountMandatory")).doubleValue() == 1 ? "Yes" : "No");
                objListTemp.setAmtMandatoryCode(DmShared.xNull(rsTemp.getString("isamountMandatory")));

                objListTemp.setRoundOff(DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("Y") ? "Yes" : (DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("N") ? "No"
                        : (DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("D") ? "Disable" : DmShared.xNull(rsTemp.getString("roundOff")).equalsIgnoreCase("H") ? "Higher" : "Lower")));
                objListTemp.setRoundOffCode(DmShared.xNull(rsTemp.getString("roundOff")));
                objListTemp.setAtRate(DmShared.xNull(rsTemp.getString("atRate")));
                objListTemp.setUniqueID(DmShared.xNull(rsTemp.getString("UniqueId")));
                objListTemp.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));

                lstTemp.add(objListTemp);
            }
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
    public DmResult<String> deleteRecordApplicable(String strKey, String applicableDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL, applicableToDate = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = " SELECT applicableFrom , applicableTo  FROM overheadlineconfig WHERE code= :code and applicableFrom= :applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                applicableToDate = rsTemp.getString("applicableTo");
            } else {
                throw new UnsupportedOperationException("ApplicableFrom Date is Invalid");
            }

            strSQL = " SELECT code, applicableFrom, applicableTo FROM overheadlineconfig "
                    + " WHERE code= :code AND applicableFrom < :applicableFrom "
                    + " ORDER BY applicableFrom DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("ApplicableFrom", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                strSQL = "update overheadlineconfig set applicableTo=:applicableTo "
                        + " where code = :code and ApplicableFrom = :ApplicableFrom ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("applicableTo", applicableToDate);
                psTemp.setString("code", DmShared.iNull(strKey).toString());
                psTemp.setString("ApplicableFrom", rsTemp.getString("ApplicableFrom"));
                psTemp.executeUpdate();
                psTemp.close();
                rsTemp.close();
            }

            strSQL = " delete from overheadlineconfig "
                    + " where code=:code and applicableFrom= :applicableFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableDate));
            psTemp.executeUpdate();

            rsTemp.close();
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
            sqlWithParam.setSQL("SELECT SM.Code,SM.Name FROM SiteMast SM  ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAtRateSaveIn(String tableName, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sc.name FROM " + DmServer.getSsnDBName(this) + ".sys.columns AS sc "
                    + " RIGHT OUTER JOIN " + DmServer.getSsnDBName(this) + ".sys.objects AS so ON so.object_id = sc.object_id "
                    + "WHERE (so.name = '" + tableName + "') AND (sc.name LIKE 'oh_at_%')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAmtSaveIn(String tableName, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sc.name FROM " + DmServer.getSsnDBName(this) + ".sys.columns AS sc "
                    + " RIGHT OUTER JOIN " + DmServer.getSsnDBName(this) + ".sys.objects AS so ON so.object_id = sc.object_id "
                    + "WHERE (so.name = '" + tableName + "') AND (sc.name LIKE 'oh_amt_%')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<TxtHPostAC_Mdl>> getTxtHPostAC(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPostAC_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPostAC_Mdl> lstTemp = new ArrayList<>();
        TxtHPostAC_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT Subcode, NAME, groupCode FROM subgroup ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHPostAC_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Subcode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("groupCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHApplicableFrom(String code, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT MAX(code) AS code, applicableFrom "
                    + " FROM overheadLineConfig "
                    + " WHERE (code = :code) "
                    + " GROUP BY applicableFrom ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("code", DmShared.iNull(code).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableFrom")));
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
    public DmResult<KeyFieldExtended> getMaxApplicableDate(String code) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended listNode = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sql = "";
        DmPreparedStatement psTemp = null;
        try {
            rstTemp.setData(listNode);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sql = " select top 1 max(applicableFrom) as applicableFrom "
                    + "FROM overheadLineConfig "
                    + "WHERE (code = :code) "
                    + "GROUP BY applicableFrom ORDER BY applicableFrom desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("code", code);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("applicableFrom")));
            }
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
    public DmResult<KeyFieldExtended> exportJson(String sessionId, OverheadLineConfiguration_Mdl mrTemp) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        String fileName = "", sqlQuery, directoryPath, serverFileName, fullFilePath;
        char separator = File.separatorChar;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        KeyFieldExtended field = null;
        File sFile = null;
        FileWriter writer = null;
        ZipUtils zipUtils = null;
        try {
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "overHeadLineConfig";
            sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  ohc.code, ohc.Name"
                    + "		from overHeadLineConfigName ohc"
                    + "		order by ohc.Name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                fileName = DmShared.xNull(rsTemp.getString("name")).replaceAll(" ", "_");
                fileName = DmShared.xNull(fileName).replaceAll("/", "_");
                if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                    throw new DmException(0, "File Name not given.");
                }
//                directoryPath = DmServer.Path.sessions + separator + sessionId;
                serverFileName = fileName + "-OHL.json";
                fullFilePath = directoryPath + separator + serverFileName;
                mrTemp = getMoveRec(DmShared.xNull(rsTemp.getString("code")), cnTemp);
                mrTemp.getGrdSiteName().clear();
                Gson gson = new Gson();
                String json = gson.toJson(mrTemp);
                writer = new FileWriter(fullFilePath);
                writer.write(json);
                writer.close();
            }
            rsTemp.close();
            psTemp.close();

            zipUtils = new ZipUtils(directoryPath, directoryPath + File.separatorChar + "overHeadLineConfig.zip");
            zipUtils.zipIt();
            field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName("overHeadLineConfig.zip");

            rstTemp.setStatus(true);
            rstTemp.setData(field);
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
                if (writer != null) {
                    writer.close();
                }
                if (zipUtils != null) {
                    zipUtils = null;
                }
            } catch (Exception e) {
            }
        }

        return rstTemp;
    }

    @Override
    public DmResult<OverheadLineConfiguration_Mdl> importJson(String filePath, String fileName) {
        DmResult<OverheadLineConfiguration_Mdl> dmrTemp = new DmResult<>();
        OverheadLineConfiguration_Mdl mrTemp = new OverheadLineConfiguration_Mdl();
        BufferedReader br = null;
        FileReader fr = null;
        dmrTemp.setData(mrTemp);
        try {

            Gson gson = new Gson();
            String readDirectoryPath = filePath + File.separatorChar + fileName;
            fr = new FileReader(readDirectoryPath);
            br = new BufferedReader(fr);
            mrTemp = gson.fromJson(br, OverheadLineConfiguration_Mdl.class);
            dmrTemp.setData(mrTemp);
            System.out.println(mrTemp.getVoucherTypeShortName() + " : " + mrTemp.getOverheadName());
            dmrTemp.setStatus(true);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {

            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<OverheadLineConfiguration_Mdl> checkImportedValues(OverheadLineConfiguration_Mdl mrTemp) {
        DmResult<OverheadLineConfiguration_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT COUNT(ocn.name) AS cnt,MAX(ocn.code) AS code,  MAX(ocn.name) AS overheadName   "
                    + " FROM  overheadLineconfigName AS ocn   "
                    + " WHERE ocn.name=:name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getOverheadName()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    mrTemp.setOverheadName(DmShared.xNull(rsTemp.getString("overheadName")));
                }
            }

            psTemp.close();
            rsTemp.close();

            List<GrdOverheadLine_Mdl> overheadList = new ArrayList<GrdOverheadLine_Mdl>();
            overheadList = mrTemp.getGrdManageLine();

            for (int i = 0; i < overheadList.size(); i++) {
                sqlQuery = "SELECT  COUNT(ohm.code) AS cnt,MAX( ohm.name) AS overHeadName, MAX(ohm.code) as overHeadCode, MAX(ohm.manualCode) AS manualCode "
                        + "  FROM overheadmast AS ohm  "
                        + "  WHERE ohm.manualCode = :manualCode  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("manualCode", DmShared.xNull(overheadList.get(i).getManualCode()));
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    GrdOverheadLine_Mdl overheadMdl = mrTemp.getGrdManageLine().get(i);
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                        overheadMdl.setOverheadCode(DmShared.xNull(rsTemp.getString("overHeadCode")));
                        overheadMdl.setOverhead(DmShared.xNull(rsTemp.getString("overHeadName")));
                        overheadMdl.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                    } else {
                        if (!DmShared.xNull(overheadMdl.getManualCode()).equalsIgnoreCase("")) {
                            overheadMdl.setMcColorStatus("1");
                        }
                    }
                }
                rsTemp.close();
                psTemp.close();
                sqlQuery = "SELECT count(ohc.subCode) AS cnt, MAX(ohc.subCode) AS subCode, MAX(sb.name) as displayName "
                        + "  FROM overheadLineconfig AS ohc  "
                        + "  LEFT JOIN subgroup AS sb ON (ohc.subCode = sb.subCode)  "
                        + "  WHERE sb.name=:name   ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(overheadList.get(i).getPostAC()));
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    GrdOverheadLine_Mdl overheadMdl = mrTemp.getGrdManageLine().get(i);
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                        overheadMdl.setPostAC(DmShared.xNull(rsTemp.getString("displayName")));
                        overheadMdl.setPostACCode(DmShared.xNull(rsTemp.getString("subCode")));
                    } else {
                        if (!DmShared.xNull(overheadMdl.getPostAC()).equalsIgnoreCase("")) {
                            overheadMdl.setPacColorStatus("1");
                        }
                    }
                }
                rsTemp.close();
                psTemp.close();

            }
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtAtRateAmount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Auto");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Manual");
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
