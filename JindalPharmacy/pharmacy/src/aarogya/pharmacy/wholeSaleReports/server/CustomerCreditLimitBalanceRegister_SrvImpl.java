package aarogya.pharmacy.wholeSaleReports.server;

import aarogya.pharmacy.wholeSaleReports.shared.CustomerCreditLimitBalanceRegister_Srv;
import aarogya.pharmacy.wholeSaleReports.shared.CustomerCreditLimitBalanceRegister_Vld;
import aarogya.pharmacy.wholeSaleReports.shared.TxtHParty_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class CustomerCreditLimitBalanceRegister_SrvImpl extends DmRSSPrivate implements CustomerCreditLimitBalanceRegister_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCatAll = "'" + pvc.PHARMACY_BILL_WHOLESALE_CASH + "','" + pvc.PHARMACY_BILL_WHOLESALE_CREDIT + "'";
    private final String DEBTORS_GROUP_NAME = "Sundry Debtors";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", condition = "";
        String sessionId;

        try {
            CustomerCreditLimitBalanceRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sessionId = DmServer.getSsnId(this);
            //<editor-fold defaultstate="collapsed" desc="Filters">
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            if (!DmShared.xNull(filter.get("customerName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND sg.subCode IN (" + DmServer.getParamList(":customerName", filter.get("customerName").getTag()) + " ) \n";
            }
            if (DmShared.xNull(filter.get("creditLimitExist").getText()).trim().equalsIgnoreCase("Yes")) {
                condition += "AND ISNULL(sg.creditLimit, 0) > 0 \n";
            } else {
                condition += "AND ISNULL(sg.creditLimit, 0) <= 0 \n";
            }
            //</editor-fold>

            sqlQuery = "SELECT \n"
                    + "		sg.subCode AS docId, sg.name As partyName, sg.manualCode, sg.add1, sg.add2, sg.pin\n"
                    + "		,CONCAT(sg.add1,' ',sg.add2,' ',sg.pin,' ',c.cityName) AS address, c.cityName, sm.name AS stateName, ct.name AS countryName\n"
                    + "		, sg.mobile, sg.eMail\n"
                    + "		, IIF(ISNULL(sg.creditLimit, 0) = 0, 'No', 'Yes') AS isCreditLimitDefined\n"
                    + "		, sg.creditLimit, ABS(vLg.balanceAmt) AS balanceAmt\n"
                    + "		, CASE WHEN ISNULL(vLg.balanceAmt, 0) > 0 THEN 'Dr' WHEN ISNULL(vLg.balanceAmt, 0) < 0 THEN 'Cr' ELSE '' END AS balanceType\n"
                    + "		, CASE WHEN ISNULL(sg.creditLimit, 0) = 0 THEN 0 \n"
                    + "				ELSE\n"
                    + "					CASE WHEN ISNULL(vLg.balanceAmt, 0) > 0 THEN ISNULL(sg.creditLimit, 0) - ISNULL(vLg.balanceAmt, 0) ELSE ISNULL(sg.creditLimit, 0) END\n"
                    + "				END AS balanceCreditLimitAmt\n"
                    + "		, CASE WHEN ISNULL(vLg.balanceAmt, 0) < 0 THEN ABS(ISNULL(vLg.balanceAmt, 0)) ELSE 0 END AS advanceAmt\n"
                    + "		, CAST(CASE WHEN ISNULL(sg.creditLimit, 0) = 0 THEN 0 \n"
                    + "				ELSE\n"
                    + "					(CASE WHEN ISNULL(vLg.balanceAmt, 0) > 0 THEN ISNULL(sg.creditLimit, 0) - ISNULL(vLg.balanceAmt, 0) ELSE ISNULL(sg.creditLimit, 0) END * 100) / sg.creditLimit\n"
                    + "				END AS DECIMAL(18,2)) AS balanceCreditLimitPer\n"
                    + "FROM subGroup sg \n"
                    + "LEFT JOIN city c ON c.cityCode = sg.cityCode\n"
                    + "LEFT JOIN stateMast sm ON sm.code = c.stateCode\n"
                    + "LEFT JOIN country ct ON ct.code = c.countryCode\n"
                    + "LEFT JOIN (\n"
                    + "		SELECT lg.subcode, (ISNULL(SUM(lg.amtDr), 0) - ISNULL(SUM(lg.amtCr), 0)) AS balanceAmt\n"
                    + "		FROM ledger lg\n"
                    + "		WHERE lg.isPosted = 1\n"
                    + "				AND lg.v_Date <= :asOnDate\n"
                    + "		GROUP BY lg.subcode\n"
                    + "		) vLg ON sg.subCode = vLg.subCode\n"
                    + "WHERE 1=1 \n" + condition
                    + "ORDER BY isCreditLimitDefined, balanceCreditLimitPer, sg.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            jasperReport = DmServer.compileJasper("Pharmacy_CustomerCreditLimitBalance");
            jasperPrint = DmServer.fillJasper(this, "Customer Credit Limit Balance", jasperReport, null, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);

            cnTemp.close();
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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("asOnDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("asOnDate", DmServer.convertDate(filter.get("asOnDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("customerName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("customerName", DmShared.xNull(filter.get("customerName").getTag()));
            }
            if (!DmShared.xNull(filter.get("creditLimitExist").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("creditLimitExist", DmShared.xNull(filter.get("creditLimitExist").getTag()));
            }

        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT \n"
                    + "		ag.groupCode, ag.groupName\n"
                    + "FROM acGroup ag\n"
                    //                    + "INNER JOIN acGroup agc ON CHARINDEX(ISNULL(ag.mainStreamCode, ''), ISNULL(agc.mainStreamCode,''),1) > 0 \n"
                    + "WHERE ag.groupName = :groupName\n");
            sqlWithParam.setString("groupName", DEBTORS_GROUP_NAME);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNo(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<TxtHParty_Mdl>> getTxtHCustomer(String creditLimitExist, DmHelper pageing, boolean blnToEndOfFile) {

        DmResultPageing<List<TxtHParty_Mdl>> dmrTemp = new DmResultPageing<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<TxtHParty_Mdl> lstTemp = new ArrayList<TxtHParty_Mdl>();
        TxtHParty_Mdl listNode;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        Connection cnTemp = null;
        dmrTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT sg.subCode AS code, sg.name as customerName, sg.manualCode, c.cityName \n"
                    + " FROM subGroup sg \n"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode \n"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode \n"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature \n"
                    + " WHERE sg.isActive = 1 \n"
                    + " AND sgsl.site_Code = :site_Code \n"
                    + " AND sgn.manualCode = 'CT' \n";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHParty_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("customerName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            dmrTemp.setData(lstTemp);
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(pageing.getPaging());
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            new DmException(0, ex.getMessage());
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
        return dmrTemp;

    }

}
