package aarogya.selfToolkit.stStatic.client;

import aarogya.selfToolkit.stStatic.shared.ConvertedQuantity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmDateTimeFormat;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import aarogya.selfToolkit.stStatic.shared.STClient_Srv;
import aarogya.selfToolkit.stStatic.shared.STClient_SrvAsync;
import aarogya.selfToolkit.stStatic.shared.STLocalSetting_Mdl;
import aarogya.selfToolkit.stStatic.shared.SaleRateRule_Mdl;
import aarogya.selfToolkit.taxSummary.shared.TaxSummary_Mdl;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import dataman.dmBase.self.shared.DmEnum.DmLocaleCountry;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class STClient {

    public interface IPACHandShake {

        public void onSuccess();
    }

    public static class Age {

        private int day = 0, month = 0, year = 0;

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

    }

    public static double getRoundoff(double amount) {
        double roundoffAmt = 0;
        roundoffAmt = DmShared.compute("Math.round(" + amount + ")") - amount;
        return roundoffAmt;
    }

    public static Age getAge(String currentDate, String DOB, int showDDUptoMonth, int showMMUptoYear) {
        int cdDD, cdMM, cdYYYY, dobDD, dobMM, dobYYYY;

        Age age = new Age();

        if (DmShared.xNull(currentDate).trim().equalsIgnoreCase("")
                && DmShared.xNull(DOB).trim().equalsIgnoreCase("")) {

        } else {
            cdDD = Integer.valueOf(DmDateTimeFormat.getFormat("dd").format(DmShared.convertDate(currentDate)));
            cdMM = Integer.valueOf(DmDateTimeFormat.getFormat("MM").format(DmShared.convertDate(currentDate)));
            cdYYYY = Integer.valueOf(DmDateTimeFormat.getFormat("yyyy").format(DmShared.convertDate(currentDate)));

            dobDD = Integer.valueOf(DmDateTimeFormat.getFormat("dd").format(DmShared.convertDate(DOB)));
            dobMM = Integer.valueOf(DmDateTimeFormat.getFormat("MM").format(DmShared.convertDate(DOB)));
            dobYYYY = Integer.valueOf(DmDateTimeFormat.getFormat("yyyy").format(DmShared.convertDate(DOB)));

            if (cdDD == 0 || cdMM == 0 || cdYYYY == 0 || dobDD == 0 || dobMM == 0 || dobYYYY == 0) {
                return age;
            }

            if (cdDD < dobDD) {
                if ((cdMM - 1 == 1) || (cdMM - 1 == 3) || (cdMM - 1 == 5) || (cdMM - 1 == 7) || (cdMM - 1 == 8) || (cdMM - 1 == 10) || (cdMM - 1 == 12)) {
                    cdDD = cdDD + 31;
                } else if ((cdMM - 1 == 4) || (cdMM - 1 == 6) || (cdMM - 1 == 9) || (cdMM - 1 == 11)) {
                    cdDD = cdDD + 30;
                } else if (cdMM - 1 == 2) {
                    if (((cdYYYY % 4 == 0) && (cdYYYY % 100 != 0)) || (cdYYYY % 400 == 0)) {
                        cdDD = cdDD + 29;
                    } else {
                        cdDD = cdDD + 28;
                    }
                }
                cdMM = cdMM - 1;
            }

            if (cdMM < dobMM) {
                cdMM = cdMM + 12;
                cdYYYY = cdYYYY - 1;
            }

            age.setDay(cdDD - dobDD);
            age.setMonth(cdMM - dobMM);
            age.setYear(cdYYYY - dobYYYY);

            if ((age.getMonth() <= showDDUptoMonth) && (age.getYear() == 0)) {

            } else if ((age.getYear() <= showMMUptoYear || showMMUptoYear == 0)) {
                age.setDay(0);
            } else {
                age.setDay(0);
                age.setMonth(0);

            }
        }

        return age;
    }

    public static ConvertedQuantity getSecondryQty(String skuQty, String convertionRate) {
        ConvertedQuantity convertedQuantity = new ConvertedQuantity();
        double skuQtyTemp, convertionTemp;

        skuQtyTemp = DmShared.vNull(DmClient.vNull(skuQty, "#.#######"));
        convertionTemp = DmShared.vNull(DmClient.vNull(convertionRate, "#.#######"));
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

    public static void pacHandShake(final IPACHandShake iPACHandShake) {
        try {
//            DmClient.setLocalSettingPrintMap(new ArrayList<DmLocalSettingPrintMap_Mdl>());
            DmClient.clearLocalSettingPrintReportMap();
            DmClient.clearLocalSettingPrinters();
            RequestBuilder rBuilder = new RequestBuilder(RequestBuilder.GET, "http://localhost:" + String.valueOf(DmClient.getPACWebPort()) + "/queryClient");
            rBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if ((!DmShared.xNull(response.getText()).equalsIgnoreCase("")) && (!DmShared.xNull(response.getText()).equalsIgnoreCase("test"))) {
//                        DmClient.setTcpSessionId(response.getText().toLowerCase().trim());
                        getLocalSettings(iPACHandShake);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Window.alert(exception.getMessage());
                }
            });
        } catch (Exception e) {
        }
    }

    private static void getLocalSettings(final IPACHandShake iPACHandShake) {
        try {
            String pacBaseURL = "http://localhost:" + DmShared.iNull(DmClient.getPACWebPort()).toString() + "/localSettings?";
            String tempUrl = pacBaseURL + "cmd=getLocalSettings";
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, tempUrl);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        getSettingsFromJson(iPACHandShake, response.getText());

                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    new DmAlertMessageBox(exception.getMessage(), (Button) null).show();
                }
            });
            requestBuilder.send();
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    private static void getSettingsFromJson(final IPACHandShake iPACHandShake, final String settingsString) {
        ServiceInvoker<STClient_SrvAsync> SrvAsync = new ServiceInvoker<STClient_SrvAsync>(GWT.create(STClient_Srv.class), STClient.class.getName());

        SrvAsync.invokeRPC().getSettingsFromJson(settingsString, new AsyncCallback<DmResult<STLocalSetting_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<STLocalSetting_Mdl> result) {
                if (result.isStatus()) {
                    DmClient.refreshLocalSettingPrintReportMap(result.getData().getDmLocalSettingPrintMap_Mdls());
                    DmClient.refreshLocalSettingPrinters(result.getData().getDmLocalSettingPrinter_Mdls());

                    if (iPACHandShake != null) {
                        iPACHandShake.onSuccess();
                    }
                }
            }
        }
        );
    }

    public static String updatePrintStatus(final String tableName, final String fieldName, final String docId) {
        String returnStatus = "";
        ServiceInvoker<STClient_SrvAsync> SrvAsync = new ServiceInvoker<STClient_SrvAsync>(GWT.create(STClient_Srv.class), STClient.class.getName());

        SrvAsync.invokeRPC().updatePrintStatus(tableName, fieldName, docId, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {

                if (result.isStatus()) {
//                    returnStatus ="";

                }

            }
        }
        );
        return returnStatus;
    }

    public static void sendReportToEmail(String docId, String vType, LinkedHashMap<String, FieldUDT_Mdl> filter, String reportTitle) {

        ServiceInvoker<STClient_SrvAsync> SrvAsync = new ServiceInvoker<STClient_SrvAsync>(GWT.create(STClient_Srv.class), STClient.class.getName());

        SrvAsync.invokeRPC(false).sendReportToEmail(docId, vType, filter, DmShared.xNull(DmClient.getRegionalLanguage()), reportTitle, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {

                if (result.isStatus()) {
//                    returnStatus ="";

                }

            }
        }
        );

    }

    public static double getSaleRate(double mrp, double rate, double conversionRate, double freeQty, String isIncludeFreeQtyInSaleRate,
            List<SaleRateRule_Mdl> lstSaleRateRule, DmLocaleCountry localeCountry, String itemCategoryProffitMargine, String saleRateCalculateOn, String isMarkupApplicable) {
        double saleRate = 0, skuRate = 0;
//        if (DmShared.iNull(isIncludeFreeQtyInSaleRate).intValue() != 1) {
//            rate = DmShared.vNull(rate).doubleValue() / (1 + DmShared.vNull(freeQty).doubleValue());
//        }

        if (localeCountry == DmLocaleCountry.CAMBODIA_USA) {
            saleRate = rate;
            skuRate = rate / conversionRate;
            if (lstSaleRateRule != null) {
                for (SaleRateRule_Mdl sr : lstSaleRateRule) {
                    if (skuRate >= DmShared.vNull(sr.getMarginFrom())
                            && skuRate <= DmShared.vNull(sr.getMarginTo())) {
                        saleRate = rate + (rate * DmShared.vNull(sr.getProfitToBeTaken()) * 0.01);
                        break;
                    }
                }
            }
        } else {
            saleRate = mrp;
            if (DmShared.xNull(saleRateCalculateOn).trim().equalsIgnoreCase("2") && DmShared.iNull(isMarkupApplicable).intValue() == 1) {
                saleRate = rate + (saleRate - rate) / 2;
            } else if (DmShared.vNull(itemCategoryProffitMargine).doubleValue() > 0) {
                saleRate = rate + (rate * DmShared.vNull(itemCategoryProffitMargine) * 0.01);

            } else {
//                if (lstSaleRateRule != null) {
//                    double profit = DmShared.vNull(DmShared.vNull(mrp) - DmShared.vNull(rate));
//                    double profitPercent = Math.round((profit * 100) / DmShared.vNull(rate));
//                    for (SaleRateRule_Mdl sr : lstSaleRateRule) {
//                        if (profitPercent >= DmShared.vNull(sr.getMarginFrom())
//                                && profitPercent <= DmShared.vNull(sr.getMarginTo())) {
//                            saleRate = rate + (rate * DmShared.vNull(sr.getProfitToBeTaken()) * 0.01);
//                            break;
//                        }
//                    }
//                }
                saleRate = 0;
            }
        }

        return saleRate;
    }

    public static double getSaleRate(double mrp, double rate, double conversionRate, double freeQty, String isIncludeFreeQtyInSaleRate, List<SaleRateRule_Mdl> lstSaleRateRule, String itemCategoryProffitMargine, String saleRateCalculateOn, String isMarkupApplicable) {
        return getSaleRate(mrp, rate, conversionRate, freeQty, isIncludeFreeQtyInSaleRate, lstSaleRateRule, DmLocaleCountry.INDIA, itemCategoryProffitMargine, saleRateCalculateOn, isMarkupApplicable);
    }

    public static String getDateFromMonthYear(String manufExpDate, boolean isLastDateRequired) {
        String date = "";
        if (!DmShared.xNull(manufExpDate).trim().equalsIgnoreCase("")) {
            if (DmShared.xNull(manufExpDate).trim().charAt(0) == '.' || DmShared.xNull(manufExpDate).trim().charAt(0) == '/'
                    || DmShared.xNull(manufExpDate).trim().charAt(0) == '-') {
                manufExpDate = "01" + DmShared.xNull(manufExpDate).trim();
            } else {
                isLastDateRequired = false;
            }
        }
        date = DmClient.returnDate(manufExpDate);
        if (isLastDateRequired) {
            try {

                Date lastDay = (Date) DmShared.convertDate(date).clone();
                CalendarUtil.addMonthsToDate(lastDay, 1);
                CalendarUtil.setToFirstDayOfMonth(lastDay);
                CalendarUtil.addDaysToDate(lastDay, -1);

                date = DmShared.dNull(lastDay);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    public static boolean isFutureDate(String date) {
        boolean flag = false;
        if (!DmShared.dNull(date).trim().equals("")) {
            if (DmShared.convertDate(date).getTime() > DmShared.convertDate(DmClient.getLoginDate()).getTime()) {
                flag = true;
            }
        }
        return flag;
    }

    public static int getOHLFocusIndex(List<OverHeadLine_Mdl> overHeadList) {
        for (OverHeadLine_Mdl ohlMdl : overHeadList) {
            if (ohlMdl != null) {
                if (DmShared.vNull(ohlMdl.getAtRateEditable()) == 1
                        || DmShared.vNull(ohlMdl.getAmountEditable()) == 1) {
                    return overHeadList.indexOf(ohlMdl);
                }
            }
        }
        return 0;
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
                    throw new DmException(0, "You can apply discount upto " + DmClient.vNull(userAllowDiscountPercent, "0.00") + "% on " + serviceName);
                }
            }
        }
    }

    public static String getRoundedAmt(double amount, String roundOffType) {

        if (DmShared.xNull(roundOffType).trim().equalsIgnoreCase("D")) {
            DmClient.vNull(amount, "0.0000000");

        } else {
            if (DmShared.xNull(roundOffType).trim().equalsIgnoreCase("Y")) {
                amount = DmShared.vNull(DmClient.vNull(amount, "0"));

            } else if (DmShared.xNull(roundOffType).trim().equalsIgnoreCase("H")) {
                amount = Math.ceil(amount);

            } else if (DmShared.xNull(roundOffType).trim().equalsIgnoreCase("L")) {
                amount = Math.floor(amount);
            }

            DmClient.vNull(amount, "0.00");
        }
        return DmShared.vNull(amount).toString();
    }

    public static TaxSummary_Mdl verifyOHFromItemList(List<List<OverHeadLine_Mdl>> ohlLists) {

        double sumTaxableAmt = 0d, sumtotalAmt = 0d;

        List<TaxSummary_Mdl> tsmList = new ArrayList<>();
        TaxSummary_Mdl tsmNode;
        TaxSummary_Mdl mrTemp = new TaxSummary_Mdl();
        boolean isNewTaxRate = false;
        double rate, cgstAmt, sgstAmt, ugstAmt, igstAmt, taxableAmt, totalAmt;

        for (List<OverHeadLine_Mdl> ohlList : ohlLists) {

            isNewTaxRate = true;
            rate = 0;
            cgstAmt = 0;
            sgstAmt = 0;
            ugstAmt = 0;
            igstAmt = 0;
            taxableAmt = 0;
            totalAmt = 0;

            for (OverHeadLine_Mdl ohl : ohlList) {
                if (ohl == null) {
                    continue;
                }
                switch (DmShared.xNull(ohl.getOhManualCode()).trim().toUpperCase()) {
                    case "CGST":
                        rate += DmShared.vNull(ohl.getAtRate());
                        cgstAmt += DmShared.vNull(ohl.getAmount());
                        totalAmt += DmShared.vNull(ohl.getAmount());
                        break;
                    case "SGST":
                        rate += DmShared.vNull(ohl.getAtRate());
                        sgstAmt += DmShared.vNull(ohl.getAmount());
                        totalAmt += DmShared.vNull(ohl.getAmount());
                        break;
                    case "UGST":
                        rate += DmShared.vNull(ohl.getAtRate());
                        ugstAmt += DmShared.vNull(ohl.getAmount());
                        totalAmt += DmShared.vNull(ohl.getAmount());
                        break;
                    case "IGST":
                        rate += DmShared.vNull(ohl.getAtRate());
                        igstAmt += DmShared.vNull(ohl.getAmount());
                        totalAmt += DmShared.vNull(ohl.getAmount());
                        break;
                    case "TV":
                        taxableAmt += DmShared.vNull(ohl.getAmount());
                        break;
                }
            }

            for (TaxSummary_Mdl tsm : tsmList) {
                if (DmShared.vNull(tsm.getRate()).doubleValue() == DmShared.vNull(rate).doubleValue()) {
                    tsm.setCgstAmt(DmClient.vNull(DmShared.vNull(tsm.getCgstAmt()) + cgstAmt, "#.#######"));
                    tsm.setSgstAmt(DmClient.vNull(DmShared.vNull(tsm.getSgstAmt()) + sgstAmt, "#.#######"));
                    tsm.setUgstAmt(DmClient.vNull(DmShared.vNull(tsm.getUgstAmt()) + ugstAmt, "#.#######"));
                    tsm.setSgstOrUgstAmt(DmClient.vNull(DmShared.vNull(tsm.getSgstOrUgstAmt()) + sgstAmt + ugstAmt, "#.#######"));
                    tsm.setIgstAmt(DmClient.vNull(DmShared.vNull(tsm.getIgstAmt()) + igstAmt, "#.#######"));
                    tsm.setTaxableAmt(DmClient.vNull(DmShared.vNull(tsm.getTaxableAmt()) + taxableAmt, "#.#######"));
                    tsm.setTotalAmt(DmClient.vNull(DmShared.vNull(tsm.getTotalAmt()) + totalAmt, "#.#######"));
                    isNewTaxRate = false;
                    break;
                }
            }

            if (isNewTaxRate && DmShared.vNull(rate).doubleValue() != 0) {
                tsmNode = new TaxSummary_Mdl();
                tsmNode.setRate(DmClient.vNull(rate, "#.#######"));
                tsmNode.setCgstAmt(DmClient.vNull(cgstAmt, "#.#######"));
                tsmNode.setSgstAmt(DmClient.vNull(sgstAmt, "#.#######"));
                tsmNode.setUgstAmt(DmClient.vNull(ugstAmt, "#.#######"));
                tsmNode.setSgstOrUgstAmt(DmClient.vNull(sgstAmt + ugstAmt, "#.#######"));
                tsmNode.setIgstAmt(DmClient.vNull(igstAmt, "#.#######"));
                tsmNode.setTaxableAmt(DmClient.vNull(taxableAmt, "#.#######"));
                tsmNode.setTotalAmt(DmClient.vNull(totalAmt, "#.#######"));
                tsmList.add(tsmNode);
            }

        }

        for (TaxSummary_Mdl node : tsmList) {

            sumTaxableAmt += DmShared.vNull(node.getTaxableAmt());
            sumtotalAmt += DmShared.vNull(node.getTotalAmt());
        }

        mrTemp.setTaxableAmt(DmClient.vNull(sumTaxableAmt, "0.00"));
        mrTemp.setTotalAmt(DmClient.vNull(sumtotalAmt, "0.00"));

        return mrTemp;
    }

    public static String addDaysToDate(String date, String days) {
        String finalDate = "";
        if (!DmShared.dNull(date).trim().equalsIgnoreCase("")) {
            try {
                Date tempDate = DmShared.convertDate(date);
                CalendarUtil.addDaysToDate(tempDate, DmShared.iNull(days).intValue());
                finalDate = DmShared.dNull(tempDate);
            } catch (Exception e) {
            }
        }
        return finalDate;
    }

}
