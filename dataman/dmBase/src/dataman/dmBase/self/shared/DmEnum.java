package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import java.io.Serializable;

public class DmEnum {

    public enum TDS_TCS_Type implements Serializable, IsSerializable {
        TDS(1, "TDS"),
        TCS(2, "TCS");

        private final Integer code;
        private final String name;

        public String getCode() {
            return code.toString();
        }

        public String getName() {
            return name;
        }

        TDS_TCS_Type(Integer code, String name) {

            this.code = code;
            this.name = name;

        }

        public static TDS_TCS_Type getViaCode(String code) {
            TDS_TCS_Type returnValue = null;
            for (TDS_TCS_Type tf : TDS_TCS_Type.values()) {
                if (code.equalsIgnoreCase(tf.getCode())) {
                    returnValue = tf;
                    break;

                }
            }

            return returnValue;
        }

        public static TDS_TCS_Type getViaName(String name) {
            TDS_TCS_Type returnValue = null;
            for (TDS_TCS_Type tf : TDS_TCS_Type.values()) {
                if (name.equalsIgnoreCase(tf.getName())) {
                    returnValue = tf;
                    break;
                }
            }
            return returnValue;
        }
    }

    public enum DmLocaleCountry {
        //Link to get alpha code of country
        //https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes
        INDIA("IND", "IND", "India", "Indian Rupee", "RS", "Rupee", "Paisa", true, new Integer[]{10}),
        CAMBODIA("KHM", "KHM", "Cambodia", "Cambodian Riel", "KHR", "Riel", "KAK", false, new Integer[]{8, 9, 10}),
        USA("USA", "USA", "United States of America", "US Dollar", "USD", "Dollar", "Cent", false, new Integer[]{10}),
        CAMBODIA_USA("KHM-USA", "USA", "United States of America", "US Dollar", "USD", "Dollar", "Cent", false, new Integer[]{8, 9, 10});

        private String dmCode, isoCode3, countryName, currencyName, currencyShortName,
                majorCurrency, minorCurrency;
        private Integer[] mobileDigit;
        private boolean isLakhFormat = false;

        private DmLocaleCountry(String dmCode, String isoCode3, String countryName,
                String currencyName, String currencyShortName, String majorCurrency,
                String minorCurrency, boolean isLakhFormat, Integer[] mobileDigit) {
            this.dmCode = dmCode;
            this.isoCode3 = isoCode3;
            this.countryName = countryName;
            this.currencyName = currencyName;
            this.currencyShortName = currencyShortName;
            this.majorCurrency = majorCurrency;
            this.minorCurrency = minorCurrency;
            this.isLakhFormat = isLakhFormat;
            this.mobileDigit = mobileDigit;
        }

        public String getDmCode() {
            return dmCode;
        }

        public String getISOCode3() {
            return isoCode3;
        }

        public String getCountryName() {
            return countryName;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public String getCurrencyShortName() {
            return currencyShortName;
        }

        public String getMajorCurrency() {
            return majorCurrency;
        }

        public String getMinorCurrency() {
            return minorCurrency;
        }

        public boolean isLakhFormat() {
            return isLakhFormat;
        }

        public Integer[] getMobileDigit() {
            return mobileDigit;
        }

        public static DmLocaleCountry getLocaleCountry111(String dmCode) {
            DmLocaleCountry rtnValue = USA;
//            dmCode = DmBaseShared.xNull(dmCode).trim();
//
//            if (dmCode.equalsIgnoreCase(INDIA.getDmCode())) {
//                rtnValue = INDIA;
//            } else if (dmCode.equalsIgnoreCase(CAMBODIA.getDmCode())) {
//                rtnValue = CAMBODIA;
//            } else if (dmCode.equalsIgnoreCase(CAMBODIA_USA.getDmCode())) {
//                rtnValue = CAMBODIA_USA;
//            }

            return rtnValue;
        }

        public static DmLocaleCountry getLocaleViaDmCode(String dmCode) {
            DmLocaleCountry rtnValue = USA;
            dmCode = DmBaseShared.xNull(dmCode).trim();

            if (dmCode.equalsIgnoreCase(INDIA.getDmCode())) {
                rtnValue = INDIA;
            } else if (dmCode.equalsIgnoreCase(CAMBODIA.getDmCode())) {
                rtnValue = CAMBODIA;
            } else if (dmCode.equalsIgnoreCase(CAMBODIA_USA.getDmCode())) {
                rtnValue = CAMBODIA_USA;
            }

            return rtnValue;
        }
    }

    public enum AlphabetCase implements Serializable, IsSerializable {

        UPPER("U", "UPPER"), LOWER("L", "lower"), PROPER_CASE("P", "Proper Case"), SENTENCE("S", "Sentence");

        private final String value;
        private final String shortName;

        public String getValue() {
            return value;
        }

        public String getShortName() {
            return shortName;
        }

        AlphabetCase(String manualCode, String text) {
            this.value = text;
            this.shortName = manualCode;
        }

        public static AlphabetCase getViaShortName(String text) {
            AlphabetCase rtnValue = null;
            if (text.equalsIgnoreCase(UPPER.getShortName())) {
                rtnValue = UPPER;
            } else if (text.equalsIgnoreCase(LOWER.getShortName())) {
                rtnValue = LOWER;
            } else if (text.equalsIgnoreCase(PROPER_CASE.getShortName())) {
                rtnValue = PROPER_CASE;
            } else if (text.equalsIgnoreCase(SENTENCE.getShortName())) {
                rtnValue = SENTENCE;
            }
            return rtnValue;
        }

        public static AlphabetCase getViaName(String text) {
            AlphabetCase rtnValue = null;
            if (text.equalsIgnoreCase(UPPER.getValue())) {
                rtnValue = UPPER;
            } else if (text.equalsIgnoreCase(LOWER.getValue())) {
                rtnValue = LOWER;
            } else if (text.equalsIgnoreCase(PROPER_CASE.getValue())) {
                rtnValue = PROPER_CASE;
            } else if (text.equalsIgnoreCase(SENTENCE.getValue())) {
                rtnValue = SENTENCE;
            }
            return rtnValue;
        }
    }

    public enum ScreenDisplay implements Serializable, IsSerializable {

        DESKTOP(1, "DESKTOP"), TABLET(2, "TABLET"), MOBILE(3, "MOBILE");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        ScreenDisplay(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static ScreenDisplay getKeyOf(int id) {
            ScreenDisplay rtnValue = null;
            if (id == DESKTOP.getId()) {
                rtnValue = DESKTOP;
            } else if (id == TABLET.getId()) {
                rtnValue = TABLET;
            } else if (id == MOBILE.getId()) {
                rtnValue = MOBILE;
            }
            return rtnValue;
        }

        public static ScreenDisplay getValueOf(String text) {
            ScreenDisplay rtnValue = null;

            if (text.equalsIgnoreCase(DESKTOP.getValue())) {
                rtnValue = DESKTOP;
            } else if (text.equalsIgnoreCase(TABLET.getValue())) {
                rtnValue = TABLET;
            } else if (text.equalsIgnoreCase(MOBILE.getValue())) {
                rtnValue = MOBILE;
            }
            return rtnValue;
        }
    }

    public enum DmPrinterImageType implements Serializable, IsSerializable {
        RGB("RGB", 0), BYTE("BYTE", 1);

        private String printerImageTypeName;
        private int printerImageTypeCode;

        public static DmPrinterImageType getPrinterImageType(String printerImageTypeName) {
            DmPrinterImageType aValue = null;
            if (DmPrinterImageType.BYTE.toString().equalsIgnoreCase(printerImageTypeName)) {
                aValue = DmPrinterImageType.BYTE;
            } else {
                aValue = DmPrinterImageType.RGB;
            }
            return aValue;
        }

        public static DmPrinterImageType getPrinterImageType(int printerImageTypeCode) {
            DmPrinterImageType aValue = null;
            if (DmPrinterImageType.BYTE.toInt() == printerImageTypeCode) {
                aValue = DmPrinterImageType.BYTE;
            } else {
                aValue = DmPrinterImageType.RGB;
            }
            return aValue;
        }

        @Override
        public String toString() {
            return this.printerImageTypeName;
        }

        public int toInt() {
            return this.printerImageTypeCode;
        }

        private DmPrinterImageType(String printerImageTypeName, int printerImageTypeCode) {
            this.printerImageTypeName = printerImageTypeName;
            this.printerImageTypeCode = printerImageTypeCode;
        }

    }

    public enum DataBackUp implements Serializable, IsSerializable {

        DB_COMPANY(1, "DB-COMPANY"), DB_TRANSACTION(2, "DB-TRANSACTION"), DB_MONGO(3, "DB-MONGO"),
        FS_DIR(4, "FS-DIR");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        DataBackUp(int id, String text) {
            this.id = id;
            this.value = text;
        }

        public static DataBackUp getKeyOf(int id) {
            DataBackUp rtnValue = null;
            if (id == DB_COMPANY.getId()) {
                rtnValue = DB_COMPANY;
            } else if (id == DB_TRANSACTION.getId()) {
                rtnValue = DB_TRANSACTION;
            } else if (id == DB_MONGO.getId()) {
                rtnValue = DB_MONGO;
            } else if (id == FS_DIR.getId()) {
                rtnValue = FS_DIR;
            }
            return rtnValue;
        }

        public static DataBackUp getValueOf(String text) {
            DataBackUp rtnValue = null;

            if (text.equalsIgnoreCase(DB_COMPANY.getValue())) {
                rtnValue = DB_COMPANY;
            } else if (text.equalsIgnoreCase(DB_TRANSACTION.getValue())) {
                rtnValue = DB_TRANSACTION;
            } else if (text.equalsIgnoreCase(DB_MONGO.getValue())) {
                rtnValue = DB_MONGO;
            } else if (text.equalsIgnoreCase(FS_DIR.getValue())) {
                rtnValue = FS_DIR;
            }
            return rtnValue;
        }
    }

    public enum ToleranceNature implements Serializable, IsSerializable {

        Add("Add(+)", 1),
        Less("Less(-)", 0);

        private String enumName;
        private int enumValue;

        @Override
        public String toString() {
            return this.enumName;
        }

        public int toInt() {
            return enumValue;
        }

        public static ToleranceNature valueOf(int valueId) {
            ToleranceNature required = null;
            if (ToleranceNature.Add.toInt() == valueId) {
                required = Add;
            } else if (ToleranceNature.Less.toInt() == valueId) {
                required = Less;
            }
            return required;
        }

        private ToleranceNature(String enumName, int enumValue) {
            this.enumValue = enumValue;
            this.enumName = enumName.trim();
        }
    }

    public enum ItsBasedOn implements Serializable, IsSerializable {

        Fixed("Fixed", 0),
        Rate("Rate", 1);

        private String enumName;
        private int enumValue;

        public String toString() {
            return this.enumName;
        }

        public int toInt() {
            return enumValue;
        }

        private ItsBasedOn(String enumName, int enumValue) {
            this.enumValue = enumValue;
            this.enumName = enumName.trim();
        }

        public static ItsBasedOn getValueOf(String chartId) {
            ItsBasedOn required = null;
            for (ItsBasedOn itsBasedOn : ItsBasedOn.values()) {
                if (!DmBaseShared.xNull(chartId).equalsIgnoreCase("") && DmBaseShared.xNull(itsBasedOn.toString()).equalsIgnoreCase(chartId)) {
                    required = itsBasedOn;
                    break;
                }
            }
            return required;
        }

        public static ItsBasedOn getValueOf(int enumValue) {
            ItsBasedOn required = null;

            switch (DmBaseShared.iNull(enumValue).intValue()) {
                case 0:
                    required = Fixed;
                    break;
                case 1:
                    required = Rate;
                    break;
                default:
                    break;
            }

            return required;
        }

    }

    public enum GSTNature implements Serializable, IsSerializable {

        GST_ITEM("G", "GST"), NON_GST_ITEM("N", "Non GST"), EXEMPTED_ITEM("E", "Exempted"),
        NILL_RATED("L", "Nill Rated");

        private final String id;
        private final String value;

        public String getId() {
            return this.id;
        }

        public String getValue() {
            return this.value;
        }

        GSTNature(String id, String text) {
            this.id = id;
            this.value = text;
        }

        public static GSTNature getValueOf(String id) {
            GSTNature returnGST = null;
            if (id.equalsIgnoreCase(GST_ITEM.getId())) {
                returnGST = GST_ITEM;
            } else if (id.equalsIgnoreCase(NON_GST_ITEM.getId())) {
                returnGST = NON_GST_ITEM;
            } else if (id.equalsIgnoreCase(EXEMPTED_ITEM.getId())) {
                returnGST = EXEMPTED_ITEM;
            } else if (id.equalsIgnoreCase(NILL_RATED.getId())) {
                returnGST = NILL_RATED;
            }
            return returnGST;
        }

