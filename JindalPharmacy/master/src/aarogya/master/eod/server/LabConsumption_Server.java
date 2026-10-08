package aarogya.master.eod.server;

import aarogya.master.eod.shared.AcPosting_Mdl;
import aarogya.master.eod.shared.EOD_Mdl;
import aarogya.master.eod.shared.GrdLabConsumptionDetail_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ProcessEOD;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LabConsumption_Server extends AbstractEventHandler<Event> {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCatLC = pvc.LAB_CONSUMPTION_POSTING;
    private String filterLCCat = "'" + pvc.LAB_REGISTRATION + "'";

    public LabConsumption_Server() {
        super(STEnum.ProcessEOD.LAB_CONSUMPTION.getValue());
    }

    @Override
    public void handle(Event event) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        SchedulerMast_Mdl schedulerMast_Mdl;
        EOD_Mdl eodMdl = new EOD_Mdl();
        String sqlQuery;
        SimpleDateFormat simpleDateFormat;

        try {
            schedulerMast_Mdl = event.get(DmEnum.DmEventSystemArgs.SchedulerMaster_Model.toString(), SchedulerMast_Mdl.class);

            cnTemp = DmServer.getConnection(schedulerMast_Mdl.getActiveDB());
            spBegin = new DmSavePoint(cnTemp);

            Date dtTemp = event.get(DmEnum.DmEventSystemArgs.CurrentDateTime.toString(), Date.class);
            simpleDateFormat = new SimpleDateFormat("HH.MM");

            for (Integer i : schedulerMast_Mdl.getLstSites()) {
                if (DmShared.iNull(i).intValue() != 0) {
                    eodMdl = new EOD_Mdl();

                    eodMdl.setCurrentDate(DmShared.dNull(dtTemp));
                    eodMdl.setCurrentTime(DmServer.vNull(simpleDateFormat.format(dtTemp), "0.00"));
                    eodMdl.setCompanyCode(DmShared.xNull(schedulerMast_Mdl.getCompanyCode()));
                    eodMdl.setDatabaseName(DmShared.xNull(schedulerMast_Mdl.getActiveDB()));
                    eodMdl.setVoucherPrefix(DmShared.xNull(schedulerMast_Mdl.getVoucherPrefix()));
                    eodMdl.setUserName(schedulerMast_Mdl.getUserName());
                    eodMdl.setSiteCode(i.toString());

                    sqlQuery = "Select manualCode from siteMast where code = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(eodMdl.getSiteCode()).toString());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        eodMdl.setSiteShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                    }
                    rsTemp.close();
                    psTemp.close();

                    /*====================================< Save Lab Consumption Posting >====================================*/
                    AcPosting_Mdl posting_Mdl = new AcPosting_Mdl();
                    posting_Mdl.setForDate(eodMdl.getCurrentDate());
                    save(posting_Mdl, eodMdl, cnTemp);
                }
            }

            spBegin.commit();
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            throw new UnsupportedOperationException(e.getMessage());
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
    }

    public List<GrdLabConsumptionDetail_Mdl> getLCDeleteDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<GrdLabConsumptionDetail_Mdl> lstLCDetail = new ArrayList<GrdLabConsumptionDetail_Mdl>();
        GrdLabConsumptionDetail_Mdl listNode;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get records of Accounting >====================================*/
            sqlQuery = "Select	sh.docId, sh.recId as voucherNo, shd.itemCode, im.name as itemName, im.sku as unitCode, "
                    + "		um.unit as unitName, shd.quantity, shd.departmentCode, dep.name as departmentName "
                    + "		from stockHeadDetail as shd "
                    + "		left join stockHead as sh on shd.docId = sh.docId "
                    + "		left join voucher_Type as vt on sh.v_Type = vt.v_Type "
                    + "		left join itemMast as im on shd.itemCode = im.code "
                    + "		left join unitMast as um on shd.unitCode = um.code "
                    + "		left join subGroup as dep on shd.departmentCode = dep.subCode "
                    + "		where vt.category = '" + vCatLC + "' and sh.v_Date = :v_Date and sh.site_Code = :site_Code and sh.v_Prefix = :v_Prefix "
                    + "		order by sh.recId, im.name, um.unit ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Prefix", eodMdl.getVoucherPrefix());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdLabConsumptionDetail_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("itemCode")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setVoucherNo(DmShared.xNull(rsTemp.getString("voucherNo")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                listNode.setQuantity(DmServer.vNull(rsTemp.getString("quantity"), "0.00"));
                lstLCDetail.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
            throw new UnsupportedOperationException(e.getMessage());
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
        return lstLCDetail;
    }

    public List<GrdLabConsumptionDetail_Mdl> getLCPostingDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<GrdLabConsumptionDetail_Mdl> lstLCDetail = new ArrayList<GrdLabConsumptionDetail_Mdl>();
        GrdLabConsumptionDetail_Mdl listNode;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get records of Accounting >====================================*/
            sqlQuery = "Select	bom.itemCode, max(im.name) as itemName, im.sku as unitCode, max(um.unit) as unitName, "
                    + "         esc.defaultDepartmentCode as departmentCode, max(dep.name) as departmentName, "
                    + "         isNull(sum(bom.quantity),0) as quantity "
                    + "		from patientRegistration as pr "
                    + "         left join patientRegistration2 as pr2 on pr.regDocId = pr2.regDocId "
                    + "		left join patientServiceDetail as psd on pr.regDocId = psd.regDocId "
                    + "         left join patientServiceMast as psm on psd.serviceCode = psm.code "
                    + "		left join patientServiceCatMast as pscm on psm.categoryCode = pscm.code "
                    + "		left join enviroServiceCategory esc on esc.site_Code = :site_Code and esc.serviceCategoryCode = pscm.code "
                    + "		left join subGroup as dep on esc.defaultDepartmentCode = dep.subCode "
                    + "		left join ( "
                    + "                 select  bom.*, om.subCode,om.shortname, rank() over(partition by om.subCode order by organizationCode desc) as flag "
                    + "                         from organizationMast om,patientServiceBOM bom "
                    + "				where om.subcode = isNull(bom.organizationCode,om.subCode) "
                    + "		) as bom on bom.serviceCode = psd.serviceCode and bom.subcode = pr.organizationCode and bom.flag = 1 "
                    + "		left join itemMast as im on bom.itemCode = im.code "
                    + "		left join unitMast as um on im.sku = um.code "
                    + "		left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                    + "		where (pr.isCancelled = 0) and (psd.isCancelled = 0) and (vt.category = " + filterLCCat + ") "
                    + "         and (bom.serviceCode is not null) and (pr2.shDocId is null) and (psd.site_Code = :site_Code) "
                    + "         and (psd.v_Date = :v_Date) and (isNull(bom.quantity,0) <> 0) and (pr.v_Prefix = :v_Prefix) and (isNull(im.propertyList,'') = '') "
                    + "		group by bom.itemCode, im.sku, esc.defaultDepartmentCode "
                    + "		order by departmentName, itemName, unitName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Prefix", eodMdl.getVoucherPrefix());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdLabConsumptionDetail_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("itemCode")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                listNode.setQuantity(DmServer.vNull(rsTemp.getString("quantity"), "0.00"));
                lstLCDetail.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
            throw new UnsupportedOperationException(e.getMessage());
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
        return lstLCDetail;
    }

    public List<KeyFieldExtended> getPendingDates(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<KeyFieldExtended> lstPendingDate = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get pending Dates of ledgerProvisional >====================================*/
            sqlQuery = "Select	psd.v_Date "
                    + "		from patientRegistration as pr "
                    + "         left join patientRegistration2 as pr2 on pr.regDocId = pr2.regDocId "
                    + "		left join patientServiceDetail as psd on pr.regDocId = psd.regDocId "
                    + "		left join ( "
                    + "                 select  bom.*, om.subCode,om.shortname, rank() over(partition by om.subCode order by organizationCode desc) as flag "
                    + "                         from organizationMast om,patientServiceBOM bom "
                    + "				where om.subcode = isNull(bom.organizationCode,om.subCode) "
                    + "		) as bom on bom.serviceCode = psd.serviceCode and bom.subcode = pr.organizationCode and bom.flag = 1 "
                    + "		left join itemMast as im on bom.itemCode = im.code "
                    + "		left join unitMast as um on im.sku = um.code "
                    + "		left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                    + "		where (pr.isCancelled = 0) and (psd.isCancelled = 0) and (vt.category = " + filterLCCat + ") "
                    + "         and (bom.serviceCode is not null) and (pr2.shDocId is null) and (psd.site_Code = :site_Code) "
                    + "         and (psd.v_Date <= :v_Date) and (isNull(bom.quantity,0) <> 0) and (pr.v_Prefix = :v_Prefix) and (isNull(im.propertyList,'') = '') "
                    + "		group by psd.v_Date "
                    + "		order by psd.v_Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.setString("v_Prefix", eodMdl.getVoucherPrefix());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("v_Date")));
                lstPendingDate.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
            throw new UnsupportedOperationException(e.getMessage());
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
        return lstPendingDate;
    }

    public void save(AcPosting_Mdl mrTemp, EOD_Mdl eodMdl, Connection cnTemp) {
        List<GrdLabConsumptionDetail_Mdl> lstLCDetail = new ArrayList<GrdLabConsumptionDetail_Mdl>();
        DmPreparedStatement psTemp = null;
        String sqlQuery, voucherType, regDocId = "";
        ResultSet rsTemp = null;

        try {
            voucherType = DmServer.getVType(vCatLC, cnTemp);

            /*====================================< Check whether EOD already done or not >====================================*/
            sqlQuery = "Select docId, v_No, recId, recIdPrefix from stockHead Where v_Date = :v_Date and v_Type = :v_Type and site_Code= :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")).trim());
                mrTemp.setObjRecId(new RecId());
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")).trim());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")).trim());
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
            }
            rsTemp.close();
            psTemp.close();

            /*===============< Delete EOD >==============*/
            delete(eodMdl, cnTemp, false);

            /*====================================< Get docId's of patientServiceBOM >====================================*/
            sqlQuery = "Select	pr.regDocId "
                    + "		from patientRegistration as pr "
                    + "		left join patientRegistration2 as pr2 on pr.regDocId = pr2.regDocId "
                    + "		left join patientServiceDetail as psd on pr.regDocId = psd.regDocId  "
                    + "		left join ( "
                    + "                 select  bom.*, om.subCode,om.shortname, rank() over(partition by om.subCode order by organizationCode desc) as flag "
                    + "                         from organizationMast om,patientServiceBOM bom "
                    + "				where om.subcode = isNull(bom.organizationCode,om.subCode) "
                    + "		) as bom on bom.serviceCode = psd.serviceCode and bom.subcode = pr.organizationCode and bom.flag = 1 "
                    + "		left join itemMast as im on bom.itemCode = im.code "
                    + "		left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                    + "		where (pr.isCancelled = 0) and (vt.category = " + filterLCCat + ") and (pr2.shDocId is null) "
                    + "         and (bom.serviceCode is not null) and (pr.v_Prefix = :v_Prefix) and (psd.isCancelled = 0) "
                    + "         and (psd.site_Code = :site_Code) and (psd.v_Date = :v_Date) and (isNull(bom.quantity,0) <> 0) and (isNull(im.propertyList,'') = '') "
                    + "		group by pr.regDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setString("v_Prefix", eodMdl.getVoucherPrefix());
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getForDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                regDocId += "'" + DmShared.iNull(rsTemp.getString("regDocId")).toString() + "',";
            }
            rsTemp.close();
            psTemp.close();

            if (regDocId.trim().length() == 0) {
                throw new DmException(0, "No record found.");
            }

            if (regDocId.trim().contains(",")) {
                regDocId = regDocId.substring(0, regDocId.length() - 1);
            }

            if (DmShared.xNull(regDocId).trim().equalsIgnoreCase("")) {
                regDocId = "0";
            }

            /*====================================< Check whether already posted or not >====================================*/
            sqlQuery = "Select regDocId from patientRegistration2 where (shDocId is not null) and (regDocId in (" + regDocId + ")) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Already Lab Consumption posted.");
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Get records of Lab Consumption >====================================*/
            lstLCDetail = getLCPostingDetail(eodMdl, cnTemp);

            if (lstLCDetail.size() > 0) {
                if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0l) {
                    mrTemp = new AcPosting_Mdl();
                    mrTemp.setSharedKeyFieldCode(DmServer.getDocId(voucherType, eodMdl.getVoucherPrefix(), eodMdl.getSiteCode(), cnTemp));
                    mrTemp.setObjRecId(DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), eodMdl.getCurrentDate(), "", DmShared.xNull(voucherType), eodMdl.getVoucherPrefix(), eodMdl.getSiteCode(), eodMdl.getSiteShortName(), eodMdl.getCompanyCode(), cnTemp, true, null));
                    mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));

                    /*====================================< Insert records into stockHead >====================================*/
                    sqlQuery = "Insert Into stockHead(docId, v_Type, v_No, v_Date, v_Prefix, recId, recIdPrefix, site_Code, transferDt, "
                            + " preparedBy, approvedBy, u_EntDt) "
                            + " values (:docId, :v_Type, :v_No, :v_Date, :v_Prefix, :recId, :recIdPrefix, :site_Code, :transferDt,  "
                            + " :preparedBy, :preparedBy, :u_EntDt) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                    psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                    psTemp.setString("v_Type", DmShared.foriegnKey(voucherType));
                    psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
                    psTemp.setString("v_Prefix", eodMdl.getVoucherPrefix());
                    psTemp.setString("transferDt", null);
                    psTemp.setString("site_Code", eodMdl.getSiteCode());
                    psTemp.setString("preparedBy", eodMdl.getUserName());
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                /*====================================< Delete records from stockHeadDetail >====================================*/
                sqlQuery = "Delete From stockHeadDetail Where docId= :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                /*==============================<< Delete records from stock >>====================================*/
                Stock_Server.deleteStock(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), cnTemp, true);

                int count = 0;
                for (GrdLabConsumptionDetail_Mdl detail_Mdl : lstLCDetail) {
                    if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0l) {
                        count += 1;

                        if (DmShared.iNull(detail_Mdl.getDepartmentCode()).longValue() == 0l) {
                            throw new DmException(0, "Department not found for " + DmShared.xNull(detail_Mdl.getSharedKeyFieldName()));
                        }

                        /*====================================< Insert records into stockHeadDetail >====================================*/
                        sqlQuery = "Insert into stockHeadDetail (docId, v_SNo, departmentCode, itemCode, unitCode, quantity, site_Code) "
                                + " Values (:docId, :v_SNo, :departmentCode, :itemCode, :unitCode, :quantity, :site_Code) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setString("v_Sno", DmShared.iNull(count).toString());
                        psTemp.setString("departmentCode", DmShared.foriegnKey(detail_Mdl.getDepartmentCode()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getUnitCode()));
                        psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity(), "0.00"));
                        psTemp.setString("site_Code", DmShared.foriegnKey(eodMdl.getSiteCode()));
                        psTemp.executeUpdate();
                        psTemp.close();

                        /*====================================< Fill stock detail >====================================*/
                        Stock_Mdl stock_Mdl = new Stock_Mdl();
                        stock_Mdl.setSharedKeyFieldCode(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        stock_Mdl.setSharedKeyFieldName(DmShared.iNull(count).toString());
                        stock_Mdl.setSite_Code(eodMdl.getSiteCode());
                        stock_Mdl.setV_Type(voucherType);
                        stock_Mdl.setV_No(DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                        stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                        stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                        stock_Mdl.setV_Date(DmShared.dNull(eodMdl.getCurrentDate()));
                        stock_Mdl.setDepartmentCode(detail_Mdl.getDepartmentCode());
                        stock_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                        stock_Mdl.setIsuQty(DmServer.vNull(detail_Mdl.getQuantity()));

                        /*====================================< Save stock >====================================*/
                        Stock_Server.saveStock(stock_Mdl, DmEnum.StockEntryType.OTHER, cnTemp);

                        /*====================================< Update patientRegistration2 >====================================*/
                        sqlQuery = "Update  pr2 set pr2.shDocId = :shDocId "
                                + "         from patientRegistration2 as pr2 "
                                + "         left join patientRegistration as pr on pr.regDocId = pr2.regDocId "
                                + "         where (pr.isCancelled = 0) and (pr2.regDocId in (" + regDocId + ")) and (pr.v_Date = :v_Date) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("shDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }

                /*====================================< Delete EOD >====================================*/
                deleteEOD(eodMdl.getCurrentDate(), ProcessEOD.LAB_CONSUMPTION.getId(), eodMdl.getSiteCode(), cnTemp);

                /*====================================< Save EOD >====================================*/
                saveEOD(eodMdl.getCurrentDate(), ProcessEOD.LAB_CONSUMPTION.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), cnTemp);

                /*====================================< Save EOD History >====================================*/
                saveEODHistory(eodMdl.getCurrentDate(), ProcessEOD.LAB_CONSUMPTION.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), "Added successfully.", cnTemp);
            }

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
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
    }

    public void delete(EOD_Mdl eodMdl, Connection cnTemp, boolean isEODDelete) {
        DmPreparedStatement psTemp = null;
        String sqlQuery, voucherType;
        ResultSet rsTemp = null;

        try {
            voucherType = DmServer.getVType(vCatLC, cnTemp);

            /*====================================< Update patientRegistration2 >====================================*/
            sqlQuery = "Update  pr2 set pr2.shDocId = :shDocId "
                    + "         from patientRegistration2 as pr2 "
                    + "         left join stockHead as sh on pr2.shDocId = sh.docId "
                    + "         where sh.v_Date = :v_Date and sh.v_Type = :v_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("shDocId", null);
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from stockHeadDetail >====================================*/
            sqlQuery = "Delete  shd "
                    + "         from stockHeadDetail as shd "
                    + "         left join stockHead as sh on sh.docId = shd.docId "
                    + "         where sh.v_Date = :v_Date and sh.v_Type = :v_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Get docId from stock >====================================*/
            sqlQuery = "Select	s.docId "
                    + "		from stock as s "
                    + "		left join stockHead as sh on s.docId = sh.docId "
                    + "         where sh.v_Date = :v_Date and sh.v_Type = :v_Type "
                    + "		group by s.docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                /*==============================<< Delete records from stock >>====================================*/
                Stock_Server.deleteStock(DmShared.iNull(rsTemp.getString("docId")).toString(), cnTemp);
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Delete records from stockHead >====================================*/
//            sqlQuery = "Delete From stockHead Where v_Date = :v_Date and v_Type = :v_Type ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
//            psTemp.setString("v_Type", voucherType);
//            psTemp.executeUpdate();
//            psTemp.close();
            if (isEODDelete) {
                /*====================================< Delete EOD >====================================*/
                deleteEOD(eodMdl.getCurrentDate(), ProcessEOD.LAB_CONSUMPTION.getId(), eodMdl.getSiteCode(), cnTemp);

                /*====================================< Save EOD History >====================================*/
                saveEODHistory(eodMdl.getCurrentDate(), ProcessEOD.LAB_CONSUMPTION.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), "Deleted successfully.", cnTemp);
            }

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
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

    }

    private void deleteEOD(String eodDate, int processCode, String siteCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*====================================< Delete record from eod >====================================*/
            sqlQuery = "Delete from eod where processCode = :processCode and eodDate = :eodDate and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("processCode", DmShared.iNull(processCode).toString());
            psTemp.setDate("eodDate", DmServer.convertDate(eodDate));
            psTemp.setString("site_Code", siteCode);
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private void saveEOD(String eodDate, int processCode, String siteCode, String userName, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*====================================< Insert into eod >====================================*/
            sqlQuery = "INSERT INTO eod(processCode, eodDate, site_Code, u_EntDt, u_Name)"
                    + " VALUES (:processCode, :eodDate, :site_Code, :u_EntDt, :u_Name)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("processCode", DmShared.iNull(processCode).toString());
            psTemp.setDate("eodDate", DmServer.convertDate(eodDate));
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("u_Name", userName);
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    private void saveEODHistory(String eodDate, int processCode, String siteCode, String userName, String remark, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*====================================< Insert into eodHistory >====================================*/
            sqlQuery = "INSERT INTO eodHistory(code, processCode, eodDate, site_Code, remark, u_EntDt, u_Name)"
                    + " VALUES (:code, :processCode, :eodDate, :site_Code, :remark, :u_EntDt, :u_Name)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", UUID.randomUUID().toString());
            psTemp.setString("processCode", DmShared.iNull(processCode).toString());
            psTemp.setDate("eodDate", DmServer.convertDate(eodDate));
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("remark", remark);
            psTemp.setString("u_Name", userName);
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }
}
