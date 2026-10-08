package aarogya.pharmacy.saleBillSearch.server;

import aarogya.pharmacy.changePaymentSource.shared.ChangePaymentSource_Mdl.SaleBillSearchType;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.pharmacy.saleBillSearch.shared.*;
import aarogya.pharmacy.saleBillSearch.shared.SearchValueWise_Mdl.FilterType;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SaleBillSearch_SrvImpl extends DmRSSPrivate implements SaleBillSearch_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<GrdSaleBillSearch_Mdl>> getFind(String type, DmHelper helper, boolean blnToEndOfFile, SearchValueWise_Mdl param) {
        DmResultPageing<List<GrdSaleBillSearch_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdSaleBillSearch_Mdl> lstTemp = new ArrayList<>();
        GrdSaleBillSearch_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", category = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(type).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Type is mandatory.");
            } else if (type.equalsIgnoreCase(DmShared.iNull(SaleBillSearchType.SALE_BILL.getId()).toString())) {
                category = pvc.PHARMACY_BILL+"','"+pvc.PHARMACY_BILL_CASH;
            } else if (type.equalsIgnoreCase(DmShared.iNull(SaleBillSearchType.SALE_BILL_RETURN.getId()).toString())) {
                category = pvc.PHARMACY_BILL_RETURN+"','"+pvc.PHARMACY_CASH_SALE_RETURN;
            }

//            sqlQuery = " SELECT mb.DocId, mb.recId AS billNo,"
//                    + "    mb.v_Date, pr.recId AS regNo, pu.recId AS UHIDNo,"
//                    + "    pbvt.short_Name AS vType_ShortName, mb.v_no, mb.recId, "
//                    + "    sdm.name AS departmentName, lm.displayName as patientName, "
//                    + "    sgcon.name AS consultantName, "
//                    + "    CASE WHEN mb.isCancelled = 1 THEN 'Discarded' ELSE '' END as status "
//                    + "    FROM saleBill mb "
//                    + "    LEFT JOIN voucher_type AS pbvt ON mb.v_Type = pbvt.v_Type "
//                    + "    LEFT JOIN patientregistration pr ON mb.regDocid = pr.regDocid "
//                    + "    LEFT JOIN voucher_type AS prvt ON pr.v_Type = prvt.v_Type "
//                    + "    LEFT JOIN patientuhid pu ON mb.UHID = pu.docId "
//                    + "    LEFT JOIN ledgerMast lm ON lm.code = mb.lmCode "
//                    + "    LEFT JOIN voucher_type AS puvt ON pu.v_Type = puvt.v_Type "
//                    + "    LEFT JOIN subgroup sdm ON mb.departmentCode = sdm.subcode "
//                    + "    LEFT JOIN subGroup AS sgcon ON mb.consultantCode  = sgcon.subCode "
//                    + "    WHERE mb.site_Code =:siteCode   "
//                    + "    AND pbvt.category IN (" + "'" + condition + "'" + ")"
//                    + "    AND mb.v_prefix =:vPrefix ";
            sqlQuery = " SELECT mb.DocId, mb.recId AS billNo, vt.short_Name AS vType_ShortName, mb.v_no, "
                    + "    mb.v_Date, pr.recId AS regNo, pu.recId AS UHIDNo,"
                    + "    lm.displayName as patientName, "
                    + "    sgcon.name AS consultantName "
                    + "    FROM saleBill mb "
                    + "    LEFT JOIN voucher_type AS vt ON mb.v_Type = vt.v_Type "
                    + "    LEFT JOIN ledgerMast lm ON lm.code = mb.lmCode "
                    + "    LEFT JOIN patientregistration pr ON mb.regDocid = pr.regDocid "
                    + "    LEFT JOIN patientuhid pu ON mb.UHID = pu.docId "
                    + "    LEFT JOIN subGroup AS sgcon ON mb.consultantCode  = sgcon.subCode "
                    + "    WHERE mb.site_Code =:siteCode   "
                    + "    AND vt.category IN (" + "'" + category + "'" + ")"
                    + "    AND mb.v_prefix =:vPrefix ";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmShared.iNull(DmServer.getSsnVoucherPrefix(this)).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdSaleBillSearch_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setBillNo(DmShared.xNull(rsTemp.getString("billNo")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setUHID(DmShared.xNull(rsTemp.getString("UHIDNo")));
                listNode.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
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
