package aarogya.login.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.PCSData;
import java.util.List;

public interface Login_SrvAsync {

    public void initialize(AsyncCallback<DmResult<Initialze_Mdl>> result);

    public void validateUser(Login_Model.Body bTemp, AsyncCallback<DmResult<PCSData>> result);

    public void validateDBPassword(String password, AsyncCallback<DmResult<Boolean>> result);

    public void lockUser(AsyncCallback<DmResult<String>> result);
    
    public void forgetPassword(ForgetPassword_Mdl forgetPasswordMdl, AsyncCallback<DmResult<String>> result);

    public void getDefaultValues(AsyncCallback<DmResult<ForgetPassword_Mdl>> result);

    public void getTxtHPltCode(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHFinancialYear(String pltCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void setLocalIPAddress(List<String> ipAddresses, AsyncCallback<DmResult<String>> result);
}
