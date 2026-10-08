package dataman.dmToolkit.overHead.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHead.shared.OverHeadMA_Mdl;
import dataman.dmToolkit.overHead.shared.OverHeadPost_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.RecId;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OverHead_Server { 

    public static List<OverHead_Mdl> getOverHead(String vType, String vDate, String siteCode, String docId, Connection cnTemp) {
        return getOverHead(vType, vDate, siteCode, docId, "overHead", "overHeadMA", cnTemp);
    }

    public static List<OverHead_Mdl> getOverHead(String vType, String vDate, String siteCode, String docId, String overHeadTableName, String overHeadTableNameMA, Connection cnTemp) {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery;
        List<OverHead_Mdl> overHeadList = new ArrayList<>();
        OverHead_Mdl overHead;
        OverHeadMA_Mdl overHeadMA;

        try {
            if (DmShared.xNull(docId).trim().equalsIgnoreCase("")) {
//                sqlQuery = "Select ohc.overHeadCode,ohc.subcode,ohc.atRate,ohc.contraOhMCode,ohc.drCr,ohc.nature,"
//                        + "ohc.atRateSaveIn,ohc.amountSaveIn,ohc.tableName,ohc.isatRateEditable,ohc.isamountEditable,ohc.isacEditable, "
//                        + "ohc.isshowFormulaValue,ohc.roundOff,ohc.ohSetUp,ohc.UniqueId, "
//                        + "ohm.name as ohName,ohm.manualCode As ohManualCode,SG.Name As ACName,0 As Amount,ohc.formula, "
//                        + "ohc.isamountMandatory,ohc.isatRateMandatory, "
//                        + "Null As overheadAlias,ohc.isoverHeadAliasEditable "
//                        + "From overHeadConfig ohc  "
//                        + "Left Join overHeadMast ohm On ohc.overHeadCode=ohm.Code "
//                        + "Left Join SubGroup SG On SG.SubCode=ohc.subcode "
//                        + "Where ohc.VType = :vType and (:vDate between applicableFrom and applicableTo) "
//                        + "Order By ohc.SNo";

                sqlQuery = "Select ohc.overHeadCode,ohc.subcode,ohc.atRate,ohc.contraOhMCode,ohc.drCr,ohc.nature,ohc.atRateSaveIn, "
                        + "ohc.amountSaveIn,ohc.tableName,ohc.isatRateEditable,ohc.isamountEditable,ohc.isacEditable,  "
                        + "ohc.isshowFormulaValue,ohc.roundOff,ohc.ohSetUp,ohc.UniqueId, ohm.name as ohName, "
                        + "ohm.manualCode As ohManualCode,SG.Name As ACName,0 As Amount,ohc.formula, ohc.formula AS actualFormula, null AS excludeFormula, ohc.isamountMandatory, "
                        + "ohc.isatRateMandatory, Null As overheadAlias,ohc.isoverHeadAliasEditable,ohc.isPrintable, ohc.calLValOn, ohc.isPartialPosting  "
                        + "From  "
                        + "( "
                        + "select ohc.* from overHeadConfig ohc, "
                        + "( "
                        + "select	top 1 ohcs.code,ohcs.site_code  "
                        + "from overHeadConfigSite ohcs, "
                        + "( "
                        + "select	ohcn.code  "
                        + "from overHeadConfigName ohcn  "
                        + "where ohcn.v_Type = :vType "
                        + ") as ohcn  "
                        + "where ohcs.code = ohcn.code and  "
                        + "isnull(ohcs.site_Code,:siteCode) = :siteCode "
                        + "order by ohcs.site_Code desc "
                        + ") as ohcs "
                        + "where ohcs.code = ohc.code and (:vDate between applicableFrom and applicableTo)  "
                        + ") as ohc "
                        + "Left Join overHeadMast ohm On ohc.overHeadCode = ohm.Code  "
                        + "Left Join SubGroup SG On SG.SubCode = ohc.subcode  "
                        + "Order By ohc.SNo ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("vType", vType);
                psTemp.setString("siteCode", siteCode);
                psTemp.setDate("vDate", DmServer.convertDate(vDate));
            } else {

                sqlQuery = "Select oh.overHeadCode,oh.subcode,oh.amount,oh.atRate,oh.contraOhMCode,oh.drCr,oh.nature,oh.sNo,"
                        + "oh.atRateSaveIn,oh.amountSaveIn,oh.tableName,oh.isatRateEditable,oh.isamountEditable,oh.isacEditable, "
                        + "oh.isshowFormulaValue,oh.roundOff,oh.ohSetUp,oh.UniqueId, "
                        + "ohm.Name ohName,oh.overHeadMCode As ohManualCode,SG.Name As ACName,oh.formula, oh.actualFormula, oh.excludeFormula, "
                        + "oh.isamountMandatory,oh.isatRateMandatory, "
                        + "oh.overheadAlias,oh.isoverHeadAliasEditable,oh.isPrintable, oh.calLValOn, oh.isPartialPosting "
                        + "From " + overHeadTableName + " oh "
                        + "Left Join overHeadMast ohm On oh.overHeadCode=ohm.Code "
                        + "Left Join SubGroup SG On SG.SubCode=oh.subcode "
                        + "Where oh.DocId=:docid "
                        + "Order By oh.SNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", docId);
            }
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                overHead = new OverHead_Mdl();
                overHead.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("overHeadCode")));
                overHead.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ohName")));
                overHead.setOverHeadAlias(DmShared.xNull(rsTemp.getString("overheadAlias")));

                if (DmShared.xNull(overHead.getOverHeadAlias()).trim().equalsIgnoreCase("")) {
                    overHead.setOverHeadAlias(overHead.getSharedKeyFieldName());
                }

                overHead.setRoundOff(DmShared.xNull(rsTemp.getString("roundOff")));

                if (DmShared.xNull(overHead.getRoundOff()).trim().equalsIgnoreCase("D")) {
                    overHead.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.0000000"));
                    overHead.setAtRate(DmServer.vNull(rsTemp.getString("atRate"), "0.0000000"));
                } else {
                    overHead.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                    overHead.setAtRate(DmServer.vNull(rsTemp.getString("atRate"), "0.000"));
                }

                if (DmShared.vNull(overHead.getAmount()) == 0) {
                    overHead.setAmount("");
                }

                if (DmShared.vNull(overHead.getAtRate()) == 0) {
                    overHead.setAtRate("");
                }

                overHead.setOverHeadAliasEditable(DmShared.xNull(rsTemp.getString("isoverHeadAliasEditable")));
                overHead.setAtRateMandatory(DmShared.xNull(rsTemp.getString("isatRateMandatory")));
                overHead.setAmountMandatory(DmShared.xNull(rsTemp.getString("isamountMandatory")));
                overHead.setOhManualCode(DmShared.xNull(rsTemp.getString("ohManualCode")));
                overHead.setUniqueId(DmShared.xNull(rsTemp.getString("UniqueId")));
                overHead.setOHSetUp(DmShared.xNull(rsTemp.getString("ohSetUp")));
                overHead.setAtRateSaveIn(DmShared.xNull(rsTemp.getString("atRateSaveIn")));
                overHead.setAmountSaveIn(DmShared.xNull(rsTemp.getString("amountSaveIn")));
                overHead.setTableName(DmShared.xNull(rsTemp.getString("tableName")));
                overHead.setAtRateEditable(DmShared.xNull(rsTemp.getString("isatRateEditable")));
                overHead.setAmountEditable(DmShared.xNull(rsTemp.getString("isamountEditable")));
                overHead.setAcEditable(DmShared.xNull(rsTemp.getString("isacEditable")));
                overHead.setPrintable(DmShared.xNull(rsTemp.getString("isPrintable")));
                overHead.setCalLValOn(DmShared.xNull(rsTemp.getString("calLValOn")));
                overHead.setShowFormulaValue(DmShared.xNull(rsTemp.getString("isshowFormulaValue")));

                overHead.setContraOhMCode(DmShared.xNull(rsTemp.getString("contraOhMCode")));
                overHead.setDrCr(DmShared.xNull(rsTemp.getString("drCr")));
                overHead.setFormula(DmShared.xNull(rsTemp.getString("formula")));
                overHead.setActualFormula(DmShared.xNull(rsTemp.getString("actualFormula")));
                overHead.setExcludeFormula(DmShared.xNull(rsTemp.getString("excludeFormula")));
                overHead.setNature(DmShared.xNull(rsTemp.getString("nature")));
                overHead.setIsPartialPosting(DmShared.xNull(rsTemp.getString("isPartialPosting")));

                if (!DmShared.xNull(docId).trim().equalsIgnoreCase("")) {
                    sqlQuery = "Select ma.SubCode As SubCode,SG.Name As AcName,ma.Amount "
                            + "From " + overHeadTableNameMA + " ma  "
                            + "Left Join SubGroup SG On SG.SubCode=ma.SubCode "
                            + "Where ma.DocId = :docId And "
                            + "ma.vSNo = :vSNo "
                            + "Order By ma.SNo";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("docid", docId);
                    psTemp1.setString("vSNo", rsTemp.getString("sNo"));
                    rsTemp1 = psTemp1.executeQuery();
                    overHead.setOverHeadMA(new ArrayList<>());
                    while (rsTemp1.next()) {
                        overHeadMA = new OverHeadMA_Mdl();
                        overHeadMA.setSharedKeyFieldCode(DmShared.xNull(rsTemp1.getString("SubCode")));
                        overHeadMA.setSharedKeyFieldName(DmShared.xNull(rsTemp1.getString("AcName")));
                        overHeadMA.setAmount(DmShared.xNull(rsTemp1.getString("Amount")));
                        overHead.getOverHeadMA().add(overHeadMA);
                    }
                    rsTemp1.close();
                    psTemp1.close();
                } else {
                    overHead.setOverHeadMA(new ArrayList<>());
                    overHeadMA = new OverHeadMA_Mdl();
                    overHeadMA.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                    overHeadMA.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("AcName")));
                    overHead.getOverHeadMA().add(overHeadMA);

                }

                overHeadList.add(overHead);
            }

            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (rsTemp1 != null) {
                    rsTemp1.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (psTemp1 != null) {
                    psTemp1.close();
                }
            } catch (Exception e) {
            }
        }

        return overHeadList;
    }

    public static void cancel(String keyFieldValue, Connection cnTemp) {
        cancel(keyFieldValue, cnTemp, "ledger", "overHead", "overHeadMA");
    }

    public static void cancel(String keyFieldValue, Connection cnTemp, String ledgerTableName, String overHeadTableName, String overHeadTableNameMA) {
        DmPreparedStatement psTemp = null;

        try {
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "delete from " + ledgerTableName + " where docId = :docId;");
            psTemp.setString("docId", keyFieldValue);
            psTemp.executeUpdate();
            psTemp.close();

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "update " + overHeadTableNameMA + " set isCancelled = 1 where docId = :docId;");
            psTemp.setString("docId", keyFieldValue);
            psTemp.executeUpdate();
            psTemp.close();

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "update " + overHeadTableName + " set isCancelled = 1 where docId = :docId;");
            psTemp.setString("docId", keyFieldValue);
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {

            }
        }

    }

    public static void delete(String keyFieldValue, Connection cnTemp) {
        delete(keyFieldValue, cnTemp, "ledger", "overHead", "overHeadMA", true);
    }

    public static void delete(String keyFieldValue, Connection cnTemp, String ledgerTableName, String overHeadTableName, String overHeadTableNameMA, boolean isPostingNeeded) {
        DmPreparedStatement psTemp = null;

        try {
            if (isPostingNeeded) {
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, "delete from " + ledgerTableName + " where docId = :docId;");
                psTemp.setString("docId", keyFieldValue);
                psTemp.executeUpdate();
                psTemp.close();
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "delete from " + overHeadTableNameMA + " where docId = :docId;");
            psTemp.setString("docId", keyFieldValue);
            psTemp.executeUpdate();
            psTemp.close();

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "delete from " + overHeadTableName + " where docId = :docId;");
            psTemp.setString("docId", keyFieldValue);
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {

            }
        }

    }

    public static void save(List<OverHead_Mdl> overHeadList, String keyFieldName, String keyFieldValue, RecId recId,
            String vType, String vPrefix, String siteCode, String vDate, String commonNarration, String refNo, String refDate,
            String chequeNo, String chequeDate, boolean isPosted, boolean isPostingNeeded, Connection cnTemp) {

        save(overHeadList, keyFieldName, keyFieldValue, recId, vType, vPrefix, siteCode, vDate, commonNarration, refNo, refDate, chequeNo, chequeDate, isPosted, isPostingNeeded, cnTemp, "ledger", "overHead", "overHeadMA");
    }

    public static void save(List<OverHead_Mdl> overHeadList, String keyFieldName, String keyFieldValue, RecId recId,
            String vType, String vPrefix, String siteCode, String vDate, String commonNarration, String refNo, String refDate,
            String chequeNo, String chequeDate, boolean isPosted, boolean isPostingNeeded, Connection cnTemp,
            String ledgerTableName, String overHeadTableName, String overHeadTableNameMA) {

        DmPreparedStatement psTemp = null;
        String sqlQuery, tableName = "", contraText, contraAcCode;
        String fieldAddition = "CMN_Addition", fieldDeduction = "CMN_Deduction";
        double addition = 0, deduction = 0;
        String credit, debit;
        List<OverHeadPost_Mdl> overHeadPostList = new ArrayList<>();
        OverHead_Mdl overHead;

        try {
            if (DmShared.xNull(keyFieldValue).replace("0", "").trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Invalid key field.");
            }

            if (overHeadList != null) {
                delete(keyFieldValue, cnTemp, ledgerTableName, overHeadTableName, overHeadTableNameMA, isPostingNeeded);
                for (int i = 0; i < overHeadList.size(); i++) {
                    tableName = DmShared.xNull(overHeadList.get(i).getTableName());

                    sqlQuery = "INSERT INTO " + overHeadTableName + " "
                            + "(docId,sno,overHeadCode,overHeadMCode,atRate,amount, " //subCode,
                            + "contraOhMCode,nature,formula,actualFormula,excludeFormula,drCr,atRateSaveIn,amountSaveIn, "
                            + "tableName,isatRateEditable,isamountEditable,isacEditable,isshowFormulaValue, "
                            + "roundOff,ohSetUp,UniqueId,isatRateMandatory,isamountMandatory, "
                            + "site_Code,overheadAlias,isoverHeadAliasEditable,isPrintable, calLValOn,isPartialPosting) "
                            + "VALUES "
                            + "(:docId,:sno,:overHeadCode,:overHeadMCode,:atRate,:amount, "
                            + ":contraOhMCode,:nature,:formula,:actualFormula,:excludeFormula,:drCr,:atRateSaveIn, " //:subCode,
                            + ":amountSaveIn,:tableName,:atRateEditable,:amountEditable,:acEditable, "
                            + ":showFormulaValue,:roundOff,:ohSetUp,:UniqueId, "
                            + ":atRateMandatory,:amountMandatory,:site_Code, "
                            + ":overheadAlias,:overHeadAliasEditable,:isPrintable, :calLValOn,:isPartialPosting); ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", keyFieldValue);
                    psTemp.setInt("sno", i + 1);
                    psTemp.setString("overHeadCode", overHeadList.get(i).getSharedKeyFieldCode());
                    psTemp.setString("overHeadMCode", overHeadList.get(i).getOhManualCode());
                    psTemp.setString("atRate", DmShared.foriegnKey(overHeadList.get(i).getAtRate()));
                    psTemp.setString("amount", DmShared.foriegnKey(overHeadList.get(i).getAmount()));
//                    psTemp.setString("subCode", DmShared.foriegnKey(overHeadList.get(i).getAccountCode()));
                    psTemp.setString("contraOhMCode", DmShared.foriegnKey(overHeadList.get(i).getContraOhMCode()));
                    psTemp.setString("nature", DmShared.foriegnKey(overHeadList.get(i).getNature()));
                    psTemp.setString("formula", DmShared.foriegnKey(overHeadList.get(i).getFormula()));
                    psTemp.setString("actualFormula", DmShared.foriegnKey(overHeadList.get(i).getActualFormula()));
                    psTemp.setString("excludeFormula", DmShared.foriegnKey(overHeadList.get(i).getExcludeFormula()));
                    psTemp.setString("drCr", DmShared.foriegnKey(overHeadList.get(i).getDrCr()));
                    psTemp.setString("atRateSaveIn", DmShared.foriegnKey(overHeadList.get(i).getAtRateSaveIn()));
                    psTemp.setString("amountSaveIn", DmShared.foriegnKey(overHeadList.get(i).getAmountSaveIn()));
                    psTemp.setString("tableName", overHeadList.get(i).getTableName());
                    psTemp.setString("atRateEditable", DmShared.vNull(overHeadList.get(i).getAtRateEditable()) == 1.0 ? "1" : "0");
                    psTemp.setString("amountEditable", DmShared.vNull(overHeadList.get(i).getAmountEditable()) == 1.0 ? "1" : "0");
                    psTemp.setString("acEditable", DmShared.vNull(overHeadList.get(i).getAcEditable()) == 1.0 ? "1" : "0");
                    psTemp.setString("showFormulaValue", DmShared.vNull(overHeadList.get(i).getShowFormulaValue()) == 1.0 ? "1" : "0");
                    psTemp.setString("roundOff", DmShared.foriegnKey(overHeadList.get(i).getRoundOff()));
                    psTemp.setString("ohSetUp", DmShared.foriegnKey(overHeadList.get(i).getOHSetUp()));
                    psTemp.setString("UniqueId", DmShared.foriegnKey(overHeadList.get(i).getUniqueId()));
                    psTemp.setString("atRateMandatory", DmShared.vNull(overHeadList.get(i).getAtRateMandatory()) == 1.0 ? "1" : "0");
                    psTemp.setString("amountMandatory", DmShared.vNull(overHeadList.get(i).getAmountMandatory()) == 1.0 ? "1" : "0");
                    psTemp.setString("site_Code", siteCode);
                    psTemp.setString("overheadAlias", (DmShared.xNull(overHeadList.get(i).getOverHeadAlias()).trim().equalsIgnoreCase(DmShared.xNull(overHeadList.get(i).getSharedKeyFieldName()).trim()) ? null : overHeadList.get(i).getOverHeadAlias()));
                    psTemp.setString("overHeadAliasEditable", DmShared.vNull(overHeadList.get(i).getOverHeadAliasEditable()) == 1.0 ? "1" : "0");
                    psTemp.setString("isPrintable", DmShared.vNull(overHeadList.get(i).getPrintable()) == 1.0 ? "1" : "0");
                    psTemp.setString("calLValOn", DmShared.vNull(overHeadList.get(i).getCalLValOn()) == 1.0 ? "1" : "0");
                    psTemp.setString("isPartialPosting", DmShared.vNull(overHeadList.get(i).getIsPartialPosting()) == 1.0 ? "1" : "0");
                    psTemp.executeUpdate();
                    psTemp.close();

                    if (overHeadList.get(i).getOverHeadMA() != null) {
                        for (int j = 0; j < overHeadList.get(i).getOverHeadMA().size(); j++) {
                            if (!DmShared.xNull(overHeadList.get(i).getOverHeadMA().get(j).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                                sqlQuery = "INSERT INTO " + overHeadTableNameMA + " "
                                        + "(docId,vSNo,sNo,amount,subCode) "
                                        + "VALUES "
                                        + "(:docId,:vSNo,:sNo,:amount,:subCode); ";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("docId", keyFieldValue);
                                psTemp.setInt("vSNo", i + 1);
                                psTemp.setInt("sno", j + 1);
                                psTemp.setString("amount", DmShared.foriegnKey(overHeadList.get(i).getOverHeadMA().get(j).getAmount()));
                                psTemp.setString("subCode", overHeadList.get(i).getOverHeadMA().get(j).getSharedKeyFieldCode());
                                psTemp.executeUpdate();
                                psTemp.close();
                            }
                        }
                    }

                    //Updating Particular Fields Start
                    sqlQuery = "update " + tableName + " set ";
                    if (!DmShared.xNull(overHeadList.get(i).getAtRateSaveIn()).trim().equalsIgnoreCase("")) {
                        sqlQuery += overHeadList.get(i).getAtRateSaveIn() + " = '" + DmShared.vNull(overHeadList.get(i).getAtRate()) + "' ";
                    }

                    if (!DmShared.xNull(overHeadList.get(i).getAmountSaveIn()).trim().equalsIgnoreCase("")) {
                        if (!DmShared.xNull(overHeadList.get(i).getAtRateSaveIn()).trim().equalsIgnoreCase("")) {
                            sqlQuery += ", ";
                        }
                        sqlQuery += overHeadList.get(i).getAmountSaveIn() + " = '" + DmShared.vNull(overHeadList.get(i).getAmount()) + "' ";
                    } else {
                        if (DmShared.xNull(overHeadList.get(i).getNature()).trim().equalsIgnoreCase("A")) {
                            addition += DmShared.vNull(overHeadList.get(i).getAmount());
                        } else if (DmShared.xNull(overHeadList.get(i).getNature()).trim().equalsIgnoreCase("D")) {
                            deduction += DmShared.vNull(overHeadList.get(i).getAmount());
                        }
                    }

                    sqlQuery += "where " + keyFieldName + " = '" + keyFieldValue + "' ";
                    if ((!DmShared.xNull(overHeadList.get(i).getAtRateSaveIn()).trim().equalsIgnoreCase(""))
                            || (!DmShared.xNull(overHeadList.get(i).getAmountSaveIn()).trim().equalsIgnoreCase(""))) {
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                    //Updating Particular Fields End 

                    //Ledger Posting Start
                    if (isPostingNeeded) {
                        if (overHeadList.get(i).getOverHeadMA() != null) {
                            if (!DmShared.xNull(overHeadList.get(i).getDrCr()).trim().equalsIgnoreCase("")) {

                                contraAcCode = "";
                                contraText = "";
                                overHead = OverHead_Shared.getOHByManualCode(overHeadList.get(i).getContraOhMCode(), overHeadList);
                                if (overHead != null) {
                                    if (overHead.getOverHeadMA() != null) {
                                        for (int j = 0; j < overHead.getOverHeadMA().size(); j++) {
                                            if (!DmShared.xNull(overHead.getOverHeadMA().get(j).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                                                if (!DmShared.xNull(contraAcCode).trim().equalsIgnoreCase("")) {
                                                    contraAcCode = "";
                                                    contraText = "";
                                                    break;
                                                }
                                                contraAcCode = overHead.getOverHeadMA().get(j).getSharedKeyFieldCode();
                                                contraText = overHead.getOverHeadMA().get(j).getSharedKeyFieldName();
                                            }
                                        }
                                    }
                                }

                                for (int j = 0; j < overHeadList.get(i).getOverHeadMA().size(); j++) {
                                    if (!DmShared.xNull(overHeadList.get(i).getOverHeadMA().get(j).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                                        prepareSQLLedger(overHeadPostList, overHeadList.get(i), contraAcCode, contraText, overHeadList.get(i).getOverHeadMA().get(j).getSharedKeyFieldCode(), overHeadList.get(i).getOverHeadMA().get(j).getAmount());
                                    }
                                }
                            }
                        }
                    }
                    //Ledger Posting End
                }

                if (!DmShared.xNull(tableName).trim().equalsIgnoreCase("")) {
                    if ((!DmShared.xNull(fieldAddition).trim().equalsIgnoreCase("")) || (!DmShared.xNull(fieldDeduction).trim().equalsIgnoreCase(""))) {
                        sqlQuery = "update " + tableName + " set ";
                        if (!DmShared.xNull(fieldAddition).trim().equalsIgnoreCase("")) {
                            sqlQuery += fieldAddition + " = '" + DmShared.vNull(addition) + "' ";
                        }

                        if (!DmShared.xNull(fieldDeduction).trim().equalsIgnoreCase("")) {
                            if (!DmShared.xNull(fieldAddition).trim().equalsIgnoreCase("")) {
                                sqlQuery += ", ";
                            }
                            sqlQuery += fieldDeduction + " = '" + DmShared.vNull(deduction) + "' ";
                        }

                        sqlQuery += "where " + keyFieldName + " = '" + keyFieldValue + "' ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }

                //Ledger Posting Start
                for (int i = 0; i < overHeadPostList.size(); i++) {
                    credit = "";
                    debit = "";
                    contraText = "";

                    if (DmShared.vNull(overHeadPostList.get(i).getAmount()) > 0) {
                        debit = overHeadPostList.get(i).getAmount();
                        if (!DmShared.xNull(overHeadPostList.get(i).getContraText()).trim().equalsIgnoreCase("")) {
                            contraText = DmServer.prepareContraText(overHeadPostList.get(i).getContraText(), DmShared.vNull(debit), "Cr");
                        }
                    } else if (DmShared.vNull(overHeadPostList.get(i).getAmount()) < 0) {
                        credit = String.valueOf(Math.abs(DmShared.vNull(overHeadPostList.get(i).getAmount())));
                        if (!DmShared.xNull(overHeadPostList.get(i).getContraText()).trim().equalsIgnoreCase("")) {
                            contraText = DmServer.prepareContraText(overHeadPostList.get(i).getContraText(), DmShared.vNull(credit), "Dr");
                        }
                    }

                    commonNarration = DmShared.xNull(commonNarration).trim();
                    if (!commonNarration.equalsIgnoreCase("")) {
                        commonNarration = commonNarration.substring(1, commonNarration.length());
                    }

                    sqlQuery = "Insert Into " + ledgerTableName + "(DocId,Site_Code,V_No,RecIdPrefix,RecId,V_Sno,Narration,V_Type,V_Prefix, "
                            + "SubCode,ContraSub,ContraText,AmtDr,AmtCr,V_Date,RefNo,RefDate,Chq_No,Chq_Date,isPosted,isPartialPosting) Values("
                            + ":DocId,:Site_Code,:V_No,:RecIdPrefix,:RecId,:V_Sno,:Narration,:V_Type,:V_Prefix, "
                            + ":SubCode,:ContraSub,:ContraText,:AmtDr,:AmtCr,:V_Date,:RefNo,:RefDate,:Chq_No,:Chq_Date,:isPosted,:isPartialPosting) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", keyFieldValue);
                    psTemp.setString("Site_Code", siteCode);
                    psTemp.setString("RecId", recId.getRecIdValue());
                    psTemp.setString("recIdPrefix", DmShared.xNull(recId.getPrefix()));
                    psTemp.setLong("V_No", DmShared.iNull(recId.getCounter()).longValue());
                    psTemp.setInt("V_Sno", i + 1);
                    psTemp.setString("Narration", DmShared.foriegnKey(commonNarration));
                    psTemp.setString("V_Type", vType);
                    psTemp.setString("V_Prefix", vPrefix);
                    psTemp.setString("SubCode", overHeadPostList.get(i).getSharedKeyFieldCode());
                    psTemp.setString("ContraSub", DmShared.foriegnKey(overHeadPostList.get(i).getContraCode()));
                    psTemp.setString("ContraText", DmShared.foriegnKey(contraText));
                    psTemp.setString("AmtDr", DmShared.foriegnKey(debit));
                    psTemp.setString("AmtCr", DmShared.foriegnKey(credit));
                    psTemp.setDate("V_Date", DmServer.convertDate(vDate));
                    psTemp.setString("RefNo", DmShared.foriegnKey(refNo));
                    psTemp.setDate("RefDate", DmServer.convertDate(refDate));
                    psTemp.setString("Chq_No", DmShared.foriegnKey(chequeNo));
                    psTemp.setDate("Chq_Date", DmServer.convertDate(chequeDate));
                    
                    if (DmShared.vNull(overHeadPostList.get(i).getIsPartialPosting()) == 1.0){
                        psTemp.setString("isPosted", "1");
                    }else {
                        psTemp.setString("isPosted", isPosted ? "1" : "0");
                    }
                    
                    psTemp.setString("isPartialPosting", DmShared.xNull(overHeadPostList.get(i).getIsPartialPosting()).trim());

                    psTemp.executeUpdate();
                    psTemp.close();
                }
                //Ledger Posting End
            }

        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {

            }
        }

    }

    private static void prepareSQLLedger(List<OverHeadPost_Mdl> overHeadPostList, OverHead_Mdl overHead, String contraAc, String contraText, String acCode, String amount) {
        OverHeadPost_Mdl overHeadPost = null;

        if (overHead != null && overHeadPostList != null) {
            if (!DmShared.xNull(overHead.getDrCr()).trim().equalsIgnoreCase("")) {
                for (int i = 0; i < overHeadPostList.size(); i++) {
                    if (DmShared.xNull(overHeadPostList.get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(acCode).trim())
                            && DmShared.xNull(overHeadPostList.get(i).getContraCode()).trim().equalsIgnoreCase(DmShared.xNull(contraAc).trim())) {
                        overHeadPost = overHeadPostList.get(i);
                    }
                }

                if (overHeadPost == null) {
                    overHeadPost = new OverHeadPost_Mdl();
                    overHeadPostList.add(overHeadPost);
                }

                overHeadPost.setSharedKeyFieldCode(acCode);
                overHeadPost.setContraCode(contraAc);
                overHeadPost.setContraText(contraText);
                overHeadPost.setIsPartialPosting(DmShared.xNull(overHead.getIsPartialPosting()));

                if (DmShared.xNull(overHead.getDrCr()).trim().equalsIgnoreCase("D")
                        && DmShared.vNull(amount) > 0) {
                    overHeadPost.setAmount(String.valueOf(DmShared.vNull(amount) + DmShared.vNull(overHeadPost.getAmount())));
                } else if (DmShared.xNull(overHead.getDrCr()).trim().equalsIgnoreCase("C")
                        && DmShared.vNull(amount) > 0) {
                    overHeadPost.setAmount(String.valueOf(DmShared.vNull(overHeadPost.getAmount()) - DmShared.vNull(amount)));
                } else if (DmShared.xNull(overHead.getDrCr()).trim().equalsIgnoreCase("D")
                        && DmShared.vNull(amount) < 0) {
                    overHeadPost.setAmount(String.valueOf(DmShared.vNull(overHeadPost.getAmount()) - Math.abs(DmShared.vNull(amount))));
                } else if (DmShared.xNull(overHead.getDrCr()).trim().equalsIgnoreCase("C")
                        && DmShared.vNull(amount) < 0) {
                    overHeadPost.setAmount(String.valueOf(Math.abs(DmShared.vNull(amount)) + DmShared.vNull(overHeadPost.getAmount())));
                }

                if (DmShared.vNull(overHeadPost.getAmount()) == 0) {
                    overHeadPostList.remove(overHeadPost);
                }
            }
        }
    }
}
