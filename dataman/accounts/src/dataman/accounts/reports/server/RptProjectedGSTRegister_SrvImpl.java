package dataman.accounts.reports.server;

import dataman.accounts.reports.shared.RptProjectedGSTRegister_Srv;
import dataman.accounts.reports.shared.RptProjectedGSTRegister_Vld;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptProjectedGSTRegister_SrvImpl extends DmRSSPrivate implements RptProjectedGSTRegister_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTempSubReport = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        DmIJasperCompiled jasperCompiled = null;
        String sqlQuery = "", condition = "", subQueryTotalTax = "";

        try {
            RptProjectedGSTRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND gst.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND gst.v_Date <= :toDate ";
            }

            if (!DmShared.xNull(filter.get("siteCode").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND gst.site_Code = :siteCode ";
            }
//            if (!DmShared.xNull(filter.get("partyType").getTag()).trim().equalsIgnoreCase("")) {
//                condition += " AND sb.partyType IN (" + DmServer.getParamList(":partyType", filter.get("partyType").getTag()) + " )";
//            }
//            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
//                condition += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
//            }
            if (!DmShared.xNull(filter.get("vType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  gst.v_Type IN (" + DmServer.getParamList(":vType", filter.get("vType").getTag()) + " ) ";
            }

            sqlQuery = "SELECT t.gstType AS gstTypeCode "
                    + "		, CASE WHEN t.gstType = 1 THEN 'Input'  WHEN t.gstType = 2 THEN 'Output' ELSE 'Void' END AS gstTypeName "
                    + "		, t.vTypeDesc, t.gstRate "
                    + "		, SUM(t.outTaxableAmount) AS outTaxableAmount "
                    + "		, SUM(t.outIGstAmount) AS outIGstAmount "
                    + "		, SUM(t.outSGstAmount) AS outSGstAmount  "
                    + "		, SUM(t.outCGstAmount) AS outCGstAmount  "
                    + "		, SUM(t.outUGstAmount) AS outUGstAmount  "
                    + "		, (ISNULL(SUM(t.outIGstAmount), 0) + ISNULL(SUM(t.outSGstAmount),0) + ISNULL(SUM(t.outCGstAmount),0) + ISNULL(SUM(t.outUGstAmount),0)) as totalOutGstAmount "
                    + "		, SUM(t.inTaxableAmount) AS inTaxableAmount "
                    + "		, SUM(t.inIGstAmount) AS inIGstAmount "
                    + "		, SUM(t.inSGstAmount) AS inSGstAmount  "
                    + "		, SUM(t.inCGstAmount) AS inCGstAmount  "
                    + "		, SUM(t.inUGstAmount) AS inUGstAmount  "
                    + "		, (ISNULL(SUM(t.inIGstAmount), 0) + ISNULL(SUM(t.inSGstAmount),0) + ISNULL(SUM(t.inCGstAmount),0) + ISNULL(SUM(t.inUGstAmount),0)) as totalInGstAmount "
                    + "  FROM ( "
                    //  all sales + debit note  
                    + "        SELECT 	gst.gstType "
                    + "		, MAX(vt.description) AS vTypeDesc "
                    + "		, MAX(vt.serialNo) AS vTypeSNo "
                    + "		, (ISNULL(gst.itemIgstRate, 0) + ISNULL(gst.itemSgstRate,0) + ISNULL(gst.itemCgstRate,0) + ISNULL(gst.itemUgstRate,0)) as gstRate "
                    + "		, SUM(gst.itemTaxableAmount) AS outTaxableAmount "
                    + "		, SUM(gst.itemIgstAmount) AS outIGstAmount "
                    + "		, SUM(gst.itemSgstAmount) AS outSGstAmount  "
                    + "		, SUM(gst.itemCgstAmount) AS outCGstAmount  "
                    + "		, SUM(gst.itemUgstAmount) AS outUGstAmount  "
                    + "		, 0 AS inTaxableAmount "
                    + "		, 0 AS inIGstAmount "
                    + "		, 0 AS inSGstAmount  "
                    + "		, 0 AS inCGstAmount  "
                    + "		, 0 AS inUGstAmount  "
                    + "		FROM gstTrans gst  "
                    + "		LEFT JOIN siteMast sm ON sm.code = gst.site_Code  "
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = gst.v_Type  "
                    + "        WHERE gst.gstType = 2  "
                    + "        AND vt.documentTypeCode in ('RI','SEWP','SEWOP', 'DE', 'WPAY', 'WOPAY', 'D','AR') " + condition
                    + "		GROUP BY gst.gstType, gst.v_Type, (ISNULL(gst.itemIgstRate, 0) + ISNULL(gst.itemSgstRate,0) + ISNULL(gst.itemCgstRate,0) + ISNULL(gst.itemUgstRate,0)) "
                    + "        UNION ALL "
                    //sales credit note 
                    + "        SELECT 	gst.gstType "
                    + "		, MAX(vt.description) AS vTypeDesc "
                    + "		, MAX(vt.serialNo) AS vTypeSNo "
                    + "		, (ISNULL(gst.itemIgstRate, 0) + ISNULL(gst.itemSgstRate,0) + ISNULL(gst.itemCgstRate,0) + ISNULL(gst.itemUgstRate,0)) as gstRate "
                    + "		, (0 - ISNULL(SUM(gst.itemTaxableAmount), 0)) AS outTaxableAmount "
                    + "		, (0 - ISNULL(SUM(gst.itemIgstAmount), 0)) AS outIGstAmount "
                    + "		, (0 - ISNULL(SUM(gst.itemSgstAmount), 0)) AS outSGstAmount  "
                    + "		, (0 - ISNULL(SUM(gst.itemCgstAmount), 0)) AS outCGstAmount  "
                    + "		, (0 - ISNULL(SUM(gst.itemUgstAmount), 0)) AS outUGstAmount  "
                    + "		, 0 AS inTaxableAmount "
                    + "		, 0 AS inIGstAmount "
                    + "		, 0 AS inSGstAmount  "
                    + "		, 0 AS inCGstAmount  "
                    + "		, 0 AS inUGstAmount  "
                    + "		FROM gstTrans gst  "
                    + "		LEFT JOIN siteMast sm ON sm.code = gst.site_Code  "
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = gst.v_Type  "
                    + "        WHERE gst.gstType = 2  "
                    + "        AND vt.documentTypeCode in ('C','R','AA') " + condition
                    + "		GROUP BY gst.gstType, gst.v_Type, (ISNULL(gst.itemIgstRate, 0) + ISNULL(gst.itemSgstRate,0) + ISNULL(gst.itemCgstRate,0) + ISNULL(gst.itemUgstRate,0)) "
                    + "        UNION ALL  "
                    // all purchase + credit note 
                    + "        SELECT 	gst.gstType "
                    + "		, MAX(vt.description) AS vTypeDesc "
                    + "		, MAX(vt.serialNo) AS vTypeSNo "
                    + "		, (ISNULL(gst.itemIgstRate, 0) + ISNULL(gst.itemSgstRate,0) + ISNULL(gst.itemCgstRate,0) + ISNULL(gst.itemUgstRate,0)) as gstRate "
                    + "		, 0 AS outTaxableAmount "
                    + "		, 0 AS outIGstAmount "
                    + "		, 0 AS outSGstAmount  "
                    + "		, 0 AS outCGstAmount  "
                    + "		, 0 AS outUGstAmount  "
                    + "		, SUM(gst.itemTaxableAmount) AS inTaxableAmount "
                    + "		, SUM(gst.itemIgstAmount) AS inIGstAmount "
                    + "		, SUM(gst.itemSgstAmount) AS inSGstAmount  "
                    + "		, SUM(gst.itemCgstAmount) AS inCGstAmount  "
                    + "		, SUM(gst.itemUgstAmount) AS inUGstAmount  "
                    + "		FROM gstTrans gst  "
                    + "		LEFT JOIN siteMast sm ON sm.code = gst.site_Code  "
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = gst.v_Type  "
                    + "        WHERE gst.gstType = 1  "
                    + "        AND vt.documentTypeCode in ('RI','SEWP','SEWOP', 'DE', 'WPAY', 'WOPAY', 'C') " + condition
                    + "		GROUP BY gst.gstType, gst.v_Type, (ISNULL(gst.itemIgstRate, 0) + ISNULL(gst.itemSgstRate,0) + ISNULL(gst.itemCgstRate,0) + ISNULL(gst.itemUgstRate,0)) "
                    + "        UNION ALL "
                    //purchase debit note
                    + "        SELECT 	gst.gstType "
                    + "		, MAX(vt.description) AS vTypeDesc "
                    + "		, MAX(vt.serialNo) AS vTypeSNo "
                    + "		, (ISNULL(gst.itemIgstRate, 0) + ISNULL(gst.itemSgstRate,0) + ISNULL(gst.itemCgstRate,0) + ISNULL(gst.itemUgstRate,0)) as gstRate "
                    + "		, 0 AS outTaxableAmount "
                    + "		, 0 AS outIGstAmount "
                    + "		, 0 AS outSGstAmount  "
                    + "		, 0 AS outCGstAmount  "
                    + "		, 0 AS outUGstAmount  "
                    + "		, (0 - ISNULL(SUM(gst.itemTaxableAmount), 0)) AS inTaxableAmount "
                    + "		, (0 - ISNULL(SUM(gst.itemIgstAmount), 0)) AS inIGstAmount "
                    + "		, (0 - ISNULL(SUM(gst.itemSgstAmount), 0)) AS inSGstAmount  "
                    + "		, (0 - ISNULL(SUM(gst.itemCgstAmount), 0)) AS inCGstAmount  "
                    + "		, (0 - ISNULL(SUM(gst.itemUgstAmount), 0)) AS inUGstAmount  "
                    + "		FROM gstTrans gst  "
                    + "		LEFT JOIN siteMast sm ON sm.code = gst.site_Code  "
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = gst.v_Type  "
                    + "        WHERE gst.gstType = 1  "
                    + "        AND vt.documentTypeCode in ('D') " + condition
                    + "		GROUP BY gst.gstType, gst.v_Type, (ISNULL(gst.itemIgstRate, 0) + ISNULL(gst.itemSgstRate,0) + ISNULL(gst.itemCgstRate,0) + ISNULL(gst.itemUgstRate,0)) "
                    + "        ) as t "
                    + "	GROUP BY t.gstType, t.vTypeDesc, t.gstRate "
                    + "	ORDER BY t.gstType DESC,t.vTypeDesc, MAX(t.vTypeSNo), t.gstRate;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            jasperCompiled = DmServer.compileJasper("Accounts_ProjectedGSTRegister", "", true);

            jasperReport = DmServer.compileJasper("Accounts_ProjectedGSTRegister");
            jasperPrint = DmServer.fillJasper(this, "Projected GST Register", jasperReport, hm, filter, rsTemp);

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
                if (psTempSubReport != null) {
                    psTempSubReport.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private String getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("siteCode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setString("siteCode", DmShared.xNull(filter.get("siteCode").getTag()));
            }

            if (!DmShared.xNull(filter.get("vType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("vType", DmShared.xNull(filter.get("vType").getTag()));
            }

        } catch (Exception e) {
        }
        return psTemp.getQuery();
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
            sqlWithParam.setSQL(" SELECT  sm.code , sm.name  FROM siteMast sm ");
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

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVType(DmHelper pageing, boolean blnToEndOfFile) {
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
                    + "    vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " FROM voucher_Type vt"
                    + " WHERE  isActive=1");
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

    @Override
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        KeyFieldExtended lstTemp = new KeyFieldExtended();
        try {
            lstTemp.setSharedKeyFieldCode(DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
            lstTemp.setSharedKeyFieldName(DmShared.xNull(DmServer.getSsnSiteDisplayName(this)));

            dmrTemp.setData(lstTemp);

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

}
