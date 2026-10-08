package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.RptCancelServiceRefundReport_Srv;
import aarogya.mis.reports.shared.*;
import aarogya.mis.reports.shared.RptCancelServiceRefundReport_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.*;
import java.util.*;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptCancelServiceRefundReport_SrvImpl extends DmRSSPrivate implements RptCancelServiceRefundReport_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
  //  private final String vrCat = "'" + pvc.LAB_REFUND_RECEIPT + "'";
    private  String vtrCat = "";
    private  String vtCat = "";
    private  String vtsCat = "";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName,String moduleName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery;
        String condition;
        String joinCondition;
        String reportName = "";

        try {
            RptCancelServiceRefundReport_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and (ref.v_Date between  :fromDate  and  :toDate) ";
            reportName = widgetName;
  //============================<FILTERS>=============================================
            if (!DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase("")) {
               // condition += " and pr.consultantCode in(" + DmServer.getParamList(":consultant", filter.get("consultant").getTag()) + " )";
               vtrCat = "'"+filter.get("module").getTag()+"'";
               reportName = moduleName +" "+ widgetName; 
            }
           
            if(vtrCat.equalsIgnoreCase( "'"+pvc.LAB_REGISTRATION+"'")){    
                joinCondition = "left join subGroup con on con.subCode = pr.referredBy  ";
                 if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and pr.referredBy in(" + DmServer.getParamList(":consultant", filter.get("consultant").getTag()) + " )";
                }
            }else{
                joinCondition = "left join subGroup con on con.subCode = pr.consultantCode  ";
                 if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and pr.consultantCode in(" + DmServer.getParamList(":consultant", filter.get("consultant").getTag()) + " )";
                }
            }
            
            
            if(!DmShared.xNull(filter.get("regDocId").getTag()).trim().equalsIgnoreCase("")){    
                condition += " and pr.regDocId in(" + DmServer.getParamList(":regDocId", filter.get("regDocId").getTag()) + " )";
                }
            
//====================<OPD CANCELLATION REPORT>================================
        if(vtrCat.equalsIgnoreCase( "'"+pvc.OPD+"','"+pvc.EMERGENCY_OPD+"','"+pvc.PAY_CLINIC+"'"))
        {
            vtCat = "'"+pvc.OPD_REFUND_RECEIPT+"'";
            vtsCat = "'"+pvc.OPD_SERVICE_CANCELLATION+"'";
        }