        public static GSTNature getKeyOf(String text) {
            GSTNature returnGST = null;

            if (text.equals(GST_ITEM.getValue())) {
                returnGST = GST_ITEM;
            } else if (text.equals(NON_GST_ITEM.getValue())) {
                returnGST = NON_GST_ITEM;
            } else if (text.equals(EXEMPTED_ITEM.getValue())) {
                returnGST = EXEMPTED_ITEM;
            } else if (text.equals(NILL_RATED.getValue())) {
                returnGST = NILL_RATED;
            }
            return returnGST;
        }
    }

    public enum GstDocumentType implements Serializable, IsSerializable {

        REGULAR_INVOICE("RI", "Regular Invoice"),
        SEZ_SUPPLIES_WITH_PAYMENT("SEWP", "SEZ Supplies With Payment"),
        SEZ_SUPPLIES_WITHOUT_PAYMENT("SEWOP", "SEZ Supplies Without Payment"),
        DEEMED_EXPORT("DE", "Deemed Export"),
        DEBIT_NOTE("D", "Debit Note"),
        CREDIT_NOTE("C", "Credit Note"),
        REFUND_VOUCHER("R", "Refund Voucher"),
        EXPORT_WITH_PAYMENT_OF_TAX("WPAY", "Export With Payment Of Tax"),
        EXPORT_WITHOUT_PAYMENT_OF_TAX("WOPAY", "Export Without Payment Of Tax"),
        ADVANCE_RECEIVED("AR", "Advance Received"),
        ADVANCE_ADJUSTED("AA", "Advance Adjusted");

        private final String value;
        private final String shortName;

        public String getValue() {
            return value;
        }

        public String getShortName() {
            return shortName;
        }

        GstDocumentType(String manualCode, String text) {
            this.value = text;
            this.shortName = manualCode;
        }

        public static GstDocumentType getViaShortName(String text) {
            GstDocumentType rtnValue = null;

            if (text.equalsIgnoreCase(REGULAR_INVOICE.getShortName())) {
                rtnValue = REGULAR_INVOICE;
            } else if (text.equalsIgnoreCase(SEZ_SUPPLIES_WITH_PAYMENT.getShortName())) {
                rtnValue = SEZ_SUPPLIES_WITH_PAYMENT;
            } else if (text.equalsIgnoreCase(SEZ_SUPPLIES_WITHOUT_PAYMENT.getShortName())) {
                rtnValue = SEZ_SUPPLIES_WITHOUT_PAYMENT;
            } else if (text.equalsIgnoreCase(DEEMED_EXPORT.getShortName())) {
                rtnValue = DEEMED_EXPORT;
            } else if (text.equalsIgnoreCase(DEBIT_NOTE.getShortName())) {
                rtnValue = DEBIT_NOTE;
            } else if (text.equalsIgnoreCase(CREDIT_NOTE.getShortName())) {
                rtnValue = CREDIT_NOTE;
            } else if (text.equalsIgnoreCase(REFUND_VOUCHER.getShortName())) {
                rtnValue = REFUND_VOUCHER;
            } else if (text.equalsIgnoreCase(EXPORT_WITH_PAYMENT_OF_TAX.getShortName())) {
                rtnValue = EXPORT_WITH_PAYMENT_OF_TAX;
            } else if (text.equalsIgnoreCase(EXPORT_WITHOUT_PAYMENT_OF_TAX.getShortName())) {
                rtnValue = EXPORT_WITHOUT_PAYMENT_OF_TAX;
            } else if (text.equalsIgnoreCase(ADVANCE_RECEIVED.getShortName())) {
                rtnValue = ADVANCE_RECEIVED;
            } else if (text.equalsIgnoreCase(ADVANCE_ADJUSTED.getShortName())) {
                rtnValue = ADVANCE_ADJUSTED;
            }
            return rtnValue;
        }

        public static GstDocumentType getViaName(String text) {
            GstDocumentType rtnValue = null;
            if (text.equalsIgnoreCase(REGULAR_INVOICE.getValue())) {
                rtnValue = REGULAR_INVOICE;
            } else if (text.equalsIgnoreCase(SEZ_SUPPLIES_WITH_PAYMENT.getValue())) {
                rtnValue = SEZ_SUPPLIES_WITH_PAYMENT;
            } else if (text.equalsIgnoreCase(SEZ_SUPPLIES_WITHOUT_PAYMENT.getValue())) {
                rtnValue = SEZ_SUPPLIES_WITHOUT_PAYMENT;
            } else if (text.equalsIgnoreCase(DEEMED_EXPORT.getValue())) {
                rtnValue = DEEMED_EXPORT;
            } else if (text.equalsIgnoreCase(DEBIT_NOTE.getValue())) {
                rtnValue = DEBIT_NOTE;
            } else if (text.equalsIgnoreCase(CREDIT_NOTE.getValue())) {
                rtnValue = CREDIT_NOTE;
            } else if (text.equalsIgnoreCase(REFUND_VOUCHER.getValue())) {
                rtnValue = REFUND_VOUCHER;
            } else if (text.equalsIgnoreCase(EXPORT_WITH_PAYMENT_OF_TAX.getValue())) {
                rtnValue = EXPORT_WITH_PAYMENT_OF_TAX;
            } else if (text.equalsIgnoreCase(EXPORT_WITHOUT_PAYMENT_OF_TAX.getValue())) {
                rtnValue = EXPORT_WITHOUT_PAYMENT_OF_TAX;
            } else if (text.equalsIgnoreCase(ADVANCE_RECEIVED.getValue())) {
                rtnValue = ADVANCE_RECEIVED;
            } else if (text.equalsIgnoreCase(ADVANCE_ADJUSTED.getValue())) {
                rtnValue = ADVANCE_ADJUSTED;
            }
            return rtnValue;
        }

    }

    public enum GstDocumentNature implements Serializable, IsSerializable {

        INVOICES_FOR_OUTWARD_SUPPLY("IOSR", "Invoices For Outward Supply"),
        INVOICES_FOR_INWARD_SUPPLY_FROM_UNREGISTERED_PERSON("IISUR", "Invoices For Inward Supply From Unregistered Person"),
        REVISED_INVOICE("RI", "Revised Invoice"),
        DEBIT_NOTE("DN", "Debit Note"),
        CREDIT_NOTE("CN", "Credit Note"),
        RECEIPT_VOUCHER("RCT", "Receipt Voucher"),
        PAYMENT_VOUCHER("PMT", "Payment Voucher"),
        REFUND_VOUCHER("RF", "Refund Voucher"),
        DELIVERY_CHALLAN_FOR_JOB_WORK("DCJW", "Delivery Challan For Job Work"),
        DELIVERY_CHALLAN_FOR_SUPPLY_ON_APPROVAL("DCSA", "Delivery Challan For Supply On Approval"),
        DELIVERY_CHALLAN_IN_CASE_OF_LIQUID_GAS("DCLG", "Delivery Challan In Case Of Liquid Gas"),
        DELIVERY_CHALLAN_IN_CASES_OTHER_THAN_BY_WAY_OF_SUPPLY("DCOWS", "Delivery Challan In Cases Other Than By Way Of Supply");

        private final String value;
        private final String shortName;

        public String getValue() {
            return value;
        }

        public String getShortName() {
            return shortName;
        }

        GstDocumentNature(String manualCode, String text) {
            this.value = text;
            this.shortName = manualCode;
        }

        public static GstDocumentNature getViaShortName(String text) {
            GstDocumentNature rtnValue = null;

            if (text.equalsIgnoreCase(INVOICES_FOR_OUTWARD_SUPPLY.getShortName())) {
                rtnValue = INVOICES_FOR_OUTWARD_SUPPLY;
            } else if (text.equalsIgnoreCase(INVOICES_FOR_INWARD_SUPPLY_FROM_UNREGISTERED_PERSON.getShortName())) {
                rtnValue = INVOICES_FOR_INWARD_SUPPLY_FROM_UNREGISTERED_PERSON;
            } else if (text.equalsIgnoreCase(REVISED_INVOICE.getShortName())) {
                rtnValue = REVISED_INVOICE;
            } else if (text.equalsIgnoreCase(DEBIT_NOTE.getShortName())) {
                rtnValue = DEBIT_NOTE;
            } else if (text.equalsIgnoreCase(CREDIT_NOTE.getShortName())) {
                rtnValue = CREDIT_NOTE;
            } else if (text.equalsIgnoreCase(RECEIPT_VOUCHER.getShortName())) {
                rtnValue = RECEIPT_VOUCHER;
            } else if (text.equalsIgnoreCase(PAYMENT_VOUCHER.getShortName())) {
                rtnValue = PAYMENT_VOUCHER;
            } else if (text.equalsIgnoreCase(REFUND_VOUCHER.getShortName())) {
                rtnValue = REFUND_VOUCHER;
            } else if (text.equalsIgnoreCase(DELIVERY_CHALLAN_FOR_JOB_WORK.getShortName())) {
                rtnValue = DELIVERY_CHALLAN_FOR_JOB_WORK;
            } else if (text.equalsIgnoreCase(DELIVERY_CHALLAN_FOR_SUPPLY_ON_APPROVAL.getShortName())) {
                rtnValue = DELIVERY_CHALLAN_FOR_SUPPLY_ON_APPROVAL;
            } else if (text.equalsIgnoreCase(DELIVERY_CHALLAN_IN_CASE_OF_LIQUID_GAS.getShortName())) {
                rtnValue = DELIVERY_CHALLAN_IN_CASE_OF_LIQUID_GAS;
            } else if (text.equalsIgnoreCase(DELIVERY_CHALLAN_IN_CASES_OTHER_THAN_BY_WAY_OF_SUPPLY.getShortName())) {
                rtnValue = DELIVERY_CHALLAN_IN_CASES_OTHER_THAN_BY_WAY_OF_SUPPLY;
            }
            return rtnValue;
        }

        public static GstDocumentNature getViaText(String text) {
            GstDocumentNature rtnValue = null;

            if (text.equalsIgnoreCase(INVOICES_FOR_OUTWARD_SUPPLY.getValue())) {
                rtnValue = INVOICES_FOR_OUTWARD_SUPPLY;
            } else if (text.equalsIgnoreCase(INVOICES_FOR_INWARD_SUPPLY_FROM_UNREGISTERED_PERSON.getValue())) {
                rtnValue = INVOICES_FOR_INWARD_SUPPLY_FROM_UNREGISTERED_PERSON;
            } else if (text.equalsIgnoreCase(REVISED_INVOICE.getValue())) {
                rtnValue = REVISED_INVOICE;
            } else if (text.equalsIgnoreCase(DEBIT_NOTE.getValue())) {
                rtnValue = DEBIT_NOTE;
            } else if (text.equalsIgnoreCase(CREDIT_NOTE.getValue())) {
                rtnValue = CREDIT_NOTE;
            } else if (text.equalsIgnoreCase(RECEIPT_VOUCHER.getValue())) {
                rtnValue = RECEIPT_VOUCHER;
            } else if (text.equalsIgnoreCase(PAYMENT_VOUCHER.getValue())) {
                rtnValue = PAYMENT_VOUCHER;
            } else if (text.equalsIgnoreCase(REFUND_VOUCHER.getValue())) {
                rtnValue = REFUND_VOUCHER;
            } else if (text.equalsIgnoreCase(DELIVERY_CHALLAN_FOR_JOB_WORK.getValue())) {
                rtnValue = DELIVERY_CHALLAN_FOR_JOB_WORK;
            } else if (text.equalsIgnoreCase(DELIVERY_CHALLAN_FOR_SUPPLY_ON_APPROVAL.getValue())) {
                rtnValue = DELIVERY_CHALLAN_FOR_SUPPLY_ON_APPROVAL;
            } else if (text.equalsIgnoreCase(DELIVERY_CHALLAN_IN_CASE_OF_LIQUID_GAS.getValue())) {
                rtnValue = DELIVERY_CHALLAN_IN_CASE_OF_LIQUID_GAS;
            } else if (text.equalsIgnoreCase(DELIVERY_CHALLAN_IN_CASES_OTHER_THAN_BY_WAY_OF_SUPPLY.getValue())) {
                rtnValue = DELIVERY_CHALLAN_IN_CASES_OTHER_THAN_BY_WAY_OF_SUPPLY;
            }
            return rtnValue;
        }
    }

    public enum DmPartyType implements Serializable, IsSerializable {

        Registered("R", "Registered"), Unregistered("U", "Unregistered"), Composite("C", "Composite");

        private final String id;
        private final String value;

        public String getId() {
            return this.id;
        }

        public String getValue() {
            return this.value;
        }

        DmPartyType(String id, String text) {
            this.id = id;
            this.value = text;
        }

