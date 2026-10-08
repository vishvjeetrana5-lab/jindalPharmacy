package dataman.accounts.billHandleApproval.server;

import dataman.accounts.billHandleApproval.shared.GrdBillApproval_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.accounts.billHandleApproval.shared.BillApproval_Srv;
import dataman.accounts.billHandleApproval.shared.DefaultValue_Mdl;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BillApproval_SrvImpl extends DmRSSPrivate implements BillApproval_Srv {

    private String v_typeShortName = new LibraryVoucherCategory().BILL_HANDLE;

    @Override
    public DmResultPageing<List<GrdBillApproval_Mdl>> getFillRecords(DefaultValue_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdBillApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdBillApproval_Mdl> lstTemp = new ArrayList<>();
        GrdBillApproval_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join;

        try {
            rstTemp.setData(lstTemp);

            condition = " ";
            join = " ";

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  mr.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  mr.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getLstLocationCode()).equalsIgnoreCase("")) {
                condition += " AND mr.toLocCode IN(" + DmServer.getParamList(":toLocCode", param.getLstLocationCode()) + " )";
            }

            if (DmShared.xNull(param.getBillStatusCode()).equalsIgnoreCase("All")) {
                condition += "  ";

            } else if (DmShared.xNull(param.getBillStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(mr.approvedBy,'') = '' ";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = mr.wtaCode and wtfp.isStatus = 0 ";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getBillStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(mr.approvedBy,'') <> '' ";
            }

            condition += " AND mr.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "      select max(mr.toLocCode) as toLocCode , max(mr.recId) as recId,max(mrd.isStatus) as isStatus ,"
                    + "        max(mr.docId) as docId, max(mr.v_Date) as v_Date ,max(mr.recId) as v_No,max(mr.module) as module, "
                    + "        max(mr.approvedBy) as approvedBy, "
                    + "        max(FORMAT(mr.approvalDateTime,'dd-MMM-yyyy HH:mm')) AS approvalDateTime,"
                    + "        max(mr.v_Type) as v_Type, max(vt.category) AS IndType,"
                    + "        max(mr.wtaCode) as wtaCode "
                    + "        from billhandle AS mr "
                    + "        LEFT JOIN billhandleDetail AS mrd ON mrd.docId=mr.docId "
                    + "        LEFT JOIN siteMast AS sm ON sm.code=mr.site_Code "
                    + "        LEFT JOIN voucher_Type AS vt on vt.v_Type=mr.v_Type " + join
                    + "        WHERE mr.site_Code =:site_Code and vt.category IN ('" + v_typeShortName + "') " + condition
                    + "        group by mr.docId";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }

            if (!DmShared.xNull(param.getLstLocationCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("toLocCode", DmShared.xNull(param.getLstLocationCode()));            //Department
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdBillApproval_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                node.setModule(DmShared.xNull(rsTemp.getString("module")));
                node.setBillDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                node.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                node.setApproval(DmShared.xNull(rsTemp.getString("approvalDateTime")));
                node.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setVoucherNo(DmShared.xNull(rsTemp.getString("v_No")));

                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    node.setIsChecked("1");
                } else {
                    node.setIsChecked("0");
                }
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Pending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("All");
            listNode.setSharedKeyFieldName("All");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHToLocation(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            if (!DmBaseServer.getSsnLocationCodeList(this).equalsIgnoreCase("")) {
                condition = "where dm.code in (" + DmServer.getParamList(":code", DmBaseServer.getSsnLocationCodeList(this)) + ")";

            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select dm.code, dm.name "
                    + "	from billHandleLocationMaster dm "
                    + condition);

            if (!DmBaseServer.getSsnLocationCodeList(this).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("code", DmBaseServer.getSsnLocationCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResult<DefaultValue_Mdl> getDefalutValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        boolean flag = true;
        String sqlQuery = "", locationCode = "", locationName = "", condition = "";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!(DmServer.getSsnUserName(this).equalsIgnoreCase("sa")
                    || DmServer.getSsnUserName(this).equalsIgnoreCase("super")
                    || DmShared.xNull(DmBaseServer.getSsnLocationCodeList(this)).equalsIgnoreCase(""))) {
                condition = "where code in (" + DmServer.getParamList(":locationCode", DmBaseServer.getSsnLocationCodeList(this)) + " )";
            }
            sqlQuery = " select code, name "
                    + "	from billHandleLocationMaster  " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!(DmServer.getSsnUserName(this).equalsIgnoreCase("sa")
                    || DmServer.getSsnUserName(this).equalsIgnoreCase("super")
                    || DmShared.xNull(DmBaseServer.getSsnLocationCodeList(this)).equalsIgnoreCase(""))) {
                psTemp.setStringList("locationCode", DmBaseServer.getSsnLocationCodeList(this));
            }
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {

                if (flag) {

                    locationCode = rsTemp.getString("code");
                    locationName = rsTemp.getString("name");
                    flag = false;
                } else {
                    locationCode += "," + rsTemp.getString("code");
                    locationName += "," + rsTemp.getString("name");
                }
            }

            psTemp.close();
            rsTemp.close();
            cnTemp.close();
            LocalDate date = LocalDate.parse(DmServer.convertDate(DmServer.getServerDateTime()[0]).toString());
            dvm.setFromDate((date.minusDays(30).format(dateFormatter)));
            dvm.setToDate(DmServer.getServerDateTime()[0]);
            dvm.setLstLocationCode(locationCode);
            dvm.setLstLocationName(locationName);
            dvm.setBillStatusCode("P");
            dvm.setBillStatusName("Pending");

            dmrTemp.setData(dvm);
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
