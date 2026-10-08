package aarogya.selfToolkit.stStatic.server;

import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import aarogya.selfToolkit.stStatic.shared.ConsultantDiary_Mdl;
import aarogya.selfToolkit.stStatic.shared.ConvertedQuantity;
import aarogya.selfToolkit.stStatic.shared.GstLocation_Mdl;
import aarogya.selfToolkit.stStatic.shared.PackageDetail_Mdl;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import aarogya.selfToolkit.stStatic.shared.RoundOffService_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import aarogya.selfToolkit.stStatic.shared.SaleRateRule_Mdl;
import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
import aarogya.selfToolkit.stStatic.shared.ShiftTime_Mdl;
import aarogya.selfToolkit.stStatic.shared.ItemPriceList_Mdl;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmDateTimeFormat;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.dmScheduler.server.DmAnonymousWebJobFactory;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.xls.server.DmResultSetToExcelWriter;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmToolkit.gsts.server.GSTS_Server;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_Server;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import java.io.File;
import java.security.Key;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import jxl.format.Border;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.Alignment;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;

public class STServer {

    private static String codifyKey1 = "~!â‰¡s5*.d", codifyKey2 = "Tâ†�mÃ„nâ•¤sY";
    private static String codifyAlgorithm = "AES";
    private static String cashOrgCode = "", cashRateListCode = "";
    private static ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    public static boolean saveRoundOffService(RoundOffService_Mdl service_Mdl, Connection cnTemp) {

        DmPreparedStatement psTemp = null;
        String sqlQuery, serviceCode = "";
        ResultSet rsTemp = null;
        int v_Sno = 0, sbVSno = 0;
        double rate = 0.0;
        try {

            //========================= GET Round OFF Amount From saleBill ======================
            sqlQuery = " select oh_Amt_RoundOff from saleBill where docid =:docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(service_Mdl.getSbDocid()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                rate = rsTemp.getDouble("oh_Amt_RoundOff");
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.vNull(rate) == 0.0) {
                return false;
            }
            //========================= GET service Code of Round OFF======================
            sqlQuery = " select code  from patientServiceMast where shortName='ROFF' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                serviceCode = DmShared.xNull(rsTemp.getString("code"));
            } else {
                throw new DmException(0, "Please define RoundOFF Service in ServiceMaster .");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " select max(v_SNo)v_SNo, max(sbV_Sno)sbV_Sno "
                    + "from patientServiceDetail "
                    + "where regdocid =:regDocId and sbDocid =:sbDocid and isFooter=1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(service_Mdl.getRegDocId()).toString());
            psTemp.setString("sbDocid", DmShared.iNull(service_Mdl.getSbDocid()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                v_Sno = DmShared.iNull(rsTemp.getString("v_SNo")).intValue();
                sbVSno = DmShared.iNull(rsTemp.getString("sbV_Sno")).intValue();
            }
            rsTemp.close();
            psTemp.close();

            v_Sno = v_Sno >= 32000 ? v_Sno + 1 : v_Sno + 32000;
            sbVSno = sbVSno >= 32000 ? sbVSno + 1 : sbVSno + 32000;

            /*====================================< Insert records into patientServiceDetail >====================================*/
            sqlQuery = "Insert Into patientServiceDetail (docId, v_SNo, v_Type, v_Date, v_Time, serviceCode, "
                    + " regDocId, quantity, rate, grossAmount, oh_Amt_net, sbDocId, sbV_SNo, site_Code, isFooter) "
                    + " values (:docId, :v_SNo, :v_Type, :v_Date, :v_Time, :serviceCode,"
                    + " :regDocId, :quantity, :rate, :grossAmount, :oh_Amt_net, :sbDocId, :sbV_SNo,:site_Code,:isFooter) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(service_Mdl.getSharedKeyFieldCode()).toString());
            psTemp.setString("v_SNo", DmShared.iNull(v_Sno).toString());
            psTemp.setString("v_Type", DmShared.foriegnKey(service_Mdl.getVoucherTypeCode()));
            psTemp.setDate("v_Date", DmServer.convertDate(service_Mdl.getVoucherDate()));
            psTemp.setString("v_Time", DmServer.vNull(service_Mdl.getVoucherTime(), "00.00"));
            psTemp.setString("serviceCode", DmShared.foriegnKey(serviceCode));
            psTemp.setString("regDocId", DmShared.foriegnKey(service_Mdl.getRegDocId()));
            psTemp.setString("quantity", DmServer.vNull(1, "0.00"));
            psTemp.setString("rate", DmServer.vNull(rate, "0.00"));
            psTemp.setString("oh_Amt_net", DmServer.vNull(rate, "0.00"));
            psTemp.setString("grossAmount", DmServer.vNull(rate, "0.00"));
            psTemp.setString("sbDocId", DmShared.iNull(service_Mdl.getSbDocid()).toString());
            psTemp.setString("sbV_SNo", DmShared.iNull(sbVSno).toString());
            psTemp.setString("site_Code", DmShared.xNull(service_Mdl.getSiteCode()));
            psTemp.setString("isFooter", DmShared.xNull("1"));

            psTemp.executeUpdate();

            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return true;
    }

    public static String getReceiptVType(String voucherType, String userName, String paymentMode, Connection cnTemp) {
        String receiptVType = "";
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;

        try {
            sqlQuery = "select top 1 T2.receiptV_Type "
                    + "  from ( "
                    + "   select T1.paymentMode,T1.receiptV_Type,case when T1.userName is Null then 0 else 1 end as UCount "
                    + "    "
                    + "     from ( "
                    + "      select  rvc.userName,rvc.paymentMode,rvc.receiptV_Type "
                    + "        from receiptVoucherConfig rvc "
                    + "        where rvc.v_Type = :v_Type "
                    + "      ) as T1 "
                    + "      where IsNull(T1.userName,:userName) = :userName "
                    + "   ) as T2 "
                    + "   where isNull(T2.paymentMode,:PaymentMode) = :PaymentMode "
                    + "   order by Ucount desc,case when T2.paymentMode is Null then 0 else 1 end desc";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_Type", DmShared.xNull(voucherType));
            psTemp.setString("userName", DmShared.xNull(userName));
            psTemp.setString("PaymentMode", DmShared.xNull(paymentMode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                receiptVType = DmShared.xNull(rsTemp.getString("receiptV_Type"));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(receiptVType).intValue() == 0) {
                throw new DmException(0, "Please define receipt type.");
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return receiptVType;
    }

    public static void verifyPharmacyCheckListStatus(String regDocId, Connection cnTemp) {

        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;

        try {

            //==================================/*Check pending requisation for Bill*/============
            sqlQuery = "select count(rq.docid) as cnt\n"
                    + "	from requisition rq \n"
                    + "	left join requisitionDetail rqd on rq.docid=rqd.docid\n"
                    + "	left Join saleBillDetail sbd on rqd.docid=sbd.requisitionDocId and rqd.v_SNo=sbd.requisitionV_SNo\n"
                    + "	where rq.regDocID=:regDocId and rqd.isStatus=0 and isNull(rq.approvedBy,'')<>'' and isNull(sbd.docid,0)=0\n"
                    + "	group by rq.docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Medicine requisation Pending for Bill.");
                }
            }
            rsTemp.close();
            psTemp.close();
            //==================================End==============================================
            //==================================/*Check Bill not Acknowledged*/===========
            sqlQuery = "select count(rq.docid) as cnt\n"
                    + "	from requisition rq \n"
                    + "	left join requisitionDetail rqd on rq.docid=rqd.docid\n"
                    + "	left Join saleBillDetail sbd on rqd.docid=sbd.requisitionDocId and rqd.v_SNo=sbd.requisitionV_SNo\n"
                    + "	where rq.regDocID=:regDocId and rqd.isStatus=2 and isNull(rq.approvedBy,'')<>'' and isNull(sbd.docid,0)<>0 and isNull(rqd.receivedStatus,0)=0\n"
                    + "	group by rq.docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Medicines are not Acknowledged by Nursing Station.");
                }
            }
            rsTemp.close();
            psTemp.close();
            //==================================End==============================================
            //==================================/*Check Medicine Return Requst pending for return*/===========
            sqlQuery = "select count(sr.docid) as cnt\n"
                    + "	from saleBillReturn sr \n"
                    + "	where sr.regDocID=:regDocId and isNull(sr.returnDocID,0)=0 and sr.isCancelled=0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Medicine Return Requst pending for return.");
                }
            }
            rsTemp.close();
            psTemp.close();
            //==================================End==============================================

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

    public static String getTempDOB(int day, int month, int year, String affetivDate) {
        String dmrTemp = "";
        String tempDate = "";
        try {
            Calendar now = Calendar.getInstance();
            if (!DmShared.dNull(affetivDate).equalsIgnoreCase("")) {
                now.setTime(DmServer.convertDate(affetivDate));
            }

            // add days to current date using Calendar.add method
            now.add(Calendar.DATE, -day);
            now.add(Calendar.MONTH, -month);
            now.add(Calendar.YEAR, -year);

            tempDate = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date tempdob = (Date) df.parse(tempDate);
            dmrTemp = DmShared.dNull(tempdob);

        } catch (Exception e) {

        }
        return dmrTemp;
    }