        public static DmPartyType getValueOf(String id) {

            DmPartyType returnValue = null;
            if (id.equalsIgnoreCase(Registered.getId())) {
                returnValue = Registered;
            } else if (id.equalsIgnoreCase(Unregistered.getId())) {
                returnValue = Unregistered;
            } else if (id.equalsIgnoreCase(Composite.getId())) {
                returnValue = Composite;
            }
            return returnValue;
        }

        public static DmPartyType getKeyOf(String text) {
            DmPartyType returnValue = null;

            if (text.equalsIgnoreCase(Registered.getValue())) {
                returnValue = Registered;
            } else if (text.equalsIgnoreCase(Unregistered.getValue())) {
                returnValue = Unregistered;
            } else if (text.equalsIgnoreCase(Composite.getValue())) {
                returnValue = Composite;
            }
            return returnValue;
        }
    }

    public enum DmLocation implements Serializable, IsSerializable {
        IntraState("Intra State", 1),
        InterState("Inter State", 2),
        UnionTerritory("Union Territory", 3),
        International("International", 4);

        private final int enumValue;
        private final String enumName;

        public String getValue() {
            return this.enumName;
        }

        public int getId() {
            return this.enumValue;
        }

        public static DmLocation getValueFrom(String value) {
            DmLocation location = null;
            if (DmLocation.InterState.getValue().equalsIgnoreCase(value)) {
                location = DmLocation.InterState;
            } else if (DmLocation.IntraState.getValue().equalsIgnoreCase(value)) {
                location = DmLocation.IntraState;
            } else if (DmLocation.UnionTerritory.getValue().equalsIgnoreCase(value)) {
                location = DmLocation.UnionTerritory;
            } else if (DmLocation.International.getValue().equalsIgnoreCase(value)) {
                location = DmLocation.International;
            }
            return location;
        }

        public static DmLocation getValueFrom(int id) {
            DmLocation location = null;

            if (DmLocation.InterState.getId() == id) {
                location = DmLocation.InterState;
            } else if (DmLocation.IntraState.getId() == id) {
                location = DmLocation.IntraState;
            } else if (DmLocation.UnionTerritory.getId() == id) {
                location = DmLocation.UnionTerritory;
            } else if (DmLocation.International.getId() == id) {
                location = DmLocation.International;
            }

            return location;
        }

        private DmLocation(String enumName, int enumValue) {
            this.enumName = enumName;
            this.enumValue = enumValue;
        }
    }

    public enum DmChartType implements Serializable, IsSerializable {
        ColumnRenderer("Column Renderer", 0),
        BarRenderer("Bar Renderer", 1),
        Gauge("Gauge", 2),
        Line("Line", 3),
        Pie("Pie", 4);

        private final String enumName;
        private final int enumValue;

        public String getEnumName() {
            return this.enumName;
        }

        public int getEnumValue() {
            return this.enumValue;
        }

        public static DmChartType getValueOf(String chartId) {
            DmChartType required = null;
            for (DmChartType chartType : DmChartType.values()) {
                if (!DmBaseShared.xNull(chartId).equalsIgnoreCase("") && DmBaseShared.xNull(chartType.getEnumName()).equalsIgnoreCase(chartId)) {
                    required = chartType;
                    break;
                }
            }
            return required;
        }

        public static DmChartType getValueOf(int enumValue) {
            DmChartType required = null;

            switch (DmBaseShared.iNull(enumValue).intValue()) {
                case 0:
                    required = ColumnRenderer;
                    break;
                case 1:
                    required = BarRenderer;
                    break;
                case 2:
                    required = Gauge;
                    break;
                case 3:
                    required = Line;
                    break;
                case 4:
                    required = Pie;
                    break;
                default:
                    break;
            }

            return required;
        }

        private DmChartType(String enumName, int enumValue) {
            this.enumValue = enumValue;
            this.enumName = enumName.trim();
        }
    }

    public enum DmUserValidationMode implements Serializable, IsSerializable {
        USERNAME("User Name", 0),
        EMAIL("eMail", 1),
        MOBILE("Mobile", 2),
        TokenKey("Token Key", 3);

        private final int validationMode;
        private final String validationModeName;

        public String getValidationModeName() {
            return this.validationModeName;
        }

        public int getValidationMode() {
            return this.validationMode;
        }

        public static DmUserValidationMode getValueFrom(String validationModeName) {
            DmUserValidationMode userValidationMode = null;
            if (DmUserValidationMode.USERNAME.getValidationModeName().equalsIgnoreCase(validationModeName)) {
                userValidationMode = DmUserValidationMode.USERNAME;
            } else if (DmUserValidationMode.EMAIL.getValidationModeName().equalsIgnoreCase(validationModeName)) {
                userValidationMode = DmUserValidationMode.EMAIL;
            } else if (DmUserValidationMode.TokenKey.getValidationModeName().equalsIgnoreCase(validationModeName)) {
                userValidationMode = DmUserValidationMode.TokenKey;
            }
            return userValidationMode;
        }

        public static DmUserValidationMode getValueFrom(int validationMode) {
            DmUserValidationMode userValidationMode = null;

            if (DmUserValidationMode.USERNAME.getValidationMode() == validationMode) {
                userValidationMode = DmUserValidationMode.USERNAME;
            } else if (DmUserValidationMode.EMAIL.getValidationMode() == validationMode) {
                userValidationMode = DmUserValidationMode.EMAIL;
            } else if (DmUserValidationMode.MOBILE.getValidationMode() == validationMode) {
                userValidationMode = DmUserValidationMode.MOBILE;
            } else if (DmUserValidationMode.TokenKey.getValidationMode() == validationMode) {
                userValidationMode = DmUserValidationMode.TokenKey;
            }

            return userValidationMode;
        }

        private DmUserValidationMode(String validationModeName, int validationMode) {
            this.validationModeName = validationModeName;
            this.validationMode = validationMode;
        }
    }

    public enum DmPosition implements Serializable, IsSerializable {

        NONE, CENTER, CENTER_VERTICAL, CENTER_HORIZONTAL
    }

    public enum PCSEnum implements Serializable, IsSerializable {

        Login, LOCK, DASHBOARD, PLT, Company, Site, WorkArea
    }

    public enum DmOperand implements Serializable, IsSerializable {

        AND, OR
    }

    public enum DmOperator implements Serializable, IsSerializable {

        BEGINSWITH, CONTAINS, EQUAL
    }

    public enum DmSortType implements Serializable, IsSerializable {

        ASC, DESC
    }

    public enum DmDataType implements Serializable, IsSerializable {
        STRING(0), STRINGLIST(1), DOUBLE(2), DATE(3), DATETIME(4), INTEGER(5), HELPSINGLE(6), LABEL(7), CHECKBOX(8), CUSTOM(9), TIMESTAMP(10), STRING_MULTILINE(11),
        HELPMULTI(12), HINDI(13);
        private final int optionValue;

        private DmDataType(int optionValue) {
            this.optionValue = optionValue;
        }

        public int toInt() {
            return optionValue;
        }

        public static String valueOf(int valueId) {
            String value = "";

            if (valueId == STRING.toInt()) {
                value = STRING.toString();
            } else if (valueId == STRINGLIST.toInt()) {
                value = STRINGLIST.toString();
            } else if (valueId == DOUBLE.toInt()) {
                value = DOUBLE.toString();
            } else if (valueId == DATE.toInt()) {
                value = DATE.toString();
            } else if (valueId == DATETIME.toInt()) {
                value = DATETIME.toString();
            } else if (valueId == INTEGER.toInt()) {
                value = INTEGER.toString();
            } else if (valueId == HELPSINGLE.toInt()) {
                value = HELPSINGLE.toString();
            } else if (valueId == LABEL.toInt()) {
                value = LABEL.toString();
            } else if (valueId == CHECKBOX.toInt()) {
                value = CHECKBOX.toString();
            } else if (valueId == CUSTOM.toInt()) {
                value = CUSTOM.toString();
            } else if (valueId == TIMESTAMP.toInt()) {
                value = TIMESTAMP.toString();
            } else if (valueId == STRING_MULTILINE.toInt()) {
                value = STRING_MULTILINE.toString();
            } else if (valueId == HELPMULTI.toInt()) {
                value = HELPMULTI.toString();
            } else if (valueId == HINDI.toInt()) {
                value = HINDI.toString();
            }
            return value.replace("_", " ");
        }
    }

    public enum SQLDataType implements Serializable, IsSerializable {

        VARCHAR, NVARCHAR, DATE, DATETIME, TINYINT, SMALLINT, INT, FLOAT, BIGINT, DECIMAL, VARCHARMAX, NVARCHARMAX
    }

    public enum SQLColumnType implements Serializable, IsSerializable {

        NONE, PRIMARY
    }

    public enum SQLIsNullable implements Serializable, IsSerializable {

        NOT_NULL, NULL
    }

    public enum RegionalLanguage implements Serializable, IsSerializable {

        HINDI("Hindi", "Mangal", "20px"),
        //        HINDI("Hindi", "Kruti Dev 010", "20px"),
        ENGLISH("English", "Arial", "20px"),
        BANGALI("Bangali", "AdarshaLipiNormal", "20px"),
        MARATHI("Marathi", "Shivaji01", "20px"),
        PUNJABI("Punjabi", "Asees", "20px");

        private String regionalName;
        private String fontName;
        private String cssFontSize;

        public String getRegionalName() {
            return regionalName;
        }

        public String getFontFamilyName() {
            return getRegionalName().toLowerCase();
        }

        public String getFontName() {
            return fontName;
        }

        public String getCssStyleName() {
            return getFontFamilyName() + "Font";
        }

        public String getCssFontSize() {
            return cssFontSize;
        }

        public static RegionalLanguage getValueFrom(String regionalName) {
            RegionalLanguage[] languages = RegionalLanguage.values();
            RegionalLanguage language = null;
            for (int i = 0; i < languages.length; i++) {
                if (languages[i].getRegionalName().equalsIgnoreCase(regionalName.trim())) {
                    language = languages[i];
                    break;
                }
            }
            return language;
        }

        public static RegionalLanguage getValueFromCssStyle(String cssStyleName) {
            RegionalLanguage[] languages = RegionalLanguage.values();
            RegionalLanguage language = null;
            for (int i = 0; i < languages.length; i++) {
                if (languages[i].getCssStyleName().equalsIgnoreCase(cssStyleName.trim())) {
                    language = languages[i];
                    break;
                }
            }
            return language;
        }

        private RegionalLanguage(String regName, String fontName, String fontSize) {
            this.regionalName = regName.trim();
            this.fontName = fontName.trim();
            this.cssFontSize = fontSize.trim();
        }

    }

    public enum DmStatus implements Serializable, IsSerializable {

        OK, CANCEL, SAVE, DELETE, INPROGRESS
    }

    public enum DmExportType implements Serializable, IsSerializable {

        PDF, EXCEL, WORD, CSV
    }

    public enum DmIDType implements Serializable, IsSerializable {

        Aadhaar(1), Voter_Id(2), BPL_No(3), Ration_Card(4);
        private final int enumValue;

        private DmIDType(int optionValue) {
            this.enumValue = optionValue;
        }

        public int toInt() {
            return enumValue;
        }

        public static String valueOf(int valueId) {
            String value = "";

            if (valueId == Aadhaar.toInt()) {
                value = Aadhaar.toString();
            } else if (valueId == BPL_No.toInt()) {
                value = BPL_No.toString();
            } else if (valueId == Ration_Card.toInt()) {
                value = Ration_Card.toString();
            } else if (valueId == Voter_Id.toInt()) {
                value = Voter_Id.toString();
            }

            return value.replace("_", " ");
        }
    }

    public enum DmReligion implements Serializable, IsSerializable {

        Hindu(1), Muslim(2), Sikh(3), Christian(4);
        private final int enumValue;

        private DmReligion(int optionValue) {
            this.enumValue = optionValue;
        }

        public int toInt() {
            return enumValue;
        }

        public static String valueOf(int valueId) {
            String value = "";

            if (valueId == Hindu.toInt()) {
                value = Hindu.toString();
            } else if (valueId == Muslim.toInt()) {
                value = Muslim.toString();
            } else if (valueId == Sikh.toInt()) {
                value = Sikh.toString();
            } else if (valueId == Christian.toInt()) {
                value = Christian.toString();
            }

            return value.replace("_", " ");
        }
    }

    public enum DmMonths implements Serializable, IsSerializable {

        JANUARY("January", 0),
        FEBRUARY("February", 1),
        MARCH("March", 2),
        APRIL("April", 3),
        MAY("May", 4),
        JUNE("June", 5),
        JULY("July", 6),
        AUGUST("August", 7),
        SEPTEMBER("September", 8),
        OCTOBER("October", 9),
        NOVEMBER("November", 10),
        DECEMBER("December", 11);

