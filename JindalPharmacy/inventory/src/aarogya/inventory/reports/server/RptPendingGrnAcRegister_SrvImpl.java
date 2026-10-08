package aarogya.inventory.reports.server;

import aarogya.inventory.reports.shared.RptPendingGrnAcRegister_Srv;
import aarogya.inventory.reports.shared.RptPendingGrnAcRegister_Vld;
import aarogya.inventory.reports.shared.TxtHItem_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptPendingGrnAcRegister_SrvImpl extends DmRSSPrivate implements RptPendingGrnAcRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery;
        String condition = " ",conditionFnl = " ",conditionSubCode = " ";

        try {
            RptPendingGrnAcRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            //            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
//                condition += " and pb.v_Date >= :fromDate ";
//            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and pb.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("PE")) {
                    conditionFnl += " where billAmount-abs(adjAmount)<>0 ";
                } else if (DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("AD")) {
                    conditionFnl += " where billAmount-abs(adjAmount)=0 ";
                }
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                conditionSubCode += " and pb.balancingAc in (" + DmServer.getParamList(":subCode", filter.get("supplier").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND   pb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }

            sqlQuery = "select docid,recID,v_Date,sgs.name as supplierName,v_No,billAmount,adjAmount,abs(adjAmount) as absAdjAmount,sgb.name as balancingAcName \n"
                    + "from ( \n"
                    + "	select pb.docid,pb.recID,pb.v_Date,pb.subCode as supplierCode,pb.v_No \n"
                    + "	,case when vt.category in('PBILD','PBLDU') then pb.oh_Amt_Net when vt.category in('PRTN','PRTNU') then -(pb.oh_Amt_Net) end as billAmount \n"
                    + "	,case when  isNull(pb.approvedBy,'')<>'' then(case when vt.category in('PRTN','PRTNU') then pb.oh_Amt_Net when vt.category in('PBILD','PBLDU') then -pb.oh_Amt_Net end) else 0 end as adjAmount \n"
                    + " ,pb.balancingAc as subCode "
                    + "		from pBill pb  \n"
                    + "		left join voucher_Type vt on pb.v_Type=vt.V_Type \n"
                    + "  	where vt.category in('PBILD','PBLDU','PRTN','PRTNU')  \n"
                    + condition+conditionSubCode+"\n"
                    + "	union all "
                    + "select pb.docid,pb.recID,pb.v_Date,pb.subCode as supplierCode,pb.v_No \n"
                    + "	,case when vt.category in('PCHL','PCHLU') then (pb.oh_Amt_Net-pb.oh_Amt_RoundOff) else 0 end as billAmount \n"
                    + "	,isNull(adj.adjAmount,0) as adjAmount,pb.balancingAc as subCode \n"
                    + "		from pChallan pb  \n"
                    + "		left join voucher_Type vt on pb.v_Type=vt.V_Type \n"
//                    + " 	left join \n"
//                    + "			( \n"
//                    + "			select pbd.pcDocId,-sum(pbd.oh_Amt_Net) as adjAmount \n"
//                    + "				from pBill pb \n"
//                    + "				left join pBillDetail pbd on pb.docid=pbd.docid \n"
//                    + "				left join voucher_Type vt on pb.v_Type=vt.v_Type \n"
//                    + "				where isNull(pb.approvedBy,'')<>'' "+ condition+"\n"
//                    + "				group by pbd.pcDocId \n"
//                    + "			) as adj on pb.docID=adj.pcDocId \n"
//                    + "		where 1=1 \n"
                    + " 	left join \n"
                    + "			( \n"
                    + "			select -(pc.oh_Amt_Net-pc.oh_Amt_RoundOff) as adjAmount,pc.docID \n"
                    + "				from pChallan pc \n"
                    + "				left Join ( \n"
                    + "                                     select pbd.pcDocID \n"
                    + "                                         from pBill pb \n"
                    + "                                         left join pBillDetail pbd on pb.docid=pbd.docID \n"
                    + "                                         where isNull(pbd.pcDocId,'')<>'' and isNull(pb.approvedBy,'')<>'' \n "+condition
                    + "                                         group by pbd.pcDocId "
                    + "                                     ) as ad on pc.docID=ad.pcDocID where isNull(ad.pcDocID,'')<>'' \n"
                    + "			) as adj on pb.docID=adj.DocId \n"
                    + "		where vt.category in('PCHL','PCHLU') \n"
                    + condition+conditionSubCode+"\n"
                    + ") as tmp "
                    + " left join subGroup sgb on tmp.subCode=sgb.subCode "
                    + " left join subGroup sgs on tmp.supplierCode=sgs.subCode "
                    + conditionFnl
                    + "	order by v_Date,recID ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            jasperReport = DmServer.compileJasper("Inventory_RptPendingGrnAcRegister");
            jasperPrint = DmServer.fillJasper(this, "Pending Grn A/c Register", jasperReport, null, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
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
            listNode.setSharedKeyFieldCode("PE");
            listNode.setSharedKeyFieldName("Pending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("AD");
            listNode.setSharedKeyFieldName("Adjusted");
            lstTemp.add(listNode);
            
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("AL");
            listNode.setSharedKeyFieldName("All");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVendor(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name AS name, sg.manualCode "
                    + " FROM subgroup sg"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " left join city as c on c.cityCode = sg.cityCode "
                    + " WHERE sgn.code IN (select code from subGroupNature where manualCode in('LG','OT','EX','RE'))");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
//            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
//                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
//            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(filter.get("supplier").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResultPageing<KeyFieldExtended> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

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

}