//    public static String getAgeDisplay(String tempDOB, String affetivDate, int showDDUptoMonth, int showMMUptoYear) throws Exception {
//        String displayDOB = "";
//        int day, month, year, cdDD, cdMM, cdYYYY, dobDD, dobMM, dobYYYY;
//
//        ///////////////////////////////////////////////////////////////////////////////////////////////////
//        Calendar now = Calendar.getInstance();
//        if (!DmShared.dNull(affetivDate).equalsIgnoreCase("")) {
//            now.setTime(DmServer.convertDate(affetivDate));
//        }
//
//        cdDD = Integer.valueOf(DmDateTimeFormat.getFormat("dd").format(now.getTime()));
//        cdMM = Integer.valueOf(DmDateTimeFormat.getFormat("MM").format(now.getTime()));
//        cdYYYY = Integer.valueOf(DmDateTimeFormat.getFormat("yyyy").format(now.getTime()));
//
////        dobDD = Integer.valueOf(DmDateTimeFormat.getFormat("dd").format(DmShared.convertDate(tempDOB)));
////        dobMM = Integer.valueOf(DmDateTimeFormat.getFormat("MM").format(DmShared.convertDate(tempDOB)));
////        dobYYYY = Integer.valueOf(DmDateTimeFormat.getFormat("yyyy").format(DmShared.convertDate(tempDOB)));
//        if (!DmShared.dNull(tempDOB).equalsIgnoreCase("")) {
//            dobDD = Integer.valueOf(DmDateTimeFormat.getFormat("dd").format(DmShared.convertDate(tempDOB)));
//            dobMM = Integer.valueOf(DmDateTimeFormat.getFormat("MM").format(DmShared.convertDate(tempDOB)));
//            dobYYYY = Integer.valueOf(DmDateTimeFormat.getFormat("yyyy").format(DmShared.convertDate(tempDOB)));
//        } else {
//            dobDD = 0;
//            dobMM = 0;
//            dobYYYY = 0;
//        }
//
//        if (cdDD == 0 || cdMM == 0 || cdYYYY == 0 || dobDD == 0 || dobMM == 0 || dobYYYY == 0) {
//            day = 0;
//            month = 0;
//            year = 0;
//        } else {
//
//            if (cdDD < dobDD) {
//                cdDD = cdDD + 30;
//                cdMM = cdMM - 1;
//            }
//
//            if (cdMM < dobMM) {
//                cdMM = cdMM + 12;
//                cdYYYY = cdYYYY - 1;
//            }
//
//            day = cdDD - dobDD;
//            month = cdMM - dobMM;
//            year = cdYYYY - dobYYYY;
//        }
//        //////////////////////////////////////////////////////////////////////////////////////////////////////
//
//        String dd, mm;
//        dd = day + "";
//        mm = month + "";
//
//        if (dd.length() == 1) {
//            dd = "0" + dd;
//        }
//        if (mm.length() == 1) {
//            mm = "0" + mm;
//        }
//        if ((month <= showDDUptoMonth) && (year == 0)) {
//            if ((month == 0) && (day == 0)) {
//                displayDOB = "";
//            } else if (month == 0) {
//                displayDOB = dd + "D";
//            } else if (day == 0) {
//                displayDOB = mm + "M";
//            } else {
//                displayDOB = mm + "M " + dd + "D";
//            }
//        } else if ((year <= showMMUptoYear)) {
//            if ((month == 0) && (year == 0)) {
//                displayDOB = "";
//            } else if (month == 0) {
//                displayDOB = year + "Y";
//            } else if (year == 0) {
//                displayDOB = mm + "M";
//            } else {
//                displayDOB = year + "Y " + mm + "M";
//            }
//        } else {
//            if ((month == 0) && (year == 0)) {
//                displayDOB = "";
//            } else if (month == 0) {
//                displayDOB = year + "Y";
//            } else if (year == 0) {
//                displayDOB = mm + "M";
//            } else {
//                displayDOB = year + "Y ";
//            }
//
//        }
//
//        return displayDOB;
//
//    }
    //========================================<Updated on 14-Dec-2021>==============================
    public static String getAgeDisplay(String tempDOB, String affetivDate, int showDDUptoMonth, int showMMUptoYear) throws Exception {
        String displayDOB = "", year = "", month = "", day = "";
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar tmpdate = Calendar.getInstance();
        long years = 0, months = 0, days = 0;
        if (!DmShared.dNull(tempDOB).equalsIgnoreCase("")) {
            startDate.setTime(DmShared.convertDate(tempDOB));
            if (!DmShared.dNull(affetivDate).equalsIgnoreCase("")) {
                endDate.setTime(DmShared.convertDate(affetivDate));
            }
            tmpdate.setTime(startDate.getTime());

            tmpdate.add(Calendar.YEAR, 1);
            while (tmpdate.compareTo(endDate) <= 0) {
                startDate.add(Calendar.YEAR, 1);
                tmpdate.add(Calendar.YEAR, 1);
                years++;
            }
            year = years + "Y";

            tmpdate.setTime(startDate.getTime());
            tmpdate.add(Calendar.MONTH, 1);
            while (tmpdate.compareTo(endDate) <= 0) {
                startDate.add(Calendar.MONTH, 1);
                tmpdate.add(Calendar.MONTH, 1);
                months++;
            }
            month = months + "M";

            tmpdate.setTime(startDate.getTime());
            tmpdate.add(Calendar.DATE, 1);
            while (tmpdate.compareTo(endDate) <= 0) {
                startDate.add(Calendar.DATE, 1);
                tmpdate.add(Calendar.DATE, 1);
                days++;
            }
            day = days + "D";

            if (months <= showDDUptoMonth && days != 0 && years == 0) {
                displayDOB = day;
            }
            if (years <= showMMUptoYear && months != 0) {
                displayDOB = month + displayDOB;
            }
            if (years != 0) {
                displayDOB = year + displayDOB;
            }

        }
        return displayDOB;

    }

    public static ServicePriceList_Mdl getServicePriceBestFit(ServicePriceList_Mdl priceListFor, Connection cnTemp) throws DmException {
        ServicePriceList_Mdl priceList = new ServicePriceList_Mdl();
        String sqlQuery, sqlCondition = "";
        String consultantCode = "", dayNameCode = "", natureCode = "", roomCatCode = "", organizationServiceCode = "", time = "", v_Type = "", rateTypeCode = "", organizationCode = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            if (DmShared.xNull(cashOrgCode).trim().equalsIgnoreCase("")) {
                cashOrgCode = DmServer.getCodeViaManualCode("CP", "shortName", "organizationMast", "subCode", cnTemp);
                cashRateListCode = DmServer.getCodeViaManualCode(cashOrgCode, "subCode", "organizationMast", "rateListCode", cnTemp);
            }

            if (priceListFor != null) {
                if (DmShared.iNull(priceListFor.getServiceCode()).longValue() == 0
                        || DmShared.dNull(priceListFor.getDate()).trim().equalsIgnoreCase("")
                        || DmShared.iNull(priceListFor.getSiteCode()).intValue() == 0
                        || DmShared.iNull(priceListFor.getOrganizationCode()).longValue() == 0) {

                } else {
                    sqlCondition += "and (";
                    if (DmShared.iNull(priceListFor.getOrganizationCode()).longValue() > 0) {
                        organizationCode = priceListFor.getOrganizationCode();
                        sqlCondition += "organizationCode = :organizationCode or ";
                    }
                    sqlCondition += "organizationCode is null) ";

                    sqlCondition += "and (";
                    if (DmShared.iNull(priceListFor.getConsultantCode()).longValue() > 0) {
                        consultantCode = priceListFor.getConsultantCode();
                        sqlCondition += "consultantCode = :consultantCode or ";
                    }
                    sqlCondition += "consultantCode is null) ";

                    if (DmShared.vNull(priceListFor.getDayNameCode()).doubleValue() == 0) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(DmShared.convertDate(priceListFor.getDate()));
                        priceListFor.setDayNameCode(String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
                    }

                    dayNameCode = DmShared.iNull(priceListFor.getDayNameCode()).toString();
                    sqlCondition += "and dayNameCode in (:dayNameCode,0) ";

                    natureCode = DmShared.foriegnKey(DmShared.iNull(priceListFor.getPriceListNature().getId()).toString());
                    if (!DmShared.xNull(natureCode).trim().equalsIgnoreCase("")) {
                        sqlCondition += "and nature in (:nature,0) ";
                    }

                    sqlCondition += "and (";
                    if (DmShared.iNull(priceListFor.getRoomCatCode()).longValue() > 0) {
                        roomCatCode = priceListFor.getRoomCatCode();
                        sqlCondition += "roomCatCode = :roomCatCode or ";
                    }
                    sqlCondition += "roomCatCode is null) ";

                    if (DmShared.iNull(priceListFor.getOrganizationServiceCode()).longValue() > 0) {
                        organizationServiceCode = priceListFor.getOrganizationServiceCode();
                        sqlCondition += "and (organizationServiceCode = :organizationServiceCode) ";
                    }

                    sqlCondition += "and (";
                    if (!DmShared.xNull(priceListFor.getTime()).trim().equalsIgnoreCase("")) {
                        time = DmShared.vNull(priceListFor.getTime()).toString();
                        sqlCondition += "(:time between timeFrom and timeTo) or ";
                    }
                    sqlCondition += "timeFrom is null or timeTo is null) ";

                    sqlQuery = "select  pspl.applicableFrom, pspl.serviceCode, pspl.organizationServiceCode, pspl.organizationCode, "
                            + "		pspl.roomCatCode, pspl.consultantCode, pspl.site_code, pspl.nature, pspl.dayNameCode, "
                            + "		pspl.timeFrom, pspl.timeTo, pspl.rate, pspl.discount, pspl.discountPer, pspl.rateListCode "
                            + "		from patientServicePriceList pspl "
                            + "         where (:date between pspl.applicableFrom and pspl.applicableTo) and serviceCode = :serviceCode and site_Code = :site_Code "
                            + "         and rateListCode = :rateListCode "
                            + sqlCondition;

                    sqlQuery = "select osm.organizationServiceName, ord.* "
                            + "from ( "
                            + sqlQuery
                            + " ) as ord "
                            + " left join organizationServiceMast as osm on ord.organizationServiceCode = osm.code "
                            + "order by rateListCode,"
                            + "(case when ord.organizationCode is null then 0 else 1 end) + "
                            + "(case when ord.organizationServiceCode is null then 0 else 1 end) + "
                            + "(case when ord.roomCatCode is null then 0 else 1 end) + "
                            + "(case when ord.consultantCode is null then 0 else 1 end) + "
                            + "(case when ord.nature = 0 then 0 else 1 end) + "
                            + "(case when ord.dayNameCode = 0 then 0 else 1 end) + "
                            + "(case when ord.timeFrom is null then 0 else 1 end) + "
                            + "(case when ord.timeTo is null then 0 else 1 end) desc  offset 0 rows fetch next 1 rows only ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("date", DmServer.convertDate(priceListFor.getDate()));
                    psTemp.setString("serviceCode", priceListFor.getServiceCode());
                    psTemp.setString("rateListCode", priceListFor.getRateListCode());
                    psTemp.setString("site_Code", priceListFor.getSiteCode());
                    if (!DmShared.xNull(consultantCode).trim().equalsIgnoreCase("")) {
                        psTemp.setString("consultantCode", consultantCode);
                    }
                    if (!DmShared.xNull(organizationCode).trim().equalsIgnoreCase("")) {
                        psTemp.setString("organizationCode", organizationCode);
                    }
                    if (!DmShared.xNull(dayNameCode).trim().equalsIgnoreCase("")) {
                        psTemp.setString("dayNameCode", dayNameCode);
                    }
                    if (!DmShared.xNull(natureCode).trim().equalsIgnoreCase("")) {
                        psTemp.setString("nature", natureCode);
                    }
                    if (!DmShared.xNull(roomCatCode).trim().equalsIgnoreCase("")) {
                        psTemp.setString("roomCatCode", roomCatCode);
                    }
                    if (!DmShared.xNull(organizationServiceCode).trim().equalsIgnoreCase("")) {
                        psTemp.setString("organizationServiceCode", organizationServiceCode);
                    }
                    if (!DmShared.xNull(time).trim().equalsIgnoreCase("")) {
                        psTemp.setString("time", time);
                    }
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        priceList.setOrganizationServiceCode(rsTemp.getString("organizationServiceCode"));
                        priceList.setOrganizationServiceName(rsTemp.getString("organizationServiceName"));
                        priceList.setRateListCode(rsTemp.getString("rateListCode"));
                        priceList.setServiceCode(rsTemp.getString("serviceCode"));
                        priceList.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                        priceList.setDiscAmt(DmServer.vNull(rsTemp.getString("discount"), "0.00"));
                        priceList.setDiscPercentage(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
                    }
                    rsTemp.close();
                    psTemp.close();
                    if (DmShared.vNull(priceList.getDiscAmt()).doubleValue() == 0 && DmShared.vNull(priceList.getDiscPercentage()).doubleValue() == 0) {
                        priceList.setDiscPercentage(DmServer.vNull(getServiceCategoryDiscount(priceListFor.getRateListCode(), priceListFor.getServiceCode(), priceListFor.getOrganizationCode(), priceListFor.getSiteCode(), cnTemp), "0.00"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

        return priceList;
    }

    private static double getServiceCategoryDiscount(String rateListCode, String serviceCode, String organizationCode, String site_Code, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlCondition;
        double discountPer = 0;
        try {
            sqlCondition = "and (";
            if (DmShared.iNull(organizationCode).longValue() > 0) {
                sqlCondition += "organizationCode = :organizationCode or ";
            }
            sqlCondition += "organizationCode is null) ";

            sqlQuery = "Select	pscd.discountPer "
                    + "		from patientServiceCatDiscount as pscd "
                    + "		left join patientServiceMast as psm on pscd.serviceCategoryCode = psm.categoryCode "
                    + "		where psm.code = :serviceCode and pscd.rateListCode = :rateListCode and pscd.site_Code = :site_Code " + sqlCondition
                    + "         order by rateListCode, (case when organizationCode is null then 0 else 1 end) desc offset 0 rows fetch next 1 rows only";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCode", DmShared.iNull(serviceCode).toString());
            if (DmShared.iNull(organizationCode).longValue() > 0) {
                psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            }
            psTemp.setString("rateListCode", DmShared.iNull(rateListCode).toString());
            psTemp.setString("site_Code", DmShared.iNull(site_Code).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                discountPer = DmShared.vNull(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
            }
            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

        return discountPer;
    }

    private static double getServiceCategoryDiscount(String serviceCode, String orgnaizationCode, String site_Code, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double discountPer = 0;

        try {
            sqlQuery = "Select	pscd.discountPer "
                    + "		from patientServiceCatDiscount as pscd "
                    + "		left join patientServiceMast as psm on pscd.serviceCategoryCode = psm.categoryCode "
                    + "		where psm.code = :serviceCode and pscd.organizationCode = :organizationCode and pscd.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCode", DmShared.iNull(serviceCode).toString());
            psTemp.setString("organizationCode", DmShared.iNull(orgnaizationCode).toString());
            psTemp.setString("site_Code", DmShared.iNull(site_Code).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                discountPer = DmShared.vNull(DmServer.vNull(rsTemp.getString("discountPer"), "0.00"));
            }
            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

        return discountPer;
    }

    public static void deletePriceList(String applicableFrom, PriceListEntryFrom priceListEntryFrom,
            String keyCode, String organizationCode, String site_Code, Connection cnTemp) {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        String sqlQuery, sqlCondition, applicableTo = "";
        ResultSet rsTemp = null;

        switch (priceListEntryFrom) {
            case CONSULTANT_MASTER:
                sqlCondition = "and consultantCode = :keyCode ";
                break;
            case ORGANIZATION_MASTER:
                sqlCondition = "and serviceCode = :keyCode ";
                break;
//            case INDIVIDUAL_SERVICE:
//                sqlCondition = "and serviceCode = :keyCode ";
//                priceListEntryFrom = PriceListEntryFrom.ORGANIZATION_MASTER;
//                break;
//            case TestMaster:
//                sqlCondition = "and serviceCode = :keyCode ";
//                break;
            default:
                throw new DmException(0, "Price List: Entry From not found.");
        }

        try {
            sqlQuery = "select applicableFrom,applicableTo "
                    + "from patientServicePriceList "
                    + "where applicableFrom = :applicableFrom "
                    + "and entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and organizationCode = :organizationCode "
                    + sqlCondition
                    + "order by applicableFrom desc offset 0 rows fetch next 1 rows only ";
            //+ "limit 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("organizationCode", organizationCode);
            psTemp.setString("keyCode", keyCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
            } else {
                throw new UnsupportedOperationException("Applicable From Date is Invalid");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select applicableFrom,applicableTo "
                    + "from patientServicePriceList "
                    + "where applicableFrom < :applicableFrom "
                    + "and entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and organizationCode = :organizationCode "
                    + sqlCondition
                    + "group by applicableFrom,applicableTo "
                    + "order by applicableFrom desc offset 0 rows fetch next 1 rows only ";
            //+ "limit 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("organizationCode", organizationCode);
            psTemp.setString("keyCode", keyCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                sqlQuery = "update patientServicePriceList set "
                        + "applicableTo = :applicableTo "
                        + "where applicableFrom = :applicableFrom "
                        + "and entryFrom = :entryFrom "
                        + "and site_Code = :site_Code "
                        + "and organizationCode = :organizationCode "
                        + sqlCondition;
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setDate("applicableTo", DmServer.convertDate(applicableTo));
                psTemp1.setDate("applicableFrom", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("applicableFrom"))));
                psTemp1.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                psTemp1.setString("site_Code", site_Code);
                psTemp1.setString("organizationCode", organizationCode);
                psTemp1.setString("keyCode", keyCode);
                psTemp1.executeUpdate();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "delete from patientServicePriceList "
                    + "where applicableFrom = :applicableFrom "
                    + "and entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and organizationCode = :organizationCode "
                    + sqlCondition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("organizationCode", organizationCode);
            psTemp.setString("keyCode", keyCode);
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (psTemp1 != null) {
                    psTemp1.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public static void savePriceList(String applicableFrom, PriceListEntryFrom priceListEntryFrom,
            String keyCode, String rateListCode, String site_Code, List<ServicePriceList_Mdl> priceList, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        String sqlQuery, sqlCondition, applicableTo = "31-Dec-9999", maxApplicableDate = "";
        ResultSet rsTemp = null, rsTemp1 = null;

        chkSave(priceListEntryFrom, priceList, keyCode);

        switch (priceListEntryFrom) {
            case CONSULTANT_MASTER:
                sqlCondition = "and consultantCode = :keyCode ";
                break;
            case ORGANIZATION_MASTER:
                sqlCondition = "and organizationCode = :keyCode ";
                break;
            case RATE_LIST_MASTER:
                sqlCondition = "and serviceCode = :keyCode and (organizationCode is null or consultantCode is null) ";
                break;
            default:
                throw new DmException(0, "Price List: Entry From not found.");
        }

        try {
            sqlQuery = "select Max(applicableFrom) as applicableFrom,Min(applicableFrom) as applicableFromMin  "
                    + "from patientServicePriceList "
                    + "where entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and rateListCode = :rateListCode "
                    + sqlCondition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("rateListCode", rateListCode);
            psTemp.setString("keyCode", keyCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFrom"));
            }
            rsTemp.close();
            psTemp.close();

            if (!maxApplicableDate.equalsIgnoreCase("")) {
                if (DmServer.convertDate(applicableFrom).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                    sqlQuery = "select applicableFrom,applicableTo "
                            + "from patientServicePriceList "
                            + "where entryFrom = :entryFrom "
                            + "and site_Code = :site_Code "
                            + "and rateListCode = :rateListCode "
                            + "and applicableFrom < :applicableFrom "
                            + sqlCondition
                            + "group by applicableFrom,applicableTo "
                            + "order by applicableFrom desc "
                            + "offset 0 rows fetch next 1 rows only ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
                    psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                    psTemp.setString("site_Code", site_Code);
                    psTemp.setString("rateListCode", rateListCode);
                    psTemp.setString("keyCode", keyCode);
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
                        sqlQuery = "update patientServicePriceList set "
                                + "applicableTo = :applicableTo "
                                + "where applicableFrom = :applicableFrom "
                                + "and entryFrom = :entryFrom "
                                + "and site_Code = :site_Code "
                                + "and rateListCode = :rateListCode "
                                + sqlCondition;
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(applicableFrom))));
                        psTemp1.setDate("applicableFrom", rsTemp.getDate("ApplicableFrom"));
                        psTemp1.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                        psTemp1.setString("site_Code", site_Code);
                        psTemp1.setString("rateListCode", rateListCode);
                        psTemp1.setString("keyCode", keyCode);
                        psTemp1.executeUpdate();
                        psTemp1.close();

                    } else {
                        sqlQuery = "select applicableFrom,applicableTo "
                                + "FROM patientServicePriceList "
                                + "where applicableFrom > :applicableFrom "
                                + "and entryFrom = :entryFrom "
                                + "and site_Code = :site_Code "
                                + "and rateListCode = :rateListCode "
                                + sqlCondition
                                + "group by applicableFrom,applicableTo "
                                + "order by applicableFrom asc offset 0 rows fetch next 1 rows only ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
                        psTemp1.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                        psTemp1.setString("site_Code", site_Code);
                        psTemp1.setString("rateListCode", rateListCode);
                        psTemp1.setString("keyCode", keyCode);
                        rsTemp1 = psTemp1.executeQuery();
                        if (rsTemp1.next()) {
                            applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFrom")));
                        }
                        psTemp1.close();
                        rsTemp1.close();
                    }
                    psTemp.close();
                    rsTemp.close();
                } else {
                    sqlQuery = "update patientServicePriceList set "
                            + "applicableTo = :applicableTo "
                            + "where applicableFrom = :applicableFrom "
                            + "and entryFrom = :entryFrom "
                            + "and site_Code = :site_Code "
                            + "and rateListCode = :rateListCode "
                            + sqlCondition;
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFrom", DmServer.convertDate(maxApplicableDate));
                    psTemp.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(applicableFrom))));
                    psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                    psTemp.setString("site_Code", site_Code);
                    psTemp.setString("rateListCode", rateListCode);
                    psTemp.setString("keyCode", keyCode);
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            sqlQuery = "delete from patientServicePriceList "
                    + "where applicableFrom = :applicableFrom "
                    + "and entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and rateListCode= :rateListCode "
                    + sqlCondition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("rateListCode", rateListCode);
            psTemp.setString("keyCode", keyCode);
            psTemp.executeUpdate();
            psTemp.close();

            for (ServicePriceList_Mdl spl : priceList) {
                if (!DmShared.xNull(spl.getServiceCode()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "insert into patientServicePriceList(entryFrom, entryFromNature, applicableFrom, applicableTo, serviceCode, rateListCode,"
                            + "         organizationServiceCode, organizationCode, roomCatCode, consultantCode, v_Type, site_code, nature, dayNameCode, "
                            + "         timeFrom,timeTo, rate, discount, discountPer, printSNo) "
                            + "values (:entryFrom, :entryFromNature, :applicableFrom, :applicableTo, :serviceCode, :rateListCode, :organizationServiceCode, "
                            + "         :organizationCode, :roomCatCode, :consultantCode, :v_Type, :site_code, :nature, :dayNameCode, :timeFrom, "
                            + "         :timeTo, :rate, :discount, :discountPer, :printSNo) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                    psTemp.setString("entryFromNature", DmShared.iNull(spl.getEntryFromNature()).toString());
                    psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
                    psTemp.setDate("applicableTo", DmServer.convertDate(applicableTo));
                    psTemp.setString("serviceCode", spl.getServiceCode());
                    psTemp.setString("organizationServiceCode", DmShared.foriegnKey(spl.getOrganizationServiceCode()));
                    psTemp.setString("organizationCode", DmShared.foriegnKey(spl.getOrganizationCode()));
                    psTemp.setString("rateListCode", DmShared.foriegnKey(rateListCode));
                    psTemp.setString("site_Code", site_Code);
                    psTemp.setString("roomCatCode", DmShared.foriegnKey(spl.getRoomCatCode()));
                    psTemp.setString("consultantCode", DmShared.foriegnKey(spl.getConsultantCode()));
                    psTemp.setString("v_Type", DmShared.foriegnKey(spl.getVoucherType()));
                    psTemp.setInt("nature", spl.getPriceListNature().getId());
                    psTemp.setString("dayNameCode", DmShared.iNull(spl.getDayNameCode()).toString());
                    psTemp.setString("timeFrom", DmShared.xNull(spl.getTime()).trim().equalsIgnoreCase("") ? null : spl.getTime());
                    psTemp.setString("timeTo", DmShared.xNull(spl.getTime()).trim().equalsIgnoreCase("") ? null : spl.getTimeTo());
                    psTemp.setString("rate", DmServer.vNull(spl.getRate(), "0.00"));
                    psTemp.setString("discount", DmServer.vNull(spl.getDiscAmt(), "0.00"));
                    psTemp.setString("discountPer", DmServer.vNull(spl.getDiscPercentage(), "0.00"));
                    psTemp.setString("printSNo", DmShared.foriegnKey(spl.getPrintSNo()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (rsTemp1 != null) {
                    rsTemp1.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (psTemp1 != null) {
                    psTemp1.close();
                }
            } catch (Exception e) {
            }
        }

    }

    private static void chkSave(PriceListEntryFrom priceListEntryFrom, List<ServicePriceList_Mdl> priceList, String keyCode) {

        for (ServicePriceList_Mdl spl : priceList) {
            if (!DmShared.xNull(spl.getServiceCode()).trim().equalsIgnoreCase("")
                    && (DmShared.vNull(spl.getRate()).doubleValue() != 0)) {

                if (DmShared.xNull(spl.getSiteCode()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Price List: Site is mandatory.");
                }

                if (DmShared.xNull(spl.getRateListCode()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Price List: Rate List name is mandatory.");
                }
                if (DmShared.xNull(spl.getRateListType().getText()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Price List: Rate List Type  is mandatory.");
                }

                if (DmShared.vNull(spl.getPriceListEntryFrom().toInt()) == 0) {
                    throw new DmException(0, "Price List: Entry From is mandatory.");
                }

                switch (priceListEntryFrom) {
                    case CONSULTANT_MASTER:
                        chkConsultantMaster(spl, priceList, keyCode);
                        break;
                    case ORGANIZATION_MASTER:
                    case RATE_LIST_MASTER:
                        break;
                    default:
                        throw new DmException(0, "Price List: Entry From not found.");
                }

            }
        }
    }

    private static void chkConsultantMaster(ServicePriceList_Mdl spl, List<ServicePriceList_Mdl> priceList, String consultantCode) {

        if (DmShared.xNull(spl.getConsultantCode()).trim().equalsIgnoreCase("")) {
            throw new DmException(0, "Price List: Consultant is mandatory.");
        }

        if (!DmShared.xNull(spl.getConsultantCode()).trim().equalsIgnoreCase(consultantCode)) {
            throw new DmException(0, "Price List: Invalid Consultant.");
        }
    }

    public static ConsultantDiary_Mdl saveDiary(boolean isNew, String userName, String siteCode, String vCat, ConsultantDiary_Mdl consultantDiary, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition, errorSource = "consultantDiary";
        boolean flag = (consultantDiary.getStatus() == DiaryStatus.Cancel || consultantDiary.getStatus() == DiaryStatus.Postpone);
        try {
            if (consultantDiary.getStatus() != DiaryStatus.Cancel) {
                if (DmShared.iNull(siteCode).longValue() == 0) {
                    throw new DmException(0, errorSource, "", "siteCode is mandatory.");
                }

                if (DmShared.iNull(consultantDiary.getDocId()).longValue() == 0) {
                    throw new DmException(0, errorSource, "", "docId is mandatory.");
                }

                if (DmShared.iNull(consultantDiary.getV_SNo()).longValue() == 0) {
                    throw new DmException(0, errorSource, "", "v_SNo is mandatory.");
                }

                if (DmShared.iNull(consultantDiary.getV_Type()).longValue() == 0) {
                    throw new DmException(0, errorSource, "", "v_Type is mandatory.");
                }

                if (DmShared.xNull(consultantDiary.getV_Date()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, errorSource, "", "v_Date is mandatory.");
                }

                if (DmShared.vNull(consultantDiary.getTimeFrom()).doubleValue() > DmShared.vNull(consultantDiary.getTimeTo()).doubleValue()) {
                    throw new DmException(0, errorSource, "", "fromTime can not be greater than toTime.");
                }

                if (DmShared.iNull(consultantDiary.getConsultantCode()).longValue() == 0 && DmShared.iNull(consultantDiary.getConsultationRoomCode()).longValue() == 0) {
                    throw new DmException(0, errorSource, "", "Consultant or Sitting Location is mandatory.");
                }

                if (consultantDiary.getNature() == null) {
                    throw new DmException(0, errorSource, "", "Nature is mandatory.");
                }

                if (DmShared.xNull(consultantDiary.getName()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, errorSource, "", "Name is mandatory.");
                }

                if (DmShared.xNull(consultantDiary.getMobileNo()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, errorSource, "", "Mobile no. is mandatory.");
                }

                if (DmShared.iNull(consultantDiary.getRegDocId()).longValue() != 0) {
                    if (DmShared.iNull(consultantDiary.getUhid()).longValue() == 0) {
                        throw new DmException(0, errorSource, "", "UHID No. is mandatory.");
                    }
                }

                if (consultantDiary.getStatus() == null) {
                    throw new DmException(0, errorSource, "", "status is mandatory.");
                }

                if (consultantDiary.getStatus() == DiaryStatus.Postpone) {
                    if (DmShared.iNull(consultantDiary.getDiaryDocId()).longValue() == 0) {
                        throw new DmException(0, errorSource, "", "dairyDocId is mandatory.");
                    }
                }

                if (DmShared.iNull(consultantDiary.getDiaryDocId()).longValue() != 0) {
                    if (DmShared.iNull(consultantDiary.getDiaryV_SNo()).longValue() == 0) {
                        throw new DmException(0, errorSource, "", "dairyV_Sno is mandatory.");
                    }
                }

                if (DmShared.xNull(vCat).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, errorSource, "", "voucher category is mandatory.");
                }
                if (isNew) {
                    if (DmShared.iNull(consultantDiary.getConsultationRoomCode()).longValue() != 0) {
                        condition = " and cd.roomCode = :roomCode ";
                    } else {
                        condition = " and cd.consultantCode = :consultantCode ";
                    }
                    sqlQuery = "Select  isNull(max(cd.tokenNo),0) + 1 as tokenNo "
                            + "         from consultantDiary cd "
                            + "         left join voucher_Type vt on vt.v_Type = cd.v_Type "
                            + "         where cd.v_Date = :v_Date and cd.site_Code = :site_Code and vt.category = :vCat " + condition;
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("v_Date", DmServer.convertDate(consultantDiary.getV_Date()));
                    if (DmShared.iNull(consultantDiary.getConsultationRoomCode()).longValue() != 0) {
                        psTemp.setString("roomCode", DmShared.iNull(consultantDiary.getConsultationRoomCode()).toString());
                    } else {
                        psTemp.setString("consultantCode", DmShared.iNull(consultantDiary.getConsultantCode()).toString());
                    }
                    psTemp.setString("site_Code", DmShared.iNull(siteCode).toString());
                    psTemp.setString("vCat", DmShared.xNull(vCat));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        consultantDiary.setTokenNo(rsTemp.getString("tokenNo"));
                    }
                    rsTemp.close();
                    psTemp.close();

                    sqlQuery = "Select  cd.docId "
                            + "         from consultantDiary as cd "
                            + "         left join voucher_Type as vt on vt.v_Type = cd.v_Type "
                            + "         where cd.docid <> :docId " + condition
                            + "         and cd.v_Date = :v_Date and cd.tokenNo = :tokenNo and vt.category = :vCat and cd.site_code = :site_code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docid", DmShared.iNull(consultantDiary.getDocId()).toString());
                    if (DmShared.iNull(consultantDiary.getConsultationRoomCode()).longValue() != 0) {
                        psTemp.setString("roomCode", DmShared.iNull(consultantDiary.getConsultationRoomCode()).toString());
                    } else {
                        psTemp.setString("consultantCode", DmShared.iNull(consultantDiary.getConsultantCode()).toString());
                    }
                    psTemp.setDate("v_Date", DmServer.convertDate(consultantDiary.getV_Date()));
                    psTemp.setString("tokenNo", consultantDiary.getTokenNo());
                    psTemp.setString("vCat", DmShared.xNull(vCat));
                    psTemp.setString("site_code", DmShared.iNull(siteCode).toString());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        throw new DmException(300, "Duplicate consultant dairy Token no.");
                    }
                    rsTemp.close();
                    psTemp.close();

                    sqlQuery = "Insert into consultantDiary (docId, v_SNo, v_Type, v_Date, timeFrom, timeTo, consultantCode, roomCode, "
                            + " nature, tokenNo, serviceDocId, serviceV_SNo, uhid, regDocId, name, mobile, email, remark, "
                            + " diaryDocId, diaryV_SNo, priorityTypeCode, isStatus, u_EntDt, preparedBy, site_Code, statusRemark) "
                            + " values(:docId, :v_SNo, :v_Type, :v_Date, :timeFrom, :timeTo, :consultantCode, :roomCode, "
                            + " :nature, :tokenNo, :serviceDocId, :serviceV_SNo, :uhid, :regDocId, :name, :mobile, :email, :remark, "
                            + " :diaryDocId, :diaryV_SNo, :priorityTypeCode, :isStatus, :u_EntDt, :preparedBy, :site_Code, :statusRemark) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(consultantDiary.getDocId()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(consultantDiary.getV_SNo()).toString());
                    psTemp.setString("v_Type", DmShared.iNull(consultantDiary.getV_Type()).toString());
                    psTemp.setDate("v_Date", DmServer.convertDate(consultantDiary.getV_Date()));
                    psTemp.setString("timeFrom", DmShared.vNull(consultantDiary.getTimeFrom()).toString());
                    psTemp.setString("timeTo", DmShared.vNull(consultantDiary.getTimeTo()).toString());
                    psTemp.setString("consultantCode", DmShared.foriegnKey(consultantDiary.getConsultantCode()));
                    psTemp.setString("roomCode", DmShared.foriegnKey(consultantDiary.getConsultationRoomCode()));
                    psTemp.setString("nature", DmShared.iNull(consultantDiary.getNature().toInt()).toString());
                    psTemp.setString("tokenNo", consultantDiary.getTokenNo());
                    psTemp.setString("serviceDocId", DmShared.foriegnKey(consultantDiary.getServiceDocId()));
                    psTemp.setString("serviceV_SNo", DmShared.foriegnKey(consultantDiary.getServiceV_SNo()));
                    psTemp.setString("uhid", DmShared.foriegnKey(consultantDiary.getUhid()));
                    psTemp.setString("regDocId", DmShared.foriegnKey(consultantDiary.getRegDocId()));
                    psTemp.setString("name", consultantDiary.getName());
                    psTemp.setString("mobile", consultantDiary.getMobileNo());
                    psTemp.setString("email", consultantDiary.getEmail());
                    psTemp.setString("remark", DmShared.xNull(consultantDiary.getRemark()));
                    psTemp.setString("diaryDocId", DmShared.foriegnKey(DmShared.iNull(consultantDiary.getDiaryDocId()).toString()));
                    psTemp.setString("diaryV_SNo", DmShared.foriegnKey(DmShared.iNull(consultantDiary.getDiaryV_SNo()).toString()));
                    psTemp.setString("priorityTypeCode", DmShared.foriegnKey(consultantDiary.getPriorityTypeCode()));
                    psTemp.setString("isStatus", DmShared.iNull(consultantDiary.getStatus().toInt()).toString());
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.setString("preparedBy", DmShared.xNull(userName));
                    psTemp.setString("statusRemark", DmShared.xNull(consultantDiary.getStatusRemark()));
                    psTemp.setString("site_Code", DmShared.iNull(siteCode).toString());
                    psTemp.executeUpdate();
                } else {
                    sqlQuery = "Update consultantDiary set "
                            + " serviceDocId = :serviceDocId, "
                            + " serviceV_SNo = :serviceV_SNo, "
                            + " uhid = :uhid, "
                            + " name = :name, "
                            + " mobile = :mobile, "
                            + " email = :email, "
                            + " remark = :remark,"
                            + " regDocId = :regDocId, "
                            + " priorityTypeCode = :priorityTypeCode, "
                            + " isStatus = :isStatus,"
                            + " statusEntDt = :uEntDt,"
                            + " u_Name = :u_Name "
                            + " where docId = :docId and v_SNo = :v_SNo ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(consultantDiary.getDocId()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(consultantDiary.getV_SNo()).toString());
                    psTemp.setString("serviceDocId", DmShared.foriegnKey(flag ? "" : consultantDiary.getServiceDocId()));
                    psTemp.setString("serviceV_SNo", DmShared.foriegnKey(flag ? "" : consultantDiary.getServiceV_SNo()));
                    psTemp.setString("uhid", DmShared.foriegnKey(flag ? "" : consultantDiary.getUhid()));
                    psTemp.setString("regDocId", DmShared.foriegnKey(flag ? "" : consultantDiary.getRegDocId()));
                    psTemp.setString("name", flag ? "" : consultantDiary.getName());
                    psTemp.setString("mobile", flag ? "" : consultantDiary.getMobileNo());
                    psTemp.setString("email", flag ? "" : consultantDiary.getEmail());
                    psTemp.setString("priorityTypeCode", DmShared.foriegnKey(flag ? "" : consultantDiary.getPriorityTypeCode()));
                    psTemp.setString("isStatus", DmShared.iNull(consultantDiary.getStatus().toInt()).toString());
                    psTemp.setString("u_Name", DmShared.xNull(userName));
                    psTemp.setString("remark", DmShared.xNull(flag ? "" : consultantDiary.getRemark()));
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                }
            } else {
                sqlQuery = "Select  cd.uhid, cd.regDocId, cd.isStatus, cd.serviceDocId "
                        + "         from consultantDiary cd "
                        + "         where cd.docId = :docId and cd.v_Sno = :v_Sno ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", consultantDiary.getDocId());
                psTemp.setString("v_Sno", consultantDiary.getV_SNo());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == DiaryStatus.Postpone.toInt()) {
                        throw new DmException(0, "Appointment already postponed.You Can't change.");
                    } else if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == DiaryStatus.Cancel.toInt()) {
                        throw new DmException(0, "Appointment already cancelled.You Can't change.");
                    } else if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == DiaryStatus.Attended.toInt()) {
                        throw new DmException(0, "Appointment already Attended.You Can't change.");
                    }
                    if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() > 0) {
                        throw new DmException(0, "Appointment can't cancel.Record have been reffered to consultation.");
                    }
                    if (DmShared.iNull(rsTemp.getString("uhid")).longValue() == 0) {
                        throw new DmException(0, "Appointment not found.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "Update consultantDiary set "
                        + " statusRemark = :remark,"
                        + " isStatus = :isStatus,"
                        + " statusEntDt = :uEntDt,"
                        + " u_Name = :u_Name "
                        + " where docId = :docId and v_SNo = :v_SNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(consultantDiary.getDocId()).toString());
                psTemp.setString("v_SNo", DmShared.iNull(consultantDiary.getV_SNo()).toString());
                psTemp.setString("isStatus", DmShared.iNull(consultantDiary.getStatus().toInt()).toString());
                psTemp.setString("u_Name", DmShared.xNull(userName));
                psTemp.setString("remark", DmShared.xNull(consultantDiary.getStatusRemark()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return consultantDiary;
    }

//    public static void changeDiaryStatus(boolean isNew, String userName, String siteCode, ConsultantDiary_Mdl consultantDiary, Connection cnTemp) throws Exception {
//        DmPreparedStatement psTemp = null;
//        String sqlQuery;
//
//        new DmException(0, "", "", "form not working.");
//
//        try {
//
//            sqlQuery = "update consultantDiary set "
//                    + "statusRemark = :statusRemark, "
//                    + "isStatus = :isStatus,"
//                    + "statusU_EntDt = now(),"
//                    + "statusModifiedBy = :u_Name "
//                    + "where docId = :docId and v_SNo = :v_SNo ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(consultantDiary.getDocId()).toString());
//            psTemp.setString("v_SNo", DmShared.iNull(consultantDiary.getV_SNo()).toString());
//            psTemp.setString("isStatus", DmShared.iNull(consultantDiary.getStatus().toInt()).toString());
//            psTemp.setString("u_Name", DmShared.xNull(userName));
//            psTemp.executeUpdate();
//
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//
//    }
    public static ConvertedQuantity getSecondryQty(String skuQty, String convertionRate) {
        ConvertedQuantity convertedQuantity = new ConvertedQuantity();
        double skuQtyTemp, convertionTemp;

        skuQtyTemp = DmShared.vNull(DmServer.vNull(skuQty, "#.#######"));
        convertionTemp = DmShared.vNull(DmServer.vNull(convertionRate, "#.#######"));
        if (convertionTemp == 0) {
            convertedQuantity.setSecondryPartQty("0");
            convertedQuantity.setSkuPartQty(String.valueOf(DmShared.iNull(skuQtyTemp).intValue()));
        } else if (skuQtyTemp != 0) {
            convertedQuantity.setSecondryPartQty(String.valueOf(DmShared.iNull(skuQtyTemp / convertionTemp).intValue()));
            convertedQuantity.setSkuPartQty(String.valueOf(DmShared.iNull(skuQtyTemp % convertionTemp).intValue()));
        } else {
            convertedQuantity.setSecondryPartQty("0");
            convertedQuantity.setSkuPartQty("0");
        }

        convertedQuantity.setDisplayQty(convertedQuantity.getSecondryPartQty() + " : " + convertedQuantity.getSkuPartQty());

        return convertedQuantity;
    }

//    public static double getBalanceUHID(String docId, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery;
//        double balance = 0;
//
//        try {
//            sqlQuery = "select	pu.docId, (sum(isNull(psd.oh_Amt_Net,0)) - sum(isNull(prec.amount,0))) as balAmt "
//                    + "		from patientUHID as pu  "
//                    + "		left join patientRegistration as pr on pu.docId = pr.uhid and pr.isCancelled <> 1 "
//                    + "		left join ( "
//                    + "                     select  regDocId, sum(isNull(amount,0)) as amount "
//                    + "                             from patientReceipt  "
//                    + "                             where isCancelled <> 1 "
//                    + "                             group by regDocId "
//                    + "         ) as prec on pr.regDocId = prec.regDocId  "
//                    + "		left join  "
//                    + "			( "
//                    + "                     select  regDocId, sum(isNull(oh_Amt_Net,0)) as oh_Amt_Net "
//                    + "                             from patientServiceDetail  "
//                    + "                             where isCancelled <> 1 and "
//                    + "                             billingNature <> " + String.valueOf(BillingNature.NON_BILLABLE.getId()) + " "
//                    + "                             group by regDocId "
//                    + "			) as psd on pr.regDocId = psd.regDocId  "
//                    + "		where pu.isCancelled <> 1 and pu.docId = :docId "
//                    + "		group by pu.docId  ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(docId).toString());
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                balance = DmShared.vNull(DmServer.vNull(rsTemp.getString("balAmt"), "0.00"));
//            }
//            psTemp.close();
//            rsTemp.close();
//
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        }
//
//        return balance;
//    }
//
//    public static double getBalanceRegistration(String docId, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery;
//        double balance = 0;
//
//        try {
//            sqlQuery = "select	pr.regDocId, (isNull(psd.oh_Amt_Net,0) - isNull(prec.amount,0)) as balAmt "
//                    + "		from patientRegistration as pr  "
//                    + "		left join "
//                    + "                  ( "
//                    + "                         select  regDocId, sum(isNull(amount,0)) as amount "
//                    + "                                 from patientReceipt  "
//                    + "                                 where isCancelled <> 1 and regDocId = :regDocId "
//                    + "					group by regDocId "
//                    + "                   ) as prec on pr.regDocId = prec.regDocId  "
//                    + "		left join "
//                    + "                   ( "
//                    + "                         select  regDocId, sum(isNull(oh_Amt_Net,0)) as oh_Amt_Net "
//                    + "                                 from patientServiceDetail  "
//                    + "					where isCancelled <> 1 and regDocId = :regDocId and "
//                    + "                                 billingNature <> " + String.valueOf(BillingNature.NON_BILLABLE.getId()) + " "
//                    + "					group by regDocId "
//                    + "                    ) as psd on pr.regDocId = psd.regDocId  "
//                    + "		where pr.isCancelled <> 1 and pr.regDocId = :regDocId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("regDocId", DmShared.iNull(docId).toString());
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                balance = DmShared.vNull(DmServer.vNull(rsTemp.getString("balAmt"), "0.00"));
//            }
//            psTemp.close();
//            rsTemp.close();
//
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        }
//
//        return balance;
//    }
    public static double getBalanceUHID(String docId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double balance = 0;

        try {
//            sqlQuery = "Select  sum(blu.balance) as balance "
//                    + "		from ( "
//                    + "			Select	max(pr.uhid) as uhid, case when (isNull(sum(bl.serviceAmt),0) - isNull(sum(bl.receiptAmt),0)) < 0 then  "
//                    + "				(isNull(sum(bl.serviceAmt),0) - isNull(sum(bl.receiptAmt),0)) else "
//                    + "				(isNull(sum(bl.serviceAmt),0) - (isNull(sum(bl.receiptAmt),0) + isNull(sum(bl.plaAmt),0))) end as balance "
//                    + "			from patientRegistration as pr "
//                    + "			left join ( "
//                    + "                          Select	psd.regDocId, sum(psd.oh_Amt_Net) as serviceAmt, 0 as receiptAmt, 0 as plaAmt "
//                    + "                                 from patientServiceDetail as psd "
//                    + "					where psd.isCancelled = 0 and psd.billingNature in (" + BillingNature.GENERAL.getId() + "," + BillingNature.BILLABLE.getId() + ") "
//                    + "					group by psd.regDocId "
//                    + "				union all "
//                    + "                         Select	prec.regDocId, 0 as serviceAmt, sum(prec.amount) as receiptAmt, 0 as plaAmt "
//                    + "                                 from patientReceipt as prec "
//                    + "					where prec.isCancelled = 0 "
//                    + "					group by prec.regDocId "
//                    + "				union all "
//                    + "				Select	pla.regDocId, 0 as serviceAmt, 0 as receiptAmt, sum(pla.amount) as plaAmt "
//                    + "                                 from patientLimitApproval as pla "
//                    + "					where pla.isCancelled = 0 and pla.nature in (" + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + "," + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ") "
//                    + "					group by pla.regDocId "
//                    + "			) as bl on pr.regDocId = bl.regDocId "
//                    + "			where pr.isCancelled = 0 and pr.uhid = :uhid "
//                    + "			group by bl.regDocId "
//                    + "		) as blu "
//                    + "		group by blu.uhid ";

            sqlQuery = "select  sum(tmp.serviceAmount)-sum(tmp.receiptAmount) as balance  \n"
                    + "\n"
                    + "from (\n"
                    + "select  isnull(sum(psd.oh_Amt_Net),0) as serviceAmount,  0 as receiptAmount \n"
                    + "\n"
                    + "from patientServiceDetail  as psd "
                    + "left join patientRegistration as pr on pr.regDocId=psd.regDocId  "
                    + "\n"
                    + " where psd.isCancelled=0 and pr.uhid= :uhid \n"
                    + "  and psd.billingNature in (" + BillingNature.GENERAL.getId() + "," + BillingNature.BILLABLE.getId() + ")  "
                    + "  union all  "
                    + " select   0 as serviceAmount , isnull(sum(prc.amount),0)as receiptAmount \n"
                    + "\n"
                    + " from patientReceipt as prc  \n "
                    + "  left join voucher_Type as vtrc on vtrc.v_Type=prc.v_Type   "
                    + "\n"
                    + " left join patientRegistration as pr on pr.regDocId=prc.regDocId  "
                    + "   and vtrc.category not in ('" + pvc.ORGANIZATION_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "','" + pvc.ORGANIZATION_BILL_SETTLEMENT_RECEIPT + "' "
                    + " , '" + pvc.PHARMACY_BILL_RECEIPT + "' )  "
                    + " where  prc.iscancelled=0 "
                    + "  and pr.uhid= :uhid \n "
                    + " ) as tmp ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("uhid", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                balance = DmShared.vNull(DmServer.vNull(rsTemp.getString("balance"), "0.00"));
            }
            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return balance;
    }

    public static double getBalanceRegistration(String regDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double balance = 0;

        try {

//            sqlQuery = "Select	case when (isNull(sum(bl.serviceAmt),0) - isNull(sum(bl.receiptAmt),0)) < 0 then "
//                    + "                   (isNull(sum(bl.serviceAmt),0) - isNull(sum(bl.receiptAmt),0)) else "
//                    + "                   (isNull(sum(bl.serviceAmt),0) - (isNull(sum(bl.receiptAmt),0) + isNull(sum(bl.plaAmt),0))) end as balance "
//                    + "		from ( "
//                    + "                 Select	psd.regDocId, sum(psd.oh_Amt_Net) as serviceAmt, 0 as receiptAmt, 0 as plaAmt "
//                    + "                         from patientServiceDetail as psd "
//                    + "				where psd.isCancelled = 0 and psd.regDocId = :regDocId "
//                    + "				and psd.billingNature in (" + BillingNature.GENERAL.getId() + "," + BillingNature.BILLABLE.getId() + ") "
//                    + "				group by psd.regDocId "
//                    + "			union all "
//                    + "			Select	prec.regDocId, 0 as serviceAmt, sum(prec.amount) as receiptAmt, 0 as plaAmt "
//                    + "                         from patientReceipt as prec "
//                    + "				where prec.isCancelled = 0 and prec.regDocId = :regDocId "
//                    + "				group by prec.regDocId "
//                    + "			union all "
//                    + "			Select	pla.regDocId, 0 as serviceAmt, 0 as receiptAmt, sum(pla.amount) as plaAmt "
//                    + "                         from patientLimitApproval as pla "
//                    + "				where pla.isCancelled = 0 and pla.regDocId = :regDocId "
//                    + "                         and pla.nature in (" + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + "," + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ") "
//                    + "				group by pla.regDocId "
//                    + "		) as bl "
//                    + "		group by bl.regDocId ";
//            
            sqlQuery = "select  sum(tmp.serviceAmount)-sum(tmp.receiptAmount) as balance "
                    + "from (  "
                    + "select  isnull(sum(psd.oh_Amt_Net),0) as serviceAmount,  0 as receiptAmount from patientServiceDetail  as psd where psd.isCancelled=0  "
                    + " and psd.regDocId= :regDocId  "
                    + " and psd.billingNature in (" + BillingNature.GENERAL.getId() + "," + BillingNature.BILLABLE.getId() + ") "
                    + "  union all  "
                    + "select   0 as serviceAmount , isnull(sum(prc.amount),0) as receiptAmount  "
                    + " from patientReceipt  prc  "
                    + "  left join voucher_Type as vtrc on vtrc.v_Type=prc.v_Type  "
                    + " where  iscancelled=0 "
                    + " and regDocId= :regDocId "
                    + "    and vtrc.category not in ('" + pvc.ORGANIZATION_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "','" + pvc.ORGANIZATION_BILL_SETTLEMENT_RECEIPT + "'"
                    + " ,'" + pvc.PHARMACY_BILL_RECEIPT + "') "
                    + ") as tmp";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                balance = DmShared.vNull(DmServer.vNull(rsTemp.getString("balance"), "0.00"));
            }
            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return balance;
    }

    public static double getBalanceDiagnostic(String uhid, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double balance = 0;

        try {
            sqlQuery = "Select  sum(blu.balance) as balance "
                    + "		from ( "
                    + "			Select	max(pr.uhid) as uhid, case when (isNull(sum(bl.serviceAmt),0) - isNull(sum(bl.receiptAmt),0)) < 0 then  "
                    + "					(isNull(sum(bl.serviceAmt),0) - isNull(sum(bl.receiptAmt),0)) else "
                    + "					(isNull(sum(bl.serviceAmt),0) - (isNull(sum(bl.receiptAmt),0) + isNull(sum(bl.plaAmt),0))) end as balance "
                    + "			from patientRegistration as pr "
                    + "			left join ( "
                    + "					Select	psd.regDocId, sum(psd.oh_Amt_Net) as serviceAmt, 0 as receiptAmt, 0 as plaAmt "
                    + "							from patientRegistration as pr "
                    + "							left join voucher_Type as vtPR on pr.v_Type = vtPR.v_Type "
                    + "                                                 left join patientServiceDetail as psd on pr.regDocId = psd.regDocId "
                    + "							left join voucher_Type as vt on psd.v_Type = vt.v_Type "
                    + "							where psd.isCancelled = 0 and psd.billingNature in (" + BillingNature.GENERAL.getId() + "," + BillingNature.BILLABLE.getId() + ") "
                    + "							and vt.category in ('" + pvc.LAB_REGISTRATION + "', '" + pvc.LAB_CANCELATION + "', '" + pvc.LAB_DISCOUNT + "') "
                    + "							and vtPR.category in ('" + pvc.LAB_REGISTRATION + "') "
                    + "							group by psd.regDocId "
                    + "					union all "
                    + "					Select	prec.regDocId, 0 as serviceAmt, sum(prec.amount) as receiptAmt, 0 as plaAmt "
                    + "							from patientReceipt as prec "
                    + "							left join voucher_Type as vt on prec.v_Type = vt.v_Type "
                    + "							where prec.isCancelled = 0 and vt.category in ('" + pvc.LAB_REGISTRATION_RECEIPT + "', '" + pvc.LAB_RECEIPT + "', '" + pvc.LAB_REFUND_RECEIPT + "') "
                    + "							group by prec.regDocId "
                    + "					union all "
                    + "					Select	pla.regDocId, 0 as serviceAmt, 0 as receiptAmt, sum(pla.amount) as plaAmt "
                    + "							from patientLimitApproval as pla "
                    + "							left join voucher_Type as vt on pla.v_Type = vt.v_Type "
                    + "							where pla.isCancelled = 0 and pla.nature in (" + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + "," + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ") "
                    + "							and vt.category in ('" + pvc.LAB_REGISTRATION + "', '" + pvc.LAB_CANCELATION + "') "
                    + "							group by pla.regDocId "
                    + "			) as bl on pr.regDocId = bl.regDocId "
                    + "			where pr.uhid = :uhid "
                    + "			group by bl.regDocId "
                    + "		) as blu "
                    + "		group by blu.uhid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("uhid", DmShared.iNull(uhid).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                balance = DmShared.vNull(DmServer.vNull(rsTemp.getString("balance"), "0.00"));
            }
            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return balance;
    }

    public static void saveCases(String regDocId, String caseCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            String[] codes = DmShared.xNull(caseCode).trim().split(",", -1);

            /*====================================< Delete records from patientReceipt >====================================*/
            sqlQuery = "Delete From patientPrescriptionCase Where docId= :regDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            for (String code : codes) {
                if (DmShared.iNull(code.trim()).longValue() != 0) {
                    /*====================================< Insert record into patientPrescriptionCase >====================================*/
                    sqlQuery = " Insert into patientPrescriptionCase (DocId, caseCode) values (:DocId, :caseCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", DmShared.iNull(regDocId.trim()).toString());
                    psTemp.setString("caseCode", DmShared.iNull(code.trim()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public static String[] getCases(String regDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        String caseCode = "", caseName = "";
        String[] cases = {"", ""};

        try {
            sqlQuery = "select	prc.DocId, prc.caseCode, cm.name as caseName "
                    + "		from patientPrescriptionCase as prc "
                    + "		left join caseMast as cm on prc.caseCode = cm.code "
                    + "		where prc.DocId = :regDocId "
                    + "		order by cm.name, prc.caseCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                caseCode += DmShared.iNull(rsTemp.getString("caseCode")).toString() + ",";
                caseName += DmShared.xNull(rsTemp.getString("caseName")).trim() + ",";
            }
            psTemp.close();
            rsTemp.close();

            if (caseCode.trim().contains(",")) {
                caseCode = caseCode.substring(0, (caseCode.length() - 1));
            }
            if (caseName.trim().contains(",")) {
                caseName = caseName.substring(0, (caseName.length() - 1));
            }

            cases[0] = caseCode;
            cases[1] = caseName;

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return cases;
    }

    public static void saveICDX(String regDocId, String icdxCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            String[] codes = DmShared.xNull(icdxCode).trim().split(",", -1);

            /*====================================< Delete records from patientReceipt >====================================*/
            sqlQuery = "Delete From patientPrescriptionICDX Where docId= :regDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            for (String code : codes) {
                if (DmShared.iNull(code.trim()).longValue() != 0) {
                    /*====================================< Insert record into patientPrescriptionCase >====================================*/
                    sqlQuery = " Insert into patientPrescriptionICDX (DocId, icdxCode) values (:DocId, :icdxCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", DmShared.iNull(regDocId.trim()).toString());
                    psTemp.setString("icdxCode", DmShared.iNull(code.trim()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public static String[] getICDX(String regDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        String icdxCode = "", icdxName = "";
        String[] icdx = {"", ""};

        try {
            sqlQuery = "select	prc.DocId, prc.icdxCode, icm.manualCode as icdxName "
                    + "		from patientPrescriptionICDX as prc "
                    + "		left join icdxMast as icm on prc.icdxCode = icm.code "
                    + "		where prc.DocId = :regDocId "
                    + "		order by icm.name, prc.icdxCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                icdxCode += DmShared.iNull(rsTemp.getString("icdxCode")).toString() + ",";
                icdxName += DmShared.xNull(rsTemp.getString("icdxName")).trim() + ",";
            }
            psTemp.close();
            rsTemp.close();

            if (icdxCode.trim().contains(",")) {
                icdxCode = icdxCode.substring(0, (icdxCode.length() - 1));
            }
            if (icdxName.trim().contains(",")) {
                icdxName = icdxName.substring(0, (icdxName.length() - 1));
            }

            icdx[0] = icdxCode;
            icdx[1] = icdxName;

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return icdx;
    }

    public static void setSsnUserLinkDetails(DmRSS rssTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, departmentCode = "", nursingStationCode = "", allowedDepartmentCodeList = "", allowDiscountPercentUpto = "", allowedLocationCodeList = "";

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "select  ula.departmentCode,ula.nursingStationCodeList,ula.allowedDepartmentCodeList, allowedDiscountPercentUpto ,ula.allowedLocationCodeList"
                    + "         from userLinkAccount ula "
                    + "         where ula.pltCode = :pltCode and ula.user_Name = :user_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("user_Name", DmServer.getSsnUserName(rssTemp));
            psTemp.setString("pltCode", DmServer.getSsnPLTCode(rssTemp));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                allowedDepartmentCodeList = DmShared.xNull(rsTemp.getString("allowedDepartmentCodeList")).trim();
                departmentCode = DmShared.xNull(rsTemp.getString("departmentCode")).trim();
                nursingStationCode = DmShared.xNull(rsTemp.getString("nursingStationCodeList")).trim();
                allowedLocationCodeList = DmShared.xNull(rsTemp.getString("allowedLocationCodeList")).trim();
                allowDiscountPercentUpto = DmShared.xNull(rsTemp.getString("allowedDiscountPercentUpto")).trim();
            }
            rsTemp.close();
            psTemp.close();

            DmServer.setSsnData(rssTemp, "DepartmentCodeList", allowedDepartmentCodeList);
            DmServer.setSsnData(rssTemp, "allowedLocationCodeList", allowedLocationCodeList);
            DmServer.setSsnData(rssTemp, "DepartmentCode", departmentCode);
            DmServer.setSsnData(rssTemp, "NursingStationCode", nursingStationCode);
            DmServer.setSsnData(rssTemp, "allowDiscountPercentUpto", allowDiscountPercentUpto);

            cnTemp.setCatalog(DmServer.getSsnDBName(rssTemp));
            DmServer.setSsnData(rssTemp, "parentDateBase", DmServer.getCodeViaManualCode(DmServer.getSsnSiteCode(rssTemp), "site_code", "enviro", "parentDateBase", cnTemp));

            cnTemp.close();

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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static String getSsnDepartmentCode(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("DepartmentCode"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnParentDateBase(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("parentDateBase"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnNursingStationCode(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("NursingStationCode"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnDepartmentCodeList(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("DepartmentCodeList"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnLocationCodeList(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("AllowedLocationCodeList"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void setSsnUserService(DmRSS rssTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp2 = null;
        ResultSet rsTemp = null, rsTemp2 = null;
        String sqlQuery, serviceName = "", service = "";

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            sqlQuery = " select serviceCode,serviceCategoryCode from userServicePermission where userName = :user ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("user", DmServer.getSsnUserName(rssTemp));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if ((!DmShared.xNull(rsTemp.getString("serviceCode")).trim().equalsIgnoreCase("")) && (!DmShared.xNull(rsTemp.getString("serviceCategoryCode")).trim().equalsIgnoreCase(""))) {
                    serviceName += "'" + DmShared.xNull(rsTemp.getString("serviceCode")).trim() + "',";
                }
                if ((DmShared.xNull(rsTemp.getString("serviceCode")).trim().equalsIgnoreCase("")) && (!DmShared.xNull(rsTemp.getString("serviceCategoryCode")).trim().equalsIgnoreCase(""))) {
                    sqlQuery = " select code from patientServiceMast where categoryCode = :serviceCategoryCode ";
                    psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp2.setString("serviceCategoryCode", DmShared.xNull(rsTemp.getString("serviceCategoryCode")).trim());
                    rsTemp2 = psTemp2.executeQuery();
                    while (rsTemp2.next()) {
                        if (!DmShared.xNull(rsTemp2.getString("code")).trim().equalsIgnoreCase("")) {
                            serviceName += "'" + DmShared.xNull(rsTemp2.getString("code")).trim() + "',";
                        }
                    }
                    psTemp2.close();
                    rsTemp2.close();
                }
            }
            if (!DmShared.xNull(serviceName).trim().equalsIgnoreCase("")) {
                service = serviceName.substring(0, serviceName.length() - 1);
            }
            DmServer.setSsnData(rssTemp, "UserService", DmShared.xNull(service).trim());
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
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
                if (rsTemp2 != null) {
                    rsTemp2.close();
                }
                if (psTemp2 != null) {
                    psTemp2.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static String getSsnUserService(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("UserService"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void setSsnNoOfDiagnostic(DmRSS rssTemp, String noOfDiagnostic) {
        try {
            DmServer.setSsnData(rssTemp, "NoOfDiagnostic", DmShared.iNull(noOfDiagnostic).toString());
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static String getSsnNoOfDiagnostic(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("NoOfDiagnostic"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void setSsnNoOfMachine(DmRSS rssTemp, String noOfMachine) {
        try {
            DmServer.setSsnData(rssTemp, "NoOfMachine", DmShared.iNull(noOfMachine).toString());
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static String getSsnNoOfMachine(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("NoOfMachine"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void checkPatientCreditLimit(String regDocId, String voucherDate, boolean isAllow, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery, balance, todaysLimitDate;
        ResultSet rsTemp = null;
        double todaysLimit = 0;

        balance = DmShared.vNull(STServer.getBalanceRegistration(DmShared.iNull(regDocId).toString(), cnTemp)).toString();

        try {
            sqlQuery = "select creditLimit,warnAtLimit,todaysLimit,todaysLimitDate from patientRegistration2 where regDocId = :docId  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                todaysLimit = 0;
                todaysLimitDate = DmShared.dNull(rsTemp.getDate("todaysLimitDate"));

                if (!(DmShared.xNull(todaysLimitDate).trim().equalsIgnoreCase("")
                        || DmShared.xNull(voucherDate).trim().equalsIgnoreCase(""))) {
                    if (DmShared.convertDate(todaysLimitDate).getTime() == DmShared.convertDate(voucherDate).getTime()) {
                        todaysLimit = DmShared.vNull(rsTemp.getString("todaysLimit"));
                    }
                }

                if (DmShared.vNull(rsTemp.getString("creditLimit")).doubleValue() != 0) {
                    if ((DmShared.vNull(rsTemp.getString("creditLimit")).doubleValue() + DmShared.vNull(todaysLimit).doubleValue()) < DmShared.vNull(balance).doubleValue()) {
                        throw new DmException(0, "Credit limit exceed. You can not save this record.");
                    }
                }

                if (!isAllow) {
                    if (DmShared.vNull(rsTemp.getString("warnAtLimit")).doubleValue() != 0) {
                        if ((DmShared.vNull(rsTemp.getString("warnAtLimit")).doubleValue() < DmShared.vNull(balance).doubleValue())) {
                            throw new DmException(0, "Msg");
                        }
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

    public static void setSsnEnviro(DmRSS rssTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));

            DmServer.setSsnNoOfUser(rssTemp, "-1");
            STServer.setSsnNoOfMachine(rssTemp, "-1");
            STServer.setSsnNoOfDiagnostic(rssTemp, "-1");

            sqlQuery = "Select  e.assetGroupForCSSD "
                    + "         from enviroCompany e ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                DmServer.setSsnData(rssTemp, "assetGroupForCSSD", DmShared.xNull(rsTemp.getString("assetGroupForCSSD")).trim());
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "Select  e.opdAllowPatientForSite, e.ipdAllowPatientForSite, e.pharmacyAllowPatientForSite,"
                    + "         e.pharmacyAllowSaleBillForSite, e.diagnosticAllowPatientForSite, e.dayCareAllowPatientForSite,"
                    + "         e.documentHeader, e.documentFooter, e.documentFooterRegional "
                    + "         from enviro e "
                    + "         where e.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(rssTemp));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                DmServer.setSsnData(rssTemp, "opdAllowPatientForSite", DmShared.xNull(rsTemp.getString("opdAllowPatientForSite")).trim());
                DmServer.setSsnData(rssTemp, "ipdAllowPatientForSite", DmShared.xNull(rsTemp.getString("ipdAllowPatientForSite")).trim());
                DmServer.setSsnData(rssTemp, "pharmacyAllowPatientForSite", DmShared.xNull(rsTemp.getString("pharmacyAllowPatientForSite")).trim());
                DmServer.setSsnData(rssTemp, "pharmacyAllowSaleBillForSite", DmShared.xNull(rsTemp.getString("pharmacyAllowSaleBillForSite")).trim());
                DmServer.setSsnData(rssTemp, "diagnosticAllowPatientForSite", DmShared.xNull(rsTemp.getString("diagnosticAllowPatientForSite")).trim());
                DmServer.setSsnData(rssTemp, "dayCareAllowPatientForSite", DmShared.xNull(rsTemp.getString("dayCareAllowPatientForSite")).trim());
                DmServer.setSsnData(rssTemp, "documentHeader", DmShared.xNull(rsTemp.getString("documentHeader")).trim());
                DmServer.setSsnData(rssTemp, "documentFooter", DmShared.xNull(rsTemp.getString("documentFooter")).trim());
                DmServer.setSsnData(rssTemp, "documentFooterRegional", DmShared.xNull(rsTemp.getString("documentFooterRegional")).trim());
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

            if (DmShared.xNull(getSsnAssetGroupForCSSD(rssTemp)).trim().equalsIgnoreCase("")) {
                DmServer.setSsnData(rssTemp, "assetGroupForCSSD", DmServer.getSsnSiteCode(rssTemp));
            }
            if (DmShared.xNull(getSsnOpdAllowPatientForSite(rssTemp)).trim().equalsIgnoreCase("")) {
                DmServer.setSsnData(rssTemp, "opdAllowPatientForSite", DmServer.getSsnSiteCode(rssTemp));
            }
            if (DmShared.xNull(getSsnIpdAllowPatientForSite(rssTemp)).trim().equalsIgnoreCase("")) {
                DmServer.setSsnData(rssTemp, "ipdAllowPatientForSite", DmServer.getSsnSiteCode(rssTemp));
            }
            if (DmShared.xNull(getSsnPharmacyAllowSaleBillForSite(rssTemp)).trim().equalsIgnoreCase("")) {
                DmServer.setSsnData(rssTemp, "pharmacyAllowSaleBillForSite", DmServer.getSsnSiteCode(rssTemp));
            }
            if (DmShared.xNull(getSsnPharmacyAllowPatientForSite(rssTemp)).trim().equalsIgnoreCase("")) {
                DmServer.setSsnData(rssTemp, "pharmacyAllowPatientForSite", DmServer.getSsnSiteCode(rssTemp));
            }
            if (DmShared.xNull(getSsnDiagnosticAllowPatientForSite(rssTemp)).trim().equalsIgnoreCase("")) {
                DmServer.setSsnData(rssTemp, "diagnosticAllowPatientForSite", DmServer.getSsnSiteCode(rssTemp));
            }
            if (DmShared.xNull(getSsnDayCareAllowPatientForSite(rssTemp)).trim().equalsIgnoreCase("")) {
                DmServer.setSsnData(rssTemp, "dayCareAllowPatientForSite", DmServer.getSsnSiteCode(rssTemp));
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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static String getSsnDocumentHeader(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("documentHeader"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnDocumentFooter(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("documentFooter"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnDocumentFooterRegional(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("documentFooterRegional"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnAssetGroupForCSSD(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("assetGroupForCSSD"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnIpdAllowPatientForSite(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("ipdAllowPatientForSite"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnOpdAllowPatientForSite(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("opdAllowPatientForSite"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnPharmacyAllowPatientForSite(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("pharmacyAllowPatientForSite"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnPharmacyAllowSaleBillForSite(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("pharmacyAllowSaleBillForSite"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnDiagnosticAllowPatientForSite(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("diagnosticAllowPatientForSite"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnDayCareAllowPatientForSite(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("dayCareAllowPatientForSite"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

//    public static void checkEOD(String tableName, String columnName, String columnValue, Connection cnTemp) throws Exception {
//        DmPreparedStatement psTemp = null;
//        String sqlQuery;
//        ResultSet rsTemp = null;
//        try {
//            if (DmShared.iNull(columnValue).longValue() > 0) {
//                if (DmShared.xNull(tableName).equalsIgnoreCase("")) {
//                    throw new DmException(0, "Please define table name.");
//                }
//                sqlQuery = "select count(*) cnt from " + tableName + " where " + columnName + " = :regDocId and lmDocId is not null ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("regDocId", DmShared.iNull(columnValue).toString());
//                rsTemp = psTemp.executeQuery();
//                if (rsTemp.next()) {
//                    if (DmShared.iNull(rsTemp.getString("cnt")).longValue() > 0) {
//                        throw new DmException(0, "Record has been posted (via EOD). You can not change this record.");
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//    }
    public static void checkEOD(String tableName, String columnName, String columnValue, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;

        try {
            if (DmShared.iNull(columnValue).longValue() > 0) {
                tableName = DmShared.xNull(tableName).trim();
                columnName = DmShared.xNull(columnName).trim();
                if (tableName.equalsIgnoreCase("")) {
                    throw new DmException(0, "Please define table name.");
                }
                if (columnName.equalsIgnoreCase("")) {
                    throw new DmException(0, "Please define column name.");
                }

                sqlQuery = "select  t." + columnName + ",sb.recId "
                        + "         from " + tableName + " as t "
                        + "         left join saleBill as sb on t.sbDocId = sb.docId "
                        + "         where t." + columnName + " = :docId and isNull(sb.approvedBy,'') <> '' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(columnValue).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new DmException(0, "(" + DmShared.xNull(rsTemp.getString("recId")) + ") bill has been generated.You can't change this record.");
                }
                rsTemp.close();
                psTemp.close();
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

    public static List<SaleRateRule_Mdl> getSaleRateRule(Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        List<SaleRateRule_Mdl> lstTemp = new ArrayList<>();
        SaleRateRule_Mdl lstNode = null;
        try {
            sqlQuery = "SELECT "
                    + "   ISNULL(sr.marginFrom,0) AS marginFrom "
                    + " , ISNULL(sr.marginTo,0) AS marginTo "
                    + " , ISNULL(sr.profitToBeTaken,0) AS profitToBeTaken "
                    + " FROM medicineSaleRateRule sr ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new SaleRateRule_Mdl();
                lstNode.setMarginFrom(DmShared.xNull(rsTemp.getString("marginFrom")));
                lstNode.setMarginTo(DmShared.xNull(rsTemp.getString("marginTo")));
                lstNode.setProfitToBeTaken(DmShared.xNull(rsTemp.getString("profitToBeTaken")));
                lstTemp.add(lstNode);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return lstTemp;
    }

    public static DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, ITSType type, String site_Code, String location, String billDate, Connection cnTemp) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        List<OverHeadITS_Mdl> overHeadITSList = new ArrayList<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "", accountField = "", itsCodeField = "";

        try {

            if (DmShared.xNull(itsBasedOn).equals("1")) {
                condition += " AND (" + DmServer.vNull(rate) + " BETWEEN iid.rateFrom AND iid.rateUpto)";
            }

            switch (type) {
                case PURCHASE:
                    accountField = "purchaseAc";
                    itsCodeField = "its_Code_Purchase";
                    break;
                case PURCHASE_RETURN:
                    accountField = "purchaseReturnAc";
                    itsCodeField = "its_Code_PurchaseRet";
                    break;
                case SALES:
                    accountField = "salesAc";
                    itsCodeField = "its_Code_Sales";
                    break;
                case SALES_RETURN:
                    accountField = "salesReturnAc";
                    itsCodeField = "its_Code_SalesRet";
                    break;
                default:
                    break;
            }

            sqlQuery = "select iid." + accountField + " as acCode "
                    + " , iid." + itsCodeField + " as itsCode "
                    + " , sg.name as acName "
                    + " FROM itemMast im   "
                    + " LEFT JOIN itemItsDetail iid ON iid.code = im.Code "
                    + " LEFT JOIN subgroup sg on sg.subCode = iid." + accountField
                    + " WHERE im.code = :itemCode "
                    + condition;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                overHeadITSList.clear();
                overHeadITSList.addAll(OverHeadLine_Server.getITS(rsTemp.getString("itsCode"), location, site_Code, billDate, cnTemp));

                dmrTemp.setValue1(DmShared.xNull(rsTemp.getString("acCode")));
                dmrTemp.setValue2(DmShared.xNull(rsTemp.getString("acName")));
            }
            dmrTemp.setData(overHeadITSList);

            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

    public static void checkBillApproval(String regDocid, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            sqlQuery = " select count(regdocId) as cnt from  saleBill "
                    + " where docId <> regdocId and isNull(approvedBy,'')<>'' and regDocId=:regDocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocid).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Bill is approved.You can not change record.");
                }
            }
            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

    }

    public static void chkIsPatientDischarge(String regDocid, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            sqlQuery = " select count(regdocId) as cnt from  patientDischarge where regDocId=:regDocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocid).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Patient has been discharged.");
                }
            }
            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

    }

    public static void checkItemsRestrictedDepartment(String itemCodesCsv, String departmentCode, String departmentName, Connection cnTemp) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempInner = null;
        ResultSet rsTemp = null;
        ResultSet rsTempInner = null;
        try {
            sqlQuery = "SELECT COUNT(*) AS totalRestrictedItems"
                    + " FROM itemRestrictedToDepartment ird"
                    + " WHERE ird.departmentCode = :departmentCode"
                    + " AND ird.code IN (" + itemCodesCsv + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("departmentCode", DmShared.xNull(departmentCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("totalRestrictedItems")) > 0) {
                    String itemNames = "";
                    sqlQuery = "SELECT STUFF ("
                            + "     (SELECT ', ' + im.name AS [text()]"
                            + "     FROM itemMast im"
                            + "     INNER JOIN itemRestrictedToDepartment ird ON ird.code = im.code"
                            + "     WHERE ird.departmentCode = :departmentCode"
                            + "     AND ird.code IN (" + itemCodesCsv + ")"
                            + "     FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,1,''"
                            + " ) AS csv";
                    psTempInner = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTempInner.setString("departmentCode", DmShared.xNull(departmentCode));
                    rsTempInner = psTempInner.executeQuery();
                    if (rsTempInner.next()) {
                        itemNames = DmShared.xNull(rsTempInner.getString("csv"));
                    }
                    throw new UnsupportedOperationException("Items '" + itemNames + "' are restricted to department '" + departmentName + "'");
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTempInner != null) {
                    psTempInner.close();
                }
                if (psTempInner != null) {
                    psTempInner.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void checkForDuplicateIndent(String indentDetailTableName, String docId, Connection cnTemp) throws Exception {
        String sqlMainQuery = "", sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempInner = null;
        ResultSet rsTemp = null;
        ResultSet rsTempInner = null;
        try {
            sqlMainQuery = "SELECT COUNT(*) AS cnt, idt.indentDocId, idt.indentV_SNo"
                    + " FROM " + indentDetailTableName + " idt"
                    + " WHERE idt.docId = :docId"
                    + " GROUP BY idt.indentDocId, idt.indentV_SNo"
                    + " HAVING COUNT(*) > 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlMainQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                String itemNames = "";
                sqlQuery = "SELECT STUFF ("
                        + "     (SELECT DISTINCT ', ' + im.name AS [text()]"
                        + "     FROM indentDetail id "
                        + "     INNER JOIN (" + sqlMainQuery
                        + "     ) vIdt ON vIdt.indentDocId = id.docId AND vIdt.indentV_SNo = id.v_SNo"
                        + "     INNER JOIN itemMast im ON im.code = id.itemCode"
                        + "     FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,1,''"
                        + " ) AS csv";
                psTempInner = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempInner.setString("docId", DmShared.xNull(docId));
                rsTempInner = psTempInner.executeQuery();
                if (rsTempInner.next()) {
                    itemNames = DmShared.xNull(rsTempInner.getString("csv")).trim();
                }
                throw new UnsupportedOperationException("Duplicate indent found in item : '" + itemNames + "'");
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTempInner != null) {
                    psTempInner.close();
                }
                if (psTempInner != null) {
                    psTempInner.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void sendMail(String id, String voucherTypeCode, DmRSS servletInstance) throws Exception {
        sendMail(id, voucherTypeCode, "", servletInstance);
    }

    public static void sendMail(String id, String voucherTypeCode, String templateName, DmRSS servletInstance) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        String ShortName = "", templateCode = "";
        Connection cnTemp = null;
        DmAnonymousWebJobFactory webJobFactory = null;
        SiteEnviro_Mdl siteEnviro;
        List<Long> templateIds = new ArrayList<Long>();
        List<String> uniqueId = new ArrayList<String>();
        String[] templateId = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(servletInstance));
            uniqueId.add(DmShared.xNull(id));
            if (DmShared.iNull(voucherTypeCode).longValue() != 0l) {

                ShortName = DmServer.getCodeViaManualCode(DmShared.xNull(voucherTypeCode), "v_Type", "voucher_Type", "short_Name", cnTemp);

                sqlQuery = "SELECT attachWJTemplates FROM voucher_Type WHERE short_Name='" + ShortName + "' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    templateCode = DmShared.xNull(rsTemp.getString("attachWJTemplates"));
                }
                rsTemp.close();
                psTemp.close();
                cnTemp.close();
            } else {

                templateCode = DmServer.getCodeViaManualCode(DmShared.xNull(templateName), "name", "wjTemplateMast", "code", cnTemp);
            }
            if (!templateCode.equalsIgnoreCase("")) {

                webJobFactory = new DmAnonymousWebJobFactory();
                siteEnviro = webJobFactory.getEnviroSettings(servletInstance);

                if (DmShared.xNull(templateCode).contains("|")) {
                    templateId = templateCode.split("\\|");
                    for (int i = 0; i < templateId.length; i++) {
                        templateIds.add(DmShared.iNull(templateId[i]).longValue());
                    }
                } else {
                    templateIds.add(DmShared.iNull(templateCode).longValue());
                }
                webJobFactory.runJobsAsync(servletInstance, siteEnviro, templateIds, uniqueId, null);
            }
        } catch (Exception ex) {
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void chkIfDocumentUsed(String fieldName, String tableName, String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;

        try {

            sqlQuery = " Select count(tb." + fieldName.trim() + ") as cnt from " + tableName.trim() + " as tb  "
                    + " where tb." + fieldName.trim() + " = :docId  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                    throw new DmException(100, "Document has been used in " + tableName.trim() + ". You can't modify / delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

//    public static double getBalanceLedgerMast(String lmCode, String siteCode, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery;
//        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
//        double balance = 0;
//        try {
//            sqlQuery = "SELECT case when (isNull(sum(vPh.billAmt),0) - isNull(sum(vPh.recAmt),0)) < 0 then "
//                    + "			(isNull(sum(vPh.billAmt),0) - isNull(sum(vPh.recAmt),0)) else "
//                    + "			(isNull(sum(vPh.billAmt),0) - (isNull(sum(vPh.recAmt),0) + isNull(sum(vPh.plaAmt),0))) end as balanceAmt "
//                    + " FROM ("
//                    + "     SELECT SUM(CASE WHEN vt.category = :sbrCategory THEN -ISNULL(sb.oh_Amt_Net, 0) ELSE ISNULL(sb.oh_Amt_Net, 0) END) AS billAmt, 0 AS recAmt, 0 AS plaAmt"
//                    + "     FROM saleBill sb"
//                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type"
//                    + "     WHERE sb.isCancelled = 0"
//                    + "     AND vt.category IN (:sbCategory, :sbrCategory, :sbfdCategory)"
//                    + "     AND sb.lmCode = :lmCode "
//                    + "     AND sb.site_Code = :site_Code "
//                    + "     UNION ALL "
//                    + "     SELECT 0 AS billAmt, SUM(pr.amount) AS recAmt, 0 AS plaAmt"
//                    + "     FROM patientReceipt pr"
//                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type "
//                    + "     WHERE pr.isCancelled = 0"
//                    + "     AND vt.category IN (:sbReceiptCategory,:sbrReceiptCategory,:receiptCategory,:refundCategory) "
//                    + "     AND pr.lmCode = :lmCode "
//                    + "     AND pr.site_Code = :site_Code "
//                    + "     UNION ALL "
//                    + "     SELECT 0 AS billAmt, 0 AS recAmt, SUM(pla.amount) AS plaAmt"
//                    + "     FROM patientLimitApproval pla "
//                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = pla.v_Type"
//                    + "     LEFT JOIN ledgerMast lm ON lm.regDocId = pla.regDocId"
//                    + "     WHERE pla.isCancelled = 0"
//                    + "     AND pla.nature in (" + String.valueOf(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") "
//                    + "     AND vt.category in (:sbCategory, :sbrCategory, :sbfdCategory) "
//                    + "     AND lm.code = :lmCode "
//                    + "     AND pla.site_Code = :site_Code "
//                    + "     ) AS vPh ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("sbCategory", pvc.PHARMACY_BILL);
//            psTemp.setString("sbrCategory", pvc.PHARMACY_BILL_RETURN);
//            psTemp.setString("sbfdCategory", pvc.PHARMACY_BILL_FINAL_DISCOUNT);
//            psTemp.setString("sbReceiptCategory", pvc.PHARMACY_BILL_RECEIPT);
//            psTemp.setString("sbrReceiptCategory", pvc.PHARMACY_BILL_RETURN_RECEIPT);
//            psTemp.setString("receiptCategory", pvc.PHARMACY_RECEIPT);
//            psTemp.setString("refundCategory", pvc.PHARMACY_REFUND);
//            psTemp.setString("lmCode", DmShared.xNull(lmCode));
//            psTemp.setString("site_Code", DmShared.xNull(siteCode));
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                balance = DmShared.vNull(DmServer.vNull(rsTemp.getString("balanceAmt"), "0.00"));
//            }
//            psTemp.close();
//            rsTemp.close();
//
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception ex) {
//            }
//        }
//
//        return balance;
//    }
    public static double getBalanceLedgerMast(String lmCode, String siteCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
        double balance = 0;
        try {
            sqlQuery = "SELECT case when (isNull(sum(vPh.billAmt),0) - isNull(sum(vPh.recAmt),0)) < 0 then "
                    + "			(isNull(sum(vPh.billAmt),0) - isNull(sum(vPh.recAmt),0)) else "
                    + "			(isNull(sum(vPh.billAmt),0) - (isNull(sum(vPh.recAmt),0) + isNull(sum(vPh.plaAmt),0))) end as balanceAmt "
                    + " FROM ("
                    + "     SELECT SUM(CASE WHEN vt.category = :sbrCategory THEN -ISNULL(sb.oh_Amt_Net, 0) ELSE ISNULL(sb.oh_Amt_Net, 0) END) AS billAmt, 0 AS recAmt, 0 AS plaAmt"
                    + "     FROM saleBill sb"
                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type"
                    + "     WHERE sb.isCancelled = 0"
                    + "     AND vt.category IN (:sbCategory, :sbrCategory, :sbfdCategory)"
                    + "     AND sb.lmCode = :lmCode "
                    + "     AND sb.site_Code = :site_Code "
                    + "     UNION ALL "
                    + "     SELECT 0 AS billAmt, SUM(pr.amount) AS recAmt, 0 AS plaAmt"
                    + "     FROM patientReceipt pr"
                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type "
                    + "     WHERE pr.isCancelled = 0"
                    + "     AND vt.category IN (:sbReceiptCategory,:sbrReceiptCategory,:receiptCategory,:refundCategory) "
                    + "     AND pr.lmCode = :lmCode "
                    + "     AND pr.site_Code = :site_Code "
                    + "     UNION ALL "
                    + "     SELECT 0 AS billAmt, 0 AS recAmt, SUM(CASE WHEN vt.category = :sbrCategory THEN -ISNULL(sb.oh_Amt_Net, 0) ELSE ISNULL(sb.oh_Amt_Net, 0) END) AS plaAmt"
                    + "     FROM saleBill sb  "
                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type"
                    + "     WHERE sb.isCancelled = 0"
                    + "     AND sb.paymentSource in (" + String.valueOf(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") "
                    + "     AND vt.category in (:sbCategory, :sbrCategory, :sbfdCategory) "
                    + "     AND sb.lmCode = :lmCode "
                    + "     AND sb.site_Code = :site_Code "
                    + "     ) AS vPh ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("sbCategory", pvc.PHARMACY_BILL);
            psTemp.setString("sbrCategory", pvc.PHARMACY_BILL_RETURN);
            psTemp.setString("sbfdCategory", pvc.PHARMACY_BILL_FINAL_DISCOUNT);
            psTemp.setString("sbReceiptCategory", pvc.PHARMACY_BILL_RECEIPT);
            psTemp.setString("sbrReceiptCategory", pvc.PHARMACY_BILL_RETURN_RECEIPT);
            psTemp.setString("receiptCategory", pvc.PHARMACY_RECEIPT);
            psTemp.setString("refundCategory", pvc.PHARMACY_REFUND);
            psTemp.setString("lmCode", DmShared.xNull(lmCode));
            psTemp.setString("site_Code", DmShared.xNull(siteCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                balance = DmShared.vNull(DmServer.vNull(rsTemp.getString("balanceAmt"), "0.00"));
            }
            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }

        return balance;
    }

//    public static double getReceievedAmt(String lmCode, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery;
//        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
//        double receiveAmt = 0;
//        try {
//
//            sqlQuery = "SELECT SUM(pr.amount) AS receiveAmt "
//                    + " FROM patientReceipt pr "
//                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type "
//                    + " WHERE pr.isCancelled = 0 "
//                    + " AND vt.category IN (:sbReceiptCategory,:sbrReceiptCategory,:receiptCategory,:refundCategory) "
//                    + " AND pr.lmCode = :lmCode ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("sbReceiptCategory", pvc.PHARMACY_BILL_RECEIPT);
//            psTemp.setString("sbrReceiptCategory", pvc.PHARMACY_BILL_RETURN_RECEIPT);
//            psTemp.setString("receiptCategory", pvc.PHARMACY_RECEIPT);
//            psTemp.setString("refundCategory", pvc.PHARMACY_REFUND);
//            psTemp.setString("lmCode", DmShared.xNull(lmCode));
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                receiveAmt = DmShared.vNull(DmServer.vNull(rsTemp.getString("receiveAmt"), "0.00"));
//            }
//            psTemp.close();
//            rsTemp.close();
//
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception ex) {
//            }
//        }
//
//        return receiveAmt;
//    }
//    public static void isRegNoCTOBilled(String regDocId, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery;
//
//        try {
//            sqlQuery = "Select  count(*) as cnt "
//                    + "		from patientLimitApproval pla "
//                    + "		left join patientServiceDetail psd on psd.regDocId = pla.regDocId "
//                    + "		left join saleBill sb on sb.docId = psd.sbDocId "
//                    + "		where pla.nature = :nature and pla.docId = :regDocId and isNull(sb.approvedBy,'') <> '' ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
//            psTemp.setString("nature", DmShared.iNull(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()).toString());
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
//                    throw new DmException(0, "CTO Bill is approved for this registration. You can not add/ delete/ modify record.");
//                }
//            }
//            psTemp.close();
//            rsTemp.close();
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        }
//    }
    public static void isRegNoCTOBilled(String regDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            sqlQuery = "Select  pr.regDocId "
                    + "		from patientRegistration as pr "
                    + "		left join patientServiceDetail as psd on psd.regDocId = pr.regDocId "
                    + "		left join saleBill sb on sb.docId = psd.sbDocId "
                    + "		where pr.paymentSource = :paymentSource and pr.regDocId = :regDocId and isNull(sb.approvedBy,'') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            psTemp.setString("paymentSource", DmShared.iNull(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "CTO Bill is approved for this registration. You can not add/ delete/ modify record.");
            }
            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    public static void checkAssetCurrentDepartment(String docId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            sqlQuery = "select  aim.name as assetItem, asm.manualCode as assetName, sgd.name as departmentName, sm.manualCode as sitemName\n"
                    + "		from (\n"
                    + "             select  distinct assetCode             \n"
                    + "                     from assetStock\n"
                    + "                     where docid = :docId           \n"
                    + "		) as cr\n"
                    + "         left join (\n"
                    + "             select  ast.assetCode, ast.departmentCode, ast.site_Code  \n"
                    + "                     from assetStock ast\n"
                    + "                     where ast.docid <> :docId\n"
                    + "                     group by ast.assetCode, ast.departmentCode, ast.site_Code  \n"
                    + "                     having (isNull(sum(ast.recQty),0) - isNull(sum(ast.isuQty),0)) > 0  \n"
                    + "		) as ast on cr.assetCode = ast.assetCode\n"
                    + "         left join (\n"
                    + "             select  astI.assetCode, astI.departmentCode, astI.site_Code\n"
                    + "                     from assetStock as astI\n"
                    + "                     where astI.docId = :docId and isNull(astI.isuQty,0) > 0\n"
                    + "         ) as astI on cr.assetCode = astI.assetCode\n"
                    + "		left join assetMast as asm on asm.code = cr.assetCode\n"
                    + "		left join assetItemMast as aim on aim.code = asm.assetItemCode\n"
                    + "		left join subGroup as sgd on sgd.subCode = ast.departmentCode\n"
                    + "		left join siteMast sm on sm.code = ast.site_Code\n"
                    //                    + "		where (isNull(asm.currentDepartmentCode,0) <> isNull(ast.departmentCode,0) or isNull(asm.currentSite_code,0) <> isNull(ast.site_Code,0)) or"
                    + "         where (isNull(astI.departmentCode,0) <> isNull(ast.departmentCode,0) or isNull(astI.site_code,0) <> isNull(ast.site_Code,0))";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Asset cannot be issued. Asset current position is:"
                        + "<br>Asset Item: " + DmShared.xNull(rsTemp.getString("assetItem"))
                        + "<br>Asset Name: " + DmShared.xNull(rsTemp.getString("assetName"))
                        + "<br>Department: " + DmShared.xNull(rsTemp.getString("departmentName"))
                        + "<br>Site Name : " + DmShared.xNull(rsTemp.getString("sitemName"))
                );
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  aim.name as assetItem, asm.manualCode as assetName, sgd.name as departmentName, sm.manualCode as sitemName\n"
                    + "		from (\n"
                    + "             select  distinct astR.assetCode\n"
                    + "                     from assetStock as astR\n"
                    + "                     where astR.docId = :docId and isNull(astR.recQty,0) > 0\n"
                    + "         ) as cr\n"
                    + "         left join (\n"
                    + "             select  ast.assetCode, ast.departmentCode, ast.site_Code  \n"
                    + "                     from assetStock ast\n"
                    + "                     where (ast.docid <> :docId) or (docId = :docId and isNull(ast.recQty,0) = 0)\n"
                    + "                     group by ast.assetCode, ast.departmentCode, ast.site_Code  \n"
                    + "                     having (isNull(sum(ast.recQty),0) - isNull(sum(ast.isuQty),0)) > 0  \n"
                    + "		) as ast on cr.assetCode = ast.assetCode\n"
                    + "		left join assetMast as asm on asm.code = cr.assetCode\n"
                    + "		left join assetItemMast as aim on aim.code = asm.assetItemCode\n"
                    + "		left join subGroup as sgd on sgd.subCode = ast.departmentCode\n"
                    + "		left join siteMast sm on sm.code = ast.site_Code\n"
                    + "         where (isNull(ast.departmentCode,0) <> 0 or isNull(ast.site_code,0) <> 0) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Asset cannot be received. Asset current position is:"
                        + "<br>Asset Item: " + DmShared.xNull(rsTemp.getString("assetItem"))
                        + "<br>Asset Name: " + DmShared.xNull(rsTemp.getString("assetName"))
                        + "<br>Department: " + DmShared.xNull(rsTemp.getString("departmentName"))
                        + "<br>Site Name : " + DmShared.xNull(rsTemp.getString("sitemName"))
                );
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    public static void updateAssetCurrentDepartment(String docId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
//            sqlQuery = "update	assetMast set assetMast.currentDepartmentCode = t.departmentCode, assetMast.currentSite_code = t.site_Code "
//                    + "		from ( "
//                    + "			select  cr.assetCode, ast.departmentCode, ast.site_Code   "
//                    + "					from ( "
//                    + "						select distinct assetCode "
//                    + "								from assetStock "
//                    + "								where docid = :docId "
//                    + "					) as cr "
//                    + "					left join assetStock ast on cr.assetCode = ast.assetCode  "
//                    + "					group by cr.assetCode, ast.departmentCode, ast.site_Code   "
//                    + "					having (isNull(sum(ast.recQty),0) - isNull(sum(ast.isuQty),0)) > 0   "
//                    + "		) as t "
//                    + "		where assetMast.code = t.assetCode ";
            sqlQuery = "update	t set t.currentDepartmentCode = t.departmentCode, t.currentSite_code = t.site_Code,t.currentLocation=t.location\n"
                    + "		from (\n"
                    + "             select  asm.code as assetCode, asm.currentDepartmentCode, asm.currentSite_code,\n"
                    + "                     ast.departmentCode, ast.site_Code,ast.location,asm.currentLocation\n"
                    + "                     from assetMast asm\n"
                    + "                     left join (\n"
                    + "                             select  cr.assetCode, ast.departmentCode, ast.site_Code,ast.location\n"
                    + "                                     from (\n"
                    + "                                             select	distinct assetCode\n"
                    + "							from assetStock\n"
                    + "							where docid = :docId\n"
                    + "                                          ) as cr\n"
                    + "                                     left join assetStock ast on cr.assetCode = ast.assetCode\n"
                    + "                                     where ast.docid = :docId\n"
                    + "                                     group by cr.assetCode, ast.departmentCode, ast.site_Code, ast.location\n"
                    + "                                     having (isNull(sum(ast.recQty),0) - isNull(sum(ast.isuQty),0)) > 0\n"
                    + "                     ) as ast on asm.code = ast.assetCode\n"
                    + "                     where asm.code in (select	distinct assetCode\n"
                    + "                                              from assetStock\n"
                    + "                                              where docid = :docId) "
                    + "		) as t ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.execute();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception ex) {
            }
        }
    }

    public static void deleteAssetCurrentDepartment(String docId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            sqlQuery = "select	top 1 ast.docId  "
                    + "from assetStock ast "
                    + "order by u_entDt desc, v_date desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("docId")).longValue() != DmShared.iNull(docId).longValue()) {
                    throw new DmException(0, "Only last record can be deleted.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "update	t set t.currentDepartmentCode = t.departmentCode, t.currentSite_code = t.site_Code "
                    + "		from ( "
                    + "             select  asm.code as assetCode, asm.currentDepartmentCode, asm.currentSite_code, "
                    + "                     ast.departmentCode, ast.site_Code "
                    + "                     from assetMast asm "
                    + "                     left join ( "
                    + "                             select  cr.assetCode, ast.departmentCode, ast.site_Code "
                    + "                                     from ( "
                    + "                                         select	distinct assetCode "
                    + "							from assetStock "
                    + "							where docid = :docId "
                    + "                                     ) as cr "
                    + "                                     left join assetStock ast on cr.assetCode = ast.assetCode "
                    + "                                     where docId <> :docId "
                    + "                                     group by cr.assetCode, ast.departmentCode, ast.site_Code "
                    + "                                     having (isNull(sum(ast.recQty),0) - isNull(sum(ast.isuQty),0)) > 0 "
                    + "                     ) as ast on asm.code = ast.assetCode "
                    + "                     where asm.code in (select	distinct assetCode "
                    + "                                                 from assetStock "
                    + "							where docid = :docId) "
                    + "		) as t ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.execute();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }
    }

//    public static PackageDetail_Mdl getBestFitPackage(String packageCode, String organizationCode, String roomCategoryCode, String applicableDate, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        PackageDetail_Mdl mrTemp = new PackageDetail_Mdl();
//        String sqlQuery;
//
//        try {
//            sqlQuery = "Select	top 1 pd.code, pd.packageCode, pm.name as packageName, pm.shortName, pd.packageValue, "
//                    + "         pd.organizationCode, org.name as organizationName, pd.roomCategoryCode, rcm.name as roomCategoryName, "
//                    + "         case when isNull(pd.roomCategoryCode,'') <> '' then concat(pm.name,' - ', rcm.name,' - ', org.manualCode, ' (', pd.code, ')') "
//                    + "         else concat(pm.name,' - ', org.manualCode, ' (', pd.code, ')') end as name "
//                    + "		from packageDetail as pd "
//                    + "		left join packageMast as pm on pd.packageCode = pm.code "
//                    + "		left join subGroup as org on pd.organizationCode = org.subCode "
//                    + "		left join roomCatMast as rcm on pd.roomCategoryCode = rcm.code "
//                    + "		where (pd.isActive = 1) and (pd.packageCode = :packageCode) "
//                    + "		and (:voucherDate between pd.fromDate and pd.toDate) "
//                    + "		and (pd.organizationCode = :organizationCode or pd.organizationCode = (Select subCode from organizationMast where shortName = 'CP')) "
//                    + "		and (pd.roomCategoryCode = :roomCategoryCode or pd.roomCategoryCode is null) "
//                    + "		order by case when pd.organizationCode = :organizationCode then 0 else 1 end, "
//                    + "         case when pd.roomCategoryCode = :roomCategoryCode then 0 else 1 end ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("packageCode", DmShared.iNull(packageCode).toString());
//            psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
//            psTemp.setString("roomCategoryCode", DmShared.iNull(roomCategoryCode).toString());
//            psTemp.setDate("voucherDate", DmServer.convertDate(applicableDate));
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                mrTemp.setPackageDetailCode(DmShared.xNull(rsTemp.getString("code")));
//                mrTemp.setPackageDetailName(DmShared.xNull(rsTemp.getString("name")));
//                mrTemp.setPackageCode(DmShared.xNull(rsTemp.getString("packageCode")));
//                mrTemp.setPackageName(DmShared.xNull(rsTemp.getString("packageName")));
//                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
//                mrTemp.setOrganizationCode(DmShared.xNull(rsTemp.getString("organizationCode")));
//                mrTemp.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")));
//                mrTemp.setRoomCatCode(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
//                mrTemp.setRoomCatName(DmShared.xNull(rsTemp.getString("roomCategoryName")));
//                mrTemp.setAmount(DmServer.vNull(rsTemp.getString("packageValue"), "0.00"));
//            }
//            rsTemp.close();
//            psTemp.close();
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return mrTemp;
//    }
    public static PackageDetail_Mdl getBestFitPackage(String packageCode, String organizationCode, String roomCategoryCode, String applicableDate, String siteCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PackageDetail_Mdl mrTemp = new PackageDetail_Mdl();
        String sqlQuery;

        try {
            sqlQuery = "Select	top 1 pd.code, pd.packageCode, pm.name as packageName, pm.shortName, pd.packageValue, "
                    + "         pd.organizationCode, org.name as organizationName, pd.roomCategoryCode, rcm.name as roomCategoryName, "
                    + "         case when isNull(pd.roomCategoryCode,'') <> '' then concat(pm.name,' - ', rcm.name,' - ', org.manualCode, ' (', pd.code, ')') "
                    + "         else concat(pm.name,' - ', org.manualCode, ' (', pd.code, ')') end as name "
                    + "		from packageDetail as pd "
                    + "		left join packageMast as pm on pd.packageCode = pm.code "
                    + "		left join subGroup as org on pd.organizationCode = org.subCode "
                    + "		left join roomCatMast as rcm on pd.roomCategoryCode = rcm.code "
                    + "		where (pd.isActive = 1) and (pd.packageCode = :packageCode) and (pd.site_Code = :site_Code) "
                    + "		and (:voucherDate between pd.fromDate and pd.toDate) "
                    + "		and (pd.organizationCode = :organizationCode or pd.organizationCode = (Select subCode from organizationMast where shortName = 'CP')) "
                    + "		and (pd.roomCategoryCode = :roomCategoryCode or pd.roomCategoryCode is null) "
                    + "		order by case when pd.organizationCode = :organizationCode then 0 else 1 end, "
                    + "         case when pd.roomCategoryCode = :roomCategoryCode then 0 else 1 end ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("packageCode", DmShared.iNull(packageCode).toString());
            psTemp.setString("site_Code", DmShared.iNull(siteCode).toString());
            psTemp.setString("organizationCode", DmShared.iNull(organizationCode).toString());
            psTemp.setString("roomCategoryCode", DmShared.iNull(roomCategoryCode).toString());
            psTemp.setDate("voucherDate", DmServer.convertDate(applicableDate));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setPackageDetailCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setPackageDetailName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setPackageCode(DmShared.xNull(rsTemp.getString("packageCode")));
                mrTemp.setPackageName(DmShared.xNull(rsTemp.getString("packageName")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setOrganizationCode(DmShared.xNull(rsTemp.getString("organizationCode")));
                mrTemp.setOrganizationName(DmShared.xNull(rsTemp.getString("organizationName")));
                mrTemp.setRoomCatCode(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
                mrTemp.setRoomCatName(DmShared.xNull(rsTemp.getString("roomCategoryName")));
                mrTemp.setAmount(DmServer.vNull(rsTemp.getString("packageValue"), "0.00"));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

    public static String getCsvFieldValue(String tableName, String columnName, String condition, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, returnValue = "";

        try {

            sqlQuery = "SELECT csv = STUFF((SELECT ',' + CAST(t.[" + columnName + "] AS nvarchar) "
                    + "    FROM [" + tableName + "] t "
                    + "    WHERE 1=1 " + condition
                    + "    FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,1,'') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                returnValue = DmShared.xNull(rsTemp.getString("csv"));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return returnValue;
    }

    public static double getCardRegistrationBalance(String cardRegDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double balance = 0;
        try {
            sqlQuery = "select  (isnull(sum(cl.amtDr), 0) - isnull(sum(cl.amtCr), 0)) as balance "
                    + "		from cardLedger cl "
                    + "		where cl.cardRegDocId = :cardRegDocId"
                    + "		group by cardRegDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("cardRegDocid", DmShared.xNull(cardRegDocId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                balance = DmShared.vNull(DmServer.vNull(rsTemp.getString("balance"), "0.00"));
            }
            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }
        return balance;
    }

    public static RegisteredWallet_Mdl getRegisteredWallet(String cardNo, String physicalCode, String uhid, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        RegisteredWallet_Mdl mrTemp = new RegisteredWallet_Mdl();
        try {
            if (DmShared.iNull(uhid).longValue() > 0) {
                condition = " and cr.uhid = :uhid";
                cardNo = DmServer.getCodeViaManualCode(uhid, "uhid", "cardRegistration", "cardNo", cnTemp);
            } else {
                condition = " and cs.cardNo = :cardNo";
            }
            sqlQuery = "select  cs.cardRegDocId, cr.recId as cardRegNo, cr.v_Date as allotedDate, cr.expiryDate, cr.personName, cr.mobile,"
                    + "		cr.eMail, cr.cardNo, cr.cardTypeCode, ctm.name as cardTypeName, ctm.isSmsAlert, ctm.isEmailAlert, cr.subCode, "
                    + "		sg.name as linkAccount, cl.balance, cr.physicalId, cr.uhid "
                    + "		from cardStock cs "
                    + "		left join cardRegistration cr on cr.docId = cs.cardRegDocId "
                    + "		left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
                    + "		left join subGroup sg on sg.subCode = cr.subCode "
                    + "		left join ("
                    + "			select  cl.cardRegDocId, (sum(cl.amtDr) - sum(cl.amtCr)) as balance"
                    + "				from cardLedger cl "
                    + "				where cl.isCancelled = 0 and cl.cardNo = :cardNo "
                    + "				group by cl.cardRegDocId"
                    + "		) cl on cl.cardRegDocId = cr.docId "
                    + "		where cs.cardRegDocId is not null " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(uhid).longValue() > 0) {
                psTemp.setString("uhid", uhid);
            }
            psTemp.setString("cardNo", DmShared.xNull(cardNo));

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setAllotmentDate(DmShared.dNull(rsTemp.getDate("allotedDate")));
                mrTemp.setBalance(DmShared.vNull(DmServer.vNull(rsTemp.getString("balance"))));
                mrTemp.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                mrTemp.setCardTypeCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                mrTemp.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                mrTemp.setContactPerson(DmShared.xNull(rsTemp.getString("personName")));
                mrTemp.setDocIdUHID(DmShared.xNull(rsTemp.getString("uhid")));
                mrTemp.setEmail(DmShared.xNull(rsTemp.getString("email")));
                mrTemp.setEmailAlert(DmShared.iNull(rsTemp.getString("isEmailAlert")).intValue() == 1);
                mrTemp.setLinkAccountCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setLinkAccountName(DmShared.xNull(rsTemp.getString("linkAccount")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardRegDocId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardRegNo")));
                mrTemp.setSmsAlert(DmShared.iNull(rsTemp.getString("isSmsAlert")).intValue() == 1);
//                mrTemp.setPhysicalId(DmShared.xNull(rsTemp.getString("physicalId")));
                if (!DmShared.xNull(rsTemp.getString("physicalId")).trim().equalsIgnoreCase("")) {
                    if (!DmShared.xNull(physicalCode).equals(DmShared.xNull(rsTemp.getString("physicalId")))) {
                        throw new DmException(0, "Wallet/Card No.: " + DmShared.xNull(mrTemp.getCardNo()) + " is invalid.");
                    }
                }
//                else {
//                    mrTemp.setPhysicalId(mrTemp.getCardNo());
//                }
                mrTemp.setValidDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
            }
            psTemp.close();
            rsTemp.close();

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new DmException(0, "Wallet/Card No.: " + cardNo + " is invalid.");
            }

            if (!DmShared.dNull(mrTemp.getValidDate()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(mrTemp.getValidDate()).getTime() <= DmShared.convertDate(DmServer.getServerDateTime()[0]).getTime()) {
                    throw new DmException(0, "Wallet/Card No.: " + DmShared.xNull(mrTemp.getCardNo()) + " is expired on " + DmShared.dNull(mrTemp.getValidDate()));
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }
        return mrTemp;
    }

    public static void checkCardBalance(List<GrdCardPayment_Mdl> lstCardPayment, String docId, boolean isDebit, Connection cnTemp) {
        String sqlQuery = "", sqlUnion = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String lstCardRegDocId = "", errorMessage = "";

        try {
            for (GrdCardPayment_Mdl grdMdl : lstCardPayment) {
                if (DmEnum.CardType.getValueOf(DmShared.iNull(grdMdl.getCardTypeNatureCode()).intValue()) == DmEnum.CardType.WALLET) {
                    if (!lstCardRegDocId.trim().equalsIgnoreCase("")) {
                        lstCardRegDocId += ",";
                    }
                    lstCardRegDocId += DmShared.iNull(grdMdl.getCardAuthorizationDocId()).toString();
                    sqlUnion += "union all "
                            + "select " + DmShared.iNull(grdMdl.getCardAuthorizationDocId()).toString() + " as cardRegDocId, 0 as balance, ";
                    if (isDebit) {
                        sqlUnion += "" + DmServer.vNull(grdMdl.getAmount(), "0.00") + " - 0.00 as amount, 1 as isThere ";
                    } else {
                        sqlUnion += "0.00 - " + DmServer.vNull(grdMdl.getAmount(), "0.00") + " as amount, 1 as isThere ";
                    }
                }
            }

            /*==============================<< Get detail >>===============================*/
            if (!lstCardRegDocId.trim().equalsIgnoreCase("")) {
                sqlQuery = "select  max(ctm.name) as cardTypeName, max(cr.cardNo) as cardNo, isNull(sum(balance),0) as balance, "
                        + "         max(cr.expiryDate) as expiryDate, "
                        + "         case when isNull(sum(balance),0) > 0 then concat(format(abs(sum(balance)), '0.00'), ' Dr') "
                        + "             else concat(format(abs(sum(balance)), '0.00'), ' Cr') end as dispalyBalance "
                        + "         from ( "
                        + "             select  cl.cardRegDocId, isNull(sum(cl.amtDr),0) - isNull(sum(cl.amtCr),0) as balance, 0 as amount, 0 as isThere "
                        + "                     from cardLedger as cl "
                        + "                     where cl.CardRegDocId in (" + DmServer.getParamList(":cardRegDocId", lstCardRegDocId) + ") and "
                        + "                         cl.docId <> :docid and cl.isCancelled = 0 "
                        + "                     group by cl.cardRegDocId ";
                sqlQuery += sqlUnion;
                sqlQuery += "       ) as temp "
                        + "         left join cardRegistration cr on temp.cardRegDocId = cr.docId "
                        + "         left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
                        + "         group by temp.cardRegDocId "
                        + "         having (isNull(sum(balance),0) + isNull(sum(amount),0)) > 0 and isNull(max(isThere),0) = 1 ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setStringList("cardRegDocId", lstCardRegDocId);
                psTemp.setString("docid", DmShared.iNull(docId).toString());
                rsTemp = psTemp.executeQuery();
                errorMessage = "";
                while (rsTemp.next()) {
                    if (!errorMessage.equalsIgnoreCase("")) {
                        errorMessage += ", ";
                    }
                    errorMessage += DmShared.xNull(rsTemp.getString("cardTypeName")) + ": " + DmShared.xNull(rsTemp.getString("cardNo"))
                            + " <b>[" + DmShared.xNull(rsTemp.getString("dispalyBalance")) + "]</b>"
                            + " <b>(Expiry Date - " + DmShared.dNull(rsTemp.getDate("expiryDate")) + ")</b>";
                }
                rsTemp.close();
                psTemp.close();
                if (!errorMessage.equalsIgnoreCase("")) {
                    throw new DmException(0, "Insuficiant Balance: <br/>" + errorMessage);
                }

//                sqlQuery = "select  cr.expiryDate, cr.cardNo, ctm.name as cardTypeName "
//                        + "         from cardRegistration cr "
//                        + "         left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
//                        + "         where cr.docId in (" + DmServer.getParamList(":cardRegDocId", lstCardRegDocId) + ") "
//                        + "             and isnull(cr.expiryDate, :vrDate) < :vrDate "
//                        + "         order by cr.expiryDate ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setStringList("cardRegDocId", lstCardRegDocId);
//                psTemp.setDate("vrDate", DmServer.convertDate(vrDate));
//                rsTemp = psTemp.executeQuery();
//                errorMessage = "";
//                while (rsTemp.next()) {
//                    if (!errorMessage.equalsIgnoreCase("")) {
//                        errorMessage += ", ";
//                    }
//                    errorMessage += DmShared.xNull(rsTemp.getString("cardTypeName")) + ": " + DmShared.xNull(rsTemp.getString("cardNo"))
//                            + " <b>[Date - " + DmShared.dNull(rsTemp.getDate("expiryDate")) + "]</b>";
//                }
//                rsTemp.close();
//                psTemp.close();
//
//                sqlQuery = "select  cr.cardNo "
//                        + "         from cardRegistration cr "
//                        + "         left join cardStock cs on cs.cardRegDocId = cr.docId "
//                        + "         where cr.docId in (" + DmServer.getParamList(":cardRegDocId", lstCardRegDocId) + ") "
//                        + "             and cs.cardRegDocId is null "
//                        + "         order by cs.cardNo ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setStringList("cardRegDocId", lstCardRegDocId);
//                rsTemp = psTemp.executeQuery();
//                errorMessage = "";
//                while (rsTemp.next()) {
//                    if (!errorMessage.equalsIgnoreCase("")) {
//                        errorMessage += ", ";
//                    }
//                    errorMessage += "<b>" + DmShared.xNull(rsTemp.getString("cardNo")) + "</b> has deactive";
//                }
//                rsTemp.close();
//                psTemp.close();
//                if (!errorMessage.equalsIgnoreCase("")) {
//                    throw new DmException(0, "Wallet/Card No. Expiry: <br/>" + errorMessage);
//                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

//    public static HashMap<String, String> getPatientDocIdsWithRuleCode(String uhid, Connection cnTemp) throws Exception {
//        HashMap<String, String> hashMap = new HashMap<>();
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery, docId = "";
//
//        try {
//            sqlQuery = "Select	pr.regDocId as docId,  "
//                    + "		case when vt.category in ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "') then 'OPD' when vt.category in ('" + pvc.IPD + "') then 'IPD' end as ruleCode "
//                    + "		from patientRegistration as pr "
//                    + "		left join voucher_Type as vt on pr.v_Type = vt.v_Type "
//                    + "		where pr.uhid = :uhid and vt.category in ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "','" + pvc.IPD + "') "
//                    + "union all "
//                    + "Select	pu.docId, 'UHID' as ruleCode "
//                    + "		from patientUHID as pu "
//                    + "		where pu.docId = :uhid "
//                    + "union all "
//                    + "Select	pd.docId, "
//                    + "		case when vt.category in ('" + pvc.DISCHARGE_CARD_IPD + "') then 'IPDDC' end as ruleCode "
//                    + "		from patientPrescription as pd "
//                    + "		left join voucher_Type as vt on pd.v_Type = vt.v_Type "
//                    + "		where pd.uhid = :uhid and vt.category in ('" + pvc.DISCHARGE_CARD_IPD + "') "
//                    + "union all "
//                    + "Select	fr.docId, "
//                    + "		case when vt.category in ('" + pvc.FILE_REGISTRATION + "') then 'FLREG' end as ruleCode "
//                    + "		from fileRegistration as fr "
//                    + "		left join voucher_Type as vt on fr.v_Type = vt.v_Type "
//                    + "		left join patientRegistration as pr on fr.regDocId = pr.regDocId "
//                    + "		where pr.uhid = :uhid and vt.category in ('" + pvc.FILE_REGISTRATION + "') ";
////                    + "union all "
////                    + "Select	ppr.docId, "
////                    + "		case when vt.category in ('" + pvc.OT_PATIENT_PRESCRIPTION + "','" + pvc.OT_PRESCRIPTION_REFERENCE + "') then 'WHIN' end as ruleCode "
////                    + "		from patientPrescriptionRef as ppr "
////                    + "		left join voucher_Type as vt on ppr.v_Type = vt.v_Type "
////                    + "		left join patientRegistration as pr on ppr.regDocId = pr.regDocId "
////                    + "		where pr.uhid = :uhid and vt.category in ('" + pvc.OT_PATIENT_PRESCRIPTION + "','" + pvc.OT_PRESCRIPTION_REFERENCE + "') ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("uhid", DmShared.iNull(uhid).toString());
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                if (hashMap.containsKey(DmShared.xNull(rsTemp.getString("ruleCode")))) {
//                    docId = hashMap.get(DmShared.xNull(rsTemp.getString("ruleCode"))) + "," + "'" + DmShared.xNull(rsTemp.getString("docId")) + "'";
//                    hashMap.replace(DmShared.xNull(rsTemp.getString("ruleCode")), docId);
//                } else {
//                    hashMap.put(DmShared.xNull(rsTemp.getString("ruleCode")), "'" + DmShared.xNull(rsTemp.getString("docId")) + "'");
//                }
//            }
//            psTemp.close();
//            rsTemp.close();
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception ex) {
//            }
//        }
//
//        return hashMap;
//    }
    public static HashMap<String, String> getPatientDocIdsWithRuleCode(String uhid, Connection cnTemp) throws Exception {
        HashMap<String, String> hashMap = new HashMap<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, docId;

        try {
            sqlQuery = "Select	pr.regDocId as docId,  "
                    + "		case when vt.category in ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "') then '" + ProjectRuleCode.CONSULTATION.getRuleCode() + "' "
                    + "         when vt.category in ('" + pvc.EMERGENCY + "') then '" + ProjectRuleCode.EMERGENCY.getRuleCode() + "' "
                    + "         when vt.category in ('" + pvc.DAY_CARE + "') then '" + ProjectRuleCode.DAY_CARE.getRuleCode() + "' end as ruleCode "
                    + "		from patientRegistration as pr "
                    + "		left join voucher_Type as vt on pr.v_Type = vt.v_Type "
                    + "		where pr.uhid = :uhid and vt.category in ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "','" + pvc.IPD + "','" + pvc.PAY_CLINIC + "'"
                    + "         ,'" + pvc.EMERGENCY + "','" + pvc.DAY_CARE + "') "
                    + "union all "
                    + "Select	pu.docId, '" + ProjectRuleCode.UHID.getRuleCode() + "' as ruleCode "
                    + "		from patientUHID as pu "
                    + "		where pu.docId = :uhid "
                    + "union all "
                    + "Select	pp.docId, "
                    + "		case when vt.category in ('" + pvc.IPD_INITIAL_ASSESSMENT + "','" + pvc.DISCHARGE_CARD_IPD + "') then '" + ProjectRuleCode.IPD.getRuleCode() + "' "
                    + "         when vt.category in ('" + pvc.EMERGENCY_DISCHARGE_CARD + "') then '" + ProjectRuleCode.EMERGENCY.getRuleCode() + "' "
                    + "         when vt.category in ('" + pvc.DAY_CARE_SUMMARY + "') then '" + ProjectRuleCode.DAY_CARE.getRuleCode() + "' "
                    + "         when vt.category in ('" + pvc.OPD_PRESCRIPTION + "') then '" + ProjectRuleCode.CONSULTATION.getRuleCode() + "' "
                    + "         end as ruleCode "
                    + "		from patientPrescription as pp "
                    + "		left join voucher_Type as vt on pp.v_Type = vt.v_Type "
                    + "		where pp.uhid = :uhid and vt.category in ('" + pvc.DISCHARGE_CARD_IPD + "','" + pvc.OPD_PRESCRIPTION + "'"
                    + "         ,'" + pvc.IPD_INITIAL_ASSESSMENT + "','" + pvc.EMERGENCY_DISCHARGE_CARD + "','" + pvc.DAY_CARE_SUMMARY + "') "
                    + "union all "
                    + "Select	fr.docId, "
                    + "		case when vt.category in ('" + pvc.FILE_REGISTRATION + "') then '" + ProjectRuleCode.MRD.getRuleCode() + "' end as ruleCode "
                    + "		from fileRegistration as fr "
                    + "		left join voucher_Type as vt on fr.v_Type = vt.v_Type "
                    + "		left join patientRegistration as pr on fr.regDocId = pr.regDocId "
                    + "		where pr.uhid = :uhid and vt.category in ('" + pvc.FILE_REGISTRATION + "') "
                    + "union all "
                    + "Select	ppr.docId, "
                    + "		case when vt.category in ('" + pvc.OT_SURGICAL_REGISTRATION + "','" + pvc.PRE_OPERATION + "','" + pvc.PRE_ANAESTHESIA + "'"
                    + "         ,'" + pvc.ANAESTHESIA_NOTES + "') then '" + ProjectRuleCode.OT_SURGICAL_REGISTRATION.getRuleCode() + "' end as ruleCode "
                    + "		from patientPrescriptionRef as ppr "
                    + "		left join voucher_Type as vt on ppr.v_Type = vt.v_Type "
                    + "		left join patientRegistration as pr on ppr.regDocId = pr.regDocId "
                    + "		where pr.uhid = :uhid and vt.category in ('" + pvc.OT_SURGICAL_REGISTRATION + "','" + pvc.PRE_OPERATION + "'"
                    + "         ,'" + pvc.PRE_ANAESTHESIA + "','" + pvc.ANAESTHESIA_NOTES + "') "
                    + " union all "
                    + "         select ps.docId,     "
                    + "		case when vt.category in ('" + pvc.MANAGE_PATIENT_SCHEDULE + "') "
                    + "                 then '" + ProjectRuleCode.PATIENT_SCHEDULE_FOLLOW.getRuleCode() + "' end as ruleCode "
                    + "         From PatientSchedule ps "
                    + "         left join voucher_Type vt on ps.v_Type=vt.v_Type    "
                    + "         where ps.uhid=uhid  and ps.isStatus<>1   ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("uhid", DmShared.iNull(uhid).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (hashMap.containsKey(DmShared.xNull(rsTemp.getString("ruleCode")))) {
                    docId = hashMap.get(DmShared.xNull(rsTemp.getString("ruleCode"))) + "," + "'" + DmShared.xNull(rsTemp.getString("docId")) + "'";
                    hashMap.replace(DmShared.xNull(rsTemp.getString("ruleCode")), docId);
                } else {
                    hashMap.put(DmShared.xNull(rsTemp.getString("ruleCode")), "'" + DmShared.xNull(rsTemp.getString("docId")) + "'");
                }
            }
            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }

        return hashMap;
    }

    public static String getSkuPurchaseRate(String itemCode, String ibCode, String departmentCode, String siteCode, String v_Date, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, returnValue = "", avgLandedskuRate = "";

        try {

            String totalTaxPerc = DmServer.executeScalar("select top 1  (isnull(pbd.oh_at_CGST,0)+isnull(pbd.oh_at_SGST,0)+isnull(pbd.oh_at_IGST,0)) as totalTaxPercent \n"
                    + "from pBillDetail as pbd\n"
                    + "left join pBill as pb on pb.docId=pbd.docId\n"
                    + "left join voucher_Type vt on vt.v_Type=pb.v_Type  \n"
                    + "where vt.parentVoucher in ('Purchase') and pbd.isFooter=0  "
                    + " and pbd.itemCode=" + itemCode + " and pbd.ibCode=" + ibCode + "", cnTemp);

            sqlQuery = " SELECT TOP 1 iStk.avgLandedSKURate "
                    + " FROM itemStock iStk "
                    + " WHERE iStk.itemCode = :itemCode "
                    + " AND iStk.departmentCode =:departmentCode "
                    + " AND iStk.site_Code = :siteCode "
                    + " AND iStk.stockValueDate <= :v_Date ";
            if (DmShared.iNull(ibCode).longValue() > 0) {
                sqlQuery += " AND iStk.ibCode = :ibCode ";
            }
            sqlQuery += " ORDER BY iStk.stockValueDate DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            if (DmShared.iNull(ibCode).longValue() > 0) {
                psTemp.setString("ibCode", DmShared.iNull(ibCode).toString());
            }
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(v_Date));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                avgLandedskuRate = DmShared.xNull(rsTemp.getString("avgLandedSKURate"));
            }
            rsTemp.close();
            psTemp.close();

            returnValue = DmServer.vNull(DmShared.vNull(avgLandedskuRate) * 100 / (100 + DmShared.vNull(totalTaxPerc)), "0.00");

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return returnValue;
    }

    public static String getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String siteCode, String v_Date, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, returnValue = "";

        try {

            sqlQuery = " SELECT TOP 1 iStk.staffSkuRate "
                    + " FROM itemStock iStk "
                    + " WHERE iStk.itemCode = :itemCode "
                    + " AND iStk.departmentCode =:departmentCode "
                    + " AND iStk.site_Code = :siteCode "
                    + " AND iStk.stockValueDate <= :v_Date ";
            if (DmShared.iNull(ibCode).longValue() > 0) {
                sqlQuery += " AND iStk.ibCode = :ibCode ";
            }
            sqlQuery += " ORDER BY iStk.stockValueDate DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            if (DmShared.iNull(ibCode).longValue() > 0) {
                psTemp.setString("ibCode", DmShared.iNull(ibCode).toString());
            }
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(v_Date));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                returnValue = DmShared.xNull(rsTemp.getString("staffSkuRate"));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return returnValue;
    }

    public static String getSkuStaffRate(String itemCode, String ibCode, String departmentCode, String siteCode, String v_Date, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, returnValue = "";

        try {

            sqlQuery = " SELECT TOP 1 iStk.staffSkuRate "
                    + " FROM itemStock iStk "
                    + " WHERE iStk.itemCode = :itemCode "
                    //                    + " AND iStk.departmentCode =:departmentCode "
                    + " AND iStk.site_Code = :siteCode "
                    + " AND iStk.staffSkuRate <> 0 ";
            if (DmShared.iNull(ibCode).longValue() > 0) {
                sqlQuery += " AND iStk.ibCode = :ibCode ";
            }
            sqlQuery += " ORDER BY iStk.stockValueDate DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            if (DmShared.iNull(ibCode).longValue() > 0) {
                psTemp.setString("ibCode", DmShared.iNull(ibCode).toString());
            }
//            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            psTemp.setString("siteCode", DmShared.iNull(siteCode).toString());

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                returnValue = DmShared.xNull(rsTemp.getString("staffSkuRate"));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return returnValue;
    }

    public static void checkForwordVoucherDateTime(String voucherDate, String voucherTime) {
        String[] dateTime;

        try {
            if (DmShared.dNull(voucherDate).trim().equals("")) {
                throw new Exception("Please Input the Date");
            }

            dateTime = DmServer.getServerDateTime();

            if (DmServer.convertDate(dateTime[0]).getTime() < DmServer.convertDate(voucherDate).getTime()) {
                throw new Exception("Forward date entry not allowed.");
            }

            if (!DmShared.xNull(voucherTime).equalsIgnoreCase("") && DmServer.convertDate(dateTime[0]).getTime() == DmServer.convertDate(DmShared.dNull(voucherDate)).getTime()) {
                if (DmShared.vNull(dateTime[1]).doubleValue() < DmShared.vNull(voucherTime).doubleValue()) {
                    throw new Exception("Forward time entry not allowed.");
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {

            } catch (Exception ex) {
            }
        }
    }

    public static DmXlsSheet setXLSFileHeader(String fileName, DmResultSetToExcelWriter xlsRsTemp, String widgetName, PageOrientation pageOrientation, PaperSize paperSize, HashMap<String, FieldUDT_Mdl> filter, DmRSSPrivate rssTemp) {
        DmXlsSheet xlsSheet = null;
        Label lbl;
        WritableFont cellFontBold;
        WritableCellFormat cellFormat;
        int dividedColumn = 0;
        String tempString = "";
        try {
            xlsSheet = xlsRsTemp.createWorkBook(fileName);
            xlsSheet.getWorkSheet().setPageSetup(pageOrientation, paperSize, 0.25, 0.278);

            cellFontBold = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);

            dividedColumn = xlsRsTemp.getResultSetColumnCount() / 2;

            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(0, 0, dividedColumn, 0);
            lbl = new Label(0, 0, DmServer.getSsnCompanyName(rssTemp), cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);

            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.RIGHT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(dividedColumn + 1, 0, xlsRsTemp.getResultSetColumnCount() - 1, 0);
            lbl = new Label(dividedColumn + 1, 0, widgetName, cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);

            cellFontBold = new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD);
            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(0, 1, dividedColumn, 1);
            lbl = new Label(0, 1, DmServer.getSsnSiteDisplayName(rssTemp), cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);

            cellFormat = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 8, WritableFont.NO_BOLD));
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(0, 2, xlsRsTemp.getResultSetColumnCount() - 1, 2);
            if (filter != null) {
                Set<String> keySet = filter.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        FieldUDT_Mdl filter_Mdl = filter.get(key);
                        if (filter_Mdl != null) {
                            if (!DmShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
                                tempString += DmShared.xNull(filter_Mdl.getFieldCaption()) + ":" + DmShared.xNull(filter_Mdl.getText()) + "; ";
                            }
                        }
                    }
                }
            }
            lbl = new Label(0, 2, tempString, cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);
        } catch (Exception e) {
        }
        return xlsSheet;
    }

    public static void setXLSFileFooter(DmXlsSheet workSheet, DmRSSPrivate rssTemp) {
        Label lbl;
        WritableFont cellFontBold;
        WritableCellFormat cellFormat;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        int lastRowIndex = 0;
        try {
            lastRowIndex = workSheet.getWorkSheet().getRows();
            cellFontBold = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD);
            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.THIN);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            workSheet.getWorkSheet().mergeCells(0, lastRowIndex, workSheet.getWorkSheet().getColumns() - 1, lastRowIndex);
            lbl = new Label(0, lastRowIndex, "**End Of Reprt**",
                    cellFormat);
            workSheet.getWorkSheet().addCell(lbl);

            lastRowIndex = workSheet.getWorkSheet().getRows();
            cellFontBold = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD);
            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.THIN);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            workSheet.getWorkSheet().mergeCells(0, lastRowIndex, workSheet.getWorkSheet().getColumns() - 1, lastRowIndex);
            lbl = new Label(0, lastRowIndex, "Printed By " + DmServer.getSsnUserName(rssTemp) + " on " + sdf.format(new java.sql.Date(DmServer.getServerTimestamp().getTime())), cellFormat);
            workSheet.getWorkSheet().addCell(lbl);
        } catch (Exception e) {
        }
    }

    public static int getRateDecimalPlaces() throws Exception {
        int returnValue = 2;
        try {
            if (DmServer.baseDmLocaleCountry == DmEnum.DmLocaleCountry.CAMBODIA_USA) {
                returnValue = 4;
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return returnValue;
    }

    public static boolean getIsPatientAllergic(String ptntPrscrptnDocId, String compostionCode, Connection cnTemp) {
        boolean isAllergic = false;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {

            sqlQuery = " select docId from patientAllergicToMedicine patm where docId=:ptntPrscrptnDocId and medicineCompoCode=:mcc  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("ptntPrscrptnDocId", DmShared.iNull(ptntPrscrptnDocId).toString());
            psTemp.setString("mcc", DmShared.iNull(compostionCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isAllergic = true;
                break;
            }

        } catch (Exception ex) {

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
        return isAllergic;

    }

    public static void createConsultantAppoinmentToken(String consultantCode, String vrDate, TokenBlockNature blockNature,
            boolean isLeave, DBCollection dBCollection, Connection cnTemp, DmRSSPrivate rssTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, applicableFrom = "";
        boolean isHoliday = false;
        int tokenNo = 0, block = 0;
        TreeMap<String, Object> map = null;
        List<ShiftTime_Mdl> lstTime = new ArrayList<>();
        BasicDBObject document = null;
        DiaryStatus appointmentStatus = DiaryStatus.Waiting;
        try {
            sqlQuery = "select  date "
                    + "         from holidayMast "
                    + "         where date = :vrDate";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("vrDate", DmServer.convertDate(vrDate));
            rsTemp = psTemp.executeQuery();
            isHoliday = rsTemp.next();
            rsTemp.close();
            psTemp.close();
            if (!isLeave || !isHoliday) {
                sqlQuery = "select top 1  max(ctd.applicableFrom) as applicableFrom, ctd.consultantCode "
                        + "		from consultantTimeDetail ctd "
                        + "		where ctd.applicableFrom <= :applicableDate and ctd.site_Code = :siteCode and consultantCode = :consultant "
                        + "		group by ctd.consultantCode "
                        + "		order by max(ctd.applicableFrom) desc ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(rssTemp));
                psTemp.setString("consultant", consultantCode);
                psTemp.setDate("applicableDate", DmServer.convertDate(vrDate));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    applicableFrom = DmShared.dNull(rsTemp.getDate("applicableFrom"));
                } else {
                    applicableFrom = DmShared.dNull(new Date(DmServer.getServerTimestamp().getTime()));
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "select	ctm.fromTime, ctm.toTime, ctm.graceMinutes, ctm.dayNameCode "
                        + "		from consultantTimeDetail ctm "
                        + "		where (ctm.consultantcode = :consultant) and ctm.applicableFrom = :applicableDate "
                        + "             and ctm.site_Code = :siteCode and ctm.dayNameCode in (0, datepart(dw,:vrDate)) "
                        + "		order by ctm.fromTime";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(rssTemp));
                psTemp.setString("consultant", consultantCode);
                psTemp.setDate("applicableDate", DmServer.convertDate(applicableFrom));
                psTemp.setDate("vrDate", DmServer.convertDate(vrDate));
                rsTemp = psTemp.executeQuery();
                lstTime = new ArrayList<>();
                while (rsTemp.next()) {
                    lstTime.addAll(getTimeList(DmServer.vNull(rsTemp.getString("fromTime"), "00.00"), DmServer.vNull(rsTemp.getString("toTime"), "00.00"), DmShared.iNull(rsTemp.getString("graceMinutes")).intValue()));
                }
                rsTemp.close();
                psTemp.close();
                tokenNo = 0;
                if (lstTime.size() > 0) {
                    for (ShiftTime_Mdl shift : lstTime) {
                        tokenNo++;
                        appointmentStatus = DiaryStatus.Waiting;
                        block++;
                        if (blockNature == TokenBlockNature.EVEN_TOKEN) {
                            if (block % 2 == 0) {
                                appointmentStatus = DiaryStatus.Block;
                            }
                        } else if (blockNature == TokenBlockNature.ODD_TOKEN) {
                            if (block % 2 != 0) {
                                appointmentStatus = DiaryStatus.Block;
                            }
                        }
                        map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                        map.put("consultantCode".toLowerCase(), DmShared.iNull(consultantCode).longValue());
                        map.put("vrDate".toLowerCase(), DmShared.dNull(vrDate));
                        map.put("fromTime".toLowerCase(), DmServer.vNull(shift.getFromTime(), "00.00"));
                        map.put("toTime".toLowerCase(), DmServer.vNull(shift.getToTime(), "00.00"));
                        map.put("tokenNo".toLowerCase(), tokenNo);
                        map.put("appointmentStatus".toLowerCase(), appointmentStatus.getOptionValue());
                        document = new BasicDBObject(map);
                        dBCollection.insert(document);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private static List<ShiftTime_Mdl> getTimeList(String fromTimeString, String toTimeString, int graceMinute) {
        List<ShiftTime_Mdl> timeDetailList = new ArrayList<ShiftTime_Mdl>();
        ShiftTime_Mdl timeDetail;
        double startTime = DmShared.vNull(fromTimeString), endTime = DmShared.vNull(toTimeString), tempTime = 0;

        try {
            graceMinute = (DmShared.iNull(graceMinute).intValue() > 0 ? DmShared.iNull(graceMinute).intValue() : 5);

            while (startTime <= endTime) {
                timeDetail = new ShiftTime_Mdl();
                timeDetail.setGraceMinute(graceMinute);
                timeDetail.setFromTime(DmServer.vNull(startTime, "00.00"));
                startTime = DmServer.mntsToHrs(DmServer.hrsToMnts(startTime) + (graceMinute));
                tempTime = DmServer.mntsToHrs(DmServer.hrsToMnts(startTime) + (- 1));
                timeDetail.setToTime(DmServer.vNull(tempTime, "00.00"));
                timeDetailList.add(timeDetail);
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
        return timeDetailList;
    }

    public static HashMap<String, String> getDefaultEntryPointVoucher(String widgetName, Connection cnTemp) {
        HashMap<String, String> rtnVal = new HashMap<>();
        String sqlQuery;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {
            sqlQuery = "select  rbm.entryPoint, vt.short_Name as entryVoucherName, vt1.short_Name as receiptVoucherName "
                    + "         from receiptBookMapping rbm "
                    + "         left join voucher_Type vt on vt.v_Type= rbm.v_Type "
                    + "         left join voucher_Type vt1 on vt1.v_Type= rbm.v_Receipt "
                    + "         where entryPoint = :entryPoint and isDefault= 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryPoint", widgetName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                rtnVal.put("entryVoucherName", DmShared.xNull(rsTemp.getString("entryVoucherName")));
                rtnVal.put("receiptVoucherName", DmShared.xNull(rsTemp.getString("receiptVoucherName")));
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
        return rtnVal;
    }

    public static String getSsnAllowDiscountPercentUpto(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("allowDiscountPercentUpto"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void checkUserWiseDiscountPercentUpto(String organizationShortName, double userAllowDiscountPercent, double serviceDiscountPercent,
            double serviceDiscountAmount, double serviceGrossAmount, String serviceName) {
        if (DmShared.xNull(organizationShortName).equalsIgnoreCase("")) {
            throw new DmException(0, "Define organization's short name to validate user discount % upto.");
        }
        if (DmShared.xNull(serviceName).equalsIgnoreCase("")) {
            throw new DmException(0, "Define service name to validate user discount % upto.");
        }
        if (DmShared.xNull(organizationShortName).equalsIgnoreCase("CP")) {
            if ((serviceDiscountAmount > 0.0d || serviceDiscountPercent > 0.0d) && userAllowDiscountPercent > 0.0d) {
                if (serviceDiscountPercent == 0.0d && serviceDiscountAmount > 0.0d) {
                    serviceDiscountPercent = (serviceDiscountAmount / serviceGrossAmount) * 100;
                }
                if (serviceDiscountPercent > 0.0d && (serviceDiscountPercent > userAllowDiscountPercent)) {
                    throw new DmException(0, "You can apply discount upto " + DmServer.vNull(userAllowDiscountPercent, "0.00") + "% on " + serviceName);
                }
            }
        }
    }

    public static String acknowledgeService(String docId, String vSNo, boolean isAutoVerify, String userName, Connection cnTemp) {
        DmPreparedStatement psTemp = null;

        String sqlQuery, returnValue = "";

        try {

            String[] dateTime = DmServer.getServerDateTime();
            sqlQuery = "Update	patientServiceDetail set  "
                    + "		acknowledgeBy = :acknowledgeBy, "
                    + "		acknowledgeDate = :acknowledgeDate, "
                    + "		acknowledgeStatus = :acknowledgeStatus, "
                    + "		acknowledgeTime = :acknowledgeTime "
                    + "		where docId = :docId and v_SNo = :v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("v_SNo", DmShared.iNull(vSNo).toString());

            if (!isAutoVerify) {
                psTemp.setString("acknowledgeBy", userName);
                psTemp.setDate("acknowledgeDate", DmServer.convertDate(dateTime[0]));
                psTemp.setString("acknowledgeTime", DmServer.vNull(dateTime[1], "00.00"));
                psTemp.setString("acknowledgeStatus", "2");
            } else {
                psTemp.setDate("acknowledgeDate", DmServer.convertDate(dateTime[0]));
                psTemp.setString("acknowledgeTime", DmServer.vNull(dateTime[1], "00.00"));
                psTemp.setString("acknowledgeBy", userName);
                psTemp.setString("acknowledgeStatus", "1");
            }
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return returnValue;
    }

    public static String getUserWithDateTime(String userName, Timestamp entryDateTime) {
        String rtnValue = "";
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy - HH:mm");
        try {
            if (!DmShared.xNull(userName).equalsIgnoreCase("") && entryDateTime != null) {
                cal = Calendar.getInstance();
                cal.setTime(new Date(entryDateTime.getTime()));
                rtnValue = userName + " - " + sdf.format(cal.getTime());
            }
        } catch (Exception e) {
        }
        return rtnValue;
    }

    public static DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, double rate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualITS, Connection cnTemp) throws Exception {
        DmResult<List<GSTS_Mdl>> dmrTemp = new DmResult();
        List<GSTS_Mdl> itsList = new ArrayList<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "", gstTypeName = "";

        try {

            if (!isManualITS) {
                if (DmShared.xNull(itsBasedOn).equals("1")) {
                    condition += " AND (" + DmServer.vNull(rate) + " BETWEEN iid.rateFrom AND iid.rateUpto)\n";
                }

                sqlQuery = "SELECT iid.gstTypeCode, it.name AS gstTypeName\n"
                        + "FROM itemMast im    \n"
                        + "LEFT JOIN itemItsDetail iid ON iid.code = im.Code  \n"
                        + "LEFT JOIN gstType it ON it.code = iid.gstTypeCode\n"
                        + "WHERE im.code = :itemCode\n"
                        + condition;

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    gstTypeCode = DmShared.xNull(rsTemp.getString("gstTypeCode"));
                    gstTypeName = DmShared.xNull(rsTemp.getString("gstTypeName"));
                }
                rsTemp.close();
                psTemp.close();
            }

            if (DmShared.iNull(locationCode).intValue() != 0
                    && DmShared.iNull(gstTypeCode).intValue() != 0) {
                itsList.clear();
                itsList.addAll(GSTS_Server.getGSTS(locationCode, gstTypeCode, cnTemp));
            }
            dmrTemp.setData(itsList);
            if (!isManualITS) {
                dmrTemp.setValue1(gstTypeCode);
                dmrTemp.setValue2(gstTypeName);
            }

        } catch (Exception e) {
            throw e;
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

    public static void savePriceListItem(String applicableFrom, PriceListEntryFrom priceListEntryFrom, String keyCode,
            String priceListCode, String site_Code, List<ItemPriceList_Mdl> priceList, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        String sqlQuery, sqlCondition = "", applicableTo = "31-Dec-9999", maxApplicableDate = "";
        ResultSet rsTemp = null, rsTemp1 = null;

        switch (priceListEntryFrom) {
            case PARTY_PRICELIST:
                sqlCondition += " and subCode = :keyCode ";
                break;
            case PRICELIST_MASTER:
                sqlCondition += " and itemCode = :keyCode ";
                break;
            default:
                throw new DmException(0, "Price List: Entry From not found.");
        }

        try {

            sqlQuery = "select Max(applicableFrom) as applicableFrom,Min(applicableFrom) as applicableFromMin  "
                    + "from priceList "
                    + "where entryFrom = :entryFrom"
                    + " and site_Code = :site_Code "
                    + "and priceListCode = :priceListCode "
                    + sqlCondition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("priceListCode", priceListCode);
            psTemp.setString("keyCode", keyCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFrom"));
            }
            rsTemp.close();
            psTemp.close();

            if (!maxApplicableDate.equalsIgnoreCase("")) {
                if (DmServer.convertDate(applicableFrom).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                    sqlQuery = "select applicableFrom,applicableTo "
                            + "from priceList "
                            + "where entryFrom = :entryFrom "
                            + "and site_Code = :site_Code "
                            + "and priceListCode = :priceListCode "
                            + "and applicableFrom < :applicableFrom "
                            + sqlCondition + "\n"
                            + " group by applicableFrom,applicableTo "
                            + "order by applicableFrom desc "
                            + "offset 0 rows fetch next 1 rows only ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
                    psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                    psTemp.setString("site_Code", site_Code);
                    psTemp.setString("priceListCode", priceListCode);
                    psTemp.setString("keyCode", keyCode);
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
                        sqlQuery = "update priceList set "
                                + "applicableTo = :applicableTo "
                                + "where applicableFrom = :applicableFrom "
                                + "and entryFrom = :entryFrom "
                                + "and site_Code = :site_Code "
                                + "and priceListCode = :priceListCode "
                                + sqlCondition;
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(applicableFrom))));
                        psTemp1.setDate("applicableFrom", rsTemp.getDate("ApplicableFrom"));
                        psTemp1.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                        psTemp1.setString("site_Code", site_Code);
                        psTemp1.setString("priceListCode", priceListCode);
                        psTemp1.setString("keyCode", keyCode);
                        psTemp1.executeUpdate();
                        psTemp1.close();
                    } else {
                        sqlQuery = "select applicableFrom,applicableTo "
                                + "FROM priceList "
                                + "where applicableFrom > :applicableFrom "
                                + "and entryFrom = :entryFrom "
                                + "and site_Code = :site_Code "
                                + "and priceListCode = :priceListCode "
                                + sqlCondition + "\n"
                                + "group by applicableFrom,applicableTo "
                                + "order by applicableFrom asc offset 0 rows fetch next 1 rows only ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
                        psTemp1.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                        psTemp1.setString("site_Code", site_Code);
                        psTemp1.setString("priceListCode", priceListCode);
                        psTemp1.setString("keyCode", keyCode);
                        rsTemp1 = psTemp1.executeQuery();
                        if (rsTemp1.next()) {
                            applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFrom")));
                        }
                        psTemp1.close();
                        rsTemp1.close();
                    }
                    psTemp.close();
                    rsTemp.close();
                } else {
                    sqlQuery = "update priceList set "
                            + "applicableTo = :applicableTo "
                            + "where applicableFrom = :applicableFrom "
                            + "and entryFrom = :entryFrom "
                            + "and site_Code = :site_Code "
                            + "and priceListCode = :priceListCode "
                            + sqlCondition;
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFrom", DmServer.convertDate(maxApplicableDate));
                    psTemp.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(applicableFrom))));
                    psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                    psTemp.setString("site_Code", site_Code);
                    psTemp.setString("priceListCode", priceListCode);
                    psTemp.setString("keyCode", keyCode);
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            sqlQuery = "delete from priceList "
                    + "where applicableFrom = :applicableFrom "
                    + "and entryFrom = :entryFrom "
                    + "and site_Code = :site_Code "
                    + "and priceListCode = :priceListCode "
                    + " and applicableTo = :applicableTo "
                    + sqlCondition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
            psTemp.setString("site_Code", site_Code);
            psTemp.setString("priceListCode", priceListCode);
            psTemp.setString("keyCode", DmShared.xNull(keyCode));
            psTemp.setDate("applicableTo", DmServer.convertDate(applicableTo));
            psTemp.executeUpdate();
            psTemp.close();

            for (ItemPriceList_Mdl ipl : priceList) {
                if (!DmShared.xNull(ipl.getItemCode()).trim().equalsIgnoreCase("") && !DmShared.xNull(ipl.getUnitCode()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "insert into priceList(entryFrom, entryFromNature, site_code, priceListCode, subCode,  itemCode, unitCode, applicableFrom, applicableTo,  "
                            + " nature,rate,mrp, discount, discountPer, printSNo) "
                            + "values (:entryFrom, :entryFromNature, :site_code, :priceListCode, :subCode, :itemCode, :unitCode, :applicableFrom, :applicableTo, "
                            + " :nature, :rate,:mrp, :discount, :discountPer, :printSNo )";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("entryFrom", String.valueOf(priceListEntryFrom.toInt()));
                    psTemp.setString("entryFromNature", DmShared.iNull(ipl.getEntryFromNature()).toString());
                    psTemp.setString("site_Code", DmShared.xNull(site_Code));
                    psTemp.setString("priceListCode", DmShared.foriegnKey(priceListCode));
                    psTemp.setString("itemCode", DmShared.foriegnKey(ipl.getItemCode()));
                    psTemp.setString("unitCode", DmShared.foriegnKey(ipl.getUnitCode()));
                    psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
                    psTemp.setDate("applicableTo", DmServer.convertDate(applicableTo));
                    psTemp.setString("rate", DmServer.vNull(ipl.getRate(), "0.00"));
                    psTemp.setString("mrp", DmServer.vNull(ipl.getMrp(), "0.00"));
                    psTemp.setString("discount", DmServer.vNull(ipl.getDiscountAmount(), "0.00"));
                    psTemp.setString("discountPer", DmServer.vNull(ipl.getDiscountPercentage()));
                    psTemp.setString("printSNo", DmShared.foriegnKey(ipl.getPrintSNo()));
                    psTemp.setString("subCode", DmShared.foriegnKey(ipl.getPartyCode()));
                    psTemp.setString("nature", DmShared.iNull(PriceListNature.None.getId()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (rsTemp1 != null) {
                    rsTemp1.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (psTemp1 != null) {
                    psTemp1.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static HashMap getSystemDefinedGstLocationMap(Connection cnTemp) throws Exception {
        HashMap<String, GstLocation_Mdl> gstLocationMap = new HashMap<String, GstLocation_Mdl>();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            sqlQuery = "SELECT lm.code, lm.name, lm.manualCode\n"
                    + "FROM locationMast lm\n"
                    + "WHERE lm.isSystemGenerated = 1\n"
                    + "AND lm.isActive = 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("manualCode")).trim().equalsIgnoreCase("")) {
                    DmEnum.DmGstLocation_SystemDefined location = DmEnum.DmGstLocation_SystemDefined.getViaManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                    if (location != null) {
                        GstLocation_Mdl glm = new GstLocation_Mdl();
                        glm.setCode(DmShared.xNull(rsTemp.getString("code")));
                        glm.setName(DmShared.xNull(rsTemp.getString("name")));
                        glm.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));

                        gstLocationMap.put(DmShared.xNull(rsTemp.getString("manualCode")), glm);
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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

        return gstLocationMap;
    }

    //=======================================<MJ>================================//
    public static void checkCustomerCreditLimit(String docid, String partyCode, double netAmount, Connection cnTemp) throws Exception {
        try {
            checkCustomerCreditLimit(docid, partyCode, netAmount, true, cnTemp);
        } catch (Exception e) {
            throw e;
        }
    }

    public static String checkCustomerCreditLimit(String docid, String partyCode, double netAmount, boolean isThrowExceptionOnExceedingLimit, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        String exceedAmt = "";
        try {
            /*==============================<< Check for credit Limit >>====================================*/
            sqlQuery = "SELECT	ISNULL(SUM(l.amtDr), 0) - ISNULL(SUM(l.amtCr), 0) AS balanceAmt \n"
                    + "		, MAX(sg.creditLimit) AS creditLimit \n"
                    + "		, MAX(sg.creditLimit_Temp) AS creditLimit_Temp \n"
                    + "		, MAX(sg.creditLimit_Temp_Date) AS creditLimit_Temp_Date \n"
                    + "		FROM ledger l \n"
                    + "		LEFT JOIN subGroup sg ON sg.subCode = l.subcode \n"
                    + "		WHERE l.subcode = :partyCode \n"
                    + "		AND ISNULL(sg.creditLimit, :zero) > :zero \n"
                    + "         AND l.isPosted = :one \n"
                    + "         AND l.docId <> :docId \n"
                    + "		GROUP BY l.subcode \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docid));
            psTemp.setString("partyCode", DmShared.xNull(partyCode));
            psTemp.setInt("zero", 0);
            psTemp.setInt("one", 1);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                double totalDrAmount = DmShared.vNull(rsTemp.getString("balanceAmt")).doubleValue() + DmShared.vNull(netAmount).doubleValue(), totalCreditLimit = 0;
                totalCreditLimit = DmShared.vNull(rsTemp.getString("creditLimit")).doubleValue();
                String currentDate = DmShared.dNull(DmServer.getServerDateTime()[0]);
                if (!DmShared.dNull(rsTemp.getDate("creditLimit_Temp_Date")).trim().equalsIgnoreCase("") && !DmShared.dNull(currentDate).trim().equalsIgnoreCase("")) {
                    if (DmShared.convertDate(DmShared.dNull(rsTemp.getDate("creditLimit_Temp_Date"))).getTime() >= DmShared.convertDate(currentDate).getTime()) {
                        totalCreditLimit += DmShared.vNull(rsTemp.getString("creditLimit_Temp")).doubleValue();
                    }
                }
                if (totalCreditLimit < totalDrAmount) {
                    exceedAmt = DmServer.vNull((totalDrAmount - totalCreditLimit), "0.00");
                    if (isThrowExceptionOnExceedingLimit) {
                        throw new Exception("Credit limit Exceeded by Rs. " + exceedAmt + "!");
                    } else {
                        return exceedAmt;
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return exceedAmt;
    }

    public static HashMap setJasperVariablesMain(String siteCode, String reportTitle, JasperReport jasperReport, LinkedHashMap<String, FieldUDT_Mdl> filter, HashMap parameter) throws Exception {
        JRParameter jrParameter[];
        String tempString = "", sqlQuery = "";//,sqlQuery
        File file;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        HashMap<String, String> jsParamValue = new HashMap<>();
        Connection cnTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            //===============================================getFilter=================================
            sqlQuery = " Select	 plt.comp_Name as companyName, com.cYear \n"
                    + "from ProductLicensedTo as plt  \n"
                    + "left join company as com on    com.pltCode =plt.pltCode and com.comp_Code=plt.latestCompCode\n"
                    + "where plt.pltCode =1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                jsParamValue.put("dmCompanyName", DmShared.xNull(rsTemp.getString("companyName")));
                jsParamValue.put("dmFinancialYear", DmShared.xNull(rsTemp.getString("cYear")));

            }
            psTemp.close();
            rsTemp.close();

            //===============================================getFilter=================================
            sqlQuery = " SELECT  name as siteName, siteDisplayName,  concat(isnull(address1,''),'  ', isnull(address2,'')) as addressFull,\n"
                    + "city,gstin, gstinDate, phone, email,drugLicenseNo\n"
                    + "FROM   siteMast where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", siteCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                jsParamValue.put("dmSiteName", DmShared.xNull(rsTemp.getString("siteName")));
                jsParamValue.put("dmSiteDisplayName", DmShared.xNull(rsTemp.getString("siteDisplayName")));
                jsParamValue.put("dmSiteAddress", DmShared.xNull(rsTemp.getString("addressFull")));
                jsParamValue.put("dmSiteCity", DmShared.xNull(rsTemp.getString("city")));
                jsParamValue.put("dmSitePhone", DmShared.xNull(rsTemp.getString("phone")));
                jsParamValue.put("dmSiteEMail", DmShared.xNull(rsTemp.getString("email")));
                jsParamValue.put("dmSiteDrugLicenseNo", DmShared.xNull(rsTemp.getString("drugLicenseNo")));
                jsParamValue.put("dmSiteGSTIN", DmShared.xNull(rsTemp.getString("gstin")));
                jsParamValue.put("dmSiteGSTINDate", DmShared.xNull(rsTemp.getString("gstinDate")));

            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

            parameter = setJasperVariablesIni(reportTitle, jasperReport, filter, parameter);

            jrParameter = jasperReport.getParameters();
            for (int i = 0; i < jrParameter.length; i++) {
                if (jrParameter[i].getName().equalsIgnoreCase("dmPrintBy")) {
                    parameter.put(jrParameter[i].getName(), "System generated.");
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmCompanyName")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmCompanyName")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmFinancialYear")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmFinancialYear")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteName")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSiteName")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteDisplayName")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSiteDisplayName")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteAddress")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSiteAddress")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmTitle")) {
                    parameter.put(jrParameter[i].getName(), reportTitle);
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteCity")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSiteCity")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSitePin")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSitePin")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSitePhone")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSitePhone")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteEMail")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSiteEMail")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteDrugLicenseNo")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSiteDrugLicenseNo")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteGSTIN")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSiteGSTIN")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteGSTINDate")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(jsParamValue.get("dmSiteGSTINDate")));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmCompanyLogo")) {
                    file = new File(DmServer.Path.images + "/" + siteCode + "/companyLogo");
                    if (file.exists()) {
                        parameter.put(jrParameter[i].getName(), DmServer.Path.images + "/" + siteCode + "/companyLogo");
                    }
                }
            }

        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return parameter;
    }

    private static HashMap setJasperVariablesIni(String reportTitle, JasperReport jasperReport, LinkedHashMap<String, FieldUDT_Mdl> filter, HashMap parameter) throws Exception {
        JRParameter jrParameter[];
        String tempString;
        Object tempObject;

        if (parameter == null) {
            parameter = new HashMap();
        }

        jrParameter = jasperReport.getParameters();
        for (int i = 0; i < jrParameter.length; i++) {
            if (jrParameter[i].getName().equalsIgnoreCase("dmCurrentDateTime")) {
                parameter.put(jrParameter[i].getName(), DmDateTimeFormat.getFormat("dd-MMM-yyyy HH:mm").format(new java.util.Date()));
            } else if (jrParameter[i].getName().equalsIgnoreCase("SUBREPORT_DIR")) {
                tempObject = parameter.get("SUBREPORT_DIR");
                tempString = "";
                if (tempObject != null) {
                    tempString = tempObject.toString();
                }

                if (tempString.trim().equalsIgnoreCase("osr")) {
                    parameter.put(jrParameter[i].getName(), DmBaseServer.Path.reports + "/osr/");

                } else {
                    parameter.put(jrParameter[i].getName(), DmBaseServer.Path.reports + "/system/");

                }
            } else if (jrParameter[i].getName().equalsIgnoreCase("dmFrmRptFormulaFieldDisplay")) {
                tempString = "";
//                if (filter != null) {
//                    Set<String> keySet = filter.keySet();
//                    if (keySet != null) {
//                        for (String key : keySet) {
//                            Filter_Mdl filter_Mdl = filter.get(key);
//                            if (filter_Mdl != null) {
//                                if (!DmBaseShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
//                                    if (!tempString.equalsIgnoreCase("")) {
//                                        tempString += "\n";
//                                    }
//                                    tempString += DmBaseShared.xNull(filter_Mdl.getFieldCaption()) + ": " + DmBaseShared.xNull(filter_Mdl.getText());
//                                }
//                            }
//                        }
//                    }
//
//                }

                if (filter != null) {
                    Set<String> keySet = filter.keySet();
                    if (keySet != null) {
                        for (String key : keySet) {
                            FieldUDT_Mdl filter_Mdl = filter.get(key);
                            if (filter_Mdl != null) {
                                if (!DmBaseShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
//                                    if (!tempString.equalsIgnoreCase("")) {
//                                        tempString += "\n";
//                                    }
                                    tempString += "<b><u>" + DmBaseShared.xNull(filter_Mdl.getFieldCaption()) + ":</b></u> " + DmBaseShared.xNull(filter_Mdl.getText()) + "; ";
//                                    tempString += DmBaseShared.xNull(filter_Mdl.getFieldCaption()) + ":<b> " + DmBaseShared.xNull(filter_Mdl.getText()) + ";</b> ";
                                }
                            }
                        }
                    }

                }
                parameter.put(jrParameter[i].getName(), tempString);
            }
        }

        return parameter;
    }

    public static void sendMailSystemReports(String filePath, String documentMailUser, String toEmail, String ccEmail, String subject, String encodedMsg, String attachedFileName) {

        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

//        String to = "webaarogya@gmail.com";
//        String from = "webaarogya@gmail.com";
//        final String username = "webaarogya@gmail.com";
//        final String password = "bhagcvigslkvwhox";
//        String host = "smtp.gmail.com";
//        String port = "465";
        String username = "";
        String password = "";
        String from = "";
        String host = "";
        String port = "";
//        String toEmail = "";
//        String ccEmail = "";

        Properties props = new Properties();

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "select top 1 emailId,password   from userPLTConfig where  userName=:userName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", documentMailUser);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                username = DmShared.xNull(rsTemp.getString("emailId"));
                from = DmShared.xNull(rsTemp.getString("emailId"));
                password = dCodify(DmShared.xNull(rsTemp.getString("password")));

            }
            rsTemp.close();
            psTemp.close();

            String transDatabase = DmServer.executeScalar("select top 1 centralData_Path from company", cnTemp);
            cnTemp.setCatalog(transDatabase);
//            cnTemp.setCatalog(DmServer.getSsnDBName(this));

            sqlQuery = "select wj_SmtpServer,wj_SmtpPort  from enviro  where site_Code= :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", "1");
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                host = DmShared.xNull(rsTemp.getString("wj_SmtpServer"));
                port = DmShared.xNull(rsTemp.getString("wj_SmtpPort"));

            }
            rsTemp.close();
            psTemp.close();

//            String msgBody = "";
//            String encodedMsg = java.net.URLEncoder.encode(msgBody, "UTF-8");
//            String encodedMsg = "Please find attachment";
//            String subject = "System Report Email";
            //==================================================================
            //============================================================Send Mail=================================================
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.password", password);
            props.put("userName", username);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(props.getProperty("userName"), props.getProperty("mail.smtp.password"));
                }
            });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            if (!DmShared.xNull(ccEmail).equalsIgnoreCase("")) {
                message.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(ccEmail));
            }

            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setContent(encodedMsg, "text/html; charset=UTF-8");

            MimeMultipart multipart = new MimeMultipart("related");

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
//            String filename = "C:\\Users\\dataman\\Desktop\\Pankaj\\saleBill.pdf";
            String filename = filePath;
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
//            messageBodyPart.setFileName("attachment.pdf");
            messageBodyPart.setFileName(attachedFileName);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Sent mail successfully by external thread.");

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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static String dCodify(String stringToDCodify) throws Exception {
        byte[] keyBytes = convertStringToBytes(codifyKey1 + codifyKey2);
        Key key = new SecretKeySpec(keyBytes, codifyAlgorithm);
        Cipher cipher = Cipher.getInstance(codifyAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = java.util.Base64.getDecoder().decode(stringToDCodify);
        byte[] decryptedValue = cipher.doFinal(decryptedValue64);
        String decryptedString = new String(decryptedValue, "utf-8");
        return decryptedString;
    }

    private static byte[] convertStringToBytes(String str) {
        char[] charArray = str.toCharArray();
        byte[] bytes = new byte[str.toCharArray().length];
        for (int i = 0; i < charArray.length; i++) {
            bytes[i] = (byte) charArray[i];
        }
        return bytes;
    }

    public static void checkUnapproveUptoDays(String siteCode, String docId, Connection cnTemp) throws Exception {
        String unApproveUptoDays = "";
        String vDate = "", currentDate = "";
        String reviewDate = "";
        try {

            unApproveUptoDays = DmServer.executeScalar("select unApproveUptoDays from customEnviro where site_Code = " + siteCode + "", cnTemp);

            if (DmShared.iNull(unApproveUptoDays).intValue() > 0) {
                vDate = DmServer.executeScalar("select format(v_Date,'dd-MMM-yyyy') from pOrder where docid = " + docId + "", cnTemp);
                currentDate = DmServer.getServerDateTime()[0];

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                Calendar c = Calendar.getInstance();
                c.setTime(formatter.parse(DmShared.dNull(vDate)));
                c.add(Calendar.DATE, DmShared.iNull(unApproveUptoDays).intValue());
                reviewDate = formatter.format(c.getTime());

                if (DmShared.convertDate(currentDate).getTime() > DmShared.convertDate(reviewDate).getTime()) {
                    throw new DmException(100, "Can'nt be Un-Approve.");
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {

            } catch (Exception e) {
            }
        }

    }

    public static void calculateEditUptoDays(String siteCode, String docId, Connection cnTemp) throws Exception {
        String vDate = "", currentDate = "";
        String reviewDate = "";
        try {

            /*==============================<< Get customEnviro Data >>====================================*/
            String editUptoDays = DmServer.executeScalar("Select editUptoDays from customEnviro where site_code = " + siteCode + "", cnTemp);

            if (DmShared.iNull(editUptoDays).intValue() > 0) {
                vDate = DmServer.executeScalar("select format(v_Date,'dd-MMM-yyyy') from pOrder where docid = " + docId + "", cnTemp);
                currentDate = DmServer.getServerDateTime()[0];

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                Calendar c = Calendar.getInstance();
                c.setTime(formatter.parse(DmShared.dNull(vDate)));
                c.add(Calendar.DATE, DmShared.iNull(editUptoDays).intValue());
                reviewDate = formatter.format(c.getTime());

                if (DmShared.convertDate(currentDate).getTime() > DmShared.convertDate(reviewDate).getTime()) {
                    throw new DmException(100, "This record cannot be edit as the edit date has expired..");
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {

            } catch (Exception e) {
            }
        }
    }
}