        private String monthName;
        private int monthCalInt;

        public String getMonthName() {
            return this.monthName;
        }

        public int getMonthCalendarValue() {
            return this.monthCalInt;
        }

        public static DmMonths getValueFrom(String monthName) {
            DmMonths month = null;
            if (DmMonths.JANUARY.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.JANUARY;
            } else if (DmMonths.FEBRUARY.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.FEBRUARY;
            } else if (DmMonths.MARCH.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.MARCH;
            } else if (DmMonths.APRIL.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.APRIL;
            } else if (DmMonths.MAY.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.MAY;
            } else if (DmMonths.JUNE.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.JUNE;
            } else if (DmMonths.JULY.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.JULY;
            } else if (DmMonths.AUGUST.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.AUGUST;
            } else if (DmMonths.SEPTEMBER.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.SEPTEMBER;
            } else if (DmMonths.OCTOBER.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.OCTOBER;
            } else if (DmMonths.NOVEMBER.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.NOVEMBER;
            } else if (DmMonths.DECEMBER.getMonthName().equalsIgnoreCase(monthName)) {
                month = DmMonths.DECEMBER;
            }
            return month;
        }

        public static DmMonths getValueFrom(int calInt) {
            DmMonths month = null;
            if (DmMonths.JANUARY.getMonthCalendarValue() == calInt) {
                month = DmMonths.JANUARY;
            } else if (DmMonths.FEBRUARY.getMonthCalendarValue() == calInt) {
                month = DmMonths.FEBRUARY;
            } else if (DmMonths.MARCH.getMonthCalendarValue() == calInt) {
                month = DmMonths.MARCH;
            } else if (DmMonths.APRIL.getMonthCalendarValue() == calInt) {
                month = DmMonths.APRIL;
            } else if (DmMonths.MAY.getMonthCalendarValue() == calInt) {
                month = DmMonths.MAY;
            } else if (DmMonths.JUNE.getMonthCalendarValue() == calInt) {
                month = DmMonths.JUNE;
            } else if (DmMonths.JULY.getMonthCalendarValue() == calInt) {
                month = DmMonths.JULY;
            } else if (DmMonths.AUGUST.getMonthCalendarValue() == calInt) {
                month = DmMonths.AUGUST;
            } else if (DmMonths.SEPTEMBER.getMonthCalendarValue() == calInt) {
                month = DmMonths.SEPTEMBER;
            } else if (DmMonths.OCTOBER.getMonthCalendarValue() == calInt) {
                month = DmMonths.OCTOBER;
            } else if (DmMonths.NOVEMBER.getMonthCalendarValue() == calInt) {
                month = DmMonths.NOVEMBER;
            } else if (DmMonths.DECEMBER.getMonthCalendarValue() == calInt) {
                month = DmMonths.DECEMBER;
            }
            return month;
        }

        private DmMonths(String monthName, int calInt) {
            this.monthName = monthName;
            this.monthCalInt = calInt;
        }
    }

    public enum DmRecurrenceUnit implements Serializable, IsSerializable {

        DAYS("Days", 1),
        WEEKS("Weeks", 2),
        MONTHS("Months", 3);

        private String unitName;
        private int unitInt;

        public String getUnitName() {
            return this.unitName;
        }

        public int getUnitValue() {
            return this.unitInt;
        }

        public static DmRecurrenceUnit getValueFrom(String unitName) {
            DmRecurrenceUnit rUnit = null;
            if (DmRecurrenceUnit.DAYS.getUnitName().equalsIgnoreCase(unitName)) {
                rUnit = DmRecurrenceUnit.DAYS;
            } else if (DmRecurrenceUnit.WEEKS.getUnitName().equalsIgnoreCase(unitName)) {
                rUnit = DmRecurrenceUnit.WEEKS;
            }
            if (DmRecurrenceUnit.MONTHS.getUnitName().equalsIgnoreCase(unitName)) {
                rUnit = DmRecurrenceUnit.MONTHS;
            }
            return rUnit;
        }

        public static DmRecurrenceUnit getValueFrom(int unitInt) {
            DmRecurrenceUnit rUnit = null;
            if (DmRecurrenceUnit.DAYS.getUnitValue() == unitInt) {
                rUnit = DmRecurrenceUnit.DAYS;
            } else if (DmRecurrenceUnit.WEEKS.getUnitValue() == unitInt) {
                rUnit = DmRecurrenceUnit.WEEKS;
            }
            if (DmRecurrenceUnit.MONTHS.getUnitValue() == unitInt) {
                rUnit = DmRecurrenceUnit.MONTHS;
            }
            return rUnit;
        }

        private DmRecurrenceUnit(String unitName, int unitInt) {
            this.unitName = unitName;
            this.unitInt = unitInt;
        }
    }

    public enum DmSchedularType implements Serializable, IsSerializable {

        ONCE("Once", 1),
        DAILY("Daily", 2),
        WEEKLY("Weekly", 3),
        MONTHLY("Monthly", 4);

        private String schType;
        private int schInt;

        public String getSchedularType() {
            return this.schType;
        }

        public int getSchedularTypeValue() {
            return this.schInt;
        }

        public static DmSchedularType getValueFrom(int schType) {
            DmSchedularType rType = null;
            if (DmSchedularType.ONCE.getSchedularTypeValue() == schType) {
                rType = DmSchedularType.ONCE;
            } else if (DmSchedularType.DAILY.getSchedularTypeValue() == schType) {
                rType = DmSchedularType.DAILY;
            } else if (DmSchedularType.WEEKLY.getSchedularTypeValue() == schType) {
                rType = DmSchedularType.WEEKLY;
            } else if (DmSchedularType.MONTHLY.getSchedularTypeValue() == schType) {
                rType = DmSchedularType.MONTHLY;
            }
            return rType;
        }

        public static DmSchedularType getValueFrom(String schType) {
            DmSchedularType rType = null;
            if (DmSchedularType.ONCE.getSchedularType().equalsIgnoreCase(schType)) {
                rType = DmSchedularType.ONCE;
            } else if (DmSchedularType.DAILY.getSchedularType().equalsIgnoreCase(schType)) {
                rType = DmSchedularType.DAILY;
            } else if (DmSchedularType.WEEKLY.getSchedularType().equalsIgnoreCase(schType)) {
                rType = DmSchedularType.WEEKLY;
            } else if (DmSchedularType.MONTHLY.getSchedularType().equalsIgnoreCase(schType)) {
                rType = DmSchedularType.MONTHLY;
            }
            return rType;
        }

        private DmSchedularType(String schType, int schInt) {
            this.schInt = schInt;
            this.schType = schType;
        }
    }

//    public enum DmScheduleExecutionType {
//
//        Automatic("Auto", 0),
//        Manual("Manual", 1);
//
//        private int exeType;
//        private String exeName;
//
//        public int getExecutionType() {
//            return this.exeType;
//        }
//
//        public String getExecutionName() {
//            return this.exeName;
//        }
//
//        public static DmScheduleExecutionType getValueFrom(int exeType) {
//            DmScheduleExecutionType type = null;
//            if (DmScheduleExecutionType.Automatic.getExecutionType() == exeType) {
//                type = DmScheduleExecutionType.Automatic;
//            } else if (DmScheduleExecutionType.Manual.getExecutionType() == exeType) {
//                type = DmScheduleExecutionType.Manual;
//            }
//            return type;
//        }
//
//        public static DmScheduleExecutionType getValueFrom(String exeName) {
//            DmScheduleExecutionType type = null;
//            if (DmScheduleExecutionType.Automatic.getExecutionName().equalsIgnoreCase(exeName)) {
//                type = DmScheduleExecutionType.Automatic;
//            } else if (DmScheduleExecutionType.Automatic.getExecutionName().equalsIgnoreCase(exeName)) {
//                type = DmScheduleExecutionType.Manual;
//            }
//            return type;
//        }
//
//        private DmScheduleExecutionType(String exeName, int executionType) {
//            this.exeName = exeName;
//            this.exeType = executionType;
//        }
//    }
    public enum DmWeekdays implements Serializable, IsSerializable {

        ALLDAY("All Day", 0),
        SUNDAY("Sunday", 1),
        MONDAY("Monday", 2),
        TUESDAY("Tuesday", 3),
        WEDNESDAY("Wednesday", 4),
        THURSDAY("Thursday", 5),
        FRIDAY("Friday", 6),
        SATURDAY("Saturday", 7);

        private final int weekDayInt;
        private final String weekDayName;

        public String getWeekDayName() {
            return this.weekDayName;
        }

        public int getWeekDay() {
            return this.weekDayInt;
        }

        public static DmWeekdays getValueFrom(String dayName) {
            DmWeekdays day = null;
            if (DmWeekdays.ALLDAY.getWeekDayName().equalsIgnoreCase(dayName)) {
                day = DmWeekdays.ALLDAY;
            } else if (DmWeekdays.SUNDAY.getWeekDayName().equalsIgnoreCase(dayName)) {
                day = DmWeekdays.SUNDAY;
            } else if (DmWeekdays.MONDAY.getWeekDayName().equalsIgnoreCase(dayName)) {
                day = DmWeekdays.MONDAY;
            } else if (DmWeekdays.TUESDAY.getWeekDayName().equalsIgnoreCase(dayName)) {
                day = DmWeekdays.TUESDAY;
            } else if (DmWeekdays.WEDNESDAY.getWeekDayName().equalsIgnoreCase(dayName)) {
                day = DmWeekdays.WEDNESDAY;
            } else if (DmWeekdays.THURSDAY.getWeekDayName().equalsIgnoreCase(dayName)) {
                day = DmWeekdays.THURSDAY;
            } else if (DmWeekdays.FRIDAY.getWeekDayName().equalsIgnoreCase(dayName)) {
                day = DmWeekdays.FRIDAY;
            } else if (DmWeekdays.SATURDAY.getWeekDayName().equalsIgnoreCase(dayName)) {
                day = DmWeekdays.SATURDAY;
            }
            return day;
        }

        public static DmWeekdays getValueFrom(int weekInt) {
            DmWeekdays day = null;
            if (DmWeekdays.ALLDAY.getWeekDay() == weekInt) {
                day = DmWeekdays.ALLDAY;
            } else if (DmWeekdays.SUNDAY.getWeekDay() == weekInt) {
                day = DmWeekdays.SUNDAY;
            } else if (DmWeekdays.MONDAY.getWeekDay() == weekInt) {
                day = DmWeekdays.MONDAY;
            } else if (DmWeekdays.TUESDAY.getWeekDay() == weekInt) {
                day = DmWeekdays.TUESDAY;
            } else if (DmWeekdays.WEDNESDAY.getWeekDay() == weekInt) {
                day = DmWeekdays.WEDNESDAY;
            } else if (DmWeekdays.THURSDAY.getWeekDay() == weekInt) {
                day = DmWeekdays.THURSDAY;
            } else if (DmWeekdays.FRIDAY.getWeekDay() == weekInt) {
                day = DmWeekdays.FRIDAY;
            } else if (DmWeekdays.SATURDAY.getWeekDay() == weekInt) {
                day = DmWeekdays.SATURDAY;
            }
            return day;
        }

        private DmWeekdays(String dayName, int dayInt) {
            this.weekDayName = dayName;
            this.weekDayInt = dayInt;
        }
    }

    public enum DmWeeks implements Serializable, IsSerializable {

        FIRST("First", 1),
        SECOND("Second", 2),
        THIRD("Third", 3),
        FOURTH("Fourth", 4),
        FIFTH("Fifth", 5),
        SIXTH("Sixth", 6),
        LAST("Last", -1),;

        private String weekName;
        private int weekCalInt;

        public String getWeekName() {
            return this.weekName;
        }

        public int getWeekCalendarValue() {
            return this.weekCalInt;
        }

        public static DmWeeks getValueFrom(int calInt) {
            DmWeeks rWeek = null;
            if (DmWeeks.FIRST.getWeekCalendarValue() == calInt) {
                rWeek = DmWeeks.FIRST;
            } else if (DmWeeks.SECOND.getWeekCalendarValue() == calInt) {
                rWeek = DmWeeks.SECOND;
            } else if (DmWeeks.THIRD.getWeekCalendarValue() == calInt) {
                rWeek = DmWeeks.THIRD;
            } else if (DmWeeks.FOURTH.getWeekCalendarValue() == calInt) {
                rWeek = DmWeeks.FOURTH;
            } else if (DmWeeks.FIFTH.getWeekCalendarValue() == calInt) {
                rWeek = DmWeeks.FIFTH;
            } else if (DmWeeks.SIXTH.getWeekCalendarValue() == calInt) {
                rWeek = DmWeeks.SIXTH;
            } else if (DmWeeks.LAST.getWeekCalendarValue() == calInt) {
                rWeek = DmWeeks.LAST;
            }
            return rWeek;
        }

