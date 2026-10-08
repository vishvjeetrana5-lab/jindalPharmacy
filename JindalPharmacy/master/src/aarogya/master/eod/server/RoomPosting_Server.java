package aarogya.master.eod.server;

import aarogya.master.eod.shared.EOD_Mdl;
import aarogya.master.eod.shared.GrdRoomDetail_Mdl;
import aarogya.master.eod.shared.GrdServiceDetail_Mdl;
import aarogya.master.eod.shared.RoomPosting_Mdl;
import aarogya.master.eod.shared.RoomPosting_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ProcessEOD;
import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RoomPosting_Server extends AbstractEventHandler<Event> {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCatRP = pvc.IPD_ROOM_POSTING;
    private String filterRPCat = "'" + pvc.IPD + "'";

    public RoomPosting_Server() {
        super(STEnum.ProcessEOD.ROOM_POSTING.getValue());
    }

    @Override
    public void handle(Event event) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        SchedulerMast_Mdl schedulerMast_Mdl;
        EOD_Mdl eodMdl = new EOD_Mdl();
        String sqlQuery = "";
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

                    sqlQuery = "select manualCode from siteMast where code = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(eodMdl.getSiteCode()).toString());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        eodMdl.setSiteShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                    }
                    rsTemp.close();
                    psTemp.close();

                    /*====================================< Save Account Posting >====================================*/
                    RoomPosting_Mdl posting_Mdl = getRPRoomDetail(eodMdl, cnTemp);
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

    private RoomPosting_Mdl getRPRoomDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        return getRPRoomDetail(eodMdl, "", cnTemp);
    }

    public RoomPosting_Mdl getRPRoomDetail(EOD_Mdl eodMdl, String regDocId, Connection cnTemp) throws Exception {
        RoomPosting_Mdl mrTemp = new RoomPosting_Mdl();
        List<GrdRoomDetail_Mdl> listRoomNode = new ArrayList<GrdRoomDetail_Mdl>();
        GrdRoomDetail_Mdl roomNode;
        List<GrdServiceDetail_Mdl> listSeriveNode = new ArrayList<GrdServiceDetail_Mdl>();
        GrdServiceDetail_Mdl serviceNode;
        List<KeyFieldExtended> listKF = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended kf;
        List<KeyFieldExtended> listRegDetail = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended regDetail;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";

        try {
            if (DmShared.iNull(regDocId).longValue() != 0) {
                condition = " and pr.regDocId = :regDocId ";
            }

            sqlQuery = "Select	rm.regDocId, max(pr.recId) as regNo "
                    + "         from roomMast as rm "
                    + "         left join patientRegistration as pr on rm.regDocId = pr.regDocId "
                    + "         left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                    + "         where (rm.isActive = 1) and (rm.regDocId is not null) and (vt.category in (" + filterRPCat + ")) "
                    + "         and (rm.site_Code = :site_Code) and (pr.isCancelled <> 1) and (pr.v_Date <= :v_Date) "
                    + "         and (rm.regDocId not in ("
                    + "                 select  regDocId "
                    + "                         from patientServiceDetail as psd "
                    + "                         left join voucher_Type as vt on psd.v_Type = vt.v_Type "
                    + "                         where psd.isCancelled = 0 and vt.category = '" + vCatRP + "' and psd.v_Date = :v_Date "
                    + "                         group by psd.regDocId)) " + condition
                    + "		group by rm.regDocId "
                    + "         order by max(pr.recId) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            if (DmShared.iNull(regDocId).longValue() != 0) {
                psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                regDetail = new KeyFieldExtended();
                regDetail.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("regDocId")));
                regDetail.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("regNo")));
                listRegDetail.add(regDetail);
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select	rm.code as roomCode, rm.roomNo, rm.roomCategoryCode, rcm.name as roomCatName, pr.consultantCode, "
                    + "		pr.regDocId, pr.recId as ipdNo, pr.v_Date as admitDate, pr.organizationCode, sg.name as orgName, om.serviceDiscountPer, "
                    + "		pu.docId as uhidDocId, pu.recId as uhidNo, pu.patientName, pu.dobDisplay as age, gen.name as gender, pr.site_Code , pr.rateListCode  "
                    + "		from roomMast as rm "
                    + "		left join roomCatMast as rcm on rm.roomCategoryCode = rcm.code "
                    + "		left join patientRegistration as pr on rm.regDocId = pr.regDocId "
                    + "         left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                    + "		left join patientUHID as pu on pr.uhid = pu.docId "
                    + "		left join patientUHID2 as pu2 on pu.docId = pu2.uhid "
                    + "         left join hl7Mast as gen on pu2.genderCode = gen.code "
                    + "		left join organizationMast as om on pr.organizationCode = om.subCode "
                    + "		left join subGroup as sg on om.subCode = sg.subCode "
                    + "		where (rm.isActive = 1) and (rm.regDocId is not null) and (rm.site_Code = :site_Code) and (pr.v_Date <= :v_Date) "
                    + "         and (pr.isCancelled = 0) and (pu.isCancelled = 0) and (vt.category in (" + filterRPCat + ")) "
                    + "         and rm.regDocId not in ("
                    + "                         select  regDocId from patientServiceDetail as psd "
                    + "					left join voucher_Type as vt on psd.v_Type = vt.v_Type "
                    + "					where (psd.isCancelled = 0) and (vt.category = '" + vCatRP + "') and (psd.v_Date = :v_Date) "
                    + "					group by psd.regDocId) " + condition
                    + "		order by pr.recId, rm.roomNo, pr.v_Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            if (DmShared.iNull(regDocId).longValue() != 0) {
                psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                roomNode = new GrdRoomDetail_Mdl();
                roomNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("roomCode")));
                roomNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomNo")));
                roomNode.setRoomCatCode(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
                roomNode.setRoomCatName(DmShared.xNull(rsTemp.getString("roomCatName")));
                roomNode.setIpdDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                roomNode.setIpdRecId(DmShared.xNull(rsTemp.getString("ipdNo")));
                roomNode.setUhidDocId(DmShared.xNull(rsTemp.getString("uhidDocId")));
                roomNode.setUhidRecId(DmShared.xNull(rsTemp.getString("uhidNo")));
                roomNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                roomNode.setAge(DmShared.xNull(rsTemp.getString("age")));
                roomNode.setGender(DmShared.xNull(rsTemp.getString("gender")));
                roomNode.setOrgCode(DmShared.xNull(rsTemp.getString("organizationCode")));
                roomNode.setOrgName(DmShared.xNull(rsTemp.getString("orgName")));
                roomNode.setOrgDiscount(DmShared.xNull(rsTemp.getString("serviceDiscountPer")));
                roomNode.setAdmitDate(DmShared.dNull(rsTemp.getDate("admitDate")));
                roomNode.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                roomNode.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                roomNode.setRateListCode(DmShared.iNull(rsTemp.getString("rateListCode")).toString());
                listRoomNode.add(roomNode);
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select	psm.code as serviceCode, psm.name as serviceName, psm.shortName "
                    + "		from patientServiceMast as psm "
                    + "		where psm.isActive = 1 and psm.serviceRoutine = 1 "
                    + "         order by psm.name, psm.code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                kf = new KeyFieldExtended();
                kf.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                kf.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceName")));
                kf.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("shortName")));
                listKF.add(kf);
            }
            rsTemp.close();
            psTemp.close();

            for (GrdRoomDetail_Mdl roomDetail : listRoomNode) {
                if (DmShared.iNull(roomDetail.getSharedKeyFieldCode()).longValue() != 0) {
                    for (KeyFieldExtended field : listKF) {
                        if (DmShared.iNull(field.getSharedKeyFieldCode()).longValue() != 0) {
                            ServicePriceList_Mdl spl = new ServicePriceList_Mdl();
                            spl.setOrganizationCode(DmShared.xNull(roomDetail.getOrgCode()));
                            spl.setServiceCode(field.getSharedKeyFieldCode());
                            spl.setRoomCatCode(roomDetail.getRoomCatCode());
                            spl.setDate(DmShared.dNull(eodMdl.getCurrentDate()));
                            spl.setSiteCode(roomDetail.getSiteCode());
                            
                          spl.setRateListCode(DmShared.iNull(roomDetail.getRateListCode()).toString());

                            ServicePriceList_Mdl priceList_Mdl = STServer.getServicePriceBestFit(spl, cnTemp);

                            serviceNode = new GrdServiceDetail_Mdl();
                            serviceNode.setSharedKeyFieldCode(DmShared.xNull(roomDetail.getSharedKeyFieldCode()));
                            serviceNode.setSharedKeyFieldName(DmShared.xNull(roomDetail.getSharedKeyFieldName()));
                            serviceNode.setAdmitDate(DmShared.dNull(roomDetail.getAdmitDate()));
                            serviceNode.setAge(DmShared.xNull(roomDetail.getAge()));

                            serviceNode.setAmount(DmServer.vNull(priceList_Mdl.getRate(), "0.00"));
                            serviceNode.setOrganizationServiceCode(DmShared.xNull(priceList_Mdl.getOrganizationServiceCode()));

                            if (DmShared.vNull(priceList_Mdl.getDiscAmt()).doubleValue() != 0d || DmShared.vNull(priceList_Mdl.getDiscPercentage()).doubleValue() != 0d) {
                                setDiscount(serviceNode, DmShared.vNull(priceList_Mdl.getDiscPercentage()).doubleValue(),
                                        DmShared.vNull(priceList_Mdl.getDiscAmt()).doubleValue(), roomDetail.getOrgCode(), roomDetail.getOrgName());

                            } else if (DmShared.vNull(roomDetail.getOrgDiscount()).doubleValue() != 0d) {
                                setDiscount(serviceNode, DmShared.vNull(roomDetail.getOrgDiscount()).doubleValue(), 0, roomDetail.getOrgCode(), roomDetail.getOrgName());

                            } else {
                                serviceNode.setNetAmount(serviceNode.getAmount());
                                serviceNode.setDiscount("");
                                serviceNode.setDiscountPer("");
                                serviceNode.setDiscountGivenBy("");
                                serviceNode.setDiscountReason("");
                            }

                            serviceNode.setGender(DmShared.xNull(roomDetail.getGender()));
                            serviceNode.setIpdDocId(DmShared.xNull(roomDetail.getIpdDocId()));
                            serviceNode.setIpdRecId(DmShared.xNull(roomDetail.getIpdRecId()));
                            serviceNode.setOrgCode(DmShared.xNull(roomDetail.getOrgCode()));
                            serviceNode.setOrgName(DmShared.xNull(roomDetail.getOrgName()));
                            serviceNode.setPatientName(DmShared.xNull(roomDetail.getPatientName()));
                            serviceNode.setRoomCatCode(DmShared.xNull(roomDetail.getRoomCatCode()));
                            serviceNode.setRoomCatName(DmShared.xNull(roomDetail.getRoomCatName()));
                            serviceNode.setSrvCode(DmShared.xNull(field.getSharedKeyFieldCode()));
                            serviceNode.setSrvName(DmShared.xNull(field.getSharedKeyFieldName()));
                            serviceNode.setUhidDocId(DmShared.xNull(roomDetail.getUhidDocId()));
                            serviceNode.setUhidRecId(DmShared.xNull(roomDetail.getUhidRecId()));
                            if (DmShared.xNull(field.getSharedModifiedBy()).trim().equalsIgnoreCase("VC")) {
                                serviceNode.setConsultantCode(DmShared.xNull(roomDetail.getConsultantCode()));
                            } else {
                                serviceNode.setConsultantCode("");
                            }
                            if (DmShared.vNull(serviceNode.getAmount()).doubleValue() != 0) {
                                listSeriveNode.add(serviceNode);
                            }
                        }
                    }
                }
            }

            mrTemp.getListRegistrationDetail().clear();
            mrTemp.getListRegistrationDetail().addAll(listRegDetail);
            mrTemp.getListServiceDetail().addAll(listSeriveNode);
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
        return mrTemp;
    }

    private void setDiscount(GrdServiceDetail_Mdl node, double discPer, double disc, String orgCode, String orgName) {
        try {
            if (discPer != 0d || disc != 0d) {
                if (discPer != 0d) {
                    disc = (DmShared.vNull(node.getAmount()).doubleValue() * discPer) / 100;
                }
                node.setNetAmount(DmServer.vNull((DmShared.vNull(node.getAmount()).doubleValue() - disc), "0.00"));
                node.setDiscount(DmServer.vNull(disc, "0.00"));
                node.setDiscountPer(DmServer.vNull(discPer, "0.00"));
                node.setDiscountGivenBy(orgCode);
                node.setDiscountReason(orgName);
            }
        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
        }
    }

    public List<GrdServiceDetail_Mdl> getRPDeleteDetails(EOD_Mdl eodMdl, String regDocId, Connection cnTemp) throws Exception {
        List<GrdServiceDetail_Mdl> listSeriveNode = new ArrayList<GrdServiceDetail_Mdl>();
        GrdServiceDetail_Mdl serviceNode;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";

        try {
            if (DmShared.iNull(regDocId).longValue() != 0) {
                condition = " and psd.regDocId = :regDocId ";
            }

            sqlQuery = "select	psd.docId, psd.v_SNo, psd.serviceCode, psm.name as serviceName, psd.roomCode, rm.roomNo, "
                    + "		rcm.name as roomCatName, pr.regDocId, pr.recId as regNo, pr.v_Date as admitDate, sg.name as orgName, "
                    + "		pu.recId as uhidNo, pu.patientName, pu.dobDisplay as age, gen.name as gender, psd.oh_Amt_Net as amount "
                    + "		from patientServiceDetail as psd "
                    + "		left join patientServiceMast as psm on psd.serviceCode = psm.code "
                    + "		left join roomMast as rm on psd.roomCode = rm.code "
                    + "		left join roomCatMast as rcm on rm.roomCategoryCode = rcm.code "
                    + "		left join patientRegistration as pr on rm.regDocId = pr.regDocId "
                    + "		left join patientUHID as pu on pr.uhid = pu.docId "
                    + "		left join patientUHID2 as pu2 on pu.docId = pu2.uhid "
                    + "		left join hl7Mast as gen on pu2.genderCode = gen.code "
                    + "		left join subGroup as sg on pr.organizationCode = sg.subCode "
                    + "         left join voucher_Type as vt on psd.v_Type = vt.v_Type "
                    + "		where vt.category = '" + vCatRP + "' and psd.v_Date = :v_Date " + condition
                    + "         and psd.isCancelled = 0 and psd.site_Code = :site_Code "
                    + "         order by pr.recId, rm.roomNo, pr.v_Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            if (DmShared.iNull(regDocId).longValue() != 0) {
                psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                serviceNode = new GrdServiceDetail_Mdl();
                serviceNode.setServiceDocId(DmShared.xNull(rsTemp.getString("docId")));
                serviceNode.setServiceVSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                serviceNode.setSrvCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                serviceNode.setSrvName(DmShared.xNull(rsTemp.getString("serviceName")));
                serviceNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("roomCode")));
                serviceNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomNo")));
                serviceNode.setRoomCatName(DmShared.xNull(rsTemp.getString("roomCatName")));
                serviceNode.setIpdDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                serviceNode.setIpdRecId(DmShared.xNull(rsTemp.getString("regNo")));
                serviceNode.setUhidRecId(DmShared.xNull(rsTemp.getString("uhidNo")));
                serviceNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                serviceNode.setAge(DmShared.xNull(rsTemp.getString("age")));
                serviceNode.setGender(DmShared.xNull(rsTemp.getString("gender")));
                serviceNode.setOrgName(DmShared.xNull(rsTemp.getString("orgName")));
                serviceNode.setAdmitDate(DmShared.dNull(rsTemp.getDate("admitDate")));
                serviceNode.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                serviceNode.setNetAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                listSeriveNode.add(serviceNode);
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
        return listSeriveNode;
    }

    public void save(RoomPosting_Mdl mrTemp, EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, vType = "";

        try {
            RoomPosting_Vld.chkSave(eodMdl, DmShared.dNull(DmServer.getServerDateTime()[0]));

            vType = DmServer.getVType(vCatRP, cnTemp);

            List<GrdServiceDetail_Mdl> listServiceDetail = mrTemp.getListServiceDetail();
            List<KeyFieldExtended> listRegistrationDetail = mrTemp.getListRegistrationDetail();

            if (listRegistrationDetail != null) {
                for (KeyFieldExtended regDetailMdl : listRegistrationDetail) {
                    if (DmShared.iNull(regDetailMdl.getSharedKeyFieldCode()).longValue() != 0) {
                        mrTemp.setSharedKeyFieldCode(DmServer.getDocId(vType, eodMdl.getVoucherPrefix(), eodMdl.getSiteCode(), cnTemp));
                        mrTemp.setObjRecId(new RecId());
                        mrTemp.setObjRecId(DmServer.getRecId("patientService", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), eodMdl.getCurrentDate(), "", DmShared.xNull(vType), eodMdl.getVoucherPrefix(), eodMdl.getSiteCode(), eodMdl.getSiteShortName(), eodMdl.getCompanyCode(), cnTemp, true, null));

                        if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                            throw new UnsupportedOperationException("Invalid key.");
                        }

                        /*====================================< Check whether Registration No. is not allowed or not >====================================*/
                        if (DmShared.iNull(regDetailMdl.getSharedKeyFieldCode()).longValue() != 0) {
                            sqlQuery = "select  pr.regDocId "
                                    + "         from patientRegistration as pr "
                                    + "         left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                                    + "         where pr.regDocId = :regDocId and vt.category in (" + filterRPCat + ") ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("regDocId", DmShared.iNull(regDetailMdl.getSharedKeyFieldCode()).toString());
                            rsTemp = psTemp.executeQuery();
                            if (!rsTemp.next()) {
                                throw new DmException(0, "This Registration No. is not allowed. Try another Registration No..");
                            }
                            rsTemp.close();
                            psTemp.close();
                        }

                        /*====================================< Check wether Room already posted or not >====================================*/
                        sqlQuery = "Select docId from patientServiceDetail where v_Type = :v_Type and v_Date = :v_Date and regDocId = :regDocId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("v_Type", vType);
                        psTemp.setString("regDocId", DmShared.iNull(regDetailMdl.getSharedKeyFieldCode()).toString());
                        psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()), true);
                        rsTemp = psTemp.executeQuery();
                        if (rsTemp.next()) {
                            throw new DmException(0, "Room already posted.");
                        }
                        rsTemp.close();
                        psTemp.close();

                        /*====================================< Insert into patientService >====================================*/
                        sqlQuery = "Insert into patientService(docId, v_No, recId, recIdPrefix, v_Type, v_Date, v_Time, v_Prefix, site_Code, preparedBy, u_EntDt, transferDt)"
                                + " values (:docId, :v_No, :recId, :recIdPrefix, :v_Type, :v_Date, :v_Time, :v_Prefix, :site_Code, :preparedBy, :u_EntDt, :transferDt)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setString("v_Type", DmShared.foriegnKey(vType));
                        psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                        psTemp.setString("recId", DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
                        psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                        psTemp.setString("v_Prefix", eodMdl.getVoucherPrefix());
                        psTemp.setString("v_Time", DmServer.vNull(eodMdl.getCurrentTime(), "00.00"));
                        psTemp.setDate("v_Date", DmServer.convertDate(DmShared.dNull(eodMdl.getCurrentDate())));
                        psTemp.setString("site_Code", eodMdl.getSiteCode());
                        psTemp.setString("preparedBy", eodMdl.getUserName());
                        psTemp.setString("transferDt", null);
                        psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                        if (listServiceDetail != null) {
                            int vSNo = 0;
                            for (GrdServiceDetail_Mdl node : listServiceDetail) {
                                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                                    if (DmShared.iNull(node.getIpdDocId()).longValue() == DmShared.iNull(regDetailMdl.getSharedKeyFieldCode()).longValue()) {
                                        /*====================================< Insert into patientServiceDetail >====================================*/
                                        sqlQuery = "Insert into patientServiceDetail (docId, v_SNo, v_Type, v_Date, v_Time, site_Code, regDocId, "
                                                + " roomCode, serviceCode, consultantCode, quantity, rate, grossAmount, oh_Amt_Gross, oh_Amt_Net, "
                                                + " discount, discountPer, discountGivenBy, discountReason, organizationServiceCode, remark) "
                                                + " values (:docId, :v_SNo, :v_Type, :v_Date, :v_Time, :site_Code, :regDocId, "
                                                + " :roomCode, :serviceCode, :consultantCode, :quantity, :rate, :rate, :netAmount, :netAmount, "
                                                + " :discount, :discountPer, :discountGivenBy, :discountReason, :organizationServiceCode, :remark)";
                                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                                        psTemp.setString("v_SNo", DmShared.iNull(++vSNo).toString());
                                        psTemp.setString("v_Type", DmShared.foriegnKey(vType));
                                        psTemp.setDate("v_Date", DmServer.convertDate(DmShared.dNull(eodMdl.getCurrentDate())));
                                        psTemp.setString("v_Time", DmServer.vNull(eodMdl.getCurrentTime(), "00.00"));
                                        psTemp.setString("site_Code", eodMdl.getSiteCode());
                                        psTemp.setString("regDocId", DmShared.foriegnKey(node.getIpdDocId()));
                                        psTemp.setString("roomCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                                        psTemp.setString("serviceCode", DmShared.foriegnKey(node.getSrvCode()));
                                        psTemp.setString("consultantCode", DmShared.foriegnKey(node.getConsultantCode()));
                                        psTemp.setString("organizationServiceCode", DmShared.foriegnKey(node.getOrganizationServiceCode()));
                                        psTemp.setString("quantity", "1");
                                        psTemp.setString("rate", DmServer.vNull(node.getAmount(), "0.00"));
                                        psTemp.setString("discount", DmServer.vNull(node.getDiscount(), "0.00"));
                                        psTemp.setString("discountPer", DmServer.vNull(node.getDiscountPer(), "0.00"));
                                        psTemp.setString("netAmount", DmServer.vNull(node.getNetAmount(), "0.00"));
                                        psTemp.setString("discountGivenBy", DmShared.foriegnKey(node.getDiscountGivenBy()));
                                        psTemp.setString("discountReason", DmShared.xNull(node.getDiscountReason()).trim());
                                        psTemp.setString("remark", "Entry from Room Posting.");
                                        psTemp.executeUpdate();
                                        psTemp.close();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /*====================================< Delete EOD >====================================*/
            deleteEOD(eodMdl.getCurrentDate(), ProcessEOD.ROOM_POSTING.getId(), eodMdl.getSiteCode(), cnTemp);

            /*====================================< Save EOD >====================================*/
            saveEOD(eodMdl.getCurrentDate(), ProcessEOD.ROOM_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), cnTemp);

            /*====================================< Save EOD History >====================================*/
            saveEODHistory(eodMdl.getCurrentDate(), ProcessEOD.ROOM_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), "Added successfully.", cnTemp);
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

    }

    public void delete(List<KeyFieldExtended> listRPDetail, EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";

        try {
            RoomPosting_Vld.chkSave(eodMdl, DmShared.dNull(DmServer.getServerDateTime()[0]));

            for (KeyFieldExtended kf : listRPDetail) {
                if (DmShared.iNull(kf.getSharedKeyFieldCode()).longValue() != 0) {
                    if (DmShared.xNull(condition).trim().equalsIgnoreCase("")) {
                        condition += " (docId = " + DmShared.iNull(kf.getSharedKeyFieldCode()).toString() + " and v_SNo = " + DmShared.iNull(kf.getSharedKeyFieldName()).toString() + " ) ";
                    } else {
                        condition += " or (docId = " + DmShared.iNull(kf.getSharedKeyFieldCode()).toString() + " and v_SNo = " + DmShared.iNull(kf.getSharedKeyFieldName()).toString() + " ) ";
                    }
                }
            }

            /*====================================< Delete record from eod >====================================*/
            sqlQuery = "Delete from patientServiceDetail where " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete EOD >====================================*/
            deleteEOD(eodMdl.getCurrentDate(), ProcessEOD.ROOM_POSTING.getId(), eodMdl.getSiteCode(), cnTemp);

            /*====================================< Save EOD >====================================*/
            saveEOD(eodMdl.getCurrentDate(), ProcessEOD.ROOM_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), cnTemp);

            /*====================================< Save EOD History >====================================*/
            saveEODHistory(eodMdl.getCurrentDate(), ProcessEOD.ROOM_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), "Deleted successfully.", cnTemp);
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
