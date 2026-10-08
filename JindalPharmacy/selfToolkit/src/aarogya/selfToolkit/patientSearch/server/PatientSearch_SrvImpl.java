package aarogya.selfToolkit.patientSearch.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.patientSearch.shared.GrdPatientDetails_Mdl;
import aarogya.selfToolkit.patientSearch.shared.PatientSearch_Srv;
import aarogya.selfToolkit.patientSearch.shared.SearchValue_Mdl;
import aarogya.selfToolkit.patientSearch.shared.SearchValue_Mdl.FilterType;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PatientSearch_SrvImpl extends DmRSSPrivate implements PatientSearch_Srv {

    private final String vCat = new ProjectVoucherCategory().UHID;
    private final String vCatIPD = new ProjectVoucherCategory().IPD;

    @Override
    public DmResultPageing<List<GrdPatientDetails_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdPatientDetails_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdPatientDetails_Mdl> listNode = new ArrayList<>();
        GrdPatientDetails_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        FilterType filterType;
        String condition = "";
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            for (int i = 0; i < param.getFilterType().length; i++) {
                filterType = param.getFilterType()[i];
                if (filterType == FilterType.WITHOUT_EXPIRED) {
                    condition += " and pu.isExpired = 0 ";
                }
            }

            String allowSiteFor = DmServer.getCodeViaManualCode(DmServer.getSsnSiteCode(this), "site_Code", "enviro", "opdAllowPatientForSite", cnTemp);
            if (!DmShared.xNull(allowSiteFor).equalsIgnoreCase("")) {
                condition += " and pu.site_Code in(" + allowSiteFor + ") ";
            }

            sqlWithParam.setSQL("Select	pu.docId, pu.recId AS uhidNo, pu.v_Date, pu.v_No, pu.patientName, "
                    + "                 pu.guardianName, pu.mobile, pu.idNo, vt.short_Name , ipdStatus.Admitstatus  "
                    + "                 from patientuhid as pu  with(nolock)   "
                    + "                 left join voucher_type as vt on pu.v_Type = vt.v_Type "
                    + "  "
                    + " left join "
                    + "(Select	case when isnull(pr.regDocId,0)=0 then 0 else 1 end  as Admitstatus , pr.uhid  "
                    + "from patientRegistration as pr   with(nolock) "
                    + "left join patientDischarge as pd on pr.regDocId = pd.regDocId  "
                    + "left join voucher_Type as vt on pr.v_Type = vt.v_Type  "
                    + "where  pd.regDocId is null  "
                    + "and pr.isCancelled =0  "
                    + "and vt.category in ('" + vCatIPD + "')) as ipdStatus on ipdStatus.uhid=pu.docId  "
                    + "  Where pu.isCancelled = 0 and vt.category = '" + vCat + "' and isNull(pu.approvedBy,'') <> ''  " + condition + ""
                    + " ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPatientDetails_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("uhidNo")));
                node.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                node.setGuardianName(DmShared.xNull(rsTemp.getString("guardianName")));
                node.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                node.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));

                node.setPatientAdmitted(DmShared.iNull(rsTemp.getString("Admitstatus")).intValue() == 1);

                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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