        public static DmWeeks getValueFrom(String weekName) {
            DmWeeks rWeek = null;
            if (DmWeeks.FIRST.getWeekName().equalsIgnoreCase(weekName)) {
                rWeek = DmWeeks.FIRST;
            } else if (DmWeeks.SECOND.getWeekName().equalsIgnoreCase(weekName)) {
                rWeek = DmWeeks.SECOND;
            } else if (DmWeeks.THIRD.getWeekName().equalsIgnoreCase(weekName)) {
                rWeek = DmWeeks.THIRD;
            } else if (DmWeeks.FOURTH.getWeekName().equalsIgnoreCase(weekName)) {
                rWeek = DmWeeks.FOURTH;
            } else if (DmWeeks.FIFTH.getWeekName().equalsIgnoreCase(weekName)) {
                rWeek = DmWeeks.FIFTH;
            } else if (DmWeeks.SIXTH.getWeekName().equalsIgnoreCase(weekName)) {
                rWeek = DmWeeks.SIXTH;
            } else if (DmWeeks.LAST.getWeekName().equalsIgnoreCase(weekName)) {
                rWeek = DmWeeks.LAST;
            }
            return rWeek;
        }

        private DmWeeks(String weekName, int calInt) {
            this.weekName = weekName;
            this.weekCalInt = calInt;
        }
    }

    public enum DmWebJobTypes implements Serializable, IsSerializable {

        SMS("SMS", 0),
        EMAIL("E-Mail", 1),
        EVENT("Event", 2);

        private String wjName;
        private int wjInt;

        public String getWebJobName() {
            return this.wjName;
        }

        public int getWebJobType() {
            return this.wjInt;
        }

        public static DmWebJobTypes getValueFrom(String name) {
            DmWebJobTypes wjType = null;
            if (DmWebJobTypes.SMS.getWebJobName().equalsIgnoreCase(name)) {
                wjType = DmWebJobTypes.SMS;
            } else if (DmWebJobTypes.EMAIL.getWebJobName().equalsIgnoreCase(name)) {
                wjType = DmWebJobTypes.EMAIL;
            } else if (DmWebJobTypes.EVENT.getWebJobName().equalsIgnoreCase(name)) {
                wjType = DmWebJobTypes.EVENT;
            }
            return wjType;
        }

        public static DmWebJobTypes getValueFrom(int wjInt) {
            DmWebJobTypes wjType = null;
            if (DmWebJobTypes.SMS.getWebJobType() == wjInt) {
                wjType = DmWebJobTypes.SMS;
            } else if (DmWebJobTypes.EMAIL.getWebJobType() == wjInt) {
                wjType = DmWebJobTypes.EMAIL;
            } else if (DmWebJobTypes.EVENT.getWebJobType() == wjInt) {
                wjType = DmWebJobTypes.EVENT;
            }
            return wjType;
        }

        private DmWebJobTypes(String wjName, int wjInt) {
            this.wjName = wjName;
            this.wjInt = wjInt;
        }
    }

    public enum DmWebJobDataSources implements Serializable, IsSerializable {

        FUNCTION("SQL", 0),
        CSV("CSV", 1);

        private String sName;
        private int sInt;

        public String getSourceName() {
            return this.sName;
        }

        public int getSourceType() {
            return this.sInt;
        }

        public static DmWebJobDataSources getValueFrom(String sName) {
            DmWebJobDataSources source = null;
            if (DmWebJobDataSources.FUNCTION.getSourceName().equalsIgnoreCase(sName)) {
                source = DmWebJobDataSources.FUNCTION;
            } else if (DmWebJobDataSources.CSV.getSourceName().equalsIgnoreCase(sName)) {
                source = DmWebJobDataSources.CSV;
            }
            return source;
        }

        public static DmWebJobDataSources getValueFrom(int sType) {
            DmWebJobDataSources source = null;
            if (DmWebJobDataSources.FUNCTION.getSourceType() == sType) {
                source = DmWebJobDataSources.FUNCTION;
            } else if (DmWebJobDataSources.CSV.getSourceType() == sType) {
                source = DmWebJobDataSources.CSV;
            }
            return source;
        }

        private DmWebJobDataSources(String sourceName, int sourceType) {
            this.sInt = sourceType;
            this.sName = sourceName;
        }
    }

    public enum DmDates implements Serializable, IsSerializable {

        DATE_1("1", 1),
        DATE_2("2", 2),
        DATE_3("3", 3),
        DATE_4("4", 4),
        DATE_5("5", 5),
        DATE_6("6", 6),
        DATE_7("7", 7),
        DATE_8("8", 8),
        DATE_9("9", 9),
        DATE_10("10", 10),
        DATE_11("11", 11),
        DATE_12("12", 12),
        DATE_13("13", 13),
        DATE_14("14", 14),
        DATE_15("15", 15),
        DATE_16("16", 16),
        DATE_17("17", 17),
        DATE_18("18", 18),
        DATE_19("19", 19),
        DATE_20("20", 20),
        DATE_21("21", 21),
        DATE_22("22", 22),
        DATE_23("23", 23),
        DATE_24("24", 24),
        DATE_25("25", 25),
        DATE_26("26", 26),
        DATE_27("27", 27),
        DATE_28("28", 28),
        DATE_29("29", 29),
        DATE_30("30", 30),
        DATE_31("31", 31),
        DATE_LAST("Last", -1);

        private String disStr;
        private int dateVal;

        public String getDisplayName() {
            return this.disStr;
        }

        public int getValue() {
            return this.dateVal;
        }

        public static DmDates getValueFrom(String dName) {
            DmDates date = null;

            if (DmDates.DATE_1.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_1;
            } else if (DmDates.DATE_2.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_2;
            } else if (DmDates.DATE_3.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_3;
            } else if (DmDates.DATE_4.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_4;
            } else if (DmDates.DATE_5.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_5;
            } else if (DmDates.DATE_6.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_6;
            } else if (DmDates.DATE_7.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_7;
            } else if (DmDates.DATE_8.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_8;
            } else if (DmDates.DATE_9.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_9;
            } else if (DmDates.DATE_10.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_10;
            } else if (DmDates.DATE_11.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_11;
            } else if (DmDates.DATE_12.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_12;
            } else if (DmDates.DATE_13.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_13;
            } else if (DmDates.DATE_14.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_14;
            } else if (DmDates.DATE_15.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_15;
            } else if (DmDates.DATE_16.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_16;
            } else if (DmDates.DATE_17.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_17;
            } else if (DmDates.DATE_18.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_18;
            } else if (DmDates.DATE_19.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_19;
            } else if (DmDates.DATE_20.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_20;
            } else if (DmDates.DATE_21.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_21;
            } else if (DmDates.DATE_22.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_22;
            } else if (DmDates.DATE_23.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_23;
            } else if (DmDates.DATE_24.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_24;
            } else if (DmDates.DATE_25.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_25;
            } else if (DmDates.DATE_26.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_26;
            } else if (DmDates.DATE_27.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_27;
            } else if (DmDates.DATE_28.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_28;
            } else if (DmDates.DATE_29.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_29;
            } else if (DmDates.DATE_30.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_30;
            } else if (DmDates.DATE_31.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_31;
            } else if (DmDates.DATE_LAST.getDisplayName().equalsIgnoreCase(dName)) {
                date = DmDates.DATE_LAST;
            }

            return date;
        }

        public static DmDates getValueFrom(int dVal) {
            DmDates date = null;

            if (DmDates.DATE_1.getValue() == dVal) {
                date = DmDates.DATE_1;
            } else if (DmDates.DATE_2.getValue() == dVal) {
                date = DmDates.DATE_2;
            } else if (DmDates.DATE_3.getValue() == dVal) {
                date = DmDates.DATE_3;
            } else if (DmDates.DATE_4.getValue() == dVal) {
                date = DmDates.DATE_4;
            } else if (DmDates.DATE_5.getValue() == dVal) {
                date = DmDates.DATE_5;
            } else if (DmDates.DATE_6.getValue() == dVal) {
                date = DmDates.DATE_6;
            } else if (DmDates.DATE_7.getValue() == dVal) {
                date = DmDates.DATE_7;
            } else if (DmDates.DATE_8.getValue() == dVal) {
                date = DmDates.DATE_8;
            } else if (DmDates.DATE_9.getValue() == dVal) {
                date = DmDates.DATE_9;
            } else if (DmDates.DATE_10.getValue() == dVal) {
                date = DmDates.DATE_10;
            } else if (DmDates.DATE_11.getValue() == dVal) {
                date = DmDates.DATE_11;
            } else if (DmDates.DATE_12.getValue() == dVal) {
                date = DmDates.DATE_12;
            } else if (DmDates.DATE_13.getValue() == dVal) {
                date = DmDates.DATE_13;
            } else if (DmDates.DATE_14.getValue() == dVal) {
                date = DmDates.DATE_14;
            } else if (DmDates.DATE_15.getValue() == dVal) {
                date = DmDates.DATE_15;
            } else if (DmDates.DATE_16.getValue() == dVal) {
                date = DmDates.DATE_16;
            } else if (DmDates.DATE_17.getValue() == dVal) {
                date = DmDates.DATE_17;
            } else if (DmDates.DATE_18.getValue() == dVal) {
                date = DmDates.DATE_18;
            } else if (DmDates.DATE_19.getValue() == dVal) {
                date = DmDates.DATE_19;
            } else if (DmDates.DATE_20.getValue() == dVal) {
                date = DmDates.DATE_20;
            } else if (DmDates.DATE_21.getValue() == dVal) {
                date = DmDates.DATE_21;
            } else if (DmDates.DATE_22.getValue() == dVal) {
                date = DmDates.DATE_22;
            } else if (DmDates.DATE_23.getValue() == dVal) {
                date = DmDates.DATE_23;
            } else if (DmDates.DATE_24.getValue() == dVal) {
                date = DmDates.DATE_24;
            } else if (DmDates.DATE_25.getValue() == dVal) {
                date = DmDates.DATE_25;
            } else if (DmDates.DATE_26.getValue() == dVal) {
                date = DmDates.DATE_26;
            } else if (DmDates.DATE_27.getValue() == dVal) {
                date = DmDates.DATE_27;
            } else if (DmDates.DATE_28.getValue() == dVal) {
                date = DmDates.DATE_28;
            } else if (DmDates.DATE_29.getValue() == dVal) {
                date = DmDates.DATE_29;
            } else if (DmDates.DATE_30.getValue() == dVal) {
                date = DmDates.DATE_30;
            } else if (DmDates.DATE_31.getValue() == dVal) {
                date = DmDates.DATE_31;
            } else if (DmDates.DATE_LAST.getValue() == dVal) {
                date = DmDates.DATE_LAST;
            }

            return date;
        }

        private DmDates(String disStr, int dateVal) {
            this.disStr = disStr;
            this.dateVal = dateVal;
        }
    }

    public enum DmYesNo implements Serializable, IsSerializable {

        YES("Yes", 1, "Y"),
        NO("No", 0, "N");

        private String enumName;
        private String enumShortName;
        private int enumValue;

        @Override
        public String toString() {
            return this.enumName;
        }

        public int toInt() {
            return enumValue;
        }

        public String getEnumShortName() {
            return enumShortName;
        }

        public void setEnumShortName(String enumShortName) {
            this.enumShortName = enumShortName;
        }

        public static DmYesNo valueOf(int valueId) {
            DmYesNo required = null;
            if (DmYesNo.NO.toInt() == valueId) {
                required = NO;
            } else if (DmYesNo.YES.toInt() == valueId) {
                required = YES;
            }
            return required;
        }

        public static DmYesNo getViaShortName(String shortName) {
            DmYesNo returnValue = null;
            for (DmYesNo mr : DmYesNo.values()) {
                if (shortName.equalsIgnoreCase(DmBaseShared.xNull(mr.getEnumShortName()))) {
                    returnValue = mr;
                    break;
                }
            }
            return returnValue;
        }

        public static DmYesNo getViaName(String name) {
            DmYesNo returnValue = null;
            for (DmYesNo mr : DmYesNo.values()) {
                if (name.equalsIgnoreCase(DmBaseShared.xNull(mr.toString()))) {
                    returnValue = mr;
                    break;
                }
            }
            return returnValue;
        }

        private DmYesNo(String enumName, int enumValue, String enumShortName) {
            this.enumValue = enumValue;
            this.enumName = enumName.trim();
            this.enumShortName = enumShortName;
        }
    }

    public enum DmSMSParams implements Serializable, IsSerializable {

        URL("url"),
        ACCOUNTNAME("accName"),
        PASSWORD("password"),
        MSG("msg"),
        MOBILE("mobile"),
        USER("user");

