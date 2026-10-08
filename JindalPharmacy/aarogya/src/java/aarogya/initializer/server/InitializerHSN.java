package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import aarogya.master.hsnMaster.shared.GrdRateITS_Mdl;
import aarogya.master.hsnMaster.shared.HSNMaster_Mdl;
import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;

public class InitializerHSN implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    public InitializerHSN() {
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
                    status = save(gson.fromJson(br, HSNMaster_Mdl.class));
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

    private int save(HSNMaster_Mdl mrTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {
            if (mrTemp != null) {
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
                spBegin = new DmSavePoint(cnTemp);
                mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

                sqlQuery = "select count(name) as cnt from hsnMast where name = :name";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException(mrTemp.getSharedKeyFieldName() + " name already exists.");
                }
                rsTemp.close();
                psTemp.close();

                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(rssTemp, "hsnMast", cnTemp));

                if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                    status = DmImportStatus.ERROR.getId();
                    throw new UnsupportedOperationException("Invalid key.");
                }

                sqlQuery = "insert into hsnMast(code, name, hsn, itsBasedOn, itemGSTNature, preparedBy, u_EntDt, nature) "
                        + " values (:code, :name, :hsn, :itsBasedOn, :itemGSTNature, :u_Name, :uEntDt, :nature) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("hsn", DmShared.iNull(mrTemp.getHSN()).toString());
                psTemp.setString("u_Name", DmServer.getSsnUserName(rssTemp));
                psTemp.setString("itsBasedOn", DmShared.xNull(mrTemp.getTaxBasedOnTag()));
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
                psTemp.setString("nature", DmShared.xNull(mrTemp.getNatureTag()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "delete from hsnITSDetail where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();

                int vSNo = 0;
                if (DmShared.xNull(mrTemp.getTaxBasedOnTag()).equalsIgnoreCase("1")) {
                    for (GrdRateITS_Mdl node : mrTemp.getGrdRateLst()) {
                        if (DmShared.vNull(node.getRateFrom()).doubleValue() > 0 && DmShared.vNull(node.getRateUpto()).doubleValue() > 0) {
                            node.setSharedKeyFieldName(String.valueOf(++vSNo));

                            node.setItsOnPurchaseReturnTag(DmServer.getCodeViaManualCode(node.getItsOnPurchaseReturnText(), "name", "its", "code", cnTemp));
                            if (DmShared.iNull(node.getItsOnPurchaseReturnTag()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                refreshDataStructure.writeLine(node.getItsOnPurchaseReturnText() + " ITS is not found for rate From: " + node.getRateFrom() + " in " + mrTemp.getSharedKeyFieldName());
                            }

                            node.setItsOnPurchaseTag(DmServer.getCodeViaManualCode(node.getItsOnPurchaseText(), "name", "its", "code", cnTemp));
                            if (DmShared.iNull(node.getItsOnPurchaseReturnTag()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                refreshDataStructure.writeLine(node.getItsOnPurchaseText() + " ITS is not found for rate From: " + node.getRateFrom() + " in " + mrTemp.getSharedKeyFieldName());
                            }

                            node.setItsOnSaleTag(DmServer.getCodeViaManualCode(node.getItsOnSaleText(), "name", "its", "code", cnTemp));
                            if (DmShared.iNull(node.getItsOnPurchaseReturnTag()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                refreshDataStructure.writeLine(node.getItsOnSaleText() + " ITS is not found for rate From: " + node.getRateFrom() + " in " + mrTemp.getSharedKeyFieldName());
                            }

                            node.setItsOnSaleReturnTag(DmServer.getCodeViaManualCode(node.getItsOnSaleReturnText(), "name", "its", "code", cnTemp));
                            if (DmShared.iNull(node.getItsOnPurchaseReturnTag()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                refreshDataStructure.writeLine(node.getItsOnSaleReturnText() + " ITS is not found for rate From: " + node.getRateFrom() + " in " + mrTemp.getSharedKeyFieldName());
                            }

                            node.setAcPurchaseTag(DmServer.getCodeViaManualCode(node.getAcPurchaseText(), "name", "subGroup", "subCode", cnTemp));
                            if (DmShared.iNull(node.getItsOnPurchaseReturnTag()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                refreshDataStructure.writeLine(node.getAcPurchaseText() + " a/c is not found for rate From: " + node.getRateFrom() + " in " + mrTemp.getSharedKeyFieldName());
                            }

                            node.setAcPurchaseReturnTag(DmServer.getCodeViaManualCode(node.getAcPurchaseReturnText(), "name", "subGroup", "subCode", cnTemp));
                            if (DmShared.iNull(node.getItsOnPurchaseReturnTag()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                refreshDataStructure.writeLine(node.getAcPurchaseReturnText() + " a/c is not found for rate From: " + node.getRateFrom() + " in " + mrTemp.getSharedKeyFieldName());
                            }

                            node.setAcSaleTag(DmServer.getCodeViaManualCode(node.getAcSaleText(), "name", "subGroup", "subCode", cnTemp));
                            if (DmShared.iNull(node.getItsOnPurchaseReturnTag()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                refreshDataStructure.writeLine(node.getAcSaleText() + " a/c is not found for rate From: " + node.getRateFrom() + " in " + mrTemp.getSharedKeyFieldName());
                            }

                            node.setAcSaleReturnTag(DmServer.getCodeViaManualCode(node.getAcSaleReturnText(), "name", "subGroup", "subCode", cnTemp));
                            if (DmShared.iNull(node.getItsOnPurchaseReturnTag()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                refreshDataStructure.writeLine(node.getAcSaleReturnText() + " a/c is not found for rate From: " + node.getRateFrom() + " in " + mrTemp.getSharedKeyFieldName());
                            }

                            sqlQuery = "Insert Into hsnITSDetail(code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                                    + " its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) Values  "
                                    + "(:code ,:v_SNo,:rateFrom,:rateUpto,:its_Code_Purchase,:its_Code_PurchaseRet,"
                                    + " :its_Code_Sales,:its_Code_SalesRet,:purchaseAc,:purchaseReturnAc,:salesAc,:salesReturnAc) ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setString("v_SNo", DmShared.iNull(node.getSharedKeyFieldName()).toString());
                            psTemp.setString("rateFrom", DmServer.vNull(node.getRateFrom(), "0.00"));
                            psTemp.setString("rateUpto", DmServer.vNull(node.getRateUpto(), "0.00"));
                            psTemp.setString("its_Code_Purchase", DmShared.foriegnKey(node.getItsOnPurchaseTag()));
                            psTemp.setString("its_Code_PurchaseRet", DmShared.foriegnKey(node.getItsOnPurchaseReturnTag()));
                            psTemp.setString("its_Code_Sales", DmShared.foriegnKey(node.getItsOnSaleTag()));
                            psTemp.setString("its_Code_SalesRet", DmShared.foriegnKey(node.getItsOnSaleReturnTag()));
                            psTemp.setString("purchaseAc", DmShared.foriegnKey(node.getAcPurchaseTag()));
                            psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(node.getAcPurchaseReturnTag()));
                            psTemp.setString("salesAc", DmShared.foriegnKey(node.getAcSaleTag()));
                            psTemp.setString("salesReturnAc", DmShared.foriegnKey(node.getAcSaleReturnTag()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                } else {
                    mrTemp.setvSNo(String.valueOf(++vSNo));
                    mrTemp.setItsOnPurchaseReturnTag(DmServer.getCodeViaManualCode(mrTemp.getItsOnPurchaseReturnText(), "name", "its", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getItsOnPurchaseReturnTag()).longValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        refreshDataStructure.writeLine(mrTemp.getItsOnPurchaseReturnText() + " ITS is not found for " + mrTemp.getSharedKeyFieldName());
                    }

                    mrTemp.setItsOnPurchaseTag(DmServer.getCodeViaManualCode(mrTemp.getItsOnPurchaseText(), "name", "its", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getItsOnPurchaseReturnTag()).longValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        refreshDataStructure.writeLine(mrTemp.getItsOnPurchaseText() + " ITS is not found for " + mrTemp.getSharedKeyFieldName());
                    }

                    mrTemp.setItsOnSaleTag(DmServer.getCodeViaManualCode(mrTemp.getItsOnSaleText(), "name", "its", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getItsOnPurchaseReturnTag()).longValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        refreshDataStructure.writeLine(mrTemp.getItsOnSaleText() + " ITS is not found for " + mrTemp.getSharedKeyFieldName());
                    }

                    mrTemp.setItsOnSaleReturnTag(DmServer.getCodeViaManualCode(mrTemp.getItsOnSaleReturnText(), "name", "its", "code", cnTemp));
                    if (DmShared.iNull(mrTemp.getItsOnPurchaseReturnTag()).longValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        refreshDataStructure.writeLine(mrTemp.getItsOnSaleReturnText() + " ITS is not found for " + mrTemp.getSharedKeyFieldName());
                    }

                    mrTemp.setAcPurchaseTag(DmServer.getCodeViaManualCode(mrTemp.getAcPurchaseText(), "name", "subGroup", "subCode", cnTemp));
                    if (DmShared.iNull(mrTemp.getItsOnPurchaseReturnTag()).longValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        refreshDataStructure.writeLine(mrTemp.getAcPurchaseText() + " a/c is not found for " + mrTemp.getSharedKeyFieldName());
                    }

                    mrTemp.setAcPurchaseReturnTag(DmServer.getCodeViaManualCode(mrTemp.getAcPurchaseReturnText(), "name", "subGroup", "subCode", cnTemp));
                    if (DmShared.iNull(mrTemp.getItsOnPurchaseReturnTag()).longValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        refreshDataStructure.writeLine(mrTemp.getAcPurchaseReturnText() + " a/c is not found for " + mrTemp.getSharedKeyFieldName());
                    }

                    mrTemp.setAcSaleTag(DmServer.getCodeViaManualCode(mrTemp.getAcSaleText(), "name", "subGroup", "subCode", cnTemp));
                    if (DmShared.iNull(mrTemp.getItsOnPurchaseReturnTag()).longValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        refreshDataStructure.writeLine(mrTemp.getAcSaleText() + " a/c is not found for " + mrTemp.getSharedKeyFieldName());
                    }

                    mrTemp.setAcSaleReturnTag(DmServer.getCodeViaManualCode(mrTemp.getAcSaleReturnText(), "name", "subGroup", "subCode", cnTemp));
                    if (DmShared.iNull(mrTemp.getItsOnPurchaseReturnTag()).longValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        refreshDataStructure.writeLine(mrTemp.getAcSaleReturnText() + " a/c is not found for " + mrTemp.getSharedKeyFieldName());
                    }
                    sqlQuery = "Insert Into hsnITSDetail(code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                            + " its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) Values  "
                            + "(:code ,:v_SNo,:rateFrom,:rateUpto,:its_Code_Purchase,:its_Code_PurchaseRet,"
                            + " :its_Code_Sales,:its_Code_SalesRet,:purchaseAc,:purchaseReturnAc,:salesAc,:salesReturnAc) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(mrTemp.getvSNo()).toString());
                    psTemp.setString("rateFrom", "0.00");
                    psTemp.setString("rateUpto", "0.00");
                    psTemp.setString("its_Code_Purchase", DmShared.foriegnKey(mrTemp.getItsOnPurchaseTag()));
                    psTemp.setString("its_Code_PurchaseRet", DmShared.foriegnKey(mrTemp.getItsOnPurchaseReturnTag()));
                    psTemp.setString("its_Code_Sales", DmShared.foriegnKey(mrTemp.getItsOnSaleTag()));
                    psTemp.setString("its_Code_SalesRet", DmShared.foriegnKey(mrTemp.getItsOnSaleReturnTag()));
                    psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getAcPurchaseTag()));
                    psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getAcPurchaseReturnTag()));
                    psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getAcSaleTag()));
                    psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getAcSaleReturnTag()));
                    psTemp.executeUpdate();
                    psTemp.close();

                }
                /*===========================< Update record of itemMast >===============================*/
                sqlQuery = "Update  itemmast  set "
                        + "         itemGSTNature = :itemGSTNature"
                        + "         where hsnCode = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "delete itemITSDetail "
                        + "FROM itemMast im "
                        + "INNER JOIN itemItsDetail iid ON iid.code = im.code "
                        + "WHERE im.hsnCode = :hsnCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("hsnCode", mrTemp.getSharedKeyFieldCode());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "insert into itemITSDetail (code, v_SNo, rateFrom, rateUpto, its_Code_Purchase, its_Code_PurchaseRet, its_Code_Sales, "
                        + "         its_Code_SalesRet, purchaseAc, purchaseReturnAc, salesAc, salesReturnAc) "
                        + " (select im.code, hid.v_SNo, hid.rateFrom, hid.rateUpto, hid.its_Code_Purchase, hid.its_Code_PurchaseRet, "
                        + "         hid.its_Code_Sales, hid.its_Code_SalesRet, hid.purchaseAc, hid.purchaseReturnAc, hid.salesAc, hid.salesReturnAc "
                        + "         from itemMast im "
                        + "         inner join hsnMast hm on hm.code = im.hsnCode "
                        + "         left join hsnITSDetail hid on hid.code = hm.code "
                        + "         where im.hsnCode = :hsnCode "
                        + "  ) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("hsnCode", mrTemp.getSharedKeyFieldCode());
                psTemp.executeUpdate();
                psTemp.close();

                spBegin.commit();
                rtnStatus = DmImportStatus.SUCCESS.getId();
            }
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
