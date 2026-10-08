package aarogya.inventory.reports.server;

import aarogya.inventory.reports.shared.RptGatePassOut_Srv;
import aarogya.inventory.reports.shared.RptGatePassOut_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmInventory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptGatePassOut_SrvImpl extends DmRSSPrivate implements RptGatePassOut_Srv {

    private String v_TypeShortName = new ProjectVoucherCategory().GATE_PASS_OUT;
    private final String vCat = new ProjectVoucherCategory().GATE_PASS_IN;
    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTempSubReport = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String fileName = widgetName.replaceAll(" ", "");
        DmIJasperCompiled jasperCompiled = null;
        String sqlQuery, condition = "", subQueryUsage = "", type = "", caption = "", passtype = "";
        FileWriter fileWriter = null;
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";

        try {
            RptGatePassOut_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            //===========================< CSV >=======================================
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(widgetName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                String directoryPath = DmServer.Path.sessions + "/" + sessionId;
                String serverFileName = fileName + ".csv";
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

            //=======================< CSV >================================
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  gio.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  gio.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and gio.departmentCode in (" + DmServer.getParamList(":departmentCode", filter.get("department").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("destination").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and replace(gio.destination,',','') in (" + DmServer.getParamList(":destination", filter.get("destination").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("0")) {
//                type += "  vt.category = '" + v_TypeShortName + "'";
                condition += " and vt.category = '" + v_TypeShortName + "'";
                passtype = " case  when gio.passType='0' then 'Gate Out Pass - Non-Returnable'  when gio.passType='1' then 'Gate Out Pass -  Returnable' "
                        + "when gio.passType='2' then 'Return Out - Returnable In Pass' end as passType ";
            } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("1")) {
//                type += " and vt.category = '" + vCat + "'";
                condition += " and vt.category = '" + vCat + "'";
                passtype = " case  when gio.passType='0' then 'Issue In Pass - Non-Returnable'  when gio.passType='1' then 'Receive in - Returnable Out Pass' "
                        + "when gio.passType='2' then 'Issue In Pass -  Returnable' end as passType ";
            }

            if (DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("0")) {

                condition += " and isnull(giod.qty,0)-isnull(giod.adj_qty,0)>0 and isNull(giod.refDocID,'')='' and gio.passType<>'0'";
            } else if (DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("1")) {

                condition += " and isnull(giod.qty,0)-isnull(giod.adj_qty,0)=0 ";
            }

            if (!DmShared.xNull(filter.get("entryType").getTag()).trim().equalsIgnoreCase("3") && !DmShared.xNull(filter.get("entryType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and gio.passType='" + DmShared.xNull(filter.get("entryType").getTag()).trim() + "' ";
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND   gio.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }

            sqlQuery = " select gio.recID,gio.v_Date,gio.v_Time," + passtype + ",gio.mode,giod.v_Sno,giod.itemDescription,giod.estdDate,giod.estdValue,giod.purpose,giod.qty\n"
                    + " , vt.category,concat(gio.docID, '|',giod.v_Sno) as gioDocIdV_Sno \n"
                    + ",gio.docId      ,gio.v_Type      ,gio.v_No       ,gio.recId      ,gio.v_Date      ,gio.v_Time      ,gio.partyCode	     ,\n"
                    + "sgp.name as PartyName	         ,gio.gateOutDate      ,gio.gateOutTime      ,\n"
                    + "gio.remark          ,gio.preparedBy      ,gio.preparedDate       ,gio.u_Name      ,\n"
                    + "gio.u_EntDt        ,vt.description AS voucherTypeName          ,gio.u_EntDt_LatestLine             , gio.mode \n"
                    + ", gio.departmentCode      , sgdm.name as departmentName      , gio.destination \n"
                    + "	, giod.itemDescription, giod.unitCode, um1.unit as unitName ,giod.specification, giod.estdValue, giod.assetCode\n"
                    + ", am.manualCode as assetName, giod.purpose, giod.estdDate \n"
                    + "	,case when isNull(giod.refDocID,'')='' then isnull(giod.qty,0)-isnull(giod.adj_qty,0) else 0 end as pendingQty\n"
                    + "	,gin.Qty as adjQty,gin.v_Date as adjDate,gin.v_Time as adjTime,gin.recID as adjRecID\n"
                    + "	,gio.docid,case when  isNull(gio.isCancelled,0) = 1 then 'Discarded' else  '' end as status\n"
                    + "	from gateInOut gio\n"
                    + "	left join gateInOutDetail giod on gio.docID=giod.DocID\n"
                    + "	left join subGroup sgp ON sgp.subCode=gio.partyCode\n"
                    + "	left join departmentMast as dm on dm.subCode = gio.departmentCode\n"
                    + "	left join subGroup as sgdm on sgdm.subCode = dm.subCode\n"
                    + "	left join voucher_Type vt on gio.v_Type=vt.v_Type\n"
                    + "	left join unitMast um1 on um1.code =giod.unitCode\n"
                    + "	left join assetMast am on am.code =giod.assetCode	\n"
                    + "	left join assetItemMast aim on aim.code = am.assetItemCode \n"
                    + "	left join (\n"
                    + "			select giod.refdocid,giod.refv_Sno,giod.Qty,gio.v_Date,gio.v_time,gio.recID\n"
                    + "				from gateInOutdetail giod\n"
                    + "				left join gateInOut gio on giod.docid=gio.docId\n"
                    + "				left join voucher_Type vt on gio.v_Type=vt.v_Type\n"
                    + "			) gin on giod.DocID=gin.refDocID and giod.v_Sno=gin.refv_Sno "
                    + "          WHERE 1=1" + condition
                    + "          ORDER BY gio.v_Date , vt.short_Name , gio.v_No,gio.recId, giod.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("0")) {
                caption = "Gate Pass Register(" + new pmInventory().GATE_PASS_OUT.caption + ")";
            } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("1")) {
                caption = "Gate Pass Register(" + new pmInventory().GATE_PASS_IN.caption + ")";
            } else {
                caption = "Gate Pass Register";
            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "RecId,V_Date,V_Time,passType,mode,itemDescription,estdDate,estdValue,purpose,qty,partyCode,PartyName,gateOutDate,gateOutTime,remark,"
                        + "preparedBy,preparedDate,u_Name,u_EntDt,u_EntDt_LatestLine,mode,departmentName,destination,itemDescription,unitName,specification,estdValue,assetCode,"
                        + "assetName,purpose,estdDate,pendingQty,adjQty,adjDate,adjTime,adjRecID";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {
                    lineData = "\"" + DmShared.xNull(rsTemp.getString("recID")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("v_Date")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("v_Time")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("passType")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("mode")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("itemDescription")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("estdDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("estdValue"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("purpose")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("qty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("partyCode")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("PartyName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("gateOutDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("gateOutTime")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("remark")) + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmShared.xNull(rsTemp.getString("preparedBy")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("preparedDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("u_Name")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("u_EntDt")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("u_EntDt_LatestLine")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("mode")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("departmentName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("destination")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("itemDescription")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("unitName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("specification")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("estdValue")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("assetCode")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("assetName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("purpose")) + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmShared.xNull(rsTemp.getString("estdDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("pendingQty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("adjQty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("adjDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("adjTime")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("adjRecID")) + "\"" + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }

                fileWriter.flush();
                fileWriter.close();

            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                HashMap hm = new HashMap();
                jasperReport = DmServer.compileJasper("Inventory_GateOutRegister");
                jasperPrint = DmServer.fillJasper(this, caption, jasperReport, hm, filter, rsTemp);
                keyField.setSharedKeyFieldName(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            }

            rstTemp.setData(keyField);
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
    public DmResultPageing<List<KeyFieldExtended>> getHRemark(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select DISTINCT replace(destination,',','') destination from gateInOut gio "
                    + "left join voucher_Type vt on vt.v_Type = gio.v_Type\n"
                    + "where vt.category in('" + pvc.GATE_PASS_OUT + "','" + pvc.GATE_PASS_IN + "') and isNull(destination,'') <> '' \n";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("destination")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("destination")));

                lstTemp.add(listNode);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('PR', 'PH') and sg.isActive = 1 ");
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
            rsTemp.close();
            psTemp.close();

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

    private String getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(filter.get("department").getTag()));
            }
            if (!DmShared.xNull(filter.get("destination").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("destination", DmShared.xNull(filter.get("destination").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            psTemp.executeQuery();
        } catch (Exception e) {
        }
        return psTemp.getQuery();
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Gate Pass Out");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Gate Pass In");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Pending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Non Pending");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntryType(String reportType, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            if (DmShared.xNull(reportType).trim().equalsIgnoreCase("0")) {      //Gate Pass Out
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("0");
                listNode.setSharedKeyFieldName("Gate Out Pass - Non-Returnable");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("Gate Out Pass -  Returnable");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("2");
                listNode.setSharedKeyFieldName("Return Out - Returnable In Pass");
                lstTemp.add(listNode);
            } else if (DmShared.xNull(reportType).trim().equalsIgnoreCase("1")) {     //Gate Pass In
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("0");
                listNode.setSharedKeyFieldName("Issue In Pass - Non-Returnable");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("2");
                listNode.setSharedKeyFieldName("Receive in - Returnable Out Pass");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("Issue In Pass -  Returnable");
                lstTemp.add(listNode);
            } else {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("3");
                listNode.setSharedKeyFieldName("--");
                lstTemp.add(listNode);
            }

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
}