        private String paramName;

        public String getParamName() {
            return this.paramName;
        }

        public static DmSMSParams getValueFrom(String pName) {
            DmSMSParams param = null;
            if (DmSMSParams.ACCOUNTNAME.getParamName().equalsIgnoreCase(pName)) {
                param = ACCOUNTNAME;
            } else if (DmSMSParams.MOBILE.getParamName().equalsIgnoreCase(pName)) {
                param = MOBILE;
            } else if (DmSMSParams.MSG.getParamName().equalsIgnoreCase(pName)) {
                param = MSG;
            } else if (DmSMSParams.PASSWORD.getParamName().equalsIgnoreCase(pName)) {
                param = PASSWORD;
            } else if (DmSMSParams.URL.getParamName().equalsIgnoreCase(pName)) {
                param = URL;
            } else if (DmSMSParams.USER.getParamName().equalsIgnoreCase(pName)) {
                param = USER;
            }
            return param;
        }

        private DmSMSParams(String pName) {
            this.paramName = pName.trim();
        }
    }

    public enum DmPluginQueryActions implements Serializable, IsSerializable {

        PACPortWeb("PACPortWeb"),;

        private String queryAction;

        @Override
        public String toString() {
            return this.queryAction;
        }

        private DmPluginQueryActions(String qAction) {
            this.queryAction = qAction.toLowerCase().trim();
        }
    }

    public enum DmSessionStatus implements Serializable, IsSerializable {
        PUBLIC("PUBLIC"), LOGGED_IN("LOGGED_IN"), LOGGED_OUT("LOGGED_OUT"), LOCK("LOCK");

        private String aValue;

        @Override
        public String toString() {
            return this.aValue;
        }

        private DmSessionStatus(String value) {
            this.aValue = value.trim();
        }
    }

    public enum DmPageSizes implements Serializable, IsSerializable {

        Four_A(new DmPageSize("4A", 1682, 2378)),
        Two_A(new DmPageSize("2A", 1189, 1682)),
        A0(new DmPageSize("A0", 841, 1189)),
        A1(new DmPageSize("A1", 594, 841)),
        A2(new DmPageSize("A2", 420, 594)),
        A3(new DmPageSize("A3", 297, 420)),
        A4(new DmPageSize("A4", 210, 297)),
        A5(new DmPageSize("A5", 148, 210)),
        A6(new DmPageSize("A6", 105, 148)),
        A7(new DmPageSize("A7", 74, 105)),
        A8(new DmPageSize("A8", 52, 74)),
        A9(new DmPageSize("A9", 37, 52)),
        A10(new DmPageSize("A10", 26, 37)),
        Four_B(new DmPageSize("4B", 2000, 2828)),
        Two_B(new DmPageSize("2B", 1414, 2000)),
        B0(new DmPageSize("B0", 1000, 1414)),
        B1(new DmPageSize("B1", 707, 1000)),
        B2(new DmPageSize("B2", 500, 707)),
        B3(new DmPageSize("B3", 353, 500)),
        B4(new DmPageSize("B4", 250, 353)),
        B5(new DmPageSize("B5", 176, 250)),
        B6(new DmPageSize("B6", 125, 176)),
        B7(new DmPageSize("B7", 88, 125)),
        B8(new DmPageSize("B8", 62, 88)),
        B9(new DmPageSize("B9", 44, 62)),
        B10(new DmPageSize("B10", 31, 44)),
        C0(new DmPageSize("C0", 917, 1297)),
        C1(new DmPageSize("C1", 648, 917)),
        C2(new DmPageSize("C2", 458, 648)),
        C3(new DmPageSize("C3", 324, 458)),
        C4(new DmPageSize("C4", 229, 324)),
        C5(new DmPageSize("C5", 162, 229)),
        C6(new DmPageSize("C6", 114, 162)),
        C7(new DmPageSize("C7", 81, 114)),
        C8(new DmPageSize("C8", 57, 81)),
        C9(new DmPageSize("C9", 40, 57)),
        C10(new DmPageSize("C10", 28, 40)),
        Legal(new DmPageSize("Legal", 216, 356)),
        Junior_Legal(new DmPageSize("Junior_Legal", 127, 203)),
        One_By_Three_A4(new DmPageSize("One_By_Three_A4", 99, 210)),
        Gutenberg_Bible(new DmPageSize("Gutenberg_Bible", 280, 398)),
        CD_Case_Insert(new DmPageSize("CD_Case_Insert", 120, 120)),
        A4_Square(new DmPageSize("A4_Square", 210, 210)),
        Print_Handbook(new DmPageSize("Print_Handbook", 119, 178)),
        A_Format_Paperback(new DmPageSize("A_Format_Paperback", 110, 178)),
        B_Format_Paperback(new DmPageSize("B_Format_Paperback", 130, 198)),
        Sheet_Billboard_48(new DmPageSize("Sheet_Billboard_48", 6096, 3048)),
        Custom(new DmPageSize("Custom", 0, 0));

        private DmPageSize pageSie;

        @Override
        public String toString() {
            return this.pageSie.getPageName().trim();
        }

        public int getWidthInMM() {
            return this.pageSie.getWidthInMM();
        }

        public int getHeightInMM() {
            return this.pageSie.getHeightInMM();
        }

        public DmPageSize getPageSize() {
            return this.pageSie;
        }

        private DmPageSizes(DmPageSize pageSize) {
            this.pageSie = pageSize;
        }

        public static DmPageSize getValueOf(String pageName) {
            DmPageSize required = null;
            if (DmPageSizes.A0.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A0.getPageSize();
            } else if (DmPageSizes.A1.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A1.getPageSize();
            } else if (DmPageSizes.A10.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A10.getPageSize();
            } else if (DmPageSizes.A2.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A2.getPageSize();
            } else if (DmPageSizes.A3.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A3.getPageSize();
            } else if (DmPageSizes.A4.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A4.getPageSize();
            } else if (DmPageSizes.A4_Square.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A4_Square.getPageSize();
            } else if (DmPageSizes.A5.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A5.getPageSize();
            } else if (DmPageSizes.A6.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A6.getPageSize();
            } else if (DmPageSizes.A7.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A7.getPageSize();
            } else if (DmPageSizes.A8.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A8.getPageSize();
            } else if (DmPageSizes.A9.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A9.getPageSize();
            } else if (DmPageSizes.A_Format_Paperback.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = A_Format_Paperback.getPageSize();
            } else if (DmPageSizes.B0.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B0.getPageSize();
            } else if (DmPageSizes.B1.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B1.getPageSize();
            } else if (DmPageSizes.B2.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B2.getPageSize();
            } else if (DmPageSizes.B3.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B3.getPageSize();
            } else if (DmPageSizes.B4.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B4.getPageSize();
            } else if (DmPageSizes.B5.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B5.getPageSize();
            } else if (DmPageSizes.B6.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B6.getPageSize();
            } else if (DmPageSizes.B7.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B7.getPageSize();
            } else if (DmPageSizes.B8.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B8.getPageSize();
            } else if (DmPageSizes.B9.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B9.getPageSize();
            } else if (DmPageSizes.B10.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B10.getPageSize();
            } else if (DmPageSizes.B_Format_Paperback.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = B_Format_Paperback.getPageSize();
            } else if (DmPageSizes.C0.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C0.getPageSize();
            } else if (DmPageSizes.C1.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C1.getPageSize();
            } else if (DmPageSizes.C2.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C2.getPageSize();
            } else if (DmPageSizes.C3.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C3.getPageSize();
            } else if (DmPageSizes.C4.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C4.getPageSize();
            } else if (DmPageSizes.C5.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C5.getPageSize();
            } else if (DmPageSizes.C6.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C6.getPageSize();
            } else if (DmPageSizes.C7.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C7.getPageSize();
            } else if (DmPageSizes.C8.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C8.getPageSize();
            } else if (DmPageSizes.C9.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C9.getPageSize();
            } else if (DmPageSizes.C10.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = C10.getPageSize();
            } else if (DmPageSizes.CD_Case_Insert.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = CD_Case_Insert.getPageSize();
            } else if (DmPageSizes.Custom.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Custom.getPageSize();
            } else if (DmPageSizes.Four_A.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Four_A.getPageSize();
            } else if (DmPageSizes.Four_B.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Four_B.getPageSize();
            } else if (DmPageSizes.Gutenberg_Bible.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Gutenberg_Bible.getPageSize();
            } else if (DmPageSizes.Junior_Legal.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Junior_Legal.getPageSize();
            } else if (DmPageSizes.Legal.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Legal.getPageSize();
            } else if (DmPageSizes.One_By_Three_A4.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = One_By_Three_A4.getPageSize();
            } else if (DmPageSizes.Print_Handbook.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Print_Handbook.getPageSize();
            } else if (DmPageSizes.Sheet_Billboard_48.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Sheet_Billboard_48.getPageSize();
            } else if (DmPageSizes.Sheet_Billboard_48.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Sheet_Billboard_48.getPageSize();
            } else if (DmPageSizes.Two_A.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Two_A.getPageSize();
            } else if (DmPageSizes.Two_B.getPageSize().getPageId().equalsIgnoreCase(pageName)) {
                required = Two_B.getPageSize();
            }
            return required;
        }
    }

    public enum DmEventSystemArgs implements Serializable, IsSerializable {
        SchedulerMaster_Model("schedulerModel"),
        Template_Model("templateModel"),
        CurrentDateTime("currentDate");

        private String argValue;

        @Override
        public String toString() {
            return this.argValue;
        }

        public static DmEventSystemArgs getSystemArgumentFrom(String aValue) {
            DmEventSystemArgs[] args = DmEventSystemArgs.values();
            DmEventSystemArgs arg = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i].toString().equalsIgnoreCase(aValue)) {
                    arg = args[i];
                    break;
                }
            }
            return arg;
        }

