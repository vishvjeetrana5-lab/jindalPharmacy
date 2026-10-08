package aarogya.selfToolkit.cardPaymentPopup.shared;

import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface CardPaymentPopup_SrvAsync {

    public void getTxtHCardType(String[] excludeCardType, DmHelper helper, boolean blnToEndOfFile, String receiptType, AsyncCallback<DmResultPageing<List<GrdCardPayment_Mdl>>> result);

    public void getTxtHAccounts(String receiptType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getWalletInfo(String cardNo, String physicalCode, String uhid, AsyncCallback<DmResult<RegisteredWallet_Mdl>> result);

    public void getMakePayment(PaymentProcess_Mdl mrTemp, AsyncCallback<DmResult<PaymentProcess_Mdl>> result);

    public void getCancelPayment(PaymentProcess_Mdl mrTemp, AsyncCallback<DmResult<PaymentProcess_Mdl>> result);

    public void getCheckStatus(PaymentProcess_Mdl mrTemp, AsyncCallback<DmResult<PaymentProcess_Mdl>> result);

    public void getPOSParams(String cardTypeCode, AsyncCallback<DmResult<RazorPayParams_Mdl>> result);

    public void getTxtHPaymentMode(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void posSettingsJSON(String posSettingsJSON, AsyncCallback<RazorPayParams_Mdl> result);

    public void printRecords(GrdCardPayment_Mdl mrTemp, AsyncCallback<DmResult<String>> result);
}
