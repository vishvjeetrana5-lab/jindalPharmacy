package dataman.dmToolkit.overHeadLine.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHeadLine.shared.OHLExtended;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.DmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OverHeadLine_Server {

    public static List<OverHeadITS_Mdl> getITS(String itsCode, String locationCode, String siteCode, String applicableDate, Connection cnTemp) {
        List<OverHeadITS_Mdl> overHeadITSList = new ArrayList<>();
        OverHeadITS_Mdl overHeadITS;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            if (DmShared.iNull(itsCode).longValue() != 0) {
                sqlQuery = "select	itsd.overHeadCode,sg.subCode,sg.name as sgName,itsd.atRate,itsd.amount,ohm.manualCode, sg.manualCode AS sgManualCode "
                        + "from itsDetail itsd "
                        + "left join subgroup sg on itsd.subCode = sg.subCode "
                        + "left join overHeadMast ohm on itsd.overHeadCode = ohm.code "
                        + "where itsd.code = :itsCode and "
                        + "itsd.locationCode = :locationCode and "
                        + "isNull(itsd.site_Code,:siteCode) = :siteCode and "
                        + ":applicableDate between itsd.applicableFrom and itsd.applicableTo ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itsCode", DmShared.iNull(itsCode).toString());
                psTemp.setString("locationCode", DmShared.iNull(locationCode).toString());
                psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
                psTemp.setDate("applicableDate", DmServer.convertDate(applicableDate));
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    overHeadITS = new OverHeadITS_Mdl();
                    overHeadITS.setOverHeadManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                    overHeadITS.setOverHeadCode(DmShared.xNull(rsTemp.getString("overHeadCode")));
                    overHeadITS.setAmount(DmServer.vNull(rsTemp.getString("amount"), "#.#######"));
                    overHeadITS.setAtRate(DmServer.vNull(rsTemp.getString("atRate"), "#.#######"));
                    overHeadITS.setAccountCode(DmShared.xNull(rsTemp.getString("subcode")));
                    overHeadITS.setAccountName(DmShared.xNull(rsTemp.getString("sgName")));
                    overHeadITS.setAccountManualCode(DmShared.xNull(rsTemp.getString("sgManualCode")));

                    overHeadITSList.add(overHeadITS);
                }
                psTemp.close();
                rsTemp.close();
            }

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

        return overHeadITSList;
    }

    public static List<OverHeadLine_Mdl> getOverHead(String vType, String vDate, String siteCode, String docId, String v_SNo, Connection cnTemp) {
        return getOverHead(vType, vDate, siteCode, docId, v_SNo, "overHeadLine", cnTemp);
    }

    public static List<OverHeadLine_Mdl> getOverHead(String vType, String vDate, String siteCode, String docId, String v_SNo, String overHeadTableName, Connection cnTemp) {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery;
        List<OverHeadLine_Mdl> overHeadList = new ArrayList<>();
        OverHeadLine_Mdl overHead;

        try {
            if (DmShared.xNull(docId).trim().equalsIgnoreCase("")) {
                sqlQuery = "Select	ohc.overHeadCode,ohc.subcode,ohc.atRate,ohc.nature,ohc.atRateSaveIn, "
                        + "ohc.amountSaveIn,ohc.tableName,ohc.isatRateEditable,ohc.isamountEditable,ohc.isacEditable,  "
                        + "ohc.isshowFormulaValue,ohc.roundOff,ohc.UniqueId, ohm.name as ohName,  "
                        + "ohm.manualCode As ohManualCode, SG.Name As ACName,0 As Amount,ohc.formula, ohc.isamountMandatory,  "
                        + "ohc.isatRateMandatory, Null As overheadAlias,ohc.isoverHeadAliasEditable,ohc.isPrintable   "
                        + "From   "
                        + "( "
                        + "select	ohc.*  "
                        + "from overHeadLineConfig ohc,  "
                        + "(  "
                        + "select	top 1 ohcs.code,ohcs.site_code   "
                        + "from overHeadLineConfigSite ohcs,  "
                        + "(  "
                        + "select	ohcn.code   "
                        + "from overHeadLineConfigName ohcn   "
                        + "where ohcn.v_Type = :vType  "
                        + ") as ohcn   "
                        + "where ohcs.code = ohcn.code and  isnull(ohcs.site_Code,:siteCode) = :siteCode  "
                        + "order by ohcs.site_Code desc  "
                        + ") as ohcs  "
                        + "where ohcs.code = ohc.code and (:vDate between applicableFrom and applicableTo)   "
                        + ") as ohc  "
                        + "Left Join overHeadMast ohm On ohc.overHeadCode = ohm.Code   "
                        + "Left Join SubGroup SG On SG.SubCode=ohc.subcode "
                        + "Order By ohc.SNo ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("vType", vType);
                psTemp.setString("siteCode", siteCode);
                psTemp.setDate("vDate", DmServer.convertDate(vDate));
            } else {

                sqlQuery = "Select oh.overHeadCode,oh.subcode,oh.amount,oh.atRate, "
                        + "oh.nature,oh.sNo, "
                        + "oh.atRateSaveIn,oh.amountSaveIn,oh.tableName,oh.isatRateEditable,oh.isamountEditable,oh.isacEditable,  "
                        + "oh.isshowFormulaValue,oh.roundOff,oh.UniqueId, "
                        + "ohm.Name ohName,oh.overHeadMCode As ohManualCode,SG.Name As ACName,oh.formula,  "
                        + "oh.isamountMandatory,oh.isatRateMandatory,  "
                        + "oh.overheadAlias,oh.isoverHeadAliasEditable,oh.isPrintable  "
                        + "From  " + overHeadTableName + "  oh  "
                        + "Left Join overHeadMast ohm On oh.overHeadCode=ohm.Code  "
                        + "Left Join SubGroup SG On SG.SubCode=oh.subcode "
                        + "Where oh.DocId = :docid and oh.vSNo = :v_SNo  "
                        + "Order By oh.SNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", DmShared.iNull(docId).toString());
                psTemp.setString("v_SNo", DmShared.iNull(v_SNo).toString());

            }
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                overHead = new OverHeadLine_Mdl();
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
                overHead.setAtRateSaveIn(DmShared.xNull(rsTemp.getString("atRateSaveIn")));
                overHead.setAmountSaveIn(DmShared.xNull(rsTemp.getString("amountSaveIn")));
                overHead.setTableName(DmShared.xNull(rsTemp.getString("tableName")));
                overHead.setAtRateEditable(DmShared.xNull(rsTemp.getString("isatRateEditable")));
                overHead.setAmountEditable(DmShared.xNull(rsTemp.getString("isamountEditable")));
                overHead.setAcEditable(DmShared.xNull(rsTemp.getString("isacEditable")));
                overHead.setPrintable(DmShared.xNull(rsTemp.getString("isPrintable")));
                overHead.setShowFormulaValue(DmShared.xNull(rsTemp.getString("isshowFormulaValue")));

                overHead.setAccountCode(DmShared.xNull(rsTemp.getString("subcode")));
                overHead.setAccountName(DmShared.xNull(rsTemp.getString("acName")));

                overHead.setFormula(DmShared.xNull(rsTemp.getString("formula")));
                overHead.setNature(DmShared.xNull(rsTemp.getString("nature")));

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

//    public static void setReference(List<OverHeadLine_Mdl> overHeadLineList, String refDocId, String v_SNo, Connection cnTemp) {
//
//        setReference(overHeadLineList, refDocId, v_SNo, "overHeadLine", cnTemp);
//    }
//
//    public static void setReference(List<OverHeadLine_Mdl> overHeadLineList, String refDocId, String v_SNo, String overHeadLineTableName, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery;
//
//        try {
//            if (overHeadLineList != null) {
//                for (int i = 0; i < overHeadLineList.size(); i++) {
//                    sqlQuery = "select ohl.atRate,ohl.amount "
//                            + "from " + overHeadLineTableName + " ohl  "
//                            + "where ohl.docId = :docId and ohl.vSNo = :vSNo and "
//                            + "ohl.overHeadCode = :overHeadCode ";
//                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                    psTemp.setString("docid", DmShared.iNull(refDocId).toString());
//                    psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
//                    psTemp.setString("overHeadCode", DmShared.iNull(overHeadLineList.get(i).getSharedKeyFieldCode()).toString());
//                    rsTemp = psTemp.executeQuery();
//                    if (rsTemp.next()) {
//                        overHeadLineList.get(i).setAmount(DmServer.vNull(rsTemp.getString("Amount"), "0.#######"));
//                        overHeadLineList.get(i).setAtRate(DmServer.vNull(rsTemp.getString("atRate"), "0.#######"));
//                    }
//                    rsTemp.close();
//                    psTemp.close();
//                }
//            }
//
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//    }
    public static void cancel(String keyFieldValue, Connection cnTemp) {
        cancel(keyFieldValue, cnTemp, "overHeadLine");
    }

    public static void cancel(String keyFieldValue, Connection cnTemp, String overHeadTableName) {
        DmPreparedStatement psTemp = null;

        try {

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
        delete(keyFieldValue, cnTemp, "overHeadLine");
    }

    public static void delete(String keyFieldValue, Connection cnTemp, String overHeadTableName) {
        DmPreparedStatement psTemp = null;

        try {

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

    public static void save(List<? extends OHLExtended> overHeadList, String keyFieldName, String keyFieldValue,
            String keyFieldV_SNoName, String siteCode, Connection cnTemp) {

        save(overHeadList, keyFieldName, keyFieldValue, keyFieldV_SNoName, siteCode, cnTemp, "overHeadLine");
    }

    public static void save(List<? extends OHLExtended> rowList, String keyFieldName, String keyFieldValue,
            String keyFieldV_SNoName, String siteCode, Connection cnTemp, String overHeadTableName) {

        DmPreparedStatement psTemp = null;
        String sqlQuery, sqlUpdateSet, sqlUpdate, sqlUpdateWhere, tableName = "";
        List<OverHeadLine_Mdl> overHeadLineList;
        OHLExtended ohl;

        try {
            if (DmShared.iNull(keyFieldValue).longValue() == 0) {
                throw new DmException(0, "Invalid key field.");
            }

            delete(keyFieldValue, cnTemp, overHeadTableName);

            if (rowList != null) {
                for (int r = 0; r < rowList.size(); r++) {
                    ohl = rowList.get(r);
                    sqlUpdateSet = "";
                    sqlUpdate = "";
                    sqlUpdateWhere = "";
                    if (ohl != null) {
                        overHeadLineList = ohl.getOHLList();
                        if (overHeadLineList != null) {
                            for (int i = 0; i < overHeadLineList.size(); i++) {
                                if (tableName.equalsIgnoreCase("")) {
                                    tableName = DmShared.xNull(overHeadLineList.get(i).getTableName());
                                }

                                sqlQuery = "INSERT INTO " + overHeadTableName + " "
                                        + "(docId,vSNo,sno,overHeadCode,overHeadMCode,atRate,amount,subCode, "
                                        + "nature,formula,atRateSaveIn,amountSaveIn, "
                                        + "tableName,isatRateEditable,isamountEditable,isacEditable,isshowFormulaValue, "
                                        + "roundOff,uniqueId,isatRateMandatory,isamountMandatory, "
                                        + "site_Code,overheadAlias,isoverHeadAliasEditable,isPrintable) "
                                        + "VALUES "
                                        + "(:docId,:vSNo,:sno,:overHeadCode,:overHeadMCode,:atRate,:amount,:subCode, "
                                        + ":nature,:formula,:atRateSaveIn, "
                                        + ":amountSaveIn,:tableName,:atRateEditable,:amountEditable,:acEditable, "
                                        + ":showFormulaValue,:roundOff,:uniqueId, "
                                        + ":atRateMandatory,:amountMandatory,:site_Code, "
                                        + ":overheadAlias,:overHeadAliasEditable,:isPrintable); ";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("docId", keyFieldValue);
                                psTemp.setInt("vSNo", r + 1);
                                psTemp.setInt("sNo", i + 1);
                                psTemp.setString("overHeadCode", overHeadLineList.get(i).getSharedKeyFieldCode());
                                psTemp.setString("overHeadMCode", overHeadLineList.get(i).getOhManualCode());
                                psTemp.setString("atRate", DmShared.foriegnKey(overHeadLineList.get(i).getAtRate()));
                                psTemp.setString("amount", DmShared.foriegnKey(overHeadLineList.get(i).getAmount()));
                                psTemp.setString("subCode", DmShared.foriegnKey(overHeadLineList.get(i).getAccountCode()));
//                                psTemp.setString("contraOhMCode", DmShared.foriegnKey(overHeadList.get(i).getContraOhMCode()));
                                psTemp.setString("nature", DmShared.foriegnKey(overHeadLineList.get(i).getNature()));
                                psTemp.setString("formula", DmShared.foriegnKey(overHeadLineList.get(i).getFormula()));
//                                psTemp.setString("drCr", DmShared.foriegnKey(overHeadList.get(i).getDrCr()));
                                psTemp.setString("atRateSaveIn", DmShared.foriegnKey(overHeadLineList.get(i).getAtRateSaveIn()));
                                psTemp.setString("amountSaveIn", DmShared.foriegnKey(overHeadLineList.get(i).getAmountSaveIn()));
                                psTemp.setString("tableName", overHeadLineList.get(i).getTableName());
                                psTemp.setString("atRateEditable", DmShared.vNull(overHeadLineList.get(i).getAtRateEditable()) == 1.0 ? "1" : "0");
                                psTemp.setString("amountEditable", DmShared.vNull(overHeadLineList.get(i).getAmountEditable()) == 1.0 ? "1" : "0");
                                psTemp.setString("acEditable", DmShared.vNull(overHeadLineList.get(i).getAcEditable()) == 1.0 ? "1" : "0");
                                psTemp.setString("showFormulaValue", DmShared.vNull(overHeadLineList.get(i).getShowFormulaValue()) == 1.0 ? "1" : "0");
                                psTemp.setString("roundOff", DmShared.foriegnKey(overHeadLineList.get(i).getRoundOff()));
//                                psTemp.setString("ohSetUp", DmShared.foriegnKey(overHeadList.get(i).getOHSetUp()));
                                psTemp.setString("uniqueId", DmShared.foriegnKey(overHeadLineList.get(i).getUniqueId()));
                                psTemp.setString("atRateMandatory", DmShared.vNull(overHeadLineList.get(i).getAtRateMandatory()) == 1.0 ? "1" : "0");
                                psTemp.setString("amountMandatory", DmShared.vNull(overHeadLineList.get(i).getAmountMandatory()) == 1.0 ? "1" : "0");
                                psTemp.setString("site_Code", siteCode);
                                psTemp.setString("overheadAlias", (DmShared.xNull(overHeadLineList.get(i).getOverHeadAlias()).trim().equalsIgnoreCase(DmShared.xNull(overHeadLineList.get(i).getSharedKeyFieldName()).trim()) ? null : overHeadLineList.get(i).getOverHeadAlias()));
                                psTemp.setString("overHeadAliasEditable", DmShared.vNull(overHeadLineList.get(i).getOverHeadAliasEditable()) == 1.0 ? "1" : "0");
                                psTemp.setString("isPrintable", DmShared.vNull(overHeadLineList.get(i).getPrintable()) == 1.0 ? "1" : "0");
                                psTemp.executeUpdate();
                                psTemp.close();

                                if (sqlUpdateSet.equalsIgnoreCase("")) {
                                    sqlUpdateSet = "update " + tableName + " set ";
                                }

                                if (sqlUpdateWhere.equalsIgnoreCase("")) {
                                    sqlUpdateWhere = "where " + keyFieldName + " = " + DmShared.iNull(keyFieldValue).toString() + " and "
                                            + keyFieldV_SNoName + " = " + String.valueOf(r + 1) + " ";
                                }

                                if (!DmShared.xNull(overHeadLineList.get(i).getAtRateSaveIn()).trim().equalsIgnoreCase("")) {
                                    if (!DmShared.xNull(sqlUpdate).trim().equalsIgnoreCase("")) {
                                        sqlUpdate += ", ";
                                    }
                                    sqlUpdate += overHeadLineList.get(i).getAtRateSaveIn() + " = '" + DmShared.vNull(overHeadLineList.get(i).getAtRate()) + "' ";
                                }

                                if (!DmShared.xNull(overHeadLineList.get(i).getAmountSaveIn()).trim().equalsIgnoreCase("")) {
                                    if (!DmShared.xNull(sqlUpdate).trim().equalsIgnoreCase("")) {
                                        sqlUpdate += ", ";
                                    }
                                    sqlUpdate += overHeadLineList.get(i).getAmountSaveIn() + " = '" + DmShared.vNull(overHeadLineList.get(i).getAmount()) + "' ";
                                }
                            }

                            if (!DmShared.xNull(sqlUpdate).trim().equalsIgnoreCase("")) {
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlUpdateSet + sqlUpdate + sqlUpdateWhere);
                                psTemp.executeUpdate();
                                psTemp.close();
                            }
                        }
                    }
                }

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
}
