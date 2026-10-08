/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.RptUHIDRegistration_Srv;
import aarogya.mis.reports.shared.RptUHIDRegistration_Vld;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
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

/**
 *
 * @author dataman
 */
public class RptUHIDRegistration_SrvImpl extends DmRSSPrivate implements RptUHIDRegistration_Srv{

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint;
        String sqlQuery = "";
        String condition = "", reportName = "";
        
        try{
            RptUHIDRegistration_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            condition = " (pu.v_Date between :fromDate and :toDate) ";
            
            if (!DmShared.xNull(filter.get("city").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  pu.cityCode IN(" + DmServer.getParamList(":city", filter.get("city").getTag()) + " )";
            }
            
            if (!DmShared.xNull(filter.get("area").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  pu.zoneCode IN(" + DmServer.getParamList(":area", filter.get("area").getTag()) + " )";
            }
            
            sqlQuery= " select pu.recId as uhid, pu.patientName, pu.v_Date, pu.mobile, gen.name as gender, pu.dobDisplay as age, " +
                    " bld.name as bloodgroup,pu.address1 as address, ct.cityName as city, zm.name as area, org.name as organizationName " +
                    " from patientUHID pu " +
                    " left join patientUHID2 pu2 on pu.docId = pu2.uhid " +
                    " left join hl7Mast gen on pu2.genderCode = gen.code " +
                    " left join hl7Mast bld on pu2.bloodGroupCode = bld.code " +
                    " left join city ct on pu.cityCode = ct.cityCode " +
                    " left join zoneMast zm on pu.zoneCode = zm.code " +
                    " left join subgroup org on org.subCode = pu.organizationCode "+
                    " where " + condition+" order by city,area,v_date,pu.v_no,uhid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("city").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("city", DmShared.xNull(filter.get("city").getTag()));
            }
            if (!DmShared.xNull(filter.get("area").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("area", DmShared.xNull(filter.get("area").getTag()));
            }
            
            rsTemp = psTemp.executeQuery();
            jasperReport = DmServer.compileJasper("MIS_UHIDRegistration");
                  
            jasperPrint = DmServer.fillJasper(this, widgetName, jasperReport, null, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
            
        }catch(Exception e){
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
        }finally{
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        
        try{
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select cityCode, cityName from city ";
            
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setData(lstTemp);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            
        }catch(Exception e){
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }finally{
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHArea(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        
        try{
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            
            sqlQuery = " select code, name from zoneMast ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setData(lstTemp);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        }catch(Exception e){
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }finally{
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
