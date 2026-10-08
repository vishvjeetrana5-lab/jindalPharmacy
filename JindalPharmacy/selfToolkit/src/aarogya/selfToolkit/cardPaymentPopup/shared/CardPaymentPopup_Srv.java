package aarogya.selfToolkit.cardPaymentPopup.shared;

import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface CardPaymentPopup_Srv extends RemoteService {

    public DmResultPageing<List<GrdCardPayment_Mdl>> getTxtHCardType(String[] excludeCardType, DmHelper helper, boolean blnToEndOfFile,String receiptType);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccounts(String receiptType, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<RegisteredWallet_Mdl> getWalletInfo(String cardNo, String physicalCode, String uhid);

    public DmResult<PaymentProcess_Mdl> getMakePayment(PaymentProcess_Mdl mrTemp);

    public DmResult<PaymentProcess_Mdl> getCancelPayment(PaymentProcess_Mdl mrTemp);

    public DmResult<PaymentProcess_Mdl> getCheckStatus(PaymentProcess_Mdl mrTemp);

    public DmResult<RazorPayParams_Mdl> getPOSParams(String cardTypeCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentMode(DmHelper helper, boolean blnToEndOfFile);
    
    public RazorPayParams_Mdl posSettingsJSON(String strKey);
    
    public DmResult<String> printRecords(GrdCardPayment_Mdl mrTemp);

}