        private DmEventSystemArgs(String aValue) {
            this.argValue = aValue;
        }
    }

    public enum DmGSTType implements Serializable, IsSerializable {

        VOID("Void", 0),
        INPUT("Input", 1),
        OUTPUT("Output", 2);

        private String enumName;
        private int enumValue;

        @Override
        public String toString() {
            return this.enumName;
        }

        public int toInt() {
            return enumValue;
        }

        public static DmGSTType valueOf(int valueId) {
            DmGSTType required = null;
            if (DmGSTType.VOID.toInt() == valueId) {
                required = VOID;
            } else if (DmGSTType.INPUT.toInt() == valueId) {
                required = INPUT;
            } else if (DmGSTType.OUTPUT.toInt() == valueId) {
                required = OUTPUT;
            }
            return required;
        }

        private DmGSTType(String enumName, int enumValue) {
            this.enumValue = enumValue;
            this.enumName = enumName.trim();
        }
    }

    public enum StockEntryType implements Serializable, IsSerializable {
        OTHER("Other", 1),
        STOCK_TRANSFER_RECEIVE("Stock Transfer Receive", 2),
        PHYSICAL_STOCK_POSTING("Physical Stock Posting", 3),
        PHYSICAL_STOCK_CLEARANCE("Physical Stock Clearence", 4),
        ITEM_RECEIVE("Item Receive", 5);

        private final int enumValue;
        private final String enumName;

        public String getValue() {
            return this.enumName;
        }

        public int getId() {
            return this.enumValue;
        }

        public static StockEntryType getValueFrom(String value) {
            StockEntryType stockEntryType = null;
            if (OTHER.getValue().equalsIgnoreCase(value)) {
                stockEntryType = OTHER;
            } else if (STOCK_TRANSFER_RECEIVE.getValue().equalsIgnoreCase(value)) {
                stockEntryType = STOCK_TRANSFER_RECEIVE;
            } else if (PHYSICAL_STOCK_POSTING.getValue().equalsIgnoreCase(value)) {
                stockEntryType = PHYSICAL_STOCK_POSTING;
            } else if (PHYSICAL_STOCK_CLEARANCE.getValue().equalsIgnoreCase(value)) {
                stockEntryType = PHYSICAL_STOCK_CLEARANCE;
            } else if (ITEM_RECEIVE.getValue().equalsIgnoreCase(value)) {
                stockEntryType = ITEM_RECEIVE;
            }
            return stockEntryType;
        }

        public static StockEntryType getValueFrom(int id) {
            StockEntryType stockEntryType = null;
            if (OTHER.getId() == id) {
                stockEntryType = OTHER;
            } else if (STOCK_TRANSFER_RECEIVE.getId() == id) {
                stockEntryType = STOCK_TRANSFER_RECEIVE;
            } else if (PHYSICAL_STOCK_POSTING.getId() == id) {
                stockEntryType = PHYSICAL_STOCK_POSTING;
            } else if (PHYSICAL_STOCK_CLEARANCE.getId() == id) {
                stockEntryType = PHYSICAL_STOCK_CLEARANCE;
            } else if (ITEM_RECEIVE.getId() == id) {
                stockEntryType = ITEM_RECEIVE;
            }
            return stockEntryType;
        }

        private StockEntryType(String enumName, int enumValue) {
            this.enumName = enumName;
            this.enumValue = enumValue;
        }
    }

    public enum CardType implements Serializable, IsSerializable {
        CASH("Cash", 0), ADJUST("Adjust", 1), CREDIT_CARD("Credit Card", 2), BANK("Bank", 3),
        DIGITAL("Digital", 4), WALLET("Wallet", 5), FOREIGN_CURRENCY("Foreign Currency", 6),POS("POS", 7);

        private final String enumName;
        private final int enumValue;

        public String getEnumName() {
            return this.enumName;
        }

        public int getEnumValue() {
            return this.enumValue;
        }

        public static CardType getValueOf(String chartId) {
            CardType required = null;
            for (CardType cardType : CardType.values()) {
                if (!DmBaseShared.xNull(chartId).equalsIgnoreCase("") && DmBaseShared.xNull(cardType.getEnumName()).equalsIgnoreCase(chartId)) {
                    required = cardType;
                    break;
                }
            }
            return required;
        }

        public static CardType getValueOf(int enumValue) {
            CardType required = null;

            switch (DmBaseShared.iNull(enumValue).intValue()) {
                case 0:
                    required = CASH;
                    break;
                case 1:
                    required = ADJUST;
                    break;
                case 2:
                    required = CREDIT_CARD;
                    break;
                case 3:
                    required = BANK;
                    break;
                case 4:
                    required = DIGITAL;
                    break;
                case 5:
                    required = WALLET;
                    break;
                case 6:
                    required = FOREIGN_CURRENCY;
                    break;
               
                case 7:
                    required = POS;
                    break;
               
                
                default:
                    break;
            }

            return required;
        }

        private CardType(String enumName, int enumValue) {
            this.enumValue = enumValue;
            this.enumName = enumName.trim();
        }
    }

    public enum ServiceEvalType implements Serializable, IsSerializable {
        FREE_VALUE(1, "Free Value", "FV"),
        PERCENTAGE_OF_GOODS_TAXABLE_VALUE(2, "Percentage of Goods Taxable Value", "PGTV"),
        PER_UNIT(3, "Per Unit", "PU");

        private final int code;
        private final String name;
        private final String shortName;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getShortName() {
            return shortName;
        }

        ServiceEvalType(int code, String name, String shortName) {

            this.code = code;
            this.name = name;
            this.shortName = shortName;
        }

        public static ServiceEvalType getViaCode(int code) {
            ServiceEvalType returnValue = null;
            for (ServiceEvalType en : ServiceEvalType.values()) {
                if (code == en.getCode()) {
                    returnValue = en;
                    break;
                }
            }

            return returnValue;
        }

        public static ServiceEvalType getViaName(String name) {
            ServiceEvalType returnValue = null;
            for (ServiceEvalType en : ServiceEvalType.values()) {
                if (name.equalsIgnoreCase(en.getName())) {
                    returnValue = en;
                    break;
                }
            }

            return returnValue;
        }

        public static ServiceEvalType getViaShortName(String shortName) {
            ServiceEvalType returnValue = null;
            for (ServiceEvalType en : ServiceEvalType.values()) {
                if (shortName.equalsIgnoreCase(en.getShortName())) {
                    returnValue = en;
                    break;
                }
            }

            return returnValue;
        }

    }

    public enum GSTS_Head implements Serializable, IsSerializable {

        SGST("SGST", "SGST", 1, 1),
        UGST("UGST", "UGST", 1, 2),
        CGST("CGST", "CGST", 1, 3),
        IGST("IGST", "IGST", 1, 4),
        PURCHASE_ACCOUNT("Purchase A/c", "PURCH", 2, 5),
        PURCHASE_RETURN_ACCOUNT("Purchase Return A/c", "PURET", 2, 6),
        SALES_ACCOUNT("Sales A/c", "SALES", 2, 7),
        SALES_RETURN_ACCOUNT("Sales Return A/c", "SLRET", 2, 8);

        private final String name;
        private final String shortName;
        private final int isTaxHead;
        private final int printSrNo;

        public String getName() {
            return name;
        }

        public String getShortName() {
            return shortName;
        }

        public int getIsTaxHead() {
            return isTaxHead;
        }

        public int getPrintSrNo() {
            return printSrNo;
        }

        GSTS_Head(String name, String shortName, int isTaxHead, int printSrNo) {

            this.name = name;
            this.shortName = shortName;
            this.isTaxHead = isTaxHead;
            this.printSrNo = printSrNo;
        }

        public static GSTS_Head getViaShortName(String shortName) {
            GSTS_Head returnValue = null;
            for (GSTS_Head itsh : GSTS_Head.values()) {
                if (shortName.equalsIgnoreCase(itsh.getShortName())) {
                    returnValue = itsh;
                    break;
                }
            }

            return returnValue;
        }

    }

    public enum DmModifyReason implements Serializable, IsSerializable {
        SYSTEM_ACTIVITY("System Activity");

        private final String name;

        public String getName() {
            return name;
        }

        DmModifyReason(String name) {

            this.name = name;

        }

        public static DmModifyReason getViaName(String name) {
            DmModifyReason returnValue = null;
            for (DmModifyReason mr : DmModifyReason.values()) {
                if (name.equalsIgnoreCase(mr.getName())) {
                    returnValue = mr;
                    break;
                }
            }
            return returnValue;
        }
    }

    public enum DmTransactionMode implements Serializable, IsSerializable {

        NEW("New"),
        EDIT("Edit"),
        DELETE("Delete"),
        DISCARD("Discard"),
        PRINT("Print"),
        APPROVE("Approve"),
        UNAPPROVE("Unapprove"),
        POST("Post"),
        UNPOST("Unpost"),
        ASSIGN("Assign"),
        UNASSIGN("Unassign");

        private final String value;

        public String getValue() {
            return value;
        }

        DmTransactionMode(String value) {
            this.value = value;
        }

        public static DmTransactionMode getViaValue(String value) {
            for (DmTransactionMode g : DmTransactionMode.values()) {
                if (g.getValue().equalsIgnoreCase(DmBaseShared.xNull(value).trim())) {
                    return g;
                }
            }
            return null;
        }
    }

    public enum EGST_ACTION implements Serializable, IsSerializable {

        E_WAY_AUTHENTICATION(1, "e-Way Bill Authentication", "EWAY_AUTH"),
        E_WAY_GENRATE_BILL(2, "Genrate e-Way Bill", "EWAY_GENRATE_BILL"),
        E_WAY_CANCEL_BILL(3, "Cancel e-Way Bill", "EWAY_CANCEL_BILL"),
        E_WAY_PRINT_BILL(4, "Print e-Way Bill", "EWAY_PRINT_BILL"),
        E_WAY_PRINT_DETAILED_BILL(5, "Print Detailed e-Way Bill", "EWAY_PRINT_DETAIL_BILL"),
        E_WAY_GET_BILL(6, "Get e-Way Bill", "GET_EWAY_BILL"),
        E_WAY_GENERATE_VIA_IRN(7, "Genrate e-Way Bill via IRN", "EWAY_GENERATE_VIA_IRN"),
        E_INV_AUTHENTICATION(8, "e-Invocie Authentication", "EINV_AUTH"),
        E_INV_GENRATE_BILL(9, "Genrate e-Invoice", "EINV_GENRATE_BILL"),
        E_INV_CANCEL_BILL(10, "Cancel e-Invoice", "EINV_CANCEL_BILL"),
        E_INV_PRINT_BILL(11, "Print e-Invoice", "EINV_PRINT_BILL"),
        E_INV_PRINT_DETAILED_BILL(12, "Print detailed e-Invoice", "EINV_PRINT_DETAIL_BILL"),
        E_INV_GET_BILL(13, "Get e-Invoice", "GET_EINV_BILL"),
        GST_UPDATE_DOCUMENTS(14, "Update GST Documents", "GST_UPDATE_DOCUMENTS");

        private final int code;
        private final String name;
        private final String transMode;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getTransMode() {
            return transMode;
        }

        EGST_ACTION(int code, String name, String transMode) {

            this.code = code;
            this.name = name;
            this.transMode = transMode;

        }

        public static EGST_ACTION getViaCode(int code) {
            EGST_ACTION returnValue = null;
            for (EGST_ACTION en : EGST_ACTION.values()) {
                if (code == en.getCode()) {
                    returnValue = en;
                    break;
                }
            }

            return returnValue;
        }

        public static EGST_ACTION getViaName(String name) {
            EGST_ACTION returnValue = null;
            for (EGST_ACTION en : EGST_ACTION.values()) {
                if (name.equalsIgnoreCase(en.getName())) {
                    returnValue = en;
                    break;
                }
            }

            return returnValue;
        }

        public static EGST_ACTION getViaTransMode(String transMode) {
            EGST_ACTION returnValue = null;
            for (EGST_ACTION en : EGST_ACTION.values()) {
                if (transMode.equalsIgnoreCase(en.getTransMode())) {
                    returnValue = en;
                    break;
                }
            }
            return returnValue;
        }
    }

    public enum Egst_Status {

        Active("ACT", "Active"),
        Cancelled("CNL", "Cancelled"),
        Discarded("DIS", "Discarded");

        private String code;
        private String value;

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        private Egst_Status(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public static String getViaCode(String code) {
            String value = "";
            for (Egst_Status status : Egst_Status.values()) {
                if (DmBaseShared.xNull(code).trim().equalsIgnoreCase(status.getCode())) {
                    value = status.getValue();
                    break;
                }
            }
            return value;
        }
    }

    public enum Egst_TaxScheme implements Serializable, IsSerializable {

        GST("GST", 1);

        private String name;
        private int code;

        public String getName() {
            return name;
        }

        public int getCode() {
            return code;
        }

        public static String getName(int code) {
            String name = "";
            for (Egst_TaxScheme egst_TaxScheme : Egst_TaxScheme.values()) {
                if (egst_TaxScheme.getCode() == code) {
                    name = egst_TaxScheme.getName();
                    break;
                }
            }
            return name;
        }

        private Egst_TaxScheme(String name, int code) {
            this.code = code;
            this.name = name.trim();
        }
    }

    public enum TransportMode implements Serializable, IsSerializable {

        BY_ROAD(1, "By Road", "Road"), BY_RAIL(2, "By Rail", "Rail"), BY_AIR(3, "By Air", "Air"), BY_SHIP(4, "By Ship", "Ship");

        private final int id;
        private final String value;
        private final String shortName;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public String getShortName() {
            return shortName;
        }

        TransportMode(int id, String text, String shortNm) {
            this.id = id;
            this.value = text;
            this.shortName = shortNm;
        }

        public static TransportMode getViaKey(int id) {
            TransportMode algo = null;
            if (id == BY_AIR.getId()) {
                algo = BY_AIR;
            } else if (id == BY_ROAD.getId()) {
                algo = BY_ROAD;
            } else if (id == BY_RAIL.getId()) {
                algo = BY_RAIL;
            } else if (id == BY_SHIP.getId()) {
                algo = BY_SHIP;
            }
            return algo;
        }

        public static TransportMode getViaText(String text) {
            TransportMode algo = null;
            if (text.equalsIgnoreCase(BY_AIR.getValue())) {
                algo = BY_AIR;
            } else if (text.equalsIgnoreCase(BY_ROAD.getValue())) {
                algo = BY_ROAD;
            } else if (text.equalsIgnoreCase(BY_RAIL.getValue())) {
                algo = BY_RAIL;
            } else if (text.equalsIgnoreCase(BY_SHIP.getValue())) {
                algo = BY_SHIP;
            }
            return algo;
        }

        public static TransportMode getViaShortName(String shortNm) {
            TransportMode algo = null;
            if (shortNm.equalsIgnoreCase(BY_AIR.getShortName())) {
                algo = BY_AIR;
            } else if (shortNm.equalsIgnoreCase(BY_ROAD.getShortName())) {
                algo = BY_ROAD;
            } else if (shortNm.equalsIgnoreCase(BY_RAIL.getShortName())) {
                algo = BY_RAIL;
            } else if (shortNm.equalsIgnoreCase(BY_SHIP.getShortName())) {
                algo = BY_SHIP;
            }
            return algo;
        }
    }

    public enum Eway_DocType {

        TAX_INVOICE("INV", "Tax Invoice"),
        BILL_OF_SUPPLY("BIL", "Bill of Supply"),
        BILL_OF_ENTRY("BOE", "Bill of Entry"),
        DELIVERY_CHALLAN("CHL", "Delivery Challan"),
        OTHERS("OTH", "Others");

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        Eway_DocType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getManualCode(String name) {
            String code = "";
            for (Eway_DocType egd : Eway_DocType.values()) {
                if (egd.getName().equalsIgnoreCase(DmBaseShared.xNull(name).trim())) {
                    code = egd.getCode();
                    break;
                }
            }
            return code;
        }

        public static String getName(String manualCode) {
            String name = "";
            for (Eway_DocType egd : Eway_DocType.values()) {
                if (egd.code.equalsIgnoreCase(DmBaseShared.xNull(manualCode).trim())) {
                    name = egd.getName();
                    break;
                }
            }
            return name;
        }

    }

    public enum Eway_SupplyType {

        INWARD("I", "Inward"),
        OUTWARD("O", "Outward"),
        BOTH_INWARD_OUTWARD("B", "Both Inward-Outward");

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        Eway_SupplyType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getCode(String name) {
            String code = "";
            for (Eway_SupplyType egd : Eway_SupplyType.values()) {
                if (egd.getName().equalsIgnoreCase(name)) {
                    code = egd.getCode();
                    break;
                }
            }
            return code;
        }

        public static String getName(String code) {
            String name = "";
            for (Eway_SupplyType egd : Eway_SupplyType.values()) {
                if (egd.getCode().equalsIgnoreCase(code)) {
                    name = egd.getName();
                    break;
                }
            }
            return name;
        }
    }

    public enum Eway_SubSupplyType {

        SUPPLY(1, Eway_SupplyType.BOTH_INWARD_OUTWARD.getCode(), "Supply"),
        IMPORT(2, Eway_SupplyType.INWARD.getCode(), "Import"),
        EXPORT(3, Eway_SupplyType.OUTWARD.getCode(), "Export"),
        JOB_WORK(4, Eway_SupplyType.OUTWARD.getCode(), "Job Work"),
        FOR_OWN_USE(5, Eway_SupplyType.BOTH_INWARD_OUTWARD.getCode(), "For Own Use"),
        JOB_WORK_RETURN(6, Eway_SupplyType.INWARD.getCode(), "Job_Work_Return"),
        SALES_RETURN(7, Eway_SupplyType.INWARD.getCode(), "Sales_Return"),
        OTHERS(8, Eway_SupplyType.BOTH_INWARD_OUTWARD.getCode(), "Others"),
        SKD_CKD_LOTS(9, Eway_SupplyType.BOTH_INWARD_OUTWARD.getCode(), "SKD/CKD/Lots"),
        LINE_SALES(10, Eway_SupplyType.OUTWARD.getCode(), "Line_Sales"),
        RECIPIENT_NOT_KNOWN(11, Eway_SupplyType.OUTWARD.getCode(), "Recipient Not Known"),
        EXHIBITION_OR_FAIRS(12, Eway_SupplyType.BOTH_INWARD_OUTWARD.getCode(), "Exhibition or Fairs");

        private int code;
        private String supplyType;
        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getSupplyType() {
            return supplyType;
        }

        Eway_SubSupplyType(int code, String supplyType, String name) {
            this.code = code;
            this.supplyType = supplyType;
            this.name = name;
        }

        public static int getCode(String name) {
            int code = 0;
            for (Eway_SubSupplyType egd : Eway_SubSupplyType.values()) {
                if (egd.getName().equalsIgnoreCase(DmBaseShared.xNull(name).trim())) {
                    code = egd.getCode();
                    break;
                }
            }
            return code;
        }

        public static String getName(int code) {
            String name = "";
            for (Eway_SubSupplyType egd : Eway_SubSupplyType.values()) {
                if (egd.getCode() == DmBaseShared.iNull(code).intValue()) {
                    name = egd.getName();
                    break;
                }
            }
            return name;
        }

    }

    public enum Eway_TransactionType {

        Regular(1, "Regular"),
        Bill_To_Ship_To(2, "Bill To - Ship To"),
        Bill_From_Ship_From(3, "Bill From - Dispatch From"),
        Combination_OF_2_And_3(4, "Combination of 2 and 3");

        private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        private Eway_TransactionType(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static String getViaCode(int code) {
            String value = "";
            for (Eway_TransactionType transactionType : Eway_TransactionType.values()) {
                if (transactionType.getCode() == code) {
                    value = transactionType.getValue();
                    break;
                }
            }
            return value;
        }
    }

    public enum Einv_SupplyTypeCode {

        Business_to_Business("B2B", "Business to Business"),
        Business_to_Consumer("B2C", "Business to Consumer"),
        SEZ_with_payment("SEZWP", "SEZ with payment"),
        SEZ_without_payment("SEZWOP", "SEZ without payment"),
        Export_with_Payment("EXPWP", "Export with Payment"),
        Export_without_Payment("EXPWOP", "Export without Payment"),
        Deemed_Export("DEXP", "Deemed Export");

        private String code;
        private String value;

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        private Einv_SupplyTypeCode(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public static String getValue(String code) {
            String value = "";
            for (Einv_SupplyTypeCode transactionType : Einv_SupplyTypeCode.values()) {
                if (transactionType.getCode().equalsIgnoreCase(DmBaseShared.xNull(code))) {
                    value = transactionType.getValue();
                    break;
                }
            }
            return value;
        }

    }

    public enum Einv_DocTypeCode {

        INVOICE("INV", "Invoice"),
        CREDIT_NOTE("CRN", "Credit Note"),
        DEBIT_NOTE("DBN", "Debit note");

        private String code;
        private String value;

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        private Einv_DocTypeCode(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public static String getValue(String code) {
            String value = "";
            for (Einv_DocTypeCode docType : Einv_DocTypeCode.values()) {
                if (docType.getCode().equalsIgnoreCase(DmBaseShared.xNull(code))) {
                    value = docType.getValue();
                    break;
                }
            }
            return value;
        }
    }

    public enum EWAY_GenerationMode implements Serializable, IsSerializable {

        E_WAY_GENERATED_DIRECTLY(1, "e-Way genterated diectly"),
        E_WAY_GENERATED_WITH_E_INVOICE(2, "e-Way generated with e-Invoice"),
        E_WAY_GENERATED_VIA_IRN(3, "e-Way generated via IRN");

        private final int code;
        private final String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        EWAY_GenerationMode(int code, String name) {

            this.code = code;
            this.name = name;
        }

        public static EWAY_GenerationMode getViaCode(int code) {
            EWAY_GenerationMode returnValue = null;
            for (EWAY_GenerationMode en : EWAY_GenerationMode.values()) {
                if (code == en.getCode()) {
                    returnValue = en;
                    break;
                }
            }

            return returnValue;
        }

        public static EWAY_GenerationMode getViaName(String name) {
            EWAY_GenerationMode returnValue = null;
            for (EWAY_GenerationMode en : EWAY_GenerationMode.values()) {
                if (name.equalsIgnoreCase(en.getName())) {
                    returnValue = en;
                    break;
                }
            }

            return returnValue;
        }
    }

    public enum EwayCancelReason implements Serializable, IsSerializable {

        Duplicate(1, "Duplicate"), Order_Cancelled(2, "Order Cancelled"), Data_Entry_mistake(3, "Data Entry mistake"), Others(4, "Others");

        private final int code;
        private final String reason;

        EwayCancelReason(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        public int getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }

        public static String getViaCode(String code) {
            String reason = "";
            for (EwayCancelReason cancelReason : EwayCancelReason.values()) {
                if (DmBaseShared.iNull(code).intValue() == cancelReason.getCode()) {
                    reason = cancelReason.getReason();
                    break;
                }
            }
            return reason;
        }

    }

    public enum EinvCancelReason implements Serializable, IsSerializable {

        Duplicate(1, "Duplicate"), Data_Entry_mistake(2, "Data Entry mistake");

        private final int code;
        private final String reason;

        EinvCancelReason(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        public int getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }

        public static String getViaCode(String code) {
            String reason = "";
            for (EinvCancelReason cancelReason : EinvCancelReason.values()) {
                if (DmBaseShared.iNull(code).intValue() == cancelReason.getCode()) {
                    reason = cancelReason.getReason();
                    break;
                }
            }
            return reason;
        }

    }

    public enum IRP_Type {  //IRP: Invoice Registration Portal

        E_INVOICE(1, "E-Invoice"),
        E_WAY(2, "E-Way");

        private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        private IRP_Type(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static IRP_Type getViaCode(int code) {
            for (IRP_Type irpType : IRP_Type.values()) {
                if (irpType.getCode() == code) {
                    return irpType;
                }
            }
            return null;
        }
    }

    public enum EGST_URL_FORMAT_TAG implements Serializable, IsSerializable {

        BASE_URL("{url}"),
        FOLDER("{folder}"),
        VERSION("{version}"),
        API_NAME("{api}"),
        ACTION("{action}"),
        ASP_ID("{asp_id}"), //aspid
        ASP_PASSWORD("{asp_password}"), //asppassword
        GSTIN("{gstin}"),
        GSP_USER_NAME("{gsp_user_name}"), //username
        GSP_PASSWORD("{gsp_pwd}"), //ewbpwd
        AUTH_TOKEN("{auth_token}"), //authtoken
        QR_CODE_SIZE("{qr_code_size}"), //qrcodeSize
        PARSE_IRN_RESP("{parse_irn_resp}"), //parseIrnResp
        EWAY_NO("&ewbNo=");     //<Ag> TBR: New format field add and manage this

        private final String value;

        public String getValue() {
            return value;
        }

        EGST_URL_FORMAT_TAG(String value) {
            this.value = value;
        }
    }

    public enum PartyTransRecordType {

        PARTY(1, "Party"),
        DELIVERY_TO_PARTY(2, "Delivery To Party"),
        CONSIGNEE(3, "Consignee"),
        DISTRIBUTOR(4, "Distributor"),
        TRANSPORTER(5, "Transporter"),
        SITE(6, "Site"),
        DISPATCH_FROM(7, "Dispatch From"),
        TRANSACTION_SITE(8, "Transaction Site"),
        BROKER(9, "Broker");

        private final int code;
        private final String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        PartyTransRecordType(int code, String value) {
            this.code = code;
            this.name = value;
        }

        public static PartyTransRecordType getCode(String value) {
            for (PartyTransRecordType s : PartyTransRecordType.values()) {
                if (DmBaseShared.xNull(s.getName()).equalsIgnoreCase(DmBaseShared.xNull(value).trim())) {
                    return s;
                }
            }
            return null;
        }

        public static PartyTransRecordType getValue(int code) {
            for (PartyTransRecordType s : PartyTransRecordType.values()) {
                if (DmBaseShared.iNull(s.getCode()).intValue() == DmBaseShared.iNull(code).intValue()) {
                    return s;
                }
            }
            return null;
        }
    }

    public enum DmGstLocation_SystemDefined implements Serializable, IsSerializable {
        INTRA_STATE("IRAST"),
        INTER_STATE("IERST"),
        UNION_TERRITORY("UT"),
        INTER_NATIONAL("INNAT");

        private final String manualCode;

        public String getManualCode() {
            return manualCode;
        }

        private DmGstLocation_SystemDefined(String manualCode) {
            this.manualCode = manualCode;
        }

        public static DmGstLocation_SystemDefined getViaManualCode(String manualCode) {
            DmGstLocation_SystemDefined returnValue = null;
            for (DmGstLocation_SystemDefined en : DmGstLocation_SystemDefined.values()) {
                if (manualCode.equalsIgnoreCase(en.getManualCode())) {
                    returnValue = en;
                    break;
                }
            }

            return returnValue;
        }

    }

    public enum DmGstTransNature implements Serializable, IsSerializable {

        INPUT("Input", 1),
        OUTPUT("Output", 2);

        private String enumName;
        private int enumValue;

        @Override
        public String toString() {
            return this.enumName;
        }

        public int toInt() {
            return enumValue;
        }

        public static DmGstTransNature valueOf(int valueId) {
            DmGstTransNature required = null;
            if (DmGstTransNature.INPUT.toInt() == valueId) {
                required = INPUT;
            } else if (DmGstTransNature.OUTPUT.toInt() == valueId) {
                required = OUTPUT;
            }
            return required;
        }

        private DmGstTransNature(String enumName, int enumValue) {
            this.enumValue = enumValue;
            this.enumName = enumName.trim();
        }
    }

    public enum GstVehicleType implements Serializable, IsSerializable {
        Regular("R", "Regular"),
        VALUE_BASED("O", "ODC(Over Dimentional Cargo)");

        private final String code;
        private final String name;

        private GstVehicleType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static String getViaCode(String code) {
            String name = "";
            for (GstVehicleType en : GstVehicleType.values()) {
                if (en.getCode().equalsIgnoreCase(code)) {
                    name = en.getName();
                    break;
                }
            }
            return name;
        }

    }
}