//=======================<IPD CANCELLATION REPORT>==================================
        if(vtrCat.equalsIgnoreCase( "'"+pvc.IPD+"'"))
        {
            vtCat = "'"+pvc.IPD_REFUND_RECEIPT+"','"+pvc.PATIENT_REFUND_RECEIPT+"'";
            vtsCat = "'"+pvc.IPD_SERVICE_CANCELLATION+"'";
        }
 //========================<DIAGNOSTIC CANCELLATION REPORT>=====================
        if(vtrCat.equalsIgnoreCase( "'"+pvc.LAB_REGISTRATION+"'"))
        {
            vtCat = "'"+pvc.LAB_REFUND_RECEIPT+"'";
            vtsCat = "'"+pvc.LAB_CANCELATION+"'";
        }
 //========================<EMERGENCY CANCELLATION REPORT>=====================
        if(vtrCat.equalsIgnoreCase( "'"+pvc.EMERGENCY+"'"))
        {
            vtCat = "'"+pvc.EMERGENCY_REFUND_RECEIPT+"'";
            vtsCat = "'"+pvc.EMERGENCY_SERVICE_CANCELLATION+"'";
        }
            

           sqlQuery = "select pr.regDocId as docId,pr.uhid AS [uhid/docId],ref.recNo,ref.v_Date [Date],pr.recId as regNo,pu.recId as UHID,\n"
                   + "pu.patientName,pr.dobDisplay,gen.shortName as gender,\n"
                   + "case when isnull(con.initial, '') = '' \n"
                   + "then con.name else concat(con.initial, ' ', con.name) \n"
                   + "end as consultant ,\n"
                   + "psm.name as serviceName,\n"
                   + "psd.oh_Amt_Net as serviceAmount,\n"
                   + "isNull(ref.refundAmount,0) as RefundAmount,ref.preparedBy, ref.remark\n"
                   + "from patientServiceDetail as psd\n"
                   + "left join patientRegistration pr on pr.regDocId = psd.regDocId\n"
                   + "left join voucher_Type vtr on vtr.v_Type = pr.v_Type\n"
                   + "left join patientServiceMast psm on psm.code = psd.serviceCode\n"
                   + "left join patientUHID pu on pu.docId = pr.uhid\n"
                   + "left join patientUHID2 pu2 on pu2.uhid = pu.docId  \n"
                   + "left join hl7Mast gen on gen.code = pu2.genderCode  \n"
                   + "left join voucher_Type vts on vts.v_Type = psd.v_Type\n"
                   + "--left join subGroup con on con.subCode = pr.consultantCode \n"
                   + joinCondition
                   + "left join \n"
                   + "(\n"
                   + "select max(prc.regDocId) regDocId,sum(prc.amount) as refundAmount ,max(prc.v_Date) v_Date,\n"
                   + "max(prc.recId) as recNo,max(prc.preparedBy) preparedBy, max(prc.remark) remark\n"
                   + "from patientReceipt prc\n"
                   + "left join voucher_Type vt on vt.v_Type = prc.v_Type\n"
                   + "\n"
                   + "where vt.category in("+vtCat+") and prc.isCancelled = 0 \n"
                   + "group by prc.regDocId\n"
                   + ") as ref on ref.regDocId = pr.regDocId\n"
                   + "\n"
                   + "where vtr.category in ("+vtrCat+") \n"
                   + "and psd.site_Code = 1 and vts.category in ("+vtsCat+")\n"+condition
                   + "order by pr.regDocId,psd.v_Date";
            
           

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
      
            if (!DmShared.xNull(filter.get("consultant").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultant", DmShared.xNull(filter.get("consultant").getTag()), true);
                }

            if (!DmShared.xNull(filter.get("regDocId").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("regDocId", DmShared.xNull(filter.get("regDocId").getTag()), true);
                }

            rsTemp = psTemp.executeQuery();
            LinkedHashMap hm = new LinkedHashMap();

            jasperReport = DmServer.compileJasper("MIS_CancelServiceRefundReport");
            jasperPrint = DmServer.fillJasper(this, reportName, jasperReport, hm, filter, rsTemp);
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
    
    @Override
    public DmResultPageing<List<TxtHReg_Mdl>> getTxtHRegNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHReg_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHReg_Mdl> lstTemp = new ArrayList<>();
        TxtHReg_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition, sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select pr.v_Date v_Date,pr.regDocId regDocId ,pu.recId as uhidName, pr.recId as regNo ,pu.patientName,pu.mobile\n"
                    + "from patientRegistration pr\n"
                    + "left join patientUhid pu on pu.docid=pr.uhid";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHReg_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("regDocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setPersonName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setUhidNo(DmShared.xNull(rsTemp.getString("uhidName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.OPD+"','"+pvc.EMERGENCY_OPD+"','"+pvc.PAY_CLINIC);
            listNode.setSharedKeyFieldName("OPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.IPD);
            listNode.setSharedKeyFieldName("IPD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.LAB_REGISTRATION);
            listNode.setSharedKeyFieldName("Diagnostic");
            lstTemp.add(listNode);
           
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.EMERGENCY);
            listNode.setSharedKeyFieldName("Emergency");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select	cm.subCode as code, sg.name "
                    + "		from consultantMast as cm  "
                    + "		left join subGroupSiteList as sgsl on cm.subCode = sgsl.subCode "
                    + "		left join subGroup as sg on cm.subCode = sg.subCode  "
                    + "		left join subgroupNature as sgn on sg.nature = sgn.code  "
                    + "		where sgn.manualCode = 'CN' and sg.isActive = 1 and sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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

}
