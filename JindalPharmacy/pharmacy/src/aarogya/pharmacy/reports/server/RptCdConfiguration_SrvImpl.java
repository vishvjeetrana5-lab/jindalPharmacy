package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptCdConfiguration_Srv;
import aarogya.pharmacy.reports.shared.RptCdConfiguration_Vld;
import aarogya.pharmacy.reports.shared.TxtHsupplier_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RptCdConfiguration_SrvImpl extends DmRSSPrivate implements RptCdConfiguration_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = "", sqlOpening = "", conditionDate = "";
        //=======================
        FileWriter fileWriter = null;
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";
        try {
            RptCdConfiguration_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                //==================CSV=======================
                String fileName = widgetName.replaceAll(" ", "");
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmssSS");
                String dateTimeValue = sdf.format(cal.getTime());

                String directoryPath = DmServer.Path.sessions + "/" + sessionId;
                String serverFileName = fileName + "_" + dateTimeValue + ".csv";
                String fullFilePath = directoryPath + "/" + serverFileName;

                File sFile = new File(directoryPath);
                if (!sFile.exists()) {
                    sFile.mkdirs();
                }
                File nFile = new File(fullFilePath);
                if (nFile.exists()) {
                    nFile.delete();
                }

                keyField.setSharedKeyFieldCode(directoryPath);
                keyField.setSharedKeyFieldName(serverFileName);
                fileWriter = new FileWriter(fullFilePath, true);
            }

            //=============================================
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("") && !DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  cdc.appFrom >= :fromDate ";
                condition += " and  cdc.appFrom <= :toDate ";
            } else if (DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("") && !DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and   :toDate BETWEEN cdc.appFrom AND cdc.appTo ";
            } else if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("To Date is mandatory.");
            }

            if (!DmShared.xNull(filter.get("supplierCode").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND cdc.supplierCode in (" + DmServer.getParamList(":supplierCode", filter.get("supplierCode").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("companyCode").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND cdc.companyCode in (" + DmServer.getParamList(":companyCode", filter.get("companyCode").getTag()) + " )";
            }

            sqlQuery = "select  sg.name as supplierName,imm.companyName,format(cdc.appFrom,'dd-MMM-yyyy') appFrom,format(cdc.appTo,'dd-MMM-yyyy') appTo ,cdc.discPer"
                    + " from CDConfiguration cdc\n"
                    + "left join itemMakeMast imm on imm.code=cdc.companyCode\n"
                    + "left join subGroup sg on sg.subCode=cdc.supplierCode\n"
                    + "\n"
                    + "where 1=1 " + condition + "  order by sg.name  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            //===================================================Export Data to CSV=====================
            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "";

                FILE_HEADER = " Supplier Name ,	Company Name ,	Applicable From ,	Applicable To ,	Discount Percent ";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {

                    lineData = "\"" + DmShared.xNull(rsTemp.getString("supplierName")) + "" + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmShared.xNull(rsTemp.getString("companyName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("appFrom")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("appTo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("discPer")) + "\"" + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }

                fileWriter.flush();
                fileWriter.close();
            } //=========================================================================================
            else if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                jasperReport = DmServer.compileJasper("rpt_cd_Configuration");
                jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, null, filter, rsTemp);
                keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            }
            rstTemp.setData(keyField);

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

            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("supplierCode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("supplierCode", DmShared.xNull(filter.get("supplierCode").getTag()));
            }

            if (!DmShared.xNull(filter.get("companyCode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("companyCode", DmShared.xNull(filter.get("companyCode").getTag()));
            }

        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<TxtHsupplier_Mdl>> getTxtHsupplier(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHsupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHsupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHsupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select sg.subCode,sg.name as supplierName,sg.mobile from subGroup sg\n"
                    + "left join subGroupNature as sgn on sgn.code=sg.nature\n"
                    + "where sgn.manualCode in ('PS') ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHsupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHcompany(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL(" select code ,  companyName as name  from itemMakeMast ");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                ;

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

}
