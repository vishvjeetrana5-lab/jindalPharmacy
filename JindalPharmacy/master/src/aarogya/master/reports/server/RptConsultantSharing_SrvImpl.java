package aarogya.master.reports.server;

import static aarogya.master.reports.shared.MIS_Shared.setFontGeneral;
import aarogya.master.reports.shared.RptConsultantSharing_Srv;
import aarogya.master.reports.shared.RptSharingPool_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmMIS;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.self.xls.server.*;
import java.util.UUID;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class RptConsultantSharing_SrvImpl extends DmRSSPrivate implements RptConsultantSharing_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCatLAB = "'" + pvc.LAB_REGISTRATION + "'";

    private final String vOPDSV = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.OPD_SERVICE + "'";
    private final String vIPDSV = "'" + pvc.IPD_SERVICE + "'";
    private final String vIPD = "'" + pvc.IPD + "'";
    private final String vOSRVC = "'" + pvc.OPD_SERVICE_CANCELLATION + "'";
    private final String vOPD = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "' ";
    private final String vOPD_IPD = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "' ,'" + pvc.IPD + "'";

    @Override
    public DmResult<String> execute(String session, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {

        DmResult<String> rstTemp = new DmResult<>();

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        String fileName = "", filePath = "";
        FileWriter fileWriter = null;
        WritableCellFormat cellFormat = null;
        WritableFont cellFont = null;
        String loginDate = "";
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            filePath = DmBaseServer.Path.sessions + "/" + getLocalRequest().getSession().getId().trim();
            fileName = UUID.randomUUID().toString() + ".xls";
            DmXlsWriter xlsWriter = new DmXlsWriter(filePath, fileName);

            xlsWriter = xlsWriter.setWorkSheetCreationCallback(new DmXlsWriter.IXlsWriterWorkSheetCreationCallback() {
                @Override
                public boolean onWorkSheetCreating(DmXlsSheet workSheet) {
                    return true;
                }

                @Override
                public String getDefaultFontName() {
                    return "Arial";
                }

                @Override
                public int getDefaultFontSize() {
                    return 10;
                }

                @Override
                public Alignment getDefaultHorizontalAlignment() {
                    return Alignment.LEFT;
                }

                @Override
                public VerticalAlignment getDefaultVerticalAlignment() {
                    return VerticalAlignment.CENTRE;
                }
            }).build(new pmMIS().CONSULTANT_SHARING.caption);

            DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
            workSheet.setDefaultFontName(fileName);

            workSheet.getWorkSheet().getSettings().setShowGridLines(false);

            ArrayList<String> headerName = new ArrayList<String>();

            headerName.add("S.No.");
            headerName.add("Department");
            headerName.add("Working Days");
            headerName.add("Consultant");

            ArrayList<String> poolConfigList = new ArrayList<String>();
            String sqlQuery = "select name as poolConfigName from poolMast "
                    + "  order by name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                poolConfigList.add(DmShared.xNull(rsTemp.getString("poolConfigName")));

            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

//            int prevLengthHeader=headerName.length;
            for (int q = 0; q < poolConfigList.size(); q++) {
                headerName.add(poolConfigList.get(q));
            }

            headerName.add("Total");
            headerName.add("Less Share to MO");
            headerName.add("Net Share");
            headerName.add("Individual  Share Amount %");
            headerName.add("Individual Share Amount");

            //=====================Add Collumn Header============================
            Label lbl;
            jxl.write.Number number;

            workSheet.mergeCells(1, 0, headerName.size(), 0);
            workSheet.getWorkSheet().setRowView(0, 500);
            cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
            lbl = new Label(1, 0, DmServer.getSsnSiteDisplayName(this), cellFormat);
            workSheet.getWorkSheet().addCell(lbl);

            workSheet.mergeCells(1, 1, headerName.size(), 1);
            workSheet.getWorkSheet().setRowView(1, 400);
            cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
            cellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THICK);

            lbl = new Label(1, 1, new pmMIS().CONSULTANT_SHARING.caption, cellFormat);
            workSheet.getWorkSheet().addCell(lbl);

            //==============================Set Header2====================================================
            workSheet.getWorkSheet().setRowView(3, 1200);
            cellFormat = setFontGeneral(true, Alignment.CENTRE, Colour.LIGHT_BLUE, false, true, false, UnderlineStyle.NO_UNDERLINE);
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            for (int i = 0; i < headerName.size(); i++) {

                workSheet.getWorkSheet().setColumnView(i + 1, 14);

                if (headerName.get(i).length() > 5 && headerName.get(i).substring(0, 6).equalsIgnoreCase("_blank")) {
                    lbl = new Label(i + 1, 3, "", cellFormat);
                } else {
                    lbl = new Label(i + 1, 3, headerName.get(i), cellFormat);
                }

                workSheet.getWorkSheet().addCell(lbl);
            }

            //==============================Fill Data====================================================
            List<RptSharingPool_Mdl> mrTemp = getData(filter);
            String previousPoolName = "";
            int strtMergRowIndex = 0;
            int endMergRowIndex = 0;
            if (mrTemp.size() > 0) {
                for (int j = 0; j < mrTemp.size(); j++) {

                    if (j % 2 == 0) {
                        cellFormat = setFontGeneral(false, Alignment.RIGHT, Colour.LAVENDER, false, true, false, UnderlineStyle.NO_UNDERLINE);
                        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

                    } else if (j % 2 != 0) {
                        cellFormat = setFontGeneral(false, Alignment.RIGHT, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                    }

                    if (!previousPoolName.equalsIgnoreCase(DmShared.xNull(mrTemp.get(j).getPoolName()))) {
                        lbl = new Label(1, j, mrTemp.get(j).getPoolName(), cellFormat);
                        workSheet.getWorkSheet().addCell(lbl);
                        strtMergRowIndex = j;
                    } else {
                        workSheet.mergeCells(1, strtMergRowIndex, 1, endMergRowIndex);
                    }

                    lbl = new Label(1, j, mrTemp.get(j).getConsultantName(), cellFormat);
                    workSheet.getWorkSheet().addCell(lbl);

                    previousPoolName = DmShared.xNull(mrTemp.get(j).getPoolName());
                    endMergRowIndex = j;
                }

                workSheet.mergeCells(1, mrTemp.size() + 4, 24, mrTemp.size() + 4);
                cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                cellFormat.setBorder(Border.TOP, BorderLineStyle.MEDIUM);
                lbl = new Label(1, mrTemp.size() + 4, "*End of Report*", cellFormat);
                workSheet.getWorkSheet().addCell(lbl);

                workSheet.mergeCells(1, mrTemp.size() + 5, 24, mrTemp.size() + 5);
                cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

                lbl = new Label(1, mrTemp.size() + 5, "Printed by " + DmServer.getSsnUserName(this) + " on " + loginDate, cellFormat);
                workSheet.getWorkSheet().addCell(lbl);

            }
            //=========
            xlsWriter.commit();
            xlsWriter.close();

            //==============
            rstTemp.setData(fileName);
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());

        } finally {
            try {

                if (fileWriter != null) {
                    fileWriter.close();
                }

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

    private int getHeaderIndex(String[] headerName, String element) {
        int headerIndex = 0;

        if (headerName.length > 0) {
            for (int i = 0; i < headerName.length; i++) {
                if (headerName[i].equalsIgnoreCase(element)) {
                    headerIndex = i + 1;
                    break;
                }
            }
        }

        return headerIndex;
    }

    private List<RptSharingPool_Mdl> getData(LinkedHashMap<String, FieldUDT_Mdl> filter) {

        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<RptSharingPool_Mdl> mrTemp = new ArrayList();
        String sqlQuery = "", condition = "";
        RptSharingPool_Mdl node;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select max(pgm.name) as poolName, max(sg.name) as consultantName, max(pm.name) as poolConfigName,"
                    + " sum(psd.oh_Amt_Net) as amount \n"
                    + "from patientServiceDetail as psd\n"
                    + "left join patientServiceMast as psm on psm.code=psd.serviceCode\n"
                    + "left join patientServiceCatMast as pscm on pscm.code=psm.categoryCode\n"
                    + "left join roomMast as rm on rm.code=psd.roomCode\n"
                    + "left join roomCatMast as rcm on rcm.code=rm.roomCategoryCode\n"
                    + "left join poolMast as pm on pm.serviceCategoryCode=pscm.code and pm.roomCategoryCode=rcm.code\n"
                    + "left join subGroup sg on sg.subCode=psd.consultantCode\n"
                    + "left join poolGroupDetailLine pgl on pgl.consultantCode=psd.consultantCode\n"
                    + "left join poolGroupDetailHeader as pgd on pgd.code=pgl.code\n"
                    + "left join poolGroupMast as pgm on pgm.code=pgd.poolGroupCode\n"
                    + "\n"
                    + "where isnull(pm.name,'')<>'' and psd.v_date between '01-Apr-2021' and '30-Apr-2021' and  isnull(pgm.name,'')<>''\n"
                    + "group by pgm.name , pm.name, sg.name  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
//            psTemp.setString("countryLocale", DmBaseServer.baseDmLocaleCountry.getDmCode());
//            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
//            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                node = new RptSharingPool_Mdl();

                node.setPoolName(DmShared.xNull(rsTemp.getString("poolName")));
                node.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                node.setPoolConfigName(DmShared.xNull(rsTemp.getString("poolConfigName")));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));

                mrTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

        } catch (Exception e) {
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

        return mrTemp;
    }

    private DmPreparedStatement getPreparedStatement(HashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
//            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
//            if (!DmShared.xNull(filter.get("dapartment").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("dapartment", DmShared.xNull(filter.get("dapartment").getTag()));
//            }
//            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("consultant", DmShared.xNull(filter.get("consultant").getTag()));
//            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
        return psTemp;
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
            sqlWithParam.setSQL("SELECT sdm.subCODE, sdm.NAME "
                    + " FROM departmentMast dm "
                    + " left join subgroup sdm on dm.subcode = sdm.subcode "
                    + " Left join subGroupSiteList as sgsl on sdm.subCode=sgsl.subCode "
                    + " WHERE  sdm.isActive = 1 and  sgsl.site_Code=:siteCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultantType(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select cm.subcode as code,sg.name "
                    + " from consultantmast cm "
                    + " left join subGroup as sg ON cm.subCode = sg.subCode "
                    + " left join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " left join subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " where CSL.site_Code='" + DmServer.getSsnSiteCode(this) + "' AND sg.isActive='1' AND sgn.manualCode='CN' ");

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
