package aarogya.pharmacy.changePaymentSource.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ChangePaymentSource_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recId = new RecId();
    private String voucherDate;
    private String voucherTypeCode;
    private String regType;
    private String changePaymentSource;
    private PatientDetail_Mdl patientDetail = new PatientDetail_Mdl();

    public ChangePaymentSource_Mdl() {
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public String getChangePaymentSource() {
        return changePaymentSource;
    }

    public void setChangePaymentSource(String changePaymentSource) {
        this.changePaymentSource = changePaymentSource;
    }

    public PatientDetail_Mdl getPatientDetail() {
        return patientDetail;
    }

    public void setPatientDetail(PatientDetail_Mdl patientDetail) {
        this.patientDetail = patientDetail;
    }

    public enum SaleBillSearchType implements Serializable, IsSerializable {

        SALE_BILL(1, "Sale Bill"),
        SALE_BILL_RETURN(2, "Sale Bill Return");

        private final int id;
        private final String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        private SaleBillSearchType(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public static SaleBillSearchType get(int id) {
            SaleBillSearchType required = null;

            switch (DmBaseShared.iNull(id).intValue()) {
                case 1:
                    required = SALE_BILL;
                    break;
                case 2:
                    required = SALE_BILL_RETURN;
                    break;
                default:
                    break;
            }

            return required;
        }

        public static SaleBillSearchType get(String value) {
            SaleBillSearchType required = null;

            switch (DmBaseShared.xNull(value).toUpperCase()) {
                case "SALE BILL":
                    required = SALE_BILL;
                    break;
                case "SALE BILL RETURN":
                    required = SALE_BILL_RETURN;
                    break;
                default:
                    break;
            }

            return required;
        }

    }

}
