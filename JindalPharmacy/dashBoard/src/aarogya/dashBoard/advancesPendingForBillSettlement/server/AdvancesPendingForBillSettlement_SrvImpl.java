package aarogya.dashBoard.advancesPendingForBillSettlement.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.dashBoard.advancesPendingForBillSettlement.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmDashBoard;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdvancesPendingForBillSettlement_SrvImpl extends DmRSSPrivate implements AdvancesPendingForBillSettlement_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<AdvancesPendingForBillSettlement_Mdl>> getData(String daysCount, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue) {
        DmResultPageing<List<AdvancesPendingForBillSettlement_Mdl>> rstTemp = new DmResultPageing<>();
        List<AdvancesPendingForBillSettlement_Mdl> lstTemp = new ArrayList<AdvancesPendingForBillSettlement_Mdl>();
        AdvancesPendingForBillSettlement_Mdl listNode;
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sql = "", startDate = "", toDate = "";
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sql = " select sg.name as  supplierName,po.recId as poNo,po.v_Date as poDate,adrq.recId advanceReqNo ,\n"
                    + "adrq.v_Date as advReqDate,adv.recId AdvanceRefNo ,adv.v_Date as advDAte,adv.paidAmt advanceForSettlement\n"
                    + "\n"
                    + "from advanceEntry adv \n"
                    + "left join advanceReqEntry adrq on adrq.docid=adv.reqDocId\n"
                    + "left join pOrder po on po.docId=adrq.poDocId\n"
                    + "left join subGroup sg on sg.subCode=po.subCode\n"
                    + "left join finPayEntDetail fpe on fpe.payReqDocId=adv.finPayReqDocId\n"
                    + " where fpe.docId is null  and adv.site_Code= :siteCode  ";

            sqlWithParam.setSQL(sql);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                listNode = new AdvancesPendingForBillSettlement_Mdl();

                listNode.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setPoNo(DmShared.xNull(rsTemp.getString("poNo")));
                listNode.setPoDate(DmShared.dNull(rsTemp.getDate("poDate")));
                listNode.setAdvanceReqNo(DmShared.xNull(rsTemp.getString("advanceReqNo")));
                listNode.setAdvReqDate(DmShared.dNull(rsTemp.getDate("advReqDate")));
                listNode.setAdvanceRefNo(DmShared.xNull(rsTemp.getString("AdvanceRefNo")));
                listNode.setAdvDAte(DmShared.dNull(rsTemp.getDate("advDAte")));
                listNode.setAdvanceForSettlement(DmShared.xNull(rsTemp.getString("advanceForSettlement")));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResult<String> getNewDate(int value, String date) {
        DmResult<String> rstTemp = new DmResult<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar;
        Date dt;
        try {
            calendar = Calendar.getInstance();
            dt = sdf.parse(date);
            calendar.setTime(dt);
            calendar.add(Calendar.DATE, value);
            rstTemp.setStatus(true);
            rstTemp.setData(sdf.format(calendar.getTime()));
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<KeyFieldExtended>();
        KeyFieldExtended defaultValue = new KeyFieldExtended();
        try {
            defaultValue.setSharedKeyFieldCode(DmServer.getServerDateTime()[0]);
            dmrTemp.setData(defaultValue);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
        }
        return dmrTemp;
    }

    @Override
    public DmResult<CardDetail_Mdl> getCardDetail(String daysCount) {
        DmResult<CardDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardDetail_Mdl mrTemp = new CardDetail_Mdl();
        List<DmChart_Mdl> listNode = new ArrayList<DmChart_Mdl>();
        DmChart_Mdl node;
        dmrTemp.setData(mrTemp);
        String sql = "", startDate = "", toDate = "";

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select count(adv.recId) as cnt\n"
                    + "\n"
                    + "from advanceEntry adv \n"
                    + "left join advanceReqEntry adrq on adrq.docid=adv.reqDocId\n"
                    + "left join pOrder po on po.docId=adrq.poDocId\n"
                    + "left join subGroup sg on sg.subCode=po.subCode\n"
                    + "left join finPayEntDetail fpe on fpe.payReqDocId=adv.finPayReqDocId\n"
                    + "where fpe.docId is null and adv.site_Code= :siteCode  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setCount(DmShared.iNull(rsTemp.getString("cnt")).toString());

                node = new DmChart_Mdl();
                node.setLabelName(DmShared.xNull(rsTemp.getString("cnt")));
                node.setLabelCode(DmShared.xNull(rsTemp.getString("cnt")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("cnt")).intValue());
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListChardData().addAll(listNode);

            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId, String daysUpto) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        String[] currentDateTime;
        char separator = File.separatorChar;
        String csvHeader = "", condition = "";
        dmrTemp.setData(keyField);
        try {
            csvHeader = "Supplier Name, P.O.No., P.O.Date, Advance Req. No., Advance Req. Date, Advance Ref. No., Advance Ref. Date, Advance For Settlement";
            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            fileName = new pmDashBoard().ADV_PENDING_FOR_SETTLEMENT.caption + String.valueOf(new java.util.Date().getTime()).trim();
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            sqlQuery = " select sg.name as  supplierName,po.recId as poNo,po.v_Date as poDate,adrq.recId advanceReqNo ,\n"
                    + "adrq.v_Date as advReqDate,adv.recId AdvanceRefNo ,adv.v_Date as advDate,adv.paidAmt advanceForSettlement\n"
                    + "\n"
                    + "from advanceEntry adv \n"
                    + "left join advanceReqEntry adrq on adrq.docid=adv.reqDocId\n"
                    + "left join pOrder po on po.docId=adrq.poDocId\n"
                    + "left join subGroup sg on sg.subCode=po.subCode\n"
                    + "left join finPayEntDetail fpe on fpe.payReqDocId=adv.finPayReqDocId\n"
                    + "where fpe.docId is null  and adv.site_Code= :siteCode \n"
                    + "\n"
                    + "order by adv.v_Date,sg.name  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                lineData = "\"" + DmShared.xNull(rsTemp.getString("supplierName")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("poNo")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.dNull(rsTemp.getDate("poDate")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("advanceReqNo")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.dNull(rsTemp.getDate("advReqDate")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("AdvanceRefNo")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.dNull(rsTemp.getDate("advDate")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("advanceForSettlement")) + "\"";

                handleFile.writeLine(lineData);

            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            /////
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
}
